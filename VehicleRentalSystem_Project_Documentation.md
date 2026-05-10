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

---

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

## Future Improvements

- Add database integration
- Add payment gateway
- Add online booking
- Generate invoices
