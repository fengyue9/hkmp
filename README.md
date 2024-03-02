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
|      焦点前调      |   15   | 暂时看不出变化  |
|      焦点后调      |   16   | 暂时看不出变化  |
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

### 设备状态去缓存中拿

由于需要实时获取设备状态，频繁更新，因此不适合缓存

## 问题记录

1.绘制功能，保存的图片只有绘制部分，没有视频帧

##### 2.录制功能，结束录制后弹出的下载视频文件大小为0kb（已完成）





### 录像回放

取流地址：  

例：取主码流，取2024年2月21号15:00到2024年2月23号16点的录像

```bash
rtsp://admin:hrj,2002527@192.168.1.64:554/Streaming/tracks/101?starttime=20240221t150000z & endtime=2024022316000000z
```

## TODO:

1.录像回放页面，支持选择设备，选择开始时间和结束时间进行回放，支持停止回放、抓图，录制，录制后弹出模态框询问：下载or保存到服务器 （已完成）

分成摄像头取流回放（通过rtsp取流，webrtc转码回放）与录制播放（实时预览或取流回放时支持录制功能，结束录制后选择保存到服务器后即可在此查看）

2.报警中心，定时任务检查设备状态时自动开启报警，页面上展示报警信息

3.新增手动报警功能，点击后会自动抓图并新增报警记录



### FFmpeg下载安装(实现后觉得不行)

下载ffmpeg-6.1.1-full_build 解压

将D:\毕设\FFmpeg\ffmpeg-6.1.1-full_build\bin 作为系统变量

配置完后在cmd输入ffmpeg -version检查是否安装成功



回放业务流程(待结束)：

前端拼接rtsp流url，传给后端，后端根据url使用ffmpeg将rtsp流转换成本地文件(mp4),然后读取这个文件，返回给前端展示



2024.2.26规划2024.2.27 ：

ToDoList：

1. 完成录像记录页面基础信息查看，新增设备名称列 √
2. 完成录像记录页面下载功能 √
3. 完成录像记录页面播放功能，支持调节倍速 √
4. 设备回放页面新增录制功能 
5. 思考如何实现回放的倍速播放，进度条拖动，下载√

ffmpeg -i recording.webm -vcodec copy -acodec copy new_rebirth-demo.webm

```
D:\>ffmpeg -i recording.webm -c:v libx264 -preset slow -crf 22 -c:a aac -b:a 128k output2.mp4
```



ffmpeg -i D:\毕设\源码\hkmp\recording_2024.02.27-20.52.14.webm -c:v libx264 -preset slow -crf 22 -c:a aac -b:a 128k D:\毕设\源码\hkmp\recording_2024.02.27-20.52.14.mp4



ffmpeg -i recording_1.webm -vcodec copy -acodec copy new_recording_1.webm



2024.2.27规划

1.完成首页系统资源占用监控，可能采用websocket？

2.思考录像管理页面播放功能里除了倍速调整还需要哪些功能

3.报警中心页面编写，sdk调用测试 报警、布防、获取报警信息

4.尝试使用DataV构造大屏

回放：PTZ相机仅支持同时一个用户登录回放，因此不能采取之前的方式获取登录状态，
解决方案：每5s检查一次：逐个ping设备ip，能ping通的说明在线，不能ping通的说明离线；






维护一个Map 放到Redis中，项目启动时遍历设备表，逐个登录，加个缓存，在有设备新增或者编辑或者删除时缓存失效，

设备id与登录=》userId

<6 ,List<userId>>

定时任务5s查询缓存中List<userId>是否有值，如果为空则状态更新成离线，如果不为空则更新成在线；如果没有缓存，则调用启用时的那个方法，缓存并遍历更新状态

回放的日期选择似乎还是有问题



