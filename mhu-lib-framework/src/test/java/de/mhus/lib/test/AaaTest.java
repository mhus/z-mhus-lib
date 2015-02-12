package de.mhus.lib.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import de.mhus.lib.adb.DbAccessManager;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbSchema;
import de.mhus.lib.adb.Persistable;
import de.mhus.lib.adb.model.Table;
import de.mhus.lib.core.config.NodeConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.persistence.aaa.AaaUtil;
import de.mhus.lib.persistence.aaa.Acl;
import de.mhus.lib.persistence.aaa.AclAccessManager;
import de.mhus.lib.persistence.aaa.AclToSubject;
import de.mhus.lib.persistence.aaa.ISubjectCheck;
import de.mhus.lib.persistence.aaa.Subject;
import de.mhus.lib.persistence.aaa.Subject.TYPE;
import de.mhus.lib.persistence.aaa.SubjectToSubject;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbPoolBundle;

public class AaaTest {

	private Subject userOther;
	private Subject userAdmin;
	private Subject groupAdmins;
	private Subject groupAll;
	private Acl aclWorld;
	private Acl aclAdmins;
	private TestMe test1;
	private TestMe test2;

	/*
	 * Users:
	 * 
	 * 'normal' is in group 'world'
	 * 'admin; is in group 'world' and 'admins'
	 * 
	 * ACLs:
	 * 
	 * 'world', group 'world' can read, default is read
	 * 'admins only', group 'admins' can read, default is nothing
	 * 
	 */
	@Test
	public void testRead() throws Exception {
		DbPool pool = createPool().getPool("test");
		Schema schema = new Schema();
		DbManager manager = new DbManager(pool, schema);
		prepare(manager);
		
		// check access
		schema.currentSubject = userOther;
		
		int cnt = 0;
		for (TestMe finding : manager.getByQualification(new TestMe(), "", null).toCacheAndClose()) {
			System.out.println(schema.currentSubject + ": " + finding);
			cnt++;
		}
		assertEquals(1, cnt);
		
		// check access
		schema.currentSubject = userAdmin;
		
		cnt = 0;
		for (TestMe finding : manager.getByQualification(new TestMe(), "", null).toCacheAndClose()) {
			System.out.println(schema.currentSubject + ": " + finding);
			cnt++;
		}
		assertEquals(2, cnt);
		
		// ---
		
		// set acls to subjects and acls ...
		
		schema.currentSubject = null;
		
		userOther.setAcl(aclAdmins.getId());
		userOther.save();
		
		userOther.setAcl(aclAdmins.getId());
		userOther.save();
		
		groupAdmins.setAcl(aclAdmins.getId());
		groupAdmins.save();
		
		groupAll.setAcl(aclAdmins.getId());
		groupAll.save();
		
		aclWorld.setAcl(aclAdmins.getId());
		aclWorld.save();
		
		aclAdmins.setAcl(aclAdmins.getId());
		aclAdmins.save();
		
		// check access to acls
		schema.currentSubject = userOther;

		try {
			userOther.setAcl(aclWorld.getId());
			userOther.save();
			fail("no access denied exception thrown");
		} catch (MException e) {
			System.out.println(e);
		}
		
		try {
			
			aclWorld.getRules().getRelations().add(new AclToSubject(AaaUtil.createPolicy(Acl.RIGHT_READ),groupAll));
			aclWorld.save();

			fail("no access denied exception thrown");
		} catch (MException e) {
			System.out.println(e);
		}
		aclWorld.reload();
		
		schema.currentSubject = userAdmin;

		try {
			userOther.setAcl(aclWorld.getId());
			userOther.save();
			for ( AclToSubject x : aclWorld.getRules().getRelations()) {
				if (x.getSubjectId().equals(groupAll.getId()))
					aclWorld.getRules().getRelations().remove(x);
			}
			aclWorld.getRules().getRelations().add(new AclToSubject(AaaUtil.createPolicy(Acl.RIGHT_READ,Acl.RIGHT_CREATE),groupAll));
			aclWorld.save();
			
		} catch (MException e) {
			System.out.println(e);
			fail("access denied exception thrown");
		}
		
		
		// test a simple entity
		
		schema.currentSubject = userOther;

		TestSimpleEntity simple = manager.injectObject(new TestSimpleEntity());
		simple.save();
		UUID simpleId = simple.getId();
		simple = manager.getObject(TestSimpleEntity.class, simpleId);
		simple.save();
		simple.reload();
		simple.delete();
		
		// test parent child relation
		
		schema.currentSubject = null;
		
		TestParent parentM = manager.injectObject(new TestParent());
		parentM.setAcl(aclWorld.getId());
		parentM.setChildAcl(aclAdmins.getId());
		parentM.setName("Mama");
		parentM.save();
		
		TestParent parentP = manager.injectObject(new TestParent());
		parentP.setAcl(aclWorld.getId());
		parentP.setChildAcl(aclAdmins.getId());
		parentP.setName("Papa");
		parentP.save();
		
		schema.currentSubject = userOther;
		
		TestChild child = manager.injectObject(new TestChild());
		child.setName("Fred");
		try {
			child.save();
			fail("no access denied exception without parent");
		} catch (MException e) {
			System.out.println(e);
//			e.printStackTrace();
		}

//		child.setParentId(parentM.getId());
//		try {
////TODO			child.save();
//		} catch (Exception e) {
//			e.printStackTrace();
////TODO			fail("access denied exception with parent");
//		}
//		
////TODO		assertEquals(aclAdmins.getId(), child.getAcl());
//		
//		// move to papa - before manipulate acl of the child
//		schema.currentSubject = null;
//		child.setAcl(aclWorld.getId());
//		child.save();
//		schema.currentSubject = userOther;
//
//		child.setParentId(parentP.getId());
//		try {
//			child.save();
////TODO			fail("no access denied exception on move");
//		} catch (MException e) {
//			System.out.println(e);
////			e.printStackTrace();
//		}
//		child.reload();
//		
//		// and correct
//		schema.currentSubject = null;
//		child.setAcl(aclAdmins.getId());
//		child.save();
//		schema.currentSubject = userAdmin;
//
//		child.setParentId(parentP.getId());
//		try {
//			child.save();
//		} catch (MException e) {
//			e.printStackTrace();
//			fail("access denied exception on move");
//		}
//		
		
		
	}

