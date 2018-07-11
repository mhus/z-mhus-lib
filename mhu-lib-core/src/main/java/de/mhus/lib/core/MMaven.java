package de.mhus.lib.core;

import java.io.File;

import org.w3c.dom.Document;

import de.mhus.lib.core.cfg.CfgString;

public class MMaven {
	
	private static CfgString CFG_MAVEN_LOCAL_REPO = new CfgString(MMaven.class, "repositoryLocaltion", null);
	private static String repositoryLocation;
	
	// https://www.codenotfound.com/maven-change-location-local-repository.html
	public static File locateArtifact(String groupId, String artifactId, String version, String type) {
		getLocalRepositoryLocation();
		if(type == null) type = "jar";
		
		String artifactLocation = groupId.replace('.', '/') + '/' + artifactId + '/' + version + '/' + artifactId + '-' + version + '.' + type;
		
		return new File(repositoryLocation + '/' + artifactLocation);
	}

	public static String getLocalRepositoryLocation() {
		
		if (repositoryLocation != null) return repositoryLocation;
		
		repositoryLocation =  CFG_MAVEN_LOCAL_REPO.value();

		if (repositoryLocation == null) {
			// check local settings
			try {
				File settingsFile = new File(MSystem.getUserHome() + "/.m2/settings.xml");
				if (settingsFile.exists() && settingsFile.isFile()) {
					Document settings = MXml.loadXml(settingsFile);
					repositoryLocation = MXml.getValue(settings.getDocumentElement(), "localRepository", null);
				}
			} catch (Throwable t) {}
		}
		
		if (repositoryLocation == null) {
			// check global settings
			if (MString.isSet(System.getenv("MAVEN_HOME"))) {
				try {
					File settingsFile = new File(System.getenv("MAVEN_HOME") + "/conf/settings.xml");
					if (settingsFile.exists() && settingsFile.isFile()) {
						Document settings = MXml.loadXml(settingsFile);
						repositoryLocation = MXml.getValue(settings.getDocumentElement(), "localRepository", null);
					}
				} catch (Throwable t) {}
			}
		}
		
		if (repositoryLocation == null) {
			// default location
			repositoryLocation =  "${user.home}/.m2/repository";
		}
		
		if (repositoryLocation != null) {
			repositoryLocation = repositoryLocation.replace("${user.home}", MSystem.getUserHome().getAbsolutePath());
		}
		
		return repositoryLocation;
	}
}
