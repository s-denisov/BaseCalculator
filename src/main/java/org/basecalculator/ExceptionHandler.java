package org.basecalculator;

import javafx.scene.control.Label;
import org.basecalculator.logic.NumberBaseException;

import java.util.function.Supplier;

// This is used throughout all 3 calculation files
// It contains reusable code for error handling
public class ExceptionHandler {

    private final Label result;

    public ExceptionHandler(Label result) {
        this.result = result;
    }

    // This takes a function which computes the result (it has no arguments and returns String).
    // The function is ran and, if successful, then the result color is set to "black"
    //   and its text is set to the result of the function.
    // If the function results in an error, then it is handled in the "catch" block of "catchErrors"
    public void setResultText(Supplier<String> resultFinder) {
        catchErrors(() -> {
            String resultString = resultFinder.get();
            // The result color is set to black every time, as it may be red due to an error happening before.
            result.setStyle("-fx-text-fill: black;");
            result.setText(resultString);
        });
    }

    // This takes a function which takes no arguments and has no return value
    // The function is ran using try-catch. If there are no errors then nothing else is done.
    // If there are errors, then they are shown in red
    public void catchErrors(Runnable problematicFn) {
        try {
            problematicFn.run();
        } catch (NumberBaseException e) { // Only catches errors from this application
            result.setStyle("-fx-text-fill: red;");
            result.setText(e.getMessage());
        }
    }
}
