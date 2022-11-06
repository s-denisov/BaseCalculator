package org.basecalculator.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OctalTest {

    @Test
    void fromDenaryTest() {
        assertAll("denary to octal converted correctly",
                () -> assertEquals(new Denary(255).to(Base.OCTAL).toString(), "377"),
                () -> assertEquals(new Denary(12345678).to(Base.OCTAL).toString(), "57060516"),
                () -> assertEquals(new Denary(0).to(Base.OCTAL).toString(), "0")
        );
    }

    @Test
    void toDenaryTest() {
        assertAll("octal to denary converted correctly",
                () -> assertEquals(new Octal("0").toDenary().toInt(), 0),
                () -> assertEquals(new Octal("147236").toDenary().toInt(), 52894),
                () -> assertEquals(new Octal("2740").toDenary().toInt(), 1504)
        );
    }


    @Test
    void toBinary() {
        assertAll("octal to binary converted correctly",
                () -> Assertions.assertEquals(new Octal("100").toBinary().toString(), "01000000"),
                () -> Assertions.assertEquals(new Octal("1").toBinary().toString(), "00000001"),
                () -> Assertions.assertEquals(new Octal(" 0  ").toBinary().toString(), "00000000"),
                () -> Assertions.assertEquals(new Octal("123 4567").toBinary().toString(), "00000101 00111001 01110111"),
                () -> Assertions.assertEquals(new Octal("377").toBinary().toString(), "11111111"),
                () -> Assertions.assertEquals(new Octal("777").toBinary().toString(), "00000001 11111111")
        );
    }

    @Test
    void addTest() {
        assertAll("adds octal numbers correctly",
                () -> assertEquals(new Denary(1389).to(Base.OCTAL).add(new Denary(2817).to(Base.OCTAL)).toDenary().toInt(), 4206),
                () -> assertEquals(new Octal("0").add("0").toDenary().toInt(), 0),
                () -> assertEquals(new Octal("0").add("77").toString(), "77"),
                () -> assertEquals(new Octal("751").add("633").toString(), "1604"),
                () -> assertEquals(new Octal("12345").add("7654").toString(), "22221")
        );
    }

    @Test
    void otherConversionTest() {
        assertAll("converts to other bases correctly",
                () -> assertEquals(new Octal("45653").to(Base.BCD).toString(), "0000 0001 1001 0011 0111 0001"),
                () -> Assertions.assertEquals(new Octal("1357").toBinary().toString(), "00000010 11101111"),
                () -> assertEquals(new Octal("567416").to(Base.HEXADECIMAL).toString(), "2EF0E")
        );
    }

    @Test
    void valueTest() {
        ArrayList<Byte> sample = new ArrayList<>();
        sample.add((byte) 0);
        sample.add((byte) 0);
        sample.add((byte) 6);
        sample.add((byte) 5);
        sample.add((byte) 4);
        sample.add((byte) 2);
        Octal sampleOctal = new Octal(sample);
        ArrayList<Byte> sampleOctalValue = sampleOctal.getValue();
        Octal sampleStringOctal = new Octal("037535010");
        ArrayList<Byte> sampleStringOctalValue = sampleStringOctal.getValue();
        assertAll("value returned correctly",
                () -> assertEquals(sampleOctalValue, sample),
                () -> assertEquals(sampleOctalValue.get(0), (byte) 0),
                () -> assertEquals(sampleOctalValue.get(1), (byte) 0),
                () -> assertEquals(sampleOctalValue.get(2), (byte) 6),
                () -> assertEquals(sampleOctalValue.get(3), (byte) 5),
                () -> assertEquals(sampleOctalValue.get(4), (byte) 4),
                () -> assertEquals(sampleOctalValue.get(5), (byte) 2),
                () -> assertEquals(sampleOctal.to(Base.OCTAL), sampleOctal),
                () -> assertEquals(sampleStringOctalValue.get(0), (byte) 0),
                () -> assertEquals(sampleStringOctalValue.get(1), (byte) 3),
                () -> assertEquals(sampleStringOctalValue.get(2), (byte) 7),
                () -> assertEquals(sampleStringOctalValue.get(6), (byte) 0),
                () -> assertEquals(sampleStringOctalValue.get(7), (byte) 1),
                () -> assertEquals(sampleStringOctalValue.get(8), (byte) 0),
                () -> assertEquals(sampleStringOctalValue.get(sampleStringOctalValue.size() - 1), (byte) 0)
        );
    }

    @Test
    void errorTest() {
        ArrayList<Byte> sample = new ArrayList<>();
        sample.add((byte) 1);
        sample.add((byte) 7);
        sample.add((byte) 8);
        sample.add((byte) 0);
        assertAll("throws exceptions correctly",
                () -> assertThrows(NumberBaseException.class, () -> new Octal("8")),
                () -> assertThrows(NumberBaseException.class, () -> new Octal("1234569")),
                () -> assertThrows(NumberBaseException.class, () -> new Octal("123A32")),
                () -> assertThrows(NumberBaseException.class, () -> new Octal("+")),
                () -> assertThrows(NumberBaseException.class, () -> new Octal("")),
                () -> assertThrows(NumberBaseException.class, () -> new Octal(sample))
        );
    }
}