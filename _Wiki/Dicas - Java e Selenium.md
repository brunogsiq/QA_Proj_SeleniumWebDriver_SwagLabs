✔ instalação do Java
✔ instalação do VS Code
✔ criação do projeto Maven
✔ configuração do Selenium
✔ primeiros testes
✔ Page Objects
✔ Java básico (função, método, classe…)
✔ arquitetura profissional com DriverFactory / BaseTest
✔ esperas
✔ logs
✔ enums de massa
✔ exemplos avançados
✔ CI/CD
✔ resolução dos warnings

# 📘 **GUIA DEFINITIVO DE JAVA + SELENIUM WEBDRIVER**

## *Do básico ao avançado — completo, organizado e profissional*

---

# 📍 **SUMÁRIO**

1. **Instalação e Configuração do Ambiente**

   * JDK
   * VS Code
   * Extensões
   * Maven
   * Criação do projeto

2. **Configurando o Selenium no Projeto**

   * pom.xml completo
   * Teste básico
   * Executando testes

3. **Java Essencial para Selenium**

   * Variáveis
   * Função vs Método
   * Classes, objetos e construtores
   * Importando e usando classes
   * Quando criar classe, função ou método?

4. **Arquitetura Profissional**

   * DriverFactory
   * BaseTest
   * Estrutura recomendada de pastas

5. **Page Object Model (POM)**

   * Conceito
   * LoginPage (simples e avançada)
   * PageFactory (@FindBy)
   * InventoryPage
   * Testes usando POM

6. **Esperas Inteligentes**

   * WebDriverWait
   * ExpectedConditions
   * Classe Waits

7. **Logs Profissionais (SLF4J)**

8. **Massa de Teste Inteligente**

   * Enum de usuários (standard_user etc.)

9. **Rodando Testes**

   * Terminal
   * VS Code
   * Maven
   * CI/CD (GitHub Actions)

10. **Erros Comuns e Soluções**

    * NoSuchElementException
    * CDP Warning
    * SLF4J Warning
    * Usuário locked_out

---

# ---------------------------------------

# 🟦 **1. INSTALAÇÃO E CONFIGURAÇÃO**

# ---------------------------------------

## ✅ **1.1 Instalar o JDK**

Baixe JDK 17 ou 21 (LTS):

