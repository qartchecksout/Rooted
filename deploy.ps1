# Rebuilds the project and redeploys it to a local Tomcat.
# Requires the CATALINA_HOME environment variable (see README).

$tomcat = $env:CATALINA_HOME
if (-not $tomcat -or -not (Test-Path "$tomcat\bin\startup.bat")) {
    Write-Host "CATALINA_HOME is not set or does not point to a Tomcat folder."
    Write-Host "See the 'One-time setup' section of README.md."
    exit 1
}

mvn clean package
if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed - not deploying."
    exit 1
}

& "$tomcat\bin\shutdown.bat"
Start-Sleep -Seconds 8

Remove-Item -Recurse -Force "$tomcat\webapps\rooted" -ErrorAction SilentlyContinue
Remove-Item -Force "$tomcat\webapps\rooted.war" -ErrorAction SilentlyContinue
Copy-Item .\target\rooted.war "$tomcat\webapps\"

& "$tomcat\bin\startup.bat"
Write-Host "Deployed. Open http://localhost:8080/rooted/"
