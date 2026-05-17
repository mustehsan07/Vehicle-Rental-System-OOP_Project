# Vehicle Rental Management System

## Project Overview
The Vehicle Rental Management System is a GUI-based Java OOP project where customers can register, log in, view available vehicles, rent vehicles, return them, and track rental history. Admin can manage vehicles through CRUD operations.

This project demonstrates:
- Encapsulation
- Abstraction
- Inheritance
- Polymorphism
- Java GUI (Swing/JavaFX)
- Runtime CRUD using ArrayLists (No Database)

---

## Complete Folder Structure

```plaintext
VehicleRentalSystem/
│
├── src/
│   ├── main/
│   │   ├── Main.java
│   │   └── AppLauncher.java
│   │
│   ├── model/
│   │   ├── Vehicle.java
│   │   ├── Car.java
│   │   ├── Bike.java
│   │   ├── Truck.java
│   │   ├── Customer.java
│   │   ├── Rental.java
│   │   └── Admin.java
│   │
│   ├── data/
│   │   ├── VehicleData.java
│   │   ├── CustomerData.java
│   │   ├── RentalData.java
│   │   └── SampleDataLoader.java
│   │
│   ├── utils/
│   │   ├── ValidationUtil.java
│   │   ├── IDGenerator.java
│   │   └── CostCalculator.java
│   │
│   ├── module1_admin/
│   │   ├── AdminDashboard.java
│   │   ├── AddVehicleGUI.java
│   │   ├── UpdateVehicleGUI.java
│   │   └── ViewVehicleGUI.java
│   │
│   ├── module2_auth/
│   │   ├── LoginGUI.java
│   │   ├── RegisterGUI.java
│   │   └── AuthController.java
│   │
│   ├── module3_vehicle_display/
│   │   ├── AvailableVehiclesGUI.java
│   │   ├── VehicleSearchGUI.java
│   │   └── RentalCostGUI.java
│   │
│   ├── module4_rental/
│   │   ├── RentVehicleGUI.java
│   │   ├── ReturnVehicleGUI.java
│   │   └── RentalController.java
│   │
│   ├── module5_history/
│   │   ├── RentalHistoryGUI.java
│   │   ├── CustomerHistoryGUI.java
│   │   └── HistoryController.java
│   │
│   └── assets/
│       ├── icons/
│       └── images/
```

---

## Team Responsibilities

### Muhammad Mustehsan: Admin Module
- Add Vehicle
- Update Vehicle
- View Vehicles

### Member 2: Authentication Module
- Register Customer
- Login Customer
- Validation

### Member 3: Vehicle Display Module
- Show Available Vehicles
- Search Vehicles
- Calculate Rental Cost

### Member 4: Rental Module
- Rent Vehicle
- Return Vehicle

### Sumeed Ahmed: History Module
- View Rental History
- View Customer History

---

## OOP Implementation

### Abstraction
Vehicle is an abstract class.

### Inheritance
Car, Bike, Truck inherit Vehicle.

### Encapsulation
Private fields with getters/setters.

### Polymorphism
calculateRent() overridden in child classes.

---

## GUI Screens

1. Login Screen
2. Registration Screen
3. Admin Dashboard
4. Add Vehicle Screen
5. Update Vehicle Screen
6. View Vehicles Screen
7. Available Vehicles Screen
8. Rent Vehicle Screen
9. Return Vehicle Screen
10. Rental History Screen

## Running The Admin Module

Because the admin classes use `package admin;`, the launcher must be started with the fully qualified class name.

From `src/admin`, compile and run with:

```powershell
javac -d ..\..\out\admin *.java
java -cp ..\..\out\admin admin.Test
```

If you run `java Test`, Java will look for a default-package class named `Test` and the admin panel will not start.
## UI Style Guide

This section defines visual tokens and basic component styling to keep the application's UI consistent and professional.

- **Purpose:** Provide a single source of truth for colors, typography, spacing, and component tokens so developers and designers can build consistent screens.


### Color Palette

- **Accent:** #7C4DFF — custom violet-indigo hue (primary action and highlights)
- **Accent (Hover/Active):** #5E2BFF — darker shade used for hover/focus states
- **Background (page):** #F5F7FA — app background
- **Card / Surface:** #FFFFFF — panels and cards
- **Text Primary:** #0F1724
- **Text Secondary:** #6B7280

Notes: use these tokens (`accent`, `accentHover`, `background`, `surface`, `textPrimary`, `textSecondary`) across the codebase instead of additional color variables.

### Typography

- **Primary font:** Inter, fallback `Segoe UI`, `Helvetica`, sans-serif
- **Font weights:** Regular 400, Medium 500, SemiBold 600, Bold 700
- **Scale (desktop):**
	- Display / H1: 32px
	- H2: 24px
	- H3: 20px
	- Body Large: 16px
	- Body Regular: 14px
	- Small / Caption: 12px

Use consistent line-height (1.4–1.6) and limit display font sizes on small screens.

### Spacing & Layout

- **Base grid:** 8px
- **Spacing scale:** 4px, 8px, 16px, 24px, 32px, 48px
- **Border radius:** 8px (pill buttons can be 999px)
- **Container widths:** center main content with max-width 1200px

### Elevation & Shadows

- **Shadow Small:** subtle card lift
- **Shadow Medium:** used for dialogs and popovers
- **Shadow Large:** used sparingly for prominent overlays


### Component Tokens (examples)

- **Button (Primary):** background `accent`, hover `accentHover`, text `#FFFFFF`, radius 8px, padding 10px 16px
- **Button (Secondary):** background `surface`, border `#E6E9EE`, text `textPrimary`
- **Input:** background `surface`, border `#E6E9EE`, focus outline `accent` 2px, radius 6px
- **Table rows:** subtle alternating backgrounds using `surface` and a slightly-tinted variant, with 12px cell padding


### Accessibility

- Aim for contrast ratio >= 4.5:1 for body text and >= 3:1 for large text.
- Always provide visible focus outlines for keyboard navigation (use `accent` with 2px thickness).
- Use semantic components where possible (buttons, labels, form fields) and include ARIA attributes for dynamic regions.


### Theme Example (Java)

Below is a small `Theme` helper you can adapt into the `utils` package to centralize the simplified color tokens for Swing/JavaFX.

```java
package utils;

import java.awt.Color;

public final class Theme {
	// Custom accent chosen from the color wheel (violet-indigo)
	public static final Color ACCENT = Color.decode("#7C4DFF");
	public static final Color ACCENT_HOVER = Color.decode("#5E2BFF");
	public static final Color BACKGROUND = Color.decode("#F5F7FA");
	public static final Color SURFACE = Color.decode("#FFFFFF");
	public static final Color TEXT_PRIMARY = Color.decode("#0F1724");
	public static final Color TEXT_SECONDARY = Color.decode("#6B7280");

	private Theme() {}
}
```

### Implementation notes

- Add the `Theme` class to `src/utils` and import tokens into GUI builders and components.
- Create a short `UI_GUIDELINES.md` or a `design/` folder if the team needs expanded assets (icons, sample screens, Figma links).

## Runtime Data Storage

- ArrayList<Vehicle>
- ArrayList<Customer>
- ArrayList<Rental>

No database is used.

---

## Application Flow

Main.java
→ Login/Register
→ Dashboard
→ Select Module
→ Perform Operations

---
