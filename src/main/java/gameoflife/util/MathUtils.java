package gameoflife.util;

/**
 * User: hawk
 * Date: Dec 13, 2011
 */
public class MathUtils {
    public static int getNextLargerPowerOfTwo(int x) {
        assert x <= 0x40000000 : "Value too large.";
        for (int bit = 0; bit < 31; bit++) {
            int v = 1 << bit;
            if (x == v) {
                return v;
            }
            if (v > x) {
                assert v >= 0 && v >= x : "x=" + x + " result=" + v;
                return v;
            }
        }
        throw new RuntimeException(String.format("Problem with %d = 0x%08x", x, x));
    }
}
