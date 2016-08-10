# xconfig

## 简介
基于zookeeper的配置中心，统一存储应用的配置，可以解决项目中各种环境（profile）的配置文件分散，难以维护问题。可以主动推送配置变化信息，让应用实时感知配置变化。

## 名词解释
* project 表示一个项目，比如user-web,mysql。
* profile 表示一个项目的不同环境（maven中也用profile来区分环境），比如user-web分为dev，alpha，beta，pre，prd环境。
* key 表示一个配置项，与properties文件中的key一直，xconfig中的key值都是以project开头的，比如user-web中的key为：user-web.size,user-web.tag
* value 表示一个value值
* 依赖 为了简化配置，project允许依赖，比如user-web可能要用到mysql数据，可以设置user-web依赖mysql，项目启动时拉取user-web配置会将其依赖的mysql配置也加入进来。

## web界面预览
![web界面预览1](doc/xconfig-web1.png)
![web界面预览2](doc/xconfig-web2.png)
![web界面预览3](doc/xconfig-web3.png)

* 所见即所得，所有配置项都能在web界面中看到（其中高密的value会被隐藏，需要一定权限），方便项目开发时候使用。
* 提供模糊匹配筛选功能，能够快速筛选想看的key。
* 修改value会实时（准实时）推送到应用中。

## xconfig-web部署


## client如何使用
1. 引入jar

	    <groupId>com.zebra.carcloud</groupId>
	    <artifactId>xconfig-client</artifactId>
	    <version>0.0.1-SNAPSHOT</version>
	    
2. spring中如何配置
		  
		  <!-- 最小配置，依赖的project -->
        <bean id="xConfig" class="com.zebra.xconfig.client.XConfig" init-method="init">
            <property name="project" value="odps-service"></property>
        </bean>

	    <!-- 与spring结合的工具类，支持${}获取属性值 -->
        <bean class="com.zebra.xconfig.client.XConfigPropertyPlaceholderConfigurer">
            <property name="XConfig" ref="xConfig"/>
        </bean>
        
	    <!-- eg:注入自定义的bean -->
        <bean id="mysqlConf" class="com.zebra.xconfig.client.MysqlConf">
            <property name="password" value="${mysql.jdbc.password}"></property>
        </bean>

        <!-- eg:数据源配置 -->
        <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
            <property name="driverClassName" value="${mysql.jdbc.driver}"></property>
            <property name="url" value="${mysql.jdbc.url}"></property>
            <property name="username" value="${mysql.jdbc.username}"></property>
            <property name="password" value="${mysql.jdbc.password}"></property>
        </bean>

3. 指定profile等配置信息，公共配置。默认读取当前用户目录下（~/.xconfig/config.properties）文件，其中有zk连接串，用户名信息，以及当前机器所属的环境。eg:/Users/ying/.xconfig/config.preperties。
 
	    profile=daily
	    zkConn=localhost:2181
	    userName=xconfig
	    password=xconfig
	 
	* profile信息目前只能从这里读取，也符合大部分项目部署的情况，一般不存在一个机器同时部署两个环境等情况。后续我们可以增加从启动参数设置profile信息。
	* zkConn，userName，password也是可以spring配置Xconfig的时候指定，这里做统一配置。遵循最近覆盖原则，如果在Spring配置的时候指定了，这里的配置就会被覆盖掉。
	* userName,password需要与xconfig-web部署的时候指定的一致，这个是zk节点的访问权限信息，如果xconfig-web没有指定，这里也可以不设置。
	* 当前机器上使用xconfig的项目都会读取这个配置文件。

4. 编程式获取配置信息。配置信息将会在client中缓存一份，并且会实时更新，可以通过下面这种方式编程式获取配置信息。

	    XConfig.getValue("mysql.jdbc.password");
	    XConfig.getValue("mysql.jdbc.password","defaultValue");
	 
	* 最佳实践，强烈建议使用这种方式获取配置值，不建议自己缓存一份value使用，使用此方法总是能够获取到最新的配置。
	* 不可避免的我们有时候需要知道配置发生变化，xconfig也提供了监听器来感知这种变化。
	
5. 注册key监听，感知配置变化。

	    XConfig.addObserver(new XKeyObserver() {
            @Override
            public String getKey() {
                return "mysql.jdbc.password";
            }

            @Override
            public void change(String value) {
                logger.debug("===change===>{}:{}",getKey(),value);
            }
        });
        
    * 在频繁更新某个value的情况下，zk会保证client得到的value的最终一致性，此监听器中的回调方法也一样。当你需要在新线程中处理value变化的时候，需要你自己来保证一致性。
    * 建议回调方法中不要做耗时操作。
    
## 设计架构
1. 111111
2. 222222
3. 333333
	* 333333
	* 333333
## 可能的问题