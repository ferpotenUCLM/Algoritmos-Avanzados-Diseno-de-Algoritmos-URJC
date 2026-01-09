
/**
 * Write a description of class CopyOfCicloHamiltoniano2 here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grados en Ingeniería Informática e Ingeniería de Computadores
 * @version (Curso 2024-25)
 */

public class CicloHamiltoniano
{
   public static int cicloHamiltonianoLocal1 (int[][]grafo) {
   // versión de ascenso simple, en la que se elige siempre el primer vecino
   //    y se itera hasta que no se encuentra ninguno que mejore
       int n = grafo.length;
       int[] ciclo = crearCicloHamiltonianoSimple (grafo);
       int longitud = calcularLongitud (ciclo, grafo);
       imprimirCiclo (grafo, ciclo); 
       System.out.println (longitud);
       boolean mejora = true;
       int i=0;
       do {
          int dist1 = grafo [ciclo[i]]       [ciclo[(i+1)%n]]
                    + grafo [ciclo[(i+1)%n]] [ciclo[(i+2)%n]]
                    + grafo [ciclo[(i+2)%n]] [ciclo[(i+3)%n]];
          int dist2 = grafo [ciclo[i]]       [ciclo[(i+2)%n]]
                    + grafo [ciclo[(i+2)%n]] [ciclo[(i+1)%n]]
                    + grafo [ciclo[(i+1)%n]] [ciclo[(i+3)%n]];
          if (dist1>dist2) {
              int aux = ciclo[(i+1)%n];
              ciclo[(i+1)%n] = ciclo[(i+2)%n];
              ciclo[(i+2)%n] = aux;
              longitud = longitud - dist1 + dist2;
              imprimirCiclo (grafo, ciclo); 
              System.out.println (longitud);
          } else
              mejora = false;
          i=(i+1)%n;
       } while (mejora);
       return longitud;
   }
   
   public static int cicloHamiltonianoLocal2 (int[][]grafo) {
   // versión de ascenso,
   //    en la que se itera hasta que no se encuentra ningún vecino que mejore
       int n = grafo.length;
       int[] ciclo = crearCicloHamiltonianoSimple (grafo);
       int longitud = calcularLongitud (ciclo, grafo);
       imprimirCiclo (grafo, ciclo); 
       System.out.println (longitud);
       boolean mejora;
       do {
          mejora = false;
          for (int i=0; (i<n) && !mejora; i++) {
              int dist1 = grafo [ciclo[i]]       [ciclo[(i+1)%n]]
                        + grafo [ciclo[(i+1)%n]] [ciclo[(i+2)%n]]
                        + grafo [ciclo[(i+2)%n]] [ciclo[(i+3)%n]];
              int dist2 = grafo [ciclo[i]]       [ciclo[(i+2)%n]]
                        + grafo [ciclo[(i+2)%n]] [ciclo[(i+1)%n]]
                        + grafo [ciclo[(i+1)%n]] [ciclo[(i+3)%n]];
              if (dist1>dist2) {
                  int aux = ciclo[(i+1)%n];
                  ciclo[(i+1)%n] = ciclo[(i+2)%n];
                  ciclo[(i+2)%n] = aux;
                  longitud = longitud - dist1 + dist2;
                  mejora = true;
                  imprimirCiclo (grafo, ciclo); 
                  System.out.println (longitud);
              }
          }
       } while (mejora);
       return longitud;
   }
   
   private static int[] crearCicloHamiltonianoSimple (int[][] grafo) {
   // suponemos que el grafo es completo
   // para un grafo cualquiera, podría crearse con un algoritmo de búsqueda en profundidad
   //    o con algún algoritmo heurístico
      int[] ciclo = new int[grafo.length];
      ciclo[0] = 0;
      for (int i=1; i<grafo.length; i++)
         ciclo[i] = i;
      return ciclo;
   }
   
   private static int calcularLongitud (int[] ciclo, int[][] grafo) {
      int longitud = 0;
      for (int i=0; i<grafo.length-1; i++) {
         longitud += grafo[ciclo[i]][ciclo[i+1]];
      }
      longitud += grafo[ciclo[grafo.length-1]][ciclo[0]];
      return longitud;
   }

