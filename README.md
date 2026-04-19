# 🍔 Jottas Burger API

Uma **API Rest moderna** para gerenciamento de uma hamburgueria, desenvolvida como MVP (Minimum Viable Product) e portfólio.

O projeto foi construído com as melhores práticas do mercado Java/Spring Boot em 2026, utilizando arquitetura em camadas por domínio (Package by Feature).

---

## ✨ Funcionalidades Atuais

- **Clientes** (CRUD completo + Soft Delete)
- **Produtos** (CRUD completo + Soft Delete + imagem)
- **Pedidos** (CRUD + alteração de status + cálculo automático de total)
- Validações avançadas com Bean Validation
- Tratamento global de exceções
- Soft Delete em todas as entidades
- Paginação nas listagens
- Logs estruturados
- Documentação interativa com Swagger

---

## 🛠 Tecnologias Utilizadas

| Tecnologia              | Versão | Propósito                          |
|------------------------|--------|------------------------------------|
| **Java**               | 21     | Linguagem principal                |
| **Spring Boot**        | 3.5.13 | Framework principal                |
| **Spring Data JPA**    | -      | Persistência                       |
| **Hibernate**          | -      | ORM                                |
| **PostgreSQL**         | 16     | Banco de dados                     |
| **Flyway**             | -      | Migrações de banco                 |
| **Lombok**             | -      | Redução de boilerplate             |
| **MapStruct**          | -      | Mapeamento de DTOs (em breve)     |
| **Springdoc OpenAPI**  | 3.0.2  | Documentação Swagger               |
| **Docker**             | -      | Containerização                    |
| **Maven**              | -      | Gerenciador de dependências        |

---
**Futuro / Em planejamento:**
- Spring Security + JWT
- Autenticação e Autorização
- Testes unitários e de integração (JUnit 5 + Testcontainers)
- Cache (Redis)
- CI/CD
- Deploy na nuvem (Railway ou Render)

---

## 🏗 Arquitetura

- **Package by Feature** (organização por domínio)
- Camadas: `Controller → Service → Repository → Model`
- DTOs de entrada e saída separados das entidades
- Soft Delete em todas as entidades principais
- Tratamento centralizado de exceções
- Uso de Records nos DTOs de resposta

```plaintext
com.jottas.burger
├── JottasBurgerApplication.java
│
├── core/
│   ├── config/
│   ├── exception/
│   ├── security/
│   └── util/
│
└── domain/
    ├── product/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── dto/
    │   ├── model/
    │   └── mapper/
    │
    ├── order/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── dto/
    │   ├── model/
    │   └── mapper/
    │
    ├── customer/
    │   ├── controller/
    │   ├── service/
    │   ├── repository/
    │   ├── dto/
    │   ├── model/
    │   └── mapper/
    │
    └── user/
        ├── controller/
        ├── service/
        ├── repository/
        ├── dto/
        ├── model/
        └── mapper/
```


---

## **Como Executar o Projeto**

### **Pré-requisitos**

- **Java 21**
- **Maven**
- **Docker** (opcional, para rodar o PostgreSQL em um contêiner)
- **PostgreSQL** (se não estiver usando Docker)

### **Passos para Execução**

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/seu-usuario/jottas-burguer.git
   cd jottas-burguer

Se estiver usando Docker, inicie o contêiner do PostgreSQL:

bash
Copy
docker-compose up -d

### **Acesse a API**
A API estará disponível em http://localhost:8080.

Acesse a documentação da API (Swagger UI) em: http://localhost:8080/swagger-ui/index.html
Acesse a documentação da API api-docs em: http://localhost:8080/api-docs