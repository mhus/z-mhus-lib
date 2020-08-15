/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.annotations.strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OperationService {

    /**
     * Display title of the operation. Default is the simple name of the current class.
     *
     * @return display name
     */
    String title() default "";
    /**
     * If you need to set the path by a class reference. Default path is the name of the current
     * class.
     *
     * @return path
     */
    Class<?> clazz() default Object.class;
    /**
     * Set full path with group.id and overwrite clazz() setting. If you set this option setting
     * clazz() makes no sense. Default path is the name of the current class.
     *
     * @return path
     */
    String path() default "";

    /**
     * Define the version of the operation. Default is 0.0.0
     *
     * @return The version
     */
    String version() default "";

    /**
     * Set to true if you wan't a strict parameter check. Unknown parameters will be rejected.
     *
     * @return true for strict check
     */
    boolean strictParameterCheck() default false;
}
