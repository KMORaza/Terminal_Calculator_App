package terminal_calculator_app;
public class Trigonometry {
    private static boolean useDegrees = false;
    public static void setAngleMode(boolean degrees) {
        useDegrees = degrees;
    }

    public static boolean isUsingDegrees() {
        return useDegrees;
    }

    private static double toRadians(double angle) {
        return useDegrees ? Math.toRadians(angle) : angle;
    }

    private static double toDegrees(double angle) {
        return useDegrees ? angle : Math.toDegrees(angle);
    }
    public static double sin(double x) {
        return Math.sin(toRadians(x));
    }

    public static double cos(double x) {
        return Math.cos(toRadians(x));
    }
    public static double tan(double x) {
        double result = Math.tan(toRadians(x));
        if (Double.isInfinite(result) || Double.isNaN(result)) {
            throw new ArithmeticException("Tangent undefined at this point");
        }
        return result;
    }
    public static double sec(double x) {
        double cosVal = Math.cos(toRadians(x));
        if (cosVal == 0) {
            throw new ArithmeticException("Secant undefined at this point");
        }
        return 1.0 / cosVal;
    }
    public static double csc(double x) {
        double sinVal = Math.sin(toRadians(x));
        if (sinVal == 0) {
            throw new ArithmeticException("Cosecant undefined at this point");
        }
        return 1.0 / sinVal;
    }

    public static double cot(double x) {
        double tanVal = Math.tan(toRadians(x));
        if (tanVal == 0) {
            throw new ArithmeticException("Cotangent undefined at this point");
        }
        return 1.0 / tanVal;
    }
    public static double asin(double x) {
        if (x < -1 || x > 1) {
            throw new ArithmeticException("Arcsine input must be between -1 and 1");
        }
        return toDegrees(Math.asin(x));
    }
    public static double acos(double x) {
        if (x < -1 || x > 1) {
            throw new ArithmeticException("Arccosine input must be between -1 and 1");
        }
        return toDegrees(Math.acos(x));
    }
    public static double atan(double x) {
        return toDegrees(Math.atan(x));
    }
    public static double asec(double x) {
        if (x > -1 && x < 1) {
            throw new ArithmeticException("Arcsecant undefined for values between -1 and 1");
        }
        return toDegrees(Math.acos(1.0 / x));
    }
    public static double acsc(double x) {
        if (x > -1 && x < 1) {
            throw new ArithmeticException("Arccosecant undefined for values between -1 and 1");
        }
        return toDegrees(Math.asin(1.0 / x));
    }
    public static double acot(double x) {
        if (x == 0) {
            return useDegrees ? 90.0 : Math.PI / 2;
        }
        return toDegrees(Math.atan(1.0 / x));
    }
    public static double sinh(double x) {
        return Math.sinh(toRadians(x));
    }

    public static double cosh(double x) {
        return Math.cosh(toRadians(x));
    }

    public static double tanh(double x) {
        return Math.tanh(toRadians(x));
    }

    public static double sech(double x) {
        return 1.0 / Math.cosh(toRadians(x));
    }
    public static double csch(double x) {
        double sinhVal = Math.sinh(toRadians(x));
        if (sinhVal == 0) {
            throw new ArithmeticException("Hyperbolic cosecant undefined at this point");
        }
        return 1.0 / sinhVal;
    }
    public static double coth(double x) {
        double tanhVal = Math.tanh(toRadians(x));
        if (tanhVal == 0) {
            throw new ArithmeticException("Hyperbolic cotangent undefined at this point");
        }
        return 1.0 / tanhVal;
    }
}