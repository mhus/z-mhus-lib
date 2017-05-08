package de.mhus.lib.vaadin.desktop;

import java.util.List;
import java.util.Locale;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.Pair;

public class NlsHelpContext extends MLog implements HelpContext {

	private MNls nls;
	private NlsHelpPanel panel;

	public NlsHelpContext(Object owner, Locale locale) {
		this(MNls.lookup(owner));
	}
	
	public NlsHelpContext(MNls nls) {
		this.nls = nls;
	}
	
	@Override
	public synchronized Component getComponent() {
		if (panel == null) {
			panel = new NlsHelpPanel();
			showHelpTopic(null);
		}
		return panel;
	}

	@Override
	public void showHelpTopic(String topic) {
		String txt = null;
		if (topic == null)
			txt = MNls.find(nls, "help.main");
		else
			txt = MNls.find(nls, "help." + topic);
		if (txt != null) {
			getComponent();
			panel.setContent(txt);
		}
	}

	private class NlsHelpPanel extends VerticalLayout {
		
		private Label text;

		public NlsHelpPanel() {
			text = new Label();
			text.setCaptionAsHtml(true);
			addComponent(text);
			text.setSizeFull();
			setSizeFull();
		}

		public void setContent(String txt) {
			text.setValue(txt);
		}
	}

	@Override
	public List<Pair<String, String>> searchTopics(String search) {
		return null;
	}

}
