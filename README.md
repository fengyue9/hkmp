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

