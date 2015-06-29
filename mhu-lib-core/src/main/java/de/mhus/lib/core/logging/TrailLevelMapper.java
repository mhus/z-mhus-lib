package de.mhus.lib.core.logging;

public interface TrailLevelMapper extends LevelMapper {

	String doSerializeTrail();
	void doConfigureTrail(String backup);
	void doResetTrail();
	boolean isLocalTrail();
	String getTrailId();

}
