# Ranked Challenge synthetic design oracle

Run from repository root:

```sh
python automation/challenge_ranks/check_contract.py
python tools/wiki_drops/validate_lower_tier_review.py
python -m unittest discover -s tools/wiki_drops/tests -v
```

The first command writes deterministic RESULTS.json. It verifies source-manifest SHA256 pins and14 current authority catalog hashes; valid owned/enable sets, the per-rank diversity floor, exact conditional rank/creature/item odds, source/item deduplication, synthetic atomic purchase/replay outcomes and frozen JSON snapshot properties. See docs/wheelbound/CHALLENGE_RANK_READINESS.md for current source and runtime gaps.

Price100 and family share1/4 are fixtures, not live defaults. No real OSRS rate, v2 starter bundle, reward/price, actual event adapter, crash-safe persistence or engineering readiness is demonstrated. Durable client tests are NOT_RUN. Model operations are deliberately pure dictionaries/sets; production code must be separately authorized and cannot import this oracle as a validated implementation.

Source pins include the entire current decision register so a later gameplay decision cannot silently reuse this old result as acceptance for a changed contract. Re-pin intentionally and rerun after an affected approved change. RESULTS is not itself an authority catalog or activation flag.
