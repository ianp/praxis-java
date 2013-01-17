// Copyright © 2013 Ian Phillips.
// See the accompanying LICENSE.md file for further information.
package org.ianp.praxis;

import java.math.BigDecimal;
import java.math.MathContext;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

/**
 * Bingo probablities.
 * <p>
 * A solution to <a href="http://programmingpraxis.com/2009/02/19/bingo/">this problem</a>.
 * Full documentation in <a href="http://ianp.org/2013/01/17/praxis-winning-at-bingo/">this blog post</a>.
 */
public class BingoOdds {

    private static final int NUMBERS = 75;
    private static final int SQUARES = 24;

    private static final int[] BINGOS = {
            //012345678901234567890123    012345678901234567890123
            0b111110000000000000000000, 0b000001111100000000000000,
            0b000000000011110000000000, 0b000000000000001111100000,
            0b000000000000000000011111, 0b100001000010001000010000,
            0b010000100001000100001000, 0b001000010000000010000100,
            0b000100001000010001000010, 0b000010000100010000100001,
            0b100000100000000001000001, 0b000010001000000100010000
    };

    private static final MathContext DIVISION = new MathContext(128);

    static BigDecimal d(long l) {
        return new BigDecimal(l, MathContext.UNLIMITED);
    }

    private static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b, MathContext.UNLIMITED);
    }

    private static BigDecimal sub(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    private static BigDecimal mul(BigDecimal a, BigDecimal b) {
        return a.multiply(b, MathContext.UNLIMITED);
    }

    private static BigDecimal div(BigDecimal a, BigDecimal b) {
        return a.divide(b, DIVISION);
    }

    private static BigDecimal pow(BigDecimal a, int b) {
        return a.pow(b, MathContext.UNLIMITED);
    }

    static BigDecimal fact(BigDecimal n) {
        switch (n.compareTo(ONE)) {
        case -1: return ZERO;
        case  0: return ONE;
        default: return mul(n, fact(sub(n, ONE)));
        }
    }

    static BigDecimal combinations(int n, int k) {
        if (k < 0 || k > n) { return ZERO; }
        if (k == 0 || k == n) { return ONE; }
        BigDecimal bn = d(n);
        BigDecimal bk = d(k);
        return div(fact(bn), mul(fact(bk), fact(sub(bn, bk))));
    }

    private static BigDecimal getHitProbability(int numberOfHits, int numberOfCalls) {
        return div(mul(combinations(SQUARES, numberOfHits), combinations(NUMBERS - SQUARES, numberOfCalls - numberOfHits)),
                combinations(NUMBERS, numberOfCalls));
    }

    public static void main(String[] args) {
        BingoOdds bingo = new BingoOdds();
        bingo.printStatistics();
        bingo.printProbabilities(1, 10, 25, 50);
    }

    // number of possible Bingo positions based on hits
    private final long[] bingoCombinations = createBingoCombinations();

    // probability of a Bingo position based on hits
    private final BigDecimal[] bingoProbabilities = createBingoProbabilities();

    // single board probability of a Bingo position based on numbers called
    private final BigDecimal[] boardProbabilities = createBoardProbabilities();

    private long[] createBingoCombinations() {
        long[] combinations = new long[SQUARES + 1];
        for (int i = 0; i <= 0b111111111111111111111111; ++i) {
            for (int bingo : BINGOS) {
                if ((i & bingo) == bingo) {
                    combinations[Integer.bitCount(i)]++;
                    break;
                }
            }
        }
        return combinations;
    }

    private BigDecimal[] createBingoProbabilities() {
        BigDecimal[] probabilities = new BigDecimal[SQUARES + 1];
        for (int i = 0; i < 4; ++i) {
            probabilities[i] = ZERO;
        }
        for (int i = 4; i <= SQUARES; ++i) {
            probabilities[i] = div(d(bingoCombinations[i]), combinations(SQUARES, i));
        }
        return probabilities;
    }

    private BigDecimal[] createBoardProbabilities() {
        BigDecimal[] probabilities = new BigDecimal[NUMBERS + 1];
        for (int n = 0; n < 4; ++n) {
            probabilities[n] = ZERO;
        }
        for (int n = 4; n <= NUMBERS; ++n) {
            BigDecimal sum = ZERO;
            for (int s = 4; s <= SQUARES; ++s) {
                sum = add(sum, mul(getHitProbability(s, n), bingoProbabilities[s]));
            }
            probabilities[n] = sum;
        }
        return probabilities;
    }

    public BigDecimal getProbability(int numberOfCalls, int numberOfBoards) {
        if (numberOfBoards == 1) {
            return boardProbabilities[numberOfCalls];
        }
        return sub(ONE, pow(sub(ONE, boardProbabilities[numberOfCalls]), numberOfBoards));
    }

    public void printStatistics() {
        System.out.println();
        System.out.println("Numbers Bingo     Bingo      ");
        System.out.println("Matched Combos    Probability");
        System.out.println("======= ========= ===========");
        for (int i = 0; i <= SQUARES; i++) {
            System.out.printf("%7d %,9d %11.6f %n",
                    i, bingoCombinations[i], bingoProbabilities[i]);
        }
    }

    public String printProbabilitiesBanner(int... boardCounts) {
        StringBuilder title = new StringBuilder("Numbers ");
        StringBuilder sub = new StringBuilder("Called  ");
        StringBuilder sep = new StringBuilder("======= ");
        StringBuilder fmt = new StringBuilder("%7d ");
        for (int i : boardCounts) {
            if (i == 1) {
                title.append("1 Board    ");
            } else {
                String s = i + " Boards";
                title.append(s);
                for (int j = 11 - s.length(); j > 0; --j) {
                    title.append(' ');
                }
            }
            sub.append("Cumulative ");
            sep.append("========== ");
            fmt.append("%.8f ");
        }
        System.out.println();
        System.out.println(title);
        System.out.println(sub);
        System.out.println(sep);
        return fmt.append("%n").toString();
    }

    public void printProbabilities(int... boardCounts) {
        String fmt = printProbabilitiesBanner(boardCounts);
        StringBuilder b = new StringBuilder();
        Object[] ps = new Object[boardCounts.length + 1];
        for (int i = 1; i <= NUMBERS; ++i) {
            b.setLength(0); // reset
            ps[0] = i;
            for (int j = 0; j < boardCounts.length; ++j) {
                ps[j + 1] = getProbability(i, boardCounts[j]);
            }
            System.out.printf(fmt, ps);
        }
    }

}