   private static void imprimirCiclo (int[][] grafo, int[] ciclo) {
       System.out.print ("Ciclo hamiltoniano, con nodos (distancia): " + ciclo[0]);
       for (int i=1; i<ciclo.length; i++) {
          System.out.print (" (" + grafo[ciclo[i-1]][ciclo[i]] + ") " + ciclo[i]);
       }
       System.out.println (" (" + grafo[ciclo[ciclo.length-1]][ciclo[0]] + ") " + ciclo[0]);
   }

   public static int cicloHamiltonianoProb (int[][]grafo) {
   // versión probabilista del algoritmo local 2
       int n = grafo.length;
       int[] ciclo = crearCicloHamiltonianoProb (grafo);
       int longitud = calcularLongitud (ciclo, grafo);
       imprimirCiclo (grafo, ciclo); 
       System.out.println (longitud);
       boolean mejora;
       do {
          mejora = false;
          for (int i=0; i<n; i++) {
              int dist1 = grafo [ciclo[i]]       [ciclo[(i+1)%n]]
                        + grafo [ciclo[(i+1)%n]] [ciclo[(i+2)%n]]
                        + grafo [ciclo[(i+2)%n]] [ciclo[(i+3)%n]];
              int dist2 = grafo [ciclo[i]]       [ciclo[(i+2)%n]]
                        + grafo [ciclo[(i+2)%n]] [ciclo[(i+1)%n]]
                        + grafo [ciclo[(i+1)%n]] [ciclo[(i+3)%n]];
              if (dist1>dist2) {
                  int aux = ciclo[(i+1)%n];
                  ciclo[(i+1)%n] = ciclo[(i+2)%n];
                  ciclo[(i+2)%n] = aux;
                  longitud = longitud - dist1 + dist2;
                  mejora = true;
                  imprimirCiclo (grafo, ciclo); 
                  System.out.println (longitud);
              }
          }
       } while (mejora);
       return longitud;
   }
   
   private static int[] crearCicloHamiltonianoProb (int[][] grafo) {
   // suponemos que el grafo es completo
   // para un grafo cualquiera, podría crearse con un algoritmo de búsqueda en profundidad
   //    o con algún algoritmo heurístico
      int[] ciclo = new int[grafo.length];
      boolean[] usados = new boolean[grafo.length];
      for (int i=0; i<usados.length; i++)
         usados[i] = false;
      int[] posibles = new int[grafo.length];
      int nPosibles;
      for (int i=0; i<grafo.length; i++) {
         nPosibles = 0;
         for (int j=0; j<grafo.length; j++)
            if (!usados[j]) {
               posibles[nPosibles] = j;
               nPosibles++;
            }
         int nodo = posibles[(int)(Math.random()*nPosibles)];
         ciclo[i] = nodo;
         usados[nodo] = true;
      }
      return ciclo;
   }

   public static int cicloHamiltonianoIterativo (int[][]grafo) {
   // versión iterativa del algoritmo probabilista
       int n = grafo.length;
       int[] mejorCiclo = new int[grafo.length];
       int[] mejorPosicion = new int[grafo.length];
       int mejorLongitud = Integer.MAX_VALUE;
       int N = 5; //número de ciclos de inicio aleatorio; puede variarse arbitrariamente
       for (int k=0; k<N; k++) {
          int[] ciclo = crearCicloHamiltonianoProb (grafo);
          int longitud = calcularLongitud (ciclo, grafo);
          imprimirCiclo (grafo, ciclo); 
          System.out.println (longitud);
          boolean mejora;
          do {
             mejora = false;
             for (int i=0; i<n; i++) {
                 int dist1 = grafo [ciclo[i]]       [ciclo[(i+1)%n]]
                           + grafo [ciclo[(i+1)%n]] [ciclo[(i+2)%n]]
                           + grafo [ciclo[(i+2)%n]] [ciclo[(i+3)%n]];
                 int dist2 = grafo [ciclo[i]]       [ciclo[(i+2)%n]]
                           + grafo [ciclo[(i+2)%n]] [ciclo[(i+1)%n]]
                           + grafo [ciclo[(i+1)%n]] [ciclo[(i+3)%n]];
                 if (dist1>dist2) {
                     int aux = ciclo[(i+1)%n];
                     ciclo[(i+1)%n] = ciclo[(i+2)%n];
                     ciclo[(i+2)%n] = aux;
                     longitud = longitud - dist1 + dist2;
                     mejora = true;
                     imprimirCiclo (grafo, ciclo); 
                     System.out.println (longitud);
                 }
             }
          } while (mejora);
          System.out.println ();
          if (longitud < mejorLongitud) {
             for (int i=0; i<grafo.length; i++)
                mejorCiclo[i] = ciclo[i];
             mejorLongitud = longitud;
          }
       } 
       return mejorLongitud;
   }

