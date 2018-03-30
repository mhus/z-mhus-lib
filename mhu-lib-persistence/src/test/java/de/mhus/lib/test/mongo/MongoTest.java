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
package de.mhus.lib.test.mongo;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import de.mhus.lib.errors.MException;
import de.mhus.lib.mongo.MoManager;
import de.mhus.lib.mongo.MoUtil;
import junit.framework.TestCase;

public class MongoTest extends TestCase {

	private MongoCollection<Document> collection;
    private MongoClient client;
    private MongoServer server;

    @Override
	@Before
    public void setUp() {
        server = new MongoServer(new MemoryBackend());

        // bind on a random local port
        InetSocketAddress serverAddress = server.bind();

        client = new MongoClient(new ServerAddress(serverAddress));
        collection = client.getDatabase("testdb").getCollection("testcollection");
    }

    @Override
	@After
    public void tearDown() {
        client.close();
        server.shutdown();
    }

    @Test
    public void testSimpleInsertQuery() throws Exception {
        assertEquals(0, collection.count());

        // creates the database and collection in memory and insert the object
        Document obj = new Document("_id", 1).append("key", "value");
        collection.insertOne(obj);

        assertEquals(1, collection.count());
        assertEquals(obj, collection.find().first());
    }
    
    @Test
    public void testMongoSchema() throws MException {
    	Schema schema = new Schema();
    	MoManager manager = new MoManager(client, schema);
    	
    	// sample from http://mongodb.github.io/morphia/1.3/getting-started/quick-tour/
    	final Employee elmer = new Employee("Elmer Fudd", 50000.0);
    	manager.save(elmer);
    	
    	assertNotNull(elmer.getId());
    	
    	Employee elmer2 = manager.getObject(Employee.class, elmer.getId());
    	assertNotNull(elmer2);
    	assertNotNull(elmer2.getId());
    	assertEquals(elmer.getName(), elmer2.getName());
    	
    	
    	final Employee daffy = new Employee("Daffy Duck", 40000.0);
    	manager.save(daffy);

    	final Employee pepe = new Employee("Pepé Le Pew", 25000.0);
    	manager.save(pepe);

    	elmer.getDirectReports().add(daffy);
    	elmer.getDirectReports().add(pepe);

    	manager.save(elmer);
    	
    }
    
    @Test
    public void testUUIDConverter() {
    	ObjectId oid = new ObjectId("5a58a8352c3439f468cd8fcf");
    	System.out.println(oid);
    	UUID uuid = MoUtil.toUUID(oid);
    	System.out.println(uuid);
    	ObjectId oid2 = MoUtil.toObjectId(uuid);
    	System.out.println(oid2);
    	assertEquals(oid.toHexString(), oid2.toHexString());
    }
    
    
}
