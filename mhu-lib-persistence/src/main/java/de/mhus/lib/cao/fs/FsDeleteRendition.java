package de.mhus.lib.cao.fs;

import java.io.File;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoException;
import de.mhus.lib.cao.CaoList;
import de.mhus.lib.cao.action.CaoConfiguration;
import de.mhus.lib.cao.action.CreateConfiguration;
import de.mhus.lib.cao.action.DeleteConfiguration;
import de.mhus.lib.cao.action.DeleteRenditionConfiguration;
import de.mhus.lib.cao.action.UploadRenditionConfiguration;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.strategy.Monitor;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.Successful;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;

public class FsDeleteRendition extends CaoAction {

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
					configuration.getList().get(0) instanceof FsNode 
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
			FsNode parent = (FsNode)configuration.getList().get(0);
			
			String rendition = configuration.getProperties().getString(DeleteRenditionConfiguration.RENDITION);
			File renditionFile = ((FsConnection)parent.getConnection()).getContentFileFor(parent.getFile(), rendition);
			if (renditionFile == null || !renditionFile.exists() || !renditionFile.isFile()) throw new MException("rendition not found", rendition);
			
			if (!renditionFile.delete())
				throw new MException("can't delete rendition",rendition);
			
			return new Successful(getName());
		} catch (Throwable t) {
			log().d(t);
			return new NotSuccessful(getName(),t.toString(),-1);
		}
	}
}
