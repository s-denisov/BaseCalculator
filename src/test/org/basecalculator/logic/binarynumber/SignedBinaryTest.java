package org.basecalculator.logic.binarynumber;

import org.basecalculator.logic.Denary;
import org.basecalculator.logic.NumberBaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignedBinaryTest {

    @Test
    void creationTest() {
        assertAll("signed binary numbers created properly",
                () -> assertEquals(new Denary(-128).toSignedBinary().toString(), "10000000"),
                () -> assertEquals(new Denary(128).toSignedBinary().toString(), "00000000 10000000"),
                () -> assertEquals(new Denary(-1).toSignedBinary().toString(), "11111111"),
                () -> assertEquals(new Denary(0).toSignedBinary().toString(), "00000000"),
                () -> assertEquals(new Denary(-42).toSignedBinary().toString(), "11010110"),
                () -> assertEquals(new Denary(127).toSignedBinary().toString(), "01111111"),
                () -> assertEquals(new Denary(-127).toSignedBinary().toString(), "10000001"),
                () -> assertEquals(new Denary(129).toSignedBinary().toString(), "00000000 10000001"),
                () -> assertEquals(new Denary(-129).toSignedBinary().toString(), "11111111 01111111"),
                () -> assertEquals(new Denary(-129).toSignedBinary().setBytes((byte) 3).toString(), "11111111 11111111 01111111"),
                () -> assertEquals(new Denary(-42).toSignedBinary().setBytes((byte) 4).toString(), "11111111 11111111 11111111 11010110"),
                () -> assertEquals(new Denary(-1).toSignedBinary().setBytes((byte) 3).toString(), "11111111 11111111 11111111"),
                () -> assertEquals(new Denary(10).toSignedBinary().setBytes((byte) 2).toString(), "00000000 00001010")
        );
    }

    @Test
    void conversionTest() {
        assertAll("converts both positive and negative binary to denary correctly",
                () -> assertEquals(new SignedBinary("11111111").toDenary().toInt(), -1),
                () -> assertEquals(new SignedBinary("00000000", (byte) 1).toDenary().toInt(), 0),
                () -> assertEquals(new SignedBinary("11010110", (byte) 1).toDenary().toInt(), -42),
                () -> assertEquals(new SignedBinary("11010110", (byte) 2).toDenary().toInt(), 214),
                () -> assertEquals(new SignedBinary("01111111", (byte) 1).toDenary().toInt(), 127),
                () -> assertEquals(new SignedBinary("10000000").toDenary().toInt(), -128),
                () -> assertEquals(new SignedBinary("01111111", (byte) 4).toDenary().toInt(), 127),
                () -> assertEquals(new SignedBinary("00000000 10000000").toDenary().toInt(), 128),
                () -> assertEquals(new SignedBinary("00000000 10000001", (byte) 2).toDenary().toInt(), 129),
                () -> assertEquals(new SignedBinary("1111111101111111", (byte) 2).toDenary().toInt(), -129)
        );
    }

    @Test
    void negationTest() {
        assertAll("negates numbers using different methods correctly",
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-128).toSignedBinary().setBytes((byte) 1).twosComplement()),
                () -> assertEquals(new SignedBinary("100", (byte) 1).twosComplement().toString(), "11111100"),
                () -> assertEquals(new Denary(-28).toSignedBinary().setBytes((byte) 1).twosComplement().toString(), "00011100"),
                () -> assertEquals(new SignedBinary("01100101", (byte) 1).twosComplement().toString(), "10011011"),
                () -> assertEquals(new Denary(5).toSignedBinary().twosComplement().toDenary().toInt(), -5),
                () -> assertEquals(new Denary(5).toSignedBinary().setBytes((byte) 1).twosComplement().toDenary().toInt(), -5),
                () -> assertEquals(new Denary(5).toSignedBinary().setBytes((byte) 3).twosComplement().toDenary().toInt(), -5),
                () -> assertEquals(new Denary(-5).toSignedBinary().twosComplement().toDenary().toInt(), 5),
                () -> assertEquals(new Denary(0).toSignedBinary().twosComplement().toDenary().toInt(), 0),
                () -> assertEquals(new SignedBinary("0").twosComplement().toString(), "00000000"),
                () -> assertEquals(new Denary(-128).toSignedBinary().twosComplement().toDenary().toInt(), 128),
                () -> assertEquals(new Denary(120).toSignedBinary().twosComplement().add(new Denary(-10).toSignedBinary()).toDenary().toInt(), -130),
                () -> assertEquals(new Denary(120).toSignedBinary().twosComplement().add(new Denary(10).toSignedBinary().twosComplement()).toDenary().toInt(), -130),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(120).toSignedBinary().setBytes((byte) 1).twosComplement().add(new Denary(-10).toSignedBinary()).toDenary().toInt()),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(120).toSignedBinary().setBytes((byte) 1).twosComplement().add(new Denary(10).toSignedBinary().twosComplement()).toDenary().toInt()),
                () -> assertEquals(new SignedBinary("10101010").onesComplement().toString(), "01010101"),
                () -> assertEquals(new SignedBinary("0 10101010").onesComplement().toString(), "11111111 01010101"),
                () -> assertEquals(new SignedBinary("0").onesComplement().toString(), "11111111"),
                () -> assertEquals(new SignedBinary("10101010", (byte) 2).onesComplement().toString(), "11111111 01010101"),
                () -> assertEquals(new Denary(127).toSignedBinary().oppositeSignMagnitude().toString(), "11111111"),
                () -> assertEquals(new Denary(127).toSignedBinary().setBytes((byte) 2).oppositeSignMagnitude().toString(), "10000000 01111111"),
                () -> assertEquals(new Denary(1).toSignedBinary().oppositeSignMagnitude().toString(), "10000001"),
                () -> assertEquals(new Denary(32_767).toSignedBinary().oppositeSignMagnitude().toString(), "11111111 11111111"),
                () -> assertEquals(new Denary(0).toSignedBinary().oppositeSignMagnitude().toString(), "10000000")
        );
    }

    @Test
    void additionTest() {
        assertAll("adds signed numbers together correctly",
                () -> assertEquals(new Denary(10).toSignedBinary().add(new Denary(-128).toSignedBinary()).toDenary().toInt(),  -118),
                () -> assertEquals(new Denary(125).toSignedBinary().add(new Denary(108).toSignedBinary()).toDenary().toInt(), 233),
                () -> assertEquals(new SignedBinary("00010011", (byte) 1).add(new SignedBinary("11010011", (byte) 1)).toString(), "11100110"),
                () -> assertEquals(new SignedBinary("11110001", (byte) 1).add(new SignedBinary("10101001", (byte) 1)).toString(), "10011010"),
                () -> assertEquals(new SignedBinary("00001100", (byte) 1).add(new SignedBinary("00100010", (byte) 1)).toString(), "00101110"),
                () -> assertEquals(new Denary(-12).toSignedBinary().setBytes((byte) 1).add(new Denary(-89).toSignedBinary().setBytes((byte) 1)).toDenary().toInt(), -101),
                () -> assertEquals(new Denary(66).toSignedBinary().setBytes((byte) 1).add(new Denary(-78).toSignedBinary().setBytes((byte) 1)).toDenary().toInt(), -12),
                () -> assertEquals(new Denary(36).toSignedBinary().setBytes((byte) 1).add(new Denary(58).toSignedBinary().setBytes((byte) 1)).toDenary().toInt(), 94),
                () -> assertEquals(new Denary(127).toSignedBinary().add("1").toString(), "00000000 10000000"),
                () -> assertEquals(new Denary(255).toSignedBinary().add(new Denary(65_700).toSignedBinary()).toDenary().toInt(),  65_955),
                () -> assertEquals(new Denary(120).toSignedBinary().add(new Denary(1_287_451).toSignedBinary()).toDenary().toInt(),  1_287_571),
                () -> assertEquals(new Denary(2_234_555).toSignedBinary().add(new Denary(10).toSignedBinary()).toDenary().toInt(),  2_234_565),
                () -> assertEquals(new Denary(2_234_555).toSignedBinary().add(new Denary(3_765_812).toSignedBinary()).toDenary().toInt(),  6_000_367),
                () -> assertEquals(new Denary(2_234_555).toSignedBinary().add(new Denary(-3_765_812).toSignedBinary()).toDenary().toInt(),  -1_531_257),
                () -> assertEquals(new Denary(2_234_555).toSignedBinary().add(new Denary(-2_234_500).toSignedBinary()).toDenary().toInt(),  55),
                () -> assertEquals(new Denary(12).toSignedBinary().add(new Denary(-2_234_555).toSignedBinary()).toDenary().toInt(),  -2_234_543),
                () -> assertEquals(new Denary(-3_765_812).toSignedBinary().add(new Denary(-2_234_555).toSignedBinary()).toDenary().toInt(),  -6_000_367),
                () -> assertEquals(new Denary(2_234_555).toSignedBinary().add(new Denary(-12).toSignedBinary()).toDenary().toInt(),  2_234_543),
                () -> assertEquals(new Denary(128).toSignedBinary().add(new Denary(-10).toSignedBinary()).toDenary().toInt(),  118),
                () -> assertEquals(new Denary(128).toSignedBinary().add(new Denary(-1).toSignedBinary()).toDenary().toInt(),  127),
                () -> assertEquals(new Denary(-138).toSignedBinary().add(new Denary(-10).toSignedBinary()).toDenary().toInt(),  -148),
                () -> assertEquals(new Denary(9_925_161).toSignedBinary().add(new Denary(-23_699_478).toSignedBinary()).toDenary().toInt(),  -13_774_317),
                () -> assertEquals(new Denary(-9_925_161).toSignedBinary().add(new Denary(-23_699_478).toSignedBinary()).toDenary().toInt(), -33_624_639),
                () -> assertEquals(new Denary(200).toSignedBinary().add(new Denary(-100).toSignedBinary()).toDenary().toInt(), 100),
                () -> assertEquals(new Denary(-200).toSignedBinary().add(new Denary(100).toSignedBinary()).toDenary().toInt(), -100),
                () -> assertEquals(new Denary(-200).toSignedBinary().add(new Denary(-100).toSignedBinary()).toDenary().toInt(), -300)
        );
    }

    @Test
    void signMagnitudeTest() {
        assertAll("converted from denary to sign magnitude correctly",
                () -> assertEquals(new Denary(-100).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 2).toString(), "10000000 01100100"),
                () -> assertEquals(new Denary(-1).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1).toString(), "10000001"),
                () -> assertEquals(new Denary(127).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1).toString(), "01111111"),
                () -> assertEquals(new Denary(-127).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1).toString(), "11111111"),
                () -> assertEquals(new Denary(100).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1).toString(), "01100100"),
                () -> assertEquals(new Denary(-100).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1).toString(), "11100100"),
                () -> assertEquals(new Denary(100).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 2).toString(), "00000000 01100100"),
                () -> assertEquals(new Denary(0).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1).toString(), "00000000"),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-128).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1)),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(128).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1)),
                () -> assertEquals(new Denary(128).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 2).toString(), "00000000 10000000"),
                () -> assertEquals(new Denary(-128).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 2).toString(), "10000000 10000000"),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(650).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1)),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-650).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 1)),
                () -> assertEquals(new Denary(650).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 2).toString(), "00000010 10001010"),
                () -> assertEquals(new Denary(-650).toSignedBinary(SignedType.SIGN_MAGNITUDE).setBytes((byte) 2).toString(), "10000010 10001010")
        );
    }

    @Test
    void onesComplement() {
        assertAll("converted from denary to one's complement correctly",
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-128).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1)),
                () -> assertEquals(new Denary(-1).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1).toString(), "11111110"),
                () -> assertEquals(new Denary(127).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1).toString(), "01111111"),
                () -> assertEquals(new Denary(-127).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1).toString(), "10000000"),
                () -> assertEquals(new Denary(100).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1).toString(), "01100100"),
                () -> assertEquals(new Denary(-100).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1).toString(), "10011011"),
                () -> assertEquals(new Denary(100).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 2).toString(), "00000000 01100100"),
                () -> assertEquals(new Denary(-100).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 2).toString(), "11111111 10011011"),
                () -> assertEquals(new Denary(0).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1).toString(), "00000000"),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(128).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1)),
                () -> assertEquals(new Denary(128).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 2).toString(), "00000000 10000000"),
                () -> assertEquals(new Denary(-128).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 2).toString(), "11111111 01111111"),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(650).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1)),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-650).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 1)),
                () -> assertEquals(new Denary(650).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 2).toString(), "00000010 10001010"),
                () -> assertEquals(new Denary(-650).toSignedBinary(SignedType.ONES_COMPLEMENT).setBytes((byte) 2).toString(), "11111101 01110101")
        );
    }

    @Test
    void overflowTest() {
        assertAll("throws overflow exceptions correctly",
                () -> assertThrows(NumberBaseException.class, () -> new Denary(128).toSignedBinary().setBytes((byte) 1)),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-129).toSignedBinary().setBytes((byte) 1)),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(127).toSignedBinary().setBytes((byte) 1).add("1")),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(127).toSignedBinary().add(new Denary(1).toSignedBinary().setBytes((byte) 1))),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(32_767).toSignedBinary().setBytes((byte) 2).add(new Denary(1).toSignedBinary())),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-32_768).toSignedBinary().setBytes((byte) 2).add(new Denary(-1).toSignedBinary())),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(-32_700).toSignedBinary().setBytes((byte) 2).add(new Denary(-100).toSignedBinary())),
                () -> assertThrows(NumberBaseException.class, () -> new Denary(32_768).toSignedBinary().setBytes((byte) 2))
        );
    }
}