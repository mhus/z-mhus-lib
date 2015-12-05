import org.w3c.dom.Document;

import de.mhus.lib.core.MXml;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ModelUtil;
import de.mhus.lib.form.definition.FmSplit;
import de.mhus.lib.form.ui.FmText;

public class Test01 {

	public static void main(String[] arg) throws MException {
		
		DefRoot root = new DefRoot(
					new FmText("name","Name","Dein Name"),
					new FmSplit(
							new FmText("a"),
							new FmText("b")
							)
					
				);
		
		root.build();
		
		System.out.println(root.dump());
		
		System.out.println("----------------------------------------------");
		
		Document doc = ModelUtil.toXml(root);
		
		System.out.println( MXml.dump(doc) );

		System.out.println("----------------------------------------------");

		DefRoot clone = ModelUtil.toModel(doc.getDocumentElement());
		
		clone.build();
		
		System.out.println(clone.dump());
		
	}
}
