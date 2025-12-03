
---

# 🏷️ Student Currency System 👨‍💻

> [!NOTE]
> **Sistema completo para reconhecimento do mérito estudantil via moeda virtual, distribuída por professores e trocada pelos alunos por vantagens em empresas parceiras.**  

---

## 📚 Índice
- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades Principais](#-funcionalidades-principais)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura)
  - [Exemplos de diagramas](#exemplos-de-diagramas)
- [Instalação e Execução](#-instalação-e-execução)
  - [Pré-requisitos](#pré-requisitos)
  - [Instalação de Dependências](#-instalação-de-dependências)
    - [Front-end (React)](#front-end-react)
    - [Back-end (Spring Boot)](#back-end-spring-boot)
  - [Como Executar a Aplicação](#-como-executar-a-aplicação)
    - [Terminal 1: Back-end (Spring Boot)](#terminal-1-back-end-spring-boot)
    - [Terminal 2: Front-end (React, Vite)](#terminal-2-front-end-react-vite)
- [Estrutura de Pastas](#-estrutura-de-pastas)
---

## 📝 Sobre o Projeto
O Sistema de Moeda Estudantil foi criado para valorizar o desempenho dos estudantes, tornando o reconhecimento do mérito visível e recompensador. Professores distribuem moedas virtuais como incentivo, e alunos trocam por benefícios diversos.

Principais cenários que o sistema atende:
Motivação de alunos através de recompensas tangíveis
Transparência no acompanhamento de méritos e recompensas
Facilidade operacional para professores e empresas
Parcerias inovadoras entre escolas, empresas e estudantes

---

## 📝 Funcionalidades Principais

- **Cadastro de Alunos:** Alunos informam nome, email, CPF, RG, endereço, instituição de ensino e curso. As instituições já estão pré-cadastradas.
- **Cadastro de Professores:** Professores são pré-cadastrados pela instituição, com nome, CPF e departamento.
- **Distribuição de Moedas:** Cada professor recebe 1.000 moedas por semestre (acumulativas) para distribuir aos alunos.
- **Notificações:** Alunos recebem notificação por email ao receber moedas.
- **Extrato:** Professores e alunos podem consultar o saldo e o histórico de transações.
- **Troca de Moedas:** Alunos podem trocar moedas por vantagens cadastradas no sistema (ex: descontos em restaurantes, mensalidades, materiais).
- **Cadastro de Empresas Parceiras:** Empresas podem se cadastrar e cadastrar vantagens, incluindo descrição e foto.
- **Resgate de Vantagens:** Ao resgatar uma vantagem, o aluno recebe um código por email para utilizar presencialmente, e a empresa parceira também é notificada.
- **Autenticação:** Alunos, professores e empresas precisam de login e senha para acessar o sistema.

---

## 🛠 Tecnologias Utilizadas

As seguintes ferramentas, frameworks e bibliotecas foram utilizados na construção deste projeto. Recomenda-se o uso das versões listadas (ou superiores) para garantir a compatibilidade.

- **Java 17+**
- **Spring Boot 3**
- **Spring Data JPA**
- **MySQL** (ou H2 para testes)
- **Lombok**
- **Maven**
- **Spring Security** (autenticação)
- **JavaMailSender** (para notificações por email)
- **VS Code** ou qualquer IDE Java

---

## 🏗 Arquitetura

Estruturado seguindo boas práticas de MVC e divisão em camadas:

- **Controller:** Endpoints REST para os diferentes perfis
- **Service:** Lógicas de negócio de cadastro, distribuição e resgate
- **Repository:** Acesso aos dados via JPA/Hibernate
- **Model/Entity:** Estruturas persistentes
- **Security:** Autenticação e autorização com Spring Security

### Exemplos de diagramas

Para melhor visualização e entendimento da estrutura do sistema, os diagramas principais estão organizados lado a lado.

**Diagrama de Classes**

<img src="https://raw.githubusercontent.com/MirellyAlvarenga/student-currency-system/main/docs/Diagrama%20de%20Classes.jpg" alt="Diagrama de Classes" width="250px">

**Diagrama de Componentes**

<img src="https://raw.githubusercontent.com/MirellyAlvarenga/student-currency-system/main/docs/Component%20Diagram%20Currency%20System.jpg" alt="Diagrama de Componentes" width="250px">

**Modelo de Dados (DER)**

<img src="https://raw.githubusercontent.com/MirellyAlvarenga/student-currency-system/main/docs/DER%20Sistema%20De%20Moeda.png" alt="Diagrama Entidade Relacionamento" width="250px">

**Casos de Uso**

<img src="https://raw.githubusercontent.com/MirellyAlvarenga/student-currency-system/main/docs/Diagrama%20de%20casos%20de%20uso.jpg" alt="Diagrama de Casos de Uso" width="250px">

---

## 🔧 Instalação e Execução

### Pré-requisitos
Certifique-se de que o usuário tenha o ambiente configurado.

* **Java JDK:** Versão **17** ou superior (Necessário para o **Back-end Spring Boot**)
* **Node.js:** Versão LTS (v18.x ou superior) (Necessário para o **Front-end em Next.js**)
* **Gerenciador de Pacotes:** npm ou yarn

---

### 📦 Instalação de Dependências

Clone o repositório e instale as dependências.

1.  **Clone o Repositório:**

```bash
git clone <https://github.com/MirellyAlvarenga/student-currency-system>
cd <student-currency-system>
```

2.  **Instale as Dependências:**

Como o projeto está dividido, você precisa instalar as dependências separadamente para o Front-end (React, usando NPM/Yarn) e garantir que o Back-end (Spring Boot, usando Maven/Gradle Wrapper) tenha suas dependências resolvidas.

#### Front-end (React)

Acesse a pasta do Front-end e instale as dependências do Node.js:

```bash
cd codigo
cd front-end
npm install
# ou
yarn install
cd .. # Retorna para a raiz
```

#### Back-end (Spring Boot)

O Spring Boot utiliza o **Maven Wrapper** (`./mvnw`) ou **Gradle Wrapper** (`./gradlew`) para gerenciar dependências. Execute o comando de instalação/build limpo antes de rodar.

* **Usando Maven (`pom.xml`):**
    ```bash
    cd codigo
    cd back-end
    ./mvnw clean install
    cd ..
    ```

* **Usando Gradle (`build.gradle`):**
    ```bash
    cd codigo
    cd back-end
    ./gradlew clean build
    cd ..
    ```

---

### ⚡ Como Executar a Aplicação
Execute a aplicação em modo de desenvolvimento em **dois terminais separados**.

#### Terminal 1: Back-end (Spring Boot)

Inicie a API do Spring Boot. Ela tentará se conectar ao banco de dados rodando no Docker.

```bash
cd codigo
cd back-end
./mvnw spring-boot:run
```
🚀 *O Back-end estará disponível em **http://localhost:8080**.*

---

#### Terminal 2: Front-end (React, Vite)

Inicie o servidor de desenvolvimento do Front-end.

```bash
cd codigo
cd front-end
npm run dev
# ou
yarn dev
```
🎨 *O Front-end estará disponível em **http://localhost:5173** (ou a porta configurada no Vite/CRA).*

---

## Abra no navegador:
   O Front-end deve estar acessível na porta configurada (Exemplo: <http://localhost:3000> ou <http://localhost:5173>)

## 📂 Estrutura de Pastas

Descreva o propósito das pastas principais.

```
.
├── .vscode/                     
├── README.md                  
├── codigo/ 
├── /frontend                   
│   ├── /public                  
│   ├── /src                  
│   │   ├── /components          
│   │   ├── /app             
│   │   ├── /hooks          
│   │   ├── /lib            
│   │   ├── /services
│   │   ├── /utils          
│
├── /backend                
│   │
│   ├── /src/main/java/student/currency/        
│   │       ├── /configuration      
│   │       ├── /controllers     
│   │       ├── /dtos     
│   │       ├── /exceptions  
│   │       ├── /mappers       
│   │       ├── /models          
│   │       ├── /repositories      
│   │       ├── /security      
│   │       └── /services   
│   │
│   ├── /src/main/resources     
│   │   ├── application.properties        
│   │      
│   └── pom.xml / build.gradle    
│
├── /docs                   
```
---

