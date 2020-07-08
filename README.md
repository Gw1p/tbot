# TBot (Telegram Bot)

TBot repo contains a Java application that provides a wide range of functionalities for a [Telegram Bot](https://core.telegram.org/bots).

☕ TBot can chat with users in Telegram. Additionally, it provides a class hierarchy for defining custom `commands` that allow you to create interactions with users of high complexity from scratch. 

Whether you want to create a bot that allows users to interact with your API, are looking for a telegram bot template that allows you to define conversation flows that are out of the box and/or include a complex combination of inline-queries, keyboards and others, then this project can help you get started.

## Overview

The project follows the MVC pattern. 

`TbotApplication` runs launches an `AppController` that is responsible for starting `BotService`. The `BotService`, in turn, handles incoming and outgoing messages.

The required table structure is located in `tbot.sql`. Any changes to that will, in turn, require changes in the code.

The bot comes with 2 types of user permissions: `ADMIN` and `USER`.

## Default Commands

By default, TBot comes with 3 sample commands:

- /help - lists all available commands
- /status - prints the status of the bot
- /stop - stops TBot (admin only)

## TODO:

- all documentation in the project is written in Russian (sorry), would be good to redo this in English
- add more meaningful commands
- make TBot robust to command errors and heal/automatically restart upon failure
- provide default methods for requesting contact details, sending images and processing inline-queries
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

All commands are in `com.gw1p.tbot.conversation.commands`. Every command inherits from `Conversation` base class.


To create a new command, you need to define variables `currentStep` (typically `-1`), `maxSteps` (number of steps required for the command to finish) and `minimumPermissions` (minimum permissions required to initialise the command).
You also need to define the method `boolean canStart(String message, User user)`. This method has to return `true` when the given `user` can start the command with the given `message`.


Lastly, `Response step(String message, User user, List<User> users)` defines how command processes user messages (given that it has started before).
All new commands should be added to `BotService.init()` in the list `availableCommands`.

---

`Response` allows the Bot to understand how to act (for example, send a text message, send a keyboard, etc). Using this object, you may also execute additional actions (i.e. update a record in the DB).

## Testing

Some basic DAO tests are included and run automatically when you build. These mainly test that constring, saving and deleting works.