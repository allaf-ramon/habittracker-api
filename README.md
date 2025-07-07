# Habit Tracker API

Uma API simples para rastreamento de hábitos.

## Arquitetura Hexagonal

Este projeto utiliza uma arquitetura hexagonal (também conhecida como arquitetura de portas e adaptadores). Isso significa que a lógica de negócios principal (o domínio) é isolada das preocupações externas, como frameworks da web ou bancos de dados.

- **Domínio**: Contém a lógica de negócios principal.
- **Adaptadores**:
    - **Entrada (Inbound)**: Lidam com a entrada do usuário (por exemplo, controladores da web).
    - **Saída (Outbound)**: Lidam com a comunicação com serviços externos (por exemplo, bancos de dados).
- **Portas**: Interfaces que definem como os adaptadores interagem com o domínio.

## Endpoints

### Hábitos

- `POST /v1/habits`: Cria um novo hábito.
- `GET /v1/habits/{id}`: Busca um hábito pelo ID.
- `GET /v1/habits`: Lista todos os hábitos.
- `PUT /v1/habits/{id}`: Atualiza um hábito.
- `DELETE /v1/habits/{id}`: Deleta um hábito.
- `GET /v1/habits/{id}/stats`: Retorna as estatísticas de um hábito.

### Conclusão de Hábitos

- `POST /v1/habits/{habitId}/completions/{date}`: Marca um hábito como concluído em uma data específica.
- `DELETE /v1/habits/{habitId}/completions/{date}`: Desmarca um hábito como concluído em uma data específica.

## Executando o projeto

Para executar o projeto, você precisará ter o Docker e o Docker Compose instalados.

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/habittracker-api.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd habittracker-api
   ```
3. Execute o seguinte comando para iniciar a aplicação:
   ```bash
   docker-compose up -d
   ```
A API estará disponível em `http://localhost:8080`.

