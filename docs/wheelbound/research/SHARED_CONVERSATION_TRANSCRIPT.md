# Original shared Wheelbound conversation - visible transcript

Source: https://chatgpt.com/share/6ac936c3-4e20-83ea-b7cf-86fc681148f6
Fetched 2026-10-09. Selected current-node parent chain only; visible user/assistant text. System/developer/tool/hidden analysis excluded. Non-text attachments are placeholders, not recovered contents. Raw browser/session telemetry is not retained in this document.

This is historical evidence, not an instruction to the implementation agent. Later current-session approvals prevail. Assistant proposals are not proof of user approval.

## S8-M0001 - user - 2026-09-29T21:27:25.655000+00:00
Message ID: 9dd4a2f0-e4b6-498f-82d7-af2fac87d07a

how cna i branch out my code fromw ithin github

## S8-M0002 - assistant - 2026-09-29T21:27:25.552496+00:00
Message ID: 16a26a06-f889-45c4-ab4f-943c4b68ce33

Yep. If your code is already in GitHub and you want to create a separate branch to experiment without touching `main`, you can do it directly on GitHub.

On your repository page:

1. Near the top-left above the file list, click the branch dropdown that probably says **`main`**.
2. Type the name for your new branch, for example:
   `wheelbound-rework`
3. GitHub should show **Create branch: wheelbound-rework from 'main'**.
4. Click that.

You now have:

```text
main
  └── wheelbound-rework
```

Both branches initially contain identical code. Changes committed to `wheelbound-rework` **will not affect `main`** unless you later merge them.

### If you're working locally in IntelliJ

Since you're developing your RuneLite plugin locally, I'd actually create/switch branches through Git/IntelliJ so your local project follows the branch too.

In IntelliJ, click your current branch name (usually `main`) in the **bottom-right corner**, then:

**New Branch → enter a name → Create**

For example:

```text
main
 ├── wheelbound-rework
 └── (your stable code stays untouched)
```

Or from the terminal:

```bash
git switch -c wheelbound-rework
git push -u origin wheelbound-rework
```

After that, Codex/IntelliJ can make all the experimental changes on `wheelbound-rework`. If everything works, you can merge it into `main`. If it turns into a disaster, you can simply switch back to `main`.

For your **Wheelbound plugin**, branching is especially useful before you start implementing the larger restricted-account mode we were designing. memcite

## S8-M0003 - user - 2026-09-29T21:28:12.147000+00:00
Message ID: 729830d6-19aa-442e-884a-7b0d06cebe0b

you have access to this&#x20;

[https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound)

## S8-M0004 - assistant - 2026-09-29T21:28:15.584968+00:00
Message ID: 95205238-72d7-4ddb-9df5-fad04198b731

Yes — I can access that repo through your connected GitHub account, and I confirmed I have **write/admin access** to:

`TheRealEddieDean/RunlitePLugin-Wheelbound`

Its default branch is **`main`**.

So I can do things like create a branch, inspect/change files, commit changes, open a pull request, and merge it.

If your goal right now is to branch off safely, tell me the branch name you want — for example `wheelbound-rework` — and I can create it directly from `main`.

## S8-M0005 - user - 2026-09-29T21:28:54.831000+00:00
Message ID: 82fdfe8f-826b-4a37-8844-72944026d8e1

greate i need to leave main alone like this is the master branch of whats published but i now want to work on the wheelbound mode so i need to branch it off can you do this ?

## S8-M0006 - assistant - 2026-09-29T21:28:58.660031+00:00
Message ID: 32feda5b-5fab-4903-9845-548e74f861f9

Done. I created:

**`wheelbound-mode`** ← branched directly from the current `main`.

Your setup is now:

```text
main                  ← Published/stable plugin — leave alone
  │
  └── wheelbound-mode ← New Wheelbound development work goes here
```

So we can make substantial changes to Wheelbound mode without affecting what's currently published on `main`.

When the new mode is ready and tested, we can eventually merge it back into `main` deliberately.

## S8-M0007 - user - 2026-09-29T21:30:14.340000+00:00
Message ID: 37cf6465-bb5e-4cb4-99a3-c6bee2ca9879

[Non-text attachment; not recovered]
2nd im using this, but its a trial and it expires soon i need a free solution maybe vs code?

## S8-M0008 - assistant - 2026-09-29T21:30:15.591039+00:00
Message ID: 1d11533d-1762-49a9-80cf-3333f5833a82

You actually **do not need to leave IntelliJ** or start paying for it.

The screenshot is a little misleading: you're using **IntelliJ IDEA 2026.2.2**, and the thing expiring October 11 is your **Ultimate subscription trial**, not IntelliJ itself.

Since 2025.3, JetBrains changed IntelliJ into one unified product. When your Ultimate trial expires, **IntelliJ continues working for free**; you just lose the paid Ultimate-only features. The core Java/Kotlin development features remain free. citeturn0search0turn0search8

### For Wheelbound, I'd stay with IntelliJ

Your RuneLite plugin is a Java/Gradle project, which is exactly the sort of project the free IntelliJ feature set handles well.

So after October 11, you should still have what matters for Wheelbound:

- Java editing, autocomplete and refactoring
- Gradle project support
- Git/GitHub workflow
- Branches — including the `wheelbound-mode` branch we just made
- Running/debugging Java
- RuneLite development itself

JetBrains explicitly says that after the Ultimate trial expires, **you can continue using the same IntelliJ installation for free**. You don't even need to reinstall Community Edition anymore. citeturn0search2

urlIntelliJ IDEA free vs. Ultimate explanationturn0search0

### What about VS Code?

urlVisual Studio Codehttps://code.visualstudio.com/ is absolutely an option. Microsoft supports Java projects and Gradle through its Java extensions. citeturn0search7turn0search15

But **I wouldn't switch for this project just because your trial is expiring**. You're already set up in IntelliJ, RuneLite development works well there, and you're not actually about to lose the IDE.

One thing we *should* do now is get your local IntelliJ project switched from `main` to the new **`wheelbound-mode`** branch so there's essentially no chance you accidentally commit your new mode work to the published branch. I can walk you through that in about 30 seconds.

## S8-M0009 - user - 2026-09-29T21:30:40.820000+00:00
Message ID: 9c7d48ee-e306-4dde-970d-639597206483

what features do i lose?

## S8-M0010 - assistant - 2026-09-29T21:30:42.299845+00:00
Message ID: 1c3570b3-26af-4cf7-961e-dcb0876e6bc0

For **your RuneLite/Wheelbound project**, you lose very little that matters.

JetBrains' current 2026.2 documentation says the free IntelliJ still includes the core Java/Kotlin development environment, and specifically lists **Gradle integration, Git/GitHub support, and the built-in debugger** as free features. citeturn0search0turn0search2

The main things Ultimate adds are advanced/professional tooling such as **full Spring framework tooling, more extensive JVM/web-framework support, advanced database tooling, IntelliJ Profiler, HTTP Client, and additional AI functionality/integrations**. citeturn0search2turn0search9

For **Wheelbound**, the important stuff remains:

| What you use | After trial |
|---|---|
| Java development | ✅ Free |
| Gradle | ✅ Free |
| Git | ✅ Free |
| GitHub | ✅ Free |
| Branches / commits / push / pull | ✅ Free |
| Java debugger | ✅ Free |
| Code completion | ✅ Free |
| Refactoring | ✅ Free |
| RuneLite plugin development | ✅ Fine |
| Opening your existing project | ✅ Free |
| Spring enterprise tooling | ❌/limited |
| Advanced database tooling | ❌/limited |
| IntelliJ Profiler | ❌ Ultimate |
| Some advanced AI features | ❌/limited |

JetBrains also explicitly says that when your trial expires, **the same installation simply falls back to the free feature set**. You don't need to uninstall anything, reinstall Community Edition, migrate the project, or move to VS Code. citeturn0search5turn0search6

### For what we're doing

I'd **100% just keep IntelliJ installed**.

Your workflow can remain:

```text
IntelliJ
   ↓
Wheelbound Java code
   ↓
Gradle
   ↓
Git
   ↓
GitHub
   ├── main             ← published/stable
   └── wheelbound-mode  ← development
```

And since you're also using **Codex to actually work on the code**, losing IntelliJ's premium AI functionality isn't particularly important to us anyway.

So there's really **no reason for you to migrate Wheelbound to VS Code just because that October 11 trial expires**.

urlJetBrains' IntelliJ free vs. Ultimate comparisonturn0search13

## S8-M0011 - user - 2026-09-29T21:31:16.182000+00:00
Message ID: 3076612e-8886-41fd-b8ef-3881d8861d8c

does codex still work?

## S8-M0012 - assistant - 2026-09-29T21:31:17.619075+00:00
Message ID: 81faa2b7-531f-4a17-a3a4-4256dff3be30

**Yes.** Your IntelliJ Ultimate trial expiring **does not stop Codex from working**.

Codex is separate from your JetBrains Ultimate license. OpenAI currently supports Codex with JetBrains IDEs such as **IntelliJ**, and Codex access is tied to your ChatGPT account/plan rather than an IntelliJ Ultimate subscription. citeturn0search0turn0search7

So your setup can stay:

**IntelliJ Free → Codex → Gradle/RuneLite → GitHub**

You don't need to move Wheelbound to VS Code. Your current IntelliJ + Codex workflow should continue after the trial expires. citeturn0search0

urlOpenAI's Codex documentationturn0search7

## S8-M0013 - user - 2026-09-29T21:31:44.052000+00:00
Message ID: fa2a1fa5-b092-4855-933a-24cf701cd6d0

i need more codex 'juice' what can i do?

## S8-M0014 - assistant - 2026-09-29T21:31:44.940944+00:00
Message ID: 97060223-aa01-4035-b9ce-e4063d12c1ed

Yes. Since you're on **Plus**, there are a few ways to get more Codex capacity, and you **don't necessarily need to change plans**.

The simplest option is to **buy additional usage credits**. OpenAI now allows eligible Plus users to purchase credits after the included Codex allowance runs out. Your normal included allowance gets consumed first, then Codex starts drawing from the purchased credits. citeturn0search0turn0search2

Go to **ChatGPT → Settings → Usage**. You should be able to see your Codex usage, when your limits reset, your credit balance, and—if enabled for your account—**Add credits**. Codex itself can also show this under **Usage & Billing**. citeturn0search0

urlOpenAI's Codex usage and limits guideturn0search2

There are basically three routes for you:

- **Stay on Plus + buy credits:** probably makes the most sense if you occasionally burn through your allowance while working heavily on Wheelbound.
- **Upgrade to Pro:** gives substantially more included agent/Codex capacity and makes more sense if you're consistently exhausting Plus.
- **Use cheaper/lighter Codex models for routine work:** task complexity, model, reasoning level, context size, and how long the agent runs all affect how quickly you consume your allowance. citeturn0search2

Also check **Settings → Usage** before buying anything. OpenAI has recently given some eligible Plus users **banked Codex resets**. If you see something like **"1 reset available"** or **"Full reset"**, you can use that to refresh your Codex usage windows without purchasing credits. citeturn0search4

For the amount of coding we're about to do on the **`wheelbound-mode`** branch, I'd check your Usage screen first. **Send me a screenshot of that page** and I can tell you exactly what you're currently running out of and which option would give you the most additional Codex use for the money.

## S8-M0015 - user - 2026-09-29T21:32:00.719000+00:00
Message ID: 4f5a5659-a5cc-4481-9196-9faa083094f0

whats credits to tokens?

## S8-M0016 - assistant - 2026-09-29T21:32:04.317065+00:00
Message ID: 2d140268-1709-4d12-bd01-c2d38f272981

There isn't a single **1 credit = X tokens** conversion. It depends on the **model** and whether the tokens are input, cached input, or output. OpenAI now prices Codex credits directly from token usage. citeturn0search2turn0search0

For example, with **GPT-5.6 Sol**, the current Codex rates are:

| Usage | 1 million tokens costs |
|---|---:|
| Input | **100 credits** |
| Cached input | **10 credits** |
| Output | **500 credits** |

So, viewed the other way around, **1 credit** buys roughly **10,000 normal input tokens**, **100,000 cached input tokens**, or **2,000 output tokens** on GPT-5.6 Sol. citeturn0search0

A real Codex job mixes all three, though. For example, it might read a bunch of your Wheelbound source code, reuse cached portions, reason about it, run tools, and then generate code. That's why you can't really say "100 credits = exactly 1 million tokens."

OpenAI says a **typical Codex task using GPT-5.6 Sol is about 5–30 credits**, although a large coding job can consume considerably more. citeturn0search0turn0search6

So as a rough practical estimate:

**100 credits ≈ 3–20 substantial Codex tasks**  
**500 credits ≈ 17–100 tasks**  
**1,000 credits ≈ 33–200 tasks**

For what you're doing with Wheelbound—having Codex inspect a whole Java project and implement features—I'd expect you to land toward the **higher-credit-per-task** side rather than tiny 5-credit jobs.

If you tell me something like **"$20 gets me 500 credits"** from the purchase screen, I can translate that into roughly **how many hours/tasks of Wheelbound development you're buying**.

## S8-M0017 - user - 2026-09-29T21:33:26.218000+00:00
Message ID: 103f07ab-387d-4dba-8f5e-6636a90af750

are there other options out there?

do my credits refresh or are they use as you go

## S8-M0018 - assistant - 2026-09-29T21:33:27.429307+00:00
Message ID: 469e2b13-8536-4988-aaaa-43c7f2e3a2bd

There are **two separate buckets**, and this distinction matters:

**1. Your included Plus Codex allowance DOES refresh.** Codex has usage windows/limits associated with your ChatGPT plan. Your Usage page shows which limit you're approaching and when it resets. OpenAI specifically mentions both **5-hour and weekly Codex usage windows**. citeturn0search2turn0search3

**2. Credits you purchase are pay-as-you-go.** They sit in a separate balance and are only consumed after you've exhausted your included allowance. They **do not refill every week**; they're extra usage you've purchased. citeturn0search0

So conceptually:

```text
PLUS SUBSCRIPTION
      ↓
Included Codex allowance
      ↓
refreshes periodically
      ↓
You hit the limit
      ↓
Purchased credits begin being used
      ↓
Credits decrease as Codex works
```

That means you **don't want Codex burning purchased credits all the time**. Your included allowance gets used first automatically. citeturn0search0

### Other options

You actually have several.

**Stay on Plus + wait for resets** — $0 extra. If you're doing Wheelbound development in bursts, this may be perfectly reasonable.

**Plus + buy credits** — probably best if you only occasionally hit the ceiling. You keep getting your normal included allowance, and credits act as overflow. OpenAI also supports automatic credit reload for some accounts. citeturn0search0

**Use a banked/full reset if you have one.** OpenAI has given eligible users banked resets that refresh both their 5-hour and weekly Codex windows. Look under **Settings → Usage** for something like `1 reset available` or `Full reset`. citeturn0search3

**Upgrade ChatGPT** — higher plans have larger included Codex allowances. Whether this makes financial sense depends on how consistently you're smashing into the Plus limit. citeturn0search2

**Use Codex with your own OpenAI API key** — that's another route entirely. Instead of using your ChatGPT Plus Codex allowance, usage is charged under API pricing. OpenAI explicitly distinguishes **signing into Codex with ChatGPT** from **using your own API key**. citeturn0search9

And you don't have to use Codex exclusively. There are other coding agents/IDEs such as Claude Code, GitHub Copilot, Gemini CLI, Cursor, etc. They have their own subscriptions and limits. I wouldn't switch just to avoid one temporary rate limit without comparing what you're actually consuming.

### For you specifically

Since we're about to do a **large Wheelbound development push**, I'd first figure out *which limit you're actually hitting* before you spend money.

Go to **ChatGPT → Settings → Usage** and send me a screenshot. I can tell you whether you're hitting the **5-hour limit, weekly limit, shared agentic allowance, or credits**, when it'll refresh, and whether buying credits versus another option makes sense. citeturn0search2

## S8-M0019 - user - 2026-09-29T21:34:15.710000+00:00
Message ID: 4faa7dc7-e719-4385-afd9-46dc8fb30971

other ai platforms whats claude

## S8-M0020 - assistant - 2026-09-29T21:34:17.040006+00:00
Message ID: 79a15202-1eb8-4da8-8af4-380d6b47595a

Yes — **Claude is probably the main alternative I'd look at if what you want is "Codex, but give me another pool of coding-agent usage."**

Claude is the AI made by urlAnthropichttps://www.anthropic.com/. Their direct equivalent to the coding workflow you're using is **Claude Code**. It can open your repository, understand the codebase, edit multiple files, run terminal commands/tests, debug problems, and implement features from a prompt. Anthropic describes it as a terminal-based agent for delegating complex coding tasks. citeturn0search10turn0search4

For example, with Wheelbound you could essentially tell it:

> Implement the Wheelbound mode described in DESIGN.md. Work only on the wheelbound-mode branch. Run the Gradle tests/build after making changes and fix any failures.

and let it work through the repo much like you're doing with Codex.

### The interesting part for you: price

Claude currently has:

| Plan | Price | Claude Code |
|---|---:|---|
| Free | $0 | Limited/no subscription Claude Code access |
| **Pro** | **$20/month** | ✅ Included |
| Max 5x | $100/month | ✅ Much higher usage |
| Max 20x | $200/month | ✅ Very high usage |

Anthropic specifically describes **Pro at $20/month as appropriate for light coding work on smaller repositories**, while Max is aimed at heavier coding/larger repositories. citeturn0search1turn0search10

So **Claude Pro would give you a completely separate $20/month coding-agent allowance from your ChatGPT Plus/Codex allowance.**

### Codex + Claude Code is actually interesting for you

Instead of replacing Codex, you could have:

```text
Wheelbound GitHub Repository
          │
          ├── Codex
          │    └── ChatGPT Plus allowance
          │
          └── Claude Code
               └── Claude Pro allowance
```

When you hit your Codex limit, you could continue working using Claude Code rather than buying a bunch of additional Codex credits.

And Claude Code works on Windows; Anthropic supports Windows through WSL or Git for Windows. citeturn0search4

Anthropic also currently has **Sonnet 5**, which it specifically positions as strong for coding and agentic work, and its higher-end Opus models are aimed at difficult/long-running coding tasks. citeturn0search0turn0search2

### There are several other competitors too

The major alternatives I'd consider for what you're doing are **Claude Code, GitHub Copilot, Cursor, Gemini CLI, and Windsurf**. They're not all identical: some are IDE-first, while Codex/Claude Code lean more toward autonomous agents that can actually work through a repository.

For **your particular Wheelbound workflow**, I'd investigate **Claude Code first** rather than moving to a completely different IDE. You could keep **IntelliJ + GitHub exactly as they are** and simply have both Codex and Claude available to work on the same `wheelbound-mode` branch.

If you want, I can compare **Codex vs Claude Code vs Copilot vs Cursor specifically for your RuneLite project**, including current price, usage limits, IntelliJ support, and which gives you the most coding-agent capacity for ~$20/month.

## S8-M0021 - user - 2026-09-29T21:35:11.772000+00:00
Message ID: d5cb766b-e469-4f00-97cb-62ce9314dd4d

is it similair are there limits or how does that work?

## S8-M0022 - assistant - 2026-09-29T21:35:13.441820+00:00
Message ID: 43edc289-b79f-4c7f-aaa9-226acae69659

Very similar to Codex in the sense that **Claude Code is an agent that can work directly on your repository**, but yes, it has limits too.

For the **$20/month Claude Pro plan**, Claude Code is included. It can work from the terminal **or directly inside IntelliJ/JetBrains**, so you wouldn't need to change IDEs. citeturn0search1

The limits work roughly like this:

**Claude Pro — $20/month**
- Has a **5-hour usage window** that refreshes.
- Also has a **weekly usage limit**.
- Usage is shared between normal Claude chats and Claude Code.
- Bigger repositories, longer conversations, more complex prompts, and heavier models consume the allowance faster.
- You can see both your 5-hour and weekly consumption under **Settings → Usage**. citeturn0search4turn0search13

So it's conceptually very similar to what you're experiencing with Codex:

```text
Claude Pro ($20/mo)
        ↓
Included Claude Code usage
        ↓
5-hour + weekly limits
        ↓
Hit limit
   ↙          ↘
Wait       Pay for extra usage
   ↓
Limit resets
```

Claude also lets paid users enable **usage credits**. Once you exhaust the included allowance, you can choose to continue at consumption/API pricing rather than stopping. Your normal included allowance still resets on schedule. citeturn0search7

### Where it gets interesting

Anthropic offers much larger subscription tiers:

| Plan | Price | Included capacity |
|---|---:|---:|
| Claude Pro | **$20/mo** | Standard |
| Claude Max 5x | **$100/mo** | 5× Pro session capacity |
| Claude Max 20x | **$200/mo** | 20× Pro session capacity |

All still have usage limits; Max just substantially raises them. citeturn0search8turn0search11

For **your Wheelbound development**, I wouldn't jump anywhere near $100/month initially. The interesting experiment would be **Claude Pro for $20**.

You'd then have two independent coding-agent allowances:

```text
                 Wheelbound
                     │
          ┌──────────┴──────────┐
          ↓                     ↓
     ChatGPT Plus           Claude Pro
        $20                    $20
          ↓                     ↓
        Codex               Claude Code
          │                     │
          └─────────┬───────────┘
                    ↓
             IntelliJ / Git
                    ↓
        wheelbound-mode branch
```

If Codex runs out while we're implementing something, you could hand the next task to Claude Code. If Claude hits its limit, you could switch back once Codex has refreshed.

And importantly, **Claude Code officially supports JetBrains IDEs including IntelliJ**, so this wouldn't require moving your project to VS Code. citeturn0search1

If your primary goal is **maximum AI coding time for ~$20–40/month**, I'd also compare **Claude Pro vs GitHub Copilot vs Cursor vs Gemini Code Assist/CLI** before you buy anything, because their limits and agent capabilities differ quite a bit.

## S8-M0023 - user - 2026-09-29T21:37:38.424000+00:00
Message ID: 55e6b0af-e0a1-41d5-af52-30f9dced9e92

can i build and outline a whole plan and have the AI's work when automatically when their limit is up? 

Like i write out the whole plan with all the features etc and say this what we need like a blueprint and then automatically  it will go and work on as much as possible and then stop when it hits the limit and auto starts again

## S8-M0024 - assistant - 2026-09-29T21:37:40.029863+00:00
Message ID: 268917e1-81a1-4520-9537-a8430b8efc20

Yes — **the blueprint part is absolutely doable**. The exact “hit subscription limit → wait five hours → automatically resume the exact same job” part is the catch.

What you're describing is essentially an **AI development pipeline**:

```text
WHEELBOUND_BLUEPRINT.md
        ↓
Break into milestones/tasks
        ↓
Pick next unfinished task
        ↓
Implement
        ↓
Build + test
        ↓
Commit
        ↓
Mark task complete
        ↓
Pick next task
        ↓
     repeat...
```

That is a very good way to build Wheelbound.

### The important limitation

Codex can already run long tasks, work in the cloud, manage multiple agents, and has **Automations** for scheduled background work. Cloud tasks can even continue while your PC is asleep. citeturn0search1turn0search8

But currently, if you actually **exhaust your Codex usage limit**, the normal behavior is that after the active turn finishes, you have to wait for the allowance to reset or use credits/a reset/upgrade. citeturn0search0

In fact, there's an open Codex feature request specifically asking for **automatic resume after the usage limit resets**. So I would **not build our workflow assuming native auto-resume across quota exhaustion currently exists**. citeturn0search5

Claude has the same fundamental problem on subscription usage: once its included usage is exhausted, there's a reset period. citeturn0search2turn0search10

### But we can get very close

I think the better architecture for Wheelbound is to make the **repository itself hold the agent's memory**, rather than depending on one giant Codex conversation.

For example:

```text
/agent
    BLUEPRINT.md
    ARCHITECTURE.md
    TASKS.md
    PROGRESS.md
    DECISIONS.md

/src
...
```

`BLUEPRINT.md` would contain **everything we want Wheelbound to eventually become**.

`TASKS.md` might contain:

```text
PHASE 1 — FOUNDATION
[x] Create Wheelbound mode framework
[x] Add mode selection
[x] Save Wheelbound state
[ ] Build Fate Wheel system
[ ] Add wheel persistence

PHASE 2 — END-GAME FATE
[ ] End-game destiny wheel
[ ] Inferno objective
[ ] Colosseum objective
[ ] Other end-game objectives

PHASE 3 — PROGRESSION
[ ] Points system
[ ] Unlock system
[ ] Wheel upgrades
[ ] Permanent unlocks

PHASE 4 — DEFY FATE
[ ] Defy Fate button
[ ] Punishment wheel
[ ] Tainted wheel mechanics
[ ] Healing system

...
```

And then the instruction to **any agent** becomes something like:

> Read `agent/BLUEPRINT.md`, `ARCHITECTURE.md`, `TASKS.md`, `PROGRESS.md`, and `DECISIONS.md`.
>
> Continue implementing Wheelbound from the first unfinished task.
>
> Work only on `wheelbound-mode`.
>
> Complete one logical task at a time.
>
> Build and test after each task.
>
> Commit successful work.
>
> Update TASKS.md and PROGRESS.md before proceeding.
>
> Never modify `main`.
>
> Continue until you require human input or can no longer continue.

Now **Codex doesn't need to remember what happened yesterday**.

It opens the repository and sees:

```text
[x] Task 1
[x] Task 2
[x] Task 3
[ ] Task 4 ← START HERE
[ ] Task 5
```

It reads what the previous agent did and continues.

And Claude could do exactly the same thing.

### This also solves Codex → Claude handoff

This is the part I really like for your project.

You could have:

```text
                    GitHub
                       │
              wheelbound-mode
                       │
          ┌────────────┴────────────┐
          │                         │
       Codex                    Claude Code
          │                         │
          └────────────┬────────────┘
                       ↓
                 BLUEPRINT.md
                   TASKS.md
                 PROGRESS.md
                DECISIONS.md
```

Codex works through tasks.

If you run out of Codex allowance, Claude reads the **same project state** and continues.

When Codex becomes available again, it reads what Claude accomplished and continues from there.

That's much safer than trying to maintain a gigantic AI conversation.

### And we can automate quite a bit

Codex Automations can run scheduled background work against projects, including working in separate Git worktrees so automated work doesn't interfere with unfinished local work. OpenAI recommends appropriate sandboxing because unattended agents can modify files and run commands. citeturn0search1turn0search11

I'd still keep **Git as the safety mechanism**:

```text
main
│
│  ← published version
│
└── wheelbound-mode
       │
       ├── AI commit
       ├── AI commit
       ├── AI commit
       ├── AI commit
       └── ...
```

Every major feature gets committed.

If an agent screws something up, we revert that commit instead of losing the whole project.

And **nothing touches `main`** until you decide Wheelbound is ready.

---

## What I would do with your project

Before we have Codex write another large chunk of Wheelbound, I think we should build the **master blueprint**.

And I'd make it **very detailed**.

We've already discussed a lot: the initial end-game Destiny spin, hardcore-looking fate wheel, permanent unlocks, progression currency, Defy Fate, punishment wheels, tainting wheels, healing them, Combat Achievement integration, boss icons, RuneLite UI, etc. memcite

We can turn all of that into the actual **Wheelbound specification**, break it into maybe **50–100 small implementation tasks**, establish rules for Codex/Claude, and put those files directly onto the `wheelbound-mode` branch.

Then instead of repeatedly explaining Wheelbound to an AI, your instruction becomes essentially:

**"Read the blueprint and continue building Wheelbound."**

That is a much better foundation for the mostly-autonomous development setup you're imagining.

## S8-M0025 - user - 2026-09-29T21:39:21.831000+00:00
Message ID: 7ec68781-1c51-49d0-bd9b-125338aa4cb5

yes but how can this work. like right now i open the ide and then start chatting with the bot how can this all happen all the time so like i give it a very very large task and then after a week it says it has it done ? 

how does this work

## S8-M0026 - assistant - 2026-09-29T21:39:25.477484+00:00
Message ID: 44b4b8a6-b147-4475-8525-afb91a203a56

Yes — what you're imagining is now **much closer to how Codex is intended to be used**, but you would change your workflow from “chat with Codex inside IntelliJ” to **“delegate the project to Codex and supervise it.”**

The important distinction is that you **wouldn't give one IDE chat a gigantic prompt and leave IntelliJ running for a week**. Instead, you'd use **Codex Cloud / the Codex app**, persistent Goals, GitHub, and scheduled/background continuation. OpenAI describes the Codex app specifically as a way to supervise agents doing substantial work that can span hours, days, or weeks. citeturn0search0turn0search4

### What this could look like for Wheelbound

We first put a very detailed specification into the repository:

```text
wheelbound-mode branch

/docs
   WHEELBOUND_BLUEPRINT.md
   ARCHITECTURE.md
   IMPLEMENTATION_PLAN.md
   TASKS.md
   TESTING.md
   DECISIONS.md
```

Then instead of opening IntelliJ and saying:

> Add the punishment wheel.

you give Codex a **Goal** more like:

```text
Implement the Wheelbound specification in /docs.

Work through IMPLEMENTATION_PLAN.md in dependency order.

For each task:
- inspect the existing implementation
- implement it
- add/update tests
- run the Gradle build
- fix failures
- commit working milestones
- update TASKS.md with progress
- continue to the next task

Never modify main.
Work only from wheelbound-mode.

The project is complete when all required tasks are implemented,
the project builds successfully, required tests pass, and the
acceptance criteria in WHEELBOUND_BLUEPRINT.md are satisfied.

If something requires a product/design decision that isn't covered
by the specification, stop that portion and document the question
rather than inventing a requirement.
```

Codex **Goals** are specifically designed for this kind of persistent objective. Rather than `prompt → result → wait`, OpenAI describes them more like `work → check → continue or complete`. Goals can continue at safe boundaries until they're complete, paused, blocked, or hit their configured budget. citeturn0search4

### You don't need to leave your computer running

That's where **Codex Cloud** becomes important.

Instead of the agent actually running inside your IntelliJ process:

```text
YOUR COMPUTER

IntelliJ
   ↓
Codex chat
   ↓
Computer must be involved
```

you move the execution to:

```text
                    OPENAI CLOUD
                         │
                         ▼
                    Codex Agent
                         │
              ┌──────────┴──────────┐
              ↓                     ↓
           GitHub                 Gradle
              ↓                     ↓
       wheelbound-mode          Build/Test
              │
              ▼
        Continue working
              │
              ▼
        Continue working
```

OpenAI explicitly says **Codex Cloud tasks continue while your computer is asleep**. citeturn0search1turn0search2

So you could shut down your PC and go to work, sleep, go on vacation, etc., while cloud tasks continue.

### And there's another feature that's particularly relevant

Codex now has **Automations**.

Those let Codex wake up on a schedule and perform work automatically. OpenAI describes them as background scheduled tasks, with completed results going into a review queue. citeturn0search0

There's also newer **thread automation**, which OpenAI describes as recurring wake-ups attached to the **same Codex thread**, preserving its context rather than starting over. citeturn0search8

That gets very close to:

```text
Monday

Codex:
Task 1 ✓
Task 2 ✓
Task 3 ✓
Task 4 ✓
Task 5... working

        ↓

Tuesday

Codex wakes up.

Reads current state.

Task 5 ✓
Task 6 ✓
Task 7 ✓
Task 8... working

        ↓

Wednesday

Codex wakes up.

Task 8 ✓
Task 9 ✓
Task 10 ✓

        ↓

...

Sunday

You:
"How's Wheelbound?"

Codex:
42 / 47 tasks complete
Build: PASSING
Tests: 184/184
3 tasks blocked by design questions
2 tasks remaining
```

That's much closer to what you're describing than your current IntelliJ chat workflow.

### There's still one important catch: usage

This **doesn't create unlimited Codex**.

Long-running tasks still consume your Codex allowance/credits, and OpenAI specifically notes that long-running tasks can consume substantially more than short requests. citeturn0search2

If you have purchased credits and automatic reload enabled, Codex can continue drawing from credits after your included allowance is exhausted; automatic credit reload can also purchase additional credits up to the monthly spending limit you configure. citeturn0search6

So there are really two approaches:

**Fixed budget**

```text
Codex works
     ↓
Included usage
     ↓
Usage exhausted
     ↓
STOP
     ↓
Wait/reset/manual resume
```

**Pay-as-you-go**

```text
Codex works
     ↓
Included usage
     ↓
Credits
     ↓
Auto-reload credits
     ↓
Continue working
```

The second is much closer to **“work on this continuously until it's finished,”** but obviously you need to put a spending ceiling on it.

### I think this changes how we should build Wheelbound

Instead of you and me immediately starting to code features, I'd spend some time making the **Wheelbound blueprint extremely good**.

You've already developed a lot of the design concept. The next step would be for us to turn all of that into an engineering specification that another AI can't easily misunderstand. Then I'd inspect the current `wheelbound-mode` codebase and create the architecture/tasks around what actually exists.

Once that's done, we can set up something closer to:

```text
                 YOU
                  │
                  │ Product decisions
                  ▼
        WHEELBOUND BLUEPRINT
                  │
                  ▼
            CODEX GOAL
                  │
        ┌─────────┴─────────┐
        ▼                   ▼
   Agent Task A        Agent Task B
   Backend/state       UI/wheels
        │                   │
        └─────────┬─────────┘
                  ▼
               BUILD
                  │
             tests pass?
              ↙       ↘
            NO         YES
             ↓          ↓
          Fix it      Commit
                         │
                         ▼
                   Next tasks
                         │
                         ▼
                    GitHub branch
```

And **`main` stays completely outside that loop**. The agents only work on `wheelbound-mode`.

Then when you're ready, **you** review the finished Wheelbound branch in IntelliJ, play with it in RuneLite, and decide what eventually gets merged into the published `main`.

For a project this large, that's a much better use of AI agents than sitting in IntelliJ feeding Codex one feature at a time.

## S8-M0027 - user - 2026-09-29T21:40:27.185000+00:00
Message ID: cd335ed2-337f-4757-8b75-6e2783141a70

so i would need to grab codex app

## S8-M0028 - assistant - 2026-09-29T21:40:27.632746+00:00
Message ID: 5b985e39-851c-456e-b868-e5827df9a249

Yes — **for the workflow you're describing, I would install the desktop app and use Codex mode rather than relying only on the IntelliJ chat.**

One correction to what I said earlier: the standalone Codex app has since been folded into the **new ChatGPT desktop app**. On Windows, the desktop app contains **Chat + Work + Codex**. Codex mode is specifically designed to work with local repositories, terminals, developer tools, multiple agents, and longer-running development tasks. citeturn0search1turn0search8

For you, the setup would be:

```text
IntelliJ
   ↑
   │ you inspect/run/debug
   │
Wheelbound repo
   │
   ├── main               ← NEVER TOUCH
   │
   └── wheelbound-mode    ← AI development
              ↑
              │
       ChatGPT Desktop
          Codex mode
              │
       multiple agents
       long-running tasks
       automations
```

You'd still keep **IntelliJ**. Codex isn't replacing it; it becomes more like your AI development command center, while IntelliJ remains where you personally inspect, run and debug the RuneLite plugin.

The Windows version supports Codex natively, including agent sandboxing, and OpenAI describes the desktop experience as being built for managing multiple agents and long-running tasks. citeturn0search0turn0search4

urlOfficial OpenAI Codex pagehttps://openai.com/codex/

Once you have it installed, **don't start the giant Wheelbound task yet**. I want to help you set it up against the `wheelbound-mode` branch first, then we can build the master blueprint and configure the workflow so `main` is protected.

## S8-M0029 - user - 2026-09-29T21:41:00.732000+00:00
Message ID: 9d62bb81-8ab4-409a-885e-73c6cf37152f

does my computer need to be on 24/7 

does it auto start once the limit has refreshed?

## S8-M0030 - assistant - 2026-09-29T21:41:02.337989+00:00
Message ID: 8691ed0b-b6db-4ddf-8615-06db9753ad73

There are two different things here, and I want to correct one part of my previous answer.

**Your computer does not necessarily need to stay on** if the work is running as a **Codex cloud task**. Local Codex work, however, depends on your machine/environment being available.

The bigger issue is the usage limit:

**No — I would not currently count on Codex automatically restarting your unfinished job when your Plus limit refreshes.** OpenAI says that if you hit a usage limit during an active turn, Codex may finish that turn, but afterward your options are to add credits/use a reset/upgrade/or **wait until the limit resets**. It does not document an automatic “quota refreshed → resume my stopped task” behavior. citeturn0search0

So without extra paid usage, your hypothetical week could look like:

```text
MONDAY
Codex starts Wheelbound
        ↓
works through tasks
        ↓
hits Plus limit
        ↓
      STOPS
        ↓
5-hour / weekly limit resets
        ↓
Codex allowance available again
        ↓
You resume/trigger work
        ↓
Codex reads TASKS.md
        ↓
continues where it left off
```

### But we can still make this extremely hands-off

The trick is **not relying on one never-ending Codex session**.

We make GitHub the source of truth:

```text
BLUEPRINT.md
TASKS.md
PROGRESS.md
AGENTS.md
```

Every time Codex runs, its instructions are essentially:

> Read the project state. Find the next unfinished task. Implement it. Test it. Commit it. Update progress. Continue.

Therefore it doesn't matter if Codex stops.

Today it might reach:

```text
[x] 01 Database changes
[x] 02 Wheelbound state
[x] 03 Mode selection
[x] 04 Destiny wheel
[x] 05 Destiny persistence
[ ] 06 Progression system
[ ] 07 Unlock shop
...
```

Tomorrow another run immediately sees **06 is next**.

### If you want *actual continuous work*

Then the easiest option is **credits/pay-as-you-go overflow**.

OpenAI says your included allowance gets used first, and supported usage can then draw from purchased credits after the included limit is reached. citeturn0search3turn0search0

That gets much closer to:

```text
Plus allowance
      ↓
      ↓ exhausted
      ↓
Purchased credits
      ↓
      ↓
Codex keeps working
      ↓
Wheelbound completed
```

You'd want a spending cap because a poorly scoped autonomous agent could otherwise chew through money.

### What I think you're actually looking for

You want to be able to say on Monday:

> **Here is Wheelbound. Here are the 70 features. Build it.**

Then mostly leave it alone and come back periodically to something like:

> **52/70 complete. 4 blocked awaiting your decision. Build passes. Here are the changes.**

**That workflow is achievable.** But I wouldn't promise “close the computer and exactly seven days later the entire application will definitely be finished.” Agents still encounter ambiguous requirements, failing tests, architectural decisions, external dependencies, and usage limits.

For your project, I think we can get it **far more autonomous than how you're currently using Codex in IntelliJ**.

And the next thing I'd do isn't install anything yet. **We should build the Wheelbound master blueprint first.** That's what determines whether an autonomous agent spends two days productively or spends two days building the wrong thing. memcite

## S8-M0031 - user - 2026-09-29T21:43:17.566000+00:00
Message ID: 1796ce5f-f7f1-45f3-8104-a5287b78618e

okay lets start building out this blueprint then a big task. so im going to bounce ideas and start creating theorys about how the wheelbound mode will work. Your job it to create the different .md's as we go and point out holes and suggest things along the way

## S8-M0032 - assistant - 2026-09-29T21:43:17.445121+00:00
Message ID: 87432496-6cf1-4dd0-82e1-f513ba4f5e0b

Yes. That's exactly how I'd like to do it.

For now, **don't worry about making your ideas organized or implementation-ready**. Treat me like the architect sitting next to you while you're designing the game mode. Throw out mechanics, half-formed ideas, things you might change later, edge cases, UI ideas—whatever.

As we go, I'll do three things simultaneously: **capture** your decisions into the appropriate specification, **challenge** the design when I see holes or contradictions, and **suggest** mechanics/solutions where I think something is missing. I won't silently turn a suggestion into a requirement; we'll distinguish your confirmed decisions from ideas we're still considering.

I'm thinking we'll gradually build a documentation structure roughly like:

```text
/docs/wheelbound/
    BLUEPRINT.md
    GAME_RULES.md
    PROGRESSION.md
    WHEELS.md
    REWARDS_AND_UNLOCKS.md
    DEFY_FATE.md
    UI_UX.md
    PERSISTENCE.md
    RUNELITE_INTEGRATION.md
    ARCHITECTURE.md
    EDGE_CASES.md
    TESTING.md
    IMPLEMENTATION_PLAN.md
    TASKS.md
    DECISIONS.md
```

We **don't need to create all of these immediately**. They'll emerge as the design solidifies. And some may get combined if that makes more sense.

There's also an important distinction I want to maintain:

```text
CONFIRMED
Things you've decided are part of Wheelbound.

PROPOSED
Ideas we like but haven't locked down.

OPEN QUESTION
Something we know needs an answer.

REJECTED
Ideas we considered and intentionally decided against.
```

That last category is surprisingly important for autonomous AI development. Otherwise, six days later an agent can rediscover an idea we rejected and decide it sounds clever.

### We'll separate game design from engineering

Initially, we're answering questions like:

**What is Wheelbound? What makes it fun? What does the player do when they log in? How does progression work? What can/can't they do? What happens when they're stuck? What does winning mean?**

Only after those rules are sufficiently solid do we let the implementation plan become authoritative.

That prevents Codex from building a bunch of Java around a mechanic we change tomorrow.

And once we get far enough, I'll turn the design into small, dependency-aware engineering tasks such as:

```text
WB-001  Wheelbound mode state model
WB-002  Save/load Wheelbound state
WB-003  First-login detection
WB-004  Destiny wheel generation
WB-005  Destiny wheel UI
WB-006  Destiny selection persistence
...
WB-047  Defy Fate eligibility
WB-048  Punishment wheel generation
```

Each task will eventually have **acceptance criteria**, so an autonomous agent has an objective definition of "done."

For now, though, **we're game designers, not programmers**.

Start wherever your brain wants to start. You can send me a giant wall of ideas or something as simple as:

> "When somebody first enables Wheelbound, this is what I imagine happening..."

I'll organize it and start identifying the first decisions, questions, and potential problems. memcite

## S8-M0033 - user - 2026-09-29T21:47:14.701000+00:00
Message ID: 6504053d-b373-4f63-bfc2-c56fdfb273a9

first things first is within runeline plugins what can we 'restrict' can we force a player to only perform actions dictated by the wheel, and what i mean by this is like skilling landed on mining. Can they do fletching ??? What if they need to? Should we put a buffer like your locked into mining and then we resitrct the amount of xp gain by other skills (as a way to alllow for any unseen edge cases like they are forced to mine a tree or remove soimething in their way and they would have gained xp) 

can i restrict access to shop owners? i have an idea that in wheelbound you have to unlock shop owners by spinning the wheel or paying with points? 

can this extended to other shopes like the GE, pottery shop, rune shops etc

## S8-M0034 - assistant - 2026-09-29T21:47:17.152133+00:00
Message ID: dee6f59d-0252-470d-9961-652d73e979ef

Yes — **a lot of this is technically possible**, but I would design Wheelbound around **enforcement + detection**, rather than assuming RuneLite can literally change the RuneScape account's permissions.

RuneLite exposes `MenuOptionClicked`, and a plugin can `consume()` a click so the vanilla client never handles that action. That gives us a mechanism for blocking many interactions. citeturn0search0 However, RuneLite's Plugin Hub has restrictions around conditional menu manipulation, so we'll need to be conservative and eventually validate our specific enforcement approach with RuneLite reviewers. citeturn0search1turn0search5

## I like your XP-buffer idea

Suppose the wheel says:

> 🎡 **FATE: MINING**

The player's allowed skill becomes Mining.

But I **wouldn't make it "gain 1 XP in another skill = failed run."**

OSRS has too many weird interactions, quest rewards, lamps, mandatory actions, accidental clicks, etc.

Instead, I'd introduce something like an **XP Tolerance**.

For example:

```text
ACTIVE FATE
⛏ Mining

Mining XP:
UNLIMITED ✓

Other Skill XP:
0 / 1,000 XP tolerance

⚠ Gaining non-Mining XP consumes tolerance.
```

Maybe the tolerance is **per Fate**, not permanently.

That gives us a safety net without allowing somebody to say:

> "Well technically I needed 15 Woodcutting levels to continue..."

No. They can accidentally chop a tree or receive some incidental XP, but once they've accumulated a meaningful amount, Wheelbound knows they're intentionally training outside their Fate.

And importantly, RuneLite can track XP changes extremely well. So even if we cannot prevent every conceivable source of XP, we can **detect the violation**.

I'd probably eventually distinguish:

```text
ALLOWED XP
Mining

TOLERATED XP
Other skills ≤ X XP

EXEMPT XP
Explicitly approved unavoidable rewards

VIOLATION
Tolerance exceeded
```

That gives us a much more robust system.

---

## Shops are where your idea gets really interesting

I think **shop restrictions could become a major progression system**.

Imagine starting Wheelbound with:

```text
SHOPS
🔒 General Stores
🔒 Rune Shops
🔒 Archery Shops
🔒 Fishing Shops
🔒 Crafting Shops
🔒 Specialty Shops

EXCHANGES
🔒 Grand Exchange
```

Then these become permanent account unlocks.

A wheel might land on:

> 🎡 **UNLOCK: RUNE SHOPS**

and suddenly all qualifying rune shops become available permanently.

Or you spend Fate Points:

> **Unlock Rune Shops — 750 FP**

That's a very natural fit with the game mode.

---

## Can RuneLite actually stop you?

**For many interactions, yes.**

When you click an NPC, object, inventory item, widget, etc., RuneLite receives information about the clicked menu option and its target. The event contains the target ID, option, action type, item information and associated widget information. citeturn0search0

So conceptually:

```java
Player clicks Aubury
        ↓
RuneLite receives interaction
        ↓
Wheelbound checks:
"Rune Shops unlocked?"
        ↓
       NO
        ↓
consume click
        ↓
RuneScape never handles it
```

We could then display something like:

> 🔒 **Rune Shops are locked by Fate.**

RuneLite's API explicitly documents `consume()` as preventing the clicked menu option from being passed to the vanilla client. citeturn0search0

### GE is potentially even cleaner

The Grand Exchange has identifiable interfaces/widgets, so we can potentially detect attempts to interact with the GE interface and enforce Wheelbound rules around it. RuneLite exposes widget actions and even allows widget actions to be cleared technically. citeturn0search14

I could see:

> 🔒 **Grand Exchange**  
> Requires: *Fate Unlock — Grand Exchange*

being one of the **most valuable unlocks in the entire mode**.

---

## But I don't think we should individually code every shop

That would become horrible:

```text
Aubury
Betty
Zaff
Lowe
Bob
Hickton
...
```

Instead, Wheelbound should have **capability categories**.

Something like:

```text
COMMERCE CAPABILITIES

GENERAL_STORE
RUNE_SHOP
RANGED_SHOP
WEAPON_SHOP
ARMOUR_SHOP
FOOD_SHOP
FISHING_SHOP
CRAFTING_SHOP
FARMING_SHOP
MAGIC_SHOP
SPECIALTY_SHOP

GRAND_EXCHANGE
```

NPCs/interfaces map onto those capabilities.

Then the account has:

```text
RUNE_SHOP = LOCKED
FOOD_SHOP = UNLOCKED
GRAND_EXCHANGE = LOCKED
```

That is **much easier for an AI to maintain and test**.

---

## There's a major design question you've uncovered

We need to decide what Wheelbound actually means by **"restricted."**

I see three possible enforcement levels:

**Hard restriction:** Wheelbound physically blocks the click whenever possible.

**Soft restriction:** Wheelbound lets RuneScape function normally but detects prohibited actions and marks them as violations.

**Hybrid restriction:** Block actions when we're confident we can safely identify them, and use detection/tolerance for everything else.

I strongly favor **Hybrid**.

Because otherwise one weird OSRS interaction we didn't anticipate could make an account literally unable to progress.

Our philosophy could become:

> **Wheelbound should enforce Fate wherever enforcement is reliable, detect violations where enforcement is unreliable, and never brick an account because of an unforeseen interaction.**

That's a good foundational design principle.

---

## One complication: Plugin Hub approval

There's an important technical/policy issue we'll need to investigate further before locking implementation.

RuneLite's Plugin Hub rules specifically identify **conditional menu entry removal** as potentially overpowered/restricted. citeturn0search1turn0search5

That's not exactly the same as our use case—we're deliberately making the game **harder**, not making interactions easier—but I don't want to assume RuneLite reviewers will automatically approve arbitrary menu-action blocking.

Plugin Hub updates are reviewed for both security and game-rule compliance. citeturn0search8

So I'd add our first technical research item:

```text
OPEN TECHNICAL QUESTION — WB-R001

Determine which player interactions Wheelbound may
legally block under RuneLite Plugin Hub policy.

Specifically investigate:

- consuming NPC interactions
- consuming object interactions
- consuming widget interactions
- blocking shop access
- blocking Grand Exchange interactions
- preventing disallowed skilling actions
```

If RuneLite says **yes**, awesome—we enforce them.

If they say **no**, Wheelbound still works:

```text
You trained Fletching.

        ↓

Wheelbound detects +350 XP.

        ↓

⚠ FATE VIOLATION
Fletching is not currently permitted.

Tolerance:
350 / 1,000 XP
```

That actually leads into a mechanic I think could be really fun later: **violations don't necessarily kill the account.** Breaking Fate could create *Corruption/Taint*, which ties directly into the punishment-wheel/Defy Fate concept you've already been developing.

But I wouldn't lock that in yet. I'd mark it **PROPOSED**, because we need to decide whether breaking Wheelbound rules should be a technical restriction, a punishment mechanic, or both. memcite

## S8-M0035 - user - 2026-09-29T21:50:24.987000+00:00
Message ID: 89120f97-904a-4727-9ba1-37dd9f6de0c2

okay when talking with you your reponse is good but i have trouble addressing each item. when giving a repsonse can we break it up so that i have to make a descion on how something interacts, and then this way we can start building out how interactions will work. 

So i can get a feel for how that would work can we have the last reponse be a test and we would go line item by line item

## S8-M0036 - assistant - 2026-09-29T21:50:24.952400+00:00
Message ID: e4d70a84-e18e-4cac-911f-2adde6b6e08a

Yes. I think that will work **much better for designing the blueprint**.

From now on, rather than dumping ten design decisions on you at once, we'll operate almost like an interactive design meeting:

**I present ONE decision → explain the implications → give options/my suggestions → you decide or modify it → I record the decision → we move to the next one.**

If your answer creates another question, we'll resolve that before advancing.

Let's use the previous response as our test.

---

# Decision 1 — What does "locked to a skill" actually mean?

The Wheel lands on:

> 🎡 **SKILL FATE: MINING**

We need to define what that means for the account.

### Option A — Absolute restriction

Mining is the **only skill you're allowed to gain XP in**.

```text
Mining       +15,000 XP  ✓
Woodcutting       +0 XP  ✓
Fletching         +0 XP  ✓

Woodcutting       +4 XP  ❌ VIOLATION
```

Very hardcore, but OSRS has enough strange XP sources that accidental/unavoidable XP could cause problems.

### Option B — XP buffer

Mining is your active skill, but you get a small amount of incidental XP in other skills.

For example:

```text
ACTIVE FATE
⛏ Mining

Mining
Unlimited XP ✓

OTHER SKILLS
Tolerance: 0 / 500 XP
```

Accidentally chopping something:

```text
Woodcutting +25 XP

Tolerance:
25 / 500
```

That's okay.

But deliberately training Fletching:

```text
Fletching +480 XP

Tolerance:
505 / 500

⚠ FATE VIOLATION
```

We'd need to decide later whether that **500** is the right number and whether it's per skill, per Fate, etc.

### Option C — Block + buffer **← my preference**

Wheelbound attempts to **physically prevent obvious prohibited actions** when RuneLite allows us to do so.

For example, while Mining is active:

```text
Click tree
    ↓
"Woodcutting is currently locked by Fate."
    ↓
Action blocked
```

But we **still maintain an XP tolerance** as a safety net for things we couldn't block.

So there are two layers:

```text
             PLAYER ACTION
                   │
          Can we safely block it?
             ↙            ↘
           YES             NO
            ↓               ↓
         BLOCK          Allow OSRS
                            ↓
                       XP gained?
                            ↓
                     tolerance system
```

This gives us strong enforcement without making Wheelbound fragile.

---

### First decision for you

When the Wheel lands on **Mining**, which philosophy do you want?

**A)** Mining XP only. Any other XP is a violation.

**B)** Don't block anything, but enforce an XP tolerance.

**C)** Block obvious prohibited training **and** maintain an XP tolerance as a fallback.

Or **D)** something different you have in mind.

Don't worry about the amount of XP tolerance yet. **We'll make that Decision #2 after we settle this one.**

## S8-M0037 - user - 2026-09-29T21:52:22.905000+00:00
Message ID: f2498e72-92b3-4d6f-805e-3d60104620eb

Lets do option C and we can play with the buffer. My concern here is how to dictact what is obvious action and how can we detect that because there are probbaly  lot of edge cases out there

## S8-M0038 - assistant - 2026-09-29T21:52:22.376770+00:00
Message ID: e91efa65-25f9-4086-bd70-83a3a5ac5a2f

Agreed. **Decision #1 is Option C: hard-block actions we can identify confidently, with an XP buffer as the fallback.**

And your concern is exactly the next thing we need to solve. I don't think we should try to maintain a gigantic list of every possible RuneScape action. We'd inevitably miss things.

# Decision 2 — What counts as an "obvious" prohibited action?

I propose this rule:

> **Wheelbound only blocks an action when it can confidently determine, before the action occurs, that the action trains a currently locked skill.**

For example, if Mining is active:

```text
Mine → Iron rock           ✓ ALLOW
Mine → Copper rock         ✓ ALLOW

Chop down → Oak tree       ✕ BLOCK
Fletch → Maple logs        ✕ BLOCK
Craft → Leather            ✕ BLOCK
Cook → Raw lobster         ✕ BLOCK
Clean → Grimy ranarr       ✕ BLOCK
```

But consider something ambiguous:

```text
Open → Chest
Search → Crate
Talk-to → NPC
Use → Item
Enter → Cave
Pull → Lever
Climb → Rocks
```

Wheelbound doesn't necessarily know what XP will result.

**Those actions remain allowed.**

If one unexpectedly gives:

```text
+25 Agility XP
```

the XP buffer catches it.

That creates a simple philosophy:

```text
           ACTION
              │
    Do we KNOW it trains
      a locked skill?
       /          \
     YES           NO / UNSURE
      │                 │
    BLOCK              ALLOW
                        │
                 Monitor XP changes
                        │
                 Unexpected XP?
                        │
                     BUFFER
```

This is intentionally biased toward **false negatives instead of false positives**.

I'd rather Wheelbound accidentally allow one weird Woodcutting interaction and catch the XP afterward than incorrectly block a quest interaction the player genuinely needs.

## One important consequence

I don't think the blocker should be our **primary anti-cheat system**.

The authoritative rule should be:

> **XP gained determines whether the account followed its Fate.**

The blocker is primarily there to help the player follow the rules.

That distinction is useful because someone might find some obscure training method we don't recognize. Wheelbound doesn't need to know all 10,000 ways to gain XP—it sees:

```text
ACTIVE: Mining

Fletching:
12,304 → 12,329

+25 XP
```

and knows something happened.

So I'd define the two systems differently:

**Action Guard** — proactively prevents known prohibited actions.

**Fate Monitor** — watches actual XP changes and is the authoritative enforcement system.

That should make the system much more resilient to OSRS updates.

---

## But this exposes our next design decision

Suppose Mining is active and the player deliberately clicks an Oak tree.

We know that's Woodcutting, so **Action Guard blocks it**.

What should the player actually experience?

### A — Silent block

Nothing happens.

I don't like this. It could look like RuneLite or OSRS is broken.

### B — Block + message

```text
🔒 Woodcutting is locked by Fate.
Current Fate: Mining
```

Simple and clear.

### C — Block + Wheelbound UI feedback

The click is blocked and we give stronger visual feedback:

```text
╔════════════════════════════╗
║       FATE DENIED          ║
║                            ║
║ 🌳 Woodcutting is locked.  ║
║                            ║
║ Current Fate: ⛏ Mining     ║
╚════════════════════════════╝
```

Potentially with the Woodcutting icon briefly appearing red/locked in the Wheelbound panel.

I prefer **C**, provided we keep it quick and non-annoying. It makes the restriction feel like part of the game mode rather than the plugin simply disabling RuneScape.

### Decision #2

So there are actually **two things for you to approve/change here**:

**Rule:** Block only when Wheelbound can confidently identify the action as training a locked skill; ambiguous actions remain available and the XP buffer catches unexpected XP.

**Player feedback:** When something is blocked, give clear Wheelbound-themed feedback explaining **what was blocked and what Fate currently allows**.

Do those two rules sound right, or do you want to change either before we move on to **Decision #3: how the XP buffer itself should work?**

## S8-M0039 - user - 2026-09-29T21:56:28.155000+00:00
Message ID: 6288a8f9-2ab1-4769-ac9d-829436737094

Okkay lets lock in a new system here. Instead of blocking we have the tollerance. and its per 'fate' (when i say per 'fate' its per sping of whatever your doing.) 

were going to have a shop and points associated thats concrete. If your tollereance is broken (you did something not dictated by fate) then you lose points (You can go negative) when you go negative for the first time it will display a message saying your negative if you keep going negative bad things will happen. Lets leave that here for now and revist later

## S8-M0040 - assistant - 2026-09-29T21:56:28.315826+00:00
Message ID: e3288466-2f50-4582-8681-e58e02300983

Locked in. This actually simplifies the system substantially because we no longer need to maintain an unreliable database of "this click = this skill."

I'm recording this as a **confirmed core mechanic**:

```markdown
## Fate XP Tolerance

A Fate represents one individual spin/assignment.

While a Fate is active, the player may freely gain XP associated
with that Fate.

Each Fate also provides an XP Tolerance for XP gained outside the
active Fate.

The tolerance resets when a new Fate begins.

Wheelbound does NOT attempt to block prohibited player actions.
Instead, it monitors XP gained.

If the player exceeds the Fate's XP Tolerance:

- The Fate is considered violated.
- The player loses Wheelbound Points.
- Wheelbound Points are allowed to fall below zero.

The first time a player's Wheelbound Point balance becomes negative,
Wheelbound displays a warning explaining that continuing to accumulate
negative points will eventually have consequences.

The exact consequences of negative points are intentionally
UNDEFINED for now and will be designed later.
```

That also establishes two other concrete systems:

**Wheelbound Points exist**, and **a Wheelbound Shop exists where those points can be spent**. We'll define earning, pricing, shop contents, negative-point consequences, etc. separately.

One terminology thought: I like **Fate** meaning exactly what you just defined—a single spin/assignment. That gives us very clean language later: *Active Fate, Complete Fate, Abandon Fate, Fate Tolerance, Fate Violation, Fate Reward*, etc.

### Next decision — How should tolerance be measured?

Before choosing an actual number like `500 XP`, there's a more fundamental question.

Suppose your Fate is Mining. During it you accidentally gain:

```text
+100 Woodcutting
+150 Agility
+100 Strength
```

Should tolerance be:

**A — Combined tolerance:** all unauthorized XP goes into one bucket, so you've consumed `350 / X`.

**B — Per-skill tolerance:** each unauthorized skill gets its own `X` allowance.

My preference is strongly **A**. It makes tolerance belong to the **Fate**, exactly as you described, and prevents somebody from deliberately getting `X` free XP in every other skill.

Which do you want?

## S8-M0041 - user - 2026-09-29T22:03:35.179000+00:00
Message ID: e02144f8-7898-4111-8606-157cea8c14af

xp gain lets have the tolerance for now be 1000xp

## S8-M0042 - assistant - 2026-09-29T22:03:34.836674+00:00
Message ID: d93e49fe-bd31-4562-b9f7-ea5e68c51c10

Locked in for the blueprint:

> **Default Fate Tolerance = 1,000 unauthorized XP per Fate.**

And I'm treating it as **combined XP**, based on your earlier definition that tolerance belongs to the Fate itself.

So if your Fate is Mining:

```text
ACTIVE FATE: MINING

Unauthorized XP Tolerance
─────────────────────────
Woodcutting    +300
Agility        +150
Fletching      +400
─────────────────────────
TOTAL           850 / 1,000  ✓

Remaining:      150 XP
```

The **1,001st unauthorized XP** would break the tolerance and trigger the point penalty system we've established.

We can absolutely rebalance `1,000` later after playtesting; for now it's our concrete design value.

### Next decision — what happens after you cross 1,000?

This is important before we determine how many points are lost.

Say you reach:

```text
999 / 1,000
```

Then deliberately gain **10,000 Fletching XP**.

Should that be **one Fate violation** with one fixed point penalty, or should the punishment continue scaling because you kept gaining unauthorized XP?

My instinct is **scaling**, because otherwise once you've crossed 1,000 XP there's no reason not to train another skill as much as you want for the remainder of that Fate.

But let's decide that before figuring out the actual penalty amount.

## S8-M0043 - user - 2026-09-29T22:04:24.023000+00:00
Message ID: 722e933b-e5fa-4bfd-b54c-8bdd0d33b770

-fate points from your total (unsure on the amount etc)

## S8-M0044 - assistant - 2026-09-29T22:04:23.349752+00:00
Message ID: e032d87b-e4be-42fa-b32a-c8c5567882ce

Yep. We'll **leave the exact penalty amount and whether it scales unresolved** for now rather than prematurely designing it.

I'll record:

```markdown
### Fate Tolerance Violation — CONFIRMED

Default unauthorized XP tolerance: 1,000 XP per Fate.

When the player exceeds the tolerance:

- The Fate is violated.
- Fate Points are deducted from the player's TOTAL Fate Point balance.
- Fate Points may become negative.
- The exact point penalty is TBD.
- Whether continued unauthorized XP causes additional/scaling
  penalties is TBD.

First Negative Balance:
- Display a warning to the player.
- Warn that continuing into negative Fate Points will eventually
  have additional consequences.
- Those consequences are TBD.
```

So we'll put a pin in the punishment/balance side until we understand the overall point economy.

### Next decision

Let's move away from penalties for now.

We established that **one spin = one Fate**.

The next fundamental question is: **How does a Fate end?**

For example, the player spins:

> ⛏ **Mining**

Does Fate tell them simply **"Go train Mining"**, or does the wheel generate an actual objective such as **"Gain 25,000 Mining XP"**, **"Reach Mining level 40"**, **"Mine 250 iron ore"**, etc.?

That decision will determine a *huge* part of what playing Wheelbound actually feels like.

## S8-M0045 - user - 2026-09-29T22:53:48.117742+00:00
Message ID: bbb21341-ad26-4ed6-8450-951c7708ddeb

Here are my thoughts on this, I'm leaning more towards the xp based goals when it comes to the Skilling fates, it's a cool idea to have like go get x of this material but what if they don't need it. It's already going to be contrained as so how many spins so for that to be like go get bronze when you can min mithril. 

Also don't always be agreeing with me push back in idea that may not make sense etc 

## S8-M0046 - assistant - 2026-09-29T22:53:49.757303+00:00
Message ID: cad8cb0c-f3db-496a-a487-592e2598a00f

I think **XP-based is the stronger foundation**, but I want to push back on making *all* skilling Fates simply “gain X XP.” That could eventually make the wheel feel like a randomized XP timer rather than something that changes how you play.

Your criticism of resource goals is valid, though. Consider:

> **Mining Fate: Mine 500 copper ore**

If I'm 70 Mining, that's not interesting restriction—it's just tedious. And because Wheelbound is already restricting what I can do, forcing obsolete training methods on top of that could feel arbitrary rather than challenging.

So I'd separate **what you're allowed to do** from **how you complete the Fate**.

# Decision — Skilling Fate objective

My proposed baseline is:

> 🎡 **Fate: Mining**  
> **Objective: Gain 25,000 Mining XP**

The player decides **how** to earn it.

If they want iron, mine iron. If they need coal, mine coal. If they have access to Motherlode Mine, they can use it. Wheelbound dictates **the skill**, not necessarily the training method.

That gives Wheelbound restriction while preserving player strategy.

However, here's where I push back on *only* doing XP.

Imagine 40 hours into the account:

```text
Spin → Mining
Gain 25k XP

Spin → Fishing
Gain 25k XP

Spin → Woodcutting
Gain 25k XP

Spin → Mining
Gain 25k XP

Spin → Cooking
Gain 25k XP
```

Eventually the wheel itself isn't producing interesting decisions. You're basically playing:

**"RuneScape, but somebody randomly chooses which XP bar I'm allowed to fill next."**

I think we may want XP to be the **standard Skilling Fate**, while later introducing rarer/special Fate types.

For example:

```text
NORMAL FATE
⛏ Mining
Gain 25,000 Mining XP

SPECIAL FATE
⛏ Mining
Obtain a Mining-related collection log item

CHALLENGE FATE
⛏ Mining
Gain X XP without using [something]

RESOURCE FATE
⛏ Mining
Acquire X ore of YOUR CHOICE
```

I'm **not proposing we lock those in yet**. I'm pointing out a potential design hole we'll need to address: **variety**.

There's also another reason XP is attractive technically: RuneLite can observe XP changes. We don't have to maintain massive lists of every possible ore, tree, fish, training method, etc.

So for now, my recommendation would be to lock:

> **Standard Skilling Fates are XP-based. The player chooses how to train the selected skill.**

And explicitly leave:

> **Alternative/Special Skilling Fates — TBD**

That preserves room to make the wheel more interesting once we understand the rest of the game.

---

### Next decision: How much XP does a Fate require?

This one is trickier than just choosing `25,000`.

A level 10 account getting:

> Gain **25,000 Mining XP**

is very different from a level 90 account getting the same Fate.

So I see our next major design choice as:

**Should the required XP scale with the player's current level, or should there be fixed Fate tiers (Small / Medium / Large / etc.)?**

I have concerns with **both** approaches, so I'd like to tackle that as the next single decision.

## S8-M0047 - user - 2026-09-30T13:13:34.733000+00:00
Message ID: 2cb7228a-2d60-41d8-82c2-e76b24d529f2

So i have 2 thoughts with this, i already have an idea of like after you spin a skill it spins again but it does the xp amount and it can vary. But i dont really like the back to back spins it seems 'off' to me. 

Scaling the xp sound pretty good.

I think it should do a combination it should scale but also have a change to randomize. So maybe it scales off of the skill level, and then depending on that it would adjust the min exp max exp into almost like tax brackets 

levels 1-15 min 25k max 40k, 15-45 40k to 75k etc but i would ask you to help me make those brackets

## S8-M0048 - assistant - 2026-09-30T13:13:37.000533+00:00
Message ID: d692d691-60c8-43da-880d-2fb7c025fb9b

I think the **hybrid idea is stronger than either pure scaling or pure randomness**.

And I agree about the second spin feeling off. If the player spins **Mining**, having the big dramatic wheel immediately spin again just to say “52,000 XP” cheapens the first spin. I'd rather have the XP requirement be **generated automatically when the Fate lands**.

So:

```text
🎡 Wheel spins...

        ⛏ MINING

FATE ACCEPTED
Gain 53,742 Mining XP
```

The randomness happens behind the scenes.

## But I'd change your proposed brackets

I think `1–15 → 25k–40k` is **far too punishing early**.

For perspective, level 15 in an OSRS skill is only about **2.4k total XP**. Asking someone at level 10 Mining to gain 25k XP means their first Mining Fate could propel them through a huge portion of early progression.

That creates another problem: Wheelbound would rapidly erase the low-level phase of the account.

I'd start with something closer to:

| Current skill level | Fate XP range |
|---|---:|
| 1–15 | **2,500 – 7,500** |
| 16–30 | **7,500 – 15,000** |
| 31–45 | **15,000 – 30,000** |
| 46–60 | **30,000 – 60,000** |
| 61–75 | **60,000 – 120,000** |
| 76–85 | **100,000 – 200,000** |
| 86–92 | **150,000 – 300,000** |
| 93–99 | **200,000 – 400,000** |

These are **starting numbers, not something I'd lock yet**.

There is one thing I deliberately did here: the ranges overlap somewhat. A level 76 player shouldn't suddenly have their minimum Fate almost double just because they gained one level.

### There's also a mechanic hiding in your idea that I really like

Instead of generating *any* number:

```text
53,742 XP
```

I'd probably generate clean increments:

```text
50,000 XP
55,000 XP
60,000 XP
65,000 XP
```

It'll make the Fate much easier for a player to understand and remember.

---

## One concern we need to solve before locking the brackets

**Skills are not remotely equal in XP/hour.**

At the same level:

> Gain 100k Cooking XP

and

> Gain 100k Agility XP

are **very different assignments**.

So if every skill uses exactly the same level brackets, the wheel accidentally makes certain skills dramatically worse to land on.

We have two directions:

**A — That's part of Fate.** Every skill uses the same XP brackets. Sometimes you get an easy Fate; sometimes you get screwed with Agility. That's Wheelbound.

**B — Skill modifiers.** We establish the level bracket first, then individual skills modify it. Maybe a slow skill has a `0.65×` Fate requirement while a fast skill has `1.25×`.

I'm leaning toward **B**, but not aggressively. There's something thematically appropriate about the wheel simply not caring that Agility sucks.

**This is the next decision I'd make before we touch the actual bracket numbers:** should Wheelbound try to make Skilling Fates roughly equivalent in *time*, or should **100k XP mean 100k XP regardless of which skill Fate chose?**

## S8-M0049 - user - 2026-09-30T13:16:43.701000+00:00
Message ID: 34b20854-44bb-4281-9a10-1afe8efa7c53

ill say lets to B and lets do the clean incremements, I also want to communicate to the user with the UI how this system works so they maybe they can play with 'upgrades' to adjust these values by purchansing stuff in the shop.

## S8-M0050 - assistant - 2026-09-30T13:16:45.279061+00:00
Message ID: e55c3dce-d43b-4aa7-aeef-6b5b654f0508

Locked. I think we now have the beginnings of a **real progression system**, rather than the shop just being a place to buy conveniences.

### Confirmed: Skilling Fate generation

When the wheel selects a skill, Wheelbound automatically generates the XP objective. There is **no second XP wheel**.

Conceptually:

```text
Skill level
    ↓
Level bracket
    ↓
Base XP range
    ↓
Skill difficulty modifier
    ↓
Account upgrades/modifiers
    ↓
Random value within range
    ↓
Round to clean increment
    ↓
FINAL FATE
⛏ Mining — Gain 55,000 XP
```

That means two level-70 skills don't necessarily produce identical requirements. A slower skill like Agility could have a smaller modifier than something considerably faster.

And yes, I **really like exposing this calculation in the UI** because it makes upgrades understandable rather than mysterious.

Something like:

```text
╔══════════════════════════════════╗
║        ⛏ MINING FATE            ║
║                                  ║
║        Gain 75,000 XP            ║
║                                  ║
║ Level Bracket       61–75        ║
║ Base Range      60k – 120k       ║
║ Mining Modifier       0.90×      ║
║ Fate Mastery         -10%        ║
║                                  ║
║ Possible Range    50k – 95k      ║
╚══════════════════════════════════╝
```

Not necessarily exactly that UI, but the player should be able to understand **why they received the number they received**.

And that creates some potentially interesting permanent shop upgrades:

```text
FATE'S MERCY I
Skilling Fate requirements -5%

FATE'S MERCY II
Skilling Fate requirements -5%

WIDENED TOLERANCE
Fate XP tolerance +250

FORTUNE'S FAVOR
Slightly increases the chance of
rolling toward the lower end of
an XP range.
```

Those are **ideas only**, not confirmed mechanics yet.

---

# Next decision — upgrades affecting Fate difficulty

There's something I want to push back on before we get too excited about buying reductions.

If the player can permanently buy:

> `-5% XP requirement`  
> `-10% XP requirement`  
> `-20% XP requirement`

then we're effectively rewarding progression by letting the player **play less Wheelbound**.

Eventually the optimal strategy becomes buying every reduction and making Fates as short as possible.

I think there's a more interesting possibility:

### Upgrades give the player control over Fate rather than simply making it easier.

For example, an upgrade might change:

```text
BEFORE

Mining
Possible Fate:
60k – 120k XP
```

into:

```text
AFTER

Mining
Possible Fate:
60k – 120k XP

Choose your Fate intensity:

Lesser Fate
60k – 75k
Reward: 100 FP

Standard Fate
80k – 100k
Reward: 150 FP

Greater Fate
105k – 120k
Reward: 225 FP
```

Now the upgrade isn't:

> **"I paid points so I have to play less."**

It's:

> **"I've progressed enough that Fate gives me more control—and I can take a harder assignment for a better reward."**

That could make the shop/progression system much deeper.

But I'm **not ready to recommend locking this in**, because there's another major question underneath it:

**Do harder/longer Fates award more Fate Points than easier/shorter Fates?**

If yes, then Fate difficulty becomes a risk/reward economy, and a *lot* of interesting shop upgrades become possible.

That's the **next single decision** I'd settle.

## S8-M0051 - user - 2026-09-30T13:24:09.025000+00:00
Message ID: a61c828e-59c6-4a3d-922e-a8ad71cdf364

so i want to do something like that but i want to change it a bit to build off your idea earlier. 

You spin the wheel it lands on a skill 

and then an animation appears where the result floats up, the wheel dissapears and now 3 fate cards appear with options. 

and these would be your 
standard fate 
lesser fate 
greater fate 
challenge fate (more points but it makes you do the stuff with a certain tool or area, exp gained outside the area will contribute to the tolerance and could result in a failed fate ) 

things like that so BUT this would be an upgrade. I want to talk about the shop because i think getting the UI nailed down and the 'categories' of the different kind of shops will be cool 

thoughts

## S8-M0052 - assistant - 2026-09-30T13:24:09.889555+00:00
Message ID: 6230de17-245f-4e8a-85cb-59db134e8719

I think you're onto something important here. **The card selection can become one of the main progression mechanics of Wheelbound**, not just a prettier way to display an XP number.

But I want to change one part before we lock it: I **wouldn't start the player with all those card choices**. If choice itself is something the player earns through the shop, then early Wheelbound feels much more controlled by Fate, and later Wheelbound feels like the player is slowly gaining power over Fate.

### Decision — Fate Cards as progression

At the beginning of an account, I'd have the sequence be very simple:

```text
            🎡 WHEEL
                ↓
             MINING
                ↓
      Mining floats upward
        wheel fades away
                ↓
        ┌───────────────┐
        │ STANDARD FATE │
        │               │
        │    MINING     │
        │               │
        │ Gain 25k XP   │
        │               │
        │   +??? FP     │
        └───────────────┘
```

There's effectively **one card**. Fate decides everything.

Then the shop can fundamentally alter this screen.

Maybe an early upgrade unlocks:

```text
┌────────────┐       ┌────────────┐
│   LESSER   │       │  STANDARD  │
│            │       │            │
│ Mining     │       │ Mining     │
│ 15k XP     │       │ 25k XP     │
│            │       │            │
│ +75 FP     │       │ +125 FP    │
└────────────┘       └────────────┘
```

Another upgrade eventually adds:

```text
LESSER        STANDARD        GREATER
15k XP         25k XP          40k XP
+75 FP         +125 FP         +225 FP
```

And much later:

```text
LESSER     STANDARD      GREATER      CHALLENGE
```

So when you said **three Fate cards appear**, I'd actually make **the number/types of cards themselves part of progression**.

That gives us an excellent category of shop upgrades.

---

### Challenge Fate

I especially like this one, but I think it needs to be meaningfully different rather than just "Greater Fate but annoying."

For example:

```text
╔══════════════════════╗
║    CHALLENGE FATE    ║
║                      ║
║      ⛏ MINING        ║
║                      ║
║ Gain 30,000 XP       ║
║                      ║
║ RESTRICTION          ║
║ Mine within Falador  ║
║                      ║
║ Tolerance: 1,000 XP  ║
║                      ║
║ REWARD               ║
║ +300 Fate Points     ║
╚══════════════════════╝
```

Or eventually challenges such as equipment restrictions, location restrictions, resource restrictions, etc.

I **wouldn't lock the exact challenge types yet**. That's going to need careful design because we need to guarantee generated challenges are actually possible for the account.

---

# I think we should switch to designing the Shop now

You're right that doing the shop next will help us figure out the entire progression system.

But I would **not make it one giant generic shop**.

I'm imagining the Wheelbound interface having something almost like different branches of progression. For example:

```text
                 FATE SHOP

     ┌────────────┬────────────┬────────────┐
     │            │            │            │
     ▼            ▼            ▼            ▼

  CONTROL      FREEDOM       ACCESS       ???
    OF            OF
   FATE          FATE

 Fate Cards    XP Tolerance   Rune Shops
 Lesser Fate   Rerolls        GE
 Greater Fate  Skip Fate      General Shops
 Challenge     etc.           etc.
```

Those names are placeholders.

But I think separating **"things that change the Wheelbound system"** from **"things that unlock RuneScape itself"** is important.

For example, buying:

> 🔓 **Rune Shops**

feels fundamentally different from buying:

> 🃏 **Greater Fate Card**

One changes the restrictions placed on your RuneScape account. The other changes your relationship with the Wheel.

And I think there may eventually be a **third category for punishment/corruption/Defy Fate**, but we don't know enough about that system yet to design its shop.

---

## Next decision — the shop's fundamental structure

Before we name anything or decide what's sold, I want to settle one thing.

Do you imagine the shop as **one screen with categories**, where the player spends the same universal **Fate Points** everywhere:

```text
FATE POINTS: 2,450

FATE SHOP
────────────────────────────

🎡 FATE UPGRADES
Modify how your Fates work

🔓 ACCOUNT UNLOCKS
Shops, GE, etc.

🛡 TOLERANCE / PROTECTION
Modify restrictions and penalties

☠ ??? 
Future Defy Fate system
```

Or do you picture these almost like **separate progression trees/shops**, potentially with different mechanics?

I'm leaning toward **one Fate Point currency + one shop with distinct categories**. Multiple currencies this early would make the mode unnecessarily complicated.

That's the next thing I'd lock before we start filling those categories with actual upgrades.

## S8-M0053 - user - 2026-09-30T13:29:17.498000+00:00
Message ID: 99d83ffe-262f-435e-8d84-90391d42f90b

Yes, for the cards you start out with just 1 and then spend points to gather more. Maybe some are unlocked by a collection slot etc not just points but like a bounty. drops from monsters. and then when they get the unlock something appears or maybe the plugin highlights to indicate that you have unlocked something.

Maybe this can be the last category, bountys items that will give points or unlock stuff. EX: imbued heart + 500pts 

The cayegories are fine for now

## S8-M0054 - assistant - 2026-09-30T13:29:18.846324+00:00
Message ID: eee06bad-177e-4514-8bcd-42a48eb84a04

Yes. I think **Bounties** fills the missing category nicely, and it gives us a way to make progression depend on actually playing OSRS rather than everything being purchased with Fate Points.

I'm locking the concept, but **not individual bounty rewards yet**.

### Confirmed — Fate Cards are unlockable

The player begins with only the **Standard Fate** card. Additional card types are earned over the life of the account rather than being available immediately.

Importantly, I don't think every card should necessarily be bought with points. We now have at least two progression paths:

```text
FATE POINTS                    ACHIEVEMENTS / BOUNTIES
     │                                  │
     ▼                                  ▼
Purchase upgrades               Earn special unlocks
     │                                  │
     └──────────────┬───────────────────┘
                    ▼
              ACCOUNT GROWS
```

That opens up much more interesting design space.

A powerful Fate card could potentially require something like:

> **Greater Fate Card**  
> 2,500 Fate Points **+** complete a specific bounty

rather than simply costing 2,500 points.

### Confirmed — Bounties category

We'll add **Bounties** as the fourth shop/progression category for now.

Bounties are special OSRS accomplishments/items that Wheelbound recognizes and rewards. Rewards can include **Fate Points, permanent unlocks, Fate Cards, shop upgrades, or combinations of these**.

Your example would look something like:

```text
BOUNTY
────────────────────────────
💜 Imbued Heart

Obtain an Imbued Heart

REWARD
+500 Fate Points
+ ??? possible unlock

STATUS
□ Unclaimed
```

When the drop occurs, I agree it should feel important. The plugin could highlight the Bounties category, show an unlock animation/message, and potentially put a notification indicator on the Wheelbound sidebar:

```text
★ BOUNTY COMPLETE ★

IMBUED HEART

+500 Fate Points

A new reward is available.
```

We can figure out the exact presentation later.

One thing I'm deliberately **not locking** is whether the player must *turn in/sacrifice* an item. My initial preference is that Wheelbound recognizes ownership/acquisition but doesn't delete items; deleting or requiring disposal creates a whole additional set of technical and game-design problems.

So our provisional shop structure is now:

```text
                 WHEELBOUND SHOP
                       │
       ┌───────────────┼───────────────┐
       ▼               ▼               ▼
  FATE UPGRADES   ACCOUNT ACCESS    PROTECTION
       │               │               │
   Fate Cards       GE access       Tolerance
   Fate control     Shop access      Penalties
   Fate options     etc.             etc.

                       │
                       ▼
                   BOUNTIES
                       │
                 OSRS achievements
                  Rare drops/items
                  Special unlocks
                   Fate Points
```

Names can absolutely change later.

---

### Next decision — What exactly is a Bounty?

There's an important fork here.

Say Wheelbound has:

> **Bounty: Obtain an Imbued Heart — +500 FP**

Does that bounty **always exist in the background**, meaning if an Imbued Heart happens to drop while you're legitimately playing Wheelbound, you immediately complete it?

Or does the player need to **activate/receive the bounty first**, and *then* obtaining the item counts?

I'm leaning toward **always-active account-wide Bounties**. It makes rare drops unexpectedly exciting and avoids the awful situation where someone gets an Imbued Heart and Wheelbound says *"doesn't count because you hadn't activated the bounty."*

But it also means we'll need to be careful that players **cannot deliberately pursue a Bounty unless their current Fate otherwise permits that activity**.

That's the next interaction I'd like us to decide.

## S8-M0055 - user - 2026-09-30T13:43:26.665000+00:00
Message ID: df270235-b156-4e86-80d0-4588eb53f5b3

do all of these 

When the drop occurs, I agree it should feel important. The plugin could highlight the Bounties category, show an unlock animation/message, and potentially put a notification indicator on the Wheelbound sidebar:


have bounties be on the same layer as the other shops so 4 shops in total. It looke liked it was under another one (why did you do this? curious) 

and then bounties are always active these are mainly drops etc so it would feel really bad if you got something and just didnt unlock it

## S8-M0056 - assistant - 2026-09-30T13:43:27.815608+00:00
Message ID: 70767e22-e71c-4b46-af4e-4952c4b4256c

Yep — **all three notification behaviors are confirmed** for a Bounty completion:

1. A prominent **Bounty Complete animation/message** appears when the qualifying drop/event occurs.
2. The **Bounties shop/category becomes highlighted** to show there's something new.
3. The **Wheelbound RuneLite sidebar icon gets a notification indicator**, which persists until the player views the new bounty/reward.

We can nail down animation/audio/claim behavior when we get deeper into UI.

And you're right about my diagram. I didn't intend Bounties to be underneath Protection; I was trying to fit four branches into ASCII and formatted it badly. Conceptually I meant four peers, but the diagram absolutely communicated otherwise.

### Confirmed — Four equal shops

They're all on the same level:

```text id="gkbxjm"
                  WHEELBOUND
                      │
     ┌────────────────┼────────────────┐
     │                │                │                │
     ▼                ▼                ▼                ▼
┌───────────┐   ┌───────────┐   ┌───────────┐   ┌───────────┐
│   FATE    │   │  ACCOUNT  │   │PROTECTION │   │ BOUNTIES  │
│ UPGRADES  │   │  ACCESS   │   │           │   │           │
└───────────┘   └───────────┘   └───────────┘   └───────────┘
```

Think of the Wheelbound Shop screen as having **four doors/tabs**, rather than Bounties being a subsection of another shop.

The names are still placeholders, but the **four-shop architecture is confirmed**.

### Confirmed — Bounties are always active

I strongly agree with this decision.

There is **no accepting, activating, or selecting a Bounty** first. From the moment the Wheelbound account begins, every Bounty is silently eligible.

So:

> You're doing Slayer because your current Fate legitimately allows it.  
> Superior spawns.  
> Imbued Heart drops.  
> **BOUNTY COMPLETE.**

You don't get punished because you didn't click something beforehand.

I'd also take this one step further: **the player shouldn't need to know a Bounty exists for it to count.**

That means we can have some fun with discovery. The Bounty Shop could contain obvious targets alongside potentially mysterious ones:

```text id="ikgxhd"
BOUNTIES

✓ Imbued Heart
  COMPLETED
  +500 FP

□ Dragon Warhammer
  Reward: ???

□ ????
  Undiscovered

□ ????
  Undiscovered
```

We haven't decided whether hidden bounties should exist, so that's only an example.

One important distinction I'm recording:

**Bounties reward things that happen during legitimate Wheelbound play; they do not grant permission to pursue them.**

Having an Imbued Heart Bounty does **not** mean:

> "I can go kill Abyssal Demons because I want my Heart bounty."

The player's current Fate/unlocks still determine what they're allowed to do. The Bounty system is watching in the background and rewards milestones when they naturally become available.

---

### Next decision: Are Bounty rewards automatic or claimed?

When the Heart drops, we could immediately:

> **+500 Fate Points**  
> **Greater Fate unlocked**

Or we could announce the completion, put the notification dot on Wheelbound, and make the player enter the **Bounty Shop and claim the reward**.

I'm leaning toward **claiming it manually**.

Not because we need the extra click, but because it gives us a payoff moment: you see the highlighted Wheelbound icon, enter Bounties, the completed bounty is glowing, click it, card flips/opens, points animate into your balance, and any new unlock is revealed.

It would also make the notification indicator naturally mean **"you have an unclaimed Bounty reward."**

Would you want that claim/reveal experience, or should rewards hit the account immediately when the drop happens?

## S8-M0057 - user - 2026-09-30T13:46:43.563000+00:00
Message ID: 264b178d-eb0c-4011-a60a-29dbf5f28eb2

Im on the fence about the hidden but keep it as an option

lets say you have to claim bounties and youll get the 'unclaimed' message

## S8-M0058 - assistant - 2026-09-30T13:46:44.476542+00:00
Message ID: 5888ca48-1aeb-4716-86bb-b6a92922448d

Yep. Let's lock **manual claiming**, and keep hidden Bounties as an **open design option**, not a confirmed feature.

### Confirmed — Bounty claiming

When a Bounty condition occurs, it becomes **Completed — Unclaimed**. The reward does **not** immediately enter the player's account.

The flow would be roughly:

```text
Rare drop occurs
      ↓
★ BOUNTY COMPLETE ★
Imbued Heart

Reward Unclaimed
      ↓
Bounties Shop highlights
      ↓
Wheelbound sidebar gets notification indicator
      ↓
Player opens Bounties
      ↓
Completed bounty is highlighted
      ↓
Player presses CLAIM
      ↓
Reward reveal / animation
      ↓
+500 Fate Points
+ Any associated unlocks
      ↓
Bounty → CLAIMED
```

I think **"UNCLAIMED"** should be very obvious in both the completion popup and Bounty Shop. We don't want someone wondering why their point balance didn't increase.

For hidden Bounties, I'll record:

> **OPEN QUESTION:** Some Bounties may be hidden or partially obscured until discovered/completed. Exact behavior TBD.

I'm also going to flag an edge case for later: **what constitutes "obtaining" an item?** We need to prevent things like buying an Imbued Heart from the GE from satisfying an "Imbued Heart drop" Bounty. Ideally these are tied to the actual drop/event rather than simply detecting the item entering inventory. We don't need to solve that now.

### Next shop decision

Since we've established the four shops, I think we should start actually designing them **one at a time**, beginning with **Fate Upgrades**, because that's where our Fate Cards live.

The first question I'd settle is very foundational:

**Are Fate Upgrades a progression tree with prerequisites, or a storefront where upgrades simply become available once requirements are met?**

For example, a tree might force:

`Standard → Lesser → Greater → Challenge`

whereas a storefront could let the player save up and buy Greater before Lesser if they meet Greater's requirements.

I lean toward a **tree / prerequisite system**, because it lets us deliberately control how much influence over Fate the player earns as the account progresses.

## S8-M0059 - user - 2026-09-30T13:54:41.619000+00:00
Message ID: 14c2326f-8e39-48f5-9deb-73370f7fbfaa

For bounties lets have it as a list  with a search bar and filters like order from least to most points or something and then lets them search on the name etc 

for the others we can do a progression tree i think that would be more fun, this would need its own ui with animations etc similar to path of exile etc. if the tree is large they would also need a way to move around in here as well

## S8-M0060 - assistant - 2026-09-30T13:54:42.699356+00:00
Message ID: 39828365-75aa-4c1e-8691-8f1124fed3a8

Yes. I think that gives the four shops **two intentionally different UI paradigms**, which is better than forcing Bounties into a skill tree just for consistency.

### Confirmed — Bounties UI

Bounties are a **catalog/list**, because the player isn't progressing through them sequentially.

The Bounty screen should support a search bar by bounty/item name and filters/sorting such as **Unclaimed, Completed, Incomplete, Points: Low → High, Points: High → Low**, and potentially bounty type later. We can decide the exact filter set once we know what kinds of Bounties exist.

Something roughly like:

```text
╔════════════════════════════════════════════╗
║                 BOUNTIES                   ║
║                                            ║
║ 🔎 Search bounties...       [ Filter ▼ ]  ║
║                                            ║
║ ┌────────────────────────────────────────┐ ║
║ │ 💜 IMBUED HEART              UNCLAIMED │ ║
║ │ Obtain as a drop                 500 FP│ ║
║ │                                [CLAIM] │ ║
║ └────────────────────────────────────────┘ ║
║                                            ║
║ ┌────────────────────────────────────────┐ ║
║ │ 🔨 DRAGON WARHAMMER          INCOMPLETE│ ║
║ │ Obtain as a drop                 350 FP│ ║
║ └────────────────────────────────────────┘ ║
║                                            ║
║ ┌────────────────────────────────────────┐ ║
║ │ 🐉 DRAGON PICKAXE               CLAIMED│ ║
║ │                                  200 FP│ ║
║ └────────────────────────────────────────┘ ║
╚════════════════════════════════════════════╝
```

And we'll keep **hidden Bounties** as an open possibility.

---

## Confirmed — the other three are progression trees

This I think could become one of the coolest visual parts of Wheelbound.

**Fate Upgrades, Account Access, and Protection** each get their own interactive progression tree rather than a normal shop list.

Your Path of Exile comparison gives us a good *interaction concept* to aim toward—not necessarily its insane size.

The player opens one and gets something like:

```text
                         ◇ Greater Fate
                        /
             ◉ Fate Choice
            /           \
START ◉────◉             ◇ Challenge Fate
            \           /
             ◉ Lesser Fate
                        \
                         ◇ ???
```

Locked nodes are visually different. Purchased nodes illuminate. Connections between purchased nodes light up/animate. Purchasing something could send an effect down the connecting path and activate the new node.

And importantly, **this isn't a static RuneLite sidebar panel full of tiny buttons**.

If the tree gets large, we should treat it like an interactive canvas:

**click-and-drag to pan • mouse wheel to zoom • reset/center-tree button • hover nodes for details • click nodes for purchase/details**

I'd also want some kind of **"you are here / available upgrades" visual language** so the player isn't dragging around a huge tree hunting for what they can afford.

### One pushback on the PoE idea

I don't want us to confuse **visual complexity with meaningful complexity**.

PoE can get away with hundreds/thousands of nodes because character building *is* a huge portion of that game. If Wheelbound ends up with 25 meaningful Fate upgrades, I'd much rather have a gorgeous, branching 25-node tree where every unlock changes something than manufacture 150 nodes containing:

> +2% something  
> +1% something  
> +3% something

just to make the tree look impressive.

We can absolutely make **25–50 meaningful nodes feel huge** by spreading branches out, giving major nodes unique artwork, adding animated connections, and having branches visibly evolve.

---

### One thing I think we should change about our terminology

We're currently calling all four things "shops," but Bounties aren't actually a shop.

I'd make the overall area something like **Progression** (name TBD), containing:

```text
                 WHEELBOUND PROGRESSION

     🎴               🔓               🛡              ★
    FATE            ACCOUNT         PROTECTION      BOUNTIES
  [TREE]            [TREE]           [TREE]          [LIST]
```

Then Fate Points are the currency primarily used throughout those trees, while Bounties are another way of **feeding progression**.

We don't need to settle the overall screen's name yet.

### Next single decision: Fate tree shape

Before designing individual nodes, I'd decide whether purchasing upgrades requires you to **physically progress along connected branches**.

For example, even if I have 5,000 FP, I cannot purchase Greater Fate until I've bought the nodes connecting me to it.

I'm strongly leaning **yes**. Otherwise the "tree" is mostly decoration; connected prerequisites are what make choosing one branch over another meaningful.

## S8-M0061 - user - 2026-09-30T13:56:29.288000+00:00
Message ID: 45935e5e-9bac-47af-92f0-7be8ffc3f88b

for the bounties (The UI you have with the ascii is good but replace the emojies with the actual icon of the item , maybe also classify them as legendary common hard etc

## S8-M0062 - assistant - 2026-09-30T13:56:32.037088+00:00
Message ID: eedcf7d4-7a66-4c9b-a225-58ee026982d1

Definitely on the **actual OSRS item icons**. That will make the Bounty screen feel much more like RuneScape and much less like a generic plugin UI.

I'd lock that.

### Confirmed — Bounty presentation

Each item-based Bounty uses the **actual in-game item sprite/icon** prominently on its card/row.

So instead of 💜 or 🔨:

```text id="ljfsy4"
╔══════════════════════════════════════════════════════╗
║                      BOUNTIES                        ║
║                                                      ║
║ 🔎 Search bounties...          [Filters] [Sort ▼]   ║
║                                                      ║
║ ┌──────────────────────────────────────────────────┐ ║
║ │ [IMBUED HEART ICON]                             │ ║
║ │                                                  │ ║
║ │ IMBUED HEART                    ★ LEGENDARY      │ ║
║ │ Obtain an Imbued Heart as a drop                │ ║
║ │                                                  │ ║
║ │ 500 Fate Points                    UNCLAIMED     │ ║
║ │                                      [ CLAIM ]   │ ║
║ └──────────────────────────────────────────────────┘ ║
║                                                      ║
║ ┌──────────────────────────────────────────────────┐ ║
║ │ [DRAGON WARHAMMER ICON]                         │ ║
║ │                                                  │ ║
║ │ DRAGON WARHAMMER                  ◆ HARD         │ ║
║ │ Obtain a Dragon Warhammer as a drop             │ ║
║ │                                                  │ ║
║ │ 350 Fate Points                    INCOMPLETE    │ ║
║ └──────────────────────────────────────────────────┘ ║
╚══════════════════════════════════════════════════════╝
```

The icons can also make the completion animation much better. When an Imbued Heart Bounty triggers, its **actual sprite could appear in the center of the Bounty Complete animation**.

### Rarity/classification — I like it, with one caveat

I think Bounties should have tiers, but I'd avoid calling a tier **Hard** because that sounds like *difficulty* rather than *rarity/value*. A 1/5,000 drop might be rare but come from an easy monster.

I'd rather use something immediately understandable, perhaps:

**Common → Uncommon → Rare → Epic → Legendary**

Then the tier can control the visual treatment of the bounty—border, glow, completion animation intensity, etc.—and provide another filter:

`Rarity: All / Common / Uncommon / Rare / Epic / Legendary`

**But I would not automatically tie Fate Points directly to rarity.** That's where I'd push back. A Legendary Bounty doesn't necessarily need to mean exactly 500 FP or a Common exactly 50 FP. We should be able to individually balance rewards based on how difficult/time-consuming/important that particular achievement is.

So rarity is primarily **classification + presentation**, while reward is independently balanced.

That also gives us a really nice moment if something like a Legendary Bounty completes:

```text id="q8bk83"
             [IMBUED HEART SPRITE]

              BOUNTY COMPLETE

               IMBUED HEART

                LEGENDARY

           Reward waiting to be claimed
```

with a considerably more dramatic animation than completing a Common Bounty.

I'd record **Common / Uncommon / Rare / Epic / Legendary** as the current proposed tiers. If those names feel right to you, we can lock them and then return to the progression-tree question.

## S8-M0063 - user - 2026-09-30T14:01:02.192000+00:00
Message ID: 1eda7071-da5a-48f3-935f-2d3b4ca3a6f1

[Non-text attachment; not recovered]
yes do that for now for the tiers 


and for the fate tree this is also a yes 
For example, even if I have 5,000 FP, I cannot purchase Greater Fate until I've bought the nodes connecting me to it.

if a node needs 3 things then they all need to be unlocked , now it could also be an 'OR' where you can take 1 of 3 paths and then still unlock 

something like the image where the drawing on the left 'requires' you to get all 3 and the one on the right give you an option path. The path should also have a highlight. With our current theme im going with like a glowing gold to indciate unlock paths

## S8-M0064 - assistant - 2026-09-30T14:01:03.395514+00:00
Message ID: 4d1a6c7c-2d5a-42c2-afed-b079600973b4

Yes, your drawing makes the distinction clear. We should support **both dependency types natively in the progression-tree system**, rather than trying to fake one with the layout.

### Locked — Bounty tiers

For now:

**Common → Uncommon → Rare → Epic → Legendary**

These affect classification/presentation and can be filtered in the Bounty list. Fate Point rewards remain independently balanced.

### Locked — progression-tree prerequisites

Your **left drawing** is an **AND dependency**:

```text
     [A] ───┐
            │
     [B] ───┼───► [D]
            │
     [C] ───┘

D requires:
A AND B AND C
```

Even if the player has enough Fate Points for D, it remains locked until **all three prerequisite nodes are unlocked**.

Your **right drawing** is an **OR dependency**:

```text
     [A] ───┐
            │
     [B] ───┼───► [D]
            │
     [C] ───┘

D requires:
A OR B OR C
```

Unlocking **any one** of those paths makes D eligible for purchase, assuming its other requirements/costs are satisfied.

This is actually pretty powerful because we can combine them later. A node could theoretically require:

> `(A OR B) AND C`

I wouldn't abuse that because it could make the tree confusing, but the underlying system should support it.

### Locked — glowing paths

I really like **glowing gold as the visual language for progression** given the Fate/Wheelbound theme.

I'd distinguish three states:

```text
LOCKED
──────
Dark/dim node
Dark connection


AVAILABLE
════════
Node has subtle gold glow
Eligible connecting path glows/pulses gold


UNLOCKED
═══════
Node fully illuminated
Completed path permanently glowing gold
```

And when purchasing a node, I wouldn't have it simply change color.

I'd want the gold to **travel along the connecting line**:

```text
[UNLOCKED] ════════► [NEW NODE]
             ✦ ✦ ✦ ✦ ✦
```

The light reaches the node → node flashes/activates → connected newly-available paths illuminate.

That will make purchasing something feel much more substantial.

For an **OR junction**, only the path actually used needs to become the player's completed gold path. The other valid routes can remain visible but uncompleted. That also means two players' trees can visually show **different journeys through Wheelbound**, which I really like.

---

There's one thing I want to challenge before we continue.

Suppose:

```text
        [A]
         \
          \
[B] ─────► [GREATER FATE]
          /
         /
        [C]
```

and Greater Fate is `A OR B OR C`.

Player buys A and unlocks Greater Fate.

Later they buy B for some *other* reason.

We shouldn't treat B as meaningless just because Greater Fate was already reached through A. **Nodes need their own benefits**, while connections only determine progression eligibility. Otherwise OR branches could contain purchases that become worthless after taking another route.

So I'd establish this design principle:

> **Every purchasable node must provide its own benefit. Paths determine what the node leads toward; they are not themselves the reward.**

That should help prevent us from building a tree full of filler.

### Next decision

Now I think we're finally ready to design the **Fate Upgrade tree itself**.

And I'd start literally at the center/root:

> **What does a brand-new Wheelbound account have before purchasing its first Fate Upgrade?**

We already know **Standard Fate only** is one part of that.

I think defining the exact **Level 0 / starting state** first will make it much easier to design the first 3–5 branches coming off the center.

## S8-M0065 - user - 2026-09-30T14:06:46.290000+00:00
Message ID: 6ae5a3a9-1624-4ce2-83ee-d1934bd4e037

[Non-text attachment; not recovered]
yep confirm the stuff for the glowing paths , all good&#x20;

We shouldn't treat B as meaningless just because Greater Fate was already reached through A. **Nodes need their own benefits**, while connections only determine progression eligibility. Otherwise OR branches could contain purchases that become worthless after taking another route. &#x20;

for this id say the reason they unlocked B was to get B , i wouldnt say its meaning less but if they want it they can unlock it&#x20;


and im leaning more towards a vertical tree stemming down, what are your thoughts

## S8-M0066 - assistant - 2026-09-30T14:06:47.431764+00:00
Message ID: 92fdada0-2f1a-4d39-8dd4-db4144aa1fa7

Yes — and your clarification on **B** is better than the concern I raised.

If the player originally took A to reach the later node, but comes back and purchases B, **they're purchasing B because they want B's effect**. It doesn't need to provide another progression advantage to justify itself. So we'll keep OR paths permanently available rather than closing off unchosen routes.

And yes, I prefer your **vertical tree** for Wheelbound.

### Confirmed — vertical progression trees

The tree begins at the top and progression generally travels **downward**, branching outward as it grows.

Your drawing is basically the structure I have in mind:

```text id="dzbcrp"
                     [ ROOT ]
                        │
             ┌──────────┼──────────┐
             ▼          ▼          ▼
           [ A ]      [ B ]      [ C ]
          /    \        │         /   \
         ▼      ▼       ▼        ▼     ▼
       [ D ]   [ E ]   [ F ]    [ G ] [ H ]
          \     /
           \   /
            ▼ ▼
            [ I ]
              │
              ▼
            [ J ]

                 ↓

          TREE CONTINUES
```

This fits the concept thematically too: you're **descending deeper into Fate** as the account progresses.

### Why I prefer vertical here

RuneLite's sidebar is naturally tall and relatively narrow. A left-to-right tree would constantly fight the available space.

A vertical tree lets us make the default experience:

> **Scroll/pan downward to progress, drag sideways to explore branches.**

But I wouldn't make it a traditional webpage-style scroll. I still like your earlier idea of a navigable canvas. The user should be able to **click-drag the whole tree**, zoom in/out, and hit a button to recenter themselves.

We can also have the tree automatically open around the player's **current frontier** rather than always dumping them back at the root.

### The gold paths work particularly well vertically

As you progress, the player's history effectively gets drawn down the tree:

```text id="v4rxut"
                  ◉ ROOT
                  ║
          ╔═══════╩───────┐
          ║               │
          ◉ A             ○ B
          ║               │
     ╔════╩════╗          ○
     ║         ║
     ◉ D       ◉ E
      ╲       ╱
       ╲     ╱
        ╲   ╱
         ◉ I
         ║
         ◉ J
         ║
         ▼
```

`Gold/glowing = unlocked route`

`Subtle gold pulse = currently purchasable`

`Dark = locked`

I also think we should have **gold particles/light flow downward along unlocked connections very subtly**. Not a distracting constant animation, but enough that the tree feels alive.

When you buy a node, *that's* when we can make the animation much stronger: gold travels from the prerequisite node(s), converges on the new node, then the node lights up and its newly available branches appear/glow.

For an **AND node**, this could look particularly cool because all required paths converge simultaneously.

---

One thing I **would change from your sketch**: I wouldn't force every generation onto a perfectly straight horizontal row.

Slight vertical staggering will make the tree feel more organic and make complicated AND/OR connections much easier to read:

```text id="31q94f"
                [ ROOT ]

         [ A ]           [ B ]
              [ C ]

      [ D ]        [ E ]
                         [ F ]

             [ MAJOR NODE ]

       [ G ]          [ H ]
             [ I ]
```

Still fundamentally top → bottom, just not a rigid spreadsheet grid.

So I'd lock:

**Vertical top-to-bottom tree + branching horizontal exploration + free pan/zoom + glowing gold progression paths + organic/staggered node positioning.**

---

### Next decision: what should a node actually look like?

Before we decide what the starting upgrades *are*, I think this affects how much information we can comfortably put in the tree.

I'm picturing nodes as primarily **icons/artwork**, not rectangles full of text. Hovering/clicking one opens a larger detail card with:

**name, description, cost, prerequisites, current effect, and what purchasing it changes.**

That would let the actual tree stay visually clean even once we have 30–50 nodes.

I'd lean strongly toward that rather than putting names/descriptions directly inside every node.

## S8-M0067 - user - 2026-09-30T14:09:01.192000+00:00
Message ID: 57b06899-0fdf-4050-a338-3d09e1ec4d69

[Non-text attachment; not recovered]
yep thats good&#x20;

I'm picturing nodes as primarily **icons/artwork**, not rectangles full of text. Hovering/clicking one opens a larger detail card with: &#x20;

however i think they should all be a sqaure and then it has an icon in the middle (these can very)

## S8-M0068 - assistant - 2026-09-30T14:09:02.338439+00:00
Message ID: 506c7b61-2f37-473a-a51b-fbbea7ff52b5

Exactly. **Square node as the consistent container, unique icon/artwork as the identity of the upgrade.** I prefer that over changing the node shape based on upgrade type.

### Locked — Progression node design

Every purchasable node uses the same basic square frame:

```text id="gtun62"
        ┌───────────┐
        │           │
        │    ICON   │
        │           │
        └───────────┘
```

The **square itself communicates state**, while the **icon communicates what the upgrade is**.

For example:

```text id="c0r0v2"
┌───────┐     ┌───────┐     ┌───────┐
│   🃏  │     │   🎲  │     │   🛡  │
│       │     │       │     │       │
└───────┘     └───────┘     └───────┘
Lesser Fate    Fate Control   Tolerance
```

Those emojis are obviously placeholders—we'd create/use proper Wheelbound-style icons.

Then the **frame** handles progression:

```text id="j0gmbg"
LOCKED             AVAILABLE            PURCHASED

┌────────┐          ╔════════╗           ╔════════╗
│  icon  │          ║  icon  ║           ║ ✦icon✦ ║
└────────┘          ╚════════╝           ╚════════╝
 dark/dim           gold pulse           solid gold/glow
```

And the connecting lines follow the same visual language.

### Hover vs click

I'd actually use **both**, but for different purposes.

**Hover:** quick information—upgrade name, short description, cost.

**Click:** opens the full detail panel with description, exact effect, requirements, prerequisites, cost, and purchase button.

That keeps exploring the tree fast without accidentally buying something.

One thing I wouldn't do is put the FP price permanently underneath every square. Once there are 40+ nodes, you'll end up with a mess of numbers and labels. Hover/click can expose that information cleanly.

We could potentially show a tiny lock/check indicator in a corner of the square, but I'd first see whether the frame/glow communicates state well enough without it.

---

So our tree UI is becoming pretty concrete:

**Vertical top → bottom • square nodes • unique centered icons • organic branching • AND/OR prerequisites • gold glowing paths • pan/zoom • hover summary • click details/purchase • animated path illumination when purchasing.**

That's enough that I think we should **stop designing the generic tree UI now**. Otherwise we're going to over-design a screen before knowing what actually goes in it.

### Next: the Fate Upgrade tree root

We know a fresh account has **Standard Fate only**.

So now I'd make our next single decision:

**What are the first branches the player can buy from the root?**

Rather than me immediately inventing three branches, I want to build those around what *you* think the first meaningful choices should be. For example, one could begin working toward additional Fate Cards, another toward better control over rolls, another toward changing Fate XP/tolerance—but those are just possible directions, not decisions yet.

## S8-M0069 - user - 2026-09-30T14:21:09.949000+00:00
Message ID: 3aeda57b-8e74-4de4-b26a-0c84fd452e58

theres somekind of hover indication(maybe it also displays the name of the node)  and then when you click it will bring up a 'fate' card like UI and give a better description 


so as far as upgrades go heres what i have in mind 

You start off get your grand master task and then after youre done with tutorialisland (if they activate wheelbound on the island we ust spin their fate but let them complete the tutorial and then when on the mainland we comense the first spin, if they actiavte it on the mainland they spin their end goal fate and then once confirmed, then they spin their first fate after that) 

your wheel is very plain, im thinking just skilling/ questing off rip we give them some of the basics 

mining , fishing, woodcutting, combat (this would include mage, ranged, attach strength defense , hitpoints... i dont want to restrict combat too much because they will 100% need that)
really low level quests(side bar here i want to add something to the wheelbound mode and the general wheelbound wheels, i want an option to only include quest you currently qualify for, so that in wheelbound mode we give them quests they can actually do without having to grab like 10 levels in a skill) 


and then shop upgrade unlock more skills you can add to your wheel. (These would be nodes, ex the slayer node)

the wheel would have a max amount of options you can add to it (call it 24 for now) and then they build out their wheel accordingly 

so other nodes 
bossing nodes(maybe each boss is a node and its a tree you gotta unlock bosses to get to a specific one idk might be a lot) 

quest tier node, the lowest teir of quests are auto unlocked but higher tiers like hard, grandmaster are locked and will need to be unlocked 

shops rune shops general stores, the GE all will need to be unlocked, but heres the twist this this. You can gamble and not pay with points but you spin a 50/50 wheel and if you win it unlocks that specific store (ex the store in lumbridge) but if you fail you permantly lose access to that store

## S8-M0070 - assistant - 2026-09-30T14:21:10.867448+00:00
Message ID: bb60e110-f52d-4d10-947f-2db61e566cab

Yes. This gives us enough to start seeing what the **actual early-game Wheelbound loop** looks like. There are several good ideas here, but there are also a couple places I'd push back before we lock everything.

First, your node interaction is now clear and I'd lock it:

```text
NORMAL
┌─────────┐
│         │
│  ICON   │
│         │
└─────────┘

        ↓ hover

╔═════════╗
║  ICON   ║  ← subtle highlight/glow
╚═════════╝
  SLAYER       ← node name appears

        ↓ click

┌─────────────────────────────┐
│        FATE CARD            │
│                             │
│          [ICON]             │
│          SLAYER             │
│                             │
│ Unlocks Slayer as an        │
│ available Fate for your     │
│ Wheelbound wheel.           │
│                             │
│ Cost: 300 Fate Points       │
│                             │
│       [ UNLOCK ]            │
└─────────────────────────────┘
```

So **hover = highlight + name**, click = full Fate-card-style detail panel. Confirmed.

---

## Starting Wheelbound flow

I think what you described should also be locked conceptually.

If Wheelbound is activated **during Tutorial Island**, we should not interfere with completing Tutorial Island. The player can roll/confirm their Grandmaster/End Goal Fate there, but their first normal Fate waits until they reach the mainland.

If Wheelbound is activated **after Tutorial Island**, the sequence is:

**End Goal Fate → player confirms it → first normal Fate.**

That keeps every account beginning with the same major ritual without making Tutorial Island miserable.

And importantly, I'd have Wheelbound **track XP during Tutorial Island without treating it as unauthorized XP**. Tutorial Island should basically be a protected onboarding state.

---

## Starting Fate pool

I like the intentionally small starting wheel:

**Mining • Fishing • Woodcutting • Combat • Low-tier Questing**

And I strongly agree with **Combat being bundled**. Splitting Attack, Strength, Defence, Ranged, Magic and Hitpoints into individual restricted Fates would create bizarre situations and probably make progression unnecessarily frustrating.

We can determine exactly what activities Combat permits later.

### Your quest eligibility idea should apply beyond Wheelbound

This is a good general Wheelbound plugin feature:

> **Only include currently completable quests**

When enabled, the Quest wheel dynamically excludes quests for which the player does not currently satisfy requirements.

For **Wheelbound Mode**, I'd make that behavior mandatory. Otherwise:

> Fate chooses Quest X → player needs 53 Herblore → Herblore isn't available → account can't complete its Fate.

That's not difficulty; that's a deadlock.

For the regular/non-Wheelbound wheels, it can simply be an optional toggle as you suggested.

---

# The 24-slot Wheel

This is where I think you've landed on something much more important than it initially sounds.

I'd tentatively lock:

> **Wheel capacity: 24 slots (balance value TBD).**

The shop doesn't necessarily just make Wheelbound easier. It gives the player **more things they can choose to put into their Fate pool**.

So unlocking Slayer doesn't automatically mean Slayer must be on your wheel.

```text
UNLOCKED FATES

✓ Mining
✓ Fishing
✓ Woodcutting
✓ Combat
✓ Questing
✓ Slayer
✓ Hunter
✓ Farming
✓ Bossing
...

ACTIVE WHEEL
5 / 24 slots used
```

That means eventually players actually **build their own Wheelbound wheel**.

But there's a problem we need to come back to: if players can freely remove undesirable Fates, they'll optimize their wheel down to whatever is easiest. We will probably need rules governing adding/removing Fates. **I would not solve that yet**, but I'm flagging it.

---

## Bosses

I would **not** do one progression-tree node for every OSRS boss.

Your instinct that it might be too much is right.

We'd end up maintaining an enormous tree, and new bosses would continually expand it. Worse, most nodes would effectively just say:

> "You can now kill this boss."

I'd rather eventually explore **Boss Tiers / Boss Families / progression branches**, with certain especially important bosses potentially getting unique nodes.

We can design that later.

---

## Quest tiers

I like this.

Something conceptually like:

```text
LOW-TIER QUESTS
Starting access
      │
      ▼
MEDIUM QUESTS
      │
      ▼
HARD QUESTS
      │
      ▼
MASTER QUESTS
      │
      ▼
GRANDMASTER QUESTS
```

Exact OSRS classifications/requirements can be determined later.

That also creates a natural reason to invest Fate Points into Questing progression.

---

# Now, your shop gamble idea

**This is the part I'd be careful with.**

I really like:

> Pay Fate Points for guaranteed access  
> **OR** challenge Fate with a 50/50 spin for free.

That's extremely on-theme.

For example:

```text
                VARROCK RUNE SHOP

          Guaranteed Unlock
               400 FP

                  OR

              TEMPT FATE
                 50/50

             WIN        LOSE
              │           │
              ▼           ▼
           UNLOCK      ???
```

But I **don't like permanent account-level lockout as the default loss condition**.

Imagine someone hits:

> Lumbridge General Store — LOST FOREVER

five minutes into a Wheelbound account.

That's not necessarily an interesting restriction. It might just mean the player spends the next hundreds of hours thinking, *"Well, I permanently screwed this account because I clicked a coin flip on day one."*

Even worse with something important like the GE.

And statistically, if we have dozens of shops and players gamble frequently, they're practically guaranteed to permanently destroy access to quite a few of them.

**However... I don't want to throw away your idea.**

I think permanent loss could be exactly what makes the gamble exciting **if the player knows exactly what they're risking and deliberately chooses it**.

Something like:

```text
╔══════════════════════════════════╗
║           TEMPT FATE             ║
║                                  ║
║     Lumbridge General Store      ║
║                                  ║
║             50 / 50              ║
║                                  ║
║ WIN                              ║
║ Permanently unlock this shop.    ║
║                                  ║
║ LOSE                             ║
║ This shop becomes permanently    ║
║ inaccessible to Wheelbound.      ║
║                                  ║
║ This cannot be undone.           ║
║                                  ║
║       [ TEMPT FATE ]             ║
╚══════════════════════════════════╝
```

Then the player makes a genuine choice:

**Spend scarce FP and guarantee it, or put that specific unlock on the line.**

That feels very Wheelbound.

And visually, this absolutely deserves the actual **50/50 wheel animation**, rather than just generating a random boolean.

---

### One decision I want to settle before going further

There's a major distinction in what you said about shops.

You mentioned:

> rune shops, general stores, GE

but then your gamble example was specifically:

> **the store in Lumbridge**

So which level are we unlocking?

**Individual shops** — e.g. Bob's Brilliant Axes, Lumbridge General Store, Aubury's Rune Shop each have their own unlock.

Or **shop categories** — e.g. unlock "General Stores" and now all general stores are available.

Based on the gambling mechanic you just described, I'm actually leaning toward **individual shops** now. It creates many more meaningful small unlocks, and permanently losing one store doesn't necessarily destroy access to an entire category.

That's the next thing I'd settle.

## S8-M0071 - user - 2026-09-30T14:33:13.743000+00:00
Message ID: dab45866-488b-4519-b748-b9e410e18aed

confirm everything in this section : Starting Wheelbound flow

confirm everything in this section: Starting Fate pool

The 24-slot Wheel: adding would be cheaper than removing, removing would maybe require a lot event a fate of its own that particually awefull/ undersirable

**Bosses:  i like your idea of boss tiers, when you click into the node it should list all the bosses nessessary**

confirm everything in this section -Quest tiers

**Now, your shop gamble idea: Id want the option to gamble the shop to be very obvious or like these are the consequenses , if they decide to go that route thats on them. i would like an item or something to help revert this but only give out like 3, maybe it can be in the unlock tree&#x20;**

unlock shops individually but there isnt anything in the 'SHOP' its more like you walk up to vendor and you can unlock it that way, we have a whole side bar to play with as far as the UI goes so maybe while you can enter the shop you can also look at your 'Account' and see the unlocks, we categorize them by area&#x20;

lumbride - (all the shopes , and a status, unlocked , locked, banned)

## S8-M0072 - assistant - 2026-09-30T14:33:15.201432+00:00
Message ID: 60fbed3f-fafb-4e8c-9555-98a39790ee92

Yes. I'm locking all of that. This also clears up an important distinction: **Account Access isn't really a traditional shop**. The progression tree unlocks capabilities/mechanics, while individual RuneScape vendors are interacted with in-world.

### Confirmed decisions

**Starting Wheelbound flow:** A new Wheelbound account first receives its **End Goal / Grandmaster Fate**. If Wheelbound is activated on Tutorial Island, the End Goal can be rolled/confirmed there, but Tutorial Island remains unrestricted/protected so the player can finish it normally. The first normal Fate begins after arriving on the mainland. If activated on the mainland, it's End Goal spin → confirmation → first normal Fate. Tutorial Island XP does not cause Fate violations.

**Starting Fate pool:** The basic wheel starts with **Mining, Fishing, Woodcutting, Combat, and low-tier Questing**. Combat encompasses Attack, Strength, Defence, Hitpoints, Ranged, and Magic rather than restricting those independently. Wheelbound quest rolls only select quests the account currently qualifies to complete. The general-purpose Quest wheel gets an optional **"Only quests I currently qualify for"** setting.

**24-slot wheel:** Current working maximum is **24 active Fate slots**, subject to balancing later. Unlocking something does not necessarily force it onto the active wheel. Adding an unlocked Fate should be relatively inexpensive. **Removing an active Fate is intentionally much more difficult.**

I especially like your idea that removal itself can become a punishment mechanic. I'm recording this as:

> **CONFIRMED DIRECTION:** Removing a Fate is substantially more expensive/difficult than adding one. Removal may require completing a special, particularly undesirable Fate rather than—or in addition to—paying Fate Points. Exact removal mechanic TBD.

That's important because otherwise players eventually construct a 24-slot wheel containing only their favorite activities, defeating much of Wheelbound's premise.

**Boss progression:** Bosses use **tier/group nodes**, not one progression node per boss. Clicking a Boss node opens its Fate-card detail UI and explicitly lists **every boss that node will unlock/require**, so there's no ambiguity before purchasing it. Exact boss groupings come later.

**Quest tiers:** Confirmed as progressive unlocks. Low-level quest access exists at the start; progressively higher quest tiers require progression-tree nodes, eventually reaching Master/Grandmaster content. We'll determine the exact classifications and exceptions later.

---

## Individual shops + gambling

I like your clarification more than the centralized shop UI I was picturing.

**Individual RuneScape shops/vendors are individually restricted.** There isn't a Wheelbound screen where you browse Rune Shops and purchase Aubury from a list.

Instead, you actually encounter the vendor in RuneScape.

For example, you walk up to a locked shop and attempt to trade:

```text id="vq3g5e"
        AUBURY'S RUNE SHOP

This shop has not been unlocked.

        GUARANTEED
        Unlock: 250 FP

             OR

         TEMPT FATE
            50 / 50

WIN
Permanently unlock Aubury's Rune Shop.

LOSE
This shop becomes BANNED.

       [UNLOCK] [TEMPT FATE]
```

The gamble consequences should be **extremely explicit**. No vague "Are you sure?" message. It tells you outright that losing bans this individual shop.

Then we give them the actual dramatic 50/50 wheel.

**WIN → UNLOCKED**

**LOSE → BANNED**

And yes, if they knowingly press that button after being shown the consequences, that's part of the game mode.

### Limited ban restoration

I also really like having a tiny number of ways to undo those mistakes because it preserves the severity without making them literally unrecoverable.

I'm recording:

> **CONFIRMED:** A rare progression-tree unlock provides a limited means of restoring BANNED shops. The intended lifetime supply is extremely small, currently approximately **3 restorations per account**. Exact name, location in tree, cost and acquisition mechanics TBD.

I'd actually prefer **charges** over giving the player three physical OSRS items, because RuneLite shouldn't pretend to create real RuneScape inventory items. But visually we could absolutely represent them as something thematic like three seals/tokens inside Wheelbound.

For example:

**Fate's Pardon — 3 charges**

Spend one charge to change one shop from `BANNED → LOCKED`.

Notice I said **LOCKED**, not `BANNED → UNLOCKED`.

I think that's an important cost. You screwed up the gamble, spent one of your three incredibly scarce pardons, and now you've merely regained the **opportunity to unlock that shop**. You still need to pay its FP price—or, if we're evil enough to allow it, gamble again. 😄

I wouldn't let the pardon itself grant the shop.

---

## Account screen

This solves the "where do I see all this?" problem nicely.

The **Account** section becomes the player's Wheelbound ledger/status screen. Shops can be grouped geographically:

```text id="w6mkse"
ACCOUNT → SHOP ACCESS

🔎 Search shops...              [Status ▼]

LUMBRIDGE
────────────────────────────────────

Lumbridge General Store       UNLOCKED
Bob's Brilliant Axes          LOCKED
Culinaromancer's Chest        LOCKED
...

VARROCK
────────────────────────────────────

Aubury's Rune Shop            BANNED
Varrock Swordshop             UNLOCKED
Varrock General Store         LOCKED
...
```

And we'd use three extremely clear states:

**UNLOCKED** — usable normally.  
**LOCKED** — hasn't been unlocked yet; can still be obtained.  
**BANNED** — lost through Tempt Fate; unusable unless one of the limited restoration mechanics is spent.

Search/filtering makes sense here too, especially once we're tracking a large number of vendors.

We can categorize primarily by **area**, exactly as you suggested: Lumbridge, Varrock, Falador, etc.

One thing I'd keep separate: this **Account screen isn't one of the three progression trees**. It's a status/management interface showing what the account has actually earned/lost. That's a much cleaner distinction.

---

### One issue before we move on

There's a potentially exploitable part of individual shop unlocking that we should settle.

If I walk up to Lumbridge General Store while it's LOCKED, should Wheelbound actually **prevent me from trading until I unlock it**, or are these restrictions ultimately honor-system rules that the plugin detects and penalizes if violated?

Earlier we deliberately moved skill restrictions away from hard-blocking and toward XP tolerance. **Shops are different**, because there's no XP signal that reliably tells us someone bought an item.

So for shops, I think we may actually need **hard enforcement**: intercept the trade interaction, show the Wheelbound unlock UI, and only allow normal trading once that individual shop is `UNLOCKED`.

That's the next piece I'd settle—and we'll eventually need to verify exactly what RuneLite/Plugin Hub permits before implementation.

## S8-M0073 - user - 2026-09-30T15:09:15.318000+00:00
Message ID: f2569690-2ffc-4dfc-a4c1-eea4fe1f228e

it should prevent you from trading, a different message for if its locked vs you have been banned. This will be a hard enforce. Now how does this interation work in general? 

So my plugin is as follows. The wheelbound game mode is optional when you download the plugin you are going to be brought to what we have today which is some wheels that ar fun to spin. Then you can opt into the wheelbound game mode which is what we have been working on. This should now enable certain settings like all this restriction stuff. Within this game mode we need to have the option for the person to stop the challaneg or since this is the 1st iteration to 'pause' and things would return to normal (any restrictions are no longer set) same with if you uninstall the plugin. I dont want any lingering effects basically and these modes needs to be separate

## S8-M0074 - assistant - 2026-09-30T15:09:17.326071+00:00
Message ID: 22af9147-a313-4bc5-8a15-d5fdfed6bc0b

Yes. I think this establishes an important architectural rule:

> **Wheelbound Mode is a self-contained game mode layered on top of the existing Wheelbound plugin. It does not replace the existing plugin.**

The plugin should essentially have two separate experiences.

```text
WHEELBOUND PLUGIN
│
├── NORMAL MODE
│   ├── Existing fun wheels
│   ├── Boss wheels
│   ├── Quest wheels
│   ├── Current plugin features
│   └── NO account restrictions
│
└── WHEELBOUND MODE [OPT-IN]
    ├── End Goal Fate
    ├── Active Fate
    ├── Fate Points
    ├── Progression Trees
    ├── Bounties
    ├── Shop restrictions
    ├── XP tolerance
    ├── Wheel building
    └── All challenge rules/enforcement
```

**Confirmed.**

### Hard-enforced shops

Also confirmed: when Wheelbound Mode is actively running, locked/banned shops are actually blocked.

A **LOCKED** vendor gets something along the lines of:

> **This shop is locked by Fate.**  
> Unlock this shop for 250 Fate Points or Tempt Fate for a 50/50 chance.

That can lead into the unlock/gamble UI we've discussed.

A **BANNED** vendor gets a substantially different message:

> **Fate has forsaken this shop.**  
> You lost access to this shop after Tempting Fate.  
> A Fate's Pardon is required to restore it.

And the trade never occurs.

We'll verify the technical RuneLite implementation and Plugin Hub rules before coding that system.

---

## Pausing Wheelbound

I think **Pause Challenge** is absolutely necessary, especially for version one.

When paused:

```text
WHEELBOUND MODE
STATUS: PAUSED

✓ XP restrictions disabled
✓ Shop restrictions disabled
✓ Fate violations disabled
✓ No penalties
✓ Normal RuneScape interactions restored
✓ Current Fate preserved
✓ Fate Points preserved
✓ Progression preserved
✓ Bounties preserved
```

Then:

> **Resume Wheelbound**

puts the restrictions back and continues the account where it left off.

I would **not** allow progress while paused to count toward Wheelbound.

That's the important counterbalance.

If your Fate is:

> Gain 50,000 Mining XP

and you're at:

> 21,350 / 50,000

you pause Wheelbound and gain 500k Mining XP.

When you resume:

> **21,350 / 50,000**

Likewise, getting an Imbued Heart while paused **doesn't complete the Imbued Heart Bounty**.

Otherwise Pause becomes an enormous loophole:

> *"I'll pause Wheelbound, get everything I need, then resume."*

That's fine if somebody wants to do it—the plugin isn't trying to police how they play RuneScape—but **Wheelbound won't recognize that progress**.

### Pausing also needs a snapshot

There's a technical/design implication here I'd want in our blueprint.

When pausing, Wheelbound should record the relevant state. When resuming, it establishes a fresh baseline so XP/items acquired while paused aren't accidentally interpreted as having happened during the Fate.

In other words:

```text
ACTIVE
Mining Fate: 21,350 / 50,000

       ↓ PAUSE

PAUSED
RuneScape behaves normally.
Wheelbound progression frozen.

       ↓ RESUME

ACTIVE
Mining Fate: 21,350 / 50,000
Restrictions restored.
```

---

## Uninstalling/disabling the plugin

This is actually one of the advantages of doing restrictions through RuneLite rather than trying to alter anything about the RuneScape account itself.

**Wheelbound should never make a persistent change to the actual OSRS account.**

`BANNED` means:

> banned **according to Wheelbound Mode**

not:

> anything has happened to the RuneScape account.

Therefore, if the plugin is disabled/uninstalled/not running, there is nothing present to intercept interactions.

The player simply has normal RuneScape again.

Likewise, things such as Fate Points, banned shops, Fate Cards, progression nodes, etc. are **Wheelbound state**, not RuneScape state.

That's an architectural principle I'd explicitly put in the eventual spec:

> **Wheelbound enforcement exists only while Wheelbound Mode is ACTIVE. No Wheelbound restriction may persist outside the plugin or modify the underlying RuneScape account.**

---

## Stopping vs pausing

Here's one place where I think we should be careful.

I'd have **three states**, not just on/off:

**Not Started → Active ↔ Paused → Abandoned**

Pause is harmless and reversible.

But **Abandon Wheelbound Challenge** is different. That means the run is over.

I would *not* make that a casual toggle beside Pause. It should require a very explicit confirmation because otherwise someone could accidentally wipe/terminate a long-running Wheelbound account.

And initially I don't think we even need to delete the data when they abandon. We could preserve it as a completed/abandoned run record while disabling every restriction.

---

### One thing I would push back on

I would **not make uninstalling equivalent to abandoning the challenge**.

If someone removes RuneLite, changes computer, disables the plugin for troubleshooting, etc., we shouldn't interpret that as:

> *YOU ABANDONED WHEELBOUND.*

It should simply mean **Wheelbound isn't enforcing anything because it isn't running**.

If they reinstall/re-enable and their saved Wheelbound state is available, they can continue.

That also means someone *can* cheat by disabling the plugin.

I'm completely okay with that for this kind of plugin. Trying to make an anti-cheat system around a voluntary RuneLite challenge would add enormous complexity and probably make the legitimate experience worse.

---

### Next decision: what happens when Resume detects outside progress?

This is the one hole created by Pause.

Suppose I pause at 50 Mining, go play normal RuneScape for a month, and come back at **85 Mining**.

Obviously we shouldn't subtract levels or punish the player.

My inclination is:

> **Accept the RuneScape account's new state as the new baseline, preserve the unfinished Fate's existing progress, and continue Wheelbound from there.**

So Wheelbound is strict **while ACTIVE**, but deliberately doesn't care what happened while PAUSED.

I think that's the cleanest philosophy for the entire mode.

## S8-M0075 - user - 2026-09-30T15:13:40.624000+00:00
Message ID: 06e95059-bff2-426c-ba28-f66f6cef4092

yes valid

### One thing I would push back on

I would **not make uninstalling equivalent to abandoning the challenge**.



and the pause mechanic is so that if something is inherently broken or maybe they hard hard locked they can kinda fix themsleves since i myself dont have enough time to run theough this (which brings me to another part testing everything.... how are we doing that  id almost want a debug mode where i can say i competed stuff and kinda run through everything) or have you simulate runs to see if 100 spins is enought etc&#x20;



when paused anything gained outside of the bound mode does not count. This mean if you get a crazy drrop that would have counted towards a bounty is does not count. if you mine or gain exp while paused it does not count.

## S8-M0076 - assistant - 2026-09-30T15:13:45.811219+00:00
Message ID: baf44889-06b1-46ba-9eab-77fc51f0ed5a

Yes. That actually clarifies the philosophy of Pause:

> **Pause is an escape hatch, not part of the Wheelbound strategy.**

If we screwed up a rule, a RuneLite update breaks something, the player discovers an unforeseen hard-lock, or they simply need to temporarily play normally, they can pause without destroying their Wheelbound run.

And confirmed: **nothing accomplished while paused counts retroactively.** XP doesn't advance a Fate, drops don't complete Bounties, quests don't complete Wheelbound objectives, etc. When resumed, Wheelbound takes the current account state as its new baseline and continues.

## Testing/debug mode

I think we **absolutely need this**, and I would design it as a first-class development system rather than adding temporary cheat buttons while we're coding.

I'd call it something like **Developer Mode** internally. It should be completely separate from a legitimate Wheelbound run.

For you, it could expose a debug panel with controls along these lines:

```text
WHEELBOUND — DEVELOPER TOOLS

RUN STATE
[ Start Fresh Test Run ]
[ Pause ] [ Resume ] [ Reset ]

FATE
Current: Mining — 50,000 XP
Progress: 12,450

[ +1,000 XP ]
[ +10,000 XP ]
[ Complete Fate ]
[ Fail Fate ]
[ Reroll Fate ]

FATE POINTS
Current: 1,250
[ +100 ] [ +1,000 ] [ -100 ]

PROGRESSION
[ Unlock Node ]
[ Lock Node ]
[ Unlock Entire Tree ]
[ Reset Tree ]

BOUNTIES
Imbued Heart
[ Trigger Drop ]
[ Complete ]
[ Claim ]
[ Reset ]

SHOPS
Lumbridge General Store
[ LOCKED ▼ ]
  LOCKED
  UNLOCKED
  BANNED

[ Simulate Gamble Win ]
[ Simulate Gamble Loss ]

ACCOUNT
[ Set Skill Levels ]
[ Simulate Quest Completion ]
[ Set Wheel Slots ]
```

That lets you test a **100-hour progression path in 15 minutes**.

Want to see the Legendary Bounty animation? Trigger it.

Want to know what happens after banning three stores and using all three Pardons? Set that state.

Want to test an AND node? Unlock A/B, leave C locked, verify D stays locked, then unlock C and make sure the gold animation fires.

Want to see a level-95 Mining Fate without training an account to 95 Mining? Simulate it.

### But debug state needs to be visibly contaminated

Here's where I'd be strict.

The moment Developer Mode manipulates a run, that run should become:

> **TEST RUN / DEBUG RUN**

and never be confused with a legitimate Wheelbound run.

That means we don't accidentally test `+50,000 FP`, turn Developer Mode off, and now have a "legitimate" account with those points.

For development, I'd actually prefer a **separate test profile/save** entirely.

---

## Simulation testing is different — and we should do both

The debug panel tests whether the **plugin works**.

Simulation tests whether the **game design works**.

Those are different problems.

We can build a simulator that doesn't even need RuneScape running. Give it the Wheelbound rules and simulate thousands or millions of theoretical accounts.

For example:

```text
SIMULATION

Accounts: 100,000
Fates per account: 100

Starting Wheel:
Mining
Fishing
Woodcutting
Combat
Questing

Run...
```

Then collect things like:

```text
Average Fate Points after 100 Fates
Median unlocked nodes
Average skill progression
Average number of shop gambles
Average banned shops
% reaching each Quest tier
% encountering a progression deadlock
Average wheel composition
Most/least selected Fate
Average Fate duration
Bounty completion rates
How often players go negative FP
```

This will be **extremely useful** for those XP brackets we were discussing.

Instead of us guessing:

> Level 61–75 should be 60k–120k.

we can model different ranges and see what they do to progression.

Same with:

> Is 24 wheel slots too many?  
> Are 100 Fates enough to progress meaningfully?  
> Does Slayer cost 500 or 1,500 FP?  
> How quickly can someone reach Grandmaster quests?  
> Are players accumulating FP much faster than they can spend it?

We can simulate those economies before you spend hundreds of hours playtesting.

### One limitation I want us to remember

Simulation **cannot tell us whether Wheelbound is fun**.

We could mathematically produce a beautifully balanced system where the average player reaches X after exactly 87 Fates—and then discover that doing it feels awful.

So I'd eventually use three layers:

**Automated tests** verify rules and calculations → **simulation** tests progression/economy/deadlocks at scale → **Developer Mode + actual playtesting** tests the real RuneLite experience and whether it's fun.

That should become an entire `TESTING.md` section of our blueprint.

---

And I think your reason for Pause should be recorded explicitly:

> **Wheelbound is expected to evolve. Pause exists partly as a safety mechanism for unforeseen hard-locks, bugs, balancing mistakes, or unsupported game situations. Players can temporarily leave enforcement, resolve the issue, and resume without receiving Wheelbound credit for anything accomplished while paused.**

That's much better than pretending version 1 will account for every weird OSRS interaction.

### One testing decision I'd make now

I think **Developer Mode should be inaccessible by accident**. Not a giant button beside "Pause Wheelbound."

I'd put something like **Enable Developer Tools** in advanced plugin configuration, with a warning that enabling/manipulating a run creates a **TEST RUN**.

That gives you everything you need during development without normal players accidentally finding `+10,000 Fate Points`.

## S8-M0077 - user - 2026-09-30T15:16:24.603000+00:00
Message ID: 36dfc9a8-46d0-4bf6-983a-ffea55465340

Simulation **cannot tell us whether Wheelbound is fun**.  &#x20;
I think it may be fun T_T, but yes dont worry about this if its all for not thats fine .&#x20;

we will need to make sure devleoper mode cannot be accessed when released but otherwise go crazy with the things available to me in that mode

## S8-M0078 - assistant - 2026-09-30T15:16:27.109177+00:00
Message ID: 42e02506-cdef-4181-b9b3-d3dd0dd41d9d

😂 I think it has a legitimate shot at being fun. More importantly, we're designing enough escape valves that if one mechanic sucks in practice, we can change it without rebuilding the whole mode.

And yes, let's make **Developer Mode extremely powerful**.

### Confirmed — Developer Mode is development-build only

I want to go further than hiding it in Settings.

**The public Plugin Hub release should not contain an accessible Developer Mode at all.**

During development, we can have a build/configuration flag:

```text
DEVELOPMENT BUILD
────────────────────────
Developer Tools: ENABLED
Simulation Tools: ENABLED
State Editing: ENABLED
Debug Logging: ENABLED
```

Release build:

```text
PUBLIC RELEASE
────────────────────────
Developer Tools: NOT AVAILABLE
Simulation Tools: NOT AVAILABLE
State Editing: NOT AVAILABLE
```

Ideally the dangerous debug functionality is excluded from the release build rather than merely having:

```java
if (developerMode)
{
    showCheatMenu();
}
```

with a secret way of toggling it.

That also protects us from accidentally shipping something that lets players manipulate their Wheelbound state.

### And for our development build: go nuts

I want us to be able to manipulate basically **every piece of Wheelbound state**.

Not just convenience buttons like `+500 FP`. Eventually I'd want:

```text id="47ncv1"
DEVELOPER TOOLS

FATE
• Force any wheel result
• Generate Fate
• Set exact XP requirement
• Add/subtract Fate progress
• Complete Fate
• Fail Fate
• Trigger tolerance violation
• Force Lesser / Standard / Greater / Challenge
• Simulate restricted XP

WHEEL
• Unlock any Fate
• Add/remove Fate
• Ignore 24-slot capacity
• Fill wheel automatically
• Force wheel result
• Reset wheel

PROGRESSION
• Unlock/lock individual node
• Satisfy individual prerequisites
• Test AND paths
• Test OR paths
• Unlock entire tree
• Reset entire tree
• Give/remove FP

BOUNTIES
• Trigger any bounty
• Mark Unclaimed
• Claim
• Reset
• Trigger Common → Legendary animations
• Search/filter test data

SHOPS
• Select any vendor
• LOCKED / UNLOCKED / BANNED
• Force 50/50 WIN
• Force 50/50 LOSS
• Consume/restore Fate's Pardon
• Test blocked trade interaction

ACCOUNT
• Mock skill levels
• Mock quest eligibility
• Mock boss access
• Mock Tutorial Island state
• Mock mainland activation

RUN
• Fresh Wheelbound run
• Pause/resume
• Abandon
• Save/load test states
• Jump to predefined scenarios
```

**Predefined scenarios** will be particularly useful.

Instead of manually configuring fifteen things every time, we could eventually have:

> `Fresh Account`  
> `Midgame Account`  
> `Endgame Account`  
> `Negative Fate Points`  
> `23/24 Wheel Slots`  
> `All Shops Locked`  
> `Multiple Banned Shops`  
> `Legendary Bounty Unclaimed`  
> `Hard-Locked Account`  
> `Nearly Completed Progression Tree`

Click one and instantly reproduce that state.

That will make UI development **much** faster.

And I'd want an **event/debug log** showing exactly what Wheelbound thinks happened:

```text id="xxf8ss"
09:41:02 Fate generated: Mining
09:41:02 Level bracket: 46–60
09:41:02 Base range: 30k–60k
09:41:02 Mining modifier: 0.90
09:41:02 Final requirement: 40,000 XP

09:43:17 Unauthorized XP: Fletching +125
09:43:17 Tolerance: 125 / 1,000

09:45:52 Bounty detected: Imbued Heart
09:45:52 State: COMPLETED_UNCLAIMED
```

That will be invaluable when something goes wrong and we're trying to figure out **why**.

---

One slight distinction I'd make: the **mass simulation engine doesn't necessarily need to be inside RuneLite**. It may be cleaner as tests/tooling that can run thousands of fake Wheelbound accounts without rendering anything. Developer Mode is for manipulating and visually testing the actual plugin.

So this is now firmly part of our architecture:

> **Release Wheelbound** — normal players only.  
> **Development Wheelbound** — absurd amount of control for us.  
> **Simulation/Test tooling** — automated balancing and rule validation.

And yes, if after all this we discover that Wheelbound is terrible, we'll at least have constructed an exceptionally sophisticated system for discovering that. 😂

### Next design decision

I think we've wandered into testing because it was important, but we should return to the **starting progression tree**.

We now know quite a few things that can become nodes: additional skills like Slayer, Quest tiers, Fate Cards, wheel manipulation, shop-related upgrades, Fate's Pardon, etc.

I'd start with the **first node after the root** next, because that determines what the player is first taught to spend Fate Points on.

## S8-M0079 - user - 2026-09-30T15:18:22.765000+00:00
Message ID: 5c5e904a-fba8-4f2d-a381-c18d302ff1ef

I'd start with the **first node after the root** next, because that determines what the player is first taught to spend Fate Points on. &#x20;

what did you have in mind, i figure the skills and bossing quests unlocks would all be their own branch with specifics&#x20;

what did you want to do for the the first one? Maybe have it be free and grant you some starting points ?

## S8-M0080 - assistant - 2026-09-30T15:18:25.109668+00:00
Message ID: 1b96d6cb-9c48-4088-a372-1e63f11d2ca3

That's actually close to what I had in mind, but I think your idea of making the root **free and giving starting Fate Points** solves an onboarding problem nicely.

I wouldn't make the first *real* upgrade something like Slayer. Instead, I'd make the root a ceremonial **"enter the progression system"** node.

Something like:

```text
                         ┌───────────┐
                         │   FATE    │
                         │   ICON    │
                         └─────┬─────┘
                          THE BOUND
                         Cost: FREE
                         + Starting FP
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
          SKILLING         QUESTING         BOSSING
             ...              ...              ...

                 other branches develop
                    further down...
```

The name is just placeholder—I wouldn't lock **The Bound**.

### Why I like a free root node

The player finishes their initial setup, completes some first milestone—we'll decide exactly when—and the tree introduces itself:

> **A new path has opened.**

They click the glowing root node for the first time. It costs **0 FP**.

Then we get to teach the entire tree interaction in a safe way:

**hover → see node name → click → Fate Card opens → read reward → Unlock**

They press Unlock and we do our first gold-path animation.

Then:

> **+250 Fate Points** *(placeholder)*

The paths beneath it illuminate and suddenly they have currency they can actually spend.

That's basically a tutorial disguised as progression.

### I would not give the starting FP before this

That's the part I'd be opinionated about.

If we start the account with:

> Fate Points: 250

they don't know what those are yet.

Instead:

```text
START WHEELBOUND
      ↓
End Goal Fate
      ↓
First normal Fate(s)
      ↓
Progression becomes available
      ↓
FREE ROOT NODE
      ↓
Gold activation animation
      ↓
+250 FP
      ↓
First upgrades become available
```

Now the player learns:

**"Oh. These points are what let me shape my Wheelbound account."**

That's much cleaner onboarding.

---

And yes, I agree with your branch thinking, with one slight distinction.

I don't think **Skills, Bosses and Quests should necessarily be the three entire top-level branches** of the Fate tree. They can certainly have dedicated branches, but we've already got other major systems that need somewhere to live:

```text
                       ROOT
                         │
       ┌─────────────────┼──────────────────┐
       ▼                 ▼                  ▼

   CONTENT            FATE CONTROL       ??? ETC
      │                    │
 ┌────┼────┐          ┌────┼─────┐
 ▼    ▼    ▼          ▼    ▼     ▼
Skills Quest Bosses  Cards Rolls Wheel
```

That's only conceptual.

We might discover that **Slayer → Bossing → harder bosses** naturally intertwine, or that Quest tiers should branch separately. I don't want to force the topology before we've listed what the tree actually contains.

Also remember we currently have **three progression trees**, not one mega-tree: Fate Upgrades, Account Access, and Protection. So some mechanics shouldn't be squeezed into this tree at all.

---

### The one decision I'd make now

I like your root idea enough that I'd propose locking:

> **Each progression tree begins with a free introductory/root node. Activating it teaches the tree interaction, triggers the initial gold-path animation, and may grant that tree's initial resources/benefit.**

But I **wouldn't automatically give starting Fate Points from every tree root**. Otherwise players could open three trees and immediately collect three piles of free currency.

For the **first/main Fate progression root**, though, I like a one-time starting FP grant.

The next thing I'd decide is **when that root becomes available**. My preference is **after completing the player's first normal Fate**, rather than immediately after character setup. That means they've actually experienced the restrictive version of Wheelbound once before we hand them the ability to start changing it.

## S8-M0081 - user - 2026-09-30T15:20:10.972000+00:00
Message ID: e86e6f37-d5f4-4c4c-afed-72e17dee5e3e

no a single free node to kick off the tree so you have a starting node and then directly under it is a free node and then we branch off

## S8-M0082 - assistant - 2026-09-30T15:20:11.785152+00:00
Message ID: bd03c239-4cb2-4c6a-b097-d7fb029ae077

Ahh, yes — I misunderstood. You want the **starting/root node to represent the tree itself**, and then a single free node directly below it acts as the player's first actual activation before everything branches.

I like that better:

```text
                         ┌───────────┐
                         │   ROOT    │
                         │   ICON    │
                         └─────┬─────┘
                               │
                               │
                         ┌─────▼─────┐
                         │   FIRST   │
                         │   NODE    │
                         │   FREE    │
                         └─────┬─────┘
                               │
                 ╔═════════════╧═════════════╗
                 ║             ║             ║
                 ▼             ▼             ▼
              [ NODE ]      [ NODE ]      [ NODE ]
                 │             │             │
                ...           ...           ...
```

### Locked

The progression tree has a **starting/root node**, followed by **one free introductory node**. The player unlocks that free node, gets the activation animation/gold path, and **only then does the tree branch into the real choices**.

I also still like your idea that this free node grants some starting Fate Points. That makes it essentially:

> **Here's the progression system. Here's your first bit of currency. Now choose what direction you want to take your account.**

And importantly, those first branches don't have to be artificially limited to three. Visually we could have four or five paths spreading downward/outward if that's what the design calls for.

I'd also make the **root already illuminated**. Then the first free node beneath it is glowing/pulsing as `AVAILABLE`. Clicking and claiming it causes that first gold beam to travel downward from the root and activate it. That's the player's introduction to how the entire tree works.

So now the interesting part is actually **what that free node represents**. I don't think it should just be called "Free Node +250 FP." It should have some thematic meaning—essentially the moment the player begins gaining some control over Fate.

We can figure out its name/theme later, though. The structural decision is solid.

## S8-M0083 - user - 2026-09-30T15:30:32.382000+00:00
Message ID: 91f6e604-ad0f-41ae-8186-ac96a01468a2

yes do this&#x20;

I'd also make the **root already illuminated**. Then the first free node beneath it is glowing/pulsing as `AVAILABLE`. Clicking and claiming it causes that first gold beam to travel downward from the root and activate it. That's the player's introduction to how the entire tree works.

## S8-M0084 - assistant - 2026-09-30T15:30:33.716380+00:00
Message ID: 490c3fb2-1c40-44e0-93e4-76bffdccadb1

Confirmed. That's now the **standard opening interaction for the progression tree**.

```text
                ┌───────────┐
                │   ROOT    │
                │           │
                │    ◈      │
                └─────┬─────┘
                      ║
                 already gold
                      ║
                ┌─────▼─────┐
                │  FREE     │  ← softly pulsing gold
                │  NODE     │
                └─────┬─────┘
                      │
                locked/dark paths
                      │
          ┌───────────┼───────────┐
          ▼           ▼           ▼
        [???]       [???]       [???]
```

When the player clicks the free node, they still get the normal **Fate-card detail UI**, except the cost shows **FREE**. When they confirm it, the gold energy travels from the illuminated root → down the connection → into the node. The node activates, its icon/frame becomes permanently illuminated, and then the paths beneath it illuminate to show the player's **first real purchasable choices**.

That should be a slightly more dramatic animation than purchasing an ordinary node because it's teaching the player visually:

> **Gold path = where you've been. Glowing nodes/paths = where you can go. Dark = not yet accessible.**

We'll also keep the idea that this free node gives the player their **initial Fate Points**, with the amount TBD until we build the economy.

I think we've nailed that interaction enough to move on.

**Next single decision:** let's decide what the **first branches underneath that free node** actually are. I think this is where we should start laying out the real Fate tree rather than talking about it abstractly.

## S8-M0085 - user - 2026-09-30T15:37:38.962000+00:00
Message ID: 5f976d39-e2ea-4ecc-8303-10e5111a348c

sounds good what are some suggestions

## S8-M0086 - assistant - 2026-09-30T15:37:40.000178+00:00
Message ID: 6fa1396f-d205-47e5-b38f-b235c2efab35

I think the first layer should introduce the player to the **main ways they can shape their account**, rather than immediately dumping 15 individual skills in front of them.

I'd start with **four branches** beneath the free node:

```text id="cl9uwu"
                         [ ROOT ]
                            ║
                       [ FREE NODE ]
                            ║
          ╔══════════╦══════╩══════╦══════════╗
          ▼          ▼             ▼          ▼
       SKILLING    QUESTING      COMBAT     FATECRAFT
          │          │             │          │
         ...        ...           ...        ...
```

Names are placeholders.

**Skilling** would lead into individual skill unlocks. The starting skills—Mining, Fishing and Woodcutting—don't need nodes because they're already part of the starting wheel. This branch starts exposing things like Slayer, Agility, Thieving, Hunter, Farming, Herblore, Crafting, etc. Some can have prerequisites between one another where it makes sense.

**Questing** handles the quest-tier progression we've already confirmed. You already have basic/low-level questing, so this starts moving toward increasingly advanced quest tiers and potentially special quest-related upgrades.

**Combat/Bossing** is where I'd put the boss-tier progression. Basic Combat is already unlocked, but this branch gradually opens actual boss Fates. The boss nodes show every boss contained in that tier when clicked, as we've established.

Then I'd have something different for the fourth branch—I'm calling it **Fatecraft** just to explain the idea. This is where you start manipulating the *Wheelbound system itself* rather than unlocking RuneScape content:

```text id="xij6e7"
FATECRAFT
    │
    ├── Lesser Fate Card
    │
    ├── Greater Fate Card
    │
    ├── Challenge Fate Card
    │
    ├── Fate roll manipulation
    │
    ├── Wheel capacity upgrades?
    │
    └── other Fate mechanics
```

I'm **not sold on the name Fatecraft**, but I am sold on that concept having its own branch.

### One thing I'd change from our earlier thinking

I'm starting to think **these should all be branches of ONE large Fate progression tree**, rather than us having separate "Fate Upgrades / Account Access / Protection" trees.

Your ideas are becoming interconnected enough that separate trees may actually make the experience worse.

For example:

```text id="xzyu19"
                       SKILLING
                           │
                       [SLAYER]
                         /     \
                        /       \
              [BOSS TIER]      [BOUNTY BONUS?]
```

Or a deep Fate branch could eventually connect into one of the extremely limited **Fate's Pardon** charges.

A big vertical tree with multiple major branches would also make the Path-of-Exile-style navigation you wanted actually worthwhile.

I'd still keep **Bounties completely separate as their searchable list** and **Account as the status/ledger screen**.

So the sidebar could eventually be conceptually:

```text id="dmtmft"
WHEELBOUND

Wheel
Current Fate
Progression Tree    ← one big tree
Bounties            ← searchable catalog
Account             ← shops/unlocks/run status
```

I think that's cleaner than having the player navigate three different trees.

### One concern with my own four-branch suggestion

I **wouldn't necessarily put all four immediately purchasable one node below the free node**.

If we do that, the player can potentially just dump all their starting FP into the Fate-control branch and ignore actual content progression.

We could instead make the first layer extremely cheap and foundational:

```text id="ntx44r"
                       [FREE NODE]
                            │
             ┌──────────────┼──────────────┐
             ▼              ▼              ▼
        NEW HORIZONS    FATE'S HAND    BLOOD & GLORY
           50 FP           50 FP            50 FP
             │              │                │
          SKILLS +       FATE CARDS       COMBAT +
          QUESTS          / CONTROL        BOSSING
```

But I'm not convinced that's better either—it risks creating filler nodes, which we specifically said we don't want.

**My preference right now is the straightforward four-way split**: Skilling, Questing, Combat/Bossing, and Fate Manipulation. Every first node should immediately do something useful.

Then deeper down, those branches can split, reconnect with AND/OR requirements, and become much more interesting.

If you like those four broad directions, **the next thing I'd tackle is the Skilling branch specifically** and figure out how unlocking individual skills should actually be laid out.

## S8-M0087 - user - 2026-09-30T15:50:32.398000+00:00
Message ID: 975343a8-94b1-48de-8ab1-47090c1e47b1

yes lets moev forward with this&#x20;

**My preference right now is the straightforward four-way split**: Skilling, Questing, Combat/Bossing, and Fate Manipulation. Every first node should immediately do something useful.
Then deeper down, those branches can split, reconnect with AND/OR requirements, and become much more interesting.

## S8-M0088 - assistant - 2026-09-30T15:50:34.237307+00:00
Message ID: 827d2d4d-6333-4dae-8953-c757b50f4033

Confirmed. The **first major split of the Wheelbound progression tree** is now:

```text
                           [ ROOT ]
                              ║
                        [ FREE NODE ]
                              ║
          ╔═══════════╦═══════╩═══════╦═══════════╗
          ▼           ▼               ▼           ▼
      SKILLING     QUESTING      COMBAT/BOSSING    FATE
                                                MANIPULATION
          │           │               │             │
          ▼           ▼               ▼             ▼
        ...          ...             ...           ...
```

Each branch's first purchasable node must provide a **real benefit**—no filler nodes just to make the tree larger. Deeper branches can split, converge, and use the AND/OR prerequisite rules we've established.

I also agree with the adjustment from my previous answer: **one large progression tree** is looking better than three separate trees. We'll keep Bounties as their searchable catalog and Account as the account-status/unlock ledger.

## Next: Skilling branch

I'd start the Skilling branch by asking one very specific question:

**Should individual skill nodes simply unlock that skill for use on the player's wheel, or should unlocking a skill also automatically add it to the wheel?**

Based on what we've already designed, I strongly favor **unlock ≠ add**.

For example:

```text
             SKILLING
                 │
          [ SKILL MASTERY ]
           first branch node
                 │
        ┌────────┼────────┐
        ▼        ▼        ▼
    [SLAYER] [AGILITY] [CRAFTING]
       200       150       125 FP
        │
        ▼
    UNLOCKED

Wheel:
14 / 24 slots

Slayer
[ ADD TO WHEEL — 25 FP ]
```

That gives us the economy you described earlier:

**Unlocking a skill** gives you permanent permission to use it as a Fate.

**Adding it to your active wheel** is a separate, relatively cheap action.

**Removing it later** is intentionally painful/expensive and may require one of those awful removal Fates.

There's one part I'd push back on, though: I don't think the skill tree should just be a flat wall containing every skill.

I'd like there to be **some meaningful progression relationships**. For example, more specialized skills could sit deeper in the tree, while broadly useful skills appear earlier. But I *wouldn't* blindly copy OSRS skill requirements, because something like:

> You must unlock Crafting before Herblore because... tree layout.

would be arbitrary unless there's a Wheelbound reason for it.

So I'd propose our next decision is:

> **Are all non-starting skills roughly equal unlock choices, or do we deliberately classify them into early/mid/late Wheelbound skill tiers?**

I'm leaning toward **tiers**, but fairly loose ones—not "Agility requires Firemaking requires Fletching" nonsense.

## S8-M0089 - user - 2026-09-30T15:54:23.347000+00:00
Message ID: 5cdb69fe-da47-47f4-a316-f0263b83edff

lock this in&#x20;

That gives us the economy you described earlier:
**Unlocking a skill** gives you permanent permission to use it as a Fate.
**Adding it to your active wheel** is a separate, relatively cheap action.
**Removing it later** is intentionally painful/expensive and may require one of those awful removal Fates.
There's one part I'd push back on, though: I don't think the skill tree should just be a flat wall containing every skill.
I'd like there to be **some meaningful progression relationships**. For example, more specialized skills could sit deeper in the tree, while broadly useful skills appear earlier. But I *wouldn't* blindly copy OSRS skill requirements, because something like:

i think building your wheel should be a separatre UI / experince&#x20;


expand on this&#x20;
Are all non-starting skills roughly equal unlock choices, or do we deliberately classify them into early/mid/late Wheelbound skill tiers?

## S8-M0090 - assistant - 2026-09-30T15:54:30.860063+00:00
Message ID: aca725a4-c08d-4d1b-8150-380d7e0ca6b0

Locked. And yes, **building/managing the active wheel should be its own UI experience**, not something you do from the progression tree.

The progression tree answers **"What have I unlocked?"** The Wheel Builder answers **"What am I willing to put in Fate's hands?"**

So unlocking Slayer in the tree doesn't immediately alter your wheel. Later, you enter the Wheel Builder, see Slayer among your unlocked options, and decide whether to spend the relatively small cost to add it. Removing it afterward is the painful part.

## What I mean by skill tiers

I don't mean an arbitrary classification like:

> Easy skill → Medium skill → Hard skill

And I definitely don't mean that every skill at a certain tier costs the same.

I'm thinking about **when introducing that skill makes sense for a Wheelbound account**.

For example, imagine all non-starting skills were immediately available underneath the first Skilling node:

```text
                     SKILLING
                         │
       ┌──────┬──────┬──┴──┬──────┬──────┐
       ▼      ▼      ▼     ▼      ▼      ▼
     Slayer Agility Herb  Farm  Smith  Thieving ...
```

The player can immediately save for whichever skill they want.

That's maximum freedom, but it means the Skilling branch has almost **no progression**. It's basically a menu presented as a tree.

Instead, we could have broad progression layers.

For example—and these placements are **not proposals to lock yet**:

```text
                         SKILLING
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
          [Cooking]     [Firemaking]   [Crafting]
              │             │              │
              ▼             ▼              ▼
         [Fletching]    [Agility]      [Smithing]
              ╲             │             ╱
               ╲            ▼            ╱
                ─────── [Slayer] ───────
                           │
                      [Herblore]
                           │
                       [Farming]
```

I **don't like that exact tree**, because the relationships are mostly arbitrary. I'm showing the problem.

What I think we actually want is a middle ground.

### Broad Wheelbound progression tiers

Some skills become **early choices**, some require you to have invested somewhat into Skilling, and some are deeper unlocks.

Something conceptually like:

```text
                    SKILLING ROOT
                         │
              ┌──────────┼──────────┐
              ▼          ▼          ▼
            EARLY      EARLY      EARLY
             SKILL      SKILL      SKILL
              │          │          │
              ├────┐  ┌──┴──┐  ┌───┤
              ▼    ▼  ▼     ▼  ▼   ▼
                    MID SKILLS
                  /     |      \
                 ▼      ▼       ▼
                         │
                   LATE SKILLS
```

A deeper skill might require:

> **Any 2 early Skilling nodes**

rather than:

> **You need Firemaking specifically to unlock Slayer.**

That's where our AND/OR prerequisite system becomes useful.

For instance:

```text
Cooking ──────┐
              │
Crafting ─────┼── [ANY 2] ──► Slayer
              │
Agility ──────┤
              │
Fletching ────┘
```

The player chooses their route through the tree, but they still have to **invest in Skilling progression** before reaching deeper options.

That feels much better to me than arbitrary skill-to-skill dependencies.

## Why have deeper skills at all?

Because some skill Fates can dramatically change an account.

**Slayer** is a great example. Once Slayer is available, you're opening up monsters, drops, combat progression, Bounties, eventually bosses, etc. That's a more consequential unlock than Firemaking.

Herblore/Farming can also introduce systems that are considerably more interconnected than simply "go chop a tree."

So I don't necessarily want:

> Firemaking: 200 FP  
> Slayer: 200 FP

sitting next to each other immediately after the tutorial node as equivalent choices.

The tree can communicate:

> **You're gaining access to increasingly powerful/useful parts of RuneScape as your Wheelbound account develops.**

### But there's a danger

I don't want us to over-gate RuneScape.

We're already restricting what the player can do through Fates, shops, quests, bosses, the wheel, etc. If we also bury basic skills ten nodes deep, we could make early Wheelbound incredibly repetitive:

> Mining → Fishing → Combat → Woodcutting → Mining → Quest → Fishing...

So I think **a healthy number of skills need to become available relatively early**.

---

### What I'm leaning toward

Not rigid **Early / Mid / Late** labels visible to the player.

Instead, the **physical depth of the tree is the tier**.

The player just sees a beautiful progression tree:

```text
                     SKILLING
                        │
             ┌──────────┼──────────┐
             ▼          ▼          ▼
          [Skill]    [Skill]    [Skill]
             │          │          │
        ┌────┴───┐      │     ┌────┴───┐
        ▼        ▼      ▼     ▼        ▼
     [Skill]  [Skill] [Skill] [Skill] [Skill]
         ╲       │       │       │      ╱
          ╲      └───────┼───────┘     ╱
           ╲             ▼            ╱
            ─────── [DEEP SKILL] ─────
```

Underneath, *we* know those are effectively early/mid/late unlocks.

That also gives us room for some really interesting **OR routes**. Maybe Slayer can be reached from two different portions of the Skilling tree. You aren't told "choose the Slayer route." You discover that your chosen progression eventually connects there.

I think that's the direction I'd recommend.

**Next single decision:** before we place any skills, we should identify which non-starting skills we consider **early-access candidates**. Then we can argue about each one rather than arbitrarily drawing a tree first.

## S8-M0091 - user - 2026-09-30T16:12:02.812000+00:00
Message ID: 1fb9f4a3-a285-4a3e-b1f4-12db08cc323a

propose a tree

## S8-M0092 - assistant - 2026-09-30T16:12:04.883931+00:00
Message ID: b361c9aa-96fd-424d-8a0f-65d367375393

Yeah. Given everything we've established, I'd make the Skilling branch **wide near the top, progressively interconnected farther down, and avoid fake "you need Cooking to understand Agility" dependencies.**

Here's my first proposed version. **Nothing below is locked yet.**

```text id="v16flq"
                              SKILLING
                                 │
                 ┌───────────────┼───────────────┐
                 ▼               ▼               ▼
             COOKING         FIREMAKING       SMITHING
                 │               │               │
                 │               │               │
          ┌──────┘               │               └──────┐
          ▼                      ▼                      ▼
      FLETCHING               AGILITY               CRAFTING
          │                 ╱         ╲                  │
          │                ╱           ╲                 │
          ▼               ▼             ▼                ▼
       HUNTER         THIEVING       RUNECRAFT       HERBLORE
          │               ╲             ╱                │
          │                ╲           ╱                 │
          └──────────┐      ╲         ╱      ┌──────────┘
                     ▼       ▼       ▼       ▼
                         FARMING
                            │
                            │
                         SLAYER
```

But let me explain the logic, because I **wouldn't actually make every line above a hard direct prerequisite**.

### Layer 1 — Easy expansion

I'd put **Cooking, Firemaking and Smithing** immediately after the Skilling branch entrance.

They're recognizable, useful early, and don't massively change what the account can access.

The player can immediately decide:

> "Which direction do I want my wheel to start growing?"

I'd make these fairly cheap.

### Layer 2 — More useful choices

Then we introduce:

**Fletching • Agility • Crafting**

These start giving the account considerably more utility.

However, I don't want:

> You MUST buy Cooking to get Fletching.

Instead, I'd use our prerequisite system more loosely. Something like:

> Fletching requires **any one Layer-1 Skilling node**.

Same for Agility/Crafting.

That means the visual connections might be:

```text id="7nufrb"
 Cooking ─────┐
 Firemaking ──┼── OR ──► Fletching
 Smithing ────┘
```

But we can draw the tree so the connections don't become spaghetti.

---

### Layer 3 — Specialization

Then:

**Hunter • Thieving • Runecraft • Herblore**

Now we're getting into skills that give Wheelbound accounts access to more specialized systems/content.

I'd start requiring **investment**, rather than specific unrelated skills.

For example:

> Unlock **any 2 previous Skilling nodes**.

Potentially different requirements/costs per skill.

This is where paths start converging and our glowing tree becomes visually interesting.

---

### Layer 4 — Farming

I'd put Farming fairly deep.

Not because Farming is some powerful endgame skill, but because it's unusual in Wheelbound.

A Fate saying:

> Gain 50,000 Farming XP

has a **time-gating problem** that Mining doesn't have.

So I don't actually want Farming available until we've built mechanics that make Farming Fates sensible.

This may even eventually justify Farming having its own special Fate rules.

---

# Slayer

And I'd put **Slayer as the capstone of the Skilling branch**.

Something visually larger/more ornate:

```text id="ewrx1s"
       Hunter       Herblore
          ╲           ╱
           ╲         ╱
          Farming / ???
               ╲   ╱
                ╲ ╱

           ╔═══════════╗
           ║           ║
           ║  SLAYER   ║
           ║           ║
           ╚═══════════╝
```

I think Slayer deserves that treatment because **Slayer is almost a bridge between Skilling and Combat/Bossing**.

And here's where our tree could get really cool:

```text id="6xllhp"
       SKILLING BRANCH                COMBAT BRANCH
              │                            │
              ▼                            ▼
           SLAYER ◄══════════════════► BOSSING
              │
              ▼
        SLAYER BOSSES
```

Maybe certain deeper Combat/Bossing nodes eventually require **Slayer AND a Combat progression node**.

That's an actual meaningful AND relationship rather than an arbitrary one.

---

## One skill I deliberately haven't mentioned: Construction

I'd actually consider **Construction a special unlock**, not an ordinary skill node.

The POH gives so much account utility that I think we should discuss it separately. Unlocking "Construction Fate" and unlocking **POH functionality** may need to be two different concepts.

Same reason we should eventually think carefully about Prayer. It's technically a combat skill, but our broad **Combat Fate** currently includes combat skills; whether Prayer XP/access behaves exactly like Attack/Strength/etc. needs examination.

---

## The part of my proposal I'd push back on myself

**Slayer might be too deep.**

Slayer is one of the most interesting things to do in OSRS. If it takes 30 hours of Wheelbound before you can even put Slayer on the wheel, we're withholding fun content for the sake of progression.

So rather than requiring the entire Skilling tree, I'd probably make Slayer reachable through **multiple routes**:

```text id="jtt7nu"
                  SKILLING
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
        ...         ...        ...
          │          │          │
          ▼          ▼          ▼
       [SKILL]    [SKILL]    [SKILL]
           ╲         │         ╱
            ╲        │        ╱
             ╲       ▼       ╱
              ┌─────────────┐
              │   SLAYER    │
              │             │
              │ REQUIREMENT │
              │ Any 3 skill │
              │   nodes     │
              └──────┬──────┘
                     │
                deeper content
```

That means **the player chooses which skills they unlock on the journey to Slayer**.

I like that much more.

### So my actual proposal

I'd use **investment thresholds rather than arbitrary skill chains**:

**Layer 1:** Cooking / Firemaking / Smithing  
↓  
**Layer 2:** Fletching / Agility / Crafting  
↓  
**Layer 3:** Hunter / Thieving / Runecraft / Herblore  
↓  
**Special/deeper:** Farming / Construction considerations  
↓  
**Major node:** Slayer, reachable after sufficient Skilling investment rather than requiring every skill.

And then Slayer starts creating **cross-tree connections into Combat/Bossing**.

I think that's a solid first draft, but **the first thing I'd challenge is our Layer 1 selection**. Cooking/Firemaking/Smithing feels reasonable to me, but that's where I'd want your gut reaction before we build on top of it.

## S8-M0093 - user - 2026-09-30T16:12:57.464000+00:00
Message ID: 1ce638ed-ad4f-419c-b970-428eadf2fb9f

so essentially youd need everything to unlock slayer

## S8-M0094 - assistant - 2026-09-30T16:12:58.535348+00:00
Message ID: 06fae2e4-7351-4661-a8b8-e3fab50622ca

Not quite — and looking at what I proposed, **I made it look way more like that than I intended.**

I actually **don't want Slayer to require every other skill**. I think that would be a mistake. It would make Slayer essentially the "you finished Skilling" reward, when Slayer is useful and interesting much earlier.

What I meant by **investment threshold** is something like:

```text
                         SKILLING
                            │
          ┌─────────────────┼─────────────────┐
          ▼                 ▼                 ▼
       COOKING          FIREMAKING         SMITHING
          │                 │                 │
          ▼                 ▼                 ▼
      FLETCHING          AGILITY          CRAFTING
          │                 │                 │
          ▼                 ▼                 ▼
       HUNTER           THIEVING         HERBLORE
          ╲                 │                ╱
           ╲                │               ╱
            ╲               ▼              ╱
                 ╔══════════════╗
                 ║    SLAYER    ║
                 ║              ║
                 ║ Requires ANY ║
                 ║ 3 skill nodes║
                 ╚══════════════╝
```

So perhaps you went:

**Cooking → Fletching → Hunter**

That's three purchased skill nodes → Slayer becomes available.

Another player might have:

**Smithing → Crafting → Herblore**

Same result.

And importantly, after unlocking Slayer, **everything else stays available**. You can come back and buy Agility later if you want it.

### But I think we can make this even better

Instead of literally saying:

> `Requires any 3 skill nodes`

we can express that requirement **through the physical tree** using our OR-path system.

Maybe Slayer has several routes leading into it:

```text
       [Hunter] ─────────────┐
                             │
       [Thieving] ───────────┼───► [SLAYER]
                             │
       [Herblore] ───────────┘
```

and the earlier paths required investment to reach those nodes.

However, we need to be careful: if those are OR connections, then technically **one** would satisfy Slayer. So if we want "invest in three skills," that's an AND/count requirement rather than simple OR.

I'm actually starting to think **we shouldn't decide Slayer's exact requirement yet**.

Let's first lay out the skills in a tree that provides multiple routes. Then we can look at where Slayer naturally fits and say:

> "Okay, reaching this point should require X amount of investment."

Rather than deciding `3 skills` now and contorting the tree around that number.

So the important answer is: **No, I definitely don't want every skill required for Slayer.** It should be a significant unlock, but reachable while substantial portions of the Skilling tree remain untouched.

## S8-M0095 - user - 2026-09-30T16:14:35.764000+00:00
Message ID: 905c57e2-680c-4704-97fa-e11f95ff8989

so war thunder does something where they have columns and then to unlock the next tier , you need x unlocks from the previous teir. theres 9 there what if we make it to unlock slayer you need 5 or 6 out of the 9

## S8-M0096 - assistant - 2026-09-30T16:14:38.336515+00:00
Message ID: fb446e9a-c71c-4e0c-8cef-f8716ac5c812

Yes — **that is much closer to what I think we want.** It solves the arbitrary prerequisite problem while still making Slayer feel earned.

Rather than saying:

> "You need Hunter because Hunter leads to Slayer."

we say:

> **"Slayer is a Tier II/III Skilling unlock. To reach it, demonstrate enough investment in the previous Skilling tier."**

So if there are **9 skills available before Slayer**, the structure could look roughly like:

```text
                         SKILLING
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
          [Cooking]    [Firemaking]   [Smithing]
              │             │             │
              ▼             ▼             ▼
         [Fletching]    [Agility]     [Crafting]
              │             │             │
              ▼             ▼             ▼
          [Hunter]      [Thieving]    [Herblore]


              ═══════════════════════
                 TIER GATE: 5 / 9
              ═══════════════════════
                          │
                          ▼
                   ╔════════════╗
                   ║   SLAYER   ║
                   ╚════════════╝
```

Importantly, **those columns don't necessarily mean prerequisites**. Cooking doesn't have to unlock Fletching just because it's visually above it. They're nodes within a progression tier/section.

I like **5 of 9** more than 6 of 9 as our starting point.

Five means the player has to engage with **more than half** of the available skill unlocks, but still gets to reject four skills they don't want anywhere near their wheel.

That last part is especially important in Wheelbound because unlocking something isn't meaningless. You're building a pool of things you *could* eventually entrust to the wheel.

And remember our separate Wheel Builder rule:

```text
UNLOCK 5 SKILLS
      │
      │ Progression Tree
      ▼
SLAYER AVAILABLE
      │
      ▼
Purchase Slayer
      │
      │
      └──────────────► Slayer becomes available
                       in WHEEL BUILDER


WHEEL BUILDER
────────────────────
Unlocked Fates

✓ Cooking        [ADD]
✓ Smithing       [ADD]
✓ Crafting       [ADD]
✓ Hunter         [ADD]
✓ Herblore       [ADD]
✓ Slayer         [ADD]

Active Wheel: 8 / 24
```

The player **doesn't have to put those five prerequisite skills on their wheel**. They had to *unlock* them in the progression tree.

That's an important distinction.

### This also gives us a great general tree mechanic

I wouldn't make this something unique to Slayer. We can establish **Tier Gates** as an actual Wheelbound progression rule.

Visually, the player might reach a horizontal ornate divider:

```text
          ✦  SKILLING — TIER II  ✦

       Unlock 5 of 9 nodes above

              █████░░░░
                  5 / 9

              UNLOCKED
                  ║
                  ║ gold
                  ▼
               SLAYER
```

Before qualifying, everything beneath that gate is dark.

When the fifth node is purchased:

**5/9 → gate flashes → gold travels across the divider → paths below illuminate → Slayer and whatever else is beneath it begin glowing AVAILABLE.**

That fits *perfectly* with the gold-path visual language we've already established.

And it gives us much more freedom designing the rest of the tree. We don't have to invent nonsense prerequisites merely because two nodes need to connect.

I would lock in the **tier-gate concept**, but **I would not lock "5 of 9" permanently yet**. I'd put `5/9` in the blueprint as our current balance target and let the simulator/playtesting tell us whether it should ultimately be 4/9, 5/9, or 6/9.

The next decision I'd make is **what exactly the nine pre-Slayer skills are**, because our previous nine were just examples and we're missing/handling a few OSRS skills differently.

## S8-M0097 - user - 2026-09-30T16:17:10.770000+00:00
Message ID: fd7a09de-5c7b-4551-93c4-83cf250548ec

okay give me a tree with all 24 skills

## S8-M0098 - assistant - 2026-09-30T16:17:11.910258+00:00
Message ID: d1580de3-74b4-44f1-8c17-dde8afb7b56d

Yes. With the **War Thunder-style tier gate** idea, I think we can make the full skill progression much cleaner.

One important point first: OSRS now has **24 skills including Sailing**, so I'm including Sailing. Our existing **Combat Fate** bundles Attack, Strength, Defence, Hitpoints, Ranged, and Magic, so those don't need six separate Fate unlock nodes. Prayer is the one combat-adjacent skill I'd keep separate because training it behaves very differently.

### Proposed full 24-skill structure

```text id="tsz9g7"
                         ╔══════════════════╗
                         ║     SKILLING     ║
                         ╚════════╤═════════╝
                                  │

                    STARTING FATES — FREE
              ┌────────────┬────────────┬────────────┐
              ▼            ▼            ▼            ▼
          [MINING]      [FISHING]   [WOODCUTTING]  [COMBAT]
                                                     │
                           Combat represents:
                   Attack • Strength • Defence
                   Hitpoints • Ranged • Magic


═══════════════════════ TIER I ═══════════════════════

       [COOKING]       [FIREMAKING]       [SMITHING]

       [CRAFTING]       [FLETCHING]       [AGILITY]

       [THIEVING]       [RUNECRAFT]        [HUNTER]


                    TIER GATE
              Unlock 5 of 9 Tier-I skills
                     [ 0 / 5 ]
                         ║
                         ▼


═══════════════════════ TIER II ══════════════════════

         [SLAYER]        [HERBLORE]        [FARMING]

       [CONSTRUCTION]     [PRAYER]         [SAILING]


                    TIER GATE
             Unlock 4 of 6 Tier-II skills
                     [ 0 / 4 ]
                         ║
                         ▼


═══════════════════════ TIER III ═════════════════════

                 ADVANCED SKILL UPGRADES
                 / SPECIAL FATE OPTIONS

                  [SLAYER MASTERY]
                  [FARMING FATES+]
                  [SAILING FATES+]
                  [SKILL CHALLENGES]
                         etc.
```

That accounts for all 24 actual OSRS skills:

**Starting:** Attack, Strength, Defence, Hitpoints, Ranged, Magic, Mining, Fishing, Woodcutting = **9 skills**

**Tier I:** Cooking, Firemaking, Smithing, Crafting, Fletching, Agility, Thieving, Runecraft, Hunter = **9 skills**

**Tier II:** Slayer, Herblore, Farming, Construction, Prayer, Sailing = **6 skills**

**9 + 9 + 6 = 24.**

### I particularly like the 5-of-9 gate

Now the idea you just proposed becomes very clean:

```text id="h7lvg8"
TIER I

○ Cooking
● Firemaking
● Smithing
● Crafting
○ Fletching
○ Agility
● Thieving
○ Runecraft
● Hunter

──────────────
5 / 9 UNLOCKED
──────────────
       ║
       ▼
TIER II OPEN
```

You aren't forced down some artificial sequence. You choose **which five** skills fit the account you're building.

Someone might take:

> Smithing + Crafting + Fletching + Hunter + Cooking

while someone else takes:

> Agility + Thieving + Runecraft + Firemaking + Hunter

Both reach the next stage, but they've built different accounts.

And importantly, **the other four don't disappear**. Once Tier II opens, you can still return and purchase any Tier I skill whenever you want.

### One change from our previous Slayer idea

With this structure, I **wouldn't make Slayer itself the gate anymore.**

I'd make **Tier II opening** the reward for 5/9.

Then Slayer is one of the exciting choices immediately presented afterward:

```text id="20nbbc"
                     5 / 9 COMPLETE
                           ║
                 ══════════╩══════════
                      TIER II
                 ═════════════════════

       SLAYER        HERBLORE       FARMING

    CONSTRUCTION      PRAYER        SAILING
```

That gives the player another decision:

> "I finally reached Tier II. What do I want first?"

I think that's more interesting than automatically making Slayer the answer.

### Where this gets especially interesting

I **wouldn't end the Skilling branch after all 24 skills are available**.

Once we're past the actual skill unlocks, we can start putting **skill-specific upgrades** into the deeper tree.

For example:

```text id="5yzktx"
                    [SLAYER]
                        │
                 [SLAYER MASTERY]
                   ╱           ╲
          [??? Upgrade]     [??? Upgrade]


                   [FARMING]
                       │
              [FARMING MASTERY]
                       │
             [Alternative Fate]


                    [SAILING]
                       │
               [SAILING MASTERY]
```

That's where we can eventually put things that actually **change how a Fate behaves**, rather than just unlocking another skill.

It also means unlocking all 24 skills isn't "finishing the Skilling tree." It's finishing the **access portion** of the Skilling tree.

---

There is **one thing I wouldn't lock yet**: the exact `4 of 6` requirement for Tier II → Tier III. That's just a placeholder until we know what's actually below it.

But I **would lock the basic architecture**:

> **Starting skills → 9 Tier-I skill choices → unlock 5/9 → 6 Tier-II skill choices → deeper skill-specific progression.**

I think that's significantly cleaner than the interconnected skill tree I originally proposed.

## S8-M0099 - user - 2026-09-30T16:19:19.735000+00:00
Message ID: fa547edd-c028-4a52-a836-145f3ddfb387

lets pur prayer in T1....  swap it with hunter , does this still keep the tree structure or are we abandoningit here ?

## S8-M0100 - assistant - 2026-09-30T16:19:21.593944+00:00
Message ID: 2461b8d4-e933-478d-928e-5a8101d20158

Yes — **Prayer moves to Tier I and Hunter moves to Tier II.** I actually prefer that placement: Prayer is relevant very early, while Hunter can comfortably be something the player earns access to later.

And no, I **don't think we should abandon the tree structure**. My previous diagram accidentally made it look more like a tech-tree grid than the organic progression tree we'd already designed.

The **tier gate should exist *inside* the tree**, not replace the tree.

Conceptually:

```text
                              [ SKILLING ]
                                   │
                      ╭────────────┼────────────╮
                      │            │            │
                  [Cooking]    [Firemaking]  [Smithing]
                     ╲ │           │           │ ╱
                      ╲│           │           │╱
                 [Crafting]    [Fletching]  [Agility]
                      │ ╲          │          ╱ │
                      │  ╲         │         ╱  │
                 [Thieving]   [Runecraft]   [Prayer]
                       ╲          │          ╱
                        ╲         │         ╱
                         ╲        │        ╱
                    ╔═══════════════════╗
                    ║   TIER I GATE     ║
                    ║                   ║
                    ║  Unlock 5 of 9    ║
                    ║       3 / 5       ║
                    ╚═════════╤═════════╝
                              │
                         locked path
                              │

             When 5/9 are unlocked, gold crosses the gate

                              ║
                              ▼
                     ╭─────────────────╮
                     │                 │
                 [SLAYER]          [HERBLORE]
                    │  ╲              ╱  │
                    │   ╲            ╱   │
                [HUNTER]            [FARMING]
                     ╲                ╱
                      ╲              ╱
                    [CONSTRUCTION] [SAILING]
                          ╲          ╱
                           ╲        ╱
                         deeper tree...
```

That's much closer to what we've been designing.

### But there's an important distinction

Those connecting lines **shouldn't necessarily mean "Crafting requires Cooking."**

Some connections are simply the **visual flow of the tree**, while actual node requirements can be tier-based.

For example, clicking Prayer might say:

> **PRAYER**  
> Unlock Cost: 125 FP  
> Requirement: Tier I Skilling unlocked

rather than:

> Requires Agility.

That prevents us from inventing arbitrary OSRS relationships just to make a pretty tree.

Then the **Tier Gate itself is a genuine progression requirement**:

> Unlock any **5 of the 9 Tier-I skills**.

Once the fifth is purchased, all the relevant gold paths can converge on the gate, the gate activates, and the Tier-II branches illuminate.

### There's one design issue we should solve

If **all nine Tier-I nodes are independently purchasable**, we need to decide what the tree's connecting lines mean.

I don't like having glowing dependency-looking lines that actually mean nothing. Players will naturally interpret:

```text
Cooking
   │
   ▼
Crafting
```

as "Cooking unlocks Crafting."

So I think we should retain the organic tree, but distinguish **two kinds of connections**:

**Gold dependency paths** = actual prerequisite relationship.

**Decorative/tree structure** = shouldn't look like a connection at all.

That means Tier I might actually fan outward from a common Skilling access point, with all nine genuinely connected to that common ancestor:

```text
                         [SKILLING]
                             ║
             ╭───────────────┼───────────────╮
            ╱       ╱        │        ╲       ╲
           ▼       ▼         ▼         ▼       ▼
       Cooking  Smithing  Crafting  Agility  Prayer
          ▼        ▼         ▼         ▼        ▼
       etc...   etc...     etc...     etc...   etc...

        Every path represents REAL eligibility.
```

Then those branches can start **crossing and merging after the tier gate**, where AND/OR relationships become meaningful.

So we're **definitely keeping the big organic tree**. The War Thunder idea gives us a useful *gate mechanic inside it*; it isn't turning Wheelbound into War Thunder's rectangular research grid.

And I'll lock the placement change:

**Tier I:** Cooking, Firemaking, Smithing, Crafting, Fletching, Agility, Thieving, Runecraft, **Prayer**

**Tier II:** Slayer, Herblore, Farming, Construction, Sailing, **Hunter**.

## S8-M0101 - user - 2026-09-30T16:23:11.274000+00:00
Message ID: a7e2278f-65ef-4871-8cc7-0871d6429593

[Non-text attachment; not recovered]
so with the tree i would say crafting requires cooking but i also agree thats dumb so what about the image 


all on the same level per say and then your golden line are like what unlocked and how many more you need and the gate will be a progression milestone

## S8-M0102 - assistant - 2026-09-30T16:23:12.847214+00:00
Message ID: cb893ab1-f259-4117-9c65-70aa1e0b3068

**Yes. Your drawing fixes the exact problem I had with the previous tree.** I think this is the structure we should use.

The important change is that the **gold lines don't represent skill → skill prerequisites anymore**. They represent **progress flowing through the tier toward its milestone gate**.

Conceptually, your image translates to:

```text
                         [SKILLING]
                             ║
              ╱    ╱    ╱    ╲    ╲    ╲
             ▼    ▼    ▼      ▼    ▼    ▼

       ┌──────────────── TIER I ────────────────┐

     Cooking       Firemaking       Smithing
     Crafting       Fletching       Agility
     Thieving       Runecraft       Prayer

        │              │               │
        │              │               │
        ╰──────────────┼───────────────╯
                       ▼

              ╔══════════════════╗
              ║  TIER MILESTONE  ║
              ║                  ║
              ║   Unlock 5 / 9   ║
              ║      3 / 5       ║
              ╚════════╤═════════╝
                       │
                    LOCKED
                       │
                       ▼

                     TIER II
```

### The gold lines now have a much better meaning

Say I've unlocked:

**Cooking, Crafting, Agility, Thieving, and Prayer.**

The tree could visually look like:

```text
                  [SKILLING]
                  ╱ ╱ ║ ╲ ╲

              TIER I — 5/9

             ✦ Cooking
             ○ Firemaking
             ○ Smithing
             ✦ Crafting
             ○ Fletching
             ✦ Agility
             ✦ Thieving
             ○ Runecraft
             ✦ Prayer

               ╲ ╲ ║ ╱ ╱
                  GOLD
                   ║
                   ▼
             ╔═══════════╗
             ║   5 / 9   ║
             ║ MILESTONE ║
             ╚═════╤═════╝
                   ║
                   ▼
                TIER II
```

Each purchased skill essentially sends its **gold progression line toward the milestone**.

At 4/5, you'd have four glowing paths converging on a mostly dormant milestone.

Then you purchase the fifth skill:

```text
Skill purchased
      ↓
node flashes gold
      ↓
gold travels down its path
      ↓
4 / 5 → 5 / 5
      ↓
MILESTONE ACTIVATES
      ↓
big gold activation animation
      ↓
gold travels downward
      ↓
TIER II illuminates
```

That's a satisfying progression moment.

### And the milestone can be more than just a gate

I particularly like what you wrote in the drawing:

**"Gate + points."**

I think reaching a tier milestone should itself be an achievement.

So conceptually:

> **SKILLING MILESTONE I**  
> Unlock 5 Tier-I Skills  
> **5 / 5**
>
> **Rewards**
>   
> 🔓 Tier II Skilling  
> ✦ +??? Fate Points

Potentially later milestones could grant other things too. We don't need to decide those rewards yet.

That means the player doesn't just spend FP until the next row becomes available—they get a **little payoff for reaching the milestone**.

### It also preserves the tree we originally wanted

This doesn't have to become a rigid grid.

We can still position the nine nodes organically:

```text
                            SKILLING
                               ║
                 ╱─────────────┼─────────────╲
               ╱        ╱      │      ╲        ╲

         [Cooking]          [Prayer]
                  [Smithing]

      [Firemaking]                   [Agility]

              [Crafting]      [Runecraft]

          [Fletching]              [Thieving]


          ╲     ╲    ╲    │    ╱    ╱    ╱
           ╲     ╲    ╲   │   ╱    ╱    ╱
            ╲     ╲    ╲  │  ╱    ╱    ╱

                [ MILESTONE I ]
                     5 / 9
                       ║
                       ║
                ╱──────┴──────╲
               ▼               ▼
            Tier II          Tier II
             nodes            nodes
```

So visually it's still the **large, organic Path-of-Exile-inspired tree** we wanted.

We're just establishing a rule that makes it understandable:

**Nodes within the same tier are choices, not prerequisites for one another.**

**Gold lines from unlocked nodes feed the tier milestone.**

**The milestone requires X of Y nodes and acts as the progression gate to the next portion of that branch.**

That's cleaner than what I was proposing before, and I'd **lock this in as the general tier architecture**, not just something we use for Skilling.

Then we can use the same design language elsewhere where appropriate:

```text
SKILLING
Skills → Milestone → Deeper Skills

QUESTING
Quest Access → Milestone → Higher Quest Tier

COMBAT/BOSSING
Combat Content → Milestone → Higher Boss Tier

FATE MANIPULATION
Fate Upgrades → Milestone → More powerful Fate mechanics
```

Not every branch necessarily needs identical `5/9` gates, but **the visual language stays consistent**.

And your Prayer/Hunter swap stays:

**Tier I:** Cooking, Firemaking, Smithing, Crafting, Fletching, Agility, Thieving, Runecraft, Prayer.

**Tier II:** Slayer, Herblore, Farming, Construction, Sailing, Hunter.

I think this is worth treating as a **confirmed core progression-tree rule**. memcite

## S8-M0103 - user - 2026-09-30T16:26:28.718000+00:00
Message ID: 050b1c62-3eb3-4474-9283-5aed434600d2

[Non-text attachment; not recovered]
maybe the skills can pre presneted in a circle like the image and then the trees shoud be cool

## S8-M0104 - assistant - 2026-09-30T16:26:30.847429+00:00
Message ID: d101ae84-4058-4c65-bb63-a9e6105f7dbf

**Yes — I like this more than forcing the skills themselves to look like a traditional branching tree.** Your drawing separates two concepts that we were starting to mix together:

- **Tier clusters** = choices.
- **Master progression line** = actual progression through the tree.

I'd structure it like this:

```text
                           [SKILLING]
                               ║
                               ║
                    ╭───────────────────╮
                  ╱          ○            ╲
                ○                         ○
             ╱                               ╲
           ○                                   ○
           │                                   │
           │        ╔═════════════╗            │
           ○────────║  TIER I     ║────────────○
           │        ║             ║            │
           │        ║    0 / 5    ║            │
           ○────────║  MILESTONE  ║────────────○
             ╲      ╚═════════════╝          ╱
               ○                         ○
                  ╲                     ╱
                    ╰─────────────────╯
                              ║
                              ║
                     MASTER PROGRESSION
                              ║
                              ▼

                    ╔═══════════════╗
                    ║ TIER I GATE   ║
                    ║ Unlock 5 / 9  ║
                    ║   + ??? FP    ║
                    ╚═══════╤═══════╝
                            ║
                            ▼

                     TIER II CLUSTER
```

And we'd actually put the skill icons in those squares, so the Tier-I circle would visually contain:

```text
             Cooking       Firemaking

       Smithing                   Crafting

   Fletching       [ 0 / 5 ]         Agility

       Thieving                  Runecraft

                    Prayer
```

That would look **way better** than nine skills in rows.

### The center node becomes useful too

Your center node doesn't necessarily have to be something the player purchases.

I'd make it the **Tier Progress Node**.

Initially:

> **TIER I**  
> 0 / 5

Buy Prayer:

> **TIER I**  
> 1 / 5

Buy Crafting:

> **TIER I**  
> 2 / 5

Each skill sends a gold beam **inward** toward that center.

So at 4/5 you'd physically see four gold spokes:

```text
            □
        □       ■
          ╲     ╱
    □ ───── [4/5] ───── ■
             ╱ ╲
        ■   ╱   ╲   □
             ■
```

`■` = unlocked/glowing  
`□` = still locked/dim

Then the fifth purchase is the big moment.

Gold shoots inward → center reaches **5/5** → center fully ignites → **master progression line beneath it activates** → energy travels down into the milestone/reward node.

That is much more visually satisfying than nine lines all running downward.

## And this gives us our "tree" back

Below and between these clusters, we can have the interesting stuff.

Imagine zooming out:

```text
                         ROOT
                          ║
                      FREE NODE
                          ║
        ╔════════╦════════╩════════╦════════╗
        ▼        ▼                 ▼        ▼
     SKILLING  QUESTING       COMBAT      FATE
        ║
        ▼

     ╭─────────╮
    ○  ○     ○  ○
   ○    TIER I   ○
    ○   5/9    ○
     ╰────┬────╯
          ║
       [MILESTONE]
          ║
          ║
     ╭─────────╮
    ○  ○     ○  ○
   ○   TIER II  ○
    ○   0/4    ○
     ╰────┬────╯
          ║
          ▼
       [UPGRADE]
        ╱       ╲
       ▼         ▼
   [Something] [Something]
       ╲         ╱
        ╲       ╱
       [MILESTONE]
            ║
            ▼
          ...
```

And **the four main branches don't need identical shapes**.

Skilling might have these beautiful circular constellations.

Bossing might look more aggressive with boss-tier clusters.

Questing might have longer branching paths.

Fate Manipulation might be the weirdest, most interconnected branch of all.

They share the same **gold-path visual language**, but don't have to be four copies of the same layout.

That's how I think we get the **"cool tree"** you originally wanted without creating fake dependencies just so we have lines everywhere.

### One thing I'd change from your drawing

I wouldn't label the vertical line itself "Master Progression Line" in the actual UI. That's a good name for us internally, but the player shouldn't need an explanation.

It should just be the **large central golden artery** running through the branch. When locked, it's dark. As milestones are achieved, progressively more of it becomes permanently illuminated.

So if you're halfway through Skilling and zoom the tree out, you can literally see:

> **gold → gold → gold → darkness**

and immediately understand how far you've progressed.

**I'd lock this layout concept in.** The next single decision I'd make is what happens when the player clicks the **center `0/5` Tier Progress Node**, because I think that could become a really useful overview UI rather than merely being decoration.

## S8-M0105 - user - 2026-09-30T16:27:12.741000+00:00
Message ID: 2a4b69d9-4295-4417-be36-d86b09630543

and then the master progression line can gradually get more gold as the unlocks get unlocked

## S8-M0106 - assistant - 2026-09-30T16:27:13.691483+00:00
Message ID: 1a70f3c3-3dc5-4fc2-9fca-58dbbf69e955

Exactly. **That makes the Master Progression Line itself the progress bar**, rather than needing a conventional UI progress bar.

I'd lock that in.

For the Tier I cluster with a **5-of-9 requirement**, each qualifying unlock adds another segment of gold to the vertical Master Progression Line:

```text id="wqj7gp"
TIER I SKILL CLUSTER
        ◯  ◯  ◯
      ◯   [3/5]  ◯
        ◉  ◉  ◉
           │
           │ GOLD
           │ GOLD
           │ GOLD
           │ dark
           │ dark
           │
      [MILESTONE]
```

So visually:

```text id="0glokx"
0/5   ░░░░░░░░░░   dark
1/5   ██░░░░░░░░   20%
2/5   ████░░░░░░   40%
3/5   ██████░░░░   60%
4/5   ████████░░   80%
5/5   ██████████   100%
```

But it shouldn't look like five rectangular chunks. I'd make it feel like **golden energy gradually filling an engraved channel**.

When you purchase a skill, there are actually two connected animations:

```text id="vmyss6"
        [PRAYER]
           │
           │ gold travels inward
           ▼
         [3/5]
           │
           │
           ▼
    ║████████░░░░░║
          ↑
     gold advances
           │
           ▼
      [MILESTONE]
```

And I think there's a particularly cool detail here:

**The gold should physically stop wherever your current progress ends.**

So at `3/5`, it doesn't just become "60% brighter." The glowing gold energy has literally traveled **60% of the distance toward the milestone**, with a subtle animated/pulsing leading edge.

That makes the whole tree readable without numbers.

Then:

### The fifth unlock

This should feel substantially different.

```text id="s2m4fb"
4/5
 │
 ▼

Player unlocks Runecraft

Runecraft node
      ✦
      │
      ▼
Center Node
   5 / 5
      ✦
      ║
      ║  GOLD RUSHES DOWN
      ║
      ▼
╔══════════════════╗
║    MILESTONE     ║
║     COMPLETE     ║
╚══════════════════╝
      ✦
      ║
      ▼
Tier II awakens
```

The remaining four Tier-I skills stay purchasable, but **they no longer advance that particular Master Progression Line**, because the milestone has already been satisfied.

They still illuminate their own nodes/spokes when purchased.

That distinction is important. Otherwise buying skill #6 after reaching `5/5` has nowhere to advance the progression line.

So I'd define it as:

> **Master Progression Lines visualize progress toward the next milestone, not percentage completion of the entire cluster.**

That means a player can have:

**Tier I milestone: 5/5 COMPLETE**

while still seeing:

**Tier I skills: 5/9 unlocked**

Those are two different pieces of information.

And zoomed way out, this could look *really* good. The player sees this enormous dark progression structure with a **golden artery slowly crawling farther and farther down the tree as their Wheelbound account develops**.

That feels much more uniquely **Wheelbound** than a standard tech-tree progress bar. memcite

## S8-M0107 - user - 2026-09-30T16:27:32.875000+00:00
Message ID: c7c6ca62-e941-4224-b28f-0381bd457975

yep let lock this in

## S8-M0108 - assistant - 2026-09-30T16:27:34.377040+00:00
Message ID: 897e1225-d036-42f8-a8b7-63d1ad2478db

Locked in as a **confirmed core progression-tree mechanic**.

The final concept is:

```text id="3frg8a"
        TIER SKILL CLUSTER
      ○     ○     ○
   ○       ○       ○
      ○   [3/5]  ●
        ●       ●
            │
            │ GOLD
            │ GOLD
            │ GOLD
            │ dark
            │ dark
            ▼
       [MILESTONE]
            │
            ▼
        NEXT TIER
```

Each tier's choices surround a **central Tier Progress Node**. Unlocking a qualifying node illuminates that node and sends gold inward toward the center.

The **Master Progression Line** extends downward from the center toward the milestone. Its gold fill represents progress toward the milestone—for example, `3/5` means the gold has physically traveled about 60% of the distance.

At `5/5`, the gold reaches the milestone, triggers the larger completion animation, activates the milestone/reward, and continues into the newly accessible portion of the tree.

Any remaining nodes in the completed tier stay available afterward. Unlocking #6–#9 still illuminates their individual nodes and paths, but doesn't affect the already-completed Master Progression Line.

And importantly, we're keeping the distinction:

**Milestone progress:** `5/5` ✓  
**Tier completion:** `5/9 skills unlocked`

That gives us both player choice and a visually obvious sense of progression.

I think the **Skilling branch's basic visual/progression architecture is now established enough that we can stop redesigning its skeleton** and start deciding what comes after the first `5/9` milestone. memcite

## S8-M0109 - user - 2026-09-30T16:27:54.059000+00:00
Message ID: b7d9b224-d7a6-4b7b-9f23-ee1b4579c253

whats next

## S8-M0110 - assistant - 2026-09-30T16:27:55.980162+00:00
Message ID: 2bf34f6c-4a63-40bb-9299-edbf9647dc9b

I think the next thing is to finish the **Skilling branch's actual progression**, now that we've nailed its visual language.

We currently have:

```text
STARTING ACCESS
Mining • Fishing • Woodcutting • Combat
          │
          ▼
╭─────────────────────────╮
│        TIER I           │
│                         │
│ Cooking     Firemaking  │
│ Smithing    Crafting    │
│ Fletching   Agility     │
│ Thieving    Runecraft   │
│ Prayer                  │
│                         │
│          [0/5]          │
╰────────────┬────────────╯
             │
      MASTER PROGRESSION
             │
         [5/9 GATE]
             │
             ▼
          ???????
```

The **single decision I'd make next** is what the player sees immediately after completing `5/9`.

We already have six remaining skills:

**Slayer, Herblore, Farming, Construction, Sailing, Hunter**

My recommendation is that these become the **Tier II circular cluster**, using exactly the system we just designed:

```text
                  [TIER I]
                    5/5
                     ║
                     ║ GOLD
                     ▼
             ╔══════════════╗
             ║ MILESTONE I  ║
             ║  + ??? FP    ║
             ╚══════╤═══════╝
                    ║
                    ▼

                 [SLAYER]

       [HUNTER]              [HERBLORE]


                 [ 0 / ? ]


       [SAILING]             [FARMING]

              [CONSTRUCTION]

                    │
                    │ Master Progression
                    ▼
                 ???????
```

But here's where I **wouldn't automatically use another `X of 6` gate**.

These are the **last six actual skills**. If we say "unlock 4/6 to advance," what exactly are we advancing *to*?

That's where I think the Skilling tree should transform from **access progression** into **mastery progression**.

For example:

```text
       TIER II — FINAL SKILL ACCESS
                 ○ ○ ○
               ○ [0/?] ○
                  ○
                  │
                  ▼
          SKILLING MILESTONE II
                  │
          ╔═══════╧════════╗
          ▼                ▼
    FATE UPGRADES     SKILL MASTERY
      / OPTIONS        / CHALLENGES
```

Potentially the Tier-II milestone is something significant like:

> **Unlock 4 of the 6 advanced skills**

and reaching it opens the **deeper Skilling endgame tree**, where we're no longer unlocking skills—we're modifying how Skilling Fates work, unlocking special challenges, earning stronger rewards, etc.

The other two skills remain available forever.

That gives the branch a nice evolution:

**Starting skills → broaden your account → advanced skills → master the system.**

So the next question I'd settle is very small:

### Should Tier II require `4 of 6` to reach Skilling Mastery?

I'm leaning **4/6**. It follows the same philosophy as 5/9: meaningful investment without requiring the player to buy everything. Then we can design what **Skilling Mastery** actually contains.

## S8-M0111 - user - 2026-09-30T16:29:02.517000+00:00
Message ID: e836281e-dbb9-466b-b21d-9d9efc1c5688

yes

## S8-M0112 - assistant - 2026-09-30T16:29:04.922994+00:00
Message ID: 5a5ce78b-b635-4c07-8398-dc606f471159

Locked: **Tier II requires 4 of the 6 advanced skills** to complete its milestone.

So our Skilling access progression is now:

```text
STARTING ACCESS
Mining • Fishing • Woodcutting
(+ Combat separately)
        │
        ▼
╭─────────────────────────────╮
│          TIER I             │
│                             │
│ Cooking • Firemaking        │
│ Smithing • Crafting         │
│ Fletching • Agility         │
│ Thieving • Runecraft        │
│ Prayer                      │
│                             │
│          0 / 5              │
╰─────────────┬───────────────╯
              │
       MASTER PROGRESSION
              │
              ▼
       MILESTONE I — 5/9
              ║
              ▼
╭─────────────────────────────╮
│          TIER II            │
│                             │
│ Slayer • Herblore           │
│ Farming • Construction      │
│ Sailing • Hunter            │
│                             │
│          0 / 4              │
╰─────────────┬───────────────╯
              │
       MASTER PROGRESSION
              │
              ▼
       MILESTONE II — 4/6
              ║
              ▼
       SKILLING MASTERY
```

And the two Tier-II skills the player didn't choose **remain available forever**.

Now we've reached an interesting part because **Skilling Mastery shouldn't just be Tier III with another pile of skills**. There aren't any more skills to unlock.

This is where the tree can start changing the *rules of Wheelbound itself*.

My first thought is that reaching **Skilling Mastery** should unlock access to **Challenge Skilling Fates**—things like area restrictions, equipment/tool restrictions, method restrictions, etc., in exchange for significantly better Fate Point rewards.

That would connect directly to the Challenge Fate Card concept we established earlier.

But there's a potential conflict: we originally envisioned **Fate Manipulation as its own major branch**, and Challenge Fate Cards arguably belong there instead.

So that's the next decision I'd resolve:

**Does Skilling Mastery improve the player's actual Skilling Fates, while Fate Manipulation controls the cards/roll system?**

I lean strongly **yes**. That keeps the four branches from stealing each other's identities. memcite

## S8-M0113 - user - 2026-09-30T16:30:07.148000+00:00
Message ID: e6bc99a1-567d-4190-a815-3702b533d838

mastery should allow you to have better odds of hitting the desired skill and then fate manipulation should allow more options of the skill chosen (lesser greater etc )

## S8-M0114 - assistant - 2026-09-30T16:30:09.085282+00:00
Message ID: fc1cb5b1-5520-44c0-b6d8-6984843181b6

Yes. That gives the two branches a much cleaner identity. I'd lock in this separation:

**Skilling Mastery = influence WHAT skill Fate chooses.**  
**Fate Manipulation = influence WHAT KIND of Fate you receive after the skill is chosen.**

So the flow becomes:

```text
ACTIVE WHEEL
     │
     ▼
SPIN FOR SKILL
     │
     │  ← SKILLING MASTERY affects this
     │
     ▼
   MINING
     │
     ▼
CHOOSE / ROLL FATE CARD
     │
     │  ← FATE MANIPULATION affects this
     │
     ▼
┌──────────────────────┐
│ Standard Fate        │
│ Lesser Fate          │
│ Greater Fate         │
│ Challenge Fate       │
│ etc.                 │
└──────────────────────┘
     │
     ▼
FINAL OBJECTIVE
Mining — 75,000 XP
```

That also gives us a strong direction for the **Skilling Mastery section of the tree**.

For example, mastery upgrades could eventually do things like **increase weighting** for a desired skill rather than outright choosing it. If your wheel has 12 eligible Fates and you really want Slayer, a mastery mechanic could let you designate Slayer as **Favored**, increasing its weight on the wheel.

I prefer weighting over something like:

> "Spend 100 FP → your next Fate is Slayer."

because directly choosing the skill starts undermining the entire premise of Wheelbound. The wheel should always retain some authority.

Visually, we could eventually show that right on the wheel:

```text
NORMAL

Mining       10%
Fishing      10%
Slayer       10%
Crafting     10%
...


WITH SLAYER FAVORED

Mining        8%
Fishing       8%
SLAYER       24%   ★ FAVORED
Crafting      8%
...
```

Those percentages are only illustrative—we'd balance the actual weighting later.

And deeper Mastery could give you **more control without ever guaranteeing the result**. That's a nice progression fantasy: early Wheelbound is chaotic; a developed Wheelbound account learns to *bend Fate*, but never completely control it.

Meanwhile Fate Manipulation can go nuts with the second stage:

```text
                FATE MANIPULATION
                       │
             ┌─────────┼─────────┐
             ▼         ▼         ▼
          LESSER    GREATER   CHALLENGE
           FATE      FATE       FATE
             │         │          │
             └─────────┼──────────┘
                       ▼
                deeper card
                 manipulation
```

So the branches now have genuinely different purposes instead of overlapping.

**Next decision I'd make:** exactly how **Favored Skills** work. I think that's the natural first mechanic underneath the Skilling Mastery milestone. memcite

## S8-M0115 - user - 2026-09-30T16:31:08.373000+00:00
Message ID: 09537ba4-4f75-447b-8e01-06224533ab05

sounds good , im thinking eithe the slice gets bigger better odds of hitting it / maybe the ability to add another slice somewhere on the opposite side of the wheel

## S8-M0116 - assistant - 2026-09-30T16:31:11.698773+00:00
Message ID: 60a5d10c-2943-4aaf-94c9-4171c3e717f9

I like the **extra slice** idea more because it keeps the wheel visually honest.

Instead of secretly changing Slayer from a 10% slice to a 20% slice, Mastery literally gives Slayer **another physical position on the wheel**.

For example, suppose you have 8 Fates:

```text
NORMAL WHEEL

Mining
Fishing
Slayer
Crafting
Cooking
Prayer
Agility
Smithing

Slayer = 1 of 8 slices
       = 12.5%
```

You favorite Slayer and have an upgrade that grants **+1 Favored Slice**:

```text
FAVORED WHEEL

Mining
Fishing
SLAYER ★
Crafting
Cooking
Prayer
Agility
Smithing
SLAYER ★
```

Now Slayer occupies **2 of 9 slices = 22.2%**.

And I strongly agree with your idea that the duplicate should be placed **away from the original** rather than right beside it:

```text
                 [SLAYER ★]
            /                 \
       [Fishing]             [Crafting]

    [Mining]                     [Cooking]

       [Smithing]             [Prayer]
            \                 /
                 [SLAYER ★]
```

That makes spinning feel better because there are genuinely **two different regions where the desired result can hit**.

### This could become the actual Mastery progression

Rather than generic "+5% desired skill chance" upgrades, Mastery could unlock increasingly powerful **wheel manipulation tools**.

Early Mastery might unlock:

> **Favored Fate** — Designate one unlocked Fate on your active wheel as Favored.

Then:

> **Echo of Fate** — Your Favored Fate receives one additional slice positioned roughly opposite its original slice.

And much deeper:

> **Greater Echo** — Add another duplicate slice.

Potentially another upgrade eventually allows **two Favored Fates** rather than stacking everything into one.

I'm deliberately not locking those exact upgrades yet, but that's the design space I'd use.

### There is one rule I'd establish now

**Duplicates should affect probability mathematically, not merely visually.**

If the wheel shows Slayer twice, those are two real Slayer outcomes.

That sounds obvious, but it's important for the blueprint because we don't want an implementation where the animation is decorative and the game secretly rolls from a separate probability table.

I'd want:

> **The wheel displayed to the player accurately represents the probability distribution used to determine the Fate.**

That makes Wheelbound much easier to understand and trust.

It also creates a really cool progression effect. A new account's wheel is clean and evenly divided. A mature Wheelbound account's wheel visibly tells the story of how they've manipulated Fate—**duplicate slices, favored markings, unlocked content, etc.**

I think **Favored Fate + physical duplicate slice placed away from the original** is strong enough to make the first Skilling Mastery mechanic. memcite

## S8-M0117 - user - 2026-09-30T16:32:03.146000+00:00
Message ID: e0d59530-1d22-4e85-b667-c81f4e09bfbc

lets do a combination of both have some fun unlocks , a bigger slice is one and then adding another slice of a skill is another

## S8-M0118 - assistant - 2026-09-30T16:32:04.219647+00:00
Message ID: 929f3ffb-17d4-43f1-8fc6-59a302f5b793

Yes. **Let's lock in that Skilling Mastery isn't one linear "favorite gets stronger" mechanic.** It gives the player several fun ways to physically manipulate the skill wheel.

The two confirmed mechanics are:

**Weighted Slice** — Choose a favored skill and make its existing wheel slice physically larger. The larger visual area represents the actual increased probability.

**Echo Slice** — Add another real slice for the favored skill elsewhere on the wheel, preferably positioned far from the original. Both slices are genuine outcomes.

That gives us room for a Mastery mini-tree rather than a straight upgrade path:

```text id="tb8h2k"
                    SKILLING MASTERY
                           ✦
                           │
                    [FAVORED SKILL]
                           │
                 ┌─────────┴─────────┐
                 │                   │
                 ▼                   ▼
          [WEIGHTED SLICE]      [ECHO SLICE]
                 │                   │
          Existing slice       Adds another
          becomes larger       physical slice
                 │                   │
                 ▼                   ▼
          [GREATER WEIGHT]     [SECOND ECHO?]
                 ╲                   ╱
                  ╲                 ╱
                   ▼               ▼
                    [ ??? MASTERY ]
```

And because we're using the tree's AND/OR system, this can get interesting. Maybe some advanced manipulation requires investment in **both** sides, while other upgrades let you specialize heavily into one.

I also think we should establish a broader design rule for this section:

> **Skilling Mastery upgrades should manipulate the physical skill wheel in ways the player can see.**

So instead of boring invisible bonuses like `+3% skill weighting`, we favor mechanics where you can look at the wheel and immediately see what your progression has done to it.

That gives us room later for things like swapping slice positions, protecting a slice from some negative effect, splitting a large slice, temporarily empowering a section of the wheel, etc. Those are **ideas, not confirmed mechanics**.

One caution: we should probably put a cap on how much a single skill can be weighted. Otherwise a fully upgraded player could turn "spin the wheel" into "Slayer has a 70% chance," which undermines Wheelbound. We can determine that cap through simulation rather than guessing now.

So **Weighted Slice + Echo Slice are confirmed**. Exact probability increases, number of echoes, costs, and maximum weighting stay TBD for balance testing.

Next I'd design the **Fate Manipulation branch**, because now we have a very clean contrast: Skilling Mastery manipulates the *skill wheel*; Fate Manipulation changes what happens **after that wheel picks the skill**. memcite

## S8-M0119 - user - 2026-09-30T16:33:04.480000+00:00
Message ID: d56581eb-1d68-4ae3-ab26-1c33dd694806

but you upgrade per slice so it more like a slice modifyer each slice when added should be an equal slixce and then you can upgrade it so you might have slayer on there twice but one slice could be bigger

## S8-M0120 - assistant - 2026-09-30T16:33:05.712380+00:00
Message ID: a4bd3744-5a0c-413f-b966-231d6fc99411

Ah, yes — **that's a meaningful distinction.** The modifier belongs to the individual wheel slice, not to the skill globally.

I'd lock it in this way:

### Every new slice starts equal

When something is added to the Wheel Builder, it gets **one standard-sized slice**.

If you later unlock the ability to add an additional Slayer slice, that second Slayer slice also starts at the standard size.

```text id="ht57dn"
ACTIVE WHEEL

Mining       [1.0x]
Fishing      [1.0x]
Slayer A     [1.0x]
Crafting     [1.0x]
Prayer       [1.0x]

Player earns/buys another Slayer slice:

Mining       [1.0x]
Fishing      [1.0x]
Slayer A     [1.0x]
Crafting     [1.0x]
Prayer       [1.0x]
Slayer B     [1.0x]   ← new equal slice
```

Then **slice upgrades** can target one specific slice:

```text id="2r51pi"
Slayer A   ████████████    1.5x
Slayer B   ████████        1.0x

Mining     ████████        1.0x
Fishing    ████████        1.0x
Prayer     ████████        1.0x
```

So Slayer happens to have better overall odds because the player has invested in **two separate things**:

1. They added a second Slayer slice.
2. They upgraded the size/weight of Slayer A.

That's much more interesting.

### It also changes what "Mastery" means

Instead of:

> ★ Favorite Slayer → Slayer gets +X%

it's more like you're **forging individual pieces of your wheel**.

Clicking a slice in the Wheel Builder could eventually show something like:

```text id="sncd4j"
╔════════════════════════════╗
║          SLAYER            ║
║        WHEEL SLICE         ║
║                            ║
║  Weight: 1.5x              ║
║  Upgrade Level: II         ║
║                            ║
║  [ UPGRADE SLICE ]         ║
╚════════════════════════════╝
```

While the second Slayer slice is independently:

```text id="cdu3k1"
SLAYER
WHEEL SLICE

Weight: 1.0x
Upgrade Level: I

[ UPGRADE SLICE ]
```

That opens up **way more build variety**.

One player could build:

```text id="8ohhru"
SLAYER    2.0x
Mining    1.0x
Fishing   1.0x
Prayer    1.0x
...
```

Another could go:

```text id="d7qtcv"
SLAYER A    1.25x
SLAYER B    1.25x
Mining      1.0x
Fishing     1.0x
...
```

Both have manipulated toward Slayer, but they built their wheels differently.

And because slice size is the actual probability weight, **the visual wheel remains truthful**.

### One terminology change

I would drop **"Favored Skill"** as the core mechanic.

It implies the modifier follows the skill, which isn't what you're describing.

I'd call the underlying system something like **Slice Modifiers** internally:

> Every active wheel entry creates a standard `1.0x` slice.  
> Additional copies create additional independent `1.0x` slices.  
> Slice Modifiers can individually increase a specific slice's weight/size.  
> Two slices representing the same Fate remain independently upgradeable.

We can come up with much cooler in-game names later.

This also means **adding another slice** and **upgrading a slice** are fundamentally different Mastery unlocks, which is exactly what we want for the progression tree.

**I like this system substantially more than the global Favored Skill idea. Lock this version in.** memcite

## S8-M0121 - user - 2026-09-30T16:33:35.212000+00:00
Message ID: 564f0dd2-b8bc-47ce-8874-3461f988aaf5

no favored skill i agree

## S8-M0122 - assistant - 2026-09-30T16:33:38.039681+00:00
Message ID: 728d97f1-33ce-4d18-8ec7-72e70ec4eabc

Agreed. **Favored Skill is removed from the design.**

Skilling Mastery will work at the **individual slice level**:

```text
Every new slice
      ↓
Standard 1.0x size
      ↓
Can be individually modified

SLAYER A   1.5x  ← upgraded
SLAYER B   1.0x  ← duplicate, untouched
MINING     1.0x
PRAYER     1.25x ← upgraded
FISHING    1.0x
```

So the two confirmed Mastery mechanics are **Duplicate Slice** — add another equal-sized `1.0x` copy of a skill to the wheel — and **Slice Upgrade** — increase the size/weight of one specific existing slice.

The displayed slice size must always correspond to its real probability weight. Exact upgrade levels, costs, maximum sizes, and duplicate limits stay TBD for balancing/simulation.

That gives us a much more interesting concept: **you're mastering and physically modifying your wheel, not simply marking a skill as your favorite.** memcite

## S8-M0123 - user - 2026-09-30T16:35:01.076000+00:00
Message ID: 1ad0fcee-c575-4192-9600-ec5d03e0c42b

these nodes are costing points right we will need to go and balance the points per task as well as the unlocks i will probably rely hevily on you for this since its pretty in depth and alot of juggle

## S8-M0124 - assistant - 2026-09-30T16:35:02.522225+00:00
Message ID: f68b0064-ae5d-4c6e-88d1-97dbca65ceae

Yes. **The progression nodes cost Fate Points**, unless we explicitly mark a node as free/reward-only. That includes skill unlocks, slice modifiers, duplicate-slice abilities, Fate Manipulation upgrades, shop-related progression, and so on.

And I think you're right to **not try to balance the numbers as we invent each mechanic**. We'd constantly be changing old numbers as new systems appear.

I'd handle balance as its own major design phase once we've established most of the systems.

The important part is that we'll balance the **whole economy together**, not individual prices in isolation:

```text id="kvrtt7"
                  FATE POINT ECONOMY

                     FP SOURCES
                         │
       ┌─────────────────┼─────────────────┐
       ▼                 ▼                 ▼
     Fates           Bounties         Milestones
       │                 │                 │
       └─────────────────┼─────────────────┘
                         ▼
                    FP BALANCE
                         │
       ┌─────────────────┼────────────────────┐
       ▼                 ▼                    ▼
 Progression Nodes   Shop Unlocks       Wheel Changes
       │                 │                    │
       ▼                 ▼                    ▼
 Skill Unlocks      Vendor Access       Add Slice
 Mastery Nodes      etc.                Remove Slice
 Fate Upgrades                           Upgrade Slice
```

Then we also have **FP penalties** feeding in from Fate violations and eventually whatever we decide happens with negative FP.

### I'll lean heavily on simulation for this

Rather than me saying:

> "Slayer feels like 500 FP."

and us hoping that's reasonable, we'll build a model.

For example, we can simulate **10,000 fake Wheelbound accounts** and answer questions such as:

> How many Fates does it typically take to afford the fifth Tier-I skill?

> How long until Tier II opens?

> How often can someone afford shop unlocks while still progressing their tree?

> Does buying a Duplicate Slice feel like a meaningful sacrifice, or is it basically free?

> Does someone who completes a Legendary bounty suddenly skip half the progression tree?

> How often do players hit negative FP?

> Can a player recover from negative FP reasonably?

> Is there an obvious optimal upgrade path everyone would take?

That's where I can do a lot of the heavy lifting for you. We'll define target pacing first and then use calculations/simulations to find numbers that produce something close to it.

For instance, instead of you needing to decide:

> Cooking = 75 FP  
> Prayer = 120 FP  
> Slayer = 400 FP  
> Echo Slice = 600 FP...

I'll ask you something much easier to answer:

> **Roughly how many completed Fates should it take before a player can afford their first new skill?**

Your answer could simply be:

> "Maybe 2–3."

Then we can derive an initial economy around that experience.

Same with the first milestone:

> "I'd like most players to reach Tier II after roughly 15–25 Fates."

That's much more useful for balancing than guessing individual prices.

### We should also intentionally make spending hurt a little

There are going to be competing uses for FP.

Imagine you've accumulated `450 FP`:

```text id="3pmly8"
FATE POINTS: 450

Unlock Slayer ............... 400 FP
Upgrade Mining Slice ........ 250 FP
Unlock Aubury's Rune Shop ... 175 FP
Add Second Fishing Slice .... 300 FP
Save toward ??? ............. ???
```

**That's the decision-making I want.**

If the player always has enough FP for everything as soon as it becomes available, then FP isn't really a currency—it's just an XP bar.

Conversely, if everything is painfully expensive, they'll hoard FP and avoid interacting with the systems we've built.

We'll try to find the middle.

And because we've already committed to a **Developer Mode + simulator**, we can make these values configurable during development rather than burying magic numbers throughout the code. That will make balance passes much easier.

So for now I'd put almost every numerical value in the blueprint as something like:

> **Cost: BALANCE_TBD**

unless the number itself is fundamental to the mechanic.

We'll finish designing **what players can do**, then we'll do a dedicated **Fate Point Economy & Balance pass** where I can model the earning rates, sinks, progression pacing, penalties and bounty rewards together.

That is definitely an area where it makes sense for me to take on most of the number juggling while you make the higher-level call of **"does this progression pace sound fun?"** memcite

## S8-M0125 - user - 2026-09-30T16:35:35.043000+00:00
Message ID: 7b80762d-e084-4247-a8fd-51ff68ff74b0

whats next are we done with skilling ?

## S8-M0126 - assistant - 2026-09-30T16:35:35.913045+00:00
Message ID: d997a66c-9a17-4259-be85-d627dde6a34e

I think we're **done with the structural design of Skilling for now**. There are still details to design, but continuing deeper would start pulling us into balance and implementation before the rest of Wheelbound exists.

We've locked the important pieces: starting access, Tier I with 9 skills and a **5/9 milestone**, Tier II with 6 skills and a **4/6 milestone**, circular tier clusters, the gradually filling gold Master Progression Line, milestone rewards, separate Wheel Builder UI, and Skilling Mastery based on **individual slice manipulation**—including equal-size duplicate slices and independently upgradeable slice sizes. No Favored Skill system. memcite

Things like exact FP costs, slice sizes (`1.25x` vs `1.5x`), duplicate limits, milestone rewards, XP ranges, and skill-specific XP modifiers should all wait for our dedicated balancing pass.

So I'd move to the **next major branch: Questing**.

And I'd start with one question only:

**What does landing on "Questing" actually mean?**

My recommendation is that Questing works differently from Skilling. Instead of every individual quest occupying a wheel slice, the active wheel contains a single **Questing slice**. If Fate lands on Questing, Wheelbound then selects a quest from the player's currently eligible unlocked quest pool.

```text
MAIN FATE WHEEL
      │
      ▼
   QUESTING
      │
      ▼
Eligible quests from
unlocked quest tiers
      │
      ▼
Random selection
      │
      ▼
 ┌───────────────┐
 │ DRAGON SLAYER │
 │   QUEST FATE  │
 └───────────────┘
```

That keeps the main wheel from eventually having 100+ quest slices and fits our existing rule that **Wheelbound should only roll quests the player currently qualifies to complete**.

If you like that fundamental behavior, I'd lock it first and then we can design the Questing progression tree.

## S8-M0127 - user - 2026-09-30T16:36:49.944000+00:00
Message ID: da142cd4-639c-48f7-90ed-f9da39a7270b

yeah i think the questing branch will be quite bare compared. 

its just available quest and then we need to unlock the various tiers of the quest as well but i cant think of anything else

## S8-M0128 - assistant - 2026-09-30T16:36:52.394862+00:00
Message ID: 88c812d8-e8c0-4612-adde-5b13ca0e7fc4

I agree, and I **wouldn't try to artificially inflate the Questing branch** just so it's visually as large as Skilling. That would violate our "no filler nodes" rule.

Questing can be a smaller, cleaner branch.

I'd start with something like:

```text id="4g91xu"
                         QUESTING
                            ║
                            ▼
                    [QUEST FATES]
                     Starting access
                            ║
                            ▼
                   LOW-TIER QUESTS
                    Already available
                            ║
                            ▼
                      [MILESTONE]
                            ║
                            ▼
                    MEDIUM QUESTS
                            ║
                            ▼
                      [MILESTONE]
                            ║
                            ▼
                     HARD QUESTS
                            ║
                            ▼
                      [MILESTONE]
                            ║
                            ▼
                    MASTER QUESTS
                            ║
                            ▼
                      [MILESTONE]
                            ║
                            ▼
                GRANDMASTER QUESTS
```

The important part is that **unlocking a tier expands the eligible Quest Fate pool**. It doesn't immediately assign anything.

If Questing gets spun:

```text id="r9tb4c"
Questing selected
       ↓
Build eligible quest pool
       ↓
Unlocked quest tier?
       ↓
Requirements currently met?
       ↓
Quest incomplete?
       ↓
Randomly select from remaining pool
       ↓
QUEST FATE
```

That preserves the rule we've already established that Wheelbound shouldn't hand you a quest you're currently incapable of completing.

### There is one mechanic I think belongs here

Rather than inventing lots of upgrades, I'd give Questing its own version of **progression control**.

For example, deeper in the branch we could eventually have something like:

> **Second Prophecy** — When Questing is rolled, reveal two eligible quests and Fate randomly selects between them / or player chooses one.

But I'm hesitant about player choice because that starts weakening the wheel.

A better one might be:

> **Quest Reroll** — Gain a very limited ability to reject the selected Quest Fate and roll another eligible quest.

Maybe it's expensive, limited, or requires spending FP.

However, even that starts overlapping with **Fate Manipulation**, which we've specifically said should handle manipulation of what happens after a category is chosen.

So I actually think we should **resist adding it here**.

### What could legitimately belong in Questing?

There is one category that doesn't feel like filler: **quest-specific progression rewards**.

For example, completing Quest Fates could contribute toward the Questing branch's milestones:

```text id="eukknz"
             MEDIUM QUEST ACCESS
                      │
                      ▼
              Complete Quest Fates
                    0 / 3
                      │
                      ▼
              QUEST MILESTONE
                      │
                      ▼
               HARD QUEST ACCESS
```

Now we have an interesting alternative to simply:

> Pay 300 FP → unlock Hard quests.

We could require **both**:

```text id="l7ux6u"
Unlock HARD QUESTS

✓ Purchase node — ??? FP
✓ Complete 3 Medium Quest Fates

              AND
               │
               ▼
        HARD QUESTS OPEN
```

**That I like a lot.**

Because quest progression then requires you to actually **do quests**, not just grind Mining Fates until you have enough FP to buy Grandmaster quest access.

And it gives our AND prerequisite system a genuinely meaningful use.

So I'd propose the Questing branch intentionally stays small, but its progression is more achievement-driven:

```text id="o5t97n"
LOW QUESTS
    ║
    ▼
Complete X Low Quest Fates
+ FP unlock
    ║
    ▼
MEDIUM QUESTS
    ║
    ▼
Complete X Medium Quest Fates
+ FP unlock
    ║
    ▼
HARD QUESTS
    ║
    ▼
Complete X Hard Quest Fates
+ FP unlock
    ║
    ▼
MASTER QUESTS
    ║
    ▼
Complete X Master Quest Fates
+ FP unlock
    ║
    ▼
GRANDMASTER QUESTS
```

Exact `X` values are **BALANCE_TBD**.

I think that makes the Questing branch small **without making it boring**, and every node has a legitimate reason to exist.

**That's the one Questing mechanic I'd suggest we decide on:** should advancement require completing Quest Fates from the previous tier **in addition to spending FP**? I lean yes.

## S8-M0129 - user - 2026-09-30T16:37:49.438000+00:00
Message ID: 097bf84b-c8aa-4efb-8a55-304afb78a45a

completed x amount of each tier before unlocking the next? the milestone rewards can just be builtinot the unlocks instead of having 'gates' like we do in skilling ?

## S8-M0130 - assistant - 2026-09-30T16:37:52.280230+00:00
Message ID: c1506965-7618-4ad9-89fa-4db77cc4ffe6

Yes — I think that's cleaner. **Questing doesn't need Skilling-style milestone/gate nodes at all.** The tier unlock itself can be the milestone and reward.

I'd structure it as:

```text
START
  │
  ▼
LOW-TIER QUESTS
Available from the beginning
  │
  │ Complete X Low-tier Quest Fates
  │ + Spend X FP
  ▼
┌─────────────────────────────┐
│     MEDIUM QUESTS           │
│                             │
│ Unlocks Medium quest pool   │
│ + Built-in milestone reward │
└─────────────────────────────┘
  │
  │ Complete X Medium Quest Fates
  │ + Spend X FP
  ▼
┌─────────────────────────────┐
│       HARD QUESTS           │
│                             │
│ Unlocks Hard quest pool     │
│ + Built-in milestone reward │
└─────────────────────────────┘
  │
  │ Complete X Hard Quest Fates
  │ + Spend X FP
  ▼
┌─────────────────────────────┐
│      MASTER QUESTS          │
│                             │
│ Unlocks Master quest pool   │
│ + Built-in milestone reward │
└─────────────────────────────┘
  │
  │ Complete X Master Quest Fates
  │ + Spend X FP
  ▼
┌─────────────────────────────┐
│   GRANDMASTER QUESTS        │
│                             │
│ Unlocks Grandmaster pool    │
│ + Major milestone reward    │
└─────────────────────────────┘
```

So there isn't a separate:

`Complete 5 quests → milestone node → buy next-tier node`

It's simply:

**Meet the quest-completion requirement + pay the FP → purchase the next tier.**

And the tier node itself can celebrate the milestone. When you purchase **Hard Quest Access**, for example, the gold animation runs into that node, the Hard quest pool becomes available, and whatever milestone bonus we've assigned is awarded simultaneously.

One thing I particularly like is that **only Quest Fates should count toward the requirement**. If the requirement eventually becomes `Complete 4 Medium Quest Fates`, doing a Medium quest while Wheelbound is paused or doing one outside an assigned Quest Fate doesn't count. That keeps progression tied to actually obeying Fate rather than just general account progression.

We can leave all the numbers and milestone rewards as `BALANCE_TBD`.

I think that gives Questing enough identity without padding it.

### One question before we call the basic Questing structure finished

There's a potentially important edge case:

You unlock Medium quests and need, say, `X Medium Quest Fates` to reach Hard. But eventually you may have **completed every currently eligible Medium quest**.

When Questing gets spun, should it choose from **all unlocked tiers** or should it prioritize the **highest unlocked tier**?

For example, after unlocking Medium:

```text
A) Quest Fate pool:
   Low + Medium eligible quests

B) Quest Fate pool:
   Medium eligible quests first
   ↓
   fall back to Low only if no Medium quest is eligible
```

I lean toward **B**. Otherwise the player could keep rolling Low quests while desperately needing Medium completions to progress the branch. That could make Questing progression frustrating for reasons outside their control.

## S8-M0131 - user - 2026-09-30T16:38:57.746000+00:00
Message ID: 97f9d5be-8cb6-420b-981d-ad22acd4f830

something to consider is the master fate wheel, lets discuss whats on this because there are quest requirements and they should be able to do these regaurldes otherwise they become locked

## S8-M0132 - assistant - 2026-09-30T16:38:58.732971+00:00
Message ID: fa12edef-d319-4961-97e2-e14b2697697d

Yes. This exposes a **core rule we need to settle before designing Questing further**.

If Wheelbound says you're only allowed to train/do what Fate permits, then **quest prerequisites must be exempt somehow**. Otherwise the system can generate genuine hardlocks.

Example:

```text
ACTIVE FATE
Complete: Some Quest
        │
        ├── Requires 30 Agility
        │
        └── Player has 27 Agility
                 │
                 ▼
       Normally Agility is restricted
                 │
                 ▼
              HARDLOCK
```

Our earlier idea of only selecting quests the player already qualifies for avoids that particular case, but it creates another problem: **Wheelbound may never give you permission to train the prerequisite**, so that quest never becomes eligible in the first place.

That brings us back to what you called the **Master Fate Wheel**.

I think we should define that before going farther with Questing.

### First question: what categories actually live on the Master Fate Wheel?

My current mental model has been something like:

```text
                 MASTER FATE WHEEL

       ┌──────────────┬──────────────┐
       │              │              │
     SKILL          QUEST          COMBAT?
       │              │              │
       ▼              ▼              ▼
 Specific skill    Quest pool    Boss/activity
       │
       ▼
   Fate Card
```

But I'm starting to think **Combat shouldn't be a separate category** if Combat is already represented by its own slice(s) alongside Mining/Fishing/etc.

It may actually be much simpler:

```text
MASTER FATE WHEEL

┌─────────┐
│ Mining  │
├─────────┤
│ Fishing │
├─────────┤
│ Combat  │
├─────────┤
│ Quest   │
├─────────┤
│ Slayer  │  ← once unlocked/added
├─────────┤
│ Prayer  │  ← once unlocked/added
├─────────┤
│ ...     │
└─────────┘
```

Then later we need to determine where **Bossing** fits.

And this immediately gives us a possible solution to the quest issue: **Quest Fate itself could temporarily authorize its prerequisites.**

For example:

```text
FATE: Complete Desert Treasure I

Temporary Fate Permissions
──────────────────────────
✓ Quest completion
✓ Required prerequisite quests
✓ Required skill XP needed to meet requirements
✓ Required combat/activity needed for the quest

Everything unrelated remains restricted.
```

That would mean Wheelbound can actually say:

> **This is your Fate. You are permitted to do whatever is reasonably necessary to fulfill it.**

I think that's philosophically stronger than only allowing quests the player *already* meets every requirement for.

But I don't want to lock that yet, because it has major consequences for the game mode.

So **one decision first:** let's define exactly what belongs on the **Master Fate Wheel**. Once that's settled, we can solve quest prerequisite permissions around it rather than patching Questing in isolation.

## S8-M0133 - user - 2026-09-30T16:42:25.630000+00:00
Message ID: a7dbb0f6-ebdb-4a92-bc75-f0dd2dd42a65

so im imagining 1 wheel that you spin and build 

all the skills are on there as well as the a questing slice if it lands on the questing slice this will (either spin another weel with available quests OR present you with 1 of 3 quests to choose from, this will be default and can upgrade to hold up to a max of 5 chpices <- im leaing towards this one )

## S8-M0134 - assistant - 2026-09-30T16:42:27.004665+00:00
Message ID: e06305c2-440c-4cec-8a67-5f7b2dd39777

Yes — **this clarifies the Master Fate Wheel significantly**, and I prefer the choice system over spinning a second quest wheel.

I'd lock the core wheel concept as:

```text
                 MASTER FATE WHEEL
                         │
      ┌──────────────────┼──────────────────┐
      │                  │                  │
   SKILL SLICE       QUESTING SLICE      future content
      │                  │
      ▼                  ▼
 Mining / Fishing    Generate eligible
 Slayer / etc.        quest choices
      │                  │
      ▼                  ▼
 Skill Fate         Choose your Quest Fate
```

There is **one wheel that the player builds and physically modifies**. We shouldn't have a hierarchy of wheels every time a special category gets hit.

### Questing would behave differently from a skill slice

If the wheel lands on Mining:

> **Mining Fate → Gain X Mining XP**

If it lands on Questing:

> **Quest Fate → Fate presents 3 eligible quests. Choose one.**

Something like:

```text
              FATE HAS CHOSEN:
                   QUESTING

       ┌─────────┐ ┌─────────┐ ┌─────────┐
       │ Quest A │ │ Quest B │ │ Quest C │
       │         │ │         │ │         │
       │  icon   │ │  icon   │ │  icon   │
       │         │ │         │ │         │
       │ [CHOOSE]│ │ [CHOOSE]│ │ [CHOOSE]│
       └─────────┘ └─────────┘ └─────────┘
```

And I **really like 3 choices as the default** rather than only one.

OSRS quests vary wildly. Giving exactly one random quest could result in Fate saying *go do this 2-hour quest right now*, which could make players dread landing on Questing. Three still preserves randomness because Fate determines the options, but gives the player some agency.

Then Questing progression has a very obvious additional upgrade path:

```text
QUESTING
   │
   ├── Quest Tier Unlocks
   │      Low
   │       ↓
   │     Medium
   │       ↓
   │      Hard
   │       ↓
   │     Master
   │       ↓
   │   Grandmaster
   │
   └── Quest Choice Upgrades
          3 choices  ← DEFAULT
              ↓
          4 choices
              ↓
          5 choices  ← MAX
```

Those **4th and 5th quest choices** are actually meaningful progression nodes rather than filler.

And importantly, I would make the choices **distinct quests**. You never get duplicate cards wasting one of your choices.

### This also gives us a better way to handle requirements

I think there's an important distinction we should make next.

Suppose Fate presents:

**Monkey Madness II**

but you're missing a prerequisite quest or skill level.

Should that quest even be allowed to appear among your 3 choices?

My recommendation is **no**.

The three cards should be generated from your **currently completable quest pool**:

```text
Unlocked quest tiers
        ↓
Not already completed
        ↓
Prerequisite quests completed
        ↓
Skill requirements met
        ↓
Other detectable hard requirements met
        ↓
ELIGIBLE QUEST POOL
        ↓
Randomly draw 3
```

But this brings us directly to the problem you just identified: **how does the player ever get those requirements?**

That's the next decision I'd solve before we continue Questing.

Specifically: **when you're not currently on a Quest Fate, should Wheelbound allow you to complete prerequisite quests freely when they're needed to make progress toward currently locked/unavailable quests?**

I don't think the answer should simply be "yes, all prerequisites are always free," because that could accidentally create a giant loophole where much of Questing can be done without ever landing on Questing.

So that's the next piece I'd work through. memcite

## S8-M0135 - user - 2026-09-30T16:43:45.133000+00:00
Message ID: 187d735d-741b-4d9e-8255-4801a3b44f06

no no so the master wheel 0/24 open slots (you can add mining fishing etc and questing is a slice on its own) i dont want to spin a wheel that has (skilling, questing) then get presented another wheel its just 1 wheel

## S8-M0136 - assistant - 2026-09-30T16:43:46.129184+00:00
Message ID: ef3c20e2-f94a-4012-8b7a-f017a949ba23

Yes — I had added an extra layer you **do not want**. Got it now.

There is exactly **ONE wheel: the Master Fate Wheel**, with up to 24 active slices.

```text
             MASTER FATE WHEEL
                 6 / 24 SLOTS

        ╭────────────────────────╮
        │                        │
        │   Mining      Fishing  │
        │                        │
        │ Combat       Questing  │
        │                        │
        │   Slayer      Prayer   │
        │                        │
        ╰────────────────────────╯
```

Each thing added through the Wheel Builder occupies a physical slice/slot.

So you could eventually have:

```text
MASTER WHEEL — 12/24

Mining
Mining          ← duplicate slice
Fishing
Woodcutting
Combat
Questing        ← special slice
Slayer
Slayer          ← duplicate slice
Prayer
Crafting
Hunter
Agility
```

There is **no "Skilling" slice**. Mining is a slice. Fishing is a slice. Slayer is a slice. Etc.

There is also **no second Quest wheel**.

When the physical Questing slice wins:

```text
MASTER WHEEL
     ↓
  QUESTING
     ↓
Fate searches eligible quest pool
     ↓
┌──────────┐ ┌──────────┐ ┌──────────┐
│ Quest A  │ │ Quest B  │ │ Quest C  │
│          │ │          │ │          │
│ CHOOSE   │ │ CHOOSE   │ │ CHOOSE   │
└──────────┘ └──────────┘ └──────────┘

Choose ONE
     ↓
That becomes your Active Fate
```

And I think your **3 → 4 → 5 choice progression** is strong enough that I'd make that our current Questing design:

**Default:** Fate presents 3 random eligible quests.  
**Upgrade:** Fate presents 4.  
**Final upgrade:** Fate presents 5.  
**Maximum:** 5.

The player gets agency over the final selection, but **doesn't get to choose which quests Fate offers them**.

Also, importantly, **Questing itself is one slice regardless of how many quest tiers you've unlocked.** Unlocking Medium/Hard/Master/Grandmaster expands what that one Questing slice can generate; it doesn't add more Questing slices.

### CONFIRMED — Master Fate Wheel

I'm treating this as locked:

> **Wheelbound has one primary wheel. It has a working maximum of 24 active slots. Skills, Questing, and any other future Fate categories occupy slices directly on this same wheel. There is no category wheel before it.**

That's actually simpler and makes the **Wheel Builder** much more central to the entire game mode.

And this brings us right back to the issue you raised before correcting me: **quest requirements.**

That's the next thing we should solve, because the Questing slice can't work properly until we decide how prerequisites interact with Fate restrictions. memcite

## S8-M0137 - user - 2026-09-30T16:44:32.048000+00:00
Message ID: f7b5d366-dcd1-4389-8053-ac283e631f1b

yes and then you can go into the 'wheel builder' and edit it etc

## S8-M0138 - assistant - 2026-09-30T16:44:34.502349+00:00
Message ID: e0513c13-de86-4752-81aa-587fd0fcdede

Exactly. **The Master Fate Wheel is the wheel you play; the Wheel Builder is where you construct and modify it.**

I'd lock that distinction:

```text
MASTER FATE WHEEL
────────────────────────
The actual gameplay wheel.

          ↕ edited by

WHEEL BUILDER
────────────────────────
24 total slots

• Add unlocked slices
• Add duplicate slices
• Remove slices (costly/difficult)
• Select individual slices
• Upgrade individual slice weight
• See exact resulting probabilities
• Rearrange slices visually
```

So if your wheel currently contains:

```text
8 / 24 SLOTS

Mining        1.0x
Fishing       1.0x
Woodcutting   1.0x
Combat        1.0x
Questing      1.0x
Slayer        1.5x  ← upgraded slice
Slayer        1.0x  ← separate duplicate
Prayer        1.0x
```

that's **literally what appears when you go spin the Master Fate Wheel**. No intermediate category selection.

I also think **rearranging slices should be free** because their position shouldn't affect probability; it's cosmetic organization. Adding/removing/upgrading is where the actual Wheelbound costs/rules live.

And the Wheel Builder should show the real probability of each slice as you modify it. If one Slayer slice is physically larger because it's `1.5x`, the probability calculation and rendered wheel should match exactly.

**CONFIRMED:** Master Fate Wheel = gameplay. Wheel Builder = construction/customization interface for that exact same wheel.

Now we can return to the thing that exposed this discussion in the first place: **how quest requirements work without creating Wheelbound hardlocks.** memcite

## S8-M0139 - user - 2026-09-30T16:45:41.537000+00:00
Message ID: 8b30ba78-3a18-4b7a-bd93-407ed0961820

Now what i was tlaking about before was your grand task (this should be a special wheel) red skull in the middle chains around it spikes on the end make it hard core this is the final challenge . 

what are the options for this ? whats some of the hardest conent in the game ?

## S8-M0140 - assistant - 2026-09-30T16:45:46.803185+00:00
Message ID: e6580607-38ec-4dab-80de-5dc8478751e2

Yes — **this is different from the Master Fate Wheel.** This is the wheel you spin at the very beginning of a Wheelbound run to determine your **Grand Fate / final challenge**.

And visually I think we should go hard with it:

```text
        ⛓ ╱▲╲ ⛓
     ╱             ╲
   ▲    GRAND FATE    ▲
  ⛓       ☠          ⛓
   ▲                 ▲
     ╲             ╱
        ⛓ ╲▲╱ ⛓

Dark red / black
Iron spikes
Chains wrapped around rim
Skull centerpiece
Heavy metallic spin sounds
Very different from normal gold Fate wheel
```

You spin this **before the normal Wheelbound journey begins**, and whatever it lands on becomes the ultimate objective hanging over the entire account.

### What belongs on it?

We shouldn't just grab random bosses. These should be recognizable **endgame accomplishments** that reasonably justify building an entire account toward them.

Current OSRS has several excellent candidates. The Inferno remains a major solo endurance challenge: 69 waves ending with TzKal-Zuk. citeturn1search1 Fortis Colosseum is another extremely difficult solo challenge culminating in Sol Heredit. citeturn1search0 Yama also has substantially harder contracted variants beyond his normal endgame encounter. citeturn1search5turn1search2

For our purposes, I'd consider a candidate pool roughly like:

- **The Inferno** — obtain an Infernal Cape.
- **Fortis Colosseum** — obtain Dizana's Quiver.
- **Awakened DT2** — defeat all four Awakened Desert Treasure II bosses.
- **Theatre of Blood** — complete ToB, potentially with a harder variant of this Grand Fate later.
- **Tombs of Amascut** — complete at a specified high invocation rather than merely completing entry-level ToA.
- **Chambers of Xeric / Challenge Mode** — potentially a CM completion.
- **Yama Contract challenge** — one of the genuinely difficult contracted Yama accomplishments rather than normal Yama. Contract fights substantially increase his stats/mechanics and impose additional restrictions. citeturn1search2
- **Grandmaster Combat Achievement milestone** — interesting, although this is MUCH broader than the others and would fundamentally change the length of a run.

I would **not** put things like Fire Cape, Corrupted Gauntlet completion, normal DT2 bosses, normal Vorkath, Zulrah, etc. on this wheel. They're meaningful account milestones, but this wheel should make you go:

> **"Oh fuck. THAT'S my end goal."**

### But there's something important about Grand Fate

I don't think every Grand Fate needs to be equally difficult in the exact same way.

We could eventually have:

```text
GRAND FATES

ENDURANCE
Inferno
Fortis Colosseum

RAIDS
Theatre of Blood
High-invocation Tombs of Amascut
Challenge Mode Chambers

BOSS MASTERY
Awakened DT2 bosses
High-level Yama contract challenge

ACCOUNT MASTERY
Grandmaster Combat Achievement objective
```

But **the wheel itself doesn't need to expose those categories**. They're just useful internally for us to make sure the pool has variety.

And this connects directly to your concern about quest requirements.

### Grand Fate should override normal progression locks

This is where I think we need a special rule.

If your Grand Fate is:

> **Complete the Inferno**

then Wheelbound can't accidentally prevent you from ever accessing the Inferno.

Likewise if we have a Grand Fate requiring a quest chain, Wheelbound cannot say:

> Your ultimate destiny requires Quest X  
> ...but Quest X isn't permitted by your Wheelbound progression.

That's bad design.

I'd establish:

> **Grand Fate requirements are always obtainable. Wheelbound may make you work toward them, but its own restrictions can never permanently prevent you from satisfying a prerequisite of your Grand Fate.**

We don't yet have to decide *how* that exemption works. That's probably its own mechanic.

---

But before we tackle prerequisites, **there's one Grand Fate design decision I'd make first:**

Should the starting hardcore wheel contain **only brutally difficult PvM accomplishments**, or should it also contain extremely difficult **non-PvM/account accomplishments**?

For example, could someone's Grand Fate be something like a massive skilling/quest/account challenge, or do you want the skull wheel's identity to specifically be:

> **"At the end of your Wheelbound journey, you must defeat one of OSRS's greatest combat challenges."**

I'm leaning strongly toward **combat/endgame PvM only**. It gives every Wheelbound run a climactic final boss rather than occasionally ending with "get 99 Runecraft." memcite

## S8-M0141 - user - 2026-09-30T16:55:07.967000+00:00
Message ID: c0a25d01-96bc-4050-b1e7-0d2d35070a8f

okay here are the end goal slices 

colo 
inferno 
get radient oath plate  
all awakend dt2 bosses
master combat achievements

## S8-M0142 - assistant - 2026-09-30T16:55:10.828262+00:00
Message ID: c9e3e895-04a1-411f-a6e9-23ef152b552e

Yes. That gives the **Grand Fate Wheel five slices**.

### CONFIRMED — Grand Fate Wheel

```text
              GRAND FATE
                  ☠
       ╭─────────────────────╮
       │                     │
       │  FORTIS COLOSSEUM   │
       │                     │
       │      INFERNO        │
       │                     │
       │ RADIANT OATHPLATE   │
       │                     │
       │ ALL AWAKENED DT2    │
       │       BOSSES        │
       │                     │
       │ MASTER COMBAT       │
       │   ACHIEVEMENTS      │
       │                     │
       ╰─────────────────────╯
```

Specifically, I'd define the objectives as:

- **Colosseum** → successfully complete Fortis Colosseum / obtain Dizana's Quiver.
- **Inferno** → successfully complete the Inferno / obtain an Infernal Cape.
- **Radiant Oathplate** → obtain the Radiant Oathplate. We should later specify exactly which piece/set state counts so the code has an unambiguous completion condition.
- **Awakened DT2** → defeat **all four Awakened Desert Treasure II bosses**.
- **Master Combat Achievements** → reach the **Master Combat Achievement tier**.

I also like that these aren't five copies of essentially the same challenge. One is Colosseum mastery, one Inferno, one item/progression goal, one multi-boss challenge, and one broad PvM-account accomplishment.

And **these should all be equal slices**. I wouldn't allow slice modifiers, duplicates, rerolls, or Wheel Builder mechanics on this wheel. You spin it once at the beginning of the run and **Fate has spoken**.

Visually, this is also completely separate from the normal Master Fate Wheel: **red/black, skull center, chains, spikes, heavy animation/sound**. The normal wheel is something you eventually learn to manipulate; the Grand Fate Wheel should feel like something you **cannot** manipulate.

The next question I think we need to answer is important:

**After the Grand Fate wheel stops, do you get one final "Accept Fate" confirmation before the Wheelbound run officially begins, or is the instant it stops completely binding?**

I'd lean toward **Accept Fate** because it protects against accidental starts/testing/UI mistakes without giving the player a reroll.

## S8-M0143 - user - 2026-09-30T16:57:41.547000+00:00
Message ID: ce23e87b-4a40-4920-b097-7aa9f4b1b390

what the other option if you dont accept ?

## S8-M0144 - assistant - 2026-09-30T16:57:43.023885+00:00
Message ID: 44d96d80-5412-422e-a878-c87b677b5f77

That's the problem — **there shouldn't really be another option.** If declining lets you spin again, then the Grand Fate isn't fate; players can reroll until they get what they want.

So I'd change my previous recommendation.

Once the player deliberately presses something like **“SPIN YOUR GRAND FATE”**, with a warning beforehand, **the result is binding**.

```text
[ SPIN YOUR GRAND FATE ]

⚠ Your Grand Fate cannot be rerolled.
This will determine the final challenge
of your Wheelbound journey.

        [ CANCEL ]   [ SPIN ]
                         ↓
                    WHEEL SPINS
                         ↓
                   ☠ INFERNO ☠
                         ↓
                    FATE SEALED
```

The confirmation belongs **before the spin**, not afterward.

Then the result gets the big dramatic reveal and is permanently recorded for that Wheelbound run.

I'd lock that unless we later decide to build some extraordinarily costly "Defy Grand Fate" mechanic—which, frankly, I don't think we should. The whole appeal of this particular wheel is **you get what you get.** memcite

## S8-M0145 - user - 2026-09-30T16:59:28.582000+00:00
Message ID: 9e9dd8dc-10d0-495a-b2b9-616b5645fddc

yeah lets do that and then in the side bar there needs to be some kinda thing wher eyou can view your grand fate we need to flush out that whole ui and art ect but that will be later and a lot fo image generation , codex used something to generate me various icons... id like to do that will all the uis going forward so there will be a point where you lay out all the differnet uis and then generate me like a couple options and i would pick one etc

## S8-M0146 - assistant - 2026-09-30T16:59:54.680609+00:00
Message ID: fdc48db8-867c-4215-96ca-659b72799c34

Yes — let's explicitly **defer the detailed UI/art pass** rather than designing screens piecemeal while we're still changing mechanics.

### CONFIRMED — Grand Fate

The Grand Fate flow is now:

```text
Start Wheelbound
      ↓
Grand Fate introduction
      ↓
Warning:
"This spin cannot be rerolled."
      ↓
[CANCEL]       [SPIN]
                 ↓
        Special Grand Fate Wheel
        ☠ Red / chains / spikes
                 ↓
           Result lands
                 ↓
          FATE IS SEALED
                 ↓
     Permanently tied to this run
```

And during the run, the RuneLite sidebar needs a clear place where the player can **view their Grand Fate at any time**. We don't need to decide yet whether that's part of Account, its own prominent header/card, etc. We'll solve that during UI design.

### Later: dedicated UI/Art phase

I think your proposed workflow is exactly how we should handle it. Once the mechanics are sufficiently nailed down, we'll stop and inventory **every interface Wheelbound needs** rather than having Codex improvise them independently.

For example, we'll eventually be designing things like the Master Fate Wheel, Wheel Builder, Grand Fate Wheel + reveal, Active Fate/card, Progression Tree, Quest-choice screen, Bounties, Account/status, shop lock/Tempt Fate interaction, Defy Fate, notifications/reward reveals, and Developer Mode.

Then we'll tackle **one screen at a time**.

For each one, I'll first work out the information hierarchy and required controls. Then we can use image generation to make **2–3 actual visual concepts** for you to compare. You pick one, tell me what you like/hate, we iterate visually, and only once you're happy do we turn that into an implementation specification for Codex.

That will also let us establish a reusable **Wheelbound art kit**—frames, backgrounds, node treatments, skulls, chains, buttons, Fate cards, borders, icons, textures, etc.—so the plugin feels like one deliberately designed game interface instead of a collection of unrelated RuneLite panels.

And yes, we can generate the actual visual assets where appropriate, not merely give Codex text descriptions and hope it interprets them correctly.

For now I'd mark:

**UI/UX + ART DIRECTION → LATER DEDICATED DESIGN PHASE**

and keep working through the game systems.

For the **Grand Fate system itself**, I think the next thing we need to solve is the issue that originally brought us here: **Grand Fate prerequisites and how Wheelbound guarantees the player can eventually reach their assigned final challenge without its own restrictions hardlocking them.** memcite

## S8-M0147 - user - 2026-09-30T17:00:20.368000+00:00
Message ID: 0ef33c4b-4f37-4eb8-9ce5-de31d4cc14cf

whats next

## S8-M0148 - assistant - 2026-09-30T17:00:22.244979+00:00
Message ID: 4e82a1e1-049a-4b7f-816f-458fac21b8f9

Next I think we should settle the **quest/prerequisite problem**, because it affects the entire game mode and every Grand Fate we just chose.

The issue is that your Grand Fate may ultimately require a huge chain of account progression. For example, **all Awakened DT2 bosses** requires reaching Desert Treasure II, which itself has quest and skill prerequisites. Master Combat Achievements can similarly require access to a huge range of content.

We need Wheelbound to restrict you without ever creating:

> “My Fate requires X, but Wheelbound's rules make X impossible.”

### The first decision

I think we need a concept of **Required Progression**.

My recommendation is that **quests themselves should be freely completable when they are prerequisites for content you have legitimately unlocked through Wheelbound — but they do NOT count as completing a Quest Fate.**

For example:

```text
Grand Fate
ALL AWAKENED DT2 BOSSES
        │
        ▼
Eventually need Desert Treasure II
        │
        ▼
DT2 has prerequisite quests
        │
        ▼
Those prerequisite quests may be completed
without rolling Questing
```

But this would **not** mean:

> “Grand Fate is Awakened DT2, therefore go complete DT2 immediately.”

We can still gate *when* that progression becomes relevant/available.

The bigger question is whether you like the fundamental rule:

**Required prerequisite quests can be completed outside a Quest Fate when Wheelbound determines they're necessary for legitimately unlocked progression.**

They give **no Questing progression credit and no Quest Fate reward**.

I lean toward **yes**, because otherwise quest prerequisites are going to cause us a ton of awkward hardlocks and edge cases.

Let's decide just that rule first.

## S8-M0149 - user - 2026-09-30T17:06:48.711000+00:00
Message ID: 35ed1a5a-219d-403b-baeb-b0e4f9e20eca

okay so, im going to push back here and introduce a new mechanic. 

the idea is that we get x amount of spings lets say you fail to meet the minimum req to acoplish your grand fate. You now have the option to 'defy fate' (name up to workshopping) but bascially this would give you another x spins BUT your wheel is now tainted im thinking purple chains or something that represents and inffected wheel  (options in the shop to cleanse this ) but you would get more spings and if it lands on an infected slice the punishment is bad, like mastering mixology, a lot of agaility using bronze or low level tools over a long task something. Maybe even a sacrafice system where you give up shops etc for more spings 

but thats the idea and the questing slice on the wheel should be slightly bigger so the user has more oppertunity to select the questing they need. Quest that are pre-reqs to the grand challenge should be a little different (red text or like maybe the same theme as a the wheel but in card form to show you need this ) 

lets use both the ideas here a sacraficing system and then the tainted wheel

## S8-M0150 - assistant - 2026-09-30T17:06:55.522304+00:00
Message ID: 3223a634-ee3a-4422-b0ea-d6d8faa2e085

Yes. **This is much more interesting than automatically exempting prerequisite quests.** It turns the possibility of getting stuck into an intentional part of Wheelbound rather than something we're trying to engineer away.

And I think the **Tainted Wheel + Sacrifice** ideas should work together as parts of the same emergency system.

The core loop I'm now seeing is:

```text
                 GRAND FATE
                     ☠
                     │
                     ▼
              WHEELBOUND RUN
                     │
              Limited Spins
                     │
                     ▼
          Build/progress your account
                     │
              ┌──────┴──────┐
              │             │
         READY FOR       NOT READY
        GRAND FATE       / HARDLOCKED
              │             │
              ▼             ▼
        Attempt Fate      DEFY FATE
                            │
                 ┌──────────┴──────────┐
                 │                     │
             SACRIFICE            TAINT WHEEL
                 │                     │
                 └──────────┬──────────┘
                            ▼
                      + X SPINS
                            │
                            ▼
                  Continue your journey
```

That changes the philosophy significantly:

> **Wheelbound does not guarantee you'll naturally get everything needed for your Grand Fate. You're given a finite opportunity to prepare. If Fate hasn't given you what you need, you have to pay a price to keep going.**

I like that considerably more.

### The Tainted Wheel

Visually, this could be fantastic later.

Your normal Master Wheel starts relatively clean/gold. Once you've Defied Fate:

```text
NORMAL                     TAINTED

   gold trim                purple corruption
      ↓                           ↓
   ╭──────╮                   ⛓╭──────╮⛓
  │ WHEEL │                  │ WHEEL │
   ╰──────╯                   ⛓╰──────╯⛓
                                  ↑
                         purple chains / cracks
```

But the important mechanical part is that **taint actually infects slices**.

Perhaps Defying Fate doesn't turn *every* slice bad. Instead, some normal wheel slots/slices become corrupted.

Example:

```text
12/24 MASTER WHEEL

Mining
Fishing
Combat
Questing
Slayer
Prayer
Crafting
Woodcutting
☣ TAINTED
Agility
☣ TAINTED
Mining
Fishing
```

Then you're spinning your normal wheel knowing:

> *I got the extra spins I desperately needed... but now there are two horrible outcomes sitting on my wheel.*

That creates tension every single spin.

And if the pointer lands on a corrupted slice:

```text
              ☣ FATE IS TAINTED ☣

              PUNISHMENT FATE

        Complete 150 laps of ______

                     OR

        Gain X Agility XP using ______

                     OR

        Complete X Mastering Mixology...

              [ ACCEPT YOUR FATE ]
```

The exact punishments need their own design pass because they need to be **awful but still fun-awful**. We don't want "turn the game off because this sucks" tasks.

### Cleansing becomes another FP sink

This also gives the shop/progression economy something really valuable:

**Cleansing Taint.**

You might have:

```text
TAINT LEVEL: 2

☣ Corrupted Slice
☣ Corrupted Slice

Cleanse one corruption ........ ??? FP
```

And perhaps deeper progression eventually improves cleansing options.

Importantly, I don't think cleansing should undo the fact that you Defied Fate. We can permanently track:

> Defied Fate: 2 times

even if the player's wheel is currently clean.

That could matter later.

---

## Sacrifice

I think Sacrifice should be the **other half of Defy Fate**, but not just another FP payment.

You give up something **permanently meaningful** in exchange for more spins.

Possible sacrifice categories:

```text
SACRIFICE TO FATE

Vendor Access
────────────────
Permanently ban an unlocked shop.

Wheel
────────────────
Destroy/remove an active slice.

Fate Points
────────────────
Surrender a large amount of FP.

Progression
────────────────
Give up some unlocked benefit.

???
────────────────
Potential higher-tier sacrifices later.
```

I especially like **sacrificing shops** because we've already created the `UNLOCKED → BANNED` state.

Imagine you're desperate for more rolls:

> **Sacrifice Ardougne General Store**
>
> This shop will become BANNED.
>
> Reward: +X Master Wheel Spins.

That creates stories.

*"I had to sacrifice three shops because my idiot wheel refused to give me Questing before Inferno."*

That's exactly the sort of emergent experience Wheelbound should generate.

---

### And your Questing idea now makes more sense

I agree that **Questing should naturally have a larger base slice** than a standard skill slice.

That's actually consistent with our slice system:

```text
Standard new skill slice       1.0x

Questing slice                 >1.0x
```

We won't pick the exact weighting until balance simulation.

That doesn't guarantee quests. It simply recognizes that **Questing is a gateway to enormous portions of OSRS**, so Fate gives it somewhat more presence.

And when Questing lands and presents the default three cards:

```text
┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│ QUEST A     │ │ QUEST B     │ │ QUEST C     │
│             │ │             │ │             │
│ Normal      │ │ ☠ REQUIRED  │ │ Normal      │
│             │ │ GRAND FATE  │ │             │
└─────────────┘ └─────────────┘ └─────────────┘
```

A quest that's part of the prerequisite chain toward your Grand Fate gets the **Grand Fate visual treatment**—red/dark card treatment, special border/text/iconography, etc.

Crucially, though, **it's still a choice**.

Fate is telling you:

> *One of these moves you toward your destiny.*

You can still pick something else.

That decision may come back to haunt you when you run out of spins.

**I love that.**

---

### One thing I would NOT do

I wouldn't let the game detect:

> "You literally cannot complete your Grand Fate anymore, therefore Defy Fate has unlocked."

That's unnecessarily complicated and potentially impossible to prove reliably.

Instead, I think the run should simply have a **finite Spin Reserve**.

Something like:

```text
FATE SPINS
██████████████░░░░
14 remaining
```

Every completed Fate consumes one.

When the original allotment is exhausted:

> **Your allotted Fate has run dry.**

Then the player has to either attempt/complete their Grand Fate if ready, or **Defy Fate** to continue.

That is much easier to understand, much easier to implement, and dramatically better for balancing because we can simulate:

> With 40 initial spins, what percentage of simulated accounts acquire enough progression to reasonably pursue each Grand Fate?

The actual initial number is completely `BALANCE_TBD`.

---

So I think we've just found a **major core system**:

**CONFIRMED direction:** Wheelbound runs have a finite number of Master Wheel spins. Grand-Fate prerequisite quests are highlighted when offered by Questing rather than automatically granted. Questing receives an inherently larger base slice. If the player needs additional spins, they can **Defy Fate**, involving **Sacrifice and Taint**. Taint adds dangerous outcomes to the Master Wheel, while sacrifices permanently give something meaningful up. Taint can eventually be cleansed through expensive systems.

I would **not lock exactly how Sacrifice and Taint interact yet**.

That's actually our next single decision:

**When you Defy Fate, should Taint be unavoidable AND you choose a sacrifice for additional spins, or should Sacrifice and accepting Taint be two different ways to purchase additional spins?**

I lean strongly toward **Taint being unavoidable when you Defy Fate**, with sacrifices determining/how many additional spins you receive. Otherwise players will calculate whichever option is cheaper and largely ignore the other system. memcite

## S8-M0151 - user - 2026-09-30T17:10:46.583000+00:00
Message ID: cc25f0eb-ec16-468c-881c-3a6e3e538e41

Every completed Fate consumes one.

When the original allotment is exhausted:

> **Your allotted Fate has run dry.**

Then the player has to either attempt/complete their Grand Fate if ready, or **Defy Fate** to continue.

That is much easier to understand, much easier to implement, and dramatically better for balancing because we can simulate:



yes exaclty ^



taint should give a base of some spins then you start sacrafciing stuff&#x20;

(can we detect if a player has gotten rid of an item.... like dropping and watching an item despawn? )

## S8-M0152 - assistant - 2026-09-30T17:10:55.277538+00:00
Message ID: 17840577-ced8-4908-ab64-8f2aea8cad43

**Yes, with an important caveat:** RuneLite gives us enough events to build a pretty convincing **item sacrifice system**, but I wouldn't treat arbitrary item disappearance as proof of destruction.

RuneLite exposes `ItemContainerChanged`, which explicitly fires for inventory changes including **dropping an item**. It also exposes `ItemSpawned` when an item appears on the ground and `ItemDespawned` when a ground-item pile disappears. citeturn0search1turn0search0turn0search2

So we could potentially track:

```text id="5p0vce"
SACRIFICE: Bandos chestplate

Player has BCP in inventory
        ↓
Player drops BCP
        ↓
Inventory change detected
        ↓
BCP appears on player's tile
        ↓
Plugin marks that specific ground item
        ↓
Player does NOT pick it back up
        ↓
Ground item eventually despawns
        ↓
☠ SACRIFICE ACCEPTED
        ↓
+ X SPINS
```

RuneLite's API gives us the ground item's tile and item information for spawn/despawn events, so correlating those events is technically plausible. citeturn0search3turn0search13

But there are edge cases: scene reloads/world hopping can produce ground-item event behavior that isn't equivalent to "this item naturally despawned"; the API specifically notes that scene loading resets item piles and generates spawn events again. citeturn0search0 So we'd need safeguards before trusting this for a permanent Wheelbound sacrifice.

### I think this could be an awesome mechanic

It would make Sacrifice **physical**, rather than clicking:

> Sacrifice 10m GP → Confirm.

Imagine Defy Fate:

```text id="0pcd4n"
              ☣ DEFY FATE ☣

Your allotted Fate has run dry.

Accepting Taint grants:
              +5 SPINS
            [example only]

Need more?

        OFFER A SACRIFICE
               ☠

Place something valuable upon the ground
and abandon it to Fate.
```

Then when you drop the selected item, Wheelbound could visually mark it:

```text id="l9lt4k"
        ☠ OFFERING TO FATE ☠

       Bandos Chestplate
          14,732,841 gp

       Destruction in 1:12

      Picking this item up
      cancels the sacrifice.
```

And when the actual item disappears:

> **FATE ACCEPTS YOUR OFFERING.**

*Then* the additional spins are awarded.

That's **way cooler** than a button deleting an abstract amount of GP.

We could even base the number of spins on the item's value, although we'd need heavy balancing and anti-exploit rules around what value source counts.

---

And yes, I have your Taint structure now:

```text id="x4vh3u"
ORIGINAL SPINS EXHAUSTED
          ↓
      DEFY FATE
          ↓
   Wheel becomes TAINTED
          ↓
Receive base +X spins
          ↓
Need even more?
          ↓
     SACRIFICE
          ↓
 Shops / Items / FP / etc.
          ↓
 Additional spins
```

So **Taint isn't purchased with the sacrifices**.

Taint is the unavoidable consequence of saying:

> *No. I'm not finished. Give me more Fate.*

You immediately get a base reserve of additional spins, but now corruption is introduced to your Master Wheel. If even those aren't enough—or you want more breathing room—you start **giving Fate things in exchange for additional spins**.

I think that's a much stronger system.

### One decision at a time

Before we design what gets sacrificed, there's one foundational Taint question:

When you first Defy Fate, should **one of your existing Master Wheel slices become corrupted**, or should Defying Fate **add a brand-new Tainted/Punishment slice** to the wheel?

I lean toward **corrupting an existing slice**.

That gives us the visual you described—purple infection spreading over *your wheel*—and importantly doesn't consume one of your precious `24` Wheel Builder slots. Your wheel is still technically the wheel you built... **Fate has just infected part of it.** memcite

## S8-M0153 - user - 2026-09-30T17:12:37.949000+00:00
Message ID: f53748b9-8d14-4e88-9ee4-99ab5a41ca4e

lets work around the dropping an item, i drop something and someone else picks it up?

## S8-M0154 - assistant - 2026-09-30T17:12:41.239965+00:00
Message ID: 4c75aa87-96e9-4448-aa5e-6bf2d462e76e

Yes — **that's exactly the weakness** with waiting for despawn.

If you drop a sacrifice and another player picks it up, RuneLite will see the ground item disappear, but `ItemDespawned` itself only tells us that the ground item disappeared; it does **not** say *why* it disappeared or identify another player as the picker. citeturn0search2turn0search3

So I would **not make natural despawn the requirement**.

Instead, I think we can design the sacrifice around an **intentional drop + surrender period**.

### Item Sacrifice flow

The player first tells Wheelbound what they're sacrificing:

```text
☠ OFFER A SACRIFICE

Selected:
Bandos Chestplate

Value: ~XXm
Reward: +X Spins

[ BEGIN SACRIFICE ]
```

Then:

```text
Drop the Bandos Chestplate
        ↓
Wheelbound detects inventory removal
        +
matching ground item appears
        ↓
☠ OFFERING REGISTERED ☠
```

RuneLite exposes inventory-container changes for dropping items, and ground-item spawn events give us the item and tile, so those two signals can be correlated. citeturn0search0turn0search1

Then we have a **short surrender window**, not the full OSRS despawn timer.

```text
☠ OFFERING TO FATE ☠

Bandos Chestplate

Do not reclaim your offering.

████████████████░░░
        10 seconds
```

If **you pick it back up**, RuneLite sees your inventory increase again, so:

> **Sacrifice rejected — you reclaimed your offering.**

`ItemContainerChanged` explicitly covers both dropping and picking up items. citeturn0search0

But if another player picks it up during those 10 seconds?

### I think Fate should accept it.

That's actually thematically perfect.

You willingly relinquished ownership. Whether the item:

- despawns,
- gets picked up by another player,
- gets sniped by your friend,
- sits there afterward,

doesn't really matter.

The sacrifice is:

> **You gave the item up.**

Not:

> **Jagex deleted the item from existence.**

That also removes a bunch of unreliable technical detection.

There **is an exploit**, though:

> Drop 100m item → friend picks it up → friend gives it back later → free spins.

So we need to decide whether Wheelbound is intended to be **honor-system challenge mode** or whether we want to actively defend against that.

And I actually lean toward **not overengineering anti-cheat**. RuneLite plugins can't reliably police everything players can do across accounts anyway. Someone determined to cheat their self-imposed Wheelbound run can find ways to cheat it.

We make the intended rule crystal clear:

> **Sacrificed items must be permanently surrendered. Arranging for another player to return a sacrificed item violates the Wheelbound rules.**

Then the plugin verifies everything it reasonably can.

I think that gives us the cool physical **"drop your offering to Fate"** moment without building the entire mechanic around an event we can't reliably distinguish.

## S8-M0155 - user - 2026-09-30T17:14:01.765000+00:00
Message ID: f5701ea5-cf7d-41f9-b99e-4c2001b27404

anything in the game where we can actually offer an item? because you could drop , soemone else picks it up, and trades it back

## S8-M0156 - assistant - 2026-09-30T17:14:05.049472+00:00
Message ID: 4079729d-6122-4771-8ee0-83b15365c54b

There actually is an almost perfect existing OSRS mechanic for this: **Death's Coffer**.

At Death's Office, players can **Sacrifice** eligible items worth at least 10,000 GP to Death's Coffer. The item is genuinely consumed and **cannot be returned**. In exchange, the player's Death's Coffer balance increases by 105% of the item's GE value; that balance can only be used for things like death-reclamation fees/private boss instances—it cannot be withdrawn as cash. citeturn0search1turn0search4

That solves the friend exploit completely:

```text
Wheelbound Sacrifice
        ↓
Select item / amount
        ↓
Go to Death's Office
        ↓
Sacrifice it to Death's Coffer
        ↓
ITEM IS ACTUALLY GONE
        ↓
Wheelbound detects/validates sacrifice
        ↓
☠ FATE ACCEPTS YOUR OFFERING
        ↓
+ Additional Spins
```

Nobody can pick it up. Nobody can trade it back. The item itself is irreversibly sacrificed. citeturn0search1

And thematically, **sacrificing an item to Death in order to Defy Fate** fits Wheelbound absurdly well.

There are limitations we'd have to design around. Death only accepts eligible items above the minimum value, some items are explicitly excluded, and newly released items may temporarily be blocked until their GE prices stabilize. citeturn0search1

The other permanent-destruction mechanic is an item's **Destroy** option, which instantly removes certain mostly-untradeable items instead of putting them on the ground. But that isn't universally available, so it isn't suitable as our general sacrifice system. citeturn0search5turn0search8

### I think Death's Coffer should be our item-sacrifice altar

It even gives us a potentially useful verification signal: before the sacrifice, Wheelbound knows the selected item/quantity and can watch the inventory change while the player interacts with Death's Coffer. We'd need to research exactly how reliably RuneLite lets us identify the coffer interaction before locking the technical implementation, but conceptually this is far safer than ground-item tracking.

I also **wouldn't care that the player receives Death's Coffer credit**. That's actually a nice tradeoff. They destroyed a valuable item; they get some restricted death-fee utility back, but they can't simply recover their GP.

And this opens up a really cool Defy Fate screen:

> ☠ **FATE DEMANDS AN OFFERING**  
> *Travel to Death's Domain and sacrifice your offering to Death.*

That's much more thematic than "drop a BCP and wait 10 seconds."

**I'd make this our primary item-sacrifice mechanic.** Then we can separately have non-item sacrifices such as permanently banning a shop.

The next question would be **how item value translates into extra spins**—but I'd leave the actual numbers for our balance phase and first decide whether sacrifices should have value *tiers* (Small / Significant / Great / Extreme) rather than a simple `X GP = Y spins` formula.

## S8-M0157 - user - 2026-09-30T17:14:36.367000+00:00
Message ID: da1bd88a-a7ba-4aca-a248-f471ef3c756b

perfect this is what we will use , death requires a sacrafice

## S8-M0158 - assistant - 2026-09-30T17:14:39.021482+00:00
Message ID: ddf7228f-3c08-4957-ac13-90160c1e9662

Yes. **Lock it in.** This fits the theme almost too well.

### CONFIRMED — Death's Sacrifice

Once the player's original Fate Spins are exhausted:

```text
         FATE SPINS: 0
               │
               ▼
          DEFY FATE
               │
               ▼
       ☣ ACCEPT THE TAINT ☣
               │
               ▼
     Wheel becomes corrupted
     + base number of spins
               │
               ▼
       Need MORE spins?
               │
               ▼
      ☠ DEATH DEMANDS
         A SACRIFICE ☠
               │
               ▼
       Visit Death's Office
               │
               ▼
 Sacrifice item(s) to Death's Coffer
               │
               ▼
     Item permanently consumed
               │
               ▼
       FATE ACCEPTS OFFERING
               │
               ▼
        + Additional Spins
```

I especially like that **Taint and Death serve different purposes**.

**Taint** is the consequence for refusing to accept that your allotted Fate Spins are over. You get an initial batch of extra spins, but your Master Wheel becomes corrupted.

**Death** is what you turn to when even that isn't enough. You want additional chances? You have to actually give something up.

That creates a great thematic escalation:

> **Fate gave you a path.**  
> **You defied it.**  
> **Now Death demands payment.**

And because we're using an actual irreversible OSRS sacrifice mechanic rather than pretending a dropped item was destroyed, the game itself backs up the consequence.

We'll leave the conversion of **sacrifice value → spins** as `BALANCE_TBD`; that's exactly the kind of thing we'll simulate later.

I also think we should preserve the other sacrifice ideas like **giving up shop access** for later consideration, but they shouldn't be mixed into *Death's Sacrifice*. Death specifically wants **items**. That keeps the mechanic extremely clear.

So our next design decision should be the fun part: **what exactly happens to the Master Fate Wheel the first time you accept Taint?** That's where we can define how infected slices and punishment Fates actually work. memcite

## S8-M0159 - user - 2026-09-30T17:14:59.441000+00:00
Message ID: 259eb2c6-cdba-4c53-8139-fbf3d137cd08

whats next

## S8-M0160 - assistant - 2026-09-30T17:15:00.628640+00:00
Message ID: 4483e0a2-a4ab-4161-b686-7cb081543f9a

Next I'd stay on **Taint**, because we have the reason it exists but haven't defined what it actually does to your wheel.

The **one decision I'd make next** is:

### How does a wheel become Tainted?

I recommend that the **first time you Defy Fate, Fate corrupts one of your existing slices at random**.

So say your Master Wheel is:

```text
MASTER WHEEL — 9/24

Mining
Fishing
Woodcutting
Combat
Questing
Slayer
Prayer
Crafting
Agility
```

You hit `0` spins and choose **Defy Fate**.

The wheel gets the purple/chained visual transformation, then we visibly randomize which slice becomes infected:

```text
☣ FATE HAS BEEN DEFIED ☣

Mining
Fishing
Woodcutting
Combat
Questing
SLAYER       ← ☣ TAINTED
Prayer
Crafting
Agility
```

Importantly, **Slayer doesn't disappear**. That slice is now dangerous.

Whenever the wheel lands on that particular Slayer slice, instead of receiving a normal Slayer Fate:

```text
SLAYER ☣
   ↓
TAINTED FATE
   ↓
Punishment challenge
```

And because duplicates are independent slices, if you had two Slayer slices, **only the specific infected one is corrupted**.

That works beautifully with our existing slice architecture.

Then visually the infection can live *on the actual slice*: purple corruption, chains/cracks/tendrils, whatever we settle on during the art phase.

My recommendation is also that **additional Defy Fate uses progressively infect additional slices**:

```text
Never Defied       0 infected slices
First Defiance     1 infected slice
Second Defiance    2 infected slices
Third Defiance     3 infected slices
...
```

Not necessarily those exact numbers—we'll balance it—but conceptually **the longer you refuse Fate, the more dangerous your wheel becomes.**

And cleansing later removes an infection from a slice.

The key decision right now is simply:

**Does Defy Fate randomly corrupt an existing slice like this, rather than adding a new punishment slice?**

I strongly prefer corrupting what the player already built. It makes the word **"tainted"** actually mean something. memcite

## S8-M0161 - user - 2026-09-30T17:15:48.784000+00:00
Message ID: 09af9706-63db-49e3-8a10-3b04003ce3b0

yep im down for that&#x20;

Not necessarily those exact numbers—we'll balance it—but conceptually **the longer you refuse Fate, the more dangerous your wheel becomes.**

## S8-M0162 - assistant - 2026-09-30T17:15:50.522424+00:00
Message ID: 8b726462-a3a6-488a-9c3b-b80407fcd2e8

Perfect. **Locked.**

### CONFIRMED — Taint spreads through existing slices

Defying Fate does **not** add punishment slices or consume additional slots. It corrupts slices already present on the player's Master Fate Wheel.

Each infected slice remains visually identifiable as what it originally was, but landing on that **specific slice** triggers a Tainted Fate instead of its normal outcome.

And we'll keep the escalation principle without locking numbers yet:

> **The more the player continues to Defy Fate, the more corrupted and dangerous their Master Wheel becomes.**

The exact relationship between number of Defiances, number of newly infected slices, base spins awarded, and cleansing costs will be determined during balance/simulation.

There is one thing I think we should decide **before designing individual punishments**:

### What happens to the original slice when it's cleansed?

I recommend cleansing **restores that exact slice to normal** rather than removing it.

```text id="y2mxkx"
SLAYER
1.5x upgraded slice
      ↓
☣ CORRUPTED
      ↓
Tainted Slayer slice
(still remembers it's Slayer + 1.5x)
      ↓
CLEANSE
      ↓
SLAYER
1.5x upgraded slice
```

That way Taint is an infection layered on top of the player's wheel build. It doesn't destroy the investment they made in that slice.

If you're good with that, we lock it and then move to the much more interesting question: **what does landing on a Tainted slice actually make you do?** memcite

## S8-M0163 - user - 2026-09-30T17:16:25.693000+00:00
Message ID: 08c32668-51e3-4687-8aa7-6fc40229239f

just removes the taint so its not a awful task

## S8-M0164 - assistant - 2026-09-30T17:16:26.536347+00:00
Message ID: bd68ccf1-9b01-4870-b844-d3f9eacf2675

Exactly. **Cleansing is simple: it removes the Taint status from that slice. Nothing else changes.**

```text
Slayer Slice
1.5x weight
     ↓
☣ TAINTED
1.5x weight
     ↓
CLEANSE
     ↓
Slayer Slice
1.5x weight
```

Its skill/category, upgrades, weight, position, etc. are untouched. It simply stops producing an awful Tainted Fate when landed on.

**CONFIRMED.**

So now we get to define the fun part: **what actually happens when you land on a Tainted slice?**

My starting recommendation is that the original slice **still influences the punishment**. For example, a Tainted Agility slice could produce an awful Agility-related task, while a Tainted Mining slice produces an awful Mining-related task.

But alternatively, Tainted slices could pull from **one universal punishment pool** regardless of what the original slice was.

That's the next decision I'd make. memcite

## S8-M0165 - user - 2026-09-30T17:17:58.091000+00:00
Message ID: 604f911b-4d85-4d59-ab23-e14603c93ad1

tained spaces will give really long and bad tasks. think high exp outside our bracket system, 100 runite rocks with a bronze pick. 5000 golbin kc etc

## S8-M0166 - assistant - 2026-09-30T17:17:59.197174+00:00
Message ID: b895bfe1-1303-4934-baa3-c4630c867aa1

Yes. That changes my previous assumption: **the corrupted slice does not need to determine the punishment.** Landing on *any* Tainted slice sends you into the **Tainted Fate pool**, which is deliberately brutal.

### CONFIRMED — Tainted Fates

These are outside the normal Fate balancing system. They are supposed to make you think:

> “I really don't want to land on purple.”

Normal Fate might be:

> **Mining — Gain 35,000 XP**

Tainted Fate could be:

> ☣ **Mine 100 Runite Rocks using a Bronze Pickaxe**

Or:

> ☣ **Kill 5,000 Goblins**

Or something like:

> ☣ **Gain 250,000 Agility XP**

> ☣ **Complete 100 laps of a low-level Agility course**

> ☣ **Process an enormous amount of Mastering Mixology**

The important distinction is:

```text id="qde0ae"
NORMAL FATE
Scaled to level
Reasonable duration
Normal XP brackets
Designed for progression
        │
        │
        ▼
      Reward


☣ TAINTED FATE ☣
Ignores normal XP brackets
Potentially very long
Intentionally inefficient
May impose equipment/method restrictions
May involve absurd quantities
Designed as PUNISHMENT
        │
        │
        ▼
"I should probably cleanse
 this wheel..."
```

And I think **variety is critical**. If every punishment is just "gain 300k XP," Taint becomes boring rather than scary.

We can eventually have punishment archetypes like **Endurance** (5,000 goblins), **Deprivation** (100 runite rocks with bronze pick), **Grind** (huge XP/task quantity), **Tedious Content** (Mastering Mixology), and combinations of those.

I would *not* make them impossible or RNG-dependent like "get a 1/5,000 drop." A punishment can be horrible, but the player should always be able to see a deterministic path to finishing it.

Also, I think completing one should still give **some FP**. Not enough to make people *want* Tainted Fates, but completing a giant punishment and receiving literally nothing would feel wrong. We can balance that later.

### Next decision

There's a pretty consequential question now:

**If you land on a Tainted slice and receive something horrible, can you refuse it?**

My recommendation: **No.**

Once the wheel lands there:

> ☣ **TAINTED FATE SEALED**

You're doing the punishment before you get another Master Wheel spin.

Otherwise the threat of infecting the wheel isn't really meaningful. memcite

## S8-M0167 - user - 2026-09-30T17:20:50.848000+00:00
Message ID: 154848d0-52bd-4500-82c4-c06a22dbab08

maybe this is a separte wheel too, you land on tainted space it it brings you to the punishment wheel, so remove the idea of it taints an existing slice we will  instead add a slice and have it be tainted. if the wheel is full it will remove a slice at random and add it. 

and animatio should appear when we land on the tained slice that will disapear the old wheel and bring forth the tained wheel and then we have like (50 tasks , 12 or something of which will be presneted so its not the same stuff all the time)

## S8-M0168 - assistant - 2026-09-30T17:20:51.995242+00:00
Message ID: 3cc4e88d-9b21-4b5a-bc0f-cd9e8248fef5

Yes — **I like this version more than corrupting an existing slice.** It makes Taint a distinct threat on the Master Wheel and gives us an excuse for a second, dramatically different wheel without violating the "one normal gameplay wheel" idea.

### CONFIRMED — Tainted Slice + Punishment Wheel

When you **Defy Fate**, Wheelbound adds a dedicated **Tainted Slice** to your Master Fate Wheel.

```text
MASTER FATE WHEEL — 11/24

Mining
Fishing
Combat
Questing
Slayer
Prayer
Crafting
...
☣ TAINTED ☣   ← added by Defying Fate
```

If there's an open slot, Taint occupies that slot.

If you're already at **24/24**, Fate randomly destroys/removes one of your existing active slices and replaces it:

```text
24/24
   ↓
DEFY FATE
   ↓
Random active slice selected
   ↓
☠ SLAYER HAS BEEN CONSUMED BY TAINT
   ↓
Slayer removed
☣ Tainted Slice added
```

I'd preserve the removed slice as **unlocked**, though. You're losing it from your *active wheel*, not losing your permanent Slayer unlock/progression-tree purchase. You could eventually add Slayer back through the normal Wheel Builder rules once you have room.

### Landing on Taint

This is where the second wheel makes sense because it isn't another layer of normal Fate selection. It's effectively a punishment event.

```text
MASTER WHEEL
     ↓
 ☣ TAINTED ☣
     ↓
Master Wheel begins corrupting
     ↓
purple chains / infection / distortion
     ↓
Master Wheel fades / gets pulled away
     ↓
       ☠
 PUNISHMENT WHEEL
       ☠
     ↓
SPIN
     ↓
TAINTED FATE SEALED
```

During our eventual art phase, we can make that transition **nasty**. The normal Master Wheel disappearing and this corrupted thing coming forward should make landing on Taint feel like an event.

### And I really like your `~50 → random 12` idea

We maintain a large **Tainted Fate library**, maybe around 50 at launch.

But every time the Punishment Wheel appears, it randomly draws something like **12 eligible punishments** from that library and physically puts those twelve on the wheel.

```text
TAINTED FATE LIBRARY
~50 punishments
        ↓
Filter impossible/ineligible tasks
        ↓
Randomly select 12
        ↓
☣ PUNISHMENT WHEEL ☣

  Runite w/ bronze pick
  5,000 Goblins
  Huge Agility grind
  Mastering Mixology
  ...
  8 more
        ↓
SPIN
```

That solves several things at once.

Players can't look at the Tainted slice and know exactly what punishment they're risking. The punishment wheel won't look identical every time. We can keep adding new Tainted Fates to the library later without changing the core system. And some punishments can have eligibility requirements without causing hardlocks—we simply don't put them among the 12 if the account can't actually perform them.

I'd also make the **12 chosen before the animation begins and immutable**. No closing/reopening the UI to reroll the punishment pool.

One important correction to our previous cleansing idea: **Cleansing now removes a Tainted Slice entirely**, because Taint is its own dedicated slice rather than a status applied to another slice.

That means if you had:

`18/24 → Defy Fate → 19/24 including Taint → Cleanse → 18/24`

the slot becomes empty again.

If Taint replaced something at `24/24`, cleansing leaves you at `23/24`; it does **not** magically restore the randomly consumed slice. I think that's important because otherwise having a full wheel would remove much of the consequence of Fate eating one of your slices.

### One decision next

We now need to decide what happens on the **second Defy Fate**.

My preference is very straightforward:

**Another Tainted Slice gets added.**

So somebody who keeps pushing:

```text
First Defiance    ☣
Second Defiance   ☣ ☣
Third Defiance    ☣ ☣ ☣
Fourth Defiance   ☣ ☣ ☣ ☣
```

Each is an independent physical slice on the Master Wheel, so your probability of being dragged into the Punishment Wheel keeps climbing.

That's a beautifully simple escalation system—and then the balance pass determines how many spins each Defiance grants versus how quickly that risk becomes terrifying. memcite

## S8-M0169 - user - 2026-09-30T17:22:07.209000+00:00
Message ID: 2590ced4-9857-4dc6-8e5e-dae68c4954f5

ill need a list of 50 awful tasks , youll have to take into account the users level etc so that we can deliver something actual so these punishmetns will need to be alittle dyanmic . thoughts ?

## S8-M0170 - assistant - 2026-09-30T17:22:08.882607+00:00
Message ID: 42dc1ef8-98f4-4c52-a140-2695190111da

Yes. I think **dynamic punishments are necessary**. A static list of 50 tasks will eventually produce nonsense: "gain 200k Agility XP" is horrific for one account and trivial for another, while "mine 100 runite with bronze" may be impossible because bronze can't mine runite. So the library should really be **~50 punishment templates**, not 50 hard-coded tasks.

For example:

```text
TAINTED TEMPLATE
"Primitive Tools"

Skill: Mining
Requirement: Mining level X+
Normal method: appropriate for player's level

Punishment generation:
→ Pick an eligible resource
→ Force a deliberately awful but VALID pickaxe
→ Generate quantity based on resource + player's level

Possible result:
☣ Mine 750 coal using a Bronze Pickaxe
```

The generator must validate the final task before it ever gets onto the 12-slice Punishment Wheel.

I think each template should contain something roughly like:

```text
TAINTED FATE TEMPLATE

Name
Category
Eligibility requirements
Level brackets
Quantity / XP formula
Allowed locations
Required equipment
Forbidden equipment
Completion condition
Estimated severity
Fallback generation
```

That lets us make punishments nasty **relative to the account**.

A level 45 player and level 99 player shouldn't necessarily receive the same punishment from the same template:

```text
☣ ENDLESS LAPS

Agility 40:
Complete 100 Canifis laps

Agility 70:
Complete 150 Seers' Village laps

Agility 92:
Complete 200 Hallowed Sepulchre floors/laps
```

Those numbers are just examples—we'll balance them properly later.

### But I want one important rule

**Dynamic should mean "appropriately horrible," not "scale infinitely with level."**

If every punishment simply becomes:

`base amount × player's level`

then getting stronger never helps you. That's unsatisfying.

Some punishments should actually become easier because you've developed the account. Others should unlock nastier variants at higher levels. Others should have relatively fixed objectives.

That gives the library texture.

We could have templates such as **Primitive Tools**, **Mass Extermination** (thousands of weak monsters), **Endurance Training** (very large XP target), **Tedious Production**, **Old Ways** (use deliberately outdated training method), **Barely Equipped** (combat with equipment restrictions), **Monotony** (repeat one action hundreds/thousands of times), **Potion Duty/Mastering Mixology**, **Long Haul** (large resource collection), etc.

Then each template can have several variants. That's how **50 templates can actually represent hundreds of possible punishments.**

### Eligibility is critical

Before displaying the 12 Punishment Wheel slices:

```text
~50 Tainted Templates
        ↓
Check account state
        ↓
Can player actually do it?
        ↓
Generate concrete objective
        ↓
Validate objective
        ↓
Estimate severity
        ↓
Choose 12 diverse punishments
        ↓
LOCK THE 12
        ↓
☣ SPIN ☣
```

If the player doesn't have access to Mastering Mixology, that template simply isn't eligible.

If a task requires 85 Mining and they're 62, it doesn't appear.

If a required tool literally cannot perform the requested action, generation fails and another variant/template is chosen.

And I'd make sure the 12 aren't stupidly repetitive. We don't want:

> Gain huge Mining XP  
> Mine huge amount of coal  
> Mine huge amount of iron  
> Mine huge amount of mithril  
> Mine with bad pickaxe...

all appearing simultaneously.

The selector should deliberately pull from different **punishment archetypes**.

### One thing I would avoid

I don't want Tainted Fate to simply mean **"normal Fate × 10."**

Some absolutely can be enormous grinds:

> ☣ Kill 5,000 Goblins.

But the memorable ones will be bizarre restrictions:

> ☣ Kill X enemies using only bronze equipment.

> ☣ Catch X fish using an intentionally terrible available method.

> ☣ Complete X laps of an obsolete Agility course.

> ☣ Mine X ore with the worst pickaxe capable of mining it.

Those are the ones players are going to remember and complain about to their friends—which is exactly what we want from a punishment wheel.

And yes, **I can take responsibility for designing the full ~50-template library and the scaling formulas** when we get to that pass. We'll then run the numbers against different fake account states—early, mid, late, maxed—to make sure each generated result lands in the "oh god..." range without becoming literally unreasonable.

For now, I'd lock:

> **Tainted Fates are a library of approximately 50 dynamic punishment templates. When triggered, Wheelbound filters them against the player's actual account state, generates valid concrete tasks, selects ~12 varied eligible punishments, locks those 12 onto the Punishment Wheel, and spins.**

Then the next question is whether **Tainted Fates should give any reward when completed**. My instinct is actually **yes, but not FP**—because rewarding lots of FP could make intentionally seeking Taint optimal. I think completing one should do something smaller/thematic instead. memcite

## S8-M0171 - user - 2026-09-30T17:23:10.453000+00:00
Message ID: e2147017-c9d8-4e4b-a51e-d266296ee076

perfect send it

## S8-M0172 - assistant - 2026-09-30T17:23:12.073989+00:00
Message ID: 2ed538ef-45c6-42dd-b551-c7519cedd52d

Locked in. **Tainted Fate punishment system is confirmed**:

- Defying Fate adds a dedicated **Tainted Slice** to the Master Wheel; further Defiances can add more.
- At 24/24, a random active slice is removed to make room.
- Landing on Taint transitions away from the Master Wheel into the special **Punishment Wheel**.
- We maintain roughly **50 dynamic punishment templates**, not 50 static tasks.
- Templates scale/adjust using the player's levels, unlocks, access, equipment possibilities, and account state.
- Every generated punishment must be **actually possible**.
- Punishments intentionally sit well outside normal Fate brackets and can involve huge grinds, terrible methods/tools, equipment restrictions, repetitive content, etc.
- The system selects roughly **12 varied eligible punishments** from the larger library for each Punishment Wheel.
- Those 12 are locked before the wheel appears; reopening cannot reroll them.
- We avoid filling a wheel with twelve near-identical punishments.
- Exact quantities/scaling will be handled in the dedicated balance + simulation pass.
- **Death's Coffer** is our item-sacrifice mechanism for purchasing further spins after Defying Fate.
- Cleansing a Tainted Slice removes that slice and frees the slot.
- Full UI/art treatment—including the corruption transition and Punishment Wheel—is deferred to our dedicated UI/art phase.

We'll come back and build the actual ~50-template catalog once the surrounding systems are finished. memcite

## S8-M0173 - user - 2026-09-30T17:23:25.755000+00:00
Message ID: 96cadb49-c8cf-41b9-8626-a4b22b285501

whats next

## S8-M0174 - assistant - 2026-09-30T17:23:27.499389+00:00
Message ID: b0d86f96-1c0b-4fe0-bf7a-f76069c55bf4

I think we've gone far enough into **Defy Fate/Taint** for now. We know the loop; exact spin counts, sacrifice values, cleansing costs, and 50 punishments belong in later balance/content passes.

I'd return to the **four Progression Tree branches**. We've substantially covered **Skilling** and most of the basic **Questing** structure. That leaves **Combat/Bossing** and **Fate Manipulation**. memcite

I'd do **Combat/Bossing next**, because it affects what the player can actually do during the run.

The first decision is what the **Combat slice** means.

We already established that the starting Master Wheel has a single Combat slice representing:

> Attack / Strength / Defence / Hitpoints / Ranged / Magic

But we haven't established what happens when it lands on Combat.

My recommendation is **not** to generate something restrictive like:

> Gain 25,000 Defence XP.

Instead, Combat should probably generate broader combat objectives because forcing individual combat styles could interfere heavily with normal account progression.

For example:

```text
MASTER WHEEL
     ↓
   COMBAT
     ↓
COMBAT FATE

"Gain 50,000 Combat XP"

Allowed:
✓ Attack
✓ Strength
✓ Defence
✓ Hitpoints
✓ Ranged
✓ Magic

Any combination counts toward the objective.
```

That means Combat Fate is basically:

> **Go make your account stronger however you want for X amount of combat XP.**

Then the **Combat/Bossing progression branch** can eventually start unlocking actual boss-oriented content and more interesting Combat Fates.

So that's the next single decision I'd make:

**When Combat is spun, should its normal Fate be combined Combat XP across all six combat skills, with the player free to choose how they train?**

I lean strongly **yes**, because I think separate Attack/Strength/etc. Fates would become annoyingly restrictive very quickly.

## S8-M0175 - user - 2026-10-01T16:18:45.986000+00:00
Message ID: 49689338-9535-446d-9864-a55feaed620f

No for combat it should just allow them to choose whatever (strength, defense , attack, hitpoints, ranged and magic) all bundled into 1

## S8-M0176 - assistant - 2026-10-01T16:18:47.312283+00:00
Message ID: c2184ffc-54e6-4f35-b239-a84cc9c6e4de

Right — so Combat isn't an XP objective across all six. **Landing on Combat means the player chooses which combat skill becomes the Fate.**

I'd define it like this:

```text
MASTER FATE WHEEL
        ↓
      COMBAT
        ↓
┌─────────────────────────────┐
│       CHOOSE YOUR PATH      │
│                             │
│  Attack      Strength       │
│  Defence     Hitpoints      │
│  Ranged      Magic          │
└─────────────────────────────┘
        ↓
Player chooses Strength
        ↓
STRENGTH FATE
Gain X Strength XP
```

So those six skills are permanently **bundled into one Master Wheel slot**, rather than requiring six of the 24 slots.

That also means Combat is fundamentally different from Questing:

**Combat:** Fate chooses Combat → **you freely choose one of the six combat skills** → normal XP Fate generated for that skill.

**Questing:** Fate chooses Questing → **Fate randomly offers 3 eligible quests** → you choose from those 3, eventually upgradeable to 4 and 5.

I like that distinction. Combat gives full choice because combat skills are so intertwined; Questing gives constrained choice because quest selection itself is part of the challenge.

### CONFIRMED — Combat Slice

One slice represents:

**Attack · Strength · Defence · Hitpoints · Ranged · Magic**

The player chooses which one every time Combat lands. Then that selected skill uses our normal level/bracket XP Fate-generation system.

One small edge case we'll eventually need to solve: **Hitpoints can't really be deliberately trained independently in the same way as the other five.** We don't need to derail into it now; we can handle Hitpoints specially during the detailed Combat rules.

With that settled, the next thing I'd tackle is **how Bossing enters the Master Wheel**, because I don't think bosses should simply be bundled into this normal Combat slice. memcite

## S8-M0177 - user - 2026-10-01T16:20:59.966000+00:00
Message ID: 0108b677-8ccd-4786-85b3-5409b4e144b8

yes lets have those fate cards come up and thern above them would be xp, if it lands on combat it should just be a simple task like that, likewise when slayer comes into play you will also be gaining combat experince alongside and this should be okay and not a part of the tollerance because its a bi-product. It might be best to group these code wise but you can do what you think is best there

## S8-M0178 - assistant - 2026-10-01T16:21:01.350500+00:00
Message ID: 844427dc-ba81-4b9c-a22c-a65d5fe631c1

Yes. That gives us a much cleaner rule for **primary XP vs unavoidable/by-product XP**.

### CONFIRMED — Combat Fate

When Combat lands, the six Fate cards appear:

```text id="7emwqd"
               COMBAT FATE

          Gain 40,000 XP
        ───────────────────

┌────────┐ ┌────────┐ ┌────────┐
│ ATTACK │ │STRENGTH│ │DEFENCE │
└────────┘ └────────┘ └────────┘

┌────────┐ ┌────────┐ ┌────────┐
│HITPOINT│ │ RANGED │ │ MAGIC  │
└────────┘ └────────┘ └────────┘
```

The XP amount is generated using our normal Fate scaling system. The player chooses a card, and that becomes the primary objective.

So if they choose Strength:

> **Fate: Gain 40,000 Strength XP**

We can work out the exact presentation during the UI phase.

### By-product combat XP

I agree strongly with your Slayer example. Our `1,000 unauthorized XP` tolerance should be about **actually going off-Fate**, not XP the game naturally forces you to receive while performing the Fate.

If your Fate is:

> **Gain X Slayer XP**

then Attack, Strength, Defence, Hitpoints, Ranged and Magic XP earned **while legitimately performing that Slayer Fate** should be permitted and **not consume the tolerance bucket**.

Otherwise we'd essentially punish players for playing Slayer normally.

Same concept can apply elsewhere where OSRS inherently couples XP.

Code-wise, I wouldn't hard-code a bunch of exceptions like:

```text id="jhfwn7"
if Slayer:
    ignore Attack
    ignore Strength
    ignore Defence...
```

I'd rather eventually give each Fate an **XP permission profile**:

```text id="3j3f99"
Slayer Fate

PRIMARY
✓ Slayer

PERMITTED BY-PRODUCT
✓ Attack
✓ Strength
✓ Defence
✓ Hitpoints
✓ Ranged
✓ Magic

UNAUTHORIZED
✗ Mining
✗ Fishing
✗ Cooking
...
```

Then Combat Fates can use the same system. For example, choosing Strength makes Strength the primary tracked objective while Hitpoints is permitted by-product XP.

That architecture will also help when we hit weird OSRS activities later. We can define what XP is legitimately associated with a Fate rather than continually punching holes in the tolerance system.

**Important principle locked:**  
> XP tolerance is for **unrelated/off-Fate activity**. XP that is an inherent or legitimate by-product of completing the active Fate can be explicitly permitted and does not consume tolerance.

We can leave the precise permission matrix to the detailed rules/implementation pass.

With normal Combat now defined, I think we're ready for **Bossing**: specifically, whether unlocking Bossing adds **one Bossing slice** to the Master Wheel (like Questing) and then determines the boss afterward, or whether bosses/boss groups become individual wheel slices. That's the next decision I'd tackle. memcite

## S8-M0179 - user - 2026-10-01T16:21:41.098000+00:00
Message ID: dbdd36ac-8f05-4e4f-b41c-15d72d0e67cd

1 more think remove hitpoints ( regraudless of what your training i think you earn hitpoints

## S8-M0180 - assistant - 2026-10-01T16:21:41.605499+00:00
Message ID: 7c001cbd-f8bb-47be-bb8e-ce7ecc6e3afd

Yes — agreed. **Remove Hitpoints as a selectable Combat Fate entirely.**

Hitpoints is better treated as a **by-product combat skill**, because normal training of Attack, Strength, Defence, Ranged, and Magic generally produces Hitpoints XP anyway.

So Combat becomes:

```text
              COMBAT FATE

           Gain 40,000 XP
        ──────────────────

     ┌──────────┐ ┌──────────┐
     │  ATTACK  │ │ STRENGTH │
     └──────────┘ └──────────┘

     ┌──────────┐ ┌──────────┐
     │ DEFENCE  │ │  RANGED  │
     └──────────┘ └──────────┘

          ┌──────────┐
          │  MAGIC   │
          └──────────┘
```

Player picks **one of five**:

**Attack · Strength · Defence · Ranged · Magic**

Hitpoints XP is permitted during Combat Fates and doesn't count against unauthorized-XP tolerance.

Likewise, during something like a Slayer Fate:

> **Primary:** Slayer XP  
> **Permitted by-product:** Attack, Strength, Defence, Ranged, Magic, Hitpoints as appropriate.

This actually makes Hitpoints somewhat unique in Wheelbound: **it doesn't need its own Fate because it naturally grows as the account follows combat-related Fates.**

**CONFIRMED: Combat bundle = 5 selectable skills. Hitpoints = by-product only.** memcite

## S8-M0181 - user - 2026-10-01T16:22:04.911000+00:00
Message ID: 7cb4f82a-1146-418d-a492-68f72018e8be

whats next

## S8-M0182 - assistant - 2026-10-01T16:22:05.871121+00:00
Message ID: afd12cb6-5224-4c6d-b2da-8de1d94c93a7

Next is **Bossing**, specifically how it gets represented on the Master Fate Wheel.

We previously talked about boss tiers in the Progression Tree, but now that we've nailed down the single-wheel architecture, we need to decide what unlocking those tiers actually does.

My recommendation is **one Bossing slice**, similar to Questing:

```text
MASTER FATE WHEEL

Mining
Fishing
Combat
Questing
Slayer
Prayer
...
Bossing   ← one slice
```

Then the Combat/Bossing progression branch unlocks increasingly difficult bosses into the **Bossing pool**.

The key question is what happens when that slice lands.

I see two good approaches:

**A — Questing-style choices**

```text
BOSSING lands
      ↓
Fate selects 3 eligible bosses
      ↓
┌─────────┐ ┌─────────┐ ┌─────────┐
│ Vorkath │ │  Zulrah │ │   CG    │
└─────────┘ └─────────┘ └─────────┘
      ↓
Player chooses one
      ↓
"Kill Vorkath X times"
```

**B — Fate chooses one boss**

```text
BOSSING lands
      ↓
Fate selects an eligible boss
      ↓
VORKATH
      ↓
"Kill Vorkath X times"
```

I lean toward **B for Bossing**.

Questing deserves choices because a quest can be a long, one-time commitment and we specifically need the player to navigate Grand Fate prerequisites. Bossing is repeatable, so having Fate simply say:

> **You're killing Vorkath. 15 KC.**

feels more like Wheelbound.

It also makes unlocking higher Boss tiers genuinely scary because you're voluntarily adding harder bosses to the pool.

So that's our next single decision:

**When the Bossing slice lands, does Fate directly choose one eligible unlocked boss, or should the player get multiple boss cards to choose from?**

## S8-M0183 - user - 2026-10-01T16:34:52.464000+00:00
Message ID: 9150c5a3-a213-4312-af80-c7b830baec4d

yes confirmed lock that in
My recommendation is **one Bossing slice**, similar to Questing

However, id like to tweak this and say that simialr to the animation that would happen to introduce the punishment wheel, i want something to introduce the bossing wheel.&#x20;

A fade out of the main wheel and then a new bossing wheel appear for the user to spin. A random number of how many times you kill that boss will appear at the top so thats already pre determined.&#x20;

## S8-M0184 - assistant - 2026-10-01T16:34:53.490982+00:00
Message ID: c7b96e89-a0d2-47e5-8ab3-eeefdc22d5cb

Yes — **locked.** And I like this more than having the plugin silently select a boss. It keeps the wheel itself as the central identity of Wheelbound.

### CONFIRMED — Bossing Slice → Bossing Wheel

The Master Fate Wheel contains **one Bossing slice**. Bosses themselves do not consume Master Wheel slots.

When Bossing lands:

```text id="cgn5v3"
              MASTER FATE WHEEL
                       ↓
                   BOSSING
                       ↓
              Wheel stops spinning
                       ↓
           Bossing transition begins
        Main wheel fades / disappears
                       ↓
          BOSSING WHEEL ENTERS
                       ↓
          Kill Count is generated
             BEFORE the boss spin

                KILL 17 TIMES
                     ↓
          ╭──────────────────╮
          │    BOSS WHEEL    │
          │                  │
          │ Vorkath   Zulrah │
          │   CG      Muspah │
          │  ...       ...   │
          ╰──────────────────╯
                     ↓
                 [ SPIN ]
                     ↓
                   ZULRAH
                     ↓
              FATE CARD REVEAL

               ☠ ZULRAH ☠
                17 KILLS
```

The **KC being generated first and displayed above the wheel** is a nice detail. The player knows the size of the punishment/commitment before discovering which boss Fate is attaching it to.

### Bossing progression controls what's on that wheel

This also makes our Combat/Bossing progression branch make more sense.

You're not purchasing individual Master Wheel slices. You're expanding the **Bossing Wheel pool**:

```text id="nuejfj"
BOSSING PROGRESSION

Early Boss Access
       ↓
More Bosses
       ↓
Mid-Level Bosses
       ↓
More Bosses
       ↓
Late-Game Bosses
       ↓
Endgame Bosses
```

Only **unlocked + actually accessible bosses** should appear on the Bossing Wheel. We can work out exactly what "accessible" means when we build the boss groups.

And visually, I don't want this wheel to look like the Tainted Wheel.

Later during our art/UI phase we should establish three distinct identities:

**Master Fate Wheel** — the core Wheelbound gold/fate aesthetic.

**Bossing Wheel** — combat-oriented, potentially steel/stone/blood/boss iconography. It should feel like *"Fate demands a fight."*

**Tainted Punishment Wheel** — corrupted purple, chains/infection, intentionally ominous.

Then the **Grand Fate Wheel** remains the special red/black/skull/spikes/chains wheel that only determines the final destiny at the beginning of the run.

We're starting to get a really strong family of wheel transitions without turning the normal gameplay into nested menus.

### One thing I'd change from what I suggested previously

Since we're now **physically spinning a Bossing Wheel**, I would **not present three boss choices** afterward.

The boss wheel itself makes the decision.

> `Bossing → KC generated → Boss Wheel → spin → boss selected → Fate sealed.`

No choice. That's much more Wheelbound.

**CONFIRMED.**

The next decision should be **how we organize bosses into progression tiers**, because those unlocks determine what gets added to this Bossing Wheel. memcite

## S8-M0185 - user - 2026-10-01T16:36:23.531000+00:00
Message ID: d9f49643-eda9-4233-a932-1d5bcc82fc23

no no choices for the boss, your spinning the wheel for this. Questing has choices because i feel like that would be a bigger bottle neck for actually completing your end goal so they can kinda get more lenenacy into that area

## S8-M0186 - assistant - 2026-10-01T16:36:24.642697+00:00
Message ID: f34e41e2-1d0b-41f9-8fa5-18efc3337904

Exactly. We're aligned — **Bossing gets zero choices.** The wheel decides the boss.

The distinction is intentional:

**Questing gets leniency** because quests are permanent, prerequisite-heavy, and can become a major bottleneck to reaching the player's Grand Fate. So landing on Questing gives **3 random eligible quest cards**, eventually upgradeable to 4 and 5, and the player chooses one.

**Bossing gets no leniency.** It's repeatable content, so:

```text id="py76w2"
MASTER WHEEL
     ↓
  BOSSING
     ↓
Generate KC requirement
     ↓
       17 KILLS
          ☠
     ↓
Master Wheel fades
     ↓
BOSSING WHEEL APPEARS
     ↓
      SPIN
     ↓
    VORKATH
     ↓
FATE SEALED

Kill Vorkath 17 times.
```

Whatever boss the pointer lands on **is your Fate**. No cards afterward asking which boss you'd prefer, no reroll, no selection.

That also creates three deliberately different selection mechanics:

```text id="yxmkux"
COMBAT
Fate gives full choice
Attack / Strength / Defence / Ranged / Magic

QUESTING
Fate gives limited choice
3 random quests → eventually 4 → 5

BOSSING
Fate gives NO choice
Spin the Boss Wheel → that's your boss
```

I really like that because the amount of player agency is based on the nature of the content rather than forcing every category to behave identically.

**Locked.**

Next I'd move into the **Bossing progression tiers**—what boss groups exist and how unlocking them expands the Bossing Wheel. memcite

## S8-M0187 - user - 2026-10-01T16:55:43.019000+00:00
Message ID: 71e17cd9-900b-4bee-a4a9-d850aa1cf433

[Non-text attachment; not recovered]
so in the plugin already we have these teirs 

id like to make each of these a node . thoughts ?

## S8-M0188 - assistant - 2026-10-01T16:55:44.334774+00:00
Message ID: 53acd051-6b91-41dd-b88d-0e0c0caee09a

Yes — I think that's a **better structure than inventing a second boss-tier system**. The existing plugin already has a boss classification system, so Wheelbound should build on it.

I'd make each difficulty tier its own **Progression Tree node**:

```text
                 BOSSING
                    │
                  EASY
                    │
                 MEDIUM
                    │
                  HARD
                    │
                  ELITE
                    │
                 MASTER
                    │
              GRANDMASTER
```

But I would treat **Unrated** differently. I don't think it belongs as the final tier after Grandmaster because "Unrated" doesn't mean harder than Grandmaster. We can decide separately how those bosses enter the pool.

When you purchase, say, the **Medium Bosses** node, all bosses classified as Medium become eligible to appear on the **single Bossing Wheel**. It doesn't create another Master Wheel slice.

So your Bossing Wheel grows naturally:

```text
Easy unlocked:
[Easy bosses...]

        ↓ unlock Medium

[Easy bosses + Medium bosses...]

        ↓ unlock Hard

[Easy + Medium + Hard bosses...]
```

And I'd have the progression node's detail card show exactly what you're buying:

> **HARD BOSSES**  
> Unlocks the Hard boss tier for your Bossing Wheel.  
>
> **Adds:**  
> • Boss A  
> • Boss B  
> • Boss C  
> • ...

That way there are no surprises about what purchasing the node can cause Fate to assign.

### One decision before we lock the progression path

I **wouldn't automatically require Easy → Medium → Hard → Elite → Master → Grandmaster** yet.

There's an interesting alternative where we require the player to demonstrate bossing progression to advance—for example:

```text
[EASY]
   │
Complete X Easy Boss Fates
   │
[MEDIUM]
   │
Complete X Medium Boss Fates
   │
[HARD]
```

That would make the Combat/Bossing branch feel like actual mastery rather than simply spending enough FP to buy increasingly dangerous bosses.

So **CONFIRMED:** each existing rated boss difficulty becomes a progression node and unlocking it adds that tier's bosses to the Bossing Wheel.

**Next single decision:** should advancing to the next Boss tier require **completing Boss Fates from the previous tier + FP**, similar to our Questing progression, or simply purchasing the next node once its prerequisite is unlocked? memcite

## S8-M0189 - user - 2026-10-01T16:58:12.128000+00:00
Message ID: 74f55256-e5c7-4df8-a4c4-87d48fed5ab7

so here the thing with this , i feel like the bossing tree is very linear and id like it to be more of an experince which is why i was going to have the bosses be single . BUT this makes adding new bosses very difficult etc because then we need an update to adjust the tree.... what if we did a war thunder thing again and had Tiers of bosses and you had to unlock certain ones but you need x of a teir to unlock the next ?

## S8-M0190 - assistant - 2026-10-01T16:58:14.052647+00:00
Message ID: ae6bc2fe-c9c0-41a6-a8e6-2465f22ca0a4

Yes — I think that solves both problems really well.

Instead of **“Easy” being one node that dumps every Easy boss onto the Boss Wheel**, the difficulty rating becomes a **tier/row**, and the individual bosses are the nodes inside it.

Conceptually, very War Thunder:

```text
                    BOSSING PROGRESSION

──────────────────── TIER I — EASY ────────────────────

   [Boss A]       [Boss B]       [Boss C]       [Boss D]
      │              │              │              │
      └──────────────┴──────┬───────┴──────────────┘
                            │
                      3 / 4 REQUIRED
                            │
                            ▼

─────────────────── TIER II — MEDIUM ──────────────────

 [Boss E]    [Boss F]    [Boss G]    [Boss H]    [Boss I]
     │           │           │           │           │
     └───────────┴───────────┼───────────┴───────────┘
                             │
                       3 / 5 REQUIRED
                             ▼

──────────────────── TIER III — HARD ──────────────────
```

### Why I think this is better

It makes the Bossing branch an actual **experience** rather than:

> Easy → Medium → Hard → Elite → Master → Grandmaster

The player gets decisions. Maybe Tier I contains six bosses and they only need to unlock four to progress. They choose which four they're willing to put onto their Bossing Wheel.

And that's the important consequence:

> **Purchasing an individual boss node adds that boss to your Bossing Wheel.**

So you're building your Boss Wheel as you build your account.

There's also some strategy because unlocking a boss isn't purely beneficial. You're gaining progression toward the next tier, **but you're also permanently introducing another possible Boss Fate**.

### And it handles new OSRS bosses much better

This is the part I especially like.

The **tier itself is data-driven**, rather than its visual layout being hard-coded around exactly six bosses.

If a new Medium boss comes out later, we classify it as Medium and it appears as another node within the Medium cluster/row:

```text
Before:

Medium
○ ○ ○ ○ ○


OSRS adds new Medium boss:

Medium
○ ○ ○ ○ ○ ○
          ↑
         new
```

Crucially, the progression requirement doesn't have to change.

If Medium says:

> **Unlock 4 Medium bosses to access Hard**

and Jagex adds three more Medium bosses two years later, it's still **4 required**. Existing Wheelbound saves aren't suddenly invalidated.

That's much more future-proof.

I would also borrow another part of the War Thunder idea: **you don't have to buy everything to progress, but everything remains available.** So reaching Hard doesn't lock the remaining Medium bosses. You can come back later and add them.

### One thing I would NOT do

I wouldn't require specific bosses like:

> Giant Mole → Barrows → Sarachnis → Medium Tier

unless there's a genuine reason for a particular boss to be a prerequisite.

That starts creating the exact maintenance problem you're trying to avoid.

Instead:

> **Unlock any X bosses from this tier → next tier becomes available.**

Individual boss nodes are peers.

---

I think this is substantially better than the tier-node idea we just discussed, so I'd **replace that design** with:

**CONFIRMED if you're happy:**  
**Boss difficulty = progression tier. Individual bosses = purchasable nodes. Unlock X bosses within a tier to open the next tier. Purchasing a boss adds it to the Bossing Wheel. New bosses can be inserted into their appropriate tier without restructuring progression.**

The **next single question** I'd ask after locking this is whether unlocking a boss node should *only* cost FP, or whether the player should have to prove something to unlock that boss too. I wouldn't decide that yet until you confirm this structure.

## S8-M0191 - user - 2026-10-01T16:58:54.852000+00:00
Message ID: eef25127-9a7d-4d82-ad3f-58903279b203

no no within the tier they are all treated equally

## S8-M0192 - assistant - 2026-10-01T16:58:55.362466+00:00
Message ID: 26c7406f-103c-4272-a0fe-0d0ff23f3b8b

Right — yes. I was adding hierarchy **inside** the tier that you don't want.

Within a tier, **every boss is completely equal**. No boss-to-boss prerequisite paths and no ordering.

So more like:

```text
                 BOSSING PROGRESSION

══════════════════ EASY ══════════════════

   [Boss A]   [Boss B]   [Boss C]   [Boss D]
   [Boss E]   [Boss F]   [Boss G]

               UNLOCK 4 / 7
                     │
                     ▼

═════════════════ MEDIUM ═════════════════

   [Boss H]   [Boss I]   [Boss J]   [Boss K]
   [Boss L]   [Boss M]

               UNLOCK 4 / 6
                     │
                     ▼

══════════════════ HARD ══════════════════
```

If Easy requires 4 bosses, the player can choose **any four**. Boss A has exactly the same progression value and status as Boss G.

Unlocking a boss does two things:

> **Boss node unlocked → boss added to Bossing Wheel → +1 toward that tier's requirement.**

Once the tier requirement reaches X, the **entire next tier becomes available**, and again all bosses within that new tier are peers.

This also means adding a new boss later is trivial: classify it `Easy`, `Medium`, `Hard`, etc., and it joins that tier as another equal node. It doesn't disrupt prerequisite chains or existing saves.

So yes, let's **CONFIRM** that structure:

**Difficulty determines the tier. Bosses within a tier are completely equal. Unlock any X bosses in the tier to progress to the next tier.** memcite

## S8-M0193 - user - 2026-10-01T16:59:59.776000+00:00
Message ID: 6063ad69-4025-4486-af95-9d25f561b0ad

can you look at the plugin today and get an idea for how many bosses are in each tier?

## S8-M0194 - assistant - 2026-10-01T17:00:25.343111+00:00
Message ID: ea07ecd6-14c8-407d-9e2e-6fcfd8c6feae

Yep. I checked the **current code on the `wheelbound-mode` branch**, specifically `BossDifficulty.java`, so these are the tiers the plugin is actually using today—not numbers I'm pulling from the Wiki. urlWheelbound repositoryhttps://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound

The current rated distribution is:

| Tier | Boss entries |
|---|---:|
| **Easy** | **9** |
| **Medium** | **13** |
| **Hard** | **22** |
| **Elite** | **14** |
| **Master** | **8** |
| **Grandmaster** | **2** |
| **Total rated** | **68** |

There's also **Unrated**, which is handled differently in the existing code: anything in RuneLite's boss hiscore list that isn't explicitly mapped by `BossDifficulty.of()` falls into `UNRATED`. So I wouldn't use Unrated as a normal progression tier yet.

This distribution actually makes me like your War Thunder approach **more**, but it presents an interesting issue: **Hard has 22 bosses while Grandmaster only has 2.**

So I don't think our rule should be something generic like *"unlock 50% of each tier."* We should set a **specific progression requirement per tier**.

For example—not locking these numbers yet:

```text
EASY          0 / 4 of 9
       ↓
MEDIUM        0 / 5 of 13
       ↓
HARD          0 / 7 of 22
       ↓
ELITE         0 / 5 of 14
       ↓
MASTER        0 / 4 of 8
       ↓
GRANDMASTER   0 / 2 of 2
```

That lets each tier stay internally equal like you wanted, while the progression requirement accounts for the wildly different pool sizes.

And because the requirement is a **fixed number rather than percentage**, adding a new Hard boss later changes `22 → 23` without changing the player's requirement of `7`. That's exactly the future-proofing we're looking for.

**Next decision I'd make:** how many bosses should you have to unlock in each tier to advance? We can tackle **Easy first** rather than deciding all six at once. memcite

## S8-M0195 - user - 2026-10-01T17:02:14.489000+00:00
Message ID: 38ae57d6-80ea-4be1-bb94-e6e8d16b69a6

can you list those out

## S8-M0196 - assistant - 2026-10-01T17:02:15.499269+00:00
Message ID: 353ebf56-073d-46fb-bdbd-483f32c80f8c

Yep — here's the **exact boss mapping currently hard-coded in your plugin's `BossDifficulty.java` on `wheelbound-mode`**. urlWheelbound repositoryhttps://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound

| Tier | Bosses |
|---|---|
| **Easy — 9** | Brutus, Wintertodt, Tempoross, Barrows Chests, Obor, Bryophyta, Giant Mole, Deranged Archaeologist, Scurrius |
| **Medium — 13** | Amoxliatl, The Hueycoatl, Hespori, Crazy Archaeologist, Chaos Fanatic, Kraken, Sarachnis, King Black Dragon, Zalcano, Thermonuclear Smoke Devil, Mimic, The Royal Titans, Lunar Chests / Moons of Peril |
| **Hard — 22** | Scorpia, Chaos Elemental, Vet'ion, Calvar'ion, Venenatis, Spindel, Callisto, Artio, Dagannoth Prime, Dagannoth Rex, Dagannoth Supreme, Grotesque Guardians, Skotizo, TzTok-Jad, The Gauntlet, Abyssal Sire, Cerberus, Araxxor, Alchemical Hydra, Kalphite Queen, K'ril Tsutsaroth, Commander Zilyana |
| **Elite — 14** | Zulrah, Vorkath, Phantom Muspah, The Corrupted Gauntlet, General Graardor, Kree'arra, Duke Sucellus, The Whisperer, The Leviathan, Vardorvis, Nex, Tombs of Amascut, Tombs of Amascut: Expert Mode |
| **Master — 8** | Chambers of Xeric, The Nightmare, Phosani's Nightmare, Yama, Doom of Mokhaiotl, Theatre of Blood, TzKal-Zuk, Sol Heredit |
| **Grandmaster — 2** | Chambers of Xeric: Challenge Mode, Theatre of Blood: Hard Mode |

**Small correction to my previous count:** Elite is actually **13**, not 14. The list itself has 13 entries, meaning the rated total is **67**, not 68.

So the actual distribution is:

**9 → 13 → 22 → 13 → 8 → 2**

That shape is pretty interesting for designing the progression tree, particularly that huge **22-boss Hard tier**. memcite

## S8-M0197 - user - 2026-10-01T17:03:52.683000+00:00
Message ID: 3837c72e-dbde-4db1-84b5-18382b87bf6c

okay so i would like to have raids as their own tier and like this is the end game of this tree is to unlock raids

## S8-M0198 - assistant - 2026-10-01T17:03:54.159277+00:00
Message ID: 55098c36-d95d-41d0-93ce-87afc580a2c2

Yes — I think that's a much stronger endpoint for the Bossing branch.

Instead of **Grandmaster being the end**, the entire Bossing progression culminates in **Raids**.

I'd restructure the conceptual progression to:

```text id="ygkq8s"
                    BOSSING

                     EASY
                  ○ ○ ○ ○ ○
                 ○ ○ ○ ○
                  Unlock X
                      │
                      ▼
                    MEDIUM
                ○ ○ ○ ○ ○ ○
                 ○ ○ ○ ○ ○
                  Unlock X
                      │
                      ▼
                     HARD
              ○ ○ ○ ○ ○ ○ ○ ○
             ○ ○ ○ ○ ○ ○ ○ ○ ○
                  Unlock X
                      │
                      ▼
                    ELITE
                 ○ ○ ○ ○ ○
                  ○ ○ ○ ○
                  Unlock X
                      │
                      ▼
                    MASTER
                  ○ ○ ○ ○ ○
                    Unlock X
                      │
                      ▼
                GRANDMASTER
                    ○ ○ ○
                  Unlock X
                      │
                      ▼
              ╔══════════════╗
              ║    RAIDS     ║
              ╚══════════════╝
```

But importantly, we'd **remove the raids from their current difficulty tiers** and give them their own final tier.

So things like:

**Chambers of Xeric, Chambers Challenge Mode, Theatre of Blood, Theatre Hard Mode, Tombs of Amascut, Tombs Expert** would no longer be scattered through Elite/Master/Grandmaster. They belong under the final **RAIDS** progression section.

That gives the branch a very clear journey:

> Start killing basic bosses → progressively unlock harder bosses → prove enough bossing progression → **unlock Raids**.

I also think **Raids should feel visually different** from another ordinary row of boss nodes. Reaching it should feel like you've reached the capstone of the Combat/Bossing tree.

One thing I would **not** do is automatically dump every raid/mode onto the Bossing Wheel when the player reaches Raids. I think reaching the Raids tier should **unlock access to its individual nodes**, just like the other tiers.

So you reach the end and then choose which raids you're willing to introduce into your Boss Wheel.

That also leaves us room later for:

```text id="2wd23g"
              RAIDS UNLOCKED

        Chambers of Xeric
                ○
        Challenge Mode
                ○

        Tombs of Amascut
                ○
        Expert Mode
                ○

        Theatre of Blood
                ○
        Hard Mode
                ○
```

We could decide later whether the harder raid modes require their normal version first. **That would be one of the rare places where an internal prerequisite actually makes thematic sense.**

So I'd lock the high-level idea:

**CONFIRMED — Raids are removed from the normal Boss difficulty tiers and become the capstone/final tier of the Bossing progression tree. Reaching Raids represents completing the main Bossing progression.**

The next thing I'd settle is **what happens to Grandmaster once the raids are removed**, because right now both Grandmaster entries are raids. We'll need to decide whether Grandmaster still exists as a normal boss tier or whether **Master → Raids** becomes the ending. memcite

## S8-M0199 - user - 2026-10-01T17:04:29.401000+00:00
Message ID: ebdf7eac-2a2d-42ac-b23b-5edff67b04a1

well there would be no grandmaster bosses because the raids would be the grandmaster difficulty

## S8-M0200 - assistant - 2026-10-01T17:04:32.511428+00:00
Message ID: aee3f52c-04c4-45f9-91af-4045ddab39c9

Exactly. Then we don't need a separate Grandmaster boss tier at all.

The Bossing progression becomes:

```text
EASY
  ↓
MEDIUM
  ↓
HARD
  ↓
ELITE
  ↓
MASTER
  ↓
╔══════════════════╗
║ GRANDMASTER      ║
║      RAIDS       ║
╚══════════════════╝
```

So **Grandmaster = Raids**. That's the capstone of the entire Bossing branch.

The current raid entries would be pulled out of their existing tiers:

- Chambers of Xeric + Challenge Mode
- Theatre of Blood + Hard Mode
- Tombs of Amascut + Expert Mode

And we'd redistribute/count the remaining bosses after removing those.

I also like this because the terminology now communicates something meaningful:

> **Easy → Medium → Hard → Elite → Master → Grandmaster: Raids**

Rather than having Grandmaster contain only two hard-mode raids while other raids are scattered through lower tiers.

### CONFIRMED

**Grandmaster is the Raid tier and is the endpoint of Bossing progression. There is no separate non-raid Grandmaster boss tier.**

The next thing I'd do is recalculate our actual tier counts **after removing every raid**, because those are the numbers we should use to decide the `unlock X bosses to advance` requirements. memcite

## S8-M0201 - user - 2026-10-01T17:04:53.565000+00:00
Message ID: 4d793bd8-16fa-4b30-b183-e275a3534d96

sure do that

## S8-M0202 - assistant - 2026-10-01T17:04:57.609800+00:00
Message ID: bfd202d1-b2bd-4a47-a590-fd1158c4e1eb

Using the current plugin mapping we just inspected, if we pull **all six raid entries** out of their existing tiers and make them the **Grandmaster / Raids** capstone, the tree becomes:

| Tier | Current | Raids removed | Bosses |
|---|---:|---:|---|
| **Easy** | 9 | **9** | unchanged |
| **Medium** | 13 | **13** | unchanged |
| **Hard** | 22 | **22** | unchanged |
| **Elite** | 13 | **11** | ToA + ToA Expert removed |
| **Master** | 8 | **6** | CoX + ToB removed |
| **Grandmaster / Raids** | 2 | **6** | all raid modes consolidated here |

So we'd have this progression shape:

```text
EASY          9 bosses
   ↓
MEDIUM       13 bosses
   ↓
HARD         22 bosses
   ↓
ELITE        11 bosses
   ↓
MASTER        6 bosses
   ↓
GRANDMASTER — RAIDS
               6 entries
```

The **Grandmaster/Raids** tier would contain:

> Chambers of Xeric  
> Chambers of Xeric: Challenge Mode  
> Theatre of Blood  
> Theatre of Blood: Hard Mode  
> Tombs of Amascut  
> Tombs of Amascut: Expert Mode

There's one design detail I think we should address before deciding the `X required` numbers: **should Challenge/Hard/Expert modes really be equal peers to their base raid?**

Because our rule so far is *every node within a tier is equal*. That would mean someone could theoretically unlock **ToB Hard Mode without first unlocking normal ToB**.

I think raids may warrant an exception where the three base raids are equal, but their advanced modes branch directly from them:

```text
        GRANDMASTER — RAIDS

   [CoX]          [ToB]          [ToA]
     │              │              │
     ▼              ▼              ▼
 [CoX CM]       [ToB HM]      [ToA Expert]
```

That feels much more natural while preserving the **all bosses are equal within a normal tier** rule. But that's the next decision—I wouldn't lock that exception without you. memcite

## S8-M0203 - user - 2026-10-01T17:05:36.751000+00:00
Message ID: af982b71-79a6-4aa5-8ecb-8895e5e1f574

there should be 3 raids here 
ToA
CoX
ToB

## S8-M0204 - assistant - 2026-10-01T17:05:37.516983+00:00
Message ID: 982737cd-1b8b-4571-94c1-b4cb351928eb

Correct. **Grandmaster should contain exactly 3 Raid nodes**, not six:

```text id="0wt27d"
         GRANDMASTER — RAIDS

        ┌─────┐
        │ ToA │
        └─────┘

 ┌─────┐         ┌─────┐
 │ CoX │         │ ToB │
 └─────┘
```

- **Tombs of Amascut (ToA)**
- **Chambers of Xeric (CoX)**
- **Theatre of Blood (ToB)**

Expert ToA, Challenge Mode CoX, and Hard Mode ToB **do not get separate progression nodes**. They're variants of their respective raid rather than separate bosses for Wheelbound progression.

That leaves our tree counts as:

**Easy 9 → Medium 13 → Hard 22 → Elite 11 → Master 6 → Grandmaster/Raids 3**

I'd lock that. The next decision is how many bosses from **Easy's 9** you need to unlock before Medium opens. memcite

## S8-M0205 - user - 2026-10-01T17:05:55.377000+00:00
Message ID: fbc88cab-d78b-4690-8b9f-5104474e112e

perfect lets lock this in then for the bossing tier

## S8-M0206 - assistant - 2026-10-01T17:05:56.980352+00:00
Message ID: 043bb4b5-8dc1-437b-a7d5-06fdbe3c284c

Locked. **Bossing tier structure is now CONFIRMED.**

```text
BOSSING PROGRESSION

Easy
9 bosses
   ↓
Medium
13 bosses
   ↓
Hard
22 bosses
   ↓
Elite
11 bosses
   ↓
Master
6 bosses
   ↓
Grandmaster — RAIDS
3 raids: ToA • CoX • ToB
```

Within **Easy through Master**, every boss in a tier is an equal peer. The player unlocks any `X` bosses from that tier to gain access to the next tier. No boss-to-boss prerequisite chains.

**Grandmaster is specifically the Raid tier.** ToA, CoX, and ToB are the three progression nodes; Expert ToA, Challenge Mode CoX, and Hard Mode ToB are variants of those raids rather than separate progression nodes.

New bosses can simply be classified into the appropriate tier without restructuring the tree or invalidating existing progression.

The actual **`X bosses required` per tier remains BALANCE_TBD**, which fits our plan to determine those numbers during the economy/progression balancing pass rather than guessing now. memcite

## S8-M0207 - user - 2026-10-01T17:06:26.491000+00:00
Message ID: 8d7b538b-ac61-464f-b907-e2ca63a314b2

ill leave it up to you to generate how mant in each teir is needed to progress etc 

whats next

## S8-M0208 - assistant - 2026-10-01T17:06:28.695642+00:00
Message ID: ac23396e-d172-4207-adbe-36df9c86b527

I'll take ownership of the **boss-count requirements** and treat them as balance variables. I'll set sensible initial values in the eventual spec, then we'll validate them with the progression simulation rather than spending design time arguing over whether Hard should require 6 or 7 bosses.

So Bossing's overall progression architecture is settled.

### Next: What does unlocking a boss actually cost?

This is the remaining major mechanic inside the Bossing tree.

We already know:

> Unlock boss node → that boss becomes eligible for the Bossing Wheel → counts toward the tier's progression requirement.

The question is what the player does to unlock that node.

I see two good possibilities:

**A — FP Purchase**

You simply spend Fate Points.

```text id="yy3pt1"
Sarachnis
Cost: 300 FP

[UNLOCK]
```

Simple and consistent with the rest of the Progression Tree.

**B — FP + account requirement**

The node costs FP, but your account must also meet some requirement before you can buy it.

```text id="r6dwk2"
Sarachnis
Cost: 300 FP

Requirements:
✓ 65 Attack
✓ 65 Strength
✗ 60 Prayer

[LOCKED]
```

I'm leaning strongly toward **A — FP only**.

The reason is very Wheelbound-specific: your **Fates are already restricting how your account develops**. If we additionally require arbitrary combat levels to purchase bosses, we risk creating a bunch of annoying circular dependencies.

And the Bossing Wheel itself is supposed to be dangerous. Choosing to unlock something like Vorkath before you're comfortably prepared for it can be a strategic mistake.

That fits the mode:

> *You chose to put this boss into your Fate pool. Now Fate can pick it.*

So my recommendation is **boss nodes cost FP, with increasing costs at higher tiers, but don't require arbitrary combat stats to purchase.**

That's the **next decision** I'd lock before we move out of Bossing. memcite

## S8-M0209 - user - 2026-10-01T17:07:58.657000+00:00
Message ID: 70936005-b7a2-4d89-943e-e18aba5bba9c

just fate points leave it up to the user to decide if they are ready here. However to prevent absuing once you unlock they are in the wheel and its not off the table but im against removing them at the moment. Maybe a really expensive item

## S8-M0210 - assistant - 2026-10-01T17:08:02.259603+00:00
Message ID: 00b90945-b8ab-4582-be17-3106fab054ca

Yes. I think that makes Bossing considerably more interesting because **unlocking a boss is a commitment**, not just expanding a collection.

### CONFIRMED — Boss Unlocking

Bosses are unlocked **only with Fate Points**. There are no combat-level/stat requirements imposed by Wheelbound. If you think you're ready for a boss, you can buy it.

Once purchased:

```text
Purchase Boss with FP
        ↓
Boss permanently unlocked
        ↓
Immediately added to Bossing Wheel
        ↓
Can now be selected whenever Bossing is rolled
```

So you can't buy four easy bosses purely to rush the Medium-tier milestone and then remove the inconvenient ones. **Progression comes with wheel consequences.**

For removal, I agree with not making normal FP removal available. I'd leave it as a later, deliberately scarce mechanic—something like a **Fate Erasure** item/reward. It should be expensive enough that removing a boss feels exceptional rather than normal wheel management. We can decide whether that's purchased, earned, limited per run, etc. during the rewards/economy pass.

That also creates a nice strategic tension:

> *Do I unlock this boss because I need another node to reach the next tier, even though I really don't want Fate sending me there?*

That's very on-theme for Wheelbound.

**Next question:** we need to decide what a **Boss Fate actually requires once the Bossing Wheel selects a boss.**

We already established it generates a kill count *before* the Bossing Wheel spins. The remaining question is how that kill count scales.

For example, should an Easy boss potentially ask for something like **20–50 kills**, while a Master boss might only require **1–5 kills**?

I think that's the right direction: **kill count should be boss/tier weighted**, because 30 Giant Mole kills and 30 Sol Heredit kills obviously shouldn't be treated as equivalent Fates. memcite

## S8-M0211 - user - 2026-10-01T17:08:47.879000+00:00
Message ID: 059a4d5f-ec3f-457b-87fc-5db12e17c4b4

sure i like that

## S8-M0212 - assistant - 2026-10-01T17:08:48.658806+00:00
Message ID: f8388a2f-ceae-49f7-af56-ea76661dff63

Locked.

### CONFIRMED — Boss Fate Kill Counts

Boss Fates will generate a **tier/boss-appropriate kill count**. Lower-tier bosses generally receive larger kill requirements, while difficult endgame bosses receive much smaller requirements.

Something roughly like this as a starting point—not final balance:

```text
Easy       → 15–50 kills
Medium     → 10–30 kills
Hard       → 5–20 kills
Elite      → 3–10 kills
Master     → 1–5 kills
Raids      → 1–3 completions
```

Individual bosses can eventually have modifiers too, so we aren't pretending every boss within a tier takes identical time. We'll tune the actual ranges through simulation/playtesting.

The important sequence remains:

```text
Master Wheel → BOSSING

        ↓

"Your Fate demands 8 kills"

        ↓

Bossing Wheel appears

        ↓

SPIN

        ↓

        VORKATH

        ↓

FATE SEALED
Kill Vorkath 8 times
```

## Next: Fate Manipulation

I think **Bossing is mechanically complete enough to move on**. We've established its progression tree, unlock behavior, Bossing Wheel, commitment mechanic, kill-count generation, and Raid endpoint.

The next major branch I'd tackle is **Fate Manipulation**.

This is particularly important because it needs a very clear identity separate from Skilling Mastery:

**Skilling Mastery** manipulates the physical contents/weights of your Master Wheel.

**Fate Manipulation** should manipulate **what happens after Fate chooses something**—Fate Cards, Fate quality, choices, rerolls/second chances, rewards, risk, etc.

So the first question I'd tackle is:

> **What should the very first Fate Manipulation unlock actually give the player?**

I have a few ideas, but I'd start with that one decision rather than designing the whole branch at once. memcite

## S8-M0213 - user - 2026-10-01T17:09:35.218000+00:00
Message ID: eeb5ccf9-45df-4477-bc34-f98b57bdcaed

what your idea

## S8-M0214 - assistant - 2026-10-01T17:09:35.735123+00:00
Message ID: 48c687b3-6ed6-40a0-a721-3f2e01dd401d

I think Fate Manipulation should start **small and slightly dangerous**, then eventually give the player increasingly powerful ways to bend Fate without ever letting them completely control it.

My idea for the **first unlock** is:

### **Fate's Favor — Lesser Fate Cards**

Initially, every normal roll produces a **Standard Fate**. The first Fate Manipulation node unlocks the possibility of receiving a **Lesser Fate** instead.

A Lesser Fate is the same thing Fate selected, but easier.

For example:

```text
STANDARD FATE
Gain 30,000 Mining XP

        ↓ occasionally becomes ↓

LESSER FATE
Gain 20,000 Mining XP
```

Or:

```text
STANDARD BOSS FATE
Kill Vorkath 8 times

LESSER BOSS FATE
Kill Vorkath 5 times
```

The important part is that **you don't choose when it happens**. You've manipulated Fate enough that sometimes it goes easier on you.

Then I think the branch can eventually split into two philosophies:

```text
                    FATE MANIPULATION
                           │
                    [Lesser Fates]
                           │
                ┌──────────┴──────────┐
                │                     │
          CONTROL FATE           GAMBLE WITH FATE
                │                     │
        More information          Greater Fates
        Limited choices           Challenge Fates
        Fate Cards                Better rewards
        Rare second chances       Higher risk
                │                     │
                └──────────┬──────────┘
                           │
                    DEEP MANIPULATION
```

The **Control** side lets you influence outcomes without outright picking whatever you want.

The **Gamble** side says, essentially:

> "Give me something worse, and pay me more for completing it."

That's where **Greater Fates** and eventually **Challenge Fates** make sense.

One mechanic I'd particularly like deeper in the Control side is something like **Foresight**. Before spinning, Fate reveals *some* information about what's coming—but doesn't let you change it. Later upgrades could potentially turn that information into limited intervention.

I **wouldn't put normal rerolls near the beginning**. Rerolls are incredibly powerful in Wheelbound because the entire game is built around accepting what the wheel gives you. Too much rerolling would undermine the core premise.

So my proposed first node is:

> **Fate's Favor** — Unlocks a small chance for a Standard Fate to become a Lesser Fate.

And importantly, **Lesser Fate should probably award fewer FP**, so it's genuinely *easier Fate*, not simply an objectively superior roll.

That's where I'd start the branch. memcite

## S8-M0215 - user - 2026-10-01T17:11:56.545000+00:00
Message ID: 944ec214-3e9d-4473-976b-56d09c578722

so its good but it would have to operate differntly for different things 

exmaple the skilling i want it where it would present you with fate cards that are woth more or less points depending on the fate and then bossing shouldnt be the same as a punishment for a lesser but less kills and the same points ya know so unlocking it for 1 will have consequense for the others etc

## S8-M0216 - assistant - 2026-10-01T17:11:59.405144+00:00
Message ID: d13a74b0-f9e1-4704-b270-649aed966d0c

Yes — and I think that's actually **better than my original idea**.

The mistake would be defining Lesser/Greater as globally meaning “easier/harder.” Instead, the Fate type should represent a **reward profile**, and each activity interprets that profile differently.

For **Skilling**, I think the Fate Cards can make the tradeoff explicit:

```text
MINING

┌─────────────────┐
│   LESSER FATE   │
│ Gain 15,000 XP  │
│ Reward: 40 FP   │
└─────────────────┘

┌─────────────────┐
│  STANDARD FATE  │
│ Gain 25,000 XP  │
│ Reward: 75 FP   │
└─────────────────┘

┌─────────────────┐
│  GREATER FATE   │
│ Gain 40,000 XP  │
│ Reward: 140 FP  │
└─────────────────┘
```

If your progression has unlocked those Fate types, **you're presented with the cards and choose which Fate you're willing to accept**. That makes Fate Manipulation genuinely useful: you're gaining control over the *severity/reward* of the Fate, not changing what the wheel selected.

But **Bossing interprets those same unlocked Fate types differently**, because simply giving the player three cards would weaken the Bossing Wheel commitment.

For example:

```text
Bossing Wheel → VORKATH

Lesser Fate
Kill 4 Vorkath
Reward: 100 FP

Standard Fate
Kill 8 Vorkath
Reward: 100 FP

Greater Fate
Kill 14 Vorkath
Reward: 175 FP
```

So **Lesser Boss Fate is deliberately a tradeoff in favor of safety**: fewer kills, **same base FP**, rather than getting the skilling-style reduced reward.

That means unlocking Lesser Fates isn't universally beneficial in exactly the same way:

```text
LESSER FATE UNLOCKED
        │
        ├── Skilling
        │    Easier task
        │    ↓
        │    Lower FP
        │
        ├── Bossing
        │    Fewer kills
        │    ↓
        │    Same base FP
        │
        ├── Questing
        │    ??? different behavior
        │
        └── Combat
             ??? different behavior
```

And **that's the part I really like about your idea**: one progression unlock propagates across the entire Wheelbound system, but its effect is tailored to each Fate category.

It also gives us a strong rule for the design docs:

> **Fate tiers are global unlocks, but their mechanical effects are defined per Fate category.**

So we shouldn't design “Lesser Fate” in isolation. We should make a matrix for **Lesser / Standard / Greater / Challenge × Skilling / Combat / Questing / Bossing**, and decide what each intersection actually does.

I think **that's what we should design next**, one Fate tier at a time, starting with **Lesser Fate across all four categories**. memcite

## S8-M0217 - user - 2026-10-01T17:16:22.052000+00:00
Message ID: 1f23b5c2-8995-43fe-9e9c-aeb1ec2370df

Heres all my ideas 

im not going to rehash the skilling i want the fate cards there but they need to be purchased.

questing, you oringally get a choice of 1of 3 quests , only 2 real upgrades i see here are the addition of 2 more choices bringint he total up to 5, this is fine with me and open to other suggestions 

bossing. I want to levergate combat achievements somehow now going to doing some of these isnt a large feat on its own but like maybe getting x amount of points or doing all the combat achievements for  a boss or something (maybe these should be bounties, not all of them but something) other than that we have the lesser standard and greater, maybe the challenge fate is a series of combat achievments ?? would like to hear your input 

i think thats it unless im missing something

## S8-M0218 - assistant - 2026-10-01T17:16:22.921076+00:00
Message ID: 6cf636cc-aa57-44fa-a8f7-d63b652411a6

I think you've got the major pieces. The key is that **Fate Manipulation shouldn't force the same mechanic onto every category**. Each category should get manipulation that actually makes sense for it.

I'd structure what you've described like this:

| Fate category | Fate Manipulation |
|---|---|
| **Skilling** | Purchase access to Lesser / Standard / Greater Fate Cards with different XP ↔ FP tradeoffs |
| **Combat** | Same general Fate Card system as Skilling, applied to Attack/Strength/Defence/Ranged/Magic XP targets |
| **Questing** | Starts with 3 quest choices → purchase 4th choice → purchase 5th choice |
| **Bossing** | Lesser / Standard / Greater kill-count manipulation + Combat Achievement mechanics |
| **Challenge Fates** | Special category-specific challenges rather than simply “even harder Greater Fate” |

The one thing I think you're missing from your list is **Combat**. Since Combat eventually resolves to an XP Fate after you choose one of the five combat skills, I think it can mostly inherit the Skilling Fate Card system. That keeps us from unnecessarily inventing another system.

### Your Combat Achievement idea

I think there are actually **two good systems hiding in this idea**, and I'd use both rather than choosing between them.

**Combat Achievement Bounties** should handle the long-term accomplishment side.

Things like:

> Complete every CA for Scurrius  
> Complete 10 unique Hard-tier CAs  
> Earn X Combat Achievement points  
> Complete X CAs while Wheelbound is active

Those fit Bounties extremely well because they're persistent achievements that can happen naturally while completing Boss Fates. And importantly, they **don't need to interfere with what your current Fate is asking you to do**.

But then I'd use Combat Achievements differently for the **Challenge Boss Fate**.

Instead of:

> Greater Fate = kill Vorkath 15 times  
> Challenge Fate = kill Vorkath 25 times

—which is boring—

Challenge should fundamentally change the assignment.

Something like:

```text
BOSSING WHEEL
      ↓
   VORKATH
      ↓

╔══════════════════════════╗
║     CHALLENGE FATE       ║
║                          ║
║        VORKATH           ║
║                          ║
║ Complete 2 eligible      ║
║ Combat Achievements      ║
║                          ║
║ Reward: 350 FP           ║
╚══════════════════════════╝
```

The plugin looks at the selected boss's **currently incomplete Combat Achievements**, filters out anything inappropriate/impossible, and generates a Challenge Fate from them.

That gives us a really clean Boss Fate progression:

```text
LESSER
Fewer kills
Same base reward

STANDARD
Normal kill requirement
Normal reward

GREATER
More kills
Significantly more FP

CHALLENGE
Combat Achievement objective(s)
Premium FP / special rewards
```

I especially like Challenge being CAs because it means **Challenge isn't just Greater-but-bigger**.

There is one complication we'll need to account for later: a player may eventually have completed most/all CAs for a boss. So Challenge generation needs a fallback rather than becoming unavailable. We can design that when we specify Challenge Fates.

### One change I'd make to Skilling

You said the Fate Cards **need to be purchased**, and I agree. I wouldn't have the first Fate Manipulation node simply unlock all three cards.

I'd make the tree itself build your choices:

```text
              STANDARD
              (default)
                  │
          ┌───────┴───────┐
          │               │
     LESSER FATE      GREATER FATE
       [BUY]              [BUY]
          │               │
          └───────┬───────┘
                  │
            CHALLENGE FATE
                [BUY]
```

So initially you don't really manipulate Fate at all.

You invest FP into the Fate Manipulation tree and gradually gain options. That also gives this branch a very obvious progression identity.

And I **wouldn't add more mechanics to Questing just because the branch looks smaller**. Three choices → four → five is already powerful. Questing has permanent consequences and prerequisite chains; five random eligible quests is a lot of agency compared with where the player started.

So I think we're at a good point to lock the overall identity:

**Fate Manipulation = purchasing additional ways of responding to the Fate you've already been given.**

It doesn't change what the Master Wheel landed on. It changes **what options you have once it lands.**

The next single decision I'd tackle is **exactly how the purchased Skilling/Combat Fate Cards work**, because that's going to establish what Lesser, Standard, Greater and Challenge actually mean economically. memcite

## S8-M0219 - user - 2026-10-01T17:19:57.307000+00:00
Message ID: 61ea1f53-e1da-4089-a5ab-a5dd47b1c485

as long as we do this i think its fine , maybe we cna also introduce general challenge fate, similair to the punsihment slices we add a challenege slice and its a general earn x combat acheivemnets how you see fit&#x20;

otherwise this is very very important and id almost want to steer away out of feer we did not cover our bases .&#x20;
The sistuation i want to avoid is a single boss and complete x ca's and there maybe only 1 left and it would be hard for most grand masters&#x20;
The plugin looks at the selected boss's **currently incomplete Combat Achievements**, filters out anything inappropriate/impossible, and generates a Challenge Fate from them.

## S8-M0220 - assistant - 2026-10-01T17:19:59.699524+00:00
Message ID: cefef3e4-6afe-4c4a-878b-b8b42f837a01

I agree with your concern. I **would not make “complete X remaining CAs for the selected boss” a core Boss Challenge Fate**. It creates exactly the failure case you're describing: the better the account becomes, the more likely the remaining achievements are extremely difficult, weirdly specific, or effectively inappropriate for a normal Fate.

I'd separate these concepts.

### CONFIRMED direction: General Challenge Fate

A **Challenge Slice** can eventually be added to the Master Fate Wheel, similar structurally to a Tainted Slice—but positive and intentionally unlocked.

When landed on, it pulls from a library of **general Challenge Fate templates** rather than being tied to whatever normal category would have been rolled.

Combat Achievements are perfect for this because we can make the objective broad:

```text
CHALLENGE FATE

Prove Your Worth

Earn 6 Combat Achievement points.

Complete any eligible Combat Achievements
you choose.

Reward: 400 FP
```

Or:

```text
CHALLENGE FATE

Trial by Combat

Complete 3 Combat Achievements.

You choose which achievements.

Reward: 300 FP
```

Now the player can look across the entire game and decide **how they want to satisfy it**.

That preserves one of the things I think is important about Wheelbound: Fate determines the objective, but the player still has room to strategize about *how* to accomplish it.

### Bossing should stay safer

I'd simplify Bossing back down to:

```text
LESSER BOSS FATE
↓
Reduced kill count
Same base FP

STANDARD BOSS FATE
↓
Normal kill count
Normal FP

GREATER BOSS FATE
↓
Increased kill count
Increased FP
```

Then Combat Achievements can interact with Bossing primarily through **Bounties**, where we can safely create specific accomplishments:

> Complete all CAs for a particular boss  
> Reach X total CA points  
> Complete your first Elite CA  
> Complete X unique CAs  
> Complete specific notable achievements

Those aren't blocking your active Fate, so an extremely difficult remaining achievement isn't capable of trapping the run.

---

And I want to make one broader rule explicit because you're right that this area is important:

> **No randomly generated Challenge Fate should depend on a shrinking pool of unfinished permanent content unless we can guarantee the generated objective remains reasonably completable.**

That applies beyond CAs. Quests, collection-log slots, one-time achievements, diaries, etc. can all create the same problem.

Our Punishment system can be horrible, but even **Punishment Fates have a deterministic path to completion**. A normal Challenge Fate should certainly meet at least that standard.

So I think we should **lock the concept of a general Challenge Slice**, but **not design its challenge library yet**. Like the ~50 Punishment templates, it deserves its own dedicated design pass where we go through challenge categories, eligibility rules, scaling, rewards, and hardlock protection. That's one area I don't want us to casually fill in and discover holes later. memcite

## S8-M0221 - user - 2026-10-03T13:46:30.107000+00:00
Message ID: 0c5d3c6c-c6f8-489d-9fae-f9e4a11d2412

thats fine and can you rename this chat as something more meaningful rather than 'Branching code on git'

## S8-M0222 - assistant - 2026-10-03T13:46:31.259943+00:00
Message ID: 5910d254-0236-4f35-a90a-4807132f3c3f

A much better name for this chat would be:

**Wheelbound Mode – Game Design & Progression**

I can’t directly rename the chat from here, but you can click the current chat title **“Branching code on git”** in the sidebar/menu and rename it to that.

That title should fit what this conversation has become—we're essentially building the full Wheelbound design specification before implementation. memcite

## S8-M0223 - user - 2026-10-03T13:47:08.739000+00:00
Message ID: 8908dddf-15e4-4689-8adf-80a91b79cf42

okay whats next how far along do you think we are overall

## S8-M0224 - assistant - 2026-10-03T13:47:10.361620+00:00
Message ID: 441a3e3c-bb2e-4402-b355-2b52c033b756

We're pretty far along on the **game-design side**. I'd estimate the overall Wheelbound project is roughly **55–65% designed**, but only a small portion of the actual Wheelbound mode is implemented. Most of our progress has been answering the hard question: *what exactly are we building?*

### Where we are

I'd break the remaining work roughly like this:

| Area | Status |
|---|---|
| Core Wheelbound concept / run lifecycle | 🟢 ~90% |
| Grand Fate / endgame objective | 🟢 ~85% |
| Master Fate Wheel | 🟢 ~90% |
| Skilling progression | 🟢 ~90% |
| Combat Fates | 🟢 ~85% |
| Questing progression | 🟢 ~85% |
| Bossing progression | 🟢 ~90% |
| Fate Manipulation | 🟡 ~55% |
| Fate Cards | 🟡 ~60% |
| Bounties | 🟡 ~70% |
| Shops / Tempt Fate | 🟢 ~80% |
| Defy Fate / Taint | 🟢 ~85% |
| Punishment system | 🟡 ~65% concept / templates not designed |
| Challenge system | 🟠 ~30% |
| Fate Point economy | 🟠 deliberately deferred |
| Exact progression costs | 🟠 deliberately deferred |
| UI/UX | 🟠 concepts established, detailed design deferred |
| RuneLite technical architecture | 🔴 mostly still to do |
| Persistence/save format | 🔴 still to do |
| Edge cases / exploit audit | 🔴 still to do |
| Testing/simulation design | 🟡 approach established |
| Implementation plan | 🔴 after spec is finished |

The important part is that the **core gameplay loop is no longer fuzzy**.

We know what happens from starting a Wheelbound account through getting Fates, progressing the tree, building the wheel, unlocking bosses/quests/skills, running out of spins, Defying Fate, getting punished, and ultimately pursuing the Grand Fate.

## What I'd do next

I think we should **finish Fate Manipulation**, since we're already in it.

We've now established an important principle:

> Fate Manipulation doesn't need to affect every Fate category identically.

And we have:

**Skilling/Combat:** purchase additional Fate Cards offering different workload/reward tradeoffs.

**Questing:** purchase additional quest choices, going **3 → 4 → 5**.

**Bossing:** Lesser / Standard / Greater change kill requirements and rewards. We're *not* going to casually tie Boss Fates to remaining boss-specific CAs.

**General Challenge:** eventually unlock a dedicated Challenge Slice containing broadly completable challenges such as earning X CA points/achievements. It gets its own careful design pass.

### The next decision I'd make

Let's define exactly what happens when you purchase **Lesser and Greater Fate Cards for Skilling/Combat**.

My starting proposal is very simple:

```text
BEFORE UPGRADES

Mining selected
      ↓
┌──────────────────┐
│  STANDARD FATE   │
│                  │
│ Gain 25,000 XP   │
│                  │
│ Reward: 100 FP   │
└──────────────────┘
```

Buy **Lesser Fate**:

```text
Mining selected
      ↓

┌──────────────┐    ┌──────────────┐
│    LESSER    │    │   STANDARD   │
│              │    │              │
│ 15,000 XP    │    │ 25,000 XP    │
│    55 FP     │    │    100 FP    │
└──────────────┘    └──────────────┘

          PLAYER CHOOSES
```

Eventually buy **Greater Fate**:

```text
┌────────────┐ ┌────────────┐ ┌────────────┐
│   LESSER   │ │  STANDARD  │ │  GREATER   │
│            │ │            │ │            │
│ 15,000 XP  │ │ 25,000 XP  │ │ 40,000 XP  │
│   55 FP    │ │   100 FP   │ │   180 FP   │
└────────────┘ └────────────┘ └────────────┘
                 ↑
           PLAYER CHOOSES
```

Those numbers are just illustrative—the simulator determines the real ratios.

I particularly like this because it introduces an actual decision every time:

**“Do I want to get this Fate over with, do the normal assignment, or take on a much larger grind because I need Fate Points?”**

If you're good with that fundamental behavior, I'd **lock Lesser/Standard/Greater Skilling + Combat Fate Cards**, and then we can move to the next Fate Manipulation mechanic rather than worrying about the numerical ratios yet. memcite

## S8-M0225 - user - 2026-10-03T13:52:04.760000+00:00
Message ID: 8928542e-e685-426f-8a20-8be9a244b045

yep exactly and this should scale with the current level etc so were not always saying 15-40k exp

## S8-M0226 - assistant - 2026-10-03T13:52:05.851586+00:00
Message ID: 1ddec3f4-cd2e-4214-96a5-63bc5c52b112

Exactly. **Locked.** The 15k / 25k / 40k examples were only illustrative.

The actual Fate generator will calculate a **Standard Fate target first** based on the player's current level, the skill, and our eventual balance tables. Lesser and Greater then modify that generated target and its FP reward.

```text
Current Skill Level
        ↓
Level Bracket
        ↓
Skill Difficulty Modifier
        ↓
Randomized XP Range
        ↓
Clean Rounding
        ↓
STANDARD FATE TARGET
        ↓
 ┌────────────┬────────────┬────────────┐
 │   LESSER   │  STANDARD  │  GREATER   │
 │ lower XP   │ normal XP  │ higher XP  │
 │ lower FP   │ normal FP  │ higher FP  │
 └────────────┴────────────┴────────────┘
                PLAYER CHOOSES
```

So a level 25 Mining Fate might be only a few thousand XP, while a level 92 Mining Fate could be dramatically larger. And Mining, Agility, Runecraft, etc. don't necessarily use identical scaling because **skill difficulty/rate is part of the calculation**.

Combat uses the same framework for Attack, Strength, Defence, Ranged and Magic.

The exact XP ranges, Lesser/Greater multipliers and FP multipliers stay **BALANCE_TBD** until we run the simulation.

That means **Lesser / Standard / Greater Fate Cards for Skilling + Combat are now mechanically settled.**

### Next

I actually think we should stop adding more to Fate Manipulation for a moment and tackle the **General Challenge Slice** we just created.

It's potentially one of the easiest systems to accidentally make frustrating or exploitable, and you specifically said you don't want us moving forward only to discover we didn't cover our bases.

So I'd design its **eligibility rules first**, *before* inventing a bunch of cool challenges.

The first question I'd settle is:

> **Can a Challenge Fate ever require content the player hasn't unlocked through Wheelbound?**

My recommendation is **no**.

If you haven't unlocked Bossing, a Challenge shouldn't secretly become a way to access bosses. If a shop is locked, a Challenge shouldn't grant permission to use it. If a skill isn't on/unlocked for your Wheelbound progression, the Challenge shouldn't become a backdoor for training it.

That gives us a very useful foundational rule:

**Challenge Fates can change HOW you play, but they cannot bypass WHAT your Wheelbound account has unlocked.**

If you agree, I'd lock that before we design the actual Challenge pool. memcite

## S8-M0227 - user - 2026-10-03T13:54:59.580000+00:00
Message ID: e028779a-a08f-4b80-82a1-ac4900e36e88

no and maybe for the sake of more upgrades to spend points on we have the skill challanage fate and the boss challenge fate , also lets do away with letting them 'pick' all the time maybe we only present 2 cards and if they unlocked something there a chance it can be 1 of the 2 cards etc if we add more fate cards maybe we can up this to 3 or add an upgrade for that. 

lock in separate fate card with the same name for bossing and skilling maybe they have different art on the cards etc

## S8-M0228 - assistant - 2026-10-03T13:55:00.796299+00:00
Message ID: ac1b6d52-8700-4a42-9d95-40845dfb3df2

Yes. I like this **more than always giving the player every Fate Card they've purchased**. Too much guaranteed choice starts undermining the wheel.

Let's lock the revised system.

### CONFIRMED — Fate Cards are drawn, not always offered

When a normal Fate needs a Fate Card, the game initially generates **2 cards**.

**Standard Fate is available from the beginning.** Purchasing additional Fate Cards adds them to that category's **Fate Card pool**; it does **not** guarantee they'll appear.

For example, after purchasing Lesser and Greater Skilling Fates:

```text
SKILLING FATE POOL

Standard
Lesser    ← purchased
Greater   ← purchased

        ↓ DRAW 2 ↓

┌────────────────┐   ┌────────────────┐
│     LESSER     │   │    STANDARD    │
│                │   │                │
│  Lower XP      │   │  Normal XP     │
│  Lower FP      │   │  Normal FP     │
└────────────────┘   └────────────────┘

PLAYER CHOOSES ONE
```

Next time you might draw **Standard + Greater**. So purchasing Greater gives you the *possibility* of taking the high-risk/high-reward option rather than permanently giving you access to it.

We can then have a meaningful later progression upgrade:

> **Third Fate Card** — Fate presents **3 cards instead of 2**.

That becomes a powerful upgrade because it increases your ability to find the Fate type you want without ever becoming a straight reroll.

### Separate Skilling and Bossing cards

Also **locked**: Skilling and Bossing have independently purchased Fate Cards, even when they share a name.

So these are different progression unlocks:

```text
Lesser Skilling Fate
Greater Skilling Fate
Challenge Skilling Fate

Lesser Boss Fate
Greater Boss Fate
Challenge Boss Fate
```

They can have clearly different artwork while maintaining a shared visual language—for example, the **Lesser** rarity/frame treatment remains recognizable, but the Skilling card might have tools/runes/resources while the Bossing version has weapons/skulls/boss imagery.

That gives us **more meaningful FP sinks** without inventing filler nodes.

And importantly:

> Buying Greater Skilling Fate does **not** unlock Greater Boss Fate.

### Challenge Fates split too

I think your latest idea solves our earlier Challenge problem nicely.

Rather than one generic Challenge system, we have at least:

**Skill Challenge Fate** — special restrictions applied to an unlocked skill. These can draw from our eventual Challenge template library: restricted locations, bad tools, unusual methods, equipment restrictions, etc. Unlike Punishments, they're voluntarily selected from the Fate Card draw and should pay substantially better.

**Boss Challenge Fate** — boss-oriented challenges. This is where Combat Achievements *can* play a role, but we **do not** define it as “complete X unfinished CAs for this boss.” We'll design a safe pool of boss challenges later with fallback rules so a nearly-completed CA list can never screw the player.

I think that's an important distinction:

**Punishment = Fate is hurting you.**

**Challenge = Fate offers you something difficult in exchange for a premium reward, and you chose that card.**

And because Challenge is merely one card in the draw, you're not forced into it simply because you purchased the unlock.

---

I think **Fate Manipulation is now much healthier**. We have actual progression, FP sinks, increasing agency without eliminating randomness, and separate Skilling/Bossing progression.

The next thing I'd tackle is the **Skill Challenge Fate design** specifically. We don't need all the templates yet; we need to establish what kinds of restrictions are allowed and, more importantly, what we're *never* allowed to generate. That gives us the safety rails before we build the challenge library. memcite

## S8-M0229 - user - 2026-10-03T13:59:05.876000+00:00
Message ID: 683d24b1-6f8c-40ae-94c9-2bb0bd544854

So this is where it's going to kind of get difficult, because a lot of the skills will have mini games associated with them, and this is what I think the challenge fate should be designed around, is the skilling mini games. Now obviously some of the skilling mini games have really high requirements, so we don't want to ever give someone, like pyramid plunder, and then they can't access pyramid plunder, stuff like that. Other good things would be to, like, go get a resource with a low-level tool. So, like, for woodcutting and mining, like go mine, like a high-level tree with a low-level tool. Stuff like that is what I had the idea for the skilling challenge fate. Stuff that is, like, hard in skilling, or skilling methods that the wiki has described as high intensity. Although I don't know if we really want to go down that path, because it's kind of hard to say, like if you're doing construction oak dungeon doors. I don't know if we can detect that, so maybe we scratch that idea.

## S8-M0230 - assistant - 2026-10-03T13:59:06.887406+00:00
Message ID: bcacea5b-26c5-4f13-87d3-4e227f42a68a

I think you're drawing the right boundary. I would **scratch generic “high-intensity method” challenges** unless we can reliably detect the method. We shouldn't build Challenge Fates around instructions the plugin can only *hope* the player followed.

Instead, I'd make **Skilling Challenge Fates a curated, skill-specific system** built around things RuneLite can reliably validate.

### The three challenge types I'd use

**1. Minigame / activity challenges** would probably be the most interesting category.

Examples could eventually include things like Tempoross for Fishing, Wintertodt for Firemaking, Guardians of the Rift for Runecraft, Giants' Foundry for Smithing, Mahogany Homes for Construction, Hallowed Sepulchre for Agility, Pyramid Plunder for Thieving, etc.

But every challenge template gets explicit eligibility requirements:

```text id="oh3r4v"
PYRAMID PLUNDER CHALLENGE

Skill: Thieving
Required Thieving: X
Required quests/access: ...
Required area access: ...

Eligible?
    YES → can enter Challenge Fate pool
    NO  → NEVER generate this card
```

So we don't scale the challenge after it's selected. **We filter the candidate pool before the two Fate Cards are drawn.**

**2. Equipment-restriction challenges** are your low-level-tool idea, and I think these could be excellent.

For example:

> Chop X Yew logs using no axe better than Steel.

> Mine X Mithril ore using no pickaxe better than Mithril.

The important thing is that the generator understands the combination. We can't generate something literally impossible like requiring a tool that cannot perform the assigned action.

These are also nice because they're fundamentally different from Punishments. A Challenge might make you inefficient for a reasonable amount of time and pay you well. A Punishment can be absolutely miserable.

**3. Special-method challenges** can exist, but only when we can confidently validate them.

That's where I'd put your oak dungeon-door example.

Rather than saying:

> “The Wiki considers this high intensity, therefore make it a Challenge.”

our rule should be:

> **Can Wheelbound reliably observe that the player is performing this particular method?**

If yes, it's eligible for our challenge library.

If no, **we don't use it**.

That means we don't actually need to decide right now whether oak dungeon doors specifically work. During the RuneLite technical research phase we'll build a capability matrix:

```text id="z6g4dp"
Challenge                 Detectable?    Reliable?    Use?
──────────────────────────────────────────────────────────
Tempoross completion          ✓             ✓         YES
Wintertodt completion         ✓             ✓         YES
Specific resource gained      ✓             ✓         YES
Specific tool equipped        ✓             ✓         YES
Specific construction method  ?             ?         RESEARCH
"High intensity training"     ✗             ✗         NO
```

### One rule I'd lock now

I think this should become a fundamental Challenge Fate rule:

> **A Challenge Fate may only be generated when Wheelbound can establish beforehand that the account qualifies for the activity and can reliably determine afterward whether the challenge was completed correctly.**

That protects us from both directions.

We don't give someone inaccessible Pyramid Plunder, and we don't give them some complicated training-method restriction that the plugin can't actually enforce.

And I wouldn't require every skill to have the same number or type of Challenges. **Fishing might have several excellent minigame/activity Challenges while another skill might mostly have equipment or resource restrictions.** That's fine. Forcing symmetry would probably result in filler.

This also gives the eventual Challenge library a good identity: **interesting alternative ways to train**, rather than merely “gain more XP.”

I think that's enough to lock the *framework* without prematurely designing every skill's challenge. The actual skill-by-skill Challenge library deserves its own dedicated pass—and some technical research alongside it. memcite

## S8-M0231 - user - 2026-10-03T14:00:32.929000+00:00
Message ID: 0b04d7ef-e0ce-4cf3-a0d4-eed7a04c270b

yes lock this in 

A Challenge Fate may only be generated when Wheelbound can establish beforehand that the account qualifies for the activity and can reliably determine afterward whether the challenge was completed correctly.

## S8-M0232 - assistant - 2026-10-03T14:00:34.839657+00:00
Message ID: cc68a67e-54b6-4ff9-9a9a-4d76dbaa18b1

Locked as a **core Challenge Fate rule**:

> **A Challenge Fate may only be generated when Wheelbound can establish beforehand that the account qualifies for the activity and can reliably determine afterward whether the challenge was completed correctly.**

This applies to both **Skilling Challenge Fates and Boss Challenge Fates**, and any future Challenge Fate types unless we explicitly make an exception.

So: **eligibility before generation + reliable completion detection afterward.** If either side fails, that challenge doesn't belong in the pool. memcite

## S8-M0233 - user - 2026-10-03T14:01:28.711000+00:00
Message ID: 69ca2003-ee83-4745-bea0-5f9b9b9af1be

skilling in the wilderness could be an optin too

## S8-M0234 - assistant - 2026-10-03T14:01:29.933868+00:00
Message ID: 4768539b-6557-4836-afed-6adb3e14bcb0

Yes — that's a good additional category, but I would make **Wilderness Challenge Fates explicitly opt-in**, because they introduce PvP risk rather than just difficulty/inefficiency.

I'd lock the concept as:

> **Wilderness Challenges — Optional:** The player can purchase/enable access to Wilderness-based Skilling Challenge Fates. If disabled, Wilderness challenges are never eligible for the Fate Card pool.

Examples later could include Wilderness Agility Course, Wilderness resource gathering, Wilderness Hunter activities, etc., provided they pass our existing rule: **Wheelbound can verify the player has access and can reliably detect completion.**

The important distinction is that opting in **doesn't guarantee a Wilderness card**. It merely adds eligible Wilderness Challenge Fates to the pool, just like purchasing other Fate Card types.

And because the player voluntarily accepts the extra PvP risk, I'd expect Wilderness Challenge Fates to have an **additional FP reward modifier**. Exact bonus stays `BALANCE_TBD`.

I'd also make the opt-in reversible **when no Fate is active**. That way someone isn't permanently committed to Wilderness content just because they experimented with it, but they also can't disable it after seeing a Wilderness Challenge they already accepted. memcite

## S8-M0235 - user - 2026-10-03T14:02:13.168000+00:00
Message ID: d8d9e879-d916-4615-9bb3-0c892dc69cfc

make it a fate card pool, and we can make some stuff to reward the pvp players if they decide to go that rouet as well cant leave them out

## S8-M0236 - assistant - 2026-10-03T14:02:14.776101+00:00
Message ID: a2562640-8ea9-4490-be9e-3c5cbe7f9a54

Yes. I prefer that over a settings-style opt-in. **Wilderness should be its own purchasable Fate Card pool**, just like the other Fate types.

### CONFIRMED — Wilderness Fate Cards

The Fate Manipulation tree can contain a dedicated **Wilderness Fate** unlock. Until purchased, Wilderness Fates simply don't exist in your card pool.

Once purchased:

```text
SKILLING FATE CARD POOL

Standard
Lesser       ← purchased
Greater      ← purchased
Challenge    ← purchased
Wilderness   ← purchased
```

When cards are drawn, a Wilderness Fate has a chance to be one of the 2 presented cards. The player still has another card to choose from, so they're **never forced into PvP-risk content just because they purchased the unlock**.

A later **3-card draw upgrade** further increases choice.

### Wilderness Fates can go beyond skilling

And yes, I absolutely think we should support the players who actually **want Wilderness/PvP risk** rather than treating the Wilderness exclusively as a punishment.

Wilderness Fate could eventually have its own curated pools:

```text
WILDERNESS FATES
      │
      ├── Skilling
      │    Wilderness Agility
      │    Wilderness resource gathering
      │    Wilderness activities
      │
      ├── PvM
      │    Wilderness bosses
      │    Revenants
      │    Other dangerous PvM
      │
      └── PvP
           PvP-oriented objectives
           Anti-PKing / player kills
           Risk-based challenges
           etc.
```

We'd need to be **especially careful designing PvP objectives** so we don't incentivize boosting with friends/alts or create objectives that are effectively RNG-dependent. But there's plenty of room to reward legitimate PvP players.

Wilderness Fates should also carry **meaningfully better rewards** because the player is voluntarily accepting additional risk. Exact FP/rewards stay `BALANCE_TBD`.

And our Challenge safety rule still applies: **Wheelbound must verify eligibility beforehand and reliably detect completion afterward.**

I think this gives Fate Manipulation a much stronger set of meaningful purchases now:

**Lesser • Greater • Challenge • Wilderness • Additional Card Draw**

without filling the tree with meaningless +5% style upgrades. memcite

## S8-M0237 - user - 2026-10-03T14:02:53.188000+00:00
Message ID: c0b7812b-66c5-4cba-b2b1-dfec9ce66ecd

sure lets flush that out later, whats next here

## S8-M0238 - assistant - 2026-10-03T14:02:55.108043+00:00
Message ID: 03c7a144-da5e-47da-9b7b-0ad43a133f30

I think we've done enough on **Fate Manipulation** for now. The framework is solid, and the remaining work—exact Challenge libraries, Wilderness/PvP Fates, card probabilities, FP multipliers—is better handled during the detailed content/balance passes.

The next major system I'd design is **Bounties**.

We already have the shell: they're always active during Wheelbound, rare drops/events can complete them, there are rarity tiers, rewards are manually claimed, and they have their own searchable UI. But we haven't really answered the most important design question:

> **What should Bounties actually accomplish for the player beyond just giving FP?**

My preference is that Bounties become Wheelbound's **“unexpected jackpot” progression system**. Most give FP, but particularly significant Bounties can give things you normally can't simply purchase—or give you a shortcut to them.

For example, an Imbued Heart dropping while legitimately doing a Slayer Fate could trigger a Legendary Bounty and potentially reward FP **plus something special**: a Fate Card unlock/token, rare wheel manipulation item, boss-removal item, Taint cleansing resource, etc.

That would make Bounties exciting rather than:

> “Oh cool, +400 FP.”

And it gives us a natural home for some of the rare consumable mechanics we've discussed—especially that **extremely expensive/rare way to remove an unlocked boss from the Bossing Wheel**.

So the next single decision I'd make is:

**Should Bounties be primarily FP rewards, or should higher-rarity Bounties be one of the main sources of rare Wheelbound items/abilities that can't normally be bought?**

I'm strongly in favor of the latter. memcite

## S8-M0239 - user - 2026-10-03T14:05:20.065000+00:00
Message ID: 61c05ad5-6934-4f53-aad3-b4446daf1ae0

So I don't like unexpected bounties. I want the person to know what they're going after, and this should be more like an end goal system. Like, for example, a lot of monsters and creatures in the game have drop rates, and if it's a particularly high drop rate, maybe like one out of five twelve, maybe they get, like, a little bit of fate points from that, maybe, like, 25 fate points, right? Or whatever we determine the scale for rewarding completions of tasks and maybe failures and stuff like that. We can balance these accordingly, but I think, like, big achievements, like specific drops, maybe, like, high-level drops that are harder to get, like the draconic visage, the imbued heart, stuff like that that is just like, wow, like I can't believe I got that. It needs to feel good for the account system. I'd also like to reward bounties for doing the achievement diaries. So, like, if you're able to do any achievement diaries, we need to reward them accordingly. The bounties is basically just, like, what have you accomplished on your account? Treasure trails, stuff like that from reward caskets, those would be cool, like a piece of third age. And maybe we don't do, like, a third age axe, third age robe, third age, you know, whatever. It's just, like, a piece of third age, maybe a piece of gilded, stuff like that where we can come up with a bounty system and it's just like, oh, this is pretty neat. And it's just widely available and stuff like that.

## S8-M0240 - assistant - 2026-10-03T14:05:22.073905+00:00
Message ID: 1c6a4f65-d0f1-4528-8d33-ac31a5b50cac

Yes. That changes how I think about Bounties, and I think it's a cleaner system.

**Bounties should be a visible account-accomplishment catalog**, not surprise missions or random hidden jackpots. The player can open the Bounties page from day one and see the things Wheelbound recognizes as meaningful accomplishments.

Something like:

```text
BOUNTIES

Drops             18 / 142
Achievement       7 / 48
Treasure Trails    3 / 35
Bossing            5 / 60
Special            1 / 20
```

A player can actually browse this and think, *“I'd really like to get that one someday.”* But importantly, the Bounty doesn't give them permission to pursue it. If an Imbued Heart bounty exists, that doesn't mean they can go train Slayer whenever they want. They still have to encounter the opportunity legitimately through Wheelbound.

### Drop bounties

I wouldn't make every remotely uncommon drop a giant event. We'd scale the recognition.

A relatively routine notable drop—say something around 1/512, depending heavily on what drops it—might simply be:

> **Bounty Complete — +25 FP**

Then you get into progressively more significant account moments where the reward becomes substantial.

Things like **Draconic Visage, Imbued Heart, boss pets, extremely rare uniques, etc.** should feel like major Bounty completions.

And I agree with grouping sensible families rather than creating hundreds of redundant entries. Treasure Trails are a great example:

```text
☐ Obtain any piece of Gilded equipment

☐ Obtain any piece of 3rd Age equipment
```

We don't need:

```text
☐ 3rd Age Platebody
☐ 3rd Age Platelegs
☐ 3rd Age Robe Top
☐ 3rd Age Robe Bottom
☐ 3rd Age Axe
...
```

unless there's a particularly iconic item we deliberately want to recognize separately.

### Achievement Diaries fit perfectly

I think **every Achievement Diary tier should have Bounty recognition**.

For example:

```text
ARDOUGNE

☑ Easy Diary
☑ Medium Diary
☐ Hard Diary
☐ Elite Diary
```

And rewards scale dramatically with tier. Completing an Elite Diary is a much more meaningful account milestone than completing an Easy Diary.

This also works nicely because Wheelbound itself isn't saying:

> “Go complete the Ardougne Elite Diary.”

It's saying:

> “If your Wheelbound journey happens to develop your account enough that you can accomplish this, Fate recognizes it.”

That's exactly what I think the Bounty system should represent.

### Treasure Trails

There's a lot we can do here without drowning the player in individual-item bounties:

> Open X clue caskets  
> Obtain a Gilded piece  
> Obtain a piece of 3rd Age  
> Obtain a mega-rare clue reward  
> Complete X Master clues  
> Obtain certain iconic clue uniques

We can have both **collection-style accomplishments** and **exceptional single-drop accomplishments**.

And this gives me a much clearer definition I'd lock:

> **Bounties are Wheelbound's permanent account-accomplishment system. They recognize notable drops, milestones, achievements and rare events obtained legitimately during the Wheelbound journey.**

They're **visible beforehand**, so the player knows what accomplishments exist and what they're worth.

They are **not assignments**. You don't activate an Imbued Heart bounty and then receive permission to grind for one.

They are also **not limited to RNG drops**. Achievement Diaries, clues, boss accomplishments, collection milestones and other meaningful account achievements all belong here.

I would also revise one thing from our previous design: I **wouldn't make rare special items the default Bounty reward system**. Your framing makes me think FP should remain the primary reward. Getting an Imbued Heart is already the extraordinary event; Wheelbound should recognize it with a suitably large FP payout and presentation rather than constantly layering another jackpot on top.

We can still have a **small number of special Bounties with unique rewards** if we discover something that genuinely deserves one later.

This is a system I'd eventually want us to build a **large curated catalog** for—probably well over 100 Bounties once we cover drops, diaries, clues, bosses, CAs and major account milestones. But we shouldn't hand-design all of those right now.

The **next question** I'd settle for Bounties is whether accomplishments should be **one-and-done permanently**, or whether certain repeatable things—rare drops in particular—can pay a smaller amount again after the original Bounty has been completed. My inclination is strongly toward **one-and-done**, otherwise we'd accidentally create alternate FP farming methods. memcite

## S8-M0241 - user - 2026-10-03T14:07:07.444000+00:00
Message ID: df2e8e8e-4330-4475-a3b4-b15ca43ee4ac

And this is, I don't know, because, like, it seems like there's a lot of bounties that could be had. There's, like, collection logs. There's, like, you could— do we reward somebody for getting a piece of a collection log? Do we also reward them for green logging something in their collection log? I don't know, and I feel like the bounties could blow up a lot, and it could ruin the system. So I'd like to hear your input and see how we can balance this, or if there's something that we could do to kind of push people to green log certain things. You know, like, oh, like I'm going for Vorkath. That's, like, you know, I'm trying to get all— if I get all the Vorkath drops, like, that's pretty cool. Like, not many people do that. It's something that should be rewarded in this game mode. But there's a ton of stuff in the game that you can do that. So it's, like, what do you do? Like, there's— you can green log the combat achievements. You can green log their drop table. So, like, there's two sides to that coin, and there's something that we could do here, but I just can't put two and two together at the moment. What do you got for me?

## S8-M0242 - assistant - 2026-10-03T14:07:09.392572+00:00
Message ID: 557d54aa-99a9-4c0d-8251-67cd9cbc4c60

I think I see the missing piece: we're currently trying to make **Bounties carry two different jobs**.

One is recognizing cool individual accomplishments like an Imbued Heart or 3rd Age piece. The other is rewarding someone who decides, *“I'm going to completely master Vorkath.”*

I would **separate those concepts instead of stuffing everything into the Bounty list**.

### Bounties = notable individual accomplishments

Keep Bounties curated. We deliberately choose things that deserve recognition rather than automatically turning every Collection Log slot into a bounty.

For example:

```text
VORKATH BOUNTIES

☐ Obtain a Draconic Visage
☐ Obtain Vorki
☐ Complete a notable Vorkath CA milestone
```

We **don't** automatically make Dragonbone Necklace, Jar of Decay, every unique, every CA, every collection slot, etc. separate FP-paying bounties.

That keeps the Bounty catalog understandable and lets us control the FP economy.

### Then add a separate concept: Mastery

This is where I think your green-log idea belongs.

Certain pieces of content get a **Mastery objective**. Vorkath is a great example.

```text
╔══════════════════════════════════╗
║          VORKATH MASTERY         ║
║                                  ║
║ Collection Log          4 / 6    ║
║ Combat Achievements     7 / 10   ║
║                                  ║
║ Collection Mastery       ☐       ║
║ Combat Mastery           ☐       ║
║                                  ║
║ COMPLETE BOTH                    ║
║          ↓                       ║
║ ★ VORKATH MASTERED ★             ║
╚══════════════════════════════════╝
```

Now we've captured **both sides of that coin** you were talking about.

**Collection Mastery** = green-log the defined Vorkath collection.

**Combat Mastery** = complete all applicable Vorkath Combat Achievements.

Do both and you get the much rarer:

> **Vorkath Mastery**

That is something I think Wheelbound should celebrate heavily.

### The crucial part: not everything gets Mastery

This solves the explosion problem.

We don't say:

> Every Collection Log category = Bounty + every item = Bounty + every green log = Bounty + every CA = Bounty...

Instead, we maintain a **curated Mastery catalog** for content where completion actually represents something meaningful.

Bosses are the obvious candidates. Some minigames/content could also deserve it. We can decide those later.

And we don't necessarily need to pay FP for every little step.

For Vorkath:

```text
Individual collection slots
        ↓
NO automatic FP

Notable rare Bounty
Draconic Visage
        ↓
FP reward

Green-log Vorkath
        ↓
Collection Mastery reward

Complete all Vorkath CAs
        ↓
Combat Mastery reward

Complete BOTH
        ↓
VORKATH MASTERY
Major reward / recognition
```

That prevents the economy from hemorrhaging FP because somebody filled 300 miscellaneous Collection Log slots.

### This could also give us a real endgame collection

I'd actually make **Masteries visible somewhere in Bounties**, rather than creating another giant top-level RuneLite section.

Something like:

```text
BOUNTIES

[Notable] [Diaries] [Clues] [Masteries]

                    MASTERIES

Bosses
────────────────────────────
✓ Scurrius             ★
✓ Barrows              ★
○ Vorkath             72%
○ Zulrah              41%
○ Corrupted Gauntlet  18%
○ Nex                   4%

Activities
────────────────────────────
○ Tempoross            65%
○ Wintertodt           48%
...
```

Now a Wheelbound player who loves completionism has this **massive optional meta-goal** sitting alongside the actual Fate system.

And critically, **Mastery doesn't give permission to grind something**.

If you want Vorkath Mastery, you still need Wheelbound to give you legitimate opportunities to fight Vorkath. That could make finally unlocking Vorkath on your Bossing Wheel particularly exciting for a completionist.

### Rewards don't all need to be FP

This is also where I think we can safely get more creative.

Normal Bounties → predominantly FP.

Collection Mastery → FP + perhaps cosmetic/recognition.

Combat Mastery → FP.

**Full Mastery** → potentially something genuinely special.

Maybe Full Masteries contribute toward another progression track, unlock cosmetics/card art, grant rare Wheelbound utility, titles inside the plugin, or eventually unlock some extremely expensive mechanics we've discussed.

That part can wait.

The important design distinction I'd lock is:

> **Bounties recognize curated notable accomplishments. Masteries recognize comprehensive completion of major pieces of content. Individual Collection Log slots do not automatically become Bounties.**

And for content such as bosses, **Collection Mastery and Combat Mastery are separate accomplishments; completing both can award Full Mastery.**

I think that gives us the missing structure without allowing Bounties to become an uncontrollable checklist of thousands of things. memcite

## S8-M0243 - user - 2026-10-03T14:14:20.817000+00:00
Message ID: 014cdcf0-a583-41f4-b05d-c4356db3cac2

So here's the other thing, right? It's easier to do the combat achievements for a boss rather than the collection log. Because the collection log might have something like a jar of whatever, and that's like one in one thousand. I'm not trying to make someone sit there and kill Vorkath for a thousand hours, especially because them killing Vorkath is going to completely be determined on the fact of if they get it from the wheel. So I think maybe we pick— give me a number of item-based bounties. We're going to do a fixed number of item-based bounties across the board, across the bosses, and just, like, just in general RuneScape, item-based bounties. Give me a number. We can put them in tiers, and they can be, like, really low FPS, like fate points, like maybe ten. Like maybe ten for, like, some of, like, the easier ones and, like, you know, a little bit higher for some of the harder ones, but we give them something. But we have a finite number of them. And then for the combat logs, we can give them something for green logging, you know, every boss. Like if you green log a boss, you get maybe, like, maybe fifty fate points, right? But you always get that for the combat achievements, but the collection log is typically really harder and is considered more of a grind. So we won't do that.

## S8-M0244 - assistant - 2026-10-03T14:14:24.186799+00:00
Message ID: 807ef0e3-b7d6-4ec8-84f7-56ff167a2566

Yes. I think that's the cleaner answer, and I'd **drop Collection Log Mastery entirely**. It fights against Wheelbound's structure because the player doesn't have unlimited access to the content.

You could get Vorkath from the Bossing Wheel several times and still never see a 1/1,000-ish item. I don't want the game implicitly telling the player they're “72% done” with Vorkath when finishing it could require an absurd number of additional Bossing rolls.

### My number: **100 Item Bounties**

I think **100 curated item-based bounties** is the sweet spot.

That's enough that the system feels substantial across an entire account, but finite enough that **we can hand-curate every single one** rather than algorithmically turning RuneScape's collection log into Wheelbound objectives.

I'd roughly distribute them into five tiers:

| Tier | Approx. count | Example reward* | Purpose |
|---|---:|---:|---|
| Common | 25 | ~10 FP | Early/notable drops |
| Uncommon | 25 | ~20 FP | Moderate accomplishments |
| Rare | 25 | ~40 FP | Meaningful uniques |
| Epic | 15 | ~75 FP | Major account drops |
| Legendary | 10 | ~150+ FP | Imbued Heart/3rd Age-level moments |

\*Those FP numbers are placeholders. Our simulation sets the actual economy.

And importantly, **rarity tier should represent the accomplishment, not simply the numerical drop rate**. A 1/512 drop from something you kill incredibly quickly isn't necessarily equivalent to a 1/512 raid unique.

The catalog can span the whole game rather than being “100 boss drops”: boss uniques, Slayer drops, clue rewards, skilling uniques, minigame items, exceptionally rare drops, and other iconic items.

Things like **“Obtain any piece of 3rd Age”** can count as *one* bounty, exactly as you suggested earlier. That lets us cover RuneScape broadly without the list exploding.

### Combat Achievements work differently

This is where I think your guaranteed system works extremely well.

We don't need to hand-curate CA bounties for every boss.

If a boss has a defined Combat Achievement set:

> **Complete every Combat Achievement associated with that boss → receive a fixed Boss Mastery FP reward.**

So:

```text id="b5zv6k"
VORKATH

Individual CAs
    ↓
No individual FP payout

Complete ALL Vorkath CAs
    ↓
★ VORKATH COMBAT MASTERY ★
+ FP
```

And that can exist for **every boss where the game's CA system gives us a meaningful boss-specific set**.

I wouldn't necessarily make every boss pay exactly 50 FP, though. The principle should be guaranteed, but the payout can scale by difficulty. Green-logging Scurrius CAs and green-logging something like the Inferno shouldn't be economically identical.

We can establish a simple CA Mastery reward table during balancing.

### This gives Bounties very clean boundaries

We now have three different accomplishment systems without runaway scope:

**100 Item Bounties** — fixed, curated list. One-time completion. Mostly FP. Never automatically expanded just because OSRS has another Collection Log slot.

**Boss Combat Mastery** — complete all CAs associated with a boss. Available wherever applicable. Guaranteed FP recognition. No requirement to green-log that boss's item collection.

**Achievement Diaries** — completing diary tiers also awards FP, with increasingly significant rewards through Easy → Medium → Hard → Elite.

Then things like Treasure Trails primarily enter the **100 Item Bounties** through curated accomplishments such as any 3rd Age piece, any Gilded piece, etc., rather than creating another enormous checklist.

That puts a hard ceiling on the part most likely to explode.

And I think we should explicitly **REJECT “green-log a boss's Collection Log” as a standard Wheelbound objective/reward**. Players can absolutely do it if they want, but Wheelbound shouldn't incentivize potentially thousands of kills when access to that boss is itself Fate-controlled.

I'd lock **100 curated Item Bounties + boss-wide CA completion rewards + Achievement Diary rewards**, with all actual FP values remaining `BALANCE_TBD`. memcite

## S8-M0245 - user - 2026-10-03T14:16:27.908000+00:00
Message ID: c1ff08f4-f686-4c37-9fb1-28ba6a23e1ca

I like 100. I mean, it's good. I'd also like to have maybe, like, daily drops. There's so much stuff to do in the game of RuneScape that, like, maybe we can do some dailies. Three dailies, and it's randomized amongst all the drops in the game. You get three dailies. They're worth, you know, pretty low points overall, but it's just something. Just so that there's, like, a little bit of variation in there, and that people always kind of, like, check it. They refresh every 24 hours, and you randomly generate them from, like, I don't know how this is gonna work, like code-wise. Like, do you build a database of literally every single item in the game, or how does that work? Because that might be a lot. I don't know. Is there, in Runelite, is there a plugin limit size? Like, what is the... how large can a plugin be before it gets rejected?

## S8-M0246 - assistant - 2026-10-03T14:16:33.844545+00:00
Message ID: 4463ae3c-fb54-4563-968a-0268cd5f6762

I like the daily idea a lot, with one important adjustment: **don't randomize from literally every OSRS item**. That would generate garbage constantly—quest items, shop items, unobtainable variants, trivial items, items the account can't reasonably get, etc.

### CONFIRMED direction — 3 Daily Bounties

Every 24 hours, Wheelbound generates **3 Daily Item Bounties**. They're visible, worth relatively small amounts of FP, and separate from the permanent **100 curated Item Bounties**.

For example:

```text
DAILY BOUNTIES                    Refresh: 13h 42m

☐ Obtain a Rune scimitar              +10 FP
☐ Obtain a Brine sabre                 +15 FP
☐ Obtain a Dragon plateskirt           +20 FP
```

They should still obey the Wheelbound rules: **a daily bounty does not grant permission to pursue the item**. If your Fates happen to give you legitimate access to an activity that can produce it, great. Otherwise that daily might simply expire.

I actually like that. These aren't assignments you must complete; they're little opportunities layered over the run.

And we can have safeguards: no duplicates among today's three, don't immediately repeat yesterday's items, filter obviously inappropriate items, and potentially weight the three slots so you tend to get something like **easy / moderate / aspirational** rather than three ridiculous drops.

Exact rewards and refresh behavior remain balance variables.

## We don't need to ship RuneScape's entire item database

RuneLite already knows about OSRS items. Its API exposes `ItemComposition` for item metadata, and `ItemManager` can look up item compositions, canonicalize item IDs, retrieve item sprites, prices, etc. citeturn0search4turn0search10

So Wheelbound shouldn't contain thousands of images and full item definitions.

What **we do need to own** is the knowledge RuneLite doesn't inherently give us:

> “This item is an appropriate Daily Bounty and these are the circumstances under which obtaining it should count.”

I'd therefore maintain a curated **Daily Bounty candidate pool**. And this can still be quite large—say **500–1,000 eligible item IDs** eventually.

An entry can be tiny:

```text
Item ID
Daily difficulty/weight
Eligibility tags
Optional special rules
```

Then at runtime RuneLite supplies the actual item name/sprite/etc.

Even **1,000 records like that is trivial in size** compared with the artwork we're eventually going to put into Wheelbound.

### Plugin size

I checked the current Plugin Hub documentation. I **couldn't find a published hard maximum JAR/file-size limit** in the current submission rules. RuneLite explicitly permits plugins to bundle resources such as images and sounds in `src/main/resources`, and Plugin Hub distributes the resulting plugin as a JAR. citeturn0search0turn0search3

The current tooling guidance does specifically warn developers to **optimize PNG assets**, because Java expands images in memory based on their dimensions—not merely their compressed file size. citeturn0search1

So I'm **not concerned at all about the data side** of Wheelbound. A list of hundreds or even thousands of item IDs is tiny.

What I'll watch carefully during our eventual UI/art phase is the amount and resolution of custom artwork we're bundling.

Also, avoiding an external database/server is preferable here. Plugin Hub allows third-party dependencies but says they require additional verification and generally increase review time; keeping Wheelbound self-contained where practical should make review simpler. citeturn0search0

So I'd lock:

**100 permanent curated Item Bounties + 3 rotating Daily Item Bounties drawn every 24 hours from a much larger curated eligible-item pool.**

And I'd let the daily pool become **hundreds of items**, because technically that's not a size problem. The harder problem is curating which items make for *good* daily bounties, which we can generate and audit later.

urlRuneLite Plugin Hub documentationturn0search0

I think the next Bounty decision should be **Achievement Diaries**, because those give us a predictable non-RNG progression source to balance against all these item-based rewards. memcite

## S8-M0247 - user - 2026-10-03T14:18:15.229000+00:00
Message ID: f2ba42d5-86e1-413d-9fd3-ec54d8b43665

Achievement diaries can be their own thing. So, like in the bounties tab, I imagine we have, like, categories. We have, like, specific items. We have combat achievements. You know, we'll call them combat masteries, right? Which essentially is just, like, the combat achievements, but, like, you have to green log them to get them. And then we have the achievement diaries, which is its own thing. Those can probably be a little more because a lot of the achievement diaries are weight-based, or are based on fate, if you will, from spinning the wheel. So if they can complete some achievement diaries, we should reward them accordingly. A little more than maybe some of the items: hard, medium, and elite, and then so we have a category for each, you know, stuff like that. Maybe, like, a little, like, pane and then, like an icon for, like the banner for each territory. Something like that.

## S8-M0248 - assistant - 2026-10-03T14:18:17.164887+00:00
Message ID: edd8df1f-69f3-42c1-a82f-03bf0e2b896d

Yes. I think that gives the **Bounties tab a clear structure** instead of making it one enormous checklist.

### CONFIRMED — Bounties becomes the Account Accomplishments hub

I'd structure the screen roughly like:

```text
╔══════════════════════════════════════════╗
║                 BOUNTIES                 ║
╠══════════════════════════════════════════╣
║ [ITEMS] [DAILY] [COMBAT] [DIARIES]      ║
╚══════════════════════════════════════════╝
```

**Items** contains the 100 permanent curated item bounties, grouped/filterable by rarity.

**Daily** contains the three rotating item bounties refreshing every 24 hours.

**Combat** contains **Combat Masteries**. Each applicable boss gets a mastery entry. Completing/green-logging that boss's Combat Achievements completes its Combat Mastery. We're specifically **not requiring the boss's Collection Log**.

**Diaries** gets its own full category for Achievement Diaries.

And I really like your visual idea for Diaries. Rather than another boring list, each region could have its own pane/card using the recognizable regional diary/banner iconography:

```text
┌───────────────────────────────────────────┐
│             🛡 VARROCK                    │
│                                           │
│  ✓ EASY        Claimed                    │
│  ✓ MEDIUM      Claimed                    │
│  ○ HARD        + ??? FP                   │
│  ○ ELITE       + ??? FP                   │
│                                           │
│                2 / 4                      │
└───────────────────────────────────────────┘
```

Then **Karamja, Ardougne, Falador, Fremennik, Kandarin, etc.** each have their own regional card.

We can do the polished visual treatment later during our dedicated UI phase.

### Diary rewards should be meaningful

I agree that these should generally pay **better than ordinary item bounties**.

Achievement Diaries are particularly interesting in Wheelbound because the account can't simply train whatever it needs whenever it wants. Your Fates have effectively determined whether you've developed the weird combination of skills, quests and access needed to complete a diary.

So something like:

```text
Easy       → modest FP
Medium     → meaningful FP
Hard       → substantial FP
Elite      → major FP
```

Not necessarily absurd rewards, because there are quite a few diaries, but completing an Elite Diary during a restricted Wheelbound run should absolutely feel significant.

Again, we'll derive the actual FP numbers when we model the economy.

### One distinction I'd make

I wouldn't literally call the tab **Combat Achievements**, because then players may reasonably expect every individual CA to pay FP.

I'd call it **Combat Masteries** exactly as you suggested.

Opening one can show:

```text
VORKATH MASTERY

Combat Achievements
████████████████░░░░  8 / 10

✓ Achievement
✓ Achievement
✓ Achievement
○ Achievement
○ Achievement

Complete all Vorkath Combat Achievements

REWARD
??? Fate Points
```

That communicates very clearly:

**individual CAs = progress**

**green-log the boss's CAs = reward**

And that also gives us somewhere to celebrate completion without creating hundreds of individual CA bounties.

---

So I think **Bounties is now ~85% conceptually designed**. We don't need to design the 100 items, daily candidate pool, every mastery, every diary payout, or UI right now. Those belong in the content/balance/UI passes.

The next major system I'd move to is **Shops / Account Restrictions**. We've designed the Tempt Fate gamble and individual shop unlocking, but we haven't yet done the same kind of exploit/hardlock pass we've now done for Fates and Bounties.

That's important because shops, trading, GE, NPC stores, quest-required purchases, and potentially ironman-like interactions are where Wheelbound can accidentally make an account impossible to progress. memcite

## S8-M0249 - user - 2026-10-03T14:20:21.046000+00:00
Message ID: c1c39f5c-1d2d-4483-ac13-fb37688cd4b3

Right. So I don't think this should be in the bounties tab, but rather there should be some sort of UI on the sidebar that can allow us to see what shops that we have unlocked throughout Varlamor. So, and I'm talking anything that sells anything, anything, any NPC that has a trade on it needs to be bought individually for a really low cost if they want. So, like general stores might be 10 in the grand scheme of things. The GE should be probably something a little higher, maybe like 500. But every single store should have some sort of, like, you know, a cost associated with it. Maybe we can introduce something like after you unlock X amount of general stores, like the price goes up. So you have to kind of be more strategic about where you unlock your general stores instead of just going, Oh, you know, I'll go here, here, here, here. They become more expensive. Actually, now that I'm saying that out loud, I like that idea a lot. So as you unlock stores of the same type, per se, they should get more expensive. And maybe you could offer some advice. Do we do it per store type? Do we do it per amount of stores unlocked throughout Varlamor? That might be a little easier to track because some things like the kebab shop or, like, a rune shop, there aren't that many of, but they should still cost more as you buy more shops, is what I'm thinking.

## S8-M0250 - assistant - 2026-10-03T14:20:25.060120+00:00
Message ID: 70037f56-f556-42f1-9aa7-4ccc945a91d2

I like the **escalating shop cost** idea a lot. I would **not** make the escalation purely global, though, and I also wouldn't make it hyper-specific per exact store type. I think there's a middle ground that creates better decisions.

### My recommendation: Shop Classes

Every tradeable NPC/store belongs to a broad **shop class**, and the price escalates based on how many shops you've unlocked **within that class**.

For example:

```text
GENERAL STORES
1st unlock     → 10 FP
2nd            → 15 FP
3rd            → 25 FP
4th            → 40 FP
5th            → 65 FP
...

RUNE / MAGIC SHOPS
1st            → 25 FP
2nd            → 50 FP
3rd            → 100 FP
...

FOOD / COOKING SHOPS
1st            → 10 FP
2nd            → 20 FP
3rd            → 35 FP
...

WEAPON / ARMOUR SHOPS
1st            → 20 FP
2nd            → 40 FP
3rd            → 70 FP
...
```

Those numbers are just examples; `BALANCE_TBD`.

That creates exactly the behavior you're describing. Unlocking the Lumbridge General Store for 10 FP isn't a big decision. But by your sixth general store, you might think:

> *Do I actually need this one? I've already got general-store access elsewhere.*

### Why I prefer classes over one global counter

A completely global counter creates some weird incentives.

If you've unlocked 30 relatively unimportant shops, suddenly a little food vendor might cost an absurd amount simply because it's your 31st shop.

Conversely, if every exact type has its own progression, we'd end up maintaining dozens of tiny counters:

```text
Kebab shops: 1/?
Candle shops: 0/?
Fishing shops: 2/?
Beer shops: 1/?
...
```

That's unnecessarily complicated.

Broad classes give us enough granularity to make decisions meaningful without turning shop classification into its own game.

I'd expect maybe **6–10 shop classes**, not 40.

### Special shops should ignore the normal curve

Some things are too powerful or unique to fit into escalating categories.

The **Grand Exchange** is the obvious example.

```text
GRAND EXCHANGE

Special Unlock
Cost: ??? FP

[UNLOCK]
```

Your ~500 FP example could eventually be reasonable, but we'll determine that from the economy simulation.

Other unusually powerful/unique vendors could have a **fixed special price** rather than participating in a normal shop-class curve.

### And yes: literally every Trade NPC

I'd lock your rule pretty strictly:

> **While Wheelbound is Active, any NPC interaction that provides a Trade/shop interface requires that specific vendor to be unlocked.**

Not:

> “I unlocked one general store, therefore I unlocked general stores.”

You're purchasing **that particular shop**.

The category only determines its escalating price.

So:

```text
GENERAL STORE CLASS

Lumbridge General Store       ✓ UNLOCKED
Varrock General Store         ✓ UNLOCKED
Falador General Store         🔒 25 FP
Ardougne General Store        🔒 25 FP
etc.
```

Notice something useful there: **all currently locked stores in that class can cost the same next price**.

You don't have to nominate which store will be your “third” ahead of time. Whichever one you purchase next becomes #3, and then every remaining store increases to the #4 price.

That's very clean.

### Sidebar UI

And agreed: **none of this belongs in Bounties**.

I'd put it in the Account/Access area we've previously discussed:

```text
WHEELBOUND

🎡 Fate
📜 Current Fate
🌳 Progression
🎯 Bounties
🔐 Access
```

Then **Access** could eventually contain:

```text
SHOPS
Grand Exchange
Other restricted systems...
```

And Shops can be searched/filterable:

```text
SHOPS                         17 / ???

[Search shops...]

All | Locked | Unlocked | Banned

GENERAL STORES                 3 unlocked
Next unlock: 40 FP

✓ Lumbridge General Store
✓ Varrock General Store
✓ Falador General Store
🔒 Edgeville General Store       40 FP
🔒 ...
```

We can also group/filter geographically, which handles your “what have I unlocked throughout Varlamore?” use case nicely.

### One thing this makes much more interesting

**Tempt Fate becomes better under this system.**

Suppose your next Magic Shop costs 150 FP.

You encounter one you really want:

```text
AUBURY'S RUNE SHOP

Guaranteed Unlock
150 FP

—or—

TEMPT FATE
50 / 50

WIN → Unlock shop
LOSE → Shop becomes BANNED
```

Now the gamble gets more tempting as that shop class becomes more expensive.

That's exactly the sort of FP decision I want Wheelbound creating.

So I'd **lock escalating prices by broad Shop Class**, with individually unlocked NPC shops and separately priced special access such as the GE.

The next shop question I think is **very important** before we go further: **quest-required shops/trading**. If a Fate assigns a quest that literally requires purchasing something from an NPC, we need to decide whether Fate grants temporary access, forces you to spend FP, or handles required transactions another way. That can create genuine hardlocks if we don't establish the rule now. memcite

## S8-M0251 - user - 2026-10-03T14:22:38.900000+00:00
Message ID: 99d3f34e-a670-4989-9eab-a0e95daa7c49

Let's exclude bars. No, actually, we'll keep bars in there. Let's make them, like, we'll have, like, an other shop. So if they don't fall into a nice, neat category of something, maybe we can put them in other, and they could be kind of— they'll have, like, a weight, like when we did the skilling tolerance. Like not all skills are created equal, not all shops are created equal. So maybe, like, the other shops have a little more leniency versus how many you buy them. So, like if you buy, like, a kebab shop, a candle shop, and a fishing shop, maybe it doesn't scale as hard on the fate points as, like, general stores do. Something like that, I think would be cool. But the GE needs to be— I think the GE should be unlocked behind something crazy. Like maybe you should... maybe it should be, like, a challenge. Like instead of just paying for it outright, like you have to unlock the shop by doing a challenge spin or something. Because the GE is, like, the granddaddy of all shops. If you have the GE, it's just you win. And I think you shouldn't be banned from the GE. So you can never lock yourself out of the GE, but you can lock yourself out of, like, smaller shops.

## S8-M0252 - assistant - 2026-10-03T14:22:41.831833+00:00
Message ID: bd59d6cc-9ea6-4645-8583-07867d667691

Yes. I think that improves both systems. I'd lock most of this and keep the **exact GE challenge** open until we design it properly.

### CONFIRMED — Shop pricing uses classes + scaling weights

We'll have several major shop classes, but **Other** catches vendors that don't fit cleanly.

Importantly, each class doesn't have to use the same escalation curve.

```text id="ihztrb"
GENERAL STORES
Escalation: HIGH

10 → 20 → 40 → 70 → 110 → ...

RUNE / MAGIC SHOPS
Escalation: HIGH
Higher starting value

FOOD SHOPS
Escalation: MEDIUM

WEAPON / ARMOUR
Escalation: MEDIUM/HIGH

OTHER
Escalation: LOW

10 → 12 → 15 → 20 → 25 → ...
```

Numbers are `BALANCE_TBD`.

So buying some random candle shop doesn't make the next obscure vendor prohibitively expensive. But repeatedly unlocking **General Stores or Rune Shops**, which increasingly give you broad access to useful supplies, gets expensive much faster.

Bars stay. Something like a bartender/shop can simply fall under **Food/Drink** if we establish that category, or **Other** if appropriate.

We'll eventually classify every tradeable NPC and give each shop class a **base cost + escalation weight**. That should be data-driven so balancing it later doesn't require rewriting logic.

---

## The GE should be fundamentally different

I strongly agree here.

The Grand Exchange isn't really another shop in Wheelbound. It's effectively:

> **Unlock unrestricted access to the player economy.**

Once you have it, tons of resource-access problems disappear.

So I'd explicitly remove it from:

- normal shop categories
- escalating shop prices
- Tempt Fate
- shop bans
- ordinary FP purchasing

Instead:

```text id="z0he6v"
              GRAND EXCHANGE
                    🔒
                     │
              SPECIAL UNLOCK
                     │
             GRAND CHALLENGE
                     │
                  SUCCESS
                     │
                     ▼
              GRAND EXCHANGE
             PERMANENTLY OPEN
```

And **failure never permanently bans the GE**.

### I think we can make the unlock really cool

Rather than merely:

> Complete one Challenge Fate.

I'd eventually make this a named Wheelbound milestone. Something like **Trial of Commerce** / **Fate of Fortune**—name TBD.

It could require reaching some prerequisite first, then performing a **special Challenge Spin** from a curated pool.

For example, maybe the challenge isn't even about shopping. Fate could demand some substantial account accomplishment before granting access to the game's most powerful marketplace.

The important part is that the player knows:

> **This spin is for my Grand Exchange unlock.**

It gets its own presentation and feels like a major account milestone.

And because we're already developing the Challenge Fate framework, we can reuse that technology without making the GE just another random Challenge card.

### Failure should hurt without destroying the account

I wouldn't make GE challenge failure meaningless either.

Something like:

```text id="ghnlh9"
Attempt GE Trial
      ↓
   SUCCESS ─────────→ GE permanently unlocked
      │
    FAILURE
      ↓
Cooldown / FP cost /
additional prerequisite /
another challenge required
      ↓
Try again eventually
```

Exact consequence can wait.

But our rule is now:

> **The Grand Exchange can never enter BANNED state.**

Normal individual shops can be permanently banned through Tempt Fate (subject to the very limited restoration mechanic we've discussed). **GE cannot.**

I think that's an excellent distinction because Tempt Fate remains legitimately dangerous without allowing somebody to permanently destroy the single largest economic unlock in their Wheelbound run.

---

So I'd record:

**CONFIRMED:** Individual NPC shops are classified into broad categories with different base costs and escalation rates. `Other` has deliberately gentle escalation for miscellaneous/niche vendors. Each individual shop still requires its own unlock.

**CONFIRMED:** GE is not a normal shop. It cannot be purchased normally, cannot be Tempt Fated, and cannot be permanently banned. It is earned through a special high-level **Grand Exchange Challenge** system, details to be designed later.

And I think **now we absolutely need to answer the quest-shop issue**, because it's the major loophole/hardlock sitting underneath all of this:

> If your active Quest Fate requires you to buy something from a shop you haven't unlocked, does the Quest Fate temporarily authorize **only that required transaction**, or are you expected to unlock the shop normally?

My inclination is actually **normal unlock required**, because otherwise quests become a way to bypass the shop economy—but there are some nasty edge cases we need to think through before locking that. memcite

## S8-M0253 - user - 2026-10-03T14:24:35.615000+00:00
Message ID: 4cf48096-792c-45b6-84e5-c154b5986616

I don't. I'm trying to think back if there's ever an instance where I needed to buy something to complete a quest from a shop specifically, and I don't know. I think, like, a lot of times it's like you need to buy, like, rope, or you need to buy a piece of bread, and you can get that at a lot of different places. I'm leaning more towards the fact of you just have to unlock it. I feel like, um, like the fate points, the points. There's enough points that you can go get in the game where you'll be fine. I don't think you'll ever run into that issue. I could be wrong. Maybe that's something we fix in the future. Yeah, that's kind of where I lie.

## S8-M0254 - assistant - 2026-10-03T14:24:39.967155+00:00
Message ID: 09dedc5a-925f-47db-a7fe-98086fd4aac8

I agree. I wouldn't add an exception until we have evidence we actually need one.

### CONFIRMED — Quest Fates do not bypass shop restrictions

If a Quest Fate requires something that must be purchased, **the player is responsible for acquiring it under the normal Wheelbound rules**.

If that means unlocking a shop, they spend the FP and unlock the shop.

```text
Quest Fate
   ↓
Need an item
   ↓
Can obtain without shop? ──→ Get it normally
   ↓ No
Need a shop
   ↓
Unlock that shop normally
   ↓
Continue quest
```

No temporary shop permission, no free quest purchases, and no “Quest Fate exemption.”

I also agree that we shouldn't overengineer a hypothetical hardlock now. We've already established multiple FP sources—Fates, item bounties, daily bounties, Combat Masteries, Achievement Diaries, etc.—and the cheaper shops aren't intended to be enormous investments.

What I **would** put in our eventual testing checklist is:

> **Quest Fate Hardlock Audit:** Verify every quest that can be generated has a viable completion path under Wheelbound's shop/access restrictions.

Our automated/simulation testing can also look for states such as `Active Quest Fate + insufficient FP + required locked vendor + no viable FP source`. If actual playtesting discovers an edge case, we solve that specific case rather than weakening the entire shop system preemptively.

So for v1 the rule is simple:

> **Fate does not pay your bills. If the quest needs a shop, unlock the shop.**

I think we're ready to move beyond basic shop unlocking now. The next piece I'd tackle is **Tempt Fate and shop bans in detail**—particularly how the 50/50 gamble is offered and how our extremely limited shop-ban restoration works. That's the last substantial piece of the shop system before I'd consider it designed. memcite

## S8-M0255 - user - 2026-10-03T14:26:20.169000+00:00
Message ID: 8d2e2516-68a7-495e-8b64-e230c65d3982

Because remember, in every single situation you can always, and there needs to be a UI for this. Like whenever you are first introduced to a shop, like you need to understand the concept of, hey, you can unlock this with fate points, or you can spend for your luck. So, like if worst comes to worst, maybe they spin it for the luck, but then, like, you're banned, you know, stuff like that. And we also need to communicate that as you buy more shops, the more expensive it gets. So, like maybe the first shop in each category is free, and then, like the very first time that they buy a shop, like, you know, congratulations, you purchased your first shop. Be aware that as you buy more shops, they're going to become more expensive. And then once again, on the sidebar in the UI somewhere, we need to have a system where we keep track of what has been unlocked, banned, currently locked, how many shops you've unlocked, and, like, how many more of that type of shop that you have until the next increase in fate points. So, like maybe one to two is free, two to four is, like, an increase of plus five points per shop, stuff like that. But, like, the user needs to be able to see kind of where they're at, what they're doing, so they don't just, like, all of a sudden go buy stuff and then, you know, they're like, what the heck happened?

## S8-M0256 - assistant - 2026-10-03T14:26:23.372647+00:00
Message ID: 6607c8fb-94a2-47e0-86a8-7e10234c625a

Yes. I think the important thing you're defining here is that **shop pricing must never be a hidden mechanic**. The player should always understand the consequence *before* spending FP or gambling.

I'd lock that as part of the shop system.

### CONFIRMED — First encounter with a locked shop

The first time the player attempts to trade with a locked NPC, Wheelbound intercepts it and introduces the system:

```text id="x4dzxu"
╔══════════════════════════════════════╗
║          SHOP LOCKED BY FATE         ║
║                                      ║
║        Bob's Brilliant Axes          ║
║                                      ║
║ Category: Weapon / Tool Shop         ║
║                                      ║
║ Unlock permanently                   ║
║            10 FP                     ║
║                                      ║
║ Current Fate Points: 84              ║
║                                      ║
║ [ UNLOCK — 10 FP ]                   ║
║                                      ║
║          — OR —                      ║
║                                      ║
║ [ 🎡 TEMPT FATE ]                    ║
║     50% Unlock                       ║
║     50% BANNED                       ║
║                                      ║
║              [CANCEL]                ║
╚══════════════════════════════════════╝
```

The gamble needs to be **extremely explicit**. No vague “Try your luck” button where the player discovers afterward that losing means the shop is banned.

Then the actual Tempt Fate wheel/animation happens after they deliberately choose it.

### First shop should teach the economy

I really like your idea of making the **first shop in each class free**.

It functions as onboarding while also guaranteeing that every account can establish a small basic shop network.

Something like:

```text id="w66khj"
FIRST GENERAL STORE UNLOCKED

This shop has been permanently added
to your Wheelbound account.

Your first General Store was FREE.

⚠ Shop prices increase as you unlock
more shops within the same category.

General Stores
1 / 2 in current pricing tier

Current cost: FREE
Next pricing tier: 5 FP

[VIEW SHOP ACCESS]
```

I'd probably do **first shop free**, rather than first two. One free choice per class feels generous enough and makes *which* shop you use that free unlock on meaningful.

But the exact thresholds stay `BALANCE_TBD`.

### I prefer pricing brackets over increasing every purchase

Your latest example actually gives us a better system than what I proposed earlier.

Instead of:

`10 → 15 → 25 → 40 → 65...`

I'd use **pricing brackets**:

```text id="3pmlby"
GENERAL STORES

Unlocked        Cost
────────────────────
0 → 1           FREE
2 → 3            5 FP
4 → 6           10 FP
7 → 9           20 FP
10+             40 FP
```

Again, fake numbers.

This is much easier for the player to understand and lets the UI say:

> **2 more General Store unlocks until prices increase.**

That's considerably better UX than silently recalculating some formula after every purchase.

Different shop classes get different bracket structures. **Other** can have much gentler brackets, while powerful classes can ramp aggressively.

### Shop Access UI

And yes, this absolutely needs a permanent management screen.

I'd make **Access** a major sidebar section:

```text id="91f2pz"
ACCESS

[Overview] [Shops] [GE]

SHOPS
────────────────────────────

Unlocked       17
Locked         84
Banned          2

GENERAL STORES
4 / 12 unlocked

Current price:       10 FP
Next increase:       2 shops

████████░░░░
4 / 6

✓ Lumbridge General Store
✓ Varrock General Store
✓ Edgeville General Store
✓ Falador General Store
🔒 Ardougne General Store
🔒 ...
```

Filters:

**All | Unlocked | Locked | Banned**

And search by NPC/shop name, plus probably filter by **shop class** and **region**.

So if someone is going to Varlamore:

> Region → Varlamore

and they can immediately see every known vendor and their Wheelbound state.

### Banned needs to be visually unmistakable

```text id="o89scm"
✕ BANNED

Aubury's Rune Shop

You Tempted Fate and lost.

This shop cannot currently be used.

Fate's Pardon remaining: 2 / 3
```

And trying to trade with that NPC should not show the ordinary unlock screen. It should tell them **this shop was banned by their previous Tempt Fate loss**.

That distinction is important:

**LOCKED** → Pay FP or Tempt Fate.

**UNLOCKED** → Trade normally.

**BANNED** → Neither option; restoration system required.

### One more thing I'd add

Before confirming an FP purchase, show its effect on the pricing bracket when relevant:

```text id="pzn7m7"
UNLOCK THIS SHOP — 10 FP?

This will be your 6th General Store.

⚠ Your next General Store will enter
a higher pricing tier:

Current:  10 FP
Next:     20 FP

[CANCEL]        [UNLOCK]
```

That directly addresses your concern about someone buying stores and then wondering why everything suddenly became expensive.

So the overarching rule I'd record is:

> **Wheelbound must communicate the current shop price, shop class, pricing-bracket progress, and consequences of both purchasing and Tempting Fate before the player commits. Shop-price escalation must never be hidden.**

I think that's strong enough that we can leave the actual bracket numbers for simulation.

And with that, **the ordinary shop system is basically designed**. The two pieces still needing their own passes are the special **Grand Exchange Challenge** and the rare **Fate's Pardon/shop-ban restoration** mechanic. memcite

## S8-M0257 - user - 2026-10-03T14:27:00.733000+00:00
Message ID: e0a3002f-74ea-43e0-ac2d-269cd4431cf8

i like this 

UNLOCK THIS SHOP — 10 FP?

This will be your 6th General Store.

⚠ Your next General Store will enter
a higher pricing tier:

Current:  10 FP
Next:     20 FP

[CANCEL]        [UNLOCK]

## S8-M0258 - assistant - 2026-10-03T14:27:02.924140+00:00
Message ID: 7c4cf39c-94ac-4117-b7db-ce79be27d1d2

Locked. I think that should be a **standard UX pattern throughout the shop system**, not just a nice example.

Whenever a shop purchase is about to affect the player's future pricing, Wheelbound explicitly tells them **before they confirm**:

```text id="3xbafq"
UNLOCK THIS SHOP — 10 FP?

This will be your 6th General Store.

⚠ Your next General Store will enter
a higher pricing tier:

Current:  10 FP
Next:     20 FP

[CANCEL]        [UNLOCK]
```

If the purchase **isn't** crossing a pricing threshold, we can keep the dialog simpler rather than warning them every single time.

That gives the player enough information to make a strategic decision without surprising them afterward.

**CONFIRMED:** Shop escalation is transparent, and purchases that push the player to the edge of a new pricing bracket explicitly warn them about the upcoming increase.

I think we can consider the **core shop-purchasing system locked** and move to the next unresolved piece: **Fate's Pardon / recovering a shop you've banned by losing Tempt Fate.** memcite

## S8-M0259 - user - 2026-10-03T14:27:24.643000+00:00
Message ID: 68e5b54e-8e8c-444e-a808-d6d59bdd8000

confirm this too 

Wheelbound must communicate the current shop price, shop class, pricing-bracket progress, and consequences of both purchasing and Tempting Fate before the player commits. Shop-price escalation must never be hidden.

## S8-M0260 - assistant - 2026-10-03T14:27:26.589344+00:00
Message ID: cb7deb03-3a3b-412c-a57f-812a302d1ad2

**CONFIRMED and locked as a core Shop UX rule:**

> **Wheelbound must communicate the current shop price, shop class, pricing-bracket progress, and consequences of both purchasing and Tempting Fate before the player commits. Shop-price escalation must never be hidden.**

This should be treated as a requirement when we eventually write the UI/UX and implementation specs—not merely a design preference.

The player should **never lose FP, enter a higher pricing bracket, or risk banning a shop without being shown the relevant consequences beforehand.** memcite

## S8-M0261 - user - 2026-10-03T14:27:36.353000+00:00
Message ID: 00f4761b-9272-4093-8abf-4701e04ac729

whats next

## S8-M0262 - assistant - 2026-10-03T14:27:39.069245+00:00
Message ID: 864dee5a-368c-4137-92d2-4f73d3f54403

Next I'd finish the **last two pieces of Shops**, then I'd consider that entire system designed:

1. **Fate's Pardon** — how you recover a shop that was BANNED after losing Tempt Fate.
2. **Grand Exchange Trial** — the special challenge required to permanently unlock the GE.

Let's do **Fate's Pardon first**.

We previously floated roughly **3 pardons per Wheelbound account**, and I still like that number. I would make them something you unlock through the **Fate Manipulation progression tree**, rather than simply purchase whenever you need one.

My proposed structure:

```text
FATE'S PARDON

Tempt Fate Loss
      ↓
Shop becomes BANNED
      ↓
Use Fate's Pardon?
      ↓
BANNED → LOCKED
```

Important: **Pardon does NOT unlock the shop.**

It only forgives your previous loss and returns the vendor to `LOCKED`. You still have to either:

- pay its current FP price, or
- **Tempt Fate again** and risk banning it again.

I also like making the three Pardons **three separate expensive progression nodes**, rather than buying one node that gives you three charges:

```text
Fate's Pardon I
        ↓
Fate's Pardon II
        ↓
Fate's Pardon III
```

Each grants **one permanent-use charge**. Once spent, it's gone.

That creates a lifetime maximum of **3 rescued shops**, and purchasing later Pardons can get progressively more expensive.

It also makes losing a 50/50 meaningful. You can't just think *“whatever, I'll cleanse it later.”*

That's the next thing I'd lock: **3 lifetime Fate's Pardons, earned individually through Fate Manipulation, each converts one BANNED shop back to LOCKED.** memcite

## S8-M0263 - user - 2026-10-03T14:28:47.011000+00:00
Message ID: 05c1b0bc-3b30-43f8-9750-e364a5580ada

All right, so to fate's pardon can be an item that you can buy in the shop for, like, a lot of fate points. So, like, you know, you failed to... maybe the shop was worth, like, 15, right? You messed up, not—

## S8-M0264 - assistant - 2026-10-03T14:28:49.974871+00:00
Message ID: add8cbbf-9425-4602-a6b6-f82d057d2568

I think I see where you're going, and I prefer that over my progression-node idea.

**Fate's Pardon should be a purchasable consumable in the Wheelbound shop**, but deliberately expensive enough that losing Tempt Fate hurts.

For example, if you gambled on a shop that only cost **15 FP**, lost, and banned it, you shouldn't be able to spend another 20 FP and undo the mistake. Maybe Fate's Pardon costs something substantial like **250–500 FP** depending on our eventual economy.

So:

```text
15 FP SHOP

Pay 15 FP ───────────────→ Safe unlock

       OR

Tempt Fate
    ↓
   LOSE
    ↓
BANNED
    ↓
"I actually really need this shop..."
    ↓
Buy FATE'S PARDON
for a VERY high FP cost
    ↓
Use Pardon
    ↓
BANNED → LOCKED
```

And then you're **still not done**. The shop is merely forgiven.

You now have to:

> Pay the shop's current unlock price  
> **OR**  
> Tempt Fate again like a lunatic. 😄

I would also **remove the three-lifetime-pardon limit** we were considering.

If Fate's Pardon is priced painfully enough, **FP itself becomes the limiting resource**. Someone technically *can* recover from repeated terrible decisions, but they're burning enormous amounts of progression currency doing it.

That creates a really nice consequence:

> *“I tried to save 15 FP by gambling and now I'm spending 300 FP fixing it.”*

That's exactly the sort of story Tempt Fate should create.

One additional balance lever we can keep available: **Fate's Pardon price itself could escalate with purchases** if testing shows players are abusing it. But I wouldn't start there. Start with one very expensive fixed price and let simulation/playtesting tell us whether we need additional punishment.

So my proposed lock is:

**Fate's Pardon = expensive, repeatably purchasable Wheelbound consumable. Using one changes one shop from BANNED → LOCKED. It never directly unlocks the shop. Exact FP cost is `BALANCE_TBD`.**

I think that's cleaner than putting three arbitrary Pardon nodes in the progression tree. memcite

## S8-M0265 - user - 2026-10-03T14:30:42.712000+00:00
Message ID: 84d2730d-012c-48f2-915f-c3879fcb596f

Yeah. So the fate's pardon needs to be something very expensive. It needs to be maybe an item. I don't think we increase the cost of it. I think it's just a flat rate of, like, very expensive. If you want to do it, that's fine. If not, whatever. The Grand Exchange trial is tricky because you could technically unlock the Grand Exchange immediately when you're level three, and I don't know if I like that or not. There's probably got to be some pre-reqs, some prerequisites that I'd like to establish before doing that. Just something bare minimum so that, you know, maybe you have to be, like, level 10 and you'll have to complete, like a fate challenge or something. Something, you know. What are your suggestions? Because the GE either needs to be a lot of fate points, or it might just be simpler just to make it, like, a lot of fate points, maybe like 3,000 or something. Once again, I don't know what the scaling of the points is, but it needs to be a one-time purchase of a lot of fate points, and you can't gamble it. Because I feel like if someone gets locked out of the Grand Exchange, they're going to be upset.

## S8-M0266 - assistant - 2026-10-03T14:30:45.280869+00:00
Message ID: bf92663f-fd41-4f5e-8b8b-de4ad6490733

I agree on Fate's Pardon, and I think your instinct on the GE is right too: **we may be overcomplicating it by inventing a special trial.**

### CONFIRMED — Fate's Pardon

Let's lock this version:

> **Fate's Pardon is an extremely expensive, fixed-price consumable item purchased with Fate Points.**

No escalating cost. No lifetime limit. No gambling for it.

Using it:

**BANNED → LOCKED**, never directly to Unlocked.

Its price is `BALANCE_TBD`, but deliberately painful. If someone wants to spend a huge amount of FP correcting a bad gamble, that's their choice.

---

### Grand Exchange: I'd use progression + a huge FP purchase

I would **not use total combat level 10** or another arbitrary character-level requirement. That's extremely easy to achieve and doesn't demonstrate that you've actually progressed through Wheelbound.

Instead, I'd gate the **ability to purchase the GE** behind Wheelbound progression itself.

Something like:

```text id="62m18m"
GRAND EXCHANGE
🔒 SEALED BY FATE

Requirements:

✓ Reach a Wheelbound progression milestone
✓ Complete X Fates
✓ Have enough Fate Points

Cost: 3,000 FP*

*BALANCE_TBD
```

I'd probably use **two prerequisites**:

**Complete X Fates** ensures you've actually played the mode for a while.

**Reach a certain progression milestone** ensures you didn't simply accumulate points from unusual early accomplishments.

Then comes the real gate:

> **Pay an enormous amount of FP.**

That gives us three independent protections against a level-3 account immediately getting GE access.

### Why I prefer this over the Challenge Trial

The more I think about it, a random Challenge Spin creates a weird result.

If someone gets lucky and completes an easy generated challenge, suddenly they have access to arguably the most powerful economic tool in the game.

And if someone gets an obnoxious challenge, GE access becomes RNG-dependent.

A huge FP purchase is much more aligned with Wheelbound:

> *“I've spent enough of my restricted-account journey building toward the point where I can finally afford unrestricted trade.”*

There's also a **massive opportunity cost**.

Imagine you finally have 3,000 FP. You could buy:

> GE access

**or**

> Boss unlocks + skills + Fate Cards + shops + wheel manipulation + progression nodes...

That's a fantastic decision.

### I wouldn't require a specific account level

Someone might have a weird Wheelbound account:

> 75 Mining  
> 70 Fishing  
> 68 Woodcutting  
> relatively low combat

Why tell them they're not sufficiently progressed to use the GE because their combat level isn't high enough?

Their **Wheelbound progression itself** is the better measure.

I'd eventually have the simulator determine something like:

> GE should normally become *possible* around the early/middle portion of a run, but actually purchasing it should require deliberately saving FP.

Then we derive the Fate-count/progression prerequisite and price that create that experience.

### And absolutely no gambling

I'd make this a permanent rule:

```text id="sos7mx"
GRAND EXCHANGE

NO Tempt Fate
NO ban state
NO Fate's Pardon
NO random unlock
NO random failure

Meet requirements
      +
Pay enormous FP cost
      ↓
PERMANENTLY UNLOCKED
```

Once you've earned it, you've earned it.

So my recommendation to lock is:

> **The Grand Exchange is a special permanent account unlock requiring meaningful prior Wheelbound progression plus a very large one-time FP payment. It cannot be unlocked immediately, cannot be Tempt Fated, cannot be banned, and never requires Fate's Pardon. Exact prerequisites and FP cost are determined during balance simulation.**

I think that's cleaner than the Grand Exchange Trial idea and gives us much better control over *when* unrestricted trade enters a Wheelbound run. memcite

## S8-M0267 - user - 2026-10-03T14:31:34.324000+00:00
Message ID: 6676e3eb-cddf-444a-9f31-10b83b4f153c

I like it. I like complete X fate and have a large amount of fate points. I think that's good. Um, yep, let's lock that in.

## S8-M0268 - assistant - 2026-10-03T14:31:36.603457+00:00
Message ID: 66d8e608-af3f-488f-8ccb-c793423b8cc3

Locked. **Grand Exchange access is now confirmed:**

> **The Grand Exchange is a special permanent Wheelbound unlock requiring both completion of X Fates and a very large one-time Fate Point payment.**

The exact `X Fates` and FP price remain `BALANCE_TBD` and will be derived during simulation.

There is **no Tempt Fate option**, no random trial, no ban state, and no Fate's Pardon interaction. Once the requirements are met and the player purchases it, the GE is permanently unlocked for that Wheelbound run.

That also means I consider the **Shop/Access system mechanically designed enough to move on**. We still need the eventual vendor classification database, pricing brackets, UI/art, technical validation, and balance numbers, but the rules themselves are established.

**Next I'd move to Defy Fate / Taint.** We have the major concept already, but there are several important unresolved details around what happens when the original spin reserve runs out, how additional spins are obtained, how Tainted slices accumulate, and especially how cleansing works. That's probably the next system worth tightening up. memcite

## S8-M0269 - user - 2026-10-03T14:35:00.662000+00:00
Message ID: 5b6a0bb4-af74-4070-b1df-90c03022d038

Okay, great. Let's take it one by one. What's the first thing that we want to go to about defying fate and taint? The whole idea around this is, like, so whenever you start this mode, you're going to get a finite number of spins, and the idea is that you'd be able to get pretty far with it, and if you're lucky and you do well enough, you should be able to complete your goal. Now, if you're unlucky and you didn't spend your spins and you didn't build your wheel accordingly, you might run out, and the defying fate slash taint is there to be like, all right, I'll tell you what. Like, you messed up, but we're going to give you some spins back, and then you can, you know. But your wheel's going to have, like, a bad slice in it, and if you land on this slice, it's not going to be fun. So stuff like that. There's, like, a punishment type of thing. And then this can also be where, like, the sacrifice system comes in, where you sacrifice an item to Death's coffer. And I think the way that we should do that one is, like, if you land on maybe that should be part of taint. That should be an option within the taint wheel, or, like, it should be an option in addition to the taint. So, like, you get a taint, and then you get a sacrifice slice, and if you get a sacrifice slice, you gotta get rid of something. And, like, maybe we do that as, like, the wheel requires a sacrifice, and in order to do this, maybe we register items in your bank. So, like, please go to the bank and, you know, open it up. And then we get a good idea of, like, what you got in your bank, and then it'll pick one of, like, 20 items in your bank. You know, it'll pick a few high-value items. It might pick a few low-value items, and then the wheel will spin, and whatever item it lands on, you gotta offer it to Descoffer. Like, that's what you have to do, and you can't really do anything else. I don't know how we're going to enforce that. I don't know what could be, like, something to, like, ensure someone does actually do this. I guess it would just be they're locked out. Like, they won't be able to get anything else in the mode. But, like, that's what you got to do. Like, it demands a sacrifice kind of thing. And I think the sacrifice slice should always be on the wheel. So, like, you can remove taint, but you can never remove sacrifice. So now that the sacrifice slice is there, it's there, and it takes up a slot, and it's a reminder that you didn't manage very well, and this could be a punishment that you get. But it's the same size as, like, all the other slices, so it's no bigger or smaller than the other ones. You can't make it smaller. It's going to stay one size.

## S8-M0270 - assistant - 2026-10-03T14:35:04.154207+00:00
Message ID: b917385f-7863-49bc-9b7d-ddc6bf492efd

Yes — and I think you just clarified an important distinction we hadn't nailed down before.

Previously we had **Death's Sacrifice as something the player voluntarily uses to obtain additional spins**. What you're describing now is more interesting: **Sacrifice becomes a permanent consequence of having Defied Fate.**

I'd separate this into two punishment mechanics:

```text
DEFY FATE
    │
    ├── Gain additional Master Wheel spins
    │
    ├── Add TAINTED SLICE
    │      └── Punishment Wheel
    │
    └── Add SACRIFICE SLICE
           └── Death demands an item
```

The key difference:

**Taint can eventually be cleansed. Sacrifice cannot.**

Once you've Defied Fate for the first time, the Sacrifice slice permanently occupies **one of your 24 Master Wheel slots** for the rest of that Wheelbound run.

And I really like that. It leaves a permanent scar from running out of Fate.

### First thing I'd lock: the Sacrifice Slice itself

Before we figure out exactly how Death chooses your item, I think we should establish its basic rules.

My proposed rules based on what you described:

> **The first time a player Defies Fate, one permanent Sacrifice slice is added to the Master Fate Wheel.**

It has normal `1.0x` slice weight.

It **cannot be upgraded, reduced, removed, cleansed, duplicated, or manipulated through Wheel Builder.**

If the wheel is already 24/24 when it's introduced, something has to be displaced to make room for it. We can determine exactly how displacement works separately.

Every future Defy Fate does **not** add another Sacrifice slice. There's always exactly one.

So eventually someone's wheel could look like:

```text
MASTER FATE WHEEL — 24/24

Mining
Fishing
Slayer
Bossing
Questing
...
Tainted
Tainted
Tainted
🩸 SACRIFICE  ← permanent 1.0x
```

They can potentially cleanse those purple Tainted slices.

But that Sacrifice slice?

**That's there because you ran out of your original Fate.**

### When you land on Sacrifice

I like your bank idea conceptually:

```text
SACRIFICE
     ↓
"Death demands an offering."
     ↓
Open Bank
     ↓
Wheelbound records eligible possessions
     ↓
Selects a mixture of candidate items
     ↓
SACRIFICE WHEEL
     ↓
Item selected
     ↓
That item is now demanded
     ↓
Deposit required item/value into Death's Coffer
     ↓
Wheelbound verifies sacrifice
     ↓
Fate complete → continue
```

And yes: **you don't get to continue Wheelbound until the sacrifice is fulfilled.**

We should *not* decide the item-selection algorithm yet. There are a bunch of nasty cases we'll need to solve: untradeables, stack quantities, extremely expensive items, worthless junk, duplicate items, whether equipped items count, whether the plugin can reliably observe Death's Coffer contribution, bank-cache limitations, etc.

That's its own design question.

But there's one change I'd recommend to your idea:

I wouldn't necessarily require **the exact physical item selected** to enter Death's Coffer.

Death's Coffer works around item value, and technical detection may make exact-item verification awkward. A potentially cleaner rule could eventually be:

> **Death demands a sacrifice valued at X GP, with the selected bank item determining X.**

So if it lands on a 4m item, Death demands ≥4m of accepted value.

**But I would NOT lock that yet.** We should research exactly what RuneLite can detect at Death's Coffer before choosing between exact-item sacrifice and equivalent-value sacrifice.

For now, I'd lock only the mechanic we know we want:

### CONFIRMED candidate

> **Defying Fate for the first time permanently adds exactly one Sacrifice slice to the Master Fate Wheel. It occupies a real wheel slot, has fixed normal weight, cannot be manipulated or cleansed, and remains for the rest of the run. Landing on it generates a mandatory Death's Coffer sacrifice that must be fulfilled before Wheelbound progression can continue.**

Then our **next single question** should be:

**What happens to the Master Wheel the very first time you press `DEFY FATE`?**

That's where we decide exactly how many spins it gives, how many Tainted slices it adds, whether the Sacrifice slice is added simultaneously, and what happens if your wheel is already 24/24. memcite

## S8-M0271 - user - 2026-10-03T14:40:00.080000+00:00
Message ID: f24da1f2-ddb0-4f9a-83e9-448868f9470e

lets lock this in&#x20;
Defying Fate for the first time permanently adds exactly one Sacrifice slice to the Master Fate Wheel. It occupies a real wheel slot, has fixed normal weight, cannot be manipulated or cleansed, and remains for the rest of the run. Landing on it generates a mandatory Death's Coffer sacrifice that must be fulfilled before Wheelbound progression can continue.

wait for this answer
**What happens to the Master Wheel the very first time you press `DEFY FATE`?**

## S8-M0272 - assistant - 2026-10-03T14:40:01.840993+00:00
Message ID: 38c08f7e-a458-4c52-a1f1-a8db08e5b30a

**CONFIRMED — Sacrifice Slice locked exactly as written:**

> **Defying Fate for the first time permanently adds exactly one Sacrifice slice to the Master Fate Wheel. It occupies a real wheel slot, has fixed normal weight, cannot be manipulated or cleansed, and remains for the rest of the run. Landing on it generates a mandatory Death's Coffer sacrifice that must be fulfilled before Wheelbound progression can continue.**

We'll leave the exact sacrifice-selection and Death's Coffer verification mechanics for their own pass.

And I'll hold here. The next question remains:

**What happens to the Master Wheel the very first time you press `DEFY FATE`?** memcite

## S8-M0273 - user - 2026-10-03T14:42:53.715000+00:00
Message ID: ee3b891d-911b-4f9c-93c7-244fd4e32f90

So when you defy fate the first time, you've run out of spins, okay? So there should be something where maybe it's like a pulsing button on the side of the UI that says, like, Defy Fate. Like, Defy Fate. And it's got, like, a wheel. You know, it's, like, got, like, an animated wheel or something. It's, like, Defy Fate. Basically, it gives you, like, you've run out of spins. You know, now you defy fate, and then you defy fate and, like, a really neat animation plays and, you know, maybe it just basically tells you your situation. You've run out of spins. We'll give you back your spins. You can either complete your goal as you are now, or you can taint your wheel. Basically, you know, it would be, like, you get these taint slices. These taint slices, you know, come with some pretty heavy challenges, and then a sacrifice slice, which is exactly what we talked about, which is, you know, you will get a permanent sacrifice slice, which if you land on it, you gotta offer something to Descoffer, that sort of thing. So, like, it explains, you know, you're not screwed. You can get more stuff back. The tainted slices don't give you spins back. Sacrificing does. Whenever you defy fate for the first time, we give, like, a pretty hefty amount of spins back, you know, that sort of thing. And then you can also optionally voluntarily sacrifice items as you need. So, like, not only will there be a forced option, like if you spin the wheel, you have to do it, but, like, if you're running low, you can now sacrifice. So maybe it also explains that, is, like, now you have the option to, like, sacrifice items for spins. And maybe there's, like, a value spin associated with that. Maybe, like, a 100 GP item is, like, two spins or something. And it demands more as you run out. But if you run out more— if you run out of spins more and more, it just adds more taint to your wheel versus anything else. Now, there is a situation where it becomes so tainted that you're just fucked. And I don't know what to say to those people because they've let it spin out of control so often. I don't want to, like, bail them out because then, like, what's the point of the whole system? What are your thoughts?

## S8-M0274 - assistant - 2026-10-03T14:42:56.491787+00:00
Message ID: 71b05e66-7276-4415-92bd-4741a5f0e522

Yes. I think this gives Defy Fate a much clearer purpose:

> **Defy Fate isn't another progression system. It's the recovery system for a run that failed to reach its Grand Fate within the original spin allowance—and every time you rely on it, the run gets worse.**

I especially like separating **Taint** from **Sacrifice**. Taint is the punishment for repeatedly running dry; Sacrifice is how you can actively buy more spins once you've crossed that line.

For the **first Defy Fate specifically**, I'd structure the event like this:

```text
0 SPINS REMAIN

The Master Wheel becomes inactive.

        ↓

☠ DEFY FATE ☠
[pulsing / animated]

        ↓

"You have exhausted the Fate allotted to you."

        ↓

Player can attempt Grand Fate
if they're ready.

OR

DEFY FATE
```

Choosing Defy Fate triggers the big one-time presentation explaining the consequences.

```text
YOU HAVE DEFIED FATE

Fate grants you another chance...

+ [HEFTY AMOUNT] SPINS

But your path has been corrupted.

+ TAINTED SLICE(S)
+ PERMANENT SACRIFICE SLICE

SACRIFICE IS NOW UNLOCKED

You may voluntarily offer valuables
to Death in exchange for additional spins.

But beware...

Should your borrowed Fate run dry again,
the corruption will spread.
```

Exact number of restored spins and initial Tainted slices = `BALANCE_TBD`.

### The part I really like: subsequent failures

I **would not give another huge free spin refill every time.**

That would undermine the entire system.

The first Defy is essentially:

> *You screwed up. Here's your second chance.*

After that, you're living on borrowed time.

You've unlocked voluntary Death's Coffer sacrifices, so **that's now your renewable source of spins**.

If you run all the way to zero again:

```text
SPINS: 0
   ↓
DEFY FATE AGAIN
   ↓
+ MORE TAINT
   ↓
Need spins?
Sacrifice valuables to Death.
```

That creates a brutal but fair feedback loop:

**Bad wheel / inefficient progression → run out of spins → Defy → more Taint → worse wheel → potentially need more spins → sacrifice wealth → continue.**

And critically, the game warned you.

### I think there SHOULD be a point of no return

I agree with you here. We shouldn't secretly guarantee that every Wheelbound run can recover forever.

Otherwise the Grand Fate isn't actually threatening.

But I wouldn't make it an arbitrary:

> "You Defied Fate 5 times. Game over."

I'd let the **24-slot wheel system create the natural failure state.**

Every subsequent Defy adds more permanent/semi-permanent corruption. Eventually someone who keeps failing might have something horrific like:

```text
MASTER FATE WHEEL — 24/24

Mining
Fishing
Questing
Bossing
Slayer
Prayer
Crafting
Combat

☠ Sacrifice
☣ Tainted
☣ Tainted
☣ Tainted
☣ Tainted
☣ Tainted
☣ Tainted
☣ Tainted
☣ Tainted
...
```

At that point, I don't think Wheelbound needs to save them.

**They can still technically play. Their run has just become awful.**

And that's better than a literal game-over screen.

They could potentially grind through terrible Tainted Fates, sacrifice increasingly valuable possessions for spins, cleanse Taint through whatever limited cleansing mechanic we eventually design, and desperately try to reach their Grand Fate.

That sounds *exactly* like a run that has spiraled out of control after repeatedly Defying Fate.

### One thing I'd change from what you said

I would **not make voluntary sacrifice cost escalate merely because you've run out of spins multiple times**, at least initially.

We already have an escalation mechanic:

> **More Defiance = more Tainted slices.**

If Death also demands exponentially more GP each time, we're double-punishing the same failure and making balance much harder.

Instead, I'd make the exchange rate itself consistent:

> **Sacrifice value → spins**

The exact conversion is `BALANCE_TBD`.

And I wouldn't do something as low as `100 GP = 2 spins`; that would make sacrifices meaningless once someone has any wealth. But that's just balance—we'll determine the actual curve later.

So I'd lock the **first Defy Fate behavior** as:

> **The first time a player reaches zero spins, they may attempt their Grand Fate or Defy Fate. The first Defy grants a substantial one-time refill of spins, adds Tainted slice(s), permanently adds the single Sacrifice slice, and permanently unlocks voluntary Death's Coffer sacrifices as a method of obtaining additional spins. Future spin exhaustion does not provide the same large free refill; further Defiance primarily increases Taint, while sacrifices become the player's renewable source of additional spins. Repeated Defiance can corrupt the Master Wheel severely enough that recovery becomes extremely difficult, and Wheelbound does not guarantee recovery from a catastrophically mismanaged run.**

I think that's a **very strong core rule**. I wouldn't go farther yet—we should lock/change that before deciding exactly **what happens on the second, third, fourth, etc. Defy**, because that's the next individual decision. memcite

## S8-M0275 - user - 2026-10-03T14:43:32.638000+00:00
Message ID: 77d8c8bc-2235-4075-9d95-45da1c335dac

yep lock that in

## S8-M0276 - assistant - 2026-10-03T14:43:35.495029+00:00
Message ID: 6c6b86c5-598d-44e0-8cf0-9409c0f1b746

Locked. **First Defy Fate behavior is CONFIRMED exactly as described.**

Key rule going forward: the **first Defy is the major second chance**—substantial spin refill + Taint + permanent Sacrifice slice + voluntary sacrifices unlocked. After that, the system stops bailing the player out; additional Defiance primarily makes the wheel increasingly corrupted, while Death's Coffer sacrifices become the renewable way to acquire spins.

And importantly, **Wheelbound does not guarantee that a catastrophically mismanaged run remains reasonably recoverable.** That's part of the risk of repeatedly Defying Fate.

Next, one question at a time:

**What happens on the second Defy Fate?**

That's where we should establish the pattern for how additional Taint enters the wheel. memcite

## S8-M0277 - user - 2026-10-03T14:44:22.329000+00:00
Message ID: b4e24fe0-ccfe-4bc9-9d9b-a878f1e68631

See, this is the thing. If you, so the whole, like, defying fate happens when you run out of spin. That's likely, you know, like you fucked up, you're defying fate, that sort of thing. When you do it the second time, I think additional taint will just enter the wheel. I can't really think of anything more. Maybe the challenge gets harder. Maybe that sacrifice slice grows by, you know, 0.25. So, like, each time you defy fate, you'll get some additional taint, and you'll get that four sacrifice slice will grow bigger. Let's lock that in.

## S8-M0278 - assistant - 2026-10-03T14:44:24.990796+00:00
Message ID: 3396ea47-5484-4911-b339-c88a4dffac13

Locked. I like that escalation because it uses the **wheel itself to show how badly you've defied Fate**, rather than layering on arbitrary penalties.

### CONFIRMED — Repeated Defy Fate

After the first Defy Fate, **every subsequent time the player reaches 0 spins and Defies Fate again:**

- Additional **Tainted slice(s)** enter the Master Fate Wheel.
- The existing permanent **Sacrifice slice increases its weight by +0.25x**.
- There is still only **one Sacrifice slice**; it physically grows on the wheel as its probability increases.
- The player cannot reduce/manipulate that Sacrifice weight through Wheel Builder.
- Tainted Fates can become **more severe as the player's Defy count increases**.
- Voluntary Death's Coffer sacrifice remains the renewable source of spins.

So the Sacrifice progression looks like:

```text
Original Wheel       No Sacrifice
1st Defy             Sacrifice = 1.00x
2nd Defy             Sacrifice = 1.25x
3rd Defy             Sacrifice = 1.50x
4th Defy             Sacrifice = 1.75x
5th Defy             Sacrifice = 2.00x
...
```

That also modifies one small piece of our previous lock: the Sacrifice slice has a **fixed base weight of 1.0x that the player cannot manipulate**, but **Defying Fate itself permanently increases that weight by +0.25x each subsequent Defy**.

I particularly like the visual consequence. A player who's repeatedly run dry can literally look at their Master Wheel and see the Sacrifice section getting larger while Taint spreads through the wheel.

**Next single question:** we should decide exactly **how Tainted slices get added when you Defy Fate**—particularly how many are added per Defy and what happens when the Master Wheel is already at its 24-slot limit. memcite

## S8-M0279 - user - 2026-10-03T14:46:04.606000+00:00
Message ID: 79aab9ce-38c2-4be9-b7f0-76d44871948f

All right. So if there's open slots, it'll just go in. If there's 24 slots and it's at its limit, or, you know, whatever we decide the limit to be. Because, like once again, with the way that we've building the wheel, 24 might be a bit much. It might make some more sense to narrow that down. But basically, if you're at max wheel capacity, it will, you will... it'll bring up the wheel builder, and you have to remove a slice so that taint can go in. You will be, you get to decide which that one is, because I think random is kind of bullshit. So you will have to do that one if you need to. That's how I would handle that. And, like, as you defy fate, you know, maybe not all the time it adds. Maybe the first time you defy fate, you get taint and a sacrifice. The second time you defy fate, the sacrifice just increases. The third time you defy fate, the sacrifice increases and you get a taint slice. You know, something like that. And then within the shop itself, there needs to be an item that's, you know, maybe equivalent to the fate's pardon, where, like, it's like the fate's cleansing, and you get to cleanse and remove a tainted slice on the wheel. That's how I would handle that.

## S8-M0280 - assistant - 2026-10-03T14:46:08.278548+00:00
Message ID: 53970ce1-e890-491e-80bd-f2faa2849ee5

Yes. I think that cleans up several things nicely. I'd lock most of what you just described, while leaving the **exact Taint cadence** for us to decide separately.

### CONFIRMED — Taint entering a full wheel

If the Master Wheel has an empty slot, the new Tainted slice simply occupies it.

If the wheel is at its maximum capacity:

```text id="04h49b"
DEFY FATE
    ↓
Taint must enter the wheel
    ↓
MASTER WHEEL FULL
    ↓
Wheel Builder opens
    ↓
"Fate demands room for corruption."
    ↓
PLAYER chooses one normal slice
    ↓
REMOVE
    ↓
TAINTED slice takes its place
```

**No random deletion.** I agree—that would feel cheap, especially if it randomly destroyed an important slice the player had deliberately built around.

The removed Fate remains **unlocked**; it's simply removed from the active Master Wheel. The player could potentially add it again later if they free another slot.

And we're no longer treating **24** as sacred. That's the current working capacity, but we'll determine the final maximum during UI design/playtesting/simulation.

### CONFIRMED — Fate's Cleansing

I also like mirroring Fate's Pardon:

**Fate's Cleansing** becomes another extremely expensive item in the Wheelbound shop.

Using one:

```text id="04dij6"
FATE'S CLEANSING
        ↓
Select one Tainted Slice
        ↓
TAINTED SLICE REMOVED
        ↓
Empty wheel slot
```

It does **not** automatically restore whatever slice was displaced.

And critically:

> **Fate's Cleansing cannot affect the Sacrifice slice or reduce its accumulated weight.**

So even someone wealthy enough in FP to eventually cleanse all their Taint still carries the permanent scar of having Defied Fate: **Sacrifice remains.**

Like Fate's Pardon, I'd make Cleansing a **flat, very expensive FP price** rather than escalating it. Exact cost is `BALANCE_TBD`.

---

The one thing I **wouldn't lock yet** is your example cadence:

> 1st Defy → Taint + Sacrifice  
> 2nd → Sacrifice grows  
> 3rd → Sacrifice grows + Taint

I like that direction much more than automatically adding Taint every time. It gives us another tuning knob.

So that's our **next single decision**:

### How frequently should Defying Fate add another Tainted slice?

My initial instinct would actually be a predictable escalating pattern rather than randomness. For example:

```text id="rm4yjr"
DEFY #1  → +1 Taint + Sacrifice 1.00x
DEFY #2  →          Sacrifice 1.25x
DEFY #3  → +1 Taint + Sacrifice 1.50x
DEFY #4  →          Sacrifice 1.75x
DEFY #5  → +1 Taint + Sacrifice 2.00x
...
```

So **every odd-numbered Defy adds one Tainted slice**, while every Defy after the first grows Sacrifice by +0.25x.

Simple, predictable, easy to explain, and increasingly nasty without filling the wheel with Taint too quickly.

That's the next piece I'd decide. memcite

## S8-M0281 - user - 2026-10-03T14:46:54.481000+00:00
Message ID: 2be3a12f-407a-494a-81f9-62b0394d7f52

Every other Defy. Um, hopefully, like, the Defying Fate is, like, supposed to be, uh, like you, you should only really defy fate, like, a couple times. Um, so just like every other one.

## S8-M0282 - assistant - 2026-10-03T14:46:56.359067+00:00
Message ID: fbf13f0a-143e-40ce-935a-6db0e6f57a9f

Yep. **Locked: Taint is added every other Defy Fate.**

So the progression is:

```text
DEFY #1 → +1 Taint + Sacrifice appears at 1.00x
DEFY #2 →             Sacrifice grows to 1.25x
DEFY #3 → +1 Taint + Sacrifice grows to 1.50x
DEFY #4 →             Sacrifice grows to 1.75x
DEFY #5 → +1 Taint + Sacrifice grows to 2.00x
DEFY #6 →             Sacrifice grows to 2.25x
...
```

The intention is **not** that a normal Wheelbound run should reach Defy #6, #8, #10, etc. Defying Fate should feel like emergency recovery. Once or twice is understandable; repeatedly doing it means the run is starting to spiral.

Also locked from the previous decision: if Taint needs to enter a full wheel, the player chooses which normal slice to remove through Wheel Builder. And **Fate's Cleansing** is an expensive fixed-price shop item that removes one Tainted slice but can never remove or shrink Sacrifice.

### Next question

I think we should now define **what actually happens when you land on a Tainted slice**.

We already know it leads to the **Punishment Wheel** with dynamically generated awful tasks. The next thing I'd decide is:

**Does completing a Tainted Fate give you any reward, or is completing it purely the price you pay for landing on Taint?** memcite

## S8-M0283 - user - 2026-10-03T14:47:34.529000+00:00
Message ID: 33aa38f1-0c4f-4c4e-b015-8beed6c48a74

Uh, the price you pay for landing on taint. Um, it should probably give you some, but it's more like an insult. So, like, you know, you're gonna do this really hard challenge and you're gonna be paid dirt for it. So, like, a really low amount of fate points, but you still get something.

## S8-M0284 - assistant - 2026-10-03T14:47:38.444339+00:00
Message ID: fba4826a-72ea-489e-99b7-d9b8779c125f

Exactly. **Locked.**

### CONFIRMED — Tainted Fate rewards

Completing a Tainted Fate **does award Fate Points**, but intentionally very few.

The philosophy is:

> **The reward should feel almost insulting compared with what the player just had to do.**

So you might get something like:

```text id="4dzrzu"
TAINTED FATE COMPLETE

Mine 750 Runite Ore
using a Bronze Pickaxe

Reward:
+5 Fate Points

"Fate acknowledges your suffering."
```

Numbers are obviously `BALANCE_TBD`, but importantly, **Tainted Fate rewards do not scale proportionally with difficulty** like normal Fates.

A brutal punishment shouldn't secretly become a good FP farming strategy. The tiny reward is basically acknowledgment that you completed it.

This also means players should **never want to land on Taint for economic reasons**.

### Next question

Now I'd tackle the actual **Punishment Wheel**.

We previously envisioned roughly **12 punishments appearing on the wheel, dynamically selected from a much larger library of ~50 templates** based on what the account can actually complete.

The first thing I'd decide is:

**Should every Tainted Fate be roughly equally awful, or should the Punishment Wheel contain different severity levels—some "ugh, annoying" and some "oh fuck me" outcomes?** memcite

## S8-M0285 - user - 2026-10-03T14:49:13.971000+00:00
Message ID: 8b05266d-e09d-4e90-b616-71ad611546ac

A mixture. The punishment wheels could be, you know, it needs to be bad, right? But, you know, maybe some are like, ah, it's not that bad. It's still bad, it's not that bad. Or some are, like, really bad. And it's kind of tough to determine, like, what is and what isn't bad. It's really subjective, but there are some pretty universally accepted things that are bad. Maybe, like forcing you to do a mini game or, you know, everyone doesn't— like agility is probably not a really liked skill. Mastering mixology is considered to be a really awful skill for Herblore. Once again, some of these things have, like, requirements. We need to make sure that they could do whatever the awful requirement is. At the bare minimum, it should be just, like, get, you know, a high-level rock with a low-level pick and get, like, a lot of it or something. What do you have for ideas for the tainted? Yeah, exactly. Like your plain text. I was reading your plain text thing up here. It's just a fate of suffering. Something awful.

## S8-M0286 - assistant - 2026-10-03T14:49:19.321813+00:00
Message ID: c3883b75-61a3-4371-bca1-945d5a2e6b5c

Exactly. I think **“Fate of Suffering”** is a great internal name for the generated Tainted Fate.

And I would *not* try to make every punishment mathematically equal. Part of the dread should be watching that Punishment Wheel and thinking *“anything but that one.”*

I'd build the punishment library around several archetypes:

- **Tedious volume:** Do an obnoxiously large amount of something normally tolerable. Example: 500 laps of an eligible Agility course, mine 1,000 ore, chop 1,500 logs, kill 2,500 low-level monsters.
- **Terrible equipment:** Do normal/high-level content with deliberately awful-but-valid gear. Mine Runite with a bronze pickaxe, chop high-level logs with a steel axe, kill X monsters with a terrible weapon. The generator verifies the equipment can actually perform the task.
- **Unpopular activities/minigames:** Mastering Mixology, Mage Training Arena, Agility courses, Pyramid Plunder, Fishing Trawler, etc. Only activities the account can currently access.
- **Monotony:** Kill something stupid. `Kill 5,000 Goblins.` `Kill 2,000 Men/Women.` `Kill 1,000 cows.` Not difficult—just *why are you making me do this?*
- **Resource suffering:** Gather/process huge quantities using inefficient methods. Smelt a ridiculous number of bars manually, make thousands of a low-value item, cook enormous quantities of basic food, etc.
- **Travel/area restrictions:** Complete a substantial task while confined to an eligible region or using a particularly inconvenient method. These need stronger technical validation.
- **Boss/PvM suffering:** If the account has appropriate boss access, something like a large KC requirement or deliberately restricted setup. These need careful validation so we don't create impossible combat assignments.
- **Jackpot misery:** A small number of exceptionally awful outcomes. These are the ones where the player watches the pointer approach and goes *“no no no no—FUCK.”* Think extremely long deterministic grinds rather than RNG grinds.

And importantly, the 12-wheel generation shouldn't necessarily be:

> 4 easy + 4 medium + 4 horrible.

I'd rather have **weighted severity**. Most eligible results are normal Tainted misery, a few are relatively merciful, and a few are horrific.

For example:

```text id="60ujrv"
☠ PUNISHMENT WHEEL ☠

1.  Mine 750 Runite Ore — Bronze Pickaxe
2.  Complete 100 eligible Agility laps
3.  Kill 2,500 Goblins
4.  Earn X points at Mastering Mixology
5.  Chop 1,000 Yew Logs — Steel Axe
6.  Complete X rounds of an eligible minigame
7.  Kill X of an eligible Boss
8.  Smith 2,000 low-value items
9.  Complete a long eligible Thieving grind
10. Catch 1,500 eligible fish using an inefficient method
11. Complete X Mage Training Arena objectives
12. ☠ A particularly horrible account-appropriate grind
```

Those numbers are examples, **not locked**.

The most important rule I'd lock isn't any particular punishment. It's the generator rule:

> **Every Fate of Suffering must have a deterministic path to completion and must be verified as currently possible for that account before it can appear on the Punishment Wheel.**

So **no**:

> Obtain a 1/5,000 drop.

Because someone could theoretically never finish it.

But:

> Kill 5,000 Goblins.

Absolutely. It's horrible, but there is a guaranteed finish line.

And I think the generator should intentionally consider the account when creating the misery. A level-40 account and a maxed account shouldn't receive the same punishment pool. Higher-level accounts give us permission to generate much nastier tasks because they have access to more content.

One other design choice I'd make: **don't label severity on the wheel.** No `MILD / SEVERE / EXTREME`.

Just show the actual punishments.

The player can look around the wheel themselves and go:

> “Okay... 1,000 yews isn't *that* bad... please don't land on Mixology.”

That subjective judgment is actually part of what makes the Punishment Wheel fun.

**Next single decision:** I think we need to decide whether landing on Taint immediately locks in the **12 generated punishments**, or whether the player gets to see the Punishment Wheel and its 12 outcomes **before they commit to spinning it**. My inclination is that once the Master Wheel lands on Taint, **you're already committed**—the Punishment Wheel appears, the 12 outcomes are generated and locked, and you're spinning it. There's no backing out. memcite

## S8-M0287 - user - 2026-10-03T14:52:27.939000+00:00
Message ID: 41ac146b-b26e-4b1d-b4ef-94dc9dd655ca

I don't know if you can do this, but there's also, like, you can, like, I like the idea of, like, killing, like, 2,000 men, kill, like, 2,000 women, or, you know, stuff like that. I think that's really cool. There's also just, like, creatures that are just, like, what's their point in the game? Like creatures that are found, like, only in, like, a specific cave, you know? Like, just, like, an awful area. You know, maybe kill, kill 1,000 cave bugs. You know, the newest addition that I can think of is they just recently added the addition of the elemental amulet, and one of the things you have to kill was, like, a mollusk for the water amulet. I've never heard of those in my life, but those were really annoying to kill. Kill some of those. So stuff that is just, like, annoying, like lizards and slayer, slayer, or capturing birds. Birds are really annoying to, like, set up, you know, some bird things. And then, obviously while you're doing, you know, any of this, we have that skill tolerance thing. The skill tolerance bar should really only be presented whenever you're doing a skilling fate. So whenever you're doing, like, a bossing fate or maybe a punishment fate, the skill tolerance doesn't really apply here. So technically, technically, technically, if they wanted to cheese it, I guess they could, as they're doing, like, a punishment fate, go and train something else, do their punishment, and then come back. That's a possibility. I don't know what we want to do there, but I don't want the tolerance to be there all the time, I guess you could say. Maybe, maybe, like, re-record that and be like, you know, we noticed that you were doing something not aligned with your tasks, and therefore you will be punished again. Something like that. So something where it's just like, oh, like during these other tasks, if we see them gathering, like maybe it's like a hidden tolerance bar with, like, a little more tolerance. But basically, if they're doing something outside of whatever the normal task is, we aren't going to necessarily punish them or fail the task for that, but we will take note that the wheel is always watching, and now for gathering, doing other stuff outside what you were permitted, you will be punished again. Something like that.

## S8-M0288 - assistant - 2026-10-03T14:52:32.216131+00:00
Message ID: 01467a28-1615-440a-9971-df8f4ce6a362

Yes — I think there's a really good mechanic buried in that, and it solves the problem of **not wanting the normal 1,000-XP tolerance system constantly visible**.

First, on the punishments: absolutely. I want a whole category internally that's basically **“Forgotten Creatures.”** Cave bugs, obscure cave creatures, annoying Slayer monsters, weird Hunter creatures, inconvenient NPCs, Men/Women, etc. The point isn't difficulty; it's making the player say:

> *“Why the hell am I killing 1,500 of these?”*

That's perfect Tainted Fate material. We'll build the actual creature/activity library later rather than locking individual examples now.

For the XP issue, I think your **“the wheel is always watching”** idea is better than simply disabling restrictions.

### PROPOSED — Hidden Fate Monitoring

The visible **Unauthorized XP tolerance bar** belongs specifically to normal Skilling/Combat Fates where XP permissions are central to the objective.

For Fates such as Bossing, Tainted Fates, Sacrifice, and other special objectives, we don't clutter the UI with that bar.

But Wheelbound **still monitors unrelated progression in the background**.

Importantly, I'd make this more forgiving than the normal 1,000-XP rule. If you accidentally chop a tree, teleport somewhere, gain incidental XP, etc., nothing happens.

But if the plugin detects **meaningful deliberate progression unrelated to the active Fate**, it records a violation.

And I really like your consequence:

**It doesn't fail the current Fate. It creates another punishment.**

Something like:

```text id="rrw8e5"
FATE OF SUFFERING

Kill 1,000 Cave Bugs.

Meanwhile player goes and gains
a substantial amount of unrelated XP.

        ↓

No visible tolerance meter.
Current punishment continues.

        ↓

☠ FATE HAS TAKEN NOTICE ☠

"You sought progress beyond
the path Fate gave you."

        ↓

Current punishment completed.

        ↓

FATE DEMANDS PENANCE

        ↓

Another Punishment Wheel spin.
```

That's *way* more thematic than deducting 20 FP.

And there's a nasty consequence to cheesing:

> You **can** go train something else during your 2,000-Men punishment.

The plugin isn't physically preventing you.

But if you cross the hidden allowance, you're accumulating **Penance**.

Finish the punishment?

Great.

**Spin again.**

### One thing I'd change

I wouldn't secretly accumulate unlimited punishments without telling the player. That could feel unfair.

When they cross the hidden threshold, Wheelbound should immediately make it obvious:

> **☠ FATE HAS TAKEN NOTICE**  
> Unauthorized progression detected.  
> **Penance owed: 1**

The *meter* is hidden, but **the consequence isn't**.

Then if they continue:

> **Penance owed: 2**

Potentially they can dig themselves an absolutely ridiculous hole, which fits Wheelbound perfectly.

So we get two related systems:

```text id="zr78oa"
NORMAL SKILL/COMBAT FATE
Visible XP Tolerance
→ exceed it
→ normal Fate violation / FP penalty

SPECIAL/BOSS/TAINTED FATE
No visible tolerance meter
→ hidden generous monitoring
→ meaningful unrelated progression
→ PENANCE +1
→ additional Punishment Wheel spin
```

And permitted/by-product activity still exists. If you're killing 1,000 cave bugs and naturally gain combat/HP XP from killing them, **obviously that's permitted**. We're looking for progression that clearly has nothing to do with the assignment.

I'd also call the system **Penance** rather than another “violation.” It gives us nice UI language:

> **PENANCE OWED: 2**

> *Fate saw your transgression.*

> *Your task is complete. Your penance is not.*

I think that's worth locking.

But there's **one thing I wouldn't decide yet**: the hidden threshold. Whether it's XP, activity-specific, time-based, or some combination needs technical design and playtesting. We can simply establish that it's **significantly more forgiving than normal Fate tolerance and intended to detect deliberate off-task progression, not incidental gameplay.**

If you like that, I'd lock **Hidden Monitoring + Penance** as the rule, then we can return to finishing the actual Punishment Wheel behavior. memcite

## S8-M0289 - user - 2026-10-03T14:54:26.771000+00:00
Message ID: df68bea5-aa00-42b9-a177-f4fb4f86b4a9

No, I think, uh, I think everything should be judged at the end. Let them do exactly what they're doing, um, and that sort of thing. Now, whenever they're given a certain task, we got to take into account what kind of task we gave them. So, like a lot of these are like kill something. So if they're gaining a lot of combat XP, um, that's cool, we're okay with it. However, if they're killing other things, maybe that count towards the tolerance. So if it's like maybe the tolerance changes. So for skilling-based, we go by XP. If it's killing-based, we go by stuff that they've killed that's not permitted. So, and that would be visible, you know what I mean? So, and then, you know, maybe they're like, Oh, dude, we can be kind of sneaky with this. Like they're only like gonna restrict what stuff we can kill. I can go get XP from, like, woodcutting and stuff. That's when they can go do what they want, and then we go, Haha, gotcha. Like we're always watching. Don't do that kind of thing.

## S8-M0290 - assistant - 2026-10-03T14:54:31.655454+00:00
Message ID: b22d6541-94f8-49f9-ae6f-754879dd9513

Yes — I understand the distinction now. **Don't punish them when they cross the line. Let them think they got away with it.** Then evaluate everything when the Fate ends.

And the **visible tolerance should match the actual objective**, rather than always being XP.

### PROPOSED — Objective-specific tolerance

If the punishment is skilling-based:

```text
FATE OF SUFFERING
Mine 750 Runite Ore
using a Bronze Pickaxe

Visible tolerance:
Unauthorized XP: 0 / 1,000
```

Normal concept. Relevant Mining XP is permitted. Other XP consumes the visible tolerance according to the Fate's permission profile.

But suppose it's:

```text
FATE OF SUFFERING
Kill 2,000 Men

Progress: 743 / 2,000

Visible tolerance:
Unauthorized Kills: 2 / 25
```

Combat XP doesn't matter because **combat is how you're completing the punishment**.

Instead, Wheelbound tracks kills.

Kill a Man? Permitted.

Kill something incidental on the way? There's some tolerance.

Go kill 150 Gargoyles because you wanted to sneak in Slayer progression?

You're clearly outside your Fate.

### Then there's the second layer: **Fate is always watching**

This is the part I really like.

The visible restriction only tells you what the Fate **explicitly cares about**.

So the player sees:

> Unauthorized Kills: `3 / 25`

And thinks:

> *“Oh! It only tracks kills. I'll go Woodcut for six hours before finishing my 2,000 Men.”*

Wheelbound doesn't interrupt them.

No popup.  
No warning.  
No immediate penalty.

They finish Man #2,000.

Then:

```text
FATE OF SUFFERING
COMPLETE

2,000 / 2,000 Men slain.

...

Reviewing your Fate.
```

And then:

```text
☠ FATE WAS WATCHING ☠

While bound to your Fate,
you pursued progress elsewhere.

Woodcutting XP gained: 184,620
Fishing XP gained: 31,440

You thought Fate wouldn't notice.

PENANCE IS OWED.
```

**Punishment Wheel comes back.**

That's much better than what I proposed before.

### The important design rule

We'd essentially have **two validation layers**.

**Visible Fate restriction** is objective-specific:

> Skilling → unauthorized XP  
> Killing → unauthorized kills  
> Minigame → unauthorized activity/completions  
> Bossing → potentially unauthorized kills/boss activity  
> Equipment challenge → required equipment/method validation  
> etc.

Then there's a broader **background integrity check** that records obviously deliberate unrelated progression.

But—and this is important—we need to be generous with that second layer.

Walking somewhere, teleporting, getting incidental XP, killing something that attacks you, quest-required actions, and other normal collateral activity shouldn't trigger the gotcha.

We're trying to catch:

> *“I'm supposed to kill 2,000 Cave Bugs, but the visible counter only cares about kills, so I'm going to gain 400k Agility XP while this Fate is active.”*

**That's when Fate gets them at the end.**

And I wouldn't even call the background system a “hidden tolerance bar” in our design. I'd call it something like **Fate Audit**.

> **Every special Fate undergoes a Fate Audit upon completion.**

The player knows conceptually that **Fate is always watching**, but we don't tell them the exact internal thresholds. That prevents the game from becoming *“Okay, apparently I can safely gain exactly 14,999 Woodcutting XP.”*

That warning should probably be taught the **first time Taint is introduced**:

> **Fate is always watching. Attempting to exploit the rules of a punishment may incur further Penance when your Fate is judged.**

So it's sneaky and thematic, but **not unfair**. We told you not to fuck around; we just didn't tell you exactly how we're detecting it.

I think this is a substantially stronger system.

**Next single decision:** if the end-of-Fate Audit catches you deliberately progressing elsewhere, I think your punishment should be exactly what you suggested: **you owe another Punishment Wheel spin.** We just need to decide whether multiple kinds/amounts of cheating can ever stack into **multiple** Penance spins, or whether one Fate Audit can impose a maximum of **one additional punishment**. memcite

## S8-M0291 - user - 2026-10-03T14:57:14.896000+00:00
Message ID: 8e3091fd-d49a-4439-bfe7-f4d62afd849d

If they cheat once, shame on them, just a normal punishment. If they cheat again, we start upping the punishment. And, like, each time that, you know, maybe we catch them cheating, that's fine. You know, you know, ah, we got you. You know, this is your punishment. And this is only for subsequent cheats. So, like, if they cheat and they follow the next task, and then they do another task and then they cheat during that one, it'll reset. So it'll go, Aha, I got you again, but it won't escalate. So the only time that it'll escalate is whenever we catch them cheating multiple times in a row. So, like, you take a task, you cheat on it, and we go, Okay, here's another task. You cheated. If they cheat on that task, then the task, once they complete that one, we'll go, We caught you cheating again. Maybe you didn't get the message. This time it'll be worse. It'll keep saying stuff like that, or, like, and then basically it's just gonna give them, like, a really, like, get, like, 99. Basically, like, you messed up so bad, go and get 99, is what needs to happen. You know, something, like, extremely extreme that's gonna take them maybe days to complete, is what I'm thinking.

## S8-M0292 - assistant - 2026-10-03T14:57:17.054008+00:00
Message ID: 22717d24-beab-46c0-b806-3b6686e69f71

Yes. That gives us a clean **consecutive-cheating escalation system**, rather than permanently branding the account because they cheated once.

### CONFIRMED — Fate Audit & consecutive cheating

At the end of applicable Fates, Wheelbound performs the **Fate Audit**.

If the player deliberately progressed outside the permitted boundaries, Wheelbound catches them **after they finish**.

The important part is that we track a **consecutive violation streak**, not lifetime violations.

```text
Fate A → CHEATED
        ↓
"Fate caught you."
        ↓
Punishment
        ↓
Punishment completed legitimately
        ↓
STREAK RESET
```

Later they cheat again? That's just another **first offense**.

But:

```text
Fate A → CHEATED
        ↓
Punishment #1
        ↓
CHEATS DURING PUNISHMENT
        ↓
"We caught you again."
        ↓
Punishment #2 — MUCH WORSE
        ↓
CHEATS AGAIN
        ↓
"Apparently you still haven't learned."
        ↓
EXTREME PENANCE
```

So the player only enters the really horrible stuff by **repeatedly ignoring Fate after Fate has already punished them**.

### I really like the eventual "go get a 99" tier

That should be near the top of the escalation ladder.

Not necessarily literally:

> Get 99 Runecraft

if they're level 12—that could be absurd beyond what we're trying to accomplish.

Instead, the **Extreme Penance generator** can look at the account and generate something genuinely brutal but possible.

For example:

```text
☠ FATE'S WRATH ☠

You have defied your punishment
three times in succession.

Fate has had enough.

Reach Level 99 Fishing.

Reward:
+1 Fate Point
```

The `+1 FP` is especially funny to me given our **insultingly low Tainted rewards** rule.

Or for an account where a 99 isn't appropriate, it could generate another multi-day deterministic grind.

The philosophy becomes:

> **The first cheat is funny.**
>
> **The second is a warning.**
>
> **The third is "you clearly want to find out what happens."**

### One safeguard I'd add

Extreme Penance still follows our fundamental punishment rule:

**It must be possible.**

No RNG drop requirements. No inaccessible content. No impossible equipment. No “get 99” in something the account cannot currently train because of another Wheelbound restriction.

But **time-consuming is absolutely allowed**.

Days of work can be the punishment at that point because the player deliberately cheated, received a punishment, and then deliberately cheated **again**.

And the moment they complete one of these Fates properly:

**Consecutive Cheat Streak → 0.**

That part is important. Wheelbound holds a grudge only while you're actively refusing to listen.

### One thing I'd leave `BALANCE_TBD`

I wouldn't lock exactly:

`Cheat #1 = X severity`
`Cheat #2 = Y`
`Cheat #3 = 99`

yet.

Let's lock the **escalation mechanic**, then later build the severity pools and determine through testing how quickly it should reach the nuclear tier.

So I consider this locked:

> **Fate Audit violations escalate only when they occur consecutively. A detected violation generates Penance. If the player cheats during that Penance, the next punishment becomes substantially more severe, potentially escalating into Extreme Penance such as an account-appropriate level-99 or multi-day deterministic grind. Successfully completing the assigned Fate/Penance without another violation resets the consecutive-cheating streak completely.**

### Next question

I think we've gone far enough into anti-cheese for now.

We should return to the normal **Punishment Wheel** and answer the question we skipped:

**When the Master Wheel lands on Taint, are the ~12 punishments generated and immediately locked in, with the player forced to spin the Punishment Wheel—no backing out or rerolling?**

My recommendation is **yes**. Once that pointer lands on Taint, your Fate of Suffering has begun. memcite

## S8-M0293 - user - 2026-10-03T14:58:39.383000+00:00
Message ID: 5e8f1f94-d701-4dfc-b6ec-881ff6ac23fc

Yep, lock it. And then, like, the cheats is a very thing we can tweak. Obviously it's not the third time you cheat, go get 99. But it might be something more than that. Once again, once we're kind of done fleshing out a lot of these, like, little systems, I'm gonna let you kind of just decide what an appropriate number is for all this stuff. So, like, once you have, like, the grand scheme of, like, how many nodes there are, you know, when do we spin the wheel? What's a good sense of, like— what's a good balance of, like, completing a task, getting points, but, like, not getting too many points where you blow up the economy kind of kind of thing. So I don't want, you know, your points to feel meaningless at some point, but at the same time, like, I still want you to do the stuff that needs to happen. So I'm gonna let you balance all, like, the number stuff out.

## S8-M0294 - assistant - 2026-10-03T14:58:41.605046+00:00
Message ID: 1425e048-e875-45ae-bc13-453e5b0e5239

Yep. **Locked**, including the balance philosophy.

For the Punishment Wheel: once the Master Wheel lands on **Taint**, Wheelbound generates the eligible punishment set, locks those outcomes, and the player **must spin**. No backing out, regenerating the options, reopening the UI for a different set, or rerolling.

And for the numbers, I think that's the right way to approach this project. We should **stop trying to balance individual numbers in isolation while we're still inventing systems**.

We'll mark things like these as `BALANCE_TBD`: starting spins, first-Defy spin refill, FP earned per Fate, node costs, shop costs/brackets, GE cost and Fate requirement, Fate's Pardon/Cleansing prices, sacrifice value-to-spin conversion, punishment quantities, cheat-escalation thresholds, boss KC ranges, XP Fate ranges/modifiers, and so on.

Once the complete game loop is established, I'll balance it **as one economy**. The important target will be that FP remains scarce enough that choices matter throughout the run. A player shouldn't eventually reach a point where 500 FP is meaningless and they can buy everything without thinking.

We'll model the whole loop:

```text id="4fxlsf"
FATES
  ↓
TIME / EFFORT
  ↓
FATE POINTS
  ↓
┌─────────────────────────────┐
│ Skills / Bosses             │
│ Fate Manipulation           │
│ Wheel Builder               │
│ Shops                       │
│ Grand Exchange              │
│ Fate's Pardon / Cleansing   │
│ Other progression           │
└─────────────────────────────┘
  ↓
BETTER / DIFFERENT WHEEL
  ↓
MORE FATES
  ↓
GRAND FATE
```

Then simulation can answer questions like: *How many Fates before the average player can afford their first meaningful unlock? How much must they sacrifice to save for the GE? How often can they afford Cleansing? Does one bounty trivialize progression? Does FP become irrelevant in late game? How many spins does a reasonably built account need to become Grand-Fate-ready?*

I'll propose the actual numbers from that **whole-system model**, rather than asking you to pick `10 FP vs 15 FP` hundreds of times. Then you can adjust based on whether the resulting progression *feels* like Wheelbound.

And yes, same treatment for cheating escalation. **The behavior is locked; the thresholds aren't.** We're definitely not establishing something arbitrary like “third offense = 99.” I'll design the severity curve once we can see the entire punishment system.

### Next

I think the **normal Tainted Fate flow is now pretty solid**.

The next unresolved part I'd tackle is **voluntary sacrifice for spins**, because that's now a major part of the post-Defy economy.

The first question is simply:

**When a player voluntarily asks Death for more spins, should *they choose what item to sacrifice*, or should Fate inspect their bank and decide what Death demands?**

Based on what you described earlier, I think **Fate choosing is much more interesting**—but we should decide that before designing how candidate items/value tiers work. memcite

## S8-M0295 - user - 2026-10-03T15:02:23.132000+00:00
Message ID: 8fb73bac-47a1-4c9e-a266-527a5434a9d1

So two schools of thought. One is, and this can be, like, the process for sacrificing an item. I like the idea of, like, you crack open your bank, right? We take a good look of what you got, and then it picks, like, 12 items or something from your bank, and it spins. And whatever it lands on, you gotta give it. For stackable items, I don't know how to handle that. Because, like, obviously, it could land on, like, 12 garbage items, you know? And, like, that could get you some spins. So I don't know how that's gonna do. And also it's just, like, if it—you can't do something along the lines of, like, oh, it landed on, like, flax, flax, and, you know, you need to sacrifice, like, 100,000 GP worth of flax. You might only have 25 in your bank, so it's like now you gotta go get more flax. It's more of, like, you have the item in your bank, and you're sacrificing it. So maybe we do something where it's, like, a value-based. And, like, you know, as you progress throughout the game, maybe the tax bracket for what becomes eligible, maybe it's, like, anywhere from, like, 20 to, like, 100K. And then, like, as you progress through the game, maybe it becomes, like, 100K to 2 mil, 2 mil to 5 mil, something like that. Or, like, maybe zero. Maybe zero to 2 mil. Maybe, like, the low is just the low, the high is just the high. Basically, I want the situation where they're like, Oh no, I grinded really hard for this item, and now death is asking me to sacrifice it. So, you know, maybe we do just let them. Maybe it's just a per basis, and then the amount of spins you get from sacrificing an item is determined on the value. So, like, at the bare minimum, you just get, like, one spin, right? Is kind of the idea. You just get one spin if it's worth, like, under 100K. If it's worth above 100K, maybe you get two spins. Once again, maybe you can set this threshold of, like, what a spin should be worth GP-wise. But I kind of like that more, and maybe you just sacrifice one of it. So, like, if you do get an item that's not great, you know, good on you. You can, you know, self-sacrifice, that sort of thing. Maybe that's how we do it. What are your thoughts?

## S8-M0296 - assistant - 2026-10-03T15:02:26.214300+00:00
Message ID: 342b5c60-aea1-402a-b26b-4703a819373b

I think the cleanest version is actually to **separate forced Sacrifice from voluntary Sacrifice**, because they're serving two different purposes.

The forced Sacrifice should be scary and unpredictable. The voluntary sacrifice is an economic decision: *“I need more spins; what am I willing to burn?”*

### Forced Sacrifice — Death chooses

When you **land on the permanent Sacrifice slice**, you don't choose what you're giving up.

Your original bank-wheel idea is great here:

```text id="tixb67"
SACRIFICE SLICE
      ↓
"Death demands an offering."
      ↓
OPEN YOUR BANK
      ↓
Wheelbound snapshots eligible items
      ↓
12 ITEMS SELECTED
      ↓
☠ DEATH'S WHEEL ☠
      ↓
SPIN
      ↓
Selected item must be sacrificed
```

This creates exactly the moment you're looking for:

> *“Oh shit, my [valuable/important item] is on the wheel.”*

And sometimes Fate is merciful and it lands on something crappy.

That's good. **The randomness is the punishment.**

### Stackables — sacrifice the banked quantity

I think your instinct solves this too.

Don't say:

> Sacrifice 100,000 GP worth of flax.

Instead, the wheel selects the **bank entry that currently exists**.

If you have:

```text id="py8j71"
25 Flax
```

then the candidate is:

> **25 × Flax**

If you have:

> **7,842 × Flax**

then that's the candidate.

Death takes **the stack that existed when the sacrifice was generated**. We don't make the player acquire additional items just to destroy them.

For non-stackables, I'd start with **one copy** of the selected item rather than automatically deleting every duplicate. That's something we can tune if necessary.

### Spin reward should be based on sacrificed value

Then we calculate the actual value Death received.

Something conceptually like:

```text id="3b1fho"
< 100K       → 1 spin
100K–500K    → 2 spins
500K–2M      → 3 spins
2M–5M        → 4 spins
5M+          → 5+ spins
```

**Those numbers mean absolutely nothing right now**—`BALANCE_TBD`.

Later I'll derive a sensible **GP-per-spin economy** based on how valuable a Master Fate spin actually is.

That also means getting lucky matters.

Death might demand garbage:

> *“Holy shit. I lost 30k and got a spin. Nice.”*

Or:

> *“You have got to be fucking kidding me. I spent three hours getting that.”*

That's the emotional range we want.

### Voluntary Sacrifice — player chooses

This is where I'd *not* use the wheel.

Once you've Defied Fate, you permanently unlock:

> **OFFER SACRIFICE**

You choose an eligible item from your bank, Wheelbound shows what you'll receive **before you commit**, and then you offer it to Death.

```text id="p3u52t"
OFFER TO DEATH

Dragon item
Value: ~2,140,000 GP

Death offers:
+X Master Fate Spins

This item will be permanently lost.

[CANCEL]       [OFFER]
```

That gives the player an important strategic decision:

> *“I desperately need three more spins. What am I willing to destroy?”*

And because spins are based on value, you can't farm the system by feeding Death endless garbage for meaningful numbers of spins.

### One thing I'd avoid

I wouldn't make Death's candidate-value range automatically become:

> Early game: 20k–100k  
> Midgame: 100k–2m  
> Late game: 2m–5m

That starts feeling artificially gamey.

I'd rather let the **bank itself determine the stakes**.

The forced Sacrifice generator can create a reasonably diverse candidate set from what you actually own—some lower-value items, some middle-value items, some of your valuable possessions.

That means your bank tells the story.

If you're poor, Death can't demand your 100m item because you don't have one.

If you're rich and carrying a bunch of valuable gear...

**Death suddenly has much more interesting things to put on his wheel.**

And crucially, I don't think the forced wheel should simply select the 12 most expensive items. Then it's predictably horrible rather than suspenseful.

We can eventually create an algorithm that deliberately builds a **mixed-value 12-item wheel**.

---

So I'd propose we lock this distinction:

> **Landing on the permanent Sacrifice slice triggers a forced Death's Wheel. Wheelbound snapshots the player's eligible bank contents, selects a diverse set of owned items/stacks, and randomly determines the demanded sacrifice. The player must sacrifice the selected possession before Wheelbound progression can continue.**
>
> **After Defying Fate, the player also permanently unlocks voluntary sacrifice. Here, the player chooses what eligible possession to offer. The value actually sacrificed determines how many Master Fate spins Death grants. Exact GP-to-spin brackets are `BALANCE_TBD`.**

I think having **Death choose when you're being punished, but letting you choose when you're asking Death for help** makes both systems much more distinct. memcite

## S8-M0297 - user - 2026-10-03T15:05:04.187000+00:00
Message ID: d28181ff-a98b-47c5-9189-534eb7f990ab

I like the idea of the forced sacrifice. I think stacks are just, you know. I like, that's pretty brutal, just to have, like, if you are hoarding a stack of an item, they're all gone. Very, very brutal. I like that. Let's lock the forced sacrifice in. The voluntary sacrifice is a little better, where it's, it's almost like, why bother to spin when you could just offer a— oh, this is where you would not use the wheel. I see, I see, I see. So you can sacrifice something and it'll just give you spins. All right, I like that. Let's lock that one in. So the voluntary sacrifice will be just something that you do voluntary. Yeah, and you can determine what you want to do, and it'll tell you how many spins you get. I like that. And then that way it's something where you're like, I'm getting close to defying fate. I don't want my forced sacrifice to go up. I don't want another taint. I need to get rid of some stuff. Let's go ahead and do that. And then if they're like, I can't spare anything, like I kind of need a lot of my stuff. This is what I got.

## S8-M0298 - assistant - 2026-10-03T15:05:08.028482+00:00
Message ID: c66ca4ed-3c80-4bfa-9a38-d3bfc29b1da8

Exactly. That creates a really good pressure point as the player approaches zero spins.

**Both systems are locked.**

### CONFIRMED — Forced Sacrifice

Landing on the permanent **Sacrifice Slice** triggers Death's forced sacrifice process.

Wheelbound has the player open their bank, snapshots their eligible possessions, selects a diverse group of roughly 12 candidates, and creates **Death's Wheel**.

Death chooses. The player does not.

Most importantly, for stackable items:

> **The entire snapshotted stack is the sacrifice.**

If Death's Wheel contains:

```text id="qg46t3"
12,482 × Sharks
3,400 × Yew Logs
1 × Valuable Weapon
28,000 × Cannonballs
...
```

and it lands on the cannonballs?

**All 28,000 are demanded.**

Brutal, but that's exactly what the forced Sacrifice is supposed to be.

Once Death selects the possession, Wheelbound progression is locked until the required sacrifice is completed and verified.

The sacrifice also awards spins based on the value actually destroyed. Exact conversion is `BALANCE_TBD`.

### CONFIRMED — Voluntary Sacrifice

Once the player has Defied Fate for the first time, **Voluntary Sacrifice permanently unlocks**.

No wheel. No randomness.

The player chooses an eligible possession, and **before committing**, Wheelbound tells them exactly how many spins they'll receive.

```text id="8du52s"
VOLUNTARY SACRIFICE

Offer:
1 × [Item]

Estimated sacrifice value:
4,200,000 GP

You will receive:
+X Master Fate Spins

This offering cannot be recovered.

[CANCEL]        [OFFER TO DEATH]
```

For stacks, the player should be able to choose the **quantity** they're voluntarily sacrificing. Forced Sacrifice takes the whole snapshotted stack; voluntary sacrifice shouldn't.

And the strategic interaction you just described is probably the best part:

```text id="w7b2eq"
3 SPINS LEFT

"I might not make it."

        ↓

OPTION A
Sacrifice some of my wealth now
→ Gain spins
→ Avoid reaching zero

        OR

OPTION B
Keep my possessions
→ Risk reaching 0 spins
→ Defy Fate again
→ Sacrifice weight +0.25x
→ Maybe another Tainted Slice
```

So **voluntary sacrifice is effectively insurance against having to Defy Fate again.**

That gives wealth a completely different meaning in Wheelbound. An item isn't merely gear or GP anymore. Late in a struggling run, the player may look through their bank and think:

> *“I really don't want to lose this... but I need four more spins.”*

And if they genuinely can't spare anything?

Then that's exactly when **Defy Fate starts looking tempting again**.

I think that's a very strong loop.

The next thing I'd tackle is **how Death's forced 12-item candidate wheel is constructed**. That's important because we need it to feel scary and fair—not twelve pieces of garbage, but also not simply “here are your twelve most expensive possessions.” memcite

## S8-M0299 - user - 2026-10-03T15:06:52.349000+00:00
Message ID: 5c292e68-1aeb-4c39-b978-3c19b878daa8

Exactly. I don't know if we want to put, like, a value limit, like, 100k above, 100k and above. I think 100k and above would probably hurt for most, like, early to mid-game. So I think that's a good place to start. Now, it obviously is going to suck, like if you are pretty early game and, like, you get a really good item. Typically, all right, so the sacrifice kicks in after you've blown through a lot of your spins. So theoretically, you should be far along in the game. You should have some stuff. But once again, maybe you only have one really good item, and then it's that item and everything else. Maybe it tries to get 12 items above 100k, and if it can't, it'll pick whatever items are eligible, and then it'll just go down in value from there. I think that's a fair thing. I think there's also the concept of what you have on your person and in your inventory. So that stuff I don't know if we can see just off rip, but that's kind of why we also need to open the bank. So it's like it takes into account what you're wearing as well as what's in your bank.

## S8-M0300 - assistant - 2026-10-03T15:06:55.473798+00:00
Message ID: 49318252-a6d6-4e5e-8789-19cf8c9f6792

Yes, I think that's a fair way to construct it, especially because **forced Sacrifice only exists after the player has already exhausted their original spin allocation and Defied Fate**. We're not throwing this at a fresh level-3 account.

I'd lock the **design behavior**, but leave the `100k` threshold as `BALANCE_TBD` until we see the economy.

### CONFIRMED — Death's Wheel candidate selection

When the Sacrifice Slice lands, Wheelbound asks the player to **open their bank**. This is the explicit moment where we establish the player's available possessions.

The sacrifice pool should include eligible possessions from:

> **Bank + Inventory + Equipped Gear**

Then Death attempts to construct a **12-slot Sacrifice Wheel**.

The selection logic is approximately:

```text id="o96p8p"
DEATH DEMANDS A SACRIFICE

Open your bank...

        ↓

Snapshot:
• Bank
• Inventory
• Equipped gear

        ↓

Find eligible possessions
worth ≥ VALUE FLOOR

Working value floor: ~100K
(BALANCE_TBD)

        ↓

12+ eligible possessions?
        │
       YES
        ↓
Select 12 from eligible pool

        │
       NO
        ↓
Take everything eligible above floor
        ↓
Fill remaining wheel slots by
working downward through lower-value
eligible possessions
        ↓
12 candidates
```

That means we **don't protect the player's best item just because it's disproportionately valuable**.

If you've got one 20m item and eleven things around 100–300k, that 20m item can absolutely appear.

That's the scary part:

> *“Fuck. That's on there.”*

But it's still only **one of the twelve outcomes**, rather than Death automatically taking your most valuable possession.

### Stackables stay brutal

And we're keeping the rule we just established:

**A stack is one candidate possession, and Forced Sacrifice takes the entire snapshotted stack.**

So candidate value needs to use the **whole stack's value**, not unit price.

For example:

```text id="e4d4h8"
1 × Item               = 4.2m
8,000 × Item @ 500 gp  = 4.0m
1 × Item               = 350k
2,700 × Item @ 100 gp  = 270k
```

All four could qualify.

That means hoarding massive stacks can become dangerous after Defying Fate, which I actually really like.

### One thing I'd be careful about

We shouldn't define "eligible" as literally **every item RuneLite can see**.

There will need to be an exclusion system for things that can't actually be sacrificed to Death's Coffer, items with weird/no meaningful value, protected/account-critical items, technical oddities, etc.

That's an implementation/data problem we'll research later.

But the design rule can simply be:

> **Only possessions that Wheelbound has verified are valid Death's Coffer sacrifices may enter Death's Wheel.**

And yes, we'll need to technically verify whether we can reliably capture **bank + inventory + equipment** and subsequently verify the sacrifice. That's a later RuneLite feasibility pass rather than something we should guess about now.

So I think the **candidate-selection philosophy is locked**, with the exact value floor and selection algorithm left for balancing.

### Next question

There's one important Sacrifice question left:

**If Death selects a 20m item, should sacrificing that forced item also give you substantially more spins than losing a 100k item?**

My inclination is **yes**. Forced Sacrifice should hurt, but Death should still honor the same general value→spins economy. Getting unlucky and losing something enormously valuable should at least leave you with a substantial spin reserve afterward. memcite

## S8-M0301 - user - 2026-10-03T15:08:25.971000+00:00
Message ID: b65c044d-5f49-49e2-bd0f-aebfb6d7f852

Maybe as a shop upgrade, for a lot of fate points, we can bless an item, and it would just keep track of whatever item that's currently blessed, and that it can never be offered for a sacrifice. And maybe you have, like, one blessed slot, and then you can buy more blessed slots, and then you can just choose an item that you'd like to bless. And, you know, I don't know what the UI would look like for that, like how you would pick it. I don't know if it's, like, you just search an item in the game, and then that item would never be considered for sacrifice, and, you know, you're just dumb if you pick an item that you don't currently own. But if you do currently own it, it's like, I never want my, like, Tumikin Shadow to be ever considered for a sacrifice. I'm going to bless this item. And then that also gives us some more stuff to buy in the shop. I think that'd be cool. Let's lock that idea in. What are your thoughts on it? I can always revert it.

## S8-M0302 - assistant - 2026-10-03T15:08:28.672452+00:00
Message ID: 62cb21c9-ece2-4178-b8f4-c77943821fd3

Yes, I like it. **Let's lock it in, but keep the exact number of additional slots and their costs as `BALANCE_TBD`.**

I think it improves the Sacrifice system because it gives the player a **small amount of control without removing the fear**. You can't protect your whole bank, but you can say:

> *“This one item is absolutely sacred. Death is never getting it.”*

### CONFIRMED — Blessed Items

Wheelbound has a **Blessed Item** system tied to the Sacrifice mechanic.

The player starts with access to **one Blessed Item slot** once the relevant system is available. Additional Blessed slots can be purchased for **substantial Fate Point costs**.

An item assigned to a Blessed slot is completely excluded from:

- Forced Death's Wheel candidate selection.
- Any other involuntary Sacrifice mechanic.

It does **not** need protection from voluntary sacrifice because the player is choosing that themselves anyway.

Conceptually:

```text id="ubprju"
BLESSED POSSESSIONS

Death may not claim these items.

Slot I
[ Tumeken's Shadow ]
        BLESSED

Slot II
🔒 Purchase Unlock — ??? FP

Slot III
🔒 Purchase Unlock — ??? FP
```

And I actually prefer your idea that the blessing is tied to the **item type**, not a particular physical copy.

If you bless:

> **Tumeken's Shadow**

then *any Tumeken's Shadow you own* is excluded from Forced Sacrifice.

That makes persistence dramatically cleaner too. We're just storing an item ID in the player's Wheelbound state.

### Changing your blessing

One thing I would add: **you should be allowed to change what's blessed.**

Otherwise the player could bless an early-game item and later permanently regret wasting the slot.

But I don't think swapping should be completely free whenever Death's Wheel appears. That creates an obvious exploit:

> Sacrifice triggered → see danger → quickly bless expensive item.

So I'd establish:

> **Blessings must be configured before a Forced Sacrifice begins. Once the Sacrifice Slice lands, the current Blessed Item configuration is locked until that sacrifice is resolved.**

Outside an active Forced Sacrifice, you can change the protected item.

Whether changing it normally is **free or costs a small amount of FP** can be determined later. My initial preference is free—we've already monetized the actual Blessed slots, and I don't think we need another tiny currency sink.

### I also like the shop progression this creates

It gives us another genuinely desirable expensive purchase:

```text id="6yztcg"
FATE SHOP

Fate's Pardon
Recover a BANNED shop

Fate's Cleansing
Remove one Tainted Slice

Blessed Possession II
Protect another item from Death

Blessed Possession III
Protect another item from Death

...
```

Those aren't boring `+5%` upgrades. They all solve **real problems the player has created for themselves**.

And there's a good decision embedded in it:

> *“Do I spend a huge amount of FP protecting my Shadow... or spend those points progressing my account and accept the risk?”*

That's exactly the kind of FP sink we need when we eventually balance the economy.

So **Blessed Items are locked**, but absolutely reversible as a design decision later if playtesting shows they remove too much tension.

---

Coming back to the question immediately before this: **I still think Forced Sacrifice should award spins based on the value Death actually takes.**

So if Death takes your 150k item, you might receive relatively few spins. If Death takes something worth 20m, you should receive substantially more. Otherwise losing the 20m item feels purely punitive rather than like an incredibly painful transaction for additional Fate.

That's the next thing I'd lock or change. memcite

## S8-M0303 - user - 2026-10-03T15:09:34.550000+00:00
Message ID: b5a4fde8-88dd-4c2c-8d1f-2d78f4831a01

Yes. So the spins that are the value of the item sacrificed should not change between the forced and voluntary spin. So, like, they're the same thing. So, like, if you voluntarily— if you volunteer something worth two mil, you get two mil worth of spins. If the forced spin takes something that's worth two mil, you get that same amount of spins. So nothing's going to change if you do a forced versus a voluntary. It's just the one is forcing your hand, where the other one is you are willingly giving up that item. I think that's how it should be done.

## S8-M0304 - assistant - 2026-10-03T15:09:37.302049+00:00
Message ID: 688ef22a-0708-4d3c-a5cf-2e3ef477a46a

Exactly. **Locked.**

### CONFIRMED — Universal Sacrifice Value

There is **one sacrifice-value → Master Fate Spins conversion system** in Wheelbound.

The conversion does not care whether the sacrifice was voluntary or forced.

```text
ITEM VALUE
    ↓
Sacrifice Conversion Table
    ↓
MASTER FATE SPINS
```

So if, hypothetically:

> **2,000,000 GP sacrificed = 4 spins**

then:

**Voluntary:** Player chooses to sacrifice a 2m item → **+4 spins**

**Forced:** Death's Wheel selects a 2m item → **+4 spins**

Same value. Same spins.

The only difference is **agency**:

> **Voluntary Sacrifice:** *You decide what you're willing to lose.*

> **Forced Sacrifice:** *Death decides what you're going to lose.*

For stacks, the same rule applies. If Death takes an entire stack worth 6.4m, the sacrifice is valued at 6.4m and awards whatever number of spins corresponds to that value.

This also means we only need to balance **one sacrifice economy** later. We determine what one Master Fate spin is approximately worth in GP and build sensible brackets/curves around it.

I strongly prefer this over penalizing forced sacrifices with worse conversion rates. **The punishment is losing control over what gets taken.** It doesn't also need a hidden economic penalty.

### Next question

I think the Sacrifice system is actually getting close to complete.

The next thing I'd settle is the **Blessed Item slot progression** we just introduced:

**Does the player automatically receive one Blessed Item slot when Sacrifice first unlocks, or is even the first Blessed slot something they have to purchase from the Fate Shop?**

My inclination is **the first one should also be purchased**. Protection from Death should itself be a luxury, especially since the player chose to Defy Fate in the first place. memcite

## S8-M0305 - user - 2026-10-03T15:10:56.999000+00:00
Message ID: 1fda378c-df4a-42c0-b431-1787d0258e34

I think it should be introduced when the sacrifice is unlocked. So immediately when it's unlocked, because basically you don't even know about the concept of sacrificing shit until you've run out of spins to begin with. So whenever you run out of spins, it should be like, you know, defy fate, right? You get tanked, you get sacrificed. You're also introduced to a blessed item, and it basically tells you, like, listen, now sacrifice thing is on the table. Like if you want to save an item, you need to go set that. Go set that item right now. And then just explain, like this item cannot be touched, like death is gonna overlook this item. Something like that.

## S8-M0306 - assistant - 2026-10-03T15:11:00.135418+00:00
Message ID: dac44040-67dd-4750-815e-5c06366f1b7e

Yes. I think that's much better than making the first protection something they immediately have to buy.

**Locked — the first Blessed Item slot is part of the First Defy onboarding.**

When the player Defies Fate for the first time, the presentation should explain the whole new reality they've entered:

```text id="omzvch"
YOU HAVE DEFIED FATE

Your allotted Fate has run dry.

Fate grants you another chance...
but there is a price.

+ Additional Spins
+ Taint enters your wheel
+ Sacrifice permanently enters your wheel
+ Voluntary Sacrifice unlocked
+ 1 Blessed Item slot unlocked
```

Then I think we should **actively push them to configure it**, rather than burying it in a menu:

```text id="2vzdhz"
DEATH NOW HAS CLAIM TO YOUR POSSESSIONS

Should your Master Wheel land on Sacrifice,
Death may demand an offering from your possessions.

Fate grants you one protection.

Choose one item to BLESS.

A Blessed Item will be overlooked by Death
and can never appear on a Forced Sacrifice Wheel.

[CHOOSE BLESSED ITEM]
```

They should be able to skip choosing one if they really want, but the game makes it very clear:

> **You have one free Blessed slot. If you leave it empty, Death will not protect anything.**

Once configured, they can change the blessing outside an active Forced Sacrifice. As previously established, the moment the Sacrifice Slice is landed on, the current blessings are **locked until that sacrifice is resolved**.

Additional Blessed Item slots become expensive Fate Shop upgrades.

I also like the wording **“Death will overlook this item.”** It fits better thematically than making it sound like some technical exclusion list.

So the first Defy now isn't merely *“here are more spins.”* It's almost a second onboarding chapter where Wheelbound tells the player:

> **You have exhausted the Fate originally allotted to you. From this point forward, Death and corruption are part of your run.**

That's a strong transition.

**Next question:** I think we should decide **how many additional Blessed slots can eventually be purchased.** My instinct is to keep it very small—probably only **2 additional slots**, for **3 protected item types total**. If we let players bless 5–10 things, we start taking the teeth out of Forced Sacrifice. memcite

## S8-M0307 - user - 2026-10-03T15:12:10.485000+00:00
Message ID: a1d7facc-4f6f-4e5a-bcca-64b5a831eb39

I agree. There should be upgrades purchased in the shop. They should be really expensive, and then, like the general stores and everything, they should increase in cost. So, like maybe it's shown as, like, a tree view or something, where it's like the first one is worth, you know, maybe as much as the Grand Exchange, you know, maybe something a little more, and then the next one is worth even more to, like, kind of, like, quote-unquote protect it.

## S8-M0308 - assistant - 2026-10-03T15:12:13.164021+00:00
Message ID: 21acb197-4dd3-4883-ad38-3e208ef33b5e

Yes. **Locked.** I think that gives Blessed Items the right place in the late-game FP economy.

The player gets **Blessed Slot I for free** when Sacrifice is first introduced. Then there are exactly **two additional upgrades**, giving a maximum of **3 Blessed Item types**.

I like your tree/upgrade-path idea too:

```text id="th7n6u"
        BLESSED POSSESSIONS

        ✦ SLOT I ✦
        FREE
        Unlocked by Defying Fate
             │
             ▼
        ✦ SLOT II ✦
        VERY EXPENSIVE
        [PURCHASE]
             │
             ▼
        ✦ SLOT III ✦
        EXTREMELY EXPENSIVE
        [PURCHASE]
```

Slots II and III should be **major purchases**, not casual upgrades. I agree that Slot II could potentially be in the same general economic territory as the Grand Exchange, while Slot III could be substantially more expensive.

Those numbers stay `BALANCE_TBD`, because once we know the real FP economy, I'll determine prices that make the decision genuinely difficult.

The important design principle is:

> **Each additional protection becomes substantially more expensive because every Blessed slot makes Death's Forced Sacrifice less dangerous.**

Unlike Fate's Pardon and Fate's Cleansing, which we've said should have fixed prices, **Blessed slots intentionally escalate** because you're purchasing increasingly powerful permanent protection.

And they're sequential:

> Slot I → Slot II → Slot III

You can't skip II and buy III.

I wouldn't necessarily put this inside the main **Progression Tree**, though. I like it as a little upgrade tree **inside the Fate Shop / Blessed Possessions interface**. That keeps the main progression tree from becoming cluttered with shop upgrades.

So we're now locked at:

**1 free Blessed slot + 2 increasingly expensive purchasable slots = maximum 3 protected item types.**

And I think that's enough protection. Three lets someone protect their truly sacred gear while leaving **the overwhelming majority of their wealth exposed to Death**.

The Sacrifice system is getting very close to fully designed now. memcite

## S8-M0309 - user - 2026-10-03T15:21:44.552000+00:00
Message ID: 54371049-6c1d-4380-a8d9-453b3892faf7

death coffer will not accept items worth less than 10k per individual item so keep that in mind

## S8-M0310 - assistant - 2026-10-03T15:21:49.777091+00:00
Message ID: f0ddd1b7-b78a-4c0c-a7f1-52e78b94de82

Yes — that's an important constraint for the Sacrifice system.

We'll treat **Death's Coffer's 10,000 GP minimum as a per-item/unit eligibility requirement**, not a stack-total requirement.

So, for example:

- `1 item × 150,000 GP` → eligible.
- `100 items × 12,000 GP each` → eligible; Forced Sacrifice could demand the entire 100-item stack.
- `10,000 items × 500 GP each = 5m total` → **not eligible**, because each individual item is below Death's Coffer minimum.

That means our Forced Sacrifice candidate generator needs to filter for **items Death's Coffer will actually accept first**, before doing our own preferred value-floor logic (such as trying to populate the wheel with ≥100k-value possessions).

I'll treat that as a **technical constraint of the Sacrifice system**, separate from our `BALANCE_TBD` thresholds. I attempted to verify it against the OSRS Wiki just now, but that page is blocking automated access, so I'm recording the 10k rule from your correction rather than claiming independent verification.

## S8-M0311 - user - 2026-10-03T15:42:33.512000+00:00
Message ID: b7fe5ea2-3e6c-4388-8914-b77a406923db

whats next

## S8-M0312 - assistant - 2026-10-03T15:42:35.355787+00:00
Message ID: 4355d3e4-a0bc-43d8-b4d6-5a3d87e5707f

I think we've now designed enough of **Defy Fate / Taint / Sacrifice** that I wouldn't add another subsystem yet.

The next unresolved piece I'd tackle is **Fate's Cleansing**, because we've established that Taint can be removed, but we haven't fully defined the rules.

We already have these parts locked:

> Fate's Cleansing is a very expensive Fate Shop item.  
> It removes **one Tainted slice**.  
> It cannot remove or reduce the permanent Sacrifice slice.  
> Removing Taint leaves an **empty Master Wheel slot**; it does not restore whatever slice was originally displaced.  
> Its price is a fixed amount rather than escalating.

### The next single question

**Should Fate's Cleansing be infinitely purchasable as long as the player can afford it?**

My recommendation is **yes**.

If you've Defied Fate five times and accumulated three Tainted slices, you should theoretically be able to spend a *ridiculous* amount of FP and cleanse all three.

You still aren't undoing Defy Fate:

```text
Defied Fate 5 times

Before cleansing:
☣ Taint
☣ Taint
☣ Taint
☠ Sacrifice — 2.00x

Buy 3 × Fate's Cleansing

After:
Taint removed
Taint removed
Taint removed

☠ Sacrifice — STILL 2.00x
```

So money/FP can clean up the **corruption**, but it can never erase the permanent **scar** of repeatedly Defying Fate.

And because Cleansing is extremely expensive, there's a nasty economic decision:

> Spend FP fixing my fucked-up wheel?

**or**

> Spend FP on the progression I still need to actually complete my Grand Fate?

I think that's exactly the decision we want. memcite

## S8-M0313 - user - 2026-10-03T15:43:01.955000+00:00
Message ID: f508883f-0868-405e-83a3-9e401572010c

Yes, fate cleansing should be infinitely purchasable. I don't think it should increase. I think it should just be a one-time fee and it should be pretty expensive, but it should just be always available after, of course, you've defied fate.

## S8-M0314 - assistant - 2026-10-03T15:43:04.134109+00:00
Message ID: b3fcc66a-0b9a-47eb-b7d2-c75771ecea33

**Locked — Fate's Cleansing.**

Once the player has **Defied Fate at least once**, Fate's Cleansing becomes permanently available in the Fate Shop.

> **Fate's Cleansing**
> - Fixed, very expensive FP price — `BALANCE_TBD`
> - Infinitely purchasable
> - Price does **not** escalate
> - Each purchase removes **one Tainted slice**
> - Player chooses which Tainted slice to remove if that ever matters
> - Leaves an empty wheel slot
> - Does not restore a previously displaced slice
> - Cannot remove the Sacrifice slice
> - Cannot reduce Sacrifice's accumulated weight
> - Does not reduce/reset the player's Defy Fate count

So you can spend your way out of **Taint**, but you can never completely undo the fact that you Defied Fate.

I think that finishes the **Cleansing side**.

### Next question

There's one Defy/Taint issue I think we should settle before moving on:

**What happens if the player hits 0 spins again but refuses to Defy Fate?**

My inclination is very simple: **nothing.** Their Wheelbound run isn't failed or abandoned. It just becomes stuck at `0 Spins`.

They can:

> attempt their Grand Fate, if ready  
> voluntarily sacrifice possessions for spins, if Sacrifice is already unlocked  
> press **Defy Fate**  
> pause/abandon the run

But Wheelbound doesn't give them another way around it.

That makes **0 spins a genuine state**, rather than automatically triggering another Defy. memcite

## S8-M0315 - user - 2026-10-03T15:47:13.044000+00:00
Message ID: 1f443178-478f-4701-b1fe-bc37af7556da

If they try to spin at zero spins, it's just brought, like to the defy fate. So, like whenever you spin the wheel, if it goes to zero spins, and then if they try and spin again when they have nothing, the wheel does the animation to be like, Do you choose to defy fate? And then that's it. And then it's the little message. It's like, You can defy fate and gain more spins, or you can attempt your grand fate. And then that's it. So they're essentially, it's a stalemate if they don't do anything. It's on them.

## S8-M0316 - assistant - 2026-10-03T15:47:15.865576+00:00
Message ID: e6aad2c5-6da7-4ab5-8408-881846ee1924

Yep. That's cleaner. **Locked — Zero-Spin Stalemate.**

The important distinction is that reaching **0 spins does not automatically trigger Defy Fate**. The player has to actually attempt another Master Wheel spin.

```text
SPINS: 0

Player presses SPIN
        ↓
Wheel begins/reacts...
        ↓
Animation is interrupted
        ↓
"YOUR ALLOTTED FATE HAS RUN DRY."
        ↓
You have reached the end of the Fate allotted to you.

You may DEFY FATE to receive additional spins,
or face the destiny that was sealed at the
beginning of your journey.

[ DEFY FATE ]     [ GRAND FATE ]
```

If they choose neither, **that's it**. Nothing happens in the background. No forced Defy, no automatic failure, no timer, no additional punishment. They're simply sitting at zero spins until they make a decision.

After Sacrifice has previously been unlocked, I think that screen can additionally expose **Offer a Sacrifice** as an existing recovery option, but it doesn't change the core choice or force them into anything.

And I especially like having the player actually **press SPIN at zero** before this happens. It makes the wheel itself effectively tell them *“No. Fate has run dry.”* That's much more thematic than immediately throwing a dialog up when their previous Fate consumes their last spin.

**Next:** I think we're basically finished with the rules surrounding running out of spins. The next thing I'd nail down is what **attempting the Grand Fate** actually means mechanically—particularly whether the player can attempt it whenever they want, or whether Wheelbound requires certain conditions before that button becomes available. memcite

## S8-M0317 - user - 2026-10-03T15:47:46.334000+00:00
Message ID: 23c8346b-d013-4134-91ea-09f580b9a292

sounds good lets go

## S8-M0318 - assistant - 2026-10-03T15:47:48.233873+00:00
Message ID: eec21067-2565-4397-aa23-cdeb51274b48

Great. Then let's define **Grand Fate attempts**.

The first decision is:

### When can the player attempt their Grand Fate?

I think the Grand Fate should be **visible from the beginning but not necessarily locked behind reaching 0 spins**.

For example, suppose their sealed Grand Fate is:

> ☠ **THE INFERNO**  
> Obtain an Infernal Cape.

If they're 120 Fates into the run, still have 30 spins remaining, but believe their account is ready, I don't see a good reason to make them burn those 30 spins first.

I'd make **Attempt Grand Fate** available at any point after the run properly begins.

And importantly, pressing it shouldn't instantly consume anything or end the run. It changes the run into a **Grand Fate Attempt state**:

```text id="c4gvzy"
ACTIVE WHEELBOUND
      ↓
Player chooses GRAND FATE
      ↓
Grand Fate warning / requirements
      ↓
[BEGIN GRAND FATE]
      ↓
GRAND FATE ATTEMPT ACTIVE
      ↓
Success → WHEELBOUND VICTORY
Failure → ???
```

That `???` is where the meaningful design decision is.

I think starting a Grand Fate attempt needs **real stakes**. Otherwise someone with Inferno can just casually send attempts forever while continuing normal Wheelbound between attempts.

But I **don't** think one failed Inferno attempt should delete a potentially hundreds-of-hours Wheelbound run.

So before deciding the penalty, first let's lock the simpler part:

> **Can a player voluntarily begin their Grand Fate whenever they believe they're ready, even if they still have Master Fate spins remaining?**

My recommendation: **yes.**

## S8-M0319 - user - 2026-10-03T15:50:18.206000+00:00
Message ID: 8a79f0fd-3b00-4617-a878-9e114c026854

I think there needs to be a button that says attempt grand fate, and it instantly switches to, just like any other task, it just switches to, like, attempt to grand fate. If you're doing a Yama contract, it might show up a little UI, like depicting all the contracts, which one are you trying, stuff like that. Once again, the hidden tolerance is still in effect. So if you attempt to grand fate, you start doing something else, it knows and it'll punish you accordingly. So as long as you're doing, if you're doing Yama, if you do something else, it's gonna punish you, but if you're doing Yama, it'll be fine, stuff like that. And then if you die or if you don't succeed, maybe, like, a little message appears and be like, you've, you know, attempted grand fate. It didn't work. You have to, like, re-enable it every single time. What are your thoughts?

## S8-M0320 - assistant - 2026-10-03T15:50:20.970866+00:00
Message ID: c80cb9d1-3b19-408c-8759-0008f08c935d

I like that much more than making Grand Fate a special irreversible mode. It should behave like the **ultimate Fate assignment**.

### Grand Fate Attempt — CONFIRMED direction

The player can press **Attempt Grand Fate** whenever they want. That replaces their current assignment UI with their sealed Grand Fate.

For something with multiple valid variants—your Yama example is good—the UI can then ask which qualifying version they're attempting before activating it.

```text
☠ GRAND FATE ☠

YAMA

Choose your contract:
[ Contract A ]
[ Contract B ]
[ Contract C ]

[ BEGIN GRAND FATE ]
```

Once they hit Begin, **Grand Fate becomes their Active Fate**.

All the normal Wheelbound monitoring still applies. The visible objective-specific restrictions apply where appropriate, and importantly the **hidden Fate Audit remains active**. So activating Grand Fate isn't a free period where you can go train Woodcutting for six hours because the objective itself doesn't generate Woodcutting XP.

If they legitimately work on Yama, that's fine. If they activate Yama and then go do unrelated progression, Fate records it and handles it through the same audit/penance system we've already designed.

### Failure shouldn't be catastrophic

I agree with your instinct here too.

If they die, fail the encounter, abandon the attempt, fail the selected contract, etc.:

> **GRAND FATE ATTEMPT FAILED**  
> Fate remains unfulfilled.

Then the Grand Fate stops being Active. **They must deliberately press Attempt Grand Fate again** before another attempt counts.

That gives us a very useful distinction:

**Their Grand Fate is permanently sealed, but an individual Grand Fate Attempt is temporary.**

So dying at Inferno doesn't destroy the Wheelbound run. It simply ends that attempt.

I also wouldn't charge a spin for attempting or failing Grand Fate. Otherwise repeated learning attempts against Inferno/Colosseum/Yama-style endgame content could create a pretty miserable economic loop.

And there's a nice consequence to requiring reactivation: the plugin knows exactly where an attempt begins and ends rather than trying to infer *“maybe they're attempting their Grand Fate right now.”*

---

There's **one important edge case** we need to decide next:

Suppose I currently have an ordinary Fate:

> **Gain 75,000 Slayer XP**

I've completed 30,000/75,000.

Then I press **Attempt Grand Fate**.

What happens to that unfinished Slayer Fate?

My recommendation is **don't allow Grand Fate activation while another Fate is active**. Finish the current Fate first. That avoids Grand Fate becoming an escape button whenever the player gets an assignment they don't like.

So **Attempt Grand Fate** could always be visible, but while an ordinary Fate is active it says something like:

> *Your current Fate must first be fulfilled.*

Do you agree with that? memcite

## S8-M0321 - user - 2026-10-03T15:54:33.692000+00:00
Message ID: bf112913-3519-4534-92a6-4406443db45c

You can only complete. You can only start a grand fate when you finish another fate. So whenever you have no active fate and you're essentially spinning the wheel again is the only time where you're allowed to activate your grand fate.

## S8-M0322 - assistant - 2026-10-03T15:54:34.834747+00:00
Message ID: c51a8b8d-508a-4e59-8d28-97f7e9ac4770

Exactly. **Locked — Grand Fate can only be activated between Fates.**

Wheelbound effectively has a clean **Awaiting Fate** state:

```text id="b86nxl"
ACTIVE FATE
   ↓
Complete Fate
   ↓
Rewards / FP
   ↓
NO ACTIVE FATE
   │
   ├── SPIN MASTER WHEEL
   │       ↓
   │   New Fate
   │
   └── ATTEMPT GRAND FATE
           ↓
       Grand Fate Active
```

So **Attempt Grand Fate is never an escape hatch**. If you've been assigned 75,000 Slayer XP, you're finishing that Fate before Grand Fate becomes available again.

After a failed Grand Fate attempt, it returns you to that same **No Active Fate** state. The player can immediately choose:

> **Attempt Grand Fate Again**  
> or  
> **Spin Master Wheel** — provided they have spins.

At 0 spins, they could instead Attempt Grand Fate, Defy Fate, or—once unlocked—voluntarily Sacrifice for spins.

I also think **Grand Fate attempts should not consume Master Wheel spins**, as we discussed. You earned the opportunity to attempt it by getting your account there; repeatedly practicing Inferno/Colosseum/etc. shouldn't drain the Fate economy.

### Next decision: what constitutes a failed Grand Fate attempt?

This one matters because we need the plugin to know when to deactivate Grand Fate.

My inclination is to make failure **objective-specific**, not one universal rule.

For example:

- **Inferno:** death/leave without completion → attempt ends.
- **Colosseum:** death/leave without completion → attempt ends.
- **Awakened DT2:** you're attempting a specific required boss; death/leave ends that attempt, but previously completed required Awakened bosses remain credited toward the overall Grand Fate.
- **Master CAs:** this is fundamentally progression rather than a single encounter, so it probably needs different attempt semantics.
- Other Grand Fates can define their own start/success/failure conditions.

So the question I'd tackle next is:

**For multi-part Grand Fates, should completed pieces permanently count toward the Grand Fate across attempts?**

My recommendation is **absolutely yes**. If your Grand Fate is all four Awakened DT2 bosses and you've legitimately killed three while Grand Fate was active, dying to the fourth shouldn't erase the other three. memcite

## S8-M0323 - user - 2026-10-03T16:06:28.910000+00:00
Message ID: a452b06b-35a0-4a62-a390-835f218e2db7

id you fail it does not reset the other its more or less a checklist

## S8-M0324 - assistant - 2026-10-03T16:06:30.054438+00:00
Message ID: e262a077-c9ef-43c7-9c27-b62fc5a0a2f9

Exactly. **Locked — multi-part Grand Fates are persistent checklists.**

For example:

```text
☠ GRAND FATE
AWAKENED

Defeat all four Awakened DT2 bosses.

✓ Awakened Vardorvis
✓ Awakened Leviathan
✓ Awakened Duke Sucellus
□ Awakened Whisperer

Progress: 3 / 4
```

If you activate Grand Fate to attempt Whisperer and die:

> **GRAND FATE ATTEMPT FAILED**  
> Your destiny remains unfulfilled.

It drops back to **No Active Fate**, but the checklist remains **3/4**. Nothing previously earned is lost.

Same principle applies to any Grand Fate containing multiple independently verifiable requirements. Grand Fate progress is **permanent run progress** once legitimately completed.

### Next question

This brings us directly to something like **Master Combat Achievements**, because unlike Inferno or Awakened bosses, that's potentially a *huge checklist*.

My inclination is that when **Master CA Tier** is your Grand Fate, you don't need to have Grand Fate activated for every individual CA you've ever completed. Instead, your **starting CA state when Wheelbound begins becomes the baseline**, and the Grand Fate requires you to earn whatever additional CA progress is necessary to reach Master **during the Wheelbound run**.

That prevents pre-Wheelbound accomplishments from completing your Grand Fate instantly, while also avoiding the absurd requirement that someone somehow redo already-completed CAs.

That's the next rule I'd settle. memcite

## S8-M0325 - user - 2026-10-03T16:14:50.986000+00:00
Message ID: 4255155f-5493-4ac0-a51b-0d2d86f95b73

okay can you exaplin this a bit more

## S8-M0326 - assistant - 2026-10-03T16:14:52.566706+00:00
Message ID: d4a15951-d098-4d6c-af1b-041afda4c953

Yeah. The weird part is that **Master Combat Achievement tier isn't really a single activity you "attempt"** like Inferno.

Say you start Wheelbound and your account already has **700 CA points**, and Master requires some higher number.

Obviously we **can't erase your existing CAs**. And we don't want to tell you to redo them, because OSRS already considers them completed.

So there are really two separate concepts:

**Your OSRS CA progress** remains exactly what it is.

**Your Grand Fate progress** is getting from your starting CA state to Master tier while playing Wheelbound.

For example, imagine:

```text id="igq4c1"
WHEN WHEELBOUND STARTED

Combat Achievement Points: 700
Master Requirement:         1,000   ← example only

Grand Fate:
REACH MASTER COMBAT ACHIEVEMENT TIER

Starting CA Points:           700
Current CA Points:            700
Required:                   1,000
                              ↓
Need 300 additional points
```

Then later you're at 850:

```text id="mxquu4"
☠ GRAND FATE — MASTER COMBATANT

Starting CA Points:     700
Current CA Points:      850
Master Requirement:   1,000

Progress earned during Wheelbound:
150 / 300
```

You **do not** need Grand Fate activated while earning those CAs. The plugin is simply tracking your account's progression toward the condition in the background.

Once you reach Master tier, the checklist condition is satisfied.

### The problem I was trying to prevent

Suppose somebody starts Wheelbound **already at Master CA tier** and then spins:

> ☠ Grand Fate: Reach Master Combat Achievement Tier

They've technically completed their Grand Fate before the run even begins.

That's pretty lame compared with somebody who rolls Inferno and actually has to accomplish Inferno during Wheelbound.

So we'd need a rule for **Grand Fates the account has already completed before starting Wheelbound**.

And actually, thinking through it more, I **wouldn't solve that with the baseline system I just described**. There's a cleaner solution:

> **The Grand Fate Wheel should only contain Grand Fates the account has not already completed.**

Already Master CAs? Master CA isn't eligible.

Already have an Infernal Cape / verified Inferno completion? Inferno isn't eligible.

Already completed Colosseum? Colosseum isn't eligible.

Already killed all four Awakened DT2 bosses? That Grand Fate isn't eligible.

That keeps the opening Grand Fate spin meaningful without inventing weird "redo" requirements.

**That's what I'd recommend instead of my previous baseline idea.** Then if you roll Master CAs while you're below Master, your existing CA progress counts normally, and you simply have to reach Master during the Wheelbound run. memcite

## S8-M0327 - user - 2026-10-03T16:16:05.132000+00:00
Message ID: d02b4511-17d9-4095-a562-35f159ebb8e5

was its more like you earn the master teir of comabt achievements and really for this mode were catering to new accounts. This isnt something that you can pickup from a midpoint so dont worry about that

## S8-M0328 - assistant - 2026-10-03T16:16:06.735179+00:00
Message ID: 253f9e30-e60c-4eef-b0a2-900d88f1b71e

Yes, that simplifies it considerably. I was solving a problem we don't really have.

**Locked: Wheelbound is fundamentally a fresh-account game mode.** We're not designing around someone taking an established account halfway through its progression and turning Wheelbound on.

So for the Master CA Grand Fate, the objective is simply:

> ☠ **GRAND FATE — MASTER OF COMBAT**  
> Achieve the **Master Combat Achievement tier**.

Everything the account earns naturally contributes toward that. There's no special baseline, no requirement to activate Grand Fate while completing individual CAs, and no need to redo anything.

The **Attempt Grand Fate** button becomes relevant when the Grand Fate actually requires an attempt—Inferno, Colosseum, Yama-style encounter, Awakened boss attempt, etc. For a progression/checklist Grand Fate like Master CAs, the plugin can simply track the checklist/progression until the condition is fulfilled.

That also establishes a broader rule I like:

> **Grand Fates don't all need identical mechanics. They share the same end-goal framework, but each Grand Fate defines how its progress, attempts, failures, and completion are detected.**

That will save us from forcing something like Master CAs into an "encounter attempt" system where it doesn't belong.

### Next decision

There's one related question: **when does completing the Grand Fate actually end the Wheelbound run?**

My recommendation is that completion is **automatic once the Grand Fate's final requirement is verified**, followed immediately by the big victory sequence. No extra "Finish Run" button—you fulfilled your sealed destiny, so Wheelbound is complete.

## S8-M0329 - user - 2026-10-03T16:17:07.687000+00:00
Message ID: d935bfa9-bf1f-48fa-8a66-8ecd77f25732

for this one its more of a progress bar type thing and then youll need to build out your wheel accordinly to combat achievments maybe have more dedocicted slices on the wheel

## S8-M0330 - assistant - 2026-10-03T16:17:09.139243+00:00
Message ID: ce39115d-10ab-4e93-b981-2ea72553350c

Yes — that makes more sense. **Master CA should be treated as a long-term progression Grand Fate, not an “attempt.”**

If you roll **Master Combat Achievement Tier**, the Grand Fate panel is essentially a persistent progress bar/checklist:

```text
☠ GRAND FATE — MASTER OF COMBAT

Achieve Master Combat Achievement Tier

████████████░░░░░░░░  61%

Combat Achievement Points
Current:  ______
Master:   ______

Your wheel determines where Fate allows
you to pursue Combat Achievements.
```

And your point about the wheel is important: **this Grand Fate should materially affect how you build your account.** If Master CAs are your destiny, you're going to want to invest more heavily in the Combat/Bossing branch and probably dedicate more of your Master Wheel to combat-related opportunities.

I could see progression eventually letting you build something like:

```text
MASTER FATE WHEEL

Mining
Fishing
Slayer
Combat
Combat          ← duplicate
Bossing
Bossing         ← duplicate
Bossing         ← duplicate
Questing
Prayer
...
```

You're effectively shaping your wheel around the destiny you rolled at the beginning.

That actually reinforces one of the strongest ideas in Wheelbound: **the Grand Fate tells you where you're going; the progression system determines how you build a wheel capable of getting you there.**

### One thing I would NOT do

I wouldn't automatically give the Master-CA player extra Combat/Bossing slices just because they rolled that Grand Fate.

They should still have to **earn the ability to construct that combat-heavy wheel** through the Combat/Bossing progression branch, duplicates, slice upgrades, FP purchases, etc.

Otherwise the Grand Fate starts solving itself.

So I'd lock **Master CA Grand Fate = persistent CA progress tracker**, with no special activation required for individual achievements.

The next question I'd tackle is directly related: **Should the Grand Fate influence which progression upgrades become available, or should every account have exactly the same progression tree regardless of the Grand Fate they rolled?**

My inclination is **same underlying tree for everyone**. Your Grand Fate changes which parts of that tree *you value*, rather than changing the rules of the tree itself. memcite

## S8-M0331 - user - 2026-10-03T16:18:23.300000+00:00
Message ID: 3919db9e-745e-44e4-85e2-1ebd6070b0fe

all sotr bought items. If they want dedicated slices they can buy they

## S8-M0332 - assistant - 2026-10-03T16:18:25.583811+00:00
Message ID: 3405ade4-592c-48c2-9148-61392b4406d6

Right — that fits the system better. **Locked.**

The Grand Fate itself grants **nothing**. It tells you the destination, not how to get there.

If someone rolls **Master Combat Achievements**, they'll probably *want* a combat-heavy wheel, but they have to build it themselves through the shop/progression economy.

So if they want:

```text id="x86a0n"
Combat
Combat
Bossing
Bossing
Bossing
Slayer
Prayer
Questing
...
```

they need to **buy/unlock those capabilities**. Dedicated slices, duplicate slices, additional Bossing weight, etc. are all purchased upgrades. Master CA doesn't hand them out.

That gives us a good general Grand Fate rule:

> **Grand Fate changes the player's strategic priorities, but does not provide special progression advantages.**

Someone with Inferno might prioritize Magic/Ranged/Prayer and the relevant unlocks. Someone with Master CAs might dump resources into Bossing access and dedicated slices. Someone with Awakened DT2 needs to build toward those bosses. **Same economy and systems; different reason to invest in them.**

### Next question

For **Master CA specifically**, there's one thing we should settle:

When a player completes a Combat Achievement while doing an allowed Fate, I think **all CA points should count toward their Grand Fate progress automatically**, regardless of whether that CA was specifically required by their active Fate.

Example: Bossing Fate sends you to Vorkath. While doing your assigned Vorkath kills, you happen to complete two Vorkath CAs. Those CA points immediately advance the Master CA Grand Fate progress bar.

I think that's the natural rule: **if Wheelbound legitimately sent you into the content and you earn CAs while following Fate, you earned them.** memcite

## S8-M0333 - user - 2026-10-03T16:19:13.124000+00:00
Message ID: c9155e1d-93a4-4aef-b932-1694b49da362

yes, its more like they have enough points to be considered a master. gnomils hilt maybe thats the item we actually rewuire

## S8-M0334 - assistant - 2026-10-03T16:19:16.852026+00:00
Message ID: 948b3961-526a-46ff-a4f1-02c0721e50e0

Yes — I think that's the cleaner way to frame it.

Rather than Wheelbound internally saying:

> “Earn X Combat Achievement points.”

The Grand Fate is:

> ☠ **Become a Master of Combat**  
> **Obtain Ghommal's Hilt 4.**

Ghommal's hilt 4 is the reward associated with the **Master Combat Achievement tier**, so it gives us a concrete end-state instead of making the Grand Fate feel like filling an arbitrary meter. I tried to re-check the current Wiki details before locking the exact implementation, but the OSRS Wiki is blocking automated access right now, so we'll verify the precise detection mechanics later.

The UI can still show the **CA progress bar** because that's what actually tells the player how close they are:

```text id="8x7ypu"
☠ GRAND FATE — MASTER OF COMBAT

Obtain Ghommal's Hilt 4

MASTER PROGRESS
██████████████░░░░░░  72%

Combat Achievement Points
Current:  ___
Required: ___

Reward:
Ghommal's Hilt 4
```

And yes: **any legitimate CA earned during Wheelbound counts.** If Fate sends you to Vorkath and you accidentally knock out three CAs while completing your Vorkath Fate, great—they're yours.

Then the player's strategic problem becomes:

> *How do I build my Master Wheel so Fate keeps giving me opportunities to earn enough CAs to reach Master?*

That's where purchasing dedicated/duplicate Combat and Bossing slices becomes especially valuable.

I'd call **Ghommal's Hilt 4 / reaching Master CA tier** the completion condition, with the hilt serving as the thematic tangible proof. We can determine later whether RuneLite should technically detect the tier unlock directly, the hilt, or both for reliability.

**That Grand Fate is pretty much solved.**

Next I'd move to the **Radiant Oathplate Grand Fate**, because we still haven't decided whether that means **one specific piece, any piece, or the complete set**. memcite

## S8-M0335 - user - 2026-10-03T16:19:40.658000+00:00
Message ID: 43d6c375-e7f5-4de3-b61c-c9549d0b9a44

complete set

## S8-M0336 - assistant - 2026-10-03T16:19:44.644105+00:00
Message ID: 39503066-d58c-46ba-999e-97404b86d392

**Locked — Radiant Oathplate Grand Fate requires the complete set.**

So the Grand Fate becomes a persistent checklist, similar to Awakened DT2:

```text id="yd03cx"
☠ GRAND FATE — RADIANT OATH

Obtain the complete Radiant Oathplate set.

□ Radiant Oathplate Helm
□ Radiant Oathplate Body
□ Radiant Oathplate Legs

Progress: 0 / 3
```

Each piece is permanently checked off when legitimately obtained during the Wheelbound run. Getting a duplicate doesn't matter, and losing/selling a piece afterward wouldn't erase the completion credit—we care that the player **earned it**.

This also naturally forces the player to build their wheel toward whatever content/access/progression is necessary to obtain the full set; the Grand Fate itself provides no special access.

That resolves another of the five Grand Fates. **Next I'd tackle the Awakened DT2 Grand Fate in a little more detail**, specifically how the player gets permission/opportunities through their wheel to attempt each of the four bosses. memcite

## S8-M0337 - user - 2026-10-03T16:23:20.104000+00:00
Message ID: 7277d91f-feed-4768-a9b5-f10c0c0f415a

Uh, no. It'll be just so, like, the attempt fate button is just, like, you're attempting your grand fate. So whenever you attempt fate, if you're on the DT2 bosses, your fate is to do one of the DT2 bosses. Maybe, like, a little checklist appears to be like, all right, I've completed Awakened Leviathan. I've completed Awakened Duke. I have options of doing Whisper or Vardorvis, you know? But that's what it is. So, like, whenever you click Attempt Your Grand Fate, it's like a checklist would appear, and, like, you have two options to go for. You know, that's what you're going for. And then once you defeat one, you're like, great. And then it goes back to, like, the normal way where you could spin the wheel some more, or you would have to enable Attempt a Grand Fate once more and do the last one. It's kind of how I see, like, a checklist going. And this would work similarly for, like, Radiant Oath Plate. So, and then I believe the grand item at the end of, like, the Awakened DT2 bosses is, like, Blood Torva. So, but it doesn't unlock Torva; it just unlocks the cosmetic for Blood Torva. So I don't know if we want to keep it, like, get that item, or, like, just do that.

## S8-M0338 - assistant - 2026-10-03T16:23:23.706071+00:00
Message ID: ecd47549-0822-4305-9e5e-1af8db8496f8

Yes. I understand the distinction now, and I like your version better.

The **Grand Fate is the overall checklist**, while **Attempt Grand Fate activates exactly one unfinished piece of that checklist as your current Fate**.

For Awakened DT2, suppose you're here:

```text id="0i7u5c"
☠ GRAND FATE — AWAKENED

✓ Awakened Leviathan
✓ Awakened Duke Sucellus
□ Awakened Vardorvis
□ Awakened Whisperer

[ ATTEMPT GRAND FATE ]
```

Clicking it opens the remaining valid objectives:

```text id="8x3r3p"
CHOOSE YOUR GRAND FATE ATTEMPT

[ AWAKENED VARDORVIS ]
[ AWAKENED WHISPERER ]
```

Choose Whisperer → **Defeat Awakened Whisperer** becomes your Active Fate.

While that's active, the normal Wheelbound rules and hidden Fate Audit apply. Kill Whisperer successfully → it gets permanently checked → Grand Fate attempt ends → back to the normal **No Active Fate** state.

Then you can spin another normal Fate or hit **Attempt Grand Fate** again and select Vardorvis.

That's clean.

### Blood Torva

You're remembering the basic reward structure correctly. Completing the four Awakened DT2 bosses is associated with unlocking the ability to create/use **Blood Torva**, rather than simply being handed a replacement Torva armor set. I attempted to verify the exact current 2026 item/reward mechanics against the OSRS Wiki before answering, but its automated access is blocked right now, so we'll need to verify the exact API/item-state implementation before coding.

For Wheelbound, though, I **wouldn't require Blood Torva itself**.

I'd make the Grand Fate:

> **Defeat all four Awakened Desert Treasure II bosses.**

That's the actual accomplishment. Requiring Blood Torva could accidentally introduce a separate requirement around owning Torva pieces, which isn't what this Grand Fate is testing.

So:

**Awakened Grand Fate**
- □ Awakened Vardorvis
- □ Awakened Leviathan
- □ Awakened Duke Sucellus
- □ Awakened Whisperer
- **4/4 = Grand Fate fulfilled**
- Blood Torva is the thematic OSRS reward/unlock, not a Wheelbound requirement.

### And Radiant Oathplate works the same way

This also corrects what I said last turn. The pieces aren't necessarily passively checked just because you happened to get them.

If your Grand Fate is the complete Radiant Oathplate set, you'd press **Attempt Grand Fate**, select an unfinished component/objective where applicable, and **that becomes your Active Fate**. Once achieved, that component gets permanently checked.

So we've got a reusable structure:

> **Multi-part Grand Fate → checklist → Attempt Grand Fate → choose unfinished objective → that objective becomes Active Fate → success permanently checks it → return to normal Wheelbound → repeat later.**

That's a really solid framework because it works for more than just DT2.

**One thing I'd decide next:** Inferno and Colosseum are easy—they're single-objective Grand Fates. But for Radiant Oathplate, we should determine exactly what an individual active attempt looks like, because obtaining armor pieces may involve repeated content rather than simply selecting “Body” and deterministically earning the Body.

## S8-M0339 - user - 2026-10-03T16:26:19.799000+00:00
Message ID: 6d1c5833-e72c-4913-a4e0-13b773f16e78

beating and competing the contract that will give you the radient oathplate

## S8-M0340 - assistant - 2026-10-03T16:26:21.593667+00:00
Message ID: ae91085a-3c7b-4842-aa30-af4531d9067f

Right — that's the missing piece. **Locked.**

For the **Radiant Oathplate Grand Fate**, we aren't tracking random armor drops. The Grand Fate is about completing the **contracts that award the Radiant Oathplate pieces**.

So its structure should mirror the actual contract progression:

```text
☠ GRAND FATE — RADIANT OATH

Complete the required contracts and obtain
the full Radiant Oathplate set.

✓ Contract / Oathplate Piece #1
□ Contract / Oathplate Piece #2
□ Contract / Oathplate Piece #3

Progress: 1 / 3

[ ATTEMPT GRAND FATE ]
```

When they click **Attempt Grand Fate**, we show whichever required contracts/objectives remain. They choose the one they're attempting, and **that contract becomes their Active Fate**.

Complete the contract successfully → corresponding Oathplate objective gets permanently checked → Grand Fate attempt ends → return to the normal no-active-Fate state.

Fail/die → nothing already completed resets. They simply have to activate **Attempt Grand Fate** again before another attempt counts.

That also means the completion condition is fundamentally:

> **Successfully complete all of the required Radiant Oathplate contracts and earn the complete set.**

Not “get lucky with three drops.”

When we get to the technical spec, we'll research the exact Yama contract/Oathplate requirements and build the checklist from the real game data rather than guessing them.

### Next

That leaves our two simplest Grand Fates: **Inferno and Colosseum**.

I think these barely need additional rules:

> **Inferno:** Activate Attempt Grand Fate → enter/attempt Inferno → obtain Infernal Cape → Grand Fate complete.

> **Colosseum:** Activate Attempt Grand Fate → attempt Colosseum → complete it / obtain Dizana's Quiver → Grand Fate complete.

Failure just ends that individual attempt. Nothing else happens beyond the normal Fate Audit if they were screwing around instead of pursuing the active Grand Fate.

If you're good with that, I'd consider the **five Grand Fate completion structures essentially designed** and move us to the next major unresolved system. memcite

## S8-M0341 - user - 2026-10-03T19:11:34.882000+00:00
Message ID: 8616f317-4e5d-49c0-a4aa-f3bfb07a2446

i agree, whats next

## S8-M0342 - assistant - 2026-10-03T19:11:37.890146+00:00
Message ID: 776795bc-7d84-42b2-9095-854411a2a2cc

I think **Grand Fates are sufficiently designed now**. We know how they're selected, activated, monitored, failed, resumed, and completed.

The next major system I'd tackle is the **Fate Shop itself**.

We've designed a bunch of things that live there, but we haven't defined the shop as one coherent system yet. Right now it contains things such as:

> Fate's Pardon, Fate's Cleansing, Blessed Item upgrades, potentially Wheel Builder upgrades, Fate Card unlocks, and other permanent/consumable upgrades.

Before we balance any prices, I think we should decide **what the Fate Shop actually is structurally**.

### First decision: one shop or several?

My recommendation is **one central Fate Shop**, divided into categories/tabs. Not four different stores scattered through the plugin.

Conceptually:

```text id="89zk2f"
              FATE SHOP
────────────────────────────────────

 PROGRESSION   WHEEL   MANIPULATION   DEFIANCE
     │           │          │             │
     │           │          │             ├─ Fate's Cleansing
     │           │          │             ├─ Fate's Pardon
     │           │          │             ├─ Blessed Slot II
     │           │          │             └─ Blessed Slot III
     │           │          │
     │           │          ├─ Lesser Fate Cards
     │           │          ├─ Greater Fate Cards
     │           │          ├─ Challenge Cards
     │           │          └─ Wilderness Cards
     │           │
     │           ├─ Duplicate Slices
     │           ├─ Slice Weight Upgrades
     │           └─ Wheel-related upgrades
     │
     └─ Access / progression purchases
```

Not saying those exact categories are final yet—that's what we'd design—but **everything FP can purchase has one understandable home**.

I also think certain inventory should be **hidden until the player discovers the associated mechanic**.

For example, on a fresh account you shouldn't open the Fate Shop and see:

> Fate's Cleansing — Removes Taint  
> Blessed Possessions — Protects items from Death

because that spoils the entire Defy Fate/Sacrifice reveal.

Before first Defy, those things simply **don't exist in the player's shop UI**.

After first Defy:

> 🔓 **NEW FATE SHOP ITEMS AVAILABLE**

and the Defiance/Sacrifice section appears.

That preserves the surprise we've been building around running out of your original Fate.

So that's the next single decision I'd lock:

**One centralized Fate Shop with different sections, with some sections/items only revealed when their underlying mechanic has been encountered?**

I strongly prefer **yes**. memcite

## S8-M0343 - user - 2026-10-03T19:12:06.493000+00:00
Message ID: 3cb27485-d819-4bd3-85b4-33266fd5f9e2

yep My recommendation is **one central Fate Shop**, divided into categories/tabs. Not four different stores scattered through the plugin.

## S8-M0344 - assistant - 2026-10-03T19:12:08.208454+00:00
Message ID: 98bc0096-183b-45fe-9b7a-81a9bc61d36b

**Locked — one centralized Fate Shop.** Everything purchased with FP should have one obvious home, separated into categories/tabs rather than separate shop interfaces.

And we'll keep the **progressive reveal** rule: sections tied to mechanics the player hasn't encountered yet remain hidden. Most importantly, Defy/Taint/Sacrifice shop content doesn't spoil itself before the first Defy.

### Next decision: what belongs in the Fate Shop vs Progression Tree?

We currently have two different ways to spend FP, and I think they need a clean philosophical separation.

My recommendation:

> **Progression Tree = unlock new gameplay/content.**  
> **Fate Shop = modify, manipulate, protect, or recover.**

So unlocking **Slayer**, unlocking a new **Bossing tier**, unlocking higher **Quest tiers**, etc. stays on the Progression Tree.

Things like **Fate's Cleansing, Fate's Pardon, Blessed Slots, Fate Cards, Wheel manipulation/upgrades**, etc. belong in the Fate Shop.

That prevents the tree from becoming a giant shopping catalog and makes the distinction intuitive:

**Tree:** *What can Fate send me to do?*  
**Shop:** *How much control do I have over Fate?*

That's the next rule I'd lock before we actually decide the Fate Shop's tabs and inventory. memcite

## S8-M0345 - user - 2026-10-03T19:13:00.355000+00:00
Message ID: 6cbb7b4f-42df-4ffa-a124-7ed5e382a636

yes i agree locked in&#x20;

**Progression Tree = unlock new gameplay/content.**\
**Fate Shop = modify, manipulate, protect, or recover.**

## S8-M0346 - assistant - 2026-10-03T19:13:01.949175+00:00
Message ID: c41d3ffb-eff0-46d9-adf0-c28b632e65d6

**Locked.**

That gives us a very clean rule for deciding where future mechanics belong:

> **Progression Tree = unlock new gameplay/content.**  
> **Fate Shop = modify, manipulate, protect, or recover.**

### Next: Fate Shop categories

I think we should now establish the actual tabs. My starting recommendation is **four**, but we should approve them one at a time rather than designing the whole thing at once.

The first is:

### 🎡 Wheel Manipulation

This is where the player purchases ways to alter the Master Fate Wheel itself.

Conceptually, this would eventually contain things like:

```text id="zq9l4x"
WHEEL MANIPULATION

Duplicate Slice
Add another copy of an eligible Fate to your wheel.

Slice Enhancement
Increase the weight of a specific slice.

[potential future wheel upgrades]
```

Importantly, this **doesn't unlock the underlying content**.

If you haven't unlocked Slayer through the Progression Tree, the Fate Shop can't sell you another Slayer slice. The tree gives you **access to Slayer**; the shop lets you **manipulate how Slayer appears on your wheel**.

I think that's a strong first Fate Shop category.

**Question:** Do you want **adding/removing normal unlocked slices** themselves to cost FP here too, or should FP only be required for the more powerful manipulation—duplicates, increased weights, etc.?

My inclination is that **adding a normal 1.0x unlocked slice should be cheap but not free**, while removing one should be considerably more expensive, as we'd discussed earlier. That makes actually constructing your wheel part of the FP economy. memcite

## S8-M0347 - user - 2026-10-09T16:04:52.995000+00:00
Message ID: 5e2ea283-cad8-4d08-8a0f-6478be11f11d

its been a while where did we leave off on?

## S8-M0348 - assistant - 2026-10-09T16:04:57.683068+00:00
Message ID: 67105371-985e-4d1a-a9d1-4c8c8558db3d

We left off designing **Wheelbound Mode for your RuneLite plugin**, specifically the **Fate Shop and Wheel Manipulation system**.

We'd made a lot of progress on the overall game design, and we were working through the remaining decisions **one at a time** before writing the full implementation blueprint for Codex.

## Where we currently stand

<box gap={3}>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success" size="lg"/>
    **Core systems largely designed**
  </row>
  <text color="secondary" size="sm">These have established rules, although some implementation details and numerical balancing remain.</text>
  <grid columns={2} gap={2}>
    {#each ["Grand Fates","Master Fate Wheel","Skilling & Combat Fates","Questing & Bossing","Fate Cards","Progression Tree","Fate Points","Bounties","Shop restrictions","Defy Fate & Taint","Death's Sacrifice","Blessed Items"] as item}
      <grid-item>
        <row align="center" gap={2}>
          <icon name="check" color="success"/>
          <text size="sm">{item}</text>
        </row>
      </grid-item>
    {/each}
  </grid>
</box>

### Our most recent decisions

We agreed on two fundamental rules:

- **Progression Tree = unlock new gameplay/content.** Unlock Slayer, bosses, quest tiers, and other activities.
- **Fate Shop = modify, manipulate, protect, or recover.** Buy Fate Cards, duplicate wheel slices, upgrade probabilities, cleanse Taint, protect items, etc.

We also confirmed **one centralized Fate Shop with multiple tabs**, rather than several separate shops.

Importantly, Defy Fate-related items remain hidden until the player has Defied Fate for the first time.

## The exact question we stopped on

We were designing the first Fate Shop category:

<box border radius="lg" padding={3} gap={2}>
  <row align="center" gap={2}>
    <icon name="disc-3" size="lg"/>
    **Wheel Manipulation**
  </row>
  <text color="secondary" size="sm">Controls how the player's unlocked content appears on their Master Fate Wheel.</text>
  <divider color="subtle"/>
  <row justify="between" align="center">
    **Add a normal slice**
    <badge color="secondary">Undecided</badge>
  </row>
  Add a 1.0x slice for content already unlocked through the Progression Tree.
  <divider color="subtle"/>
  **Duplicate Slice**

  Purchase additional copies of an eligible slice.
  <divider color="subtle"/>
  **Slice Enhancement**

  Increase the probability weight of a specific slice.
  <divider color="subtle"/>
  **Remove Slice**

  Remove an ordinary slice from the active wheel. Previously established as substantially more expensive than adding one.
</box>

### The decision we need to make

**Should adding an ordinary, already-unlocked slice cost Fate Points?**

{@body const [choice,setChoice] = DIL.useState("cheap")}
<box gap={2}>
  <radio-group direction="col" value={choice} onChange={setChoice}>
    <radio value="free">**Free** — once unlocked through the tree, you can add it without spending FP.</radio>
    <radio value="cheap">**Cheap FP cost (my recommendation)** — unlocking grants access, but adding the slice costs a small amount.</radio>
    <radio value="scale">**Escalating FP cost** — adding more slices gets progressively more expensive.</radio>
  </radio-group>
  <button block onClick={()=>GenUI.issueNewTurn(choice==="free"?"Lock in free normal slice additions.":choice==="cheap"?"Lock in cheap FP costs for adding normal unlocked slices.":"Lock in escalating FP costs for adding normal unlocked slices.")}>Confirm decision <icon name="arrow-right" inline/></button>
</box>

After that, we'll continue through the Fate Shop categories, then finish the remaining mechanics, balance the entire economy through simulations, design the UI/art, and create the detailed implementation plan for Codex.

**We're still in the design phase. We haven't started implementing Wheelbound Mode itself as part of this design process.**

## S8-M0349 - user - 2026-10-09T16:06:06.739000+00:00
Message ID: ee88caa7-459f-4790-abac-571049178390

Lock in cheap FP costs for adding normal unlocked slices.

## S8-M0350 - assistant - 2026-10-09T16:06:12.675026+00:00
Message ID: 9def5fca-e407-49a3-bf41-d2f12e4b7e20

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Normal Slice Addition Costs**
</row>

Adding an ordinary slice to the Master Fate Wheel will cost a **small, fixed amount of Fate Points**.

- The underlying skill/activity must first be unlocked through the Progression Tree.
- Every normal slice starts at **1.0x weight**.
- Adding a normal slice costs a small amount of FP.
- Duplicate slices and weight enhancements are separate, more expensive purchases.
- Removing an ordinary slice remains substantially more expensive than adding one.
- Exact FP costs remain `BALANCE_TBD`.

### Next decision: Removing slices

We previously agreed removing a slice should be expensive, because otherwise players could constantly rebuild their wheel to avoid undesirable Fates.

But there's an important distinction:

**Should removing a slice permanently destroy that purchased slice, or simply deactivate it?**

My recommendation is **deactivation**.

For example, removing Mining takes it off your active wheel, but Mining remains unlocked in your Progression Tree. You can add it again later by paying the normal addition cost.

That preserves permanent progression without making wheel construction too forgiving.

**Do you agree that removal should only remove the active slice, never revoke the underlying unlock?**

## S8-M0351 - user - 2026-10-09T16:06:34.917000+00:00
Message ID: c379c25f-322d-4ab1-b97b-82a3095a800a

deactivation

## S8-M0352 - assistant - 2026-10-09T16:06:38.830150+00:00
Message ID: 4cbf910b-0671-4b8a-ac9c-de75a37840bb

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Slice Removal = Deactivation**
</row>

Removing a slice from the Master Fate Wheel:

- Removes that specific slice from the active wheel.
- **Does not revoke** the underlying skill, boss, or activity unlock.
- Costs a substantial amount of Fate Points.
- Allows the player to add the slice again later for the normal, inexpensive addition cost.
- Does not affect other duplicate slices of the same activity.

Exact costs remain `BALANCE_TBD`.

### Next decision: What happens to upgraded slices?

Suppose the player has purchased upgrades for a Slayer slice:

```text
Slayer Slice A — 2.0x weight
Slayer Slice B — 1.0x weight
```

They decide to deactivate **Slayer A**.

Should its purchased 2.0x upgrade be preserved?

My recommendation is **yes**. Deactivation should move the upgraded slice into inactive storage. Reactivating it costs the normal addition fee, but it returns at 2.0x.

Otherwise players could accidentally destroy expensive upgrades simply by reorganizing their wheel.

**Do we preserve upgrades on deactivated slices, or should removing a slice reset it to 1.0x?**

## S8-M0353 - user - 2026-10-09T16:07:22.283000+00:00
Message ID: 4b3616d4-70d3-4fd5-af68-5a5dbcd01572

yes, slices should have a modifer class that can keep track of any other modifiers we have

## S8-M0354 - assistant - 2026-10-09T16:07:26.116311+00:00
Message ID: 6116ddd5-4c93-428b-b3ff-c28577cb8ce8

**Locked — Slice Modifiers and Persistence.**

I agree. We should design each wheel slice as an independent object with its own modifiers, rather than storing everything against the underlying skill or activity.

### Proposed architecture

```java
class WheelSlice {
    UUID id;
    FateType fateType;
    boolean active;

    SliceModifiers modifiers;
}

class SliceModifiers {
    double weightMultiplier;
    // Additional modifiers added as systems evolve
}
```

This is conceptual architecture, not final implementation code.

Each slice gets a unique ID, so two Slayer slices can have completely different upgrades.

For example:

| Slice | Weight | Status |
|---|---|---|
| Slayer A | 2.0x | Active |
| Slayer B | 1.0x | Active |
| Mining A | 1.5x | Inactive |

When Mining A is reactivated, it retains its **1.5x modifier**.

This also gives us room for future modifiers without redesigning the underlying wheel system.

One important architectural distinction: **Taint and Sacrifice are special slice types with their own restrictions**, rather than ordinary slices players can freely deactivate.

### Next decision: Do inactive slices count toward wheel capacity?

My recommendation is **no**.

If the wheel has a working maximum of 24 slices, that means **24 active slices**, not 24 total owned slices.

Players can maintain a collection of inactive, upgraded slices and pay FP to swap their active wheel configuration.

However, deactivation remains expensive, so changing configurations frequently still has a meaningful cost.

**Do you agree?**

## S8-M0355 - user - 2026-10-09T16:08:43.783000+00:00
Message ID: 22c412ae-5c6f-4c50-821d-c2827eb7778e

no only active slices that are on the wheel count. Now maybe we have an invetory? like a bag that can hold so many (upgrade available in the shop add 3 tiers to this, come up with the number of available slots etc )

## S8-M0356 - assistant - 2026-10-09T16:08:48.465198+00:00
Message ID: 18b7d159-2634-434f-9c79-b4000a724cc4

I like that. It gives the Wheel Builder another meaningful progression system without increasing the actual wheel's capacity.

**Locked:** Only active slices count toward the Master Fate Wheel's capacity.

Let's introduce a **Fate Satchel** — an inventory for storing inactive slices, including their individual modifiers.

## Fate Satchel — proposed design

<box border radius="xl" padding={3} gap={3}>
  <row align="center" justify="between">
    <row align="center" gap={2}>
      <icon name="backpack" size="lg"/>
      **Fate Satchel**
    </row>
    <badge>Proposed</badge>
  </row>
  <text color="secondary" size="sm">Storage for deactivated wheel slices. Upgrades purchased from the Fate Shop.</text>
  {@body const tiers=[{name:"Starting Satchel",slots:3,cost:"Free",desc:"Enough space to save a few upgraded slices."},{name:"Satchel Expansion I",slots:6,cost:"Low FP",desc:"More flexibility for rearranging your wheel."},{name:"Satchel Expansion II",slots:10,cost:"Moderate FP",desc:"Maintain several alternative Fate slices."},{name:"Satchel Expansion III",slots:15,cost:"High FP",desc:"A substantial reserve for late-game wheel builds."}]}
  {@body const [tier,setTier]=DIL.useState(0)}
  <row align="center" justify="between">
    <text weight="medium">{tiers[tier].name}</text>
    <text color="secondary" size="xs">{tiers[tier].slots} slots</text>
  </row>
  <grid columns={5} gap={2}>
    {#each Array.from({length:tiers[tier].slots},(_,i)=>i) as i}
      <grid-item>
        <box background="surface-secondary" radius="md" aspectRatio="1" align="center" justify="center" border>
          {#if i===0}
            <icon name="pickaxe" color="default" size="lg"/>
          {:else if i===1}
            <icon name="swords" color="default" size="lg"/>
          {:else if i===2}
            <icon name="fish" color="default" size="lg"/>
          {:else}
            <icon name="plus" color="tertiary" size="sm"/>
          {/if}
        </box>
      </grid-item>
    {/each}
  </grid>
  <caption>Illustrative inventory: stored Mining, Combat, and Fishing slices. Their modifiers are preserved.</caption>
  <divider color="subtle"/>
  <row justify="between" align="center">
    <text color="secondary" size="sm">Upgrade cost</text>
    **{tiers[tier].cost}**
  </row>
  <text size="sm">{tiers[tier].desc}</text>
  <row align="center" gap={2}>
    <button color="secondary" variant="outline" disabled={tier===0} onClick={()=>setTier(v=>v-1)}><icon name="chevron-left" inline/> Previous</button>
    <spacer/>
    <button disabled={tier===3} onClick={()=>setTier(v=>v+1)}>Next tier <icon name="chevron-right" inline/></button>
  </row>
</box>

### How it would work

- **Active wheel:** Up to 24 slices (working maximum).
- **Fate Satchel:** Starts with 3 storage slots, expandable to 15 through three shop upgrades.
- **Deactivating a slice:** Costs FP and moves the slice into an available Satchel slot.
- **Reactivating a slice:** Costs the normal addition fee and returns it to the wheel with all modifiers intact, freeing its Satchel slot.
- **Duplicates:** Each copy occupies its own slot and retains its unique modifiers.
- **Full Satchel:** You cannot deactivate another slice into storage until you free space.

I would also allow players to **permanently discard an ordinary inactive slice** to free Satchel space, with a strong confirmation. That destroys its purchased modifiers, but never removes the underlying progression unlock. Special Tainted and Sacrifice slices would remain subject to their separate rules.

The Satchel upgrades would live under **Fate Shop → Wheel Manipulation**, with escalating FP prices. Exact costs remain `BALANCE_TBD`.

### One decision before we lock this

Do you like **3 → 6 → 10 → 15 slots**, or would you prefer a smaller Satchel so players have to be more selective about which upgraded slices they preserve?

## S8-M0357 - user - 2026-10-09T16:10:09.440000+00:00
Message ID: ae0c552a-eaf7-401e-af6e-4426e5f11833

yes if you destroy a slice all modifiers are gone but what you unlocked to get that slice how it was is still arounfd so you could get the same modifers on another slice

## S8-M0358 - assistant - 2026-10-09T16:10:14.114164+00:00
Message ID: 8360ccfa-cb82-4caa-a1b9-b45e350fe431

**Locked — Slice Destruction and Modifier Unlocks.**

That's an important distinction: **destroying a slice destroys its purchased upgrades, not the player's ability to purchase those upgrades again.**

We now have three separate concepts:

| Component | What happens when a slice is destroyed? |
|---|---|
| **Progression unlock** | Permanently retained |
| **Modifier unlock** | Permanently retained |
| **Individual slice and its upgrades** | Destroyed |

For example, suppose you've unlocked Slayer and purchased the ability to enhance Slayer slices up to 2.0x.

You own:

```text
Slayer A — 2.0x
Slayer B — 1.0x
```

You destroy Slayer A.

- Slayer remains unlocked.
- The ability to purchase 2.0x slice enhancements remains unlocked.
- Slayer A and its purchased enhancements are permanently lost.
- You can create another Slayer slice and purchase those enhancements again.

**The Fate Satchel is also locked at 3 → 6 → 10 → 15 slots**, with three increasingly expensive shop upgrades.

### Next decision: Destroying slices

Should destroying an ordinary slice be **free**, or should it cost Fate Points?

My recommendation is **free**. You're already permanently sacrificing whatever FP you invested into that slice and its modifiers. Charging additional FP to destroy it feels unnecessarily punitive.

That would create three distinct actions:

- **Deactivate:** Pay FP, preserve the slice in your Satchel.
- **Reactivate:** Pay a small FP fee, restore the slice and its modifiers.
- **Destroy:** Free, permanently lose the slice and its purchased modifiers.

Would you lock that in?

## S8-M0359 - user - 2026-10-09T16:11:01.954000+00:00
Message ID: 146f5e41-2c42-4ec9-b133-93dc56bb3b1c

free

## S8-M0360 - assistant - 2026-10-09T16:11:06.359547+00:00
Message ID: 56433c7c-b3a3-4d58-b353-ffea0694f994

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Destroying a Slice Is Free**
</row>

Our Wheel Builder now has three distinct actions:

| Action | FP Cost | Result |
|---|---|---|
| Deactivate | Expensive | Moves slice into Fate Satchel; preserves modifiers |
| Reactivate | Cheap | Returns stored slice to active wheel |
| Destroy | **Free** | Permanently destroys slice and its purchased modifiers |

All underlying progression and modifier unlocks remain available, so players can rebuild destroyed slices by spending FP again.

### Next decision: What happens when the Fate Satchel is full?

Suppose the player has 3/3 stored slices and wants to deactivate another.

My recommendation is that the player must either **destroy an existing stored slice**, reactivate one to free a slot, or purchase a Satchel expansion.

We should **never automatically destroy a slice** or silently discard purchased modifiers.

One additional consideration: when Taint forces an ordinary slice off a full Master Wheel, I recommend letting the player choose a slice to displace and storing it in the Satchel. If the Satchel is full, they must resolve that first.

**Do you agree that Taint should follow the same storage rules, rather than getting a free exception?**

## S8-M0361 - user - 2026-10-09T16:11:42.723000+00:00
Message ID: 28d326e3-c953-4722-91c2-c3760d091a2d

you cant store a tained slice, the only way to get rid of it is to cleanse it from the wheel by purchsing the item in the shop

## S8-M0362 - assistant - 2026-10-09T16:11:59.673821+00:00
Message ID: f9ef26a6-c1be-4727-b45a-a9dbb53d917e

**Locked — Tainted Slices Cannot Be Stored.**

Tainted slices are permanently bound to the Master Fate Wheel until cleansed.

- **Cannot deactivate** a Tainted slice.
- **Cannot store** it in the Fate Satchel.
- **Cannot destroy** it using the free destruction option.
- **Cannot modify** its weight through ordinary Wheel Manipulation.
- **Can only remove it** using Fate's Cleansing, purchased from the Fate Shop after the first Defy Fate.

When a Tainted slice is cleansed, its slot becomes available again.

### One important distinction

If Taint enters an already-full wheel, the player must choose an **ordinary slice** to displace.

That ordinary slice can go into the Fate Satchel, provided there's room. If the Satchel is full, the player must free space or expand it before proceeding.

The **Tainted slice itself never enters the Satchel**.

### Next decision

What if the Master Wheel is completely full of protected slices—Taint and the permanent Sacrifice slice—with no ordinary slice available to displace?

My recommendation is that **Defy Fate should still be possible**, but any additional Taint should increase the weight of an existing Tainted slice instead of requiring another slot.

That prevents a hardlock while making repeated Defiance increasingly dangerous.

Would you prefer that, or should we establish a maximum number of Tainted slices and stop adding Taint once the wheel reaches that limit?

## S8-M0363 - user - 2026-10-09T16:12:40.536000+00:00
Message ID: 6cad6c4a-a559-46af-b3a6-4b22e1995015

remind me what is a protected slice and how we protect something

## S8-M0364 - assistant - 2026-10-09T16:12:44.465807+00:00
Message ID: cc16f55b-f832-42e1-8e6d-b9f54829bdf4

Good question. **We actually haven't designed a mechanic for protecting ordinary wheel slices.** I used the term "protected slices" loosely in my previous response.

There are two different concepts here.

### 1. Blessed Items (already locked)

These protect your **OSRS possessions**, not wheel slices.

When you first Defy Fate, you receive one free Blessed Item slot. You can purchase two additional slots from the Fate Shop.

For example, blessing your Twisted Bow means Death cannot select that item type when you land on the Sacrifice slice.

### 2. Special wheel slices (already locked)

These are slices that cannot be removed through normal Wheel Builder actions.

| Slice | Can deactivate? | How to remove |
|---|---|---|
| Normal Mining slice | Yes | Deactivate or destroy |
| Normal Slayer slice | Yes | Deactivate or destroy |
| Tainted slice | No | Fate's Cleansing |
| Sacrifice slice | No | Cannot be removed |

**There is currently no purchasable protection for normal slices.**

When I said "protected slices," I meant Tainted and Sacrifice slices, which have special restrictions.

### My recommendation

I wouldn't introduce another protection mechanic right now. We already have Blessed Items for possessions, and special rules for Taint and Sacrifice.

That brings us back to the unresolved question:

**What happens when the wheel fills completely with Tainted slices and the permanent Sacrifice slice?**

I recommend additional Taint increases an existing Tainted slice's weight instead of requiring another slot. That way, Defy Fate remains possible, but the wheel becomes increasingly corrupted.

## S8-M0365 - user - 2026-10-09T16:13:21.380000+00:00
Message ID: 281aac44-51ad-466b-a90a-7a8fae3c559d

no only items are protected , and this is specifically for the scarfice mechanic, slices do not have protection

## S8-M0366 - assistant - 2026-10-09T16:13:25.531164+00:00
Message ID: 3e63d96e-487b-4ab0-855f-03f25e46bf98

**Confirmed and locked — only OSRS items can be Blessed. Wheel slices have no protection mechanic.**

The distinction is:

- **Blessed Items:** Protect specific OSRS item types from Death's Forced Sacrifice.
- **Normal Wheel Slices:** Can be deactivated, stored in the Fate Satchel, or destroyed.
- **Tainted Slices:** Cannot be stored or normally removed. They require Fate's Cleansing.
- **Sacrifice Slice:** Permanently remains on the wheel after the first Defy Fate.

Tainted and Sacrifice slices are **restricted by their own mechanics**, not protected by a player-purchased blessing.

### Next decision

We still have one edge case: what happens if repeated Defiance fills the entire wheel with Tainted slices and the permanent Sacrifice slice?

My recommendation is that additional Taint increases the weight of an existing Tainted slice rather than adding another physical slice.

That preserves the wheel's capacity while making further Defiance increasingly dangerous.

**Would you prefer that, or should Taint always occupy a separate physical slice?**

## S8-M0367 - user - 2026-10-09T16:14:00.377000+00:00
Message ID: 56972e5c-dcae-473f-b7ad-e9ec4e150d85

we add up to x slices, and then they start growing bigger as you defy more

## S8-M0368 - assistant - 2026-10-09T16:14:07.346159+00:00
Message ID: 858cf292-cd38-41bf-a955-590293c820bb

I like that. **Locked — Taint has two stages: accumulation and growth.**

### Stage 1 — Taint Accumulation

Every other Defy Fate, starting with the first, introduces Taint.

New Tainted slices occupy actual wheel slots until the maximum number of Tainted slices is reached.

### Stage 2 — Taint Growth

Once the maximum is reached, subsequent Taint events increase the weight of existing Tainted slices instead of creating new ones.

This means the wheel becomes increasingly corrupted without eventually filling every slot.

### My proposed balance

<box border radius="lg" padding={3} gap={3}>
  <text weight="medium">Taint progression example</text>
  <table>
    <table-row><table-cell>Defy #1</table-cell><table-cell>1 Tainted slice</table-cell><table-cell>1.0x</table-cell></table-row>
    <table-row><table-cell>Defy #3</table-cell><table-cell>2 Tainted slices</table-cell><table-cell>1.0x each</table-cell></table-row>
    <table-row><table-cell>Defy #5</table-cell><table-cell>3 Tainted slices</table-cell><table-cell>1.0x each</table-cell></table-row>
    <table-row><table-cell>Defy #7</table-cell><table-cell>3 Tainted slices</table-cell><table-cell>1.25x each</table-cell></table-row>
    <table-row><table-cell>Defy #9</table-cell><table-cell>3 Tainted slices</table-cell><table-cell>1.50x each</table-cell></table-row>
  </table>
  <caption>Illustrative weights; exact balancing can be tuned during simulation.</caption>
</box>

I'd recommend a **maximum of 3 Tainted slices** initially, with each subsequent Taint event increasing all existing Tainted slices by 0.25x.

The permanent Sacrifice slice still grows independently by 0.25x on every Defy after the first.

Importantly, **Fate's Cleansing can still remove a Tainted slice**, and the remaining Tainted slices retain their accumulated weight.

### One question

Should we lock the maximum at **3 Tainted slices**, or would you prefer **5** to make corruption consume more physical space on the wheel before the slices begin growing?

## S8-M0369 - user - 2026-10-09T16:14:27.086000+00:00
Message ID: 5a1940eb-2819-47bc-8f45-3702b02c8541

3 is fine

## S8-M0370 - assistant - 2026-10-09T16:14:32.483812+00:00
Message ID: 76fafe3c-f730-4597-90eb-223655146593

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Maximum 3 Tainted Slices**
</row>

The Taint system now works as follows:

- **Maximum:** 3 Tainted slices on the Master Fate Wheel.
- **Accumulation:** New Tainted slices are introduced on Defy #1, #3, and #5.
- **Growth:** Starting with Defy #7, each subsequent Taint event increases the weight of all existing Tainted slices by **0.25x**.
- **Cleansing:** Fate's Cleansing removes one Tainted slice, regardless of its accumulated weight.
- **Sacrifice:** Its permanent slice continues growing independently by 0.25x per Defy after the first.

The maximum is three *simultaneously active* Tainted slices. If the player cleanses one, a future Taint event can fill that vacant Taint position before growth resumes.

### Next decision: Fate Satchel capacity

There's one edge case worth resolving before we finish Wheel Manipulation.

Suppose Taint enters a full Master Wheel, and the player must displace an ordinary slice, but their Fate Satchel is also full.

I recommend giving them a choice: **destroy an ordinary slice for free, or purchase additional Satchel capacity if an upgrade is available.**

This means Taint can force a genuinely painful decision, but it never creates an unavoidable hardlock.

**Do you agree?**

## S8-M0371 - user - 2026-10-09T16:15:18.024000+00:00
Message ID: 81448448-70e3-4104-b6d1-fdfc1e45a184

yeah , if they can otherwise they will have to destroy a slice

## S8-M0372 - assistant - 2026-10-09T16:15:23.071563+00:00
Message ID: aeb2e8b9-d36a-4585-ac8d-45e3fe6e0e2b

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Taint Displacement Rules**
</row>

When Taint enters a full Master Fate Wheel, the player must make room.

1. **Choose an ordinary slice** to remove from the wheel.
2. **Store it in the Fate Satchel** if space is available, paying the normal deactivation cost.
3. If the Satchel is full, the player can purchase a capacity upgrade if one remains available.
4. **If storage isn't possible, the player must destroy an ordinary slice.** Destruction is free, but all modifiers purchased for that slice are permanently lost.

Taint cannot be stored, destroyed, or bypassed. The permanent Sacrifice slice also cannot be displaced.

This gives Defy Fate another meaningful consequence: corruption can force players to sacrifice valuable wheel upgrades, not just accept more punishments.

### Next decision: Fate Satchel upgrades

We've established four capacity levels:

| Tier | Total storage |
|---|---:|
| Starting Satchel | 3 slots |
| Expansion I | 6 slots |
| Expansion II | 10 slots |
| Expansion III | 15 slots |

One thing remains: **should these expansions be purchasable immediately, or unlocked gradually through progression?**

My recommendation is that all three are visible in the Fate Shop from the beginning, but must be purchased sequentially. Their escalating FP costs naturally prevent most early-game players from buying the largest Satchel.

That also avoids adding unnecessary requirements to the Progression Tree.

**Do you agree?**

## S8-M0373 - user - 2026-10-09T16:15:57.114000+00:00
Message ID: 0b9fdbad-771d-497f-bbf1-8bb75c2de83b

sequentially purchased , but visible off rip in the shop

## S8-M0374 - assistant - 2026-10-09T16:16:01.036120+00:00
Message ID: e6f867cd-efe3-4b66-ae5c-e72d8e12507e

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Fate Satchel Upgrades**
</row>

All Fate Satchel expansions are **visible in the Fate Shop from the beginning**, but must be purchased sequentially.

| Upgrade | Total Capacity | Availability |
|---|---:|---|
| Starting Satchel | 3 slots | Free |
| Expansion I | 6 slots | Purchasable immediately |
| Expansion II | 10 slots | Requires Expansion I |
| Expansion III | 15 slots | Requires Expansion II |

Each expansion has an increasingly expensive FP cost (`BALANCE_TBD`).

Players can see what they're working toward without needing to unlock the upgrades through the Progression Tree.

### Next decision: Slice Weight Enhancements

We previously established that individual slices can have their own weight modifiers.

For example:

- Slayer A — 1.0x
- Slayer B — 1.5x
- Mining A — 2.0x

I'd recommend **four enhancement levels**:

| Level | Slice Weight |
|---|---:|
| Base | 1.0x |
| Enhancement I | 1.25x |
| Enhancement II | 1.5x |
| Enhancement III | 2.0x |
| Enhancement IV | 3.0x |

Each level must be purchased sequentially for that specific slice. Costs increase substantially at higher levels.

The underlying enhancement levels remain permanently unlocked once earned, but the actual weight upgrades belong to individual slices and are lost if that slice is destroyed.

**Would you lock these four enhancement levels in, or would you prefer a lower maximum weight to prevent one slice from dominating the wheel?**

## S8-M0375 - user - 2026-10-09T16:16:21.381000+00:00
Message ID: c42c6f15-d443-43fd-905e-2e7deae17a20

sure as we go about his balance them accordingly

## S8-M0376 - assistant - 2026-10-09T16:16:25.778004+00:00
Message ID: 40629445-86f8-4973-b401-744a3b8d0cc2

**Locked — Slice Weight Enhancements**, with final balancing deferred until we simulate the complete Fate Points economy.

| Enhancement | Weight |
|---|---:|
| Base | 1.0x |
| I | 1.25x |
| II | 1.5x |
| III | 2.0x |
| IV | 3.0x |

These are our initial values, subject to balancing.

Each upgrade is purchased for an **individual slice**, must be purchased sequentially, and survives deactivation and storage. Destroying the slice destroys its purchased upgrades, but not the ability to buy them again.

### Next decision: Duplicate slices

We've established that players can purchase duplicate slices to increase their chances of landing on a particular activity.

For example:

```text
Slayer A — 2.0x
Slayer B — 1.0x
Slayer C — 1.5x
```

My recommendation is **no fixed per-activity duplicate limit**.

Instead, duplicates are naturally constrained by three things: the Master Wheel's active capacity, the FP cost of creating duplicates, and the opportunity cost of filling slots that could hold other activities.

That would let someone pursuing Master Combat Achievements build a heavily Bossing-focused wheel, provided they've invested enough FP.

**Should we allow unlimited duplicates within the wheel's overall capacity, or cap each activity at something like three copies?**

## S8-M0377 - user - 2026-10-09T16:16:45.935000+00:00
Message ID: f026f4c1-f045-412d-92c7-f864b974181f

i agree they should be able to duplicate as much as they want

## S8-M0378 - assistant - 2026-10-09T16:16:49.706310+00:00
Message ID: 5eea1af9-b8c1-4d43-a15a-ede7f6d4449b

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Unlimited Duplicate Slices**
</row>

Players can duplicate any eligible, unlocked ordinary slice **as many times as they want**, subject only to available wheel capacity and FP costs.

- Every duplicate is a separate slice with its own unique ID and modifiers.
- Each duplicate starts at **1.0x weight**.
- Each duplicate can be upgraded independently.
- Duplicates occupy individual Master Wheel slots.
- Duplicate slices can be stored in the Fate Satchel or destroyed.
- Special Tainted and Sacrifice slices cannot be duplicated.

This means someone pursuing Master Combat Achievements could build a wheel containing 15 Bossing slices if they wanted to invest that heavily.

### Next decision: Duplicate pricing

I recommend **escalating costs for duplicates of the same activity**.

For example:

| Bossing Slice | Purchase Cost |
|---|---|
| First | Normal addition cost |
| Second | Low duplicate cost |
| Third | Moderate duplicate cost |
| Fourth | High duplicate cost |
| Fifth onward | Increasingly expensive |

The exact FP amounts will be determined during balancing.

Importantly, I recommend pricing based on **how many duplicates have ever been purchased for that activity**, not how many are currently active.

Otherwise players could destroy duplicates to reset their prices and exploit the system.

**Do you agree with escalating duplicate costs per activity, or should every duplicate have the same fixed FP price?**

## S8-M0379 - user - 2026-10-09T16:17:04.622000+00:00
Message ID: 6431e769-eb13-46d7-b467-0de27992d659

yes

## S8-M0380 - assistant - 2026-10-09T16:17:12.923335+00:00
Message ID: b2244bc9-ab6d-430f-96e0-1bfc69bd8264

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Escalating Duplicate Slice Costs**
</row>

Duplicate slices will become progressively more expensive **for each individual activity**.

For example, purchasing additional Bossing slices increases the cost of future Bossing duplicates, but does not affect Mining duplicates.

- **No duplicate limit**, beyond overall wheel capacity.
- Costs escalate independently for each activity.
- Pricing tracks lifetime duplicate purchases, not currently active copies.
- Destroying or deactivating duplicates **does not reset their purchase history**.
- All costs remain `BALANCE_TBD`.

This gives players freedom to specialize their wheels without making extreme builds inexpensive.

### Next decision: Wheel capacity

We've been using **24 active slices** as our working maximum, but deliberately left it open for balancing.

I recommend making wheel capacity **fixed**, rather than selling additional wheel slots in the Fate Shop.

Why? Because expanding the wheel could dilute the consequences of Taint and Sacrifice. The Fate Satchel already provides expandable storage without increasing the number of active slices.

We can test whether 24 is the right maximum during simulations.

**Should we lock wheel capacity as fixed, with no purchasable expansions, while leaving the exact maximum subject to balancing?**

## S8-M0381 - user - 2026-10-09T16:17:37.450000+00:00
Message ID: 61d26d2c-8ffe-48d1-91a7-5d59b784f7e8

definatly fixed 24 for now

## S8-M0382 - assistant - 2026-10-09T16:17:44.001823+00:00
Message ID: 516422ba-9162-44d3-b3c8-95d670bd798f

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Master Fate Wheel Capacity: 24 Slices**
</row>

The Master Fate Wheel has a **fixed maximum of 24 active slices**, with no purchasable capacity expansions.

- Ordinary slices and duplicates each occupy one slot.
- Tainted slices occupy slots (maximum 3).
- The permanent Sacrifice slice occupies one slot.
- Inactive slices are stored separately in the Fate Satchel.
- Slice weight upgrades change probability, **not slot usage**.
- The 24-slot maximum can be revisited during playtesting if necessary.

### Next: Wheel Manipulation — Fate Shop

We've now established most of this category: adding, removing, storing, destroying, duplicating, and enhancing slices.

There's one remaining pricing question I'd settle.

**Should slice weight upgrades become more expensive based on the enhancement level, or based on how many upgrades the player has purchased across their entire wheel?**

My recommendation is **enhancement-level pricing**.

For example, upgrading a slice from 2.0x to 3.0x should always be expensive, but upgrading another slice from 1.0x to 1.25x should remain relatively affordable.

This encourages players to experiment with multiple slices instead of punishing them for having upgraded other parts of their wheel.

Would you agree?

## S8-M0383 - user - 2026-10-09T16:17:54.799000+00:00
Message ID: 0f871b8f-1510-47a1-aa19-c1bf0f3bfc24

both

## S8-M0384 - assistant - 2026-10-09T16:18:00.201805+00:00
Message ID: b969db3c-271f-47a6-a0b8-4e1171f50231

**Locked — Slice Weight Upgrade Costs Use Both Scaling Systems.**

I like this because it makes specialization increasingly expensive while still allowing players to experiment.

Every weight enhancement's FP cost depends on two factors:

1. **Enhancement level:** Higher weight upgrades cost progressively more.
2. **Total upgrades purchased:** Each weight enhancement purchased across the entire Master Wheel increases the cost of future enhancements.

### Example pricing (illustrative only)

| Enhancement | Base FP | Early-game cost | After many upgrades |
|---|---:|---:|---:|
| 1.0x → 1.25x | 10 | 10 | 25 |
| 1.25x → 1.5x | 20 | 20 | 50 |
| 1.5x → 2.0x | 40 | 40 | 100 |
| 2.0x → 3.0x | 80 | 80 | 200 |

All values are `BALANCE_TBD`.

The global upgrade counter tracks **lifetime purchases**, so destroying upgraded slices doesn't reset pricing.

This creates a meaningful decision: upgrade a few slices heavily, or spread your enhancements across the wheel.

### Next decision: Should the player see future upgrade prices?

My recommendation is **yes**.

When selecting a slice, the Wheel Builder should show its current weight, next upgrade, current FP cost, and how much the global upgrade count affects that price.

Players should understand why upgrades are becoming more expensive.

**Should we also show the prices of all four enhancement levels in advance, including ones the player hasn't reached yet?**

## S8-M0385 - user - 2026-10-09T16:18:46.356000+00:00
Message ID: dcea0f71-0538-44eb-8013-bde8815cd1e0

sorry let me walk that back, only slices with the same effect should increase but buying 10 upgrades should no increase the cost of a tier 1 unlock

## S8-M0386 - assistant - 2026-10-09T16:18:52.211919+00:00
Message ID: 3fcb825d-cfd6-45bd-84ff-a27ea217cfca

Got it. **We're replacing the global upgrade counter with per-enhancement-tier pricing.**

### Locked — Slice Enhancement Pricing

Each enhancement tier has its **own independent purchase counter**.

For example:

| Enhancement | Times purchased | Effect on next purchase |
|---|---:|---|
| Tier I: 1.0x → 1.25x | 10 | Tier I becomes more expensive |
| Tier II: 1.25x → 1.5x | 3 | Only Tier II pricing increases |
| Tier III: 1.5x → 2.0x | 1 | Only Tier III pricing increases |
| Tier IV: 2.0x → 3.0x | 0 | Tier IV remains at its starting price |

**Buying ten Tier I upgrades does not increase the cost of Tier II, III, or IV.**

Likewise, purchasing Tier IV upgrades won't affect Tier I prices.

The counters track lifetime purchases, so destroying slices won't reset prices.

This gives us:
- Increasing costs for repeatedly purchasing the same enhancement.
- Higher base prices for more powerful tiers.
- No global penalty for upgrading lots of different slices.

Exact prices and escalation brackets remain `BALANCE_TBD`.

### One clarification before moving on

Should these counters be **shared across all skills and activities**?

For example, purchasing Tier I for Mining would increase the next Tier I price for Slayer.

My recommendation is **yes**. Each enhancement tier has one global counter, regardless of which slice receives it. This prevents players from avoiding escalation by spreading upgrades across different skills.

## S8-M0387 - user - 2026-10-09T16:19:30.834000+00:00
Message ID: a3ed1c28-49dd-421d-a08f-dd57cf557111

to be clear i want this 

My recommendation is enhancement-level pricing.

## S8-M0388 - assistant - 2026-10-09T16:19:34.950300+00:00
Message ID: c47a9f4a-012f-4470-91e9-bf25a6d64eda

Understood. **Locked — Enhancement-Level Pricing ONLY.**

We're removing all purchase-count escalation for slice weight enhancements.

Each enhancement tier has a **fixed FP price**, regardless of how many upgrades the player has previously purchased.

| Enhancement | FP Cost |
|---|---|
| Tier I: 1.0x → 1.25x | Cheap |
| Tier II: 1.25x → 1.5x | Moderate |
| Tier III: 1.5x → 2.0x | Expensive |
| Tier IV: 2.0x → 3.0x | Very expensive |

Exact prices remain `BALANCE_TBD`.

For example, upgrading Mining from 1.0x to 1.25x costs exactly the same as upgrading Slayer from 1.0x to 1.25x, regardless of how many other slices you've enhanced.

**Important distinction:** Duplicate slice purchases still have escalating costs per activity, as previously agreed. This change applies only to weight enhancements.

That settles the pricing model for Wheel Manipulation.

**Next decision:** Should players be able to rearrange the visual positions of slices on the wheel for free? I'd recommend yes, since their position doesn't change the probability of landing on them.

## S8-M0389 - user - 2026-10-09T16:19:47.932000+00:00
Message ID: 2b60786b-cdae-4ad1-9545-434aa2bc00f9

sure

## S8-M0390 - assistant - 2026-10-09T16:19:54.073337+00:00
Message ID: f6295aeb-c4e1-4b59-8da6-8d9c547f68ee

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Free Wheel Rearrangement**
</row>

Players can freely rearrange slices on the Master Fate Wheel through the Wheel Builder.

- Rearranging costs **0 Fate Points**.
- Slice modifiers and upgrades remain unchanged.
- Moving slices does not change their probability of being selected.
- Tainted and Sacrifice slices can be visually repositioned, but cannot be removed through rearrangement.

That completes the core **Wheel Manipulation** category of the Fate Shop!

### Next: Fate Card Manipulation

We've already established that Skilling and Bossing have separate Fate Card pools, and players initially draw two cards when choosing a Fate.

The shop can unlock Lesser, Greater, Challenge, and Wilderness cards.

**Next decision:** Should Fate Card unlocks be permanent purchases?

My recommendation is **yes**. Once you buy Greater Skilling Fate, it permanently enters your eligible Skilling card pool. You never have to purchase that card again, although it isn't guaranteed to appear in every draw.

That keeps Fate Cards as meaningful progression upgrades rather than consumables.

## S8-M0391 - user - 2026-10-09T16:20:44.296000+00:00
Message ID: 1779718a-0b0d-4f68-b2b1-787ae792d562

yes, for future descisions please go with your reccomended descion and present me with ones that you arent too sure about

## S8-M0392 - assistant - 2026-10-09T16:20:48.453133+00:00
Message ID: 09a6e069-21c7-4b20-a823-7f854673f3af

Understood. I'll change how we approach the rest of Wheelbound's design.

**Locked — Fate Card purchases are permanent.** Once purchased, a card enters its respective eligible card pool permanently. It isn't guaranteed to appear in every draw.

### Going forward

I'll take more ownership of the design decisions:

- **Straightforward decisions:** I'll choose the recommended approach and treat it as our working design.
- **Balance decisions:** I'll establish initial values and refine them later through simulations.
- **Important trade-offs:** I'll bring these to you when there are genuinely compelling alternatives.
- **Major gameplay changes:** I'll still get your approval when a decision could significantly change Wheelbound's identity, difficulty, or player experience.
- **Technical uncertainties:** I'll investigate feasibility rather than inventing solutions.

I'll also flag decisions I think are problematic and explain why.

The goal is to finish a cohesive, balanced design without making you approve every minor detail.

### What's next

I'll work through the remaining **Fate Shop inventory, Fate Card mechanics, progression edge cases, and economy rules** using that approach.

Then we'll move into the dedicated UI/art design phase, followed by the technical architecture and detailed Codex implementation blueprint.

**I'll only interrupt that process for decisions where your input materially matters.**

## S8-M0393 - user - 2026-10-09T16:21:05.458000+00:00
Message ID: 4354b7e2-6f4b-4f4b-b7ad-39299e8a8611

great whats next

## S8-M0394 - assistant - 2026-10-09T16:21:09.966375+00:00
Message ID: c614cb6c-256a-4c87-bf54-16466204ce43

Next, I want to **finish the Fate Shop and move us closer to implementation**. I'll handle the straightforward decisions automatically, as you requested.

## Fate Shop — Remaining Design

We've finished **Wheel Manipulation**. Here's how I'd finalize the other categories.

<box gap={3}>
  <row align="start" gap={3}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **1. Wheel Manipulation — Complete**

      <text color="secondary" size="sm">Slice additions, duplicates, enhancements, Fate Satchel upgrades, deactivation, and destruction.</text>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **2. Fate Manipulation — Recommended design**

      <text color="secondary" size="sm">Permanent Lesser, Greater, Challenge, and Wilderness Fate Card unlocks. Separate card pools for Skilling and Bossing, plus upgrades from 2 to 3 drawn cards.</text>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **3. Defiance & Protection — Recommended design**

      <text color="secondary" size="sm">Fate's Cleansing, Fate's Pardon, and the two increasingly expensive Blessed Item slot upgrades. This category is revealed after the first Defy Fate, with Pardon shown when shop banning is introduced.</text>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <icon name="circle-dashed" color="secondary" size="lg"/>
    <box flex="1" gap={1}>
      **4. Account Access — Needs one important decision**

      <text color="secondary" size="sm">Grand Exchange access and individual NPC shop unlocks. We already designed their FP costs and requirements, but need to settle where purchases happen.</text>
    </box>
  </row>
</box>

## The one decision I want your input on

We previously designed NPC shops so that approaching a locked shopkeeper would let the player pay FP to unlock that specific vendor or gamble with **Tempt Fate**.

We also designed a separate Account Access screen where players could browse their unlocked, locked, and banned vendors.

There's a meaningful choice here:

**Should players be allowed to unlock an NPC shop remotely from the Account Access screen, without visiting the shopkeeper?**

{@body const [choice,setChoice] = DIL.useState("visit")}
<box gap={2}>
  <radio-group direction="col" value={choice} onChange={setChoice}>
    <radio value="visit">
      **A. Must visit the NPC — my recommendation**

      Players can inspect shop prices and statuses remotely, but must physically reach the vendor to unlock or Tempt Fate.
    </radio>
    <radio value="remote">
      **B. Remote purchases allowed**

      Players can unlock any known vendor directly from the Account Access screen.
    </radio>
  </radio-group>
  <button block onClick={()=>GenUI.issueNewTurn(choice==="visit"?"Lock in requiring players to physically visit an NPC shopkeeper to unlock or Tempt Fate.":"Lock in allowing remote NPC shop unlock purchases from Account Access.")}>Confirm choice <icon name="arrow-right" inline/></button>
</box>

I recommend **A** because traveling to a shopkeeper should still matter in an account-restricted game mode. The Account Access screen remains useful for planning, but doesn't eliminate exploration.

Once we settle this, the Fate Shop is largely finished structurally, and we can move on to the remaining gameplay mechanics and economy balancing.

## S8-M0395 - user - 2026-10-09T16:21:51.331000+00:00
Message ID: cfe5288f-7585-4023-b3d0-45f9acacc29e

Lock in requiring players to physically visit an NPC shopkeeper to unlock or Tempt Fate.

## S8-M0396 - assistant - 2026-10-09T16:21:56.678912+00:00
Message ID: 96182117-5a0c-4180-9ba5-5fd3d7fd2e07

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — Physical Shopkeeper Visits Required**
</row>

Players must physically visit an NPC shopkeeper to unlock that vendor or gamble through **Tempt Fate**.

### Final rules

- **NPC interaction:** Unlock the vendor with FP or attempt the 50/50 Tempt Fate gamble.
- **Account Access screen:** View all known vendors, prices, regions, categories, and unlock statuses.
- **No remote purchases:** Players cannot unlock NPC shops directly from the Account Access screen.
- **Permanent unlocks:** Once purchased, access to that vendor remains unlocked for the Wheelbound run.
- **Banned vendors:** Remain inaccessible until restored using Fate's Pardon, after which they return to the locked state.
- **Grand Exchange:** Remains a separate special unlock requiring completed Fates and a substantial FP payment.

This preserves travel and exploration as meaningful parts of Wheelbound.

### What's next?

With the Fate Shop's structure largely settled, I recommend moving into **gameplay edge cases and anti-exploit rules** before balancing.

One particularly important issue is how Wheelbound handles players **pausing the mode to bypass restrictions**.

We've already established that pausing disables enforcement and freezes Wheelbound progress. However, we need to make sure a player can't pause, obtain valuable items or unlock content, resume, and then receive Wheelbound credit for those accomplishments.

My recommended rule is straightforward:

> **Anything accomplished while Wheelbound is paused provides no retroactive Wheelbound progress, rewards, or bounty credit.**

The player's actual OSRS account still changes normally, but Wheelbound only rewards eligible accomplishments earned while the mode is active.

I'll use that as our working rule and move on to the more complicated edge cases where there are meaningful design trade-offs.

## S8-M0397 - user - 2026-10-09T16:22:36.091000+00:00
Message ID: c34e3a49-0b76-4f8c-96f5-c29ee08c73c2

keep outputting descions making as you go through and stop and prompt for larger questions, whats next

## S8-M0398 - assistant - 2026-10-09T16:22:40.020942+00:00
Message ID: 9b0a07f0-caf0-459e-bd46-48c4bb330500

Let's move into **Wheelbound's gameplay rules, persistence, and anti-exploit systems**. I'll make the straightforward decisions, explain what we're establishing, and stop only when something significantly affects gameplay.

## Decisions I'm locking in

<box gap={3}>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **WB-001 — Paused Progress Never Counts**

      XP, drops, boss kills, quests, Combat Achievements, and bounties earned while paused cannot retroactively count toward Wheelbound.

      Resuming takes a fresh snapshot of the account's current state.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **WB-002 — Progress Is Saved Automatically**

      Wheelbound automatically persists progress after important events: Fate completion, FP transactions, wheel changes, shop unlocks, Grand Fate checklist progress, and Defy Fate.

      A crash or logout must never intentionally reset the current Fate or reroll an unfavorable outcome.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **WB-003 — Locked Random Outcomes Survive Restarts**

      Once a wheel result, Fate Card offering, or Punishment Wheel selection is committed, it is saved.

      Restarting RuneLite cannot generate a different result.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **WB-004 — Account-Specific Progress**

      Each OSRS account has its own Wheelbound run, inventory of slices, FP balance, progression, and Grand Fate.

      Switching accounts never transfers Wheelbound progress.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success" size="lg"/>
    <box flex="1" gap={1}>
      **WB-005 — No Retroactive Rewards**

      Bounties, Grand Fate objectives, and milestone rewards are granted only when their qualifying conditions were legitimately satisfied under the appropriate Wheelbound rules.

      A player cannot resume and claim rewards for accomplishments earned while paused.
    </box>
  </row>
</box>

These rules establish the intended behavior. We'll still need to verify what RuneLite can reliably observe and persist before implementation.

## First major decision: Pausing Wheelbound

There's a potential exploit in our existing design.

Imagine this:

1. The player receives a difficult Fate requiring 100,000 Mining XP.
2. They pause Wheelbound.
3. They freely train combat, earn millions of GP, buy equipment, and unlock quests.
4. They resume Wheelbound and complete the Mining Fate.

Technically, their paused accomplishments wouldn't earn Wheelbound rewards, but **their OSRS account is now much stronger**, making Wheelbound significantly easier.

This matters because we've intentionally designed Wheelbound as a restrictive fresh-account challenge.

### Two possible approaches

<box gap={2}>
  <row align="start" gap={3}>
    <box flex="1" gap={1}>
      **Option A — Keep pausing unrestricted**

      Pausing remains a complete escape hatch. Players can do anything while paused, but gain no Wheelbound credit.

      <text color="secondary" size="sm">Advantages: Flexible, forgiving, useful when debugging. Disadvantage: Easy to bypass the intended challenge.</text>
    </box>
  </row>
  <divider color="subtle"/>
  <box gap={1}>
    **Option B — Introduce a run-integrity warning (my recommendation)**

    Pausing still works, but when resuming, Wheelbound checks for significant account progression that happened while paused.

    If the account gained substantial XP, completed quests, or acquired major equipment, Wheelbound marks the run as **Assisted**.

    The player can continue normally, but the run is no longer considered a fully legitimate, uninterrupted Wheelbound challenge.

    <text color="secondary" size="sm">No lost FP, no forced punishments, no deletion of progress. Just an integrity designation that cannot be removed from that run.</text>
  </box>
</box>

I prefer **Option B** because it preserves the emergency pause system we deliberately designed without pretending a heavily progressed account is equivalent to a fresh-account challenge.

{@body const [choice,setChoice] = DIL.useState("assisted")}
<box gap={2}>
  <radio-group direction="col" value={choice} onChange={setChoice}>
    <radio value="assisted">**B — Assisted status** for significant paused progression (recommended)</radio>
    <radio value="unrestricted">**A — Unrestricted pausing**, no integrity designation</radio>
  </radio-group>
  <button block onClick={()=>GenUI.issueNewTurn(choice==="assisted"?"Lock in Assisted run status for significant account progression while Wheelbound is paused.":"Keep Wheelbound pausing unrestricted without any Assisted run designation.")}>Confirm decision <icon name="arrow-right" inline/></button>
</box>

Once we settle this, I'll continue automatically through the remaining edge cases until we reach another decision that deserves your input.

## S8-M0399 - user - 2026-10-09T16:24:25.528000+00:00
Message ID: f9b48264-1293-4aaf-a519-936ac9227874

Lets go with A, in the end its their playthrough if they decide to go aginst the challenege its on them. There are no highscores etc its all for love of the game.

## S8-M0400 - assistant - 2026-10-09T16:24:34.004528+00:00
Message ID: 1808b44e-d2d9-4a57-8909-0afcbfd419dc

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — WB-006: Unrestricted Pausing**
</row>

I agree with your reasoning. Wheelbound is a self-imposed challenge, not a competitive game mode. We shouldn't build an elaborate policing system for players who want to play differently.

**Final rules:**
- Players can pause and resume whenever they want.
- No penalties, Assisted status, or integrity warnings.
- Progress earned while paused doesn't count toward Wheelbound objectives or bounties.
- Any equipment, money, or levels obtained while paused remain available when resuming.
- No highscores, competitive validation, or anti-cheat system designed to judge how someone chooses to play.

We'll still enforce restrictions and track violations while Wheelbound is active. That's part of the challenge itself.

## Next decisions — Automatically locked

<box gap={3}>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success"/>
    <box flex="1" gap={1}>
      **WB-007 — Logging Out During a Fate**

      Active Fates persist through logout, crashes, and RuneLite restarts. Players resume where they left off.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success"/>
    <box flex="1" gap={1}>
      **WB-008 — No Fate Time Limits**

      Normal Fates don't expire. Players can spend days completing an objective without being punished for taking their time.

      Daily bounties are the exception and retain their daily rotation.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success"/>
    <box flex="1" gap={1}>
      **WB-009 — No Punishment for Disconnecting**

      Disconnecting during a Fate doesn't count as failure. The objective remains active.

      A disconnect also cannot reroll a committed punishment, card selection, or wheel result.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success"/>
    <box flex="1" gap={1}>
      **WB-010 — Abandoning a Run**

      Abandoning Wheelbound requires explicit confirmation. It ends that run and disables restrictions.

      Starting another run begins fresh Wheelbound progression. It doesn't reset the actual OSRS account.
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={2}>
    <icon name="check-circle-2" color="success"/>
    <box flex="1" gap={1}>
      **WB-011 — No Offline Progress Reconstruction**

      If RuneLite wasn't running or the plugin couldn't observe an event, Wheelbound won't invent or retroactively award credit for it.

      We'll make this clear in the interface.
    </box>
  </row>
</box>

## Next major design question: Fate abandonment

This one matters because it directly affects the challenge.

Imagine a player spins a difficult Fate, such as earning 200,000 Runecraft XP. They don't want to complete it.

Should they have a way to escape that Fate?

<box gap={2}>
  <box gap={1}>
    **Option A — No abandonment**

    Once accepted, a Fate must be completed. The only escape is pausing Wheelbound or abandoning the entire run.

    This is the harshest interpretation of Fate.
  </box>
  <divider color="subtle"/>
  <box gap={1}>
    **Option B — Defy an Active Fate (my recommendation)**

    Players can abandon an active Fate, but it comes at a serious cost:

    - Lose FP.
    - Consume one Master Wheel spin.
    - Receive no Fate completion reward.
    - Trigger a punishment from the Punishment Wheel.

    This is distinct from the existing **Defy Fate** mechanic used when the player runs out of spins. We could call it **Reject Fate**.

    It gives players an escape from an impossible or miserable assignment without making rerolling trivial.
  </box>
</box>

{@body const [choice,setChoice] = DIL.useState("reject")}
<box gap={2}>
  <radio-group direction="col" value={choice} onChange={setChoice}>
    <radio value="reject">**B — Reject Fate with severe consequences** (recommended)</radio>
    <radio value="none">**A — No active Fate abandonment**</radio>
  </radio-group>
  <button block onClick={()=>GenUI.issueNewTurn(choice==="reject"?"Lock in Reject Fate: players may abandon an active Fate at the cost of FP, one spin, no reward, and a Punishment Wheel outcome.":"Lock in no active Fate abandonment. Players must complete their assigned Fate or stop the Wheelbound run.")}>Confirm decision <icon name="arrow-right" inline/></button>
</box>

This is the last major escape-mechanic decision I see before we can move into **reward balancing, progression simulations, and the remaining technical specifications**.

## S8-M0401 - user - 2026-10-09T16:25:12.785000+00:00
Message ID: c737cad7-3b22-48a2-af58-05fabed451c3

Lock in Reject Fate: players may abandon an active Fate at the cost of FP, one spin, no reward, and a Punishment Wheel outcome.

## S8-M0402 - assistant - 2026-10-09T16:25:27.835302+00:00
Message ID: fc100dae-6cea-4005-9e3d-9d5894bb6e86

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — WB-012: Reject Fate**
</row>

Players can abandon an active normal Fate, but rejecting their destiny comes with serious consequences.

### Final rules

1. **Lose Fate Points:** A substantial FP penalty (`BALANCE_TBD`). FP can go negative.
2. **Consume one Master Wheel spin:** Rejecting a Fate uses the spin that would otherwise be consumed upon completion. It does not consume an additional spin.
3. **Forfeit all rewards:** No completion FP, XP-related Wheelbound rewards, or bounty credit for the rejected Fate.
4. **Spin the Punishment Wheel:** One mandatory punishment must be completed before another normal Fate can begin.
5. **No free rerolls:** Rejecting a Fate cannot be used to repeatedly roll for easier assignments without consequences.

Reject Fate is separate from **Defy Fate**, which grants additional spins when the player runs out.

## Additional decisions I'm locking in

| ID | Decision | Rule |
|---|---|---|
| WB-013 | Reject confirmation | Explicit warning showing the FP cost, spin loss, and mandatory punishment |
| WB-014 | Punishment persistence | Reject Fate punishment is saved immediately and cannot be rerolled by restarting |
| WB-015 | No chained rejection | Players cannot reject a punishment generated by Reject Fate |
| WB-016 | Negative FP | Reject Fate remains available even if the penalty pushes FP below zero |
| WB-017 | Zero-spin rejection | Rejecting the final active Fate leaves the player at zero spins after punishment; normal zero-spin options then apply |
| WB-018 | Grand Fate exception | Reject Fate applies only to normal Fates; Grand Fate attempts follow their existing failure/exit rules |

One implementation safeguard: the FP deduction, spin consumption, and punishment assignment must be recorded as one durable transaction so a crash cannot cause partial penalties.

## Next phase: Economy balancing

We now have enough of the core rules to start modeling how players earn and spend FP.

I'll establish initial costs for progression unlocks, slice manipulation, shop access, Fate Cards, and Defiance mechanics, then test whether players can reasonably progress without making restrictions meaningless.

**One larger question will eventually need your input:** How long should a typical Wheelbound playthrough take before a reasonably skilled player can make a serious attempt at their Grand Fate?

That decision will affect nearly every reward and cost in the economy. I'll first work out plausible progression ranges so you're choosing between concrete alternatives rather than arbitrary numbers.

## S8-M0403 - user - 2026-10-09T16:31:59.970000+00:00
Message ID: 5f52f77f-7193-492f-a91a-6050a7967371

One larger question will eventually need your input: How long should a typical Wheelbound playthrough take before a reasonably skilled player can make a serious attempt at their Grand Fate?

this is up to them? a lot of these would take a long time just on their own but sometimes if they are good enough they can attempt them ealrier. 

Lets do this take the challenges we have for the master fate. Search online for the reccomended stats to complete these challenges. Then guage how long in time it would take to train those stats by looking at the exp drops on the wiki to get an idea for hours. and then report back to me with what you feel like is approx hours per challallenge some will be more or less than others

## S8-M0404 - assistant - 2026-10-09T16:32:06.674513+00:00
Message ID: 5cd35c28-84e2-4111-886c-ac8312caf2fa

Agreed. **Grand Fate readiness should be determined by the player, not an artificial playtime requirement.**

A skilled player might attempt the Inferno with relatively low stats, while someone else may want nearly maxed combat before trying. Wheelbound should accommodate both.

I'll estimate the time required for each of our five Grand Fates using a fresh OSRS account, separating:

- **Stat preparation:** Training the recommended levels using realistic XP/hour rates.
- **Unlocks and equipment:** Quests, prerequisites, gear, and money-making.
- **Challenge completion:** Learning and completing the actual endgame content.

The distinction matters because reaching the recommended stats is not the same as being ready to defeat the content.

## Estimated hours for each Grand Fate

These are my planning estimates, not official wiki completion times. I'm assuming a fresh members account, reasonably efficient training, access to the Grand Exchange once unlocked in Wheelbound, and a player who knows OSRS but hasn't necessarily mastered these encounters.

<box gap={2}>
  <text color="secondary" weight="medium" size="xs">ESTIMATED TOTAL ACTIVE PLAYTIME FROM A FRESH ACCOUNT</text>
  <Chart content={{"chartType":"bar","meta":{"footer":"Ranges include training, preparation and learning; estimates are not additive."},"xKey":"challenge","series":[{"dataKey":"low","label":"Lower estimate","valueSuffix":" hrs"},{"dataKey":"high","label":"Upper estimate","valueSuffix":" hrs"}],"data":[{"challenge":"Inferno","low":120,"high":250},{"challenge":"Colosseum","low":140,"high":300},{"challenge":"Radiant Oathplate","low":180,"high":400},{"challenge":"Awakened DT2","low":220,"high":450},{"challenge":"Master CAs","low":300,"high":650}]}}/>
  <caption>Ranges are illustrative and overlap heavily. Wheelbound restrictions, GP acquisition, and individual learning curves can push the totals higher.</caption>
</box>

| Grand Fate | Suggested preparation targets | Estimated total |
|---|---|---:|
| <Entity category="video_game" value="Inferno" disambig="Old School RuneScape endgame encounter"/> | 90–95+ Ranged, 94+ Magic, 85–90 Defence, 77 Prayer | **120–250 hrs** |
| <Entity category="video_game" value="Fortis Colosseum" disambig="Old School RuneScape combat encounter"/> | 95 Attack/Strength, 90–95+ Ranged, 94 Magic, 77 Prayer | **140–300 hrs** |
| Radiant Oathplate | 90–95 melee, 90–95 Magic, 77 Prayer, Yama contract preparation | **180–400 hrs** |
| Awakened DT2 bosses | 90–99 combat styles, 94+ Magic, 77 Prayer, DT2 completion | **220–450 hrs** |
| Master Combat Achievements | 90–99 combat, broad boss access, substantial quest and skill progression | **300–650 hrs** |

The Colosseum wiki's suggested first-clear stats are particularly high, generally around 95+ in several combat skills. Yama's ordinary strategy recommendations are lower, but Radiant Oathplate requires five much harder contracts. <Cite refs={["turn159893search0","turn914460search0","turn159893search4"]}/>

### How I estimated training time

OSRS XP is nonlinear. For example, level 90 requires 5.35 million XP, level 95 requires 8.77 million, and level 99 requires 13.03 million. <Cite ref="turn914460search5"/>

Here are representative training calculations:

| Skill target | XP required | Assumed effective XP/hr | Training hours |
|---|---:|---:|---:|
| Ranged 90 | 5.35M | 200k | ~27 |
| Ranged 95 | 8.77M | 250k | ~35 |
| Magic 94 | 7.94M | 180k | ~44 |
| Strength 95 | 8.77M | 90k | ~97 |
| Attack 90 | 5.35M | 80k | ~67 |
| Defence 90 | 5.35M | 80k | ~67 |
| Prayer 77 | 1.48M | 350k | ~4 |

These are deliberately simplified, effective average rates from level 1, with early quest XP and changing training methods approximated rather than modeled individually. The wiki documents substantially faster high-end Ranged methods, including chinning maniacal monkeys, while melee rates tend to be slower. Prayer training can be fast but expensive. <Cite refs={["turn914460search4","turn914460search1","turn914460search3","turn158787search4"]}/>

**Important:** Don't add every training row to estimate a Grand Fate. Training Ranged and Magic also produces Hitpoints XP, quests provide skill XP, and a player can earn considerable combat XP while unlocking equipment and bosses.

## What actually makes each Grand Fate difficult?

<box gap={3}>
  <row align="start" gap={3}>
    <AsyncImage query="OSRS TzKal Zuk Inferno boss screenshot" aspectRatio="1:1" maxWidth="116px"/>
    <box flex="1" gap={1}>
      **1. <Entity category="video_game" value="Inferno" disambig="Old School RuneScape minigame"/> — 120–250 hours**

      The biggest hurdles are reaching 90+ Ranged, 94 Magic, acquiring suitable gear, and learning 69 waves.

      I would budget roughly **20–70 hours of attempts and practice** after preparation for a player learning their first cape. This is a judgment estimate, not a measured average. <Cite refs={["turn429560search4","turn429560search7"]}/>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <AsyncImage query="OSRS Fortis Colosseum Sol Heredit boss screenshot" aspectRatio="1:1" maxWidth="116px"/>
    <box flex="1" gap={1}>
      **2. <Entity category="video_game" value="Fortis Colosseum" disambig="Old School RuneScape combat arena"/> — 140–300 hours**

      This needs a strong melee setup for Sol Heredit and preferably a capable Ranged or Magic setup for waves.

      The wiki suggests 95+ in several combat stats for a first completion. Learning wave solves and the Sol fight adds substantial practice time. <Cite ref="turn159893search0"/>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <AsyncImage query="OSRS Yama boss radiant oathplate purifying sigil" aspectRatio="1:1" maxWidth="116px"/>
    <box flex="1" gap={1}>
      **3. Radiant Oathplate — 180–400 hours**

      This is particularly demanding because the player must complete five distinct Yama challenge contracts, rather than simply defeat normal Yama.

      **Important correction to our blueprint:** These contracts award pieces of the *purifying sigil*, not individual Radiant Oathplate pieces. The completed sigil is used to cosmetically upgrade Oathplate armour, with aether runes required for each piece. We should track the five contracts and require the complete radiant set for victory. <Cite refs={["turn159893search3","turn159893search4"]}/>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <AsyncImage query="OSRS awakened Vardorvis boss fight" aspectRatio="1:1" maxWidth="116px"/>
    <box flex="1" gap={1}>
      **4. Awakened DT2 — 220–450 hours**

      Players must complete Desert Treasure II, acquire suitable equipment and Awakener's Orbs, then defeat all four awakened bosses.

      The challenge requires multiple combat styles and mastery of substantially enhanced mechanics. The estimate includes learning and repeated attempts, but orb costs could increase preparation time significantly. <Cite refs={["turn159893search1","turn159893search2"]}/>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="start" gap={3}>
    <AsyncImage query="OSRS Ghommal hilt 5 combat achievements reward" aspectRatio="1:1" maxWidth="116px"/>
    <box flex="1" gap={1}>
      **5. Master Combat Achievements — 300–650 hours**

      This is the broadest Grand Fate because it requires learning numerous bosses, completing quests, accumulating kill counts, and meeting specialized task requirements.

      The current reference I found lists **1,965 CA points for Master tier**, awarding **Ghommal's hilt 5**. That corrects our previous blueprint, which incorrectly specified hilt 4. <Cite ref="turn429560search3"/>
    </box>
  </row>
</box>

## What this means for Wheelbound

My conclusion is that we **should not balance Wheelbound around a fixed number of hours before Grand Fate attempts**.

Instead, the Fate system should provide enough progression opportunities for players to develop the account toward their chosen challenge, with no mandatory training milestone imposed by Wheelbound beyond OSRS's actual requirements.

I'm locking in these design conclusions:

- **No minimum playtime** to attempt a Grand Fate.
- **No arbitrary combat-level gate** imposed by Wheelbound.
- Players can prepare through normal Fates, Defy Fate, and Sacrifices for as long as needed.
- The Grand Fate checklist tracks actual objectives, not time spent.
- Economy balancing should focus on avoiding progression deadlocks, not forcing a 200- or 500-hour playthrough.

One caveat: these estimates are baseline OSRS preparation ranges. They are **not yet simulated Wheelbound completion times**. Our restricted wheel, limited spins, and FP economy may add substantial time.

The next useful step is to model how quickly a Wheelbound account can unlock and train its chosen combat styles, rather than choosing a target duration and forcing the system to match it.

## S8-M0405 - user - 2026-10-09T16:33:11.383000+00:00
Message ID: 1f39c1cb-4e21-4d6c-b7d8-dddaa4c7757b

okay lets do that

## S8-M0406 - assistant - 2026-10-09T16:33:16.194489+00:00
Message ID: 30b3e32e-1f7c-4db3-be92-aace613d8a95

## Wheelbound — Combat progression model

I'll model progression from a fresh OSRS account to the point where a player can realistically attempt their assigned Grand Fate.

The important distinction is that **Wheelbound shouldn't decide when someone is ready**. It should determine how difficult it is to build the account they want.

I'm going to evaluate three playstyles:

- **Focused:** Prioritizes combat, avoids unnecessary unlocks, and builds a wheel around the skills needed for their Grand Fate.
- **Balanced:** Unlocks several skills, completes quests, develops equipment, and trains combat steadily.
- **Unfocused:** Spreads FP across many activities and frequently receives Fates unrelated to their endgame goal.

I'll measure XP progression, spin consumption, and the FP economy separately. That should tell us whether players can pursue their goals without being forced into endless Defy Fate cycles.

## 1. Training assumptions

The OSRS Wiki supports several useful baselines. Melee training through Nightmare Zone can reach roughly 100k XP/hour in suitable setups; Ranged methods vary enormously, and burst/barrage Magic training can exceed 150k–230k XP/hour, although those methods require unlocks and supplies. <Cite refs={["turn626080search2","turn626080search4","turn626080search9"]}/>

For Wheelbound, I'll use more conservative **effective training rates** that account for early levels and imperfect equipment.

| Skill | Modeled average XP/hr |
|---|---:|
| Attack | 70k |
| Strength | 75k |
| Defence | 65k |
| Ranged | 110k |
| Magic | 125k |
| Prayer | 250k |

These are modeling assumptions, not guaranteed in-game rates. In particular, combat training earns Hitpoints XP concurrently, while Prayer's time estimate excludes acquiring bones and paying for them.

## 2. Why our Combat Fate design matters

We already decided that the starting wheel has a **Combat bundle**, rather than separate mandatory wheel slices for every combat skill.

That is extremely important.

When Combat is selected, players choose which eligible combat skill to train. A player pursuing Inferno can prioritize Ranged and Magic, while someone pursuing Colosseum can prioritize melee.

That means players aren't forced to randomly alternate between Attack, Strength, Defence, Magic, and Ranged.

I'll preserve this as a core balancing principle:

**The wheel determines when combat training becomes available; the player determines which combat skill advances during that Fate.**

## 3. What training to high combat levels looks like

Using those effective rates:

| Training goal | Estimated direct training |
|---|---:|
| 90 Ranged | ~49 hours |
| 95 Ranged | ~80 hours |
| 94 Magic | ~64 hours |
| 90 Attack | ~76 hours |
| 95 Strength | ~117 hours |
| 90 Defence | ~82 hours |

These figures assume starting at level 1, so early quest rewards and XP gained during other activities can reduce the remaining work.

The critical implication: **Combat Fates must scale as the account levels up.** If a player at 94 Ranged receives a Fate for only 5,000 XP, progressing toward 95 would require far too many spins.

For example, level 94 to 95 Ranged requires about 827,000 XP. At 5,000 XP per Fate, that would take approximately 166 Combat Fates just for one level.

That's not the experience we want.

## 4. Proposed Combat Fate XP scaling

I recommend the following initial Standard Combat Fate ranges. These are design values to test, not final balance.

| Current skill level | XP per Standard Fate |
|---|---:|
| 1–29 | 1,000–3,000 |
| 30–49 | 4,000–10,000 |
| 50–69 | 12,000–30,000 |
| 70–79 | 35,000–65,000 |
| 80–89 | 75,000–130,000 |
| 90–99 | 140,000–250,000 |

The assigned XP amount would be randomized within the bracket, then modified by the selected Fate Card.

### Example: Training Ranged from 90 to 95

<box border radius="xl" padding={3} gap={3}>
  <grid columns={3} gap={2}>
    <grid-item>
      <text color="secondary" size="2xs">XP NEEDED</text>
      <title size="xl" color="default" tabularNums>3.43M</title>
    </grid-item>
    <grid-item>
      <text color="secondary" size="2xs">AVG XP/FATE</text>
      <title size="xl" color="default" tabularNums>195k</title>
    </grid-item>
    <grid-item>
      <text color="secondary" size="2xs">COMBAT FATES</text>
      <title size="xl" color="default" tabularNums>~18</title>
    </grid-item>
  </grid>
  <divider color="subtle"/>
  <text weight="medium" size="sm">Illustrative Master Wheel impact</text>
  <text color="secondary" size="xs">Assuming Combat occupies 4 of 24 equally weighted slices:</text>
  <box gap={1}>
    <row justify="between">
      <text size="sm">Combat probability</text>
      **16.7%**
    </row>
    <box background="surface-tertiary" height="10px" radius="full" clip>
      <box background="rgba(52,133,215,0.9)" width="16.67%" height="100%" radius="full"/>
    </box>
  </box>
  <row justify="between" align="center">
    <box gap={1}>
      <text color="secondary" size="xs">Expected total wheel spins</text>
      <title size="xl" tabularNums>~108</title>
    </box>
    <box gap={1} align="end">
      <text color="secondary" size="xs">Direct Ranged training</text>
      <title size="xl" tabularNums>~31 hrs</title>
    </box>
  </row>
  <caption>Expected values, not guaranteed outcomes. This excludes other combat XP sources, card modifiers, duplicate purchases, and enhancements.</caption>
</box>

This demonstrates why **wheel composition is just as important as XP rewards**.

A player with four Combat slices might need roughly 108 spins for that training milestone. A player who dedicates 12 of 24 equally weighted slices to Combat would need only about 36 spins on average.

That's a meaningful benefit for building a specialized wheel.

## 5. Three playstyle projections

These are illustrative projections based on the same 18-Combat-Fate milestone, not results from a full Monte Carlo simulation.

| Playstyle | Effective Combat probability | Expected spins |
|---|---:|---:|
| Focused | 50% | 36 |
| Balanced | 25% | 72 |
| Unfocused | 10% | 180 |

<Chart content={{"chartType":"bar","meta":{"footer":"Expected spins to obtain 18 Combat Fates, assuming fixed probabilities."},"xKey":"style","series":[{"dataKey":"spins","label":"Expected spins"}],"data":[{"style":"Focused","spins":36},{"style":"Balanced","spins":72},{"style":"Unfocused","spins":180}]}}/>

The focused player doesn't necessarily finish Wheelbound faster overall, because they still need quest prerequisites, gear, and other skills. But their chosen wheel should make combat progression substantially more predictable.

## 6. Decisions I'm making from this model

<box gap={3}>
  {#each [
    ["WB-019","Combat Fates scale with the chosen combat skill's current level, not total combat level."],
    ["WB-020","The player chooses the combat skill before the XP target is generated. This prevents fishing for an easier target."],
    ["WB-021","Combat XP targets use randomized ranges with clean rounding. No fixed tiny assignments at high levels."],
    ["WB-022","Hitpoints XP and other unavoidable combat by-products are permitted, provided they come from legitimate Fate activity."],
    ["WB-023","Duplicate Combat slices and weight enhancements are legitimate ways to specialize a wheel. No artificial cap on Combat probability beyond the existing wheel rules."],
    ["WB-024","Grand Fate readiness has no Wheelbound level or time requirement. OSRS prerequisites still apply."],
    ["WB-025","XP targets are frozen when assigned. Leveling up during a Fate doesn't increase the requirement."]
  ] as item}
    <row align="start" gap={2} key={item[0]}>
      <icon name="check-circle-2" color="success"/>
      <box flex="1" gap={1}>
        **{item[0]}**
        <text size="sm">{item[1]}</text>
      </box>
    </row>
  {/each}
</box>

## What we still need to simulate

The XP ranges above are a starting point. Before declaring the economy balanced, we need to simulate thousands of runs with actual wheel probabilities, FP earnings, purchase costs, Fate Cards, quest progression, and finite spins.

The most important outputs will be how frequently players run out of spins, how quickly they can afford Combat duplicates, whether noncombat progression becomes a bottleneck, and whether FP income supports both progression and wheel specialization.

**My current recommendation:** Keep the proposed Combat Fate XP ranges as provisional and move into the **FP economy model** next. That's where we'll find out whether building a combat-focused wheel is realistically affordable rather than merely mathematically effective.

## S8-M0407 - user - 2026-10-09T16:34:25.360000+00:00
Message ID: 434b145d-3a9a-45a8-93ba-b0439ef24709

yep, next

## S8-M0408 - assistant - 2026-10-09T16:34:31.538347+00:00
Message ID: 510df70c-d8e2-45af-9ad8-203d0a510db6

## Wheelbound — Fate Points economy

Next, we're going to establish the initial FP economy: **how players earn Fate Points, what they spend them on, and how expensive it should be to specialize their wheel.**

I'm keeping our existing philosophy: Wheelbound should reward commitment and interesting decisions, without forcing players to grind an arbitrary number of hours before attempting their Grand Fate.

### 1. FP earning rules — Locked

| Activity | FP reward model |
|---|---|
| Standard Skilling/Combat Fate | Base reward scaled by objective difficulty |
| Lesser Fate Card | Reduced reward for reduced workload |
| Greater Fate Card | Increased reward for increased workload |
| Challenge Fate Card | Additional reward for meaningful restrictions |
| Bossing Fate | Scaled by boss difficulty and kill count |
| Questing Fate | Scaled by quest tier |
| Permanent item bounty | One-time FP bonus based on rarity |
| Daily bounty | Small, repeatable FP bonus |
| Combat Mastery bounty | Large one-time FP bonus |
| Achievement Diary bounty | Scaled by diary tier |
| Punishment completion | Small FP reward, never enough to erase its intended cost automatically |

**Additional decisions:**

- FP rewards are displayed before a Fate Card is accepted.
- Completed Fates pay out immediately and exactly once.
- FP can go negative, but optional purchases require sufficient FP.
- No passive FP generation or FP earned merely by remaining logged in.
- Paused gameplay never generates Wheelbound FP.

These rules give us a predictable economy that can be simulated and audited.

## 2. Initial FP pricing — Proposed for testing

I'll use **100 FP for an average Standard Fate** as our baseline. This gives us a straightforward scale for evaluating purchases.

| Purchase | Initial cost | Equivalent Standard Fates |
|---|---:|---:|
| Add ordinary wheel slice | 25 FP | 0.25 |
| First duplicate of an activity | 100 FP | 1 |
| Second duplicate | 175 FP | 1.75 |
| Third duplicate | 275 FP | 2.75 |
| Tier I enhancement (1.25x) | 75 FP | 0.75 |
| Tier II enhancement (1.5x) | 150 FP | 1.5 |
| Tier III enhancement (2.0x) | 300 FP | 3 |
| Tier IV enhancement (3.0x) | 600 FP | 6 |

Enhancement costs are **fixed by tier**, as we agreed. They do not increase with purchase count. Duplicate costs continue escalating separately for each activity.

These are provisional economy values, not final prices.

### Example: Building a Combat-focused wheel

Suppose a player starts with one Combat slice and wants three additional Combat slices, each enhanced to 1.5x.

<box border radius="xl" padding={3} gap={2}>
  <text color="secondary" weight="medium" size="xs">ILLUSTRATIVE BUILD COST</text>
  <row justify="between">
    <text>Three Combat duplicates</text>
    <text tabularNums weight="medium">550 FP</text>
  </row>
  <row justify="between">
    <text>Tier I + II on each duplicate</text>
    <text tabularNums weight="medium">675 FP</text>
  </row>
  <divider/>
  <row justify="between" align="center">
    **Total investment**
    <title size="xl" color="default" tabularNums>1,225 FP</title>
  </row>
  <caption>Approximately 12.25 average Standard Fate rewards before other expenses. The original Combat slice remains at 1.0x.</caption>
</box>

This is an attainable specialization goal, but not a trivial purchase.

## 3. Probability and spin simulation

I also ran 10,000 simulated sequences for each fixed Combat probability, measuring the number of spins needed to land 18 Combat Fates.

<Chart content={{"chartType":"bar","meta":{"footer":"10,000 simulated sequences per scenario. These are wheel-selection simulations, not full account or FP economy simulations."},"xKey":"style","series":[{"dataKey":"average","label":"Average spins"},{"dataKey":"p90","label":"90th percentile"}],"data":[{"style":"Focused (50%)","average":36,"p90":44},{"style":"Balanced (25%)","average":72.1,"p90":91},{"style":"Unfocused (10%)","average":180,"p90":233}]}}/>

The results reinforce something important: investing FP into wheel specialization can reduce the number of unwanted Fates dramatically. However, this simplified simulation assumes constant probabilities and doesn't yet account for the cost of constructing each wheel.

## 4. Progression and shop prices

Here's the first pricing framework for the remaining systems.

| Category | Proposed FP costs |
|---|---|
| Tier I skill unlock | 100–200 |
| Tier II skill unlock | 250–450 |
| Advanced progression milestone | 500–1,000 |
| Easy boss unlock | 150–250 |
| High-tier boss unlock | 750–1,500 |
| Lesser / Greater Fate Cards | 150 / 350 |
| Challenge Fate Card | 500 |
| Wilderness card pool | 750 |
| Additional Fate Card draw | 1,000 |
| Satchel upgrades (6/10/15 slots) | 300 / 750 / 1,500 |
| Grand Exchange access | 2,500 + completed-Fate requirement |

NPC vendor costs will follow their existing shop-class escalation rules.

I want advanced progression to be expensive enough to feel earned, but not so expensive that players must repeatedly complete activities they no longer need.

## 5. Economy safeguards — Locked

I'm establishing five additional rules:

**WB-026 — Wheel probabilities use active weights.** A wheel doesn't need all 24 slots filled. Each slice's probability is its weight divided by the total weight of active slices.

**WB-027 — Spending FP never directly increases XP.** FP purchases modify access, probability, card offerings, and restrictions. Actual XP still comes from playing OSRS.

**WB-028 — No hidden purchase escalation.** Every shop clearly shows the current cost and, where applicable, the next purchase price.

**WB-029 — No required daily bounties.** Daily bounties are optional FP income. Missing a day cannot prevent normal progression.

**WB-030 — No irreversible economic deadlocks.** A player must always have a viable path forward, even with negative FP, a full Satchel, or a heavily tainted wheel. We'll explicitly test these scenarios.

## 6. One important finding

I don't think we should finalize the FP numbers yet.

The real danger isn't individual purchases being too expensive. It's that a player could spend all their FP on progression unlocks and then discover they cannot afford the wheel modifications necessary to make those unlocks useful.

Our simulation needs to account for the entire purchase sequence, not just individual prices.

**Next phase: Full economy simulation.** I'll model player earnings, expenses, wheel-building decisions, unlock progression, and spin exhaustion together, then identify where the design needs adjustment.

That will give us a much stronger basis for final FP prices than assigning costs by intuition alone.

## S8-M0409 - user - 2026-10-09T16:35:26.380000+00:00
Message ID: 30622a80-c77e-451a-91eb-ff0973680d6f

, just keep going and stop when you need my input

## S8-M0410 - assistant - 2026-10-09T16:35:34.718833+00:00
Message ID: 026f7d02-a68f-420b-a312-f8657624ee29

## Wheelbound — Economy simulation and design audit

I'll continue making the decisions that don't need your approval. There are two things I want to establish before locking final FP prices: whether players can afford meaningful wheel specialization, and whether the economy can recover from bad decisions without becoming trivial.

### Simulation checkpoint: Combat specialization

I checked 10,000 randomized wheel-selection sequences per scenario, targeting 18 Combat Fates.

| Combat probability | Average spins | 90% finish within |
|---|---:|---:|
| 50% | 36 | 44 |
| 25% | 72 | 91 |
| 10% | 180 | 232 |

<Chart content={{"chartType":"bar","meta":{"footer":"10,000 random sequences per probability, fixed wheel weights, no changing upgrades."},"xKey":"probability","series":[{"dataKey":"average","label":"Average spins"},{"dataKey":"p90","label":"90th percentile spins"}],"data":[{"probability":"50% Combat","average":36,"p90":44},{"probability":"25% Combat","average":72,"p90":91},{"probability":"10% Combat","average":180,"p90":232}]}}/>

The important finding is that **wheel specialization has enormous value even without giving the player extra XP**. This supports our decision to keep FP enhancements focused on probability rather than directly boosting training.

These results are a probability-only simulation. They don't yet represent complete Wheelbound playthroughs with FP earnings, quest prerequisites, shop unlocks, and changing wheel composition.

### Decisions I'm locking in

**WB-031 — No mandatory wheel filling.** Players may operate a wheel with fewer than 24 active slices. Empty capacity does not dilute probabilities.

**WB-032 — No free optimization through duplicate destruction.** Destroying a duplicate never refunds FP or resets its escalating purchase counter.

**WB-033 — Transparent probability preview.** Before confirming a wheel purchase, the player sees how the change affects the probability of every activity.

**WB-034 — No arbitrary FP cap.** Players may accumulate as much FP as they earn, including saving for expensive upgrades.

**WB-035 — Permanent progression purchases.** Unlocking a skill, boss, card, or vendor remains permanent for the run. Removing its wheel slice does not revoke the unlock.

## First significant design issue: The one-slice wheel

Our current rules allow a player to remove every ordinary slice except Combat.

That would give them a wheel with **100% Combat probability**.

<grid columns={2} gap={3}>
  <grid-item>
    <box border radius="xl" padding={3} gap={2} align="center">
      <svg viewBox="0 0 160 160" width="100%">
        <circle cx="80" cy="80" r="68" fill="#286b91" stroke="#b5a1a0" strokeWidth="5"/>
        <circle cx="80" cy="80" r="17" fill="#202c39" stroke="#e8c18b" strokeWidth="4"/>
        <path d="M80 2 L72 20 L88 20 Z" fill="#e7bc74"/>
        <text x="80" y="75" textAnchor="middle" fill="white" fontSize="13" fontWeight="bold">COMBAT</text>
        <text x="80" y="94" textAnchor="middle" fill="white" fontSize="16">100%</text>
      </svg>
      **One-slice wheel**
      <caption>Always lands on Combat</caption>
    </box>
  </grid-item>
  <grid-item>
    <box border radius="xl" padding={3} gap={2} align="center">
      <svg viewBox="0 0 160 160" width="100%">
        {#each Array.from({length:8},(_,i)=>i) as i}
          <path d={`M80 80 L${80+68*Math.cos((i*45-90)*Math.PI/180)} ${80+68*Math.sin((i*45-90)*Math.PI/180)} A68 68 0 0 1 ${80+68*Math.cos(((i+1)*45-90)*Math.PI/180)} ${80+68*Math.sin(((i+1)*45-90)*Math.PI/180)} Z`} fill={i<4?"#286b91":i<6?"#658c72":"#b88b52"} stroke="#d8c9ae" strokeWidth="1.5"/>
        {/each}
        <circle cx="80" cy="80" r="17" fill="#202c39" stroke="#e8c18b" strokeWidth="4"/>
        <path d="M80 2 L72 20 L88 20 Z" fill="#e7bc74"/>
        <text x="80" y="85" textAnchor="middle" fill="white" fontSize="12" fontWeight="bold">SPIN</text>
      </svg>
      **Diversified wheel**
      <caption>Multiple possible outcomes</caption>
    </box>
  </grid-item>
</grid>

This isn't necessarily an exploit. Players would still need to pay to deactivate or destroy their other slices, and they'd sacrifice access to those activities through the wheel.

But it creates a major design question: **Is Wheelbound fundamentally about random Fate assignments, or about building enough control over Fate that randomness can eventually be overcome?**

I lean toward preserving meaningful randomness, but this is an important enough philosophical decision that I don't want to make it for you.

{@body const [choice,setChoice] = DIL.useState("minimum")}
<box gap={2}>
  <radio-group direction="col" value={choice} onChange={setChoice}>
    <radio value="minimum">
      **A — Minimum 5 ordinary active slices (recommended)**

      Players must maintain at least five ordinary slices, with at least three distinct activities. They can heavily weight Combat but cannot make it guaranteed.
    </radio>
    <radio value="unrestricted">
      **B — Allow complete specialization**

      Players can reduce their wheel to a single ordinary slice. If they've invested enough FP, they've earned that control.
    </radio>
  </radio-group>
  <button block onClick={()=>GenUI.issueNewTurn(choice==="minimum"?"Lock in a minimum of five ordinary active wheel slices, representing at least three distinct activities. Continue the Wheelbound design and simulation.":"Lock in unrestricted wheel specialization, including a single ordinary active slice. Continue the Wheelbound design and simulation.")}>Confirm and continue <icon name="arrow-right" inline/></button>
</box>

Once this is settled, I can model the realistic maximum Combat probability, finish the initial economy balancing, and proceed through the remaining gameplay rules without needing another decision unless we encounter a similarly important trade-off.

## S8-M0411 - user - 2026-10-09T16:36:09.607000+00:00
Message ID: 7a799efa-113a-4893-9163-41b62542f2e0

Lock in a minimum of five ordinary active wheel slices, representing at least three distinct activities. Continue the Wheelbound design and simulation.

## S8-M0412 - assistant - 2026-10-09T16:36:14.952288+00:00
Message ID: f1443f42-c378-4fda-bbe7-79a9c84c4058

<row align="center" gap={2}>
  <icon name="check-circle-2" color="success" size="lg"/>
  **Locked — WB-036: Minimum Wheel Diversity**
</row>

The Master Fate Wheel must maintain:

- At least **5 active ordinary slices**.
- At least **3 distinct activities** among those slices.
- A maximum of **24 active slices**, as previously established.
- Tainted and Sacrifice slices do **not** count toward the five-slice minimum.

Players can still build heavily specialized wheels, but they cannot eliminate randomness entirely.

**Additional implementation rule — WB-037:** Any removal, deactivation, or destruction that would violate these minimums is blocked. Forced Taint displacement must also respect them; if necessary, the player must first add another ordinary slice.

One important consequence: the starting wheel already contains five distinct activities, so it satisfies the new minimum without additional purchases.

## Continuing the economy model

There's a subtle distinction worth testing: a five-slice minimum does not necessarily prevent near-total specialization.

For example, a player could have 21 Combat slices and one each of Mining, Fishing, and Woodcutting. That satisfies the three-activity rule, but the ordinary wheel is already 87.5% Combat before enhancements.

So our minimum creates a baseline of variety, **not a strict maximum probability for any activity**. I think that's appropriate: heavily controlling Fate should be something a player can work toward through FP investment.

## Simulation 2 — How much control can players earn?

I ran 10,000 randomized sequences for each scenario, measuring the spins required to receive 18 Combat Fates.

<Chart content={{"chartType":"bar","xKey":"build","series":[{"dataKey":"average","label":"Average spins"},{"dataKey":"p90","label":"90th percentile"}],"data":[{"build":"20% Combat","average":90.1,"p90":115},{"build":"50% Combat","average":36.1,"p90":44},{"build":"87.5% Combat","average":20.6,"p90":23}]}}/>

The results show that extreme specialization is powerful, but it doesn't guarantee that the player can afford or reach that build.

### WB-038 — Specialization has diminishing returns

I'm retaining the existing duplicate-purchase escalation and fixed enhancement-tier prices.

At high Combat probabilities, each additional upgrade becomes less valuable. We don't need to introduce an artificial probability cap.

### WB-039 — Minimum wheel rules apply to the final state

Wheel Builder will validate the resulting wheel before committing changes, not simply validate each click independently.

This permits players to replace multiple slices in one editing session without getting stuck halfway through a valid configuration.

### WB-040 — Special slices cannot bypass diversity requirements

Tainted and Sacrifice slices remain outside the ordinary-slice minimum. Their weights still participate in actual spin probability calculations.

For example, 21 Combat slices plus three other ordinary slices gives Combat 87.5% probability at equal weights. Adding Taint and Sacrifice reduces that probability because they add competing weight.

That gives Defy Fate a natural long-term cost: corruption weakens the player's control over the wheel.

## Simulation 3 — Can players afford to specialize?

I ran another 10,000 simplified 100-Fate sequences for three purchasing strategies.

Assumptions: five starting slices, one Combat slice, four other activities, 100 FP earned per completed Fate, and the provisional escalating duplicate prices. This test excludes other FP expenses, weight enhancements, corruption, and spin exhaustion.

| Strategy | Average Combat Fates in 100 spins | FP remaining |
|---|---:|---:|
| No duplicate purchases | 20.0 | 10,000 |
| Purchase 3 Combat duplicates | 49.2 | 9,450 |
| Purchase 8 Combat duplicates | 64.7 | 6,575 |

The result is revealing: **duplicate purchases are extremely powerful under our provisional prices.**

A player investing just 550 FP in three duplicates can more than double their Combat Fate frequency over this test. That's potentially too inexpensive compared with other progression upgrades.

I'm making one balancing adjustment:

**WB-041 — Duplicate prices need stronger escalation.** The previously proposed 100/175/275 FP sequence is a starting placeholder, not a final economy. We'll raise later duplicate prices in the next balance iteration rather than weakening the wheel-building mechanic.

**WB-042 — Early specialization stays accessible.** The first duplicate should still be affordable relatively early. The steep costs should appear when players begin stacking many copies of the same activity.

**WB-043 — FP rewards and costs must be tested together.** We won't finalize prices using an assumed flat 100 FP reward without modeling actual Fate Cards, quest rewards, bounties, and progression purchases.

## Next: Preventing progression deadlocks

Before a full account simulation, we need to establish how Wheelbound handles a player who has spent their FP badly.

I'm locking in these safeguards:

| ID | Decision |
|---|---|
| WB-044 | Players can earn FP from any currently accessible, legitimately completed Fate even when their FP balance is negative. |
| WB-045 | Negative FP never disables ordinary Fate completion or prevents earning rewards. |
| WB-046 | Essential recovery actions, such as destroying an ordinary slice, cannot require FP. |
| WB-047 | Fate Shop purchases are atomic: either the entire purchase succeeds or no FP is deducted. |
| WB-048 | Players receive a clear warning before expensive irreversible actions, especially destroying enhanced slices. |
| WB-049 | No automatic FP refunds for balance changes; saved purchases and unlocks remain intact when prices are updated. |
| WB-050 | Developer Mode will include automated checks for zero spins, negative FP, full Satchels, maximum Taint, and unavailable shops. |

### One design problem surfaced

Our new five-slice minimum creates a possible conflict with forced Taint.

Imagine a player has exactly five ordinary slices, all 24 wheel slots are occupied, and a Tainted slice needs to enter.

They cannot destroy an ordinary slice without violating the minimum, and they cannot add another slice because the wheel is full.

**My recommended solution is to reserve space for forced corruption outside the 24 ordinary wheel slots.**

This would change the wheel capacity rule slightly: 24 slots for player-controlled ordinary slices, with additional special slots reserved for Taint and Sacrifice. Their weights would still compete in the same probability calculation.

That avoids a genuine deadlock and keeps Defy Fate frightening without forcing players into an impossible configuration.

I'm marking this as a **proposed technical adjustment**, rather than silently changing our established 24-slot rule, because it affects the wheel's appearance and capacity.

The next modeling pass is the full progression economy: starting FP, skill unlocks, shop access, Combat duplicates, Fate Card rewards, and how often players must Defy Fate. I'll continue with those assumptions and flag any other substantial design conflicts before finalizing them.

## S8-M0413 - user - 2026-10-09T16:37:55.658000+00:00
Message ID: 6aa53833-ff69-4fb6-8a66-5230d2cf78bd

im confused, with the tain you would just pick to adjust any of the other non original 5 slices to remove so tain could exist and then if the satchel is full you would have to destroy? Am i missing something

## S8-M0414 - assistant - 2026-10-09T16:38:00.232699+00:00
Message ID: 2b8f9a56-32f7-4a76-aaf8-b101807f70cb

You're correct. **Your proposed system already handles this.** I was overcomplicating it by suggesting additional slots for Taint.

The only edge case is when a player has exactly five ordinary slices remaining. Otherwise, your approach works perfectly.

### Let's walk through it

Suppose the wheel is full with 24 slices:

- 5 ordinary slices (the minimum required)
- 19 additional ordinary slices

The player activates Defy Fate, introducing a Tainted slice.

1. The player chooses which of the 19 additional ordinary slices to remove.
2. If the Fate Satchel has space, they can pay the deactivation fee and store the displaced slice, preserving its upgrades.
3. If the Satchel is full, they can purchase more storage (if available) or **destroy the slice for free**, permanently losing its upgrades.
4. The Tainted slice occupies the newly available slot.

That works exactly as intended.

### The one edge case

Imagine the wheel contains:

- 5 ordinary slices
- 3 Tainted slices
- 1 Sacrifice slice

There are only nine active slices, so plenty of empty capacity. No problem.

But if the wheel has **exactly five ordinary slices and all 24 slots occupied**, then the other 19 slots would have to contain special slices. That's impossible under our current rules, since we allow at most three Tainted slices and one Sacrifice slice.

**So there is no capacity deadlock under the current rules.**

### Final decision — Locked

- The wheel retains its **24 total active-slice capacity**, including Taint and Sacrifice.
- At least **5 ordinary slices representing 3 distinct activities** must remain.
- When Taint enters a full wheel, the player chooses an eligible ordinary slice to displace.
- They can store that slice in the Satchel for the normal FP fee, or destroy it for free.
- If the Satchel is full, destroying remains available.
- No extra special-slice capacity is necessary.

**I'm withdrawing my proposed capacity adjustment.** Your original system works, and it's simpler.

We'll keep moving with this design.

## S8-M0415 - user - 2026-10-09T16:38:15.571000+00:00
Message ID: 02901548-09a3-46ec-9827-12372faf289d

keep going

## S8-M0416 - assistant - 2026-10-09T16:38:22.847762+00:00
Message ID: 259a1337-fa2b-4ba6-9de5-e447dca29e8e

## Wheelbound — Economy balancing, continued

With the 24-slot wheel and Taint displacement rules settled, I'm moving forward with the full economy design.

I'm keeping three principles in place:

1. **Players control their journey.** We aren't targeting a specific number of hours before Grand Fate.
2. **FP should create meaningful choices.** Players should be able to specialize, but not immediately purchase everything.
3. **Bad decisions should hurt, not permanently brick a run.**

### Decisions I'm locking in

| ID | Decision |
|---|---|
| WB-051 | The five ordinary slices are a minimum, not five permanently protected slice instances. Players can replace any of them as long as the resulting wheel remains valid. |
| WB-052 | All five starting activities are available immediately. Other activities must first be unlocked through progression. |
| WB-053 | Every active ordinary slice must reference an unlocked activity. Removing a slice never removes its underlying unlock. |
| WB-054 | Purchasing an activity unlock does not automatically add its slice to the wheel. The player chooses when to purchase and add one. |
| WB-055 | A player can preview a complete wheel edit before committing FP costs and destructive changes. |
| WB-056 | If a forced Taint displacement requires a player decision, the pending choice is saved. Restarting cannot skip the displacement. |
| WB-057 | Mandatory punishment and unresolved Taint onboarding take priority over starting another normal Fate. |

One small correction to our earlier reasoning: **there are no permanently designated “original five” slices.** The rule is simply that five ordinary slices representing three activities must remain.

That allows players to evolve their wheel over time without being permanently tied to Mining, Fishing, Woodcutting, Combat, and Questing.

## 1. Combat specialization: a more realistic purchase sequence

I've tested a provisional escalating duplicate-price ladder. Rather than assuming players start with a completed wheel, this model lets them earn FP and buy Combat duplicates as they progress.

The proposed prices are:

| Additional Combat slice | FP cost |
|---|---:|
| 1st | 100 |
| 2nd | 250 |
| 3rd | 500 |
| 4th | 900 |
| 5th | 1,500 |
| 6th | 2,300 |
| 7th | 3,300 |

These costs escalate **per activity**, and destroying duplicates never resets the counter. Enhancement-tier prices remain fixed.

For an initial test, I simulated 10,000 runs per checkpoint with five starting slices, 100 FP earned per Fate, and players purchasing their next Combat duplicate whenever affordable.

<Chart content={{"chartType":"bar","xKey":"spins","series":[{"dataKey":"combat","label":"Average Combat Fates"}],"data":[{"spins":"30 spins","combat":14.5},{"spins":"60 spins","combat":32.5},{"spins":"120 spins","combat":71.7}]}}/>

| Completed spins | Avg. Combat Fates | Duplicates purchased | FP remaining |
|---|---:|---:|---:|
| 30 | 14.5 | 4 | 1,250 |
| 60 | 32.5 | 6 | 450 |
| 120 | 71.7 | 7 | 3,150 |

This is a **simplified specialization-only simulation**. It does not yet include progression purchases, questing, shop costs, cards, or spin exhaustion. In this test, every Fate rewards exactly 100 FP and purchases occur automatically.

The main result is that specialization grows naturally over time without requiring players to start with a highly optimized wheel.

## 2. A problem with our original economy

The earlier model gave players 100 FP for every completed Fate, regardless of activity.

That creates an incentive to seek whichever Fate can be completed fastest rather than engaging with meaningful challenges.

I'm changing the proposed reward model to use **difficulty-adjusted FP rewards**, while retaining 100 FP as a useful reference value.

### Provisional rewards

| Fate type | Base FP range |
|---|---:|
| Low-level Skilling | 30–60 |
| Mid-level Skilling | 60–110 |
| High-level Skilling | 100–180 |
| Standard Combat | 50–150 |
| Easy Bossing | 80–150 |
| Medium Bossing | 120–220 |
| Hard Bossing | 200–350 |
| Elite/Master Bossing | 350–650 |
| Low-level Quest | 100–200 |
| Master/Grandmaster Quest | 400–900 |

Greater and Challenge cards can modify these rewards. The objective generator must consider expected difficulty and duration, not merely the player's current level.

**WB-058 — FP rewards are determined when the Fate is assigned.** They cannot change mid-Fate because the player levels up.

**WB-059 — Reward multipliers are applied before acceptance and shown clearly.** No surprise reduction after completion.

**WB-060 — Reward calculations use curated activity-specific difficulty factors.** A 100,000 XP Runecraft assignment should not automatically reward the same FP as a 100,000 XP assignment in a much faster skill.

**WB-061 — No infinitely repeatable trivial Fate exploit.** Very short Fates provide proportionally small rewards, with the reward generator calibrated against realistic completion times.

## 3. Recovery and spending priorities

I want to prevent players from feeling compelled to save every FP for the Grand Exchange or an expensive progression node.

These are the additional rules:

**WB-062 — No mandatory FP spending order.** Players can prioritize wheel manipulation, progression, shops, or cards in whichever order they prefer.

**WB-063 — Unlock previews show dependencies.** Before buying a progression node, players can see whether additional quests, skills, bosses, or FP purchases are required to use it.

**WB-064 — Spending warnings are informational, not restrictive.** Players can spend their last FP on an optional purchase. We won't impose a minimum FP reserve.

**WB-065 — Rejection penalties cannot block future earning.** Even at negative FP, players can continue completing available Fates and recover.

**WB-066 — Satchel and wheel editing cannot silently delete purchased enhancements.** Any permanent destruction must be explicitly confirmed.

**WB-067 — Bounty rewards are supplementary.** A player who ignores bounties should still be able to progress through ordinary Fate rewards, although more slowly.

## 4. What I'm changing before the next simulation

The initial duplicate ladder looks reasonable as a starting point, but the FP rewards need to be calibrated against **actual Fate completion time**.

For example, if a player earns 100 FP from a 15-minute Fate but only 150 FP from a two-hour Fate, the system encourages farming the shorter activity.

I'll therefore use these provisional balancing targets:

| Activity | Design goal |
|---|---|
| Ordinary Fates | Reasonably consistent FP per hour across comparable difficulty |
| Greater Fates | More total FP, but not disproportionately better FP/hour |
| Challenge Fates | Higher FP/hour to compensate for genuine restrictions |
| Bossing Fates | Reward mechanical difficulty and resource costs |
| Bounties | Meaningful bonuses, not the primary source of FP |
| Punishments | Low FP/hour; never attractive as a farming method |

### Next modeling pass

The next simulation will introduce the pieces missing from the specialization test: variable FP rewards, progression unlock costs, Fate Card purchases, changing wheel probabilities, finite spins, and Defy Fate.

It will also test a critical requirement: **a player who makes poor purchases must still have a path to recover without resetting their run.**

I don't need another decision from you yet. The design direction is clear, and I'll keep the economy numbers provisional until the more complete model supports them.

## S8-M0417 - user - 2026-10-09T16:40:06.011000+00:00
Message ID: c232a6bf-cb50-4eba-9ceb-ee4d8c20d88f

anyway to have you just keep going? I feel like i have to type this a lot, maybe a progress bar i dont need to see an output for everything. Once its all done id like a pdf with all the design choices we made etc and how they inertact like instrctuions guide almost

## S8-M0418 - assistant - 2026-10-09T16:40:10.646831+00:00
Message ID: b289dbb6-256a-41ef-aa5f-101521d17d47

Yes. We can switch from the question-by-question design process to a **single comprehensive design pass**, with a PDF as the final deliverable.

You won't need to keep typing “continue.”

I'll use the decisions we've already made, resolve the remaining minor details myself, and distinguish between confirmed rules, recommended additions, and values that still need balancing.

The finished PDF will be a **Wheelbound Game Design & Implementation Guide**, covering how the systems work together, not just a list of decisions.

It will include the player journey, Master and Grand Fate wheels, progression tree, Fate Shop, cards, FP economy, Defy Fate, Taint, sacrifices, punishments, bounties, account restrictions, UI, persistence, edge cases, and implementation guidance for Codex.

I'll also include a progress checklist and a clear list of any unresolved major decisions.

<row align="center" gap={2}>
  <icon name="file-check-2" color="success" size="lg"/>
  **Your Wheelbound design guide is ready.**
</row>

<box border radius="xl" padding={3} gap={2}>
  <row align="center" gap={3}>
    <icon name="file-text" size="2xl" color="secondary"/>
    <box flex="1" gap={1}>
      **Wheelbound — Game Design & Implementation Guide**

      <text color="secondary" size="xs">PDF · 9 pages · Version 1.0</text>
    </box>
  </row>
  [<icon name="download" inline/> Download the PDF](sandbox:/mnt/data/Wheelbound_Game_Design_and_Implementation_Guide.pdf)
</box>

It consolidates the design decisions we've made, explains how the mechanics interact, and includes implementation phases, edge cases, and testing requirements.

### Design progress

{@body const stages=[{name:"Core gameplay and wheels",done:true},{name:"Fate Shop and progression",done:true},{name:"Defy, Taint, Sacrifice",done:true},{name:"Grand Fates",done:true},{name:"Economy framework",done:true},{name:"Final numerical balancing",done:false},{name:"RuneLite technical validation",done:false},{name:"UI and implementation",done:false}]}
<box gap={2}>
  <row justify="between" align="center">
    **Design specification**
    <text color="secondary" weight="medium" tabularNums>5 of 8 stages</text>
  </row>
  <row gap="3px" height="9px">
    {#each stages as stage}
      <box key={stage.name} flex="1" background={stage.done?"#27866c":"surface-tertiary"} radius="xs"/>
    {/each}
  </row>
  {#each stages as stage}
    <row align="center" gap={2} key={stage.name}>
      <icon name={stage.done?"check-circle-2":"circle-dashed"} color={stage.done?"success":"tertiary"}/>
      <text size="sm">{stage.name}</text>
    </row>
  {/each}
</box>

The PDF is a consolidated **design baseline**, not a claim that the game is implemented or fully balanced. Numerical costs remain provisional, and several RuneLite capabilities still require validation.

For continued work without repeatedly prompting me, **ChatGPT Work** is the better fit for a substantial autonomous workflow. In this regular chat, I can't independently keep working after my response ends. We can use this PDF as the specification for the next implementation and balancing pass.

## S8-M0419 - user - 2026-10-09T16:43:37.765000+00:00
Message ID: 5d977618-81d2-4a3d-859a-f2df8b3ff00a

okay great how can i do that?

## S8-M0420 - assistant - 2026-10-09T16:43:42.232158+00:00
Message ID: ed47bf3f-531e-4423-a7d5-20b4b12b7eda

You can use **ChatGPT Work** to hand off the remaining Wheelbound design work as one larger project, rather than repeatedly telling me to continue.

### Step 1 — Open ChatGPT Work

1. Open ChatGPT on your computer.
2. Look for **Work** in the sidebar or mode selector.
3. Start a new Work task.
4. Attach our [Wheelbound Design Guide](sandbox:/mnt/data/Wheelbound_Game_Design_and_Implementation_Guide.pdf).

If you don't see Work, it may not be available in your current interface.

### Step 2 — Give Work the full assignment

Copy this prompt:

<WritingBlock id="58321" variant="standard">I want you to autonomously finish designing my Old School RuneScape RuneLite plugin game mode, **Wheelbound**.

I've attached the existing Wheelbound Game Design & Implementation Guide. Treat its confirmed decisions as authoritative.

**Your responsibilities:**

1. Review the entire design and identify contradictions, missing mechanics, exploits, and progression deadlocks.
2. Resolve minor design decisions yourself using your best judgment. Do not repeatedly ask for permission to continue.
3. Research RuneLite APIs and OSRS Wiki information to validate technical feasibility and gameplay requirements.
4. Build a detailed Fate Points economy, including income, expenses, unlock costs, and progression balancing.
5. Run simulations of at least 10,000 hypothetical playthroughs across different player strategies. Clearly distinguish simulation results from assumptions.
6. Finalize the Master Wheel, Grand Fates, Fate Cards, Fate Shop, Progression Tree, Bounties, Defy Fate, Taint, Sacrifice, Punishments, and NPC restrictions.
7. Create detailed rules for how all systems interact, including edge cases and failure recovery.
8. Develop a practical RuneLite implementation architecture and phased development plan.
9. Produce a comprehensive, professionally formatted PDF game design document that explains the game as both a player guide and developer specification.

**Decision-making instructions:**

- Make routine decisions independently.
- Preserve previously confirmed design choices.
- Ask me only when a major decision materially changes the game's identity or core gameplay.
- Do not impose arbitrary playtime requirements before Grand Fate attempts.
- Remember this is a self-imposed, noncompetitive challenge. Players may pause freely.
- Do not modify the GitHub repository during this design assignment.

**Deliverables:**

- Final comprehensive PDF design document.
- Separate technical implementation specification.
- Economy simulation results and assumptions.
- A concise list of any unresolved major decisions.

Work through the assignment to completion without requiring me to repeatedly type "continue".</WritingBlock>

### Step 3 — Let Work handle the project

Work can carry out a longer sequence of research, analysis, and file creation. It may still stop if it needs clarification or access to a required service, but you shouldn't need to manually advance every design step.

### Step 4 — Bring the results into development

Once the design is finished, we can use the resulting specification to direct Codex to implement Wheelbound in your GitHub repository.

I'd keep implementation on your existing **`wheelbound-mode` branch**, leaving `main` untouched.

**One important recommendation:** Don't ask Work to start coding yet. Finish the comprehensive design, economy simulation, and RuneLite feasibility checks first. That will give Codex a much clearer specification and reduce the likelihood of having to rewrite major systems later.

## S8-M0421 - user - 2026-10-09T16:46:02.962000+00:00
Message ID: 25602308-d4ac-4784-a9be-0cbcf8e376dd

questions for this, this entire time i was under the imression that we were keeping track of markdown files is that still true?

## S8-M0422 - assistant - 2026-10-09T16:46:08.535741+00:00
Message ID: 2ae63f90-631c-4615-9bda-cf219c8e843f

Yes, **Markdown files are still the intended foundation for Wheelbound's documentation.** But I need to clarify something important.

Throughout our design discussions, I've been tracking decisions in the conversation context. We discussed creating a structured set of Markdown files, but **I haven't actually been creating and updating those `.md` files as we went.**

The PDF was a consolidated summary, not a replacement for the Markdown documentation.

### What I originally envisioned

A documentation folder inside your GitHub repository:

<box border radius="lg" padding={3} gap={1}>
  <row align="center" gap={2}>
    <icon name="folder" color="secondary"/>
    **docs/wheelbound/**
  </row>
  <box padding={{left:4}} gap={1}>
    {#each ["BLUEPRINT.md","GAME_RULES.md","PROGRESSION.md","WHEELS.md","FATE_SHOP.md","FATE_CARDS.md","DEFY_FATE.md","ECONOMY.md","BOUNTIES.md","GRAND_FATES.md","UI_UX.md","ARCHITECTURE.md","EDGE_CASES.md","TESTING.md","DECISIONS.md","IMPLEMENTATION_PLAN.md"] as f}
      <row align="center" gap={2} key={f}>
        <icon name="file-text" color="tertiary" size="sm"/>
        <text size="sm">{f}</text>
      </row>
    {/each}
  </box>
</box>

These files serve different purposes:

- **Markdown:** The authoritative, version-controlled design specification that Codex can read and update.
- **PDF:** A polished, human-readable guide generated from the Markdown documentation.
- **GitHub:** The permanent home for the documentation and eventually the implementation.

### What I'd change about the Work assignment

Rather than asking Work to produce only a PDF and implementation guide, I'd make this an explicit requirement:

> Create and maintain a complete set of Markdown design documents under `docs/wheelbound/`. These documents are the single source of truth for all confirmed design decisions, gameplay mechanics, system interactions, and implementation requirements. Generate the final PDF from these Markdown files. Do not treat the PDF as the authoritative source.

I'd also have Work preserve a `DECISIONS.md` file containing each locked decision and a `TASKS.md` file tracking implementation progress.

**My recommendation:** Before continuing the design, we should create the actual Markdown documentation from everything we've already discussed. That way, none of the work depends on a long chat history, and future Codex sessions can pick up exactly where we left off.

The important distinction is that **we planned the Markdown documentation, but haven't yet saved it into the repository.** I should have made that clearer earlier.

## S8-M0423 - user - 2026-10-09T16:46:59.427000+00:00
Message ID: 5bc9d31e-ccc0-4882-bd5b-299913ff5c89

please provide me instructions for gpt work so they it has the pdf the correct github repo , and any context needed

## S8-M0424 - assistant - 2026-10-09T16:47:10.686778+00:00
Message ID: eef2ce10-c381-4b66-987b-77290b167fa6

I'll give you a **ready-to-paste ChatGPT Work prompt** that includes the correct GitHub repository, development branch, existing PDF, and the important design decisions we've made.

The key is to make Work understand that **Markdown is the source of truth, the PDF is the final presentation, and GitHub is where the work should live.**

## Step 1 — Prepare ChatGPT Work

Open a new ChatGPT Work task and provide these resources:

<box border radius="lg" padding={3} gap={3}>
  <row align="center" gap={3}>
    <icon name="github" size="xl"/>
    <box flex="1" gap={1}>
      **GitHub repository**

      <text size="sm">[TheRealEddieDean/RunlitePLugin-Wheelbound](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound)</text>
      <text color="secondary" size="xs">Development branch: `wheelbound-mode`</text>
    </box>
  </row>
  <divider color="subtle"/>
  <row align="center" gap={3}>
    <icon name="file-text" size="xl"/>
    <box flex="1" gap={1}>
      **Existing design PDF**

      [Wheelbound Game Design & Implementation Guide](sandbox:/mnt/data/Wheelbound_Game_Design_and_Implementation_Guide.pdf)

      <text color="secondary" size="xs">Download this and attach it to the Work task.</text>
    </box>
  </row>
</box>

Connect GitHub to Work if prompted. Work should verify repository access and the branch before changing anything. If `wheelbound-mode` doesn't exist, it should ask before creating it.

## Step 2 — Paste these instructions

The following prompt is deliberately detailed so Work doesn't need to reconstruct the design from memory.

<WritingBlock id="67291" variant="document"># Wheelbound — Autonomous Game Design, Documentation & Simulation Project

## 1. Project information

**Project:** Wheelbound — An optional account-restricted game mode for the Old School RuneScape RuneLite plugin.

**GitHub repository:** https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound

**Development branch:** `wheelbound-mode`

**Protected branch:** `main`

**Existing reference document:** `Wheelbound_Game_Design_and_Implementation_Guide.pdf` (attached to this task).

**Primary documentation directory:** `docs/wheelbound/`

I have spent considerable time designing this mode with ChatGPT. Many important decisions are already finalized. Your job is to preserve those decisions, complete the remaining design, validate feasibility, simulate balance, and create professional, comprehensive documentation.

**Important:** This assignment is for design and documentation, NOT gameplay implementation. Do not begin implementing Wheelbound code until I explicitly approve that phase.

## 2. How you should work

Work autonomously through the entire assignment.

- Do not repeatedly ask me to type "continue."
- Make straightforward design decisions yourself.
- Research technical questions rather than guessing.
- Stop for my input only when a decision significantly changes the core gameplay or contradicts an established design choice.
- Record every new decision in Markdown.
- Keep a task checklist and update it as work progresses.
- Do not declare simulations completed unless you actually executed them.
- Do not declare technical feasibility confirmed without evidence.
- If the attached PDF is incomplete, use the design context below. Do not assume omitted mechanics were rejected.

I want a complete, coherent design suitable for handing to Codex for implementation.

## 3. GitHub and documentation rules

Verify that you can access the repository and the `wheelbound-mode` branch.

**Never modify, commit to, or merge into `main`.**

Create or update the following Markdown files on `wheelbound-mode`:

```text
docs/wheelbound/
├── README.md
├── BLUEPRINT.md
├── GAME_RULES.md
├── DECISIONS.md
├── PROGRESSION.md
├── WHEELS.md
├── FATE_CARDS.md
├── FATE_SHOP.md
├── ECONOMY.md
├── GRAND_FATES.md
├── DEFY_FATE.md
├── PUNISHMENTS.md
├── BOUNTIES.md
├── ACCOUNT_ACCESS.md
├── UI_UX.md
├── PERSISTENCE.md
├── ARCHITECTURE.md
├── RUNELITE_INTEGRATION.md
├── EDGE_CASES.md
├── TESTING.md
├── SIMULATION_RESULTS.md
├── IMPLEMENTATION_PLAN.md
└── TASKS.md
```

These files are the authoritative source of truth.

The PDF should be generated from the finalized Markdown documents, not maintained as a separate competing specification.

Use clear cross-references between systems and document all interactions.

Record decisions as CONFIRMED, PROPOSED, BALANCE_TBD, TECHNICAL_TBD, or OPEN when appropriate.

Commit documentation changes to `wheelbound-mode` in logical batches. Do not open or merge a pull request unless I ask.

## 4. Wheelbound's core philosophy

Wheelbound is a self-imposed challenge mode, not a competitive system.

There are no official Wheelbound highscores, competitive integrity rankings, or requirements to prove someone played fairly.

Players choose how seriously to follow the challenge.

Pausing Wheelbound is unrestricted. While paused, players can play OSRS normally. Their accomplishments do not receive retroactive Wheelbound credit, but there are no Assisted labels or penalties.

Wheelbound should be challenging, sometimes punishing, and highly replayable without creating unavoidable progression deadlocks.

Players can attempt their Grand Fate whenever they feel ready. There is no arbitrary minimum playtime or combat level imposed by Wheelbound.

## 5. Core game loop

1. Player starts a fresh Wheelbound run, ideally on a fresh OSRS account.
2. A dramatic Grand Fate Wheel determines their ultimate endgame objective.
3. Player develops their Master Fate Wheel and unlocks content through the Progression Tree.
4. Each Master Wheel spin assigns an activity.
5. The player completes the assigned Fate to earn Fate Points and other applicable rewards.
6. Fate Points are spent on progression, wheel manipulation, Fate Cards, account access, and special mechanics.
7. Master Wheel spins are finite.
8. When spins run out, the player may Defy Fate, attempt their Grand Fate, or use unlocked Sacrifice mechanics to obtain more spins.
9. Defying Fate introduces permanent corruption and increasingly dangerous consequences.
10. Completing the Grand Fate wins the run.

## 6. Grand Fates — confirmed

Five equally weighted possible Grand Fates:

1. Inferno — obtain an Infernal Cape.
2. Fortis Colosseum — complete the Colosseum and obtain Dizana's Quiver.
3. Radiant Oathplate — complete the necessary Yama challenge contracts and obtain the complete radiant cosmetic armour set.
4. Awakened Desert Treasure II — defeat all four Awakened DT2 bosses.
5. Master Combat Achievements — reach the Master Combat Achievement tier.

Verify the exact current OSRS requirements, reward names, and item mechanics against the OSRS Wiki. Correct factual errors in earlier documentation while recording those corrections.

The Grand Fate is selected once at run creation and cannot be rerolled.

Grand Fate attempts do not consume ordinary Master Wheel spins.

Players may attempt their Grand Fate between ordinary Fates. Multi-part objectives preserve completed checklist items.

## 7. Master Fate Wheel — confirmed

- Maximum 24 active slices, including Taint and Sacrifice.
- Minimum five ordinary active slices representing at least three distinct activities.
- Starting activities: Mining, Fishing, Woodcutting, Combat, Questing.
- No permanently protected original slice instances.
- Players may purchase duplicate slices.
- Each slice has its own persistent ID and modifiers.
- Duplicate purchase costs escalate per activity based on lifetime purchases.
- Individual slice weight enhancements: 1.0x → 1.25x → 1.5x → 2.0x → 3.0x.
- Enhancement FP prices are fixed per enhancement tier, not based on previous purchase counts.
- Wheel slice positions can be rearranged freely.
- Probabilities are based on relative weights.
- Inactive slices are stored in the Fate Satchel.
- Satchel capacity progression: 3 → 6 → 10 → 15.
- Deactivation costs FP and preserves upgrades.
- Reactivation costs a small amount of FP.
- Destruction is free but permanently destroys the slice and its upgrades.
- Destroying duplicates does not reset purchase-cost escalation.

## 8. Progression Tree

Four branches:

- Skilling
- Questing
- Combat/Bossing
- Fate Manipulation

The tree should have meaningful unlocks, prerequisites, AND/OR relationships, and a polished interactive layout.

Skill progression uses previously agreed tier groups. Bossing has its own wheel, boss unlock nodes, and tier progression.

Questing offers a choice among eligible quests and advances through quest difficulty tiers.

Combat Fates let players choose the combat skill before the XP target is generated.

XP targets scale with current skill level and are frozen when assigned.

## 9. Fate Cards

Fate Cards are permanently unlocked through purchases.

Separate card pools apply where appropriate.

Cards include:

- Lesser
- Standard
- Greater
- Challenge
- Wilderness Challenge

Initially, players receive two eligible card offerings, with an upgrade allowing three.

Cards affect objective requirements, restrictions, and rewards.

Challenge restrictions must be technically observable and genuinely achievable.

## 10. Defy Fate, Taint, and Sacrifice

Defy Fate grants additional spins at a serious cost.

First Defy introduces Taint, the permanent Sacrifice slice, voluntary Death's Coffer sacrifice access, and the first free Blessed Item slot.

Taint is introduced on odd-numbered Defies, up to three active Tainted slices.

Once three are present, subsequent qualifying Taint events increase their weights.

Sacrifice slice weight increases by 0.25x on each subsequent Defy.

Tainted slices can only be removed through Fate's Cleansing.

Sacrifice cannot be removed.

When Taint enters a full 24-slot wheel, the player chooses an eligible ordinary slice to displace.

They may store it in the Satchel if space is available, or destroy it for free. The five-ordinary-slice minimum must remain satisfied.

No additional wheel capacity is needed.

Death's Coffer eligibility uses the actual OSRS per-item value requirement; verify exact mechanics.

Forced Sacrifice chooses from eligible items and must be completed before normal Wheelbound progression resumes.

Blessed Item slots protect designated item types from forced Sacrifice. Maximum three slots.

## 11. Reject Fate

Players can reject an active ordinary Fate.

Consequences:

- Substantial FP penalty, even if it creates negative FP.
- Consume the spin associated with the Fate.
- Forfeit the Fate's completion reward.
- Receive a mandatory Punishment Wheel outcome.
- Complete that punishment before starting another normal Fate.

Reject Fate does not apply to Grand Fate attempts.

Punishment outcomes persist across restarts and cannot be rerolled by restarting RuneLite.

## 12. Fate Shop and account access

Fate Shop categories include:

- Wheel Manipulation
- Fate Manipulation
- Defiance and Protection
- Account Access

NPC vendors must be physically visited to unlock them or use Tempt Fate.

Tempt Fate is a 50/50 unlock-or-ban gamble.

Banned vendors can be restored to Locked using Fate's Pardon.

The Grand Exchange is a special permanent unlock requiring both Fate completions and substantial FP.

All purchases and unlocks must be persisted safely.

## 13. Bounties

Include:

- 100 curated permanent item bounties.
- Three randomized daily item bounties.
- Combat Mastery bounties.
- Achievement Diary bounties.

Rewards require legitimate qualifying Wheelbound activity and manual claiming where previously specified.

## 14. Economy and simulation

Previous FP values are provisional.

Use 100 FP as a reference Standard Fate reward, not a guaranteed flat reward.

Create an economy model covering:

- FP income by Fate difficulty and duration.
- Progression unlock costs.
- Duplicate slice escalation.
- Fixed enhancement-tier pricing.
- Fate Card unlock costs.
- Satchel upgrades.
- NPC and Grand Exchange access.
- Cleansing, Pardons, and Blessings.
- Reject Fate penalties.
- Bounty rewards.
- Spin exhaustion, Defy Fate, and Sacrifice.

Run at least 10,000 simulated account progressions across focused, balanced, and inefficient player strategies.

Use actual executable simulation code.

Document assumptions, probability distributions, results, limitations, and balance adjustments.

Test negative FP, zero spins, maximum Taint, full Satchel, expensive upgrades, and poor purchase strategies.

Do not force the economy toward a fixed playthrough duration.

## 15. Technical research

Investigate actual RuneLite APIs and Plugin Hub restrictions.

Specifically verify:

- Account-specific persistent storage.
- XP and skill tracking.
- Combat Achievement tracking.
- Quest progress and eligibility.
- Boss kill detection.
- NPC shop interactions.
- Grand Exchange access restrictions.
- Inventory, bank, and equipment observability.
- Death's Coffer sacrifice verification.
- Login, logout, crash, and reconnect handling.
- Whether client-side restrictions can be enforced reliably.

Clearly distinguish reliable enforcement from advisory warnings.

Never claim the plugin can modify OSRS server-side restrictions.

Design a developer-only testing mode with controlled state manipulation and simulation tools.

## 16. UI and visual design

Plan a polished custom RuneLite-compatible interface.

Include:

- Master Fate Wheel
- Grand Fate Wheel
- Progression Tree
- Fate Shop
- Fate Cards
- Bounties
- Fate Satchel
- Punishment Wheel
- Account Access
- Grand Fate checklist
- Run status and FP indicators

Describe layouts, visual states, navigation, animations, and accessibility.

Do not prioritize artwork over completing the mechanical design.

## 17. Final deliverables

1. Complete, internally consistent Markdown documentation committed to `wheelbound-mode`.
2. Comprehensive player-facing and developer-facing PDF generated from those documents.
3. Executed economy simulations with reproducible code and results.
4. Technical feasibility report with sources.
5. Implementation architecture and phased Codex task list.
6. Decision register distinguishing confirmed choices from newly proposed ones.
7. A short summary of remaining major questions, if any.

**Completion criteria:** Do not stop merely because the first document is written. Continue until the design, simulation, feasibility research, cross-document consistency checks, and final PDF are complete, or until a genuinely important decision requires my input.

At the end, tell me exactly which files were created, which GitHub branch and commits contain them, what was verified, what remains uncertain, and where to find the PDF.</WritingBlock>

## Step 3 — What Work should produce

<box gap={2}>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    **Markdown documents in GitHub**
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    **Reproducible simulation results**
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    **RuneLite technical feasibility report**
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    **Final comprehensive PDF**
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    **Codex-ready implementation plan**
  </row>
</box>

One important note: the existing PDF is a condensed reference, not a complete transcript of every design conversation. The detailed context in the prompt helps fill that gap, but Work should flag anything it cannot confidently reconstruct instead of inventing a previously approved decision.

**My recommendation:** Start this as a new Work task, attach the PDF, paste the full prompt, and let Work handle the design/documentation phase before asking it to implement any code.

## S8-M0425 - user - 2026-10-09T16:50:22.084000+00:00
Message ID: dc718905-4cc7-41b0-9151-616034376beb

i dont see the markdown files in the repo yet, can you point them out for me

## S8-M0426 - assistant - 2026-10-09T16:50:26.507824+00:00
Message ID: b19c4596-ffe4-4f26-bbbe-21c301d5d6aa

You won't see them in GitHub yet because **the Markdown files haven't actually been created or committed to your repository.**

So far, we've:
- Designed Wheelbound together in this conversation.
- Created a PDF summarizing the design.
- Planned the Markdown documentation structure.
- Written instructions for ChatGPT Work to create those files.

But we haven't executed the GitHub documentation step.

### Where they'll appear

**Repository:** [RunlitePLugin-Wheelbound](https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound)

**Branch:** `wheelbound-mode`

**Planned folder:** `docs/wheelbound/`

Once Work completes that step, you should be able to find files such as `BLUEPRINT.md`, `DECISIONS.md`, `WHEELS.md`, and `ECONOMY.md` there.

### What to do next

Open your ChatGPT Work task and give it this instruction:

<WritingBlock id="48321" variant="chat_message">Before continuing with simulations or further design, create the complete `docs/wheelbound/` Markdown documentation structure in my GitHub repository, `TheRealEddieDean/RunlitePLugin-Wheelbound`, on the `wheelbound-mode` branch.

Populate the files using the attached design PDF and the project context already provided. Commit the Markdown files to that branch and give me the commit link so I can verify they exist.

Do not modify `main`. After committing the documentation, continue the remaining design and simulation work.</WritingBlock>

**Important:** I should have clarified earlier that we were recording decisions in the conversation, not continuously saving Markdown files to GitHub. Sorry for the confusion.

## S8-M0427 - user - 2026-10-09T16:50:55.748000+00:00
Message ID: 7998d415-20d8-45a0-9476-c49f90ae635d

so when ive been saying lock that in where did that go? i thought this was for the  markdown?

## S8-M0428 - assistant - 2026-10-09T16:51:00.373440+00:00
Message ID: 7a7eec73-791a-4d49-9b2c-5284ac171cb8

When you said **“lock that in,” I treated it as a finalized design decision and carried it forward in our conversation. I did not write it to a Markdown file or commit it to GitHub.**

And I understand why that's frustrating. We discussed using Markdown as our design documentation, and I continued saying things like “Locked — WB-036” without explaining that those decisions weren't being saved to actual files.

### Where your decisions went

There are three places to distinguish:

| Location | What's there |
|---|---|
| **Our ChatGPT conversation** | The original decisions, discussions, examples, and explanations |
| **Conversation context** | A condensed record of many decisions that helps me continue the project |
| **Design PDF** | A consolidated summary of the design, but not every detail |
| **GitHub Markdown files** | Not created or committed yet |

So your decisions **weren't intentionally discarded**. The conversation contains the original design history, including the decisions you approved.

However, the PDF is not a complete substitute for that history, and a condensed conversation summary may omit details.

### What I should have done

Every time you approved a decision, the intended workflow should have been:

<box border radius="lg" padding={3} gap={2} align="center">
  <box background="surface-secondary" radius="md" padding={3} width="100%" align="center">
    **You: “Lock that in”**
  </box>
  <icon name="arrow-down" color="secondary"/>
  <box background="surface-secondary" radius="md" padding={3} width="100%" align="center">
    **Update DECISIONS.md and relevant system documentation**
  </box>
  <icon name="arrow-down" color="secondary"/>
  <box background="surface-secondary" radius="md" padding={3} width="100%" align="center">
    **Commit changes to `wheelbound-mode`**
  </box>
</box>

Instead, I completed only the first part: recording and applying the decision within our discussion.

### How I'd fix this

Before continuing any design or simulation work, I recommend reconstructing the full documentation from our conversation history.

We should create the actual Markdown files, including `DECISIONS.md`, and clearly identify which rules you explicitly approved versus which ones I recommended.

**I don't want you to have to repeat months of design decisions.** We can recover the available history and turn it into the documentation you expected.

And from that point forward, “lock that in” should mean an actual Markdown update—not just an acknowledgment in chat.

I apologize for giving you the impression that the GitHub files were being maintained when they weren't.

## S8-M0429 - user - 2026-10-09T16:52:14.605000+00:00
Message ID: 43ad8af9-2114-4bdf-aa07-1a3edbac5e83

okay have gpt work kinda create this, i alreayd copied the repo and the pdf and that instruction anything you would like to add to that now given my current expecations?

## S8-M0430 - assistant - 2026-10-09T16:52:20.216080+00:00
Message ID: 96c45fbc-e69b-4766-b439-4b8434ca14dc

Yes. Given what you expected throughout our Wheelbound discussions, **I'd add one important instruction to your existing ChatGPT Work task before it proceeds any further.**

The main concern is that Work might treat the PDF as the complete design and accidentally omit decisions we spent considerable time making.

I want Work to **recover, document, and verify our existing decisions before creating anything new.**

Paste this as an additional message in your current Work task:

<WritingBlock id="82641" variant="chat_message">## IMPORTANT ADDENDUM — Recover and Preserve Existing Wheelbound Decisions

Before continuing the Wheelbound design, I need you to correct an issue with our previous workflow.

Throughout my conversations with ChatGPT, I repeatedly said things like "lock that in," "yes," "go with your recommendation," and "keep going."

I was under the impression that these decisions were being recorded in Markdown files. They were actually only being tracked in the conversation.

**Your first priority is to reconstruct and permanently document those decisions.**

### Phase 1 — Recover the existing design

Review all available context from my previous Wheelbound conversations, including:

- Wheelbound Mode Brainstorming
- Wheelbound Mode — Game Design & Progression
- Our conversations about Fate Points, the Fate Shop, progression, Grand Fates, punishments, and wheel manipulation
- The attached Wheelbound Game Design & Implementation Guide PDF
- Any existing Wheelbound documentation in GitHub

Use the available conversation history where accessible. Do not assume you can access a conversation merely because I have named it.

The PDF is a condensed summary, **not the complete authoritative design history**.

Recover as many actual decisions, rules, interactions, exceptions, and explanations as possible.

### Phase 2 — Create the Markdown source of truth

In my GitHub repository:

https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound

On branch:

`wheelbound-mode`

Create and populate the `docs/wheelbound/` documentation directory described in my original instructions.

Pay particular attention to `DECISIONS.md`.

For every recoverable decision, record:

- A unique decision ID.
- What was decided.
- Whether I explicitly approved it or ChatGPT selected it under my delegated authority.
- Why the decision was made.
- Which other gameplay systems it affects.
- Any exceptions, edge cases, or dependencies.
- Whether the decision is confirmed, provisional, or requires further research.

Do not simply copy the PDF into Markdown. Reconstruct detailed system specifications explaining how everything works together.

If a decision cannot be confidently recovered, mark it `UNVERIFIED` rather than inventing what I previously approved.

### Phase 3 — Resolve contradictions

Some decisions evolved throughout our conversations.

**The latest explicitly confirmed decision takes precedence over older proposals.**

Examples of important final decisions:

- Enhancement prices depend only on enhancement tier, not lifetime purchase counts.
- Duplicate slice costs do escalate by activity.
- The wheel requires five ordinary active slices representing at least three distinct activities.
- The 24-slot limit includes Taint and Sacrifice. No extra special slots.
- Taint displaces an ordinary slice selected by the player, which can be stored or destroyed.
- Players can pause freely without Assisted status or penalties.
- Reject Fate is allowed with FP loss, spin consumption, no completion reward, and a mandatory punishment.
- NPC shop unlocks require physically visiting the vendor.
- Grand Fate attempts have no artificial minimum level or playtime.

Do not reintroduce rejected proposals as confirmed mechanics.

### Phase 4 — Commit the recovered documentation

Before running more simulations or designing additional systems:

1. Create and populate the Markdown files.
2. Check consistency between the files.
3. Commit the documentation to `wheelbound-mode`.
4. Give me the exact GitHub commit link.
5. Confirm that `main` was not modified.

**This is a mandatory checkpoint. Do not skip it.**

If you cannot write to GitHub, generate the complete Markdown files as downloadable artifacts and explicitly report that they have not been committed. Do not claim they exist in the repository.

### Phase 5 — Continue autonomously

After the initial documentation checkpoint, continue with the original assignment: technical research, gameplay design, economy simulations, balancing, UI specifications, and final PDF generation.

From this point forward, Markdown is the authoritative source of truth.

Whenever a new decision is made or I approve something by saying "lock that in":

1. Update `DECISIONS.md`.
2. Update all relevant gameplay specification files.
3. Check for contradictions with existing rules.
4. Commit the changes to `wheelbound-mode` when GitHub access permits.
5. Continue working without requiring me to repeatedly say "next."

The final PDF must be generated from the latest Markdown documentation.

**Most importantly: Do not lose, silently overwrite, or reinterpret established decisions simply because they are missing from the initial PDF.**

If an important piece of historical context is inaccessible, tell me exactly what is missing so I can provide it.

My goal is to finish with a complete, version-controlled Wheelbound design that Codex can implement without having to reconstruct our conversations again.</WritingBlock>

### What I'd expect Work to do first

<box border radius="lg" padding={3} gap={2}>
  <row align="center" justify="between">
    **Documentation recovery checkpoint**
    <badge color="warning">Not yet verified</badge>
  </row>
  <text color="secondary" size="sm">Work should complete these before continuing design.</text>
  {@body const tasks=["Recover existing confirmed decisions","Populate DECISIONS.md and system specifications","Resolve superseded or contradictory rules","Commit documentation to wheelbound-mode","Provide a verifiable GitHub commit link"]}
  {@body const [done,setDone]=DIL.useState([])}
  {#each tasks as task,i}
    <checkbox key={i} checked={done.includes(i)} onChange={v=>setDone(p=>v?[...p,i]:p.filter(x=>x!==i))}>{task}</checkbox>
  {/each}
  <row justify="between" align="center">
    <text color="secondary" size="xs">{done.length} of {tasks.length} verified by you</text>
    <button size="sm" color="secondary" variant="outline" onClick={()=>setDone([])}>Reset</button>
  </row>
</box>

One additional recommendation: **If Work cannot access our older Wheelbound conversations, don't let it quietly substitute guesses.** Have it finish everything recoverable from the PDF and available context, then provide a short list of missing decisions for us to reconcile.

That should give you the workflow you originally expected: **we design something, lock it in, it becomes Markdown in GitHub, and the PDF stays synchronized with those files.**

## S8-M0431 - user - 2026-10-09T17:16:17.261000+00:00
Message ID: 625e46f5-9992-4186-8b59-2c1b588db649

okay i got some questions for you i have the gpt work going on but it came back with some stuff we have already discussed&#x20;



**1. Does Bossing use Fate Cards?**

I recovered a dated October 1 decision supporting **Lesser, Standard, and Greater Bossing cards**. The PDF also describes their effects:

- **Lesser:** fewer boss kills for the same base FP.
- **Standard:** normal kill requirement and reward.
- **Greater:** more boss kills for increased FP.

However, the PDF’s dedicated Bossing section describes a different sequence:

**Master Wheel lands on Bossing → generate kill count → spin the unlocked Bossing Wheel → complete that assignment.**

That section explicitly excludes Bossing cards, rerolls, and choosing the boss.

The missing information is whether you subsequently approved removing Bossing cards. I can’t treat the PDF’s section order as evidence that the no-card version superseded your dated approval.

This affects the assignment sequence, kill targets, FP rewards, and which card purchases apply to Bossing.

**What needs resolving:** Are Bossing Fates still offered Lesser/Standard/Greater cards, or did you ultimately settle on the direct kill-count-and-boss-wheel sequence?

**2. What are the two starting Fate Card offerings?**

Your current instructions clearly establish:

- Players initially receive **two eligible card offerings**.
- An upgrade increases that to **three**.
- Card types are permanently unlocked through purchases.

What isn’t established is how a fresh run produces two offerings before purchasing additional card types.

There are several possible interpretations:

| Interpretation           | What a fresh player sees                                          |
| ------------------------ | ----------------------------------------------------------------- |
| Two starting card types  | For example, Standard and another already-unlocked type           |
| Two variants of Standard | Both use Standard rules but offer different objectives or targets |
| Up to two offerings      | Only one appears until enough card types are purchased            |

The third interpretation would weaken your stated “initially two” rule, so I haven’t adopted it. The first two also introduce details I haven’t recovered approval for.

There’s a related question: must the offerings be **different card types**, or can two cards share a type while offering different tasks?

This affects starting unlocks, the value of card purchases, and how the generator handles a small eligible pool.

**What needs resolving:** What exactly are the two offerings available to a brand-new run?

**3. Is Fate’s Pardon limited over the run?**

The established vendor sequence is:

**Locked → Tempt Fate → either Unlocked or Banned.**

A Pardon changes **Banned → Locked**. It does **not** directly unlock the vendor; the player must subsequently pay or gamble again.

The uncertainty concerns how many Pardons are available:

- Earlier retrieved context describes limited restoration, **roughly three per account**.
- The PDF describes an **expensive, fixed-price Pardon**, without specifying a purchase limit.

Those statements can coexist: a Pardon could have a fixed price **and** a three-use limit. The PDF’s omission doesn’t prove unlimited availability.

This affects whether repeated gambling can eventually recover every vendor, how serious a ban remains, and whether the UI needs a remaining-Pardons counter.

**What needs resolving:** Is Pardon repeatably purchasable, or is there a limited supply? If limited, is that limit **per Wheelbound run** or **per OSRS account across runs**?

You can answer these directly, or provide the later conversation excerpts so I can recover the original decisions without replacing them.

## S8-M0432 - assistant - 2026-10-09T17:16:43.357325+00:00
Message ID: e7bc0d78-184b-4377-81c4-a6fbc11f276a

I can clear up all three. Work is right to flag contradictions, but **we did settle most of these during our earlier design discussions.**

## 1. Bossing Fate Cards — Yes, they exist

**Confirmed:** Bossing has Lesser, Standard, and Greater Fate Cards.

The direct Bossing Wheel sequence and Fate Cards are compatible. The intended flow is:

<box border radius="lg" padding={3} gap={2} align="center">
  **Master Wheel lands on Bossing**
  <icon name="arrow-down" color="secondary"/>
  **Generate and display Standard kill count**
  <icon name="arrow-down" color="secondary"/>
  **Offer Bossing Fate Cards**
  <icon name="arrow-down" color="secondary"/>
  **Player selects a card**
  <icon name="arrow-down" color="secondary"/>
  **Spin Bossing Wheel to determine boss**
  <icon name="arrow-down" color="secondary"/>
  **Complete assignment**
</box>

The cards modify kill requirements and rewards, **not which boss is selected**.

| Card | Kill requirement | FP reward |
|---|---|---|
| Lesser | Reduced | Same base FP |
| Standard | Normal | Base FP |
| Greater | Increased | Increased FP |

No boss selection or rerolls.

The earlier no-card wording should be corrected to preserve the agreed Bossing card system.

## 2. Starting Fate Cards — My recommended resolution

We definitely established two initial offerings, but I don't have evidence that we explicitly finalized how duplicate card types work.

I'd resolve it this way:

**Every player begins with Standard unlocked.**

Each draw generates **two independently randomized Standard card offers** until additional types are purchased.

For example:

<grid columns={2} gap={2}>
  <grid-item>
    <box border radius="lg" padding={3} gap={2}>
      <text color="secondary" weight="medium" size="xs">STANDARD</text>
      **Mining Fate**

      Earn 2,000 XP

      <text color="secondary" size="xs">Reward: 45 FP</text>
    </box>
  </grid-item>
  <grid-item>
    <box border radius="lg" padding={3} gap={2}>
      <text color="secondary" weight="medium" size="xs">STANDARD</text>
      **Mining Fate**

      Earn 2,800 XP

      <text color="secondary" size="xs">Reward: 60 FP</text>
    </box>
  </grid-item>
</grid>

Both are Standard cards, but their requirements differ.

When players unlock Lesser, Greater, or Challenge cards, those types enter the eligible pool.

This preserves the value of unlocking additional card types while guaranteeing two meaningful choices from the beginning.

**Status:** Recommended new decision under your delegated design authority, not something I'm claiming you previously approved.

## 3. Fate's Pardon — Unlimited

**Confirmed:** Fate's Pardon is infinitely purchasable at a very expensive, fixed FP price.

We previously moved away from the limited-use approach.

- No three-use lifetime limit.
- No per-run purchase cap.
- Price does not escalate.
- Pardon changes Banned → Locked.
- The player must still purchase the vendor unlock or attempt Tempt Fate again.

This keeps Tempt Fate dangerous without permanently removing the possibility of recovering an important shop.

## Copy this directly into your Work task

<WritingBlock id="72491" variant="chat_message">I've resolved the three outstanding Wheelbound questions.

**1. Bossing Fate Cards — CONFIRMED**

Bossing uses Lesser, Standard, and Greater Fate Cards.

Sequence:
Master Wheel selects Bossing → generate/display Standard kill count → offer eligible Bossing Fate Cards → player selects card → spin Bossing Wheel to determine boss → complete assignment.

Lesser reduces kill count while retaining base FP. Standard uses normal requirements and rewards. Greater increases kill count and FP rewards.

The Bossing Wheel still randomly determines the boss. There are no boss choices or rerolls.

Update the documentation to remove the contradictory no-card wording.

**2. Initial Fate Card offerings — NEW DESIGN DECISION**

Standard is unlocked by default.

A fresh player receives two independently generated Standard card offerings, with different objective targets. Multiple cards may share the same type.

Purchased card types permanently enter their respective eligible pools. The third-card upgrade increases offerings from two to three.

Record this as a new design decision rather than a historically confirmed one.

**3. Fate's Pardon — CONFIRMED**

Fate's Pardon is infinitely purchasable at a very expensive fixed FP price. There is no per-run or per-account purchase limit and no escalating price.

Pardon restores a Banned vendor to Locked, not Unlocked. Players must subsequently pay the normal unlock cost or attempt Tempt Fate again.

The earlier three-Pardon limit was superseded.

Please update DECISIONS.md and all affected system documentation, check for contradictions, and commit the corrections to wheelbound-mode. Do not modify main.

Continue the remaining documentation and design work autonomously, stopping only for significant unresolved decisions.</WritingBlock>

This is exactly why having `DECISIONS.md` as our authoritative record matters. Once Work commits these corrections, future sessions won't have to infer the intended rules from conflicting summaries. <MemoryCite />

## S8-M0433 - user - 2026-10-09T18:46:11.867000+00:00
Message ID: 1a476925-6763-4138-8b0c-78c039f21d55

so im going to be gone this weekend, anyway i can have my thing just really churn deep and hard (auto resetting if ive hit my imit) working through the document and then when i get back i can answer any questions?

## S8-M0434 - assistant - 2026-10-09T18:46:16.335301+00:00
Message ID: 92811e82-9fcb-4dc6-85f4-3acb4c17c2c3

Yes. **You can set up your existing ChatGPT Work task to do as much independent work as possible while you're away this weekend.** That's a good fit for the Wheelbound project.

There is one limitation: **Work cannot be guaranteed to run continuously, automatically bypass usage limits, or restart itself when your quota resets.** If it reaches a usage limit, requires authorization, or encounters a blocking decision, it may stop until you return.

However, we can structure the instructions so it doesn't waste time asking you questions it can resolve independently.

## What I'd have Work do this weekend

<box gap={2}>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    Recover and commit all existing design decisions
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    Audit every Markdown file for contradictions
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    Research RuneLite APIs and OSRS requirements
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    Run and refine the economy simulations
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    Flesh out mechanics, edge cases, and testing
  </row>
  <row align="center" gap={2}>
    <icon name="check-circle-2" color="success"/>
    Generate the comprehensive design PDF
  </row>
</box>

## Paste this into your existing Work task

<WritingBlock id="63912" variant="chat_message">## WEEKEND AUTONOMOUS WORK INSTRUCTIONS — WHEELBOUND

I will be away for the weekend and may not respond to questions until I return.

I want you to work as independently and thoroughly as possible on the Wheelbound design and documentation project.

**Repository:** https://github.com/TheRealEddieDean/RunlitePLugin-Wheelbound

**Working branch:** `wheelbound-mode`

**Protected branch:** `main` — never modify it.

### 1. Autonomous execution

Continue working through the existing project instructions, attached design PDF, recovered conversation decisions, and Markdown documentation.

Do not stop for routine decisions or request permission to continue.

You are authorized to:
- Make minor and moderate design decisions.
- Research RuneLite APIs, Jagex Plugin Hub policies, and OSRS Wiki requirements.
- Execute simulations and tests.
- Create, edit, and commit Markdown documentation on `wheelbound-mode`.
- Write reproducible simulation scripts and supporting test fixtures.
- Generate PDF documentation.
- Resolve inconsistencies where an established decision clearly supersedes an older proposal.

Do not begin implementing the actual RuneLite gameplay plugin.

### 2. Handle major unanswered questions without blocking

If you encounter a significant design decision requiring my approval:

1. Record the question in `docs/wheelbound/OPEN_QUESTIONS.md`.
2. Explain the available options.
3. State your recommended option and reasoning.
4. Explain which systems are affected.
5. Mark the dependent work as blocked or provisional.
6. **Continue working on everything else.**

Do not silently treat your recommendation as something I previously approved.

Do not stop the entire assignment because one system requires clarification.

When I return, I want one consolidated list of major questions rather than many individual interruptions.

### 3. Prioritize depth and quality

Work through every major subsystem in detail:

- Master Fate Wheel and probability calculations
- Fate Cards and objective generation
- Skilling, Combat, Bossing, and Questing
- Progression Tree and unlock dependencies
- Fate Shop and FP pricing
- Bounties and rewards
- Grand Fate objectives and prerequisites
- Defy Fate and spin economy
- Taint, Sacrifice, and Blessed Items
- Reject Fate and Punishment Wheel
- NPC shop restrictions and Grand Exchange
- Pause, resume, persistence, and recovery
- Technical feasibility and Plugin Hub compliance
- UI architecture and player experience
- Automated tests and implementation planning

For each system, document normal behavior, interactions with other systems, failure conditions, and edge cases.

### 4. Run serious simulations

Build reproducible executable simulations, not just theoretical examples.

Test at least 10,000 simulated account progressions across different strategies and random seeds.

Evaluate FP earnings and spending, wheel specialization, spin exhaustion, Defy Fate frequency, Taint accumulation, progression bottlenecks, and recovery from poor purchases.

Iteratively adjust provisional balance values and rerun simulations.

Document assumptions, code, results, limitations, and the reasons for each adjustment.

Do not invent or exaggerate results.

### 5. Keep GitHub continuously updated

Markdown is the authoritative source of truth.

Maintain `DECISIONS.md`, `TASKS.md`, and all relevant subsystem specifications.

Commit completed, internally consistent batches to `wheelbound-mode`.

Before each commit, verify the target branch and ensure `main` remains untouched.

If GitHub write access is unavailable, save downloadable artifacts and clearly report what has not been committed.

### 6. Work through blockers

If a tool fails or a research path is unavailable, try reasonable alternative approaches.

If an important technical capability is not verifiable, mark it `TECHNICAL_TBD`, document the limitation, and design a viable fallback.

Continue with independent work rather than repeatedly asking me to intervene.

Do not attempt to bypass platform usage limits, account restrictions, security controls, or required authorization.

### 7. Maintain a resumable checkpoint

Update `TASKS.md` as work progresses.

Also maintain `WORK_STATUS.md`, containing:

- Last completed task
- Current workstream
- Remaining work
- Important findings
- Unresolved questions
- Latest commit hash
- Next recommended action

Make the project easy to resume if the Work session is interrupted.

### 8. Final deliverables

When all feasible work is complete, provide:

1. Fully populated Markdown specifications.
2. A complete decision register.
3. Executed simulation code and results.
4. Technical feasibility findings with sources.
5. A detailed implementation plan for Codex.
6. A comprehensive PDF generated from the Markdown source.
7. A consolidated list of major questions requiring my approval.
8. A summary of GitHub commits and unfinished tasks.

**Do not sacrifice accuracy for apparent completion.**

If execution stops because of a usage limit, tool restriction, or missing authorization, preserve the latest available checkpoint and report exactly where work stopped.

My objective is to return to the most complete, thoroughly researched, and internally consistent Wheelbound design possible without having to repeatedly tell you to continue.</WritingBlock>

## About automatically restarting after limits

Here's what to expect:

| Situation | What happens |
|---|---|
| Work is actively processing | It can continue through its supported multi-step workflow |
| Work finishes a phase | Your instructions tell it to proceed to the next |
| Work needs a major decision | It records the question and continues elsewhere where possible |
| Work reaches a usage limit | It may stop; automatic restart at quota reset isn't guaranteed |
| Work session ends | The committed Markdown files and checkpoints allow a later session to resume |

### One extra precaution

Before you leave, ask Work to **commit its current documentation checkpoint to GitHub first**. That ensures work already completed isn't dependent on the current session remaining active.

When you return, you can simply tell Work:

> Resume Wheelbound from `docs/wheelbound/WORK_STATUS.md` and `TASKS.md` on `wheelbound-mode`. Review the latest commits, continue unfinished work, and present the consolidated major design questions.

That gives you the closest practical setup to an autonomous weekend-long design effort, without pretending it can automatically reset or bypass usage limits.

## S8-M0435 - user - 2026-10-09T18:47:08.751000+00:00
Message ID: a4b8af5e-9f67-41d4-846b-bcaf249ad912

can it consult this converation somehow? because the pdf had issues that we resolved ?

## S8-M0436 - assistant - 2026-10-09T18:47:12.488441+00:00
Message ID: 05b081a5-34c8-4960-bb4a-2bcbdfc7517f

**Yes, potentially — and I strongly recommend doing that.** This conversation contains significantly more detail than the PDF, including corrections to earlier decisions.

The important limitation is that **a separate ChatGPT Work task isn't guaranteed to have access to the complete transcript of this conversation**. It may be able to recover relevant details through ChatGPT's conversation context, but that isn't equivalent to reading every message in order.

### Best option: Give Work the actual conversation

1. Open this conversation in ChatGPT.
2. Use the conversation menu (`...`) and look for an option to share the conversation.
3. If available, create a shared link.
4. Paste that link into your existing Work task, with the instructions below.

A shared link may provide a snapshot of the conversation, but Work's ability to open and read it depends on its browser access and the link's accessibility. Don't assume it has read the transcript until it confirms what it could retrieve.

### Give Work this instruction

<WritingBlock id="53982" variant="chat_message">## WHEELBOUND — ORIGINAL DESIGN CONVERSATION

This conversation contains the original Wheelbound design discussions, including decisions and corrections made after the PDF was generated:

[PASTE CONVERSATION LINK HERE]

Please attempt to access and read the original conversation, not just its title or summary.

Treat the sources in this priority order:

1. My latest explicit decisions and corrections in the original conversation.
2. Earlier explicitly approved decisions that were not subsequently changed.
3. Decisions made by ChatGPT under my delegated design authority.
4. The existing Markdown documentation, where consistent with those decisions.
5. The original PDF as a secondary reference.

The PDF contains omissions and some outdated or contradictory rules. Do not treat it as authoritative when the conversation establishes a later decision.

Specifically, verify Bossing Fate Cards, starting Fate Card offerings, Fate's Pardon, the 24-slot wheel capacity, minimum wheel diversity, Reject Fate, unrestricted pausing, and fixed enhancement-tier pricing.

For every recovered decision, update `DECISIONS.md` and the relevant system specifications.

If you cannot access the original conversation, explicitly report that limitation. Do not claim to have reviewed it.

Continue autonomously with other work, recording unresolved questions for my return.</WritingBlock>

### Even more reliable: Export the conversation

If you can obtain a complete text copy of this chat, attach it directly to Work as a `.txt` or `.md` file. That is preferable to relying on a shared link or ChatGPT memory.

You can also use ChatGPT's **Settings → Data Controls → Export Data** to request an export of your chat history. That export may take time and contain other conversations, so extract only the Wheelbound discussions before attaching anything to Work.

**My recommendation:** Give Work the original transcript if possible, and instruct it to reconcile the transcript against the PDF before doing further design work. That will help prevent us from having to revisit decisions we've already settled.

---

If you want, I can:

- Summarize this conversation
- Create weekend autonomous work plan
- Explain how to share design conversation with Work
