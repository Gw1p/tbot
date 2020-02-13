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

TODO

## Известные Проблемы/Баги и Задачи

Проблемы/Баги:
- на текущий момент - никаких проблем.

## Требования

- Java 11
- Spring 2.2.4
- (java-telegram-bot-api v4.6.0)[https://github.com/pengrad/java-telegram-bot-api]
- Имеет постоянный открытый канал с SQL БД.
- Исходный репозиторий проекта, взятого за основу: https://github.com/pengrad/java-telegram-bot-api.

TODO

## Deployment

- добавить `logPath` в `application.properties` с путем к лог файлу (без расширения, дата будет добавлена автоматически)
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

## Тестирование

TODO

## Стиль Кода

[Google Java Style](https://google.github.io/styleguide/javaguide.html)
Пожалуйста, поддерживайте качество кода на необходимом уровне!