# mvp_Template

mvp_template 是一个MVP框架的快速开发框架。

这里的有三个demo使用了这个框架：一个是szx2，一个是znpos，一个是pad(这个内容是空的)。

mvpbeam是mvp框架

server是访问网络的。用的是okhttp，retrofit

hardware是不同硬件设备的

config是配置文件

common是公共包

model中模型模块

szx2（znpos，pad）是activity和presenter组成

========================================

用到的包bufferknife，rxandroid

==========================================

结构

<img src="https://raw.githubusercontent.com/whtchl/mvp_Template/master/art/1.png"/>

======================================

hardware（应为是不同厂家提供的demo）和 UI通讯是没有用到上面的框架。他们之间通讯是用的是broadcast。后期改为用eventbus
 
 
 ==================================
该项目中，WrapperConverter 或WrapperConverterSzx jsonObject返回的内容扩展性不好。这里已经写死了只能用一种数据结构SzxTransResponse 来处理所以的接口返回的数据。 如果要通用的，请查看Fishing-master(1).rar 下的WrapperConverter.java, 然后根据具体的项目来协商返回的数据接口。

-- Fishing-master(1).rar 下的WrapperConverter.java中返回的json 一定要有STATUS（返回的数据状态），INFO（返回的message），DATA（设置一个json字符串，每个接口自己定义class来接受这个DATA）
 
 
= =============================
Server3 这个module：它的应用app是testLib module

项目使用的retrofit 2.1.0，rxjava 2.2.0, okhttp 3.8.1.

server3的框架来自GanHuoIO-master.rar  . 
 
通过反射机制来的到每个service （如SmsService，GankService）的baseUrl。
 
