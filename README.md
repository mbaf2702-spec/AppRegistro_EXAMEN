# NexoTalento — Sistema de Gestión de Reclutamiento y Selección de Personal

**Proyecto ABP — Aplicación de escritorio con acceso a datos**
3ro de Bachillerato Informática | Java (Swing) + MySQL

---

## 1. Contexto del problema

**Problemática:** en muchas empresas pequeñas y medianas, el área de Talento Humano registra a los candidatos que aplican a una vacante en hojas de cálculo (Excel) o en cuadernos físicos, y además no existe un canal formal para que el propio candidato se postule: todo depende de correos sueltos o llamadas.

**Proceso actual con dificultades:**

- Se duplican candidatos porque nadie sabe si ya fue registrado.
- Se pierde el historial de entrevistas (quién lo entrevistó, qué resultado obtuvo).
- No se sabe con certeza qué vacantes siguen abiertas.
- No existe trazabilidad de qué persona del equipo de RRHH atendió a cada candidato.
- Cualquier empleado que entra al sistema tiene el mismo nivel de acceso, sin distinción entre quien administra y quien solo necesita consultar información.

**Actores involucrados:**

- Personal de Recursos Humanos (usuarios internos del sistema, con roles distintos).
- Candidatos/aspirantes externos que buscan un puesto de trabajo (no tienen cuenta).
- Jefes de departamento que solicitan una vacante.

**Necesidad que origina la aplicación:** se requiere una aplicación de escritorio que permita a los candidatos postularse ellos mismos sin necesitar una cuenta, mientras el equipo de RRHH administra vacantes, revisa postulaciones y programa entrevistas desde un panel con permisos diferenciados según el rol de cada usuario.

---

## 2. Análisis de requerimientos

| # | Requerimiento | Dónde se cumple |
|---|---|---|
| 1 | Registro de información | Formularios de Vacantes, Aspirantes y Entrevistas |
| 2 | Consulta y visualización de información | Tablas (`JTable`) con pastillas de color por estado |
| 3 | Búsqueda de registros | Campo de búsqueda en cada pantalla de gestión |
| 4 | Modificación de información | Selección de fila → edición → botón Guardar |
| 5 | Eliminación de registros | Botón Eliminar con confirmación previa |
| 6 | Control de acceso | Iniciar sesión + registro de cuentas + recuperación de contraseña |
| 7 | Roles y permisos diferenciados | Rol RRHH (acceso total) vs Empleado (acceso limitado) |
| 8 | Autopostulación pública | Un aspirante se registra sin necesitar cuenta ni iniciar sesión |
| 9 | Trazabilidad | Cada aspirante/entrevista guarda qué usuario lo registró |

---

## 3. Diagrama del modelo lógico

Ver imagen: `docs/diagrama_er.svg`

**Resumen de tablas y relaciones:**

```
usuarios   (1) ── registra ──► (N) aspirantes   (opcional: puede ser NULL si el aspirante se autopostuló)
usuarios   (1) ── registró ──► (N) entrevistas
vacantes   (1) ── recibe   ──► (N) aspirantes
aspirantes (1) ── tiene    ──► (N) entrevistas
```

---

## 4. Script del modelo físico

Ver archivo: `sql/script_bd.sql`

**Incluye:**

- Creación de la base de datos `db_reclutamiento`.
- Las 4 tablas con llaves primarias (`PK`) y foráneas (`FK`).
- Restricciones: `UNIQUE` en cédula y nombre de usuario, `CHECK` en puntaje (escala Likert 1-5), `ENUM` para estados, `ON DELETE RESTRICT/CASCADE` según corresponda.
- `aspirantes.id_usuario` acepta `NULL`, para permitir la autopostulación pública sin necesitar un empleado de RRHH detrás del registro.
- Un usuario administrador de prueba y 2 vacantes de ejemplo.

**Usuario administrador de prueba (rol RRHH, acceso total):**

- Usuario: `admin`
- Contraseña: `Admin2026*`
- Pregunta de seguridad: ¿Cuál es el nombre de tu primera mascota?
- Respuesta: `Firulais`

> Este usuario es solo para fines de demostración/evaluación. No debe usarse en un entorno real sin cambiar la contraseña.

---

## 5. Flujo de la aplicación

La aplicación tiene **dos puertas de entrada distintas**, según quién la use, y a partir de ahí el sistema se ramifica según el rol.

### 5.1 Pantalla inicial

Al abrir la aplicación, el usuario ve dos opciones:

1. **Postularme** (público, sin cuenta) → lleva directo al formulario de aspirantes.
2. **Iniciar sesión** (personal de RRHH/Empleado) → pide usuario y contraseña.

### 5.2 Flujo del candidato (autopostulación pública)

1. El candidato selecciona **Postularme** desde la pantalla inicial. No necesita crear cuenta ni iniciar sesión.
2. Llena el formulario de registro (`RegistroAspirantesForm`): cédula, nombres, apellidos, teléfono, correo electrónico y la vacante a la que aplica (seleccionada de una lista de vacantes abiertas).
3. El formulario valida en el cliente antes de enviar:
   - Campos obligatorios no vacíos.
   - Cédula y teléfono: solo números, sin espacios ni guiones, y la cédula debe cumplir el algoritmo de validación de cédula ecuatoriana (dígito de provincia, tercer dígito y dígito verificador).
   - Correo con formato válido.
