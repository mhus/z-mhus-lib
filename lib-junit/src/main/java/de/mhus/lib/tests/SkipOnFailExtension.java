package de.mhus.lib.tests;

import java.util.Optional;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SkipOnFailExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {

        boolean failed = context.getExecutionException().isPresent();
        Optional<Object> instance = context.getTestInstance();
        if (failed && instance.isPresent() && instance.get() instanceof TestCase) {
            System.out.println("*** SKIP ON FAIL ***");
            ((TestCase)instance.get()).skipTest = true;
        }
    }

}
