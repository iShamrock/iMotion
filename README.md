iMotion
=======

iShamrock 2014

iMotion开发文档

Version: 1.0

开发者：童仲毅 李逢双 张琪

一、	产品概述
--------------
iMotion是非常轻量的一个心情分享平台，一个完全基于地理位置的移动社交网络。你可以通过iMotion搜寻到你周围任意范围内的陌生人的心情气泡，和他们交流、分享看法。iMotion可以帮助你快速记录心情，不管在任何时候、任何地点。一段时间后，你可以在Mood Box中回忆过去某时某地的心情。
二、	需求分析
--------------------
传统的社交网络(如Facebook)通过时间轴来呈现新鲜事流，这种呈现方式相比以往让新鲜事流阅读起来更加流畅，但并非是最完美的体验方式。在关注的好友达到一定数量且发生某一规模性事件时，新鲜事的数量会出现井喷式的增长，用户会被大量雷同的信息刷屏。此外，新鲜事的时间属性为主导，位置属性被削弱。
我们构建了一个全新概念的心情分享平台iMotion，一个完全基于地理位置的社交网络。我们希望每一个用户都可以尽情表达此刻的心情，每一个用户关心身边人的心情而无他。

一种创新的交互方式，用户的心情将以“气泡”的形式呈现。地图上会不时冒出新鲜的气泡，也会有陈旧的气泡逐渐消失，形成一个实时的心情地图。每个气泡具有一定的新鲜度，半衰期为30分钟，随时间气泡会渐渐淡去。瞬间的伤心或喜悦就由时间自然地沉淀。一个气泡可以“引起”(invokes)其他用户情感的共鸣，在心情下不断进行讨论来维持气泡的新鲜度。
一定的智能。通过传感器，感知用户的位置、高度、运动方向、运动状态（静止、走路、坐车），通过用户分享的状态或音乐感知用户的情感，使气泡呈现不同的外观(颜色/形状)；可以通过当时的天气/周围人的心情做出一些决策；可以根据地理位置和状态内容将同一主题折叠在一个气泡下。
三、概要设计
------------
DemoApplication: Application类，主要用于通过KEY获取百度地图API权限
Activity 设计：
* Login
* MapView
程序主界面，主视图为地图，气泡信息在地图上相应位置出现。
长按地图某一点可以在该点处发布气泡。
从界面最左侧向右拉，可调出Navigation Drawer，执行各项操作。
* BubbleAdd
添加气泡界面，输入要发布的内容，同时地点会自动显示在下方的地址条中，如果不够精确，可以手动修改。
* NearbyDiscuss
显示你所在地点附近的气泡，点击进入讨论。
* Discussion
气泡回复界面，对自己或者他人的气泡进行回复。
* ARActivity
打开摄像头，并且可以在界面上显示气泡，气泡横向位置是相对用户该气泡所在的方向，纵向位置是和用户的距离，越下方的气泡越远。

四、数据库设计
-------------
主要数据类型：

### User(用户)：
数据类型	|信息名
---------|----------
Long	|id
String	|name;
String	|password;
String	|sex;
java.sql.Date	|registerDate;
java.sql.Date	|lasttime;
ArrayList<Status>	|Statuslist ;


### Status（心情）：
数据类型	|信息名
---------|----------
Long	|Id
String	|text
String	|emotion
java.sql.Date	|createdDate;
String	|username;
double[2]	|location
String	|place



### 联网接口及主要功能：
接口：CTDinterface     
实现注册，注销，获取心情，检查用户名，添加状态的功能

方法名	|返回类型
---------|----------
register	|User
login	|User
getStatuslis	|ArrayList<staus>
addStatus	|ArrayList<staus>
checkname	|boolean
getname	|ArrayList<staus>
addcomments	|ArrayList<staus>
getcomment	|ArrayList<staus>



### 数据库设计
服务：本应用采用了mysql，jdbc驱动以及阿里RDS数据库

### 逻辑结构设计
 (Image)

五、详细设计
-------------
### 全局变量：
1、	用户名USER，密码PASSWORD
2、	气泡数据ArrayList<Bubble>
3、	回复数据Arraylist<Reply>
4、	从服务器接受的数据ArrayList<Status>
5、	经纬度数据locData，地址数据location

### 对象设计：
1、	Bubble类
存储Bubble数据以及绘制气泡等方法
2、Reply类
添加、存储回复信息
3、User类
存储用户数据
4、Status类
可以存储Bubble信息和Reply信息，用来和服务器交换数据
5、	Emotion类
用来表示气泡的心情状态。
6、	Angel类
用来在AR中计算气泡的信息，实现气泡的定位

### 功能类：
1、	Location_Geo类
用来获取用户当前的位置信息。定时刷新。
2、	connectToDatabase类
提供与服务器通信的基本方法。
3、	Connect类
提供标准方法实现与服务器交换数据，并对全局变量进行相应修改

六、软件测试
------------
产品经过了黑盒测试。
