CREATE TABLE Users(
	id bigint NOT NULL PRIMARY KEY,
    firstName NVARCHAR(255),
    lastName NVARCHAR(255),
    username NVARCHAR(255),
	firstMessage DATETIME NOT NULL,
	phone bigint
);
GO

CREATE TABLE Permissions(
	id int NOT NULL PRIMARY KEY,
	permission NVARCHAR(255) NOT NULL
);
GO

INSERT INTO Permissions (id, permission) VALUES (1, 'ADMIN');
GO

INSERT INTO Permissions (id, permission) VALUES (2, 'USER');
GO

CREATE TABLE UserPermissions(
	id int NOT NULL PRIMARY KEY IDENTITY(1, 1),
	userId bigint NOT NULL FOREIGN KEY REFERENCES Users(id),
	permissionId int NOT NULL FOREIGN KEY REFERENCES Permissions(id),
	UNIQUE(userId, 	permissionId)
);
GO

CREATE TABLE Chats(
	id bigint NOT NULL PRIMARY KEY,
    chatType NVARCHAR(10) NOT NULL, -- Private, group, supergroup, channel
	firstName NVARCHAR(255),
    lastName NVARCHAR(255),
    username NVARCHAR(255),
    title NVARCHAR(255),
	inviteLink VARCHAR(255)
);
GO

-- Какие есть чаты с определенным пользователем
CREATE TABLE UserChats(
	id int NOT NULL PRIMARY KEY IDENTITY(1, 1),
	userId bigint NOT NULL FOREIGN KEY REFERENCES Users(id),
	chatId bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	chatType NVARCHAR(255) NOT NULL, -- Private, group, supergroup, channel
	discoveredDate DATETIME NOT NULL,
	UNIQUE(userId, chatId)
);
GO

CREATE TABLE MessagesIn(
	id bigint NOT NULL PRIMARY KEY,
	message NVARCHAR(max) NOT NULL,
	messageDate DATETIME NOT NULL,
	chatId bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	userId bigint NOT NULL FOREIGN KEY REFERENCES Users(id)
);
GO

CREATE TABLE MessagesOut(
	id bigint NOT NULL PRIMARY KEY,
	chatId bigint NOT NULL FOREIGN KEY REFERENCES Chats(id),
	message NVARCHAR(max),
	messageDate DATETIME,
	sent BIT NOT NULL DEFAULT(0)
);
GO
