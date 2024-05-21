package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverUtils {

    public static WebDriver getWebDriver() {
        // Configure WebDriver based on your preference (e.g., Chrome)
        return new ChromeDriver();
    }

    public static void closeDriver(WebDriver driver) {
        driver.quit();
    }
}