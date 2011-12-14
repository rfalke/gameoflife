package gameoflife.impl;

import gameoflife.GameOfLife;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: hawk
 * Date: Dec 14, 2011
 */
public class RaimarsNonCachingQuadTreeGameOfLife implements GameOfLife {
    private QuadTree quadTree;
    private static final int NEIGHBOUR_COUNT = 8;

    static {
        try {
            assert false;
            //noinspection UseOfSystemOutOrSystemErr
            System.err.println("Please enable assertions.");
            System.exit(1);
        } catch (Throwable t) {
            // ignored
        }
    }

    public RaimarsNonCachingQuadTreeGameOfLife(Rectangle boundingBoxToSupport) {
        quadTree = new QuadTree(boundingBoxToSupport);
    }

    public void setCellAlive(int x, int y) {
        quadTree.setCellAlive(x, y);
    }

    public void calculateNextGeneration() {
        final QuadTree nextGeneration = new QuadTree(extendBoundingBox(quadTree.getBoundingBox()));
        Map<Point, Integer> _calculatedDeadCells = new HashMap<Point, Integer>();
        for (Point eachLivingCell : quadTree.getCoordinatesOfAliveCells()) {

            final java.util.List<Point> deadNeighbours = getDeadNeighbours(eachLivingCell);
            final int numberOfLivingNeighbours = NEIGHBOUR_COUNT - deadNeighbours.size();
            if (numberOfLivingNeighbours == 2 || numberOfLivingNeighbours == 3) {
                nextGeneration.setCellAlive(eachLivingCell.x, eachLivingCell.y);
            }
            for (Point deadNeighbour : deadNeighbours) {
                Integer numberOfAliveNeighbours = _calculatedDeadCells.get(deadNeighbour);
                if (numberOfAliveNeighbours == null) {
                    numberOfAliveNeighbours = 0;
                }
                numberOfAliveNeighbours++;
                _calculatedDeadCells.put(deadNeighbour, numberOfAliveNeighbours);
            }
        }
        for (Map.Entry<Point, Integer> pointIntegerEntry : _calculatedDeadCells.entrySet()) {
            final Point deadCell = pointIntegerEntry.getKey();
            final Integer numberOfNeighbours = pointIntegerEntry.getValue();
            if (numberOfNeighbours == 3) {
                nextGeneration.setCellAlive(deadCell.x, deadCell.y);
            }
        }
        quadTree = nextGeneration;
    }

    private List<Point> getDeadNeighbours(Point cell) {
        final List<Point> result = new ArrayList<Point>();
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if (i != 0 || j != 0) {
                    final int x = cell.x + i;
                    final int y = cell.y + j;
                    if (!quadTree.get(x, y)) {
                        final Point neighbour = new Point(x, y);
                        result.add(neighbour);
                    }
                }
            }
        }
        return result;
    }

    private Rectangle extendBoundingBox(Rectangle box) {
        Rectangle result = new Rectangle(box);
        result.x -= 2;
        result.y -= 2;
        result.width += 4;
        result.height += 4;
        return result;
    }

    public Iterable<Point> getCoordinatesOfAliveCells() {
        return quadTree.getCoordinatesOfAliveCells();
    }
}
