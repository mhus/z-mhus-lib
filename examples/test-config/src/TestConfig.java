import java.io.PrintStream;
import java.util.logging.Logger;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.logging.Log;


public class TestConfig {

	public static void main(String[] args) {

		System.setProperty("java.util.logging.config.file", "./logging.properties");
		Logger LOGGER = Logger.getLogger("InfoLogging");
		LOGGER.info("aha!!!");
		
		final PrintStream err = System.err;
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.setErr(err);
				e.printStackTrace();
			}
		});
		
		try {
			System.setProperty("mhus.lib.config.file", "mhus-config.xml");
			MApi.setDirtyTrace(true);
			
			Log log = Log.getLog(TestConfig.class);
			log.i("aha");
			
		} catch (Throwable t) {
			System.setErr(err);
			t.printStackTrace();
		}
	}
}
