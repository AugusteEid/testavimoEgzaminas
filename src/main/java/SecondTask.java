import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecondTask {
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
    public void openMP3Players() throws InterruptedException {
        WebElement dropdown = driver.findElement(By.xpath("//*[@id=\"menu\"]/div[2]/ul/li[8]"));
        Actions actionProvider = new Actions(driver);
        actionProvider.moveToElement(dropdown).build().perform();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOf(dropdown.findElement(By.cssSelector(".dropdown-menu"))));
        dropdown.findElement(By.cssSelector(".see-all")).click();
        String categoryName = driver.findElement(By.cssSelector("#content h2")).getText();
        Assertions.assertTrue(categoryName.contains("MP3 Players"), "Category name does not contain MP3 Players");
        System.out.println("Category name contains MP3 Players");
        driver.findElement(By.id("list-view")).click();
    }

    @ParameterizedTest
    @ValueSource(strings = {"iPod Nano", "iPod Touch", "iPod Shuffle"})
    @Order(2)
    public void addToCart(String searchingProductName) throws InterruptedException {
        List<WebElement> products = driver.findElements(By.cssSelector(".product-layout"));
        boolean productExists = false;
        WebElement cart = driver.findElement(By.id("cart"));
        for (WebElement product : products) {
            String productName = product.findElement(By.cssSelector(".caption h4")).getText();
            if (productName.equals(searchingProductName)) {
                productExists = true;
                product.findElement(By.cssSelector("button")).click();

                //Tikriname, ar rodoma teisinga "success" zinute
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
                wait.until(ExpectedConditions.textMatches(By.cssSelector(".alert"), Pattern.compile("Success: You have added " + productName + " to your shopping cart!")));
                WebElement alertBlock = driver.findElement(By.cssSelector(".alert"));
                Assertions.assertEquals(true, alertBlock.isDisplayed(), "Success block is not displayed");
                System.out.println("Success block is displayed");

                String successBlockText = alertBlock.getText();
                Assertions.assertEquals("Success: You have added " + productName + " to your shopping cart!", successBlockText.substring(0,successBlockText.length()-2),
                        "Error block text does not match" );
                System.out.println("Alert block text matches");

                //Tikriname, ar produktas idetas i krepseli
                FluentWait cartClickableWait = new FluentWait(driver).withTimeout(Duration.ofSeconds(8)).pollingEvery(Duration.ofMillis(2000));
                cartClickableWait.until(ExpectedConditions.visibilityOf(cart));
                cart.click();
                Assertions.assertDoesNotThrow(()->cart.findElement(By.linkText(productName)),"Product is not in cart");
                System.out.println("Product "+productName+" is added to cart");
                break;
            }
        }
        Assertions.assertTrue(productExists, "Product \"" + searchingProductName + "\" does not exist.");

    }

}
