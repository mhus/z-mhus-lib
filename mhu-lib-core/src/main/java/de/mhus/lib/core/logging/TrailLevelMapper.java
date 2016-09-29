package de.mhus.lib.core.logging;

/**
 * <p>TrailLevelMapper interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface TrailLevelMapper extends LevelMapper {

	/**
	 * <p>doSerializeTrail.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String doSerializeTrail();
	/**
	 * <p>doConfigureTrail.</p>
	 *
	 * @param backup a {@link java.lang.String} object.
	 */
	void doConfigureTrail(String backup);
	/**
	 * <p>doResetTrail.</p>
	 */
	void doResetTrail(); //TODO change to doReleaseTrail
	/**
	 * <p>isLocalTrail.</p>
	 *
	 * @return a boolean.
	 */
	boolean isLocalTrail();
	/**
	 * <p>getTrailId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String getTrailId();

}
