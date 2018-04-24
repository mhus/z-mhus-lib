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
package de.mhus.lib.core.security;

import java.util.Collection;

import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.MException;

public interface ModifyAccountApi {

	void createAccount(String username, String password, IReadProperties properties) throws MException;
	
	void deleteAccount(String username) throws MException;
	
	void changePassword(String username, String newPassword) throws MException;
	
	void changeAccount(String username, IReadProperties properties) throws MException;
	
	void appendGroups(String username, String ... group) throws MException;
	
	void removeGroups(String username, String ... group) throws MException;

	Collection<String> getGroups(String username) throws MException;
	
}
