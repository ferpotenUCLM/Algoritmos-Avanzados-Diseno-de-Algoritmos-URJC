
/**
 * Write a description of class NumAleatorios here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grado en Ingeniería Informática
 * @version (Curso 2014-15)
 */
public class NumAleatorios
{

   public static double realRandom (double inf, double sup) {
      return (Math.random()*(sup-inf)+inf);
   }

   public static int intRandom (int inf, int sup) {
      return (int) realRandom(inf,sup+1);
   }

   public static int natRandom (int sup) {
      return intRandom(1,sup);
   }

   public static boolean booleanRandom () {
      return intRandom(0,1)==0;
   }

   public static int[] sorteoConRepes (int n) {
      int[] nums = new int[n];
      for (int i=0; i<n; i++) {
         nums[i] = natRandom(n);
         System.out.print (nums[i] + "   ");
      }   
      System.out.println ();
      return nums;
   }

      public static int[] sorteoSinRepes (int n) {
      boolean[] usados = new boolean[n];
      for (int i=0; i<n; i++)
         usados[i] = false;
      int[] nums = new int[n];
      int i=0;
      while (i<n) {
         int sig = natRandom(n);
         if (!usados[sig-1]) {
            nums[i] = sig;
            System.out.print (nums[i] + "   ");
            usados[sig-1] = true;
            i++;
         }
      }
      System.out.println ();
      return nums;
   }
   
}
