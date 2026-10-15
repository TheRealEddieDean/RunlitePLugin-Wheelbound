# Ranked Challenge purchases and shared target ownership

Authority: WB-D150 delegated2026-10-15, WB-D124/142 preserved history and WB-D147–149 current ranks/controls/offers. This is documentation of required product behavior; **v2 purchasable content and prices are not authored or activated**.

## Products and entitlement graph

| Product | Required ownership | Granted once | Does not grant |
| --- | --- | --- | --- |
| Lesser Challenge entry | Ordinary Bossing Lesser AND Greater | Lesser rank, reviewed versioned initial target bundle, explicit Lesser enable | A normal boss unlock, ready pool, other ranks |
| Standard Challenge | Lesser Challenge | Standard rank | Another starter bundle, automatic enable, new source access |
| Greater Challenge | Standard Challenge | Greater rank | Another starter bundle, automatic enable, new source access |
| Superior Challenge | Greater Challenge | Superior rank | Another starter bundle, automatic enable, new source access |
| Individual Challenge source expansion | Existing card/target-branch purchase rules and reviewed product | Canonical target entitlement | Any rank, normal boss permission or missing game requirement |

A target entitlement is shared across all reviewed rank contexts for that canonical source. Items at several rarities do not require repurchasing the source. A source with multiple accepted NPC forms still has one entitlement. Different source keys do not merge because they share an item; grouping needs explicit source evidence. Approved reuse of existing normal boss ownership requires explicit catalog mapping; never infer identity from labels. Challenge-only grants do not modify normal L/S/G unlocks or eligible roster.

## Purchase preview

Show permanent prerequisites, exact frozen price when authored, newly granted rank, full named initial bundle for first purchase, ownership-versus-enabled distinction, actual eligible count for the selected rank and specific unavailable reasons. A first purchase confirms Lesser enable; a later purchase keeps existing enabled ranks and offers an explicit opt-in only when the settings boundary allows it. Show that further source unlocks/actual game requirements may be needed before offers appear.

Underfloor ownership is allowed: fewer than three actual eligible keys is an offering hold, not a new purchase prohibition or refund entitlement. However an incomplete product payload, missing reviewed bundle, unsupported price/quote or unknown account/balance is not a purchasable runtime product. Display “content/quote unavailable” rather than simulate a free purchase or silently select candidates from the drop table. Do not display a fake0 FP price.

The first combined purchase/enabling requires no active assignment, obligation, pending offer or unresolved selection under WB-D148. A later rank purchase without an enabled-setting change follows existing legal shop interaction; current offers remain frozen. Owning the predecessor suffices even if that predecessor is disabled or currently belowfloor.

## Atomic purchase requirements

Freeze account/run, operation ID, product version, rank/target grants, prerequisite version, price/FP revision and explicit enabled-setting action before confirmation. Revalidate prerequisites, affordability and required boundary before committing. Record debit, rank grant, bundle grant and enabled settings together so restart cannot leave a charged card without its included bundle. Duplicate acknowledgement restores the same receipt, never another debit or grant. Unknown state blocks a new mutation; it is not a successful purchase or automatically refunded one.

Failure cases: wrong Skilling prerequisite, skipped rank, missing content/price, changed account, stale affordability or illegal settings edit must leave ownership/FP/settings untouched. An already owned product is displayed owned; replaying the original purchase returns its receipt rather than charging again. This is a future transaction contract, not proven persistence.

## Initial bundle remains an explicit content deliverable

The historical WB-D142 bundle contains Brine rat, Turoth, Cave horror, Sulphur Nagua, Kurask and Skeletal Wyvern. The complete historical44-hunt set now maps29 Greater/15 Superior and zero Lesser/Standard. Those historical grants and FP receipts remain byte-identical in v1. They must not be called a functioning Lesser starter bundle.

The v2 initial bundle must name canonical target keys, supported Lesser contexts and exact grant provenance, with reviewed source/access/item/receipt/personal probability/reward and price packet. Actual account access may still leave fewer than three eligible keys. No exact roster or guarantee is invented here; A67/A68 candidates are not admitted sources. Higher rank content can use owned targets with separately reviewed qualifying items. Additional target purchases follow one shared entitlement branch, not four duplicated shops.

## Legacy data and later engineering

No current production Wheelbound implementation exists or is authorized by this work. For future legacy design/save fixtures, retain the original single-card version and grant receipts; require an explicit reviewed mapping. Do not grant all ranks, revoke bundle targets, charge again or rewrite an already committed hunt merely because schema names changed. Existing exact committed item/source/reward/context evidence is protected, and A56 invalidation/reconciliation remains independent.

A73 will exercise synthetic entitlement/purchase invariants and list remaining content/client gates. These checks cannot certify a real starter roster, balance, RuneLite API behavior or durable filesystem transactions.
