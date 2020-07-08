# TBot (Telegram Bot)

TBot repo contains a Java application that provides a wide range of functionalities for a [Telegram Bot](https://core.telegram.org/bots).

☕ TBot can chat with users in Telegram. Additionally, it provides a class hierarchy for defining custom `commands` that allow you to create interactions with users of high complexity from scratch. 

Whether you want to create a bot that allows users to interact with your API, are looking for a telegram bot template that allows you to define conversation flows that are out of the box and/or include a complex combination of inline-queries, keyboards and others, then this project can help you get started.


## Default Commands

By default, TBot comes with 3 sample commands:

- /help - lists all available commands
- /status - prints the status of the bot
- /stop - stops TBot (admin only)

## TODO:

- all documentation in the project is written in Russian (sorry), would be good to redo this in English
- add more meaningful commands
- make TBot robust to command errors and heal/automatically restart upon failure
- many more...

## Dependencies

- Java 11
- Spring 2.2.4
- (java-telegram-bot-api v4.6.0)[https://github.com/pengrad/java-telegram-bot-api]
- [Jakarta XML Binding API (2.3.2)](https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api/2.3.2)
- [JAXB Runtime (2.3.2)](https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime/2.3.2)
- connection to an SQL DB (but you can rewrite it to use anything else).
- Source repo, which this project is based on [https://github.com/pengrad/java-telegram-bot-api](https://github.com/pengrad/java-telegram-bot-api).

See `./build.gradle` for a full list of dependencies.

## Deployment

- create a new Telegram bot and obtain a token using [BotFather](https://core.telegram.org/bots#6-botfather)
- download and unzip the TBot repo
- insert the bot token into `application.properties`
- create a database and populate it with tables that are defined in `tbot.sql`
- paste the database config to `application.properties` (`spring.datasource.url=jdbc:sqlserver://localhost;databaseName=<db-name>;useUnicode=true&characterEncoding=utf-8`, `spring.datasource.username=<db-username>` and
`spring.datasource.password=<db-password>`
- build and run the application (remember, you have to message the bot first before it can send you any messages!)

## Adding New Commands

All commands are in `com.ufc.tbot.conversation.commands`. Every command inherits from `Conversation` base class.


To create a new command, you need to define variables `currentStep` (typically `-1`), `maxSteps` (number of steps required for the command to finish) and `minimumPermissions` (minimum permissions required to initialise the command).
You also need to define method `boolean canStart(String message, User user)`. This method has to return `true` when the given `user` can start the command with the given `message`.


Напоследок, `Response step(String message, User user, List<User> users)` определяет что делает комманда.
Все новые комманды надо добавить в `BotService.init()` в список `availableCommands`.

---

`Response` позволяет Боту понять, что необходимо сделать, как результат комманды.
С помощью этого объекта, можно отправить пользователю сообщение (включая кастомную клавиатуру), а также выполнить дополнительные действия (например, обновить пользователя в БД).

## Тестирование

- добавленные тесты используются автоматически при создании нового build

## Стиль Кода

[Google Java Style](https://google.github.io/styleguide/javaguide.html)
Пожалуйста, поддерживайте качество кода на необходимом уровне!