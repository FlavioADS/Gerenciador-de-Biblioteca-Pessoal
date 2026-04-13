@echo off
rem Atalho para iniciar a aplicacao pelo JAR
rem Para mais opcoes, use: build.bat

setlocal
set "SCRIPT_DIR=%~dp0"
set "JAR_NAME=target\gerenciador-biblioteca-pessoal-1.0.0.jar"

echo ===================================
echo Biblioteca Pessoal - Iniciando...
echo ===================================
echo.

rem Detecta Java automaticamente
set "JAVA_CMD="

if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"

if not defined JAVA_CMD for /f "delims=" %%I in ('where java 2^>nul') do (
    if not defined JAVA_CMD set "JAVA_CMD=%%I"
)

if not defined JAVA_CMD (
    echo [ERROR] Java 17 nao encontrado!
    pause
    exit /b 1
)

if not exist "%JAR_NAME%" (
    echo [INFO] JAR nao encontrado. Gerando agora...
    call "%SCRIPT_DIR%build.bat" package
    if errorlevel 1 (
        echo [ERROR] Nao foi possivel gerar o JAR!
        pause
        exit /b 1
    )
)

echo [OK] Java encontrado.
echo [INFO] Acesse: http://localhost:8080
echo [INFO] Pressione Ctrl+C para parar
echo.

if not defined BIBLIOTECA_DB_PASSWORD set "BIBLIOTECA_DB_PASSWORD=biblioteca123"
"%JAVA_CMD%" -jar "%JAR_NAME%"
pause
