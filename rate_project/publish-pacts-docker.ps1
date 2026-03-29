$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$pacts = Join-Path $root "rate_printer\target\pacts"
if (-not (Test-Path $pacts)) {
    Write-Host "No pacts folder. Run: mvn -f rate_printer\pom.xml test"
    exit 1
}
docker run --rm `
    -v "${pacts}:/pacts" `
    pactfoundation/pact-cli:latest `
    publish /pacts `
    --consumer-app-version "0.0.1-SNAPSHOT" `
    --branch "main" `
    --broker-base-url "http://host.docker.internal:9292"
