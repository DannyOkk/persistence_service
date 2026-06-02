# Ejecuta la aplicación Spring Boot con variables de entorno para la conexión PostgreSQL.
#
# Variables de entorno requeridas:
#   DB_URL        URL JDBC de PostgreSQL  (ej: jdbc:postgresql://localhost:5432/mydb)
#   DB_USERNAME   Usuario de la base de datos
#   DB_PASSWORD   Contraseña de la base de datos
#
# Variables de entorno opcionales (tienen valores por defecto):
#   DB_DRIVER         Driver JDBC        (default: org.postgresql.Driver)
#   DB_DIALECT        Dialecto Hibernate (default: org.hibernate.dialect.PostgreSQLDialect)
#   DDL_AUTO          Estrategia DDL     (default: create-drop)
#   SHOW_SQL          Mostrar SQL        (default: true)
#   FORMAT_SQL        Formatear SQL      (default: false)
#   DB_POOL_MAX_SIZE  Máx conexiones HikariCP (default: 10)
#   DB_POOL_MIN_IDLE  Mín conexiones HikariCP (default: 5)
#
# Ejemplo de uso:
#   $env:DB_URL      = "jdbc:postgresql://localhost:5432/mydb"
#   $env:DB_USERNAME = "postgres"
#   $env:DB_PASSWORD = "secret"
#   .\run.ps1

param()

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# ── Helpers ────────────────────────────────────────────────────────────────────
function Write-Info  { param([string]$Msg) Write-Host "[INFO]  $Msg" -ForegroundColor Cyan }
function Write-Ok    { param([string]$Msg) Write-Host "[OK]    $Msg" -ForegroundColor Green }
function Write-Warn  { param([string]$Msg) Write-Host "[WARN]  $Msg" -ForegroundColor Yellow }
function Write-Err   { param([string]$Msg) Write-Host "[ERROR] $Msg" -ForegroundColor Red }

# ── Valores por defecto ────────────────────────────────────────────────────────
if (-not $env:DB_DRIVER)        { $env:DB_DRIVER        = "org.postgresql.Driver" }
if (-not $env:DB_DIALECT)       { $env:DB_DIALECT       = "org.hibernate.dialect.PostgreSQLDialect" }
if (-not $env:DDL_AUTO)         { $env:DDL_AUTO         = "create-drop" }
if (-not $env:SHOW_SQL)         { $env:SHOW_SQL         = "true" }
if (-not $env:FORMAT_SQL)       { $env:FORMAT_SQL       = "false" }
if (-not $env:DB_POOL_MAX_SIZE) { $env:DB_POOL_MAX_SIZE = "10" }
if (-not $env:DB_POOL_MIN_IDLE) { $env:DB_POOL_MIN_IDLE = "5" }

# ── Validación de variables requeridas ────────────────────────────────────────
$missing = $false

if (-not $env:DB_URL) {
    Write-Err "DB_URL no está definida. Ejemplo: jdbc:postgresql://localhost:5432/mydb"
    $missing = $true
}
if (-not $env:DB_USERNAME) {
    Write-Err "DB_USERNAME no está definida."
    $missing = $true
}
if (-not $env:DB_PASSWORD) {
    Write-Err "DB_PASSWORD no está definida."
    $missing = $true
}

if ($missing) {
    Write-Host ""
    Write-Err "Falta al menos una variable de entorno requerida. Abortando."
    Write-Host ""
    Write-Host "  Uso:"
    Write-Host '    $env:DB_URL      = "jdbc:postgresql://localhost:5432/mydb"'
    Write-Host '    $env:DB_USERNAME = "postgres"'
    Write-Host '    $env:DB_PASSWORD = "secret"'
    Write-Host '    .\run.ps1'
    exit 1
}

# ── Verificar que Maven esté disponible ───────────────────────────────────────
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Err "Maven (mvn) no encontrado en el PATH."
    Write-Err "Instálalo o asegúrate de que JAVA_HOME y M2_HOME estén configurados."
    exit 1
}

# ── Verificar Java 21+ ────────────────────────────────────────────────────────
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Err "Java no encontrado en el PATH."
    exit 1
}

$javaVerLine = (java -version 2>&1)[0].ToString()
if ($javaVerLine -match '"(\d+)') {
    $javaMajor = [int]$Matches[1]
    if ($javaMajor -lt 21) {
        Write-Warn "Java $javaMajor detectado. Este proyecto requiere Java 21+."
    }
}

# ── Resumen de configuración ──────────────────────────────────────────────────
$maskedPass = "*" * $env:DB_PASSWORD.Length

Write-Host ""
Write-Info "=== Configuración de arranque ==="
Write-Info "  DB_URL          : $env:DB_URL"
Write-Info "  DB_USERNAME     : $env:DB_USERNAME"
Write-Info "  DB_PASSWORD     : $maskedPass"
Write-Info "  DB_DRIVER       : $env:DB_DRIVER"
Write-Info "  DB_DIALECT      : $env:DB_DIALECT"
Write-Info "  DDL_AUTO        : $env:DDL_AUTO"
Write-Info "  SHOW_SQL        : $env:SHOW_SQL"
Write-Info "  FORMAT_SQL      : $env:FORMAT_SQL"
Write-Info "  POOL MAX/MIN    : $env:DB_POOL_MAX_SIZE / $env:DB_POOL_MIN_IDLE"
Write-Host ""

# ── Directorio del proyecto ───────────────────────────────────────────────────
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

# ── Arrancar aplicación ───────────────────────────────────────────────────────
Write-Ok "Iniciando Spring Boot (mvn spring-boot:run)..."
Write-Host ""

mvn spring-boot:run