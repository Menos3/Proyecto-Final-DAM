package org.sergio.melado.m13.classes;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class EncryptHelper {
    
    /**
     * Generacion de una SecretKey
     * @return una SecretKey
     */
    private static SecretKey getKey() {
        String password="qwert";
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] salt = new byte[16];

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = null;
        try {
            tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
    /**
     *
     * @param pass Contrasenya a encriptar
     * @return un byte [] encriptado
     * @throws NoSuchPaddingException per controlar el cipher
     * @throws NoSuchAlgorithmException per controlar el cipher
     * @throws BadPaddingException per controlar el aes.doFinal
     * @throws IllegalBlockSizeException per controlar el aes.doFinal
     * @throws InvalidKeyException per controlar el aes.init
     */
    public String AESEncrypt(String pass, boolean encriptat) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // Alternativamente, una clave que queramos que tenga al menos 16 bytes
        Key key = getKey();

        // Se obtiene un cifrador AES
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");

        // Se inicializa para encriptacion y se encripta el texto, que debemos pasar como bytes.
        if (encriptat) {

            aes.init(Cipher.ENCRYPT_MODE, key);

        } else {

            aes.init(Cipher.DECRYPT_MODE, key);
        }

        byte[] encriptado = aes.doFinal(pass.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : encriptado) {
            sb.append(Integer.toHexString(0xFF & b));
        }
        return sb.toString();
    }


}
