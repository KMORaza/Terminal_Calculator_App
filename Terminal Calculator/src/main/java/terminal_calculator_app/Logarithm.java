package terminal_calculator_app;
public class Logarithm {
    public static double calculateLog(double base, double value) {
        if (base <= 0 || base == 1) {
            throw new ArithmeticException("Logarithm base must be positive and not equal to 1");
        }
        if (value <= 0) {
            throw new ArithmeticException("Logarithm argument must be positive");
        }
        return Math.log(value) / Math.log(base);
    }
}