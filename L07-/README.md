# Gestor de TODOs (TUI) — Caso de Pruebas de Software

Aplicación de consola para gestionar tareas (TODOs), usada como sistema bajo prueba
en el curso de Ingeniería de Software II.

- **Lenguaje / build:** Java 17+ con Maven
- **Interfaz:** TUI (Text User Interface) con [Lanterna](https://github.com/mabe02/lanterna)
- **Arquitectura:** MVVM (Model – View – ViewModel)
- **Persistencia:** SQLite (driver `sqlite-jdbc`)
- **Pruebas:** JUnit 5 + JaCoCo (cobertura)

El enunciado del ejercicio está en [`docs/ENUNCIADO.md`](docs/ENUNCIADO.md).

## Cómo ejecutar

```bash
# Compilar, ejecutar pruebas y generar el reporte de cobertura
mvn test
# Reporte: target/site/jacoco/index.html

# Ejecutar la aplicación (crea/usa el archivo todos.db en la carpeta actual)
mvn -q compile exec:java

# O generar un JAR ejecutable con todas las dependencias
mvn package
java -jar target/todo-tui-1.0.0.jar            # usa ./todos.db
java -jar target/todo-tui-1.0.0.jar otra.db    # usa otra base de datos
```

> Ejecute la aplicación desde una terminal real (Terminal, iTerm, Windows Terminal, consola
> de Linux). Si no hay terminal disponible (por ejemplo, desde algunos IDE), Lanterna abre
> una ventana Swing que emula la terminal.

### Teclas

| Tecla           | Acción                                          |
|-----------------|-------------------------------------------------|
| `↑` / `↓`       | Moverse por la lista                            |
| `Enter` o `c`   | Marcar como completado / pendiente              |
| `a`             | Agregar un TODO (abre el formulario)            |
| `e`             | Eliminar el TODO seleccionado (pide confirmar)  |
| `f`             | Cambiar filtro: pendientes → completados → todos |
| `q`             | Salir                                           |
| `Tab`           | Moverse entre campos y botones                  |

## Arquitectura (MVVM)

```
src/main/java/pe/edu/ulima/is2/todo
├── App.java                     Arma las capas y arranca la TUI
├── model/                       MODEL: datos y reglas de negocio (sin UI)
│   ├── Todo, Prioridad, NivelUrgencia, FiltroTodo
│   ├── TodoRepository           Interfaz de persistencia
│   ├── SqliteTodoRepository     Implementación con SQLite (JDBC)
│   ├── TodoService              Casos de uso: agregar, listar, completar, eliminar
│   ├── UrgenciaCalculator       Cálculo del nivel de urgencia de un TODO
│   └── ValidacionException, PersistenciaException
├── viewmodel/                   VIEWMODEL: estado observable + comandos (sin Lanterna)
│   ├── Propiedad<T>             Valor observable (data binding)
│   ├── TodoListViewModel        Lista, filtro, mensajes; completar/eliminar
│   ├── TodoFormViewModel        Campos del formulario; guardar
│   └── TodoItemViewModel        Fila formateada para mostrar
└── view/                        VIEW: componentes Lanterna, solo se enlazan al ViewModel
    ├── TodoListView
    └── TodoFormView
```

Flujo: la **View** se suscribe a las `Propiedad<T>` del **ViewModel** y le delega cada
acción del usuario. El **ViewModel** invoca al **Model** (`TodoService`) y actualiza sus
propiedades, lo que redibuja la vista automáticamente. Ni el Model ni el ViewModel conocen
Lanterna, por eso pueden probarse con JUnit sin interfaz.

## Base de datos

Tabla `todos` (se crea automáticamente al iniciar):

| Columna        | Tipo    | Notas                         |
|----------------|---------|-------------------------------|
| `id`           | INTEGER | PK autoincremental            |
| `titulo`       | TEXT    | obligatorio                   |
| `descripcion`  | TEXT    | `''` si no se ingresó         |
| `prioridad`    | TEXT    | `ALTA`, `MEDIA` o `BAJA`      |
| `fecha_limite` | TEXT    | `AAAA-MM-DD` o `NULL`         |
| `completado`   | INTEGER | `0` / `1`                     |
| `creado_en`    | TEXT    | fecha y hora ISO-8601         |

Para pruebas, `SqliteTodoRepository.enMemoria()` crea una base SQLite en memoria.


