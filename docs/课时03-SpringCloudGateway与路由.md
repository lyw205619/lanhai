# 课时 03：Spring Cloud Gateway 与路由

## 学习目标

1. 能默写 **网关监听端口**、**Nacos 地址** 在 yml 中的 **完整配置路径**。  
2. **逐条解释** `spring.cloud.gateway.routes` 中 **每一条** route 的 **id、uri、predicates**。  
3. 说明 **`lb://服务名`** 与 **Nacos**、**LoadBalancer** 的关系。  
4. 解释 **`discovery.locator.enabled: true`** 的 **作用与副作用**（与显式 routes 并存）。  
5. 配置 **全局 CORS** 时，各字段 **allowedOriginPatterns / allowedMethods / maxAge** 的含义。

---

## 1. 模块与入口

| 项 | 值 |
|----|-----|
| 模块 | `lanhai-server-gateway` |
| 主类 | `com.liyiwei.lanhai.gateway.GatewayApplication` |
| 激活配置 | `spring.profiles.active: dev`（见 `application.yml`） |
| 实际端口 | `application-dev.yml` 中 **`server.port: 8500`** |

---

## 2. 核心配置片段（摘自源码）

文件：`lanhai-server-gateway/src/main/resources/application-dev.yml`

### 2.1 Nacos 注册

```yaml
spring:
  application:
    name: lanhai-server-gateway
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
```

**网关自身**也会注册到 Nacos（便于 **运维监控** 与其它服务 **发现网关**）；若你 **不需要** 网关被其它服务调用，**仍建议注册** 以统一治理。

### 2.2 动态路由定位器

```yaml
gateway:
  discovery:
    locator:
      enabled: true
```

**含义**：根据 **Nacos 注册的服务名** **自动生成** 路由规则（常见约定：`/{serviceId}/**`）。  
**注意**：与下面 **显式 `routes`** **同时存在** 时，**匹配优先级** 与 **Predicate 顺序** 需以 **Spring Cloud Gateway 文档** 为准；联调时若出现 **意外转发**，优先检查 **显式 routes 是否覆盖**。

### 2.3 全局跨域（globalcors）

```yaml
globalcors:
  cors-configurations:
    '[/**]':
      allowedOriginPatterns: "*"
      allowedHeaders: "*"
      allowedMethods: "*"
      maxAge: 36000
```

| 字段 | 作用 |
|------|------|
| `allowedOriginPatterns` | Spring 5.3+ 推荐替代 `allowedOrigins`，支持通配；**生产**应收紧为 **具体前端域名** |
| `allowedHeaders` | 允许 **自定义头**（如 `token`） |
| `allowedMethods` | GET/POST/OPTIONS 等 |
| `maxAge` | 预检 **OPTIONS** 结果缓存秒数，减少预检请求 |

---

## 3. 显式 routes 逐条说明

| id | uri | Path Predicate | 含义 |
|----|-----|------------------|------|
| service-product | `lb://service-product` | `/*/product/**` | 任意第一段后接 **/product/** 的路径（如 `/api/product/...`） |
| service-user | `lb://service-user` | `/*/user/**` | 同理，**user** 模块 |
| service-cart | `lb://service-cart` | `/api/order/cart/**` | **购物车** 固定前缀 |
| service-order | `lb://service-order` | `/api/order/orderInfo/**` | **订单** 前缀 |
| service-pay | `lb://service-pay` | `/api/order/alipay/**` | **支付宝** 相关 |
| service-ai | `lb://service-ai` | `/api/ai/**` | **AI 助手**：HTTP 转发本机 **Ollama**（`/api/chat` 等） |

**`lb://` 解析**：

1. `lb` = **LoadBalancer**（Spring Cloud）。  
2. 从 **Nacos** 拉取 **`service-product` 等** 实例列表。  
3. **轮询 / 随机** 等策略选择一个 **IP:端口**（默认 **RoundRobin**）。

**若 Nacos 中无该服务**：`lb` **无实例** → **503** 或 **Connection refused** 类错误。

---

## 4. Redis

网关 `application-dev.yml` 中：

```yaml
spring:
  data:
    redis:
      port: 6379
      host: localhost
```

供 **`AuthGlobalFilter`** 使用 **`RedisTemplate`** 读取 **`user:lanhai:{token}`**（详见课时 05）。

---

## 5. 调试技巧

| 场景 | 做法 |
|------|------|
| 看 **请求是否到达网关** | 网关加 **日志** 或 **Actuator**（若引入） |
| 看 **路由命中哪条** | 开启 **DEBUG** `org.springframework.cloud.gateway` |
| 直连下游 **绕过网关** | 浏览器访问 **8511～8516**（仅本地联调，**生产禁止**） |

---

## 6. 易错点

| 现象 | 可能原因 |
|------|----------|
| 404 | **Path** 与 **Controller** 前缀 **不一致**；或 **未带** 网关前缀 |
| 503 | **Nacos 无实例**、服务 **未启动** |
| CORS 失败 | **预检** 未放行；**allowedOriginPatterns** 过严 |

---

## 7. 本课小结

- **8500** 是 C 端 **统一入口**；**routes** 决定 **流量落点**。  
- **`lb://` + Nacos** 是 **服务发现** 的标准组合。

---

## 8. 自测与扩展

1. 在 Nacos **下线** `service-product`，再访问商品接口，观察 **HTTP 状态码**。  
2. 阅读 Spring 文档：**GatewayPredicate** 的 **Path** 与 **正则** 写法。  
3. **扩展**：为 **生产环境** 写一份 **收紧 CORS** 的 `application-prod.yml` 片段（不写真实域名可用占位符）。