   public static int cicloHamiltonianoEstocastico (int[][]grafo) {
   // versión estocástica del algoritmo probabilista
       int n = grafo.length;
       int[] ciclo = crearCicloHamiltonianoProb (grafo);
       int longitud = calcularLongitud (ciclo, grafo);
       imprimirCiclo (grafo, ciclo); 
       System.out.println (longitud);
       int t = 50; //acepta el cambio la mitad de las veces aprox.
                   //con t==1, sólo cambia si hay mejora
       for (int i=0; i<10; i++) {
           int j = NumAleatorios.intRandom (1,n-1);
           int dist1 = grafo [ciclo[j]]       [ciclo[(j+1)%n]]
                     + grafo [ciclo[(j+1)%n]] [ciclo[(j+2)%n]]
                     + grafo [ciclo[(j+2)%n]] [ciclo[(j+3)%n]];
           int dist2 = grafo [ciclo[j]]       [ciclo[(j+2)%n]]
                     + grafo [ciclo[(j+2)%n]] [ciclo[(j+1)%n]]
                     + grafo [ciclo[(j+1)%n]] [ciclo[(j+3)%n]];
           if ((dist1>dist2) || (Math.random()<(1/(1+Math.pow(2.71828,(dist2-dist1)/t))))) {
              int aux = ciclo[(j+1)%n];
              ciclo[(j+1)%n] = ciclo[(j+2)%n];
              ciclo[(j+2)%n] = aux;
              longitud = longitud - dist1 + dist2;
              imprimirCiclo (grafo, ciclo); 
              System.out.println (longitud);
           }
       }
       return longitud;
   }
   
   public static int cicloHamiltonianoRecocido (int[][]grafo) {
   // versión de recocido simulado del algoritmo probabilista
       int n = grafo.length;
       int t = 50; //acepta el cambio la mitad de las veces aprox; con t==1, sólo cambia si hay mejora
       System.out.println ("temperatura inicial = " + t);
       int[] ciclo = crearCicloHamiltonianoProb (grafo);
       int longitud = calcularLongitud (ciclo, grafo);
       imprimirCiclo (grafo, ciclo); 
       System.out.println (longitud);
       boolean mejora;
       do {
          mejora = false;
          for (int i=0; (i<10) || !mejora; i++) {
              int j = NumAleatorios.intRandom (1,n-1);
              int dist1 = grafo [ciclo[j]]       [ciclo[(j+1)%n]]
                        + grafo [ciclo[(j+1)%n]] [ciclo[(j+2)%n]]
                        + grafo [ciclo[(j+2)%n]] [ciclo[(j+3)%n]];
              int dist2 = grafo [ciclo[j]]       [ciclo[(j+2)%n]]
                        + grafo [ciclo[(j+2)%n]] [ciclo[(j+1)%n]]
                        + grafo [ciclo[(j+1)%n]] [ciclo[(j+3)%n]];
              if ((dist1>dist2) || (Math.random()<(1/(1+Math.pow(2.71828,(dist2-dist1)/t))))) {
                 int aux = ciclo[(j+1)%n];
                 ciclo[(j+1)%n] = ciclo[(j+2)%n];
                 ciclo[(j+2)%n] = aux;
                 longitud = longitud - dist1 + dist2;
                 mejora = true;
                 imprimirCiclo (grafo, ciclo); 
                 System.out.println (longitud);
              }
          }
          t = t / 2;
          System.out.println ("cambio de temperatura a " + t);
       } while (t>=1);
       return longitud;
   }
   
}
