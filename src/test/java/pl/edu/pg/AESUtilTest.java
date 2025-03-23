package pl.edu.pg;

import org.junit.jupiter.api.Test;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class AESUtilTest {

    @Test
    void encryptObject_Test1() throws Exception{
        Czlowiek czlowiek1 = new Czlowiek("Jan", "Kowalski", 30, Plec.MEZCZYZNA);
        SecretKey key = AESUtil.generateKey();
        GCMParameterSpec iv = AESUtil.generateIV();

        SealedObject sealedObject = AESUtil.encryptObject(czlowiek1, key, iv);
        assertNotNull(sealedObject);

        Serializable decryptedObject = AESUtil.decryptObject(sealedObject, key, iv);
        assertNotNull(decryptedObject);
        assertTrue(decryptedObject instanceof Czlowiek);

        Czlowiek decryptedCzlowiek = (Czlowiek) decryptedObject;
        assertEquals(czlowiek1, decryptedCzlowiek);
    }
    @Test
    void encryptObject_Test2() throws Exception{
        Czlowiek rodzic = new Czlowiek("Jan", "Kowalski", 21, Plec.MEZCZYZNA);
        Czlowiek podlegly1 = new Czlowiek("Jan", "Kowalski", 21, Plec.MEZCZYZNA);
        Czlowiek podlegly2 = new Czlowiek("Jan", "Nowak", 21, Plec.MEZCZYZNA);
        Czlowiek podlegly3 = new Czlowiek("Marek", "Kowalski", 21, Plec.MEZCZYZNA);
        podlegly2.dodajPodleglego(podlegly3);
        podlegly1.dodajPodleglego(podlegly2);
        rodzic.dodajPodleglego(podlegly1);

        SecretKey key = AESUtil.generateKey();
        GCMParameterSpec iv = AESUtil.generateIV();

        SealedObject zaszyfrowany_rodzic = AESUtil.encryptObject(rodzic, key, iv);
        SealedObject zaszyfrowany_podlegly1 = AESUtil.encryptObject(podlegly1, key, iv);
        assertNotNull(zaszyfrowany_rodzic);
        assertNotNull(zaszyfrowany_podlegly1);

        Serializable odszyfrowany_rodzic = AESUtil.decryptObject(zaszyfrowany_rodzic, key, iv);
        Serializable odszyfrowany_podlegly1 = AESUtil.decryptObject(zaszyfrowany_podlegly1, key, iv);
        assertNotNull(odszyfrowany_rodzic);
        assertNotNull(odszyfrowany_podlegly1);
        assertTrue(odszyfrowany_rodzic instanceof Czlowiek);
        assertEquals(rodzic,(Czlowiek) odszyfrowany_rodzic);
        assertTrue(odszyfrowany_podlegly1 instanceof Czlowiek);
        assertEquals(podlegly1,(Czlowiek) odszyfrowany_podlegly1);
        assertEquals(podlegly1,((Czlowiek) odszyfrowany_rodzic).getPodlegli().iterator().next());
        assertNotEquals(podlegly1,(Czlowiek) odszyfrowany_rodzic);
    }


}