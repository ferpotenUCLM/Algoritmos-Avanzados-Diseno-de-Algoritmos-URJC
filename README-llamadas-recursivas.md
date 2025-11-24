# Documentación detallada — `RecursionVisualizer`

**Resumen breve.**
`RecursionVisualizer` es una aplicación Java Swing que permite experimentar y visualizar distintas formas de resolver problemas recursivos y su optimización mediante memorización y programación dinámica (tabulación / memoria mínima). Implementa ejemplos clásicos: **Fibonacci**, **Combinatoria (C(m,n))**, **Función de Ackermann** y un ejemplo de **recursión mutua**. Para cada problema la UI muestra resultados, cuenta llamadas recursivas (cuando aplica) y registra tiempos de ejecución en una tabla.

---

# Índice

1. Propósito y alcance
2. Requisitos y cómo ejecutar
3. Arquitectura y diseño de la UI
4. Descripción de los módulos / paneles
5. Explicación técnica de las implementaciones

   * Fibonacci (4 variantes)
   * Combinatoria (4 variantes)
   * Ackermann (2 variantes)
   * Recursión mutua
6. Complejidad temporal y espacial (resumen)
7. Ejemplos de uso / salidas esperadas
8. Casos límite y errores conocidos
9. Pruebas recomendadas (unitarias y manuales)
10. Mejora y extensiones sugeridas
11. Javadoc / documentación por método (plantilla)
12. Licencia y notas finales

---

# 1. Propósito y alcance

Esta aplicación tiene fines didácticos: demostrar la diferencia entre enfoques recursivos puros, memorización (top-down), tabulación (bottom-up) y optimizaciones de memoria, además de evidenciar el coste en llamadas recursivas y tiempo de ejecución. Permite comparar directamente resultados y métricas para entender la *eliminación de redundancia recursiva*.

---

# 2. Requisitos y cómo ejecutar

**Requisitos mínimos**

* JDK 8+ (compilación y ejecución con `javac`/`java`)
* Biblioteca estándar Swing (incluida en JDK)

**Compilar y ejecutar desde consola**

```bash
javac RecursionVisualizer.java
java RecursionVisualizer
```

O abrir el archivo en un IDE (Eclipse, IntelliJ, NetBeans) y ejecutar la clase `RecursionVisualizer` que contiene `public static void main`.

---

# 3. Arquitectura y diseño de la UI

La ventana principal (`JFrame`) contiene un `JTabbedPane` con 4 pestañas:

* **Fibonacci**: entrada `n`, botones: Recursivo, Con Memorización, Tabulación, Minima Memoria; salida en `JTextArea` y registro en `JTable`.
* **Combinatoria**: entradas `m` y `n`; botones análogos a Fibonacci; `JTextArea` + `JTable`.
* **Ackermann**: entradas `m` y `n`; botones: Recursivo y Con Memorizacion; `JTextArea` + `JTable`.
* **Recursion Mutua**: entrada `n`; botón para calcular; salida `JTextArea`.

Cada operación registra: método, parámetros, resultado, número de llamadas recursivas (si aplica) y tiempo en nanosegundos en la tabla correspondiente.

---

# 4. Descripción de los componentes clave (atributos)

* `JTabbedPane tabbedPane` — contenedor de pestañas.
* Para cada problema hay: `JPanel`, `JTextField`(s) de entrada, `JTextArea` de salida, `JTable` con `DefaultTableModel`.
* Mapas para memorización:

  * `Map<Integer,Integer> fibMemo`
  * `Map<String,Integer> combMemo`
  * `Map<String,Integer> ackMemo`
* Contadores de llamadas recursivas:

  * `fibCallCount`, `combCallCount`, `ackCallCount`

---

# 5. Explicación técnica de las implementaciones

## Fibonacci

Se implementan 4 variantes:

1. **Recursivo puro**

   ```java
   private int fibonacciRecursive(int n) {
       fibCallCount++;
       if (n == 0 || n == 1) return 1;
       return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
   }
   ```

   * **Descripción:** versión clásica recursiva que recalcula subproblemas repetidamente.
   * **Llamadas:** contador `fibCallCount` incrementado por cada invocación.
   * **Riesgo:** crecimiento exponencial de llamadas; para n moderadamente grandes (≥40) tarda mucho y puede agotar tiempo.

