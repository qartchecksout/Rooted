# Code mein har badlaav ke baad, this rebuilds the project and redeploys it to local Tomcat
$tomcat = "C:\Tools\apache-tomcat-10.1.60"

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