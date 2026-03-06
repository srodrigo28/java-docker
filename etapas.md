## Treinamento por etapas (foco inicial: Produtos)

### Estrutura de pastas atualizada (com Seeds e Tests)
```text
src/main/java/com/treinamento/
├─ controller/
│  └─ ProdutoController.java
├─ service/
│  └─ ProdutoService.java
├─ repository/
│  └─ ProdutoRepository.java
├─ dto/
│  └─ produto/
│     ├─ ProdutoRequestDTO.java
│     └─ ProdutoResponseDTO.java
├─ model/
│  └─ Produto.java
├─ mapper/
│  └─ ProdutoMapper.java
├─ exception/
│  ├─ ResourceNotFoundException.java
│  └─ GlobalExceptionHandler.java
└─ config/
   └─ SeedConfig.java (opcional)

src/main/resources/
├─ application.properties
└─ db/
   └─ seed/
      └─ produtos.sql

src/test/java/com/treinamento/
└─ produto/
   ├─ controller/
   │  └─ ProdutoControllerIT.java
   ├─ service/
   │  └─ ProdutoServiceTest.java
   └─ repository/
      └─ ProdutoRepositoryTest.java

src/test/resources/
└─ application-test.properties
```

### Resumo das camadas

#### `model` (Entidade)
- Mapeia a tabela `produtos`.
- Campos esperados: `id`, `nome`, `descricao`, `qtd`, `valor`, `dataCompra`.

#### `repository`
- Interface JPA para acesso a dados (`JpaRepository<Produto, Long>`).
- Consulta e persistência do produto.

#### `service`
- Regras de negócio do CRUD.
- Validações como `qtd >= 0` e `valor > 0`.

#### `controller`
- Endpoints REST de produtos.
- Recebe DTO, chama service e retorna resposta HTTP.

#### `dto`
- `ProdutoRequestDTO`: entrada (POST/PUT).
- `ProdutoResponseDTO`: saída (GET/POST/PUT).

#### `mapper`
- Conversão entre `Produto` e DTOs.

#### `exception`
- Erros de negócio e de recurso não encontrado (`404`).
- Padroniza resposta de erro da API.

#### `db/seed`
- Scripts SQL de carga inicial.
- Evita digitação manual de registros para teste.

#### `test`
- `repository`: comportamento de persistência.
- `service`: regras de negócio.
- `controller`: contrato HTTP/endpoints.

### Fluxo MVC no CRUD
1. `Controller` recebe requisição.
2. Valida DTO e chama `Service`.
3. `Service` aplica regras.
4. `Service` usa `Repository`.
5. `Mapper` transforma entidade/DTO.
6. `Controller` responde HTTP.

### Plano de implementação (somente Produtos)
1. Criar `Produto` em `model`.
2. Criar `ProdutoRepository`.
3. Criar DTOs + mapper.
4. Criar `ProdutoService` com CRUD.
5. Criar `ProdutoController`.
6. Adicionar tratamento global de exceções.
7. Habilitar seed automático.
8. Criar testes (`repository`, `service`, `controller`).
9. Validar endpoints com base nos 10 registros iniciais.

### Endpoints de Produtos
- `POST /produtos`
- `GET /produtos`
- `GET /produtos/{id}`
- `PUT /produtos/{id}`
- `DELETE /produtos/{id}`

### Seed de Produtos (10 registros)
- Arquivo criado em: `src/main/resources/db/seed/produtos.sql`
- Carga automática recomendada para ambiente de teste: `src/test/resources/application-test.properties`
- Quando a entidade `Produto` estiver pronta, usar no `application.properties`:

```properties
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:db/seed/produtos.sql
```
