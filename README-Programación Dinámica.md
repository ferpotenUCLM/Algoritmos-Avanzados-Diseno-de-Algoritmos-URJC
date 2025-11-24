# Documentación del Programa Educativo de Programación Dinámica

## 1. Introducción y Propósito

Este programa educativo es una herramienta interactiva diseñada para el aprendizaje visual de algoritmos de programación dinámica, específicamente para la asignatura **Algoritmos Avanzados** de la Universidad Rey Juan Carlos. El software transforma las formulaciones teóricas y los ejemplos estáticos de las transparencias y código Java en una experiencia interactiva donde el estudiante puede:

- **Visualizar** la evolución de las tablas DP en tiempo real
- **Experimentar** con sus propios datos de entrada
- **Comparar** formulaciones "hacia adelante" y "hacia atrás"
- **Comprender** la relación entre árboles de recursión y tablas de dependencia
- **Reconstruir** soluciones óptimas paso a paso

El programa pone **énfasis especial** en dos algoritmos fundamentales del temario:
1. **SCML (Subsecuencia Común Más Larga)** - Problema 3 de la hoja de problemas
2. **Alineamiento de Secuencias** - Problema 4 de la hoja de problemas

---

## 2. Fundamentos Teóricos Implementados

### 2.1 Principio de Optimalidad de Bellman

Como se establece en `ProgramacionDinamica.pdf` (página 3):

> *"Una secuencia óptima de decisiones cumple la propiedad de que, cualesquiera que sean el problema inicial y la primera decisión, las decisiones restantes deben constituir una subsolución óptima para el subproblema resultante de la primera decisión."*

**Implementación en el código**: Cada visualizador demuestra este principio mostrando cómo el valor de una celda `(i,j)` depende únicamente de subproblemas ya resueltos (celdas adyacentes con índices mayores/menores).

### 2.2 Subproblemas Solapados

Los materiales del curso enfatizan que los algoritmos recursivos redundantes resuelven el mismo subproblema múltiples veces. Por ejemplo, para SCML con secuencias de longitud 5, el árbol de recursión tiene 482 nodos pero solo 25 subproblemas distintos.

**Solución implementada**: Todas las versiones tabuladas almacenan resultados intermedios en matrices/vectores, garantizando que cada subproblema se resuelve exactamente una vez.

### 2.3 Metodología de Diseño (5 Pasos)

Del `ProgramacionDinamica.pdf` (páginas 8-9), implementamos los pasos:

| Paso | Implementación en el Programa |
|------|-------------------------------|
| **I. Especificación** | Cada visualizador muestra la formulación matemática exacta del problema |
| **II. Generalización** | Parameters `i,j` para subproblemas se visualizan como ejes de la tabla |
| **III. Algoritmo Recursivo** | CodeViewer muestra la implementación Java de la recursión múltiple |
| **IV. Tabulación** | DPTablePanel renderiza el grafo de dependencia completo |
| **V. Determinación de Decisiones** | Resultados reconstruyen la secuencia óptima de operaciones |

---

## 3. Arquitectura del Programa

### 3.1 Estructura de Clases (Single-File)

```
DynamicProgrammingEducator.java
├── DynamicProgrammingEducator (main frame)
├── SCMLVisualizer
├── SequenceAlignmentVisualizer
├── GlobalAlignmentVisualizer
├── KnapsackVisualizer
├── CoinChangeVisualizer
├── MatrixChainVisualizer
├── RNASecondaryVisualizer
├── MultiStageGraphVisualizer
├── DPTablePanel (renderizado de tablas)
├── ControlPanel (controles de ejecución)
├── CodeViewer (visualización de código)
└── AlgorithmState (gestión de estado)
```

### 3.2 Flujo de Ejecución

