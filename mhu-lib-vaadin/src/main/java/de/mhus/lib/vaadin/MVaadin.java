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
package de.mhus.lib.vaadin;

import com.vaadin.event.Action.Listener;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;

@SuppressWarnings("deprecation")
public class MVaadin {
	
	public static boolean closeDialog(AbstractComponent component) {
		while (component != null) {
			if (component instanceof Window) {
				((Window)component).close();
				return true;
			}
		}
		return false;
	}

	  /**
	   * Handles the ENTER key while the TextField has focus.
	   * See https://vaadin.com/forum/#!/thread/77601/7736664
	   * See http://ramontalaverasuarez.blogspot.co.at/2014/06/vaadin-7-detect-enter-key-in-textfield.html
	 * @param searchField 
	 * @param listener 
	 * e.g.
	 * TextField searchField = new TextField();
     * TextFieldUtils.handleEnter(searchField, (sender, target) -&gt; {
     *  Notification.show("search: " + searchField.getValue());
     * });
	 * 
	   */
	  public static void handleEnter(TextField searchField, Listener listener) {
	    ShortcutListener enterShortCut = new ShortcutListener(
	        "EnterShortcut", ShortcutAction.KeyCode.ENTER, null) {
	      private static final long serialVersionUID = -1L;
	      @Override
	      public void handleAction(Object sender, Object target) {
	        listener.handleAction(sender, target);
	      }
	    };
	    handleShortcut(searchField, enterShortCut);
	  }

	  /**
	   * Lets the ShortcutListener handle the action while the TextField has focus.
	 * @param textField 
	 * @param shortcutListener 
	   */
	  public static void handleShortcut(AbstractTextField textField, ShortcutListener shortcutListener) {
	    textField.addFocusListener((FieldEvents.FocusEvent event) -> textField.addShortcutListener(shortcutListener));
	    textField.addBlurListener((FieldEvents.BlurEvent event) -> textField.removeShortcutListener(shortcutListener));
	  }
	  
}
