package de.mhus.lib.core.shiro;

import java.io.Closeable;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

public class SubjectEnvironment implements Closeable {

    private Subject subject;
    private Subject predecessor;

    public SubjectEnvironment(Subject subject, Subject predecessor) {
        this.subject = subject;
        this.predecessor = predecessor;
    }

    public Subject getSubject() {
        return subject;
    }
    
    @Override
    public void close() {
        if (predecessor == null)
            ThreadContext.remove();
        else
            ThreadContext.bind(predecessor);
    }

    @Override
    public String toString() {
        return AccessUtil.toString(subject);
    }
    
}
