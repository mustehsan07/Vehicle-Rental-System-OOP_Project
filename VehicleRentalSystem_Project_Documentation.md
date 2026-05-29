# Vehicle Rental Management System — Project Documentation

This document describes the current state of the Vehicle Rental Management System in this workspace. It covers the architecture, modules, runtime data stores, build/run instructions, shared UI theming, email notifications, and maintenance notes.

Status: active Swing-based Java application using in-memory data stores for customers, vehicles, rentals, rental requests, and rental history. The code demonstrates OOP principles and now uses shared theme/table utilities plus SMTP-based email notifications for request events.

Contents
- Overview
- Current project structure
- Core modules and responsibilities
- Data stores and model layer
- Build and run
- UI, theme, and table styling
- Email notifications and environment variables
- Security and maintenance notes

Overview
--------
This application is a desktop GUI Vehicle Rental Management System built with Swing. Main user flows:
- Customer: register/login, view available vehicles, submit rental requests, view active rentals and rental history.
- Admin: manage vehicles and customers, view rental requests, approve/reject requests, view rental data.

Current project structure
-------------------------
Top-level workspace contains `src/`, `lib/`, `out/`, `.env`, and `.env.example`.

- `src/model` — domain objects such as `Vehicle`, `Car`, `Bike`, `Truck`, `Customer`, `Rental`, `RentalHistory`, `RentalRequest`, `Admin`.
- `src/data` — in-memory stores and loaders such as `CustomerData`, `VehicleData`, `RentalData`, `RentalHistoryData`, `RentalRequestData`, `SampleDataLoader`.
- `src/auth` — authentication controller and UI.
- `src/customer` — customer dashboard and requested/active rental views.
- `src/vehicle_display` — available vehicles screen and booking workflow.
- `src/admin` — admin dashboard, vehicle/customer management, and rental request dialog.
- `src/rental_history` — reusable rental history table/panel.
- `src/utils` — shared app theme, table styling helpers, SMTP config, and email service.

Core modules and responsibilities
----------------------------------
- Authentication: `src/auth/AuthController.java` handles login/register logic and returns an `AuthRecord`.
- Vehicle browsing: `src/vehicle_display/VehicleRentalModuleApp.java` shows available vehicles, booking details, and request submission.
- Customer dashboard: `src/customer/CustomerDashboard.java` shows active rentals, history, and the requested vehicles dialog.
- Admin dashboard: `src/admin/AdminPanelGUI.java` and related admin tabs provide vehicle/customer management.
- Rental requests: `src/admin/RentalRequestsDialog.java` lists pending requests and supports approve/reject actions.
- Rental history: `src/rental_history/RentalHistoryPanel.java` provides a reusable table for historical rentals.

Data stores and model layer
--------------------------
The application uses simple in-memory data stores:

- `src/data/CustomerData.java` — customer CRUD and lookup.
- `src/data/VehicleData.java` — vehicle store and lookup.
- `src/data/RentalData.java` — active rentals.
- `src/data/RentalRequestData.java` — pending/approved/rejected rental requests.
- `src/data/RentalHistoryData.java` — historical rentals.
- `src/data/SampleDataLoader.java` — sample records and bootstrap data.

The main OOP model classes live in `src/model`:

- `Vehicle` is abstract and defines rent calculation.
- `Car`, `Bike`, and `Truck` extend `Vehicle` and override pricing behavior.
- `Customer`, `Rental`, `RentalHistory`, `RentalRequest`, and `Admin` represent the business entities used across the app.

Build and run
-------------
Requirements: JDK 11+.

The project is run from `app.MainClass` and expects third-party jars in the project-root `lib/` folder.

Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
New-Item -ItemType Directory -Force lib | Out-Null

Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/2.0.2/jakarta.mail-2.0.2.jar" -OutFile "lib/jakarta.mail-2.0.2.jar"
Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/com/sun/activation/jakarta.activation/2.0.1/jakarta.activation-2.0.1.jar" -OutFile "lib/jakarta.activation-2.0.1.jar"

Get-Content .env | ForEach-Object {
	$line = $_.Trim()
	if (-not [string]::IsNullOrWhiteSpace($line) -and -not $line.StartsWith('#')) {
		$parts = $line -split '=', 2
		if ($parts.Length -eq 2) {
			$name = $parts[0].Trim()
			$value = $parts[1].Trim().Trim('"').Trim("'")
			Set-Item -Path "Env:$name" -Value $value
		}
	}
}

javac -d out -sourcepath src -cp "lib/*" $(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp "out;lib/*" app.MainClass
```

Linux / macOS:

```bash
mkdir -p out lib
curl -L -o lib/jakarta.mail-2.0.2.jar https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/2.0.2/jakarta.mail-2.0.2.jar
curl -L -o lib/jakarta.activation-2.0.1.jar https://repo1.maven.org/maven2/com/sun/activation/jakarta.activation/2.0.1/jakarta.activation-2.0.1.jar
javac -d out -sourcepath src -cp "lib/*" $(find src -name "*.java")
java -cp "out:lib/*" app.MainClass
```

UI, theme, and table styling
----------------------------
- `src/utils/AppTheme.java` centralizes shared colors, fonts, sizes, radius values, and background painting.
- `src/utils/TableStyles.java` centralizes shared table header/body renderers and alignment helpers.
- The available fleet screen, customer/admin tables, and rental request dialog now share the same visual language.
- Dialogs such as requested vehicles and rent requests use custom undecorated headers and rounded windows.

Email notifications and environment variables
---------------------------------------------
- `src/utils/SmtpConfig.java` reads SMTP values from environment variables.
- `.env` is used locally to store SMTP credentials and notification recipients.
- `.env.example` documents the expected variable names with placeholder values.
- `src/utils/EmailService.java` sends notifications when:
  - a new rental request is created
  - an admin approves a request
  - an admin rejects a request

Required environment variables:

- `SMTP_HOST`
- `SMTP_PORT`
- `SMTP_USERNAME`
- `SMTP_PASSWORD`
- `SMTP_FROM`
- `SMTP_AUTH`
- `SMTP_STARTTLS`
- `ADMIN_NOTIFICATION_EMAILS`

Security and maintenance notes
------------------------------
- Keep `lib/`, `.env`, and other local-only artifacts out of version control.
- Do not store real SMTP secrets in source files; use environment variables only.
- The current mail flow uses reflection so the app can run with either Jakarta Mail or legacy JavaMail jars.
- The app still uses in-memory data stores, so data resets on restart unless persistence is added later.
- The request flow depends on the console for diagnostics; mail failures are printed there for troubleshooting.

Recommended next steps
----------------------
1. Add persistent storage for customers, vehicles, and rental requests so data survives restart.
2. Replace remaining reflection-based mail calls with direct typed Jakarta Mail usage once the dependency choice is final.
3. Add a small SMTP test utility or settings panel so mail can be verified without submitting a rental request.
4. Add tests for the data-layer helpers (`CustomerData`, `RentalRequestData`, `RentalHistoryData`).

Updated: May 30, 2026

