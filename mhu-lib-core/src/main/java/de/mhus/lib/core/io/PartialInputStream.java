package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>PartialInputStream class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PartialInputStream extends InputStream {

	private int max;
	private InputStream src;
	private int cnt;
	private Integer delimiter;

	/**
	 * <p>Constructor for PartialInputStream.</p>
	 *
	 * @param pSrc a {@link java.io.InputStream} object.
	 * @param length a int.
	 */
	public PartialInputStream(InputStream pSrc, int length) {
		src = pSrc;
		max = length;
		cnt = 0;
	}

	/**
	 * <p>setLength.</p>
	 *
	 * @param length a int.
	 */
	public void setLength(int length) {
		cnt = 0;
		max = length;
	}

	/**
	 * <p>getBytesLeft.</p>
	 *
	 * @return a int.
	 */
	public int getBytesLeft() {
		return max - cnt;
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		if (max >= 0 && cnt >= max)
			return -1;
		cnt++;
		if (delimiter != null) {
			int ret = src.read();
			if (ret == delimiter) {
				max = 0;
				cnt = 0;
				return -1;
			}
		}
		return src.read();
	}

	/**
	 * <p>Setter for the field <code>delimiter</code>.</p>
	 *
	 * @param d a {@link java.lang.Integer} object.
	 */
	public void setDelimiter(Integer d) {
		delimiter = d;
	}

}
