# MybatisLogFree

[English](README.md) | 简体中文

一个将 MyBatis 的 SQL 日志还原为完整、可直接执行 SQL 的 IntelliJ IDEA 插件。

它读取 MyBatis 打印到运行控制台的标准 `Preparing:` / `Parameters:` 日志行，把 `?` 占位符还原成真实的参数值，并在独立的、带颜色区分的 **SQL** 工具窗口中展示还原后的完整 SQL。

> 兼容最新版本的 IntelliJ IDEA（2024.1+）。

## 功能特性

- 🔄 **SQL 还原** —— 将 `?` 占位符替换为真实参数值，生成可直接执行的 SQL。字符串 / 日期 / 时间 / `BigDecimal` 等类型会自动加上单引号。
- 🎨 **语句着色** —— `INSERT` / `DELETE` / `UPDATE` / `SELECT` 可分别配置显示颜色。
- ✨ **格式化美化** —— 可在工具栏一键开关 SQL 格式化。
- 🔎 **SQL 跳转** —— 在输出中快速跳转到上一条 / 下一条 SQL。
- ⚙️ **可配置前缀与关键字** —— 自定义 `Preparing:` / `Parameters:` 前缀，并可添加忽略关键字以过滤干扰行。
- 🧭 **工具栏控制** —— 启动 / 停止、重跑、清空、软换行、滚动到底部。

## 使用方法

1. 将 MyBatis 的 SQL 日志开启到 `DEBUG` 级别（见 [使用要求](#使用要求--限制)）。
2. 打开工具窗口：`Tools` -> `MybatisLogFree`（或在控制台右键选择 `MybatisLogFree`）。
3. 点击工具窗口中的**启动**（播放）按钮。
4. 运行你的应用程序。随着 MyBatis 日志的打印，还原后的 SQL 会显示在 **SQL** 标签页中。

## 使用要求 / 限制

本插件的工作方式是**逐行扫描运行控制台的输出**，并**不会**直接接入 MyBatis。要让它显示 SQL，必须同时满足以下条件：

1. **必须开启 MyBatis 的 `DEBUG` 级别 SQL 日志**，使控制台打印出标准的 `Preparing:` 和 `Parameters:` 行。如果没有这些行（例如未配置 logback/log4j，或 mapper 的日志级别高于 DEBUG），插件将无内容可解析，也就不会显示任何 SQL。

   示例（logback）：
   ```xml
   <logger name="com.your.mapper.package" level="DEBUG"/>
   ```

2. **必须先启动插件。** 先打开 `MybatisLogFree` 工具窗口并点击启动（播放）按钮——若插件未运行，控制台输出会被忽略。

3. 日志中必须包含由 DEBUG 级别日志产生的标准 MyBatis 关键字 `Preparing:` 和 `Parameters:`。

> 简而言之：如果原始 SQL（`Preparing:` / `Parameters:`）没有出现在运行控制台中，插件就无法还原或显示它。

## 设置

点击工具窗口工具栏中的**设置**按钮，可以配置：

- **Preparing / Parameters 前缀** —— 若你的日志格式与默认值（`Preparing: ` / `Parameters: `）不同，可在此调整。
- **忽略关键字** —— 每行一个；任何包含该关键字的日志行都会重置当前的 SQL 缓存并被跳过。
- **语句颜色** —— 分别为 `INSERT` / `DELETE` / `UPDATE` / `SELECT` 选择颜色。

## 安装

- **通过 JetBrains 插件市场：** 在 IntelliJ IDEA 中进入 `Settings` -> `Plugins` -> `Marketplace`，搜索 **MybatisLogFree** 并安装。
- **从磁盘安装：** 下载插件 `.zip` 包，通过 `Settings` -> `Plugins` -> ⚙️ -> `Install Plugin from Disk...` 安装。

## 构建

需要 JDK 17+。

```bash
# 打包插件（产物：build/distributions/MybatisLogFree-<version>.zip）
./gradlew buildPlugin

# 在沙箱 IDE 中运行调试（支持热重载）
./gradlew runIde
```

插件安装包将生成在 `build/distributions/MybatisLogFree-<version>.zip`。

## 参与贡献

欢迎提交 Issue 和 Pull Request。请保持改动聚焦，涉及 SQL 解析逻辑时，尽量补充单元测试。

## 许可证

见 [LICENSE](LICENSE)。
