-- 蓝海商城 lanhai：由仓库内实体与 MyBatis Mapper 反向整理的建库建表脚本
-- MySQL 8.x，字符集 utf8mb4

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS db_lanhai
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE db_lanhai;

-- ========== 商品域 ==========

CREATE TABLE IF NOT EXISTS brand (
  id            BIGINT       NOT NULL PRIMARY KEY COMMENT '主键（业务侧雪花等）',
  name          VARCHAR(255) NOT NULL DEFAULT '',
  logo          VARCHAR(512)          DEFAULT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌';

CREATE TABLE IF NOT EXISTS category (
  id            BIGINT       NOT NULL PRIMARY KEY,
  name          VARCHAR(255) NOT NULL DEFAULT '',
  image_url     VARCHAR(512)          DEFAULT NULL,
  parent_id     BIGINT                NOT NULL DEFAULT 0,
  status        TINYINT               NOT NULL DEFAULT 1,
  order_num     INT                   NOT NULL DEFAULT 0,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类';

CREATE TABLE IF NOT EXISTS category_brand (
  id            BIGINT       NOT NULL PRIMARY KEY,
  brand_id      BIGINT       NOT NULL,
  category_id   BIGINT       NOT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0,
  KEY idx_cb_brand (brand_id),
  KEY idx_cb_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类-品牌关联';

CREATE TABLE IF NOT EXISTS product (
  id                   BIGINT          NOT NULL PRIMARY KEY,
  name                 VARCHAR(512)    NOT NULL DEFAULT '',
  brand_id             BIGINT          NOT NULL,
  category1_id         BIGINT          NOT NULL,
  category2_id         BIGINT          NOT NULL,
  category3_id         BIGINT          NOT NULL,
  unit_name            VARCHAR(64)              DEFAULT NULL,
  slider_urls          TEXT                     DEFAULT NULL,
  spec_value           TEXT                     DEFAULT NULL,
  status               INT                      DEFAULT 0,
  audit_status         INT                      DEFAULT 0,
  audit_message        VARCHAR(512)             DEFAULT NULL,
  create_time          DATETIME                 DEFAULT CURRENT_TIMESTAMP,
  update_time          DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted           TINYINT                  NOT NULL DEFAULT 0,
  KEY idx_product_brand (brand_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SPU';

CREATE TABLE IF NOT EXISTS product_sku (
  id            BIGINT          NOT NULL PRIMARY KEY,
  sku_code      VARCHAR(128)             DEFAULT NULL,
  sku_name      VARCHAR(512)    NOT NULL DEFAULT '',
  product_id    BIGINT          NOT NULL,
  thumb_img     VARCHAR(512)             DEFAULT NULL,
  sale_price    DECIMAL(18,2)            DEFAULT NULL,
  market_price  DECIMAL(18,2)            DEFAULT NULL,
  cost_price    DECIMAL(18,2)            DEFAULT NULL,
  stock_num     INT                      DEFAULT 0,
  sale_num      INT                      DEFAULT 0,
  sku_spec      VARCHAR(1024)            DEFAULT NULL,
  weight        VARCHAR(64)              DEFAULT NULL,
  volume        VARCHAR(64)              DEFAULT NULL,
  status        INT                      DEFAULT 0,
  create_time   DATETIME                 DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT                  NOT NULL DEFAULT 0,
  KEY idx_sku_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='SKU';

CREATE TABLE IF NOT EXISTS product_details (
  id            BIGINT       NOT NULL PRIMARY KEY,
  product_id    BIGINT       NOT NULL,
  image_urls    TEXT                  DEFAULT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0,
  KEY idx_pd_product (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品详情图';

CREATE TABLE IF NOT EXISTS product_spec (
  id            BIGINT       NOT NULL PRIMARY KEY,
  spec_name     VARCHAR(255)          DEFAULT NULL,
  spec_value    VARCHAR(1024)         DEFAULT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='规格字典';

CREATE TABLE IF NOT EXISTS product_unit (
  id            BIGINT       NOT NULL PRIMARY KEY,
  name          VARCHAR(128) NOT NULL DEFAULT '',
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='计量单位';

-- ========== C 端用户 ==========

CREATE TABLE IF NOT EXISTS user_info (
  id              BIGINT       NOT NULL PRIMARY KEY,
  username        VARCHAR(128) NOT NULL,
  password        VARCHAR(255)          DEFAULT NULL,
  nick_name       VARCHAR(255)          DEFAULT NULL,
  avatar          VARCHAR(512)          DEFAULT NULL,
  sex             TINYINT               DEFAULT NULL,
  phone           VARCHAR(32)           DEFAULT NULL,
  memo            VARCHAR(512)          DEFAULT NULL,
  open_id         VARCHAR(128)          DEFAULT NULL,
  union_id        VARCHAR(128)          DEFAULT NULL,
  last_login_ip   VARCHAR(64)           DEFAULT NULL,
  last_login_time DATETIME              DEFAULT NULL,
  status          TINYINT               DEFAULT 1,
  create_time     DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time     DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted      TINYINT               NOT NULL DEFAULT 0,
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='C端用户';

CREATE TABLE IF NOT EXISTS user_address (
  id            BIGINT       NOT NULL PRIMARY KEY,
  user_id       BIGINT       NOT NULL,
  name          VARCHAR(128)          DEFAULT NULL,
  phone         VARCHAR(32)           DEFAULT NULL,
  tag_name      VARCHAR(64)           DEFAULT NULL,
  province_code VARCHAR(32)           DEFAULT NULL,
  city_code     VARCHAR(32)           DEFAULT NULL,
  district_code VARCHAR(32)           DEFAULT NULL,
  address       VARCHAR(512)          DEFAULT NULL,
  full_address  VARCHAR(1024)         DEFAULT NULL,
  is_default    TINYINT               DEFAULT 0,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0,
  KEY idx_addr_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址';

-- ========== 订单 / 支付 ==========

CREATE TABLE IF NOT EXISTS order_info (
  id                     BIGINT          NOT NULL PRIMARY KEY,
  user_id                BIGINT          NOT NULL,
  nick_name              VARCHAR(255)             DEFAULT NULL,
  order_no               VARCHAR(64)     NOT NULL,
  coupon_id              BIGINT                   DEFAULT NULL,
  total_amount           DECIMAL(18,2)            DEFAULT NULL,
  coupon_amount          DECIMAL(18,2)            DEFAULT NULL,
  original_total_amount  DECIMAL(18,2)            DEFAULT NULL,
  feight_fee             DECIMAL(18,2)            DEFAULT NULL,
  pay_type               INT                      DEFAULT NULL,
  order_status           INT                      DEFAULT NULL,
  receiver_name          VARCHAR(128)             DEFAULT NULL,
  receiver_phone         VARCHAR(32)              DEFAULT NULL,
  receiver_tag_name      VARCHAR(64)              DEFAULT NULL,
  receiver_province      VARCHAR(64)              DEFAULT NULL,
  receiver_city          VARCHAR(64)              DEFAULT NULL,
  receiver_district      VARCHAR(64)              DEFAULT NULL,
  receiver_address       VARCHAR(512)             DEFAULT NULL,
  payment_time           DATETIME                 DEFAULT NULL,
  delivery_time          DATETIME                 DEFAULT NULL,
  receive_time           DATETIME                 DEFAULT NULL,
  remark                 VARCHAR(512)             DEFAULT NULL,
  cancel_time            DATETIME                 DEFAULT NULL,
  cancel_reason          VARCHAR(512)             DEFAULT NULL,
  create_time            DATETIME                 DEFAULT CURRENT_TIMESTAMP,
  update_time            DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted             TINYINT                  NOT NULL DEFAULT 0,
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_order_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单';

CREATE TABLE IF NOT EXISTS order_item (
  id            BIGINT          NOT NULL PRIMARY KEY,
  order_id      BIGINT          NOT NULL,
  sku_id        BIGINT          NOT NULL,
  sku_name      VARCHAR(512)             DEFAULT NULL,
  thumb_img     VARCHAR(512)             DEFAULT NULL,
  sku_price     DECIMAL(18,2)            DEFAULT NULL,
  sku_num       INT                      DEFAULT 0,
  create_time   DATETIME                 DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT                  NOT NULL DEFAULT 0,
  KEY idx_item_order (order_id),
  KEY idx_item_sku (sku_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细';

CREATE TABLE IF NOT EXISTS order_log (
  id              BIGINT       NOT NULL PRIMARY KEY,
  order_id        BIGINT       NOT NULL,
  operate_user    VARCHAR(128)          DEFAULT NULL,
  process_status  INT                   DEFAULT NULL,
  note            VARCHAR(1024)         DEFAULT NULL,
  create_time     DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time     DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted      TINYINT               NOT NULL DEFAULT 0,
  KEY idx_olog_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单日志';

CREATE TABLE IF NOT EXISTS order_statistics (
  id            BIGINT          NOT NULL PRIMARY KEY,
  order_date    DATE            NOT NULL,
  total_amount  DECIMAL(18,2)            DEFAULT NULL,
  total_num     INT                      DEFAULT NULL,
  create_time   DATETIME                 DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT                  NOT NULL DEFAULT 0,
  KEY idx_os_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单统计';

CREATE TABLE IF NOT EXISTS payment_info (
  id                BIGINT          NOT NULL PRIMARY KEY,
  user_id           BIGINT                   DEFAULT NULL,
  order_no          VARCHAR(64)     NOT NULL,
  pay_type          INT                      DEFAULT NULL,
  out_trade_no      VARCHAR(128)             DEFAULT NULL,
  amount            DECIMAL(18,2)            DEFAULT NULL,
  content           VARCHAR(1024)            DEFAULT NULL,
  payment_status    TINYINT                  NOT NULL DEFAULT 0,
  callback_time     DATETIME                 DEFAULT NULL,
  callback_content  TEXT                     DEFAULT NULL,
  create_time       DATETIME                 DEFAULT CURRENT_TIMESTAMP,
  update_time       DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted        TINYINT                  NOT NULL DEFAULT 0,
  KEY idx_pay_order (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付单';

-- ========== 管理端系统 ==========

CREATE TABLE IF NOT EXISTS sys_user (
  id            BIGINT       NOT NULL PRIMARY KEY,
  username      VARCHAR(128) NOT NULL,
  password      VARCHAR(255)          DEFAULT NULL,
  name          VARCHAR(128)          DEFAULT NULL,
  phone         VARCHAR(32)           DEFAULT NULL,
  avatar        VARCHAR(512)          DEFAULT NULL,
  description   VARCHAR(512)          DEFAULT NULL,
  status        TINYINT               DEFAULT 1,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='后台用户';

CREATE TABLE IF NOT EXISTS sys_role (
  id            BIGINT       NOT NULL PRIMARY KEY,
  role_name     VARCHAR(128) NOT NULL DEFAULT '',
  role_code     VARCHAR(128)          DEFAULT NULL,
  description   VARCHAR(512)          DEFAULT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';

CREATE TABLE IF NOT EXISTS sys_menu (
  id            BIGINT       NOT NULL PRIMARY KEY,
  parent_id     BIGINT                NOT NULL DEFAULT 0,
  title         VARCHAR(255) NOT NULL DEFAULT '',
  component     VARCHAR(512)          DEFAULT NULL,
  sort_value    INT                   DEFAULT 0,
  status        TINYINT               DEFAULT 1,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单';

CREATE TABLE IF NOT EXISTS sys_role_menu (
  id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  role_id       BIGINT       NOT NULL,
  menu_id       BIGINT       NOT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0,
  is_half       TINYINT               NOT NULL DEFAULT 0,
  UNIQUE KEY uk_role_menu (role_id, menu_id),
  KEY idx_rm_menu (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色-菜单';

CREATE TABLE IF NOT EXISTS sys_user_role (
  id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id       BIGINT       NOT NULL,
  role_id       BIGINT       NOT NULL,
  create_time   DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time   DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted    TINYINT               NOT NULL DEFAULT 0,
  UNIQUE KEY uk_user_role (user_id, role_id),
  KEY idx_ur_role (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户-角色';

CREATE TABLE IF NOT EXISTS sys_oper_log (
  id              BIGINT       NOT NULL PRIMARY KEY,
  title           VARCHAR(255)          DEFAULT NULL,
  method          VARCHAR(255)          DEFAULT NULL,
  request_method  VARCHAR(32)           DEFAULT NULL,
  business_type   INT                   DEFAULT NULL,
  operator_type   VARCHAR(32)           DEFAULT NULL,
  oper_name       VARCHAR(128)          DEFAULT NULL,
  oper_url        VARCHAR(512)          DEFAULT NULL,
  oper_ip         VARCHAR(64)           DEFAULT NULL,
  oper_param      TEXT                  DEFAULT NULL,
  json_result     TEXT                  DEFAULT NULL,
  status          TINYINT               DEFAULT 0,
  error_msg       VARCHAR(1024)         DEFAULT NULL,
  create_time     DATETIME              DEFAULT CURRENT_TIMESTAMP,
  update_time     DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  is_deleted      TINYINT               NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志';

SET FOREIGN_KEY_CHECKS = 1;
