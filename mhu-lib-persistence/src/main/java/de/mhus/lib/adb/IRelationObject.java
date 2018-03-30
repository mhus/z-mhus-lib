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
package de.mhus.lib.adb;

import de.mhus.lib.adb.model.FieldRelation;
import de.mhus.lib.sql.DbConnection;

/**
 * <p>IRelationObject interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface IRelationObject {

	/**
	 * <p>prepareCreate.</p>
	 *
	 * @throws java.lang.Exception if any.
	 */
	void prepareCreate() throws Exception;

	/**
	 * <p>created.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 */
	void created(DbConnection con) throws Exception;

	/**
	 * <p>saved.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 */
	void saved(DbConnection con) throws Exception;

	/**
	 * <p>setManager.</p>
	 *
	 * @param fieldRelation a {@link de.mhus.lib.adb.model.FieldRelation} object.
	 * @param obj a {@link java.lang.Object} object.
	 */
	void setManager(FieldRelation fieldRelation, Object obj);

	/**
	 * <p>isChanged.</p>
	 *
	 * @return a boolean.
	 */
	boolean isChanged();

	/**
	 * <p>loaded.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 */
	void loaded(DbConnection con);

	/**
	 * <p>prepareSave.</p>
	 *
	 * @param con a {@link de.mhus.lib.sql.DbConnection} object.
	 * @throws java.lang.Exception if any.
	 */
	void prepareSave(DbConnection con) throws Exception;

}
