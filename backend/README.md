# Sistema de Gestão de Crises Corporativas - Backend

Backend REST desenvolvido com **Java 17**, **Quarkus**, **PostgreSQL** e arquitetura MVC em camadas.

## 📋 Requisitos

- Java 17 ou superior
- PostgreSQL 12+
- Maven 3.8+

## 🚀 Instalação e Configuração

### 1. Clonar o repositório

```bash
cd /workspaces/Sistema-de-Gestao-de-Crises/backend
```

### 2. Criar banco de dados PostgreSQL

```sql
CREATE DATABASE gestaocrise_db;
```

### 3. Configurar conexão (se necessário)

Edite `src/main/resources/application.properties`:

```properties
quarkus.datasource.username=seu_usuario
quarkus.datasource.password=sua_senha
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/gestaocrise_db
```

### 4. Instalar dependências e compilar

```bash
mvn clean install
```

## 🎯 Executar a Aplicação

### Modo Desenvolvimento

```bash
mvn quarkus:dev
```

A aplicação estará disponível em: `http://localhost:8080`

### Modo Produção (build native)

```bash
mvn clean package -Pnative
./target/sistema-gestao-crises-1.0.0-runner
```

## 🔐 Autenticação e Credenciais Padrão

O sistema cria automaticamente os seguintes usuários na inicialização:

| Email | Senha | Perfil |
|-------|-------|--------|
| admin@empresa.com | admin123 | ADMIN |
| gerente@empresa.com | gerente123 | GERENTE |
| analista@empresa.com | analista123 | ANALISTA |
| visualizador@empresa.com | vis123 | VISUALIZADOR |

