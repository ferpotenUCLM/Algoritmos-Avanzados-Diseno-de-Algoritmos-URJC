
/**
 * Write a description of class EquilibradoCarga here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grados en Ingeniería Informática e Ingeniería de Computadores
 * @version (Curso 2024-25)
 */
public class EquilibradoCarga
{
   public static int equilibrarCargasAprox1 (int m, int[] ts) {
   // Cada tarea se asigna a la máquina que tiene menos carga
      // máquina asignada a cada tarea i
      int[] as = new int[ts.length];
      for (int i=0; i<as.length; i++)
         as[i] = -1;
      // tiempo acumulado por cada máquina
      int[] tms = new int[m];
      for (int i=0; i<m; i++)
         tms[i] = 0;
      // algoritmo aproximado
      for (int i=0; i<ts.length; i++) {
         int min = 0;
         for (int j=1; j<m; j++)
            if (tms[j]<tms[min])
               min = j;
         as[i] = min;
         tms[min] += ts[i];
      }
      int max = 0;
      for (int i=1; i<m; i++)
         if (tms[i]>tms[max])
            max = i;
      return tms[max];
   }

   public static int equilibrarCargasAprox1Prob (int m, int[] ts) {
   // Cada tarea se asigna a una máquina de forma aleatoria
      // máquina asignada a cada tarea i
      int[] as = new int[ts.length];
      for (int i=0; i<as.length; i++)
         as[i] = -1;
      // tiempo acumulado por cada máquina
      int[] tms = new int[m];
      for (int i=0; i<m; i++)
         tms[i] = 0;
      // algoritmo probabilista
      for (int i=0; i<ts.length; i++) {
         int proc = NumAleatorios.intRandom (0,m-1);
         as[i] = proc;
         tms[proc] += ts[i];
      }
      int max = 0;
      for (int i=0; i<m; i++)
         if (tms[i]>tms[max])
            max = i;
      return tms[max];
   }

}
