# 写在最前面的
## 说明
1. 教程能够成功执行，依赖于LTP官方提供了CMakeLists.txt
2. 本项目部分代码来源于[官方LTP4j项目](https://github.com/HIT-SCIR/ltp4j)，主要是对LTP的封装，若有侵权请联系删除。
3. 修改内容如下:
	- 接口使用的封装，以及官方给定的模型集成
	- native方法增加模型是否已经加载的判定
	- linux、windows系统下的动态链接文件适配加载
4. 编译环境
	- linux:CentOS Linux release 7.3.1611
	- windows:win7 64位+vs2013

# 准备
- vs2013安装 （windows下）
- cmake安装
- maven安装

# 编译

## 编译LTP
> 因为本人已经做了编译，可以忽略
1. 下载LTP源码
2. 进入ltp项目根目录，并创建build目录（当然目录名随便起）
3. 进行build目录，执行cmake -G "Visual Studio 12 2013 Win64" ..（注：若要编译32位，执行cmake ..即可，默认为32位，同时需要编辑CMakeLists，将LTP_HOME指向x32。另外linux下直接cmake ..即可）
4. 执行cmake --build . (linux下执行make)
5. 将编译结果，即bin、lib、include目录，拷贝至ltp4j/ltp

## 编译LTP4J(非官方的LTP4J)
> 本人也做了编译，也可以忽略
1. 进入ltp4j下并创建build目录
2. 进入build目录，执行cmake -G "Visual Studio 12 2013 Win64" .. （注：若要编译32位，执行cmake ..即可，默认为32位，同时需要编辑CMakeLists，将LTP_HOME指向x32。另外linux下直接cmake ..）
3. 执行cmake --build . （linux下执行make）


# 构建Jar
使用maven进行构建即可.
因模型文件较大，所以将基础NLP任务（词法分析）、句法分析分别进行了打包构建。

# 使用
```java
	LTPPipeLine defaultPipe = new LTPPipeLine();
	LTPResult result = defaultPipe.parser("我的邮箱坏了，找谁处理");
	System.out.println(result.getTerms());
	LTPPipeLine ltpPipeLine = new LTPPipeLine(LTP_TASK.TOKEN, LTP_TASK.POS);
	System.out.println(ltpPipeLine.parser("我的邮箱坏了，找谁处理").getTerms());
```



