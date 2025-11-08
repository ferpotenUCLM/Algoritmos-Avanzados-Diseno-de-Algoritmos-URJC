import java.util.*;

public class HospitalPlanningBranchBound {
    
    
    //Nodo para el algoritmo de ramificacion y poda
    static class Node implements Comparable<Node> {
        int nivel;
        boolean[] seleccionados;
        int valorActual;
        double cotaSuperior;
        
        public Node(int nivel, boolean[] seleccionados, int valorActual, double cotaSuperior) {
            this.nivel = nivel;
            this.seleccionados = seleccionados.clone();
            this.valorActual = valorActual;
            this.cotaSuperior = cotaSuperior;
        }
        
        @Override
        public int compareTo(Node other) {
            // Orden descendente por cota superior (mayor cota primero)
            return Double.compare(other.cotaSuperior, this.cotaSuperior);
        }
    }
    
    // Algoritmo de ramificacion y poda
    
    public static int hospitalesBranchBound(int[] xs, int[] ps) {
        int n = xs.length;
        
        // Precalcular suma acumulada para la funcion de cota
        int[] sumaAcumulada = new int[n + 1];
        sumaAcumulada[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            sumaAcumulada[i] = ps[i] + sumaAcumulada[i + 1];
        }
        
        // Cola de prioridad para nodos activos
        PriorityQueue<Node> cola = new PriorityQueue<>();
        
        // Nodo inicial
        boolean[] seleccionadosInicial = new boolean[n];
        double cotaInicial = calcularCota(0, 0, sumaAcumulada);
        cola.offer(new Node(0, seleccionadosInicial, 0, cotaInicial));
        
        int mejorValor = 0;
        
        while (!cola.isEmpty()) {
            Node actual = cola.poll();
            
            // Si la cota es menor que el mejor valor, podar
            if (actual.cotaSuperior <= mejorValor) {
                continue;
            }
            
            // Si es nodo hoja, actualizar mejor valor
            if (actual.nivel == n) {
                if (actual.valorActual > mejorValor) {
                    mejorValor = actual.valorActual;
                }
                continue;
            }
            
            // Generar hijos
            
            // Hijo 1: No seleccionar hospital actual
            double cotaNoSeleccionar = calcularCota(actual.nivel + 1, actual.valorActual, sumaAcumulada);
            if (cotaNoSeleccionar > mejorValor) {
                cola.offer(new Node(actual.nivel + 1, actual.seleccionados, 
                                  actual.valorActual, cotaNoSeleccionar));
            }
            
            // Hijo 2: Seleccionar hospital actual (si es valido)
            if (esValidoOptimizado(actual.nivel, actual.seleccionados, xs)) {
                boolean[] nuevosSeleccionados = actual.seleccionados.clone();
                nuevosSeleccionados[actual.nivel] = true;
                int nuevoValor = actual.valorActual + ps[actual.nivel];
                double cotaSeleccionar = calcularCota(actual.nivel + 1, nuevoValor, sumaAcumulada);
                
                if (cotaSeleccionar > mejorValor) {
                    cola.offer(new Node(actual.nivel + 1, nuevosSeleccionados, 
                                      nuevoValor, cotaSeleccionar));
                }
            }
        }
        
        return mejorValor;
    }
    
    //Funcion de cota superior
    // Devuelve el valor actual mas la suma acumulada de beneficios pendientes
     
    private static double calcularCota(int nivel, int valorActual, int[] sumaAcumulada) {
        return valorActual + sumaAcumulada[nivel];
    }
    
    // Validity check optimizado (mismo que en backtracking)
    private static boolean esValidoOptimizado(int i, boolean[] seleccionados, int[] xs) {
        for (int j = i - 1; j >= 0; j--) {
            if (seleccionados[j]) {
                if (xs[i] - xs[j] <= 5) {
                    return false;
                }
                break;
            }
        }
        return true;
    }
}
