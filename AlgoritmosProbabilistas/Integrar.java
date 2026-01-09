
/**
 * Write a description of class Integrales here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grado en Ingeniería Informática
 * @version (Curso 2014-15)
 */
public class Integrar
{

   public static float integrarDeter (int n, int a, int b) {
      float suma = 0.0F;
      float delta = ((float)(b-a))/n;
      float x = a + delta/2;
      for (int i=1; i<=n; i++) {
         suma += f(x);
         System.out.println ("x = " + x);
         x += delta;
      }
      float integral = suma*delta; // suma*(b-a)/n
      System.out.println ("Valor determinista estimado de la integral: " + integral);
      System.out.println ();
      return integral;
   }
   
   public static float integrarProb (int n, int a, int b) {
      float suma = 0.0F;
      for (int i=1; i<=n; i++) {
         float x = (float) NumAleatorios.realRandom (a, b);
         System.out.println ("x = " + x);
         suma += f(x);
      }
      float integral = suma*(b-a)/n;
      System.out.println ("Valor probabilista estimado de la integral: " + integral);
      System.out.println ();
      return integral;
   }
   
   public static void prueba (int n, int a, int b) {
      float f = integrarDeter (n,a,b);
      for (int i=0; i<10; i++)
         f = integrarProb (n,a,b);
   }
   
   private static float f (float x) {
   // función de ejemplo; puede cambiarse por cualquier otra
      return x*x;
   }
   
}
