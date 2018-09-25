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
package de.mhus.lib.core.currency;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class CCurrency implements Externalizable {

	private static final long serialVersionUID = 1L;
	public enum CRYPTO_CURRENCY {
		UNKNOWN("?"),
		BTC("Bitcoin"),
		LTC("Litecoin"), 
		NMC("Namecoin"), 
		BCH("Bitcoin Cash"), 
		XRP("Ripple"), 
		DASH("Dash"), 
		XMR("Monero"), 
		XLM("Stellar"), 
		ETH("Ethereum"), 
		ETC("Ethereum Classic"), 
		ZEC("Zcash"), 
		EOS("EOS.IO"), 
		NEO("NEO"), 
		USDT("USD Tether"), 
		EURT("EUR Tether");
		
		private String title;
		
		CRYPTO_CURRENCY(String title) {
			this.title = title;
		}
		private final CCurrency currency = new CCurrency(this.name());
		public CCurrency toCurrency() {
			if (this == UNKNOWN) return null;
			return currency;
		}
		
		public String getTitle() {
			return title;
		}
		
	};
	
	public enum FIAT_CURRENCY {
		UNKNOWN("?"),
		USD("US Dollar"), 
		EUR("Euro"), 
		JPY("Japanese Yen"), 
		GBR("British Pound"), 
		CHF("Swiss Franc"), 
		CAD("Canadian Dollar"), 
		AUD("Australian Dollar"), 
		HKD("Hong Kong Dollar"), 
		CNY("Chinese Yuan Renminbi"), 
		INR("Indian Rupee"), 
		RUB("Russian Ruble");
		
		private String title;
		
		FIAT_CURRENCY(String title) {
			this.title = title;
		}

		private final CCurrency currency = new CCurrency(this.name());
		public CCurrency toCurrency() {
			if (this == UNKNOWN) return null;
			return currency;
		}
		
		public String getTitle() {
			return title;
		}

	};
	
	private String name;

	public CCurrency(String in) {
		if (in == null) throw new NullPointerException("Currency name can't be null");
		name = in.trim().toUpperCase();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object in) {
		if (in == null) return false; 
		return name.equals(in.toString());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(name);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		name = in.readUTF();
	}
	
	public CRYPTO_CURRENCY toCryptoCurrency() {
		try {
			return CRYPTO_CURRENCY.valueOf(name);
		} catch (IllegalArgumentException e) {
			return CRYPTO_CURRENCY.UNKNOWN;
		}
	}
	
	public FIAT_CURRENCY toFiatCurrency() {
		try {
			return FIAT_CURRENCY.valueOf(name);
		} catch (IllegalArgumentException e) {
			return FIAT_CURRENCY.UNKNOWN;
		}
	}
}
