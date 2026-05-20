# Vehicle Rental Management System — Project Documentation

This document describes the current, production-ready state of the Vehicle Rental Management System in this workspace. It covers architecture, modules, runtime data stores, build/run instructions, design tokens, and recommended next steps.

Status: active Swing-based Java application using in-memory data stores for Customers, Vehicles, Rentals and Rental History. The code demonstrates OOP principles (Encapsulation, Abstraction, Inheritance, Polymorphism) and uses a modular layout of GUI modules.

Contents
- Overview
- Project structure (current)
- Key modules and responsibilities
- Data stores and model layer
- Build and run
- UI and design tokens
- Security and maintenance notes
- Recommended next steps

Overview
--------
This application is a desktop GUI (Swing) Vehicle Rental Management System. Main user flows:
- Customer: register/login, view available vehicles, rent vehicles, return vehicles, view rental history.
- Admin: manage vehicles (CRUD), view customers and rental data.

Project structure (current)
---------------------------
Top-level workspace: project root contains a `src/` folder with modular packages. Notable packages and example files:

- `src/model` — domain objects: `Vehicle`, `Car`, `Bike`, `Truck`, `Customer`, `Rental`, `RentalHistory`, `Admin`.
- `src/data` — in-memory data stores and helpers: `CustomerData`, `VehicleData`, `RentalData`, `RentalHistoryData`, `SampleDataLoader`.
- `src/auth` — authentication UI and controller: `ProVehicleLogin`, `AuthController`.
- `src/customer` — customer dashboard UI: `CustomerDashboard`.
- `src/vehicle_display` — available-vehicles UI and rent workflow: `VehicleRentalModuleApp`, `VehicleCatalog`, display `Vehicle` model used for UI.
- `src/rental` — an independent rental helper module used in some places (contains its own `Vehicle`/`RentalService` model for demonstrations).
- `src/rental_history` — consolidated `RentalHistoryPanel` (UI panel showing rental history table).

See the source tree (workspace) for all files; key files referenced below link to the workspace paths.

Key modules and responsibilities
-------------------------------
- Authentication: `src/auth/AuthController.java` handles login/register logic and returns `AuthRecord`. `ProVehicleLogin` is the login UI.
- Customer UI: `src/customer/CustomerDashboard.java` provides profile, active rentals, and rental history views. It now uses `rental_history/RentalHistoryPanel` for the history table.
- Vehicle Display: `src/vehicle_display/VehicleRentalModuleApp.java` shows available vehicles, booking details, and a rent popup.
- Rental logic: runtime rentals are stored in `src/data/RentalData.java`. Some modules also use `src/rental/RentalService.java` for a separate engine-like example.
- Admin: Admin UI and services live under `src/admin` (vehicle/customer management GUI and wrapper services) and use `data` stores.

Data stores and model layer
--------------------------
- Central model objects live in `src/model` and include `Vehicle` (abstract), `Car`, `Bike`, `Truck`, `Customer`, `Rental`, `RentalHistory`. These implement OOP fundamentals:
	- Encapsulation: private/protected fields with getters/setters (see [src/model/Customer.java](src/model/Customer.java)).
	- Abstraction: `Vehicle` is abstract and defines `calculateRent(int days)` used by children (see [src/model/Vehicle.java](src/model/Vehicle.java)).
	- Inheritance/Polymorphism: `Car`, `Bike`, `Truck` extend `Vehicle` and override `calculateRent(...)`.

- Runtime data stores are simple in-memory ArrayLists with helper APIs:
	- `src/data/CustomerData.java` — customers CRUD and lookup.
	- `src/data/VehicleData.java` — canonical vehicle store (add/update/remove/find).
	- `src/data/RentalData.java` — active rentals store and lookups.
	- `src/data/RentalHistoryData.java` — historical rentals list and per-customer queries.

Note: the project currently contains multiple vehicle representations (in `model`, `vehicle_display`, and `rental` packages). They serve UI convenience and module encapsulation, but this duplication is a maintenance concern. Recommendation: consolidate to `model.Vehicle` where practical and adapt UI view models or adapters.

