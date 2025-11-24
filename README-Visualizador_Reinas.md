# 📘 `NQueensVisualizer` – Documentación Oficial

**Autor:** Fernando Potenciano
**Versión:** 1.0
**Lenguaje:** Java
**Propósito:** Visualizador educativo del algoritmo Backtracking para el problema de las N-Reinas

---

# 📑 Índice

* [Descripción general](#descripción-general)
* [Arquitectura del sistema](#arquitectura-del-sistema)
* [Diagrama general de la aplicación](#diagrama-general-de-la-aplicación)
* [Algoritmo y registro de pasos](#algoritmo-y-registro-de-pasos)
* [Diagramas del flujo del algoritmo](#diagramas-del-flujo-del-algoritmo)
* [Estructuras de datos](#estructuras-de-datos)
* [Documentación Javadoc](#documentación-javadoc)
* [Diagrama UML](#diagrama-uml)
* [Compilación y ejecución](#compilación-y-ejecución)
* [Extensiones posibles](#extensiones-posibles)

---

# 🧩 Descripción general

`NQueensVisualizer` es una aplicación Java Swing que **visualiza paso a paso** el algoritmo de Backtracking para resolver el problema de las N-Reinas.

Permite:

* Mostrar el tablero y las reinas en tiempo real
* Ver intentos válidos, inválidos y backtracks
* Resaltar casillas atacadas
* Reproducir automáticamente el proceso
* Avanzar y retroceder en los pasos
* Revisar las soluciones encontradas
* Mostrar colores y leyendas explicativas

Está diseñado específicamente con fines **docentes**.

---

# 🏛️ Arquitectura del sistema

```
NQueensVisualizer (JFrame)
│
├── ControlPanel (JPanel)
│     ├── Botón Generate Steps
│     ├── Botones Step Forward / Step Back
│     ├── Botón Auto-play
│     ├── Slider velocidad
│
├── BoardPanel (JPanel)
│     ├── Dibujo del tablero
│     ├── Resaltado ataque
│     ├── Representación de reinas
│
├── RightPanel (JPanel)
│     ├── Log de eventos
│     ├── Lista de soluciones
│     ├── Estados (paso actual, total pasos…)
│
└── Motor de Backtracking
      ├── backtrackRecord()
      ├── isSafe()
      ├── placeQueen()
      ├── removeQueen()
      ├── Registro de StepNode
```

---

# 🧭 Diagrama general de la aplicación

```
+-------------------------------------------------------------------+
|                         NQueensVisualizer                         |
+-------------------------------------------------------------------+
|   [Generate Steps] [Step Back] [Step Forward] [Auto ▶] [Velocidad]|
+---------------------------+---------------------------------------+
|                           |                                       |
|       BoardPanel          |               RightPanel              |
|   (tablero gráfico)       |   - Log de eventos                    |
|                           |   - Soluciones                        |
|                           |   - Datos del estado                  |
|                           |                                       |
+---------------------------+---------------------------------------+
```

---

# 🔍 Algoritmo y registro de pasos

El visualizador **ejecuta primero el backtracking completo**, guardando:

```
StepNode {
    int[] boardSnapshot
    int row, col
    StepType type  // PLACE, INVALID, BACKTRACK, SOLUTION
}
```

Luego la UI reproduce esos pasos como si fuese un vídeo interactivo.

---

# 🔄 Diagramas del flujo del algoritmo

### Flujo principal del Backtracking

```
backtrackRecord(row):

    if row == N:
        registrar SOLUCIÓN
        return

    for col in [0..N-1]:

        registrar intento INVALID si no es seguro
        if !isSafe(row, col):
            continue

        colocar reina
        registrar PLACE

        backtrackRecord(row + 1)

        quitar reina
        registrar BACKTRACK
```

---

### Flujo de reproducción visual

```
Usuario → "Generate Steps"
        ↓
backtracking produce lista de StepNode
        ↓
Usuario → Step Forward / Step Back / Auto-Play
        ↓
Se actualiza boardSnapshot según StepNode
        ↓
BoardPanel repinta el tablero
```

---

# 📦 Estructuras de datos

### Estado del tablero

```
board[row] = col  // posición de reina

cols[col] = true            // columna ocupada
diag1[row+col] = true       // diagonal ↘
diag2[row-col+N] = true     // diagonal ↙
```

### Tipos de paso

```
PLACE     → reina colocada correctamente
INVALID   → intento rechazado
BACKTRACK → retirada de reina
SOLUTION  → fila = N
```

---

# 📚 Documentación Javadoc

Puedes **copiar y pegar** esta documentación directamente en tu código.

---

## 🌐 Clase `NQueensVisualizer`

```java
/**
 * NQueensVisualizer
 * ------------------
 * Visualizador interactivo del algoritmo Backtracking para el
 * problema clásico de las N-Reinas.
 *
 * Funcionalidades:
 *  - Representación visual del tablero.
 *  - Reproducción de todos los pasos del algoritmo.
 *  - Identificación de intentos inválidos, placements y backtracks.
 *  - Lista visual de soluciones.
 *
 * Diseño:
 *  - El algoritmo se ejecuta completamente al inicio y guarda
 *    todos los estados en StepNode.
 *  - La interfaz reproduce estos pasos hacia delante/atrás.
 *
 * Uso:
 *  Compilar:  javac NQueensVisualizer.java
 *  Ejecutar:  java NQueensVisualizer
 */
```

---

## 🌐 Clase `StepNode`

```java
/**
 * StepNode
 * --------
 * Representa un paso del algoritmo durante el backtracking.
 *
 * Campos:
 *  - boardSnapshot : estado del tablero en ese instante.
 *  - row, col      : casilla afectada.
 *  - type          : tipo de operación (PLACE, INVALID, BACKTRACK, SOLUTION).
 *
 * Los StepNode permiten reproducir
 * la ejecución completa de forma visual.
 */
```

---

## 🌐 Enum `StepType`

```java
/**
 * Tipos de paso durante la ejecución del backtracking:
 *  - PLACE     : reina colocada correctamente.
 *  - INVALID   : intento fallido (casilla atacada).
 *  - BACKTRACK : retirada de la reina.
 *  - SOLUTION  : tablero completo sin conflictos.
 */
```

---

## 🔧 Método `generateSteps()`

```java
/**
 * Ejecuta el algoritmo completo de búsqueda:
 *  - Reinicia el estado interno.
 *  - Limpia logs y paneles.
 *  - Lanza el backtracking.
 *  - Registra todos los StepNode generados.
 *
 * Tras finalizar, el usuario puede visualizar cada paso.
 */
```

---

## 🔧 Método `backtrackRecord(int row)`

```java
/**
 * Backtracking con registro detallado.
 * Guarda cada movimiento en la lista 'steps'.
 *
 * - INVALID   : casilla no válida.
 * - PLACE     : se coloca una reina.
 * - BACKTRACK : se retira una reina.
 * - SOLUTION  : se alcanzó una solución.
 *
 * Este método genera la base para la visualización temporal.
 */
```

---

## 🔧 Método `isSafe(int row, int col)`

```java
/**
 * Comprueba si colocar una reina en (row, col)
 * es seguro en O(1), usando:
 *  - cols[]
 *  - diag1[]
 *  - diag2[]
 */
```

---

## 🔧 Métodos `placeQueen()` y `removeQueen()`

```java
/**
 * placeQueen(row, col)
 * --------------------
 * Coloca una reina actualizando las estructuras cols, diag1, diag2.
 *
 * removeQueen(row, col)
 * ---------------------
 * Deshace la colocación de una reina.
 */
```

---

## 🎨 `BoardPanel.paintComponent(Graphics g)`

```java
/**
 * Dibuja el tablero, las reinas, los ataques,
 * los colores de cada StepNode y la leyenda lateral.
 */
```

---

# 📐 Diagrama UML

```
+---------------------+
|  NQueensVisualizer  |
+---------------------+
| - board[]           |
| - cols[], diag[]    |
| - steps : List      |
| - stepIndex         |
+---------------------+
| + generateSteps()   |
| + stepForward()     |
| + stepBack()        |
| + toggleAuto()      |
| + backtrackRecord() |
+---------------------+
          |
          | uses
          v
+------------------+
|    StepNode      |
+------------------+
| - boardSnapshot  |
| - row, col       |
| - StepType type  |
+------------------+

+------------------+
|   BoardPanel     |
+------------------+
| + paintComponent |
+------------------+
```

---

# ▶️ Compilación y ejecución

```
javac NQueensVisualizer.java
java NQueensVisualizer
```
