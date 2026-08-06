# CLAUDE.md

本文件为 Claude Code 在本仓库工作时提供指导。所有回复使用中文。

## 项目概述

MybatisLogFree 是一个 IntelliJ IDEA 插件,把 MyBatis 打印在运行控制台的 SQL 日志(`Preparing:` / `Parameters:`)还原成完整、可直接执行的 SQL,并在独立的、带颜色区分的 **SQL** 工具窗口中展示。

- 语言:Java 21
- 构建:Gradle + `org.jetbrains.intellij.platform` 插件(2.7.1)
- 目标平台:IntelliJ IDEA 2025.1+(`sinceBuild = 251`)

## 常用命令

```bash
# 编译
./gradlew compileJava

# 打包插件(产物:build/distributions/MybatisLogFree-<version>.zip)
./gradlew buildPlugin

# 在沙箱 IDE 中运行调试(支持插件热重载)
./gradlew runIde

# 运行全部测试
./gradlew test

# 运行单个测试类
./gradlew test --tests "com.mawl.mybatislog.MyBatisLogConsoleFilterTest"
```

> 需要 JDK 21+。

## 工作原理

插件**不直接 hook MyBatis**,而是逐行扫描运行控制台的输出。要正常工作需满足:

1. MyBatis SQL 日志开启到 `DEBUG` 级别,控制台会打印标准的 `Preparing:` 与 `Parameters:` 行。
2. 需在 `MybatisLogFree` 工具窗口点击启动(play)按钮,否则控制台输出会被忽略(`MyBatisLogManager.running` 为 false 时 Filter 直接返回)。

## 代码架构

核心数据流:**控制台输出 → Filter 拦截解析 → Manager 渲染到 SQL 工具窗口**。

- `MyBatisLogConsoleFilterProvider`(`consoleFilterProvider` 扩展点,注册于 `plugin.xml`)—— 为每个 `Project` 提供一个 `MyBatisLogConsoleFilter`(通过 `project.getUserData` 缓存,每个 Project 一个实例)。
- `MyBatisLogConsoleFilter`(`Filter`)—— 核心解析逻辑,`applyFilter` 逐行处理:
  - 命中用户配置的忽略 `keywords` → 重置当前 SQL 缓存并跳过;
  - 命中 `Preparing:` 前缀 → 缓存 SQL 模板;
  - 命中 `Parameters:` 前缀 → `parseParams` 解析参数,`parseSql` 按顺序把 `?` 替换为参数值(字符串/日期/时间/`BigDecimal` 等 `NEED_BRACKETS` 类型自动加单引号),按 SQL 首单词(insert/delete/update/select)选定着色 key,调用 `manager.println`。
  - 配置 key 常量(`PREPARING_KEY`、`PARAMETERS_KEY`、`KEYWORDS_KEY`、`*_SQL_COLOR_KEY`)集中定义在此类。
- `gui/MyBatisLogManager`(`Disposable`)—— SQL 工具窗口与控制台的生命周期管理,每个 Project 单例(`project.getUserData(KEY)`)。负责工具栏动作装配、着色输出(`println`)、运行开关(`running`)、从 `PropertiesComponent` 读取配置;通过 `RangeHighlighterDocumentListener` 为每条输出前缀 `-- <序号> -- ...` 打 `SQL_LAYER`(3000)标记,供上/下条跳转使用。
- `gui/MyBatisLogExecutor`(`Executor`)—— 定义工具窗口(`TOOL_WINDOW_ID = "MybatisLogFree"`)。
- `BasicFormatter` —— SQL 美化格式化器(对应工具栏 PrettyPrint 开关)。
- `action/` —— 工具栏动作:启动入口(`MyBatisLogAction`,注册在 Tools 菜单与控制台右键菜单)、停止(`StopAction`)、重跑(`RerunAction`)、设置(`SettingsAction`)、清空(`ClearAllAction`)、上/下一条 SQL 跳转(`Previous/Next/JumpSqlAction`)、格式化开关(`PrettyPrintToggleAction`)、捐赠(`DonateAction`)。
- `gui/SettingsDialogWrapper`(配合 `.form` UI Designer 表单)/ `DonateDialogWrapper` —— 设置与捐赠对话框。
- 配置持久化统一走 `PropertiesComponent`。

## 测试现状

- 测试位于 `src/test/java`,`MyBatisLogConsoleFilterTest` 目前为空壳(仅占位 `test()` 方法)。改动 SQL 解析逻辑时,建议优先补充 `parseSql` / `parseParams` 的单元测试。

## 版本发布规范

- 后续打包新版本时,必须把该新版本的更新内容记录到 `src/main/resources/META-INF/plugin.xml` 文件的 `<change-notes>` 中,并同步更新 `<version>`。
- 目的:方便插件市场(JetBrains Marketplace)展示每个版本更新了哪些内容。
- 版本号需与 `build.gradle` 中的 `version` 保持一致。
- 更新内容可参考 `CHANGELOG.md`,保持两处内容一致。
- `build.gradle` 的 `intellijPlatform.pluginConfiguration` 未设置 `changeNotes`,构建时不会覆盖 `plugin.xml` 手写的 `<change-notes>`;`<version>` 默认由 `pluginConfiguration.version`(取自 `project.version`)覆盖,故须与 `build.gradle` 一致。

### 版本升级清单(以 `2.0.2` → 新版本 `X.Y.Z` 为例)

发布新版本时,需修改以下 **3 个文件、4 处位置**:

| 文件 | 位置 | 内容 |
|------|------|------|
| `build.gradle` | `version` | `version 'X.Y.Z'` —— 权威版本号来源 |
| `src/main/resources/META-INF/plugin.xml` | `<version>` | `<version>X.Y.Z</version>` |
| `src/main/resources/META-INF/plugin.xml` | `<change-notes>` | 新增一条 `<li><b>X.Y.Z</b> - 本次更新说明</li>` |
| `CHANGELOG.md` | 顶部 | 新增 `## X.Y.Z` 段落及更新条目 |

说明:
- `build.gradle` 的 `version` 是权威来源,构建时会用它覆盖产物中的 `<version>`;`plugin.xml` 手写的 `<version>` 需与之保持一致。
- `plugin.xml` 的 `<change-notes>` 与 `CHANGELOG.md` 的更新内容应保持一致。
- `build.gradle` 中 `intellijPlatform.pluginConfiguration.ideaVersion` 的 `sinceBuild` / `untilBuild` 仅在支持的 IDE 版本范围变化时才需修改,常规版本升级无需改动。

## 编码规范

- 所有回复使用中文。
- DTO 后缀使用 `Dto`(驼峰),禁止全大写 `DTO`;类名使用 PascalCase。
- (如涉及 Web Controller)仅使用 `@GetMapping`(查询)/ `@PostMapping`(提交)并指定完整路径,禁用 `@DeleteMapping`/`@PutMapping`/`@PatchMapping`。
