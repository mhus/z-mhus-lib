package de.mhus.lib.core;

import java.util.Locale;
import java.util.UUID;

/**
 * <p>MConstants class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MConstants {

	/** Constant <code>LOG_MAPPER="_mhus_log"</code> */
	public static final String LOG_MAPPER = "_mhus_log";

	/** Constant <code>EMPTY_UUID</code> */
	public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	/** Constant <code>LOCALE_DE_DE</code> */
	public static final Locale LOCALE_DE_DE = new Locale("de","DE");
	/** Constant <code>LOCALE_EN_US</code> */
	public static final Locale LOCALE_EN_US = new Locale("en","US");

	/** Constant <code>PROP_PREFIX="mhus.lib."</code> */
	public static final String PROP_PREFIX = "mhus.lib.";
	
	/** Constant <code>PROP_DIRTY_TRACE="mhu.lib.singleton.trace"</code> */
	public static final String PROP_DIRTY_TRACE = "mhu.lib.singleton.trace";
	/** Constant <code>PROP_SINGLETON_FACTORY_CLASS="mhus.singleton.factory"</code> */
	public static final String PROP_SINGLETON_FACTORY_CLASS = "mhus.singleton.factory";
//	public static final String PROP_FILE_WATCH = "mhus.config.watch";
	/** Constant <code>PROP_CONFIG_FILE="PROP_PREFIX + config.file"</code> */
	public static final String PROP_CONFIG_FILE = PROP_PREFIX + "config.file";

	/** Constant <code>DEFAULT_MHUS_CONFIG_FILE="mhus-config.xml"</code> */
	public static final String DEFAULT_MHUS_CONFIG_FILE = "mhus-config.xml";

	/** Constant <code>PROP_LOG_FACTORY_CLASS="log.factory"</code> */
	public static final String PROP_LOG_FACTORY_CLASS = "log.factory";

	/** Constant <code>PROP_LOG_LEVEL_MAPPER_CLASS="level.mapper"</code> */
	public static final String PROP_LOG_LEVEL_MAPPER_CLASS = "level.mapper";

	/** Constant <code>PROP_LOG_PARAMETER_MAPPER_CLASS="parameter.mapper"</code> */
	public static final String PROP_LOG_PARAMETER_MAPPER_CLASS = "parameter.mapper";

	/** Constant <code>PROP_LOG_PARAMETER_ENTRY_MAPPER_CLASS="parameter.entry.mapper"</code> */
	public static final String PROP_LOG_PARAMETER_ENTRY_MAPPER_CLASS = "parameter.entry.mapper";

	/** Constant <code>PROP_BASE_DIR="base.dir"</code> */
	public static final String PROP_BASE_DIR = "base.dir";

	/** Constant <code>PROP_LOG_CONSOLE_REDIRECT="log.console.redirect"</code> */
	public static final String PROP_LOG_CONSOLE_REDIRECT = "log.console.redirect";

	/** Constant <code>DEFAULT_MHUS_TIMER_CONFIG_FILE="mhus_timer.properties"</code> */
	public static final String DEFAULT_MHUS_TIMER_CONFIG_FILE = "mhus_timer.properties";

	/** Constant <code>PROP_TIMER_CONFIG_FILE="timer.config.file"</code> */
	public static final String PROP_TIMER_CONFIG_FILE = "timer.config.file";
	

}
