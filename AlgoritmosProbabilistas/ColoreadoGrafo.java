 /**
 * Write a description of class ColoreadoGrafo here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grados en Ingeniería Informática e Ingeniería de Computadores
 * @version (Curso 2023-24)
 */
public class ColoreadoGrafo
{
   public static int coloreadoGrafoH1 (int[][] grafo) {
      int num = 0;
      int[] colores = new int[grafo.length];
      colores[0] = 0;
      for (int i=1; i<grafo.length; i++) {
         boolean usado = true;
         int c;
         for (c=0; (c<=num) && usado; c++) {
            usado = false;
            for (int j=0; (j<i) && !usado; j++)
               if (grafo[i][j]==1)
                  usado = (colores[j]==c);
         }
         if (usado) {
            num++;
            colores[i] = num;
         } else
            colores[i] = c-1;
      }
      imprimirGrafoColoreado (num+1, colores);
      return num+1;
   }
   
   private static void imprimirGrafoColoreado (int n, int[] color) {
      System.out.print ("Nodos tomados en el orden de los números naturales: ");
      System.out.println ("El grafo se ha coloreado con " + n + " colores de la siguiente forma:");
      for (int i=0; i<color.length; i++)
         System.out.println ("El nodo " + i + " tiene el color " + color[i]);
      System.out.println ();
   }

   public static int coloreadoGrafoProb (int[][] grafo) {
      int[] is = generarIndicesAlea(grafo.length);
      int num = 0;
      int[] colores = new int[grafo.length];
      colores[is[0]] = 0;
      for (int i=1; i<grafo.length; i++) {
         boolean usado = true;
         int c;
         for (c=0; (c<=num) && usado; c++) {
            usado = false;
            for (int j=0; (j<i) && !usado; j++)
               if (grafo[is[i]][is[j]]==1)
                  usado = (colores[is[j]]==c);
         }
         if (usado) {
            num++;
            colores[is[i]] = num;
         } else
            colores[is[i]] = c-1;
      }
      imprimirGrafoColoreadoOrden (num+1, colores, is); //falta imprimir el orden de los nodos
      return num+1;
   }
   

   public static int[] generarIndicesAlea (int n) {
      int[] orden = NumAleatorios.sorteoSinRepes (n);
      for (int i=0; i<n; i++)
         orden[i]--;
      return orden;
   }

   public static int coloreadoGrafoIter (int[][] grafo) {
      int numMin = Integer.MAX_VALUE;
      int[] coloresMin = new int[grafo.length];
      int[] isMin = new int[grafo.length];
      for (int n=0; n<10; n++) { //número arbitrario de repeticiones)
         int[] is = generarIndicesAlea(grafo.length);
         int num = 0;
         int[] colores = new int[grafo.length];
         colores[is[0]] = 0;
         for (int i=1; i<grafo.length; i++) {
            boolean usado = true;
            int c;
            for (c=0; (c<=num) && usado; c++) {
               usado = false;
               for (int j=0; (j<i) && !usado; j++)
                  if (grafo[is[i]][is[j]]==1)
                     usado = (colores[is[j]]==c);
            }
            if (usado) {
               num++;
               colores[is[i]] = num;
            } else
               colores[is[i]] = c-1;
         }
         imprimirGrafoColoreadoOrden (num+1, colores, is);
         if (num<numMin) {
            numMin = num;
            for (int i=0; i<colores.length; i++) {
               coloresMin[i] = colores[i];
               isMin[i] = is[i];
            }
         }
      }
      System.out.println ("Solución mejor obtenida:");
      imprimirGrafoColoreadoOrden (numMin+1, coloresMin, isMin);
      return numMin+1;
   }
   
   private static void imprimirGrafoColoreadoOrden (int n, int[] color, int[] orden) {
      System.out.print ("Nodos tomados en el siguiente orden: ");
      for (int i=0; i<color.length-1; i++)
         System.out.print (orden[i] + ", ");
      System.out.println (orden[color.length-1]);
      System.out.println ("El grafo se ha coloreado con " + n + " colores de la siguiente forma:");
      for (int i=0; i<color.length; i++)
         System.out.println ("El nodo " + i + " tiene el color " + color[i]);
      System.out.println ();
   }

}