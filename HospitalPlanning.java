import java.util.Arrays;
import java.util.Comparator;

public class HospitalPlanning {
    
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
    
    // Métodos auxiliares
    private static boolean puedeSeleccionar(int hospital, boolean[] seleccionados, int[] xs) {
        // Verificar que no haya hospitales seleccionados dentro del radio de 5 km
        for (int i = 0; i < seleccionados.length; i++) {
            if (seleccionados[i] && Math.abs(xs[hospital] - xs[i]) <= 5) {
                return false;
            }
        }
        return true;
    }
    
    private static void bloquearCercanos(int hospital, boolean[] bloqueados, int[] xs) {
        // Marcar hospitales dentro del radio de 5 km como no seleccionables
        for (int i = 0; i < bloqueados.length; i++) {
            if (!bloqueados[i] && Math.abs(xs[hospital] - xs[i]) <= 5) {
                bloqueados[i] = true;
            }
        }
    }
    
    private static int contarHospitalesEnRadio(int hospital, int[] xs, double radio) {
        int count = 0;
        for (int i = 0; i < xs.length; i++) {
            if (Math.abs(xs[hospital] - xs[i]) <= radio) {
                count++;
            }
        }
        return count;
    }
    
    // Método de prueba con el ejemplo del enunciado
    public static void main(String[] args) {
        int[] xs = {6, 7, 12, 14};
        int[] ps = {5, 6, 5, 1};
        
        System.out.println("Ejemplo del enunciado:");
        System.out.println("Posiciones: " + Arrays.toString(xs));
        System.out.println("Pacientes: " + Arrays.toString(ps));
        System.out.println();
        
        int resultado1 = hospitals1(xs, ps);
        int resultado2 = hospitals2(xs, ps);
        
        System.out.println("Algoritmo 1 (valor descendente): " + resultado1);
        System.out.println("Algoritmo 2 (densidad de valor): " + resultado2);
        System.out.println("Solución óptima esperada: 10");
    }
}