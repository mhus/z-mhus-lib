package de.mhus.lib.logging.auris;

import java.io.IOException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MString;

public class SimpleUdpSender extends UdpSender {

	public SimpleUdpSender(IProperties config) {
		super(config);
	}

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
