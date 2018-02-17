create table Account (
	username char(20) not null,
	email char (30),
	password char(30),
	numfriends int default 0,
    accountID int auto_increment,
Primary key (accountID));

create table Settings (
	accountID int not null references Acconut,
    showStats bool default true,
    controllerSize double default 7,
    musicVolumePercent double default 1,
    gameVolumePercent double default 1,
    mute bool default false,
Primary key (accountID));

create table FriendRequests (
	fromID int not null,
    toID int not null
);

create table GameInvites (
	fromID int not null,
    toID int not null
);

create table FriendsList (
	accountID int not null references Acconut,
    friend0 int(20),
    friend1 int(20),
    friend2 int(20),
    friend3 int(20),
    friend4 int(20),
    friend5 int(20),
    friend6 int(20),
    friend7 int(20),
    friend8 int(20),
    friend9 int(20),
    friend10 int(20),
    friend11 int(20),
    friend12 int(20),
    friend13 int(20),
    friend14 int(20),
    friend15 int(20),
    friend16 int(20),
    friend17 int(20),
    friend18 int(20),
    friend19 int(20),
    friend20 int(20),
    friend21 int(20),
    friend22 int(20),
    friend23 int(20),
    friend24 int(20),
    friend25 int(20),
    friend26 int(20),
    friend27 int(20),
    friend28 int(20),
    friend29 int(20),
    friend30 int(20),
    friend31 int(20),
    friend32 int(20),
    friend33 int(20),
    friend34 int(20),
    friend35 int(20),
    friend36 int(20),
    friend37 int(20),
    friend38 int(20),
    friend39 int(20),
    friend40 int(20),
    friend41 int(20),
    friend42 int(20),
    friend43 int(20),
    friend44 int(20),
    friend45 int(20),
    friend46 int(20),
    friend47 int(20),
    friend48 int(20),
    friend49 int(20),
    friend50 int(20),
Primary key(accountID));