    private void prepare(DbManager manager) throws Exception {
		userOther = manager.injectObject(new Subject(TYPE.USER,"normal"));
		userOther.setDisplayName("Ursula");
		userOther.save();
		
		userAdmin = manager.injectObject(new Subject(TYPE.USER,"admin"));
		userAdmin.setDisplayName("Anton");
		userAdmin.save();
		
		groupAdmins = manager.injectObject(new Subject(TYPE.GROUP,"admins"));
		groupAdmins.setDisplayName("Admins");
		groupAdmins.save();

		groupAll = manager.injectObject(new Subject(TYPE.GROUP,"everyone"));
		groupAll.setDisplayName("Everyone");
		groupAll.save();
		
		groupAdmins.getChildren().getRelations().add(new SubjectToSubject(userAdmin));
		groupAdmins.save();

		groupAll.getChildren().getRelations().add(new SubjectToSubject(userOther));
		groupAll.getChildren().getRelations().add(new SubjectToSubject(userAdmin));
		groupAll.save();
		
		
		aclWorld = manager.injectObject(new Acl());
		aclWorld.setName("world");
		aclWorld.setDefaultPolicy(AaaUtil.createPolicy(Acl.RIGHT_READ));
//		aclWorld.setDefaultPolicy(AaaUtil.createPolicy());
		aclWorld.save();
		
		aclWorld.getRules().getRelations().add(new AclToSubject(AaaUtil.createPolicy(Acl.RIGHT_READ,Acl.RIGHT_CREATE),groupAll));
		aclWorld.save();
		
		aclAdmins = manager.injectObject(new Acl());
		aclAdmins.setName("admins only");
		aclAdmins.setDefaultPolicy(AaaUtil.createPolicy());
		aclAdmins.save();
		
		aclAdmins.getRules().getRelations().add(new AclToSubject(AaaUtil.createPolicy(Acl.RIGHT_ALL),groupAdmins));
		aclAdmins.save();
		
		test1 = manager.injectObject(new TestMe());
		test1.setName("admin Test");
		test1.setAcl(aclAdmins.getId());
		test1.save();
		
		test2 = manager.injectObject(new TestMe());
		test2.setName("world Test");
		test2.setAcl(aclWorld.getId());
		test2.save();
				
	}

	public DbPoolBundle createPool() {
    	NodeConfig cdb = new NodeConfig();
    	NodeConfig cconfig = new NodeConfig();
    	NodeConfig ccon = new NodeConfig();
    	
//    	ccon.setProperty("driver", "com.mysql.jdbc.Driver");
//    	ccon.setProperty("url", "jdbc:mysql://localhost:3306/test");
//    	ccon.setProperty("user", "test");
//    	ccon.setProperty("pass", "test");

    	ccon.setProperty("driver", "org.hsqldb.jdbcDriver");
    	ccon.setProperty("url", "jdbc:hsqldb:mem:aname");
    	ccon.setProperty("user", "sa");
    	ccon.setProperty("pass", "");
    	
    	
    	cdb.setConfig("connection", ccon);
    	
//    	NodeConfig cqueries = new NodeConfig();
//    	cqueries.setProperty("create", "create table test (a_text varchar(100))");
//    	cqueries.setProperty("select", "select * from test");
//    	cqueries.setProperty("cleanup", "delete from test");
//    	cqueries.setProperty("insert", "insert into test (a_text) values ($text,text$)");
//    	cqueries.setProperty("dropblub", "drop table if exists blub");
//
//    	cdb.setConfig("queries", cqueries);
    	
    	cconfig.setConfig("test", cdb);
    	
    	DbPoolBundle pool = new DbPoolBundle(cconfig,null);
    	
    	return pool;
    }

    public static class Schema extends DbSchema implements ISubjectCheck {

    	private Subject currentSubject;
    	
		@Override
		public void findObjectTypes(List<Class<? extends Persistable>> list) {
			AaaUtil.findObjectTypes(list);
			list.add(TestMe.class);
			list.add(TestSimpleEntity.class);
			list.add(TestParent.class);
			list.add(TestChild.class);
		}
		
		@Override
		public DbAccessManager getAccessManager(Table c) {
			return new AclAccessManager(this);
		}

		@Override
		public boolean hasRight(DbManager manager, DbConnection con, Acl acl,
				String right) {
			
			// find rule for current subject
			try {
				String policy = AaaUtil.findPolicyForSubject(manager,currentSubject,acl);
				return AaaUtil.hasRight(right,policy);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
		public boolean isAdministrator(DbManager manager, DbConnection con) {
			return currentSubject == null;
		}

		@Override
		public String getCurrentUserInfo() {
			return currentSubject == null ? "-- super --" : currentSubject.toString();
		}

    }
    
}