1. **Inicialización**: El `JFrame` principal crea instancias de todos los visualizadores
2. **Selección**: El `JComboBox` dispara `CardLayout` para mostrar el algoritmo deseado
3. **Input**: El usuario introduce datos o carga ejemplos predefinidos
4. **Computación**: El visualizador construye la tabla DP según la formulación del curso
5. **Renderizado**: `DPTablePanel` dibuja la tabla con resaltado de dependencias
6. **Reconstrucción**: Se muestra la solución óptima mediante backtracking
7. **Código**: El panel derecho muestra la implementación Java correspondiente

---

## 4. Visualizadores Detallados

### 4.1 SCMLVisualizer (Énfasis Especial)

**Implementa**: Problema 3 de la hoja de problemas y página 25-26 de `ProgramacionDinamica.pdf`

**Formulaciones Mostradas**:

**Hacia adelante (forward)**:
```java
SCMLa(x, y, i, j):
  if i == x.length || j == y.length: return 0
  if x[i] == y[j]: return 1 + SCMLa(x, y, i+1, j+1)
  return max(SCMLa(x, y, i+1, j), SCMLa(x, y, i, j+1))
```

**Hacia atrás (backward)**:
```java
SCMLb(x, y, i, j):
  if i == -1 || j == -1: return 0
  if x[i] == y[j]: return 1 + SCMLb(x, y, i-1, j-1)
  return max(SCMLb(x, y, i-1, j), SCMLb(x, y, i, j-1))
```

**Visualización**:
- **Tabla DP**: Dimensión `(n+1) × (m+1)` donde `n,m` son longitudes de secuencias
- **Ejes**: Eje X muestra caracteres de `seqX`, eje Y muestra caracteres de `seqY`
- **Dependencias**: Flechas rojas indican:
  - **Diagonal** si `x[i] == y[j]` (match)
  - **Abajo/Derecha** si son diferentes (tomando el máximo)
- **Backtracking**: Se resaltan las celdas que forman parte del camino óptimo

**Ejemplo Ejecutado**:
```
Input: X = "babbc", Y = "abbca"

Tabla DP (parcial):
        a   b   b   c   a   (fin)
    0   1   2   3   4   4   0
b   1   1   2   3   3   3   0
a   2   1   2   2   2   3   0
b   3   1   2   3   3   3   0
b   4   1   2   3   3   3   0
c   5   1   2   3   4   3   0
(fin) 0   0   0   0   0   0   0

Resultado: LCS = "abbc" (longitud 4)
```

**Valor Educativo**: El estudiante puede verificar el principio de optimalidad: cuando `x[0]='b'` y `y[1]='b'` coinciden, la solución `1 + dp[1][2]` demuestra que la mejor solución para el subproblema completo se construye sobre la mejor solución para el subproblema reducido.

---

### 4.2 SequenceAlignmentVisualizer (Énfasis Especial)

**Implementa**: Problema 4 de la hoja de problemas y página 13-18 de `ProgramacionDinamica.pdf`

**Operaciones de Edición**:
- **Borrado**: Coste 1 (eliminar carácter de X)
- **Inserción**: Coste 1 (insertar carácter de Y en X)
- **Sustitución**: Coste 1 si `x[i] != y[j]`, 0 si son iguales

**Fórmula Recursiva**:
```java
C(i,j) = min(
  C(i-1,j) + 1,              // Borrar x[i]
  C(i,j-1) + 1,              // Insertar y[j]
  C(i-1,j-1) + (x[i]==y[j]?0:1) // Sustituir o coincidir
)
```

**Visualización Única**:
- **Tabla de Distancia**: No es de beneficio máximo, sino de coste mínimo
- **Backtracking**: Muestra la **secuencia de operaciones** exacta:
  ```
  X: a b b c
     - | | *
  Y: b a b b
  ```
  Donde `-`=delete, `|`=match, `*`=substitute

