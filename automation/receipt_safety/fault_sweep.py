"""Independent arithmetic expectations at every logical crash point.

The sealed in-memory journal assumption is deliberately the same as oracle.py.
This adds coverage of operation/boundary combinations, not durability evidence.
"""
from oracle import Oracle, digest


def sweep():
    # Initial state, frozen input, expected final FP/spins/stage/vendor.
    operations = [
        ('draw', {}, {'outcome': {'fixture_target': 'frozen-A'}}, (1000, 9, 'ACTIVE_OBJECTIVE', 'BANNED')),
        ('taint', {}, {'outcome': {'fixture_target': 'penance-A'}}, (1000, 9, 'PUNISHMENT_PENDING', 'BANNED')),
        ('sacrifice', {}, {'outcome': {'fixture_target': 'item-A', 'quantity': 2}}, (1000, 9, 'SACRIFICE_RECEIPT_PENDING', 'BANNED')),
        ('pardon', {}, {'quote': 500}, (500, 10, 'NONE', 'LOCKED')),
        ('reject', {'obligation': 'ACTIVE_OBJECTIVE', 'spins': 9, 'fp': 10}, {'quote': 50}, (-40, 9, 'PUNISHMENT_PENDING', 'BANNED')),
        ('complete', {'obligation': 'ACTIVE_OBJECTIVE', 'spins': 9}, {'quote': 75, 'evidence': 'fixture-completion'}, (1075, 9, 'NONE', 'BANNED')),
        ('donation', {'obligation': 'SACRIFICE_RECEIPT_PENDING', 'spins': 9}, {'quote': 5, 'verified': True, 'evidence': 'fixture-donation'}, (1000, 14, 'NONE', 'BANNED')),
        ('bounty_claim', {}, {'quote': 75, 'evidence': 'fixture-earned-item'}, (1075, 10, 'NONE', 'BANNED')),
    ]
    rows = []
    for op, initial, payload, expected in operations:
        for fault in ('before_commit', 'after_commit', 'after_snapshot', 'partial_record'):
            o = Oracle(initial)
            c = dict(id='fixture-command', op=op, revision=0, account='fixture-account-A',
                     run='fixture-run-A', generation=1, **payload)
            untouched = digest(o.state)
            assert o.command(c, fault) == 'WRITE_UNCONFIRMED'
            assert o.acks == []
            assert o.command(dict(c, id='fixture-second')) == 'PENDING_RECONCILIATION'
            raw_before = digest(o.journal)
            status = o.recover()
            if fault == 'partial_record':
                assert status == 'READ_ONLY' and o.read_only
                assert digest(o.journal) == raw_before, 'Unreadable payload was modified/deleted'
                assert digest(o.state) == untouched
                assert o.command(c) == 'READ_ONLY'
            else:
                assert status == 'RESTORED'
                if fault == 'before_commit':
                    assert digest(o.state) == untouched and len(o.journal) == 0
                    assert o.command(c) == 'COMMITTED'
                    assert o.acks == [c['id']]
                else:
                    assert o.command(c) == 'REPLAYED' and o.acks == []
                actual = tuple(o.state[k] for k in ('fp', 'spins', 'obligation', 'vendor'))
                assert actual == expected, (op, fault, actual, expected)
                assert len(o.journal) == 1
                assert o.state['receipts'] == [c['id']]
            rows.append(dict(operation=op, fault=fault, result='PASS', projection_sha256=digest(o.projection())))
    return rows
