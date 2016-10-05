package de.mhus.lib.logging.adapters;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import de.mhus.lib.core.logging.Log;

public class Log4JAppender extends AppenderSkeleton {

	
	@Override
	protected void append(LoggingEvent evt) {
		Level level = evt.getLevel();
		LocationInfo location = evt.getLocationInformation();
		String loggerName = evt.getLoggerName();
		Object msg = evt.getMessage();
		ThrowableInformation throwableInfo = evt.getThrowableInformation();
		Throwable t = null;
		if (throwableInfo != null)
			t = throwableInfo.getThrowable();
		
		Log logger = Log.getLog(loggerName);
		
		String method = location.getClassName() + "." + location.getMethodName() + ":" + location.getLineNumber();
		
		switch (level.toInt()) {
		case Level.FATAL_INT:
			logger.f(method, msg,t);
			break;
		case Level.ERROR_INT:
			logger.e(method, msg,t);
			break;
		case Level.WARN_INT:
			logger.w(method, msg,t);
			break;
		case Level.INFO_INT:
			logger.i(method, msg,t);
			break;
		case Level.DEBUG_INT:
			logger.d(method, msg,t);
			break;
		case Level.TRACE_INT:
			logger.t(method, msg,t);
			break;
		default:
			logger.t(method, msg,t);
			break;
		}
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	public static void configure() {
		
		Log4JAppender appender = new Log4JAppender();
		appender.setThreshold(Level.ALL);
		appender.setName("mlog2log4j");
		appender.activateOptions();
		
		Logger.getRootLogger().addAppender(appender);
		
	}
	
}
