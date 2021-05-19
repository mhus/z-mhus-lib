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
        return new OperationDescription(this,"Test",null);
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
