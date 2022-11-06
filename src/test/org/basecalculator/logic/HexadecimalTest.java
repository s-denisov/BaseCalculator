package org.basecalculator.logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HexadecimalTest {

    @Test
    void fromDenaryTest() {
        assertAll("denary to hex converted correctly",
                () -> assertEquals(new Denary(15).to(Base.HEXADECIMAL).toString(), "F"),
                () -> assertEquals(new Denary(2570).to(Base.HEXADECIMAL).toString(), "A0A"),
                () -> assertEquals(new Denary(4318).to(Base.HEXADECIMAL).toString(), "10DE"),
                () -> assertEquals(new Denary(0).to(Base.HEXADECIMAL).toString(), "0"),
                () -> assertEquals(new Denary(1).to(Base.HEXADECIMAL).toString(), "1"),
                () -> assertEquals(new Denary(10).to(Base.HEXADECIMAL).toString(), "A")
        );
    }

    @Test
    void toDenaryTest() {
        assertAll("hex to denary converted correctly",
                () -> assertEquals(new Hexadecimal("FAF").toDenary().toInt(), 4015),
                () -> assertEquals(new Hexadecimal("9D56").toDenary().toInt(), 40_278),
                () -> assertEquals(new Hexadecimal("2F7").toDenary().toInt(), 759),
                () -> assertEquals(new Hexadecimal("A").toDenary().toInt(), 10),
                () -> assertEquals(new Hexadecimal("C").toDenary().toInt(), 12),
                () -> assertEquals(new Hexadecimal("0").toDenary().toInt(), 0),
                () -> assertEquals(new Hexadecimal("1").toDenary().toInt(), 1)
        );
    }

    @Test
    void addTest() {
        assertAll("adds hexadecimal numbers correctly",
                () -> assertEquals(new Hexadecimal("8AB").add("B78").toString(), "1423"),
                () -> assertEquals(new Hexadecimal("ABCDEF").add(new Hexadecimal("1119A")).toString(), "ACDF89"),
                () -> assertEquals(new Hexadecimal("A").add("0").toString(), "A"),
                () -> assertEquals(new Hexadecimal("0").add("F").toString(), "F"),
                () -> assertEquals(new Hexadecimal("0").add("0").toString(), "0")
        );
    }

    @Test
    void toBinaryTest() {
        assertAll("hex to binary converted correctly",
                () -> assertEquals(new Hexadecimal("11").toBinary().toString(), "00010001"),
                () -> assertEquals(new Hexadecimal("F").toBinary().toString(), "00001111"),
                () -> assertEquals(new Hexadecimal("AAF").toBinary().toString(), "00001010 10101111"),
                () -> assertEquals(new Hexadecimal("60E3").toBinary().toString(), "01100000 11100011"),
                () -> assertEquals(new Hexadecimal("A50EAAA").toBinary().toString(), "00001010 01010000 11101010 10101010"),
                () -> assertEquals(new Hexadecimal("0").toBinary().toString(), "00000000")
        );
    }

    @Test
    void otherConversion() {
        assertAll("converts to other bases correctly",
                () -> assertEquals(new Hexadecimal("14EBE2").to(Base.BCD).toString(), "0000 0001 0011 0111 0001 0001 0000 0110"),
                () -> assertEquals(new Hexadecimal("9B9A7").to(Base.OCTAL).toString(), "2334647"),
                () -> {
                    Hexadecimal sampleValue = new Hexadecimal("1B4CD2");
                    assertEquals(sampleValue.to(Base.HEXADECIMAL), sampleValue);
                }
        );
    }

    @Test
    void invalidInputTest() {
        ArrayList<Byte> sampleHexArray = new ArrayList<>();
        sampleHexArray.add((byte) 1); sampleHexArray.add((byte) 10);
        assertAll("throws exceptions correctly",
                () -> assertThrows(IllegalArgumentException.class, () -> new Denary(-1).to(Base.HEXADECIMAL)),
                () -> assertThrows(NumberBaseException.class, () -> new Hexadecimal("AG1")),
                () -> assertThrows(NumberBaseException.class, () -> new Hexadecimal("+")),
                () -> assertThrows(NumberBaseException.class, () -> new Hexadecimal("1+1")),
                () -> assertThrows(NumberBaseException.class, () -> new Hexadecimal("")),
                () -> assertEquals(new Hexadecimal(sampleHexArray).toString(), "1A"),
                () -> assertThrows(NumberBaseException.class, () -> {
                    sampleHexArray.add((byte) 16);
                    new Hexadecimal(sampleHexArray);
                })
        );
    }
}