# 🍔 JottasBurger API

Backend de uma hamburgueria desenvolvido com **Java 21 + Spring Boot 3**, focado em **boas práticas de mercado, arquitetura limpa e evolução incremental**.

Este projeto simula um sistema real com **autenticação, autorização, gestão de pedidos e isolamento de dados por usuário**.

---

## 📌 Objetivo

Este projeto foi criado como **portfólio profissional**, com foco em:

* simular um sistema real de delivery
* aplicar padrões utilizados no mercado
* evoluir de um CRUD simples para um backend completo
* demonstrar domínio de:

  * arquitetura
  * segurança
  * testes
  * performance

---

## 🧱 Arquitetura

O projeto segue um **monólito modular**, organizado por domínio:

```
com.jfb.jottasburger
├── auth        → autenticação e segurança (JWT)
├── user        → entidade de usuário
├── category    → categorias de produtos
├── product     → catálogo de produtos
├── order       → pedidos e itens
├── common      → utilitários compartilhados
├── config      → configurações
└── exception   → tratamento global de erros
```

Cada módulo contém:

* `controller` → entrada da API (REST)
* `service` → regras de negócio
* `repository` → acesso a dados
* `dto` → contratos de entrada/saída
* `model` → entidades JPA

---

## ⚙️ Tecnologias

* Java 21
* Spring Boot 3
* Spring Web
* Spring Data JPA
* Spring Security (JWT)
* PostgreSQL
* Flyway (versionamento de banco)
* Bean Validation
* SpringDoc OpenAPI (Swagger)
* Lombok
* JUnit + Mockito
* MockMvc (testes de integração)

---

## 🔐 Segurança

Implementação completa com:

* autenticação via JWT
* controle de acesso por roles:

  * `ADMIN`
  * `CUSTOMER`
* endpoints protegidos por perfil
* isolamento de dados por usuário (`/orders/me`)

---

## 🚀 Funcionalidades

### 👤 Autenticação

* `POST /api/auth/register` → cadastro de cliente
* `POST /api/auth/login` → login com JWT

---

### 📂 Categorias

* criar categoria
* listar categorias ativas
* buscar por ID
* atualizar
* ativar / desativar

---

### 🍔 Produtos

* cadastro de produtos
* associação com categoria
* ativação / desativação
* listagem com filtros e paginação

---

### 📦 Pedidos

* criação de pedidos (CUSTOMER e ADMIN)
* associação automática ao usuário autenticado
* cálculo de valor total
* controle de status
* isolamento por usuário (`/orders/me`)
* paginação + filtro por status

---

### 🔄 Status do Pedido

* RECEIVED
* IN_PREPARATION
* READY
* OUT_FOR_DELIVERY
* DELIVERED
* CANCELED

---

## 📊 Paginação

A API utiliza um padrão customizado de resposta:

```json
{
  "data": [...],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

---

## 🗄️ Banco de Dados

* PostgreSQL
* migrations versionadas com Flyway

Exemplo:

```
V1__create_categories_table.sql
V2__create_products_table.sql
V3__create_orders_table.sql
```

---

## ⚡ Performance

* `open-in-view: false`
* resolução de problema N+1 em pedidos
* carregamento otimizado de `OrderItem` em lote

---

## 🧪 Testes

### ✔️ Testes unitários

* services com Mockito

### ✔️ Testes de integração

* fluxo completo:

  * register → login → criar pedido → consultar `/orders/me`
* validação de segurança:

  * CUSTOMER vs ADMIN
  * endpoints protegidos

---

## 📄 Documentação da API

Após iniciar a aplicação:

* Swagger UI
  http://localhost:8080/swagger-ui/index.html

* OpenAPI JSON
  http://localhost:8080/api-docs

---

## ▶️ Como rodar o projeto

### 1. Clonar

```bash
git clone https://github.com/seu-usuario/jottasburger.git
cd jottasburger
```

---

### 2. Subir banco com Docker

```bash
docker run --name postgres-jottas \
  -e POSTGRES_DB=jottasburger \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres
```

---

### 3. Configurar `application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jottasburger
    username: postgres
    password: postgres
```

---

### 4. Rodar aplicação

```bash
./mvnw spring-boot:run
```

---

## 🧠 Decisões técnicas

* uso de **Flyway** ao invés de `ddl-auto`
* separação por módulos de negócio
* DTOs para entrada/saída
* `open-in-view: false`
* tratamento global de exceções
* autenticação stateless com JWT
* isolamento de dados por usuário
* paginação customizada
* solução manual para N+1 com carregamento em lote

---

## 📈 Próximos passos

* `GET /api/users/me`
* atualização de perfil
* integração com pagamento
* cache com Redis
* mensageria (eventos de pedido)
* observabilidade (logs estruturados)
* CI/CD
* Testcontainers

---

## 📬 Contato

Desenvolvido por **João (JFB)**
Projeto para fins de estudo e portfólio.
