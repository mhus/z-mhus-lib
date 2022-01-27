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

import de.mhus.lib.core.util.MUri;

public class NotSuccessful extends MutableOperationResult {

    public NotSuccessful(String path, String msg, int rc) {
        setSuccessful(false);
        setMsg(MUri.implodeKeyValues("m", msg));
        setOperationPath(path);
        setReturnCode(rc);
    }

    public NotSuccessful(Operation operation, String msg, int rc) {
        setSuccessful(false);
        setMsg(MUri.implodeKeyValues("m", msg));
        setReturnCode(rc);
        if (operation != null && operation.getDescription() != null) {
            setOperationPath(operation.getDescription().getPath());
            setCaption(operation.getDescription().getCaption());
        }
    }

    public NotSuccessful(Operation operation, String msg, String caption, int rc) {
        setSuccessful(false);
        setMsg(MUri.implodeKeyValues("m", msg, "c", caption));
        setReturnCode(rc);
        if (operation != null && operation.getDescription() != null) {
            setOperationPath(operation.getDescription().getPath());
            setCaption(operation.getDescription().getCaption());
        }
    }
}
