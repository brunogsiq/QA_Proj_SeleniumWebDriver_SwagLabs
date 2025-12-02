# 📘 GUIA COMPLETO — SELENIUM WEBDRIVER COM JAVASCRIPT (NODE.JS)

Automação de testes de interface usando:

* **Node.js**
* **JavaScript (ES Modules)**
* **Selenium WebDriver (selenium-webdriver)**
* **Mocha** (test runner)
* **Page Object Model (POM)**

---

# 📍 SUMÁRIO

- [📘 GUIA COMPLETO — SELENIUM WEBDRIVER COM JAVASCRIPT (NODE.JS)](#-guia-completo--selenium-webdriver-com-javascript-nodejs)
- [📍 SUMÁRIO](#-sumário)
  - [1. INSTALAÇÃO E CONFIGURAÇÃO DO AMBIENTE](#1-instalação-e-configuração-do-ambiente)
    - [✅ 1.1 Instalar Node.js](#-11-instalar-nodejs)
    - [✅ 1.2 VS Code](#-12-vs-code)
    - [✅ 1.3 Criar a pasta do projeto](#-13-criar-a-pasta-do-projeto)
  - [2. CRIANDO O PROJETO COM NODE.JS + NPM](#2-criando-o-projeto-com-nodejs--npm)
  - [3. INSTALANDO SELENIUM WEBDRIVER E FERRAMENTAS](#3-instalando-selenium-webdriver-e-ferramentas)
  - [4. PRIMEIRO TESTE COM SELENIUM + JS](#4-primeiro-teste-com-selenium--js)
  - [5. CONCEITOS DE JAVASCRIPT PARA AUTOMAÇÃO](#5-conceitos-de-javascript-para-automação)
    - [🔹 Variáveis (let / const)](#-variáveis-let--const)
    - [🔹 Função em JS](#-função-em-js)
    - [🔹 Classe em JS](#-classe-em-js)
    - [🔹 Módulos (import / export)](#-módulos-import--export)
    - [✅ Função x Método em JS (similar ao Java conceitualmente)](#-função-x-método-em-js-similar-ao-java-conceitualmente)
  - [6. ESTRUTURA PROFISSIONAL DE PROJETO](#6-estrutura-profissional-de-projeto)
  - [7. PAGE OBJECT MODEL EM JAVASCRIPT](#7-page-object-model-em-javascript)
    - [🔹 core/driver.js](#-coredriverjs)
    - [🔹 core/waits.js](#-corewaitsjs)
    - [🔹 pages/login.page.js](#-pagesloginpagejs)
    - [🔹 pages/inventory.page.js](#-pagesinventorypagejs)
  - [8. ESPERAS INTELIGENTES (WebDriverWait / until)](#8-esperas-inteligentes-webdriverwait--until)
  - [9. MASSA DE DADOS E “ENUMS” EM JS](#9-massa-de-dados-e-enums-em-js)
    - [🔹 data/usuarios.js](#-datausuariosjs)
  - [10. EXECUTANDO OS TESTES](#10-executando-os-testes)
    - [🔹 tests/login.test.js — NEGATIVOS + POSITIVOS (Swag Labs)](#-testslogintestjs--negativos--positivos-swag-labs)
  - [11. DICAS, BOAS PRÁTICAS E PRÓXIMOS PASSOS](#11-dicas-boas-práticas-e-próximos-passos)

---

## 1. INSTALAÇÃO E CONFIGURAÇÃO DO AMBIENTE

### ✅ 1.1 Instalar Node.js

Baixe o LTS (recomendado):

🔗 [https://nodejs.org/](https://nodejs.org/)

Verifique a instalação no terminal:

```bash
node -v
npm -v
```

---

### ✅ 1.2 VS Code

Você já está usando, então só reforçando as extensões úteis:

* **JavaScript and TypeScript Nightly** (opcional)
* **ESLint**
* **Prettier**
* **GitLens** (para versionamento)

---

### ✅ 1.3 Criar a pasta do projeto

Exemplo de estrutura inicial (pode ficar dentro da sua área de QA / GitHub):

```bash
mkdir QA_Proj_SeleniumWebDriver_SwagLabs_JS
cd QA_Proj_SeleniumWebDriver_SwagLabs_JS
```

Abra no VS Code:

```bash
code .
```

---

## 2. CRIANDO O PROJETO COM NODE.JS + NPM

No terminal dentro da pasta do projeto:

```bash
npm init -y
```

Isso cria um `package.json` básico.

Vamos já adaptar para usar **ES Modules** (import/export):

No `package.json`, adicione `"type": "module"`:

```jsonc
{
  "name": "qa-proj-selenium-swaglabs-js",
  "version": "1.0.0",
  "description": "Automação Swag Labs com Selenium WebDriver + JavaScript",
  "main": "index.js",
  "type": "module",          // >>> IMPORTANTE para usar import/export
  "scripts": {
    "test": "mocha ./tests/**/*.test.js"
  },
  "author": "Bruno Siqueira",
  "license": "MIT"
}
```

---

## 3. INSTALANDO SELENIUM WEBDRIVER E FERRAMENTAS

Vamos instalar:

* **selenium-webdriver**
* **chromedriver** (binário do Chrome)
* **mocha** (test runner)
* **chai** (assertions)

```bash
npm install selenium-webdriver chromedriver
npm install --save-dev mocha chai
```

Opcional: ESLint e Prettier se quiser padronização:

```bash
npm install --save-dev eslint prettier
```

---

## 4. PRIMEIRO TESTE COM SELENIUM + JS

Crie a pasta de testes:

```bash
mkdir tests
```

Crie o arquivo `tests/primeiroTeste.test.js`:

```js
// tests/primeiroTeste.test.js
import { Builder } from "selenium-webdriver";
import "chromedriver";
import { expect } from "chai";

describe("Primeiro teste com Selenium + JavaScript", function () {
  // Aumenta timeout padrão do Mocha (importante para Selenium)
  this.timeout(30000);

  let driver;

  before(async () => {
    driver = await new Builder().forBrowser("chrome").build();
  });

  after(async () => {
    if (driver) {
      await driver.quit();
    }
  });

  it("Deve abrir o Google e verificar o título", async () => {
    await driver.get("https://www.google.com");
    const titulo = await driver.getTitle();
    console.log("Título da página:", titulo);

    expect(titulo).to.contain("Google");
  });
});
```

Rodar o teste:

```bash
npm test
```

Se tudo estiver ok, abre o Chrome, faz o teste e fecha.

---

## 5. CONCEITOS DE JAVASCRIPT PARA AUTOMAÇÃO

Você saiu de **Java** → agora é **JavaScript (Node.js)**. Aqui vão os básicos focados em automação.

### 🔹 Variáveis (let / const)

```js
const nome = "Bruno"; // constante
let idade = 30;       // variável que pode mudar
```

---

### 🔹 Função em JS

Função normal:

```js
function somar(a, b) {
  return a + b;
}
```

Função arrow:

```js
const somar = (a, b) => a + b;
```

---

### 🔹 Classe em JS

```js
class Pessoa {
  constructor(nome, idade) {
    this.nome = nome;
    this.idade = idade;
  }

  apresentar() {
    console.log(`Olá, eu sou ${this.nome} e tenho ${this.idade} anos.`);
  }
}

const p = new Pessoa("Bruno", 30);
p.apresentar();
```

---

### 🔹 Módulos (import / export)

Arquivo `pessoa.js`:

```js
export class Pessoa {
  constructor(nome, idade) {
    this.nome = nome;
    this.idade = idade;
  }

  apresentar() {
    console.log(`Sou ${this.nome}`);
  }
}
```

Arquivo `testePessoa.js`:

```js
import { Pessoa } from "./pessoa.js";

const p = new Pessoa("Bruno", 30);
p.apresentar();
```

---

### ✅ Função x Método em JS (similar ao Java conceitualmente)

* **Função**: bloco que retorna algo ou executa algo, pode estar solta ou dentro de uma classe.
* **Método**: função **dentro de uma classe/objeto** (como `apresentar()` em `Pessoa`).

Na prática, em JS para Selenium você usará **muita função dentro de classe** (Page Objects).

---

## 6. ESTRUTURA PROFISSIONAL DE PROJETO

Sugestão para o teu Swag Labs:

```bash
QA_Proj_SeleniumWebDriver_SwagLabs_JS/
 ├── package.json
 ├── tests/
 │    ├── login.test.js
 ├── pages/
 │    ├── login.page.js
 │    └── inventory.page.js
 ├── core/
 │    ├── driver.js
 │    └── waits.js
 ├── data/
 │    └── usuarios.js
 └── README.md
```

---

## 7. PAGE OBJECT MODEL EM JAVASCRIPT

Vamos usar o **Swag Labs** (igual antes) e estruturar os objetos de página.

### 🔹 core/driver.js

```js
// core/driver.js
import { Builder } from "selenium-webdriver";
import "chromedriver";

let driver;

export async function getDriver() {
  if (!driver) {
    driver = await new Builder().forBrowser("chrome").build();
    await driver.manage().window().maximize();
  }
  return driver;
}

export async function quitDriver() {
  if (driver) {
    await driver.quit();
    driver = null;
  }
}
```

---

### 🔹 core/waits.js

```js
// core/waits.js
import { until } from "selenium-webdriver";

export async function esperarVisivel(driver, locator, timeout = 10000) {
  return await driver.wait(until.elementLocated(locator), timeout);
}

export async function esperarClique(driver, locator, timeout = 10000) {
  const element = await driver.wait(until.elementLocated(locator), timeout);
  await driver.wait(until.elementIsVisible(element), timeout);
  await driver.wait(until.elementIsEnabled(element), timeout);
  return element;
}
```

---

### 🔹 pages/login.page.js

```js
// pages/login.page.js
import { By } from "selenium-webdriver";
import { esperarVisivel } from "../core/waits.js";

export class LoginPage {
  constructor(driver) {
    this.driver = driver;
    this.url = "https://www.saucedemo.com/";

    // Locators
    this.logo = By.className("login_logo");
    this.usernameInput = By.id("user-name");
    this.passwordInput = By.id("password");
    this.loginButton = By.id("login-button");
    this.errorMessage = By.cssSelector("[data-test='error']");
  }

  async abrirPagina() {
    await this.driver.get(this.url);
    await esperarVisivel(this.driver, this.logo);
    return this;
  }

  async informarUsuario(usuario) {
    const input = await this.driver.findElement(this.usernameInput);
    await input.clear();
    await input.sendKeys(usuario);
    return this;
  }

  async informarSenha(senha) {
    const input = await this.driver.findElement(this.passwordInput);
    await input.clear();
    await input.sendKeys(senha);
    return this;
  }

  async clicarLogin() {
    const botao = await this.driver.findElement(this.loginButton);
    await botao.click();
    return this;
  }

  async obterMensagemErro() {
    const el = await esperarVisivel(this.driver, this.errorMessage);
    const texto = await el.getText();
    return texto.trim();
  }
}
```

---

### 🔹 pages/inventory.page.js

```js
// pages/inventory.page.js
import { By } from "selenium-webdriver";
import { esperarVisivel } from "../core/waits.js";

export class InventoryPage {
  constructor(driver) {
    this.driver = driver;
    this.titulo = By.className("title");
    this.logo = By.className("app_logo");
  }

  async aguardarCarregamento() {
    await esperarVisivel(this.driver, this.titulo);
    return this;
  }

  async obterTitulo() {
    const el = await this.driver.findElement(this.titulo);
    return (await el.getText()).trim();
  }

  async obterLogo() {
    const el = await this.driver.findElement(this.logo);
    return (await el.getText()).trim();
  }
}
```

---

## 8. ESPERAS INTELIGENTES (WebDriverWait / until)

Na versão JS, usamos:

```js
import { until } from "selenium-webdriver";

await driver.wait(until.elementLocated(By.id("user-name")), 10000);
```

Por isso criamos `core/waits.js`, para centralizar e reaproveitar.

---

## 9. MASSA DE DADOS E “ENUMS” EM JS

### 🔹 data/usuarios.js

```js
// data/usuarios.js
export const Usuarios = {
  STANDARD: { user: "standard_user", pass: "secret_sauce" },
  LOCKED: { user: "locked_out_user", pass: "secret_sauce" },
  PROBLEM: { user: "problem_user", pass: "secret_sauce" },
  PERFORMANCE: { user: "performance_glitch_user", pass: "secret_sauce" },
  ERROR: { user: "error_user", pass: "secret_sauce" },
  VISUAL: { user: "visual_user", pass: "secret_sauce" },
};
```

---

## 10. EXECUTANDO OS TESTES

### 🔹 tests/login.test.js — NEGATIVOS + POSITIVOS (Swag Labs)

```js
// tests/login.test.js
import { expect } from "chai";
import { By } from "selenium-webdriver";
import { getDriver, quitDriver } from "../core/driver.js";
import { LoginPage } from "../pages/login.page.js";
import { InventoryPage } from "../pages/inventory.page.js";
import { Usuarios } from "../data/usuarios.js";

describe("Swag Labs - Cenários de Login", function () {
  this.timeout(60000);

  let driver;
  let loginPage;

  before(async () => {
    driver = await getDriver();
    loginPage = new LoginPage(driver);
  });

  after(async () => {
    await quitDriver();
  });

  // --------- CENÁRIOS NEGATIVOS ---------

  it("1 - Login falha quando username e password estão vazios", async () => {
    await loginPage.abrirPagina();
    await loginPage.clicarLogin();

    const msg = await loginPage.obterMensagemErro();
    expect(msg).to.equal("Epic sadface: Username is required");
  });

  it("2 - Login falha quando apenas o username é preenchido", async () => {
    await loginPage.abrirPagina();
    await loginPage.informarUsuario("abc");
    await loginPage.clicarLogin();

    const msg = await loginPage.obterMensagemErro();
    expect(msg).to.equal("Epic sadface: Password is required");
  });

  it("3 - Login falha quando apenas o password é preenchido", async () => {
    await loginPage.abrirPagina();
    await loginPage.informarSenha("abc123");
    await loginPage.clicarLogin();

    const msg = await loginPage.obterMensagemErro();
    expect(msg).to.equal("Epic sadface: Username is required");
  });

  it("4 - Login falha com username inválido e password válido", async () => {
    await loginPage.abrirPagina();
    await loginPage.informarUsuario("usuario_invalido");
    await loginPage.informarSenha("secret_sauce");
    await loginPage.clicarLogin();

    const msg = await loginPage.obterMensagemErro();
    expect(msg).to.equal(
      "Epic sadface: Username and password do not match any user in this service"
    );
  });

  it("5 - Login falha com username válido e password inválido", async () => {
    await loginPage.abrirPagina();
    await loginPage.informarUsuario(Usuarios.STANDARD.user);
    await loginPage.informarSenha("senha_errada");
    await loginPage.clicarLogin();

    const msg = await loginPage.obterMensagemErro();
    expect(msg).to.equal(
      "Epic sadface: Username and password do not match any user in this service"
    );
  });

  // --------- CENÁRIOS POSITIVOS ---------

  async function validarLoginComSucesso(usuario) {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(usuario.user)
      .informarSenha
      ? null
      : null;
  }

  it("6 - Login com sucesso usando standard_user", async () => {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(Usuarios.STANDARD.user)
      .informarSenha(Usuarios.STANDARD.pass)
      .clicarLogin();

    const inventory = new InventoryPage(driver);
    await inventory.aguardarCarregamento();

    expect(await inventory.obterTitulo()).to.equal("Products");
    expect(await inventory.obterLogo()).to.equal("Swag Labs");
  });

  it("7 - Login com locked_out_user deve exibir mensagem de bloqueio", async () => {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(Usuarios.LOCKED.user)
      .informarSenha(Usuarios.LOCKED.pass)
      .clicarLogin();

    const msg = await loginPage.obterMensagemErro();
    expect(msg).to.equal(
      "Epic sadface: Sorry, this user has been locked out."
    );
  });

  it("8 - Login com sucesso usando problem_user", async () => {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(Usuarios.PROBLEM.user)
      .informarSenha(Usuarios.PROBLEM.pass)
      .clicarLogin();

    const inventory = new InventoryPage(driver);
    await inventory.aguardarCarregamento();

    expect(await inventory.obterTitulo()).to.equal("Products");
  });

  it("9 - Login com sucesso usando performance_glitch_user", async () => {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(Usuarios.PERFORMANCE.user)
      .informarSenha(Usuarios.PERFORMANCE.pass)
      .clicarLogin();

    const inventory = new InventoryPage(driver);
    await inventory.aguardarCarregamento();

    expect(await inventory.obterTitulo()).to.equal("Products");
  });

  it("10 - Login com sucesso usando error_user", async () => {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(Usuarios.ERROR.user)
      .informarSenha(Usuarios.ERROR.pass)
      .clicarLogin();

    const inventory = new InventoryPage(driver);
    await inventory.aguardarCarregamento();

    expect(await inventory.obterTitulo()).to.equal("Products");
  });

  it("11 - Login com sucesso usando visual_user", async () => {
    await loginPage.abrirPagina();
    await loginPage
      .informarUsuario(Usuarios.VISUAL.user)
      .informarSenha(Usuarios.VISUAL.pass)
      .clicarLogin();

    const inventory = new InventoryPage(driver);
    await inventory.aguardarCarregamento();

    expect(await inventory.obterTitulo()).to.equal("Products");
  });
});
```

> Obs.: se quiser deixo esse bloco mais “seco” depois, mas a ideia é:
> você já tem **todos os cenários negativos e positivos** iguais ao que fizemos em Java, só que agora em **JavaScript**.

Rodar:

```bash
npm test
```

---

## 11. DICAS, BOAS PRÁTICAS E PRÓXIMOS PASSOS

* Use **`console.log()`** como equivalente leve de `cy.log()` / logs simples.
* Para relatórios, você pode usar:

  * **mochawesome**
  * **Allure (com adaptador para Mocha)**.
* Pode criar uma camada de:

  * `utils/` para helpers
  * `commands/` para ações repetitivas (ex.: login, logout, limpar carrinho)
* Tudo o que você fez em **Java + Selenium** (Page Object, Base, Massas)
  → agora você consegue replicar em **JS + Selenium** com a mesma mentalidade.

---