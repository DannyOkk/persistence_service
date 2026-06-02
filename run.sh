#!/usr/bin/env bash
# Ejecuta la aplicación Spring Boot con variables de entorno para la conexión PostgreSQL.
# Uso: ./run.sh [opciones]
#
# Variables de entorno requeridas:
#   DB_NAME       Nombre de la base de datos  (ej: notebookum_db)
#   DB_USER       Usuario de la base de datos
#   DB_PASSWORD   Contraseña de la base de datos
#
# Variables de entorno opcionales (tienen valores por defecto):
#   DB_WRITE_HOST Host de escritura PostgreSQL  (default: localhost)
#   DB_PORT       Puerto PostgreSQL              (default: 5432)
#   DB_DRIVER     Driver JDBC                   (default: org.postgresql.Driver)
#   DB_DIALECT    Dialecto Hibernate            (default: org.hibernate.dialect.PostgreSQLDialect)
#   DDL_AUTO      Estrategia DDL                (default: create-drop)
#   SHOW_SQL      Mostrar SQL                   (default: true)
#   FORMAT_SQL    Formatear SQL                 (default: false)
#   DB_POOL_MAX_SIZE  Máx conexiones HikariCP   (default: 10)
#   DB_POOL_MIN_IDLE  Mín conexiones HikariCP   (default: 5)
#   REDIS_HOST    Host de Redis                 (default: localhost)
#   REDIS_PORT    Puerto de Redis               (default: 6379)
#
# Ejemplo de uso rápido:
#   DB_NAME="notebookum_db" \
#   DB_USER="notebookum_user" \
#   DB_PASSWORD="secret" \
#   ./run.sh

set -euo pipefail

# ── Colores ────────────────────────────────────────────────────────────────────
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
RESET='\033[0m'

# ── Helpers ────────────────────────────────────────────────────────────────────
info()  { echo -e "${CYAN}[INFO]${RESET}  $*"; }
ok()    { echo -e "${GREEN}[OK]${RESET}    $*"; }
warn()  { echo -e "${YELLOW}[WARN]${RESET}  $*"; }
error() { echo -e "${RED}[ERROR]${RESET} $*" >&2; }

# ── Valores por defecto ────────────────────────────────────────────────────────
export DB_WRITE_HOST="${DB_WRITE_HOST:-localhost}"
export DB_PORT="${DB_PORT:-5432}"
export DB_DRIVER="${DB_DRIVER:-org.postgresql.Driver}"
export DB_DIALECT="${DB_DIALECT:-org.hibernate.dialect.PostgreSQLDialect}"
export DDL_AUTO="${DDL_AUTO:-create-drop}"
export SHOW_SQL="${SHOW_SQL:-true}"
export FORMAT_SQL="${FORMAT_SQL:-false}"
export DB_POOL_MAX_SIZE="${DB_POOL_MAX_SIZE:-10}"
export DB_POOL_MIN_IDLE="${DB_POOL_MIN_IDLE:-5}"
export REDIS_HOST="${REDIS_HOST:-localhost}"
export REDIS_PORT="${REDIS_PORT:-6379}"

# ── Validación de variables requeridas ────────────────────────────────────────
MISSING=0

if [[ -z "${DB_NAME:-}" ]]; then
  error "DB_NAME no está definida. Ejemplo: notebookum_db"
  MISSING=1
fi

if [[ -z "${DB_USER:-}" ]]; then
  error "DB_USER no está definida."
  MISSING=1
fi

if [[ -z "${DB_PASSWORD:-}" ]]; then
  error "DB_PASSWORD no está definida."
  MISSING=1
fi

if [[ $MISSING -eq 1 ]]; then
  echo ""
  error "Falta al menos una variable de entorno requerida. Abortando."
  echo ""
  echo "  Uso:"
  echo "    DB_NAME=\"notebookum_db\" \\"
  echo "    DB_USER=\"notebookum_user\" \\"
  echo "    DB_PASSWORD=\"secret\" \\"
  echo "    ./run.sh"
  exit 1
fi

# ── Verificar que Maven esté disponible ───────────────────────────────────────
if ! command -v mvn &>/dev/null; then
  error "Maven (mvn) no encontrado en el PATH."
  error "Instálalo o asegúrate de que JAVA_HOME y M2_HOME estén configurados."
  exit 1
fi

# ── Verificar que Java 21+ esté disponible ────────────────────────────────────
if ! command -v java &>/dev/null; then
  error "Java no encontrado en el PATH."
  exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d. -f1)
if [[ "$JAVA_VERSION" -lt 21 ]]; then
  warn "Java $JAVA_VERSION detectado. Este proyecto requiere Java 21+."
fi

# ── Resumen de configuración ──────────────────────────────────────────────────
echo ""
info "=== Configuración de arranque ==="
info "  DB_WRITE_HOST   : ${DB_WRITE_HOST}"
info "  DB_PORT         : ${DB_PORT}"
info "  DB_NAME         : ${DB_NAME}"
info "  DB_USER         : ${DB_USER}"
info "  DB_PASSWORD     : $(echo "${DB_PASSWORD}" | sed 's/./*/g')"
info "  DB_DRIVER       : ${DB_DRIVER}"
info "  DB_DIALECT      : ${DB_DIALECT}"
info "  DDL_AUTO        : ${DDL_AUTO}"
info "  SHOW_SQL        : ${SHOW_SQL}"
info "  FORMAT_SQL      : ${FORMAT_SQL}"
info "  POOL MAX/MIN    : ${DB_POOL_MAX_SIZE} / ${DB_POOL_MIN_IDLE}"
info "  REDIS_HOST      : ${REDIS_HOST}"
info "  REDIS_PORT      : ${REDIS_PORT}"
echo ""

# ── Directorio del proyecto ───────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# ── Arrancar aplicación ───────────────────────────────────────────────────────
ok "Iniciando Spring Boot (mvn spring-boot:run)..."
echo ""

mvn spring-boot:run