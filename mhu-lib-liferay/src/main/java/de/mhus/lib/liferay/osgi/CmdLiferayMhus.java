/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.liferay.osgi;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.VirtualHost;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.TriggerState;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.CompanyServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.VirtualHostLocalServiceUtil;
import com.liferay.portal.kernel.transaction.Transactional;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.console.ConsoleTable;

public class CmdLiferayMhus implements CommandProvider {

	@Override
	public String getHelp() {
		return "clc - Clear Liferay Cache\n"
				+ "setpassword - Set New Password <virtual host> <user> <new password> [<need user reset>]";
	}

//	Role role = RoleLocalServiceUtil.getRole(company.getCompanyId(), company.getAdminName());
//	User admin = company.getDefaultUser();
//	PermissionChecker pc = PermissionCheckerFactoryUtil.getPermissionCheckerFactory().create(admin);
//	PermissionThreadLocal.setPermissionChecker(pc);
	
	public Object _clc(CommandInterpreter intp) {
		intp.println("Clear Liferay Caches");
        CacheRegistryUtil.clear();
	    EntityCacheUtil.clearCache();
	    FinderCacheUtil.clearCache();
		return null;
	}
	
	public Object _virtualhosts(CommandInterpreter ci) throws PortalException {
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Id","Hostname","Company Id", "Company Name", "Company Admin");
		for (VirtualHost vh : VirtualHostLocalServiceUtil.getVirtualHosts(0, VirtualHostLocalServiceUtil.getVirtualHostsCount() ) ) {
			long companyId = vh.getCompanyId();
			Company company = CompanyServiceUtil.getCompanyById(companyId);
			out.addRowValues(vh.getVirtualHostId(), vh.getHostname(), companyId, company.getName(), company.getAdminName());
		}
		ci.println(out);
		return null;
		
	}
	
	public Object _users(CommandInterpreter ci) throws Exception {

		String virtualHost = ci.nextArgument();
		Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
		long companyId = company.getCompanyId();
		
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Id","Screen Name","EMail", "Full Name");
		for (User user : UserLocalServiceUtil.getCompanyUsers(companyId, 0, UserLocalServiceUtil.getCompanyUsersCount(companyId))) {
			out.addRowValues(user.getUserId(), user.getScreenName(), user.getEmailAddress(), user.getFullName());
		}
		
		ci.println(out);
		return null;
		
	}
	
	public Object _user(CommandInterpreter ci) throws Exception {

		String virtualHost = ci.nextArgument();
		Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
//		long companyId = company.getCompanyId();
		
		String screenName = ci.nextArgument();
		User u = UserLocalServiceUtil.getUserByScreenName(company.getCompanyId(), screenName);
		ci.println("UserId             : " + u.getUserId());
		ci.println("UserUUID           : " + u.getUserUuid());
		ci.println("ScreenName         : " + u.getScreenName());
		ci.println("EMail..............: " + u.getEmailAddress());
		ci.println("Display EMail      : " + u.getDisplayEmailAddress());
		ci.println("Original EMail     : " + u.getOriginalEmailAddress());
		ci.println("Greeting           : " + u.getGreeting());
		ci.println("Initials...........: " + u.getInitials());
		ci.println("FirstName          : " + u.getFirstName());
		ci.println("MiddleName         : " + u.getMiddleName());
		ci.println("LastName           : " + u.getLastName());
		ci.println("FullName...........: " + u.getFullName());
		ci.println("Comments           : " + u.getComments());
		ci.println("LastLoginDate      : " + u.getLastLoginDate());
		ci.println("LastLoginIP        : " + u.getLastLoginIP());
		ci.println("LastFailedLogin....: " + u.getLastFailedLoginDate());
		ci.println("FailedLoginAttempts: " + u.getFailedLoginAttempts());
		ci.println("IsActive           : " + u.isActive());
		ci.println("IsAgreedTermsOfUse : " + u.isAgreedToTermsOfUse());
		ci.println("IsMale.............: " + u.isMale());
		ci.println("IsFemale           : " + u.isFemale());
		ci.println("IsPasswordReset    : " + u.isPasswordReset());
		ci.println("ReminderQuestion   : " + u.getReminderQueryQuestion());
		ci.println("ReminderAnswer.....: " + u.getReminderQueryAnswer());
		ci.println("JobTitle           : " + u.getJobTitle() );
		
		ci.println();
		
		for (Group s : GroupLocalServiceUtil.getUserSitesGroups(u.getUserId())) {
			ci.println("Site  : " + s.getGroupId() + " " + s.getFriendlyURL() + " " + s.getName());
		}
		for (Group s : GroupLocalServiceUtil.getUserGroups(u.getUserId())) {
			ci.println("Group : " + s.getGroupId() + " " + s.getFriendlyURL() + " " + s.getName());
		}
		
		
		return null;

	}
	
	public Object _groups(CommandInterpreter ci) {
		
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Id","Company","Active","Site","URL");
		for (Group s : GroupLocalServiceUtil.getGroups(0,GroupLocalServiceUtil.getGroupsCount())) {
			if (!s.isUser())
				out.addRowValues(s.getGroupId(), s.getCompanyId(), s.getActive(), s.isSite(), s.getFriendlyURL());
		}
		ci.print(out);
		return null;
	}
	
