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



### 抓图记录页面

1. **筛选和搜索功能：** 允许用户根据日期范围、设备名称或其他条件筛选和搜索抓图记录，以便快速找到特定时间或特定设备的抓图记录。
2. **分页功能：** 如果抓图记录较多，可以将结果分页显示，以提高页面加载速度和用户体验。
3. **缩略图预览：** 在列表中显示抓图的缩略图，方便用户快速浏览和识别。
4. **图片放大功能：** 允许用户点击图片放大查看，以便更清晰地查看抓图内容。
5. **删除功能：** 允许用户删除不需要的抓图记录，释放存储空间和清理数据。
6. **导出功能：** 允许用户将抓图记录导出为 Excel 等格式，以便进行进一步分析或备份。
7. **定时任务：** 提供定时删除过期抓图记录的功能，避免数据过多导致系统性能下降。

### 设备状态实时监控(已完成)

1.设备信息管理中编辑设备不能修改状态   

2.状态从启用停用改成在线离线



## 问题记录

1.绘制功能，保存的图片只有绘制部分，没有视频帧

2.录制功能，结束录制后弹出的下载视频文件大小为0kb

### 设备状态去缓存中拿（待实现）

由于需要实时获取设备状态，频繁更新，因此不适合缓存
