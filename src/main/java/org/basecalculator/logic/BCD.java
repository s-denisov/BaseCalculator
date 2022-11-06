package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;

public class BCD extends NumberBaseImplementation {

    // Defines the constants used in NumberBaseImplementation
    @Override protected byte maximumDigit() { return 1; }
    @Override protected Base thisBase() { return Base.BCD; }
    @Override protected int digitSeparator() { return 4; }
    @Override protected boolean removeLeadingZeros() { return false; }

    public BCD(ArrayList<Byte> value) {
        super(value);
    }

    public BCD(String value) {
        super(value);
    }

    // Keeps all the default conversions except toDenary
    @Override
    public Denary toDenary() {
        // Converting the binary number with the same value to hexadecimal results in the desired result.
        // This is because binary is converted to hexadecimal in nibbles, and so is BCD to denary.
        ArrayList<Byte> denaryDigits = new UnsignedBinary(value).toHexadecimal().getValue();
        // However, we need to check that the result is valid, as hexadecimal can have values from 10 to 15, which
        //   are invalid in denary
        // An Exception would have also been thrown if we tried converting this to denary immediately, but the exception would
        //   be unclear, as it would be intended for Denary not BCD. Therefore we throw a custom exception.
        for (byte digit : denaryDigits) {
            if (digit < 0 || digit > 9) {
                throw new NumberBaseException("All BCD nibbles must be 0000 to 1001");
            }
        }
        return (Denary) new Denary(denaryDigits).setWorking(working);
    }
}
