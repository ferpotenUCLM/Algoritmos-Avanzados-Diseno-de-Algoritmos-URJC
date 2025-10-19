import java.util.Arrays;
public class HospitalPlanningBack {
/**
 * Algoritmo de vuelta atrás exacto para el problema de hospitales
 * Técnica: Búsqueda exhaustiva con poda por restricciones
 * 
 * @param xs Array de posiciones de hospitales (ordenado creciente)
 * @param ps Array de beneficios de hospitales
 * @return Valor máximo de beneficios alcanzables
 */
public static void main(String[] args) {
    int[] xs = {6, 7, 12, 14};
    int[] ps = {5, 6, 5, 1};
    
    System.out.println("\n=== PRUEBA CON DATOS DEL ENUNCIADO ===");
    System.out.println("Posiciones: " + Arrays.toString(xs));
    System.out.println("Pacientes: " + Arrays.toString(ps));
    System.out.println();
    
    int resultado1 = hospitals1(xs, ps);
    int resultado2 = hospitals2(xs, ps);
    
    System.out.println("Algoritmo 1 (valor descendente): " + resultado1);
    System.out.println("Algoritmo 2 (densidad de valor): " + resultado2);

	int resultado = hospitales(xs, ps);
	System.out.println("Máximo valor alcanzable , se usa backtracking : " + resultado);
	

    
    int r4 = algoopt(xs, ps);
    
    System.out.println("Algoritmo Optimo Híbrido: " + r4);
    System.out.println("Solución óptima esperada: 10");
}

/**
 * Algoritmo de vuelta atrás exacto para el problema de hospitales
 * Técnica: Búsqueda exhaustiva con poda por restricciones
 */
public static int hospitales(int[] xs, int[] ps) {
    int n = xs.length;
    boolean[] seleccionados = new boolean[n];
    int[] mejorValor = {0};
    
    // Precalcular suma acumulada para poda
    int[] sumaAcumulada = new int[n + 1];
    sumaAcumulada[n] = 0;
    for (int i = n - 1; i >= 0; i--) {
        sumaAcumulada[i] = ps[i] + sumaAcumulada[i + 1];
    }
    
    backtrack(0, seleccionados, 0, mejorValor, xs, ps, sumaAcumulada);
    return mejorValor[0];
}
/**
 * Método recursivo de vuelta atrás.
 * Basado en el esquema de optimización por búsqueda exhaustiva
 * con poda por cota superior.
 * 
 * @param i Índice actual (nivel del árbol)
 * @param seleccionados Array booleano de hospitales seleccionados
 * @param valorActual Valor acumulado hasta el momento
 * @param mejorValor Array con la mejor solución encontrada
 * @param xs Array de posiciones
 * @param ps Array de beneficios
 * @param sumaAcumulada Array con sumas acumuladas para poda
 */
private static void backtrack(int i, boolean[] seleccionados, 
                              int valorActual, int[] mejorValor, 
                              int[] xs, int[] ps, int[] sumaAcumulada) {
    
    // Poda por cota superior: 
    // Si no podemos superar el mejor valor actual, cortamos
    if (valorActual + sumaAcumulada[i] <= mejorValor[0]) {
        return;
    }
    
    // Caso base: hemos procesado todos los hospitales
    if (i == xs.length) {
        if (valorActual > mejorValor[0]) {
            mejorValor[0] = valorActual;
        }
        return;
    }
    
    // Opción 1: No seleccionar el hospital i
    backtrack(i + 1, seleccionados, valorActual, mejorValor, 
              xs, ps, sumaAcumulada);
    
    // Opción 2: Seleccionar el hospital i (si es válido)
    if (esValidoOptimizado(i, seleccionados, xs)) {
        seleccionados[i] = true;
        backtrack(i + 1, seleccionados, valorActual + ps[i], 
                  mejorValor, xs, ps, sumaAcumulada);
        seleccionados[i] = false; // Backtrack
    }
}

/**
 * Verifica si se puede seleccionar el hospital i respetando 
 * las restricciones de distancia.
 * 
 * Optimización: Aprovecha que xs está ordenado para buscar 
 * solo hacia atrás hasta encontrar un hospital a más de 5 unidades.
 * Complejidad: O(1) en promedio, O(n) en el peor caso.
 * 
 * @param i Índice del hospital a verificar
 * @param seleccionados Array de selecciones
 * @param xs Array de posiciones (ordenado)
 * @return true si se puede seleccionar, false si viola restricción
 */
private static boolean esValidoOptimizado(int i, 
                                          boolean[] seleccionados, 
                                          int[] xs) {
    // Como xs está ordenado en orden creciente, buscamos solo hacia atrás
    // hasta encontrar un hospital a más de 5 unidades
    for (int j = 0; j < i; j++) {
        if (seleccionados[j] && Math.abs(xs[i] - xs[j]) <= 5) {
            return false;
        }
    }
    return true;
            // Si este hospital está a más de 5, todos los anteriores 
            // también lo están (porque el array está ordenado)
}


/**
 * Algoritmo heurístico 1: Selección por valor descendente
 * Justificación: Prioriza los hospitales que atienden más víctimas,
 * similar al enfoque voraz por beneficio en el problema de la mochila
 */
public static int hospitals1(int[] xs, int[] ps) {
    int n = xs.length;
    Integer[] indices = new Integer[n];
    for (int i = 0; i < n; i++) indices[i] = i;
    
    // Ordenar por valor descendente
    Arrays.sort(indices, (a, b) -> ps[b] - ps[a]);
    
    boolean[] seleccionados = new boolean[n];
    int total = 0;
    
    for (int i : indices) {
        if (puedeSeleccionar(i, seleccionados, xs)) {
            seleccionados[i] = true;
            total += ps[i];
            // Bloquear hospitales dentro del radio de 5 km
            bloquearCercanos(i, seleccionados, xs);
        }
    }
    return total;
}

/**
 * Algoritmo heurístico 2: Selección por densidad de valor
 * Justificación: Considera no solo el valor absoluto sino también
 * cuántos otros hospitales "bloquea" por la restricción de distancia
 */
public static int hospitals2(int[] xs, int[] ps) {
    int n = xs.length;
    Integer[] indices = new Integer[n];
    for (int i = 0; i < n; i++) indices[i] = i;
    
    // Calcular "densidad" de valor (valor / número de hospitales bloqueados)
    double[] densidad = new double[n];
    for (int i = 0; i < n; i++) {
        int bloqueados = contarHospitalesEnRadio(i, xs, 5.0);
        densidad[i] = (double) ps[i] / bloqueados;
    }
    
    // Ordenar por densidad descendente
    Arrays.sort(indices, (a, b) -> Double.compare(densidad[b], densidad[a]));
    
    boolean[] seleccionados = new boolean[n];
    int total = 0;
    
    for (int i : indices) {
        if (puedeSeleccionar(i, seleccionados, xs)) {
            seleccionados[i] = true;
            total += ps[i];
            bloquearCercanos(i, seleccionados, xs);
        }
    }
    return total;
}

/**
 * Versión no optimizada de esValido en bloquearCercanos para comparación.
 * Verifica con TODOS los hospitales seleccionados.
 * Complejidad: O(n)
 * 
 * @param i Índice del hospital a verificar
 * @param seleccionados Array de selecciones
 * @param xs Array de posiciones
 * @return true si se puede seleccionar, false si viola restricción
 */
private static void bloquearCercanos(int hospital, boolean[] bloqueados, int[] xs) {
    // Marcar hospitales dentro del radio de 5 km como no seleccionables
    for (int i = 0; i < bloqueados.length; i++) {
        if (!bloqueados[i] && Math.abs(xs[hospital] - xs[i]) <= 5) {
            bloqueados[i] = true;
        }
    }
}
/**
 * Algoritmo Optimo GenIA: Híbrido - Ordenación por posición + valor
 * Estrategia: Combinar proximidad geográfica con valor
 */
public static int algoopt(int[] xs, int[] ps) {
    int n = xs.length;
    Integer[] indices = new Integer[n];
    for (int i = 0; i < n; i++) indices[i] = i;
    
    // Ordenar por posición, luego por valor descendente
    Arrays.sort(indices, (a, b) -> {
        if (xs[a] != xs[b]) {
            return xs[a] - xs[b];
        }
        return ps[b] - ps[a];
    });
    
    boolean[] seleccionados = new boolean[n];
    int total = 0;
    int ultimaPosicion = -10;
    
    for (int i = 0; i < n; i++) {
        int currentIndex = indices[i];
        if (xs[currentIndex] - ultimaPosicion > 5) {
            seleccionados[currentIndex] = true;
            total += ps[currentIndex];
            ultimaPosicion = xs[currentIndex];
        }
    }
    return total;
}


// Métodos auxiliares mejorados
private static boolean puedeSeleccionar(int hospital, boolean[] seleccionados, int[] xs) {
    // Verificar que no haya conflictos con hospitales ya seleccionados
    for (int i = 0; i < seleccionados.length; i++) {
        if (seleccionados[i] && Math.abs(xs[hospital] - xs[i]) <= 5) {
            return false;
        }
    }
    return true;
}

private static int contarHospitalesEnRadio(int hospital, int[] xs, double radio) {
    int count = 0;
    for (int i = 0; i < xs.length; i++) {
        if (i != hospital && Math.abs(xs[hospital] - xs[i]) <= radio) {
            count++;
        }
    }
    return count;
}
}