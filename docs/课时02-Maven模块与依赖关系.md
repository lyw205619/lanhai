# 课时 02：Maven 模块与依赖关系

## 学习目标

1. 能画出 **父工程 lanhai** 下 **全部子模块** 的树状结构，并标明 **packaging（pom/jar）**。  
2. 解释 **dependencyManagement** 与 **dependencies** 的区别，以及 **为何在父 POM 里锁版本**。  
3. 说明 **lanhai-model**、**lanhai-common**、**lanhai-service-client** 分别被 **谁依赖**，避免 **循环依赖**。  
4. 独立完成 **`mvn clean install -DskipTests`** 并理解 **何时必须 install**。  
5. 了解 **Spring Boot 2 → 3**、**Spring Cloud 2022** 与 **Spring Cloud Alibaba** 的 **BOM 导入** 方式。

---

## 1. 根 `pom.xml` 核心结构

### 1.1 坐标与打包

```xml
<groupId>com.liyiwei</groupId>
<artifactId>lanhai</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>pom</packaging>
```

- **`packaging=pom`**：父工程 **自身不产出 jar**，**只负责聚合子模块 + 版本管理**。

### 1.2 父工程继承

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.0.5</version>
</parent>
```

**继承带来的好处**（口述即可）：

- 统一 **插件版本**（compiler、surefire、spring-boot-maven-plugin 等）。  
- 默认 **UTF-8**、**Java 版本** 可被子模块覆盖。  
- **依赖版本** 对部分 Spring 生态组件有 **默认推荐**。

### 1.3 子模块列表（与仓库一致）

```xml
<modules>
    <module>lanhai-common</module>
    <module>lanhai-model</module>
    <module>lanhai-manager</module>
    <module>lanhai-server-gateway</module>
    <module>lanhai-service</module>
    <module>lanhai-service-client</module>
</modules>
```

---

## 2. 模块树（思维导图级）

```
lanhai (pom)
├── lanhai-common (pom)
│   ├── common-util (jar)
│   ├── common-service (jar)
│   └── common-log (jar)
├── lanhai-model (jar)
├── lanhai-server-gateway (jar, 可运行)
├── lanhai-service (pom)
│   ├── service-product (jar)
│   ├── service-user (jar)
│   ├── service-order (jar)
│   ├── service-cart (jar)
│   ├── service-pay (jar)
│   └── service-ai (jar，转发本机 Ollama)
├── lanhai-service-client (pom)
│   ├── service-product-client (jar)
│   ├── service-user-client (jar)
│   ├── service-cart-client (jar)
│   └── service-order-client (jar)
└── lanhai-manager (jar, 可运行)
```

---

## 3. dependencyManagement

根 POM 中通过 **`dependencyManagement`** 引入 **Spring Cloud BOM**、**Spring Cloud Alibaba BOM**、**MyBatis**、**MySQL**、**Fastjson**、**Lombok**、**支付宝 SDK** 等。

**含义**：

- **子模块** 声明依赖时 **可省略 `<version>`**，版本由 **BOM** 或 **父 dependencyManagement** 决定。  
- **不会** 把依赖自动打进每个子模块；**子模块 POM 里仍要写 `dependencies`** 才会真正引入。

**易错**：子模块写了 **错误版本** 覆盖 BOM → **冲突**；解决：**删掉子模块 version** 或 **与父对齐**。

---

## 4. 各层职责与依赖方向

### 4.1 lanhai-model

- **纯 Java Bean**：实体、VO、枚举、统一 `Result` 等。  
- **被依赖方**：网关、各 service、manager、common、client 等。  
- **禁止**：依赖 **service-impl**、**Controller**（否则 **循环依赖**）。

### 4.2 lanhai-common

| 子模块 | 典型内容 |
|--------|----------|
| common-util | 工具类、辅助方法 |
| common-service | Knife4j、全局异常、`GlobalExceptionHandler` 等 |
| common-log | `@EnableLogAspect`、操作日志切面 |

- **被依赖方**：各 service、manager（通过 `common-service` 等）。

### 4.3 lanhai-service-client

- **Feign 接口** + **DTO**（依赖 **lanhai-model**、**common-util**，scope 常为 **provided**）。  
- **被谁用**：需要 **跨服务 HTTP 调用** 的服务模块。

### 4.4 lanhai-service（聚合）

- **父 POM** 统一 **spring-boot-starter-web、mybatis、redis、nacos-discovery、feign（若需）** 等，**子服务** 继承减少重复。

---

## 5. 构建与安装

### 5.1 首次或 clean 后

在 **仓库根目录**（与根 `pom.xml` 同级）执行：

```bash
mvn clean install -DskipTests
```

**作用**：按 **Reactor 构建顺序** 编译各模块，并把 **common-service、lanhai-model** 等 **install 到本地 `~/.m2/repository`**。

### 5.2 常见错误

| 报错信息 | 原因 | 处理 |
|----------|------|------|
| Could not find artifact com.liyiwei:xxx:jar | **未 install** 或版本不对 | 根目录 **install** |
| 模块变红 | IDEA 未刷新 Maven | **重新导入 Maven** 或 **Invalidate Caches** |

---

## 6. 与 Gradle 多项目对比（扩展）

| 点 | Maven | Gradle Kotlin DSL |
|----|--------|---------------------|
| 多模块 | `<modules>` | `include("a", "b")` |
| 版本 | `dependencyManagement` | `platform()` / `libs.versions.toml` |

本项目选 Maven：**教程多、与 IDE 集成成熟**；换 Gradle **不改变** 模块边界设计。

---

## 7. 本课小结

- **父 POM + BOM** = **版本统一**；**model/common** = **复用与契约**。  
- **install** 是 **多模块本地联调** 的必经之路之一。

---

## 8. 自测与扩展

1. 在 IDEA **Maven 工具窗口** 展开 **lanhai**，数清 **有几个** `pom` 打包的聚合模块。  
2. 在 `~/.m2/repository/com/liyiwei` 下找到 **lanhai-model** 的 **jar**。  
3. **扩展**：阅读 **Spring Boot 官方文档**「Creating a Multi Module Project」。
