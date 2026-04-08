# 课时 05：网关鉴权与 Redis 会话

## 学习目标

1. **逐段读懂** `AuthGlobalFilter` 的 **`filter` / `getUserInfo` / `out`** 方法。  
2. 说明 **哪些 URL** 会触发登录校验，**哪些** 直接放行。  
3. 解释 **Redis 键** `user:lanhai:{token}` 的 **设计意图** 与 **失效场景**。  
4. 说明 **网关返回 JSON** 时 **Content-Type** 与 **编码** 的处理。  
5. 对比 **Session**、**JWT 无状态**、**Token+Redis** 三种常见方案（口述）。

---

## 1. 类位置与依赖

| 项 | 值 |
|----|-----|
| 类全名 | `com.liyiwei.lanhai.gateway.filter.AuthGlobalFilter` |
| 实现接口 | `GlobalFilter`（Gateway）、`Ordered`（顺序） |
| 注解 | `@Component` — 由 Spring 扫描注册为 **全局过滤器** |
| Redis | `@Autowired RedisTemplate<String, String>` |

**依赖模块**：`lanhai-model` 中的 **`UserInfo`**、**`Result`**、**`ResultCodeEnum`**；JSON 使用 **Fastjson / Fastjson2**（源码中同时出现 `JSONObject` 与 `JSON`，以 **统一风格** 为后续重构点）。

---

## 2. 过滤器核心逻辑（逐步）

### 2.1 `getOrder()` 返回 0

**数字越小越靠前**（在 **同类型全局过滤器** 中）。若后续增加 **限流、日志 ID**，注意 **顺序** 约定。

### 2.2 `filter(ServerWebExchange exchange, GatewayFilterChain chain)`

1. **`request.getRequest().getURI().getPath()`** 得到 **路径字符串**（不含域名）。  
2. **`AntPathMatcher.match("/api/**/auth/**", path)`**  
   - **为 true**：该请求 **需要登录**。  
   - **为 false**：**不校验**，直接 **`chain.filter(exchange)`** 放行。

**路径模式说明**：

- `/api/**/auth/**` 表示：**第一段为 api**，中间 **任意多级**，再出现 **auth** 段，后面还有 **子路径**。  
- **示例匹配**：`/api/order/auth/xxx`、`/api/user/auth/profile`（具体以 **Ant 规则** 为准，建议 **用单元测试** 或 **日志打印 path** 验证）。

**常见误解**：注释里写「登录校验」但模式是 **`/api/**/auth/**`**，**不是** 所有 `/api/**` 都校验——**仅匹配该模式的路径**。

### 2.3 需登录时的 `getUserInfo(request)`

1. **Header `token`**：`request.getHeaders().get("token")`，取 **第一个** 值。  
2. 若 **token 为空字符串**：**返回 null** → 视为未登录。  
3. 若 **非空**：**Redis GET** 键 **`user:lanhai:" + token`**。  
4. 若 **Redis 无值**：**返回 null**。  
5. 若有 **JSON 字符串**：**`JSON.parseObject(userJson, UserInfo.class)`** 反序列化。

**登录态建立**（在用户服务登录成功时）：应由 **业务侧** 写入 **同结构 JSON** 到 **同一 Redis 键**（本课只讲 **网关读取**）。

### 2.4 未登录响应 `out(response, ResultCodeEnum.LOGIN_AUTH)`

1. **`Result.build(null, resultCodeEnum)`** 构造统一响应体。  
2. **`JSONObject.toJSONString(result)`** 转 **UTF-8 字节**。  
3. **`response.getHeaders().add("Content-Type", "application/json;charset=UTF-8")`** — **避免中文乱码**。  
4. **`response.writeWith(Mono.just(buffer))`** — **响应式** 写出。

---

## 3. Redis 键设计

| 项 | 说明 |
|----|------|
| 前缀 `user:lanhai:` | **命名空间**，避免与其它业务 **键冲突** |
| 后缀 `token` | 一般为 **UUID** 或 **随机串**，**不放明文密码** |
| 值 | **UserInfo JSON**，便于网关 **快速取用户 ID** |

**失效**：**Redis TTL**、**用户主动登出（DEL 键）**、**改密码踢下线** 等需在 **用户服务** 实现。

---

## 4. 安全边界（面试）

| 点 | 说明 |
|----|------|
| **HTTPS** | 生产 **必须**，否则 **token 明文** 被窃听 |
| **网关鉴权** | 仅证明 **「有人登录」**；**细粒度权限**（如「仅 VIP」）多在 **服务内** |
| **Token 伪造** | 依赖 **Redis 存在性**；若需 **防篡改** 可换 **JWT 签名** |

---

## 5. 与单体 Session 对比

| 方案 | 优点 | 缺点 |
|------|------|------|
| **Tomcat Session** | 简单 | **多实例** 需 **Session 粘性或 Redis Session** |
| **JWT 无状态** | 易扩展 | **难踢下线**、**payload 大** |
| **Token + Redis（本项目）** | **易踢下线**、**服务端可控** | **依赖 Redis 高可用** |

---

## 6. 本课小结

- **`AuthGlobalFilter`** = **路径匹配 + Redis 查用户 + 统一 JSON 错误**。  
- **前端** 需在 **需登录接口** 上带 **Header: token**。

---

## 7. 自测与扩展

1. 用 **Postman** 访问 **需登录路径**：先 **不带 token**，再 **带错误 token**，再 **带正确 token**（需在 Redis 手动 **set** 测试）。  
2. **打印** `path` 与 **`match` 结果**，确认 **哪些 API** 实际走鉴权。  
3. **扩展**：实现 **网关传递用户信息到下游**（**自定义 Header** 如 `X-User-Id`，需 **防伪造** 校验）。
