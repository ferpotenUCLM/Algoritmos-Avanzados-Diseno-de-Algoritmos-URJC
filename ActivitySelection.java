import java.util.Arrays;

public class ActivitySelection {

    public static void main(String[] args) {
        // Datos originales del enunciado
        int[] startTimes = {11, 24, 7, 0, 5, 12, 23, 2, 16, 15};
        int[] finishTimes = {21, 29, 8, 3, 11, 25, 24, 18, 20, 24};
        
        System.out.println("=== ALGORITMO SELECCION DE ACTIVIDAD ===");
        System.out.println("Original-TIEMPOS DE INICIO: " + Arrays.toString(startTimes));
        System.out.println("Original-TIEMPOS DE FIN: " + Arrays.toString(finishTimes));
        System.out.println();
        
        // Ordenar índices por tiempo de finalización
        int[] sortedIndices = ordenarIndicesPorFin(finishTimes);
        System.out.println("Indices ordenados por tiempo de finalización: " + Arrays.toString(sortedIndices));
        
        // Crear arrays ordenados para el algoritmo idealizado
        int[] sortedStart = new int[startTimes.length];
        int[] sortedFinish = new int[finishTimes.length];
        for (int i = 0; i < sortedIndices.length; i++) {
            sortedStart[i] = startTimes[sortedIndices[i]];
            sortedFinish[i] = finishTimes[sortedIndices[i]];
        }
        
        // 1. Algoritmo idealizado (entrada ordenada por tiempo de finalización)
        System.out.println("1. ALGORITMO IDEALIZADO (Pre-sorted input):");
        boolean[] resultIdeal = selecActividadesIdealizado(sortedStart, sortedFinish);
        // Mapear resultado a índices originales
        boolean[] resultIdealOriginal = new boolean[startTimes.length];
        for (int i = 0; i < resultIdeal.length; i++) {
            if (resultIdeal[i]) {
                int origIndex = sortedIndices[i];
                resultIdealOriginal[origIndex] = true;
            }
        }
        printResults(resultIdealOriginal, startTimes, finishTimes);
        
        System.out.println();
        
        // 2. Algoritmo realista (entrada no ordenada)
        System.out.println("2. ALGORITMO REALISTA (Unsorted input):");
        boolean[] resultReal = selecActividadesRealista(startTimes, finishTimes);
        printResults(resultReal, startTimes, finishTimes);
    }
    
    // Algoritmo idealizado (asume entrada ordenada por tiempo de finalización)
    public static boolean[] selecActividadesIdealizado(int[] c, int[] f) {
        int n = c.length;
        boolean[] seleccionadas = new boolean[n];
        seleccionadas[0] = true;
        int ultimaSeleccionada = 0;
        
        for (int j = 1; j < n; j++) {
            if (c[j] >= f[ultimaSeleccionada]) {
                seleccionadas[j] = true;
                ultimaSeleccionada = j;
            }
        }
        return seleccionadas;
    }
    
    // Algoritmo realista (maneja entrada no ordenada)
    public static boolean[] selecActividadesRealista(int[] c, int[] f) {
        int n = c.length;
        boolean[] seleccionadas = new boolean[n];
        int[] indices = ordenarIndicesPorFin(f);
        
        seleccionadas[indices[0]] = true;
        int ultimaSeleccionada = indices[0];
        
        for (int i = 1; i < n; i++) {
            int currentIndex = indices[i];
            if (c[currentIndex] >= f[ultimaSeleccionada]) {
                seleccionadas[currentIndex] = true;
                ultimaSeleccionada = currentIndex;
            }
        }
        return seleccionadas;
    }
    
    // Método auxiliar para ordenar índices basado en tiempos de finalización
    private static int[] ordenarIndicesPorFin(int[] f) {
        int n = f.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        
        // Ordenación por inserción basado en tiempos de finalización
        for (int i = 1; i < n; i++) {
            int currentIndex = indices[i];
            int currentFinish = f[currentIndex];
            int j = i - 1;
            while (j >= 0 && f[indices[j]] > currentFinish) {
                indices[j + 1] = indices[j];
                j--;
            }
            indices[j + 1] = currentIndex;
        }
        return indices;
    }
    
    // Método utility para imprimir resultados
    public static void printResults(boolean[] result, int[] starts, int[] finishes) {
        System.out.print("Actividades Seleccionadas: ");
        for (int i = 0; i < result.length; i++) {
            if (result[i]) {
                System.out.print("A" + i + " ");
            }
        }
        System.out.println();
        
        System.out.print("Array de Seleccion:     [");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] ? "true" : "false");
            if (i < result.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        
        int count = 0;
        for (boolean b : result) if (b) count++;
        System.out.println("Total de actividades seleccionadas: " + count);
    }
}