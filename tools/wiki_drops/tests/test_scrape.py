"""Offline semantic/collection tests; no Wiki network requests."""
import argparse
import importlib.util
import json
from pathlib import Path
import sqlite3
import tempfile
import unittest
from unittest.mock import patch
from urllib.error import HTTPError
from urllib.robotparser import RobotFileParser

ROOT = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("wiki_scrape", ROOT / "scrape.py")
scrape = importlib.util.module_from_spec(spec)
spec.loader.exec_module(scrape)
FIXTURES = Path(__file__).parent / "fixtures"


class Rates(unittest.TestCase):
    def test_exact_rational_and_endpoints(self):
        self.assertEqual(scrape.parse_rarity("499/250,000")["exact_probability"], {"numerator":499,"denominator":250000})
        self.assertEqual(scrape.parse_rarity("1/1,500")["exact_probability"]["denominator"],1500)

    def test_alternative_contexts_not_silently_selected(self):
        r = scrape.parse_rarity("1/5,000; 1/1,000 [1]")
        self.assertEqual(r["kind"],"alternatives")
        self.assertIsNone(r["exact_probability"])
        self.assertEqual([x["exact_probability"]["denominator"] for x in r["alternatives"]],[5000,1000])
        self.assertTrue(r["needs_context_review"])

    def test_multiple_rolls_no_assumed_independence(self):
        r = scrape.parse_rarity("3 × 1/1,026")
        self.assertEqual(r["roll_count"],3)
        self.assertIsNone(r["exact_probability"])
        self.assertEqual(r["per_roll_probability"]["denominator"],1026)

    def test_approximation_unknown_and_invalid(self):
        self.assertIsNone(scrape.parse_rarity("~1/500")["exact_probability"])
        self.assertEqual(scrape.parse_rarity("Rare")["kind"],"unknown")
        self.assertEqual(scrape.parse_rarity("1/0")["kind"],"invalid")
        self.assertEqual(scrape.parse_rarity("2/1")["kind"],"invalid")


class Parsing(unittest.TestCase):
    def test_item_colspan_and_context_retention(self):
        p = scrape.parse_page((FIXTURES/"ordinary.html").read_text(), scrape.BASE+"/w/Fixture_monster")
        self.assertEqual(p["classification"],"monster")
        self.assertEqual(len(p["drops"]),4)
        self.assertEqual(p["drops"][0]["item_name"],"Fixture axe")
        self.assertTrue(all(d["layout_supported"] for d in p["drops"]))
        self.assertEqual(p["drops"][1]["annotations"],["[1]"])
        self.assertIn("matching Slayer task",p["footnotes"][0]["text"])
        self.assertIn("special weapons",p["page_context"][0])
        self.assertEqual(p["drops"][3]["quantity_raw"],"25; 38")
        self.assertFalse(p["runtime_enabled"])

    def test_boss_and_different_methods(self):
        p = scrape.parse_page((FIXTURES/"boss.html").read_text(),scrape.BASE+"/w/Fixture_boss")
        self.assertEqual(p["classification"],"boss")
        self.assertEqual(p["drops"][0]["section"],"Drops / Uniques")
        self.assertEqual(p["drops"][1]["section"],"Pickpocketing loot")

    def test_boss_category_ancestry_requires_monster_evidence(self):
        p = scrape.parse_page('<h1>Pet item</h1><table class="infobox"><tr><th>Price</th><td>1</td></tr></table>',scrape.BASE+"/w/Pet", "boss", ["category_ancestry:boss"])
        self.assertEqual(p["classification"],"unknown")
        p = scrape.parse_page('<h1>Creature</h1><table class="infobox"><tr><th>Combat level</th><td>10</td></tr></table>',scrape.BASE+"/w/Creature","boss")
        self.assertEqual(p["classification"],"boss")

    def test_unsupported_layout_and_missing_tables_not_empty_proof(self):
        p = scrape.parse_page('<h1>X</h1><table class="item-drops"><tr><td>Unknown</td><td>1/500</td></tr></table>',scrape.BASE+"/w/X")
        self.assertEqual(len(p["drops"]),1)
        self.assertFalse(p["drops"][0]["layout_supported"])
        self.assertIsNone(p["drops"][0]["item_name"])
        p = scrape.parse_page('<h1>No data</h1>',scrape.BASE+"/w/X")
        self.assertEqual(p["drop_tables_status"],"NO_SUPPORTED_TABLES")
        self.assertEqual(p["classification"],"unknown")

    def test_category_discovery_and_blockable_pagination(self):
        html = '<div id="mw-pages"><a href="/w/Monster">Monster</a><a href="/w/Category:Monsters?pagefrom=Z">next 200</a></div><div id="mw-subcategories"><a href="/w/Category:Slayer_monsters">Slayer monsters</a></div>'
        pages, cats, pagination = scrape.category_links(html,scrape.BASE+"/w/Category:Monsters")
        self.assertEqual(pages,[scrape.BASE+"/w/Monster"])
        self.assertEqual(cats,[scrape.BASE+"/w/Category:Slayer_monsters"])
        self.assertIn("pagefrom=Z",pagination[0])


