# rea_projects

## 可编译的施工复现工程

源码位于 [`wajibao-replica/`](wajibao-replica/)，包含 Android 工程、Gradle Wrapper、找平/挖沟模拟流程、本地记录、CSV 导出和自动测试。

- [编译和使用说明](wajibao-replica/README.md)
- [与原 App 的对应关系及待恢复部分](wajibao-replica/docs/implementation-map.md)
- [首版验证记录](wajibao-replica/artifacts/验证结果.md)

```bash
cd wajibao-replica
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

此工程是可运行的模拟施工首版，尚未接入原版 VMP、原生求解器和真实设备，不等于完整恢复的原版工程。
