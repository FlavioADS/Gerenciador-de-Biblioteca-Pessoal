@echo off
rem Atalho para iniciar a aplicacao pelo JAR
rem Para mais opcoes, use: build.bat

echo ============================================
echo  Biblioteca Pessoal - Iniciando...
echo ============================================
echo.

rem Detecta Java 17 automaticamente
set "JAVA_CMD="
for /d %%D in ("C:\Users\%USERNAME%\URA\Java\java-17*") do set "JAVA_CMD=%%D\bin\java.exe"
if not defined JAVA_CMD for /d %%D in ("C:\Program Files\Java\jdk-17*") do set "JAVA_CMD=%%D\bin\java.exe"
if not defined JAVA_CMD for /d %%D in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set "JAVA_CMD=%%D\bin\java.exe"
if not defined JAVA_CMD where java >nul 2>nul && set "JAVA_CMD=java"

if not defined JAVA_CMD (
    echo [ERRO] Java 17 nao encontrado!
    pause
    exit /b 1
)

if not exist "target\biblioteca-pessoal-1.0.0.jar" (
    echo [ERRO] JAR nao encontrado! Execute build.bat e escolha opcao 6 primeiro.
    pause
    exit /b 1
)

echo [OK] Java: %JAVA_CMD%
echo [INFO] Acesse: http://localhost:8080
echo [INFO] Pressione Ctrl+C para parar
echo.

"%JAVA_CMD%" -jar target\biblioteca-pessoal-1.0.0.jar
pause