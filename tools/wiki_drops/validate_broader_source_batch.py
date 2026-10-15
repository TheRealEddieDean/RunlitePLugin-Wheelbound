"""Validate three bounded source packets; no network or roster mutation."""
import argparse
from fractions import Fraction
import hashlib
import json
from pathlib import Path
from review_context_evidence import Capture, constants, PIN
from challenge_tier_rules_v2 import tier_for_probability

p=argparse.ArgumentParser();p.add_argument('--source-dir',required=True);p.add_argument('--primary-dir',required=True);a=p.parse_args()
R=Path(__file__).resolve().parents[2];D=R/'docs/wheelbound'
packet=json.loads((D/'research/BROADER_SOURCE_BATCH_A96.json').read_text())
assert packet['runtime_enabled'] is False and packet['core_changed'] is False
assert packet['constants_pin']==PIN and len(packet['source_candidates'])==3
assert hashlib.sha256((D/'catalogs/challenge_core_v2_draft.json').read_bytes()).hexdigest()=='9d567d4cc616168bdd76571cb7e7994060a2ca74a25a6ec2fba5e96de1c0c4e7'
c=Capture(a.source_dir);primary=Path(a.primary_dir)
for name,digest in packet['constant_file_hashes'].items():assert hashlib.sha256((primary/name).read_bytes()).hexdigest()==digest
items=constants(primary/'ItemID.java');npcs=constants(primary/'NpcID.java')
for candidate in packet['source_candidates']:
    e=candidate['evidence'];assert c.evidence(e['source_url'],candidate['specific_item'])==e
    rows=e['raw_drop_rows'];assert len(rows)==1
    rate=rows[0]['record']['rarity']['exact_probability'];assert tier_for_probability(Fraction(rate['numerator'],rate['denominator']))==candidate['nominal_rank']
    item=candidate['item_constant'];assert item=={'symbol':item['symbol'],**items[item['symbol']]};assert item['id']==candidate['canonical_item_id']
    for npc in candidate['npc_constant_leads']:assert npc=={'symbol':npc['symbol'],**npcs[npc['symbol']]}
    assert candidate['fp_quote'] is candidate['product_price'] is None
    assert candidate['client_traces']=='NOT_RUN' and candidate['runtime_enabled'] is candidate['authored_into_core'] is False
    assert candidate['unresolved_gates']
by_source={x['source']:x for x in packet['source_candidates']}
assert by_source['Turoth']['review_status']=='HELD_UNCITED_ESTIMATE'
assert by_source['Gargoyle']['page_level_rate_reference']['primary_image_read'] is False
assert by_source['Lizardman shaman']['direct_primary_evidence']['read_directly'] is True
print(json.dumps({'status':'PASS_THREE_CAPTURE_ROW_CONTEXT_CONSTANT_AND_RANK_REVIEWS_NOT_ADMISSION','source_candidates':3,'core':'UNCHANGED_14_KEYS_16_HUNTS','client_tests':'NOT_RUN'}))
