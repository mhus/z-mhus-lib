package de.mhus.lib.core;

import java.util.Locale;
import java.util.UUID;

public class MConstants {

	public static final String LOG_MAPPER = "_mhus_log";

	public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	public static final Locale LOCALE_DE_DE = new Locale("de","DE");
	public static final Locale LOCALE_EN_US = new Locale("en","US");
	
	
	public static final String PROP_DIRTY_TRACE = "mhu.lib.singleton.trace";
	public static final String PROP_SINGLETON_FACTORY_CLASS = "mhus.singleton.factory";
	public static final String PROP_FILE_WATCH = "mhus.config.watch";
	public static final String PROP_CONFIG_FILE = "mhus.config.file";

	public static final String DEFAULT_MHUS_CONFIG_FILE = "mhus-config.xml";

	public static final String PROP_LOG_FACTORY_CLASS = "log.factory";

	public static final String PROP_LOG_LEVEL_MAPPER_CLASS = "level.mapper";

	public static final String PROP_PREFIX = "mhus.lib.";

	public static final String PROP_LOG_PARAMETER_MAPPER_CLASS = "parameter.mapper";

	public static final String PROP_LOG_PARAMETER_ENTRY_MAPPER_CLASS = "parameter.entry.mapper";

	public static final String PROP_BASE_DIR = "base.dir";

	public static final String PROP_LOG_CONSOLE_REDIRECT = "log.console.redirect";
	

}
