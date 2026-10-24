# FitManager

O FitManager é um projeto acadêmico de gestão de academia, desenvolvido com foco no estudo e na aplicação de React. O frontend utiliza Vite, e o backend fornece a API REST em Spring Boot.

## Pré-requisitos

- Node.js 20.19 ou superior (ou 22.12 ou superior) e npm para executar o frontend localmente.
- Java 21 ou superior para executar o backend localmente. O projeto inclui Maven Wrapper, então não é necessário instalar Maven separadamente.
- Docker Desktop com Docker Compose para executar os dois serviços em containers.

## Como executar

### 1. Somente o frontend

Na pasta `frontend`, instale as dependências e inicie o Vite:

```powershell
cd frontend
npm ci
npm run dev
```

O frontend ficará disponível em `http://localhost:5173`. Sem o backend, as funcionalidades que dependem da API, como login e carregamento dos dados, não funcionarão corretamente.

### 2. Frontend e backend localmente

Inicie cada aplicação em um terminal separado. No primeiro, execute o backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

No macOS ou Linux, use `./mvnw spring-boot:run` no lugar de `mvnw.cmd`.

No segundo terminal, inicie o frontend:

```powershell
cd frontend
npm ci
npm run dev
```

O frontend usa o proxy do Vite para encaminhar as chamadas `/api` ao backend em `http://localhost:8080`.

### 3. Docker Compose

Na raiz do projeto, execute:

```sh
docker compose up --build
```

O Compose inicia o backend e o frontend e configura o proxy do Vite para encaminhar as chamadas à API entre os containers. Acesse `http://localhost:5173`. Para parar os serviços, use `Ctrl+C` ou execute `docker compose down` em outro terminal.

## Contexto acadêmico e dados

O foco principal deste projeto acadêmico é o desenvolvimento do frontend com React e Vite. O backend existe para fornecer dados e permitir a demonstração da comunicação entre frontend e API.

O banco utilizado é o H2 em memória, configurado somente para demonstração. Os dados de exemplo são carregados pelo backend e não são persistidos após seu encerramento.

### Login de demonstração

A tela de login aceita qualquer login e senha não vazios; não há validação de credenciais de usuários. O login existe somente para validar o fluxo de rotas públicas e autenticadas. Não use esse comportamento como autenticação real em produção.

## Estrutura

- `frontend/`: aplicação web em React, executada e compilada com Vite.
- `backend/`: API REST em Spring Boot.