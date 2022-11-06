package org.basecalculator.logic;

import org.basecalculator.logic.binarynumber.UnsignedBinary;

import java.util.ArrayList;

// Contains functions to convert NumberBases between each other.
// This enables polymorphism, as operations can be performed on NumberBases without knowing their specific type.
// It also extends WorkingLoggable, so that all NumberBases allow working to be logged.
// Notably, signed binary is not a number bases, as it can't be converted to most bases (e.g. hexadecimal).
public interface NumberBase extends WorkingLoggable {
    ArrayList<Byte> getValue();
    Denary toDenary();
    UnsignedBinary toBinary();
    NumberBase to(Base base);
    NumberBase add(String num2);
    NumberBase add(NumberBase num2);
}
