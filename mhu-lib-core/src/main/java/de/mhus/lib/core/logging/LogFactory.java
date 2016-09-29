package de.mhus.lib.core.logging;

import java.util.WeakHashMap;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.system.CfgManager;

/**
 * <p>Abstract LogFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(ConsoleFactory.class)
public abstract class LogFactory implements IBase {

	WeakHashMap<String, LogEngine> buffer = new WeakHashMap<String, LogEngine>();
	protected LEVEL level = LEVEL.INFO;
	protected LevelMapper levelMapper;
    private ParameterMapper parameterMapper;

/**
 * Convenience method to derive a name from the specified class and
 * call <code>getInstance(String)</code> with it.
 *
 * @param clazz Class for which a suitable Log name will be derived
 * @return a {@link de.mhus.lib.core.logging.LogEngine} object.
 */
    public LogEngine getInstance(Class<?> clazz) {
    	return getInstance(clazz.getCanonicalName());
    }

    public abstract void init(ResourceNode config) throws Exception;

	public void init() throws Exception {
		ResourceNode config = MSingleton.baseLookup(this,CfgManager.class).getCfg(this, null);
		init(config);
	/**
	 * <p>init.</p>
	 *
	 * @param config a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @throws java.lang.Exception if any.
	 */
	}
	
/**
 * <p>init.</p>
 *
 * @throws java.lang.Exception if any.
 */

			/**
			 * <p>Construct (if necessary) and return a <code>Log</code> instance,
			 * using the factory's current set of configuration attributes.</p>
			 *
			 * <p><strong>NOTE</strong> - Depending upon the implementation of
			 * the <code>LogFactory</code> you are using, the <code>Log</code>
			 * instance you are returned may or may not be local to the current
			 * application, and may or may not be returned again on a subsequent
			 * call with the same name argument.</p>
			 *
			 * @param name Logical name of the <code>Log</code> instance to be
			 *  returned (the meaning of this name is only known to the underlying
			 *  logging implementation that is being wrapped)
			 * @return a {@link de.mhus.lib.core.logging.LogEngine} object.
			 */
    public synchronized LogEngine getInstance(String name) {
		LogEngine inst = buffer.get(name);
		if (inst == null) {
			inst = createInstance(name);
			inst.doInitialize(this);
			buffer.put(name,inst);
		}
		return inst;
    }

/**
 * <p>Construct and return a <code>Log</code> instance,
 * using the factory's current set of configuration attributes.</p>
 *
 * <p><strong>NOTE</strong> - Depending upon the implementation of
 * the <code>LogFactory</code> you are using, the <code>Log</code>
 * instance you are returned may or may not be local to the current
 * application, and may or may not be returned again on a subsequent
 * call with the same name argument.</p>
 *
 * @param name Logical name of the <code>Log</code> instance to be
 *  returned (the meaning of this name is only known to the underlying
 *  logging implementation that is being wrapped)
 * @return a {@link de.mhus.lib.core.logging.LogEngine} object.
 */
    public abstract LogEngine createInstance(String name);

	public LogEngine getLog(Class<?> class1) {
		return getInstance(class1);
	}

	public void setDefaultLevel(LEVEL level) {
		/**
		 * <p>getLog.</p>
		 *
		 * @param class1 a {@link java.lang.Class} object.
		 * @return a {@link de.mhus.lib.core.logging.LogEngine} object.
		 */
		this.level = level;
	}
	
	public LEVEL getDefaultLevel() {
		/**
		 * <p>setDefaultLevel.</p>
		 *
		 * @param level a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
		 */
		return level;
	}

//	public synchronized LevelMapper getLevelMapper(Class<? extends LevelMapper> defaultMapper) {
/**
 * <p>getDefaultLevel.</p>
 *
 * @return a {@link de.mhus.lib.core.logging.Log.LEVEL} object.
 */
//		if (levelMapper == null && defaultMapper != null) {
//			try {
//				levelMapper = defaultMapper.newInstance();
//			} catch (Exception e) {
//			}
//		}
//		if (defaultMapper != null && defaultMapper.isInstance(levelMapper))
//			return levelMapper;
//		else
//			return null;
//	}
	
	public LevelMapper getLevelMapper() {
		return levelMapper;
	}

	public void setLevelMapper(LevelMapper levelMapper) {
		/**
		 * <p>Getter for the field <code>levelMapper</code>.</p>
		 *
		 * @return a {@link de.mhus.lib.core.logging.LevelMapper} object.
		 */
		this.levelMapper = levelMapper;
	}

	public ParameterMapper getParameterMapper() {
		/**
		 * <p>Setter for the field <code>levelMapper</code>.</p>
		 *
		 * @param levelMapper a {@link de.mhus.lib.core.logging.LevelMapper} object.
		 */
		return parameterMapper;
	}

	public void setParameterMapper(ParameterMapper parameterMapper) {
		/**
		 * <p>Getter for the field <code>parameterMapper</code>.</p>
		 *
		 * @return a {@link de.mhus.lib.core.logging.ParameterMapper} object.
		 */
		this.parameterMapper = parameterMapper;
	}
	
//    public void update(Observable o, Object arg) {
/**
 * <p>Setter for the field <code>parameterMapper</code>.</p>
 *
 * @param parameterMapper a {@link de.mhus.lib.core.logging.ParameterMapper} object.
 */
//        setTrace(MSingleton.instance().getConfig().getBoolean(name + ".TRACE" , MSingleton.instance().getConfig().getBoolean("TRACE",false) ));
//    }

}
