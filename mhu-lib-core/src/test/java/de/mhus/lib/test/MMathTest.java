package de.mhus.lib.test;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MMath;
import junit.framework.TestCase;

public class MMathTest extends TestCase {

	public void testByteAddRotate() {
		for (byte d = Byte.MIN_VALUE; d < Byte.MAX_VALUE; d++) {
			for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
				byte l = MMath.addRotate(b, d);
				byte r = MMath.subRotate(l, d);
				if (b != r) {
					System.out.println( b + " -> " + l + " -> " + r );
					System.out.println( MCast.toBitsString(b) + " -> " + MCast.toBitsString(l) + " -> " + MCast.toBitsString(r) );
				}
				assertEquals(b, r);
			}
		}
	}

	public void testByteRotate() {
		for (int d = 1; d < 8; d++) {
			for (byte b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++) {
				byte l = MMath.rotl(b, d);
				byte r = MMath.rotr(l, d);
				if (b != r) {
					System.out.println( b + " -> " + l + " -> " + r );
					System.out.println( MCast.toBitsString(b) + " -> " + MCast.toBitsString(l) + " -> " + MCast.toBitsString(r) );
				}
				assertEquals(b, r);
			}
		}
	}

	public void testIntRotate() {
		for (int d = 1; d < 32; d++) {
			{
				int i = Integer.MIN_VALUE;
				int l = MMath.rotl(i, d);
				int r = MMath.rotr(l, d);
				if (i != r) System.out.println(i + " -> " + l + " -> " + r);
				assertEquals(i, r);
			}
			{
				int i = Integer.MAX_VALUE;
				int l = MMath.rotl(i, d);
				int r = MMath.rotr(l, d);
				if (i != r) System.out.println(i + " -> " + l + " -> " + r);
				assertEquals(i, r);
			}
			{
				int i = 1;
				int l = MMath.rotl(i, d);
				int r = MMath.rotr(l, d);
				if (i != r) System.out.println(i + " -> " + l + " -> " + r);
				assertEquals(i, r);
			}
			{
				int i = -1;
				int l = MMath.rotl(i, d);
				int r = MMath.rotr(l, d);
				if (i != r) System.out.println(i + " -> " + l + " -> " + r);
				assertEquals(i, r);
			}
			{
				int i = 0;
				int l = MMath.rotl(i, d);
				int r = MMath.rotr(l, d);
				if (i != r) System.out.println(i + " -> " + l + " -> " + r);
				assertEquals(i, r);
			}
		}
	}

}
