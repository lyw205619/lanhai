# 课时 04：Nacos 与 OpenFeign

## 学习目标

1. 说明 **Nacos Discovery** 在本项目中的 **最小配置项**（`server-addr`、`application.name`）。  
2. 解释 **服务注册** 与 **心跳** 的 **粗粒度原理**（面试够用）。  
3. 能打开 **`lanhai-service-client`** 下任一 **Feign 接口**，指出 **`@FeignClient` 的 name** 与 **Nacos 服务名** 的对应关系。  
4. 说明 **OpenFeign** + **Spring Cloud LoadBalancer** 如何 **替代** 已停更的 **Ribbon**。  
5. 列出 **本地联调** 时 **必须先启动** 的中间件顺序。

---

## 1. Nacos 是什么（本项目用法）

在本仓库中，**Nacos** 主要承担 **服务发现（Service Discovery）**：

- 各微服务启动时向 **Nacos Server** 注册：**服务名 + IP + 端口 + 元数据**。  
- **Gateway** 使用 **`lb://服务名`** 时，向 **Nacos** 查询 **健康实例**，再 **负载均衡** 转发。

**配置中心（Config）**：父工程 `lanhai-service/pom.xml` 中 **Nacos Config** 依赖曾 **注释掉**——即 **配置仍以本地 yml 为主**；若需 **集中配置**，可解除注释并加 **dataId**。

---

## 2. 最小配置模板（每个微服务）

典型结构（以 product 为例）：

```yaml
spring:
  application:
    name: service-product   # 即 Nacos 中的「服务名」，须全局唯一
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

| 配置项 | 含义 |
|--------|------|
| `spring.application.name` | **注册名**，须与 **Gateway `lb://` 名称** 完全一致 |
| `server-addr` | Nacos **地址**；集群用 **逗号分隔** |

---

## 3. 控制台操作（建议动手）

1. 浏览器打开 **`http://localhost:8848/nacos`**（默认账号密码见 Nacos 版本文档，常为 **nacos/nacos**）。  
2. **服务管理 → 服务列表**，查看 **已注册** 的 **服务名**、**实例数**、**健康状态**。  
3. **下线/上线** 某个实例，观察 **网关** 行为。

---

## 4. OpenFeign 与 lanhai-service-client

### 4.1 模块结构

`lanhai-service-client` 为 **pom 聚合**，子模块包括：

- `service-product-client`  
- `service-user-client`  
- `service-cart-client`  
- `service-order-client`  

（**pay** 是否单独 client 以仓库为准。）

### 4.2 依赖要点（父 client POM）

- **`spring-cloud-starter-openfeign`**：声明式 HTTP 客户端。  
- **`spring-cloud-starter-loadbalancer`**：与 **Spring Cloud 2020+** 配套，**替代 Ribbon**。  
- **`lanhai-model`、`common-util`**：通常 **provided**，由 **调用方服务** 最终打包带入。

### 4.3 使用方式（概念）

在 **需要调用** 其他服务的模块上：

1. **`@EnableFeignClients`**（主类或配置类）。  
2. 注入 **`XxxClient`** 接口，调用方法即 **发 HTTP**。

**`@FeignClient(name = "service-order")`**：

- **name** 必须等于 **被调用方** 的 **`spring.application.name`**。  
- Feign 从 **Nacos** 解析 **service-order** 的 **实例地址**。

---

## 5. 与 RestTemplate / WebClient 对比

| 方式 | 优点 | 缺点 |
|------|------|------|
| **Feign** | **接口即契约**、与 **熔断**（若接 Sentinel）集成好 | 需 **维护 client 模块** |
| **RestTemplate** | 简单 | **模板代码多**、**负载均衡**需手动 **@LoadBalanced** |
| **WebClient** | **响应式** | 学习曲线 |

---

## 6. 联调顺序（再强调）

1. **Nacos**（否则 **注册失败** / **lb 无实例**）  
2. **Redis**（网关鉴权依赖）  
3. **MySQL**  
4. **各业务服务**（按需）  
5. **Gateway**  
6. **Manager**（若测后台）

---

## 7. 易错点

| 问题 | 原因 |
|------|------|
| **UnknownHost service-xxx** | **name** 拼写与 Nacos **不一致** |
| **调用总是同一台** | **实例数为 1** 或 **负载均衡策略** 固定 |
| **Read timed out** | **被调用方慢** 或 **Feign 超时** 过短 |

---

## 8. 本课小结

- **Nacos** = **电话簿**；**Feign** = **按名字拨号** 的 **HTTP 封装**。  
- **服务名** 全链路 **必须一致**：**yml name** = **lb://** = **@FeignClient name**。

---

## 9. 自测与扩展

1. 在代码中 **全局搜索** `@FeignClient`，列出 **所有 name**。  
2. 在 Nacos **删除** 某服务全部实例，再 Feign 调用，观察 **异常类型**。  
3. **扩展**：阅读 **Spring Cloud LoadBalancer** 的 **自定义 LoadBalancer** 配置类写法。
