package terminal_calculator_app;
import java.math.BigDecimal;
import java.math.BigInteger;
public class Summation {
    private static final BigDecimal E = BigDecimal.valueOf(Math.E);
    public static double sum(double n) {
        if (n < 0) {
            throw new ArithmeticException("n must be non-negative for summation");
        }
        if (Math.floor(n) != n) {
            throw new ArithmeticException("n must be an integer for summation");
        }
        BigInteger bigN = BigInteger.valueOf((long)n);
        BigInteger sum = bigN.multiply(bigN.add(BigInteger.ONE)).divide(BigInteger.valueOf(2));
        return sum.doubleValue();
    }
    public static double sumSquares(double n) {
        if (n < 0) {
            throw new ArithmeticException("n must be non-negative for square summation");
        }
        if (Math.floor(n) != n) {
            throw new ArithmeticException("n must be an integer for square summation");
        }
        BigInteger bigN = BigInteger.valueOf((long)n);
        BigInteger nPlus1 = bigN.add(BigInteger.ONE);
        BigInteger twoNPlus1 = bigN.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE);
        BigInteger product = bigN.multiply(nPlus1).multiply(twoNPlus1);
        BigInteger sum = product.divide(BigInteger.valueOf(6));
        return sum.doubleValue();
    }
    public static double sumCubes(double n) {
        if (n < 0) {
            throw new ArithmeticException("n must be non-negative for cube summation");
        }
        if (Math.floor(n) != n) {
            throw new ArithmeticException("n must be an integer for cube summation");
        }
        BigInteger bigN = BigInteger.valueOf((long)n);
        BigInteger nPlus1 = bigN.add(BigInteger.ONE);
        BigInteger halfSum = bigN.multiply(nPlus1).divide(BigInteger.valueOf(2));
        BigInteger sum = halfSum.multiply(halfSum);
        return sum.doubleValue();
    }
    public static double sumExponentials(double n) {
        if (n < 0) {
            throw new ArithmeticException("n must be non-negative for exponential summation");
        }
        if (Math.floor(n) != n) {
            throw new ArithmeticException("n must be an integer for exponential summation");
        }
        BigDecimal sum = BigDecimal.ZERO;
        int intN = (int)n;
        for (int k = 0; k <= intN; k++) {
            sum = sum.add(E.pow(k));
        }
        return sum.doubleValue();
    }
    public static double sumFactorials(double n) {
        if (n < 0) {
            throw new ArithmeticException("n must be non-negative for factorial summation");
        }
        if (Math.floor(n) != n) {
            throw new ArithmeticException("n must be an integer for factorial summation");
        }
        BigInteger sum = BigInteger.ZERO;
        int intN = (int)n;
        for (int k = 0; k <= intN; k++) {
            sum = sum.add(factorial(BigInteger.valueOf(k)));
        }
        return sum.doubleValue();
    }
    private static BigInteger factorial(BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) < 0) {
            throw new ArithmeticException("Factorial of negative number");
        }
        if (n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE)) {
            return BigInteger.ONE;
        }
        BigInteger result = BigInteger.ONE;
        for (BigInteger j = BigInteger.valueOf(2); j.compareTo(n) <= 0; j = j.add(BigInteger.ONE)) {
            result = result.multiply(j);
        }
        return result;
    }
}