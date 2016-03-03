package de.mhus.lib.logging.auris;

import java.io.IOException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;

/**
 * <p>SimpleUdpSender class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class SimpleUdpSender extends UdpSender {

	/**
	 * <p>Constructor for SimpleUdpSender.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.IProperties} object.
	 */
	public SimpleUdpSender(IProperties config) {
		super(config);
	}

	/** {@inheritDoc} */
	@Override
	protected void send(String msg) {
		try {
			byte[] sendData = msg.getBytes(MString.CHARSET_UTF_8);
			send(sendData, 0, sendData.length);
		} catch (IOException e) {
			log().e(name,e);
		}

	}
	
}
