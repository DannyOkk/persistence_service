# Microservicio de Persistencia (Java)

Este repositorio contiene la implementación en **Java (Spring Boot)** del microservicio de persistencia del proyecto NotebookUm. Su propósito principal es gestionar de forma centralizada el acceso seguro y resiliente a la base de datos para entidades clave (usuarios, documentos históricos y resúmenes).

## 🚀 Tecnologías Principales

- **Lenguaje:** Java 21 (Eclipse Temurin)
- **Framework:** Spring Boot 3 (Web, Data JPA, Validation)
- **Base de Datos:** PostgreSQL
- **Gestión de dependencias:** Maven
- **Infraestructura:** Docker, Docker Compose, Traefik

## 🛡️ Arquitectura y Seguridad

Este microservicio ha sido blindado mediante múltiples capas de seguridad y resiliencia:

1. **Defensa contra Mass Assignment:** Toda comunicación a través de la API utiliza **DTOs (Data Transfer Objects)** validados de forma estricta. Esto evita que los clientes sobrescriban metadatos protegidos o IDs de bases de datos de forma maliciosa.
2. **Inyección SQL mitigada:** Uso de *Spring Data JPA*, que utiliza *Prepared Statements* por defecto para interactuar con la Base de Datos.
3. **Contenedores Seguros (Rootless):** Se emplea un *Multi-stage Build* en Docker. El contenedor final ejecuta la aplicación bajo un usuario del sistema sin privilegios (`appuser`).
4. **Resiliencia de Red:** A través de etiquetas de Traefik en `docker-compose.yml`, se implementan de forma nativa patrones **Circuit Breaker** (para limitar llamadas a bases de datos colapsadas) y **Bulkhead** (para limitar peticiones concurrentes masivas).

## 🌐 API Endpoints

Las rutas proveen una interfaz estandarizada RESTful (CRUD) bajo el prefijo `/api/v1/db/`:

- **Usuarios:** `/api/v1/db/users` (GET, POST)
- **Documentos:** `/api/v1/db/documents` (GET, POST, GET `/{id}`, PATCH `/{id}`, DELETE `/{id}`)
- **Resúmenes:** `/api/v1/db/summaries` (GET, POST, GET `/{id}`, PATCH `/{id}`, DELETE `/{id}`)

## 🛠️ Ejecución Local (Desarrollo)

Para compilar y ejecutar el proyecto localmente sin Docker (requiere Java 21):

```powershell
# Compilar el proyecto
.\mvnw clean compile

# Construir el ejecutable (app.jar)
.\mvnw clean package -DskipTests

# Ejecutar localmente
.\mvnw spring-boot:run
```

## 🐳 Despliegue con Docker

### Utilizando scripts locales
Puedes construir la imagen localmente utilizando los scripts incluidos:

```powershell
.\build-image.ps1
# Opcional con parámetros: .\build-image.ps1 -ImageName persistence-java -Tag latest
```

### Utilizando Docker Compose (Arquitectura de producción)
El archivo `docker-compose.yml` configura 2 réplicas de la aplicación (Load Balancer Pattern) conectadas a las redes de Traefik y la base de datos interna.

```powershell
docker compose up -d
```
