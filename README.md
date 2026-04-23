# Fleet Mobile — Android

Два приложения в одном репозитории (monorepo):

| Приложение | Аудитория | Назначение |
|------------|-----------|------------|
| **Fleet Manager** | Управляющий автопарком электрогрузовиков | Парк ТС, водители, плановое ТО, реакция на внеплановые ситуации |
| **Fleet Driver** | Водитель | Быстрый доступ к данным по авто, зарядке и сервису; бортовой журнал и единое окно в поддержку |

Детальный план спринтов: [ROADMAP.md](./ROADMAP.md).

Бэкенд-контракт (REST, Flask): см. `../backend/http_api.py` в проекте zai-gen.

## Текущее состояние (Sprint 1)

В репозитории создан базовый Android монорепо-каркас:

- `:app-manager` — отдельное приложение Fleet Manager
- `:app-driver` — отдельное приложение Fleet Driver
- `:core:ui` — общие UI-ресурсы (цвета/токены)

Оба приложения запускаются как отдельные launcher app с разными `applicationId`.

## Быстрый старт

1. Откройте папку `fleet-mobile` в Android Studio.
2. Дождитесь Gradle Sync и установки SDK (если IDE попросит).
3. Выберите run-конфигурацию:
   - `app-manager`
   - `app-driver`
4. Запустите на эмуляторе Android или физическом Android-устройстве.

## Как тестировать на macOS, если у вас iOS

iPhone нельзя использовать для запуска Android APK, поэтому варианты такие:

1. **Android Emulator (рекомендуется)**
   - Android Studio -> Device Manager -> Create Device
   - Системный образ: Android 14/15 (Google APIs, arm64 для Apple Silicon)
   - Запуск и тест обоих приложений по очереди

2. **Физическое Android-устройство (если есть)**
   - Включить Developer options + USB debugging
   - Подключить по USB и выбрать устройство в Run target

3. **Для CI/проверки сборки без запуска UI**
   - Достаточно собирать `assembleDebug` для обоих модулей

## CI

Добавлен workflow GitHub Actions: `.github/workflows/android-debug.yml`.

- Триггеры: `push`, `pull_request`, `workflow_dispatch`
- Сборка: `:app-manager:assembleDebug` и `:app-driver:assembleDebug`
- Артефакты: два debug APK, доступны в Actions run artifacts
