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
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Hex;

import com.sun.jersey.core.util.Base64;

public class AccountManager {

    // Placeholder for database
    private Map<String, SaltedPassword> TEMPpwMap;

    private Map<String, User> userMap;

    public AccountManager() {
        this.TEMPpwMap = new HashMap<String, SaltedPassword>();
        this.userMap = new HashMap<String, User>();
    }

    /**
     * Private class to store salted passwords. To check a salted password, we
     * have to store the generated hash, the salt in plain text and the counter
     * of interations to generate the hash
     * 
     * @author Meldanor
     * 
     */
    public static final class SaltedPassword {
        // Use Strings instead of the charArray because strings are immutable,
        // arrays aren't.
        final String hash;
        final String salt;
        final int iterations;

        public SaltedPassword(String hash, String salt, int iterations) {
            this.hash = hash;
            this.salt = salt;
            this.iterations = iterations;
        }

        public final boolean isEquals(SaltedPassword other) {
            return this.hash.equals(other.hash);
        }

        public byte[] getHash() {
            try {
                return Hex.decodeHex(hash.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public byte[] getSalt() {
            try {
                return Hex.decodeHex(salt.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return String.format("%d:%s:%s", iterations, hash, salt);
        }
    }

    /**
     * Create a new user
     * 
     * @param name
     *            The name of the user - must be unique
     * @param password
     *            The users password. Can be clear, but should pre-hashed with
     *            at least SHA2. The password is Hex encoded and must be decoded
     */
    public void createUser(String name, String password) {
        if (TEMPpwMap.containsKey(name)) {
            // TOOD: Duplicate user!
        }
        try {
            char[] pw = password.toCharArray();
            // Salt Length must be the same as the length of the password
            // SaltLength = PasswordLength / 2
            // Reason: The password is Hex encoded and decoding the Hex halfs
            // the length
            int saltLength = pw.length / 2;
            byte[] salt = generateSalt(saltLength);

            name = name.toLowerCase();
            SaltedPassword saltedPassword = saltPassword(pw, salt, saltLength);
            this.TEMPpwMap.put(name, saltedPassword);

            this.userMap.put(name, new User(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param username
     * @return
     */
    public User getUser(String username) {
        return userMap.get(username.toLowerCase());
    }

    /**
     * Validate a users login information by generating the salted password hash
     * again with the given information. The newly generated salted password
     * must match the generated salted password when the user was created
     * 
     * @param headers
     *            The headers containg the users login information
     * @return True when the passwords match
     */
    public boolean validateUser(HttpHeaders headers) {
        // extract Value behind Authorization head from request
        String base64String = headers.getRequestHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // Not authorization head in the request
        if (base64String == null)
            return false;

        // decode base64 string
        base64String = base64String.substring("Basic ".length());
        base64String = Base64.base64Decode(base64String);
        // Split at ':' as defined for HTTPBasicAuthentification
        int pos = base64String.indexOf(':');
        // Not found
        if (pos == -1)
            return false;
        // extract values
        String username = base64String.substring(0, pos);
        String password = base64String.substring(pos + 1);
        return validateUser(username, password);
    }

    /**
     * Compare the newly generated salted password with the given salted
     * password
     * 
     * @param username
     *            The username
     * @param password
     *            The password
     * @return True when both passwords match
     */
    private boolean validateUser(String username, String password) {
        try {
            SaltedPassword origin = TEMPpwMap.get(username);
            if (origin == null)
                return false;
            byte[] salt = origin.getSalt();
            SaltedPassword generated = saltPassword(password.toCharArray(), salt, origin.iterations, salt.length);
            return origin.isEquals(generated);
        } catch (Exception e) {
            e.printStackTrace();
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

    /**
     * Algorithm to salt the password
     */
    private final static String KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
    /**
     * Default iteration steps to salt the given password the generated salt
     */
    private final static int DEFAULT_ITERATION = 1000;

    /**
     * Salt a given password with a random salt
     * 
     * @param password
     *            The password to salt
     * @param salt
     *            The random generated salt
     * @param bytes
     *            How many bytes the passwords contains
     * @return A salted password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
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
