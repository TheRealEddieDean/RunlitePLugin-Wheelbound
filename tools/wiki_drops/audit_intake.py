"""Read-only uploaded-output audit; no network. Run from repository root.
python tools/wiki_drops/audit_intake.py OUTPUT ZIP REPORT_JSON
"""
import collections, csv, hashlib, json, pathlib, re, sqlite3, sys
from fractions import Fraction

def sha(p):
    h=hashlib.sha256()
    with p.open('rb') as f:
        for b in iter(lambda:f.read(1024*1024),b''):h.update(b)
    return h.hexdigest()

def audit(root, archive):
    c=json.loads((root/'catalog.json').read_text()); included=c['creatures']; pages=included+c['unclassified_pages']; byurl={p['source_url']:p for p in pages}
    issues=collections.defaultdict(list); manifest=[]
    for f in sorted(root.rglob('*')):
        if f.is_file():manifest.append({'path':f.relative_to(root).as_posix(),'bytes':f.stat().st_size,'sha256':sha(f)})
    for p in pages:
        key=hashlib.sha256(p['source_url'].encode()).hexdigest(); pf=root/'pages'/(key+'.json'); hf=root/'html'/(key+'.html'); rf=hf.with_suffix('.json')
        if not pf.exists() or json.loads(pf.read_text())!=p:issues['page_payload_mismatch'].append(p['source_url'])
        if not hf.exists() or hashlib.sha256(hf.read_text(encoding='utf-8').encode()).hexdigest()!=p['source_sha256']:issues['html_normalized_text_hash_mismatch'].append(p['source_url'])
        if not rf.exists() or any(p.get('capture',{}).get(k)!=v for k,v in json.loads(rf.read_text()).items()):issues['receipt_mismatch'].append(p['source_url'])
    db=sqlite3.connect('file:'+str((root/'drops.sqlite').resolve())+'?mode=ro',uri=True)
    for url,payload in db.execute('select source_url,payload_json from creatures'):
        if json.loads(payload)!=byurl.get(url):issues['db_page_mismatch'].append(url)
    for url,i,payload in db.execute('select source_url,row_number,payload_json from drops'):
        if json.loads(payload)!=byurl[url]['drops'][i]:issues['db_drop_mismatch'].append([url,i])
    expected=[]
    for p in included:
        for d in p['drops']:expected.append([p['name'],p['classification'],p['source_url'],d['section'],d['item_name'] or '',d['quantity_raw'] or '',d['rarity']['raw'],d['rarity']['kind'],json.dumps(d['annotations']),'False'])
    with (root/'drops.csv').open(newline='',encoding='utf-8') as f:rows=list(csv.reader(f))
    issues['csv_differences']=[i for i,(a,b) in enumerate(zip(expected,rows[1:])) if a!=b]
    drops=[d for p in included for d in p['drops']]; all_drops=[d for p in pages for d in p['drops']]
    def dup(field):
        g=collections.defaultdict(list)
        for p in pages:g[str(p.get(field))].append(p['source_url'])
        return {k:v for k,v in g.items() if len(v)>1}
    npc=collections.defaultdict(list)
    for p in included:
        for n in p['reported_npc_ids']:npc[str(n)].append(p['source_url'])
    boss_hint=[p['name'] for p in included if p['classification']=='boss' and not any(e.startswith('page_category:') and re.search(r'\bboss(?:es)?\b',e,re.I) for e in p['classification_evidence'])]
    duplicates=[]
    for p in included:
        counts=collections.Counter(json.dumps(d,sort_keys=True) for d in p['drops'])
        for d,n in counts.items():
            if n>1:duplicates.append({'url':p['source_url'],'copies':n,'item':json.loads(d)['item_name'],'section':json.loads(d)['section']})
    times=[p['capture']['fetched_at'] for p in pages]; cache_receipts=[json.loads(f.read_text()) for f in (root/'html').glob('*.json')]
    candidates=[]
    for p in included:
        for i,d in enumerate(p['drops']):
            e=d['rarity'].get('exact_probability')
            if d['rarity']['kind']=='fraction' and e and Fraction(1,1500)<=Fraction(e['numerator'],e['denominator'])<=Fraction(1,400):candidates.append({'url':p['source_url'],'row':i,'item':d['item_name'],'raw':d['rarity']['raw']})
    import scrape
    samples=[]
    names={'Abyssal demon','King kurask','Zulrah','Basilisk Knight','Vorkath','Summoned Soul','Barrows','Kurask'}
    for p in pages:
        if p['name'] not in names:continue
        hf=root/'html'/(hashlib.sha256(p['source_url'].encode()).hexdigest()+'.html');html=hf.read_text(encoding='utf-8');q=scrape.parse_page(html,p['source_url'])
        samples.append({'name':p['name'],'url':p['source_url'],'drop_rows':len(p['drops']),'normalized_text_sha256':p['source_sha256'],'raw_file_sha256':sha(hf),'reported_revision_ids':sorted(set(re.findall(r'"wgRevisionId"\s*:\s*(\d+)',html))),'field_equal_without_ancestry_hint':{k:q[k]==p[k] for k in ['name','drops','footnotes','categories','infobox_fields','reported_npc_ids','page_context','source_sha256']},'classification_without_hint':q['classification'],'stored_classification':p['classification']})
    return {'cached_html_reparse_sample':samples,'archive':{'name':archive.name,'sha256':sha(archive),'bytes':archive.stat().st_size,'library_id':'libfile_12514dfedef0819184279d6d84d380ff'},'collector_source_sha256':sha(pathlib.Path(__file__).with_name('scrape.py')),'collector_version_proof':'No script/commit pin in upload; schema and user agent compatible with committed 0.1, exact executed source unproven.','coverage':c['coverage'],'counts':{'included_pages':len(included),'unknown_pages':len(c['unclassified_pages']),'all_page_records':len(pages),'unique_source_urls':len(byurl),'classifications':dict(collections.Counter(p['classification'] for p in pages)),'included_drop_rows':len(drops),'unknown_page_drop_rows':len(all_drops)-len(drops),'all_drop_rows':len(all_drops),'rate_kinds':dict(collections.Counter(d['rarity']['kind'] for d in drops)),'unsupported_included_rows':sum(not d['layout_supported'] for d in drops),'unsupported_all_rows':sum(not d['layout_supported'] for d in all_drops),'included_no_tables':sum(not p['drops'] for p in included),'all_no_tables':sum(not p['drops'] for p in pages),'missing_npc_ids_included':sum(not p['reported_npc_ids'] for p in included),'cache_receipts':len(cache_receipts),'db_creatures':db.execute('select count(*) from creatures').fetchone()[0],'db_drops':db.execute('select count(*) from drops').fetchone()[0],'csv_rows':len(rows)-1,'csv_expected_rows':len(expected),'page_json_files':len(list((root/'pages').glob('*.json'))),'syntactic_band_fraction_rows':len(candidates)},'provenance':{'origins':dict(collections.Counter(p.get('capture',{}).get('origin') for p in pages)),'fixture_flags':dict(collections.Counter(str(p.get('capture',{}).get('fixture')) for p in pages)),'fetched_at_min':min(times),'fetched_at_max':max(times),'generated_at':c['generated_at'],'capture_last_modified_missing':sum(not p['capture'].get('last_modified') for p in pages),'runtime_enabled_pages':sum(bool(p.get('runtime_enabled')) for p in pages),'runtime_enabled_drops':sum(bool(d.get('runtime_enabled')) for d in all_drops)},'cross_checks':dict(issues),'raw_html_byte_hash_differs_from_text_hash':sum(sha(root/'html'/(hashlib.sha256(p['source_url'].encode()).hexdigest()+'.html'))!=p['source_sha256'] for p in pages),'sqlite_integrity':db.execute('pragma integrity_check').fetchone()[0],'coverage_matches_catalog':json.loads((root/'coverage.json').read_text())==c['coverage'],'db_coverage_matches':json.loads(db.execute("select value_json from metadata where key='coverage'").fetchone()[0])==c['coverage'],'duplicates':{'source_urls':dup('source_url'),'names':dup('name'),'html_hashes':dup('source_sha256'),'shared_npc_ids':{k:v for k,v in npc.items() if len(v)>1},'identical_row_groups':duplicates},'boss_without_direct_boss_category':boss_hint,'syntactic_band_leads_sample_NOT_ELIGIBLE':candidates[:20],'files':[f for f in manifest if '/' not in f['path']],'full_file_inventory':{'count':len(manifest),'total_bytes':sum(f['bytes'] for f in manifest),'sha256_of_sorted_compact_json':hashlib.sha256(json.dumps(manifest,sort_keys=True,separators=(',',':')).encode()).hexdigest()}}

if __name__=='__main__':
    result=audit(pathlib.Path(sys.argv[1]),pathlib.Path(sys.argv[2]));pathlib.Path(sys.argv[3]).write_text(json.dumps(result,indent=2,ensure_ascii=False)+'\n');print(json.dumps({k:v for k,v in result.items() if k in ['counts','provenance','sqlite_integrity','coverage_matches_catalog','db_coverage_matches']},indent=2))
