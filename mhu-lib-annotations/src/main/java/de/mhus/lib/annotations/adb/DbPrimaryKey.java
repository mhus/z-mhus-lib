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
package de.mhus.lib.annotations.adb;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The same like DbPersistent but the column is used as primary key. You can
 * mark more then one getter/setter as primary key to generate combined keys.
 * The names will be ordered alphabetically if you search an object with this
 * key.
 * 
 * @author mikehummel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DbPrimaryKey {
	boolean auto_id() default true;
	String prefix() default "";
}
