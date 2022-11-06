package org.basecalculator.logic.binarynumber;

import org.basecalculator.logic.NumberBaseException;

// It extends NumberBaseException, so that we can catch it whenever we expect a NumberBaseException(i.e. an error from this application).
// However, we use a separate class so that we can pass in the binaryValue, which allows operations to be continued if it turns out we
//   don't need to worry about the exception (e.g. if we are allowed to increase the number of bytes).
public class BinaryOverflowException extends NumberBaseException {
    final String binaryValue;
    BinaryOverflowException(String message, String binaryValue) {
        super(message);
        this.binaryValue = binaryValue;
    }
}