	public Object _setpassword(CommandInterpreter ci) throws PortalException {
		try {
			String virtualHost = ci.nextArgument();
			String screenName = ci.nextArgument();
			String pw = ci.nextArgument();
			String reset = ci.nextArgument();
			
			Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
			User user = UserLocalServiceUtil.getUserByScreenName(company.getCompanyId(), screenName);
			UserLocalServiceUtil.updatePassword(user.getUserId(), pw, pw, MCast.toboolean(reset, false), true);
			return null;
		} catch (Throwable t) {
			ci.printStackTrace(t);
			throw t;
		}
	}
	
	public Object _roles(CommandInterpreter ci) throws PortalException {
		String virtualHost = ci.nextArgument();
		Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
		
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Id","Name","Type","Title", "User Id", "User Name");
		for (Role role : RoleLocalServiceUtil.getRoles(company.getCompanyId())) {
			out.addRowValues(role.getRoleId(), role.getName(), role.getTypeLabel(),role.getTitle(), role.getUserId(), role.getUserName());
		}
		ci.println(out);
		return null;
	}
	
	@Transactional
	public Object _role_delete(CommandInterpreter ci) throws PortalException {
		long id = MCast.tolong(ci.nextArgument(), -1);
		if (id < 0) return null;
		Role role = RoleLocalServiceUtil.deleteRole(id);
		ci.println("Deleted Role " + role.getRoleId() + " " + role.getName());
		return role;
	}
	
	public Object _companies(CommandInterpreter ci) throws PortalException {
		
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Id","Active","Name","AccoutnId","DefaultUser","DefaultWebId","GroupId", "Mx");
		for ( Company c : CompanyLocalServiceUtil.getCompanies()) {
			out.addRowValues(c.getCompanyId(),c.getActive(),c.getName(),c.getAccountId(),c.getDefaultUser().getUserId(),c.getDefaultWebId(),c.getGroupId(),c.getMx());
		}
		ci.println(out);
		return null;
	}

	public Object _company(CommandInterpreter ci) throws PortalException {
		long id = MCast.tolong(ci.nextArgument(), -1);
		Company c = CompanyLocalServiceUtil.getCompany(id);
		ci.println(c.toXmlString());
		return null;
	}
	
	@Transactional
	public Object _user_delete(CommandInterpreter ci) throws PortalException {
		long id = MCast.tolong(ci.nextArgument(), -1);
		if (id < 0) return null;
		User user = UserLocalServiceUtil.deleteUser(id);
		ci.println("Deleted User " + user.getUserId() + " " + user.getScreenName());
		return user;
	}
	
	public Object _user_add(CommandInterpreter ci) throws PortalException {
		try {
			String virtualHost = ci.nextArgument();
			Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
			String screenName = ci.nextArgument();
			String email = ci.nextArgument();
			String firstName = ci.nextArgument();
			String lastName = ci.nextArgument();
	
			
			User user = UserLocalServiceUtil.addUser(
					0,
					company.getCompanyId(),
					false,
					"asd", "asd", 
					false,
					screenName, 
					email,
					0,
					null,
					Locale.GERMAN, 
					firstName,
					"",
					lastName,
					0,
					0,
					true,
					1, 1, 1970, 
					"", 
					new long[0], 
					new long[0], 
					new long[0], 
					new long[0], 
					false, 
					null
					);
			
			return user;
		} catch (Throwable t) {
			ci.printStackTrace(t);
			throw t;
		}
	}
	
	@Transactional
	public Object _user_set(CommandInterpreter ci) throws PortalException {
		try {
			String virtualHost = ci.nextArgument();
			Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
			String screenName = ci.nextArgument();
			
			User u = UserLocalServiceUtil.getUserByScreenName(company.getCompanyId(), screenName);
			
			while (true) {
				String pair = ci.nextArgument();
				if (pair == null) break;
				
				if (MString.isIndex(pair, '=')) {
					String key = MString.beforeIndex(pair, '=');
					String val = MString.afterIndex(pair, '=');
					key = key.toLowerCase().trim();
					switch (key) {
					case "screenname":
						u.setScreenName(val);
						break;
					case "email":
						u.setEmailAddress(val);
						break;
					case "greeting":
						u.setGreeting(val);
						break;
					case "firstname":
						u.setFirstName(val);
						break;
					case "middlename":
						u.setMiddleName(val);
						break;
					case "lastname":
						u.setLastName(val);
						break;
					case "comments":
						u.setComments(val);
						break;
					case "lastlogindate":
						u.setLastLoginDate(MCast.toDate(val, null));
						break;
					case "lastloginip":
						u.setLastLoginIP(val);
						break;
					case "isagreedtermsofuse":
						u.setAgreedToTermsOfUse(MCast.toboolean(val, false));
						break;
					case "ispasswordreset":
						u.setPasswordReset(MCast.toboolean(val, false));
						break;
					case "jobtitle":
						u.setJobTitle(val);
						break;
					default:
						ci.println("Unknown parameter: " + key);
					}
					
				}
				
				
			}
			
			UserLocalServiceUtil.updateUser(u);
			ci.println("UPDATED " + u);
			
			return u;

		} catch (Throwable t) {
			ci.printStackTrace(t);
			throw t;
		}
	}

