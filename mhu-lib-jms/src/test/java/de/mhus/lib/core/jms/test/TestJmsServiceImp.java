package de.mhus.lib.core.jms.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TestJmsServiceImp implements TestJmsService {

	public String lastAction = null;
	
	@Override
	public void withoutReturnValue() {
		lastAction = "withoutReturnValue";
	}

	@Override
	public void oneWayWithoutReturn() {
		lastAction = "oneWayWithoutReturn";
	}

	@Override
	public void withParameters(String nr1, long nr2, int nr3) {
		lastAction = "withParameters " + nr1 + nr2 + nr3;
	}

	@Override
	public String withParametersAndReturn(String nr1, long nr2, int nr3) {
		lastAction = "withParametersAndReturn " + nr1 + nr2 + nr3;
		return "R " + nr1;
	}

	@Override
	public Map<String, String> mapSample(Map<String, String> in) {
		lastAction = "mapSample " + in;
		HashMap<String, String> ret = new HashMap<>();
		ret.put("x", "y");
		return ret;
	}

	@Override
	public List<String> listSample(List<String> in) {
		lastAction = "listSample " + in;
		LinkedList<String> ret = new LinkedList<>();
		ret.add("x");
		return ret;
	}

}
