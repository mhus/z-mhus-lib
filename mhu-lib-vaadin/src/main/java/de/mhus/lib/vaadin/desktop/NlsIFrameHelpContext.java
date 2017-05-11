package de.mhus.lib.vaadin.desktop;

import java.util.List;
import java.util.Locale;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MUri;
import de.mhus.lib.core.util.Pair;

public class NlsIFrameHelpContext extends MLog implements HelpContext {

	private MNls nls;
	private HelpPanel panel;

	public NlsIFrameHelpContext(Object owner, Locale locale) {
		this(MNls.lookup(owner));
	}
	
	public NlsIFrameHelpContext(MNls nls) {
		this.nls = nls;
	}
	
	@Override
	public synchronized Component getComponent() {
		if (panel == null) {
			panel = new HelpPanel();
			showHelpTopic(null);
		}
		return panel;
	}

	@Override
	public void showHelpTopic(String topic) {
		String url = null;
		if (topic == null)
			url = MNls.find(nls, "help.main.url");
		else
			url = MNls.find(nls, "help." + topic + ".url");
		if (url != null) {
			getComponent();
			panel.setContent(url);
		}
	}

	private class HelpPanel extends VerticalLayout {
		
		private BrowserFrame iframe;

		public HelpPanel() {
			iframe = new BrowserFrame();
			addComponent(iframe);
			iframe.setSizeFull();
			setSizeFull();
		}

		public void setContent(String url) {
			iframe.setSource(new ExternalResource(url));
		}
	}

	@Override
	public List<Pair<String, String>> searchTopics(String search) {
		return null;
	}

	@Override
	public List<Pair<String, String>> getIndex() {
		return null;
	}

}
