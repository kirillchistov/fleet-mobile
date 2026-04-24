# Debug Task: Empty List in `ServiceActivity` ("Journals")

## Goal

Identify why `ServiceActivity` opens correctly but shows no list items in the "Journals" section.

## Current Scope

The screen has been simplified to:
- one `ServiceActivity`
- one `RecyclerView`
- one list type (service tickets)
- no tab switching logic

This should reduce moving parts and make root-cause analysis straightforward.

## Files to Inspect

### 1) Data Source
- `app-manager/src/main/java/com/sova/fleet/manager/ManagerRepository.kt`
- `app-manager/src/main/java/com/sova/fleet/manager/ServiceModels.kt`

Check:
- `serviceTickets` is populated at runtime
- ticket fields are valid
- `vehicleLabel()` does not fail for ticket `vehicleId`

### 2) Screen Logic
- `app-manager/src/main/java/com/sova/fleet/manager/ServiceActivity.kt`

Check:
- `renderTickets()` is called from `onCreate()`
- list size before `adapter.submit(...)`
- `adapter.itemCount` after submit
- `emptyStateText` visibility logic

### 3) Adapter + Item Binding
- `app-manager/src/main/java/com/sova/fleet/manager/ManagerItemAdapter.kt`
- `app-manager/src/main/res/layout/item_manager_entity.xml`

Check:
- `submit()` updates internal list
- `notifyDataSetChanged()` is called
- `getItemCount()` returns expected value
- item text color/background is readable on dark theme

### 4) Layout/Constraints
- `app-manager/src/main/res/layout/activity_service.xml`

Check:
- `RecyclerView` has non-zero height at runtime
- constraints are valid (`top` to title, `bottom` to nav bar)
- no overlay blocks visual rendering

### 5) Routing
- `app-manager/src/main/java/com/sova/fleet/manager/EntityListActivity.kt`
- `app-manager/src/main/AndroidManifest.xml`

Check:
- navigation opens `ServiceActivity` (and not any legacy screen)
- `ServiceActivity` is declared in manifest with correct theme

## Added Debug Logs (Already Patched)

`ServiceActivity` now logs:
- activity start
- recycler adapter attachment
- `serviceTickets` size from repository
- chosen data source size and whether fallback is used
- `adapter.itemCount` after submit
- empty-state visibility
- item id on details click

Tag:
- `ServiceActivityDebug`

## How to Collect Evidence

1. Open `Journals` screen in debug build.
2. Capture Logcat filtered by `ServiceActivityDebug`.
3. Confirm these values:
   - repository size
   - submitted list size
   - adapter item count
   - empty state status

## Expected Healthy Sequence

- `onCreate: ServiceActivity started`
- `renderTickets: repository serviceTickets size > 0` (or fallback used)
- `renderTickets: data source size > 0`
- `renderTickets: adapter itemCount > 0`
- `renderTickets: emptyState visible=false`

If `adapter itemCount > 0` but UI is still empty, the issue is likely visual/layout (item rendering or constraints), not data.

