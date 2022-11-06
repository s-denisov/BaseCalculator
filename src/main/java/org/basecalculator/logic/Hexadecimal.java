package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;

public class Hexadecimal extends NumberBaseImplementation {

    @Override protected byte maximumDigit() { return 15; }
    @Override protected Base thisBase() { return Base.HEXADECIMAL; }

    public Hexadecimal(ArrayList<Byte> value) {
        super(value);
    }

    public Hexadecimal(String value) {
        super(value);
    }

    @Override
    protected byte charToDenary(char c) {
        if (65 <= c && c <= 70) { // If the char is 'A' to 'F' then it converted to 10 to 15
            return (byte) (c - 55); // as 'A' has a value of 55
        }
        return super.charToDenary(c);
    }

    @Override
    protected char denaryToChar(byte num) {
        if (10 <= num && num <= 15) {
            return (char) (num + 55); // Adds 55 for the additional numbers, as 65 is 'A', 66 is 'B' etc.
        }
        return super.denaryToChar(num);
    }

    @Override
    public NumberBase to(Base base) {
        if (base == Base.BINARY) {
            // Converts to binary by converting each digit into a 4 bit byte.
            UnsignedBinary b = UnsignedBinary.convertByChunks(value, (byte) 4, working);
            b.setWorking(working);
            return b;
        }
        return super.to(base);
    }
}
