
/**
 * Write a description of class GeneradorAleatorio here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grado en Ingeniería Informática
 * @version (Curso 2014-15)
 */
public class GeneradorAleatorio
{
   static long semilla;
 
   public static void iniciar (int n) {
      semilla = n;
   }
   
   public static float generadorLehmer () {
      int a = 16807;        //7^5
      long m = 2147483647L; //2^31 - 1
      semilla = ((long)a * semilla) % m;
      return (float)semilla / (float)m;
   }
   
   public static void Prueba (int sup) {
      for (int i=1; i<=sup; i++) {
         System.out.print (generadorLehmer() + "   ");
      }
      System.out.println ();
   }
   
   public static int natGeneradorLehmer (int sup) {
      int a = 16807;        //7^5
      long m = 2147483647L; //2^31 - 1
      semilla = ((long)a * semilla) % m;
      float num = (float)semilla / (float)m;
      // al reducirse el rango de los números, pueden producirse repeticiones indeseadas,
      // no pareciendo una función generadora de periodo completo
      return (int) (num*sup+1);
   }

   public static void natPrueba (int sup) {
      for (int i=1; i<=sup; i++) {
         System.out.print (natGeneradorLehmer(sup) + "   ");
      }
      System.out.println ();
   }
   
}