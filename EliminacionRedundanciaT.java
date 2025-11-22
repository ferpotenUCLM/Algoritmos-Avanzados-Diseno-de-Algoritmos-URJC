public class EliminacionRedundanciaT {

    /* ----------------- MEMORIZACION (recursiva) ----------------- */
    public static int fMem(int x) {
        int[][] t = new int[x+1][2];
        for (int i = 0; i <= x; i++) {
            t[i][0] = -1;
            t[i][1] = -1;
        }
        return gMem(x, 0, t);
    }

    private static int gMem(int x, int y, int[][] t) {
        if (t[x][y] != -1) return t[x][y];
        if (x == 0) t[x][y] = 0;
        else if (x == 1) t[x][y] = y;
        else if (y == 0) t[x][y] = gMem(x-1, 0, t) + gMem(x, 1, t);
        else /* y == 1 */ t[x][y] = gMem(x-2, 0, t) + gMem(x-1, 1, t);
        return t[x][y];
    }

    /* ----------------- TABULACION BASICA (iterativa) ----------------- */
    public static int fTab(int x) {
        if (x == 0) return 0;
        if (x == 1) return 0;
        int[][] t = new int[x+1][2];
        t[0][0] = 0; t[0][1] = 0;
        t[1][0] = 0; t[1][1] = 1;
        for (int i = 2; i <= x; i++) {
            t[i][1] = t[i-2][0] + t[i-1][1];
            t[i][0] = t[i-1][0] + t[i][1];
        }
        return t[x][0];
    }

    /* ----------------- TABULACION OPTIMIZADA (iterativa, O(1) memoria) ----------------- */
    public static int fTabOpt(int x) {
        if (x == 0) return 0;
        if (x == 1) return 0;
        int prev2_y0 = 0;  // t[i-2][0]
        int prev1_y0 = 0;  // t[i-1][0]
        int prev1_y1 = 1;  // t[i-1][1]
        int current_y0 = 0, current_y1 = 0;
        for (int i = 2; i <= x; i++) {
            current_y1 = prev2_y0 + prev1_y1;
            current_y0 = prev1_y0 + current_y1;
            prev2_y0 = prev1_y0;
            prev1_y0 = current_y0;
            prev1_y1 = current_y1;
        }
        return current_y0;
    }

    public static int fTabParaSRec(int x) {
        // simple wrapper: usa memorizaciOn (gMem) para que SRec vea la recursiOn
        return fMem(x);
    }

    public static int fTabOptParaSRec(int x) {
        // idem: si quieres ver la "misma" computaciOn pero con trazado de recursiOn
        return fMem(x);
    }

}
