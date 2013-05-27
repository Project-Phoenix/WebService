/*
 * Copyright (C) 2013 Project-Phoenix
 * 
 * This file is part of WebService.
 * 
 * WebService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * WebService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WebService.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Encrypter {

    /* Singletone Start */
    private static final Encrypter INSTANCE = new Encrypter();

    private Encrypter() {

    }

    public final static Encrypter getInstance() {
        return INSTANCE;
    }

    /* Singletone End */

    /** Algorithm to salt the password */
    private final static String KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    /** Default iteration steps to salt the given password the generated salt */
    private final static int DEFAULT_ITERATION = 1000;

    /**
     * Encrypt a password using a secure hash function
     * 
     * @param password
     *            The password in SHA512 encoded to encrypt
     * @return Encrypted password with it's salt
     */
    public SaltedPassword encryptPassword(String password) {
        try {
            char[] pw = password.toCharArray();
            // Salt Length must be the same as the length of the password
            // SaltLength = PasswordLength / 2
            // Reason: The password is Hex encoded and decoding the Hex halfs
            // the length
            int saltLength = pw.length / 2;
            byte[] salt = generateSalt(saltLength);

            return saltPassword(pw, salt, saltLength);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SaltedPassword("", "", DEFAULT_ITERATION);
    }

    /**
     * Compare the newly generated salted password with the given salted
     * password
     * 
     * @param password
     *            The password to be proven
     * @param hashed
     *            The hash of the password from database
     * @param salt
     *            The salt of the salted password from database
     * @return <code>True</code> if, and only if, the rehashed password matches
     *         the hash from database
     */
    public boolean validatePassword(String password, String hashed, String salt) {
        try {
            // Correct password from database
            SaltedPassword original = new SaltedPassword(hashed, salt, DEFAULT_ITERATION);
            byte[] saltBytes = original.getSaltHex();

            // Rehash the sent password
            SaltedPassword tmp = saltPassword(password.toCharArray(), saltBytes, DEFAULT_ITERATION, saltBytes.length);

            // Both hashes must match
            return original.equals(tmp);
        } catch (Exception e) {
            return false;
        }

    }

    private final static SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generate a random unique salt
     * 
     * @param saltLength
     *            The length of the password - should match at least the length
     *            of the password
     * @return A random order of bytes - the generated salt
     */
    private byte[] generateSalt(int saltLength) {
        byte[] salt = new byte[saltLength];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    private SaltedPassword saltPassword(char[] password, byte[] salt, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return saltPassword(password, salt, DEFAULT_ITERATION, bytes);
    }

    /**
     * Salt a given password with a random salt
     * 
     * @param password
     *            The password to salt
     * @param salt
     *            The random generated salt
     * @param iterations
     *            The number of iterations the password is salted by the
     *            algorithmn
     * @param bytes
     *            How many bytes the passwords contains
     * @return A salted password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private SaltedPassword saltPassword(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        String saltString = Hex.encodeHexString(salt);
        String saltedHash = Hex.encodeHexString(skf.generateSecret(spec).getEncoded());
        return new SaltedPassword(saltedHash, saltString, iterations);
    }
}
