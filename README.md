# Caonecta

Aplicação web para adoção responsável de animais. A plataforma permite cadastrar animais e usuários, pesquisar animais disponíveis e acompanhar solicitações de adoção. Também inclui área administrativa para gerenciar cadastros e consultar o histórico.

## Funcionalidades

- Cadastro e autenticação de usuários.
- Perfil e consulta dos próprios cadastros de animais.
- Cadastro e pesquisa de animais.
- Solicitação de adoção e acompanhamento das solicitações.
- Administração de animais, usuários e solicitações.
- Envio e exibição de imagens dos animais.

## Tecnologias

- Java 24
- Spring Boot 3.5.0
- Spring Web e Thymeleaf
- Spring JDBC
- MySQL Connector/J
- Maven

## Requisitos

- JDK 24.
- Maven 3.9 ou compatível.
- MySQL 8 em execução.

## Configurar o banco

Crie um banco chamado `caonecta` no MySQL. A configuração padrão em `src/main/resources/application.properties` usa `localhost:3306`, usuário `root` e senha vazia. Altere `spring.datasource.url`, `spring.datasource.username` e `spring.datasource.password` conforme seu ambiente. Não use credenciais reais em commits.

## Executar

Na raiz do projeto:

```bash
mvn spring-boot:run
```

Acesse `http://localhost:8080` no navegador. O servidor aceita imagens de até 10 MB por arquivo e requisição. Os arquivos enviados ficam em `uploads/animais/`.

## Estrutura

- `src/main/java/com/caonecta`: controladores, serviços, DAOs, modelos e utilitários.
- `src/main/resources/templates`: páginas Thymeleaf.
- `src/main/resources/static`: CSS e outros arquivos estáticos.
- `src/main/resources/application.properties`: configuração da aplicação e do banco.
- `uploads/animais`: imagens de exemplo usadas pelo projeto.

## Licença

Nenhuma licença foi definida. Consulte o autor antes de reutilizar ou distribuir o projeto.
