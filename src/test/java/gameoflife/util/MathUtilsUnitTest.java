package gameoflife.util;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: hawk
 * Date: Dec 14, 2011
 */
public class MathUtilsUnitTest {
    @Test
    public void testGetNextLargerPowerOfTwo() throws Exception {
        assertThat(MathUtils.getNextLargerPowerOfTwo(3)).isEqualTo(4);
        assertThat(MathUtils.getNextLargerPowerOfTwo(4)).isEqualTo(4);
        assertThat(MathUtils.getNextLargerPowerOfTwo(5)).isEqualTo(8);
        assertThat(MathUtils.getNextLargerPowerOfTwo(0x40000000)).isEqualTo(0x40000000);
    }
}
