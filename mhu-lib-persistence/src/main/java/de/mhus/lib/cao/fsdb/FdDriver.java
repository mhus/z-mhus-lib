package de.mhus.lib.cao.fsdb;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoLoginForm;

public class FdDriver extends CaoDriver {

	@Override
	public CaoCore connect(URI uri, String authentication) {
		try {
			return new FdCore("fd_" + UUID.randomUUID(), this, new File(  uri.getPath() ) );
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public CaoLoginForm createLoginForm(URI uri, String authentication) {
		return null;
	}

}
