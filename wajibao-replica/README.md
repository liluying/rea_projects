# 施工复现工程 · v0.2 原版核心接口集成

一个独立、可编译的 Android 工程。包名 `com.rea.wajibao.replica`，可与原 App 同时安装。最低 Android 8.0；当前 APK 包含原版 ARM64 库，面向 ARM64 设备。平面演示可离线运行。

**原版 `libbcCore.so` 和 JNI 参数接线已经接入，但 SDK 施工/演示授权未通过，原版计算当前不可用。** 工程会明确拦截，不输出零坐标、不自动回退冒充原版算法。平面演示模式仍能完整运行。详见 [原版核心接入状态](docs/native-integration.md)。

本工程不是完整恢复的原版源码，不加载加固壳或 VMP。复用原库不等于恢复其 C++ 源码，也不等于完成全部业务算法。

## 已有功能

1. 选择计算引擎：平面演示使用三连杆参数；原版核心使用完整机械/附件 JSON 参数，并可检查原库加载及授权状态。
2. 找平：输入水平设计高程。
3. 挖沟：输入地面高程、沟深、沟底宽和边坡水平/垂直比。
4. 施工：播放/暂停/逐帧查看演示 GNSS 输入，调节平面机具姿态和横向偏移，显示斗齿高程、设计高程、高差和横断面。
5. 保存施工点，查看记录，用 Android 系统文件选择器导出 CSV。保存内容包括输入参数、帧索引及算法版本，便于复核。
6. 后台暂停回放，屏幕旋转保留设计与帧位置；表单草稿不会覆盖已验证参数。

## 编译

安装完整 JDK 17 或 21（须有 `javac` 和 `jlink`）、Android SDK Platform 35、Build Tools 35.0.0。Android Studio 打开本目录。构建前须提供原 APK（仓库中由 Git LFS 管理），Gradle 会按原始哈希提取库，见 [原库提取说明](docs/native-integration.md#构建时取出原库)。

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
| `core/PoseSolver.java` | 平面演示求解器边界 |
| `nativecore/` | 原版 32+7 参数、完整输入帧、JNI 后端、资源管理与授权拦截 |
| `OriginalReplayController.java` | 原版状态化回放及坐标到本版横断面的适配 |
| `native-validation/` | 主机 ARM64 原库诊断，当前明确返回 SDK 授权阻塞状态 |
| `core/Guidance.java` | 高差方向和 ±0.03 m 演示容差 |

Java 源码位于 `app/src/main/java/com/rea/wajibao/replica/`。业务逻辑不依赖第三方运行库；JUnit / Robolectric 仅用于测试。

## 输入与算法边界

- `replay.json` 保留平面演示使用的坐标；`native-replay.json` 额外保留原版全部 GNSS / 传感器字段，`native-profile.json` 保存机械与附件参数。数据包含：找平 881 帧、挖沟 576 帧，间隔 200 ms。来源与提取文件哈希见 `docs/replay-provenance.json`。
- 这里只复用了演示输入，未复用或声称恢复原版斗齿输出。
- 演示求解器将 GNSS 高程作为虚拟大臂支点，三个连杆角均为相对水平面的绝对角，输入单位为度，长度为米。真实 RTK 安装位置、传感器坐标系、机具标定和工作模式尚未接入。
- 高差定义为“斗齿高程 − 当前位置设计高程”：正值提示继续下挖，负值提示抬升/回填。
- 记录最多 500 条，不会自动删除旧记录；请导出后再确认清空。

原版接口与授权阻塞见 `docs/native-integration.md`；剩余恢复范围见 `docs/implementation-map.md`。20 项自动测试通过不等于原版数值已验证。
