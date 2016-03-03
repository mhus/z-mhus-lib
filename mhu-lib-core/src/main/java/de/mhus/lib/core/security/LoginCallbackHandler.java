package de.mhus.lib.core.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;


/**
 * <p>LoginCallbackHandler class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class LoginCallbackHandler implements CallbackHandler {

	  /**
	   * <p>Constructor for LoginCallbackHandler.</p>
	   */
	  public LoginCallbackHandler() {
	    super();
	  }
	 
	  /**
	   * <p>Constructor for LoginCallbackHandler.</p>
	   *
	   * @param name a {@link java.lang.String} object.
	   * @param password a {@link java.lang.String} object.
	   */
	  public LoginCallbackHandler( String name, String password) {
	    super();
	    this.username = name;
	    this.password = password;
	  }
	 
	  /**
	   * <p>Constructor for LoginCallbackHandler.</p>
	   *
	   * @param password a {@link java.lang.String} object.
	   */
	  public LoginCallbackHandler( String password) {
	    super();
	    this.password = password;
	  }
	 
	  private String password;
	  private String username;
	 
	  /**
	   * {@inheritDoc}
	   *
	   * Handles the callbacks, and sets the user/password detail.
	   */
	  @Override
	public void handle( Callback[] callbacks)
	      throws IOException, UnsupportedCallbackException {
	 
	    for ( int i=0; i<callbacks.length; i++) {
	      if ( callbacks[i] instanceof NameCallback && username != null) {
	        NameCallback nc = (NameCallback) callbacks[i];
	        nc.setName( username);
	      }
	      else if ( callbacks[i] instanceof PasswordCallback) {
	        PasswordCallback pc = (PasswordCallback) callbacks[i];
	        pc.setPassword( password.toCharArray());
	      }
	      else {
	        /*throw new UnsupportedCallbackException(
	        callbacks[i], "Unrecognized Callback");*/
	      }
	    }
	  }
	 
	}
