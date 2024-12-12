# Henkan - Backend

## Índice

- [Introdução](#introdução)
- [Requisitos](#requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Execução da Aplicação](#execução-da-aplicação)
- [Rotas Disponíveis](#rotas-disponíveis)
- [Modelos](#modelos)

## Introdução

Este documento fornece uma visão geral da configuração do backend do Henkan, incluindo instruções de instalação, configuração e uso das APIs disponíveis.

## Requisitos

- **Node.js** v14 ou superior
- **NPM** v6 ou superior
- **MySQL**
- **Docker** (opcional, para ambiente containerizado)

## Instalação e Configuração

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/desenvolvimento-web-unifil/compras-compartilhada-henkan.git
   cd compras-compartilhada-henkan/backend
   ```

2. **Instale as dependências:**

   ```bash
   npm install
   ```

3. **Configure o arquivo `.env`:**

   Crie um arquivo `.env` na raiz do diretório `backend` com o seguinte conteúdo:

   ```env
   DATABASE_URL=mysql://usuario:senha@host:porta/henkan

   DB_NAME=henkan
   DB_USER=usuario
   DB_PASSWORD=senha
   DB_HOST=localhost
   DB_PORT=3306
   DB_DIALECT=mysql
   ```

   Substitua `usuario`, `senha` e `host` pelas credenciais do seu banco de dados.

## Execução da Aplicação

Para iniciar o servidor:

```bash
npm start
```

O servidor estará rodando na porta `3000` por padrão.

### Utilizando Docker

1. **Certifique-se de que o Docker e o Docker Compose estão instalados.**

2. **Construa e inicie os contêineres:**

   ```bash
   docker-compose up --build
   ```

   Isso iniciará os contêineres do backend e do banco de dados.

## Rotas Disponíveis

### Usuário

- **Criar Usuário**

  ```
  POST /user
  ```

  **Parâmetros:**

  - `name` (string): Nome do usuário.
  - `mac` (string): Endereço MAC do usuário.

- **Obter Usuário por ID**

  ```
  GET /user/:ID
  ```

- **Atualizar Usuário**

  ```
  PUT /user/:ID
  ```

  **Parâmetros:**

  - `name` (string): Novo nome do usuário (opcional).

### Lista

- **Criar Lista**

  ```
  POST /list
  ```

- **Obter Lista por ID**

  ```
  GET /list/:ID
  ```

- **Atualizar Lista**

  ```
  PUT /list/:ID
  ```

### Associação Usuário-Lista

- **Obter Associações**

  ```
  GET /user-list
  ```

- **Obter Listas por ID do Usuário**

  ```
  GET /user-list/:user_id
  ```

### Solicitação de Lista

- **Criar Solicitação**

  ```
  POST /list-request
  ```

- **Obter Solicitação por ID**

  ```
  GET /list-request/:ID
  ```

- **Atualizar Situação da Solicitação**

  ```
  PUT /list-request/:ID
  ```

- **Deletar Solicitação**

  ```
  DELETE /list-request/:ID
  ```

## Modelos

### Usuário (`User`)

- `id_user` (UUID): Identificador único.
- `name` (string): Nome do usuário.
- `mac` (string): Endereço MAC único.

### Lista (`List`)

- `id_list` (UUID): Identificador único.
- `name` (string): Nome da lista.
- `id_owner` (UUID): ID do proprietário.

### Produto (`Product`)

- `id_product` (UUID): Identificador único.
- `name` (string): Nome do produto.
- `quantity` (inteiro): Quantidade.
- `price` (float): Preço.
- `user_id_user` (UUID): ID do usuário que adicionou.
- `list_id_list` (UUID): ID da lista associada.

### Associação Usuário-Lista (`User_has_List`)

- `id_user_lists` (UUID): Identificador único.
- `user_id_user` (UUID): ID do usuário.
- `list_id_list` (UUID): ID da lista.

### Solicitação de Lista (`List_Request`)

- `id_lists_requests` (UUID): Identificador único.
- `situation` (char): Situação da solicitação ('P' para pendente).
- `user_id_user` (UUID): ID do usuário solicitante.
- `list_id_list` (UUID): ID da lista.

