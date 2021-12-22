
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FirstTask {
    static WebDriver driver;

    @BeforeAll
    static void setUp() {
        driver = Utils.invokeDriver();
        driver.get("https://demo.opencart.com/");
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    @Test
    @Order(1)
    public void openLogin() {
        driver.findElement(By.cssSelector("#top-links .dropdown a")).click();
        driver.findElement(By.linkText("Login")).click();
    }

    @Test
    @Order(2)
    public void checkIfBlocksAreVisible() {
        List<WebElement> blocks = driver.findElements(By.cssSelector("#content .well"));
        for (WebElement block : blocks) {
            String blockText = block.findElement(By.tagName("h2")).getText();
            boolean isBlockVisible = block.isDisplayed();
            Assertions.assertEquals(true, isBlockVisible, "Block " + blockText + " is not displayed.");
            System.out.println("Block " + blockText + " is displayed.");
        }
    }

    @Test
    @Order(3)
    public void loginWithFakeDetails() throws InterruptedException {
        WebElement emailInput = driver.findElement(By.id("input-email"));
        WebElement passwordInput = driver.findElement(By.id("input-password"));
        Utils.clickAndTypeText(emailInput, "fake_email@fmail.com");
        Utils.clickAndTypeText(passwordInput, "fakepassword");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#account-login .alert")));
        WebElement errorBlock = driver.findElement(By.cssSelector("#account-login .alert"));
        Assertions.assertEquals(true, errorBlock.isDisplayed(), "error block is not displayed");
        System.out.println("Error block is visible");
        Assertions.assertEquals("Warning: No match for E-Mail Address and/or Password.", errorBlock.getText(),
                "Error block text does not match" );
        System.out.println("Error block text matches");
    }
}
