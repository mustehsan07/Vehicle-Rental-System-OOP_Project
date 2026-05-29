# Vehicle-Rental-System-OOP_Project

## Group Members Names
- Muhammad Mustehsan Sajjad
- Abdullah Azhar
- Hashir Ali
- Muhammad Zain 
- Sumeed Ahmed

## Run Instructions

The application starts from `app.MainClass`.

### Prepare
- Create output and library folders (PowerShell):

New-Item -ItemType Directory -Force out | Out-Null
New-Item -ItemType Directory -Force lib | Out-Null

- Download required mail jars into `lib/` (Jakarta Mail):

### run in powershell to load env variables

Get-Content .env | ForEach-Object {
    $line = $_.Trim()
    if (-not [string]::IsNullOrWhiteSpace($line) -and -not $line.StartsWith('#')) {
        $parts = $line -split '=', 2
        if ($parts.Length -eq 2) {
            $name  = $parts[0].Trim()
            $value = $parts[1].Trim().Trim('"').Trim("'")
            # use Set-Item to avoid the ${env:...} dynamic-name issue
            Set-Item -Path "Env:$name" -Value $value
        }
    }
}

### Compile & Run (Windows PowerShell)
Compile including jars on the classpath:

javac -d out -sourcepath src -cp "lib/*" $(Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName })
java -cp "out;lib/*" app.MainClass


### Compile & Run (Linux / macOS)

```bash
mkdir -p out lib
javac -d out -sourcepath src -cp "lib/*" $(find src -name "*.java")
java -cp "out:lib/*" app.MainClass
```

Notes:
- Place required third-party jars into the `lib/` folder and keep `lib/` out of version control (see `.gitignore`).
- If you prefer a build tool (Maven/Gradle), add the Jakarta Mail / Activation dependencies instead of managing jars manually.