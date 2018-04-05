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
package de.mhus.lib.core.mail;

import java.io.File;

public class MailAttachment {

	private File file;
	private String name;
	private boolean deleteAfterSent = false;

	public MailAttachment(String path, boolean deleteAfterSent) {
		this(new File(path), deleteAfterSent);
	}

	public MailAttachment(File file, boolean deleteAfterSent) {
		this.file = file;
		this.name = file.getName();
		this.deleteAfterSent = deleteAfterSent;
	}
	
	public MailAttachment(String path, String name, boolean deleteAfterSent) {
		this.file = new File(path);
		this.name = name;
		this.deleteAfterSent = deleteAfterSent;
	}

	public MailAttachment(File file, String name, boolean deleteAfterSent) {
		this.file = file;
		this.name = name;
		this.deleteAfterSent = deleteAfterSent;
	}
	
	public File getFile() {
		return file;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + ":" + file;
	}

	public boolean isDeleteAfterSent() {
		return deleteAfterSent;
	}

}