🔗 [https://adoptium.net/](https://adoptium.net/)

Verifique:

```bash
java -version
```

Deve aparecer:

```
openjdk version "17..."
```

---

## ✅ **1.2 Instalar o VS Code**

[https://code.visualstudio.com/](https://code.visualstudio.com/)

---

## ✅ **1.3 Instalar Extensões**

Procure e instale:

1️⃣ **Extension Pack for Java**
2️⃣ **Maven for Java**
3️⃣ **Debugger for Java**

---

## ✅ **1.4 Criar projeto Maven pelo terminal**

Abra o terminal dentro da pasta onde deseja criar o projeto:

```bash
mvn archetype:generate -DgroupId=br.com.projeto -DartifactId=ProjetoSelenium -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

Estrutura criada:

```
ProjetoSelenium/
 ├── pom.xml
 ├── src/main/java
 └── src/test/java
```

Abrir projeto:

```bash
code ProjetoSelenium
```

---

# ---------------------------------------

# 🟦 **2. CONFIGURAR SELENIUM NO pom.xml**

# ---------------------------------------

Substitua o conteúdo do `<dependencies>` por:

```xml
<dependencies>

    <!-- Selenium WebDriver -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.20.0</version>
    </dependency>

    <!-- WebDriverManager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.8.0</version>
    </dependency>

    <!-- SLF4J: para resolver o aviso "no providers" -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.9</version>
    </dependency>

</dependencies>
```

Atualize o Maven:

```bash
mvn clean install
```

---

# ---------------------------------------

# 🟦 **3. JAVA ESSENCIAL PARA SELENIUM**

# ---------------------------------------

## 🚀 Variáveis

```java
String nome = "Bruno";
int idade = 30;
double salario = 3500.50;
boolean ativo = true;
```

---

## 🚀 Diferença entre FUNÇÃO e MÉTODO

### ✔ Função → retorna algo

```java
public int somar(int a, int b) {
    return a + b;
}
```

### ✔ Método → ação (pode retornar ou não)

```java
public void clicarBotao() {
    System.out.println("Cliquei!");
}
```

---

## 🚀 Criando classes, objetos e construtores

```java
public class Pessoa {
    String nome;
    int idade;

    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    public void apresentar() {
        System.out.println("Sou " + nome);
    }
}
```

Usando a classe em outro arquivo:

```java
Pessoa p = new Pessoa("Bruno", 30);
p.apresentar();
```

---

## 🚀 Quando criar CLASSE, MÉTODO ou FUNÇÃO?

### ✔ Criar CLASSE quando:

* representar algo (LoginPage, Produto, Pessoa)
* representar uma tela (Page Object)
* organizar testes
* criar utilitários (DriverFactory)

### ✔ Criar MÉTODO quando:

* for uma ação
* clicar
* preencher campo
* navegar
* esperar elemento

### ✔ Criar FUNÇÃO quando:

* precisar retornar um valor
* pegar textos do DOM
* capturar URL
* calcular algo

---

# ---------------------------------------

# 🟦 **4. ARQUITETURA PROFISSIONAL**

# ---------------------------------------

## Estrutura recomendada:

```
src/test/java/br/com/projeto/
 ├── core/
 │    ├── DriverFactory.java
 │    └── BaseTest.java
 ├── pages/
 │    ├── LoginPage.java
 │    └── InventoryPage.java
 ├── tests/
 │    └── LoginTests.java
 └── data/
      └── Usuarios.java
```

---

## 🔹 DriverFactory.java

```java
package br.com.projeto.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

    private static WebDriver driver;

    private DriverFactory() {}

    public static WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void killDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
```

---

## 🔹 BaseTest.java

```java
package br.com.projeto.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
        DriverFactory.killDriver();
    }
}
```

---

# ---------------------------------------

# 🟦 **5. PAGE OBJECT MODEL (POM)**

# ---------------------------------------

# 💠 LoginPage (versão avançada)

```java
package br.com.projeto.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By logo = By.className("login_logo");
    private By user = By.id("user-name");
    private By pass = By.id("password");
    private By btn = By.id("login-button");
    private By error = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage abrir() {
        driver.get("https://www.saucedemo.com/");
        return this;
    }

    public LoginPage escreverUsuario(String u) {
        driver.findElement(user).clear();
        driver.findElement(user).sendKeys(u);
        return this;
    }

    public LoginPage escreverSenha(String s) {
        driver.findElement(pass).clear();
        driver.findElement(pass).sendKeys(s);
        return this;
    }

    public InventoryPage loginComSucesso() {
        driver.findElement(btn).click();
        return new InventoryPage(driver);
    }

    public LoginPage loginComErro() {
        driver.findElement(btn).click();
        return this;
    }

    public String obterErro() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(error));
        return driver.findElement(error).getText();
    }
}
```

---

# 💠 InventoryPage.java

```java
package br.com.projeto.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {

    private WebDriver driver;

    private By titulo = By.className("title");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public String obterTitulo() {
        return driver.findElement(titulo).getText().trim();
    }
}
```

---

# ---------------------------------------

# 🟦 **6. ESPERAS INTELIGENTES**

# ---------------------------------------

## 🔹 Waits.java

```java
package br.com.projeto.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class Waits {

    private WebDriver driver;
    private WebDriverWait wait;

    public Waits(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WebElement visivel(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement clicavel(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
```

---

# ---------------------------------------

# 🟦 **7. LOGS PROFISSIONAIS**

# ---------------------------------------

Adicionar no pom:

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.9</version>
</dependency>
```

Usar:

```java
private static final Logger log = LoggerFactory.getLogger(LoginTests.class);

log.info("Iniciando teste de login...");
```

---

# ---------------------------------------

# 🟦 **8. MASSA DE TESTE INTELIGENTE**

# ---------------------------------------

## 🔹 Usuarios.java

```java
package br.com.projeto.data;

public enum Usuarios {

    STANDARD("standard_user", "secret_sauce"),
    LOCKED("locked_out_user", "secret_sauce"),
    PROBLEM("problem_user", "secret_sauce"),
    ERROR("error_user", "secret_sauce"),
    VISUAL("visual_user", "secret_sauce"),
    PERFORMANCE("performance_glitch_user", "secret_sauce");

    public final String user;
    public final String pass;

    Usuarios(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
}
```

---

# ---------------------------------------

# 🟦 **9. RODANDO TESTES**

# ---------------------------------------

## Terminal:

```bash
mvn test
```

## VS Code:

→ Botão ▶ Run acima da classe
→ Ou menu: Run > Run Without Debugging

---

# ---------------------------------------

# 🟦 **10. CI/CD (GitHub Actions)**

# ---------------------------------------

`.github/workflows/tests.yml`:

```yaml
name: Selenium Tests

on:
  push:
    branches: [ main ]
  pull_request:

jobs:
  tests:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Run tests
        run: mvn clean test
```

---

# ---------------------------------------

# 🟦 **11. ERROS COMUNS E SOLUÇÕES**

# ---------------------------------------

## ❗ NoSuchElementException

Causas:

* elemento não existe na página atual
* login falhou
* espera insuficiente

Solução:

* usar WebDriverWait
* validar fluxo
* revisar mapeamentos

---

## ❗ CDP Warning

```
Unable to find CDP implementation matching...
```

✔ Pode ignorar → você não está usando DevTools Protocol.

---

## ❗ SLF4J Warning

```
No SLF4J providers were found
```

✔ Resolvido ao instalar:

```xml
<artifactId>slf4j-simple</artifactId>
```

---

## ❗ locked_out_user falhando

Ele **não** deve acessar a página Products.

Mensagem correta:

```
Epic sadface: Sorry, this user has been locked out.
```

---

# 🎯 FINALIZAÇÃO

Bruno, agora você tem:

✅ guia completo
✅ arquitetura profissional
✅ material de estudo organizado
✅ Page Objects
✅ testes estruturados
✅ esperas
✅ logs
✅ CI/CD
✅ resolução de erros