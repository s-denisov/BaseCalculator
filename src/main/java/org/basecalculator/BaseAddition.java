package org.basecalculator;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.basecalculator.logic.Base;
import org.basecalculator.logic.NumberBase;
import org.basecalculator.logic.NumberBaseException;
import org.basecalculator.logic.WorkingLogger;
import org.basecalculator.logic.binarynumber.SignedBinary;
import org.basecalculator.logic.binarynumber.UnsignedBinary;

public class BaseAddition {

    // These values refer to GUI elements from the FXML (markup) file. They are automatically initialized.
    @FXML private ToggleGroup operation;
    @FXML private TextField number1;
    @FXML private TextField number2;
    @FXML private ChoiceBox<Base> base;
    @FXML private Label result;
    @FXML private RadioButton addChoice;
    @FXML private RadioButton subtractChoice;
    @FXML private Label workingOutput;
    // Set ExceptionHandler as a class member to avoid initializing it every time, or having to pass the result label every time
    private ExceptionHandler handler;

    public void initialize() {
        // The possible values for the ChoiceBox are set here.
        // They are set to the possible enum values for Base (e.g. Binary, Hexadecimal, Denary)
        // This box describes the base in which the numbers that are added/subtracted are
        base.getItems().setAll(Base.values());
        base.setValue(Base.DENARY); // The default value for the ChoiceBox is DENARY
        handler = new ExceptionHandler(result);
    }

    @FXML
    public void addBases() {
        handler.setResultText(() -> {
            // Working is added to the NumberBase's WorkingLogger during calculations. A single WorkingLogger
            //  is created here and used to create number bases, so that it is passed to the number bases automatically
            //  and therefore all working is collected within a single logger
            WorkingLogger working = new WorkingLogger(new StringBuilder());
            // base.getValue() contains the desired base. The WorkingLogger creates the desired base automatically
            NumberBase num1 = working.create(number1.getText(), base.getValue());
            NumberBase num2 = working.create(number2.getText(), base.getValue());
            // Contains the selected radio button. This can be compared with the values of the know radio buttons
            //   to determine which operation has been selected.
            RadioButton selected = (RadioButton) operation.getSelectedToggle();
            String result = "";
            // One of the if statements should always be followed, but the result still needs a default value, so it
            //   is set to the empty string.
            if (selected.equals(addChoice)) {
                // Adds the two numbers using an already defined method (done by converting to binary, then adding, then converting back)
                NumberBase sum = num1.add(num2);
                result = sum.toString();
            } else if (selected.equals(subtractChoice)) {
                // The inputted value is first converted to unsigned binary
                UnsignedBinary num1bin = (UnsignedBinary) num1.to(Base.BINARY);
                // If the first value is a 1, then the number of bytes in the unsigned binary is incremented (by 1),
                //   as its string representation is converted to signed binary directly, but a first bit of 1 in
                //   signed binary means that it is negative, and it must be positive.
                if (num1bin.getValue().get(0) == 1) num1bin.incrementBytes();
                UnsignedBinary num2bin = (UnsignedBinary) num2.to(Base.BINARY);
                if (num2bin.getValue().get(0) == 1) num2bin.incrementBytes();
                // We find the two's complement of num2 and then add as num1 - num2 = num1 + (-num2)
                SignedBinary diff = num1bin.toSigned().add(num2bin.toSigned().twosComplement());
                // We can only represent negative numbers in denary here (we can't represent in binary as the number of bytes
                //   is not fixed). There if the difference is negative and the base isn't negative then there is an error.
                if (diff.isNegative() && base.getValue() != Base.DENARY) {
                    throw new NumberBaseException("Number 2 must be smaller, except for denary");
                }
                // This statement is used to avoid unnecessary conversion: if the base needed is denary then the signed binary
                //   is converted to denary directly. Otherwise, it is converted to denary and then to the desire base.
                result = base.getValue() == Base.DENARY
                    ? diff.toDenary().toString()
                    : diff.toDenary().to(base.getValue()).toString();
            }
            // Sets the working. This is separate from the result, which is set using the ExceptionHandler.
            workingOutput.setText(working.toString());
            return result;
        });
    }
}
