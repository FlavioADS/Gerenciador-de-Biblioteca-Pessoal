@echo off
rem Atalho para iniciar a aplicacao pelo JAR
rem Para mais opcoes, use: build.bat

echo ===================================
echo Biblioteca Pessoal - Iniciando...
echo ===================================
echo.

rem Detecta Java automaticamente
set "JAVA_CMD="

if defined JAVA_HOME set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"

if not defined JAVA_CMD for /d %%D in ("C:\Program Files\Java\jdk-17*") do set "JAVA_CMD=%%D\bin\java.exe"
if not defined JAVA_CMD for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set "JAVA_CMD=%%D\bin\java.exe"

if not defined JAVA_CMD (
    echo [ERROR] Java 17 nao encontrado!
    pause
    exit /b 1
)

if not exist "target\biblioteca-pessoal-1.0.0.jar" (
    echo [ERROR] JAR nao encontrado! Execute build.bat e escolha opcao 6 primeiro.
    pause
    exit /b 1
)

echo [OK] Java encontrado.
echo [INFO] Acesse: http://localhost:8080
echo [INFO] Pressione Ctrl+C para parar
echo.

"%JAVA_CMD%" -jar target\biblioteca-pessoal-1.0.0.jar
pause
