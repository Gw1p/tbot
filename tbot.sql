CREATE TABLE Users(
	id bigint NOT NULL PRIMARY KEY,
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
	firstMessage DATETIME NOT NULL,
	approved BIT NOT NULL DEFAULT(0)
);
GO

CREATE UNIQUE INDEX appr_usr_indx ON Users (approved);
GO

CREATE TABLE Chats(
	id bigint NOT NULL PRIMARY KEY,
    chatType VARCHAR(10) NOT NULL, -- Private, group, supergroup, channel
	firstName VARCHAR(255),
    lastName VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    title VARCHAR(255),
	inviteLink VARCHAR(255)
);
GO

-- Какие есть чаты с определенным пользователем
CREATE TABLE User_Chats(
	id bigint NOT NULL PRIMARY KEY,
	userId bigint NOT NULL FOREIGN KEY REFERENCES Users(id),
	chatId bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	chatType VARCHAR(255) NOT NULL, -- Private, group, supergroup, channel
	discoveredDate DATETIME NOT NULL
);
GO

CREATE TABLE MessagesIn(
	id bigint NOT NULL PRIMARY KEY,
	message VARCHAR(255),
	messageDate DATETIME NOT NULL,
	chatId bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	userId bigint NOT NULL FOREIGN KEY REFERENCES Users(id)
);
GO

CREATE TABLE MessagesOut(
	id bigint NOT NULL PRIMARY KEY,
	chatId bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	message VARCHAR(255),
	messageDate DATETIME,
	sent BIT NOT NULL DEFAULT(0)
);
GO
