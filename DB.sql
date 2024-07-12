-- ArticleManager Data
CREATE DATABASE ArticleManager;
USE ArticleManager;

DROP TABLE article;

CREATE TABLE article (
    `id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `regDate` DATETIME NOT NULL,
    `updateDate` DATETIME NOT NULL,
    `title` VARCHAR(50) NOT NULL,
    `body` TEXT
);

INSERT INTO article
SET title = '제목1', `body` = '내용1';

SELECT * FROM article;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = '제목1',
`body` = '내용1';

-- 문자열 붙이기
SELECT CONCAT('제목' , ' 1');
-- 랜덤 수 출력 (범위 : 0~1)
SELECT RAND();

SELECT SUBSTRING(RAND() * 1000 FROM 1 FOR 2);

-- 문자열 붙이기 + 랜덤 수 출력
INSERT INTO article
SET regDate = NOW(),
    updateDate = NOW(),
    title = CONCAT('제목', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),
    `body` = CONCAT('내용', SUBSTRING(RAND() * 1000 FROM 1 FOR 2));

#########################################################################
-- `member` table 생성
CREATE TABLE `member` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),
    `regDate`	DATETIME NOT NULL,
    `userId` VARCHAR(100) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(100) NOT NULL
);

DESC `article`;
DESC `member`;

INSERT INTO `member` (`regDate`, `userId`, `password`, `nickname`)
VALUES
    (NOW(), 'test01', 'test01', '홍길동'),
    (NOW(), 'test02', 'test02', '홍길순'),
    (NOW(), 'test03', 'test03', '임꺽정');

SELECT * FROM article;
SELECT * FROM `member`;


