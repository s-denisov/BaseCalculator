package org.basecalculator;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.basecalculator.logic.Base;
import org.basecalculator.logic.NumberBase;
import org.basecalculator.logic.WorkingLogger;

public class BaseConversion {

    @FXML private ChoiceBox<Base> fromBase;
    @FXML private ChoiceBox<Base> toBase;
    @FXML private Label result;
    @FXML private TextField inputValue;
    @FXML private Label workingOutput;
    private ExceptionHandler handler;

    public void initialize() {
        // Both the base from which we convert and the one to which we convert can take any base.
        // By default, we convert from denary to binary
        fromBase.getItems().setAll(Base.values());
        fromBase.setValue(Base.DENARY);
        toBase.getItems().setAll(Base.values());
        toBase.setValue(Base.BINARY);
        handler = new ExceptionHandler(result);
    }

    @FXML
    public void convertBases() {
        handler.setResultText(() -> {
            WorkingLogger working = new WorkingLogger(new StringBuilder());
            // Creates a number with the desired value and base, and passes it our working object.
            NumberBase fromValue = working.create(inputValue.getText(), fromBase.getValue());
            // Converts to the desired base
            NumberBase resultantValue = fromValue.to(toBase.getValue());
            workingOutput.setText(fromValue.getWorking()); // Outputs the working
            return resultantValue.toString(); // Returns the result, which is outputted by the ExceptionHandler
        });
    }

    // Swaps the base from which we convert with the base to which we convert
    @FXML
    public void swapBases() {
        Base from = fromBase.getValue();
        Base to = toBase.getValue();
        fromBase.setValue(to);
        toBase.setValue(from);
    }
}
