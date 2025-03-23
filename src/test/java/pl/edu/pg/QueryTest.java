package pl.edu.pg;

import org.junit.jupiter.api.Test;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import static org.junit.jupiter.api.Assertions.*;

class QueryTest {
    @Test
    void testQuery() throws Exception {
        Czlowiek czlowiek = new Czlowiek("Jan", "Kowalski", 30, Plec.MEZCZYZNA);
        Query encryptQuery = new Query(QueryType.ENCRYPT, czlowiek);
        assertEquals(QueryType.ENCRYPT, encryptQuery.getQueryType());
        assertEquals(czlowiek, encryptQuery.getData());

        SecretKey key = AESUtil.generateKey();
        GCMParameterSpec iv = AESUtil.generateIV();

        SealedObject sealedObject = AESUtil.encryptObject(czlowiek, key, iv);
        assertNotNull(sealedObject);

        Query decryptQuery = new Query(QueryType.DECRYPT, sealedObject);
        assertEquals(QueryType.DECRYPT, decryptQuery.getQueryType());
        assertEquals(sealedObject, decryptQuery.getData());
    }

}