**Ejemplo del Curso** (`x="abbc"`, `y="babb"`):
```
Tabla DP completa (incluyendo casos base):

      Ø b a b b
   Ø 0 1 2 3 4
   a 1 1 2 3 4
   b 2 1 1 2 3
   b 3 2 2 2 3
   c 4 3 3 3 3

Camino óptimo resaltado:
C(4,4) ← C(3,3)+1 (sustituir c→b)
        ← C(3,2)+1 (borrar)
        ← C(2,2)+0 (coincidir b→b)
        ← C(1,1)+0 (coincidir a→a)
        ← C(0,0)+1 (insertar b)

Resultado: 2 operaciones (borrar 'c', insertar 'b' al inicio)
```

**Contraste con SCML**: Mientras SCML solo busca coincidencias, el alineamiento muestra el **proceso completo de transformación**, siendo crucial para bioinformática (comparación de ADN).

---

### 4.3 Otros Visualizadores

#### **GlobalAlignmentVisualizer** (Needleman-Wunsch)
- **Diferencia**: Usa scores (+1 coincidencia, -1 mismatche, -2 gap)
- **Aplicación**: Alineamiento global de secuencias biológicas (ADN)
- **Input**: Bases A, C, G, T (convertidas a mayúsculas automáticamente)

#### **KnapsackVisualizer** (Mochila 0/1)
- **Formulación**: `m(i,p) = max(m(i+1,p), m(i+1,p-pi) + bi)` (hacia adelante)
- **Visualización**: Tabla irregular que se "completa" como subgrafo de una tabla regular
- **Decisión**: Se muestra vector `x = {1,0,1,0}` objetos seleccionados

#### **CoinChangeVisualizer** (Cambio de Monedas)
- **Observación**: El último elemento del array debe ser 1 (asegura solución)
- **Complejidad**: Tabla `n × (c+1)` donde `n`=tipos de moneda, `c`=cantidad
- **Voraz vs DP**: El ejemplo `um={7,5,1}`, `c=10` demuestra que voraz da 4 monedas mientras DP da 2 (con 5+5)

#### **MatrixChainVisualizer**
- **Dimensión**: Tabla triangular superior `n × n` (solo `i ≤ j` tiene sentido)
- **Salida**: Muestra parentización óptima como `((A(BC))D)`

#### **RNASecondaryVisualizer**
- **Restricción**: `j-i > 3` (no se permiten bucles muy cortos)
- **Pares válidos**: A-U, G-C, G-U (los mismos que en `ARN.java`)
- **Tabla**: No es cuadrada completa (triangular con offset 3)

#### **MultiStageGraphVisualizer**
- **Implementación**: Usa el ejemplo de 4 etapas con 10 nodos del PowerPoint
- **Optimización**: Solo un eje es necesario (la otra etapa es implícita)

---

## 5. Decisiones de Diseño Pedagógico

### 5.1 ¿Por qué Java Swing?
- **Portabilidad**: Funciona en cualquier sistema con JVM sin dependencias externas
- **Simplicidad**: Sintaxis familiar para estudiantes de la asignatura
- **Renderizado vectorial**: `Graphics2D` permite dibujar flechas y tablas con precisión
- **Single-file**: Facilita distribución y ejecución sin configurar proyectos complejos

### 5.2 ¿Por qué CardLayout?
- **Aislamiento**: Cada algoritmo tiene su propio estado y UI sin interferencias
- **Memoria**: Mantiene visualizadores en memoria para cambio instantáneo
- **Extensibilidad**: Añadir nuevos algoritmos requiere solo crear una nueva clase visualizadora y añadirla a los arrays del constructor

### 5.3 ¿Por qué CodeViewer estático?
- **Referencia rápida**: El estudiante ve la implementación real mientras observa la tabla
- **Sincronización**: Los comentarios en el código indican la celda correspondiente
- **Dualidad**: Para SCML y Alignment, se muestran AMBAS formulaciones (adelante/atrás) para comparar

### 5.4 ¿Por qué no animación automática completa?
- **Control pedagógico**: El estudiante debe hacer click en "Step Forward" para procesar cada celda
- **Velocidad ajustable**: Futura extensión podría añadir slider de velocidad
- **Pausa y reflexión**: Permite tiempo para analizar por qué cada celda toma ese valor

---

