#!/usr/bin/env python3
"""Research-only OSRS Wiki drop collector. Python 3.10+, standard library only."""
from __future__ import annotations

import argparse
from collections import deque
import csv
from datetime import datetime, timezone
from fractions import Fraction
import hashlib
from html.parser import HTMLParser
import json
from pathlib import Path
import re
import sqlite3
import sys
import time
from urllib.error import HTTPError, URLError
from urllib.parse import unquote, urljoin, urlsplit, urlunsplit
from urllib.request import HTTPRedirectHandler, Request, build_opener
from urllib.robotparser import RobotFileParser

BASE = "https://oldschool.runescape.wiki"
AGENT = "WheelboundDropCatalog/0.1 (+https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound)"
MAX_BYTES = 16 * 1024 * 1024
VOID = {"area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta", "param", "source", "track", "wbr"}


def now():
    return datetime.now(timezone.utc).isoformat()


def atomic_json(path, value):
    path = Path(path)
    path.parent.mkdir(parents=True, exist_ok=True)
    temporary = path.with_suffix(path.suffix + ".tmp")
    temporary.write_text(json.dumps(value, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    temporary.replace(path)


class Node:
    def __init__(self, tag="root", attrs=(), parent=None):
        self.tag, self.attrs, self.parent = tag, dict(attrs), parent
        self.children = []

    def walk(self):
        yield self
        for child in self.children:
            if isinstance(child, Node):
                yield from child.walk()

    def text(self, skip=()):
        if self.tag in skip:
            return ""
        return " ".join(child.text(skip) if isinstance(child, Node) else child for child in self.children)

    def has(self, name):
        return name in self.attrs.get("class", "").split()

    def closest(self, tag):
        node = self.parent
        while node is not None:
            if node.tag == tag:
                return node
            node = node.parent
        return None


def clean(text):
    return re.sub(r"\s+", " ", text).strip()


class TreeParser(HTMLParser):
    def __init__(self):
        super().__init__(convert_charrefs=True)
        self.root = Node()
        self.stack = [self.root]

    def handle_starttag(self, tag, attrs):
        # Handle common omitted HTML table/list end tags.
        if tag in {"td", "th"} and self.stack[-1].tag in {"td", "th"}:
            self.stack.pop()
        if tag == "tr":
            while self.stack[-1].tag in {"td", "th", "tr"}:
                self.stack.pop()
        if tag == "li" and self.stack[-1].tag == "li":
            self.stack.pop()
        node = Node(tag, attrs, self.stack[-1])
        self.stack[-1].children.append(node)
        if tag not in VOID:
            self.stack.append(node)

    def handle_startendtag(self, tag, attrs):
        self.handle_starttag(tag, attrs)
        if tag not in VOID:
            self.handle_endtag(tag)

    def handle_endtag(self, tag):
        for index in range(len(self.stack) - 1, 0, -1):
            if self.stack[index].tag == tag:
                del self.stack[index:]
                return

    def handle_data(self, data):
        self.stack[-1].children.append(data)


def parse_tree(html):
    parser = TreeParser()
    parser.feed(html)
    return parser.root


def wiki_url(url):
    """Restrict fetches and redirects to canonical HTTPS Wiki host."""
    parts = urlsplit(urljoin(BASE + "/", url))
    if (parts.scheme != "https" or parts.hostname != "oldschool.runescape.wiki"
            or parts.username or parts.password or parts.port not in {None, 443}):
        raise ValueError("Only HTTPS oldschool.runescape.wiki URLs are allowed")
    return urlunsplit(("https", "oldschool.runescape.wiki", parts.path, parts.query, ""))


def ratio(value):
    return {"numerator": value.numerator, "denominator": value.denominator}


def parse_rarity(raw):
    """Preserve conditions. Never silently pick first alternative/multiply rolls."""
    expression = clean(raw)
    # Bracketed reference labels are retained in raw, not mistaken for numerators.
    bare = re.sub(r"\[[^\]]*\]", "", expression).strip()
    result = {"raw": raw, "expression": bare, "kind": "unknown", "exact_probability": None,
              "needs_context_review": bool(re.search(r"\[[^\]]*\]", raw))}
    if ";" in bare:
        result.update(kind="alternatives", alternatives=[parse_rarity(p.strip()) for p in bare.split(";")],
                      needs_context_review=True)
        return result
    approximate = bare.startswith(("~", "≈"))
    body = bare[1:].strip() if approximate else bare
    repeated = re.fullmatch(r"(\d+)\s*[×x]\s*(\d[\d,]*)\s*/\s*(\d[\d,]*)", body)
    fraction = re.fullmatch(r"(\d[\d,]*)\s*/\s*(\d[\d,]*)", body)
    try:
        if repeated:
            rolls, numerator, denominator = (int(v.replace(",", "")) for v in repeated.groups())
            p = Fraction(numerator, denominator)
            if not 0 <= p <= 1 or rolls < 1:
                raise ValueError("Invalid roll probability")
            result.update(kind="multiple_rolls", roll_count=rolls, per_roll_probability=ratio(p),
                          approximate=approximate, needs_context_review=True)
        elif fraction:
            p = Fraction(*(int(v.replace(",", "")) for v in fraction.groups()))
            if not 0 <= p <= 1:
                raise ValueError("Probability outside [0,1]")
            result.update(kind="approximate" if approximate else "fraction", approximate=approximate)
            if approximate:
                result["reported_probability"] = ratio(p)
                result["needs_context_review"] = True
            else:
                result["exact_probability"] = ratio(p)
        elif bare.lower() in {"always", "guaranteed"}:
            result.update(kind="always", exact_probability=ratio(Fraction(1)))
    except (ValueError, ZeroDivisionError):
        result["kind"] = "invalid"
        result["needs_context_review"] = True
    return result


def cell_refs(cell):
    return [clean(n.text()) for n in cell.walk() if n.tag == "sup"]


def parse_page(html, source_url, kind_hint=None, classification_evidence=None):
    root = parse_tree(html)
    nodes = list(root.walk())
    title_node = next((n for n in nodes if n.tag == "h1"), None)
    title = clean(title_node.text()) if title_node else unquote(urlsplit(source_url).path.rsplit("/", 1)[-1]).replace("_", " ")
    category_nodes = [n for n in nodes if n.attrs.get("id") in {"catlinks", "mw-normal-catlinks"}]
    categories = sorted({clean(a.text()) for n in category_nodes for a in n.walk()
                         if a.tag == "a" and "/w/Category:" in a.attrs.get("href", "")})
    infoboxes = [n for n in nodes if n.tag == "table" and n.has("infobox")]
    metadata = []
    for table in infoboxes:
        for tr in table.walk():
            if tr.tag != "tr" or tr.closest("table") is not table:
                continue
            cells = [n for n in tr.children if isinstance(n, Node) and n.tag in {"th", "td"}]
            if len(cells) >= 2:
                metadata.append({"field": clean(cells[0].text()), "value": clean(" ".join(n.text() for n in cells[1:]))})
    evidence = list(classification_evidence or [])
    boss_categories = [c for c in categories if re.search(r"\bboss(?:es)?\b", c, re.I)]
    monster_signal = any(re.search(r"combat level|hitpoints|slayer level", m["field"], re.I) for m in metadata)
    monster_categories = [c for c in categories if re.search(r"\bmonsters?\b", c, re.I)]
    if boss_categories and (monster_signal or monster_categories):
        kind = "boss"
        evidence.extend("page_category:" + c for c in boss_categories)
    elif kind_hint == "boss" and monster_signal:
        kind = "boss"
    elif monster_signal or monster_categories:
        kind = "monster"
        if monster_signal:
            evidence.append("monster_infobox_fields")
        evidence.extend("page_category:" + c for c in monster_categories)
    else:
        kind = "unknown"
    heading = {}
    drops, warnings = [], []
    for node in nodes:
        if node.tag in {"h2", "h3", "h4"}:
            level = int(node.tag[1])
            heading = {k: v for k, v in heading.items() if k < level}
            heading[level] = clean(node.text()).replace("[ edit | edit source ]", "").strip()
        if node.tag != "table" or not node.has("item-drops"):
            continue
        headers = None
        section = " / ".join(heading[k] for k in sorted(heading)) or "Unlabelled drop table"
        for tr in node.walk():
            if tr.tag != "tr" or tr.closest("table") is not node:
                continue
            cells = [n for n in tr.children if isinstance(n, Node) and n.tag in {"th", "td"}]
            if not cells:
                continue
            if all(c.tag == "th" for c in cells):
                candidate = []
                for cell in cells:
                    label = clean(cell.text()).lower()
                    span = cell.attrs.get("colspan", "1")
                    if label in {"item", "item name"} and span == "2":
                        candidate.extend(["item icon", "item"])
                    else:
                        candidate.append(label)
                if any("item" in h for h in candidate) and any("rarity" in h or "chance" in h for h in candidate):
                    headers = candidate
                continue
            texts = [clean(c.text()) for c in cells]
            raw = {"cells": texts, "table_headers": headers}
            # Keep all rows, including unsupported layouts, but never fabricate column identities.
            layout_issue = headers is None or len(headers) != len(cells) or any(
                c.attrs.get("rowspan", "1") != "1" or c.attrs.get("colspan", "1") != "1" for c in cells)
            columns = {} if layout_issue else dict(zip(headers, cells))
            item_cell = next((c for h, c in columns.items() if h in {"item", "item name"}), None)
            chance_cell = next((c for h, c in columns.items() if "rarity" in h or "chance" in h), None)
            quantity_cell = next((c for h, c in columns.items() if "quantity" in h), None)
            item_name = clean(item_cell.text(skip={"sup"})) if item_cell else None
            links = [{"text": clean(a.text()), "href": urljoin(source_url, a.attrs["href"])}
                     for a in (item_cell.walk() if item_cell else []) if a.tag == "a" and a.attrs.get("href")]
            rate_text = clean(chance_cell.text()) if chance_cell else ""
            annotations = [ref for cell in cells for ref in cell_refs(cell)]
            record = {"section": section, "item_name": item_name, "item_links": links,
                      "quantity_raw": clean(quantity_cell.text()) if quantity_cell else None,
                      "rarity": parse_rarity(rate_text), "annotations": annotations,
                      "raw_row": raw, "layout_supported": not layout_issue,
                      "canonical_item_ids": [], "runtime_enabled": False, "review_status": "UNREVIEWED"}
            drops.append(record)
            if layout_issue:
                warnings.append("Unsupported table layout in " + section)
    notes = [{"id": n.attrs.get("id"), "text": clean(n.text())} for n in nodes
             if n.tag == "li" and n.attrs.get("id", "").startswith("cite_note")]
    page_context = []
    for n in nodes:
        if n.tag == "p" and clean(n.text()):
            page_context.append(clean(n.text()))
    npc_ids = sorted({int(v) for m in metadata if re.fullmatch(r"(?:monster|npc)\s*ids?", m["field"], re.I)
                      for v in re.findall(r"\b\d+\b", m["value"])})
    return {"schema_version": 1, "name": title, "source_url": source_url, "reported_npc_ids": npc_ids,
            "source_sha256": hashlib.sha256(html.encode("utf-8")).hexdigest(),
            "classification": kind, "classification_evidence": sorted(set(evidence)),
            "categories": categories, "infobox_fields": metadata, "page_context": page_context,
            "footnotes": notes, "drops": drops, "warnings": sorted(set(warnings)),
            "drop_tables_status": "PARSED_UNREVIEWED" if drops else "NO_SUPPORTED_TABLES",
            "runtime_enabled": False}


def category_links(html, current_url):
    root = parse_tree(html)
    candidates, categories, pagination = set(), set(), set()
    for n in root.walk():
        if n.attrs.get("id") not in {"mw-pages", "mw-subcategories"}:
            continue
        for a in n.walk():
            if a.tag != "a" or not a.attrs.get("href"):
                continue
            try:
                url = wiki_url(urljoin(current_url, a.attrs["href"]))
            except ValueError:
                continue
            label = clean(a.text()).lower()
            if "next" in label and ("page" in label or re.search(r"\d", label)):
                pagination.add(url)
            elif "previous" not in label:
                name = unquote(urlsplit(url).path).split("/w/", 1)[-1]
                if name.startswith("Category:"):
                    categories.add(url)
                elif ":" not in name and "/" not in name and urlsplit(url).path.startswith("/w/"):
                    candidates.add(url)
    return sorted(candidates), sorted(categories), sorted(pagination)


class FetchError(Exception):
    pass


class AccessStop(FetchError):
    pass


class CheckedRedirects(HTTPRedirectHandler):
    def __init__(self, check):
        self.check = check

    def redirect_request(self, request, fp, code, message, headers, newurl):
        self.check(wiki_url(newurl))
        return super().redirect_request(request, fp, code, message, headers, newurl)


class Fetcher:
    def __init__(self, out, delay=2.0, timeout=30, offline=False, refresh=False):
        self.out = Path(out)
        self.delay, self.timeout, self.offline = max(2.0, delay), timeout, offline
        self.last_request = 0.0
        self.last_response_metadata = {}
        self.refresh = refresh
        self.robot = None
        self.cache_dir = self.out / "html"
        self.cache_dir.mkdir(parents=True, exist_ok=True)
        self.opener = build_opener(CheckedRedirects(self.check))

    def check(self, url):
        url = wiki_url(url)
        if self.robot is None or not self.robot.can_fetch(AGENT, url):
            raise FetchError("robots.txt does not authorize this URL: " + url)

    def _get(self, url):
        remaining = self.delay - (time.monotonic() - self.last_request)
        if remaining > 0:
            time.sleep(remaining)
        self.last_request = time.monotonic()
        request = Request(url, headers={"User-Agent": AGENT, "Accept": "text/html,text/plain;q=0.9"})
        try:
            with self.opener.open(request, timeout=self.timeout) as response:
                raw = response.read(MAX_BYTES + 1)
                if len(raw) > MAX_BYTES:
                    raise FetchError("Response exceeds16MiB")
                if response.headers.get_content_type() not in {"text/html", "text/plain"}:
                    raise FetchError("Unsupported response content type")
                self.last_response_metadata = {"resolved_url": wiki_url(response.geturl()),
                    "etag": response.headers.get("ETag"), "last_modified": response.headers.get("Last-Modified")}
                return raw.decode(response.headers.get_content_charset() or "utf-8")
        except HTTPError as exc:
            if exc.code in {403, 429}:
                raise AccessStop(f"HTTP{exc.code}; stopping requests, no bypass/retry") from exc
            raise FetchError(str(exc)) from exc
        except (URLError, UnicodeError, OSError) as exc:
            raise FetchError(str(exc)) from exc

    def init_robots(self):
        if self.offline:
            raise FetchError("Offline mode cannot initialize network fetching")
        # robots itself is fetched once; redirects fail closed until policy loaded.
        text = self._get(BASE + "/robots.txt")
        if "user-agent:" not in text.lower():
            raise FetchError("Unrecognized robots.txt; fail closed")
        atomic_json(self.out / "robots_receipt.json", {"url": BASE + "/robots.txt", "fetched_at": now(),
                    "user_agent": AGENT, "text": text, "sha256": hashlib.sha256(text.encode()).hexdigest()})
        self.robot = RobotFileParser(BASE + "/robots.txt")
        self.robot.parse(text.splitlines())
        crawl_delay = self.robot.crawl_delay(AGENT)
        if crawl_delay:
            self.delay = max(self.delay, crawl_delay)
        request_rate = self.robot.request_rate(AGENT)
        if request_rate and request_rate.requests:
            self.delay = max(self.delay, request_rate.seconds / request_rate.requests)

    def fetch(self, url):
        url = wiki_url(url)
        self.check(url)  # Recheck policy even for cached content in network mode.
        key = hashlib.sha256(url.encode()).hexdigest()
        cached, receipt = self.cache_dir / (key + ".html"), self.cache_dir / (key + ".json")
        if not self.refresh and cached.exists() and receipt.exists():
            info = json.loads(receipt.read_text(encoding="utf-8"))
            html = cached.read_text(encoding="utf-8")
            if hashlib.sha256(html.encode()).hexdigest() == info.get("sha256"):
                return html, info
        html = self._get(url)
        root = parse_tree(html)
        if any(n.tag == "title" and re.search(r"just a moment|access denied", n.text(), re.I) for n in root.walk()):
            raise AccessStop("Access challenge returned; stopping requests, no bypass")
        cached.write_text(html, encoding="utf-8")
        info = {"requested_url": url, "fetched_at": now(), "sha256": hashlib.sha256(html.encode()).hexdigest(),
                "user_agent": AGENT, "cache_file": str(cached), **self.last_response_metadata}
        atomic_json(receipt, info)
        return html, info


def save_page(out, page):
    key = hashlib.sha256(page["source_url"].encode()).hexdigest()
    atomic_json(Path(out) / "pages" / (key + ".json"), page)


def export(out, coverage):
    out = Path(out)
    pages = [json.loads(f.read_text(encoding="utf-8")) for f in sorted((out / "pages").glob("*.json"))]
    included = [p for p in pages if p["classification"] in {"boss", "monster"}]
    atomic_json(out / "catalog.json", {"schema_version": 1, "generated_at": now(),
                "runtime_enabled": False, "coverage": coverage, "creatures": included,
                "unclassified_pages": [p for p in pages if p["classification"] == "unknown"]})
    with sqlite3.connect(out / "drops.sqlite") as db:
        db.execute("CREATE TABLE IF NOT EXISTS creatures (source_url TEXT PRIMARY KEY, name TEXT, kind TEXT, payload_json TEXT)")
        db.execute("CREATE TABLE IF NOT EXISTS drops (source_url TEXT, row_number INTEGER, item_name TEXT, section TEXT, rarity_raw TEXT, payload_json TEXT, PRIMARY KEY(source_url,row_number))")
        db.execute("CREATE TABLE IF NOT EXISTS metadata (key TEXT PRIMARY KEY, value_json TEXT)")
        db.execute("DELETE FROM drops")
        db.execute("DELETE FROM creatures")
        for p in included:
            db.execute("INSERT INTO creatures VALUES (?,?,?,?)", (p["source_url"], p["name"], p["classification"], json.dumps(p, ensure_ascii=False)))
            for i, d in enumerate(p["drops"]):
                db.execute("INSERT INTO drops VALUES (?,?,?,?,?,?)", (p["source_url"], i, d["item_name"], d["section"], d["rarity"]["raw"], json.dumps(d, ensure_ascii=False)))
        db.execute("INSERT OR REPLACE INTO metadata VALUES (?,?)", ("coverage", json.dumps(coverage)))
    with (out / "drops.csv").open("w", encoding="utf-8", newline="") as file:
        writer = csv.writer(file)
        writer.writerow(["creature", "kind", "source_url", "section", "item", "quantity_raw", "rarity_raw", "rarity_kind", "conditions_json", "runtime_enabled"])
        for p in included:
            for d in p["drops"]:
                writer.writerow([p["name"], p["classification"], p["source_url"], d["section"], d["item_name"],
                                 d["quantity_raw"], d["rarity"]["raw"], d["rarity"]["kind"], json.dumps(d["annotations"]), False])
    return len(included), sum(len(p["drops"]) for p in included)


def run(args):
    out = Path(args.output)
    if args.offline_manifest and (args.refresh or args.retry_errors):
        raise ValueError("Offline import cannot use network refresh/retry options")
    out.mkdir(parents=True, exist_ok=True)
    coverage = {"scope": "offline_manifest" if args.offline_manifest else "category_discovery" if args.discover_all else "url_manifest",
                "started_at": now(), "status": "IN_PROGRESS", "all_monsters_verified": False,
                "errors": [], "pending": [], "processed_pages": 0, "discovered_pages": 0}
    try:
        mode = "offline" if args.offline_manifest else "network"
        input_file = args.offline_manifest or args.urls
        fingerprint = hashlib.sha256(Path(input_file).read_bytes()).hexdigest() if input_file else None
        scope = {"mode": mode, "discover_all": args.discover_all,
                 "input_path": str(Path(input_file).resolve()) if input_file else None,
                 "input_sha256": fingerprint}
        scope_path = out / "scope.json"
        if scope_path.exists() and json.loads(scope_path.read_text(encoding="utf-8")) != scope:
            raise FetchError("Output belongs to a different scope/input; use a new output folder")
        atomic_json(scope_path, scope)
        if args.offline_manifest:
            for entry in json.loads(Path(args.offline_manifest).read_text(encoding="utf-8")):
                file = Path(args.offline_manifest).parent / entry["file"]
                html = file.read_text(encoding="utf-8")
                url = wiki_url(entry["url"])
                page = parse_page(html, url, entry.get("kind"), ["offline_manifest:" + str(file)])
                cache = out / "html"
                cache.mkdir(parents=True, exist_ok=True)
                (cache / (hashlib.sha256(url.encode()).hexdigest() + ".html")).write_text(html, encoding="utf-8")
                page["capture"] = {"origin": "offline_import", "imported_at": now(), "fetched_at": entry.get("fetched_at"),
                                   "fixture": bool(entry.get("fixture", False))}
                save_page(out, page)
                coverage["processed_pages"] += 1
            coverage["status"] = "OFFLINE_IMPORT_COMPLETE"
        else:
            fetcher = Fetcher(out, args.delay, args.timeout, refresh=args.refresh)
            fetcher.init_robots()
            state_path = out / "crawl_state.json"
            signature = scope
            if state_path.exists():
                state = json.loads(state_path.read_text(encoding="utf-8"))
                if state["signature"] != signature:
                    raise FetchError("Output belongs to a different input scope; choose a new output folder")
            else:
                tasks = []
                if args.discover_all:
                    # Rooted membership gives evidence for boss classification, not combat-level guesses.
                    tasks.extend({"url": BASE + "/w/Category:" + cat, "role": "category", "kind": kind}
                                 for cat, kind in [("Monsters", "monster"), ("Bosses", "boss")])
                if args.urls:
                    for line in Path(args.urls).read_text(encoding="utf-8").splitlines():
                        if line.strip() and not line.lstrip().startswith("#"):
                            tasks.append({"url": wiki_url(line.strip()), "role": "page", "kind": None})
                state = {"signature": signature, "queue": tasks, "done": [], "errors": [], "processed": 0, "scope_started_at": now()}
            queue = deque(state["queue"])
            done = {tuple(v) for v in state["done"]}
            errors = list(state["errors"])
            if args.refresh:
                # Rebuild discovery from roots/input; old pages remain until replaced by valid captures.
                fresh_tasks = []
                if args.discover_all:
                    fresh_tasks.extend({"url": BASE + "/w/Category:" + cat, "role": "category", "kind": kind}
                                       for cat, kind in [("Monsters", "monster"), ("Bosses", "boss")])
                if args.urls:
                    fresh_tasks.extend({"url": wiki_url(line.strip()), "role": "page", "kind": None}
                                       for line in Path(args.urls).read_text(encoding="utf-8").splitlines()
                                       if line.strip() and not line.lstrip().startswith("#"))
                queue = deque(fresh_tasks)
                done = set()
                state.setdefault("error_history", []).extend(errors)
                errors = []
            if args.retry_errors:
                for failed in errors:
                    task = {k: failed[k] for k in ("url", "role", "kind")}
                    done.discard((task["url"], task["role"], task["kind"]))
                    queue.append(task)
                state.setdefault("error_history", []).extend(errors)
                errors = []
            attempts = 0
            in_flight = None
            try:
                while queue and (args.max_pages == 0 or attempts < args.max_pages):
                    task = queue.popleft()
                    key = (task["url"], task["role"], task["kind"])
                    if key in done:
                        continue
                    attempts += 1
                    in_flight = task
                    try:
                        html, receipt = fetcher.fetch(task["url"])
                        if task["role"] == "category":
                            candidates, subs, pagination = category_links(html, task["url"])
                            # No recognized category structure must not mean successful empty discovery.
                            if not any(n.attrs.get("id") in {"mw-pages", "mw-subcategories"} for n in parse_tree(html).walk()):
                                raise FetchError("Unsupported/missing category layout")
                            queue.extend({"url": u, "role": "page", "kind": task["kind"]} for u in candidates)
                            queue.extend({"url": u, "role": "category", "kind": task["kind"]} for u in subs + pagination)
                        else:
                            page = parse_page(html, task["url"], task["kind"], ["category_ancestry:" + task["kind"]] if task["kind"] else [])
                            page["capture"] = {"origin": "network_html", **receipt, "fixture": False}
                            # Preserve a boss-root classification when reached again via monster ancestry.
                            page_key = hashlib.sha256(page["source_url"].encode()).hexdigest()
                            old_path = out / "pages" / (page_key + ".json")
                            if old_path.exists():
                                old = json.loads(old_path.read_text(encoding="utf-8"))
                                if old["classification"] == "boss" and page["classification"] != "boss":
                                    page["classification"] = "boss"
                                    page["classification_evidence"] += old["classification_evidence"]
                            save_page(out, page)
                            state["processed"] += 1
                        print(task["role"] + ": " + task["url"], flush=True)
                    except AccessStop as exc:
                        errors.append({**task, "error": str(exc), "at": now()})
                        print("Stopped: " + str(exc), file=sys.stderr, flush=True)
                        # Keep remaining work intact; explicit retry-errors may retry this task later.
                        done.add(key)
                        break
                    except (FetchError, ValueError) as exc:
                        errors.append({**task, "error": str(exc), "at": now()})
                        print("Unavailable: " + task["url"] + " — " + str(exc), file=sys.stderr, flush=True)
                    done.add(key)
                    in_flight = None
                    state.update(queue=list(queue), done=[list(v) for v in sorted(done, key=str)], errors=errors)
                    atomic_json(state_path, state)
            except KeyboardInterrupt:
                coverage["interrupted"] = True
                if in_flight and (in_flight["url"], in_flight["role"], in_flight["kind"]) not in done:
                    queue.appendleft(in_flight)
            state.update(queue=list(queue), done=[list(v) for v in sorted(done, key=str)], errors=errors)
            atomic_json(state_path, state)
            coverage.update(errors=errors, pending=list(queue), processed_pages=state["processed"],
                            discovered_pages=sum(v[1] == "page" for v in done) + sum(t["role"] == "page" for t in queue))
            coverage["status"] = "PARTIAL" if errors or queue or coverage.get("interrupted") else "DISCOVERY_EXHAUSTED_UNVERIFIED" if args.discover_all else "MANIFEST_EXHAUSTED_UNVERIFIED"
    except (FetchError, OSError, ValueError, KeyError) as exc:
        coverage["status"] = "BLOCKED"
        coverage["errors"].append({"error": str(exc), "at": now()})
        print(str(exc), file=sys.stderr)
    finally:
        coverage["finished_at"] = now()
        saved = list((out / "pages").glob("*.json"))
        parsed = [json.loads(p.read_text(encoding="utf-8")) for p in saved]
        coverage["unclassified_pages"] = sum(p["classification"] == "unknown" for p in parsed)
        coverage["pages_without_supported_tables"] = sum(p["drop_tables_status"] == "NO_SUPPORTED_TABLES" for p in parsed)
        coverage["unsupported_row_count"] = sum(not d["layout_supported"] for p in parsed for d in p["drops"])
        count, drops = export(out, coverage)
        atomic_json(out / "coverage.json", coverage)
        print(f"Saved{count} creatures/{drops} drop rows; status={coverage['status']}. All-monster completeness is unverified.")
    return 2 if coverage["status"] in {"BLOCKED", "PARTIAL"} else 0


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", default="tools/wiki_drops/output", help="Local research output directory")
    parser.add_argument("--offline-manifest", help="JSON array: file,url, optional kind/fetched_at/fixture; no network")
    parser.add_argument("--discover-all", action="store_true", help="Traverse Monsters/Bosses categories; report missing/blocked coverage")
    parser.add_argument("--urls", help="Text file with explicit Wiki URLs, one per line")
    parser.add_argument("--max-pages", type=int, default=250, help="Per-invocation page/category request budget;0 means unlimited")
    parser.add_argument("--delay", type=float, default=2.0, help="Seconds between uncached requests; minimum2, honors robots delay")
    parser.add_argument("--timeout", type=float, default=30)
    parser.add_argument("--refresh", action="store_true", help="Rewalk the scope and recapture allowed URLs; existing captures keep their original timestamps until replaced")
    parser.add_argument("--retry-errors", action="store_true", help="Explicitly retry failed tasks on a later invocation, still respecting robots")
    args = parser.parse_args()
    if args.offline_manifest and (args.discover_all or args.urls or args.refresh or args.retry_errors):
        parser.error("Offline import cannot be combined with network modes")
    if not (args.offline_manifest or args.discover_all or args.urls):
        parser.error("Choose --offline-manifest, --discover-all or --urls")
    if args.max_pages < 0 or args.timeout <= 0:
        parser.error("max-pages must be nonnegative and timeout positive")
    return run(args)


if __name__ == "__main__":
    raise SystemExit(main())
