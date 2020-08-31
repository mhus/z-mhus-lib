/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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

import de.mhus.lib.core.config.IConfig;

public class DefaultTaskContext extends DefaultMonitor implements TaskContext {

    public DefaultTaskContext(Class<?> owner) {
        super(owner);
    }

    protected IConfig config;
    protected boolean test = false;
    protected IConfig parameters;
    protected String errorMessage;

    public void setTestOnly(boolean test) {
        this.test = test;
    }

    public void setConfig(IConfig config) {
        this.config = config;
    }

    public void setParameters(IConfig parameters) {
        this.parameters = parameters;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    public boolean isTestOnly() {
        return test;
    }

    @Override
    public IConfig getParameters() {
        return parameters;
    }

    @Override
    public synchronized void addErrorMessage(String msg) {
        if (msg == null) return;
        if (errorMessage == null) errorMessage = msg;
        else errorMessage = errorMessage + "\n" + msg;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
