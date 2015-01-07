package de.mhus.lib.test.jpa;

import java.util.List;

import junit.framework.TestCase;
import de.mhus.lib.jpa.JpaDefaultProperties;
import de.mhus.lib.jpa.JpaEntityManager;
import de.mhus.lib.jpa.JpaManager;
import de.mhus.lib.jpa.JpaQuery;
import de.mhus.lib.test.jpa.model.Book;
import de.mhus.lib.test.jpa.model.BookStoreSchema;
import de.mhus.lib.test.jpa.model.Person;

public class JpaTest extends TestCase {

	public void testJpa() {
		
		JpaDefaultProperties prop = new JpaDefaultProperties();
		prop.configureHsqlMemory();
		prop.enableSqlTrace();
		
		BookStoreSchema schema = new BookStoreSchema();
		prop.setSchema(schema);
		
		JpaManager manager = new JpaManager(prop);
		
		JpaEntityManager em = manager.createEntityManager();
		
		em.begin();
		
		Book book1 = new Book();
		book1.setName("Oh wie schoen ist Panama");
		em.persist(book1);
		
		Book book2 = new Book();
		book2.setName("Sofies Welt");
		em.persist(book2);
		
		em.commit();
		
		System.out.println("ID: " + book1.getId() );
		System.out.println("ID: " + book2.getId() );
		
		assertNotSame(book1.getId(), book2.getId());

		Book foundBook = em.find(Book.class, book1.getId());
		
		assertSame(book1.getName(), foundBook.getName());
		
		assertSame(book1, foundBook);
		
		Book copyBook1 = em.copy(book1);
		
		assertSame(book1.getName(), copyBook1.getName());
		
		assertTrue(book1 != copyBook1);
		
		// assertTrue(foundBook.is)
		
		{
			JpaQuery<Book> query = em.createQuery("select b from Book b", Book.class);
			
	
			List<Book> list = query.getResultList();
			
			assertTrue(list.size() == 2);
			
			for ( Book res : list) {
				System.out.println("RES: " + res.getId() + " " + res.getName());
			}
		}	
		
		em.begin();
		
		Person p1 = em.injectObject(new Person());
		p1.setName("Kurt Cobain");
		p1.save();
		
		
		Person p2 = em.injectObject(new Person());
		p2.setName("Lola Langohr");
		p2.save();
		
		em.commit();
		
		{
			JpaQuery<Person> query = em.createQuery("select p from Person p", Person.class);
			List<Person> list = query.getResultList();
			assertTrue(list.size() == 2);
			
			for ( Person res : list) {
				System.out.println("RES: " + res.getId() + " " + res.getName());
			}
		}
		
		em.begin();
		p2.remove();
		em.commit();
		
		{
			JpaQuery<Person> query = em.createQuery("select p from Person p", Person.class);
			List<Person> list = query.getResultList();
			assertTrue(list.size() == 1);
			
			for ( Person res : list) {
				System.out.println("RES: " + res.getId() + " " + res.getName());
			}
		}
		
	}
	
}
