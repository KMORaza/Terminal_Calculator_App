package terminal_calculator_app;
public class Mod {
    public static double modulus(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Modulus by zero is undefined");
        }
        return a % b;
    }
}