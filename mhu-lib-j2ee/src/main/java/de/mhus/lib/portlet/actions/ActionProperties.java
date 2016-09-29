package de.mhus.lib.portlet.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.portlet.ActionRequest;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>ActionProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ActionProperties extends AbstractProperties {

	ActionRequest request = null;
	
	/**
	 * <p>Constructor for ActionProperties.</p>
	 *
	 * @param request a {@link javax.portlet.ActionRequest} object.
	 */
	public ActionProperties(ActionRequest request) {
		this.request = request;
	}

	/** {@inheritDoc} */
	@Override
	public Object getProperty(String name) {
		return request.getParameter(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isProperty(String name) {
		return request.getParameter(name) != null;
	}

	/** {@inheritDoc} */
	@Override
	public void removeProperty(String key) {
	}

	/** {@inheritDoc} */
	@Override
	public void setProperty(String key, Object value) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEditable() {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Set<String> keys() {
		return new HashSet<String>(Collections.list(request.getParameterNames()));
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		int cnt = 0;
		Enumeration<String> en = request.getParameterNames();
		while (en.hasMoreElements()) {
			en.nextElement();
			cnt++;
		}
		return cnt;
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(Object value) {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<Object> values() {
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new NotSupportedException();
	}

}
