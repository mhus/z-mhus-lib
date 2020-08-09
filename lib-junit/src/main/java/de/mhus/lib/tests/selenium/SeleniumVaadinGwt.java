package de.mhus.lib.tests.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Selenium helper for vaadin until version 8 with base gwt architecture. 
 * Not for vaadin flow architecture staring with version 10.
 * 
 * @author mikehummel
 *
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
            if (e.getText().equals(caption))
                return e.findElement(By.xpath("./../.."));
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
