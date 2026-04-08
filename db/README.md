# 数据库脚本说明

## 文件

| 文件 | 说明 |
|------|------|
| [schema.sql](schema.sql) | 创建库 **`db_lanhai`** 及本项目用到的 **全部业务表**（由实体与 MyBatis XML **反向整理**，供本地从零初始化）。 |

## 导入（MySQL 8）

在 MySQL 客户端执行：

```bash
mysql -u root -p < db/schema.sql
```

或在 **Navicat / DBeaver** 中打开 `schema.sql` 执行。

导入后请将各模块 `application-dev.yml` 中的 **库名、账号、密码** 与本地一致。

## 说明

- 表结构以当前仓库 **Mapper / 实体** 为准；若你本地改过字段，请以实际为准并同步修改本脚本。  
- **不含** 业务演示数据；需要可自行 `INSERT`。  
- **购物车** 在 `service-cart` 中主要使用 **Redis**，不依赖购物车表。
