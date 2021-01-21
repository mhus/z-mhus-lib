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
package de.mhus.lib.core.currency;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

import de.mhus.lib.core.MString;

public class CCurrency implements Externalizable {

    private static final long serialVersionUID = 1L;
    public static final CCurrency UNKNOWN = new CCurrency("?", "");
    public static final String SCHEME_FIAT_PREFIX = "fiat-";

    public enum CRYPTO_CURRENCY {
        UNKNOWN("?", ""),
        BTC("Bitcoin", "bitcoin"),
        LTC("Litecoin", "litecoin"),
        NMC("Namecoin", "namecoin"),
        BCH("Bitcoin Cash", "bitcoincash"),
        XRP("Ripple", "ripple"),
        DASH("Dash", "dash"),
        XMR("Monero", "monero"),
        XLM("Stellar", "stellar"),
        ETH("Ethereum", "ethereum"),
        ETC("Ethereum Classic", "ethereumclassic"),
        ZEC("Zcash", "zcash"),
        EOS("EOS.IO", "eos"),
        NEO("NEO", "neo"),
        USDT("USD Tether", "usdtether"),
        EURT("EUR Tether", "eurtether");

        private String title;
        private String scheme;

        CRYPTO_CURRENCY(String title, String scheme) {
            this.title = title;
            this.scheme = scheme;
        }

        private final CCurrency currency = new CCurrency(this.name(), this.getScheme());

        public CCurrency toCurrency() {
            if (this == UNKNOWN) return null;
            return currency;
        }

        public String getTitle() {
            return title;
        }

        public String getScheme() {
            return scheme;
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
        private String scheme;

        FIAT_CURRENCY(String title) {
            this.title = title;
            this.scheme = SCHEME_FIAT_PREFIX + name().toLowerCase();
        }

        private final CCurrency currency = new CCurrency(this.name(), getScheme());

        public CCurrency toCurrency() {
            if (this == UNKNOWN) return null;
            return currency;
        }

        public String getTitle() {
            return title;
        }

        public String getScheme() {
            return scheme;
        }
    };

    private static HashMap<String, CCurrency> schemeMapping = new HashMap<>();

    static {
        for (CRYPTO_CURRENCY item : CRYPTO_CURRENCY.values())
            if (item != CRYPTO_CURRENCY.UNKNOWN)
                schemeMapping.put(item.getScheme(), item.toCurrency());
        for (FIAT_CURRENCY item : FIAT_CURRENCY.values())
            if (item != FIAT_CURRENCY.UNKNOWN)
                schemeMapping.put(item.getScheme(), item.toCurrency());
    }

    private String name;
    private String scheme;

    public CCurrency(String in) {
        if (in == null) throw new NullPointerException("Currency name can't be null");
        name = in.trim().toUpperCase();
        if (scheme == null) scheme = ""; // not null!
        this.scheme = getSchemeForCurrency(in);
    }

    public CCurrency(String in, String scheme) {
        if (in == null) throw new NullPointerException("Currency name can't be null");
        name = in.trim().toUpperCase();
        if (scheme == null) scheme = ""; // not null!
        this.scheme = scheme.trim().toLowerCase();
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
        out.writeUTF(scheme);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = in.readUTF();
        scheme = in.readUTF();
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

    public String getScheme() {
        return scheme;
    }

    public static String getSchemeForCurrency(String currency) {
        try {
            currency = currency.trim().toUpperCase();
            return CRYPTO_CURRENCY.valueOf(currency).getScheme();
        } catch (Throwable e) {
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public static CCurrency getCurrenctyForName(String name) {
        if (name == null) return UNKNOWN;
        name = name.trim();
        // find by scheme
        CCurrency s = schemeMapping.get(name.toLowerCase());
        if (s != null) return s;

        // test name
        String currency = name.toUpperCase();
        try {
            return CRYPTO_CURRENCY.valueOf(currency).toCurrency();
        } catch (Throwable e) {
        }
        try {
            return FIAT_CURRENCY.valueOf(currency).toCurrency();
        } catch (Throwable e) {
        }

        return UNKNOWN;
    }

    public boolean isUnknown() {
        return "?".equals(name) || "fiat-?".equals(name) || MString.isEmpty(name);
    }

    public boolean isFiat() {
        return scheme.startsWith(SCHEME_FIAT_PREFIX);
    }
}
