package org.basecalculator.logic;

// This custom exception is used throughout the application. We only catch NumberBaseExceptions, in order
//   to avoid catching Java exceptions, which could make debugging harder
public class NumberBaseException extends IllegalArgumentException {
    public NumberBaseException(String message) {
        super(message);
    }
}
