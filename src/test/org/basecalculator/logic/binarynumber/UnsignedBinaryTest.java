package org.basecalculator.logic.binarynumber;

import org.basecalculator.logic.Denary;
import org.basecalculator.logic.NumberBaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnsignedBinaryTest {

    @Test
    void creationTest() {
        assertAll("binary number created correctly",
                () -> assertThrows(IllegalArgumentException.class, () -> new UnsignedBinary("2")),
                () -> assertEquals(new Denary(2).toBinary().toString(), "00000010"),
                () -> assertEquals(new Denary(500).toBinary().toString(), "00000001 11110100"),
                () -> assertEquals(new Denary(12345678).toBinary().toString(), "10111100 01100001 01001110"),
                () -> assertEquals(new Denary("1").toBinary().toString(), "00000001"),
                () -> assertEquals(new UnsignedBinary("1", (byte) 3).toString(), "00000000 00000000 00000001"),
                () -> assertEquals(new Denary(500).toBinary().setBytes((byte) 4).toString(), "00000000 00000000 00000001 11110100"),
                () -> assertEquals(new Denary(255).toBinary().setBytes((byte) 1).toString(), "11111111"),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(256).toBinary().setBytes((byte) 1)),
                () -> assertEquals(new Denary(65_535).toBinary().setBytes((byte) 2).toString(), "11111111 11111111"),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(65_536).toBinary().setBytes((byte) 2)),
                () -> assertThrows(NumberBaseException.class, () -> new UnsignedBinary("000000000", (byte) 1)),
                () -> assertEquals(new UnsignedBinary("  111  111  11").toString(), "11111111")
        );
    }

    @Test
    void additionTest() {
        assertAll("binary numbers are added correctly",
                () -> assertEquals(new UnsignedBinary("11").add(new UnsignedBinary("101")).toString(), "00001000"),
                () -> assertEquals(new UnsignedBinary("11110000").add("0100100110").toString(), "00000010 00010110"),
                () -> assertEquals(new UnsignedBinary("11111101010").add("0101001001111").toString(), "00010010 00111001"),
                () -> assertEquals(new UnsignedBinary("10").add(new UnsignedBinary("10").add(new UnsignedBinary("10"))).toString(), "00000110"),
                () -> assertEquals(new Denary(25_991).toBinary().add(new Denary(176_121).toBinary()).toDenary().toInt(), 202_112),
                () -> assertEquals(new Denary(25_991).toBinary().add(new Denary(176_121).toBinary()).toDenary().toInt(), 202_112),
                () -> assertEquals(new Denary(250).toBinary().add(new Denary(100).toBinary()).toDenary().toInt(), 350),
                () -> assertThrows(BinaryOverflowException.class, () -> new Denary(250).toBinary().setBytes((byte) 1).add(new Denary(100).toBinary().setBytes((byte) 1))),
                () -> assertThrows(BinaryOverflowException.class, () -> new Denary(250).toBinary().setBytes((byte) 1).add(new Denary(100).toBinary())),
                () -> assertThrows(BinaryOverflowException.class, () -> new Denary(250).toBinary().add(new Denary(6).toBinary().setBytes((byte) 1))),
                () -> assertEquals(new UnsignedBinary("11111111 11111111 11111111").add("100").toString(), "00000001 00000000 00000000 00000011"),
                () -> assertThrows(BinaryOverflowException.class, () -> new UnsignedBinary("11111111 11111000", (byte) 2).add("1000")),
                () -> assertEquals(new UnsignedBinary("11111111 11111000").add("1000").toString(), "00000001 00000000 00000000")
        );
    }

    @Test
    void toHexadecimalTest() {
        assertAll("binary to hex converted correctly",
                () -> assertEquals(new UnsignedBinary("1000 0001 1010 0101 0101 0101").toHexadecimal().toString(), "81A555"),
                () -> assertEquals(new UnsignedBinary("1010 0101 0000 1110 101010101010").toHexadecimal().toString(), "A50EAAA"),
                () -> assertEquals(new UnsignedBinary("0100 0000 1010 0101").toHexadecimal().toString(), "40A5"),
                () -> assertEquals(new UnsignedBinary("0").toHexadecimal().toString(), "0"),
                () -> assertEquals(new UnsignedBinary("1111").toHexadecimal().toString(), "F")
        );
    }
}