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
package de.mhus.lib.core.util;

public abstract class CompareDirEntry {

    public abstract boolean isFolder();

    public String getParentPath(String path) {
        return path.substring(0, path.lastIndexOf('/')) + ',';
    }

    public String extractPath(String path) {
        return path.substring(0, path.lastIndexOf(','));
    }

    /**
     * Compare with the other entry. If the entries are not differed (not changed) return true.
     * Otherwise return false. Usually compare size, MD5 and/or modify date.
     *
     * @param other
     * @return true if equals
     */
    public abstract boolean compareWithEntry(CompareDirEntry other);
}
