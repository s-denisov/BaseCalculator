module org.basecalculator {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.basecalculator to javafx.fxml;
    exports org.basecalculator;
}