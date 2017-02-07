package de.mhus.lib.cao.adb;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.cao.action.DeleteRenditionConfiguration;
import de.mhus.lib.cao.aspect.Changes;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.Successful;

public class AdbDeleteRendition extends CaoAction {

	@Override
	public String getName() {
		return DELETE_RENDITION;
	}

	@Override
	public CaoConfiguration createConfiguration(CaoList list, IProperties configuration) throws CaoException {
		return new DeleteRenditionConfiguration(null, list, null);
	}

	@Override
	public boolean canExecute(CaoConfiguration configuration) {
		try {
			return configuration.getList().size() != 1 
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
			AdbNode parent = (AdbNode)configuration.getList().get(0);
			
			String rendition = configuration.getProperties().getString(DeleteRenditionConfiguration.RENDITION);

			// TODO delete rendition
			
			Changes changes = parent.adaptTo(Changes.class);
			if (changes != null) changes.deletedRendition(rendition);
			
			return new Successful(getName());
		} catch (Throwable t) {
			log().d(t);
			return new NotSuccessful(getName(),t.toString(),-1);
		}

	}
	
}
