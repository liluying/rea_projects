# 施工复现工程 · v0.1

一个独立、可编译的 Android 工程，用模拟设备跑通找平和挖沟。包名 `com.rea.wajibao.replica`，可与原 App 同时安装。最低 Android 8.0，不需要登录、网络、定位或蓝牙权限。

**这是一版新建的功能复现骨架，不是已恢复并可直接构建的原版源码。** 本版不加载原 APK 的加固库或 VMP，不将反编译文件批量纳入编译。施工几何采用明确标记的演示算法；原生核心的等价性尚未验证。

## 已有功能

1. 配置机械名称、大臂、小臂和铲斗长度，检查输入并保存参数。
2. 找平：输入水平设计高程。
3. 挖沟：输入地面高程、沟深、沟底宽和边坡水平/垂直比。
4. 施工：播放/暂停/逐帧查看演示 GNSS 输入，调节平面机具姿态和横向偏移，显示斗齿高程、设计高程、高差和横断面。
5. 保存施工点，查看记录，用 Android 系统文件选择器导出 CSV。保存内容包括输入参数、帧索引及算法版本，便于复核。
6. 后台暂停回放，屏幕旋转保留设计与帧位置；表单草稿不会覆盖已验证参数。

## 编译

安装完整 JDK 17 或 21（须有 `javac` 和 `jlink`）、Android SDK Platform 35、Build Tools 35.0.0。Android Studio 打开本目录，等待 Gradle 同步即可。

使用 `ANDROID_HOME` 指定 SDK，或者在 `local.properties` 写入 `sdk.dir=/your/android/sdk`。该文件是本机配置，不包含在源码包中。

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest :app:lintDebug
```

Windows 使用 `gradlew.bat`。Gradle Wrapper 固定 8.9，并校验发行包 SHA-256；Android Gradle Plugin 为 8.7.3。

当前云环境的缓存、代理和 JDK 已配置，可用：

```bash
./scripts/build-cloud.sh :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

APK 输出：`app/build/outputs/apk/debug/app-debug.apk`。它使用开发调试签名，适合安装验证，尚非正式发布版本。

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.rea.wajibao.replica/.MainActivity
```

## 代码入口

| 文件 | 内容 |
|---|---|
| `MainActivity.java` | 三步施工流程、页面、回放调度和记录操作 |
| `ProfileView.java` | 横断面和斗齿位置绘制 |
| `ReplaySource.java` | 演示输入读取与边界检查 |
| `RecordStore.java` | 原子保存、本地读取与 CSV 导出 |
| `core/Design.java` | 水平面及梯形沟设计高程 |
| `core/DemoPoseSolver.java` | 三连杆平面演示计算 |
| `core/PoseSolver.java` | 求解器边界，后续扩展为经过验证的原生适配层 |
| `core/Guidance.java` | 高差方向和 ±0.03 m 演示容差 |

Java 源码位于 `app/src/main/java/com/rea/wajibao/replica/`。业务逻辑不依赖第三方运行库；JUnit / Robolectric 仅用于测试。

## 输入与算法边界

- `replay.json` 包含原 APK 演示数据提取后的 GNSS 三维坐标：找平 881 帧、挖沟 576 帧，间隔 200 ms。来源与提取文件哈希见 `docs/replay-provenance.json`。
- 这里只复用了演示输入，未复用或声称恢复原版斗齿输出。
- 演示求解器将 GNSS 高程作为虚拟大臂支点，三个连杆角均为相对水平面的绝对角，输入单位为度，长度为米。真实 RTK 安装位置、传感器坐标系、机具标定和工作模式尚未接入。
- 高差定义为“斗齿高程 − 当前位置设计高程”：正值提示继续下挖，负值提示抬升/回填。
- 记录最多 500 条，不会自动删除旧记录；请导出后再确认清空。

下一步恢复计划见 `docs/implementation-map.md`。它明确列出与原版之间的缺口。
