package gameoflife.impl;

import org.junit.Test;

import java.awt.*;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: hawk
 * Date: Dec 13, 2011
 */
public class QuadTreeUnitTest {

    @Test
    public void testSetAndGet() throws Exception {
        QuadTree tree = new QuadTree(new Rectangle(-10, -10, 20, 20));
        assertThat(tree.get(2, 1)).isFalse();
        tree.setCellAlive(2, 1);
        assertThat(tree.get(2, 1)).isTrue();
    }

}
