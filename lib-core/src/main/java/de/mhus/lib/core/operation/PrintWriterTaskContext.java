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

import java.io.PrintWriter;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.PrintWriterLog;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.MNode;

public class PrintWriterTaskContext implements TaskContext {

    private INode attributes = new MNode();
    private INode config;
    private boolean testOnly = false;
    private PrintWriter writer;
    private long estimated;
    private long step;
    private Log log = null;
    private String errorMessage;

    public PrintWriterTaskContext(String name, PrintWriter writer, INode config, boolean testOnly) {
        log = new PrintWriterLog(name, writer);
        this.writer = writer;
        this.config = config;
        this.testOnly = testOnly;
    }

    public PrintWriterTaskContext(PrintWriterLog log, INode config, boolean testOnly) {
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
    public INode getConfig() {
        return config;
    }

    public boolean isTestOnly() {
        return testOnly;
    }

    @Override
    public INode getParameters() {
        return attributes;
    }

    public long getEstimated() {
        return estimated;
    }

    @Override
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
