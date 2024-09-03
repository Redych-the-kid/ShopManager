# ShopManager - тестовое для Aikamsoft
## Стэк
* Java 8
* Lombok
* Javatuple
* JDBC
* Jackson
* Maven
## Сборка
Внесиде данные в `properties`, находящемся в `src/main/resources`, для подключения к базе данных. База должна быть проинициализирована перед этим с помощью `init.sql`, находящемся в `test files`. Затем в корне репозитория запускаем:
```
mvn clean package
```
После этого JAR будет находиться в каталоге `target`
## Запуска
Пример запуска программы:
```
java -jar ShopManager.jar stat input.json output.json
```
Примеры входных файлов есть в `test files`
