package gameoflife.impl;

import gameoflife.util.Square;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static gameoflife.util.MathUtils.getNextLargerPowerOfTwo;

/**
 * User: hawk
 * Date: Dec 13, 2011
 */
public class QuadTree {
    private Rectangle boundingBox = new Rectangle();
    private final Node root;

    public QuadTree(Rectangle boundingBoxToSupport) {
        root = constructRootFromBoundingBox(boundingBoxToSupport);
    }

    public void setCellAlive(int x, int y) {
        boundingBox.add(x, y);
        root.set(x, y);
    }

    public boolean get(int x, int y) {
        return root.get(x, y);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private Node constructRootFromBoundingBox(Rectangle boundingBoxToSupport) {
        assert boundingBoxToSupport.width > 0 && boundingBoxToSupport.height > 0;
        int size = Math.max(boundingBoxToSupport.width, boundingBoxToSupport.height);
        if (size < 8) {
            size = 8;
        } else {
            size = getNextLargerPowerOfTwo(size);
        }
        int centerX = (int) boundingBoxToSupport.getCenterX();
        int centerY = (int) boundingBoxToSupport.getCenterY();
        int x = centerX - size / 2;
        int y = centerY - size / 2;
        final Square square = new Square(x, y, size);
        if (size == 8) {
            return new LeafNode(square);
        } else {
            return new InnerNode(square);
        }
    }

    public Iterable<Point> getCoordinatesOfAliveCells() {
        Set<Point> result = new HashSet<Point>();
        root.collectCoordinatesOfAliveCells(result);
        return result;
    }

    public abstract class Node {
        protected final Square square;

        protected Node(Square square) {
            this.square = square;
        }

        public abstract void set(int x, int y);

        public abstract boolean get(int x, int y);

        int toInnerX(int x) {
            final int r = x - square.x;
            if (r < 0 || r >= square.size) {
                throw new IllegalArgumentException();
            }
            return r;
        }

        int toInnerY(int y) {
            final int r = y - square.y;
            assert r >= 0 && r < square.size;
            return r;
        }

        public abstract void collectCoordinatesOfAliveCells(Set<Point> points);
    }


    public class InnerNode extends Node {
        final Node children[] = new Node[4];

        public InnerNode(Square square) {
            super(square);
        }

        @Override
        public void set(int x, int y) {
            if (!square.contains(x, y)) {
                throw new IllegalArgumentException("The point (" + x + "," + y + ") is not inside " + square);
            }
            final ChildLoc loc = getChildLocation(x, y);
            Node child = children[loc.ordinal()];
            if (child == null) {
                int newSize = square.size / 2;
                assert newSize >= 8;
                int newX = square.x;
                int newY = square.y;
                if (loc == ChildLoc.NE || loc == ChildLoc.SE) {
                    newX += newSize;
                }
                if (loc == ChildLoc.SW || loc == ChildLoc.SE) {
                    newY += newSize;
                }
                Square newSquare = new Square(newX, newY, newSize);
                if (newSize == 8) {
                    child = new LeafNode(newSquare);
                } else {
                    child = new InnerNode(newSquare);
                }
                children[loc.ordinal()] = child;
            }
            child.set(x, y);
        }

        private ChildLoc getChildLocation(int x, int y) {
            int innerX = toInnerX(x);
            int innerY = toInnerY(y);
            boolean east = (innerX >= square.size / 2);
            boolean south = (innerY >= square.size / 2);
            final ChildLoc loc;
            if (south && east) {
                loc = ChildLoc.SE;
            } else if (south && !east) {
                loc = ChildLoc.SW;
            } else if (east) {
                loc = ChildLoc.NE;
            } else {
                loc = ChildLoc.NW;
            }
            return loc;
        }

        @Override
        public boolean get(int x, int y) {
            if (!square.contains(x, y)) {
                return false;
            }
            final ChildLoc loc = getChildLocation(x, y);
            final Node child = children[loc.ordinal()];
            if (child == null) {
                return false;
            }
            return child.get(x, y);
        }

        @Override
        public void collectCoordinatesOfAliveCells(Set<Point> points) {
            for (Node child : children) {
                if (child != null) {
                    child.collectCoordinatesOfAliveCells(points);
                }
            }
        }
    }

    public class LeafNode extends Node {
        private long bits;

        public LeafNode(Square square) {
            super(square);
        }

        @Override
        public void set(int x, int y) {
            int bitIndex = calcBitIndex(x, y);
            bits |= 1L << bitIndex;
        }

        private int calcBitIndex(int x, int y) {
            int innerX = toInnerX(x);
            int innerY = toInnerY(y);
            return innerX * 8 + innerY;
        }

        @Override
        public boolean get(int x, int y) {
            int bitIndex = calcBitIndex(x, y);
            return (bits & (1L << bitIndex)) != 0;
        }

        @Override
        public void collectCoordinatesOfAliveCells(Set<Point> points) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    final int x = square.x + i;
                    final int y = square.y + j;
                    if (get(x, y)) {
                        points.add(new Point(x, y));
                    }
                }
            }
        }
    }

    public enum ChildLoc {
        NW, SW, NE, SE;
    }

}
