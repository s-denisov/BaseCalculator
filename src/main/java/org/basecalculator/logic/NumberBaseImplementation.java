package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;

// Class is abstract so that it can't be initialized
public abstract class NumberBaseImplementation implements NumberBase {

    // Defines fields that will be inherited by subclasses
    protected final ArrayList<Byte> value; // Uses "Byte", as the numbers are small
    protected WorkingLogger working = new WorkingLogger(new StringBuilder());

    // Defines constants that will be used throughout the other methods.
    // These are defined by each class but some have default values.
    protected abstract byte maximumDigit();
    protected abstract Base thisBase();
    // A value of -1 means the digits aren't separated. A positive value of x means the digits are separated by a space
    //   after every x digits.
    protected int digitSeparator() { return -1; }
    protected boolean removeLeadingZeros() { return true; }
    private final String invalidNumText = "Invalid number: all elements must be 0 to " + maximumDigit();

    // Sets the value immediately
    public NumberBaseImplementation(ArrayList<Byte> value) {
        this.value = value;
        checkValue();
    }

    public NumberBaseImplementation(String value) {
        ArrayList<Byte> equivalentArray = new ArrayList<>();
        value = removeSeparators(value); // Ignores spaces and commas
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            equivalentArray.add(charToDenary(current)); // Converts the char to denary then add it to the denary array
        }
        this.value = equivalentArray;
        checkValue();
    }

    // Removes all commas and spaces. This allows the users to separate digits, making it easier to read the numbers they input
    protected String removeSeparators(String num) {
        return num.replace(" ", "").replace(",", "");
    }

    @Override
    public WorkingLoggable setWorking(WorkingLogger newWorking) {
        working = newWorking; // Sets the working
        return this; // Returns the object to allow chaining of "set" methods
    }

    @Override // Returns the working as a string
    public String getWorking() {
        return working.toString();
    }

    // This method is specifically defined so that it can be overridden by Hexadecimal, without overriding the constructor from String
    protected byte charToDenary(char c) {
        if (48 <= c && c <= 48 + maximumDigit()) {
            return (byte) (c - 48); // Subtracts 48 from the Unicode code of the character and returns the result as an integer (more precisely, "byte")
            // This is possible because the char '0' has ASCII code 48 and so on up to '9', which has code 57.
            // maximumDigit() defines an upper limit, beyond which an error is thrown.
        }
        // We don't need an "else" clause, because if the if statement was ran, then we would have returned from the function
        throw new NumberBaseException(invalidNumText);
    }

    // Inverse of the above: adds 48 to the number then converts it to a char.
    protected char denaryToChar(byte num) {
        return (char) (num + 48);
    }

    protected void checkValue() {
        if (value.size() == 0) {
            throw new NumberBaseException("Can't have empty number");
        }
        for (byte digit : value) {
            if (digit < 0 || digit > maximumDigit()) { // Throws an error if any numbers are out of range
                throw new NumberBaseException(invalidNumText);
            }
        }
    }

    public Denary toDenary() {
        return toDenary(value, (byte) (maximumDigit() + 1), working);
    }

    // Is public and static so that it can be used by UnsignedBinary, which doesn't extend this class
    public static Denary toDenary(ArrayList<Byte> num, byte base, WorkingLogger working) {
        int pointer = num.size() - 1; // Starts from the end
        int multiplier = 1;
        int total = 0;
        working.append(num).append("\n");
        while (pointer >= 0) {
            byte currentDigit = num.get(pointer);
            int result = currentDigit * multiplier; // Multiplies the current digit by the place value multiplier
            // e.g. 1 * 64 = 64,         10 * 256 = 2560
            working.append(currentDigit).append(" * ").append(multiplier).append(" = ").append(result).append(", ");
            // Shows how the above is added to the running total:  33 + 64 = 97
            working.append(total).append(" + ").append(result).append(" = ").append(total + result).append("\n");
            total += result;
            multiplier *= base; // Multiplier is multiplied by the base
            pointer--;
        }
        Denary result = new Denary(total); // Converts the total to denary
        // Prints the result and then an empty line, to separate this visually from other block of calculations
        working.append(result).append("\n\n");
        result.setWorking(working); // Sets working, so that further calculations are recorded
        return result;
    }

    @Override // toBinary() is added separately from the generic "to" methods because it is used so often.
    public UnsignedBinary toBinary() {
        return (UnsignedBinary) to(Base.BINARY);
    }

    @Override
    public NumberBase to(Base base) {
        if (base == thisBase()) return this; // Can convert to current base by simply returning the current value
        return toDenary().to(base); // By default converts to the base by converting to denary and then from denary to the base.
    }

    @Override // Adds bases by converting them to binary first
    public NumberBase add(NumberBase num2) {
        return toBinary().add(num2.toBinary().toString()).to(thisBase());
    }

    @Override
    public NumberBase add(String num2) {
        return add(working.create(num2, thisBase()));
    }

    @Override
    public String toString() {
        if (removeLeadingZeros()) {
            while (value.size() > 1 && value.get(0) == 0) {
                value.remove(0); // Removes 0s from the start while they are there
            }
        }
        StringBuilder result = new StringBuilder();
        // We start from the end because the number of digits might be divisible by the separator (e.g. 12 345)
        for (int i = 1; i <= value.size(); i++) {
            result.insert(0, denaryToChar(value.get(value.size() - i)));
            if (digitSeparator() > 0) {
                if (i % digitSeparator() == 0) { // Adds a space if i is divisible by digitSeparator
                    result.insert(0, " ");
                }
            }
        }
        if (result.charAt(0) == ' ') result.deleteCharAt(0); // An extra, unnecessary separator might have been added. It is deleted.
        return result.toString();
    }

    @Override
    public ArrayList<Byte> getValue() {
        return value;
    }
}
