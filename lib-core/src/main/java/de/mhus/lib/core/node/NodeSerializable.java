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
package de.mhus.lib.core.node;

public interface NodeSerializable {

    /**
     * Read the inner state of the object from the given node object.
     *
     * @param node Node with stored state for this object
     * @throws Exception
     */
    void readSerializabledNode(INode node) throws Exception;

    /**
     * Write the inner state of the object to the given node object.
     *
     * @param node Node to store the state of this object in
     * @throws Exception
     */
    void writeSerializabledNode(INode node) throws Exception;
}
