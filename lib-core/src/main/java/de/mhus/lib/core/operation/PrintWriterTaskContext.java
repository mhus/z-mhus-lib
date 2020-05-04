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
package de.mhus.lib.core.operation;

import java.io.PrintWriter;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.PrintWriterLog;

public class PrintWriterTaskContext implements TaskContext {

    private MProperties attributes = new MProperties();
    private IConfig config;
    private boolean testOnly = false;
    private PrintWriter writer;
    private long estimated;
    private long step;
    private Log log = null;
    private String errorMessage;

    public PrintWriterTaskContext(
            String name, PrintWriter writer, IConfig config, boolean testOnly) {
        log = new PrintWriterLog(name, writer);
        this.writer = writer;
        this.config = config;
        this.testOnly = testOnly;
    }

    public PrintWriterTaskContext(PrintWriterLog log, IConfig config, boolean testOnly) {
        this.log = log;
        this.writer = log.getWriter();
        this.config = config;
        this.testOnly = testOnly;
    }

    @Override
    public void println() {
        writer.println();
    }

    @Override
    public void println(Object... out) {
        for (Object o : out) writer.print(o);
        writer.println();
    }

    @Override
    public void print(Object... out) {
        for (Object o : out) writer.print(o);
    }

    @Override
    public Log log() {
        return log;
    }

    @Override
    public void setSteps(long steps) {
        estimated = steps;
    }

    @Override
    public void setStep(long step) {
        this.step = step;
    }

    @Override
    public void incrementStep() {
        step++;
    }

    @Override
    public IConfig getConfig() {
        return config;
    }

    @Override
    public boolean isTestOnly() {
        return testOnly;
    }

    @Override
    public MProperties getParameters() {
        return attributes;
    }

    public long getEstimated() {
        return estimated;
    }

    public long getStep() {
        return step;
    }

    @Override
    public void addErrorMessage(String msg) {
        if (msg == null) return;
        if (errorMessage == null) errorMessage = msg;
        else errorMessage = errorMessage + "\n" + msg;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
