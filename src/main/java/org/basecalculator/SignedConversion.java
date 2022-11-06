package org.basecalculator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.basecalculator.logic.Denary;
import org.basecalculator.logic.WorkingLogger;
import org.basecalculator.logic.binarynumber.SignedBinary;
import org.basecalculator.logic.binarynumber.SignedType;

public class SignedConversion implements ChangeListener<String> {

    @FXML private ChoiceBox<SignedType> signedType;
    @FXML private Label result;
    @FXML private ToggleGroup conversionType;
    @FXML private RadioButton toBinary;
    @FXML private RadioButton toDenary;
    @FXML private Spinner<Integer> binaryBytes;
    @FXML private TextField input;
    @FXML private Label formattedBinary;
    @FXML private Label workingOutput;
    private ExceptionHandler handler;

    public void initialize() {
        // Sets the ChoiceBox's possible values to the possible values of the SignedType enum
        //   (i.e. the signed binary types: two's complement, one's complement and sign/magnitude)
        signedType.getItems().setAll(SignedType.values());
        signedType.setValue(SignedType.TWOS_COMPLEMENT); // Is two's complement by default
        input.textProperty().addListener(this); // Calls the "changed" function whenever the value
                                                            // of the input TextField is changed
        handler = new ExceptionHandler(result);
    }

    // This adds shows the binary number the user inputted, but with leading zeros so that it has the correct number of bits.
    // This is important for signed binary, because the first digit determines the sign, so users need to be shown if leading zeros
    //   have been added to their input, so that the can get the results they expect.
    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // Catches any errors and prints them to the result text. We don't use setResultText, because we don't want to change
        //   the result text, unless there are errors.
        handler.catchErrors(() -> {
            // This is only necessary if we convert from binary to denary
            if (conversionType.getSelectedToggle() == toDenary) {
                // Creates a signed binary number with the specified value and number of bytes.
                // It is then formatted using existing functions.
                SignedBinary num = new SignedBinary(newValue, binaryBytes.getValue().byteValue());
                formattedBinary.setText(num.toString());
            } else { // If we convert from denary to binary then the formattedBinary label is reset
                formattedBinary.setText("");
            }
        });
    }

    @FXML
    public void convertSigned() {
        handler.setResultText(() -> {
            String convertedNum = "";
            WorkingLogger working = new WorkingLogger(new StringBuilder());
            if (conversionType.getSelectedToggle() == toBinary) { // If the denary to binary radio button has been selected
                Denary num = new Denary(input.getText());
                num.setWorking(working);
                // Converts the denary number to SignedBinary using the specified type and sets the number of bytes
                SignedBinary binNum = num.toSignedBinary(signedType.getValue()).setBytes(binaryBytes.getValue().byteValue());
                convertedNum = binNum.toString();
            } else if (conversionType.getSelectedToggle() == toDenary) {
                // Creates a SignedBinary from the input text, bytes and representation
                SignedBinary num = new SignedBinary(input.getText(), binaryBytes.getValue().byteValue());
                num.setWorking(working);
                num.setRepresentation(signedType.getValue());
                convertedNum = num.toDenary().toString();
            }
            workingOutput.setText(working.toString()); // Outputs the working out
            return convertedNum;
        });
    }
}
