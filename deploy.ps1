# Rebuilds the project and redeploys it to a local external Tomcat.
# Requires CATALINA_HOME environment variable (see README).

$tomcat = $env:CATALINA_HOME
if (-not $tomcat -or -not (Test-Path "$tomcat\bin\startup.bat")) {
    Write-Host "CATALINA_HOME is not set or does not point to a Tomcat folder."
    Write-Host "Tip: To run Rooted without installing Tomcat, just run .\run.bat or .\run.ps1 instead!"
    exit 1
}

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn clean package
} else {
    .\mvnw.cmd clean package
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed - not deploying."
    exit 1
}

try {
    & "$tomcat\bin\shutdown.bat" 2>$null
} catch {
}
Start-Sleep -Seconds 5

Remove-Item -Recurse -Force "$tomcat\webapps\rooted" -ErrorAction SilentlyContinue
Remove-Item -Force "$tomcat\webapps\rooted.war" -ErrorAction SilentlyContinue
Copy-Item .\target\rooted.war "$tomcat\webapps\"

& "$tomcat\bin\startup.bat"
Write-Host "Deployed. Open http://localhost:8080/rooted/"
