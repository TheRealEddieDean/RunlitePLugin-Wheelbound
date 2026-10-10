"""Run from repository root: python3 automation/receipt_safety/checks.py.

Checks an offline reference oracle, not Java implementation or a real save file.
"""
import copy
import hashlib
import json
import platform
import sys
from pathlib import Path
from oracle import execute, digest
from fault_sweep import sweep

ROOT = Path(__file__).resolve().parents[2]
HERE = Path(__file__).resolve().parent
OPS = {'draw', 'taint', 'sacrifice', 'reject', 'complete', 'punishment_complete',
       'donation', 'pardon', 'purchase', 'bounty_claim', 'pause', 'resume', 'grand',
       'q10_settlement', 'mixed_ca_settlement', 'audit_reward_order', 'q6_suspend'}
COMMAND_KEYS = {'id', 'op', 'revision', 'account', 'run', 'generation', 'outcome',
                'quote', 'evidence', 'verified'}


def validate(packet):
    assert packet['version'] == 1
    assert packet['runtime_enabled'] is False and packet['live_trace_count'] == 0
    assert packet['economy_values'] == 'PLACEHOLDER_TEST_INPUTS'
    ids = [c['id'] for c in packet['cases']]
    assert len(ids) == len(set(ids)) == 44
    for c in packet['cases']:
        assert c['purpose'] and c['expect'] and c['steps']
        assert c['initial'].get('revision', 0) == 0
        assert (ROOT / 'docs/wheelbound' / c['source'].split('#')[0]).is_file()
        for step in c['steps']:
            assert step['kind'] in {'command', 'recover', 'damage'}
            if step['kind'] != 'command':
                continue
            command = step['command']
            assert set(command) <= COMMAND_KEYS
            assert command['op'] in OPS
            assert command['account'].startswith('fixture-account-')
            assert command['run'].startswith('fixture-run-')
            for key in ('revision', 'generation'):
                assert type(command[key]) is int and command[key] >= 0
            if 'quote' in command:
                assert type(command['quote']) is int and command['quote'] > 0
            assert step.get('fault') in {None, 'before_commit', 'after_commit', 'after_snapshot', 'partial_record'}


def main():
    packet = json.loads((HERE / 'fixtures_v1.json').read_text())
    validate(packet)
    manifest = json.loads((HERE / 'SOURCE_MANIFEST.json').read_text())
    for item in manifest['files']:
        assert hashlib.sha256((ROOT / item['path']).read_bytes()).hexdigest() == item['sha256'], item['path']
    outputs = []
    for case in packet['cases']:
        mismatches, state = execute(case)
        assert not mismatches, (case['id'], mismatches)
        assert type(state['state']['fp']) is int
        assert type(state['state']['spins']) is int and state['state']['spins'] >= 0
        # Repeat verifies deterministic interpreter execution only; not RNG quality.
        assert execute(case)[1] == state
        outputs.append(dict(id=case['id'], result='PASS', step_count=len(case['steps']),
                            projection_sha256=digest(state)))
    mutations = []
    for mutant in ('ignore_identity', 'forget_command', 'ignore_generation', 'ignore_revision',
                   'credit_paused', 'forget_evidence', 'ack_before_commit',
                   'allow_overdraft', 'ignore_obligation', 'change_frozen_outcome'):
        failed = [c['id'] for c in packet['cases'] if execute(c, mutant)[0]]
        assert failed, ('Surviving mutant', mutant)
        mutations.append(dict(mutant=mutant, detected_by=failed, result='DETECTED'))
    invalid = []
    for name in ('runtime_enabled', 'negative_quote', 'float_quote', 'credential_field', 'empty_expect', 'unknown_op'):
        bad = copy.deepcopy(packet)
        if name == 'runtime_enabled': bad['runtime_enabled'] = True
        elif name == 'negative_quote': bad['cases'][10]['steps'][0]['command']['quote'] = -10
        elif name == 'float_quote': bad['cases'][10]['steps'][0]['command']['quote'] = 10.5
        elif name == 'credential_field': bad['cases'][0]['steps'][0]['command']['launcher_token'] = 'FAKE_TEST_SENTINEL'
        elif name == 'empty_expect': bad['cases'][0]['expect'] = {}
        elif name == 'unknown_op': bad['cases'][0]['steps'][0]['command']['op'] = 'game_action'
        try:
            validate(bad)
        except AssertionError:
            invalid.append(dict(input=name, result='REJECTED'))
        else:
            raise AssertionError('Invalid fixture accepted: ' + name)
    result = dict(status='PASS', scope='IN_MEMORY_OFFLINE_ACCEPTANCE_ORACLE',
                  python=platform.python_version(), fixture_count=len(outputs),
                  mutation_count=len(mutations), malformed_input_count=len(invalid),
                  runtime_enabled=False, live_trace_count=0, production_test_count=0,
                  persistence_io_test_count=0, added_balance_trajectories=0,
                  source_manifest_sha256=hashlib.sha256((HERE / 'SOURCE_MANIFEST.json').read_bytes()).hexdigest(),
                  cases=outputs, fault_sweep_count=32, fault_sweep=sweep(),
                  mutations=mutations, malformed_inputs=invalid)
    encoded = json.dumps(result, indent=2) + '\n'
    if '--verify' in sys.argv:
        assert (HERE / 'RESULTS.json').read_text() == encoded, 'Saved result mismatch'
    else:
        (HERE / 'RESULTS.json').write_text(encoded)
    print('PASS: 44 fixtures + 32 operation/crash combinations; 10 detected mutations; 6 rejected malformed inputs; zero live/production/disk-I/O tests')


if __name__ == '__main__':
    main()
