package pl.edu.pg.utils;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import pl.edu.pg.Czlowiek;
import pl.edu.pg.Plec;

import java.io.*;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AESUtilTest {
    @Test
    void testGenerateKey() throws NoSuchAlgorithmException {
        String key = AESUtil.generateKey();
        assertNotNull(key);
        assertFalse(key.isEmpty());
    }
    @Test
    void testGenerateIV() {
        String iv = AESUtil.generateIV();
        assertNotNull(iv);
        assertFalse(iv.isEmpty());
    }
    @Test
    void testEncryptObject() throws Exception {
        String key = AESUtil.generateKey();
        String iv = AESUtil.generateIV();
        Czlowiek rodzic = new Czlowiek("Marek", "Nowak", 21, Plec.MEZCZYZNA);
        Gson gson = new Gson();

        File inputFile = File.createTempFile("testInput", ".json");
        File outputFile = File.createTempFile("testOutput", ".enc");
        File decryptedFile = File.createTempFile("testDecrypted", ".json");

        try (Writer writer = new FileWriter(inputFile)) {
            gson.toJson(rodzic, writer);
        }

        AESUtil.encryptObject(new String[]{inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), key, iv});
        AESUtil.decryptObject(new String[]{outputFile.getAbsolutePath(), decryptedFile.getAbsolutePath(), key, iv});

        Czlowiek decryptedRodzic;
        try (Reader reader = new FileReader(decryptedFile)) {
            decryptedRodzic = gson.fromJson(reader, Czlowiek.class);
        }
        assertEquals(rodzic, decryptedRodzic);
    }
}