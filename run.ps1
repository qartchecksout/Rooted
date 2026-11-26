# One-command launcher for Rooted (no separate Tomcat or MySQL installation required).
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "Starting Rooted via Embedded Tomcat..." -ForegroundColor Green
& .\mvnw.cmd compile exec:java
