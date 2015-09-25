package de.mhus.lib.liferay.wizards;

import java.util.List;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.control.WizardCall;
import de.mhus.lib.vaadin.ColumnDefinition;
import de.mhus.lib.vaadin.FilterRequest;
import de.mhus.lib.vaadin.SimpleTable;
import de.mhus.lib.vaadin.form2.AbstractListWizard;

public class OrganizationSelector extends AbstractListWizard {

	@Override
	public ColumnDefinition[] createColumnDefinitions() {
		return new ColumnDefinition[] {
				new ColumnDefinition("name", String.class, "", "Name", true)
				
		};
	}

	@Override
	public void fillTable(WizardCall arg0, SimpleTable table,	FilterRequest filter) {
		
		String namePattern = null;
		long parentOrganization = 0;
		try {
			ResourceNode opts = arg0.getOptions();
			if (opts != null) {
				namePattern = opts.getString("namePattern", null);
				parentOrganization = opts.getLong("parent", 0);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		try {
			DynamicQuery query = DynamicQueryFactoryUtil.forClass(Organization.class); // active ...
			if (filter.isFiltering()) {
				for (String f : filter.getGeneralFilters()) {
					query = query.add(PropertyFactoryUtil.forName("fullName").like("%" + f + "%" ));
				}
			}

			@SuppressWarnings("unchecked")
			List<Organization> res = OrganizationLocalServiceUtil.dynamicQuery(query);
			for (Organization u : res) {
				if (	(namePattern == null || MString.compareRegexPattern(u.getName(), namePattern))
						&&
						(parentOrganization == 0 || u.getParentOrganizationId() == parentOrganization)
				   )
				table.addItem(new Object[] {u.getName()}, u.getUserId());
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean setSelected(WizardCall call, Object selected) {
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
