# MybatisLogFree

[English](README.md) | 简体中文

将 MyBatis 的 SQL 日志还原为完整、可直接执行的 SQL。

兼容最新版本的 IntelliJ IDEA（2024.1+）。

## 使用方法

1. 点击 `Tools` -> `MybatisLogFree`
2. 在控制台中点击 `MybatisLogFree`

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

## 构建

需要 JDK 17+。

```bash
./gradlew buildPlugin
```

插件安装包将生成在 `build/distributions/MybatisLogFree-<version>.zip`。
