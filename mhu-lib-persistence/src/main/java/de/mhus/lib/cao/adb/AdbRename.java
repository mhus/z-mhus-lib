package de.mhus.lib.cao.adb;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.cao.action.CopyConfiguration;
import de.mhus.lib.cao.action.RenameConfiguration;
import de.mhus.lib.cao.aspect.Changes;
import de.mhus.lib.cao.fdb.FdbNode;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.Successful;

public class AdbRename extends CaoAction {

	@Override
	public String getName() {
		return RENAME;
	}

	@Override
	public CaoConfiguration createConfiguration(CaoList list, IProperties configuration) throws CaoException {
		return new RenameConfiguration(null, list, null);
	}

	@Override
	public boolean canExecute(CaoConfiguration configuration) {
		try {
			return configuration.getList().size() == 1 
					&&
					core.containsNodes(configuration.getList())
					;
		} catch (Throwable t) {
			log().d(t);
			return false;
		}
	}

	@Override
	public OperationResult doExecuteInternal(CaoConfiguration configuration, Monitor monitor) throws CaoException {
		if (!canExecute(configuration)) return new NotSuccessful(getName(), "can't execute", -1);

		try {
			String name = configuration.getProperties().getString(RenameConfiguration.NAME);
			AdbNode n = (AdbNode)configuration.getList().get(0);

			// TODO rename
			
			Changes change = n.adaptTo(Changes.class);
			if (change != null) change.renamed();

			return new Successful(getName(),"ok",0);
		} catch (Throwable t) {
			log().d(t);
			return new NotSuccessful(getName(),t.toString(),-1);
		}

	}
	
}
