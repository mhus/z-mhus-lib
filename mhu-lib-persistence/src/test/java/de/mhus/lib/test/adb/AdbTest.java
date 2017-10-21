package de.mhus.lib.test.adb;

import java.util.List;
import java.util.UUID;

import de.mhus.lib.adb.DbCollection;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.DbManagerJdbc;
import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MStopWatch;
import de.mhus.lib.core.config.NodeConfig;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.util.lambda.LambdaUtil;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbPoolBundle;
import de.mhus.lib.test.adb.model.Book;
import de.mhus.lib.test.adb.model.BookStoreSchema;
import de.mhus.lib.test.adb.model.Finances;
import de.mhus.lib.test.adb.model.Person;
import de.mhus.lib.test.adb.model.Person2;
import de.mhus.lib.test.adb.model.Regal;
import de.mhus.lib.test.adb.model.Store;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AdbTest extends TestCase {

	public AdbTest(String name) {
		super(name);
		try {
			MApi.get().getLogFactory().setDefaultLevel(LEVEL.DEBUG);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		//    	new StaticBase().log().setTrace(true);
		return new TestSuite( AdbTest.class );
	}

	public DbPoolBundle createPool(String name) {
		NodeConfig cdb = new NodeConfig();
		NodeConfig cconfig = new NodeConfig();

		//    	ccon.setProperty("driver", "com.mysql.jdbc.Driver");
		//    	ccon.setProperty("url", "jdbc:mysql://localhost:3306/test");
		//    	ccon.setProperty("user", "test");
		//    	ccon.setProperty("pass", "test");

		cdb.setProperty("driver", "org.hsqldb.jdbcDriver");
		cdb.setProperty("url", "jdbc:hsqldb:mem:" + name);
		cdb.setProperty("user", "sa");
		cdb.setProperty("pass", "");


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


	public void testModel() throws Throwable {

		DbPool pool = createPool("testModel").getPool("test");

		BookStoreSchema schema = new BookStoreSchema();

		MStopWatch timer = new MStopWatch();
		timer.start();

//		MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
		DbManager manager = new DbManagerJdbc(pool, schema);
//		MApi.get().getLogFactory().setDefaultLevel(LEVEL.INFO);

		// create persons
		Person p = new Person();
		p.setName("Klaus Mustermann");
		manager.createObject(p);
		UUID p1 = p.getId();

		p.setId(null);
		p.setName("Alex Admin");
		manager.createObject(p);
		UUID p2 = p.getId();

		{
			List<Person> list = manager.getByQualification(Db.query(Person.class).asc("name") ).toCacheAndClose();
			assertEquals(3, list.size());
			assertEquals("Alex Admin", list.get(0).getName());
			assertEquals("Hausmeister Krause", list.get(1).getName());
			assertEquals("Klaus Mustermann", list.get(2).getName());
		}
		// create books

		Book b = new Book();
		b.setName("Yust Another Boring Book");
		manager.createObject(b);
		UUID b1 = b.getId();

		b.setId(null);
		b.setName("Mystic Almanach");
		manager.createObject(b);
		UUID b2 = b.getId();

		// get a book and modify

		b = manager.getObject(Book.class, b1);
		b.setLendToId(p1);
		manager.saveObject(b);

		b.setLendToId(null);
		manager.saveObject(b);

		b.setLendToId(p2);
		b.setAuthorId(new UUID[] {p1});
		manager.saveObject(b);

		Book copy = manager.getObject(Book.class, b.getId());
		assertEquals(copy.getAuthorId()[0],p1) ;


		// test relations
		assertNotNull(b.getLendTo());
		Person rel = b.getLendTo().getRelation();
		assertNotNull(rel);
		assertEquals(rel.getId(), p2);

		assertNotNull(rel.getLendTo());
		List<Book> retRel = rel.getLendTo().getRelations();
		assertNotNull(retRel);
		assertEquals(1,retRel.size());
		assertEquals(b.getId(), retRel.get(0).getId());

		retRel.add(manager.getObject(Book.class, b2));

		manager.saveObject(rel);

		rel.getLendTo().reset();
		retRel = rel.getLendTo().getRelations();
		assertNotNull(retRel);
		assertEquals(2,retRel.size());

		b = manager.getObject(Book.class, b1);
		assertNotNull(b.getLendToId());
		b.getLendTo().setRelation(null);
		manager.saveObject(b);
		assertNull(b.getLendToId());
		b.getLendTo().setRelation(p);
		manager.saveObject(b);
		assertNotNull(b.getLendToId());

		// test getFields ...
		List<String> names = manager.getAttributedByQualification("name", Db.query(Book.class));
		System.out.println(names);
		assertEquals(2, names.size());
		
		// remove book

		manager.deleteObject(b);

		b = manager.getObject(Book.class, b1);
		assertNull(b);

		// test selection

		System.out.println("----------------------");
		DbCollection<Person> col = manager.executeQuery(new Person(), "select * from $db.person$",  null);
		int count = 0;
		for (Person pp : col) {
			System.out.println("--- " + pp.getId() + " " + pp.getName());
			count++;
		}
		assertEquals(count, 3);

		System.out.println("----------------------");

		col = manager.getByQualification(new Person(), null,  null);
		count = 0;
		for (Person pp : col) {
			System.out.println("--- " + pp.getId() + " " + pp.getName());
			count++;
		}
		assertEquals(count, 3);

		System.out.println("----------------------");

		col = manager.getByQualification(new Person(), "$db.person.name$ like 'Klaus%'",  null);
		count = 0;
		for (Person pp : col) {
			System.out.println("--- " + pp.getId() + " " + pp.getName());
			count++;
		}
		assertEquals(count, 1);

		System.out.println("----------------------");

		//    	col = manager.getByQualification(new Person(), Db.query( Db.like(Db.db(Person.class, "name"), Db.value("'Klaus%'")) ) );
		col = manager.getByQualification(Db.query(Person.class).like(Db.attr("name"), Db.fix("'Klaus%'")) );
		count = 0;
		for (Person pp : col) {
			System.out.println("--- " + pp.getId() + " " + pp.getName());
			count++;
		}
		assertEquals(count, 1);

		System.out.println("----------------------");

		// test a native sql execute - remove all persons

		DbConnection con = manager.getPool().getConnection();
		con.createStatement("DELETE FROM $db.person$", null ).execute(manager.getNameMapping());
		con.commit();

		System.out.println("----------------------");
		col = manager.executeQuery(new Person(), "select * from $db.person$",  null);
		count = 0;
		for (Person pp : col) {
			System.out.println("--- " + pp.getId() + " " + pp.getName());
			count++;
		}
		assertEquals(count, 0);

		// -------------
		// test comfortable object

		Store s1 = new Store();
		s1.setName("Creasy Bookstore");
		s1.setAddress("The Oaks\nDublin");
		s1.create(manager);
		s1.setSqlDate(new java.sql.Date(0));
		s1.setAddress("The Lakes\nDublin");
		s1.save();

		// test change in another session and reload
		Store s2 = manager.getObject(Store.class, s1.getId());
		s2.setAddress("");
		s2.save();

		s1.reload();
		assertEquals(s1.getAddress(), s2.getAddress());
		assertEquals(s1.getSqlDate().toString(), new java.sql.Date(0).toString());

		// remove and check behavior of updates
		s1.delete();

		try {
			s2.reload();
			assertTrue(false);
		} catch (MException e) {
			System.out.println(e);
		}

		try {
			s2.save();
			assertTrue(false);
		} catch (MException e) {
			System.out.println(e);
		}

		// -------------
		// test access control

		Finances f = new Finances();
		f.setActiva(10);
		f.setPassiva(10);
		f.setStore(s1.getId());
		f.create(manager);

		f.setActiva(20);
		f.save();

		f.setNewConfidential("write");
		f.save();

		try {
			f.save();
			assertTrue(false);
		} catch (MException e) {
			System.out.println(e);
		}

		f.reload();

		try {
			f.save();
			assertTrue(false);
		} catch (MException e) {
			System.out.println(e);
		}

		f.setConfidential("read"); // hack :)
		f.setNewConfidential("read");
		f.save();

		try {
			f.reload();
			assertTrue(false);
		} catch (MException e) {
			System.out.println(e);
		}

		f.setConfidential("read"); // hack :)
		f.setNewConfidential("remove");
		f.save();
		f.reload();

		try {
			f.delete();
			assertTrue(false);
		} catch (MException e) {
			System.out.println(e);
		}

		f.setConfidential("read"); // hack :)
		f.setNewConfidential("");
		f.save();
		f.reload();
		f.delete();

		// -------------
		// test dynamic objects
		{
			Regal r = new Regal();
			r.setValue("store", s1.getId());
			r.setValue("name", "regal 1");
			r.create(manager);
	
			r.setValue("name", "regal 22113221");
			r.save();
	
			Regal r2 = manager.getObject(Regal.class, r.getValue("id"));
			assertNotNull(r2);
	
			r2.reload();
	
			r2.delete();
	
		}
		
		timer.stop();
		System.out.println("Time: " + timer.getCurrentTimeAsString());
	}
	
	public void testDbQuery() throws Exception {
		
		DbPool pool = createPool("testModel2").getPool("test");

		BookStoreSchema schema = new BookStoreSchema();

		MStopWatch timer = new MStopWatch();
		timer.start();

		DbManager manager = new DbManagerJdbc(pool, schema);
		
		Store store1 = manager.inject(new Store());
		store1.save();
		
		{
			List<Store> res = manager.getByQualification(Db.query(Store.class).isNull("name")).toCacheAndClose();
			assertEquals(1, res.size());
		}
		{
			List<Store> res = manager.getByQualification(Db.query(Store.class).isNotNull("name")).toCacheAndClose();
			assertEquals(0, res.size());
		}
		
		// Test of sub queries - first create a scenario
		
		Person2 p1 = manager.inject(new Person2());
		p1.setName("Max");
		p1.save();
		
		Person2 p2 = manager.inject(new Person2());
		p2.setName("Moritz");
		p2.save();
		
		Person2 p3 = manager.inject(new Person2());
		p3.setName("Witwe Bolte");
		p3.save();
		
		store1.setName("NYC");
		store1.setPrincipal(p1.getId());
		store1.save();
		
		Store store2 = manager.inject(new Store());
		store2.setName("LA");
		store2.setPrincipal(p2.getId());
		store2.save();
		
		Store store3 = manager.inject(new Store());
		store3.setName("LA West");
		store3.setPrincipal(p3.getId());
		store3.save();

		{
			AQuery<Person2> q1 = Db.query(Person2.class).eq("name", "Max");
			List<Person2> res = manager.getByQualification(q1).toCacheAndClose();
			assertEquals(1, res.size());
			assertEquals("Max",res.get(0).getName());
		}

		{
			LambdaUtil.debugOut = true;
			AQuery<Person2> q1 = Db.query(Person2.class).eq(Person2::getName, "Max");
			List<Person2> res = manager.getByQualification(q1).toCacheAndClose();
			assertEquals(1, res.size());
			assertEquals("Max",res.get(0).getName());
		}
		
		{
			AQuery<Person2> q1 = Db.query(Person2.class).eq("name", "Max");
			AQuery<Store> q2 = Db.query(Store.class).in("principal", "id", q1);
			List<Store> res = manager.getByQualification(q2).toCacheAndClose();
			assertEquals(1, res.size());
			assertEquals("NYC",res.get(0).getName());
		}
		
		{
			AQuery<Person2> q1 = Db.query(Person2.class).eq("name", "Moritz");
			AQuery<Store> q2 = Db.query(Store.class).in("principal", "id", q1);
			List<Store> res = manager.getByQualification(q2).toCacheAndClose();
			assertEquals(1, res.size());
			assertEquals("LA",res.get(0).getName());
		}
		
		// test limit
		
		{
			AQuery<Store> q = Db.query(Store.class).limit(1);
			List<Store> res = manager.getByQualification(q).toCacheAndClose();
			assertEquals(1, res.size());
		}
		
		{
			AQuery<Store> q = Db.query(Store.class).like("name","LA%").limit(1);
			List<Store> res = manager.getByQualification(q).toCacheAndClose();
			assertEquals(1, res.size());
		}
				
	}

	public void testReconnect() throws Exception {
		DbPool pool = createPool("testReconnect").getPool("test");

//		MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
		BookStoreSchema schema1 = new BookStoreSchema();
		DbManager manager1 = new DbManagerJdbc(pool, schema1);

		BookStoreSchema schema2 = new BookStoreSchema();
		DbManager manager2 = new DbManagerJdbc(pool, schema2);

		manager1.reconnect();
		manager2.reconnect();
		
	}

}
