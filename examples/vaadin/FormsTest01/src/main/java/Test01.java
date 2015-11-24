import de.mhus.lib.core.config.MConfigFactory;
import de.mhus.lib.core.config.XmlConfig;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.errors.MException;
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
		
	}
}
