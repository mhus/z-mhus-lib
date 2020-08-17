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

import java.util.concurrent.TimeoutException;

import de.mhus.lib.core.MThread;

public class WaitExecuteStrategy extends ExecuteStrategy {

    private Operation executable;
    private long waitTime;
    private long timeout;

    @Override
    protected OperationResult doExecute2(TaskContext context) throws Exception {
        if (executable == null)
            return new NotSuccessful(this, "executable not found", OperationResult.EMPTY);
        try {
            long cnt = timeout;
            ;
            while (!executable.setBusy(this)) {
                MThread.sleep(waitTime);
                if (cnt > 0) {
                    cnt -= waitTime;
                    if (cnt <= 0) throw new TimeoutException("timeout");
                }
            }
            if (executable == null)
                return new NotSuccessful(this, "executable not found", OperationResult.EMPTY);
            return executable.doExecute(context);
        } finally {
            if (executable != null) executable.releaseBusy(this);
        }
    }

    @Override
    public Operation getExecutable() {
        return executable;
    }

    @Override
    public void setExecutable(Operation executable) {
        this.executable = executable;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public OperationDescription getDescription() {
        if (executable == null) return null;
        return executable.getDescription();
    }

    @Override
    public boolean canExecute(TaskContext context) {
        if (executable == null) return false;
        return executable.canExecute(context);
    }

    @Override
    public boolean hasAccess() {
        if (executable == null) return false;
        return executable.hasAccess();
    }

    @Override
    protected OperationDescription createDescription() {
        return null;
    }
}
