@echo off
setlocal
echo Close ALL RuneLite windows, including RuneLite (configure), first.
echo This removes the development login file and disables saving it again.
echo It does not close programs or end your Jagex account sessions.
echo.
pause
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\cleanup-jagex-login.ps1"
set "cleanupResult=%ERRORLEVEL%"
echo.
pause
exit /b %cleanupResult%
