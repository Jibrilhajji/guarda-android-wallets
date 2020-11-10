/*
 * Copyright 2011 Google Inc.
 * Copyright 2015 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.guarda.ethereum.crypto;

import com.guarda.ethereum.BuildConfig;

import java.util.Arrays;

import static com.guarda.ethereum.models.constants.Const.MAINNET_FLAVOR;
import static com.guarda.ethereum.models.constants.Const.TESTNET_FLAVOR;


/**
 * Parses and generates private keys in the form used by the Bitcoin "dumpprivkey" command. This is the private key
 * bytes with a header byte and 4 checksum bytes at the end. If there are 33 private key bytes instead of 32, then
 * the last byte is a discriminator value for the compressed pubkey.
 */
public class DumpedPrivateKey {
  private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
  private static final int[] INDEXES = new int[128];
  private static final byte DUMPED_PRIVATE_KEY_HEADER_MAINNET = (byte) 128; //mainnet
  private static final byte DUMPED_PRIVATE_KEY_HEADER_TESTNET = (byte) 239; //testnet
  private byte[] bytes;
  static {
    Arrays.fill(INDEXES, -1);
    for (int i = 0; i < ALPHABET.length; i++) {
      INDEXES[ALPHABET[i]] = i;
    }
  }

  DumpedPrivateKey(byte[] keyBytes, boolean compressed) {
    bytes = encode(keyBytes, compressed);
  }

  private static byte[] encode(byte[] keyBytes, boolean compressed) {
    if (!compressed) {
      return keyBytes;
    } else {
      byte[] bytes = new byte[33];
      System.arraycopy(keyBytes, 0, bytes, 0, 32);
      bytes[32] = 1;
      return bytes;
    }
  }

  public static ECKey fromBase58(String base58) throws IllegalArgumentException {
    byte[] versionAndDataBytes = decodeChecked(base58);
    byte[] bytes = new byte[versionAndDataBytes.length - 1];
    System.arraycopy(versionAndDataBytes, 1, bytes, 0, versionAndDataBytes.length - 1);
    boolean compressed;
    if (bytes.length == 33 && bytes[32] == 1) {
      compressed = true;
      bytes = Arrays.copyOf(bytes, 32);  // Chop off the additional marker byte.
    } else if (bytes.length == 32) {
      compressed = false;
    } else {
      throw new IllegalArgumentException("Invalid length of private key");
    }

    return ECKey.fromPrivate(bytes, compressed);
  }

  public final String toBase58() {
    // A stringified buffer is:
    //   1 byte version + data bytes + 4 bytes check code (a truncated hash)
    byte[] addressBytes = new byte[1 + bytes.length + 4];
    if (BuildConfig.FLAVOR.equals(MAINNET_FLAVOR)) {
      addressBytes[0] = DUMPED_PRIVATE_KEY_HEADER_MAINNET;
    } else if (BuildConfig.FLAVOR.equals(TESTNET_FLAVOR)) {
      addressBytes[0] = DUMPED_PRIVATE_KEY_HEADER_TESTNET;
    }

    System.arraycopy(bytes, 0, addressBytes, 1, bytes.length);
    byte[] checksum = Sha256Hash.hashTwice(addressBytes, 0, bytes.length + 1);
    System.arraycopy(checksum, 0, addressBytes, bytes.length + 1, 4);
    return Base58.encode(addressBytes);
  }

  private static byte[] decodeChecked(String input) throws IllegalArgumentException{
    byte[] decoded  = decode(input);
    if (decoded.length < 4)
      throw new IllegalArgumentException("Input too short");
    byte[] data = Arrays.copyOfRange(decoded, 0, decoded.length - 4);
    byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 4, decoded.length);
    byte[] actualChecksum = Arrays.copyOfRange(Sha256Hash.hashTwice(data), 0, 4);
    if (!Arrays.equals(checksum, actualChecksum))
      throw new IllegalArgumentException("Checksum does not validate");
    return data;
  }

  private static byte[] decode(String input) throws IllegalArgumentException {
    if (input.length() == 0) {
      return new byte[0];
    }

    byte[] input58 = new byte[input.length()];
    for (int i = 0; i < input.length(); ++i) {
      char c = input.charAt(i);
      int digit = c < 128 ? INDEXES[c] : -1;
      if (digit < 0) {
        throw new IllegalArgumentException("Invalid input.");
      }
      input58[i] = (byte) digit;
    }
    // Count leading zeros.
    int zeros = 0;
    while (zeros < input58.length && input58[zeros] == 0) {
      ++zeros;
    }
    // Convert base-58 digits to base-256 digits.
    byte[] decoded = new byte[input.length()];
    int outputStart = decoded.length;
    for (int inputStart = zeros; inputStart < input58.length; ) {
      decoded[--outputStart] = divmod(input58, inputStart);
      if (input58[inputStart] == 0) {
        ++inputStart; // optimization - skip leading zeros
      }
    }
    // Ignore extra leading zeroes that were added during the calculation.
    while (outputStart < decoded.length && decoded[outputStart] == 0) {
      ++outputStart;
    }
    // Return decoded data (including original number of leading zeros).
    return Arrays.copyOfRange(decoded, outputStart - zeros, decoded.length);
  }

  private static byte divmod(byte[] number, int firstDigit) {
    int base = 58, divisor = 256;
    int remainder = 0;
    for (int i = firstDigit; i < number.length; i++) {
      int digit = (int) number[i] & 0xFF;
      int temp = remainder * base + digit;
      number[i] = (byte) (temp / divisor);
      remainder = temp % divisor;
    }
    return (byte) remainder;
  }
}
