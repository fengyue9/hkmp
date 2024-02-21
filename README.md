# hkmp

## 依赖安装

**jna.jar与example.jar的安装**

在hkmp根目录下分别运行以下命令

```bash
mvn install:install-file -Dfile="D:\毕设\源码\hkmp\lib\examples.jar" -DgroupId=com.fy.hkmp -DartifactId=examples -Dversion=1.0.0 -Dpackaging=jar -DlocalRepositoryPath=D:\Softwares\apache-maven-3.9.6\mvn_repo

mvn install:install-file -Dfile="D:\毕设\源码\hkmp\lib\jna.jar" -DgroupId=com.fy.hkmp -DartifactId=jna -Dversion=1.0.0 -Dpackaging=jar -DlocalRepositoryPath=D:\Softwares\apache-maven-3.9.6\mvn_repo
```

**通过JitPack安装github上hk-springboot-starter**

## 启动环境

### Redis5

```bash
redis-serve.exe redis.windows.conf 
```

### MinIO

```
minio.exe server D:\毕设\源码\minio\data
```





```
rtsp://admin:hrj,2002527@192.168.1.64:554/Streaming/Channels/101?transportmode=unicast
```



### rtsp

```bash
rtsp://admin:hrj,2002527@192.168.1.64:554/Streaming/Channels/101?transportmode=unicast
```





```
rtsp://admin:hrj,2002527@192.168.1.64:554/h264/ch01/main/av_stream
```

通过VLC播放器播放这个流可以看到摄像头实时预览信息



### Webrtc-streamer

进入D:\毕设\WebRTC\webrtc-streamer-v0.6.5-Windows-AMD64-Release

cmd运行（后续需要集成到hkmp项目启动）

```bash
webrtc-streamer.exe
```





### 云台控制相关数据字典

|        含义        | code值 |                 |
| :----------------: | :----: | :-------------: |
|        左上        |   1    | PTZ摄像机不支持 |
|         上         |   2    |                 |
|        右上        |   3    | PTZ摄像机不支持 |
|         左         |   4    |                 |
|    左右自动扫描    |   5    | PTZ摄像机不支持 |
|         右         |   6    |                 |
|        左下        |   7    | PTZ摄像机不支持 |
|         下         |   8    |                 |
|        右下        |   9    | PTZ摄像机不支持 |
| 焦距变大(倍率变大) |   10   | 暂时看不出变化  |
| 焦距变小(倍率变小) |   11   | 暂时看不出变化  |
|      光圈扩大      |   12   | 暂时看不出变化  |
|      光圈缩小      |   13   | 暂时看不出变化  |
|    接通灯光电源    |   14   | 暂时看不出变化  |
|      焦点前调      |   15   |                 |
|      焦点后调      |   16   |                 |
|                    |        |                 |
|                    |        |                 |
|                    |        |                 |
|                    |        |                 |

### 策略模式

CodeHandler类设计

"策略模式"（Strategy Pattern）。策略模式定义了一系列算法，将每个算法封装起来，并且使它们可以互相替换。这种模式使得算法可以独立于使用它的客户端而变化。

每个处理方法都代表一个具体的策略，而`handleCode()`方法则充当了环境角色，根据传入的参数来选择并执行不同的策略。这种方式使得你可以动态地改变或扩展系统的行为，而无需修改`handleCode()`方法的代码。

策略模式的主要优点包括：

1. 提供了一种易于扩展和维护的方式来管理不同的算法或策略。
2. 可以在运行时动态地选择和切换算法，而不需要修改客户端代码。
3. 将算法的实现细节隐藏在各自的策略类中，使得客户端更加简洁和清晰。

总之，策略模式是一种非常有用的设计模式，特别适用于需要根据不同情况选择不同行为的情况。
