/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import de.mhus.lib.annotations.pojo.Hidden;
import de.mhus.lib.core.logging.Log;

/**
 * This class is currently only a place holder for a smarter strategy. But the interface should be
 * fix. TODO implement strategy
 *
 * @author mikehummel
 */
public class MLog implements ILog {

    public static final String LOG_LEVEL_MAPPING = "loglevelmapping";
    @Hidden private Log log;

    @Override
    public synchronized Log log() {
        if (log == null) {
            log = MApi.get().lookupLog(this);
        }
        return log;
    }
}
