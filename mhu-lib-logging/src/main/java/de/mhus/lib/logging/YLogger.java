package de.mhus.lib.logging;

import de.mhus.lib.core.logging.Log;

/**
 * <p>YLogger class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class YLogger extends Log {

	private Log[] targets;

	/**
	 * <p>Constructor for YLogger.</p>
	 *
	 * @param targets a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public YLogger(Log ... targets) {
		super(targets[0].getName());
		this.targets = targets;
	}

    /** {@inheritDoc} */
    @Override
	public void log(LEVEL level, Object ... msg) {
    	for (Log target : targets)
    		target.log(level, msg);
    }
    
	/** {@inheritDoc} */
	@Override
	public void update() {
	}
	
	/** {@inheritDoc} */
	@Override
	public void register() {
	}
	
	/** {@inheritDoc} */
	@Override
	public void unregister() {
	}

	/** {@inheritDoc} */
	@Override
	public boolean isLevelEnabled(LEVEL level) {
		return targets[0].isLevelEnabled(level);
	}
	
	/** {@inheritDoc} */
	@Override
	public void close() {
    	for (Log target : targets)
    		target.close();
	}
	
}
