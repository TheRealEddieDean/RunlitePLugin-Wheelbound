"""Read-only validation of this planning packet. Run from repository root."""
from pathlib import Path
import re
import xml.etree.ElementTree as ET
ROOT=Path(__file__).resolve().parents[3]
UI=Path(__file__).resolve().parent
body=(UI/'UI_IMPLEMENTATION_BACKLOG.md').read_text()
sections=re.split(r'\n## (WB-UI-\d+) — ',body)[1:]
tasks={sections[i]:sections[i+1] for i in range(0,len(sections),2)}
assert len(tasks)==43 and len(sections)==86
assert set(tasks)=={f'WB-UI-{i:02}' for i in range(10,53)}
existing=(ROOT/'automation/IMPLEMENTATION_BACKLOG.md').read_text()
visiting=set(); done=set()
def visit(task):
    assert task not in visiting, f'cycle {task}'
    if task in done:return
    visiting.add(task)
    dep=re.search(r'Dependencies .*?: (.*?)\.',tasks[task]).group(1)
    for target in re.findall(r'WB-UI-\d+',dep):
        assert target in tasks,(task,target)
        visit(target)
    visiting.remove(task);done.add(task)
for key,section in tasks.items():
    visit(key)
    for field in ['Parent:','acceptance:','Approved authority','Current implementation status:','Dependencies','Likely files','Priority','Testing requirements:','Visual review:','Autonomous merge permitted:']:
        assert field in section,(key,field)
    parent=re.search(r'Parent: \*\*(.*?)\*\*',section).group(1)
    assert parent in existing,(key,parent)
queue=(ROOT/'automation/TASK_QUEUE.md').read_text()
for task in tasks: assert f'| {task} |' in queue,task
for md in UI.rglob('*.md'):
    for link in re.findall(r'\]\(([^)]+)\)',md.read_text()):
        if '://' in link or link.startswith('#'):continue
        assert (md.parent/link.split('#')[0]).exists(),(md,link)
for svg in (UI/'previews').glob('*.svg'):ET.parse(svg)
assert len(list((UI/'previews').glob('*.svg')))==10
deferred={key for key,section in tasks.items() if '**DEFERRED — not implemented**' in section}
active=set(tasks)-deferred
assert len(deferred)==11 and len(active)==32
immediate=sum('immediately implementable' in tasks[key] for key in active)
assert immediate==4
for key in active:
    dep=re.search(r'Dependencies .*?: (.*?)\.',tasks[key]).group(1)
    assert not (set(re.findall(r'WB-UI-\d+',dep)) & deferred),key
print(f'PASS: {len(tasks)} detailed tasks, acyclic dependencies, parents, queue IDs, links, 10 SVGs')
print(f'Initial scope: {len(active)} tasks ({immediate} immediate / {len(active)-immediate} dependent); {len(deferred)} deferred visual tasks')

# Plain-control coverage must remain aligned with its preview and existing tasks.
import json
manifest=json.loads((UI/'previews/simple-screens.json').read_text())
assert len(manifest)==36
screen_ids={s['id'] for s in manifest}
assert len(screen_ids)==36
preview=(UI/'previews/simple-screens.html').read_text()
embedded=preview.split('<script type="application/json" id="ws-screen-data">',1)[1].split('</script>',1)[0]
assert json.loads(embedded)==manifest
for screen in manifest:
    for label,dest in screen.get('links',[]): assert dest in screen_ids,(screen['id'],dest)
    for task in re.findall(r'WB-UI-\d+',screen['task']): assert task in tasks,(screen['id'],task)
    for field in ['title','kind','summary','blocked','binding']: assert screen.get(field),(screen['id'],field)
assert 'button(s.action,null,true)' in preview
assert len(preview.encode())<1_000_000
print('PASS: 36 simple screens, valid routes/task mappings, preview manifest parity and disabled gameplay commands')
