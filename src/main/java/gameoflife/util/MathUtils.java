package gameoflife.util;

/**
 * User: hawk
 * Date: Dec 13, 2011
 */
public class MathUtils {
    public static int getNextLargerPowerOfTwo(int x) {
        int v = x;
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        v++;

        int r = v;
        assert r >= x : "x=" + x + " r=" + r;
        return r;
    }    
}