2. **Memorización (top-down)**

   ```java
   private int fibonacciMemoization(int n) {
       fibCallCount++;
       if (n == 0 || n == 1) return 1;
       if (fibMemo.containsKey(n)) return fibMemo.get(n);
       int result = fibonacciMemoization(n - 1) + fibonacciMemoization(n - 2);
       fibMemo.put(n, result);
       return result;
   }
   ```

   * **Descripción:** almacena resultados parciales en `fibMemo` para evitar recomputaciones.
   * **Mejora:** reduce llamadas a O(n).

3. **Tabulación (bottom-up)**

   ```java
   private int fibonacciTabulation(int n) {
       if (n == 0 || n == 1) return 1;
       int[] fib = new int[n+1];
       fib[0]=1; fib[1]=1;
       for (int i=2; i<=n; ++i) fib[i]=fib[i-1]+fib[i-2];
       return fib[n];
   }
   ```

   * **Descripción:** construye la solución iterativamente.
   * **Ventaja:** control total del orden de cálculo; tiempo O(n), espacio O(n).

4. **Mínima memoria**

   ```java
   private int fibonacciMinMemory(int n) {
       if (n == 0 || n == 1) return 1;
       int prev1 = 1, prev2 = 1, current = 0;
       for (int i = 2; i <= n; i++) {
           current = prev1 + prev2;
           prev2 = prev1;
           prev1 = current;
       }
       return current;
   }
   ```

   * **Descripción:** guarda solo dos valores previos; espacio O(1).

---

## Combinatoria — `C(m, n)` (coeficiente binomial)

Variantes similares: recursivo, memorización, tabulación y mínima memoria.

1. **Recursivo**

   ```java
   private int combinatoriaRecursive(int m, int n) {
       combCallCount++;
       if (n == 0 || m == n) return 1;
       return combinatoriaRecursive(m - 1, n) + combinatoriaRecursive(m - 1, n - 1);
   }
   ```

   * **Descripción:** usa la identidad de Pascal; es exponencial sin memorización.

2. **Memorización**

   * Clave composite `m + "," + n` almacenada en `combMemo`.
   * Evita recomputaciones iguales.

3. **Tabulación**

   ```java
   private int combinatoriaTabulation(int m, int n) {
       int[][] dp = new int[m + 1][n + 1];
       for (int i = 0; i<=m; i++) {
           for (int j = 0; j <= Math.min(i,n); j++) {
               if (j==0 || j==i) dp[i][j]=1;
               else dp[i][j]=dp[i-1][j-1]+dp[i-1][j];
           }
       }
       return dp[m][n];
   }
   ```

   * **Tiempo:** O(m·n). **Espacio:** O(m·n).

4. **Mínima memoria**

   ```java
   private int combinatoriaMinMemory(int m, int n) {
       if (n > m - n) n = m - n;
       int[] dp = new int[n+1];
       dp[0] = 1;
       for (int i = 1; i <= m; i++) {
           for (int j = Math.min(i,n); j>0; j--) {
               dp[j] = dp[j] + dp[j-1];
           }
       }
       return dp[n];
   }
   ```

   * **Descripción:** usa vector de longitud `n+1`, computa en O(m·n) tiempo y O(n) espacio.

---

## Ackermann

Funciones:

1. **Recursiva clásica**

   ```java
   private int ackermannRecursive(int m, int n) {
       ackCallCount++;
       if (m == 0) return n + 1;
       else if (n == 0) return ackermannRecursive(m - 1, 1);
       else return ackermannRecursive(m - 1, ackermannRecursive(m, n - 1));
   }
   ```

   * **Descripción:** ejemplifica recurrencias profundamente recursivas y crecientes muy rápidas.
   * **Riesgo:** `StackOverflowError` con valores pequeños (p. ej. (4,1) ya es enorme); la implementación atrapa `StackOverflowError` y muestra alerta.

2. **Memorización**

   * Similar pero guarda resultados en `ackMemo` por clave `m+","+n`.
   * **Nota importante:** Aunque la memorización ayuda, la función de Ackermann no es primitiva recursiva y crece tan rápido que la memorización **no** hace mágicamente tratable cualquier par (m,n) grandes; en la práctica, sólo pequeños pares son manejables.

