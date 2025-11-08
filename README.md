# 🚀 InovaGurupi - Portal de Inovação Pública

## 📋 Sobre o Projeto

**InovaGurupi** é uma plataforma inovadora que conecta universidades, prefeitura e cidadãos em um ecossistema de inovação pública. O portal permite que pesquisadores submetam projetos voltados a desafios reais da cidade, baseados em dados oficiais. A população vota e acompanha o impacto dos projetos, enquanto a prefeitura toma decisões com base em evidências e engajamento social.

### 🎯 Problema que Resolve

Como poder público, é difícil saber o que a população realmente quer e necessita. O InovaGurupi visa permitir que a própria população escolha quais projetos acreditam que trarão mais retorno para a sociedade, unindo a comunidade acadêmica de Gurupi e a sociedade com projetos de valor. Com base nos votos, a prefeitura poderá tomar decisões mais assertivas em quais projetos financiar.

## 👥 Três Visões do Portal

### 🎓 Acadêmicos
- Enviam propostas de projetos com retorno social
- Recebem investimento ou patrocínio da prefeitura
- Ganham pontos e selos por engajamento
- Podem vincular projetos ao currículo acadêmico

### 👨‍👩‍👧‍👦 População (Cidadãos)
- Acessa lista de projetos públicos
- Vê detalhes completos dos projetos
- Vota nos projetos mais interessantes
- Comenta e acompanha o desenvolvimento
- Ganha pontos por participação

### 🏛️ Prefeitura (Gestores)
- Dashboard de gerenciamento completo
- Aprova/rejeita projetos submetidos
- Avalia tecnicamente os projetos
- Cria desafios prioritários
- Acompanha estatísticas e indicadores

## 🎲 Modelo de Avaliação

**Nota Final = 50% Técnica + 50% Popular**

- **Nota Técnica**: Avaliação de gestores baseada em critérios (viabilidade, impacto, inovação, orçamento)
- **Nota Popular**: Baseada nos votos da população

## 🏗️ Arquitetura do Projeto

### Tecnologias
- **Backend**: Quarkus (Java)
- **ORM**: Hibernate com Panache
- **Banco de Dados**: PostgreSQL
- **API**: RESTful

### Estrutura de Camadas
```
src/main/java/br/unitins/topicos1/
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── repository/     # Repositórios Panache
├── service/        # Lógica de negócio
└── resource/       # Endpoints REST
```

## 📊 Principais Entidades

- **Usuários**: Academico, Cidadao, GestorPrefeitura
- **Projeto**: Informações completas do projeto
- **Voto**: Sistema de votação popular
- **AvaliacaoTecnica**: Avaliação dos gestores
- **Comentario**: Interação da população
- **Desafio**: Desafios prioritários da prefeitura
- **RepositorioDados**: Links para dados públicos

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### Configuração do Banco de Dados

Crie um arquivo `.env` ou configure variáveis de ambiente:

```properties
# Desenvolvimento
DEV_DB_TYPE=postgresql
DEV_DB_USER=seu_usuario
DEV_DB_PASSWORD=sua_senha
DEV_DB_ADDRESS=jdbc:postgresql://localhost:5432/inovagurupi

# Produção
DB_TYPE=postgresql
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
DB_ADDRESS=jdbc:postgresql://localhost:5432/inovagurupi
```

### Executar em Modo Dev

```bash
./mvnw quarkus:dev
```

A aplicação estará disponível em: `http://localhost:8080`

### Compilar para Produção

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## 📡 Principais Endpoints

### Autenticação
- `POST /auth/solicitar-codigo` - Solicita código de autenticação por email
- `POST /auth/login` - Faz login com email e código

### Usuários
- `GET/POST /academicos` - Gerenciar acadêmicos
- `GET/POST /cidadaos` - Gerenciar cidadãos
- `GET/POST /gestores` - Gerenciar gestores

### Projetos
- `GET /projetos` - Listar todos os projetos
- `GET /projetos/publicos` - Listar projetos públicos (aprovados)
- `GET /projetos/top-votados` - Top projetos mais votados
- `POST /projetos` - Criar novo projeto
- `POST /projetos/{id}/submeter` - Submeter projeto para avaliação
- `POST /projetos/{id}/aprovar` - Aprovar projeto (gestor)
- `POST /projetos/{id}/rejeitar` - Rejeitar projeto (gestor)

### Votação
- `POST /votos` - Votar em um projeto
- `GET /votos/projeto/{id}` - Listar votos de um projeto
- `DELETE /votos/projeto/{projetoId}/cidadao/{cidadaoId}` - Remover voto

### Avaliação Técnica
- `POST /avaliacoes` - Avaliar tecnicamente um projeto
- `GET /avaliacoes/projeto/{id}` - Listar avaliações de um projeto

### Comentários
- `POST /comentarios` - Comentar em um projeto
- `GET /comentarios/projeto/{id}` - Listar comentários de um projeto

### Desafios
- `GET /desafios` - Listar todos os desafios
- `GET /desafios/ativos` - Listar desafios ativos
- `POST /desafios` - Criar novo desafio

### Dashboard
- `GET /dashboard` - Obter estatísticas gerais

### Repositórios de Dados
- `GET /repositorios` - Listar repositórios de dados públicos

## 🎮 Dados de Exemplo

O sistema vem com dados de exemplo já populados:
- 3 Acadêmicos
- 4 Cidadãos
- 2 Gestores da Prefeitura
- 5 Projetos (3 aprovados, 2 aguardando aprovação)
- 9 Votos
- 6 Avaliações técnicas
- 5 Comentários
- 3 Desafios
- 5 Repositórios de dados públicos

## 📈 Critérios de Avaliação do Hackathon

- ✅ **Resolução do problema** – 20 pts
- ✅ **Viabilidade** – 15 pts
- ✅ **Inovação** – 15 pts
- ✅ **Facilidade de uso** – 10 pts
- ✅ **Custo-benefício** – 10 pts
- ✅ **Apresentação** – 10 pts
- ✅ **Tecnologias inovadoras** – 10 pts
- ✅ **Facilidade de implantação** – 10 pts

## 🎯 Diferenciais do InovaGurupi

1. **Sistema de Gamificação**: Pontos e selos por engajamento
2. **Avaliação Híbrida**: 50% técnica + 50% popular
3. **Trilhas de Colaboração**: Desafios definidos pela prefeitura
4. **Repositório de Dados**: Evidências para embasar propostas
5. **Transparência**: Acompanhamento público dos projetos
6. **Replicável**: Modelo pode ser usado por outras cidades

## 🤝 Contribuindo

Este projeto foi desenvolvido para o Hackathon SEBRAE/Prefeitura de Gurupi 2024.

## 📄 Licença

Este projeto está sob licença MIT.

## 👨‍💻 Autor

Desenvolvido para o Hackathon de Inovação Pública - Gurupi/TO 2024

---

**"Conectando universidades, prefeitura e cidadãos para transformar conhecimento em soluções reais, com base em evidências e participação popular."**
