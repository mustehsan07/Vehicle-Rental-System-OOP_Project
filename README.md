# Vehicle-Rental-System-OOP_Project

## Grop Members Names
- Muhammad Mustehsan Sajjad
- Abdullah Azhar
- Hashir Ali
- Muhammad Zain 
- Sumeed Ahmed

## Run Instructions

The application starts from `app.MainClass`.

### Compile

From the project root, run:

New-Item -ItemType Directory -Force out | Out-Null
javac -d out -sourcepath src src\app\MainClass.java src\auth\*.java src\customer\*.java src\data\*.java src\model\*.java src\admin\*.javaa src\vehicle_display\*.java
java -cp out app.MainClass


If you prefer to compile everything first, you can also use:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out -sourcepath src @(Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })
java -cp out app.MainClass
```