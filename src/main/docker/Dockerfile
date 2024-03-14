# Use a imagem base para o Quarkus em modo de desenvolvimento
FROM maven:3-eclipse-temurin-17-alpine

# Defina o diretório de trabalho no contêiner
WORKDIR /usr/src/app

#Copie o arquivo pom.xml para o diretório de trabalho
COPY  . .

## Baixe as dependências do Maven
# RUN mvn clean install -DskipTests

# Exponha a porta 8080 (a porta padrão do Quarkus)
EXPOSE 8080

# Inicie a aplicação Quarkus em modo de desenvolvimento
CMD mvn quarkus:dev
