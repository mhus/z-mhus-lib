package de.mhus.lib.cao.fs;

import java.io.File;
import java.net.URI;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoLoginForm;
import de.mhus.lib.cao.CaoMetadata;
import de.mhus.lib.cao.CaoPolicyProvider;
import de.mhus.lib.core.MProperties;

public class FsDriver extends CaoDriver {

	@Override
	public CaoConnection connect(URI uri, String authentication) {
		return new FsConnection(this, new File(  uri.getPath() ) );
	}

	@Override
	public CaoLoginForm createLoginForm(URI uri, String authentication) {
		return null;
	}

}
