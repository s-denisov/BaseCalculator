package org.basecalculator.logic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BCDTest {

    @Test
    void fromDenaryTest() {
        assertAll("denary to bcd converted correctly",
                () -> assertEquals(new Denary(91427).to(Base.BCD).toString(), "0000 1001 0001 0100 0010 0111"),
                () -> assertEquals(new Denary(0).to(Base.BCD).toString(), "0000 0000"),
                () -> assertEquals(new Denary(128).to(Base.BCD).toString(), "0000 0001 0010 1000"),
                () -> assertEquals(new Denary(10).to(Base.BCD).toString(), "0001 0000")
        );
    }

    @Test
    void toDenaryTest() {
        assertAll("bcd to denary converted correctly",
                () -> assertEquals(new BCD("1001 0010").toDenary().toInt(), 92),
                () -> assertEquals(new BCD("0000 0000").toDenary().toInt(), 0),
                () -> assertEquals(new BCD("  0 ").toDenary().toInt(), 0),
                () -> assertEquals(new BCD("00 000001").toDenary().toInt(), 1),
                () -> assertEquals(new BCD("0 1001").toDenary().toInt(), 9),
                () -> assertEquals(new BCD("001 1").toDenary().toInt(), 3),
                () -> assertEquals(new BCD("11 0001").toDenary().toInt(), 31),
                () -> assertEquals(new BCD("1001100 0 0111 0110 0101 0100 0011 0000").toDenary().toInt(), 98765430)
        );
    }

    @Test
    void additionTest() {
        assertAll("adds bcd correctly",
                () -> assertEquals(new Denary(508).to(Base.BCD).add(new Denary(240).to(Base.BCD)).toDenary().toInt(), 748),
                () -> assertEquals(new BCD("1001 1001 1000").add("0110 1001 0001").toString(), "0001 0110 1000 1001"),
                () -> assertEquals(new BCD("00000").add("000000000").toString(), "0000 0000"),
                () -> assertEquals(new BCD("0").add("000000000").toString(), "0000 0000"),
                () -> assertEquals(new BCD("11").add("11").toString(), "0000 0110")
        );
    }

    @Test
    void valueTest() {
        BCD sample = new BCD("1001 0000 0010 0111");
        ArrayList<Byte> result = sample.getValue();
        assertAll("value returned correctly",
                () -> assertEquals(result.get(0), (byte) 1),
                () -> assertEquals(result.get(1), (byte) 0),
                () -> assertEquals(result.get(2), (byte) 0),
                () -> assertEquals(result.get(3), (byte) 1),
                () -> assertEquals(result.get(5), (byte) 0),
                () -> assertEquals(result.get(12), (byte) 0),
                () -> assertEquals(result.get(13), (byte) 1),
                () -> assertEquals(result.get(14), (byte) 1),
                () -> assertEquals(result.get(15), (byte) 1),
                () -> assertEquals(sample.to(Base.BCD), sample)
        );
    }

    @Test
    void otherConversions() {
        assertAll("converts correctly to bases other than denary",
                () -> assertEquals(new BCD("0010 0101 0101").toBinary().toString(), "11111111"),
                () -> assertEquals(new BCD("1001 0110 0111").to(Base.HEXADECIMAL).toString(), "3C7"),
                () -> assertEquals(new BCD("0001 1000 0010 0011").to(Base.OCTAL).toString(), "3437")
        );
    }

    @Test
    void errorTest() {
        assertAll("throws exceptions correctly",
                () -> assertThrows(NumberBaseException.class, () -> new BCD("1111").toDenary()),
                () -> assertThrows(NumberBaseException.class, () -> new BCD("1000 0001 1010 0000").toBinary()),
                () -> assertThrows(NumberBaseException.class, () -> new BCD("1000 1001 1010 1111").to(Base.HEXADECIMAL)),
                () -> assertThrows(NumberBaseException.class, () -> new BCD("11111111111111").to(Base.OCTAL))
        );
    }
}