package de.mhus.lib.core.crypt.pem;

import java.util.Base64;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.ParseException;
// import de.mhus.lib.core.util.Base64;

public class PemBlockModel extends MProperties implements PemBlock {

	private String name;
	protected String block;
	private String rest;
	
	private static final int BLOCK_WIDTH = 50;

	public PemBlockModel() {
		
	}
	
	public PemBlockModel(String name) {
		setName(name);
	}

	public PemBlockModel(String name, String block) {
		setName(name);
		setBlock(block);
	}
	
	public PemBlockModel(String name, byte[] block) {
		setName(name);
		setBlock(block);
	}
	
	public PemBlockModel(PemBlock clone) {
		setName(clone.getName());
		this.block = clone.getBlock();
		for (java.util.Map.Entry<String, Object> item : clone.entrySet())
			put(item.getKey(),item.getValue());
	}

	public PemBlockModel encode(String block) throws ParseException {
		// parse
		
		// find start
		int p = block.indexOf("-----START ");
		if (p < 0) throw new ParseException("start of block not found");
		
		block = block.substring(p+11);
		// get name
		p = block.indexOf("-----");
		if (p < 0) throw new ParseException("end of header not found");
		String n = block.substring(0, p);
		if (n.contains("\n") || n.contains("\r"))
			throw new ParseException("name contains line break",n);
		setName(n);
		block = block.substring(p+5);
		
		// find end
		String endMark = "-----END " + getName()+"-----";
		p = block.indexOf(endMark);
		if (p < 0)
			throw new ParseException("end of block not found",getName());
	
		rest = block.substring(p + endMark.length()).trim();
		block = block.substring(0,p).trim(); // remove line break
		
		// read lines
		boolean params = true;
		String blockOrg = "";
		while(true) {
			String line = block;
			p = block.indexOf('\n');
			if (p >= 0) {
				line = block.substring(0,p);
				block = block.substring(p+1);
			}
			if (params) {
				line = line.trim();
				if (line.length() == 0) {
					params = false;
				} else {
					int pp = line.indexOf(':');
					if (pp < 0)
						throw new ParseException("Parameter key not identified",line);
					String key = line.substring(0,pp).trim();
					String value = line.substring(pp+1).trim();
					setString(key, value);
				}
			} else {
//				if (line.length() == 0)
//					break; // end of block
				blockOrg = blockOrg + line;
			}
			
			if (p < 0) break; // end of block
		}
		
		// decode unicode
		this.block = MString.decodeUnicode(blockOrg);
		
		return this;
	}

	public String getRest() {
		return rest;
	}
	
	protected void setName(String name) {
		this.name = name.toUpperCase();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getBlock() {
		return block;
	}
	@SuppressWarnings("unchecked")
	public <T extends PemBlockModel> T setBlock(String block) {
		this.block = block;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends PemBlockModel> T set(String key, Object value) {
		put(key, value);
		return (T) this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("-----START ").append(getName()).append("-----\n");
		for (java.util.Map.Entry<String, Object> item : entrySet())
			sb.append(item.getKey()).append(": ").append(item.getValue()).append('\n');
		sb.append('\n');
				
		sb.append(getEncodedBlock());
		sb.append("\n\n");
		sb.append("-----END ").append(getName()).append("-----\n");
		return sb.toString();
	}

	public String getEncodedBlock() {
		// encode all unusual characters
		String b = MString.encodeUnicode(block, true);
		// transform to block
		StringBuilder c = new StringBuilder();
		while (b.length() > BLOCK_WIDTH) {
			c.append(b.substring(0, BLOCK_WIDTH)).append('\n');
			b = b.substring(BLOCK_WIDTH);
		}
		c.append(b); // the rest of b
		return c.toString();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends PemBlockModel> T  setBlock(byte[] bytes) {
//		setBlock(Base64.encode(bytes));
		setBlock(Base64.getEncoder().encodeToString(bytes));
		return (T) this;
	}
	
	@Override
	public byte[] getBytesBlock() {
//		return Base64.decode(getBlock());
		return Base64.getDecoder().decode(getBlock());
	}

}
