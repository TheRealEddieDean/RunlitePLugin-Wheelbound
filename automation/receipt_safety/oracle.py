"""Offline acceptance oracle. No game API, production persistence, or RNG engine.

Durable records are represented by an in-memory list. A sealed record is an
assumption of the existing state contract, not proof of filesystem atomicity.
Numbers and outcomes in fixtures are test inputs, never economy parameters.
"""
import copy
import hashlib
import json


def digest(value):
    return hashlib.sha256(json.dumps(value, sort_keys=True, separators=(',', ':')).encode()).hexdigest()


class Oracle:
    def __init__(self, initial=None, mutant=None):
        self.state = dict(revision=0, fp=1000, spins=10, run='fixture-run-A',
                          account='fixture-account-A', generation=1, lifecycle='ACTIVE',
                          obligation='NONE', outcome=None, vendor='BANNED',
                          receipts=[], evidence=[], bounty_evidence=['fixture-earned-item'],
                          grand=None, reject_bounty_disposition_pending=False)
        self.state.update(copy.deepcopy(initial or {}))
        self.snapshot = copy.deepcopy(self.state)
        self.snapshot_seal = digest(self.snapshot)
        self.checkpoint = 0
        self.journal = []
        self.pending = False
        self.read_only = False
        self.acks = []
        self.results = []
        self.mutant = mutant

    def respond(self, status):
        self.results.append(status)
        return status

    def command(self, c, fault=None):
        if self.read_only:
            return self.respond('READ_ONLY')
        if self.pending:
            return self.respond('PENDING_RECONCILIATION')
        if self.mutant != 'ignore_identity' and (c['account'] != self.state['account'] or c['run'] != self.state['run']):
            return self.respond('WRONG_IDENTITY')
        known = next((r for r in self.journal if r.get('command_id') == c['id']), None)
        if known and self.mutant != 'forget_command':
            # Changed payload reuse is a documented contract gap; fail closed in
            # this oracle without selecting a production exception policy.
            if known['command_digest'] != digest(c):
                return self.respond('UNSPECIFIED_COMMAND_ID_COLLISION')
            return self.respond('REPLAYED')
        if self.mutant != 'ignore_generation' and c['generation'] != self.state['generation']:
            return self.respond('STALE_GENERATION')
        if self.mutant != 'ignore_revision' and c['revision'] != self.state['revision']:
            return self.respond('STALE_REVISION')
        if c['op'] in ('q10_settlement', 'mixed_ca_settlement', 'audit_reward_order', 'q6_suspend'):
            return self.respond('UNRESOLVED_RULE')
        if c['op'] not in ('pause', 'resume') and self.state['lifecycle'] != 'ACTIVE' and self.mutant != 'credit_paused':
            if c['op'] == 'bounty_claim' and self.state['lifecycle'] == 'PAUSED':
                return self.respond('UNSPECIFIED_PAUSED_CLAIM')
            return self.respond('INACTIVE')
        if c.get('evidence') and c['evidence'] in self.state['evidence'] and self.mutant != 'forget_evidence':
            return self.respond('DUPLICATE_EVIDENCE')
        s = copy.deepcopy(self.state)
        op = c['op']
        fp, spins = 0, 0
        if op in ('draw', 'taint', 'sacrifice'):
            if s['obligation'] != 'NONE':
                return self.respond('OBLIGATION_PENDING')
            if s['spins'] < 1:
                return self.respond('INSUFFICIENT_SPINS')
            spins = -1
            s['outcome'] = copy.deepcopy(c['outcome'])
            s['obligation'] = {'draw': 'ACTIVE_OBJECTIVE', 'taint': 'PUNISHMENT_PENDING',
                               'sacrifice': 'SACRIFICE_RECEIPT_PENDING'}[op]
        elif op == 'reject':
            if s['obligation'] != 'ACTIVE_OBJECTIVE':
                return self.respond('WRONG_STAGE')
            fp = -c['quote']
            s['obligation'] = 'PUNISHMENT_PENDING'
            s['reject_bounty_disposition_pending'] = True
            # Allocation already paid at draw; no completion reward or new spin.
        elif op == 'complete':
            if s['obligation'] != 'ACTIVE_OBJECTIVE':
                return self.respond('WRONG_STAGE')
            fp = c['quote']
            s['obligation'] = 'NONE'
        elif op == 'punishment_complete':
            if s['obligation'] != 'PUNISHMENT_PENDING':
                return self.respond('WRONG_STAGE')
            s['obligation'] = 'NONE'
        elif op == 'donation':
            if s['obligation'] != 'SACRIFICE_RECEIPT_PENDING':
                return self.respond('WRONG_STAGE')
            if not c.get('verified'):
                return self.respond('AMBIGUOUS_EVIDENCE')
            spins = c['quote']
            s['obligation'] = 'NONE'
        elif op == 'pardon':
            if s['vendor'] != 'BANNED':
                return self.respond('WRONG_VENDOR_STATE')
            fp = -c['quote']
            s['vendor'] = 'LOCKED'
        elif op == 'purchase':
            fp = -c['quote']
        elif op == 'bounty_claim':
            if c['evidence'] not in s['bounty_evidence']:
                return self.respond('UNQUALIFIED_EVIDENCE')
            if s['reject_bounty_disposition_pending']:
                return self.respond('UNRESOLVED_RULE')  # Q10; preserve receipt.
            fp = c['quote']
        elif op == 'pause':
            s['lifecycle'] = 'PAUSED'
        elif op == 'resume':
            s['lifecycle'] = 'ACTIVE'
        elif op == 'grand':
            if s['obligation'] != 'NONE' and self.mutant != 'ignore_obligation':
                return self.respond('OBLIGATION_PENDING')
            s['grand'] = copy.deepcopy(c['outcome'])
        else:
            raise ValueError('Unsupported fixture operation: ' + op)
        if op in ('pardon', 'purchase') and s['fp'] + fp < 0 and self.mutant != 'allow_overdraft':
            return self.respond('INSUFFICIENT_FP')
        s['fp'] += fp
        s['spins'] += spins
        s['revision'] += 1
        s['receipts'].append(c['id'])
        if c.get('evidence'):
            s['evidence'].append(c['evidence'])
        previous = self.journal[-1]['seal'] if self.journal else None
        r = dict(command_id=c['id'], command_digest=digest(c), before=self.state['revision'],
                 after=s['revision'], fp_delta=fp, spin_delta=spins, state=s, previous=previous)
        r['seal'] = digest(r)
        if self.mutant == 'ack_before_commit':
            self.acks.append(c['id'])
        if fault == 'before_commit':
            self.pending = True
            return self.respond('WRITE_UNCONFIRMED')
        if fault == 'partial_record':
            self.journal.append({'raw_unparsed': copy.deepcopy(r)})
            self.pending = True
            return self.respond('WRITE_UNCONFIRMED')
        self.journal.append(copy.deepcopy(r))
        if fault == 'after_commit':
            self.pending = True
            return self.respond('WRITE_UNCONFIRMED')
        self.state = s
        self.snapshot = copy.deepcopy(s)
        self.snapshot_seal = digest(self.snapshot)
        self.checkpoint = len(self.journal)
        if fault == 'after_snapshot':
            self.pending = True
            return self.respond('WRITE_UNCONFIRMED')
        if self.mutant != 'ack_before_commit':
            self.acks.append(c['id'])
        return self.respond('COMMITTED')

    def recover(self):
        # This is logical replay verification, NOT a journal truncation strategy.
        previous = None
        rev = 0
        for r in self.journal:
            body = {k: v for k, v in r.items() if k != 'seal'}
            if ('seal' not in r or digest(body) != r['seal'] or r['previous'] != previous
                    or r['before'] != rev or r['after'] != rev + 1):
                self.read_only = True
                return self.respond('READ_ONLY')
            previous, rev = r['seal'], r['after']
        if digest(self.snapshot) != self.snapshot_seal:
            self.read_only = True
            return self.respond('READ_ONLY')
        state = copy.deepcopy(self.snapshot)
        for r in self.journal[self.checkpoint:]:
            if r['state']['account'] != state['account'] or r['state']['run'] != state['run']:
                self.read_only = True
                return self.respond('READ_ONLY')
            state = copy.deepcopy(r['state'])
        if self.mutant == 'change_frozen_outcome' and state['outcome']:
            state['outcome'] = {'fixture_target': 'MUTATED_ON_RESTORE'}
        self.state = state
        self.pending = False
        return self.respond('RESTORED')

    def step(self, step):
        kind = step['kind']
        if kind == 'command':
            return self.command(step['command'], step.get('fault'))
        if kind == 'recover':
            return self.recover()
        if kind == 'damage':
            if step['target'] == 'snapshot':
                self.snapshot['fp'] += 1
            elif step['target'] == 'receipt':
                self.journal[step['index']]['state']['fp'] += 1
            elif step['target'] == 'sequence':
                self.journal.append(copy.deepcopy(self.journal[0]))
            else:
                raise ValueError('Unknown damage target')
            return None
        raise ValueError('Unknown step kind')

    def projection(self):
        return dict(state=self.state, results=self.results, acknowledged=self.acks,
                    journal_count=len(self.journal), pending=self.pending, read_only=self.read_only)


def execute(case, mutant=None):
    o = Oracle(case.get('initial'), mutant)
    for step in case['steps']:
        o.step(step)
    observed = o.projection()
    mismatches = []
    for path, expected in case['expect'].items():
        value = observed
        for key in path.split('.'):
            value = value[key]
        if value != expected:
            mismatches.append(dict(path=path, expected=expected, observed=value))
    return mismatches, observed
