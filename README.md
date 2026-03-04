# Gradle 多模块 Spring Boot + Jib + Trivy 示例

本项目演示如何使用 **Gradle 多模块** + **Spring Boot** + **Google Jib** + **Trivy**，并通过 **GitLab CI** 实现自动构建、打包 Docker 镜像以及安全扫描。

## 项目结构

- `settings.gradle`：声明多模块。
- `build.gradle`：根项目通用配置（Java 版本、依赖管理等）。
- `common/`：通用模块，仅打包成 jar，被其它服务依赖，不生成 Docker 镜像。
- `order-command-service/`：下单服务（order-command-service），Spring Boot 应用，依赖 `common`，通过 Jib 打包为独立 Docker 镜像。
- `order-query-service/`：订单查询服务（order-query-service），Spring Boot 应用，依赖 `common`，通过 Jib 打包为独立 Docker 镜像。
- `.gitlab-ci.yml`：GitLab CI 流水线配置，包含构建、镜像打包与 Trivy 扫描。

## 主要技术点

- **Gradle 多模块**：根项目统一管理 Java 版本、仓库、Spring Boot BOM 等；子模块分别配置自己的依赖。
- **Spring Boot**：`order-command-service`、`order-query-service` 各自有 `@SpringBootApplication` 启动类，可独立运行。
- **Google Jib**：
  - 在 `order-command-service`、`order-query-service` 的 `build.gradle` 中使用 `com.google.cloud.tools.jib` 插件。
  - 直接从 Gradle 构建 Docker 镜像，无需 Dockerfile。
  - 基础镜像示例：`eclipse-temurin:17-jre`。
  - 镜像地址通过环境变量拼接：`REGISTRY_URL` + `IMAGE_NAMESPACE` + 服务名。
  - Tag 策略：使用 `CI_COMMIT_SHORT_SHA` 和 `latest` 双 tag。
- **Trivy**：
  - 在 GitLab CI 的 `scan` 阶段，对 `order-command-service`、`order-query-service` 镜像分别执行 `trivy image` 扫描。
  - 生成独立报告文件：`reports/trivy-order-command-service.json`、`reports/trivy-order-query-service.json`。

## 运行与构建

### 本地构建

在项目根目录执行：

```bash
./gradlew clean build
```

### 本地运行服务

```bash
# 运行 order-command-service（下单服务）
./gradlew :order-command-service:bootRun

# 运行 order-query-service（订单查询服务）
./gradlew :order-query-service:bootRun
```

运行后示例访问路径（端口根据代码配置为例）：

- `order-command-service`：`http://localhost:8080/api/orders`
- `order-query-service`：`http://localhost:8081/api/orders`

### 使用 Jib 构建镜像

需要先在环境中设置镜像仓库相关变量，例如（示意）：

```bash
export REGISTRY_URL=registry.example.com
export IMAGE_NAMESPACE=my-team/my-project
export CI_COMMIT_SHORT_SHA=dev  # 本地调试时可自定义
```

#### 分别构建两个服务的镜像

```bash
./gradlew :order-command-service:jib
./gradlew :order-query-service:jib
```

Jib 会根据 `build.gradle` 中的配置，将镜像推送到：

- `${REGISTRY_URL}/${IMAGE_NAMESPACE}/order-command-service`
- `${REGISTRY_URL}/${IMAGE_NAMESPACE}/order-query-service`

#### 一条命令构建所有服务镜像

在根项目中已经定义了聚合任务 `jibAll`，可以使用一条命令同时为所有 Spring Boot 服务构建并推送镜像：

```bash
./gradlew jibAll
```

这条命令等价于依次执行：

- `./gradlew :order-command-service:jib`
- `./gradlew :order-query-service:jib`

## GitLab CI 与 Trivy 扫描

项目根目录下的 `.gitlab-ci.yml` 定义了三个阶段：

1. **build**：使用 Gradle 构建所有模块（`gradle clean build`）。
2. **docker**：分别调用：
   - `gradle :order-command-service:jib`
   - `gradle :order-query-service:jib`
   使用 Jib 构建并推送 Docker 镜像。
3. **scan**：基于 `aquasec/trivy` 镜像，对上一步推送的镜像执行安全扫描：
   - `trivy image --severity HIGH,CRITICAL --format json --output reports/trivy-order-command-service.json ...`
   - `trivy image --severity HIGH,CRITICAL --format json --output reports/trivy-order-query-service.json ...`

Trivy 报告会作为 CI artifacts 保存在流水线中，便于后续下载与分析。

## GitLab CI 所需变量

在 GitLab 项目或组的 **CI/CD Variables** 中建议配置：

- `REGISTRY_URL`：私有镜像仓库地址，例如 `registry.example.com`。
- `IMAGE_NAMESPACE`：镜像命名空间，例如 `my-team/my-project`。
- `REGISTRY_USERNAME`：镜像仓库登录用户名（供 Jib 推送使用）。
- `REGISTRY_PASSWORD`：镜像仓库登录密码或 Token。

Trivy Job 会复用 `REGISTRY_URL` / `IMAGE_NAMESPACE` 拼接出要扫描的镜像名称。

## 扩展

- 新增业务模块时，可参考 `order-command-service` / `order-query-service` 的结构复制一份：
  - 在 `settings.gradle` 中添加新模块。
  - 为新模块编写 `build.gradle`，依赖 `common`。
  - 配置 Jib（镜像名使用新的服务名）。
  - 在 `.gitlab-ci.yml` 中增加对应的 `docker` Job 和 Trivy 扫描命令。

通过这种方式，可以快速扩展更多服务，同时保持统一的构建、镜像与安全扫描流程。

