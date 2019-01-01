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
package de.mhus.lib.test;

import java.util.ConcurrentModificationException;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MEventHandler;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class EventHandlerTest {

    /**
     * Test registration of normal and weak listeners. Test if weak
     * listener will be removed after full gc().
     */
	@Test
    public void testListeners()
    {
    	MEventHandler<MyListener> eh = new MEventHandler<MyListener>() {
			@Override
			public void onFire(MyListener listener, Object event, Object... values) {
			}
    	};
    	MyListener l1 = new MyListener();
    	MyListener l2 = new MyListener();
    	
    	eh.register(l1);
    	eh.registerWeak(l2);
    	
    	assertTrue( eh.getListenersArray().length == 2 );
    	
    	l1 = null;
    	l2 = null;
    	System.gc();
    	
    	assertTrue( eh.getListenersArray().length == 1 );
    	
    }
    
    /**
     * Test unregister for normal and weak listeners
     */
	@Test
    public void testUnregister()
    {
    	MEventHandler<MyListener> eh = new MEventHandler<MyListener>() {
			@Override
			public void onFire(MyListener listener, Object event, Object... values) {
			}
    	};
    	MyListener l1 = new MyListener();
    	MyListener l2 = new MyListener();
    	
    	eh.register(l1);
    	eh.registerWeak(l2);
    	
    	assertTrue( eh.getListenersArray().length == 2 );
    	
    	eh.unregister(l1);
    	eh.unregister(l2);
    	
    	assertTrue( eh.getListenersArray().length == 0 );
    	
    }
    
    /**
     * Test if weak mode is supported.
     */
	@Test
    public void testWeakMode()
    {
    	MEventHandler<MyListener> eh = new MEventHandler<MyListener>(true) {
			@Override
			public void onFire(MyListener listener, Object event, Object... values) {
			}
    	};
    	MyListener l1 = new MyListener();
    	MyListener l2 = new MyListener();
    	
    	eh.register(l1);
    	eh.registerWeak(l2);
    	
    	assertTrue( eh.getListenersArray().length == 2 );
    	
    	l1 = null;
    	l2 = null;
    	System.gc();
    	
    	assertTrue( eh.getListenersArray().length == 0 );
    	
    }
    
	@Test
    public void testIterator() {
    	MEventHandler<MyListener> eh = new MEventHandler<MyListener>(true) {
			@Override
			public void onFire(MyListener listener, Object event, Object... values) {
			}
    	};
    	MyListener l1 = new MyListener();
    	MyListener l2 = new MyListener();
    	
    	eh.register(l1);
    	eh.registerWeak(l2);
    	
    	int cnt = 0;
    	for ( MyListener cur : eh.getListeners() ) {
    		cur.doIt();
    		cnt++;
    	}
    	
    	assertTrue( cnt == 2 );
    }

	@Test
    public void testConcurrentModification() {
    	
    	MEventHandler<MyListener> eh = new MEventHandler<MyListener>(true) {
			@Override
			public void onFire(MyListener listener, Object event, Object... values) {
			}
    	};
    	MyListener l1 = new MyListenerModify(eh);
    	MyListener l2 = new MyListenerModify(eh);
    	
    	eh.register(l1);
    	eh.registerWeak(l2);
    	
    	try {
	    	for ( MyListener cur : eh.getListeners() ) {
	    		cur.doIt();
	    	}
	    	assertTrue(false,"Error should be thrown");
    	} catch ( ConcurrentModificationException ex ) {
    	}
    	
    	for ( Object cur : eh.getListenersArray() ) {
    		((MyListener)cur).doIt();
    	}
    	
    }
    
	@Test
    public void testFireMethod() throws SecurityException, NoSuchMethodException {
    	
    	MEventHandler<MyListener> eh = new MEventHandler<MyListener>(true) {

			@Override
			public void onFire(MyListener listener, Object event, Object... values) {
				listener.doIt();
			}
    		
    	};
    	MyListener l1 = new MyListener();
    	eh.register(l1);
    	
    	eh.fire();
    	
    	assertTrue(l1.done);
    }
    
    public static class MyListener {
    	private boolean done = false;

		public void doIt() {
    		done = true;
    	}
    }
    
    public class MyListenerModify extends MyListener {
    	private MEventHandler<MyListener> eh;

		public MyListenerModify(MEventHandler<MyListener> eh) {
			this.eh = eh;
		}

		@Override
		public void doIt() {
    		eh.register(new MyListener());
    	}
		
    }
    
}
