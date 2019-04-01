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

import javax.jms.Message;

import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MLog;

public class InterceptorChain extends MLog implements JmsInterceptor {

	private JmsInterceptor[] chain;
	
	public InterceptorChain() {
	}
	
	public InterceptorChain(JmsInterceptor ... chain) {
		setChain(chain);
	}
	
	@Override
	public void begin(Message message) {
		synchronized (this) {
			if (chain == null) return;
			for (JmsInterceptor inter : chain) {
				if (inter == null) continue;
				try {
					inter.begin(message);
				} catch (Throwable t) {
					log().w(inter,t);
				}
			}
		}
	}

	@Override
	public void end(Message message) {
		synchronized (this) {
			if (chain == null) return;
			for (int i = chain.length; i > 0; i--) {
				JmsInterceptor inter = chain[i-1];
				if (inter == null) continue;
				try {
					inter.end(message);
				} catch (Throwable t) {
					log().w(inter,t);
				}
			}
		}
	}

	@Override
	public void prepare(Message answer) {
		synchronized (this) {
			if (chain == null) return;
			for (JmsInterceptor inter : chain) {
				if (inter == null) continue;
				try {
					inter.prepare(answer);
				} catch (Throwable t) {
					log().w(inter,t);
				}
			}
		}
	}

	@Override
	public void answer(Message message) {
		synchronized (this) {
			if (chain == null) return;
			for (JmsInterceptor inter : chain) {
				if (inter == null) continue;
				try {
					inter.answer(message);
				} catch (Throwable t) {
					log().w(inter,t);
				}
			}
		}
	}

	public JmsInterceptor[] getChain() {
		return chain;
	}

	public InterceptorChain setChain(JmsInterceptor ... chain) {
		synchronized (this) {
			this.chain = chain;
		}
		return this;
	}
	
	public InterceptorChain append(JmsInterceptor ... interceptors) {
		synchronized (this) {
			chain = MCollection.append(chain,interceptors);
		}
		return this;
	}

}
