# Easy Auto Fishing

A client-side Fabric mod for Minecraft 26.2 that fishes for you.
**[繁體中文說明在下方](#繁體中文)**

## Features

- Press **K** (rebindable in Controls) to toggle auto fishing; the status is shown above the hotbar.
- Casts automatically when you hold a fishing rod (main hand or off hand) and have no bobber out.
- Detects a bite from the splash sound next to your own bobber, reels in after a random 4–8 ticks, then recasts after a random 10–20 ticks.
- Stops automatically when the rod has 1 durability left, reeling in first when that costs no durability.
- Pauses while any screen (inventory, chat, …) is open.

Client-only: it works on any server without needing to be installed there.

## Installation

Requires Minecraft 26.2, [Fabric Loader](https://fabricmc.net/use/) 0.19.5+, [Fabric API](https://modrinth.com/mod/fabric-api) and Java 25.
Put `easy-auto-fishing-<version>.jar` (not the `-sources` jar) into your `mods` folder.

## Building

Requires JDK 25.

```bash
./gradlew build       # output: build/libs/easy-auto-fishing-<version>.jar
./gradlew runClient   # launch a dev client for testing
```

## Note

Some servers forbid automated fishing. Check the server's rules before using this mod.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## 繁體中文

Minecraft 26.2 的純客戶端 Fabric 自動釣魚模組。

### 功能

- 按 **K**（可在「控制」設定中更改）開關自動釣魚，狀態顯示在快捷欄上方。
- 主手或副手拿著釣竿、且沒有浮標時自動拋竿。
- 偵測自己浮標旁的上鉤水花聲，隨機 4–8 tick 後收竿，再隨機 10–20 tick 後重新拋竿。
- 釣竿只剩 1 點耐久時自動關閉；若此時收竿不扣耐久，會先收竿再關閉。
- 開啟任何介面（背包、聊天等）時暫停。

純客戶端模組，伺服器不需要安裝。

### 安裝

需要 Minecraft 26.2、[Fabric Loader](https://fabricmc.net/use/) 0.19.5 以上、[Fabric API](https://modrinth.com/mod/fabric-api) 與 Java 25。
把 `easy-auto-fishing-<版本>.jar`（不是 `-sources` 那個）放進 `mods` 資料夾。

### 建置

需要 JDK 25。執行 `./gradlew build`，產物在 `build/libs/`；`./gradlew runClient` 可啟動測試用客戶端。

### 注意

部分伺服器禁止自動釣魚，使用前請先確認伺服器規則。

### 授權

本專案採用 MIT 授權，詳見 [LICENSE](LICENSE)。
