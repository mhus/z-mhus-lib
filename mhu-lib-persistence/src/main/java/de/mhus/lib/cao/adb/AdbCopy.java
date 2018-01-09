package de.mhus.lib.cao.adb;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.cao.action.CopyConfiguration;
import de.mhus.lib.cao.aspect.Changes;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.Successful;

public class AdbCopy extends CaoAction {

	@Override
	public String getName() {
		return COPY;
	}

	@Override
	public CaoConfiguration createConfiguration(CaoList list, IProperties configuration) throws CaoException {
		return new CopyConfiguration(null, list, null);
	}

	@Override
	public boolean canExecute(CaoConfiguration configuration) {
		Object to = configuration.getProperties().getProperty(CopyConfiguration.NEW_PARENT);
		return configuration.getList().size() > 0 
				&&
				to instanceof AdbNode
				&&
				core.containsNode((CaoNode)to)
				&&
				core.containsNodes(configuration.getList());
	}

	@Override
	public OperationResult doExecuteInternal(CaoConfiguration configuration, Monitor monitor) throws CaoException {
		if (!canExecute(configuration)) return new NotSuccessful(getName(), "can't execute", -1);

		try {

//			AdbNode to = (AdbNode) configuration.getProperties().getProperty(CopyConfiguration.NEW_PARENT);
//			final boolean recursive = configuration.getProperties().getBoolean(CopyConfiguration.RECURSIVE, false);
	
			CaoNode createdNode = null;
			monitor.setSteps(configuration.getList().size());
			for (CaoNode item : configuration.getList()) {
				monitor.log().d("===",item);
				if (item instanceof AdbNode) {
					monitor.incrementStep();
					AdbNode n = (AdbNode)item;
					
					// TODO copy
					
					Changes change = createdNode.adaptTo(Changes.class);
					if (change != null) change.deleted();
				}
			}
			return new Successful(getName(),"ok",0,createdNode);
		} catch (Throwable t) {
			log().d(t);
			return new NotSuccessful(getName(),t.toString(),-1);
		}

	}
	
}