Build and run
-------------
Requirements: JDK 11+ installed.

From project root compile and run (simple manual commands):

```powershell
javac -d out -sourcepath src -cp src src/main/Main.java
java -cp out main.Main
```

Or run the primary UI classes directly during development from an IDE (IntelliJ IDEA, Eclipse, or VS Code with Java extensions). The main UIs to run manually are:
- `src/auth/ProVehicleLogin.java` — launches the login screen.
- `src/vehicle_display/VehicleRentalModuleApp.java` — available vehicles screen.

If you want a quick test harness for the rental history panel, run `src/rental_history/RentalHistoryPanel.java` main; for customer flows run `src/customer/CustomerDashboard.java` main.

UI and design tokens
--------------------
The repository contains a set of UI helpers and theme tokens used across GUI classes (colors, fonts, spacing). There is an example `utils.Theme` recommended for centralization. Current UI code uses local color constants (see `src/vehicle_display/VehicleRentalModuleApp.java` and `src/customer/CustomerDashboard.java`).

Security and maintenance notes
------------------------------
- Password handling: At present, plain-text passwords are available via getters and are included in `AuthController.AuthRecord`. For production or secure practices, do not store or expose plaintext passwords. Use hashed password storage and compare hashes during authentication.
- Reflection usage: A few places previously used reflective calls (Class.forName / Method.invoke). Refactoring those to typed calls improves readability and safety; parts of the codebase have already been simplified. See `src/vehicle_display/VehicleRentalModuleApp.java` and search for `Class.forName` to find remaining reflections.
- Duplicate models: There are UI-specific `Vehicle` classes in `vehicle_display` and `rental` packages in addition to `model.Vehicle`. Consolidating these reduces bugs and simplifies teaching the code to beginners.

Recommended next steps
----------------------
1. Consolidate the `Vehicle` domain into a single canonical `model.Vehicle` and replace or adapt UI/rental types to use it or to use explicit adapters.
2. Replace remaining reflection with direct typed calls for clarity (example replacement: instantiate `VehicleRentalModuleApp` and call `setCurrentCustomer(...)` directly). This improves maintainability and is easier for beginners.
3. Improve password handling: store hashed passwords and remove `getPassword()` from public interfaces; update `AuthController` to compare hashes.
4. Add short Javadoc to public model classes and controller methods to help new contributors learn the code quickly.
5. Add a `README.md` or `DEV_GUIDE.md` with quick start, coding conventions, and how to run tests/manual checks.

Contributing and style
----------------------
- Preferred format: small, focused commits; keep GUI changes and model changes separate.
- Follow single-responsibility per class where possible; keep UI logic out of model/data classes.
- Add unit tests for data-layer logic (e.g., `RentalData`, `RentalHistoryData`) before refactors.

Contact / Authors
------------------
Primary maintainer: Muhammad Mustehsan (project folder owner). See AUTHORS or ask in repository for team roles.

Appendix — quick file pointers
-----------------------------
- `src/model/Vehicle.java` — abstract vehicle model and OOP anchors.
- `src/model/Car.java`, `src/model/Bike.java`, `src/model/Truck.java` — concrete vehicle types.
- `src/data/CustomerData.java`, `src/data/VehicleData.java`, `src/data/RentalData.java`, `src/data/RentalHistoryData.java` — runtime stores.
- `src/customer/CustomerDashboard.java` — customer UI and stats cards.
- `src/vehicle_display/VehicleRentalModuleApp.java` — available vehicles UI and Rent popup.
- `src/rental_history/RentalHistoryPanel.java` — consolidated history UI panel.

If you'd like, I can:
- produce a `DEV_GUIDE.md` with IDE run configurations and debug tips, or
- automatically refactor remaining reflections and duplicate Vehicle types (I can draft a safe plan and apply changes incrementally).

---
Updated: May 21, 2026