---

## Recursión mutua (bebes / adultos)

Implementación:

```java
private int f(int n) { return bebes(n) + adultos(n); }

private int bebes(int n) {
    if (n == 0) return 1;
    else return adultos(n - 1);
}

private int adultos(int n) {
    if (n == 0) return 0;
    else return adultos(n - 1) + bebes(n - 1);
}
```

* **Observación matemática:** `f(n)` coincide con la sucesión de Fibonacci con base `f(0)=1`, `f(1)=1`.
  Demostración por pequeña verificación:

  * n=0 → f(0)=1 (1+0)
  * n=1 → bebes(1)=adultos(0)=0; adultos(1)=adultos(0)+bebes(0)=1 ⇒ f(1)=1
  * n=2 → f(2)=2
* **Propósito:** ejemplo de recursión mutua y cómo descomponer/interpretar funciones mutuamente recursivas en términos de otras sucesiones conocidas.

---

# 6. Complejidad temporal y espacial (resumen)

| Problema        |                Método |                                                                                   Tiempo |                          Espacio |
| --------------- | --------------------: | ---------------------------------------------------------------------------------------: | -------------------------------: |
| Fibonacci       |        Recursivo puro |                                                               Exponencial (O(\varphi^n)) |                     O(n) (stack) |
| Fibonacci       |          Memorización |                                                                                     O(n) |         O(n) (memo) + stack O(n) |
| Fibonacci       |            Tabulación |                                                                                     O(n) |                             O(n) |
| Fibonacci       |        Mínima memoria |                                                                                     O(n) |                             O(1) |
| Combinatoria    |        Recursivo puro |                     Exponencial en m (similar a número de nodos del triángulo de Pascal) |                       O(m) stack |
| Combinatoria    |          Memorización |                                          O(m·n) amortizado (cada estado calculado 1 vez) |                     O(m·n) (map) |
| Combinatoria    |            Tabulación |                                                                                   O(m·n) |                           O(m·n) |
| Combinatoria    |        Mínima memoria |                                                                                   O(m·n) |                             O(n) |
| Ackermann       |             Recursivo |                   Crecimiento no primitivo — extremadamente rápido; no práctico para m≥4 |               Muy grande (stack) |
| Ackermann       |          Memorización | Puede mejorar para algunos pares, pero sigue siendo impracticable para valores moderados | Map de resultados (can be large) |
| Recursión mutua | Actual implementación |                                                                                     O(n) |                       O(n) stack |

> Nota: las constantes y límites reales dependen de entradas y características de la máquina (p. ej. tiempo en ns varía mucho).

---

# 7. Ejemplos de uso y salidas esperadas

**Fibonacci**

* Entrada: `n = 0` → Salida: `Fibonacci(0) = 1`
* `n = 7` → `Fibonacci(7) = 21` (asumiendo serie 1,1,2,3,5,8,13,21)

**Combinatoria**

* `m=5, n=2` → `C(5,2) = 10`

**Ackermann**

* `m=1, n=2` → `Ackermann(1,2) = 4`
* `m=3, n=2` → riesgo de tiempo; comprobar con memorización y cuidado

**Recursión mutua**

* `n=5` → coincide con `Fibonacci(5)` según la definición de la app

La ventana mostrará además:

* Contador de llamadas recursivas (para versiones recursivas/memo).
* Tiempo en nanosegundos.
* Filas añadidas a la tabla para comparar ejecuciones previas.

---

# 8. Casos límite y errores conocidos

* **Entrada no numérica**: controlada con `NumberFormatException` y `JOptionPane` de error.
* **Valores grandes en recursiones**: `StackOverflowError` atrapada para Ackermann (muestra diálogo). Para Fibonacci recursivo y combinatoria recursiva, grandes `n` o `m`/`n` provocarán tiempos muy largos o `StackOverflow`.
* **Overflow entero**: el código usa `int`. Para valores grandes los resultados pueden desbordarse (overflow). Considerar usar `long` o `BigInteger` para casos grandes (p. ej. `C(50,25)` cabe en long? puede superar; usar `BigInteger` si se necesita exactitud).
* **Memoria de `Map`**: memos crecen con número de estados visitados; potencialmente grandes para Ackermann con muchos estados distintos.

