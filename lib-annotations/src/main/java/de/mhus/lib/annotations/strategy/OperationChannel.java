package de.mhus.lib.annotations.strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface OperationChannel {

    /**
     * Technical name of the channel.
     * 
     * @return technical name
     */
    String name();

    /**
     * Set JMS connection name if you describe a JMS channel.
     * @return JMS connection name
     */
    String jmsConnection() default "";
    
}
