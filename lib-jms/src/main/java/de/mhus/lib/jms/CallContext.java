package de.mhus.lib.jms;

import javax.jms.Message;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;

public class CallContext {
    
    private Message message;
    private MProperties properties;

    public CallContext(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public IProperties getProperties() {
        if (properties == null) properties = new MProperties();
        return properties;
    }

}