### Obter Token JWT

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@empresa.com","senha":"admin123"}'
```

Resposta:
```json
{
  "status": 200,
  "mensagem": "Login realizado com sucesso",
  "dados": {
    "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tipoToken": "Bearer",
    "usuario": {...}
  }
}
```

Use o token em requisições:
```bash
curl -H "Authorization: Bearer SEU_TOKEN" http://localhost:8080/api/crises
```

## 📚 Arquitetura e Camadas

```
com.gestaocrise
├── entity/         → Entidades JPA (mapeamento do banco)
├── dao/            → Data Access Objects (consultas e persistência)
├── bo/             → Business Objects (lógica de negócio)
├── dto/            → Data Transfer Objects (contrato com frontend)
├── resource/       → Endpoints REST (Controllers)
├── security/       → JWT e autenticação
├── audit/          → Filtro de auditoria
└── exception/      → Tratamento de exceções
```

**Regra crítica**: DTOs sempre entre cliente e servidor, nunca Entities.

## 🔌 Endpoints REST

### Autenticação

- **POST** `/api/auth/login` - Login (sem autenticação)
- **POST** `/api/auth/logout` - Logout

### Usuários (ADMIN)

- **GET** `/api/usuarios` - Listar todos
- **GET** `/api/usuarios/{id}` - Buscar por ID
- **POST** `/api/usuarios` - Criar
- **PUT** `/api/usuarios/{id}` - Atualizar
- **DELETE** `/api/usuarios/{id}` - Desativar

### Crises

- **GET** `/api/crises` - Listar todas (VISUALIZADOR+)
- **GET** `/api/crises/{id}` - Buscar por ID (VISUALIZADOR+)
- **POST** `/api/crises` - Criar (GERENTE+)
- **PUT** `/api/crises/{id}` - Atualizar (GERENTE+)
- **PATCH** `/api/crises/{id}/status?novoStatus=EM_ANDAMENTO` - Alterar status (GERENTE+)
- **DELETE** `/api/crises/{id}` - Deletar (ADMIN)

### Ações de Crise

- **GET** `/api/crises/{criseId}/acoes` - Listar ações (ANALISTA+)
- **GET** `/api/crises/{criseId}/acoes/{id}` - Buscar ação (ANALISTA+)
- **POST** `/api/crises/{criseId}/acoes` - Registrar ação (ANALISTA+)

### Relatórios

- **GET** `/api/relatorios` - Listar todos (GERENTE+)
- **GET** `/api/relatorios/{id}` - Buscar por ID (GERENTE+)
- **GET** `/api/relatorios/crise/{criseId}` - Listar por crise (GERENTE+)
- **POST** `/api/relatorios` - Gerar relatório (GERENTE+)

### Auditoria (ADMIN, GERENTE)

- **GET** `/api/auditoria` - Listar todos os logs
- **GET** `/api/auditoria/usuario/{email}` - Buscar por usuário
- **GET** `/api/auditoria/endpoint?endpoint=/api/crises` - Buscar por endpoint

## ✅ Testes

### Executar todos os testes

```bash
mvn test
```

### Executar teste específico

```bash
mvn test -Dtest=UsuarioDAOTest
mvn test -Dtest=AuthResourceTest
mvn test -Dtest=ControleAcessoTest
```

### Testes Implementados

1. **ConexaoBancoTest** - Valida conexão com PostgreSQL
2. **UsuarioDAOTest** - Testa CRUD de usuários
3. **AuthResourceTest** - Testa autenticação JWT
4. **AuditoriaTest** - Valida registro automático de logs
5. **ControleAcessoTest** - Valida controle por perfil

## 🏗️ Estrutura de Dados

### Estados de Crise

- **ABERTA** → EM_ANDAMENTO → RESOLVIDA → ENCERRADA
- **ENCERRADA** não pode retroceder

### Níveis de Crise

- BAIXO
- MEDIO
- ALTO
- CRITICO

### Tipos de Ação

- CONTENCAO
- COMUNICACAO
- RESOLUCAO
- MONITORAMENTO

### Perfis e Permissões

| Perfil | Permissões |
|--------|-----------|
| ADMIN | Acesso total |
| GERENTE | Criar/editar crises, alterar status, gerar relatórios |
| ANALISTA | Registrar ações, visualizar crises |
| VISUALIZADOR | Somente leitura |

## 📝 Exemplo de Fluxo Completo

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"gerente@empresa.com","senha":"gerente123"}' \
  | jq -r '.dados.token')

# 2. Criar crise
CRISE=$(curl -s -X POST http://localhost:8080/api/crises \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Outage em Produção","descricao":"Sistema fora do ar","nivel":"CRITICO","responsavelId":2}' \
  | jq -r '.dados.id')

# 3. Registrar ação
curl -s -X POST http://localhost:8080/api/crises/$CRISE/acoes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Escalado para time DevOps","tipo":"CONTENCAO"}'

# 4. Alterar status
curl -s -X PATCH "http://localhost:8080/api/crises/$CRISE/status?novoStatus=EM_ANDAMENTO" \
  -H "Authorization: Bearer $TOKEN"

# 5. Gerar relatório
curl -s -X POST http://localhost:8080/api/relatorios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"titulo":"Relatório do Incidente","conteudo":"Análise do ocorrido...","criseId":'$CRISE'}'
```

## 🐛 Troubleshooting

### Erro: "Connection refused" no PostgreSQL

Verifique se o PostgreSQL está rodando:
```bash
psql -h localhost -U postgres
```

### Erro: "JWT token not found"

Certifique-se de enviar o token no header:
```bash
-H "Authorization: Bearer SEU_TOKEN"
```

### Erro: "Forbidden (403)"

Verifique se seu perfil tem permissão para acessar o endpoint.

## 📦 Dependências Principais

- Quarkus 3.8.0
- Hibernate ORM Panache
- PostgreSQL Driver
- SmallRye JWT
- BCrypt
- RESTEasy Reactive
- JUnit 5
- REST Assured

## 📖 Documentação Oficial

- [Quarkus](https://quarkus.io/)
- [Hibernate ORM](https://hibernate.org/)
- [SmallRye JWT](https://smallrye.io/smallrye-jwt/)
- [JAX-RS](https://jakarta.ee/specifications/restful-web-services/)

## 📄 Licença

Desenvolvido como sistema corporativo de gestão de crises.

## 👤 Autor

Sistema desenvolvido com arquitetura em camadas seguindo os padrões Java EE.
