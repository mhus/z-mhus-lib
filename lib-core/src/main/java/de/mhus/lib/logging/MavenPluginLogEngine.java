package de.mhus.mvn.manual;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;

import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class MavenPluginLogEngine extends LogEngine {

	private Log log;

	public MavenPluginLogEngine(AbstractMojo owner) {
		super(owner.getClass().getName());
		this.log = owner.getLog();
	}

	@Override
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	@Override
	public boolean isFatalEnabled() {
		return log.isErrorEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	@Override
	public void trace(Object message) {
		log.debug(String.valueOf(message));
	}

	@Override
	public void trace(Object message, Throwable t) {
		if (t == null)
			trace(message);
		else
			log.debug(String.valueOf(message), t);
	}

	@Override
	public void debug(Object message) {
		log.debug(String.valueOf(message));
	}

	@Override
	public void debug(Object message, Throwable t) {
		if (t == null)
			debug(message);
		else
			log.debug(String.valueOf(message), t);
	}

	@Override
	public void info(Object message) {
		log.info(String.valueOf(message));
	}

	@Override
	public void info(Object message, Throwable t) {
		if (t == null)
			info(message);
		else
			log.info(String.valueOf(message), t);
	}

	@Override
	public void warn(Object message) {
		log.warn(String.valueOf(message));
	}

	@Override
	public void warn(Object message, Throwable t) {
		if (t == null)
			warn(message);
		else
			log.warn(String.valueOf(message), t);
	}

	@Override
	public void error(Object message) {
		log.error(String.valueOf(message));
	}

	@Override
	public void error(Object message, Throwable t) {
		if (t == null)
			error(message);
		else
			log.error(String.valueOf(message), t);
	}

	@Override
	public void fatal(Object message) {
		log.error(String.valueOf(message));
	}

	@Override
	public void fatal(Object message, Throwable t) {
		if (t == null)
			fatal(message);
		else
			log.error(String.valueOf(message), t);
	}

	@Override
	public void doInitialize(LogFactory logFactory) {

	}

	@Override
	public void close() {
	}

}
