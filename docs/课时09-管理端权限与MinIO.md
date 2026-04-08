# 课时 09：管理端权限与 MinIO

## 学习目标

1. 说明 **`ManagerApplication`** 上 **注解** 的作用：`@EnableScheduling`、`@EnableLogAspect`、`@ComponentScan` 等。  
2. 理解 **管理端** 与 **C 端** 在 **端口、鉴权、技术栈** 上的 **分离原因**。  
3. 能描述 **RBAC** 在本项目中的 **典型表结构**（用户、角色、菜单、关联表）。  
4. 配置 **MinIO** 的 **endpoint、ak/sk、bucket**，并说明 **上传后 URL** 如何 **存库**。  
5. 解释 **白名单** `noAuthUrls` 的 **必要性**（避免 **登录接口被拦截死循环**）。

---

## 1. 管理端应用

| 项 | 值 |
|----|-----|
| 主类 | `com.liyiwei.lanhai.manager.ManagerApplication` |
| 端口 | **8501**（`application-dev.yml`） |
| 扫描 | `@ComponentScan(basePackages = {"com.liyiwei.lanhai"})` — **范围较大**，注意 **Bean 冲突** |
| 定时任务 | `@EnableScheduling` — 如 `OrderStatisticsTask` |
| 操作日志 | `@EnableLogAspect` — 引用 **common-log** |

---

## 2. 为何独立 manager（而非走 Gateway）

| 原因 | 说明 |
|------|------|
| **权限模型不同** | 后台 **RBAC**、**菜单树**；C 端 **用户登录** 即可 |
| **发布节奏** | **运营后台** 与 **C 端大促** 可 **独立发版** |
| **安全域** | **管理端** 常 **内网/VPN**；**不暴露** 到公网 |

**技术栈**：**Spring MVC**（非 WebFlux），**与 Gateway 团队** 学习成本 **不同**。

---

## 3. 登录与拦截

### 3.1 拦截器

- 类如 **`LoginAuthInterceptor`**：拦截 **除白名单外** 的请求。  
- **校验**：**Session** 或 **Token**（以项目实现为准）。

### 3.2 白名单

`application-dev.yml`：

```yaml
lanhai:
  auth:
    noAuthUrls:
      - /admin/system/index/login
      - /admin/system/index/generateValidateCode
```

**含义**：**登录、验证码** 等 **无需登录** 即可访问；**若漏配**，会导致 **无法登录**。

---

## 4. RBAC（概念）

典型表：

- **sys_user**；**sys_role**；**sys_menu**；**sys_role_menu**；**sys_user_role**（命名以实际表为准）。

**控制器**：`SysUserController`、`SysRoleController`、`SysMenuController`、`SysRoleMenuController` 等。

**面试**：「**菜单** 与 **接口权限** 可 **二选一或组合**；**按钮级** 需 **前端 v-if + 后端鉴权** 双保险。」

---

## 5. MinIO

### 5.1 配置项

```yaml
lanhai:
  minio:
    endpointUrl: http://127.0.0.1:9000
    accessKey: ...
    secreKey: ...
    bucketName: lanhai-bucket
```

### 5.2 流程

1. **服务启动** 确保 **MinIO 进程** 已运行。  
2. **上传**：`FileUploadServiceImpl` 等 **PutObject**。  
3. **DB** 存 **可访问 URL**；**前端** `<img src="...">`。

### 5.3 与 OSS

**MinIO** = **自托管 S3**；**阿里云 OSS** = **托管**。**API 类似**，**迁移** 成本相对低。

---

## 6. 本课小结

- **8501** = **后台**；**RBAC + 拦截器 + MinIO** = **典型中后台能力**。  
- **白名单** 是 **登录链路** 的 **第一道门**。

---

## 7. 自测与扩展

1. **注释掉** 一条 `noAuthUrls`，再访问登录，观察 **是否死循环**。  
2. **MinIO Console** 创建 **bucket**，上传文件，**浏览器** 访问 URL。  
3. **扩展**：**Spring Security** 与 **手写拦截器** 的 **选型**。
