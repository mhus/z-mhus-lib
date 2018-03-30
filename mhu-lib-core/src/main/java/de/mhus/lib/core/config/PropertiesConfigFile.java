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
package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.mhus.lib.errors.MException;

public class PropertiesConfigFile extends PropertiesConfig {

	private static final long serialVersionUID = 1L;
	private File file;

	public PropertiesConfigFile(File file) throws IOException {
		if (file.exists()) {
			FileInputStream fis = new FileInputStream(file);
			properties.load(fis);
			fis.close();
		}
		this.file = file;
	}
	
	public PropertiesConfigFile(InputStream is) throws IOException {
		properties.load(is);
		this.file = null;
	}
	
	public boolean canSave() {
		return file != null && file.canWrite();
	}
	
	public void save() throws MException {
		try {
			if (!canSave()) return;
			log().t("save config",this);
			FileOutputStream os = new FileOutputStream(file);
			properties.store(os, "");
			os.close();
		} catch (Exception e) {
			throw new MException(e);
		}
	}
	
	@Override
	public String toString() {
		return getClass() + ": " + (file == null ? "?" : file.getAbsolutePath());
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public File getFile() {
		return file;
	}

}
