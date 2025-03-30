package pl.edu.pg.utils;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
  private static final String ALGORITHM = "AES/GCM/NoPadding";
  private static final int KEY_SIZE = 256;
  private static final int IV_SIZE = 12;
  private static final int TAG_SIZE = 128;
  private static final int BYTE_AMOUNT = 8192;

  public static String generateKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    keyGen.init(KEY_SIZE);
    SecretKey secretKey = keyGen.generateKey();
    return Base64.getEncoder().encodeToString(secretKey.getEncoded());
  }

  public static String generateIV() {
    byte[] iv = new byte[IV_SIZE];
    new SecureRandom().nextBytes(iv);
    return Base64.getEncoder().encodeToString(iv);
  }

  public static void encryptObject(String[] arguments) throws Exception {
    encryptObject(arguments, BYTE_AMOUNT);
  }

  public static void quickEncryptObject(String[] arguments) throws Exception {
    final int byteAmount = 8192;
    encryptObject(arguments, byteAmount);
  }

  private static void encryptObject(String[] arguments, int byteAmount) throws IOException, NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, OverlappingFileLockException {
    String inPath = arguments[0];
    String outPath = arguments[1];
    SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(arguments[2]), "AES");
    byte[] ivBytes = Base64.getDecoder().decode(arguments[3]);
    GCMParameterSpec iv = new GCMParameterSpec(TAG_SIZE, ivBytes);

    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    try (var bis = new FileInputStream(inPath);
        var bos = new FileOutputStream(outPath);
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        FileLock lock = bos.getChannel().tryLock()) {
      byte[] buffer = new byte[byteAmount];
      int bytesRead;
      while ((bytesRead = bis.read(buffer)) != -1) {
        cos.write(buffer, 0, bytesRead);
      }
    }
  }

  public static void decryptObject(String[] arguments) throws IOException, NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, OverlappingFileLockException {
    String inPath = arguments[0];
    String outPath = arguments[1];
    SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(arguments[2]), "AES");
    byte[] ivBytes = Base64.getDecoder().decode(arguments[3]);
    GCMParameterSpec iv = new GCMParameterSpec(TAG_SIZE, ivBytes);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key, iv);

    try (var bis = new FileInputStream(inPath);
        var bos = new FileOutputStream(outPath);
        CipherInputStream cis = new CipherInputStream(bis, cipher);
        FileLock lock = bos.getChannel().tryLock()) {
      if (lock == null) {
        Logger.error("File is locked by another process");
        throw new OverlappingFileLockException();
      }
      byte[] buffer = new byte[BYTE_AMOUNT];
      int bytesRead;
      while ((bytesRead = cis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
      }
    }
  }
}