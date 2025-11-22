public class EliminacionRedundancia {

    public static int f(int x) {
        // Crear tabla de memorización inicializada con -1
        int[][] t = new int[x+1][2];
        for (int i = 0; i <= x; i++) {
            t[i][0] = -1;
            t[i][1] = -1;
        }
        return gMem(x, 0, t);
    }
    
    private static int gMem(int x, int y, int[][] t) {
        // Si ya está calculado, devolver valor almacenado
        if (t[x][y] != -1) {
            return t[x][y];
        }
        
        // Casos base
        if (x == 0) {
            t[x][y] = 0;
        } else if (x == 1) {
            t[x][y] = y;
        } 
        // Casos recursivos
        else if (y == 0) {
            t[x][y] = gMem(x-1, 0, t) + gMem(x, 1, t);
        } else { // y == 1
            t[x][y] = gMem(x-2, 0, t) + gMem(x-1, 1, t);
        }
        
        return t[x][y];
    }
}