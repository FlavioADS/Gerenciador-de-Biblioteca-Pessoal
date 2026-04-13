@ECHO OFF
SETLOCAL

SET "DIR=%~dp0"
SET "WRAPPER_JAR=%DIR%.mvn\wrapper\maven-wrapper.jar"
SET "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
SET "MAVEN_PROJECTBASEDIR=%DIR:~0,-1%"

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO [ERROR] Maven Wrapper JAR nao encontrado em %WRAPPER_JAR%
  ECHO [ERROR] Copie o arquivo maven-wrapper.jar para .mvn\wrapper\maven-wrapper.jar
  EXIT /B 1
)

IF EXIST "%JAVA_CMD%" (
  "%JAVA_CMD%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
) ELSE (
  WHERE java >NUL 2>&1
  IF ERRORLEVEL 1 (
    ECHO [ERROR] Java nao encontrado. Configure JAVA_HOME ou adicione java ao PATH.
    EXIT /B 1
  )
  java -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
)