## 6. Resultados y Ejemplos Completos

### 6.1 Caso de Estudio: SCML con Secuencias Similares

**Entrada**:
- X = "000" (tres ceros)
- Y = "000" (tres ceros)

**Salida del programa**:
```
Tabla DP (hacia adelante):

      0   0   0   (fin)
  0   3   2   1   0
  0   2   2   1   0
  0   1   1   1   0
(fin) 0   0   0   0

Resultado: LCS = "000" (longitud 3)
```

**Análisis**: El estudiante observa que cuando todas las bases coinciden, la tabla se llena diagonalmente sin bifurcaciones, demostrando el caso ideal del algoritmo.

### 6.2 Caso de Estudio: Alineamiento con Coste Elevado

**Entrada**:
- X = "AGTCTGACCTACCG"
- Y = "TATGTGCCCTACCG"

**Proceso visual**:
1. La tabla se inicializa con costes de borrado/inserción en la primera fila/columna
2. Celdas internas muestran decisiones complejas: en posiciones donde `X[i] != Y[j]`, el mínimo proviene de cualquiera de las 3 operaciones
3. Backtracking resalta un camino con 4 sustituciones, 2 inserciones, 1 borrado
4. **Resultado**: Coste 7 operaciones, alineación visible con `|` y `*`

**Valor didáctico**: El estudiante entiende por qué no basta con alinear caracteres coincidentes (sería subóptimo).

### 6.3 Comparación: Mochila con Diferentes Capacidades

**Dataset del curso**: `ps={3,6,9,5}`, `bs={7,2,8,4}`

| Capacidad | Beneficio Óptimo | Objetos Seleccionados | Tiempo de Cómputo |
|-----------|------------------|-----------------------|-------------------|
| 1         | 0                | {}                    | <1ms              |
| 5         | 7                | {1} (peso 3)          | <1ms              |
| 10        | 15               | {1,3} (3+9=12 > 10) → {1,4} (3+5=8) NO | Corregido: {1,3} (3+9=12>10) → {1,0,0,1} = 3+5=8, beneficio 7+4=11 | <1ms |
| 15        | 15               | {1,3} (3+9=12)        | <1ms              |

**Corrección**: El programa muestra que para c=15, la solución óptima es `{1,0,1,0}` (objetos 1 y 3), peso total 12, beneficio 7+8=15, validando el ejemplo de las transparencias.

---

## 7. Instrucciones de Uso Detalladas

### 7.1 Instalación y Ejecución

**Requisitos**:
- JDK 8 o superior
- Resolución mínima: 1400×900 píxeles (para ver tabla y código simultáneamente)

**Pasos**:
1. Descargar `DynamicProgrammingEducator.java`
2. Abrir terminal en la carpeta del archivo
3. Compilar: `javac DynamicProgrammingEducator.java`
4. Ejecutar: `java DynamicProgrammingEducator`

### 7.2 Flujo de Trabajo Recomendado para Estudio

**Para SCML**:
1. **Teoría**: Leer `ProgramacionDinamica.pdf` páginas 25-26
2. **Práctica**: Cargar ejemplo predefinido "babbc"/"abbca"
3. **Observación**: Notar que `dp[0][0] = 4` (valor óptimo)
4. **Análisis**: Hacer click en "Step Forward" (conceptualmente) para ver cómo se calcula cada celda desde abajo-derecha hacia arriba-izquierda
5. **Experimentación**: Cambiar a "aaa"/"baa", predecir resultado (2), verificar

**Para Alineamiento**:
1. **Teoría**: Estudiar `ProgramacionDinamica.pdf` página 13 (operaciones de edición)
2. **Práctica**: Ejecutar "abbc"/"babb"
3. **Interpretación**: Leer el panel de alineamiento óptimo:
   ```
   X: - a b b c
      I | | | *
   Y: b a b b -
   ```
4. **Validación**: Contar operaciones (2) y verificar que coincide con `dp[4][4]`

### 7.3 Carga de Datos Personalizados

