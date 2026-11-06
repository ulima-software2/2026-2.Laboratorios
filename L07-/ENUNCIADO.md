# Ejercicio: Pruebas de caja blanca y caja negra

**Curso:** Ingeniería de Software II
**Sistema bajo prueba:** Gestor de TODOs (TUI, MVVM, SQLite) — ver [`README.md`](../README.md)

## Objetivos

1. Aplicar **pruebas de caja blanca** con la técnica de **camino básico**: construir el grafo de
   flujo de una función, calcular su complejidad ciclomática y derivar los casos de prueba.
2. Aplicar **pruebas de caja negra** con la técnica de **partición equivalente**: identificar
   clases de equivalencia a partir de una especificación y derivar los casos de prueba.
3. Automatizar ambos conjuntos de casos con **JUnit 5** y verificar la cobertura con **JaCoCo**.

## Preparación

1. Instale JDK 17 o superior y Maven 3.9+.
2. Ejecute `mvn test` y luego `mvn -q compile exec:java` para comprobar que todo funciona.
3. Use la aplicación unos minutos: agregue TODOs con y sin fecha límite, con distintas
   prioridades, márquelos como completados y observe la columna **Urgencia**.
4. Revise la arquitectura descrita en el `README.md`.

---

## Parte A — Caja blanca: camino básico (10 puntos)

### Función bajo prueba

`pe.edu.ulima.is2.todo.model.UrgenciaCalculator#calcular(Todo todo, LocalDate hoy)`

```java
public NivelUrgencia calcular(Todo todo, LocalDate hoy) {
    if (todo.isCompletado()) {
        return NivelUrgencia.NINGUNA;
    }
    Prioridad prioridad = todo.getPrioridad();
    LocalDate fechaLimite = todo.getFechaLimite();
    if (fechaLimite == null) {
        if (prioridad == Prioridad.ALTA) {
            return NivelUrgencia.MEDIA;
        }
        return NivelUrgencia.BAJA;
    }
    long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaLimite);
    if (diasRestantes < 0) {
        return NivelUrgencia.CRITICA;
    }
    if (diasRestantes <= DIAS_INMINENTE) {          // DIAS_INMINENTE = 2
        if (prioridad == Prioridad.ALTA) {
            return NivelUrgencia.CRITICA;
        }
        return NivelUrgencia.ALTA;
    }
    if (diasRestantes <= DIAS_PROXIMO && prioridad != Prioridad.BAJA) {   // DIAS_PROXIMO = 7
        return NivelUrgencia.MEDIA;
    }
    return NivelUrgencia.BAJA;
}
```

> Puede elegir otra función del proyecto **solo si** tiene una complejidad ciclomática de al
> menos 6 y el docente lo aprueba. Justifique la elección.

### Actividades

| #  | Actividad |
|----|-----------|
| A1 | Numere las sentencias/bloques del código y explique qué agrupa cada nodo. **Las condiciones compuestas (`&&`, `\|\|`) deben separarse en un nodo por cada condición simple.** |
| A2 | Dibuje el **grafo de flujo** (nodos, aristas, nodos predicado y regiones). |
| A3 | Calcule la **complejidad ciclomática V(G)** por los **tres** métodos: `V(G) = E − N + 2`, `V(G) = P + 1` y número de regiones. Los tres deben coincidir. | 
| A4 | Liste un **conjunto básico de caminos independientes** (tantos como V(G)). Cada camino debe introducir al menos una arista nueva. |
| A5 | Diseñe un **caso de prueba por camino**: datos de entrada (`completado`, `prioridad`, `fechaLimite`, `hoy`), resultado esperado y camino que recorre. Indique si algún camino es infactible. |
| A6 | Implemente los casos en `src/test/java/pe/edu/ulima/is2/todo/cajablanca/UrgenciaCalculatorTest.java`, ejecútelos y adjunte la captura de la cobertura de **ramas** de `UrgenciaCalculator` en el reporte de JaCoCo (`target/site/jacoco/index.html`). |

