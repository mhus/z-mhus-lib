package de.mhus.lib.liferay.wizzards;

import java.util.Arrays;
import java.util.List;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.control.WizzardCall;
import de.mhus.lib.liferay.MLiferayUtil;
import de.mhus.lib.vaadin.ColumnDefinition;
import de.mhus.lib.vaadin.FilterRequest;
import de.mhus.lib.vaadin.SimpleTable;
import de.mhus.lib.vaadin.form2.AbstractListWizzard;

public class UserSelector extends AbstractListWizzard {

	@Override
	public ColumnDefinition[] createColumnDefinitions() {
		return new ColumnDefinition[] {
				new ColumnDefinition("name", String.class, "", "Name", true)
				,
				new ColumnDefinition("mail", String.class, "", "EMail", true)
				
		};
	}

	@Override
	public void fillTable(WizzardCall arg0, SimpleTable table,	FilterRequest filter) {
		
		List<String> roles = null;
		String namePattern = null;
		String emailPattern = null;
		try {
			ResourceNode opts = arg0.getOptions();
			if (opts != null) {
				String rolesN = opts.getString("roles", null);
				if (rolesN != null) {
					roles = Arrays.asList(rolesN.split(",") );
				}
				namePattern = opts.getString("namePattern", null);
				emailPattern = opts.getString("emailPattern", null);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		try {
			DynamicQuery query = DynamicQueryFactoryUtil.forClass(User.class); // active ...
			if (filter.isFiltering()) {
				for (String f : filter.getGeneralFilters()) {
					query = query.add(PropertyFactoryUtil.forName("fullName").like("%" + f + "%" ));
				}
			}

			@SuppressWarnings("unchecked")
			List<User> res = UserLocalServiceUtil.dynamicQuery(query);
			for (User u : res) {
				if ( (roles == null || MLiferayUtil.hasRole(u, roles))
					&&
					(namePattern == null || MString.compareRegexPattern(u.getFullName(), namePattern))
					&&
					(emailPattern == null || MString.compareRegexPattern(u.getEmailAddress(), emailPattern))
					)
					table.addItem(new Object[] {u.getFullName(), u.getEmailAddress()}, u.getUserId());
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean setSelected(WizzardCall call, Object selected) {
		if (selected == null) return false;
		try {
			call.setString("(" + String.valueOf(selected) + ")");
		} catch (MException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
