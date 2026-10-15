"""Bounded lossless context extraction from a supplied capture; not roster admission."""
import hashlib
import json
import re
from pathlib import Path
import scrape

PIN = '42a6f17a6a2e8e478aa763890ecd0181a59dad38'


def constants(path):
    """Only top-level constants: nested Cert/Placeholder values are different IDs."""
    text = Path(path).read_text()
    return {m[1]: {'id': int(m[2]), 'line': text.count('\n', 0, m.start()) + 1,
                   'declaration': m[0].strip()}
            for m in re.finditer(r'^\tpublic static final int (\w+) = (\d+);', text, re.M)}


class Capture:
    def __init__(self, directory):
        self.directory = Path(directory)
        self.catalog = json.loads((self.directory / 'catalog.json').read_text())
        self.pages = {p['source_url']: p for p in self.catalog['creatures'] + self.catalog['unclassified_pages']}

    def evidence(self, source_url, item):
        page = self.pages[source_url]
        data = (self.directory / 'html' / (hashlib.sha256(source_url.encode()).hexdigest() + '.html')).read_bytes()
        text = data.decode('utf-8').replace('\r\n', '\n').replace('\r', '\n')
        assert hashlib.sha256(text.encode()).hexdigest() == page['source_sha256']
        root = scrape.parse_tree(text)
        html_rows = []
        for node in root.walk():
            if node.tag != 'tr':
                continue
            cells = [n for n in node.children if getattr(n, 'tag', '') == 'td']
            item_cells = [n for n in cells if 'item-col' in n.attrs.get('class', '').split()]
            if not any(scrape.clean(n.text()) == item for n in item_cells):
                continue
            note_ids = sorted({n.attrs['href'].split('#')[-1] for n in node.walk()
                               if n.tag == 'a' and '#cite_note' in n.attrs.get('href', '')})
            html_rows.append({'row_text': scrape.clean(node.text()),
                              'reported_item_id': int(node.attrs['data-item-id']) if node.attrs.get('data-item-id', '').isdigit() else None,
                              'note_fragment_ids': note_ids,
                              'referenced_notes': [n for n in page['footnotes'] if n['id'] in note_ids]})
        assert html_rows, (source_url, item)
        return {'source_url': source_url, 'item': item, 'capture': page['capture'],
                'normalized_source_sha256': page['source_sha256'],
                'raw_file_sha256': hashlib.sha256(data).hexdigest(),
                'reported_npc_ids': page['reported_npc_ids'],
                'source_context': page['page_context'],
                'access_infobox': [r for r in page['infobox_fields'] if any(k in r['field'].lower() for k in ['id', 'slayer', 'member'])],
                'raw_drop_rows': [{'row_zero_based': i, 'record': r} for i, r in enumerate(page['drops']) if r['item_name'] == item],
                'html_drop_rows': html_rows,
                'page_estimate_statements': [s for s in page['page_context'] if 'drop rates are estimated' in s.lower()],
                'runtime_enabled': False}
