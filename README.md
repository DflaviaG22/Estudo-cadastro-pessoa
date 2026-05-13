# Cadastro de Pessoas API

API REST para gerenciamento de cadastros de pessoas com integração ViaCEP para validação e preenchimento automático de endereços.

## Tecnologias

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Data JPA**
- **PostgreSQL**
- **OpenFeign** (integração ViaCEP)
- **SpringDoc OpenAPI** (Swagger UI)

## Funcionalidades

- ✅ Cadastrar pessoas com validação de CPF
- ✅ Listar todos os cadastros
- ✅ Atualizar cadastro por CPF
- ✅ Deletar cadastro por CPF
- ✅ Integração automática com ViaCEP para validação e preenchimento de endereço
- ✅ Formatação automática de CPF e telefone

## Configuração

### Pré-requisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Variáveis de Ambiente

Configure o arquivo `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cadastro_pessoas
    username: seu_usuario
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Instalação e Execução

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

### Swagger UI

A documentação interativa da API está disponível em:

```
http://localhost:8080/swagger-ui.html
```

### OpenAPI JSON

O schema OpenAPI em JSON:

```
http://localhost:8080/v3/api-docs
```

## Endpoints

### 1. Criar Cadastro

**POST** `/cadastro`

Cria um novo cadastro de pessoa com validação de CPF e CEP.

**Request Body:**
```json
{
  "nomeCompleto": "João da Silva",
  "email": "joao@example.com",
  "cpf": "123.456.789-00",
  "telefone": "(11) 98765-4321",
  "cep": "01310-100"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nomeCompleto": "João da Silva",
  "email": "joao@example.com",
  "cpf": "123.456.789-00",
  "telefone": "(11) 98765-4321",
  "cep": "01310-100",
  "logradouro": "Avenida Paulista",
  "complemento": "",
  "bairro": "Cerqueira César",
  "uf": "SP",
  "estado": "São Paulo",
  "ddd": "11"
}
```

---

### 2. Listar Todos os Cadastros

**GET** `/cadastro`

Retorna uma lista com todos os cadastros de pessoas.

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nomeCompleto": "João da Silva",
    "email": "joao@example.com",
    "cpf": "123.456.789-00",
    "telefone": "(11) 98765-4321",
    "cep": "01310-100",
    "logradouro": "Avenida Paulista",
    "complemento": "",
    "bairro": "Cerqueira César",
    "uf": "SP",
    "estado": "São Paulo",
    "ddd": "11"
  }
]
```

---

### 3. Atualizar Cadastro por CPF

**PUT** `/cadastro/{cpf}`

Atualiza os dados de um cadastro existente usando o CPF como identificador.

**Path Parameters:**
- `cpf` (string) - CPF da pessoa (com ou sem formatação)

**Request Body:**
```json
{
  "nomeCompleto": "João da Silva Santos",
  "email": "joao.santos@example.com",
  "cpf": "123.456.789-00",
  "telefone": "(11) 99876-5432",
  "cep": "01310-100"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "nomeCompleto": "João da Silva Santos",
  "email": "joao.santos@example.com",
  "cpf": "123.456.789-00",
  "telefone": "(11) 99876-5432",
  "cep": "01310-100",
  "logradouro": "Avenida Paulista",
  "complemento": "",
  "bairro": "Cerqueira César",
  "uf": "SP",
  "estado": "São Paulo",
  "ddd": "11"
}
```

**Response (404 Not Found):**
Quando o CPF não existe no banco de dados.

---

### 4. Deletar Cadastro por CPF

**DELETE** `/cadastro/{cpf}`

Remove um cadastro de pessoa usando o CPF como identificador.

**Path Parameters:**
- `cpf` (string) - CPF da pessoa (com ou sem formatação)

**Response (204 No Content):**
Cadastro deletado com sucesso.

**Response (404 Not Found):**
Quando o CPF não existe no banco de dados.

---

## Validações

### CPF

- Deve ser válido (algoritmo de validação)
- Será formatado automaticamente como `XXX.XXX.XXX-XX`

### CEP

- Deve conter 8 dígitos
- Será validado contra a API ViaCEP
- Se inválido, retorna erro 400 Bad Request

### Email

- Deve ser um email válido

### Telefone

- Será formatado automaticamente como `(XX) XXXXX-XXXX`

## Tratamento de Erros

### 400 Bad Request

Retornado quando:
- CEP inválido
- CPF inválido
- Dados obrigatórios ausentes

```json
{
  "timestamp": "2024-05-12T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "CEP invalido"
}
```

### 404 Not Found

Retornado quando o recurso não é encontrado.

```json
{
  "timestamp": "2024-05-12T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Cadastro não encontrado"
}
```

### 500 Internal Server Error

Erros não previstos no processamento.

---

## Exemplos de Uso

### cURL

#### Criar cadastro
```bash
curl -X POST http://localhost:8080/cadastro \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Maria Silva",
    "email": "maria@example.com",
    "cpf": "987.654.321-00",
    "telefone": "(21) 91234-5678",
    "cep": "20040020"
  }'
```

#### Listar todos
```bash
curl -X GET http://localhost:8080/cadastro
```

#### Atualizar por CPF
```bash
curl -X PUT http://localhost:8080/cadastro/987.654.321-00 \
  -H "Content-Type: application/json" \
  -d '{
    "nomeCompleto": "Maria Silva Santos",
    "email": "maria.santos@example.com",
    "cpf": "987.654.321-00",
    "telefone": "(21) 91234-5679",
    "cep": "20040020"
  }'
```

#### Deletar por CPF
```bash
curl -X DELETE http://localhost:8080/cadastro/987.654.321-00
```

---

## Estrutura do Projeto

```
src/main/java/estudo/pessoa/cadastro/
├── CadastroApplication.java          # Classe principal da aplicação
├── controller/
│   └── CadastroController.java       # Endpoints da API
├── service/
│   └── CadastroService.java          # Lógica de negócio
├── repository/
│   └── CadastroRepository.java       # Acesso aos dados
├── entity/
│   └── CadastroPessoa.java           # Modelo de dados
├── client/
│   └── ViaCepClient.java             # Cliente HTTP para ViaCEP
├── dto/
│   └── EnderecoViaCepResponse.java   # DTO para resposta ViaCEP
└── utils/
    ├── FormatacaoCampo.java          # Utilitários de formatação
    └── ValidadorCpf.java             # Validação de CPF
```

---

## Licença

Este projeto está sob a licença MIT.

---

## Suporte

Para dúvidas ou issues, abra uma issue no repositório.
