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
package de.mhus.lib.jms;

import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.Session;

import de.mhus.lib.core.config.IConfig;

public abstract class JmsChannel extends JmsObject {

	protected JmsDestination dest;
	protected int deliveryMode = DeliveryMode.NON_PERSISTENT;
	protected int priority = 0; // default
	protected long timeToLive = 60 * 60 * 1000;

	{
		 IConfig cfg = MJms.getConfig();
		timeToLive = cfg.getLong("msgTimeToLive", timeToLive);
	}
	
	public JmsChannel(String destination, boolean destinationTopic) {
		dest = new JmsDestination(destination, destinationTopic);
	}
	
	public JmsChannel(JmsDestination dest) {
		this.dest = dest;
	}
	
	protected String createMessageId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public JmsDestination getJmsDestination() {
		return dest;
	}
	
	@Override
	public Session getSession() {
		return dest.getSession();
	}
	
	@Override
	public void close() {
		super.close();
	}

	@Override
	public String toString() {
		return getName() + "/" + getClass().getSimpleName();
	}
	
	public abstract void doBeat();
	public abstract String getName();

	public void checkConnection() {
	}

	public void reset(JmsDestination dest) {
		this.dest = dest;
		reset();
	}

	public boolean isDeliveryModePersistent() {
		return deliveryMode == DeliveryMode.PERSISTENT;
	}

	public void setDeliveryModePersistent(boolean persistent) {
		this.deliveryMode = persistent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
