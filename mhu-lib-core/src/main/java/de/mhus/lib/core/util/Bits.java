/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

public class Bits {

	Bits() {
	}

	public static boolean getBoolean(byte abyte0[], int i) {
		return abyte0[i] != 0;
	}

	public static char getChar(byte abyte0[], int i) {
		return (char) (((abyte0[i + 1] & 255) << 0) + (abyte0[i + 0] << 8));
	}

	public static short getShort(byte abyte0[], int i) {
		return (short) (((abyte0[i + 1] & 255) << 0) + (abyte0[i + 0] << 8));
	}

	public static int getInt(byte abyte0[], int i) {
		return ((abyte0[i + 3] & 255) << 0) + ((abyte0[i + 2] & 255) << 8)
				+ ((abyte0[i + 1] & 255) << 16) + (abyte0[i + 0] << 24);
	}

	public static float getFloat(byte abyte0[], int i) {
		int j = ((abyte0[i + 3] & 255) << 0) + ((abyte0[i + 2] & 255) << 8)
				+ ((abyte0[i + 1] & 255) << 16) + (abyte0[i + 0] << 24);
		return Float.intBitsToFloat(j);
	}

	public static long getLong(byte abyte0[], int i) {
		return (((long) abyte0[i + 7] & 255L) << 0)
				+ (((long) abyte0[i + 6] & 255L) << 8)
				+ (((long) abyte0[i + 5] & 255L) << 16)
				+ (((long) abyte0[i + 4] & 255L) << 24)
				+ (((long) abyte0[i + 3] & 255L) << 32)
				+ (((long) abyte0[i + 2] & 255L) << 40)
				+ (((long) abyte0[i + 1] & 255L) << 48)
				+ ((long) abyte0[i + 0] << 56);
	}

	public static double getDouble(byte abyte0[], int i) {
		long l = (((long) abyte0[i + 7] & 255L) << 0)
				+ (((long) abyte0[i + 6] & 255L) << 8)
				+ (((long) abyte0[i + 5] & 255L) << 16)
				+ (((long) abyte0[i + 4] & 255L) << 24)
				+ (((long) abyte0[i + 3] & 255L) << 32)
				+ (((long) abyte0[i + 2] & 255L) << 40)
				+ (((long) abyte0[i + 1] & 255L) << 48)
				+ ((long) abyte0[i + 0] << 56);
		return Double.longBitsToDouble(l);
	}

	public static void putBoolean(byte abyte0[], int i, boolean flag) {
		abyte0[i] = (byte) (flag ? 1 : 0);
	}

	public static void putChar(byte abyte0[], int i, char c) {
		abyte0[i + 1] = (byte) (c >>> 0);
		abyte0[i + 0] = (byte) (c >>> 8);
	}

	public static void putShort(byte abyte0[], int i, short word0) {
		abyte0[i + 1] = (byte) (word0 >>> 0);
		abyte0[i + 0] = (byte) (word0 >>> 8);
	}

	public static void putInt(byte abyte0[], int i, int j) {
		abyte0[i + 3] = (byte) (j >>> 0);
		abyte0[i + 2] = (byte) (j >>> 8);
		abyte0[i + 1] = (byte) (j >>> 16);
		abyte0[i + 0] = (byte) (j >>> 24);
	}

	public static void putFloat(byte abyte0[], int i, float f) {
		int j = Float.floatToIntBits(f);
		abyte0[i + 3] = (byte) (j >>> 0);
		abyte0[i + 2] = (byte) (j >>> 8);
		abyte0[i + 1] = (byte) (j >>> 16);
		abyte0[i + 0] = (byte) (j >>> 24);
	}

	public static void putLong(byte abyte0[], int i, long l) {
		abyte0[i + 7] = (byte) (int) (l >>> 0);
		abyte0[i + 6] = (byte) (int) (l >>> 8);
		abyte0[i + 5] = (byte) (int) (l >>> 16);
		abyte0[i + 4] = (byte) (int) (l >>> 24);
		abyte0[i + 3] = (byte) (int) (l >>> 32);
		abyte0[i + 2] = (byte) (int) (l >>> 40);
		abyte0[i + 1] = (byte) (int) (l >>> 48);
		abyte0[i + 0] = (byte) (int) (l >>> 56);
	}

	public static void putDouble(byte abyte0[], int i, double d) {
		long l = Double.doubleToLongBits(d);
		abyte0[i + 7] = (byte) (int) (l >>> 0);
		abyte0[i + 6] = (byte) (int) (l >>> 8);
		abyte0[i + 5] = (byte) (int) (l >>> 16);
		abyte0[i + 4] = (byte) (int) (l >>> 24);
		abyte0[i + 3] = (byte) (int) (l >>> 32);
		abyte0[i + 2] = (byte) (int) (l >>> 40);
		abyte0[i + 1] = (byte) (int) (l >>> 48);
		abyte0[i + 0] = (byte) (int) (l >>> 56);
	}
}