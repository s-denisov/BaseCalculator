package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.SignedBinary;
import org.basecalculator.logic.binarynumber.SignedType;
import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;

public class Denary extends NumberBaseImplementation {

    @Override protected byte maximumDigit() { return 9; }
    @Override protected Base thisBase() { return Base.DENARY; }
    @Override protected int digitSeparator() { return 3; }

    private int intValue;

    public Denary(ArrayList<Byte> digits) {
        super(digits);
        intValue = Integer.parseInt(toString().replace(" ", ""));
    }

    public Denary(String value) {
        super(value.substring(value.indexOf('-') + 1));
        value = removeSeparators(value);
        char first = value.charAt(0);
        if (value.length() > 10 || (value.length() == 10 && first != '0' && first != '1')) {
            throw new NumberBaseException("Denary numbers must be less than 2 billion");
        }
        intValue = Integer.parseInt(value);
    }

    public Denary(int value) {
        this(Integer.toString(value));
        intValue = value;
    }

    private ArrayList<Byte> fromDenary(int num, byte base) {
        ArrayList<Byte> total = new ArrayList<>();
        if (num == 0) {
            total.add((byte) 0);
            return total;
        }
        while (num > 0) {
            byte remainder = (byte) (num % base);
            int integerDivisionResult = num / base;
            working.append(num).append(" / ").append(base).append(" = ")
                    .append(integerDivisionResult).append(" r ").append(remainder).append("\n");
            num = integerDivisionResult;
            total.add(0, remainder);
        }
        working.append(total).append("\n\n");
        return total;
    }

    public Denary negate() {
        Denary num = new Denary(-intValue);
        num.setWorking(working);
        return num;
    }

    public int toInt() {
        return intValue;
    }

    @Override
    public Denary toDenary() {
        return this;
    }

    @Override
    public NumberBase to(Base base) {
        switch(base) {
            case BINARY: return working.create(fromDenary(intValue, (byte) 2), Base.BINARY);
            case HEXADECIMAL: return working.create(fromDenary(intValue, (byte) 16), Base.HEXADECIMAL);
            case OCTAL: return working.create(fromDenary(intValue, (byte) 8), Base.OCTAL);
            case BCD: return working.create(UnsignedBinary.convertByChunks(getValue(), (byte) 4, working).getValue(), Base.BCD);
            default: return super.to(base);
        }
    }

    public SignedBinary toSignedBinary(SignedType type) {
        if (intValue >= 0) {
            UnsignedBinary unsigned = toBinary();
            if (unsigned.getValue().get(0) == (byte) 1) {
                unsigned.incrementBytes();
            }
            return unsigned.toSigned().setRepresentation(type);
        } else {
            Denary positiveDen = new Denary(-intValue);
            positiveDen.setWorking(working);
            UnsignedBinary positive = positiveDen.toBinary();
            boolean lowestNegative = false;
            if (positive.getValue().get(0) == (byte) 1) {
                lowestNegative = true;
                for (int i = 1; i < positive.getValue().size(); i++) {
                    if (type != SignedType.TWOS_COMPLEMENT || positive.getValue().get(i) == (byte) 1) {
                        positive.incrementBytes();
                        lowestNegative = false;
                        break;
                    }
                }
            }
            return lowestNegative
                    ? positive.toSigned().setRepresentation(type)
                    : positive.toSigned().setRepresentation(type).negate();
        }
    }

    public SignedBinary toSignedBinary() {
        return toSignedBinary(SignedType.TWOS_COMPLEMENT);
    }

    @Override
    public String toString() {
        if (intValue < 0) return "-" + negate().toString();
        return super.toString();
    }
}
