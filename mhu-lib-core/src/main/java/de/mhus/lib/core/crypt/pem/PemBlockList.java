package de.mhus.lib.core.crypt.pem;

import java.util.LinkedList;

import de.mhus.lib.core.parser.ParseException;

public class PemBlockList extends LinkedList<PemBlock> {

	private static final long serialVersionUID = 1L;

	public PemBlockList() {}
	
	public PemBlockList(String string) {
		while(true) {
			try {
				PemBlockModel next = new PemBlockModel().encode(string);
				add(next);
				string = next.getRest();
			} catch (ParseException e) {
				break;
			}
		}
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		for (PemBlock block : this) {
			b.append(block);
			//b.append('\n');
		}
		return b.toString();
	}
}
