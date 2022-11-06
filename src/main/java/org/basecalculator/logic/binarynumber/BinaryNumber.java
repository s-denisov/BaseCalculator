package org.basecalculator.logic.binarynumber;

import org.basecalculator.logic.*;

import java.util.ArrayList;

public abstract class BinaryNumber implements WorkingLoggable {
    protected ArrayList<Byte> value;
    protected byte bytes;
    protected boolean automaticBytes = false;
    protected WorkingLogger working = new WorkingLogger(new StringBuilder());

    public BinaryNumber(ArrayList<Byte> value, byte bytes) {
        if (bytes < bytesNeeded(value.size())) {
            throw new NumberBaseException("Not enough bytes");
        }
        for (byte c : value) {
            if (c != 1 && c != 0) {
                throw new NumberBaseException("Invalid binary: all elements must be 0 or 1");
            }
        }
        this.value = value;
        addLeadingZeros(bytes);
    }

    public BinaryNumber(ArrayList<Byte> value) {
        this(value, bytesNeeded(value.size()));
        automaticBytes = true;
    }

    public BinaryNumber(String value, byte bytes) {
        this(strValueToList(value), bytes);
    }

    public BinaryNumber(String value) {
        this(strValueToList(value));
        automaticBytes = true;
    }

    private static byte bytesNeeded(int length) {
        return (byte) Math.ceil((double) length / 8);
    }

    private static ArrayList<Byte> strValueToList(String value) {
        ArrayList<Byte> valueArray = new ArrayList<>();
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '1') {
                valueArray.add((byte) 1);
            } else if (value.charAt(i) == '0') {
                valueArray.add((byte) 0);
            } else if (value.charAt(i) != ' ') {
                throw new NumberBaseException("Invalid binary: all characters must be 0, 1 or a space");
            }
        }
        return valueArray;
    }

    public ArrayList<Byte> getValue() { return value; }
    public byte getBytes() { return bytes; }

    // Separate from setBytes, because leading zeros are always added in the constructor, even for negative SignedBinary,
    // but SignedBinary adds leading 1s in setBytes if the number is negative.
    protected void addLeadingZeros(byte bytes) {
        checkBytes(bytes);
        this.bytes = bytes;
        int difference = bytes * 8 - value.size();
        for (int i = 0; i < difference; i ++) {
            value.add(0, (byte) 0);
        }
    }

    protected BinaryNumber setBytes(byte bytes, boolean updateAuto) {
        minimizeBytes();
        addLeadingZeros(bytes);
        if (updateAuto) automaticBytes = false;
        return this;
    }

    public BinaryNumber setBytes(byte bytes) {
        return setBytes(bytes, true);
    }

    public void incrementBytes() {
        setBytes((byte) (bytes + 1));
    }

    protected BinaryNumber setAutomaticBytes(boolean auto) {
        automaticBytes = auto;
        return this;
    }

    protected void checkBytes(byte bytes) {
        if (bytes > 4 || bytes < 1) {
            throw new NumberBaseException("All numbers must be representable in 1 to 4 bytes");
        } else if (value.size() > bytes * 8) {
            throw new NumberBaseException("Not enough bytes");
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= value.size(); i++) {
            result.append(value.get(i - 1));
            if (i % 8 == 0 && i != bytes * 8) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public Denary toDenary() {
        Denary num = NumberBaseImplementation.toDenary(value, (byte) 2, working);
        num.setWorking(working);
        return num;
    }

    @Override
    public BinaryNumber setWorking(WorkingLogger newWorking) {
        working = newWorking;
        return this;
    }

    @Override
    public String getWorking() {
        return working.toString();
    }

    public void minimizeBytes() {
        boolean changed = true;
        while (changed) {
            changed = removeByteWithSingleBit((byte) 0);
        }
    }

    protected boolean removeByteWithSingleBit(byte bit) {
        if (bytes <= 1 || value.size() <= 8) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            if (value.get(i) != bit) {
                return false;
            }
        }
        value.subList(0, 8).clear();
        bytes -= 1;
        return true;
    }
}
