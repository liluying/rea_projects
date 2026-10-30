# 原版核心接入状态 · v0.2

## 结论

**原版 ARM64 库已打包，JNI 绑定与完整参数接线已完成；原版计算仍因 SDK 授权未通过而不可用。** 这不是“完整原版算法已经运行通过”。目前仅有原 APK，没有可用于本工程新包名的 SDK 授权资料。

原库在运行时提供两个独立状态：施工功能授权、演示功能授权。本轮 ARM64 仿真中两者均为 `false`。机械和附件初始化均返回成功，但原库计算入口提前返回全零坐标。集成版会阻止这种结果进入施工显示和记录，也不会自动用平面演示算法冒充原版结果。

原 App 能在手机上运行，不会让新包名自动取得相同的 SDK 授权。后续需要厂商提供适用的 SDK 接入方式、授权版本或授权材料；本工程没有伪造授权状态、修改原库或复制登录凭据。

## 已接通的内容

- 原样 `libbcCore.so`，SHA-256：`443a89c357df2277ea3e604db699188fc0b52eba3cd2306332f4e544b49bfa32`。
- 原 JNI 命名空间 `com.bingce.wajibao.wajibaoModule` 与坐标读取接口。
- `new_WajiInner → initMachine → initAttachment → calculate → 读取斗底多点 → delete_WajiInner`。
- 32 项机械数值、7 项附件数值、4 个安装侧、主天线方向与工作模式编号。
- 计算输入：GNSS XYZ、RTK roll/pitch/yaw、机身/大臂/小臂/铲斗各两轴姿态、简化斗模式、4 项天线/改正参数、演示标志。
- 斗底中心/左右点高程、中心点 XYZ 和机具方位角的读取。
- `isWjbValidate`、`isWjbEmulatorValidate` 两个原版只读授权查询接口。
- 找平 881 帧、挖沟 576 帧的完整演示输入，角度按原 Java 调用链从度转换为弧度。

机械 32 项顺序：

```
ab, bc, cd, az, jz, zr, ak, zk, zs, ks, pq, sw,
swOffset, us, usOffset, usHd, uv, uvOffset, uvHd, st,
sToWaterHd, azCorrect, abCorrect, bcCorrect, akCorrect,
zkCorrect, zsCorrect, rollCorrect, pitchCorrect,
yawCorrect, jzCorrect, skCorrect
```

附件顺序：`ad, aj, dj, dg, ag, simpleAjTilt, mn`。其中 `dj` 使用原记录的 `attrCalcDj`，而不是凭名字改用其他长度。

默认配置来自原 APK 的演示机械，工作模式 5、附件模式 0。原配置未写入的安装侧保留原 Java 模型默认值 0，主天线前方标志默认 false。接口接受原版 0–9 的工作模式，但目前没有任何模式通过授权后的数值等价性验证。

## 构建时取出原库

库不重复提交为一个大文件；Gradle 从原 APK 提取，并核对 APK 与库两个 SHA-256。原 APK 的 SHA-256 必须为：

```
322c4be737c06f89e723cb7632ccb2c8ca3482a904ac4151d40dddc9b3788c34
```

仓库原 APK 由 Git LFS 管理。克隆后先在仓库根目录运行：

```bash
git lfs pull --include=wjb-bingce-arm64-v8a-release_1_0.apk
cd wajibao-replica
./gradlew :app:assembleDebug
```

也可以自行提供原 APK：

```bash
./gradlew -PoriginalApk=/absolute/path/to/original.apk :app:assembleDebug
```

或者设置环境变量 `ORIGINAL_APK`。生成的 `.so` 位于 `app/build/generated/original-jni/`，被 Git 忽略。打包时保留原库字节，不剥离符号、不修改校验逻辑。APK 包含 ARM64 原库，需在 ARM64 手机/平板上安装；没有真机验证。

## 原库运行诊断

```bash
./scripts/build-cloud.sh :native-validation:run
```

普通开发环境可以直接使用 `./gradlew :native-validation:run`。该任务使用 Unidbg/Unicorn2 在主机上运行原 ARM64 字节，不依赖 Android 图形模拟器。诊断结果写入 `artifacts/native-runtime-validation.json`。

本轮结果：

- 原库加载、原生对象创建及机械/附件初始化成功。
- 10 个模式常量逐项匹配 0–9。
- `Ab=0.21`、`Pq=0.305`、`Sw=0.87`、`AkCorrect=0.06632251126369536`、`ZkCorrect=-1.51614941273146` 经原 getter 返回，验证了部分寄存器参数、栈上浮点参数与浮点返回。
- 找平、挖沟各 8 次原始计算诊断均因当前授权状态未通过而返回零；这些样本全部标记 `validForConstruction=false`。
- 应用层授权拦截测试通过。

**`diagnosticsPassed=true` 表示诊断完成，不表示原版计算通过。** 状态字段为 `BLOCKED_SDK_AUTHORIZATION`，`nativeCalculationValidated=false`。

校验工具的通用调用器在超过 8 个浮点参数时不满足本接口的 AAPCS64 布局，因此诊断后端显式安排 D0–D7 与栈参数。该处理仅用于主机诊断；手机上的 JNI 参数由 Android ART 正常传递。

Unidbg 环境没有 EGL/GLES 图形库，因此该诊断仅涉及对象、接口和授权门控，不验证 CAD、地形渲染或完整原 App。

## 尚未接入的“完整算法”范围

- 原版授权后的机具计算结果、全部安装模式和附件组合，尚未验证。
- 找平/挖沟的目标设计面仍使用 v0.1 的独立实现，不声称等价于原版所有设计业务。
- 797 个 VMP 方法没有因此恢复；坐标转换、TIN、道路、CAD 等所有业务也没有因此完成移植。
- 缺少正式 SDK 初始化、授权接入与原 App 数值对照。获得材料后还需完成这些步骤，才能启用并验收原版计算。
