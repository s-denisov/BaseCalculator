package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;
import java.util.List;

public class WorkingLogger {

    private final StringBuilder working;

    public WorkingLogger(StringBuilder working) {
        this.working = working;
    }

    // Appends to the working. This can take any object, as the object is converted to a string.
    // This is convenient as it can allow logging number bases.
    public WorkingLogger append(Object value) {
        working.append(value.toString());
        return this; // Returns the logger, so that append calls can be chained.
    }

    @Override
    public String toString() {
        return working.toString();
    }

    // The "create" function takes a value for a number and its base.
    // This allows numbers to be created, with their bases determined dynamically.
    // The number's working is set to this object. This makes it easy to make sure all of the numbers
    // have the same working out, which can then be outputted to the user.
    public NumberBase create(ArrayList<Byte> num, Base base) {
        NumberBase inBase = null;
        switch(base) {
            case DENARY: inBase = new Denary(num); break;
            case BINARY: inBase =  new UnsignedBinary(num); break;
            case HEXADECIMAL: inBase = new Hexadecimal(num); break;
            case OCTAL: inBase =  new Octal(num); break;
            case BCD: inBase = new BCD(num);
        }
        inBase.setWorking(this);
        return inBase;
    }

    // The "create" method is overloaded, so that it can take either a String or an ArrayList,
    // in order to increase convenience
    public NumberBase create(String num, Base base) {
        NumberBase inBase = null;
        switch(base) {
            case DENARY: inBase = new Denary(num); break;
            case BINARY: inBase =  new UnsignedBinary(num); break;
            case HEXADECIMAL: inBase = new Hexadecimal(num); break;
            case OCTAL: inBase =  new Octal(num); break;
            case BCD: inBase = new BCD(num);
        }
        inBase.setWorking(this);
        return inBase;
    }

    // A list is printed in the format [1, 2, 3]
    // This removes all "["    ","    " "     "]" characters to convert the list to a string. Thus the above list becomes 123.
    public static String listToString(List<Byte> list) {
        return list.toString().replaceAll("[],\\[ ]", "");
    }
}
