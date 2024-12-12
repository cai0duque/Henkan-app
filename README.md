# Projeto HENKAN - Lista Compartilhada

## Descrição

O **HENKAN** é um aplicativo de **listas compartilhadas**. O projeto é composto por uma API RESTful construída com **Node.js** e **Express**, que se comunica com um banco de dados **MySQL**, além de um aplicativo móvel desenvolvido em **Kotlin** para **Android**. A solução também utiliza **Docker** para orquestração e gerenciamento dos contêineres de aplicação e banco de dados.

## Tecnologias Utilizadas

- **Backend**:
  - **Node.js** com **Express** para construção da API RESTful
  - **Sequelize** como ORM para integração com o banco de dados
  - **MySQL** como Sistema de Gerenciamento de Banco de Dados (SGBD)
  - **Docker** e **Docker Compose** para gerenciamento de contêineres

- **Frontend**:
  - **Kotlin** para o desenvolvimento do aplicativo Android

- **Banco de Dados**:
  - **MySQL** versão 8.0.32, gerenciado via Docker

## Funcionalidades

- **Gestão de Listas**: Os usuários podem criar, atualizar, listar e excluir suas listas.
- **Compartilhamento de Listas**: As listas podem ser compartilhadas entre usuários, permitindo a colaboração e a troca de informações.
- **Conexão Mobile e Backend**: O aplicativo Android consome os endpoints da API para gerenciar os dados das listas.

## Bibliotecas e Dependências

### Backend (API)

- **@sequelize/mysql**: `^7.0.0-alpha.43`
- **axios**: `^1.7.7`
- **express**: `^4.21.1`
- **mysql2**: `^3.11.4`
- **sequelize**: `^6.37.5`

### Banco de Dados

- **MySQL**: `8.0.32`

### Frontend (Aplicativo Android)

- **Kotlin**: Utilizado para o desenvolvimento do aplicativo Android.


## Endpoints da API

- **POST /user**: Cria um novo usuário.
- **GET /user/{ID}**: Recupera um usuário pelo ID.
- **POST /list**: Cria uma nova lista.
- **GET /list/{ID}**: Recupera uma lista pelo ID.
- **PUT /list/{ID}**: Atualiza uma lista existente.
- **DELETE /list/{ID}**: Deleta uma lista.
