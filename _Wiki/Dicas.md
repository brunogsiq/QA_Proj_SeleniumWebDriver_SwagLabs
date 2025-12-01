1. Rodar o teste pelo VS Code

    No arquivo → Vai aparecer um botão:

        ▶ Run

    Se não aparecer, abra pelo menu:

        Run > Run Without Debugging

    Ou via terminal:

        mvn test

2. Diferença entre o .java e o .class

    Por que os dois arquivos parecem iguais?

    Porque o .class é o resultado da compilação do .java.
    Quando você abre no VS Code, ele usa um decompiler (FernFlower) pra mostrar o código de volta em Java, o que dá a sensação de que são dois arquivos diferentes, mas não são:

        .java → seu código fonte - Aqui é onde deverá ser feito o código, que resulta em um "log", que seria o .class

        .class → bytecode que a JVM executa (o VS Code só está te mostrando de forma “legível”)

3. Diferença entre @Test e void: onde entram os testes?

    No arquivo .java que você mostrou:

    @Test
    @DisplayName("1 - Login falha quando username e password estão vazios")
    void loginSemUsernameESemPassword() {
        clicarLogin();
        String mensagem = obterMensagemErro();
        Assertions.assertEquals("Epic sadface: Username is required", mensagem);
    }


    @Test → diz ao JUnit que esse método é um cenário de teste.

    void → método não retorna nada; ele só executa ações e faz Assertions.

    Cada método com @Test é um teste independente.

    Então:

    Você escreve/edita tudo sempre no .java

    O Maven/JUnit lê esse .java, compila em .class e executa.