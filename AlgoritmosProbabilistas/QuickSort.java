
/**
 * Brevísima descripción de la clase Ordenar
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grado en Ingeniería Informática
 * @version (Curso 2014-15)
 */
public class QuickSort
{
  
   public static void quickSort (int[] v) {
      System.out.print ("Vector a ordenar: "); imprimir (v);
      qSort (v, 0, v.length-1);
      System.out.print ("Vector ordenado: "); imprimir (v); System.out.println();
   }
   private static void qSort (int[] v, int inf, int sup) {
      System.out.println ("Llamada con límites: "+inf+" y "+sup);
      if (inf<sup) {
         int med = partir (v, inf, sup);
         System.out.println ("Pivote "+v[med]+" en el índice "+med);
         if (inf<med)
            qSort (v, inf, med-1);
         if (med<sup)
            qSort (v, med+1, sup);
      }
   }
   private static int partir (int[] v, int inf, int sup) {
      int piv  = v[inf];
      int izq  = inf+1;
      int dcha = sup;
      int temp;
      do {
         for (; v[izq]<=piv  && izq<sup   ; izq++);
         for (; v[dcha]>piv /*&&dcha>inf*/; dcha--);
         if (izq<dcha) {
            temp = v[izq];
            v[izq] = v[dcha];
            v[dcha] = temp;
         }
      } while (izq<dcha);
      /* v[inf] <-> v[dcha] */
      temp = v[inf];
      v[inf] = v[dcha];
      v[dcha] = temp;
      return dcha;
   }
   
   public static void quickSortProb (int[] v) {
      System.out.print ("Vector a ordenar: "); imprimir (v);
      qsProb (v, 0, v.length-1);
      System.out.print ("Vector ordenado: "); imprimir (v); System.out.println();
   }
   private static void qsProb (int[] v, int inf, int sup) {
      System.out.println ("Llamada con límites: "+inf+" y "+sup);
      if (sup-inf<3) { // algoritmo sencillo (de la burbuja) para subvector de 3 o menos elementos
         for (int i=inf; i<sup; i++)
           for (int j=sup; j>i; j--)
               if (v[j-1]>v[j]) {
                  int aux = v[j-1];
                  v[j-1] = v[j];
                  v[j] = aux;   
               }
      }
      else {
         //genera un pivote que sea un elemento central
         int margen = (sup-inf+1)/4;
         int med;
         boolean central;
         do {
            med = partirProb (v, inf, sup);
            central = (med>=inf+margen) && (med<=sup-margen);
            if (central)
               System.out.println ("Éxito: tomado pivote "+v[med]+" en la posición "+med);
            else
               System.out.println ("Fallo: tomado pivote "+v[med]+" en la posición "+med);
         }
         while (!central);
         //llamadas recursivas
         if (inf<med)
            qsProb (v, inf, med-1);
         if (med<sup)
            qsProb (v, med+1, sup);
      }
   }
   private static int partirProb (int[] v, int inf, int sup) {
      //elige un elemento aleatoriamente, desplaza los que están a su izquierda una posición a su derecha,
      //dejando el pivote a la izquierda de todos
      int iPiv = NumAleatorios.intRandom(inf,sup);
      int piv = v[iPiv];
      for (int i=iPiv; i>inf; i--)
         v[i] = v[i-1];
      v[inf] = piv;
      //partición del vector en dos partes
      return partir (v, inf, sup);
   }

   private static void imprimir (int[] v) {
      for (int i=0; i<v.length; i++)
         System.out.print (v[i]+" ");
      System.out.println();
   }

}
