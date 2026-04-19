# 🍔 JottasBurger API

Backend API para gestão de uma hamburgueria, desenvolvida com **Java 21** e **Spring Boot**, com foco em boas práticas de mercado, arquitetura limpa e evolução incremental.

---

## 📌 Objetivo

Este projeto foi desenvolvido como **portfólio profissional**, simulando um sistema real de uma hamburgueria, incluindo:

* Catálogo de produtos
* Gestão de pedidos
* Regras de negócio
* Estrutura escalável e organizada

A proposta é evoluir o sistema progressivamente, aplicando conceitos usados no mercado.

---

## 🧱 Arquitetura

O projeto segue uma abordagem de **monólito modular**, organizado por contexto de negócio:

```
com.jfb.jottasburger
├── category
├── product
├── order
├── common
├── config
└── exception
```

Cada módulo contém:

* `controller` → entrada da API
* `service` → regras de negócio
* `repository` → acesso a dados
* `dto` → contratos da API
* `model` → entidades JPA

---

## ⚙️ Tecnologias

* Java 21
* Spring Boot 3.5
* Spring Web
* Spring Data JPA
* Spring Security
* PostgreSQL
* Flyway (versionamento de banco)
* Spring Validation
* SpringDoc OpenAPI (Swagger)
* Lombok
* Testcontainers
* Docker (planejado)

---

## 🚀 Funcionalidades (MVP)

### 📂 Categorias

* Criar categoria
* Listar categorias ativas
* Buscar por ID
* Atualizar nome
* Ativar / desativar

### 🍔 Produtos *(em desenvolvimento)*

* Cadastro de produtos
* Associação com categoria
* Ativação / desativação
* Listagem de produtos ativos

### 📦 Pedidos *(planejado)*

* Criação de pedido
* Itens de pedido
* Cálculo de total
* Controle de status

---

## 🔄 Status do Pedido *(planejado)*

* RECEIVED
* IN_PREPARATION
* READY
* OUT_FOR_DELIVERY
* DELIVERED
* CANCELED

---

## 🗄️ Banco de Dados

* PostgreSQL
* Controle de schema com Flyway
* Migrations versionadas

Exemplo:

```
V1__create_categories_table.sql
```

---

## 📄 Documentação da API

Após iniciar a aplicação:

* Swagger UI:
  http://localhost:8080/swagger-ui.html

* OpenAPI JSON:
  http://localhost:8080/api-docs

---

## ▶️ Como rodar o projeto

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/jottasburger.git
cd jottasburger
```

### 2. Subir o PostgreSQL

Exemplo com Docker:

```bash
docker run --name postgres-jottas \
  -e POSTGRES_DB=jottasburger \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres
```

### 3. Configurar `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/jottasburger
    username: postgres
    password: postgres
```

### 4. Rodar a aplicação

```bash
./mvnw spring-boot:run
```

---

## 🧪 Testes

* Testes unitários
* Testes de integração com Testcontainers *(planejado)*

---

## 🔐 Segurança

Atualmente:

* endpoints liberados para desenvolvimento

Planejado:

* autenticação com JWT
* controle de acesso por perfil (ADMIN / CUSTOMER)

---

## 📈 Evolução futura

* Autenticação com JWT
* Carrinho de compras
* Cupons de desconto
* Taxa de entrega por região
* Integração com pagamento
* Cache com Redis
* Mensageria (eventos de pedido)
* Observabilidade avançada
* CI/CD

---

## 💡 Decisões técnicas

* Uso de **Flyway** ao invés de `ddl-auto`
* Separação por módulos de negócio
* DTOs para entrada/saída (não expor entidades)
* `open-in-view: false`
* Tratamento global de exceções

---

## 📬 Contato

Desenvolvido por **João (JFB)**
Projeto para fins de estudo e portfólio.

---
