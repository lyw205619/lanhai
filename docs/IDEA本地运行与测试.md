# IntelliJ IDEA：运行与测试（lanhai）

本文说明在 **IntelliJ IDEA（以下简称 IDEA）** 中如何 **导入 lanhai、配置 JDK/Maven、初始化数据库、按顺序启动各进程**，以及 **如何自测**（网关、管理端、可选 AI）。与 [课时11-本地运行与联调.md](课时11-本地运行与联调.md) 互补：**课时 11** 偏原理与排错，**本文** 逐步对应 IDEA 菜单与点击路径。

---

## 1. 文档是否齐全？

| 内容 | 位置 |
|------|------|
| 架构、端口、Maven、网关、业务拆分、支付、管理端等 | `docs/` 下 **课时 01～13** 与 [README.md](README.md) 索引 |
| 数据库建库建表 | 仓库根 [`db/schema.sql`](../db/schema.sql)、[`db/README.md`](../db/README.md) |
| AI（Ollama）专项 | [课时13-AI助手与Ollama接入.md](课时13-AI助手与Ollama接入.md) |
| **本文** | IDEA **从打开工程到运行、验证** 的操作手册 |

---

## 2. 本机依赖清单

| 依赖 | 说明 |
|------|------|
| **JDK 17** | IDEA：`File` → `Project Structure` → `Project SDK` 选 **17**。 |
| **Maven 3.6+** | 可用 IDEA **Bundled Maven**：`Settings` → `Build Tools` → `Maven`。 |
| **MySQL 8** | 已启动；导入 [`db/schema.sql`](../db/schema.sql) 得到库 **`db_lanhai`**。 |
| **Redis** | 默认 **6379**（网关鉴权、业务缓存）。 |
| **Nacos 2.x** | 默认 **8848**，须先于各微服务启动。 |
| **MinIO**（可选） | 管理端上传，默认 **9000**，见 `lanhai-manager` 的 `application-dev.yml`。 |
| **Ollama**（可选） | 仅跑 **service-ai** 时需要，默认 **11434**，见 [课时13](课时13-AI助手与Ollama接入.md)。 |

---

## 3. 第一次：数据库

1. 用 MySQL 客户端执行仓库根 **[`db/schema.sql`](../db/schema.sql)**（或命令行：`mysql -u root -p < db/schema.sql`，路径按本机调整）。  
2. 打开各模块 **`src/main/resources/application-dev.yml`**，把 **JDBC 的 `url` / `username` / `password`** 改成与你本机 **一致**（**勿把真实密码提交到公开仓库**）。  
3. 根目录执行一次 **`mvn clean install -DskipTests`**（见下一节），保证多模块 jar 已安装到本地 `~/.m2`。

---

## 4. 用 IDEA 打开工程

1. `File` → `Open`，选择 **`lanhai` 根目录**（包含根 **`pom.xml`** 的文件夹）。  
2. 提示 **Trust Project** 则信任；提示 **Import Maven Project** 选 **Import** / **Enable Auto-Import**。  
3. 等待 **索引与依赖下载** 完成（首次需联网）。

**若子模块报「找不到 com.liyiwei:xxx」**：右侧 **Maven** 工具窗口 → 根 **`lanhai`** → **Lifecycle** → 双击 **`install`**（或终端在根目录执行 `mvn clean install -DskipTests`）。

**JDK / Maven 建议**：

| 项 | 路径 | 建议 |
|----|------|------|
| 项目 SDK | `File` → `Project Structure` → `Project` | **17** |
| Maven JDK | `Settings` → `Build Tools` → `Maven` → `Importing` | **17** |
| Maven 主目录 | 同上 → `Maven home path` | 本机 Maven 或 **Bundled** |

---

## 5. 启动顺序（务必遵守）

| 顺序 | 进程 | 说明 |
|------|------|------|
| 1 | **MySQL** | 库表已导入 |
| 2 | **Redis** | |
| 3 | **Nacos** | **必须先于** 网关与各微服务 |
| 4 | **MinIO** | 仅测管理端上传时 |
| 5 | **业务微服务** | `service-product` … `service-pay`，按需启动；全测则全起 |
| （可选） | **service-ai** | 需本机 **Ollama** |
| 6 | **lanhai-server-gateway** | C 端入口 **8500** |
| 7 | **lanhai-manager** | 管理端 **8501** |

