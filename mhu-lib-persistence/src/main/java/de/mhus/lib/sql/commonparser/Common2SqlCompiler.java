package de.mhus.lib.sql.commonparser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.mhus.lib.core.MXml;
import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.core.parser.ParseException;
import de.mhus.lib.core.parser.Parser;
import de.mhus.lib.core.parser.StringPart;
import de.mhus.lib.sql.parser.ICompiler;

/**
 
<common>
 <select>
    <from></from>
 	<where></where>
 </select>
</common>
 
 */
public class Common2SqlCompiler implements Parser {

	protected ICompiler compiler;

	public Common2SqlCompiler(ICompiler compiler) {
		this.compiler = compiler;
	}
	
	@Override
	public CompiledString compileString(String in) throws ParseException {
		try {
			Document xml = MXml.loadXml(in);
			
			Element rootElement = xml.getDocumentElement();
			if (!rootElement.getNodeName().equals("common"))
				throw new ParseException("xml is not in common language, need to start with the tag <common>",in);
			
			
			
			return new CompiledString(new StringPart[] {});
		} catch (Exception e) {
			throw new ParseException(in,e);
		}
	}

}
