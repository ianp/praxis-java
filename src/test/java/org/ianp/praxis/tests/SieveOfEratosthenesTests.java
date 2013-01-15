// Copyright © 2013 Ian Phillips.
// See the accompanying LICENSE.md file for further information.
package org.ianp.praxis.tests;

import org.ianp.praxis.SieveOfEratosthenes;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SieveOfEratosthenesTests {

    @Test
    public void testSieve30() {
        assertThat("sieve 30", SieveOfEratosthenes.sieve(30).cardinality(), is(10));
    }

    @Test
    public void testSieve15485863() {
        assertThat("sieve 15485863 ", SieveOfEratosthenes.sieve(15485863).cardinality(), is(1000000));
    }

}
