import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Utils {
    public static WebDriver invokeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--ignore-certificate-errors",
                "--start-maximized"
        );
        WebDriver driver = new ChromeDriver();
        return driver;
    }
    public static void clickAndTypeText(WebElement input, String inputText){
        input.click();
        input.clear();
        input.sendKeys(inputText);
    }
}

