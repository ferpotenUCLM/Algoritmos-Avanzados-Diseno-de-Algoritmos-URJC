
/**
 * Write a description of class N_reinas here.
 * 
 * @author (Ángel Velázquez Iturbide, Departamento de Informática y Estadística, Universidad Rey Juan Carlos)
 * Asignatura: Algoritmos Avanzados, Grado en Ingeniería Informática
 * @version (Curso 2014-15)
 */
public class N_reinas
{
   public static boolean n_reinasDeterminista (int n) {
      int[] c = new int[n];
      boolean[] f = new boolean[n];
      for (int i=0; i<n; i++)
         f[i] = true;
      boolean[] dp = new boolean[2*n-1];
      for (int i=0; i<2*n-1; i++)
         dp[i] = true;
      boolean[] ds = new boolean[2*n-1];
      for (int i=0; i<2*n-1; i++)
         ds[i] = true;
      boolean exito = buscarReinas (n-1, 0, c, f, dp, ds);
      if (exito)
         System.out.print ("Solución correcta: ");
      else
         System.out.print ("Solución fallida: ");
      imprimir (c);
      return exito;
   }
   private static boolean buscarReinas (int n_1,
                                        int i,
                                        int[] solucion,
                                        boolean[] f,
                                        boolean[] dp,
                                        boolean[] ds) {
      boolean exito = false;
      for (int j=0; j<=n_1 && !exito; j++)
         if (f[j] && dp[i-j+n_1] && ds[i+j]) {
            solucion[i] = j;
            f[j] = false;
            dp[i-j+n_1] = false;
            ds[i+j] = false;
            if (i==n_1)
               exito = true;
            else {
               exito = buscarReinas(n_1, i+1, solucion, f, dp, ds);
               if (!exito) {
                  f[j] = true;
                  dp[i-j+n_1] = true;
                  ds[i+j] = true;
               }
            }
         }
      return exito;
   }

   public static boolean n_reinasProbabilistaInseguro (int n) {
      int[] solucion = new int[n];
      for (int i=0; i<n; i++)
         solucion[i] = -1;
      boolean[] f = new boolean[n];
      for (int i=0; i<n; i++)
         f[i] = true;
      boolean[] dp = new boolean[2*n-1];
      for (int i=0; i<2*n-1; i++)
         dp[i] = true;
      boolean[] ds = new boolean[2*n-1];
      for (int i=0; i<2*n-1; i++)
         ds[i] = true;
      int nPosibles;
      int posibles[] = new int[n];
      boolean exito = true;
      for (int i=0; (i<n) && exito; i++) {
         nPosibles = 0;
         for (int j=0; j<n; j++)
            if (f[j] && dp[i-j+n-1] && ds[i+j]) {
               posibles[nPosibles] = j;
               nPosibles++;
            }
         if (nPosibles==0)
            exito = false;
         else {
            int k = posibles[(int)(Math.random()*nPosibles)];
            solucion[i] = k;
            f[k] = false;
            dp[i-k+n-1] = false;
            ds[i+k] = false;
         }
      }
      if (exito)
         System.out.print ("Solución correcta: ");
      else
         System.out.print ("Solución fallida: ");
      imprimir(solucion);
      return (exito);
   }


   public static void n_reinasProbabilistaSeguro (int n) {
      do { }
      while (!n_reinasProbabilistaInseguro (n));
   }

   public static boolean n_reinasProbabilistaMixtoInseguro (int n) {
      int[] solucion = new int[n];
      for (int i=0; i<n; i++)
         solucion[i] = -1;
      boolean[] f = new boolean[n];
      for (int i=0; i<n; i++)
         f[i] = true;
      boolean[] dp = new boolean[2*n-1];
      for (int i=0; i<2*n-1; i++)
         dp[i] = true;
      boolean[] ds = new boolean[2*n-1];
      for (int i=0; i<2*n-1; i++)
         ds[i] = true;
      int nPosibles;
      int posibles[] = new int[n];
      boolean exito = true;
      // límite arbitrario: puede cambiarse
      int limite = 3;// n/2; //(int)0.9*n;
      for (int i=0; i<limite && exito; i++) {
         nPosibles = 0;
         for (int j=0; j<n; j++)
            if (f[j] && dp[i-j+n-1] && ds[i+j]) {
               posibles[nPosibles] = j;
               nPosibles++;
            }
         if (nPosibles==0)
            exito = false;
         else {
            int k = posibles[(int)(Math.random()*nPosibles)];
            solucion[i] = k;
            f[k] = false;
            dp[i-k+n-1] = false;
            ds[i+k] = false;
         }
      }
      exito = buscarReinas (n-1, limite, solucion, f, dp, ds);
      if (exito)
         System.out.print ("Solución correcta: ");
      else
         System.out.print ("Solución fallida: ");
      imprimir(solucion);
      return exito;
   }

   public static void n_reinasProbabilistaMixtoSeguro (int n) {
     do { }
     while (!n_reinasProbabilistaMixtoInseguro (n));
   }


   private static void imprimir (int[] v) {
      for (int i=0; i<v.length; i++)
         System.out.print (((v[i]==-1)?"-":v[i])+" ");
      System.out.println();
      System.out.println();
   }
}