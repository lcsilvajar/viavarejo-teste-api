# API Teste para Via Varejo

Api de teste para Via Varejo, esta api recebe dados de uma compra e informa o valor de parcelas com juros simples.

### Pre-requisitos

JDK Java 1.8
Git
Postman
Maven
Web Browser

## Deploy Api

Heroku
endpoint : https://dry-mountain-79582.herokuapp.com/api/compra
Body para Post:
```
{"produto":{"codigo":123,"nome":"TELEVISÃO SAMSUNG","valor":9999.99},"condicaoPagamento":{"valorEntrada":999.99,"qtdeParcelas":9}}
```
Deploy Local:cd /viavarejo (raiz do projeto)
java -jar target/viavarejo-0.0.1-SNAPSHOT.jar
endpoint : http://localHost:8080/api/compra
Body para Post:
```
{"produto":{"codigo":123,"nome":"TELEVISÃO SAMSUNG","valor":9999.99},"condicaoPagamento":{"valorEntrada":999.99,"qtdeParcelas":9}}
```

## Executar testes

Abrir a pasta raiz do projeto e executar o comando abaixo:
```
mvn clean install
```
## Documentação
Swagger
```
https://dry-mountain-79582.herokuapp.com/swagger-ui.html
ou
http://localHost:8080/swagger-ui.html
```
## Autor

* **Leonardo Costa Silva** - *Java Developer* - [lcsilva](https://github.com/lcsilvajar)

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details