---

# 9. Pruebas recomendadas

## Unit tests sugeridos (JUnit)

* Fibonacci:

  * `fibonacciRecursive(0) == 1`
  * `fibonacciMemoization(10) == fibonacciTabulation(10) == fibonacciMinMemory(10)`
* Combinatoria:

  * `combinatoriaTabulation(5,2) == 10`
  * `combinatoriaMinMemory(10,3) == combinatoriaTabulation(10,3)`
* Ackermann:

  * `ackermannRecursive(0, n) == n+1` para varios `n`
  * comparar memorización vs recursivo para pares pequeños (0<=m<=3)
* Recursión mutua:

  * `f(0) == 1`, `f(1) == 1`, `f(5)==fibonacciTabulation(5)`

## Pruebas manuales en UI

* Ejecutar Fibonacci recursivo con `n=30` observar alto número de llamadas / tiempo; repetir con memorización y comparar.
* Ejecutar `Ackermann(3,6)` con precaución (puede tardar o causar overflow).

---

# 10. Mejoras y extensiones sugeridas

* **Soporte a tipos más grandes**: cambiar `int` a `long` o `BigInteger` para evitar overflow.
* **Memo persistente / cache global**: opción para mantener memorias entre ejecuciones (útil para benchmarking).
* **Gráficas**: mostrar gráficos (matplotlib-equivalente o librería Java) del crecimiento de llamadas/tiempos contra `n`.
* **Control de límites**: bloquear valores muy grandes en la UI para evitar bloqueos/StackOverflow.
* **Multi-threading**: ejecutar cálculos en hilos separados y actualizar la UI con `SwingWorker` para evitar bloquear el EDT (Event Dispatch Thread).
* **Exportar resultados**: permitir exportar las tablas a CSV/Excel.
* **Visualizador de árbol de llamadas**: mostrar árbol de llamadas recursivas (para n pequeño) visualmente.
* **Documentación Javadoc completa** (vea plantilla más abajo).

---

# 11. Plantilla Javadoc y documentación por método

A continuación una plantilla de Javadoc que puede añadirse al código (ejemplo para la clase y un método):

```java
/**
 * RecursionVisualizer
 *
 * Aplicación Swing para visualizar técnicas de eliminación de redundancia
 * en recursiones: recursivo, memorización (top-down), tabulación (bottom-up)
 * y optimizaciones de memoria. Se incluyen ejemplos: Fibonacci, combinatoria,
 * Ackermann y recursión mutua.
 *
 * Uso: ejecutar la clase y utilizar las pestañas correspondientes.
 *
 * @author
 * @version 1.0
 */
public class RecursionVisualizer extends JFrame {
    ...
}

/**
 * Calcula fibonacci de forma recursiva (definición clásica).
 * NOTA: Esta implementación tiene coste exponencial y no está
 * destinada a valores altos de n.
 *
 * @param n índice de la sucesión (n >= 0)
 * @return Fibonacci(n) con la convención F(0)=1, F(1)=1
 */
private int fibonacciRecursive(int n) {
    ...
}
```

Se recomienda documentar cada método público/privado crítico con su complejidad y precondiciones (por ejemplo: `n >= 0`).

---

# 12. Notas de seguridad / rendimiento

* Los cálculos se ejecutan en el hilo UI (EDT). Para entradas que tardan mucho, usar `SwingWorker` para evitar congelar la interfaz.
* Verificar límites para evitar `StackOverflowError`. Para Ackermann y recursivas profundas, validar entradas y advertir al usuario.
* Evitar almacenar resultados innecesarios por seguridad de memoria (limpiar `Map` cuando sea apropiado).

---

# 13. Conclusión y recomendaciones finales

`RecursionVisualizer` es una herramienta excelente para entender la diferencia entre enfoques recursivos y de programación dinámica. Para uso docente, añadir gráficas, exportación y control de ejecución en background (SwingWorker) mejorará la usabilidad. También se recomienda introducir pruebas unitarias y manejo de tipos grandes (BigInteger) si se requiere exactitud para entradas elevadas.
