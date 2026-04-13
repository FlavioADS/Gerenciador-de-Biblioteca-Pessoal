@echo off
setlocal
set "SCRIPT_DIR=%~dp0"
set "MVN_CMD=%SCRIPT_DIR%mvnw.cmd"
if not exist "%MVN_CMD%" set "MVN_CMD=mvn"
echo ===================================
echo Biblioteca Pessoal - Build Script
echo ===================================
echo.

rem Detecta Java 17 e Maven automaticamente
call :detectar_java_maven
if %ERRORLEVEL% NEQ 0 goto :end

if "%1" == "" goto menu
if "%1" == "compile" goto compile
if "%1" == "test" goto test
if "%1" == "coverage" goto coverage
if "%1" == "run" goto run
if "%1" == "clean" goto clean
if "%1" == "package" goto package
goto menu

:menu
echo Opcoes disponiveis:
echo 1. compile  - Compilar o projeto
echo 2. test     - Executar testes
echo 3. coverage - Gerar relatorio de cobertura
echo 4. run      - Iniciar aplicacao
echo 5. clean    - Limpar arquivos gerados
echo 6. package  - Gerar JAR executavel
echo.
set /p choice="Escolha uma opcao (1-6): "

if "%choice%"=="1" goto compile
if "%choice%"=="2" goto test
if "%choice%"=="3" goto coverage
if "%choice%"=="4" goto run
if "%choice%"=="5" goto clean
if "%choice%"=="6" goto package
goto menu

:compile
echo [INFO] Compilando o projeto...
call "%MVN_CMD%" clean compile -q
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilacao concluida com sucesso!
) else (
    echo [ERROR] Falha na compilacao!
)
goto end

:test
echo [INFO] Executando testes...
call "%MVN_CMD%" test
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Todos os testes passaram!
) else (
    echo [ERROR] Alguns testes falharam!
)
goto end

:coverage
echo [INFO] Gerando relatorio de cobertura...
call "%MVN_CMD%" test jacoco:report -q
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Relatorio gerado em: target/site/jacoco/index.html
    start target/site/jacoco/index.html
) else (
    echo [ERROR] Falha ao gerar relatorio!
)
goto end

:run
echo [INFO] Iniciando aplicacao...
echo [INFO] Acesse: http://localhost:8080
echo [INFO] Pressione Ctrl+C para parar
echo.
call "%MVN_CMD%" spring-boot:run
goto end

:clean
echo [INFO] Limpando arquivos gerados...
call "%MVN_CMD%" clean -q
if exist data rmdir /s /q data
echo [SUCCESS] Limpeza concluida!
goto end

:package
echo [INFO] Gerando JAR executavel...
call "%MVN_CMD%" clean package -Dmaven.test.skip=true -q
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] JAR gerado em: target/gerenciador-biblioteca-pessoal-1.0.0.jar
    echo [INFO] Para executar: java -jar target/gerenciador-biblioteca-pessoal-1.0.0.jar
) else (
    echo [ERROR] Falha ao gerar JAR!
)
goto end

:detectar_java_maven
rem Lógica de detecção omitida para brevidade, conforme o vídeo.
goto :eof

:end
pause
