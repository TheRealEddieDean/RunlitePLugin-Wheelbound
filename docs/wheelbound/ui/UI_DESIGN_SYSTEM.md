# Restrained stone and parchment design system

Status: Category A reusable foundations; final screen composition is Category B. Preserve WheelStyle's existing Master rendering and gold constant (219,180,94). Do not apply a new global Swing LookAndFeel.

| Token | Proposed value | Use |
| --- | --- | --- |
| canvas | #202322 | Tree field and window body |
| surface | #2C302E | Raised panels |
| inset | #171A19 | Recessed counters/inputs |
| border | #707266 | 1 logical-unit stone/iron edge |
| text | #E1DDCF | Primary body |
| secondary | #B9B6A9 | Supporting copy |
| gold | #DBB45E | Selected/purchased |
| blue | #82B8CE | Available path / Bossing accent |
| warning | #E2B979 | Caution |
| error | #EEA19A | Error/debt |
| disabledText | #A4A79E | Disabled label, with explicit reason |
| parchment | #D0C2A0 on #28251F | Optional compact detail sheet; keep long copy on dark surfaces |

Use RuneLite FontManager's default/default-bold for body and existing RuneScape fonts sparingly in wheel labels/headings. Body target 13 logical units, never below 12; headings 16–18; resource numbers 16. Inherit host scaling rather than multiplying pixel dimensions by monitor DPI twice. Check actual font metrics/localization. Spacing scale 4/8/12/16/24; buttons ≥28 high, target 32, focus outline 2. Quiet bevels use two flat strokes; avoid giant textures, glow, blur or custom web fonts. Stone means geometry/border treatment, not large photographic bitmaps.

State never relies on color alone. Purchased = check + gold solid frame; available = open diamond + light outline; unaffordable = coin label + balance shortfall; locked = lock + requirement text; unsupported = warning + evidence reason; pending = spinner/text with disabled action. Selection uses outer white focus rectangle independently of ownership. Hover lightens a border; no game action from hover. Disabled tooltips and an always-readable detail reason remain reachable by keyboard. Validate ≥4.5:1 body contrast and ≥3:1 meaningful control outlines against each actual surface; adjust decorative borders where needed without altering Master.

| Reusable component (proposed) | Contract |
| --- | --- |
| WbButton | Action + label + enabledReason; pending handling stays in presenter, no game reads |
| SectionHeader | title, optional count/action; scales/wraps |
| ResourceStrip | optional FP/debt/spins, accessibility text; unknown distinct from zero |
| ObjectiveCard | immutable objective, progress/restrictions/evidence state, permitted actions |
| NodeGlyph | identity, kind and independent ownership/availability/focus flags; paint only |
| FateCardView | type/rank, pre-target rule/reward preview or frozen target, select intent |
| WbTooltip | escaped plain text/limited safe markup, bounded screen position, keyboard equivalent |
| ProgressView | current/required/unit plus evidence status, never derives completion rewards |
| StatusBadge | icon + text + detail reason |
| HubNavigation | route selection/back, keyboard names, no window ownership |
| ConfirmationSheet | consequence snapshot + cancel/confirm; no Enter default for destructive action |
| ScrollSection | vertical scroll + width wrapping, no nested scroll traps |
| ErrorPanel | typed reason, safe retry/detail, no raw stack trace/private account hash |

Reuse existing BossChecklist escaping and sprite provider patterns after review. No asset network downloads during painting. Decode classpath resources once using getResourceAsStream, bound caches by stable key/size and invalidate on shutdown. Documentation reference art never ships as a pre-rendered interactive wheel. Keep icons locally sourced/licensed with provenance; listing-icon limits are distinct from runtime assets, as GF4 spec explains.

Keyboard: Tab follows navigation → controls → content → inspector. Tree arrow keys select nearest directional node; Enter opens detail, explicit Unlock button commits after confirmation; +/- zoom, Home fits, Escape closes tooltip/drawer before window. Search uses local focused-window bindings only. Accessible list/table alternative exposes every node, status and requirements to assistive tech; custom painted nodes alone are insufficient.

Reduced motion skips path travel/confetti without changing outcomes. Tooltips clamp within owner work area. Error handling retains selected entity and explains retry; an uncertain save never reports success. Confirmations name exact product, reviewed price, resulting balance, irreversible effects and stale-state handling. No invented FP amounts in design previews.
