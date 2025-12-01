package br.com.projeto_swaglabs;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SwagLabsLoginTests {

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

    private void validarLoginComSucesso() {
        String tituloProdutos = driver.findElement(By.className("title")).getText().trim();
        String logo = driver.findElement(By.className("app_logo")).getText().trim();

        Assertions.assertEquals("Products", tituloProdutos);
        Assertions.assertEquals("Swag Labs", logo);
    }

    // ----------------- CENÁRIOS NEGATIVOS -----------------

    @Test
    @DisplayName("1 - Login falha quando username e password estão vazios")
    void loginSemUsernameESemPassword() {
        clicarLogin();

        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Username is required", mensagem);
    }

    @Test
    @DisplayName("2 - Login falha quando apenas o username é preenchido")
    void loginComApenasUsername() {
        preencherUsername("abc");
        clicarLogin();

        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Password is required", mensagem);
    }

    @Test
    @DisplayName("3 - Login falha quando apenas o password é preenchido")
    void loginComApenasPassword() {
        preencherPassword("abc123");
        clicarLogin();

        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Username is required", mensagem);
    }

    @Test
    @DisplayName("4 - Login falha com username inválido e password válido")
    void loginComUsernameInvalidoEPasswordValido() {
        preencherUsername("usuario_invalido");
        preencherPassword("secret_sauce");
        clicarLogin();

        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Username and password do not match any user in this service", mensagem);
    }

    @Test
    @DisplayName("5 - Login falha com username válido e password inválido")
    void loginComUsernameValidoEPasswordInvalido() {
        preencherUsername("standard_user");
        preencherPassword("senha_errada");
        clicarLogin();

        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Username and password do not match any user in this service", mensagem);
    }

    @Test
    @DisplayName("6 - Login com sucesso usando locked_out_user")
    void loginComLockedOutUser() {
        preencherUsername("locked_out_user");
        preencherPassword("secret_sauce");
        clicarLogin();

        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Sorry, this user has been locked out.", mensagem);
    }

    // ----------------- CENÁRIOS POSITIVOS -----------------

    @Test
    @DisplayName("7 - Login com sucesso usando standard_user")
    void loginComStandardUser() {
        realizarLoginValido("standard_user");
    }

    @Test
    @DisplayName("8 - Login com sucesso usando problem_user")
    void loginComProblemUser() {
        realizarLoginValido("problem_user");
    }

    @Test
    @DisplayName("9 - Login com sucesso usando performance_glitch_user")
    void loginComPerformanceGlitchUser() {
        realizarLoginValido("performance_glitch_user");
    }

    @Test
    @DisplayName("10 - Login com sucesso usando error_user")
    void loginComErrorUser() {
        realizarLoginValido("error_user");
    }

    @Test
    @DisplayName("11 - Login com sucesso usando visual_user")
    void loginComVisualUser() {
        realizarLoginValido("visual_user");
    }

    private void realizarLoginValido(String username) {
        abrirPaginaLogin(); // garante que sempre começamos na tela de login
        preencherUsername(username);
        preencherPassword("secret_sauce");
        clicarLogin();

        validarLoginComSucesso();
    }
}