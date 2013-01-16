// Copyright © 2013 Ian Phillips.
// See the accompanying LICENSE.md file for further information.
package org.ianp.praxis;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.ianp.praxis.BingoOdds.combinations;
import static org.ianp.praxis.BingoOdds.d;
import static org.ianp.praxis.BingoOdds.fact;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link BingoOdds}.
 */
public class BingoOddsTests {

    @Test
    public void testFactorials() {
        assertThat(fact(d(0)), is(d(0)));
        assertThat(fact(d(1)), is(d(1)));
        assertThat(fact(d(2)), is(d(2)));
        assertThat(fact(d(4)), is(d(24)));
    }

    @Test
    public void testCombinations() {
        assertThat(combinations(52, 5), is(d(2598960)));
    }

//    @Test
//    public void testCallsToHits() {
//        assertThat(callsToHits(52, 5), is(d(2598960)));
//    }

}
