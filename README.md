# TBot

Проект по разработке Telegram Bot, бота для приложения по обмену сообщениями -  [Telegram](https://telegram.org/).

Бот контролирует интеракции пользователей в Telegram и дает юзерам доступ к необходимому, дистанционному функционалу.

## Для чего TBot?

Этот Бот разрабатывается чтобы мониторить сообщения в чатах Telegram.
Являясь обычным членом чата, пользователи могут посылать сообщения Боту, на которые он может реагировать.
Бот также может читать сообщения из групповых чатов.

Все сообщения, зафиксированные Ботом, записываются в дистанционную SQL БД.
В этой же БД, программа следит за специальной таблицей, в которой периодически появляются новые записи.
Именно новые записи, оставленные сторонней программой, дают Боту понять кому и какое сообщение надо отправить.

## Доступные Комманды

- /помощь - выдает список доступных комманд
- /пользователи - позволяет админам изменять права пользователей
- /стоп - позволяет админам остановить работу бота

## Известные Проблемы/Баги и Задачи

Проблемы/Баги:
- на текущий момент - никаких проблем.

## Требования

- Java 11
- Spring 2.2.4
- (java-telegram-bot-api v4.6.0)[https://github.com/pengrad/java-telegram-bot-api]
- Имеет постоянный открытый канал с SQL БД.
- Исходный репозиторий проекта, взятого за основу: https://github.com/pengrad/java-telegram-bot-api.

## Deployment

- добавить `logPath` в `application.properties` с путем к лог файлу (без расширения, дата будет добавлена автоматически)
- добавить `botToken` в `application.properties` с токеном бота
- настроить и добавить следующие конфигурации в `application.properties`:
```
	spring.datasource.url=<jdbc-link>
	spring.datasource.username=<database-username>
	spring.datasource.password=<database-password>
	spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
	spring.jpa.show-sql=true
	spring.jpa.properties.hibernate.format_sql=true
	spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect
	spring.jpa.hibernate.ddl-auto=update
```

На текущий момент, работает как сервис `tbot` на Linux сервере.
Для обновления программы, создайте новый build (например, `tbot-0.0.1-SNAPSHOT.jar`) и скопируйте его (например, используя `scp`) на сервер под `/home/anton/tbot`.
Скрипт `/home/anton/tbot/start.sh` задает комманду, с помощью которой сервис запускает приложение.

## Тестирование

- добавленные тесты используются автоматически при создании нового build

## Стиль Кода

[Google Java Style](https://google.github.io/styleguide/javaguide.html)
Пожалуйста, поддерживайте качество кода на необходимом уровне!