Formato sugerido para A5:

| ID   | Camino | Input | Resultado esperado |
|------|--------|------------|-----------|
| CB01 | …      | …          | …         |


---

## Parte B — Caja negra: partición equivalente (1 punto)

### Funcionalidad bajo prueba: *Agregar TODO*

Desde la interfaz, el usuario abre el formulario (`a`) e ingresa los datos. El ViewModel los
envía como texto a:

```java
Todo TodoService.agregar(String titulo, String descripcion, String prioridad, String fechaLimite)
```

Si los datos son válidos, el TODO se guarda como **pendiente** y se devuelve con su `id`.
Si no, se lanza `ValidacionException` con el nombre del campo que falló (`getCampo()`) y
**no se guarda nada**.

### Especificación

| Regla | Campo | Descripción |
|-------|-------|-------------|
| R1 | Título | Obligatorio. No puede ser nulo, vacío ni contener solo espacios. |
| R2 | Título | Sin contar espacios al inicio y al final, debe tener entre **3 y 100** caracteres. |
| R3 | Título | No puede existir otro TODO **pendiente** con el mismo título (sin distinguir mayúsculas/minúsculas). Sí se permite repetir el título de un TODO **completado**. |
| R4 | Descripción | Opcional (nula o vacía se guarda como `""`). Máximo **500** caracteres. |
| R5 | Prioridad | `ALTA`, `MEDIA` o `BAJA`, sin distinguir mayúsculas. Si es nula o vacía se usa **`MEDIA`**. Cualquier otro valor es inválido. |
| R6 | Fecha límite | Opcional. Si se ingresa, debe tener formato **`AAAA-MM-DD`**, ser una fecha que exista en el calendario y **no ser anterior a hoy** (hoy sí se permite). |

> En la interfaz la prioridad se elige con una lista desplegable, pero el servicio es la
> frontera que se prueba y **debe** validar cualquier texto que reciba.

### Actividades

| #  | Actividad |
|----|-----------|
| B1 | Identifique las **condiciones de entrada** a partir de la especificación. |
| B2 | Construya la **tabla de clases de equivalencia**: condición de entrada, clases válidas y clases inválidas, cada una con un identificador (V1, V2…, NV1, NV2…). |
| B3 | Diseñe los **casos de prueba**: los casos válidos deben cubrir **la mayor cantidad posible** de clases válidas a la vez; cada caso inválido debe cubrir **una sola** clase inválida (el resto de datos, válidos). |
| B4 | Implemente los casos en `src/test/java/pe/edu/ulima/is2/todo/cajanegra/AgregarTodoTest.java`. Use una base en memoria (`SqliteTodoRepository.enMemoria()`) y un reloj fijo (`Clock.fixed(...)`) para que las pruebas con fechas no dependan del día en que se ejecutan. |

Formatos sugeridos:

| Condición de entrada | Clases válidas | Clases inválidas |
|----------------------|----------------|------------------|
| …                    | V1: …          | NV1: …           |

| ID   | Clases cubiertas | titulo | descripcion | prioridad | fechaLimite | Resultado esperado |
|------|------------------|--------|-------------|-----------|-------------|--------------------|
| CN01 | …                | …      | …           | …         | …           | …                  |

---

## Entregables

1. Documento Word/PDF con las respuestas de A1–A4 y B1–B4 (el grafo puede hacerse a mano, con
   draw.io o con Mermaid).
2. Proyecto con las clases de prueba implementadas. `mvn test` debe terminar en `BUILD SUCCESS`.
3. Captura del reporte de JaCoCo.

## Recomendaciones

- No modifique el código de `src/main`. Si cree haber encontrado un defecto, documéntelo
  (entrada, resultado esperado, resultado obtenido) en lugar de corregirlo.
- Use `@DisplayName` para que el nombre de cada prueba indique el ID del caso y el camino o
  las clases que cubre.
- Para construir fechas relativas use `HOY.plusDays(n)` / `HOY.minusDays(n)` sobre una fecha fija.
