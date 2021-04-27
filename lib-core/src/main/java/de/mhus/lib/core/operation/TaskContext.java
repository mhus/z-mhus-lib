/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.operation;

import de.mhus.lib.core.node.INode;

public interface TaskContext extends Monitor {

    /**
     * Return a local configuration object if exists or null if no configuration is present.
     *
     * @return The Configuration
     */
    INode getConfig();

    /**
     * Return the parameters of the current operation call.
     *
     * @return The job parameters
     */
    INode getParameters();

    /**
     * Add a error message (will be appended to the error message).
     *
     * @param msg
     */
    void addErrorMessage(String msg);

    /**
     * Return the current error message.
     *
     * @return The messages as one string.
     */
    String getErrorMessage();
}
