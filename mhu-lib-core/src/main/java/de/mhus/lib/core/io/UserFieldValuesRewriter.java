package de.mhus.lib.core.io;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.core.MXml;

/*
<text:user-field-decls>
  <text:user-field-decl office:value-type="string" office:string-value="test" text:name="v2"/>
</text:user-field-decls>
<text:user-field-get text:name="v2">test</text:user-field-get>
 */
public class UserFieldValuesRewriter implements StreamRewriter {
	
	private IReadProperties values;
	
	public UserFieldValuesRewriter(IReadProperties values) {
		this.values = values;
	}
	
	@Override
	public InputStream rewriteContent(String file, InputStream in) throws Exception {
		
		if (!file.equals(SOfficeConnector.SOFFICE_CONTENT))
			return in;
		
		Document content = MXml.loadXml(in);
		
		NodeList list = content.getDocumentElement().getElementsByTagName("text:user-field-decl");
		for (int i = 0; i < list.getLength(); i++) {
			Element node = (Element) list.item(i);
			String name = node.getAttribute("text:name");
			String value = values.getString(name, null);
			if (value != null) {
				node.setAttribute("office:string-value", value);
			}
		}
		
		StreamBuffer buffer = new StreamBuffer();
		MXml.saveXml(content, buffer.getOutputStream());
		return buffer.getInputStream();
	}

}
