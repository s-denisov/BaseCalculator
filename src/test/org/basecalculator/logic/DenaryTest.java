package org.basecalculator.logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DenaryTest {

    @Test
    void additionTest() {
        assertAll("adds numbers correctly",
                () -> assertEquals(((Denary) new Denary(123).add("1000")).toInt(), 1123),
                () -> assertEquals(((Denary) new Denary(1_234_567).add("9 753 111")).toInt(), 10_987_678)
        );
    }

    @Test
    void creationTest() {
        assertAll("creates valid denary numbers correctly",
                () -> assertEquals(new Denary(0).toInt(), 0),
                () -> {
                    ArrayList<Byte> sampleValue = new ArrayList<>();
                    sampleValue.add((byte) 1);
                    sampleValue.add((byte) 9);
                    sampleValue.add((byte) 7);
                    sampleValue.add((byte) 2);
                    assertEquals(new Denary(sampleValue).toInt(), 1972);
                },
                () -> {
                    Denary sampleDenary = new Denary("1 234 567 890");
                    assertEquals(sampleDenary.toDenary(), sampleDenary);
                }
        );
    }

    @Test
    void errorTest() {
        assertAll("throws exceptions for invalid input correctly",
                () -> assertThrows(NumberBaseException.class, () -> new Denary("a")),
                () -> assertThrows(NumberBaseException.class, () -> new Denary("123.12")),
                () -> assertThrows(NumberBaseException.class, () -> {
                    ArrayList<Byte> sampleValue = new ArrayList<>();
                    sampleValue.add((byte) 1);
                    sampleValue.add((byte) 9);
                    sampleValue.add((byte) 10);
                    new Denary(sampleValue);
                })
        );
    }
}