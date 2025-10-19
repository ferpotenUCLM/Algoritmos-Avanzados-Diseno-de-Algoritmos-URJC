public class HospitalPlanningBacktrack {
    
    /**
     * Algoritmo de vuelta atrás exacto para el problema de hospitales
     * Técnica: Búsqueda exhaustiva con poda por restricciones
     */
    public static int hospitalesBacktracking(int[] xs, int[] ps) {
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
     * Método recursivo de vuelta atrás
     * Basado en el esquema de las diapositivas "TécnicaDeVueltaAtrasParaOptimizacion"
     */
    private static void backtrack(int i, boolean[] seleccionados, int valorActual, 
                                int[] mejorValor, int[] xs, int[] ps, int[] sumaAcumulada) {
        
        // Poda: si no podemos superar el mejor valor actual, cortamos
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
        backtrack(i + 1, seleccionados, valorActual, mejorValor, xs, ps, sumaAcumulada);
        
        // Opción 2: Seleccionar el hospital i (si es válido)
        if (esValido(i, seleccionados, xs)) {
            seleccionados[i] = true;
            backtrack(i + 1, seleccionados, valorActual + ps[i], mejorValor, xs, ps, sumaAcumulada);
            seleccionados[i] = false; // Backtrack
        }
    }
    
    /**
     * Verifica si se puede seleccionar el hospital i respetando las restricciones de distancia
     * Optimización: solo verifica con hospitales ya seleccionados a la izquierda
     */
    private static boolean esValido(int i, boolean[] seleccionados, int[] xs) {
        for (int j = 0; j < i; j++) {
            if (seleccionados[j] && Math.abs(xs[i] - xs[j]) <= 5) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Versión optimizada de esValido que aprovecha el ordenamiento
     */
    private static boolean esValidoOptimizado(int i, boolean[] seleccionados, int[] xs) {
        // Como xs está ordenado, podemos buscar solo hacia atrás hasta encontrar 
        // un hospital a más de 5 unidades
        for (int j = i - 1; j >= 0; j--) {
            if (seleccionados[j]) {
                if (xs[i] - xs[j] <= 5) {
                    return false;
                }
                // Si este hospital está a más de 5, todos los anteriores también estarán
                // (porque el array está ordenado)
                break;
            }
        }
        return true;
    }
}