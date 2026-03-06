### Estrutura do projeto
> * Spring DevTools
> * Spring MySQL
> * Spring Lombok
> * Spring Web MVC
> * Spring Data JPA

```text
java-docker/
├─ database/
├─ src/
│  ├─ main/
│  │  ├─ java/com/treinamento/
│  │  │  ├─ controller/
│  │  │  │  └─ ProdutoController.java
│  │  │  ├─ dto/produto/
│  │  │  │  ├─ ProdutoRequestDTO.java
│  │  │  │  └─ ProdutoResponseDTO.java
│  │  │  ├─ exception/
│  │  │  │  ├─ ApiErrorResponse.java
│  │  │  │  ├─ GlobalExceptionHandler.java
│  │  │  │  └─ ResourceNotFoundException.java
│  │  │  ├─ mapper/
│  │  │  │  └─ ProdutoMapper.java
│  │  │  ├─ model/
│  │  │  │  └─ Produto.java
│  │  │  ├─ repository/
│  │  │  │  └─ ProdutoRepository.java
│  │  │  ├─ service/
│  │  │  │  └─ ProdutoService.java
│  │  │  └─ JavaAppApplication.java
│  │  └─ resources/
│  │     ├─ application.properties
│  │     └─ db/seed/produtos.sql
│  └─ test/
│     ├─ java/com/treinamento/JavaAppApplicationTests.java
│     └─ resources/application-test.properties
├─ .mvn/
├─ Dockerfile
├─ docker-compose.yml
├─ etapas.md
├─ README.md
└─ pom.xml
```

### rodar o projeto
```
docker compose up --build
```

### rodar testes no docker (sem maven local)
```
docker compose --profile test run --rm tests
```

### Dockerfile (build com Maven dentro do container)
```
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests dependency:go-offline
COPY src src
RUN ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml
```
services:
  mysql:
    image: mysql:8.0
    container_name: mysql_db
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: app_db
    ports:
      - "3306:3306"
    volumes:
      - ./database:/var/lib/mysql

  app:
    build: .
    container_name: spring_app
    restart: always
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/app_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root

  tests:
    image: eclipse-temurin:21-jdk
    working_dir: /workspace
    profiles:
      - test
    volumes:
      - ./:/workspace
    command: sh -c "chmod +x ./mvnw && ./mvnw test"
```

### application.properties
```
spring.datasource.url=jdbc:mysql://mysql:3306/app_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.defer-datasource-initialization=true

spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:db/seed/produtos.sql
```

docker compose up --build
docker compose down

### se o `mvnw.cmd` falhar no Windows
- O wrapper já foi ajustado para o erro de array nula no PowerShell.
- Rode com cache local dentro do projeto:
```
set MAVEN_USER_HOME=%cd%\.m2
mvnw.cmd test
```

---

## Plano MVC (Treinamento CRUD)

### Objetivo
Treinar arquitetura MVC com Spring Boot criando CRUD de:
- `Produto`: `nome`, `descricao`, `qtd`, `valor`, `dataCompra`
- `Usuario`: `nome`, `email`, `telefone`, `senha`

### Estrutura de pastas (simples e organizada)
```text
src/main/java/com/treinamento/
├─ controller/
│  ├─ ProdutoController.java
│  └─ UsuarioController.java
├─ service/
│  ├─ ProdutoService.java
│  └─ UsuarioService.java
├─ repository/
│  ├─ ProdutoRepository.java
│  └─ UsuarioRepository.java
├─ dto/
│  ├─ produto/
│  │  ├─ ProdutoRequestDTO.java
│  │  └─ ProdutoResponseDTO.java
│  └─ usuario/
│     ├─ UsuarioRequestDTO.java
│     └─ UsuarioResponseDTO.java
├─ model/
│  ├─ Produto.java
│  └─ Usuario.java
├─ mapper/
│  ├─ ProdutoMapper.java
│  └─ UsuarioMapper.java
├─ exception/
│  ├─ ResourceNotFoundException.java
│  └─ GlobalExceptionHandler.java
└─ config/
   └─ SecurityConfig.java (opcional no começo)
