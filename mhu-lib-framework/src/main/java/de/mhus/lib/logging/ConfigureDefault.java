package de.mhus.lib.logging;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.logging.adapters.JavaLoggerHandler;
import de.mhus.lib.logging.adapters.Log4JAppender;

public class ConfigureDefault extends MObject {

	public ConfigureDefault() {
		log().d("configure default logger adapters");
		try {
			if (Class.forName("org.apache.log4j.Logger") != null) {
				log().d("configure log4j");
				Log4JAppender.configure();
			}
		} catch (Throwable t) {}

		log().d("configure java logger");
		JavaLoggerHandler.configure();
		
		try {
			if (Class.forName("org.apache.log4j.Logger") != null) {
				log().d("configure slf4j");
			//	SLF4JAppender.configure();
			}
		} catch (Throwable t) {}
	}
}
