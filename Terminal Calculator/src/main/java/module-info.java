module javafx.terminal_calculator_app.terminalcalculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;


    opens terminal_calculator_app to javafx.fxml;
    exports terminal_calculator_app;
}