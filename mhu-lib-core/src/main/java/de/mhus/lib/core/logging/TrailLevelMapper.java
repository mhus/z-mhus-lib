package de.mhus.lib.core.logging;

public interface TrailLevelMapper extends LevelMapper {

	public String doSerializeTrail();
	public void doConfigureTrail(String backup);
	public void doResetTrail();
	public boolean isLocalTrail();
	
}