**原因**：无 **Nacos** → 网关 **`lb://`** 无实例；无 **Redis** → 网关 **`/api/**/auth/**`** 鉴权不可用。

---

## 6. 在 IDEA 里添加 Spring Boot 运行配置

对每个可运行模块：**打开对应的 `*Application.java`** → 类左侧 **绿色三角** → **`Run '…Application'`**。首次若问 **Use classpath of module**，选择 **与目录一致的模块**（例如 `lanhai-server-gateway`）。

建议 **`Run` → `Edit Configurations...`** 里把配置 **改名**，便于辨认，例如：

| 显示名 | 主类（示例路径） | 典型端口 |
|--------|------------------|----------|
| `gateway` | `lanhai-server-gateway/.../GatewayApplication.java` | 8500 |
| `manager` | `lanhai-manager/.../ManagerApplication.java` | 8501 |
| `product` | `service-product/.../ProductApplication.java` | 8511 |
| `user` | `service-user/.../UserApplication.java` | 8512 |
| `cart` | `service-cart/.../CartApplication.java` | 8513 |
| `order` | `service-order/.../OrderApplication.java` | 8514 |
| `pay` | `service-pay/.../PayApplication.java` | 8515 |
| `ai` | `service-ai/.../AiApplication.java` | 8516 |

**VM options（可选）**：`-Dspring.profiles.active=dev`（若未在 `application.yml` 里写死 `active`）。

**Compound（一键多进程）**：`Edit Configurations` → **`+`** → **`Compound`**，把 **`gateway`、`manager`** 等你常一起起的配置加进去。**注意**：仍须先手动起 **中间件**；Compound **不会**替你启动 MySQL/Redis/Nacos。

---

## 7. 怎么测试（最小验证）

### 7.1 Nacos

浏览器打开 **`http://localhost:8848/nacos`**（账号密码以你安装的 Nacos 为准，常见为账号 **nacos**、密码 **nacos**）。**服务列表** 中应能看到 **`service-product`、`lanhai-server-gateway`** 等（取决于你已启动的进程）。

### 7.2 Redis

终端执行：`redis-cli ping` → 应返回 **`PONG`**。

### 7.3 网关是否起来

浏览器或 curl 访问 **`http://localhost:8500`**（具体路径因路由而异，能连上进程即可）。

### 7.4 管理端

访问 **`http://localhost:8501`**（具体登录路径以前端/接口为准；白名单见 manager 的 `lanhai.auth.noAuthUrls`）。

### 7.5 C 端接口（经网关）

示例（**AI**，需 **service-ai + Ollama + 网关 + Nacos**）：

```bash
curl -s -X POST "http://localhost:8500/api/ai/chat" -H "Content-Type: application/json" -d "{\"message\":\"你好\"}"
```

更多说明见 [课时13-AI助手与Ollama接入.md](课时13-AI助手与Ollama接入.md)。

### 7.6 需登录的接口

网关对 **`/api/**/auth/**`** 校验 **Header：`token`**，且 Redis 中存在对应会话。测试前需先走 **用户登录** 拿到 token，再带 token 调业务接口（详见 [课时05-网关鉴权与Redis会话.md](课时05-网关鉴权与Redis会话.md)）。

### 7.7 支付 / 支付宝

需配置密钥与可达的 **异步通知地址**；沙箱与联调步骤见 [课时08-支付宝接入与回调.md](课时08-支付宝接入与回调.md)。

---

## 8. 常见问题（速查）

| 现象 | 处理 |
|------|------|
| 模块报红、找不到依赖 | 根目录 **`mvn clean install -DskipTests`**，IDEA **Maven → Reload**。 |
| **503 / No instances** | **Nacos** 是否启动；对应微服务是否已启动并注册。 |
| **连接不上 MySQL** | **`application-dev.yml`** 库名、账号密码、端口是否与 **`db/schema.sql`** 导入环境一致。 |
| **登录后仍 401** | **Redis** 是否启动；请求头是否带 **`token`**。 |

更长的排错表见 [课时11-本地运行与联调.md](课时11-本地运行与联调.md) 第六节。

---

## 9. 小结

- **文档**：体系化内容在 **`docs/README.md`**；**数据库**在 **`db/`**；**IDEA 实操**以 **本文 + 课时 11** 为准。  
- **运行**：**中间件 → 微服务 → 网关 → 管理端**；可选 **service-ai + Ollama**。  
- **测试**：先 **Nacos / Redis**，再 **网关与管理端**，业务与支付按课时文档深入。
