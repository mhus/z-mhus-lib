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
package de.mhus.lib.vaadin.desktop;

import java.util.List;

import com.vaadin.ui.Component;

import de.mhus.lib.core.util.Pair;

public interface HelpContext {
	/**
	 *  Returns the help UI Component. Should be the same for every request for every HelpContext object.
	 *  
	 * @return the visible component
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
