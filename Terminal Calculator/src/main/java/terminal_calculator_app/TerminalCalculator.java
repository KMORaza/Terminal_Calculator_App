package terminal_calculator_app;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import java.util.Stack;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigInteger;
public class TerminalCalculator extends Application {
    private TextField inputField;
    private TextArea outputArea;
    private static final double PI = Math.PI;
    private static final double E = Math.E;
    private ArrayList<String> sessionHistory = new ArrayList<>();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void start(Stage primaryStage) {
        inputField = new TextField();
        inputField.setPromptText("Enter expression (e.g., SumFact(5) or 2 + 3)");
        inputField.setStyle(
                "-fx-background-color: black; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Consolas'; " +
                        "-fx-border-color: white; -fx-border-radius: 5px;" +
                        "-fx-border-width: 1px;" +
                        "-fx-font-size: 12px;");
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setStyle(
                "-fx-control-inner-background: black; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Consolas'; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px;" +
                        "-fx-font-size: 13px;");
        inputField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                evaluateExpression();
            }
        });
        ToolBar toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: black;");
        Button viewHistoryButton = new Button("History");
        viewHistoryButton.setStyle(
                "-fx-background-color: black; " +
                        "-fx-text-fill: rgb(255,255,153); " +
                        "-fx-border-color: rgb(255,255,153); " +
                        "-fx-border-width: 1px; -fx-border-radius: 5px; " +
                        "-fx-font-family: 'Segoe UI Emoji'; " +
                        "-fx-font-size: 12px;");


        viewHistoryButton.setOnAction(e -> showHistoryWindow());
        HBox buttonContainer = new HBox(viewHistoryButton);
        buttonContainer.setStyle("-fx-alignment: center;");
        HBox.setHgrow(buttonContainer, javafx.scene.layout.Priority.ALWAYS);
        toolBar.getItems().add(buttonContainer);
        VBox root = new VBox(10);
        root.getChildren().addAll(toolBar, inputField, outputArea);
        root.setStyle("-fx-padding: 10; -fx-background-color: black;");
        Scene scene = new Scene(root, 400, 250);
        primaryStage.setTitle("Terminal Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        inputField.requestFocus();
    }
    private void evaluateExpression() {
        String expression = inputField.getText().trim();
        if (!expression.isEmpty()) {
            try {
                String timestamp = LocalDateTime.now().format(dtf);
                String historyEntry;
                if (expression.equals("deg")) {
                    Trigonometry.setAngleMode(true);
                    historyEntry = timestamp + "\n> deg\nAngle mode set to degrees\n\n";
                    outputArea.appendText("> deg\nAngle mode set to degrees\n\n");
                } else if (expression.equals("rad")) {
                    Trigonometry.setAngleMode(false);
                    historyEntry = timestamp + "\n> rad\nAngle mode set to radians\n\n";
                    outputArea.appendText("> rad\nAngle mode set to radians\n\n");
                } else {
                    double result = evaluate(expression);
                    String output = String.format("> %s\n= %.4f\n\n", expression, result);
                    historyEntry = timestamp + "\n" + output;
                    outputArea.appendText(output);
                }
                sessionHistory.add(historyEntry);
                inputField.clear();
            } catch (Exception e) {
                String timestamp = LocalDateTime.now().format(dtf);
                String error = String.format("> %s\nError: %s\n\n",
                        expression, e.getMessage() != null ? e.getMessage() : "Invalid expression");
                String historyEntry = timestamp + "\n" + error;
                outputArea.appendText(error);
                sessionHistory.add(historyEntry);
                inputField.clear();
            }
        }
    }
    private void showHistoryWindow() {
        Stage historyStage = new Stage();
        historyStage.setTitle("Session History");
        TextFlow historyArea = new TextFlow();
        historyArea.setStyle(
                "-fx-background-color: black; " +
                        "-fx-padding: 10;"
        );
        if (sessionHistory.isEmpty()) {
            Text noHistory = new Text("Nothing in history for now!\n");
            noHistory.setStyle("-fx-fill: white; -fx-font-family: 'Consolas';");
            historyArea.getChildren().add(noHistory);
        } else {
            for (String entry : sessionHistory) {
                String[] lines = entry.split("\n");
                Text timestampText = new Text(lines[0] + "\n");
                timestampText.setStyle("-fx-fill: rgb(255,255,153); -fx-font-family: 'Consolas';");
                TextFlow entryText = new TextFlow();
                for (int i = 1; i < lines.length; i++) {
                    Text line = new Text(lines[i] + "\n");
                    line.setStyle("-fx-fill: white; -fx-font-family: 'Consolas';");
                    entryText.getChildren().add(line);
                }
                historyArea.getChildren().addAll(timestampText, entryText);
            }
        }
        ScrollPane scrollPane = new ScrollPane(historyArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: black; -fx-border-color: white; -fx-border-width: 1px;");
        VBox historyLayout = new VBox(10);
        historyLayout.getChildren().add(scrollPane);
        historyLayout.setStyle("-fx-background-color: black; -fx-padding: 10;");
        Scene historyScene = new Scene(historyLayout, 400, 300);
        historyStage.setScene(historyScene);
        historyStage.setResizable(false);
        historyStage.show();
    }
    private double evaluate(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        expression = expression.replaceAll("\\s+", "");
        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (i + 7 < expression.length() && expression.substring(i, i + 7).equals("SumFact")) {
                i += 7;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid SumFact syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder sumArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) sumArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in SumFact");
                }
                double n = evaluate(sumArg.toString());
                numbers.push(Summation.sumFactorials(n));
                continue;
            }
            if (i + 6 < expression.length() && expression.substring(i, i + 6).equals("SumExp")) {
                i += 6;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid SumExp syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder sumArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) sumArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in SumExp");
                }
                double n = evaluate(sumArg.toString());
                numbers.push(Summation.sumExponentials(n));
                continue;
            }
            if (i + 7 < expression.length() && expression.substring(i, i + 7).equals("SumCube")) {
                i += 7;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid SumCube syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder sumArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) sumArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in SumCube");
                }
                double n = evaluate(sumArg.toString());
                numbers.push(Summation.sumCubes(n));
                continue;
            }
            if (i + 5 < expression.length() && expression.substring(i, i + 5).equals("SumSq")) {
                i += 5;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid SumSq syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder sumArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) sumArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in SumSq");
                }
                double n = evaluate(sumArg.toString());
                numbers.push(Summation.sumSquares(n));
                continue;
            }
            if (i + 3 < expression.length() && expression.substring(i, i + 3).equals("Sum")) {
                i += 3;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid Sum syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder sumArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) sumArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in Sum");
                }
                double n = evaluate(sumArg.toString());
                numbers.push(Summation.sum(n));
                continue;
            }
            if (i + 1 < expression.length() && expression.charAt(i) == 'P' && expression.charAt(i + 1) == '(') {
                i += 2;
                int parenCount = 1;
                StringBuilder permArgs = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) permArgs.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in P(n,r)");
                }
                String[] args = permArgs.toString().split(",");
                if (args.length != 2) {
                    throw new IllegalArgumentException("P(n,r) requires exactly 2 arguments");
                }
                double n = evaluate(args[0].trim());
                double r = evaluate(args[1].trim());
                numbers.push(Permutations.nPr(n, r));
                continue;
            }
            if (i + 1 < expression.length() && expression.charAt(i) == 'C' && expression.charAt(i + 1) == '(') {
                i += 2;
                int parenCount = 1;
                StringBuilder combArgs = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) combArgs.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in C(n,r)");
                }
                String[] args = combArgs.toString().split(",");
                if (args.length != 2) {
                    throw new IllegalArgumentException("C(n,r) requires exactly 2 arguments");
                }
                double n = evaluate(args[0].trim());
                double r = evaluate(args[1].trim());
                numbers.push(Combinations.nCr(n, r));
                continue;
            }
            if (i + 3 < expression.length()) {
                String func = expression.substring(i, i + 4);
                if ("sinh".equals(func) || "cosh".equals(func) || "tanh".equals(func) ||
                        "sech".equals(func) || "csch".equals(func) || "coth".equals(func)) {
                    i += 4;
                    if (i >= expression.length() || expression.charAt(i) != '(') {
                        throw new IllegalArgumentException("Invalid " + func + " syntax");
                    }
                    i++;
                    int parenCount = 1;
                    StringBuilder arg = new StringBuilder();
                    while (i < expression.length() && parenCount > 0) {
                        if (expression.charAt(i) == '(') parenCount++;
                        else if (expression.charAt(i) == ')') parenCount--;
                        if (parenCount > 0) arg.append(expression.charAt(i));
                        i++;
                    }
                    if (parenCount != 0) {
                        throw new IllegalArgumentException("Unmatched parentheses in " + func);
                    }
                    double value = evaluate(arg.toString());
                    switch (func) {
                        case "sinh": numbers.push(Trigonometry.sinh(value)); break;
                        case "cosh": numbers.push(Trigonometry.cosh(value)); break;
                        case "tanh": numbers.push(Trigonometry.tanh(value)); break;
                        case "sech": numbers.push(Trigonometry.sech(value)); break;
                        case "csch": numbers.push(Trigonometry.csch(value)); break;
                        case "coth": numbers.push(Trigonometry.coth(value)); break;
                    }
                    continue;
                }
            }
            if (i + 2 < expression.length()) {
                String func = expression.substring(i, i + 3);
                if ("sin".equals(func) || "cos".equals(func) || "tan".equals(func) ||
                        "sec".equals(func) || "csc".equals(func) || "cot".equals(func)) {
                    i += 3;
                    if (i >= expression.length() || expression.charAt(i) != '(') {
                        throw new IllegalArgumentException("Invalid " + func + " syntax");
                    }
                    i++;
                    int parenCount = 1;
                    StringBuilder arg = new StringBuilder();
                    while (i < expression.length() && parenCount > 0) {
                        if (expression.charAt(i) == '(') parenCount++;
                        else if (expression.charAt(i) == ')') parenCount--;
                        if (parenCount > 0) arg.append(expression.charAt(i));
                        i++;
                    }
                    if (parenCount != 0) {
                        throw new IllegalArgumentException("Unmatched parentheses in " + func);
                    }
                    double value = evaluate(arg.toString());
                    switch (func) {
                        case "sin": numbers.push(Trigonometry.sin(value)); break;
                        case "cos": numbers.push(Trigonometry.cos(value)); break;
                        case "tan": numbers.push(Trigonometry.tan(value)); break;
                        case "sec": numbers.push(Trigonometry.sec(value)); break;
                        case "csc": numbers.push(Trigonometry.csc(value)); break;
                        case "cot": numbers.push(Trigonometry.cot(value)); break;
                    }
                    continue;
                }
            }
            if (i + 3 < expression.length()) {
                String func = expression.substring(i, i + 4);
                if ("asin".equals(func) || "acos".equals(func) || "atan".equals(func) ||
                        "asec".equals(func) || "acsc".equals(func) || "acot".equals(func)) {
                    i += 4;
                    if (i >= expression.length() || expression.charAt(i) != '(') {
                        throw new IllegalArgumentException("Invalid " + func + " syntax");
                    }
                    i++;
                    int parenCount = 1;
                    StringBuilder arg = new StringBuilder();
                    while (i < expression.length() && parenCount > 0) {
                        if (expression.charAt(i) == '(') parenCount++;
                        else if (expression.charAt(i) == ')') parenCount--;
                        if (parenCount > 0) arg.append(expression.charAt(i));
                        i++;
                    }
                    if (parenCount != 0) {
                        throw new IllegalArgumentException("Unmatched parentheses in " + func);
                    }
                    double value = evaluate(arg.toString());
                    switch (func) {
                        case "asin": numbers.push(Trigonometry.asin(value)); break;
                        case "acos": numbers.push(Trigonometry.acos(value)); break;
                        case "atan": numbers.push(Trigonometry.atan(value)); break;
                        case "asec": numbers.push(Trigonometry.asec(value)); break;
                        case "acsc": numbers.push(Trigonometry.acsc(value)); break;
                        case "acot": numbers.push(Trigonometry.acot(value)); break;
                    }
                    continue;
                }
            }
            if (i + 3 < expression.length() && expression.substring(i, i + 4).equals("fact")) {
                i += 4;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid fact syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder factArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) factArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in fact");
                }
                double arg = evaluate(factArg.toString());
                if (arg < 0) {
                    throw new ArithmeticException("Factorial of negative number");
                }
                if (Math.floor(arg) != arg) {
                    throw new ArithmeticException("Factorial requires an integer");
                }
                BigInteger factResult = factorial(BigInteger.valueOf((long)arg));
                numbers.push(factResult.doubleValue());
                continue;
            }
            if (i + 1 < expression.length() && expression.substring(i, i + 2).equals("pi")) {
                numbers.push(PI);
                i += 2;
                continue;
            }
            if (c == 'e') {
                numbers.push(E);
                i++;
                continue;
            }
            if (i + 2 < expression.length() && expression.substring(i, i + 3).equals("log")) {
                i += 3;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid log syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder logArgs = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) logArgs.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in log");
                }
                String[] args = logArgs.toString().split(",");
                if (args.length != 2) {
                    throw new IllegalArgumentException("log requires exactly 2 arguments");
                }
                double base = evaluate(args[0].trim());
                double value = evaluate(args[1].trim());
                numbers.push(Logarithm.calculateLog(base, value));
                continue;
            }
            if (i + 1 < expression.length() && expression.substring(i, i + 2).equals("ln")) {
                i += 2;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid ln syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder lnArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) lnArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in ln");
                }
                double arg = evaluate(lnArg.toString());
                if (arg <= 0) {
                    throw new ArithmeticException("Natural log of non-positive number");
                }
                numbers.push(Math.log(arg));
                continue;
            }
            if (i + 2 < expression.length() && expression.substring(i, i + 3).equals("pct")) {
                i += 3;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid pct syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder pctArgs = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) pctArgs.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in pct");
                }
                String[] args = pctArgs.toString().split(",");
                if (args.length != 2) {
                    throw new IllegalArgumentException("pct requires exactly 2 arguments");
                }
                double value = evaluate(args[0].trim());
                double percent = evaluate(args[1].trim());
                numbers.push(value * (percent / 100.0));
                continue;
            }
            if (i + 2 < expression.length() && expression.substring(i, i + 3).equals("nrt")) {
                i += 3;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid nrt syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder nrtArgs = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) nrtArgs.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in nrt");
                }
                String[] args = nrtArgs.toString().split(",");
                if (args.length != 2) {
                    throw new IllegalArgumentException("nrt requires exactly 2 arguments");
                }
                double n = evaluate(args[0].trim());
                double value = evaluate(args[1].trim());
                if (n == 0) {
                    throw new ArithmeticException("Root index cannot be zero");
                }
                if (value < 0 && n % 2 == 0) {
                    throw new ArithmeticException("Even root of negative number");
                }
                numbers.push(Math.pow(value, 1.0 / n));
                continue;
            }
            if (i + 3 < expression.length() && expression.substring(i, i + 4).equals("sqrt")) {
                i += 4;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid sqrt syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder sqrtArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) sqrtArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in sqrt");
                }
                double arg = evaluate(sqrtArg.toString());
                if (arg < 0) {
                    throw new ArithmeticException("Square root of negative number");
                }
                numbers.push(Math.sqrt(arg));
                continue;
            }
            if (i + 3 < expression.length() && expression.substring(i, i + 4).equals("cbrt")) {
                i += 4;
                if (i >= expression.length() || expression.charAt(i) != '(') {
                    throw new IllegalArgumentException("Invalid cbrt syntax");
                }
                i++;
                int parenCount = 1;
                StringBuilder cbrtArg = new StringBuilder();
                while (i < expression.length() && parenCount > 0) {
                    if (expression.charAt(i) == '(') parenCount++;
                    else if (expression.charAt(i) == ')') parenCount--;
                    if (parenCount > 0) cbrtArg.append(expression.charAt(i));
                    i++;
                }
                if (parenCount != 0) {
                    throw new IllegalArgumentException("Unmatched parentheses in cbrt");
                }
                double arg = evaluate(cbrtArg.toString());
                numbers.push(Math.cbrt(arg));
                continue;
            }
            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expression.length() &&
                        (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }
                numbers.push(Double.parseDouble(num.toString()));
                continue;
            }
            if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    performOperation(numbers, operators.pop());
                }
                if (!operators.isEmpty()) operators.pop();
                else throw new IllegalArgumentException("Unmatched parentheses");
            } else if (isOperator(c)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    performOperation(numbers, operators.pop());
                }
                operators.push(c);
            }
            i++;
        }
        while (!operators.isEmpty()) {
            if (operators.peek() == '(') {
                throw new IllegalArgumentException("Unmatched parentheses");
            }
            performOperation(numbers, operators.pop());
        }
        if (numbers.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return numbers.pop();
    }
    private BigInteger factorial(BigInteger n) {
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
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%';
    }
    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                return 0;
        }
    }
    private void performOperation(Stack<Double> numbers, char operator) {
        if (numbers.size() < 2) {
            throw new IllegalArgumentException("Invalid expression");
        }
        double b = numbers.pop();
        double a = numbers.pop();

        switch (operator) {
            case '+':
                numbers.push(a + b);
                break;
            case '-':
                numbers.push(a - b);
                break;
            case '*':
                numbers.push(a * b);
                break;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                numbers.push(a / b);
                break;
            case '%':
                numbers.push(Mod.modulus(a, b));
                break;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}