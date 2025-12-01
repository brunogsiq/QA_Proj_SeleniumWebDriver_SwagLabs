package br.com.projeto;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class tela_Login_CenariosNegativos {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "https://www.saucedemo.com/";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        abrirPaginaLogin();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ----------------- HELPERS -----------------

    private void abrirPaginaLogin() {
        driver.get(BASE_URL);
    }

    private void preencherUsername(String username) {
        WebElement userInput = driver.findElement(By.id("user-name"));
        userInput.clear();
        userInput.sendKeys(username);
    }

    private void preencherPassword(String password) {
        WebElement passInput = driver.findElement(By.id("password"));
        passInput.clear();
        passInput.sendKeys(password);
    }

    private void clicarLogin() {
        driver.findElement(By.id("login-button")).click();
    }

    private String obterMensagemErro() {
        WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        return error.getText().trim();
    }
}