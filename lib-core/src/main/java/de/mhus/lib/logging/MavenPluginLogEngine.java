/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.logging;

import org.apache.maven.plugin.AbstractMojo;

import de.mhus.lib.core.logging.LogEngine;
import de.mhus.lib.core.logging.LogFactory;

public class MavenPluginLogEngine extends LogEngine {

    private AbstractMojo owner;

    public MavenPluginLogEngine(AbstractMojo owner) {
        super(owner.getClass().getName());
        this.owner = owner;
    }

    public MavenPluginLogEngine(AbstractMojo owner, String name) {
        super(name);
        this.owner = owner;
    }

    @Override
    public boolean isDebugEnabled() {
        return owner.getLog().isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return owner.getLog().isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return owner.getLog().isErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return owner.getLog().isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return owner.getLog().isDebugEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return owner.getLog().isWarnEnabled();
    }

    @Override
    public void trace(Object message) {
        owner.getLog().debug(String.valueOf(message));
    }

    @Override
    public void trace(Object message, Throwable t) {
        if (t == null) trace(message);
        else owner.getLog().debug(String.valueOf(message), t);
    }

    @Override
    public void debug(Object message) {
        owner.getLog().debug(String.valueOf(message));
    }

    @Override
    public void debug(Object message, Throwable t) {
        if (t == null) debug(message);
        else owner.getLog().debug(String.valueOf(message), t);
    }

    @Override
    public void info(Object message) {
        owner.getLog().info(String.valueOf(message));
    }

    @Override
    public void info(Object message, Throwable t) {
        if (t == null) info(message);
        else owner.getLog().info(String.valueOf(message), t);
    }

    @Override
    public void warn(Object message) {
        owner.getLog().warn(String.valueOf(message));
    }

    @Override
    public void warn(Object message, Throwable t) {
        if (t == null) warn(message);
        else owner.getLog().warn(String.valueOf(message), t);
    }

    @Override
    public void error(Object message) {
        owner.getLog().error(String.valueOf(message));
    }

    @Override
    public void error(Object message, Throwable t) {
        if (t == null) error(message);
        else owner.getLog().error(String.valueOf(message), t);
    }

    @Override
    public void fatal(Object message) {
        owner.getLog().error(String.valueOf(message));
    }

    @Override
    public void fatal(Object message, Throwable t) {
        if (t == null) fatal(message);
        else owner.getLog().error(String.valueOf(message), t);
    }

    @Override
    public void doInitialize(LogFactory logFactory) {}

    @Override
    public void close() {}
}
