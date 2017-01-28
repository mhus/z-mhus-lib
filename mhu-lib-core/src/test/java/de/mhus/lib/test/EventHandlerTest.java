package de.mhus.lib.test;

import java.util.ConcurrentModificationException;

import de.mhus.lib.core.MEventHandler;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class EventHandlerTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public EventHandlerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( EventHandlerTest.class );
    }

    /**
     * Test registration of normal and weak listeners. Test if weak
     * listener will be removed after full gc().
     */
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
	    	assertTrue("Error should be thrown",false);
    	} catch ( ConcurrentModificationException ex ) {
    	}
    	
    	for ( Object cur : eh.getListenersArray() ) {
    		((MyListener)cur).doIt();
    	}
    	
    }
    
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
