package de.mhus.lib.cao.fsdb;

import java.io.File;
import java.util.Map.Entry;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.CaoWritableElement;
import de.mhus.lib.cao.action.CantExecuteException;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.cao.action.CreateConfiguration;
import de.mhus.lib.cao.action.DeleteConfiguration;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.Successful;

public class FdCreate extends CaoAction {

	@Override
	public String getName() {
		return CREATE;
	}

	@Override
	public CaoConfiguration createConfiguration(CaoList list, IProperties configuration) throws CaoException {
		return new CreateConfiguration(null, list, null);
	}

	@Override
	public boolean canExecute(CaoConfiguration configuration) {
		try {
			return configuration.getList().size() == 1 
					&& 
					configuration.getList().get(0) instanceof FdNode 
					&&
					! new File( ((FdNode)configuration.getList().get(0)).getFile(), configuration.getProperties().getString(CreateConfiguration.NAME) ).exists()
					;
		} catch (Throwable t) {
			log().d(t);
			return false;
		}
	}

	@Override
	public OperationResult doExecute(CaoConfiguration configuration, Monitor monitor) throws CaoException {
		if (!canExecute(configuration)) return new NotSuccessful(getName(), "can't execute", -1);

		try {
			FdNode parent = (FdNode)configuration.getList().get(0);
			
			File nextFile = new File(parent.getFile(), configuration.getProperties().getString(CreateConfiguration.NAME) );
			nextFile.mkdir();
			((FdConnection)parent.getConnection()).indexFile(nextFile);
			
			CaoNode nextNode = parent.getNode(nextFile.getName());
			CaoWritableElement nextWrite = nextNode.getWritableNode();
			
			for (Entry<String, Object> entry : configuration.getProperties().entrySet()) {
				if (!entry.getKey().startsWith("_"))
					nextWrite.put(entry.getKey(), entry.getValue());
			}
			nextWrite.getUpdateAction().doExecute(monitor);
			nextNode.reload();
			
			return new Successful(getName(),"ok",0,nextNode);
		} catch (Throwable t) {
			log().d(t);
			return new NotSuccessful(getName(),t.toString(),-1);
		}
	}

}
