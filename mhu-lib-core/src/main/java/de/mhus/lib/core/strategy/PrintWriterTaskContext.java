package de.mhus.lib.core.strategy;

import java.io.PrintWriter;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.PrintWriterLog;

/**
 * <p>PrintWriterTaskContext class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PrintWriterTaskContext implements TaskContext {

	private MProperties attributes = new MProperties();
	private IConfig config;
	private boolean testOnly = false;
	private PrintWriter writer;
	private long estimated;
	private long step;
	private Log log = null;
	private String errorMessage;
	
	/**
	 * <p>Constructor for PrintWriterTaskContext.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param writer a {@link java.io.PrintWriter} object.
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 * @param testOnly a boolean.
	 */
	public PrintWriterTaskContext(String name, PrintWriter writer, IConfig config, boolean testOnly) {
		log = new PrintWriterLog(name, writer);
		this.writer = writer;
		this.config = config;
		this.testOnly = testOnly;
	}
	
	/**
	 * <p>Constructor for PrintWriterTaskContext.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.PrintWriterLog} object.
	 * @param config a {@link de.mhus.lib.core.config.IConfig} object.
	 * @param testOnly a boolean.
	 */
	public PrintWriterTaskContext(PrintWriterLog log, IConfig config, boolean testOnly) {
		this.log = log;
		this.writer = log.getWriter();
		this.config = config;
		this.testOnly = testOnly;
	}
	
	/** {@inheritDoc} */
	@Override
	public void println() {
		writer.println();
	}

	/** {@inheritDoc} */
	@Override
	public void println(Object... out) {
		for (Object o : out)
			writer.print(o);
		writer.println();
	}

	/** {@inheritDoc} */
	@Override
	public void print(Object... out) {
		for (Object o : out)
			writer.print(o);
	}

	/** {@inheritDoc} */
	@Override
	public Log log() {
		return log;
	}

	/** {@inheritDoc} */
	@Override
	public void setSteps(long steps) {
		estimated = steps;
	}

	/** {@inheritDoc} */
	@Override
	public void setStep(long step) {
		this.step = step;
	}

	/** {@inheritDoc} */
	@Override
	public void incrementStep() {
		step++;
	}

	/** {@inheritDoc} */
	@Override
	public IConfig getConfig() {
		return config;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTestOnly() {
		return testOnly;
	}

	/** {@inheritDoc} */
	@Override
	public MProperties getParameters() {
		return attributes;
	}

	/**
	 * <p>Getter for the field <code>estimated</code>.</p>
	 *
	 * @return a long.
	 */
	public long getEstimated() {
		return estimated;
	}

	/**
	 * <p>Getter for the field <code>step</code>.</p>
	 *
	 * @return a long.
	 */
	public long getStep() {
		return step;
	}

	/** {@inheritDoc} */
	@Override
	public void addErrorMessage(String msg) {
		if (msg == null) return;
		if (errorMessage == null)
			errorMessage = msg;
		else
			errorMessage = errorMessage + "\n" + msg;
	}

	/** {@inheritDoc} */
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

}
