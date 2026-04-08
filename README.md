# 蓝海商城（lanhai）

基于 **Spring Boot 3**、**Java 17**、**Spring Cloud Alibaba** 的微服务商城后端：包含 **Spring Cloud Gateway**、**Nacos**、**商品 / 用户 / 购物车 / 订单 / 支付** 等微服务，以及可选的 **service-ai**（HTTP 转发本机 **Ollama**）；**管理端** 独立部署；集成 **Redis**、**MyBatis**、**支付宝**、**MinIO** 等。

## 文档

系统化讲解（学习目标、源码导读、表格、时序图、mermaid、易错点、自测与面试）见：

**[docs/README.md](docs/README.md)**

用 **IntelliJ IDEA** 从打开工程到启动、测试的逐步说明见：**[docs/IDEA本地运行与测试.md](docs/IDEA本地运行与测试.md)**。

共 **13 课时** + **索引**，覆盖架构、Maven、网关路由、Nacos/Feign、鉴权、业务拆分、订单与支付链路、支付宝回调、管理端与 MinIO、公共模块、本地联调、面试要点，以及 **AI 助手与 Ollama**（见 `docs/课时13-AI助手与Ollama接入.md`）。

## 数据库初始化

建库建表脚本见 **`db/schema.sql`**，说明见 **`db/README.md`**（MySQL 8 下执行即可得到 **`db_lanhai`** 及全部业务表）。

## 构建

在仓库根目录（含 `pom.xml`）执行：

```bash
mvn clean install -DskipTests
```

## 运行依赖（本地）

- **MySQL**（如库名 `db_lanhai`）、**Redis**、**Nacos**（默认 `8848`）。  
- 使用 **支付**、**上传** 时需配置 **支付宝密钥**、**MinIO** 等（详见 `docs/课时11-本地运行与联调.md`）。  
- 使用 **AI 助手（service-ai）** 时需本机 **[Ollama](https://ollama.com)** 运行（默认 `http://127.0.0.1:11434`），并 **`ollama pull`** 与配置一致的模型（见 `lanhai-service/service-ai/.../application-dev.yml` 中的 **`lanhai.ollama.model`**）。

## 开发环境端口速查（默认 dev）

| 进程 | 端口 |
|------|------|
| Gateway | 8500 |
| Manager | 8501 |
| service-product | 8511 |
| service-user | 8512 |
| service-cart | 8513 |
| service-order | 8514 |
| service-pay | 8515 |
| service-ai | 8516 |

以各模块 `application-dev.yml` 为准。
