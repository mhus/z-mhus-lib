package de.mhus.lib.jms;

import javax.jms.Message;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MProperties;

public class JmsContext {
    
    private Message message;
    private MProperties properties;
    private Message answer;

    public JmsContext(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public IProperties getProperties() {
        if (properties == null) properties = new MProperties();
        return properties;
    }

    public Message getAnswer() {
        return answer;
    }

    public void setAnswer(Message answer) {
        this.answer = answer;
    }

}
