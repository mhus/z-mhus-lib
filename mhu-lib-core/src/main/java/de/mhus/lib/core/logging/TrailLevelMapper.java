package de.mhus.lib.core.logging;

public interface TrailLevelMapper extends LevelMapper {

	String doSerializeTrail();
	void doConfigureTrail(String backup);
	void doResetTrail(); //TODO change to doReleaseTrail
	boolean isLocalTrail();
	String getTrailId();
	void doResetAllTrails();

}
