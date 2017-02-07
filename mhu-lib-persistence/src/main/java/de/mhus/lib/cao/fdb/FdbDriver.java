package de.mhus.lib.cao.fdb;

import java.io.File;
import java.net.URI;
import java.util.UUID;

import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoLoginForm;

public class FdbDriver extends CaoDriver {

	@Override
	public CaoCore connect(URI uri, String authentication) {
		try {
			return new FdbCore("fd_" + UUID.randomUUID(), this, new File(  uri.getPath() ) );
		} catch (Exception e) {
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
