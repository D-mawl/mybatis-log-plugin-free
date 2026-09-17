# 更新日志

## 2.0.6

- 移除清空控制台动作对 IntelliJ Platform 内部 API `ExecutionBundle` 的依赖,改用插件自有本地化资源

## 2.0.5

- 修复过期 API:改用 `PluginAwareClassLoader` 读取插件版本号
- `Document.addDocumentListener` 迁移到带 `Disposable` 的重载

## 2.0.4

- 清理 IntelliJ Platform 弃用 API(RunContentManager、PluginManagerCore、ToolWindowManagerListener 新签名等)

## 2.0.3

- 升级到 Java 21,最低支持 IntelliJ IDEA 2025.1
- 构建工具迁移到 IntelliJ Platform Gradle Plugin 2.x

## 2.0.2

- 显式引入commons-lang3,兼容最新版本的 IntelliJ IDEA
