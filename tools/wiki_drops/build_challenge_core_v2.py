"""Compile reviewed finite core fragments; never activate a runtime roster."""
from collections import Counter
from fractions import Fraction
import hashlib
import json
from pathlib import Path

from challenge_tier_rules_v2 import tier_for_probability

ROOT = Path(__file__).resolve().parents[2]
D = ROOT / 'docs/wheelbound'
INPUTS = ['catalogs/challenge_hunts_v2_draft.json',
          'catalogs/challenge_standard_expansions_v2_draft.json',
          'catalogs/challenge_greater_superior_v2_draft.json']
RANKS = ('lesser', 'standard', 'greater', 'superior')


def compile_core():
    sources = []; creatures = []; hunts = []
    for path in INPUTS:
        content = (D / path).read_bytes(); p = json.loads(content)
        assert p['runtime_enabled'] is False
        sources.append({'path':path,'version':p['version'],'sha256':hashlib.sha256(content).hexdigest()})
        for c in p['creatures']:
            route = c.get('requirements', c.get('route_requirements'))
            creatures.append({'key':c['key'],'name':c['name'],'source_fragment':path,
                              'source_context_id':c['context_id'],
                              'composition_leads':c.get('composition_ids',c.get('npc_form_ids')),
                              'identity_status':'PINNED_SOURCE_LEADS_NOT_CLIENT_VALIDATED',
                              'minimum_skills':c.get('minimum_skills',{}),
                              'authored_access':route,'source_hunt_ids':c['hunts'],
                              'target_grant':'initial-lesser-v2-draft' if path==INPUTS[0] else 'individual-expansion',
                              'grants_normal_boss_unlock':False,'runtime_enabled':False})
        for h in p['hunts']:
            q=h['reward_quote']
            hours=q.get('reference_mean_hours',q.get('reference_expected_hours'))
            hunts.append({'id':h['id'],'creature_key':h['creature_key'],'rank':h['rank'],
                          'specific_item':h['specific_item'],'canonical_item_id':h['canonical_item_id'],
                          'accepted_drop_item_ids':h['accepted_drop_item_ids'],'base_rate':h['base_rate'],
                          'personal_probability_source_model':h['personal_probability_source_model'],
                          'fp_reward':h['fp_reward'],'reference_expected_hours':hours,
                          'authored_reference_kph':q['reference_kills_per_hour'],
                          'source_quote_version':q.get('quote_version',q.get('version')),
                          'source_fragment':path,'completion_contract':'challenge-personal-drop-v2-draft',
                          'runtime_enabled':False})
    result={'version':'challenge-core-v2-a89','authority':'Compiled WB-D151-154 design fragments; does not admit raw candidate rows',
            'runtime_enabled':False,'runtime_ready':False,'current_client_tests':'NOT_RUN',
            'sources':sources,'creatures':creatures,'hunts':hunts,
            'counts':{'canonical_keys':len(creatures),'hunts':len(hunts),
                      'keys_per_rank':{r:len({h['creature_key'] for h in hunts if h['rank']==r}) for r in RANKS},
                      'hunts_per_rank':dict(Counter(h['rank'] for h in hunts))},
            'product_prices':None,'numeric_family_schedule':None,
            'not_in_this_core':['Remaining36 historical v1 hunts pending prospective context review',
                                'Unadmitted display candidates and alternate/task/group contexts',
                                'Sea Snake Young held variant; Turoth necklace conflicting source rate'],
            'activation_gates':['Actual rank/source ownership and current legally repeatable access',
                                'Supported per-rank>=3 canonical eligible keys with frozen source/quote snapshot',
                                'Actual composition/local-recipient/earning-time/active-state adapter and durable receipts',
                                'Reviewed product prices and numeric offer-family schedule',
                                'Explicit engineering authorization; no main or existing normal behavior changes']}
    validate_core(result)
    return result


