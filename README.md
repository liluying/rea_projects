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

v0.2 已接入原版 ARM64 库及 JNI 完整参数，但 SDK 授权未通过，原版计算仍被拦截。平面演示可运行；VMP、真实设备和全部算法等价性尚未完成。详见 [原版核心接入状态](wajibao-replica/docs/native-integration.md)。
