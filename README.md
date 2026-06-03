# ⚔️ Legado de Valientes — Backend

> Proyecto final de semestre · Ingeniería de Software · 4° Semestre

**Legado de Valientes** es una aplicación web de tipo RPG por turnos en la que los jugadores pueden registrarse, adquirir mascotas con distintas rarezas y habilidades, entrenarlas y enfrentarlas en combates contra rivales. El objetivo del proyecto es aplicar los principios de diseño de software vistos durante el semestre (arquitectura en capas, separación de responsabilidades, API REST) dentro de un contexto entretenido y funcional.

---

## 🏗️ Arquitectura del Proyecto

El backend sigue una **arquitectura en capas** (Layered Architecture), donde cada capa tiene una responsabilidad única y bien definida. Las capas se comunican de forma unidireccional (de arriba hacia abajo) y el frontend se desacopla completamente del backend a través de una **API REST**.

```
Cliente (React) ──HTTP/JSON──▶ [Presentation Layer]
                                      │
                               [Application Layer]
                                      │
                                [Domain Layer]
                                      │
                            [Infrastructure Layer]
                                      │
                               MongoDB Atlas
```

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **Presentation** | `presentation/controller` | Recibe las peticiones HTTP, valida la entrada y delega al servicio correspondiente |
| **Application** | `application/service` | Contiene la lógica de negocio: reglas de juego, entrenamiento, combates y logros |
| **Domain** | `domain/model` | Define las entidades del dominio (Jugador, Mascota, Combate, Logro) e interfaces de comportamiento |
| **Infrastructure** | `infrastructure/repository` | Implementa el acceso a datos mediante Spring Data MongoDB |

> Esta separación garantiza que un cambio en la base de datos, por ejemplo, **no afecte** la lógica de negocio ni los controladores.

---

## 🛠️ Tecnologías Utilizadas

**Backend**
- ☕ Java 21
- 🍃 Spring Boot 4.0.6 (Web MVC, Data MongoDB, Validation)
- 📦 Maven

**Base de datos**
- 🍃 MongoDB Atlas (base de datos en la nube)

**Frontend** *(repositorio separado)*
- ⚛️ React + Vite

**Despliegue**
- 🚀 Render — servidor del backend (contenedor Docker)
- 🌐 Vercel — hosting del frontend
- 🐳 Docker — empaquetado del backend para producción

---

## 📁 Estructura del Backend

```
src/main/java/com/legado/backend/
│
├── presentation/
│   └── controller/
│       ├── JugadorController.java      ← endpoints de jugadores
│       ├── MascotaController.java      ← endpoints de mascotas
│       ├── CombateController.java      ← endpoints de combates
│       └── LogroController.java        ← endpoints de logros
│
├── application/
│   ├── service/
│   │   ├── JugadorService.java
│   │   ├── MascotaService.java
│   │   ├── CombateService.java
│   │   └── AchievementService.java
│   └── dto/
│       ├── request/                    ← objetos de entrada (lo que llega del cliente)
│       └── response/                   ← objetos de salida (lo que se retorna al cliente)
│
├── domain/
│   ├── model/
│   │   ├── Jugador.java
│   │   ├── Mascota.java  ◀─ clase base
│   │   ├── Dragon.java   ◀─┐
│   │   ├── Leon.java     ◀─┤ subclases con comportamiento propio
│   │   ├── Aguila.java   ◀─┤
│   │   └── Gorila.java   ◀─┘
│   └── service/
│       ├── Combatiente.java            ← interfaz de comportamiento en combate
│       ├── Entrenable.java             ← interfaz de entrenamiento
│       └── Identificable.java
│
└── infrastructure/
    ├── repository/
    │   ├── JugadorRepository.java
    │   ├── MascotaRepository.java
    │   ├── CombateRepository.java
    │   └── LogroRepository.java
    └── type/
        ├── Rareza.java
        ├── Dificultad.java
        ├── EstadoCombate.java
        └── TipoEntrenamiento.java
```

---

## ▶️ Cómo Ejecutarlo Localmente

### Requisitos previos
- Java 21 instalado
- Maven instalado (`mvn -v` para verificar)
- Una cadena de conexión a MongoDB Atlas (o instancia local)

### Pasos

**1. Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd Legado_backend
```

**2. Configurar la variable de entorno de MongoDB**
```bash
# En Linux/Mac
export MONGODB_URI="mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/"

# En Windows (PowerShell)
$env:MONGODB_URI = "mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/"
```

**3. Compilar y ejecutar**
```bash
mvn spring-boot:run
```

**4. Verificar que el servidor está activo**

El servidor estará disponible en `http://localhost:8080`.

### Con Docker

```bash
# Construir la imagen
docker build -t legado-backend .

# Ejecutar el contenedor
docker run -e MONGODB_URI="<tu-uri>" -p 8080:8080 legado-backend
```

---

## 🚦 Estado del Proyecto

> **En despliegue funcional en producción.**
>
> El backend se encuentra desplegado en **Render** y el frontend en **Vercel**. La aplicación responde correctamente a las peticiones del cliente, la conexión con MongoDB Atlas está activa y los flujos principales (registro de jugador, gestión de mascotas, combates y logros) funcionan de extremo a extremo.

---

## 👨‍💻 Autor

**Luis Sebastián Díaz** · Ingeniería de Software · 4° Semestre
