package utility;

import com.google.common.io.Files;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class BrowserDriver {

    public static WebDriver driver;

    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date());
        String screenshotsDirectory = "screenshots/";

        File screenshotsDir = new File(screenshotsDirectory);
        if (!screenshotsDir.exists()) {
            screenshotsDir.mkdir();
        }

        try {
            File dest = new File(screenshotsDirectory + screenshotName + "_" + timestamp + ".png");
            Files.copy(screenshotFile, dest);
        } catch (IOException e) {
            System.out.println("Error while taking screenshot: " + e.getMessage());
        }
    }
}