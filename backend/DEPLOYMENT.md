# Deployment Guide - Sistema de Gestão de Crises

## 🐳 Executar com Docker Compose

### Pré-requisitos

- Docker instalado
- Docker Compose instalado

### Passo 1: Build da imagem

```bash
cd /workspaces/Sistema-de-Gestao-de-Crises/backend
mvn clean package -Pnative -DskipTests
```

Ou sem native (mais rápido):

```bash
mvn clean package -DskipTests
```

### Passo 2: Subir os containers

```bash
docker-compose up -d
```

Aguarde 30 segundos para o PostgreSQL estar pronto e a aplicação iniciar.

### Passo 3: Verificar status

```bash
docker-compose ps
```

Você deve ver:
- `gestaocrise_db` - PostgreSQL (RUNNING)
- `gestaocrise_backend` - Quarkus (RUNNING)

### Passo 4: Testar

```bash
curl http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@empresa.com","senha":"admin123"}'
```

## 🛑 Parar os containers

```bash
docker-compose down
```

## 🔧 Variáveis de Ambiente

Edite `docker-compose.yml` para alterar:

```yaml
environment:
  QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://postgres:5432/gestaocrise_db
  QUARKUS_DATASOURCE_USERNAME: postgres
  QUARKUS_DATASOURCE_PASSWORD: postgres
```

## 📊 Acessar banco de dados

```bash
docker exec -it gestaocrise_db psql -U postgres -d gestaocrise_db
```

## 📜 Verificar logs

```bash
docker logs gestaocrise_backend -f
```

## 🚀 Deployment em Produção

### Kubernetes

Crie um `deployment.yaml`:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gestaocrise-backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: gestaocrise-backend
  template:
    metadata:
      labels:
        app: gestaocrise-backend
    spec:
      containers:
      - name: backend
        image: gestaocrise-backend:1.0.0
        ports:
        - containerPort: 8080
        env:
        - name: QUARKUS_DATASOURCE_JDBC_URL
          value: jdbc:postgresql://postgres:5432/gestaocrise_db
        - name: QUARKUS_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: QUARKUS_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
```

Deploy:
```bash
kubectl apply -f deployment.yaml
```

## 🔐 Segurança em Produção

1. **Alterar credenciais padrão** de usuários
2. **Usar HTTPS/TLS** em produção
3. **Configurar JWT com chave privada segura**
4. **Limitar acesso ao banco de dados**
5. **Ativar WAF (Web Application Firewall)**
6. **Implementar rate limiting**
7. **Usar secrets manager** para credenciais

## 📈 Monitoramento

Adicione ao `docker-compose.yml`:

```yaml
prometheus:
  image: prom/prometheus
  volumes:
    - ./prometheus.yml:/etc/prometheus/prometheus.yml
  ports:
    - "9090:9090"
```

## 🆘 Troubleshooting

**Container não inicia:**
```bash
docker logs gestaocrise_backend
```

**Porta já em uso:**
```bash
lsof -i :8080
kill -9 <PID>
```

**Banco de dados não conecta:**
```bash
docker logs gestaocrise_db
docker exec gestaocrise_db pg_isready
```

## 📝 Checklist de Deployment

- [ ] Compilação bem-sucedida
- [ ] Testes passando
- [ ] Credenciais configuradas
- [ ] Backup do banco de dados
- [ ] HTTPS configurado
- [ ] Logs centralizados
- [ ] Monitoramento ativo
- [ ] Plano de rollback
- [ ] Documentação atualizada
