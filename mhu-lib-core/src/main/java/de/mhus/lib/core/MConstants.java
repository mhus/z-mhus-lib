/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import java.util.Locale;
import java.util.UUID;

public class MConstants {

	public static final String LOG_MAPPER = "_mhus_log";

	public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

	public static final Locale LOCALE_DE_DE = new Locale("de","DE");
	public static final Locale LOCALE_EN_US = new Locale("en","US");

	public static final String PROP_PREFIX = "mhus.lib.";
	
	public static final String PROP_DIRTY_TRACE = "mhu.lib.api.trace";
	public static final String PROP_API_FACTORY_CLASS = "mhus.api.factory";
//	public static final String PROP_FILE_WATCH = "mhus.config.watch";
	public static final String PROP_CONFIG_FILE = PROP_PREFIX + "config.file";

	public static final String DEFAULT_MHUS_CONFIG_FILE = "mhus-config.xml";

	public static final String PROP_LOG_FACTORY_CLASS = "log.factory";
	
	public static final String PROP_LOG_MLOG_FACTORY_CLASS = "log.mlog.factory";

	public static final String PROP_LOG_LEVEL_MAPPER_CLASS = "level.mapper";

	public static final String PROP_LOG_PARAMETER_MAPPER_CLASS = "parameter.mapper";

	public static final String PROP_LOG_PARAMETER_ENTRY_MAPPER_CLASS = "parameter.entry.mapper";

	public static final String PROP_BASE_DIR = "base.dir";

	public static final String PROP_LOG_CONSOLE_REDIRECT = "log.console.redirect";

	public static final String DEFAULT_MHUS_TIMER_CONFIG_FILE = "mhus-timer.properties";

	public static final String PROP_TIMER_CONFIG_FILE = "timer.config.file";

	public static final String PROP_LOG_MAX_MESSAGE_SIZE = "log.max.message.size";

	public static final String PROP_LOG_LEVEL = "log.level";

	public static final int MAX_DEPTH_LEVEL = 20;
	

}
