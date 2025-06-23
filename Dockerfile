# Estágio de Build: Usa uma imagem Maven com JDK 17 para compilar o projeto.
FROM maven:3.8.5-openjdk-17 AS build

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Copia o pom.xml para o contêiner para baixar as dependências.
COPY pom.xml .

# Copia o wrapper do Maven para garantir que a mesma versão seja usada.
COPY .mvn/ .mvn
COPY mvnw .
COPY mvnw.cmd .

# Baixa as dependências do projeto. Isso otimiza o build, pois as dependências
# são cacheadas em uma camada separada.
RUN ./mvnw dependency:go-offline

# Copia o código-fonte da aplicação.
COPY src ./src

# Compila a aplicação e gera o arquivo JAR, pulando os testes.
RUN ./mvnw clean install -DskipTests

# Estágio de Execução: Usa uma imagem JRE mais leve para rodar a aplicação.
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Copia o arquivo JAR gerado no estágio de build para o contêiner final.
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080, que é a porta padrão do Spring Boot.
EXPOSE 8080

# Define o comando para iniciar a aplicação quando o contêiner for executado.
ENTRYPOINT ["java","-jar","app.jar"]
