param(
    [string]$ImageName = "notebookum-persistence-java",
    [string]$Tag = "v1.0.0",
    [switch]$SkipTests
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Write-Info { param([string]$Msg) Write-Host "[INFO]  $Msg" -ForegroundColor Cyan }
function Write-Ok   { param([string]$Msg) Write-Host "[OK]    $Msg" -ForegroundColor Green }
function Write-Err  { param([string]$Msg) Write-Host "[ERROR] $Msg" -ForegroundColor Red }

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Err "Docker no está disponible en el PATH."
    exit 1
}

if (-not (Test-Path ".\mvnw.cmd")) {
    Write-Err "No se encontró el wrapper de Maven (.\mvnw.cmd)."
    exit 1
}

# FIX: lógica de SkipTests estaba invertida
$mavenArgs = @("package")
if ($SkipTests) {
    $mavenArgs = @("-DskipTests", "package")
}

Write-Info "Compilando el proyecto con Maven..."
& .\mvnw.cmd @mavenArgs
if ($LASTEXITCODE -ne 0) {
    Write-Err "La compilación falló. No se construirá la imagen."
    exit $LASTEXITCODE
}

$fullImageName = "$ImageName`:$Tag"
Write-Info "Construyendo la imagen Docker $fullImageName..."
docker build -t $fullImageName .
if ($LASTEXITCODE -ne 0) {
    Write-Err "Docker build falló."
    exit $LASTEXITCODE
}

Write-Ok "Imagen construida correctamente: $fullImageName"