```

### Resumo de cada camada

#### `model` (Entidades)
- Representa as tabelas do banco.
- Contém anotações JPA (`@Entity`, `@Table`, `@Id`).
- Exemplo: classe `Produto` mapeando colunas da tabela `produtos`.

#### `repository`
- Camada de acesso a dados.
- Interfaces que herdam de `JpaRepository`.
- Aqui ficam consultas ao banco (findBy..., existsBy...).

#### `service`
- Regras de negócio.
- Orquestra validações, busca em repositório e regras do sistema.
- Evita colocar regra no controller.

#### `controller`
- Entrada da API (endpoints REST).
- Recebe requisição HTTP, chama service e retorna resposta.
- Exemplo: `POST /produtos`, `GET /usuarios/{id}`.

#### `dto`
- Objetos de entrada/saída da API.
- Evita expor entidade diretamente.
- Permite controlar formato dos dados e validações.

#### `mapper`
- Converte `Entity <-> DTO`.
- Mantém controller/service limpos, sem conversões espalhadas.

#### `exception`
- Tratamento centralizado de erros.
- Padroniza mensagens e status HTTP (`404`, `400`, etc.).

#### `config`
- Configurações gerais da aplicação.
- Exemplo: segurança, CORS, beans comuns.

### Fluxo MVC no CRUD
1. `Controller` recebe requisição.
2. `Controller` valida DTO e chama `Service`.
3. `Service` aplica regras de negócio.
4. `Service` usa `Repository` para salvar/buscar no banco.
5. Retorna DTO de resposta para o `Controller`.
6. `Controller` responde HTTP.

### Plano de implementação (passo a passo)
1. Criar `model` (`Produto`, `Usuario`) com JPA.
2. Criar `repository` para cada entidade.
3. Criar DTOs de request/response.
4. Criar mappers para conversão.
5. Criar services com regras básicas de CRUD.
6. Criar controllers com endpoints REST.
7. Adicionar tratamento global de exceções.
8. Criar testes de controller/service (etapa final do treino).

### Endpoints sugeridos para treino

#### Produtos
- `POST /produtos`
- `GET /produtos`
- `GET /produtos/{id}`
- `PUT /produtos/{id}`
- `DELETE /produtos/{id}`

#### Usuários
- `POST /usuarios`
- `GET /usuarios`
- `GET /usuarios/{id}`
- `PUT /usuarios/{id}`
- `DELETE /usuarios/{id}`

---

## Seeds e Testes (Produtos)

### Como os seeds funcionam
- Arquivo de seed: `src/main/resources/db/seed/produtos.sql`
- Contém 10 registros iniciais de produtos para não precisar cadastrar manualmente.
- Nos testes, o Spring usa `src/test/resources/application-test.properties` com banco H2 em memória.
- Propriedades usadas no teste:
  - `spring.jpa.hibernate.ddl-auto=create-drop`
  - `spring.sql.init.mode=always`
  - `spring.sql.init.data-locations=classpath:db/seed/produtos.sql`
- Fluxo:
  1. Sobe o contexto de teste.
  2. Cria estrutura das tabelas.
  3. Executa `produtos.sql`.
  4. Roda os testes com base nesses 10 dados.

### Como os testes funcionam
- `RepositoryTest`: valida persistência e consultas JPA.
- `ServiceTest`: valida regras de negócio.
- `ControllerIT`: valida contrato HTTP (status e payload).

### Plano com 10 testes (detalhado e mapeado)
1. `deveConectarNoBancoDeTeste`
   - Tipo: conexão
   - Camada: infraestrutura/repository
   - Valida: datasource ativo e `SELECT 1` com sucesso.
2. `deveCarregarSeedCom10Produtos`
   - Tipo: seed
   - Camada: repository
   - Valida: `count()` inicial igual a `10`.
3. `deveCriarProdutoComSucesso`
   - Tipo: create
   - Camada: service
   - Valida: salvar produto e retornar `id` gerado.
4. `naoDeveCriarProdutoComValorInvalido`
   - Tipo: create (regra)
   - Camada: service
   - Valida: erro ao tentar salvar com `valor <= 0`.
5. `deveAtualizarProdutoExistente`
   - Tipo: update
   - Camada: service
   - Valida: alterar `nome`, `descricao`, `qtd`, `valor` e persistir mudança.
6. `naoDeveAtualizarProdutoInexistente`
   - Tipo: update (erro)
   - Camada: service
   - Valida: retornar exceção de `not found`.
7. `deveListarTodosProdutos`
   - Tipo: select
   - Camada: controller
   - Endpoint: `GET /produtos`
   - Valida: status `200` e lista com tamanho >= 10.
8. `deveBuscarProdutoPorId`
   - Tipo: select por id
   - Camada: controller
   - Endpoint: `GET /produtos/{id}`
   - Valida: status `200` e produto correto no payload.
9. `deveRetornar404AoBuscarIdInexistente`
   - Tipo: select por id (erro)
   - Camada: controller
   - Endpoint: `GET /produtos/{id}`
   - Valida: status `404`.
10. `deveBuscarProdutosPorNome`
   - Tipo: select por nome
   - Camada: repository/controller
   - Endpoint sugerido: `GET /produtos?nome=Notebook`
   - Valida: retorna apenas itens com nome contendo o filtro.

### Ordem recomendada de implementação dos testes
1. Conexão + seed (`1` e `2`).
2. Regras de create/update (`3` ao `6`).
3. Endpoints de leitura (`7` ao `10`).
