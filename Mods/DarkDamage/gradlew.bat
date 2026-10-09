@echo off
set DIR=%~dp0
set APP_BASE_NAME=%~n0
set APP_HOME=%DIR%
set DEFAULT_JVM_OPTS=
set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

java %DEFAULT_JVM_OPTS% -cp %CLASSPATH% org.gradle.wrapper.GradleWrapperMain %*
