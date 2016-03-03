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

/**
 * <p>Abstract UdpSender class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class UdpSender extends AurisSender {

	protected InetAddress to;
	protected DatagramSocket clientSocket;

	/**
	 * <p>Constructor for UdpSender.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.IProperties} object.
	 */
	public UdpSender(IProperties config) {
		super(config);
		try {
			clientSocket = new DatagramSocket();
		    to = InetAddress.getByName(host);
		} catch (UnknownHostException | SocketException e) {
			log().e(name,e);
		}
	}	
	
	/**
	 * <p>send.</p>
	 *
	 * @param data a {@link java.util.Map} object.
	 */
	protected void send(Map<String,String> data) {
		send(Rfc1738.implode(data));
	}

	/**
	 * <p>send.</p>
	 *
	 * @param sendData an array of byte.
	 * @param offset a int.
	 * @param length a int.
	 * @throws java.io.IOException if any.
	 */
	protected void send(byte[] sendData, int offset, int length) throws IOException {
		DatagramPacket sendPacket = new DatagramPacket(sendData, offset, length, to, port);
		clientSocket.send(sendPacket);
	}

	/**
	 * <p>send.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	protected abstract void send(String msg);

}
