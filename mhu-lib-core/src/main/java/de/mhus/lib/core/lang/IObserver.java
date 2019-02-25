/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.lang;

/**
 * Use this interface if you need a fast interface in inner structures. Do not use it in official APIs.
 * The interface replace the java Observer interface since it is be deprecated in jdk9. The implementation
 * is more lazy then the strict Observer - Observable implementation from java.
 * 
 * @author mikehummel
 *
 * @param <T>
 */
public interface IObserver<T> {

    /**
     * Called if an something happens.
     * 
     * @param source
     * @param reason 
     * @param arg
     */
    void update(Object source, Object reason, T arg);
}
