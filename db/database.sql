-- ------------------------
-- Table structure for user
-- ------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` -- 用户信息表
(
    `id`                 INTEGER PRIMARY KEY AUTOINCREMENT, -- 用户id
    `name`               VARCHAR(64)  NOT NULL,             -- 用户名
    `nickname`           VARCHAR(64)  NOT NULL,             -- 昵称
    `passwd`             VARCHAR(128) NOT NULL,             -- 密码
    `locked`             TINYINT     DEFAULT 0,             -- 是否已锁定，0-否，1-是
    `try_count`          TINYINT     DEFAULT 0,             -- 尝试输入密码次数，超过3次账号将会被锁定
    `last_login_ip`      VARCHAR(64) DEFAULT '',            -- 上一次登录ip
    `last_login_time`    INTEGER     DEFAULT 0,             -- 上一次登录时间（时间戳，单位s）
    `current_login_ip`   VARCHAR(64) DEFAULT '',            -- 当前登录ip
    `current_login_time` INTEGER     DEFAULT 0,             -- 当前登录时间（时间戳，单位s）
    `add_time`           INTEGER     DEFAULT 0,             -- 创建时间（时间戳，s）
    `upd_time`           INTEGER     DEFAULT 0              -- 修改时间（时间戳，单位s）
);

-- admin
INSERT INTO `user`(`name`, `nickname`, `passwd`, `add_time`)
VALUES ('admin', 'admin', '$2a$10$ZsS2bA7B7AQtIBBpW7xz3OIw3FWU0CnXX7HZMi6vBNt9ZNcA2RNGG', STRFTIME('%s', 'now'));


-- -----------------------
-- Table structure for git
-- -----------------------
DROP TABLE IF EXISTS `git`;
CREATE TABLE `git` -- Git信息表
(
    `id`       INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `name`     VARCHAR(64) NOT NULL,              -- 名称
    `type`     TINYINT     NOT NULL,              -- 授权类型：1-用户名和密码，2-key
    `user`     VARCHAR(64)  DEFAULT '',           -- 用户
    `passwd`   VARCHAR(128) DEFAULT '',           -- 密码
    `key`      TEXT         DEFAULT '',           -- key
    `add_time` INTEGER      DEFAULT 0,            -- 创建时间（时间戳，s）
    `upd_time` INTEGER      DEFAULT 0             -- 修改时间（时间戳，s）
);


-- --------------------------
-- Table structure for server
-- --------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` -- 服务器信息表
(
    `id`       INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `name`     VARCHAR(64) NOT NULL,              -- 名称
    `host`     VARCHAR(64) NOT NULL,              -- 远程主机
    `port`     INTEGER     NOT NULL,              -- 端口
    `user`     VARCHAR(64) NOT NULL,              -- 用户
    `type`     TINYINT     NOT NULL,              -- 授权类型：1-密码，2-key
    `passwd`   VARCHAR(128) DEFAULT '',           -- 密码
    `key`      TEXT         DEFAULT '',           -- key
    `add_time` INTEGER      DEFAULT 0,            -- 创建时间（时间戳，s）
    `upd_time` INTEGER      DEFAULT 0             -- 修改时间（时间戳，s）
);


-- ------------------------
-- Table structure for item
-- ------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` -- 项目信息表
(
    `id`        INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `name`      VARCHAR(64)  NOT NULL,             -- 名称
    `git_id`    INTEGER      NOT NULL,             -- 所属Git git.id
    `repo_url`  VARCHAR(512) NOT NULL,             -- 仓库地址
    `branch`    VARCHAR(64)  NOT NULL,             -- 分支名
    `server_id` INTEGER      NOT NULL,             -- 所属服务器 server.id
    `script`    TEXT         DEFAULT '',           -- 自动部署脚本
    `secret`    VARCHAR(128) DEFAULT '',           -- Webhook密钥
    `add_time`  INTEGER      DEFAULT 0,            -- 创建时间（时间戳，s）
    `upd_time`  INTEGER      DEFAULT 0             -- 修改时间（时间戳，s）
);


-- -----------------------------
-- Table structure for user_item
-- -----------------------------
DROP TABLE IF EXISTS `user_item`;
CREATE TABLE `user_item` -- 用户-项目信息表
(
    `user_id`  INTEGER NOT NULL,  -- 用户id user.id
    `item_id`  INTEGER NOT NULL,  -- 项目id item.id
    `add_time` INTEGER DEFAULT 0, -- 创建时间（时间戳，s）
    PRIMARY KEY (`user_id`, `item_id`)
);


-- --------------------------
-- Table structure for record
-- --------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` -- 项目部署记录信息表（xx_etime = -1 时，说明xx流程发生了异常）
(
    `id`            INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `item_id`       INTEGER NOT NULL,                  -- 所属项目 item.id
    `user_id`       INTEGER      DEFAULT 0,            -- 触发用户 user.id
    `add_time`      INTEGER      DEFAULT 0,            -- 创建时间（时间戳，s）
    `pull_stime`    INTEGER      DEFAULT 0,            -- 【从远程仓库拉取代码】开始时间（时间戳，s）
    `pull_etime`    INTEGER      DEFAULT 0,            -- 【从远程仓库拉取代码】结束时间（时间戳，s）
    `pull_msg`      TEXT         DEFAULT '',           -- 【从远程仓库拉取代码】信息
    `commit_id`     VARCHAR(128) DEFAULT '',           -- 提交id
    `commit_author` VARCHAR(128) DEFAULT '',           -- 提交作者
    `commit_date`   VARCHAR(128) DEFAULT '',           -- 提交日期
    `commit_msg`    VARCHAR(512) DEFAULT '',           -- 提交信息
    `build_stime`   INTEGER      DEFAULT 0,            -- 【构建】开始时间（时间戳，s）
    `build_etime`   INTEGER      DEFAULT 0,            -- 【构建】结束时间（时间戳，s）
    `build_msg`     TEXT         DEFAULT '',           -- 【构建】信息
    `pack_stime`    INTEGER      DEFAULT 0,            -- 【打包】开始时间（时间戳，s）
    `pack_etime`    INTEGER      DEFAULT 0,            -- 【打包】结束时间（时间戳，s）
    `pack_msg`      TEXT         DEFAULT '',           -- 【打包】信息
    `upload_stime`  INTEGER      DEFAULT 0,            -- 【上传到远程主机】开始时间（时间戳，s）
    `upload_etime`  INTEGER      DEFAULT 0,            -- 【上传到远程主机】结束时间（时间戳，s）
    `upload_msg`    TEXT         DEFAULT '',           -- 【上传到远程主机】信息
    `unpack_stime`  INTEGER      DEFAULT 0,            -- 【解压缩包】开始时间（时间戳，s）
    `unpack_etime`  INTEGER      DEFAULT 0,            -- 【解压缩包】结束时间（时间戳，s）
    `unpack_msg`    TEXT         DEFAULT '',           -- 【解压缩包】信息
    `deploy_stime`  INTEGER      DEFAULT 0,            -- 【部署】开始时间（时间戳，s）
    `deploy_etime`  INTEGER      DEFAULT 0,            -- 【部署】结束时间（时间戳，s）
    `deploy_msg`    TEXT         DEFAULT ''            -- 【部署】信息
);