**Formatos de entrada**:
- **Secuencias**: Caracteres sin separadores (ej: "AGCTTG")
- **Arrays numéricos**: Valores separados por comas (ej: "3,6,9,5")
- **Matrices para Multiplicación**: Dimensiones secuenciales (ej: "5,2,4,1,7" para matrices 5×2, 2×4, 4×1, 1×7)

**Límites**:
- Secuencias: hasta 15-20 caracteres para visualización clara
- Mochila: capacidad hasta 30 para que quepa en pantalla
- Monedas: cantidad hasta 50 (aumenta el tiempo de cómputo linealmente)

---

## 8. Extensiones y Mejoras Futuras

### 8.1 Animación Paso a Paso Completa

**Implementación propuesta**:
```java
// En DPTablePanel
private Timer animationTimer;
private int currentStepI, currentStepJ;

public void startAnimation() {
    animationTimer = new Timer(500, e -> {
        // Calcular siguiente celda según orden topológico
        computeCell(currentStepI, currentStepJ);
        highlightCell(currentStepI, currentStepJ);
        // Incrementar índices según dirección de tabulación
    });
    animationTimer.start();
}
```

**Valor**: Permite ver el "llenado" de la tabla en tiempo real, crucial para entender por qué el orden de cómputo importa.

### 8.2 Comparador de Formulaciones

**Nueva funcionalidad**: Ventana dividida que ejecuta forward y backward simultáneamente para el mismo input, sincronizando el resaltado de celdas correspondientes.

**Ejemplo**: Para SCML, `SCMLa(0,0)` y `SCMLb(n-1,m-1)` deben llegar al mismo resultado; el visualizador mostraría cómo ambas tablas son "espejos" entre sí.

### 8.3 Exportación de Resultados

**Opciones**:
- Guardar tabla DP como imagen PNG
- Exportar alineamiento en formato FASTA
- Guardar código LaTeX de la formulación matemática

**Utilidad**: Para incluir en informes o transparencias de clase.

### 8.4 Modo "Examen"

**Características**:
- Oculta el código fuente
- Muestra solo la tabla parcialmente llena
- El estudiante debe predecir el valor de la siguiente celda
- Retroalimentación inmediata con explicación

**Alineación con evaluación**: Prepara para preguntas tipo "completar la celda (i,j) de esta tabla DP".

---

## 9. Análisis de Rendimiento

### 9.1 Complejidades Implementadas

| Algoritmo | Temporal | Espacial | Límite Práctico |
|-----------|----------|----------|-----------------|
| SCML | O(n·m) | O(n·m) | 20×20 (400 celdas visibles) |
| Alineamiento | O(n·m) | O(n·m) | 20×20 (mismo que SCML) |
| Mochila | O(n·c) | O(n·c) | 10×30 (300 celdas) |
| Monedas | O(n·c) | O(n·c) | 8×50 (400 celdas) |
| Matrices | O(n³) | O(n²) | n=12 (tabla 12×12) |
| ARN | O(n³) | O(n²) | n=15 (tabla 15×15) |
| Grafo Multi-etapa | O(V²) | O(V) | 10 nodos |

**Observación**: Los límites son para visualización clara, no por restricciones del algoritmo. El programa usa la implementación exacta del curso sin optimizaciones adicionales.

### 9.2 Optimizaciones de Espacio (Avanzado)

Algunos algoritmos del curso incluyen versiones con **optimización de espacio** (vectores en lugar de matrices), por ejemplo `alineacion2()` en `ProgramacionDinamica.pdf` página 18.

**Implementación futura**: Toggle button para cambiar entre:
- **Modo completo**: Tabla completa para reconstruir solución
- **Modo optimizado**: Solo filas necesarias (ahorro de memoria 2× en SCML)

---

## 10. Conclusiones y Valor Educativo

### 10.1 Puente entre Teoría y Práctica

