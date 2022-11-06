package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;

public class Octal extends NumberBaseImplementation {

    @Override protected byte maximumDigit() { return 7; }
    @Override protected Base thisBase() { return Base.OCTAL; }

    public Octal(ArrayList<Byte> value) {
        super(value);
    }

    public Octal(String value) {
        super(value);
    }

    @Override
    public NumberBase to(Base base) {
        if (base == Base.BINARY) {
            // Converts to binary, by converting each digit into a 3 bit binary and then concatenating them.
            UnsignedBinary extraZeros = UnsignedBinary.convertByChunks(value, (byte) 3, working);
            extraZeros.minimizeBytes(); // Extra zeros might have been added, which are removed.
            extraZeros.setWorking(working);
            return extraZeros;
        }
        return super.to(base); // If base isn't binary, uses the default conversion (i.e. through denary)
    }
}
