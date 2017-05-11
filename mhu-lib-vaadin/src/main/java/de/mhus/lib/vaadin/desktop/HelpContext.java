package de.mhus.lib.vaadin.desktop;

import java.util.List;

import com.vaadin.ui.Component;

import de.mhus.lib.core.util.Pair;

public interface HelpContext {
	/**
	 *  Returns the help UI Component. Should be the same for every request for every HelpContext object.
	 *  
	 * @return
	 */
	Component getComponent();
	
	/**
	 * Navigate to the topic page. Set null to navigate to the main help page.
	 * 
	 * @param topic
	 */
	void showHelpTopic(String topic);
	
	/**
	 * Search for a topic. Return the results. Return null if search is not supported.
	 * 
	 * @param search
	 * @return List of 'Display Name' and 'topic' ordered by score
	 */
	List<Pair<String,String>> searchTopics(String search);

	/**
	 * Return the index of all (important) topics. If you need sub topics add
	 * space or spaces in front of the display name.
	 * 
	 * If you don't support an index return null.
	 * 
	 * @return List of 'Display Name' and 'topic' 
	 */
	List<Pair<String,String>> getIndex();
}
