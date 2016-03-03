package de.mhus.lib.cao;

import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.core.form.MForm;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsFactory;

/**
 * A action is doing something with one or more objects. The main
 * difference to a operation is that the objects do not have
 * meanings. A action can call a operation to execute.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoAction extends MObject {

	private MNls resourceBundle;

	/**
	 * <p>Constructor for CaoAction.</p>
	 */
	public CaoAction() {
		resourceBundle = new MNls();
	}

	/**
	 * <p>Constructor for CaoAction.</p>
	 *
	 * @param res a {@link de.mhus.lib.core.directory.MResourceProvider} object.
	 * @param resourceName a {@link java.lang.String} object.
	 */
	public CaoAction(MResourceProvider<?> res, String resourceName) {
		resourceBundle = base(MNlsFactory.class).load(res,this.getClass(), resourceName, null);
	}

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getName();

	/**
	 * Returns a configuration Form for the operation. The list of elements
	 * should be a representative list. The configuration use most time the
	 * first element of this list. In other cases the hole list is needed,
	 * it depends on the operation.
	 *
	 * @param list a {@link de.mhus.lib.cao.CaoList} object.
	 * @param initConfig specific initial attributes
	 * @throws de.mhus.lib.cao.CaoException if any.
	 * @return a {@link de.mhus.lib.core.form.MForm} object.
	 */
	public abstract MForm createConfiguration(CaoList list,Object...initConfig) throws CaoException;

	/**
	 * <p>canExecute.</p>
	 *
	 * @param list a {@link de.mhus.lib.cao.CaoList} object.
	 * @param initConfig a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public abstract boolean canExecute(CaoList list, Object...initConfig);

	/**
	 * Executes a defined action. Is the action need to execute an operation it will
	 * return the operation object to be executed by the caller.
	 *
	 * @param list a {@link de.mhus.lib.cao.CaoList} object.
	 * @param configuration a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.cao.CaoException if any.
	 * @return a {@link de.mhus.lib.cao.CaoOperation} object.
	 */
	public abstract CaoOperation execute(CaoList list, Object configuration) throws CaoException;

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Action " + getName() + " (" + getClass().getCanonicalName() + ")";
	}

	/**
	 * <p>Getter for the field <code>resourceBundle</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.util.MNls} object.
	 */
	public MNls getResourceBundle() {
		return resourceBundle;
	}

}
