package de.mhus.lib.test.adb;

import java.util.List;
import java.util.UUID;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import de.mhus.lib.adb.DbCollection;
import de.mhus.lib.adb.DbManager;
import de.mhus.lib.adb.query.Db;
import de.mhus.lib.core.MStopWatch;
import de.mhus.lib.core.config.NodeConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;
import de.mhus.lib.sql.DbPool;
import de.mhus.lib.sql.DbPoolBundle;
import de.mhus.lib.test.adb.model.Book;
import de.mhus.lib.test.adb.model.BookStoreSchema;
import de.mhus.lib.test.adb.model.Finances;
import de.mhus.lib.test.adb.model.Person;
import de.mhus.lib.test.adb.model.Regal;
import de.mhus.lib.test.adb.model.Store;

public class AdbTest extends TestCase {

	public AdbTest(String name) {
		super(name);
    	try {
//			MSingleton.instance().getConfig().setBoolean("TRACE", true);
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
    
    @SuppressWarnings("unused")
	public void testModel() throws Throwable {
    	
    	DbPool pool = createPool().getPool("test");
    	
    	BookStoreSchema schema = new BookStoreSchema();
    	
    	MStopWatch timer = new MStopWatch();
    	timer.start();
    	
    	DbManager manager = new DbManager(pool, schema);

    	// create persons
    	Person p = new Person();
    	p.setName("Klaus Mustermann");
    	manager.createObject(p);
    	UUID p1 = p.getId();
    	
    	p.setId(null);
    	p.setName("Alex Admin");
    	manager.createObject(p);
    	UUID p2 = p.getId();
    	
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
    	
    	b = (Book) manager.getObject(Book.class, b1);
    	b.setLendToId(p1);
    	manager.saveObject(b);
    	
    	b.setLendToId(null);
    	manager.saveObject(b);
    	
    	b.setLendToId(p2);
    	b.setAuthorId(new UUID[] {p1});
    	manager.saveObject(b);

    	Book copy = (Book) manager.getObject(Book.class, b.getId());
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
    	
    	
    	// remove book
    	
    	manager.removeObject(b);
    	
    	b = (Book) manager.getObject(Book.class, b1);
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
    	Store s2 = (Store) manager.getObject(Store.class, s1.getId());
    	s2.setAddress("");
    	s2.save();
    	
    	s1.reload();
    	assertEquals(s1.getAddress(), s2.getAddress());
    	assertEquals(s1.getSqlDate().toString(), new java.sql.Date(0).toString());
    	
    	// remove and check behavior of updates
    	s1.remove();
    	
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
    		f.remove();
    		assertTrue(false);
    	} catch (MException e) {
    		System.out.println(e);
    	}
    	
    	f.setConfidential("read"); // hack :)
    	f.setNewConfidential("");
    	f.save();
    	f.reload();
    	f.remove();
    	
    	// -------------
    	// test dynamic objects

    	Regal r = new Regal();
    	r.setValue("store", s1.getId());
    	r.setValue("name", "regal 1");
    	r.create(manager);
    	
    	r.setValue("name", "regal 22113221");
    	r.save();
    	
    	Regal r2 = (Regal) manager.getObject(Regal.class, r.getValue("id"));
    	assertNotNull(r2);

    	r2.reload();
    	
    	r2.remove();
    	
    	timer.stop();
    	System.out.println("Time: " + timer.getCurrentTimeAsString(true));
    }
	
	
}
