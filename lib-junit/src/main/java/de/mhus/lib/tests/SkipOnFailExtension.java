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
            ((TestCase) instance.get()).skipTest = true;
        }
    }
}
