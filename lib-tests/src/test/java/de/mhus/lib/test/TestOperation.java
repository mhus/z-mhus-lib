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
package de.mhus.lib.test;

import java.util.UUID;

import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationDescription;
import de.mhus.lib.core.operation.OperationResult;
import de.mhus.lib.core.operation.TaskContext;
import de.mhus.lib.core.util.MNls;

public class TestOperation implements Operation {

    @Override
    public MNls getNls() {
        return null;
    }

    @Override
    public String nls(String text) {
        return null;
    }

    @Override
    public boolean hasAccess(TaskContext context) {
        return false;
    }

    @Override
    public boolean canExecute(TaskContext context) {
        return false;
    }

    @Override
    public OperationDescription getDescription() {
        return new OperationDescription(this, "Test", null);
    }

    @Override
    public OperationResult doExecute(TaskContext context) throws Exception {
        return null;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public boolean setBusy(Object owner) {
        return false;
    }

    @Override
    public boolean releaseBusy(Object owner) {
        return false;
    }

    @Override
    public UUID getUuid() {
        return null;
    }
}
