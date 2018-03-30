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
package de.mhus.lib.core.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;


public class LoginCallbackHandler implements CallbackHandler {

	  public LoginCallbackHandler() {
	    super();
	  }
	 
	  public LoginCallbackHandler( String name, String password) {
	    super();
	    this.username = name;
	    this.password = password;
	  }
	 
	  public LoginCallbackHandler( String password) {
	    super();
	    this.password = password;
	  }
	 
	  private String password;
	  private String username;
	 
	  /**
	   * Handles the callbacks, and sets the user/password detail.
	   * @param callbacks the callbacks to handle
	   * @throws IOException if an input or output error occurs.
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