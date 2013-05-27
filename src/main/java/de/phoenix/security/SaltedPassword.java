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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public final class SaltedPassword {

    protected final String hash;
    protected final String salt;
    protected final int iterations;

    protected SaltedPassword(String hash, String salt, int iterations) {
        this.hash = hash;
        this.salt = salt;
        this.iterations = iterations;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || (!(obj instanceof SaltedPassword)))
            return false;
        SaltedPassword other = (SaltedPassword) obj;
        return this.hash.equals(other.hash);
    }

    @Override
    public String toString() {
        return String.format("%d:%s:%s", iterations, hash, salt);
    }

    public String getHash() {
        return hash;
    }

    public int getIterations() {
        return iterations;
    }

    public String getSalt() {
        return salt;
    }

    public byte[] getSaltHex() throws DecoderException {
        return Hex.decodeHex(salt.toCharArray());

    }
}