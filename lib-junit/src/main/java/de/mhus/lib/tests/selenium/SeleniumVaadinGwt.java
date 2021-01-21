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
package de.mhus.lib.tests.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Selenium helper for vaadin until version 8 with base gwt architecture. Not for vaadin flow
 * architecture staring with version 10.
 *
 * @author mikehummel
 */
public class SeleniumVaadinGwt extends SeleniumPage {

    public SeleniumVaadinGwt(WebDriver driver) {
        super(driver);
    }

    public WebElement clickVaadinButton(String text) {
        WebElement ele = findVaadinButton(text);
        if (ele == null) return null;
        ele.click();
        return ele;
    }

    public WebElement findVaadinButton(String caption) {
        for (WebElement e : driver.findElements(By.className("v-button-caption"))) {
            if (e.getText().equals(caption)) return e.findElement(By.xpath("./../.."));
        }
        return null;
    }

    public WebElement findVaadinInputByLabel(String caption) {
        for (WebElement e : driver.findElements(By.className("v-caption"))) {
            WebElement span = e.findElement(By.tagName("span"));
            if (span != null && span.getText().equals(caption)) {
                String inputId = span.getAttribute("for");
                if (inputId != null && inputId.startsWith("gwt-uid-")) {
                    WebElement input = driver.findElement(By.id(inputId));
                    if (input != null) {
                        return input;
                    }
                }
            }
        }
        return null;
    }
}