	@Transactional
	public Object _role_add(CommandInterpreter ci) throws PortalException {
		String virtualHost = ci.nextArgument();
		Company company = CompanyServiceUtil.getCompanyByVirtualHost(virtualHost);
		String userName = ci.nextArgument();
		
		User user = UserLocalServiceUtil.getUserByScreenName(company.getCompanyId(), userName);
		
		String name = ci.nextArgument();
		Role role = RoleLocalServiceUtil.addRole(user.getUserId(), null, 0, name, null, null, 0, null, null);
		return role;
	}
	
	public Object _db_commit(CommandInterpreter ci) throws SQLException {
		Connection con = DataAccess.getConnection();
		con.commit();
		return null;
	}
	
	public Object _db_rollback(CommandInterpreter ci) throws SQLException {
		Connection con = DataAccess.getConnection();
		con.rollback();
		return null;
	}
	
	public Object _db_tables(CommandInterpreter ci) throws SQLException {
		Connection con = DataAccess.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getTables(null, null, null, new String[] {"TABLE"});
		ConsoleTable out = ConsoleTable.fromJdbcResult(res);
		ci.println(out);
		return null;
	}
	
	public Object _db_table(CommandInterpreter ci) throws SQLException {
		String name = ci.nextArgument();

		Connection con = DataAccess.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, name, "%");
		ConsoleTable out = ConsoleTable.fromJdbcResult(res);
		ci.println(out);
		return null;
	}
	
	public Object _db_indexes(CommandInterpreter ci) throws SQLException {
		String idxName = ci.nextArgument();
		Connection con = DataAccess.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet tres = meta.getTables(null, null, null, new String[] {"TABLE"});
		ConsoleTable out = null;
		while (tres.next()) {
			ResultSet res = meta.getIndexInfo(con.getCatalog(), null, tres.getString("TABLE_NAME"), true, false);
			ResultSetMetaData resMeta = res.getMetaData();
			if (out == null) {
				out = new ConsoleTable();
				String[] h = new String[resMeta.getColumnCount()];
				for (int i = 0; i < resMeta.getColumnCount(); i++)
					h[i] = resMeta.getColumnName(i+1);
				out.setHeaderValues(h);
			}
			while (res.next()) {
				if (idxName == null || idxName.equals(res.getString("INDEX_NAME"))) {
					ConsoleTable.Row r = out.addRow();
					for (int i = 0; i < resMeta.getColumnCount(); i++)
							r.add(String.valueOf(res.getObject(i+1)));
				}
			}
		}
		ci.println(out);
		return null;
		
	}
	
	public Object _db_select(CommandInterpreter ci) throws SQLException {
		String sql = ci.nextArgument();
		Connection con = DataAccess.getConnection();
		Statement sth = con.createStatement();
		ResultSet res = sth.executeQuery(sql);
		ConsoleTable out = ConsoleTable.fromJdbcResult(res);
		sth.close();
		ci.println(out);
		return null;
	}

	public Object _db_update(CommandInterpreter ci) throws SQLException {
		String sql = ci.nextArgument();
		Connection con = DataAccess.getConnection();
		Statement sth = con.createStatement();
		int res = sth.executeUpdate(sql);
		ci.println("Result: " + res);
		return null;
	}
	
	public Object _jobs(CommandInterpreter ci) throws SchedulerException {
		ConsoleTable out = new ConsoleTable();
		out.setHeaderValues("Name", "Group", "Storage Type", "Next", "State");
		for (  SchedulerResponse job : SchedulerEngineHelperUtil.getScheduledJobs()) {
			Date next = SchedulerEngineHelperUtil.getNextFireTime(job.getJobName(), job.getGroupName(),job.getStorageType());
			TriggerState state = SchedulerEngineHelperUtil.getJobState(job.getJobName(), job.getGroupName(), job.getStorageType());
			out.addRowValues(job.getJobName(), job.getGroupName(), job.getStorageType(), next, state );
		}
		ci.println(out);
		return null;
	}
	
	public Object _config_info(CommandInterpreter ci) throws SchedulerException {
		ci.println(new File(".").getAbsolutePath() );
		ci.println( MApi.get().getCfgManager() );
		return null;
	}
	
	public Object _dirty_trace(CommandInterpreter ci) throws SchedulerException {

		MApi.setDirtyTrace( MCast.toboolean(ci.nextArgument(), false) );
		ci.println("DirtyTrace: " + MApi.isDirtyTrace());
		return null;
	}
	
	public Object _config_reset(CommandInterpreter ci) throws SchedulerException {
		MApi.get().getCfgManager().reConfigure();
		ci.println("OK");
		return null;
	}
	
//	public Object _log_(CommandInterpreter ci) throws SchedulerException {
//		
//		return null;
//	}
	
}
