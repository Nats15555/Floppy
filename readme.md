# Floppy

_Для корректной работы необходим токен бота Telegram для серверной части и деплой клиентской части на сервер с TLS_

### Серверная часть
Стек
* Java
* Spring Boot
* JPA
* Liquibase
* H2

Запуск
1. `cd backend`
2. Сохранить токен для работы бота в переменную окружения `FLOPPY_BOT_TOKEN_TG` 
3. `./gradlew bootRun` (на MacOS и Linux) или `.\gradlew.bat bootRun` (на Windows) 

### Клиентская часть
Стек
* Typescript
* React
* Vite
* Axios

Запуск
1. `cd frontend`
2. `npm i`
3. `npm run dev` (для локальной проверки. Для полноценной работы необходим деплой на сервер с TLS)