class Collection(unittest.TestCase):
    def args(self,out,**kw):
        values = dict(output=str(out),offline_manifest=None,discover_all=False,urls=None,max_pages=1,delay=2,timeout=30,retry_errors=False,refresh=False)
        values.update(kw)
        return argparse.Namespace(**values)

    def test_offline_outputs_do_not_claim_live_coverage(self):
        with tempfile.TemporaryDirectory() as tmp:
            with patch.object(scrape.Fetcher,"init_robots",side_effect=AssertionError("network")):
                self.assertEqual(scrape.run(self.args(tmp,offline_manifest=str(FIXTURES/"manifest.json"))),0)
            catalog=json.loads((Path(tmp)/"catalog.json").read_text())
            self.assertEqual(len(catalog["creatures"]),2)
            self.assertFalse(catalog["coverage"]["all_monsters_verified"])
            self.assertTrue(all(p["capture"]["fixture"] for p in catalog["creatures"]))
            with sqlite3.connect(Path(tmp)/"drops.sqlite") as db:
                self.assertEqual(db.execute("SELECT count(*) FROM drops").fetchone()[0],6)
                self.assertEqual(set(r[0] for r in db.execute("SELECT kind FROM creatures")),{"boss","monster"})
            self.assertEqual(len(list((Path(tmp)/"html").glob("*.html"))),2)

    def test_robots_denial_fails_closed_and_redirect_host_rejected(self):
        with tempfile.TemporaryDirectory() as tmp:
            f=scrape.Fetcher(tmp)
            f.robot=RobotFileParser()
            f.robot.parse(["User-agent: *","Disallow: /"])
            with patch.object(f,"_get",side_effect=AssertionError("should not fetch")):
                with self.assertRaises(scrape.FetchError):f.fetch(scrape.BASE+"/w/Monster")
            with self.assertRaises(ValueError):scrape.wiki_url("https://example.com/w/X")
            with self.assertRaises(ValueError):scrape.wiki_url("https://u:p@oldschool.runescape.wiki/w/X")
            with self.assertRaises(scrape.FetchError):scrape.Fetcher(tmp,offline=True).init_robots()

    def test_resume_keeps_unprocessed_work(self):
        with tempfile.TemporaryDirectory() as tmp:
            urls=Path(tmp)/"urls.txt";urls.write_text(scrape.BASE+"/w/A\n"+scrape.BASE+"/w/B\n")
            out=Path(tmp)/"out"
            html=(FIXTURES/"ordinary.html").read_text()
            with patch.object(scrape.Fetcher,"init_robots"),patch.object(scrape.Fetcher,"fetch",return_value=(html,{"fetched_at":"fixture","fixture":True})):
                self.assertEqual(scrape.run(self.args(out,urls=str(urls))),2)
                self.assertEqual(len(json.loads((out/"crawl_state.json").read_text())["queue"]),1)
                self.assertEqual(scrape.run(self.args(out,urls=str(urls))),0)
                self.assertEqual(json.loads((out/"crawl_state.json").read_text())["processed"],2)

    def test_http_access_stop_does_not_continue_queue(self):
        with tempfile.TemporaryDirectory() as tmp:
            urls=Path(tmp)/"urls.txt";urls.write_text(scrape.BASE+"/w/A\n"+scrape.BASE+"/w/B\n")
            out=Path(tmp)/"out"
            with patch.object(scrape.Fetcher,"init_robots"),patch.object(scrape.Fetcher,"fetch",side_effect=scrape.AccessStop("HTTP403")) as get:
                self.assertEqual(scrape.run(self.args(out,urls=str(urls),max_pages=0)),2)
                self.assertEqual(get.call_count,1)
            state=json.loads((out/"crawl_state.json").read_text())
            self.assertEqual(len(state["queue"]),1)
            self.assertEqual(len(state["errors"]),1)

    def test_retry_errors_and_refresh_preserve_scope(self):
        with tempfile.TemporaryDirectory() as tmp:
            urls=Path(tmp)/"urls.txt";urls.write_text(scrape.BASE+"/w/A\n")
            out=Path(tmp)/"out";html=(FIXTURES/"ordinary.html").read_text()
            with patch.object(scrape.Fetcher,"init_robots"),patch.object(scrape.Fetcher,"fetch",side_effect=scrape.FetchError("fixture denial")):
                self.assertEqual(scrape.run(self.args(out,urls=str(urls))),2)
            with patch.object(scrape.Fetcher,"init_robots"),patch.object(scrape.Fetcher,"fetch",return_value=(html,{"fetched_at":"fixture"})) as get:
                self.assertEqual(scrape.run(self.args(out,urls=str(urls),retry_errors=True)),0)
                self.assertEqual(get.call_count,1)
                self.assertEqual(scrape.run(self.args(out,urls=str(urls),refresh=True)),0)
                self.assertEqual(get.call_count,2)

    def test_hash_checked_cache_and_refresh(self):
        with tempfile.TemporaryDirectory() as tmp:
            f=scrape.Fetcher(tmp);f.robot=RobotFileParser();f.robot.parse(["User-agent: *","Disallow:"])
            html=(FIXTURES/"ordinary.html").read_text()
            with patch.object(f,"_get",return_value=html) as get:
                f.fetch(scrape.BASE+"/w/A");f.fetch(scrape.BASE+"/w/A")
                self.assertEqual(get.call_count,1)
                f.refresh=True;f.fetch(scrape.BASE+"/w/A")
                self.assertEqual(get.call_count,2)

    def test_interrupt_keeps_in_flight_page_for_resume(self):
        with tempfile.TemporaryDirectory() as tmp:
            urls=Path(tmp)/"urls.txt";urls.write_text(scrape.BASE+"/w/A\n")
            out=Path(tmp)/"out"
            with patch.object(scrape.Fetcher,"init_robots"),patch.object(scrape.Fetcher,"fetch",side_effect=KeyboardInterrupt):
                self.assertEqual(scrape.run(self.args(out,urls=str(urls))),2)
            state=json.loads((out/"crawl_state.json").read_text())
            self.assertEqual(len(state["queue"]),1)
            self.assertEqual(state["done"],[])

    def test_scope_change_cannot_mix_catalogs(self):
        with tempfile.TemporaryDirectory() as tmp:
            scrape.run(self.args(tmp,offline_manifest=str(FIXTURES/"manifest.json")))
            self.assertEqual(scrape.run(self.args(tmp,discover_all=True)),2)


if __name__ == "__main__":unittest.main()
