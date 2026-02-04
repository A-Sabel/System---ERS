# Build and Run Script for ERS System (PowerShell)

Write-Host "Cleaning previous build..." -ForegroundColor Yellow
if (Test-Path "bin") {
    Remove-Item -Recurse -Force "bin"
}
New-Item -ItemType Directory -Force -Path "bin" | Out-Null

Write-Host "`nCompiling Java files..." -ForegroundColor Cyan
javac -encoding UTF-8 -d bin src/ers/group/*.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nCompilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host "`nCompilation successful!" -ForegroundColor Green
Write-Host "`nRunning StudentCourseTab..." -ForegroundColor Cyan
java -cp bin ers.group.StudentCourseTab
