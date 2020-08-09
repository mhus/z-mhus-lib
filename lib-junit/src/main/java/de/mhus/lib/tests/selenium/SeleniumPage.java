package de.mhus.lib.tests.selenium;

import org.openqa.selenium.WebDriver;

import de.mhus.lib.core.MThread;

public class SeleniumPage {

    protected WebDriver driver;

    public SeleniumPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean waitForText(String text, int sleep, int loops ) {
        for (int i = 0; i < loops; i++) {
            MThread.sleep(sleep);
            if (driver.getPageSource().contains(text)) return true;
        }
        return false;
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    public boolean containsText(String text) {
        return driver.getPageSource().contains(text);
    }
    
}
