package org.basecalculator.logic;

public enum Base {
    DENARY, BINARY, HEXADECIMAL, OCTAL, BCD("BCD"); // BCD is kept in lowercase
    private final String value;

    // Like with SignedType, each possible option has a custom value
    Base(String value) {
        this.value = value;
    }

    // We define a constructor without arguments to provide a default way to convert to String,
    //   but also a constructor with a String argument in order to manually override it.
    // The default method is to keep the first letter as capital, but convert all other letter to lowercase
    Base() {
        String inCapitals = super.toString();
        this.value = inCapitals.charAt(0) + inCapitals.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
