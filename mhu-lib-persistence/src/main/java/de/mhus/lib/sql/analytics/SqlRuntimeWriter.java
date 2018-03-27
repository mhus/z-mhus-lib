package de.mhus.lib.sql.analytics;

import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MDate;

public class SqlRuntimeWriter extends SqlRuntimeAnalyzer {

	private Timer timer;
	private File file;

	@Override
	public void start() {
		file = MApi.getFile(getClass().getCanonicalName() + "_" + MDate.toFileFormat(new Date()) + ".csv");
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				doSave();
			}
		}, 60000, 60000);
	}

	protected void doSave() {
		try {
			PrintStream ps = new PrintStream(file);
			synchronized (this) {
				for (Container container : list.values()) {
					ps.print(container.getSql());
					ps.print(";");
					ps.print(container.getCnt());
					ps.print(";");
					ps.println(container.getRuntime());
				}
				ps.flush();
				ps.close();
			}
		} catch (Throwable t) {
			log().e(file,t);
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
