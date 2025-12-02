# 🧭 Guia Completo: Java + Selenium WebDriver no VS Code (Swag Labs)

## 1. Visão geral

Neste guia você tem, em ordem lógica:

1. Instalação de ambiente (JDK, VS Code, extensões).
2. Criação do projeto Maven via terminal.
3. Configuração do `pom.xml` com Selenium, WebDriverManager e JUnit 5.
4. Primeiro teste simples (abrir o Google).
5. Como rodar os testes (VS Code + Maven) e ver logs/resultados.
6. Estrutura do projeto que você está usando (Projeto_SwagLabs/Auto/Frontend).
7. Classe de testes de login no **Swag Labs** com cenários negativos e positivos.
8. Dicas rápidas de Java/POO para organizar seus testes (função x método x classe / Page Object).
9. Depuração de erros comuns (NoSuchElementException, CDP warnings, etc.).

---

## 2. Instalar ferramentas essenciais

### 2.1. Instalar o JDK (Java Development Kit)

Use um JDK LTS (17 ou 21). Você pode usar, por exemplo:

* **Adoptium / Temurin**
* Ou **Zulu Platform x64** (é apenas uma distribuição do OpenJDK mantida pela Azul Systems — segura de usar).

Baixe em:
🔗 [https://adoptium.net/](https://adoptium.net/)
ou
🔗 [https://www.azul.com/downloads/](https://www.azul.com/downloads/) (Zulu)

Depois confirme no terminal:

```bash
java -version
```

Saída esperada (exemplo):

```text
openjdk version "21.0.2" ...
```

> Se aparecer algo de Zulu (ex: `Zulu Platform x64`), é só dizendo qual distribuição do JDK você instalou.

---

### 2.2. Instalar o VS Code

Baixe em:
🔗 [https://code.visualstudio.com/](https://code.visualstudio.com/)

---

### 2.3. Extensões recomendadas no VS Code

Instale as extensões:

1. **Extension Pack for Java**
2. **Maven for Java**
3. **Debugger for Java**

Isso vai te dar:

* Criação e import de projetos Java
* Integração com Maven
* Botão de **Run Test / Debug Test** nas classes JUnit
* Autocomplete, navegação, etc.

> Você também já ativou o **GitHub Copilot** — ok, isso é opcional, mas ajuda a sugerir código.

---

## 3. Criar um projeto Maven (via terminal)

### 3.1. Criar projeto “do zero” (modelo geral)

No terminal (PowerShell, CMD ou integrado do VS Code), na pasta onde quer criar o projeto:

```bash
mvn archetype:generate \
  -DgroupId=br.com.projeto \
  -DartifactId=ProjetoSelenium \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

Isso gera:

```text
ProjetoSelenium/
 ├── pom.xml
 ├── src/main/java/br/com/projeto/App.java (ou similar)
 └── src/test/java/br/com/projeto/AppTest.java
```

Depois:

```bash
cd ProjetoSelenium
code .
```

> ⚠ Importante: **não rodar esse comando dentro de um projeto Maven já existente** (com `pom.xml`).
> Foi por isso que você recebeu o erro:
> “Unable to add module to the current project as it is not of packaging type 'pom'”.

---

### 3.2. No seu caso específico (estrutura Swag Labs)

Você montou a estrutura assim:

```text
QA_Proj_Selenium/
 └── QA_Proj_SeleniumWebDriver_SwagLabs/
     └── Projeto_SwagLabs/
         └── Auto/
             └── Frontend/
                 ├── pom.xml
                 ├── src/main/java/...
                 └── src/test/java/...
```

Ou seja:

* **Projeto Maven principal** = `Projeto_SwagLabs/Auto/Frontend`
* É nesse `pom.xml` que você configurou as dependências.
* A partir de agora, tudo que for **Selenium + JUnit** vive dentro desse módulo.

---

## 4. Configurar o `pom.xml` para Selenium + JUnit 5

Aqui vai um exemplo de `pom.xml` **coerente** com o que você estava fazendo (inclui JUnit 5 + Selenium + WebDriverManager + surefire):

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/maven-v4_0_0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>br.com.projeto</groupId>
    <artifactId>ProjetoSelenium</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>ProjetoSelenium</name>

    <properties>
        <!-- Versão de Java usada no projeto -->
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Selenium WebDriver -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.20.0</version>
        </dependency>

        <!-- WebDriverManager (baixa/configura driver automaticamente) -->
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.8.0</version>
        </dependency>

        <!-- JUnit 5 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Plugin para rodar testes com JUnit 5 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <useModulePath>false</useModulePath>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

Depois de editar o `pom.xml`, rode:

```bash
mvn clean test
```

ou

```bash
mvn clean install
```

Isso baixa as dependências e compila o projeto.

---

## 5. Primeiro teste simples com Selenium

Crie em `src/test/java/br/com/projeto/PrimeiroTeste.java`:

```java
package br.com.projeto;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class PrimeiroTeste {

    public static void main(String[] args) {

        // Configura o ChromeDriver automaticamente
        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://www.google.com");

        System.out.println("Título da página: " + driver.getTitle());

        driver.quit();
    }
}
```

> Esse é um teste “estilo main”, só pra validar se o Selenium + driver estão funcionando.

---

## 6. Rodando testes e vendo resultados

### 6.1. Rodar com Maven (linha de comando)

Na pasta do projeto (`.../Frontend`):

```bash
mvn test
```

* O Maven vai compilar e rodar as classes com `@Test` (JUnit 5).
* No final ele mostra **Tests run / Failures / Errors / Skipped**.
* Detalhes ficam em:

```text
target/surefire-reports/
```

Lá você encontra, por exemplo:

* `TEST-br.com.projeto.SwagLabsLoginTests.xml`
* `TEST-br.com.projeto.SwagLabsLoginTests.txt`

### 6.2. Rodar com VS Code (botão de play)

Se sua classe tem `@Test` (JUnit 5) e o Java + Maven estão ok:

* À esquerda, painel **Testing** do VS Code mostra as classes de teste.
* No editor, ao lado de cada método `@Test`, aparecem ícones tipo ▶ “Run Test” / “Debug Test”.

Você comentou que **não estava vendo o botão de play** – normalmente é porque:

* O VS Code não detectou o projeto como Java/Maven ainda (faltou abrir a pasta raiz do módulo onde está o `pom.xml`).
* Ou as extensões de Java ainda estavam inicializando.

---

### 6.3. Logs e algo “tipo cy.log”

Não existe um `cy.log`, mas você pode:

* Usar `System.out.println("mensagem")` dentro dos testes ou helpers.
* Ou configurar um logger (ex: SLF4J + Logback).

Exemplo simples:

```java
System.out.println("[LOGIN] Preenchendo usuário inválido");
```

Isso aparece no console quando você roda `mvn test` ou usa o botão de Run Test no VS Code.

> No log que você trouxe, apareceu:
>
> * `SLF4J(W): No SLF4J providers were found.`
>   Só quer dizer que não há implementação de log configurada — pode ignorar por enquanto.

---

## 7. Swag Labs — testes de login (negativos e positivos)

Você construiu uma classe `SwagLabsLoginTests` com:

* `@BeforeEach` → abre o navegador, maximiza, define timeouts e navega para `https://www.saucedemo.com/`.
* Helpers para:

  * Preencher username
  * Preencher password
  * Clicar login
  * Obter mensagem de erro
  * Validar login com sucesso (`Products` + `Swag Labs`).
* Cenários negativos (valida mensagens de erro).
* Cenários positivos (usuários válidos).

### 7.1. Versão organizada da sua classe de testes (ajustada)

```java
package br.com.projeto;

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

    private void realizarLoginValido(String username) {
        abrirPaginaLogin();
        preencherUsername(username);
        preencherPassword("secret_sauce");
        clicarLogin();
        validarLoginComSucesso();
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
        Assertions.assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                mensagem
        );
    }

    @Test
    @DisplayName("5 - Login falha com username válido e password inválido")
    void loginComUsernameValidoEPasswordInvalido() {
        preencherUsername("standard_user");
        preencherPassword("senha_errada");
        clicarLogin();
        String mensagem = obterMensagemErro();
        Assertions.assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                mensagem
        );
    }

    // ----------------- CENÁRIOS POSITIVOS -----------------

    @Test
    @DisplayName("6 - Login com sucesso usando standard_user")
    void loginComStandardUser() {
        realizarLoginValido("standard_user");
    }

    @Test
    @DisplayName("7 - Login com locked_out_user deve exibir mensagem de bloqueio")
    void loginComLockedOutUser() {
        abrirPaginaLogin();
        preencherUsername("locked_out_user");
        preencherPassword("secret_sauce");
        clicarLogin();

        // Aqui NÃO é sucesso, o sistema mostra erro de usuário bloqueado
        String mensagem = obterMensagemErro();
        Assertions.assertEquals(
                "Epic sadface: Sorry, this user has been locked out.",
                mensagem
        );
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
}
```

### 7.2. Erro que apareceu: `NoSuchElementException` com `.title`

O log mostrava:

> `no such element: Unable to locate element: {"method":"css selector","selector":".title"}`
> em `validarLoginComSucesso` chamado pelo teste `loginComLockedOutUser`.

Motivo:

* Para o `locked_out_user`, o sistema **não redireciona para a página de produtos**, então o elemento `.title` (`Products`) nunca aparece.
* Mas você estava chamando `realizarLoginValido("locked_out_user")`, que chama `validarLoginComSucesso()` — isso não faz sentido para esse usuário.

Correção (como fiz acima):

* Para usuários “normais” → usa `realizarLoginValido(...)`.
* Para `locked_out_user` → testa **explicitamente** a mensagem de erro, sem chamar `validarLoginComSucesso`.

---

## 8. Java + Selenium: lembretes rápidos (para estudo)

Você já pediu e eu já reorganizei um guia completo de Java/Selenium com:

* Diferença entre **função** e **método**
* Quando criar **classe**, **método**, **função**
* Estrutura de **Page Object** (LoginPage, InventoryPage)
* `DriverFactory`, `BaseTest`, Fluent Interface, etc.

Aqui vão só os **pontos-chave**, para você lembrar:

### 8.1. Classe

* Representa algo: página, entidade ou utilitário.

```java
public class LoginPage {
    WebDriver driver;
}
```

### 8.2. Método (ação)

```java
public void clicarLogin() {
    driver.findElement(By.id("login-button")).click();
}
```

### 8.3. Função (retorna algo)

```java
public String getMensagemErro() {
    return driver.findElement(By.cssSelector("[data-test='error']")).getText();
}
```

### 8.4. Page Object básico (Login)

```java
public class LoginPage {

    private WebDriver driver;
    private By username = By.id("user-name");
    private By password = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage abrirPagina() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    public LoginPage informarUsuario(String usuario) {
        driver.findElement(username).sendKeys(usuario);
        return this;
    }

    public LoginPage informarSenha(String senha) {
        driver.findElement(password).sendKeys(senha);
        return this;
    }

    public void clicarLogin() {
        driver.findElement(loginButton).click();
    }

    public String obterMensagemErro() {
        return driver.findElement(errorMessage).getText();
    }
}
```

---

## 9. Depuração: avisos e erros que você viu

### 9.1. Avisos de CDP (Chrome DevTools Protocol)

Você viu:

```text
ADVERTENCIA: Unable to find CDP implementation matching 142
...
You may need to include a dependency on a specific version of the CDP...
```

* Isso é um aviso de que **não existe implementação específica de DevTools** para sua versão exata do Chrome.
* Para testes “normais” de UI (sem mexer em DevTools), você pode **ignorar**.
* Se um dia for usar `selenium-devtools` (network, console, etc.), aí sim cai nessa.

### 9.2. `SLF4J(W): No SLF4J providers were found.`

* Só te avisando que não existe um backend de logging configurado.
* Não impede testes de rodarem.
* Mais pra frente, se quiser logs bonitos, dá pra adicionar Logback/Log4j.