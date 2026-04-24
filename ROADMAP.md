# Fleet Mobile Roadmap (Updated)

Этот roadmap отражает **текущее фактическое состояние** проекта и ближайшие приоритеты по двум приложениям: `app-manager` и `app-driver`.

## Current Status Snapshot

- **Build system:** monorepo на Gradle Kotlin DSL, модуль `core/ui` используется в обоих приложениях.
- **app-manager:** базовые CRUD-экраны и навигация работают, но сервисный раздел (`Журналы/Service`) остается нестабильным в части отображения списков.
- **app-driver:** активно развивается UI-flow, добавлены экраны dashboard/home/details/journal/STS/specs.

## Focus Areas (Next 3 Iterations)

### Iteration A — Stabilization & Build Reliability

- Зафиксировать стабильную сборку `:app-manager` и `:app-driver` без ручных workaround.
- Убрать остаточные XML/resource регрессии после UI-итераций.
- Вывести единый check-list перед merge:
  - `./gradlew :app-manager:assembleDebug`
  - `./gradlew :app-driver:assembleDebug`
  - smoke run на эмуляторе (основные переходы).

### Iteration B — app-driver UX Parity with Figma

- Довести экран авто (driver home) до визуального match:
  - позиционирование KPI/иконок/карты,
  - размер и отступы action-блоков,
  - подключение финального ассета EV из `zai-gen/public/vehicles/ev50-ya-angle.svg`.
- Завершить flow:
  - login by phone -> dashboard,
  - dashboard -> profile / cars / routes / chargers / support,
  - home -> journal / vehicle info / map.
- Улучшить journal модалку (`Новая запись`): dropdown, фото-галерея, switch состояния, CTA.

### Iteration C — app-manager Service Screen Recovery

- Отладить и стабилизировать сервисный экран с `Заявками` и `Зарядками`.
- Убрать циклическую проблему состояния/навигации:
  - проверка data source,
  - проверка adapter binding,
  - проверка layout visibility/constraints.
- Конечная цель: гарантированный рендер обоих списков + корректные переходы в детали.

## Known High-Priority Issue

### Service Screen Loop (app-manager)

- **Симптом:** сервисный экран периодически открывается, но списки пустые или рендерятся нестабильно.
- **Риск:** блокирует демонстрацию одного из ключевых менеджерских сценариев.
- **Current diagnostic artifact:** `debug-service-activity.md`.
- **Exit criterion:** deterministic render при каждом запуске + прохождение smoke-test сценария (open -> switch tab -> open details -> back).

## Milestones

- **M1:** Green debug builds + smoke flow driver/manager.
- **M2:** Driver UI parity (home + journal + vehicle details) > 90% визуального совпадения.
- **M3:** Manager service flow fixed (no empty states unless real data empty).
- **M4:** Demo-ready package for stakeholder review.

## Definition of Done (per feature block)

- UI соответствует референсу по структуре и ключевым отступам.
- Навигация без тупиков и циклических переходов.
- Нет resource/linking/merge ошибок на debug build.
- Минимум 1 проверяемый user-flow проходит вручную end-to-end.
