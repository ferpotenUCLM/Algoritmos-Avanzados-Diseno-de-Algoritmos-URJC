
/**
 * Write a description of class Seleccionar here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grado en Ingeniería Informática
 * @version (Curso 2014-15)
 */
public class Seleccionar
{
   public static int seleccionar (int[] v, int k) {
      return select (v, 0, v.length-1, k);
   }
   private static int select (int[] v, int inf, int sup, int k) {
      int med = partir (v, inf, sup);
      if (med==inf+k)
         return v[med];
      else if (med<inf+k)
         return select (v, med+1, sup, k-(med-inf+1));
      else // med>inf+k
         return select (v, inf, med, k);
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

   public static int seleccionarProb (int[] v, int k) {
      return selProb (v, 0, v.length-1, k);
   }
   private static int selProb (int[] v, int inf, int sup, int k) {
      //genera un pivote que sea un elemento central
      int margen = (sup-inf+1)/4;
      int med;
      do {
         med = partirProb (v, inf, sup);
      }
      while ((med-inf<margen) || (sup-med<margen));
      if (med==inf+k)
         return v[med];
      else if (med<inf+k)
         return selProb (v, med+1, sup, k-(med-inf+1));
      else // med>inf+k
         return selProb (v, inf, med, k);
   }
   private static int partirProb (int[] v, int inf, int sup) {
      //elige un elemento aleatoriamente, desplaza los que están a su izquierda una posición y a su derecha,
      //dejando el pivote a la izquierda de todos
      int iPiv = NumAleatorios.intRandom(inf,sup);
      int piv = v[iPiv];
      for (int i=iPiv; i>inf; i--)
         v[i] = v[i-1];
      v[inf] = piv;
      //partición del vector en dos partes
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
}
