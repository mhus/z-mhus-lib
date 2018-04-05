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
