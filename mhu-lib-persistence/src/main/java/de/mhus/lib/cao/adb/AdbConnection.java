package de.mhus.lib.cao.adb;

import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoActionList;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoAspectFactory;
import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.ConnectionAdapter;
import de.mhus.lib.errors.MException;

public class AdbConnection extends ConnectionAdapter {

	public AdbConnection(AdbCore con) {
		super(con);
	}

}
