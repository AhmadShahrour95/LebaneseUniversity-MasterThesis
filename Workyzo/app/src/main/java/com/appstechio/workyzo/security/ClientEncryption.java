package com.appstechio.workyzo.security;

import android.os.Build;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import androidx.annotation.RequiresApi;

public class ClientEncryption {


    public ClientEncryption() {
    }

    //HASH ALGORITHM
    public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static boolean validatePassword(String DATA, String storedDATA) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] parts = storedDATA.split(":");

        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(DATA.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String HashingMessage(String Message) throws Exception {
        String generatedSecuredPasswordHash = generateStorngPasswordHash(Message);
        //System.out.println(generatedSecuredPasswordHash);
        return  generatedSecuredPasswordHash;
    }

    //RSA ENCRYPTION ALGORITHM


/*    public void GenerateKey() throws NoSuchAlgorithmException,InvalidKeySpecException {
        System.out.println("Convert RSA public key into a string an dvice versa");
        // generate a RSA key pair
        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(2048, new SecureRandom());
        KeyPair keyPair = keygen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("publicKey: " + publicKey);
        // get encoded form (byte array)
        byte[] publicKeyByte = publicKey.getEncoded();
        // Base64 encoded string
        String publicKeyString = Base64.encodeToString(publicKeyByte, Base64.NO_WRAP);
        System.out.println("publicKeyString: " + publicKeyString);
        // ... transport to server
        // Base64 decoding to byte array
        byte[] publicKeyByteServer = Base64.decode(publicKeyString, Base64.NO_WRAP);
        // generate the publicKey
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKeyServer = (PublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByteServer));
        System.out.println("publicKeyServer: " + publicKeyServer);
    };*/



    // Generate key pair for 2048-bit RSA encryption and decryption
    Key publicKey = null;
    Key privateKey = null;
    KeyPair kp = null;
 /*   public KeyPair GeneratePairKey() throws Exception {
        try {
            KeyPairGenerator kpg = null;
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
            } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                noSuchAlgorithmException.printStackTrace();
            }
            kpg.initialize(2048);
             kp = kpg.genKeyPair();

        } catch (Exception e) {
          //  Log.e("KEYPAIR", String.valueOf(kp));
        }

        return  kp;
    };*/

   /* public Key GeneratePublicKey() throws Exception {
        try {
            KeyPair keyPair = GeneratePairKey();
            publicKey = keyPair.getPublic();
           // privateKey = kp.getPrivate();
        } catch (Exception e) {
            //Log.d("KEYPUB", String.valueOf(publicKey));
        }

        return  publicKey;
    };*/

/*    public Key GeneratePrivateKey () throws Exception {
        try {
            KeyPair keyPair = GeneratePairKey();
            //publicKey = kp.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (Exception e) {
           // Log.d("KEYPRIV", String.valueOf(privateKey));
        }

        return  privateKey;
    };*/

/*         @RequiresApi(api = Build.VERSION_CODES.O)
        public byte[]  RSAEncryption (String Message) throws Exception {
        // Encode the original data with the RSA private key
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, GeneratePrivateKey());
            encodedBytes = c.doFinal(Message.getBytes());
        } catch (Exception e) {
            Log.e("Crypto", "RSA encryption error");
        }

        //String EncryptedString = android.util.Base64.encodeToString(encodedBytes, Base64.DEFAULT);
             Log.d("Encoded string: ", new String(encodedBytes, StandardCharsets.UTF_8));

        return encodedBytes;

    }*/


/*    public byte[]  RSADecryption ( byte[] encodedBytes,Key publickey) throws Exception {
        // Decode the encoded data with the RSA public key
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, publickey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("Crypto", "RSA decryption error");
        }

        Log.d("ENC_DECODE ", new String(decodedBytes));
       // boolean matched = validatePassword(LoginLayout.inputPasswordtxt.getText().toString().trim(), signUpwithEncryptedPass());
        //if (matched){
         //   Log.d("ENC_DECODEPlain ", LoginLayout.inputPasswordtxt.getText().toString().trim());
        //String DecryptedString = new String(decodedBytes);
        return  decodedBytes;
        }*/




    }

