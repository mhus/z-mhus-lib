package de.mhus.lib.servlet.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoSecurityWatch implements SecurityApi {

	@Override
	public boolean checkHttpRequest(HttpServletRequest req, HttpServletResponse res) {
		return true;
	}

}
