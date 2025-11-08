public class HospitalPlanningImproved {
    
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
    
    private static void backtrack(int i, boolean[] seleccionados, int valorActual, 
                                int[] mejorValor, int[] xs, int[] ps, int[] sumaAcumulada) {
        
        // Poda por cota superior
        if (valorActual + sumaAcumulada[i] <= mejorValor[0]) {
            return;
        }
        
        // Caso base: procesados todos los hospitales
        if (i == xs.length) {
            if (valorActual > mejorValor[0]) {
                mejorValor[0] = valorActual;
            }
        } else {
            // Opción 1: No seleccionar el hospital i
            backtrack(i + 1, seleccionados, valorActual, mejorValor, xs, ps, sumaAcumulada);
            
            // Opción 2: Seleccionar el hospital i (si es válido)
            if (esValidoOptimizado(i, seleccionados, xs)) {
                seleccionados[i] = true;
                backtrack(i + 1, seleccionados, valorActual + ps[i], mejorValor, xs, ps, sumaAcumulada);
                seleccionados[i] = false; // Backtrack
            }
        }
    }
    
    private static boolean esValidoOptimizado(int i, boolean[] seleccionados, int[] xs) {
        // Como xs está ordenado, buscamos solo hacia atrás hasta encontrar
        // un hospital a más de 5 unidades
        for (int j = i - 1; j >= 0; j--) {
            if (seleccionados[j]) {
                if (xs[i] - xs[j] <= 5) {
                    return false;
                }
                // Si este hospital está a más de 5, todos los anteriores también
                break;
            }
        }
        return true;
    }
}