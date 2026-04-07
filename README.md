# 📒 Agenda de Contatos — API REST com Spring Boot

> CRUD completo de contatos com Spring Boot 3, PostgreSQL, Swagger e Bean Validation.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apachemaven)
![Status](https://img.shields.io/badge/status-concluído-brightgreen)

---

## Sobre o Projeto

API REST para gerenciamento de uma agenda de contatos. O projeto é uma refatoração de um CRUD original em C# ASP.NET MVC, reescrito em Java com Spring Boot seguindo boas práticas de mercado: separação em camadas, DTOs, validações e tratamento centralizado de erros.

**Projeto original (C# MVC):** [Projeto-MVC--Crud-completo-](https://github.com/RodrigoPresida/Projeto-MVC--Crud-completo-)

---

## Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/contatos` | Lista todos os contatos |
| `GET` | `/api/contatos/ativos` | Lista apenas contatos ativos |
| `GET` | `/api/contatos/{id}` | Busca contato por ID |
| `GET` | `/api/contatos/buscar?nome=` | Busca por nome (parcial) |
| `GET` | `/api/contatos/categoria/{categoria}` | Filtra por categoria |
| `POST` | `/api/contatos` | Cria um novo contato |
| `PUT` | `/api/contatos/{id}` | Atualiza um contato |
| `DELETE` | `/api/contatos/{id}` | Remove um contato |

**Documentação interativa:** `http://localhost:8080/swagger-ui.html`

---

## Modelo de Dados

```json
{
  "nome": "João Silva",
  "telefone": "(11) 99999-9999",
  "email": "joao@email.com",
  "categoria": "TRABALHO",
  "ativo": true
}
```

**Categorias disponíveis:** `PESSOAL` · `TRABALHO` · `FAMILIA` · `OUTRO`

---

## Validações

| Campo | Regra |
|-------|-------|
| `nome` | Obrigatório |
| `telefone` | Obrigatório · formato `(99) 99999-9999` |
| `email` | Opcional · deve ser e-mail válido |
| `categoria` | Obrigatória · deve ser um valor do enum |

Erros de validação retornam `400 Bad Request` com detalhes por campo.

---

## Stack

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem |
| Spring Boot | 3.5 | Framework principal |
| Spring Data JPA | 3.5 | Persistência |
| PostgreSQL | 16 | Banco de dados |
| Hibernate | 6.x | ORM |
| Bean Validation | 3.x | Validações |
| SpringDoc OpenAPI | 2.x | Swagger / documentação |
| Lombok | latest | Redução de boilerplate |
| Maven | 3.x | Build |

---

## Arquitetura

```
Controller (HTTP)
    ↓
Service (regras de negócio)
    ↓
Repository (JPA / banco)
    ↓
PostgreSQL
```

```
src/main/java/com/rodrigopresida/agenda/
├── config/
│   └── SwaggerConfig.java
├── controller/
│   └── ContatoController.java
├── dto/
│   ├── ContatoRequestDTO.java
│   └── ContatoResponseDTO.java
├── exception/
│   ├── ApiErrorResponse.java
│   ├── ContatoNotFoundException.java
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Categoria.java
│   └── Contato.java
├── repository/
│   └── ContatoRepository.java
├── service/
│   └── ContatoService.java
└── AgendaSpringBootApplication.java
```

---

## Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.x
- PostgreSQL 16+

### Configuração do banco

```sql
CREATE DATABASE agenda_db;
```

### Configuração da aplicação

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/agenda_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Executar

```bash
git clone https://github.com/RodrigoPresida/agenda-spring-boot.git
cd agenda-spring-boot
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`  
Swagger UI em `http://localhost:8080/swagger-ui.html`

---

## Autor

**Rodrigo Cruz dos Santos** — Analista de Dados

[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?logo=linkedin)](https://linkedin.com/in/rodrigopresidati)
[![GitHub](https://img.shields.io/badge/GitHub-black?logo=github)](https://github.com/RodrigoPresida)