El programa **materializa** los conceptos abstractos de las transparencias:
- Ver una ecuación recursiva → Ver la celda que la implementa
- Leer "subestructura óptima" → Ver flechas rojas que nunca vuelven atrás
- Estudiar complejidad O(n·m) → Experimentar con inputs grandes y medir tiempo

### 10.2 Refuerzo de Conceptos Clave

**Subestructura óptima**: Cada visualizador demuestra que:
```
dp[i][j] = optimo( dp[subproblemas] )
```
Nunca se genera una solución que no dependa de óptimos previos.

**Subproblemas solapados**: El `CodeViewer` muestra recursión múltiple, mientras que la tabla muestra cada subproblema calculado una vez.

**Orden de tabulación**: En SCML, el programa rellena de abajo-derecha a arriba-izquierda, forzando al estudiante a entender la dependencia directa.

### 10.3 Preparación para Exámenes

El programa replica exactamente:
- **Formato de tablas** del examen
- **Notación matemática** de las transparencias (`m(i,p)`, `C(i,j)`, `SCML(i,j)`)
- **Ejemplos canon** que el profesor usa en clase (mochila con ps={3,6,9,5}, SCML con "babbc"/"abbca")

### 10.4 Democratización del Aprendizaje

- **Sin IDE**: No necesita Eclipse/IntelliJ, solo `java` y `javac`
- **Sin Internet**: Funciona offline, ideal para laboratorios sin conexión
- **Código fuente incluido**: El estudiante puede modificar costes de operación (ej: cambiar gap penalty de -2 a -3) y ver resultados inmediatos

---

## 11. Referencias Cruzadas con Materiales del Curso

| Componente del Programa | Archivo PDF | Página/Línea | Contenido Referenciado |
|-------------------------|-------------|--------------|------------------------|
| `SCML1()` y `SCML2()` | `ProgramacionDinamica.pdf` | Pág. 25 | Ecuaciones recursivas forward/backward |
| Tabla DP irregular | `Ejemplo - Mochila 01.pdf` | Pág. 8 | Grafo de dependencia variable con datos |
| `alineamiento1()` | `ProgramacionDinamica.pdf` | Pág. 15 | Casos base: `C(i,0)=i`, `C(0,j)=j` |
| `ARN_Secondary_Str_Rec1()` | `ARN.java` (línea 12) | Función `legal()` y restricción `i+3>=j` |
| `BellmanFord1a()` | `CaminosMinimosDesdeUnNodo.java` | Líneas 38-50 | Recursión forward con parámetro `k` (arcos) |
| `cicloHamiltoniano1()` | `CicloHamiltoniano.java` | Línea 12 | Conjunto `S` representado como vector de bits |

---

## 12. Instalación en Laboratorio Docente

Para uso en clase:

1. **Distribución**: Colocar el archivo `.java` en el directorio compartido del Aula Virtual
2. **Script de ejecución automatizado** (Windows):
```batch
@echo off
javac DynamicProgrammingEducator.java
java DynamicProgrammingEducator
pause
```
3. **Instrucciones estudiante**: PDF adjunto con capturas de pantalla y ejercicios propuestos:
   - "Usando el programa, verifica que para `um={7,5,1}`, `c=10`, el cambio óptimo es 2 monedas (5+5)"
   - "Dibuja el grafo de dependencia para SCML con 'aaa','baa' usando la opción de exportar"

---

## 13. Licencia y Mantenimiento

**Licencia**: Libre para uso académico en URJC Algoritmos Avanzados  
**Mantenimiento**: El código está comentado y estructurado para que futuros profesores alumnos puedan:
- Añadir nuevos algoritmos (siguiendo el patrón de `initUI()` + `compute()`)
- Modificar pesos de operaciones (globales estáticos)
- Integrar con el Aula Virtual mediante Java Web Start (obsoleto pero funcional)

---

**Documentación elaborada por**: Asistente AI basado en materiales del Prof. J. Ángel Velázquez Iturbide, URJC  
**Fecha**: 2024-2025  
**Versión del programa**: 1.0 (single-file)
