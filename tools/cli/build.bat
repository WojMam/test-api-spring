@echo off
setlocal

rem Build the CLI tool
echo Building Test API CLI tool...
cd /d "%~dp0"
call mvn clean package

rem Check if the build was successful
if %ERRORLEVEL% equ 0 (
    echo Build successful!
    
    rem Create a path to the JAR file
    set CLI_JAR=%CD%\target\test-api-cli-0.1.0-jar-with-dependencies.jar
    
    echo.
    echo To run the CLI tool, use:
    echo java -jar "%CLI_JAR%"
    echo.
    
    rem Create a temporary batch file for easier usage
    echo @echo off > apicli.bat
    echo java -jar "%CLI_JAR%" %%* >> apicli.bat
    
    echo For convenience, a temporary batch file 'apicli.bat' has been created in this directory.
    echo You can run it as: apicli [command] [options]
    echo.
    echo Run 'apicli --help' for usage information.
) else (
    echo Build failed. Please check the error messages above.
)

endlocal 