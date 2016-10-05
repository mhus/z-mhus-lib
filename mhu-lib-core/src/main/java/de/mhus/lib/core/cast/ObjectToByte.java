package de.mhus.lib.core.cast;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.ObjectContainer;

public class ObjectToByte implements Caster<Object,Byte> {

	private final static Log log = Log.getLog(ObjectToByte.class);
	
	@Override
	public Class<? extends Byte> getToClass() {
		return Byte.class;
	}

	@Override
	public Class<? extends Object> getFromClass() {
		return Object.class;
	}

	@Override
	public Byte cast(Object in, Byte def) {
		ObjectContainer<Byte> ret = new ObjectContainer<>(def);
		toByte(in, (byte) 0, ret);
		return ret.getObject();
	}

	public byte toByte(Object in, byte def, ObjectContainer<Byte> ret) {
		if (in == null) return def;
		if (in instanceof Byte) {
			if (ret != null) ret.setObject((Byte)in);
			return ((Byte)in).byteValue();
		}
		if (in instanceof Number) {
			byte r = ((Number)in).byteValue();
			if (ret != null) ret.setObject(r);
			return r;
		}

		String _in = String.valueOf(in);
		try {
			if (_in.startsWith("0x") || _in.startsWith("-0x") || _in.startsWith("+0x")) {
				int start = 2;
				if (_in.startsWith("-")) start = 3;
				int out = 0;
				for (int i = start; i < _in.length(); i++) {
					int s = -1;
					char c = _in.charAt(i);
					if (c >= '0' && c <= '9')
						s = c - '0';
					else if (c >= 'a' && c <= 'f')
						s = c - 'a' + 10;
					else if (c >= 'A' && c <= 'F')
						s = c - 'A' + 10;

					if (s == -1)
						throw new NumberFormatException(_in);
					out = out * 16 + s;
				}
				if (_in.startsWith("-")) out = -out;
				if (out > Byte.MAX_VALUE) out = Byte.MAX_VALUE;
				if (out < Byte.MIN_VALUE) out = Byte.MIN_VALUE;
				if (ret != null) ret.setObject((byte)out);
				return (byte)out;
			}
			
			byte r = Byte.parseByte(_in);
			if (ret != null) ret.setObject(r);
			return r;
		} catch (Throwable e) {
			log.t(_in, e.toString());
			return def;
		}
	}
}
