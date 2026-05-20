# Vehicle-Rental-System-OOP_Project

## Group Members Names
- Muhammad Mustehsan Sajjad
- Abdullah Azhar
- Hashir Ali
- Muhammad Zain 
- Sumeed Ahmed

## Run Instructions

The application starts from `app.MainClass`.

### Compile

From the project root, run the following comands:

New-Item -ItemType Directory -Force out | Out-Null
javac -d out -sourcepath src src\app\MainClass.java src\auth\*.java src\customer\*.java src\data\*.java src\model\*.java src\admin\*.java src\vehicle_display\*.java src\rental\*.java src\rental_history\*.java
java -cp out app.MainClass