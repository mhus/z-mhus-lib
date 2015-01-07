package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;

public class PartialInputStream extends InputStream {

	private int max;
	private InputStream src;
	private int cnt;
	private Integer delimiter;

	public PartialInputStream(InputStream pSrc, int length) {
		src = pSrc;
		max = length;
		cnt = 0;
	}

	public void setLength(int length) {
		cnt = 0;
		max = length;
	}

	public int getBytesLeft() {
		return max - cnt;
	}

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

	public void setDelimiter(Integer d) {
		delimiter = d;
	}

}
