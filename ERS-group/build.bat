@echo off
REM Build and Run Script for ERS System

echo Cleaning previous build...
if exist "bin" rmdir /s /q bin
mkdir bin

echo.
echo Compiling Java files...
javac -encoding UTF-8 -d bin src/ers/group/*.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo.
echo Running StudentCourseTab...
java -cp bin ers.group.StudentCourseTab

pause
