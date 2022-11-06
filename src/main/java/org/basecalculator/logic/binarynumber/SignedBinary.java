package org.basecalculator.logic.binarynumber;

import org.basecalculator.logic.Denary;

import java.util.ArrayList;

public class SignedBinary extends BinaryNumber {

    private SignedType representation = SignedType.TWOS_COMPLEMENT;

    public SignedBinary setRepresentation(SignedType representation) {
        this.representation = representation;
        return this;
    }

    public SignedBinary(ArrayList<Byte> value, byte bytes) {
        super(value, bytes);
    }

    public SignedBinary(ArrayList<Byte> value) {
        super(value);
    }

    public SignedBinary(String value, byte bytes) {
        super(value, bytes);
    }

    public SignedBinary(String value) {
        super(value);
    }

    public SignedBinary negate() {
        switch(representation) {
            case TWOS_COMPLEMENT: return twosComplement();
            case ONES_COMPLEMENT: return onesComplement();
            case SIGN_MAGNITUDE: return oppositeSignMagnitude();
            default: throw new IllegalStateException("This should not happen");
        }
    }

    public SignedBinary add(SignedBinary num2) {
        BinaryOverflowException overflowException;
        try {
            byte bytesNeeded = (byte) Math.max(bytes, num2.bytes);
            setBytes(bytesNeeded, false); num2.setBytes(bytesNeeded, false);
            UnsignedBinary thisUnsigned = new UnsignedBinary(this.value, bytes);
            UnsignedBinary num2Unsigned = new UnsignedBinary(num2.value, num2.bytes);
            thisUnsigned.setWorking(working); num2Unsigned.setWorking(working);
            SignedBinary tentativeResult = thisUnsigned.add(num2Unsigned).toSigned();
            overflowException = new BinaryOverflowException("", tentativeResult.toString());
        } catch(BinaryOverflowException e) {
            overflowException = e;
        }
        boolean bothNegativeOverflow = isNegative() && num2.isNegative() && overflowException.binaryValue.charAt(0) == '0';
        boolean bothPositiveOverflow = !isNegative() && !num2.isNegative() && overflowException.binaryValue.charAt(0) == '1';
        if (bothNegativeOverflow || bothPositiveOverflow) {
            if (automaticBytes && num2.automaticBytes) {
                incrementBytes();
                num2.incrementBytes();
                return add(num2);
            }
            throw overflowException;
        }
        return setValue(overflowException.binaryValue);
    }

    public SignedBinary add(String num2) {
        return add(new SignedBinary(num2));
    }

    public SignedBinary onesComplement() {
        working.append("Start: ").append(this).append("\n");
        ArrayList<Byte> flipped = new ArrayList<>();
        for (byte num : value) {
            byte newNum = (byte) (num == 0 ? 1 : 0);
            flipped.add(newNum);
        }
        SignedBinary b = setValue(flipped);
        working.append("Flipped: ").append(b).append("\n");
        return b;
    }

    public SignedBinary twosComplement() {
        ArrayList<Byte> flipped = onesComplement().getValue();
        int index = flipped.size() - 1;
        while (index >= 0 && flipped.get(index) == 1) {
            flipped.set(index, (byte) 0);
            index--;
        }
        if (index >= 0) flipped.set(index, (byte) 1);
        // flipped is 10000000
        SignedBinary b;
        if (flipped.equals(value) && isNegative()) {
            if (automaticBytes) {
                for (int i = 0; i < 8; i++) {
                    flipped.add(0, (byte) 0);
                }
                b = new SignedBinary(flipped, (byte) (bytes + 1));
                b.setAutomaticBytes(automaticBytes);
                b.setWorking(working);
            } else {
                throw new BinaryOverflowException("The two's complement of this number can't be represented in the same number of bytes", toString());
            }
        } else {
            b = setValue(flipped);
        }
        working.append("Add 1: ").append(b).append("\n\n");
        return b;
    }

    public SignedBinary oppositeSignMagnitude() {
        SignedBinary copy = setValue(value);
        copy.value.set(0, (byte) (copy.value.get(0) == 1 ? 0 : 1));
        return copy;
    }

    public boolean isNegative() {
        return value.get(0) == 1;
    }

    @Override
    public Denary toDenary() {
        if (isNegative()) {
            return negate().toDenary().negate();
        } else {
            return super.toDenary();
        }
    }

    @Override
    protected SignedBinary setBytes(byte bytes, boolean updateAuto) {
        if (value.isEmpty() || value.size() % 8 != 0 || !isNegative()) {
            super.setBytes(bytes, updateAuto);
        } else {
            if (bytes != this.bytes) {
                checkBytes(bytes);
                this.value = negate().setBytes(bytes).negate().value;
                this.bytes = bytes;
            }
            if (updateAuto) {
                automaticBytes = false;
            }
        }
        return this;
    }

    @Override
    public SignedBinary setBytes(byte bytes) {
        return setBytes(bytes, true);
    }

    public SignedBinary setValue(ArrayList<Byte> value) {
        SignedBinary s = (SignedBinary) new SignedBinary(value, bytes).setWorking(working).setAutomaticBytes(automaticBytes);
        return s.setRepresentation(representation);
    }

    public SignedBinary setValue(String value) {
        SignedBinary s = (SignedBinary) new SignedBinary(value, bytes).setWorking(working).setAutomaticBytes(automaticBytes);
        return s.setRepresentation(representation);
    }

    @Override
    public void minimizeBytes() {
        boolean changed = value.size() > 8;
        if (isNegative()) {
            while (changed && get8Equals((byte) 1)) {
                changed = removeByteWithSingleBit((byte) 1);
            }
        } else {
            while (changed && get8Equals((byte) 0)) {
                changed = removeByteWithSingleBit((byte) 0);
            }
        }
    }

    private boolean get8Equals(byte num) {
        return value.size() > 8 && value.get(8) == num;
    }
}
