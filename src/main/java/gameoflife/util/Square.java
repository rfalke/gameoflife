package gameoflife.util;

/**
 * User: hawk
 * Date: Dec 13, 2011
 */
public class Square {
    public int x, y, size;

    public Square(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public String toString() {
        return "Square{" +
                "x=" + x +
                ", y=" + y +
                ", size=" + size +
                '}';
    }
}
