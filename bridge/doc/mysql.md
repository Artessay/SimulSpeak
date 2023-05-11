# MySQL 数据库的创建与连接

```mysql
DROP DATABASE IF EXISTS simulspeak;
CREATE DATABASE simulspeak;
USE simulspeak;

CREATE TABLE `user_info`(
    `user_id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'user' NOT NULL UNIQUE,
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_auth` (
    `auth_id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id` int,
    `identity_type` varchar(32) NOT NULL,
    `identifier` varchar(255) NOT NULL,
    `credential` varchar(255) NOT NULL,
    PRIMARY KEY (`auth_id`),
    FOREIGN KEY (`user_id`) REFERENCES user_info(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE video_info(
    `video_id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id` int,
    `video_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `upload_time` timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`video_id`),
    FOREIGN KEY (`user_id`) REFERENCES user_info(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

