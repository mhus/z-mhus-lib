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

public class IFrameHelpContext extends MLog implements HelpContext {

	private HelpPanel panel;
	private String mainUrl;

	public IFrameHelpContext(String mainUrl) {
		this.mainUrl = mainUrl;
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
		panel.setContent(topic);
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
			if (url == null)
				url = mainUrl;
			iframe.setSource(new ExternalResource(url));
		}
	}

	@Override
	public List<Pair<String, String>> searchTopics(String search) {
		return null;
	}

}
