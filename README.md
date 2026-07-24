# MybatisLogFree

English | [简体中文](README_zh.md)

Restore mybatis sql log to original whole executable sql.

Compatible with the latest IntelliJ IDEA versions (2024.1+).

## Usage

1. Click `Tools` -> `MybatisLogFree`
2. Click `MybatisLogFree` in the console

## Requirements / Limitations

This plugin works by scanning the **run console output** line by line. It does **not** hook into MyBatis directly. For it to display SQL, all of the following must be true:

1. **MyBatis SQL logging must be enabled at `DEBUG` level**, so that the console prints the standard `Preparing:` and `Parameters:` lines. Without these lines (e.g. no logback/log4j config, or the mapper log level is above DEBUG), the plugin has nothing to parse and shows nothing.

   Example (logback):
   ```xml
   <logger name="com.your.mapper.package" level="DEBUG"/>
   ```

2. **The plugin must be started.** Open the `MybatisLogFree` tool window and click the start (play) button first — if it is not running, console output is ignored.

3. The log must contain the standard MyBatis keywords `Preparing:` and `Parameters:` produced by DEBUG-level logging.

> In short: if the raw SQL (`Preparing:` / `Parameters:`) does not appear in the run console, the plugin cannot restore or display it.

## Build

Requires JDK 17+.

```bash
./gradlew buildPlugin
```

The plugin package will be generated at `build/distributions/MybatisLogFree-<version>.zip`.
