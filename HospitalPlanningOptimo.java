import java.util.Arrays;
import java.util.Comparator;

public class HospitalPlanningOptimo {
    
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
    
    // Método de prueba mejorado
    public static void main(String[] args) {
        // Ejemplo del enunciado
        int[] xs = {6, 7, 12, 14};
        int[] ps = {5, 6, 5, 1};
        
        System.out.println("=== PRUEBA CON DATOS DEL ENUNCIADO ===");
        System.out.println("Posiciones: " + Arrays.toString(xs));
        System.out.println("Pacientes: " + Arrays.toString(ps));
        System.out.println();
        
        int r4 = algoopt(xs, ps);
        
        System.out.println("Algoritmo Optimo GenIA (híbrido): " + r4);
        System.out.println("Solución óptima esperada: 10");
        
        // Prueba adicional con datos más complejos
        System.out.println("\n=== PRUEBA ADICIONAL ===");
        int[] xs2 = {1, 6, 7, 12, 13, 18};
        int[] ps2 = {10, 8, 9, 7, 8, 10};
        
        System.out.println("Posiciones: " + Arrays.toString(xs2));
        System.out.println("Pacientes: " + Arrays.toString(ps2));
        
        System.out.println("Algoritmo Optimo GenIA: " + algoopt(xs2, ps2));
    }
}