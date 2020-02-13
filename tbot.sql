CREATE TABLE Users(
	id bigint NOT NULL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
	first_message DATETIME NOT NULL,
	approved BIT NOT NULL DEFAULT(0)
);
GO

CREATE UNIQUE INDEX appr_usr_indx ON Users (approved);
GO

CREATE TABLE Chats(
	id bigint NOT NULL PRIMARY KEY,
    chat_type VARCHAR(10) NOT NULL, -- Private, group, supergroup, channel
	first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    title VARCHAR(255),
	invite_link VARCHAR(255)
);
GO

-- Какие есть чаты с определенным пользователем
CREATE TABLE User_Chats(
	id bigint NOT NULL PRIMARY KEY,
	user_id bigint NOT NULL FOREIGN KEY REFERENCES Users(id),
	chat_id bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	chat_type VARCHAR(255) NOT NULL, -- Private, group, supergroup, channel
	discovered_date DATETIME NOT NULL
);
GO

CREATE TABLE MessagesIn(
	id bigint NOT NULL PRIMARY KEY,
	message VARCHAR(255),
	message_date DATETIME NOT NULL,
	chat_id bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	user_id bigint NOT NULL FOREIGN KEY REFERENCES Users(id)
);
GO

CREATE TABLE MessagesOut(
	id bigint NOT NULL PRIMARY KEY,
	chat_id bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	message VARCHAR(255),
	message_date DATETIME NOT NULL,
	sent BIT NOT NULL DEFAULT(0)
);
GO
