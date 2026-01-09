
/**
 * Write a description of class SiPrimos here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grados en Ingeniería Informática e Ingneiería de Computadores
 * @version (Curso 2022-23)
 */
public class SiPrimos
{
   public static boolean esPrimoFermat (int n) {
      int a = NumAleatorios.intRandom (1, n-1);
      return expomod(a,n-1,n) == 1;
   }
   
   private static int expomod (int a, int n, int z) {
      int i = n; int r = 1; int x = a % z;
      while (i>0) {
         if (i%2==1)
            r = r*x % z;
         x = x*x % z;
         i = i/2;
      }
      return r;
   }
   
   public static boolean esPrimoMillerRabin (int n) {
   // sólo be llamarse para n>4 impar 
      int a = NumAleatorios.intRandom (2, n-2);
      return pruebaB(a,n);
   }

   private static boolean pruebaB (int a, int n) {
      int s = 0; int t = n-1;
      do {
         s++;
         t = t/2;
      } while (t%2==0);
      int x = expomod(a,t,n);
      if ((x==1)||(x==n-1))
         return true;
      for (int i=1; i<=s-1; i++) {
         x = x*x % n;
         if (x==n-1)
            return true;
      }
      return false;
   }
}
