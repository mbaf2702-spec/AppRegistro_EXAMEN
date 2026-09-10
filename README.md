# AppRegistro_EXAMEN

# NexoTalento — Sistema de Gestión de Reclutamiento y Selección de Personal

Proyecto ABP — Aplicación de escritorio con acceso a datos
3ro de Bachillerato Informática | Java (Swing) + MySQL

---

## 1. Contexto del problema

**Problemática:** En muchas empresas pequeñas y medianas, el área de Talento Humano
registra a los candidatos que aplican a una vacante en **hojas de cálculo (Excel)**
o en cuadernos físicos, y además no existe un canal formal para que el propio
candidato se postule: todo depende de correos sueltos o llamadas.

**Proceso actual con dificultades:**
- Se duplican candidatos porque nadie sabe si ya fue registrado.
- Se pierde el historial de entrevistas (quién lo entrevistó, qué resultado obtuvo).
- No se sabe con certeza qué vacantes siguen abiertas.
- No existe trazabilidad de qué persona del equipo de RRHH atendió a cada candidato.
- Cualquier empleado que entra al sistema tiene el mismo nivel de acceso, sin
  distinción entre quien administra y quien solo necesita consultar información.

**Actores involucrados:**
- Personal de Recursos Humanos (usuarios internos del sistema, con roles distintos).
- Candidatos/aspirantes externos que buscan un puesto de trabajo (no tienen cuenta).
- Jefes de departamento que solicitan una vacante.

**Necesidad que origina la aplicación:** Se requiere una aplicación de escritorio
que permita a los candidatos postularse ellos mismos sin necesitar una cuenta,
mientras el equipo de RRHH administra vacantes, revisa postulaciones y programa
entrevistas desde un panel con permisos diferenciados según el rol de cada usuario.

---

## 2. Análisis de requerimientos

| # | Requerimiento | Dónde se cumple |
|---|----------------|------------------|
| 1 | Registro de información | Formularios de Vacantes, Aspirantes y Entrevistas |
| 2 | Consulta y visualización de información | Tablas (JTable) con pastillas de color por estado |
| 3 | Búsqueda de registros | Campo de búsqueda en cada pantalla de gestión |
| 4 | Modificación de información | Selección de fila → edición → botón Guardar |
| 5 | Eliminación de registros | Botón Eliminar con confirmación previa |
| 6 | Control de acceso | Login + registro de cuentas + recuperación de contraseña |
| 7 | Roles y permisos diferenciados | Rol `RRHH` (acceso total) vs `Empleado` (acceso limitado) |
| 8 | Autopostulación pública | Un aspirante se registra sin necesitar cuenta ni login |
| 9 | Trazabilidad | Cada aspirante/entrevista guarda qué usuario lo registró |

---

## 3. Diagrama del modelo lógico

Ver imagen: [`docs/diagrama_er.svg`](docs/diagrama_er.svg)

Resumen de tablas y relaciones:
- **usuarios** (1) ── registra ──► (N) **aspirantes** *(opcional: puede ser NULL si el aspirante se autopostuló)*
- **usuarios** (1) ── registra ──► (N) **entrevistas**
- **vacantes** (1) ── recibe ──► (N) **aspirantes**
- **aspirantes** (1) ── tiene ──► (N) **entrevistas**

---

## 4. Script del modelo físico

Ver archivo: [`sql/script_bd.sql`](sql/script_bd.sql)

Incluye:
- Creación de la base de datos `db_reclutamiento`.
- Las 4 tablas con llaves primarias (`PK`) y foráneas (`FK`).
- Restricciones: `UNIQUE` en cédula y nombre de usuario, `CHECK` en puntaje (escala
  Likert 1-5), `ENUM` para estados, `ON DELETE RESTRICT/CASCADE` según corresponda.
- `aspirantes.id_usuario` acepta `NULL`, para permitir la autopostulación pública
  sin necesitar un empleado de RRHH detrás del registro.
- Un usuario administrador de prueba y 2 vacantes de ejemplo.

**Usuario administrador de prueba (rol RRHH, acceso total):**
- Usuario: `admin`
- Contraseña: `Admin2026*`
- Pregunta de seguridad: ¿Cuál es el nombre de tu primera mascota?
- Respuesta: `Firulais`

---

## 5. Flujo de la aplicación
