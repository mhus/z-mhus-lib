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

import java.util.HashSet;
import java.util.UUID;

import org.apache.shiro.authz.AuthorizationException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.definition.DefRoot;
import de.mhus.lib.core.logging.LogProperties;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.ParameterDefinition;
import de.mhus.lib.core.util.ParameterDefinitions;
import de.mhus.lib.core.util.Version;
import de.mhus.lib.form.IFormProvider;

public abstract class AbstractOperation extends MLog implements Operation {

    protected boolean strictParameterCheck = false;
    private Object owner;
    private OperationDescription description;
    private MNls nls;
    private UUID uuid = UUID.randomUUID();

    @Override
    public boolean hasAccess(TaskContext context) {
        try {
            if (Aaa.isAnnotated(getClass())) Aaa.checkPermission(getClass());
        } catch (AuthorizationException e) {
            return false;
        }
        return true;
    }

    @Override
    public final OperationResult doExecute(TaskContext context) throws Exception {
        log().d("execute", new LogProperties(context.getParameters()));
        // must be done by caller before
        //        if (!hasAccess()) {
        //            log().d("access denied", context, context.getErrorMessage());
        //            return new NotSuccessful(this, "access denied",
        // OperationResult.ACCESS_DENIED);
        //        }
        //        if (!canExecute(context)) {
        //            log().d("execution denied", context.getErrorMessage());
        //            return new NotSuccessful(
        //                    this,
        //                    context.getErrorMessage() != null ? context.getErrorMessage() : "can't
        // execute",
        //                    OperationResult.NOT_EXECUTABLE);
        //        }
        OperationResult ret = doExecute2(context);
        log().d("result", ret);
        return ret;
    }

    protected abstract OperationResult doExecute2(TaskContext context) throws Exception;

    @Override
    public boolean isBusy() {
        synchronized (this) {
            return owner != null;
        }
    }

    @Override
    public boolean setBusy(Object owner) {
        synchronized (this) {
            if (this.owner != null) return false;
            this.owner = owner;
        }
        return true;
    }

    @Override
    public boolean releaseBusy(Object owner) {
        synchronized (this) {
            if (this.owner == null) return true;
            //			if (!this.owner.equals(owner)) return false;
            if (this.owner != owner) return false;
            this.owner = null;
        }
        return true;
    }

    @Override
    public boolean canExecute(TaskContext context) {
        if (getDescription() == null) return true; // no definition, no check
        return validateParameters(getDescription().getParameterDefinitions(), context);
    }

    @Override
    public OperationDescription getDescription() {
        if (description == null) description = createDescription();
        return description;
    }

    /**
     * Create and return a operation definition. The method is called only one time.
     *
     * @return
     */
    protected OperationDescription createDescription() {

        Class<?> clazz = this.getClass();
        String title = clazz.getName();
        Version version = null;
        DefRoot form = null;

        String path = clazz.getCanonicalName();

        de.mhus.lib.annotations.strategy.OperationDescription desc =
                getClass()
                        .getAnnotation(de.mhus.lib.annotations.strategy.OperationDescription.class);
        if (desc != null) {
            if (MString.isSet(desc.title())) title = desc.title();
            if (desc.clazz() != Object.class) {
                clazz = desc.clazz();
                path = clazz.getCanonicalName();
            }
            if (MString.isSet(desc.path())) {
                path = desc.path();
            }
            if (MString.isSet(desc.version())) {
                version = new Version(desc.version());
            }
            strictParameterCheck = desc.strictParameterCheck();
        }

        if (this instanceof IFormProvider) {
            form = ((IFormProvider) this).getForm();
        }

        MProperties labels = null;
        if (desc != null) {
            labels = IProperties.explodeToMProperties(desc.labels());
        }
        OperationDescription ret =
                new OperationDescription(getUuid(), path, version, this, title, labels, form);
        prepareCreatedDescription(ret);
        return ret;
    }

    /**
     * Overwrite to manipulate created description.
     *
     * @param desc
     */
    protected void prepareCreatedDescription(OperationDescription desc) {}

    public boolean validateParameters(ParameterDefinitions definitions, TaskContext context) {
        if (definitions == null) return true;
        HashSet<String> sendKeys = null;
        if (strictParameterCheck) sendKeys = new HashSet<>(context.getParameters().keys());
        for (ParameterDefinition def : definitions.values()) {
            Object v = context.getParameters().get(def.getName());
            if (def.isMandatory() && v == null) {
                context.addErrorMessage("Mandatory: " + def.getName());
                return false;
            }
            if (!def.validate(v)) {
                context.addErrorMessage("Not valid: " + def.getName());
                return false;
            }
            if (strictParameterCheck) sendKeys.remove(def.getName());
        }
        if (strictParameterCheck && sendKeys.size() != 0) {
            context.addErrorMessage("Unknown parameters: " + sendKeys);
            return false;
        }
        return true;
    }

    @Override
    public MNls getNls() {
        if (nls == null) nls = MNls.lookup(this);
        return nls;
    }

    @Override
    public String nls(String text) {
        return MNls.find(this, text);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}
