import java.io.File;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.cfg.CfgProperties;
import de.mhus.lib.core.cfg.PropertiesCfgFileWatch;
import de.mhus.lib.core.system.CfgManager;
import de.mhus.lib.core.system.ISingletonInternal;


public class TestUpdate {

	protected static boolean updated;

	public static void main(String[] args) {

		
		File f = new File("../test_config.xml");
		if (f.exists()) f.delete();
		
		System.setProperty(MConstants.PROP_CONFIG_FILE , "../test_config.xml");

		MSingleton.get().getCfgManager().registerCfgInitiator("demo", new CfgInitiator() {
			
			@Override
			public void doInitialize(ISingletonInternal internal, CfgManager manager) {
				System.out.println("--- Initialize");
				updated = true;
			}
		});
		
		String configData1 = "<mhus></mhus>";
		
		System.out.println("*** Test Create");
		updated = false;
		MFile.writeFile(f, configData1);
		while (!updated) {
			MThread.sleep(200);
		}
		System.out.println("*** Done !");
		
		System.out.println("*** Test Update");
		updated = false;
		MFile.writeFile(f, configData1);
		while (!updated) {
			MThread.sleep(200);
		}
		System.out.println("*** Done !");

		
		
		System.out.println("*** Test extra properties ");
		File fp = new File("../testprop.properties");
		String configData2 = "prop=abc";
		MSingleton.get().getCfgManager().registerCfgProvider(TestUpdate.class.getCanonicalName(), new PropertiesCfgFileWatch(fp));
		
		CfgProperties p = new CfgProperties(TestUpdate.class, null) {
			protected void onPostUpdate(IProperties newValue) {
				System.out.println("--- Updated");
				updated = true;
			}
		};
		
		updated = false;
		MFile.writeFile(fp, configData2);
		while (!updated) {
			MThread.sleep(200);
		}
		System.out.println("*** Done !");
		
		// finally

		System.out.println("*** Test Delete");
		f.delete();
		updated = false;
		MFile.writeFile(f, configData1);
		while (!updated) {
			MThread.sleep(200);
		}
		System.out.println("*** Done !");

		// cleanup
		fp.delete();
		
		
	}

}
