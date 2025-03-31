package terminal_calculator_app;
import java.math.BigInteger;
public class Permutations {
    public static double nPr(double n, double r) {
        if (n < 0 || r < 0) {
            throw new ArithmeticException("n and r must be non-negative for permutations");
        }
        if (Math.floor(n) != n || Math.floor(r) != r) {
            throw new ArithmeticException("n and r must be integers for permutations");
        }
        if (r > n) {
            throw new ArithmeticException("r cannot be greater than n for permutations");
        }
        BigInteger nFact = factorial(BigInteger.valueOf((long)n));
        BigInteger nMinusRFact = factorial(BigInteger.valueOf((long)(n - r)));
        if (nMinusRFact.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Division by zero in permutation calculation");
        }
        BigInteger result = nFact.divide(nMinusRFact);
        return result.doubleValue();
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