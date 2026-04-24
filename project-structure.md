# Fleet Mobile Project Structure

Краткая карта проекта с фокусом на уже реализованные блоки и зоны доработки.

## Legend

- `DONE` — реализовано и используется в текущем flow.
- `WIP` — реализовано частично, требуется доработка.
- `RISK` — проблемная зона, влияет на стабильность демо.
- `NEXT` — следующий этап разработки.

## Repository Layout

```text
fleet-mobile/
├── app-manager/                    # Android app for fleet managers
│   ├── src/main/java/...           # Activities, adapters, repositories
│   ├── src/main/res/layout         # Manager screens and dialogs
│   └── src/main/res/values         # Strings, themes
├── app-driver/                     # Android app for drivers
│   ├── src/main/java/...           # Driver flows: login/dashboard/home/journal/details
│   ├── src/main/res/layout         # Driver UI screens
│   └── src/main/res/values         # Driver-specific strings and theme
├── core/ui/                        # Shared design tokens and visual primitives
│   └── src/main/res                # colors, dimens, styles, shared drawables
├── .github/workflows/              # CI pipelines (debug APK builds)
├── debug-service-activity.md       # Detailed diagnostics for manager service screen
├── ROADMAP.md                      # Current development roadmap
└── project-structure.md            # This document
```

## Component Map by Status

### `core/ui`

- `DONE` Shared colors, dimensions, typography, card styles.
- `DONE` Reusable `bg_kpi_widget` for KPI blocks in both apps.
- `NEXT` Extend token set for stricter Figma parity (spacing/typography tiers).

### `app-manager`

- `DONE` Dashboard, entity lists, detail cards, CRUD dialogs, bottom navigation.
- `DONE` Relations/navigation between key entities (cars/fleets/companies/users).
- `WIP` Visual parity fine-tuning for selected headers/cards.
- `RISK` Service/Journals area (`ServiceActivity`) has cyclic rendering problem with tickets/charging content.
- `NEXT` Stabilize service data flow and reintroduce fully reliable dual-list scenario (`Заявки` + `Зарядки`).

### `app-driver`

- `DONE` Phone input screen with disabled/enabled CTA logic.
- `DONE` Driver dashboard with KPI and navigation buttons.
- `DONE` Driver home, vehicle details, logbook list, new record, STS, specs screens.
- `WIP` Pixel-level Figma alignment (asset sizing, paddings, map/tile balance).
- `NEXT` Complete final UX polish and unify transitions between dashboard/home/support paths.

### CI / DX

- `DONE` GitHub Actions debug APK build.
- `WIP` Formalize smoke test checklist for both app modules.
- `NEXT` Add lightweight quality gate (lint/build matrix) before merge.

## Main Risk Block: Service Screen Cycle

`app-manager` service section is the primary unresolved block.  
Target diagnostic sequence:

1. verify repository data population (`ManagerRepository` / fallback data);
2. verify list binding lifecycle in `ServiceActivity`;
3. verify visibility/constraint conditions in `activity_service.xml`;
4. verify navigation state restoration after back stack transitions.

Reference diagnostic guide: `debug-service-activity.md`.

## Recommended Near-Term Development Order

1. Stabilize manager service screen (`RISK` block first).
2. Final pass on driver Figma parity (`WIP` visual block).
3. Add repeatable smoke-test script/checklist.
4. Freeze demo baseline and branch for stakeholder review.
