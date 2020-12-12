package com.alphawallet.attestation.cheque;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alphawallet.attestation.core.AttestationCrypto;
import com.alphawallet.attestation.IdentifierAttestation.AttestationType;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestCheque {
  private static AsymmetricCipherKeyPair senderKeys;
  private static SecureRandom rand;

  @BeforeAll
  public static void setupKeys() throws Exception {
    rand = SecureRandom.getInstance("SHA1PRNG");
    rand.setSeed("seed".getBytes());
    AttestationCrypto crypto = new AttestationCrypto(rand);
    senderKeys = crypto.constructECKeys();
  }

  @Test
  public void testFullDecoding() throws Exception {
    Cheque cheque = new Cheque("test@test.ts", AttestationType.EMAIL, 1000, 3600000, senderKeys, BigInteger.TEN);
    byte[] encoded = cheque.getDerEncoding();
    Cheque newCheque = new Cheque(encoded);
    assertTrue(cheque.verify());
    assertTrue(cheque.checkValidity());
    assertArrayEquals(encoded, newCheque.getDerEncoding());

    Cheque otherConstructor = new Cheque(newCheque.getRiddle(), newCheque.getAmount(),
        newCheque.getNotValidBefore(), newCheque.getNotValidAfter(), newCheque.getSignature(),
        newCheque.getPublicKey());
    assertEquals(cheque.getAmount(), otherConstructor.getAmount());
    assertEquals(cheque.getNotValidBefore(), otherConstructor.getNotValidBefore());
    assertEquals(cheque.getNotValidAfter(), otherConstructor.getNotValidAfter());
    assertArrayEquals(cheque.getRiddle(), otherConstructor.getRiddle());
    assertArrayEquals(cheque.getSignature(), otherConstructor.getSignature());
    // Note that apparently a proper equality has not been implemented for AsymmetricKeyParameter
//    Assert.assertEquals(cheque.getPublicKey(), otherConstructor.getPublicKey());
    assertArrayEquals(encoded, otherConstructor.getDerEncoding());
  }

  @Test
  public void testIllegalKeys() throws Exception {
    Cheque cheque = new Cheque("test@test.ts", AttestationType.EMAIL, 1000, 3600000, senderKeys, BigInteger.TEN);
    Field field = cheque.getClass().getDeclaredField("signature");
    field.setAccessible(true);
    // Change a bit in the signature
    ((byte[]) field.get(cheque))[20] ^= 1;
    assertFalse(cheque.verify());
  }

  @Test
  public void testInvalid() {
    Cheque cheque = new Cheque("test@test.ts", AttestationType.EMAIL, 1000, -1000, senderKeys, BigInteger.TEN);
    assertFalse(cheque.checkValidity());
  }

}