4. Al enviar, la aplicación inserta el registro en la tabla `aspirantes` con `id_usuario = NULL` (nadie de RRHH lo registró manualmente) y estado inicial (por ejemplo, `Postulado`).
5. Se muestra un mensaje de confirmación de postulación exitosa, o un mensaje de error claro si algo falla (cédula inválida, campo vacío, error de conexión a la base de datos, etc.).

### 5.3 Flujo del personal interno (RRHH / Empleado)

1. El usuario ingresa sus credenciales en **Iniciar sesión**.
2. Si olvidó la contraseña, puede usar **Recuperar contraseña** (`RecuperarContrasenaForm`), respondiendo su pregunta de seguridad para poder restablecerla.
3. Al iniciar sesión correctamente, el sistema valida el rol del usuario y muestra un menú distinto según corresponda:
   - **Rol RRHH** (acceso total): Vacantes, Aspirantes, Entrevistas, Usuarios/roles.
   - **Rol Empleado** (acceso limitado): solo consulta de Aspirantes y Entrevistas, sin poder crear, editar ni eliminar.
4. **Gestión de vacantes** (solo RRHH): crear una vacante nueva (cargo, departamento, estado abierta/cerrada), editarla o cerrarla cuando ya se cubrió.
5. **Gestión de aspirantes:** ver la lista completa de postulantes (autopostulados + registrados manualmente), buscar por cédula/nombre/vacante, editar sus datos si hay un error, o eliminarlos (con confirmación previa) si corresponde.
6. **Gestión de entrevistas:** seleccionar un aspirante, registrar la entrevista (usuario evaluador, fecha, puntaje en escala Likert 1-5, decisión: aprobado/rechazado/en espera). Cada entrevista queda asociada al usuario de RRHH que la registró, lo cual resuelve el problema de trazabilidad descrito en la sección 1.
7. En cualquier pantalla de gestión, antes de eliminar un registro se muestra un cuadro de confirmación, y toda operación contra MySQL está dentro de un manejo de errores (try/catch) que informa al usuario con un mensaje comprensible en vez de un error técnico crudo.

### 5.4 Resumen visual del flujo

```
                    ┌─────────────────────┐
                    │  Pantalla inicial   │
                    └──────────┬──────────┘
              ┌────────────────┴────────────────┐
              ▼                                 ▼
     "Postularme" (público)             "Iniciar sesión"
              │                                 │
              ▼                        ┌────────┴────────┐
   Formulario de aspirantes            ▼                 ▼
   (valida y guarda con           Rol: RRHH         Rol: Empleado
    id_usuario = NULL)            (acceso total)    (solo consulta)
              │                        │                 │
              ▼                        ▼                 ▼
   Confirmación de postulación   Vacantes / Aspirantes / Entrevistas
                                  (crear, editar, eliminar, buscar)
```

---

## 6. Tecnologías utilizadas

- **Lenguaje:** Java
- **Interfaz gráfica:** Swing (`JFrame`, `JTable`, `JOptionPane`)
- **Base de datos:** MySQL
- **Conexión:** JDBC (`mysql-connector-j`)
- **Control de versiones:** Git / GitHub

> Ajusta esta lista si usaste alguna librería adicional (por ejemplo, para el hash de contraseñas o para dar formato a las tablas).

---

## 7. Requisitos previos e instalación

1. Tener instalado **JDK 17 o superior** y **MySQL Server**.
2. Clonar el repositorio:
   ```bash
   git clone <URL-del-repositorio>
   ```
3. Crear la base de datos ejecutando el script:
   ```bash
   mysql -u root -p < sql/script_bd.sql
   ```
4. Configurar la cadena de conexión (usuario, contraseña, puerto) en el archivo de conexión del proyecto.
5. Abrir el proyecto en tu IDE (NetBeans / IntelliJ / Eclipse) y ejecutar la clase principal.

---

## 8. Estructura del proyecto

```
AppRegistro_EXAMEN/
├── src/
│   ├── forms/          # Formularios Swing (Login, RegistroAspirantes, Vacantes, Entrevistas, RecuperarContrasena...)
│   ├── dao/            # Clases de acceso a datos (CRUD contra MySQL)
│   ├── modelo/         # Clases de dominio (Usuario, Aspirante, Vacante, Entrevista)
│   └── conexion/       # Configuración de la conexión JDBC
├── sql/
│   └── script_bd.sql
├── docs/
│   └── diagrama_er.svg
└── README.md
```

> Ajusta los nombres de paquetes/carpetas a la estructura real de tu proyecto en NetBeans/IntelliJ.

---

## 9. Capturas de pantalla / evidencias

*(Agregar aquí las capturas pedidas por la rúbrica: login, formulario de postulación, validaciones, lista de aspirantes, búsqueda, edición, eliminación con confirmación, y los datos reflejados directamente en MySQL.)*

---

## 10. Autoras

- Integrantes: Melany Acuña, Ashley Morante, Pierina Arce, Daniela Muñoz, Jazmin Paladines, Adriana Bajaña
- Curso: 3ro de Bachillerato Técnico en Informática "A"
- Proyecto ABP — Aplicación con acceso a datos
