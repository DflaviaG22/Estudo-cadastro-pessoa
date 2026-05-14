# Cadastro de Pessoas API

API REST para gerenciamento de cadastros de pessoas com integracao ViaCEP para validacao e preenchimento automatico de endereco.

## Tecnologias

- Java 17
- Spring Boot 4.0.6
- Spring Data JPA
- PostgreSQL
- OpenFeign para integracao ViaCEP
- SpringDoc OpenAPI para Swagger UI

## Funcionalidades

- Cadastrar pessoas usando CPF como identificador
- Listar todos os cadastros
- Atualizar cadastro por CPF
- Deletar cadastro por CPF
- Buscar endereco automaticamente pelo CEP via ViaCEP
- Retornar endereco em formato de lista
- Formatar CPF e telefone automaticamente
- Retornar `409 Conflict` quando o CPF ja estiver cadastrado

## Configuracao

### Pre-requisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### application.yaml

Configure o arquivo `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgres
    username: postgres
    password: "123456"
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Executar

```bash
mvn spring-boot:run
```

A aplicacao fica disponivel em:

```text
http://localhost:8080
```

## Documentacao da API

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

As anotacoes OpenAPI dos endpoints ficam separadas em `CadastroControllerDocs`, mantendo o `CadastroController` focado apenas nas rotas.

## Endpoints

### Criar cadastro

`POST /cadastro`

Cria um novo cadastro. Envie o `cep`; os demais dados de endereco sao preenchidos automaticamente pelo ViaCEP.

Request:

```json
{
  "cpf": "98765432100",
  "nomeCompleto": "Maria Oliveira Santos",
  "telefone": "21977776666",
  "email": "maria.santos@email.com",
  "cep": "30140071"
}
```

Response `200 OK`:

```json
{
  "nomeCompleto": "Maria Oliveira Santos",
  "cpf": "987.654.321-00",
  "email": "maria.santos@email.com",
  "telefone": "(21) 97777-6666",
  "endereco": [
    {
      "cep": "30140-071",
      "logradouro": "Avenida Afonso Pena",
      "complemento": "",
      "uf": "MG",
      "estado": "Minas Gerais",
      "bairro": "Centro",
      "ddd": "31"
    }
  ]
}
```

Response `409 Conflict`, quando o CPF ja existe:

```json
{
  "status": 409,
  "mensagem": "Cpf ja cadastrado",
  "timestamp": "2026-05-14T20:00:00"
}
```

### Listar cadastros

`GET /cadastro`

Response `200 OK`:

```json
[
  {
    "nomeCompleto": "Maria Oliveira Santos",
    "cpf": "987.654.321-00",
    "email": "maria.santos@email.com",
    "telefone": "(21) 97777-6666",
    "endereco": [
      {
        "cep": "30140-071",
        "logradouro": "Avenida Afonso Pena",
        "complemento": "",
        "uf": "MG",
        "estado": "Minas Gerais",
        "bairro": "Centro",
        "ddd": "31"
      }
    ]
  }
]
```

### Atualizar cadastro por CPF

`PUT /cadastro/{cpf}`

Atualiza um cadastro existente usando CPF como identificador. O CPF no path pode ser enviado com ou sem mascara.

Request:

```json
{
  "cpf": "98765432100",
  "nomeCompleto": "Maria Oliveira Santos",
  "telefone": "21977776666",
  "email": "maria.santos@email.com",
  "cep": "20040020"
}
```

Response `200 OK`:

```json
{
  "nomeCompleto": "Maria Oliveira Santos",
  "cpf": "987.654.321-00",
  "email": "maria.santos@email.com",
  "telefone": "(21) 97777-6666",
  "endereco": [
    {
      "cep": "20040-020",
      "logradouro": "Avenida Rio Branco",
      "complemento": "",
      "uf": "RJ",
      "estado": "Rio de Janeiro",
      "bairro": "Centro",
      "ddd": "21"
    }
  ]
}
```

Response `404 Not Found`: quando o CPF nao existe.

### Deletar cadastro por CPF

`DELETE /cadastro/{cpf}`

Remove um cadastro usando CPF como identificador.

Response `204 No Content`: cadastro deletado.

Response `404 Not Found`: CPF nao encontrado.

## cURL

### Criar cadastro

```bash
curl -X POST "http://localhost:8080/cadastro" \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "98765432100",
    "nomeCompleto": "Maria Oliveira Santos",
    "telefone": "21977776666",
    "email": "maria.santos@email.com",
    "cep": "30140071"
  }'
```

### Atualizar por CPF

```bash
curl -X PUT "http://localhost:8080/cadastro/98765432100" \
  -H "Content-Type: application/json" \
  -d '{
    "cpf": "98765432100",
    "nomeCompleto": "Maria Oliveira Santos",
    "telefone": "21977776666",
    "email": "maria.santos@email.com",
    "cep": "20040020"
  }'
```

### Listar todos

```bash
curl -X GET "http://localhost:8080/cadastro"
```

### Deletar por CPF

```bash
curl -X DELETE "http://localhost:8080/cadastro/98765432100"
```

## Validacoes e regras

- CPF deve conter 11 digitos e e usado como chave primaria.
- CPF e telefone sao formatados automaticamente.
- CEP deve conter 8 digitos.
- O endereco e consultado no ViaCEP e retornado em `endereco: []`.
- Ao cadastrar CPF ja existente, a API retorna `409 Conflict`.

## Estrutura principal

```text
src/main/java/estudo/pessoa/cadastro/
├── CadastroApplication.java
├── client/
│   └── ViaCepClient.java
├── config/
│   ├── CadastroControllerAdvice.java
│   ├── JacksonConfig.java
│   └── OpenApiConfig.java
├── controller/
│   ├── CadastroController.java
│   └── docs/
│       └── CadastroControllerDocs.java
├── dto/
│   ├── CadastroPessoaResponse.java
│   ├── EnderecoResponse.java
│   ├── EnderecoViaCepResponse.java
│   └── ErrorResponse.java
├── entity/
│   └── CadastroPessoa.java
├── exception/
├── repository/
│   └── CadastroRepository.java
├── service/
│   └── CadastroService.java
└── utils/
    ├── FormatacaoCampo.java
    └── ValidadorCpf.java
```

## Observacao sobre banco existente

Como o CPF passou a ser a chave primaria, bancos criados com o modelo antigo podem ter registros duplicados. Em ambiente de estudo, o caminho mais simples e recriar a tabela `cadastro_pessoa`.
