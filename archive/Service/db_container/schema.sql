DROP TABLE IF EXISTS users;
CREATE TABLE users (
  id       UUID PRIMARY KEY,
  account  TEXT UNIQUE,
  password TEXT,
  salt     TEXT
);
DROP TABLE IF EXISTS userInfo;
CREATE TABLE userInfo (
  userId       UUID PRIMARY KEY,
  serial       TEXT NOT NULL,
  nickname     TEXT NOT NULL,
  avatar       TEXT,
  weekBudget   NUMERIC(10, 2) DEFAULT 0,
  monthBudget  NUMERIC(10, 2) DEFAULT 0,
  yearBudget   NUMERIC(10, 2) DEFAULT 0,
  isWishPublic BOOL           DEFAULT FALSE,
  level        INT            DEFAULT 0,
  createAt     TIMESTAMP      DEFAULT now(),
  updateAt     TIMESTAMP      DEFAULT now()
);
DROP TABLE IF EXISTS userBind;
CREATE TABLE userBind (
  userId     UUID PRIMARY KEY,
  account    TEXT,
  platform   INT,
  oauthToken TEXT
);
DROP TABLE IF EXISTS book;
CREATE TABLE book (
  id         UUID PRIMARY KEY,
  userId     UUID NOT NULL,
  name       TEXT NOT NULL,
  cover      TEXT,
  isPublic   BOOL           DEFAULT FALSE,
  saveTarget NUMERIC(10, 2) DEFAULT 0,
  createAt   TIMESTAMP      DEFAULT now(),
  updateAt   TIMESTAMP      DEFAULT now()
);
DROP TABLE IF EXISTS recordSheet;
CREATE TABLE recordSheet (
  userId   UUID NOT NULL,
  recordId UUID NOT NULL,
  bookId   UUID NOT NULL,
  createAt TIMESTAMP DEFAULT now()
);
DROP TABLE IF EXISTS record;
CREATE TABLE record (
  id           UUID PRIMARY KEY,
  userId       UUID           NOT NULL,
  place        TEXT,
  type         TEXT,
  amount       NUMERIC(10, 2) NOT NULL,
  attachment   TEXT,
  isRegular    BOOL      DEFAULT FALSE,
  regularRule  TEXT,
  source       TEXT,
  comment      TEXT,
  isReimbursed BOOL,
  happenAt     TIMESTAMP      NOT NULL,
  createAt     TIMESTAMP DEFAULT now(),
  updateAt     TIMESTAMP DEFAULT now()
);
DROP TABLE IF EXISTS bookAuth;
CREATE TABLE bookAuth (
  bookId    UUID NOT NULL,
  ownerId   UUID NOT NULL,
  ownerType INT2,
  ownerAuth INT2
);
DROP TABLE IF EXISTS organization;
CREATE TABLE organization (
  id       UUID PRIMARY KEY,
  avatar   TEXT,
  name     TEXT NOT NULL,
  userId   UUID NOT NULL,
  createAt TIMESTAMP DEFAULT now(),
  updateAt TIMESTAMP DEFAULT now()
);
DROP TABLE IF EXISTS organizationMember;
CREATE TABLE organizationMember (
  userId         UUID,
  organizationId UUID,
  createAt       TIMESTAMP DEFAULT now(),
  auth           INT2 NOT NULL
);
DROP TABLE IF EXISTS iou;
CREATE TABLE iou (
  name        TEXT           NOT NULL,
  userId      UUID           NOT NULL,
  createAt    TIMESTAMP DEFAULT now(),
  updateAt    TIMESTAMP DEFAULT now(),
  totalAmount NUMERIC(10, 2) NOT NULL,
  type        TEXT           NOT NULL,
  payers      UUID []        NOT NULL,
  confirm     BOOL []        NOT NULL,
  shareAmount NUMERIC(10, 2) [],
  state       SMALLINT  DEFAULT 0,
  checkers    UUID []        NOT NULL,
  checkList   SMALLINT []    NOT NULL,
  isFinished  BOOL      DEFAULT FALSE
);
DROP TABLE IF EXISTS wishList;
CREATE TABLE wishList (
  id          UUID PRIMARY KEY,
  wish        TEXT           NOT NULL,
  price       NUMERIC(10, 2) NOT NULL,
  userId      UUID           NOT NULL,
  comment     TEXT,
  state       INT DEFAULT 1000,
  type        TEXT           NOT NULL,
  predictPlan DATE           NOT NULL
);
DROP TABLE IF EXISTS change;
CREATE TABLE change (
  userId     UUID NOT NULL,
  targetId   UUID NOT NULL,
  targetType INT2 NOT NULL,
  createAt   TIMESTAMP DEFAULT now()
);
DROP TABLE IF EXISTS notification;
CREATE TABLE notification (
  userId   UUID NOT NULL,
  content  TEXT NOT NULL,
  type     INT  NOT NULL DEFAULT 1001,
  extra    JSON,
  createAt TIMESTAMP     DEFAULT now()
);
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO save;