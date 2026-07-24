# MybatisLogFree

English | [简体中文](README_zh.md)

An IntelliJ IDEA plugin that restores MyBatis SQL logs into complete, directly executable SQL.

It reads the standard `Preparing:` / `Parameters:` lines that MyBatis prints to the run console, fills the `?` placeholders back with the real parameter values, and displays the finished SQL in a dedicated, color-coded **SQL** tool window.

> Compatible with the latest IntelliJ IDEA versions (2024.1+).

## Features

- 🔄 **SQL restoration** — replaces `?` placeholders with actual parameter values to produce runnable SQL. String / date / time / `BigDecimal` types are automatically quoted.
- 🎨 **Statement coloring** — `INSERT` / `DELETE` / `UPDATE` / `SELECT` can each be shown in a customizable color.
- ✨ **Pretty print** — toggle SQL formatting on/off from the toolbar.
- 🔎 **SQL navigation** — jump to the previous / next SQL statement in the output.
- ⚙️ **Configurable prefixes & keywords** — customize the `Preparing:` / `Parameters:` prefixes and add ignore keywords to filter out noisy lines.
- 🧭 **Toolbar controls** — start / stop, rerun, clear all, soft wrap, and scroll to end.

## Usage

1. Enable MyBatis SQL logging at `DEBUG` level (see [Requirements](#requirements--limitations)).
2. Open the tool window: `Tools` -> `MybatisLogFree` (or right-click in the console and choose `MybatisLogFree`).
3. Click the **start** (play) button in the tool window.
4. Run your application. Restored SQL appears in the **SQL** tab as MyBatis logs are printed.

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

## Settings

Click the **settings** button in the tool window toolbar to configure:

- **Preparing / Parameters prefix** — adjust if your log format differs from the defaults (`Preparing: ` / `Parameters: `).
- **Ignore keywords** — one keyword per line; any log line containing a keyword resets the current SQL buffer and is skipped.
- **Statement colors** — pick a color for each of `INSERT` / `DELETE` / `UPDATE` / `SELECT`.

## Installation

- **From JetBrains Marketplace:** in IntelliJ IDEA, go to `Settings` -> `Plugins` -> `Marketplace`, search for **MybatisLogFree**, and install.
- **From disk:** download the plugin `.zip` and use `Settings` -> `Plugins` -> ⚙️ -> `Install Plugin from Disk...`.

## Build

Requires JDK 17+.

```bash
# Build the plugin (output: build/distributions/MybatisLogFree-<version>.zip)
./gradlew buildPlugin

# Run a sandbox IDE with the plugin for debugging (supports hot reload)
./gradlew runIde
```

The plugin package will be generated at `build/distributions/MybatisLogFree-<version>.zip`.

## Contributing

Issues and pull requests are welcome. Please keep changes focused, and when touching the SQL parsing logic, add unit tests where possible.

## License

See [LICENSE](LICENSE).
