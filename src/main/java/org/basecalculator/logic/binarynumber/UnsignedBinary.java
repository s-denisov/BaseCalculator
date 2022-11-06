package org.basecalculator.logic.binarynumber;

import org.basecalculator.logic.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class UnsignedBinary extends BinaryNumber implements NumberBase {

    public UnsignedBinary(ArrayList<Byte> value, byte bytes) {
        super(value, bytes);
        setBytes(bytes);
    }
    public UnsignedBinary(ArrayList<Byte> value) { super(value); }
    public UnsignedBinary(String value, byte bytes) { super(value, bytes); }
    public UnsignedBinary(String value) { super(value); }

    public SignedBinary toSigned() {
        SignedBinary b = new SignedBinary(getValue(), getBytes());
        b.setAutomaticBytes(automaticBytes);
        b.setWorking(working);
        return b;
    }

    private AbstractMap.SimpleEntry<Byte, Byte> addBits(byte b1, byte b2, byte b3) {
        byte denaryTotal =  (byte) (b1 + b2 + b3);
        if (denaryTotal == 3) {
            return new AbstractMap.SimpleEntry<>((byte) 1, (byte) 1);
        } else if (denaryTotal == 2) {
            return new AbstractMap.SimpleEntry<>((byte) 1, (byte) 0);
        } else {
            return new AbstractMap.SimpleEntry<>((byte) 0, denaryTotal);
        }
    }

    public static UnsignedBinary convertByChunks(ArrayList<Byte> value, byte numberOfDigits, WorkingLogger working) {
        ArrayList<Byte> result = new ArrayList<>();
        for (byte digit : value) {
            ArrayList<Byte> chunk = new Denary(digit).toBinary().getValue();
            List<Byte> truncated = chunk.subList(8 - numberOfDigits, 8);
            working.append(digit).append(" = ").append(WorkingLogger.listToString(truncated)).append("\n");
            result.addAll(truncated);
        }
        working.append("\n");
        return (UnsignedBinary) new UnsignedBinary(result).setWorking(working);
    }

    @Override
    public UnsignedBinary toBinary() {
        return this;
    }

    private byte valueSectionToDenary(int start, int length) {
        ArrayList<Byte> section = new ArrayList<>(value.subList(start, start + length));
        return (byte) NumberBaseImplementation.toDenary(section, (byte) 2, working).toInt();
    }

    public Hexadecimal toHexadecimal() {
        ArrayList<Byte> hexResult = new ArrayList<>();
        for (int i = 0; i < bytes * 8; i += 4) {
            hexResult.add(valueSectionToDenary(i, 4));
        }
        return new Hexadecimal(hexResult);
    }

    public Octal toOctal() {
        ArrayList<Byte> octResult = new ArrayList<>();
        int remainder = bytes * 8 % 3;
        octResult.add(valueSectionToDenary(0, remainder));
        for (int i = remainder; i < bytes * 8; i += 3) {
            octResult.add(valueSectionToDenary(i, 3));
        }
        return new Octal(octResult);
    }

    public BCD toBCD() {
        return (BCD) toDenary().to(Base.BCD);
    }

    @Override
    public NumberBase to(Base base) {
        switch(base) {
            case DENARY: return toDenary();
            case BINARY: return this;
            case HEXADECIMAL: return toHexadecimal();
            case OCTAL: return toOctal();
            case BCD: return toBCD();
            default: throw new IllegalStateException("This should not happen");
        }
    }

    public UnsignedBinary add(UnsignedBinary num2) {
        byte bytesNeeded = (byte) Math.max(bytes, num2.bytes);
        setBytes(bytesNeeded, false); num2.setBytes(bytesNeeded, false);
        working.append(this).append(" + ").append(num2).append("\n");
        int i = bytesNeeded * 8 - 1;
        byte carry = 0;
        StringBuilder total = new StringBuilder();
        while (i >= 0) {
            AbstractMap.SimpleEntry<Byte, Byte> results = addBits(value.get(i), num2.getValue().get(i), carry);
            working.append(value.get(i)).append(" + ").append(num2.getValue().get(i)).append(" + ").append(carry).append(" = ")
                    .append(results.getValue()).append(" carry ").append(results.getKey()).append("\n");
            carry = results.getKey();
            total.insert(0, results.getValue());
            i--;
        }
        if (carry != 0) {
            working.append("\n");
            if (automaticBytes && num2.automaticBytes) {
                incrementBytes();
                num2.incrementBytes();
                return add(num2);
            }
            throw new BinaryOverflowException("", total.toString());
        }
        UnsignedBinary u = new UnsignedBinary(total.toString(), bytesNeeded);
        u.setAutomaticBytes(automaticBytes);
        u.setWorking(working);
        working.append(u).append("\n\n");
        return u;
    }

    public UnsignedBinary add(String num2) {
        return add(new UnsignedBinary(num2));
    }

    public NumberBase add(NumberBase num2) {
        return add(num2.toBinary());
    }

    @Override
    public UnsignedBinary setBytes(byte bytes) {
        return (UnsignedBinary) super.setBytes(bytes);
    }
}