def validate_core(core):
    if core['runtime_enabled'] or core['runtime_ready']:
        raise ValueError('design core cannot be activated by compilation')
    if len({c['key'] for c in core['creatures']})!=len(core['creatures']):
        raise ValueError('duplicate canonical creature key')
    if len({h['id'] for h in core['hunts']})!=len(core['hunts']):
        raise ValueError('duplicate hunt identity')
    provenance = {}
    for source in core['sources']:
        data=(D/source['path']).read_bytes()
        if hashlib.sha256(data).hexdigest()!=source['sha256']:
            raise ValueError('changed fragment requires recompile/version review')
        p=json.loads(data)
        provenance[source['path']]={h['id']:h for h in p['hunts']}
    keys={c['key'] for c in core['creatures']};logical=set()
    for h in core['hunts']:
        if h['creature_key'] not in keys or tier_for_probability(Fraction(h['base_rate']))!=h['rank']:
            raise ValueError('unknown source or incorrect rank')
        item_key=(h['creature_key'],h['rank'],h['canonical_item_id'])
        if item_key in logical:
            raise ValueError('duplicate logical item within source/rank')
        logical.add(item_key)
        original=provenance[h['source_fragment']][h['id']]
        for field in ['creature_key','rank','canonical_item_id','accepted_drop_item_ids','base_rate','fp_reward']:
            if h[field]!=original[field]:raise ValueError('source/quote drift: '+field)
        if h['runtime_enabled'] or h['accepted_drop_item_ids']!=[h['canonical_item_id']]:
            raise ValueError('unreviewed enable/variant')
    for r in RANKS:
        actual=len({h['creature_key'] for h in core['hunts'] if h['rank']==r})
        if actual!=core['counts']['keys_per_rank'][r] or actual<3:
            raise ValueError('core design diversity not met; not actual account eligibility')
    return True


def render(core):
    names={c['key']:c['name'] for c in core['creatures']}
    s='''# Finite four-rank Challenge design core — A89

Compiled from three reviewed content fragments; **14 canonical keys/16 hunts**. This is a finite authored core, not all483 display groups, the full future catalog, current-account eligibility or a playable implementation. Every runtime flag is false. [Core JSON](catalogs/challenge_core_v2_draft.json) pins source fragment hashes and frozen quote versions.

| Rank | Canonical keys before account filtering | Hunts |
| --- | ---: | ---: |
'''
    for r in RANKS:s+=f"| {r} | {core['counts']['keys_per_rank'][r]} | {core['counts']['hunts_per_rank'][r]} |\n"
    for r in RANKS:
        s+='\n## '+r.title()+'\n\n| Source | Exact dropped item | Base rate | Draft FP | Authored mean hours |\n| --- | --- | --- | ---: | ---: |\n'
        for h in core['hunts']:
            if h['rank']==r:s+=f"| {names[h['creature_key']]} | {h['specific_item']} ({h['canonical_item_id']}) | {h['base_rate']} | {h['fp_reward']} | {float(Fraction(h['reference_expected_hours'])):.3f} |\n"
    s+='''\n## Meaning of coverage and held state

Three keys per rank is a design-content count before owned/enabled/source/actual-access/client/quote filtering. Unknown predicates remain unsupported; no gate is waived by compiler output. Higher-rank ownership retains lower ranks; one Challenge family and default2/upgraded3 offers remain. Creature-first/item-second sampling does not give multi-item God Wars bosses extra creature weight.

Lesser sources are the first-purchase included bundle; other sources are individual expansions. Product prices and numeric family frequencies remain unadopted. Composition values are pinned *leads*, not a declaration that every passive/phase form emits personal loot. Actual source/region/time/recipient correlation and durable once-only settlement remain mandatory. Keep item, require fresh active drop, no prior-possession exclusion, KC cap, pity or repricing.

Reference hours use source models and authored throughput, not measured training rates or guarantees. Rank rarity does not order task duration. Source provenance distinguishes direct A84 implementation evidence from mediated captured moderator statements and page-level Jagex-provided models. Remaining36 old hunts, broader candidates, Young variant and Turoth conflict are not silently imported. Original v1 and raw evidence are preserved.

Reproduce: `python tools/wiki_drops/build_challenge_core_v2.py`; validate source/quote/rank mutations with `python tools/wiki_drops/check_challenge_core_v2.py`. These are offline design checks, not future client acceptance.
'''
    return s


if __name__=='__main__':
    core=compile_core()
    (D/'catalogs/challenge_core_v2_draft.json').write_text(json.dumps(core,indent=2)+'\n')
    (D/'CHALLENGE_CORE_V2.md').write_text(render(core))
    print(json.dumps(core['counts']))
