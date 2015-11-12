package de.mhus.lib.logging.auris;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.util.Rfc1738;

public abstract class UdpSender extends AurisSender {

	protected InetAddress to;
	protected DatagramSocket clientSocket;

	public UdpSender(IProperties config) {
		super(config);
		try {
			clientSocket = new DatagramSocket();
		    to = InetAddress.getByName(host);
		} catch (UnknownHostException | SocketException e) {
			log().e(name,e);
		}
	}	
	
	protected void send(Map<String,String> data) {
		send(Rfc1738.implode(data));
	}

	protected void send(byte[] sendData, int offset, int length) throws IOException {
		DatagramPacket sendPacket = new DatagramPacket(sendData, offset, length, to, port);
		clientSocket.send(sendPacket);
	}

	protected abstract void send(String msg);

}
