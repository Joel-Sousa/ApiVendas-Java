# ApiVendas-Java
Api de vendas feita com SpringBoot com autenticacao de usuarios

- Spring boot 
- JPA
- Hibernate
- H2
- JUnit
- Bean validation
- Spring security e JWT(JsonWebToken)
- SWAGGER

Comandos:

http://localhost:8080/h2-console // Base do H2

mvn clean package // Limpa e realiza build no bash

java -jar './vendas-0.0.1-SNAPSHOT.jar' //entrar na pasta 'target' Executa o .jar

mvn clean package -P 'nome do profile' // Executa o build de acordo com o profile "desenvolvimento || producao"

http://localhost:8080/swagger-ui.html // Documentacao
