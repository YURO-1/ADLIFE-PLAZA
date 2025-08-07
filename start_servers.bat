@echo off
echo Starting ADLIFE PLAZA Medical System...
echo.

echo Starting Login Server...
start "Login Server" cmd /k "cd /d %~dp0 && java -cp "app\build\classes\java\main" org.example.Server.LoginServer"

echo Starting Receptionist Server...
start "Receptionist Server" cmd /k "cd /d %~dp0 && java -cp "app\build\classes\java\main" org.example.Server.ReceptionistServer"

echo Starting Doctor Server...
start "Doctor Server" cmd /k "cd /d %~dp0 && java -cp "app\build\classes\java\main" org.example.Server.DoctorServer"

echo.
echo Waiting 3 seconds for servers to start...
timeout /t 3 /nobreak > nul

echo Starting ADLIFE PLAZA Application...
start "ADLIFE PLAZA" cmd /k "cd /d %~dp0 && java -cp "app\build\classes\java\main" --add-modules javafx.controls,javafx.fxml org.example.App"

echo.
echo All servers and application started!
echo.
echo Test Credentials:
echo Receptionist: #rcpMary / rcpPass123
echo Doctor: #dctAdrian / dctPass456
echo.
pause
