package de.mhus.lib.vaadin;

import com.vaadin.event.Action.Listener;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

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
