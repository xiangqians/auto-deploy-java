-- ------------------------
-- Table structure for user
-- ------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` -- 用户信息表
(
    `id`                 INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `name`               VARCHAR(64)  DEFAULT '',           -- 用户名
    `nickname`           VARCHAR(64)  DEFAULT '',           -- 昵称
    `passwd`             VARCHAR(128) DEFAULT '',           -- 密码
    `locked`             TINYINT      DEFAULT 0,            -- 是否已锁定，0-否，1-是
    `try_count`          TINYINT      DEFAULT 0,            -- 尝试输入密码次数，超过3次账号将会被锁定
    `last_login_ip`      VARCHAR(64)  DEFAULT '',           -- 上一次登录ip
    `last_login_time`    INTEGER      DEFAULT 0,            -- 上一次登录时间（时间戳，单位s）
    `current_login_ip`   VARCHAR(64)  DEFAULT '',           -- 当前登录ip
    `current_login_time` INTEGER      DEFAULT 0,            -- 当前登录时间（时间戳，单位s）
    `add_time`           INTEGER      DEFAULT 0,            -- 创建时间（时间戳，单位s）
    `upd_time`           INTEGER      DEFAULT 0             -- 修改时间（时间戳，单位s）
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
    `name`     VARCHAR(64)  DEFAULT '',           -- 名称
    `user`     VARCHAR(64)  DEFAULT '',           -- 用户
    `passwd`   VARCHAR(128) DEFAULT '',           -- 密码
    `add_time` INTEGER      DEFAULT 0,            -- 创建时间（时间戳，单位s）
    `upd_time` INTEGER      DEFAULT 0             -- 修改时间（时间戳，单位s）
);


-- --------------------------
-- Table structure for server
-- --------------------------
DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` -- 服务器信息表
(
    `id`       INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `name`     VARCHAR(64)  DEFAULT '',           -- 名称
    `host`     VARCHAR(128) DEFAULT '',           -- 主机
    `port`     INTEGER      DEFAULT 22,           -- 端口
    `user`     VARCHAR(64)  DEFAULT '',           -- 用户
    `passwd`   VARCHAR(128) DEFAULT '',           -- 密码
    `add_time` INTEGER      DEFAULT 0,            -- 创建时间（时间戳，单位s）
    `upd_time` INTEGER      DEFAULT 0             -- 修改时间（时间戳，单位s）
);


-- ------------------------
-- Table structure for item
-- ------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` -- 项目信息表
(
    `id`        INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `name`      VARCHAR(64)  DEFAULT '',           -- 名称
    `git_id`    INTEGER      DEFAULT 0,            -- Git id
    `uri`       VARCHAR(512) DEFAULT '',           -- 仓库地址
    `branch`    VARCHAR(64)  DEFAULT '',           -- 分支名
    `server_id` INTEGER      DEFAULT 0,            -- 服务器id
    `script`    TEXT         DEFAULT '',           -- 自动部署脚本
    `secret`    VARCHAR(128) DEFAULT '',           -- Webhook密钥
    `add_time`  INTEGER      DEFAULT 0,            -- 创建时间（时间戳，单位s）
    `upd_time`  INTEGER      DEFAULT 0             -- 修改时间（时间戳，单位s）
);


-- -----------------------------
-- Table structure for user_item
-- -----------------------------
DROP TABLE IF EXISTS `user_item`;
CREATE TABLE `user_item` -- 用户-项目信息表
(
    `user_id`  INTEGER NOT NULL,  -- 用户id
    `item_id`  INTEGER NOT NULL,  -- 项目id
    `add_time` INTEGER DEFAULT 0, -- 创建时间（时间戳，单位s）
    PRIMARY KEY (`user_id`, `item_id`)
);


-- --------------------------
-- Table structure for record
-- --------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` -- 项目部署记录信息表
(
    `id`            INTEGER PRIMARY KEY AUTOINCREMENT, -- id
    `item_id`       INTEGER      DEFAULT 0,            -- 项目id
    `user_id`       INTEGER      DEFAULT 0,            -- 部署用户id
    `add_time`      INTEGER      DEFAULT 0,            -- 创建时间（时间戳，单位s）
    `pull_stime`    INTEGER      DEFAULT 0,            -- 【拉取】开始时间（时间戳，单位s）
    `pull_etime`    INTEGER      DEFAULT 0,            -- 【拉取】结束时间（时间戳，单位s）
    `pull_msg`      TEXT         DEFAULT '',           -- 【拉取】信息
    `pull_state`    TINYINT      DEFAULT 0,            -- 【拉取】状态，0-待拉取，1-拉取中，2-拉取成功，3-拉取失败
    `commit_id`     VARCHAR(64)  DEFAULT '',           -- 提交id
    `commit_author` VARCHAR(128) DEFAULT '',           -- 提交作者
    `commit_time`   VARCHAR(64)  DEFAULT '',           -- 提交日期
    `commit_msg`    VARCHAR(128) DEFAULT '',           -- 提交信息
    `build_stime`   INTEGER      DEFAULT 0,            -- 【构建】开始时间（时间戳，单位s）
    `build_etime`   INTEGER      DEFAULT 0,            -- 【构建】结束时间（时间戳，单位s）
    `build_msg`     TEXT         DEFAULT '',           -- 【构建】信息
    `build_state`   TINYINT      DEFAULT 0,            -- 【构建】状态，0-待构建，1-构建中，2-构建成功，3-构建失败
    `pack_stime`    INTEGER      DEFAULT 0,            -- 【压缩】开始时间（时间戳，单位s）
    `pack_etime`    INTEGER      DEFAULT 0,            -- 【压缩】结束时间（时间戳，单位s）
    `pack_msg`      TEXT         DEFAULT '',           -- 【压缩】信息
    `pack_state`    TINYINT      DEFAULT 0,            -- 【压缩】状态，0-待压缩，1-压缩中，2-压缩成功，3-压缩失败
    `upload_stime`  INTEGER      DEFAULT 0,            -- 【上传】开始时间（时间戳，单位s）
    `upload_etime`  INTEGER      DEFAULT 0,            -- 【上传】结束时间（时间戳，单位s）
    `upload_msg`    TEXT         DEFAULT '',           -- 【上传】信息
    `upload_state`  TINYINT      DEFAULT 0,            -- 【上传】状态，0-待上传，1-上传中，2-上传成功，3-上传失败
    `unpack_stime`  INTEGER      DEFAULT 0,            -- 【解压】开始时间（时间戳，单位s）
    `unpack_etime`  INTEGER      DEFAULT 0,            -- 【解压】结束时间（时间戳，单位s）
    `unpack_msg`    TEXT         DEFAULT '',           -- 【解压】信息
    `unpack_state`  TINYINT      DEFAULT 0,            -- 【解压】状态，0-待解压，1-解压中，2-解压成功，3-解压失败
    `deploy_stime`  INTEGER      DEFAULT 0,            -- 【部署】开始时间（时间戳，单位s）
    `deploy_etime`  INTEGER      DEFAULT 0,            -- 【部署】结束时间（时间戳，单位s）
    `deploy_msg`    TEXT         DEFAULT '',           -- 【部署】信息
    `deploy_state`  TINYINT      DEFAULT 0             -- 【部署】状态，0-待部署，1-部署中，2-部署成功，3-部署失败
);
