/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;

public class MBigMath {

    public static final char[] BASE_62_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    public static final char[] BASE_91_CHARS = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '±', '§', '!', '@', '#', '$', '%', '^', '&', '*',
        '(', ')', '_', '+', '=', '{', '[', '}', ']', ';', '|', '~', '`', '<', ',', '>', '.', '?',
        '/'
    };
    public static final BigInteger TWO = BigInteger.valueOf(2);
    public static final BigInteger SIXTY_TWO = BigInteger.valueOf(62);
    public static final BigInteger NINE_ONE = BigInteger.valueOf(91);
    public static final BigDecimal BD_MINUS_ONE = BigDecimal.valueOf(-1);
    public static final BigDecimal BD_TWO = BigDecimal.valueOf(2);
    public static final BigDecimal BD_TEN = BigDecimal.valueOf(10);
    public static final BigDecimal BD_ONE_HUNDRED = BigDecimal.valueOf(100);

    private static final BigDecimal SQRT_DIG = new BigDecimal(150);
    private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

    /**
     * Calculate a pow with modulo using the binary algorithm.
     *
     * @param base
     * @param pow
     * @param mod
     * @return the pow
     * @throws IOException
     */
    public static BigInteger binaryPow(BigInteger base, BigInteger pow, BigInteger mod)
            throws IOException {

        if (mod.subtract(BigInteger.ONE).pow(2).compareTo(base) > 1) {
            throw new IOException("modulo is too big");
        }
        BigInteger res = BigInteger.ONE;
        base = base.mod(mod);

        while (pow.compareTo(BigInteger.ZERO) == 1) {
            if (pow.mod(TWO).equals(BigInteger.ONE)) res = res.multiply(base).mod(mod);
            pow = pow.shiftRight(1);
            base = base.multiply(base).mod(mod);
        }
        return res;
    }

    /**
     * Calculate the pow with modulo using divide algorithm. Using the binaryPow is the fastest!
     *
     * @param base
     * @param pow
     * @param mod
     * @return the pow
     */
    public static BigInteger dividePow(BigInteger base, BigInteger pow, BigInteger mod) {
        return dividePow(base, pow, 1).mod(mod);
    }

    private static BigInteger dividePow(BigInteger base, BigInteger pow, long level) {
        boolean odd = false;
        if (pow.mod(TWO).equals(BigInteger.ONE)) {
            // odd
            pow = pow.subtract(BigInteger.ONE);
            odd = true;
        }

        BigInteger half = pow.divide(TWO);

        BigInteger res = null;
        if (half.equals(BigInteger.ONE)) {
            res = base.multiply(base);
        } else {
            res = dividePow(base, half, level + 1);
            res = res.multiply(res);
        }
        if (odd) res = res.multiply(base);

        return res;
    }

    /**
     * Calculate the pow with modulo using the simple mathematics straight forward method. Do not
     * use this method! Use the binaryPow for good performance.
     *
     * @param base
     * @param pow
     * @param mod
     * @return the pow
     */
    public static BigInteger straightPow(BigInteger base, BigInteger pow, BigInteger mod) {
        BigInteger res = base;
        for (BigInteger bi = BigInteger.ONE; bi.compareTo(pow) == -1; bi = bi.add(BigInteger.ONE)) {
            res = res.multiply(base);
        }
        return res.mod(mod);
    }

    public static BigInteger splitPow(BigInteger base, BigInteger pow, BigInteger mod) {
        BigInteger res = base;
        for (BigInteger bi = BigInteger.ONE; bi.compareTo(pow) == -1; bi = bi.add(BigInteger.ONE)) {
            res = res.multiply(base).mod(mod);
        }
        return res.mod(mod);
    }

    /**
     * Calculate a log10
     *
     * @param b
     * @param dp
     * @return log 10
     */
    // http://everything2.com/index.pl?node_id=946812
    public static BigDecimal log10(BigDecimal b, int dp) {
        final int NUM_OF_DIGITS = dp + 2; // need to add one to get the right number of dp
        //  and then add one again to get the next number
        //  so I can round it correctly.

        MathContext mc = new MathContext(NUM_OF_DIGITS, RoundingMode.HALF_EVEN);

        // special conditions:
        // log(-x) -> exception
        // log(1) == 0 exactly;
        // log of a number lessthan one = -log(1/x)
        if (b.signum() <= 0) throw new ArithmeticException("log of a negative number! (or zero)");
        else if (b.compareTo(BigDecimal.ONE) == 0) return BigDecimal.ZERO;
        else if (b.compareTo(BigDecimal.ONE) < 0)
            return (log10((BigDecimal.ONE).divide(b, mc), dp)).negate();

        StringBuilder sb = new StringBuilder();
        // number of digits on the left of the decimal point
        int leftDigits = b.precision() - b.scale();

        // so, the first digits of the log10 are:
        sb.append(leftDigits - 1).append(".");

        // this is the algorithm outlined in the webpage
        int n = 0;
        while (n < NUM_OF_DIGITS) {
            b = (b.movePointLeft(leftDigits - 1)).pow(10, mc);
            leftDigits = b.precision() - b.scale();
            sb.append(leftDigits - 1);
            n++;
        }

        BigDecimal ans = new BigDecimal(sb.toString());

        // Round the number to the correct number of decimal places.
        ans =
                ans.round(
                        new MathContext(
                                ans.precision() - ans.scale() + dp, RoundingMode.HALF_EVEN));
        return ans;
    }

    /**
     * Compute D from E using baghdad algorithm.
     *
     * @param e
     * @param z
     * @return D
     */
    public static BigInteger computeDfromE(BigInteger e, BigInteger z) {
        BigDecimal E = new BigDecimal(e);
        BigDecimal Z = new BigDecimal(z);
        BigDecimal D = new BigDecimal(1);
        BigDecimal T = null;
        do {
            D = D.add(Z);
            T = D.divide(E, 100, RoundingMode.UP).stripTrailingZeros();
        } while (T.scale() > 0);
        return T.toBigInteger();
    }

    public static BigDecimal sqrt(BigDecimal x, int scale) {
        // Check that x >= 0.
        if (x.signum() < 0) {
            throw new IllegalArgumentException("x < 0");
        }

        // n = x*(10^(2*scale))
        BigInteger n = x.movePointRight(scale << 1).toBigInteger();

        // The first approximation is the upper half of n.
        int bits = (n.bitLength() + 1) >> 1;
        BigInteger ix = n.shiftRight(bits);
        BigInteger ixPrev;

        // Loop until the approximations converge
        // (two successive approximations are equal after rounding).
        do {
            ixPrev = ix;

            // x = (x + n/x)/2
            ix = ix.add(n.divide(ix)).shiftRight(1);

            Thread.yield();
        } while (ix.compareTo(ixPrev) != 0);

        return new BigDecimal(ix, scale);
    }

    /**
     * Private utility method used to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    private static BigDecimal sqrtNewtonRaphson(BigDecimal c, BigDecimal xn, BigDecimal precision) {
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx, 2 * SQRT_DIG.intValue(), RoundingMode.DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1) {
            return xn1;
        }
        return sqrtNewtonRaphson(c, xn1, precision);
    }

    /**
     * Uses Newton Raphson to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @param c
     * @return square root
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    public static BigDecimal bigSqrt(BigDecimal c) {
        return sqrtNewtonRaphson(c, new BigDecimal(1), new BigDecimal(1).divide(SQRT_PRE));
    }

    public static String toBase62(BigInteger in) {
        StringBuilder out = new StringBuilder();
        boolean negative = false;
        if (in.signum() == 0) return "0";
        if (in.signum() == -1) {
            negative = true;
            in = in.negate();
        }
        while (in.signum() != 0) {
            int m = in.mod(SIXTY_TWO).intValue();
            out.insert(0, BASE_62_CHARS[m]);
            in = in.divide(SIXTY_TWO);
        }
        if (negative) out.insert(0, '-');
        return out.toString();
    }

    public static BigInteger fromBase62(String in) {
        return fromBase62(in, false);
    }

    public static BigInteger fromBase62(String in, boolean ignoreWhitespace) {
        BigInteger out = BigInteger.ZERO;
        in = in.trim();
        boolean negative = false;
        if (in.startsWith("-")) {
            negative = true;
            in = in.substring(1);
        }
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            int m = 0;
            if (c >= '0' && c <= '9') {
                m = c - '0';
            } else if (c >= 'a' && c <= 'z') {
                m = c - 'a' + 10;
            } else if (c >= 'A' && c <= 'Z') {
                m = c - 'A' + 36;
            } else if (ignoreWhitespace && (c == '\n' || c == '\r' || c == '\t' || c == ' ')) {
                // ignore if requested
                continue;
            } else {
                break; // unknown character will end the number
            }
            out = out.multiply(SIXTY_TWO).add(BigInteger.valueOf(m));
        }

        if (negative) out = out.negate();
        return out;
    }

    public static String toBase62(BigInteger[] in) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length; i++) {
            if (i != 0) out.append(':');
            out.append(toBase62(in[i]));
        }
        return out.toString();
    }

    public static BigInteger[] fromBase62Array(String in) {
        LinkedList<BigInteger> out = new LinkedList<>();
        while (true) {
            BigInteger n = fromBase62(in, true);
            out.add(n);
            int pos = in.indexOf(':');
            if (pos >= 0) {
                in = in.substring(pos + 1);
            } else break;
        }
        return out.toArray(new BigInteger[out.size()]);
    }

    //

    public static String toBase91(BigInteger in) {
        StringBuilder out = new StringBuilder();
        boolean negative = false;
        if (in.signum() == 0) return "0";
        if (in.signum() == -1) {
            negative = true;
            in = in.negate();
        }
        while (in.signum() != 0) {
            int m = in.mod(NINE_ONE).intValue();
            out.insert(0, BASE_91_CHARS[m]);
            in = in.divide(NINE_ONE);
        }
        if (negative) out.insert(0, '-');
        return out.toString();
    }

    public static BigInteger fromBase91(String in) {
        return fromBase91(in, false);
    }

    public static BigInteger fromBase91(String in, boolean ignoreWhitespace) {
        BigInteger out = BigInteger.ZERO;
        in = in.trim();
        boolean negative = false;
        if (in.startsWith("-")) {
            negative = true;
            in = in.substring(1);
        }
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            int m = 0;
            if (c >= '0' && c <= '9') {
                m = c - '0';
            } else if (c >= 'a' && c <= 'z') {
                m = c - 'a' + 10;
            } else if (c >= 'A' && c <= 'Z') {
                m = c - 'A' + 36;
            } else
            //  0: '0','1','2','3','4','5','6','7','8','9',
            // 10:
            // 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            // 36:
            // 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            // 62: '±','§','!','@','#','$','%','^','&','*','(',')','_','+','=',
            // 77: '{','[','}',']',';','|','~','`','<',',','>','.','?','/'
            if (c == '±') m = 62;
            else if (c == '§') m = 63;
            else if (c == '!') m = 64;
            else if (c == '@') m = 65;
            else if (c == '#') m = 66;
            else if (c == '$') m = 67;
            else if (c == '%') m = 68;
            else if (c == '^') m = 69;
            else if (c == '&') m = 70;
            else if (c == '*') m = 71;
            else if (c == '(') m = 72;
            else if (c == ')') m = 73;
            else if (c == '_') m = 74;
            else if (c == '+') m = 75;
            else if (c == '=') m = 76;
            else if (c == '{') m = 77;
            else if (c == '[') m = 78;
            else if (c == '}') m = 79;
            else if (c == ']') m = 80;
            else if (c == ';') m = 81;
            else if (c == '|') m = 82;
            else if (c == '~') m = 83;
            else if (c == '`') m = 84;
            else if (c == '<') m = 85;
            else if (c == ',') m = 86;
            else if (c == '>') m = 87;
            else if (c == '.') m = 88;
            else if (c == '?') m = 89;
            else if (c == '/') m = 90;
            else if (ignoreWhitespace && (c == '\n' || c == '\r' || c == '\t' || c == ' ')) {
                // ignore if requested
                continue;
            } else {
                break; // unknown character will end the number
            }
            out = out.multiply(NINE_ONE).add(BigInteger.valueOf(m));
        }

        if (negative) out = out.negate();
        return out;
    }

    public static String toBase91(BigInteger[] in) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < in.length; i++) {
            if (i != 0) out.append(':');
            out.append(toBase91(in[i]));
        }
        return out.toString();
    }

    public static BigInteger[] fromBase91Array(String in) {
        LinkedList<BigInteger> out = new LinkedList<>();
        while (true) {
            BigInteger n = fromBase91(in, true);
            out.add(n);
            int pos = in.indexOf(':');
            if (pos >= 0) {
                in = in.substring(pos + 1);
            } else break;
        }
        return out.toArray(new BigInteger[out.size()]);
    }

    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) > 0 ? b : a;
    }

    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        if (a == null) return b;
        if (b == null) return a;
        return a.compareTo(b) > 0 ? a : b;
    }
}
