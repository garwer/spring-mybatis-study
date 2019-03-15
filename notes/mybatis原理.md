## Mybatis

MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Old Java Objects,普通的 Java对象)映射成数据库中的记录。

```
持久层框架：可以从存储的方式来理解，数据存放在硬盘或数据库上，不会像在内存一样丢失数据。
对于很多业务来说，数据比应用程序更重要【比如订单数据客户数据】，因此数据持久功能大多是必要的，而持久层框架则提供了便利性。
ORM:Object Relational Mapping，即对象关系映射。它的实现思想就是将关系数据库中表的数据映射成为对象，以对象的形式展现，这样开发人员就可以把对数据库的操作转化为对这些对象的操作。因此它的目的是为了方便开发人员以面向对象的思想来实现对数据库的操作。【简单来说就是可以通过操纵对象来操纵数据库】
```



### mybatis对比

jdbc是java提供操作数据库的api【 1 //JDBC的步骤，1.加载驱动。2.获取连接。3.执行sql语句。4.处理结果集。5.关闭资源】

```java
Class.forName("com.mysql.jdbc.Driver").newInstance();
Connection conn=DrivateManager.getConnection(url,username,password);
java.sql.PrepareStatement st=conn.prepareStatement(sql);
st.setInt(0,1);
st.execute();
java.sql.ResultSet rs=st.getResultSet();
while(rs.next()){
  String resultString=getString(columnname);      
}
```

对比jdbc

```
1.sql管理
使用jdbc操作数据库、sql语句散落在不同java类中
①可读性差、不易于维护和性能调优、不方便拿出来测试【硬编码的方式、可能要去找，中间还有java代码，有append和+等】
②改动java代码需要重新编译，打包部署
而myabtis可以将sql集中分配在自定义的地方，以xml的形式
2.动态sql
在查询或其它操作需要根据一些属性组合查询，比如根据两个、三个不同属性查询，可以根据是否为null进行查询，mybatis提供了一些<if> <when></otherwise>之类的标签可让我们动态选择需要的，如果是jdbc可能要多写java代码、mybatis封装了大量java代码动态支持，节省java代码量
3.连接问题
连接池多样、可能变化。
传统:
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"       
       destroy-method="close">       
    <property name="driverClassName" value="com.mysql.jdbc.Driver" />      
    <property name="url" value="jdbc:mysql://localhost:3309/sampledb" />      
    <property name="username" value="root" />      
    <property name="password" value="1234" />      
</bean>

通过DataSource进行隔离解耦，我们统一从DataSource里面获取数据库连接，DataSource具体由DBCP实现还是由容器的JNDI实现都可以，所以我们将DataSource的具体实现通过让用户配置来应对变化。
4.结果集映射
jdbc查询返回结果集，ResultSet，需要根据ResultSet转成我们需要的类型，使用mybatis可以配置resultType和resultMap自动转成我们需要的格式，接口层返回相应的数据类型。
public static JSONArray toJSON(ResultSet rs) throws SQLException {
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
	//获取有关数据的数据【描述数据 通过这个获取列名 行数等】
    ResultSetMetaData metaData = rs.getMetaData();
    jsonArray = new JSONArray();
    while (rs.next()) {
    	jsonObject = new JSONObject();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
        jsonObject.put(metaData.getColumnLabel(i), StringUtils
        .isEmpty(rs.getString(i)) ? "" : rs.getString(i));
    }
   	 jsonArray.add(jsonObject);
 	}

    return jsonArray;
}
5:重复sql
如果只是部分重复、可以独立出来
```

```
对性能要求高、底层要求高还是用jdbc，需要mybatis动态sql、动态配置。方便数据库管理【jdbc:数据库连接，使用时就创建，不使用立即释放，对数据库进行频繁连接开启和关闭，造成数据库资源浪费，影响数据库性能。
mybatis：在SqlMapConfig.xml中配置数据连接池,使用数据库连接池管理数据库连接】的话建议mybatis。
```

对比spring中的jdbcTemplate

```
springjdbcTemplate太简便、不易于统一管理，java代码量更大，sql分散在不同的java类中，可读性差。
```



##### Mybatis初始化过程

```java
public class Main {
    public static void main(String[] args) throws IOException {

        String resource = "mybatis.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
            List<Product> productList = productMapper.selectProductList();
            for (Product product : productList) {
                System.out.printf(product.toString());
            }
        } finally {
            sqlSession.close();
        }
    }
}
```

```
1.系统初始化时、读取配置文件、将其解析成InputStream
2.创建SqlSessionFactoryBuilder对象
3.通过重载build创建SqlSessionFactory 
[这边要经过XMLConfigBuilder]
```

此处openSession()方法还有很多个重载

```
SqlSession openSession()
SqlSession openSession(boolean autoCommit)
SqlSession openSession(Connection connection)
SqlSession openSession(TransactionIsolationLevel level)
SqlSession openSession(ExecutorType execType,TransactionIsolationLevel level)
SqlSession openSession(ExecutorType execType)
SqlSession openSession(ExecutorType execType, boolean autoCommit)
SqlSession openSession(ExecutorType execType, Connection connection)
Configuration getConfiguration();
默认的 openSession()方法没有参数，它会创建有如下特性的 SqlSession：
会开启一个事务（也就是不自动提交）。  
将从由当前环境配置的 DataSource 实例中获取 Connection 对象。
事务隔离级别将会使用驱动或数据源的默认设置。
预处理语句不会被复用，也不会批量处理更新。
```

```
默认情况下mybatis不会自动提交事务，除非侦测到有修改的操作改变了数据库
```



##### 创建SqlSessionFactory对象

SqlSessionFactoryBuilder是用来创建SqlSessionFactory对象

SqlSessionFactoryBuilder中只有一些重载的build函数，这些build函数的入参都是MyBatis配置文件的输入流，返回值都是SqlSessionFactory；由此可见，SqlSessionFactoryBuilder的作用很纯粹，就是用来通过配置文件创建SqlSessionFactory对象的。作者：大

SqlSessionFactoryBuilder里多个重载、只有build方法、返回对象类型都是SqlSessionFactory、目的只是返回SqlSessionFactory对象

```java
public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
    SqlSessionFactory var5;
    try {
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
        var5 = this.build(parser.parse());
    } catch (Exception var14) {
        throw ExceptionFactory.wrapException("Error building SqlSession.", var14);
    } finally {
        ErrorContext.instance().reset();

        try {
            reader.close();
        } catch (IOException var13) {
            ;
        }

    }
    return var5;
}

此处build返回DefaultSqlSessionFactory类型
    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
    
DefaultSqlSessionFactory的openSession方法

 
 public SqlSession openSession() {
        return this.
        openSessionFromDataSource(this.configuration.getDefaultExecutorType(), (TransactionIsolationLevel)null, false);
    }


    private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
        Transaction tx = null;

        DefaultSqlSession var8;
        try {
        	//加载配置文件中数据库信息
            Environment environment = this.configuration.getEnvironment();
            //创建事务工厂
            TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
            //实例化事务对象。以及是否自动提交事务
            tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
            //从配置中获取Executor执行器
            Executor executor = this.configuration.newExecutor(tx, execType);
            var8 = new DefaultSqlSession(this.configuration, executor, autoCommit);
        } catch (Exception var12) {
            this.closeTransaction(tx);
            throw ExceptionFactory.wrapException("Error opening session.  Cause: " + var12, var12);
        } finally {
            ErrorContext.instance().reset();
        }

        return var8;
    }
    
此处实例化了TransactionFactory事务工厂 是个接口 只有两个实现类
JdbcTransactionFactory和ManagedTransactionFactory
```



##### build的时候构建XMLConfigBuilder

XMLConfigBuilder是用来解析xml配置文件的、用于解析mybatis配置文件【非映射】

XMLMapperBuilder用于解析映射文件

这些builder的共同父类-BaseBuilder、维护一个Configuration对象、解析后以这个对象存储

初始化这些映射文件时就会 super(new Configuration()) 初始化Configuration

初始化`Configuration`对象的时候，一些别名会被注册到`Configuration`的`typeAliasRegistry`容器中

XMLConfigBuilder有个parseConfiguration方法 用于解析配置文件

```java
//root.evalNode用于获取标签名
private void parseConfiguration(XNode root) {
    try {
        this.propertiesElement(root.evalNode("properties"));
        Properties settings = this.settingsAsProperties(root.evalNode("settings"));
        this.loadCustomVfs(settings);
        this.typeAliasesElement(root.evalNode("typeAliases"));
        this.pluginElement(root.evalNode("plugins"));
        this.objectFactoryElement(root.evalNode("objectFactory"));
        this.objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
        this.reflectorFactoryElement(root.evalNode("reflectorFactory"));
        this.settingsElement(settings);
        this.environmentsElement(root.evalNode("environments"));
        this.databaseIdProviderElement(root.evalNode("databaseIdProvider"));
        this.typeHandlerElement(root.evalNode("typeHandlers"));
        this.mapperElement(root.evalNode("mappers"));
    } catch (Exception var3) {
        throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + var3, var3);
    }
}

//事务上的控制
private void environmentsElement(XNode context) throws Exception {
  if (context != null) {
    if (this.environment == null) {
      this.environment = context.getStringAttribute("default");
    }

    Iterator var2 = context.getChildren().iterator();

    while(var2.hasNext()) {
      XNode child = (XNode)var2.next();
      String id = child.getStringAttribute("id");
      if (this.isSpecifiedEnvironment(id)) {
        TransactionFactory txFactory = this.transactionManagerElement(child.evalNode("transactionManager"));
        DataSourceFactory dsFactory = this.dataSourceElement(child.evalNode("dataSource"));
        DataSource dataSource = dsFactory.getDataSource();
        Builder environmentBuilder = (new Builder(id)).transactionFactory(txFactory).dataSource(dataSource);
        this.configuration.setEnvironment(environmentBuilder.build());
      }
    }
  }

}

//不同标签不同解析方式
//http://www.mybatis.org/mybatis-3/zh/configuration.html
//比如properties可以动态配置数据源
<properties resource="org/mybatis/example/config.properties">
  <property name="username" value="dev_user"/>
  <property name="password" value="F2Fa3!33TYyg"/>
</properties>

<dataSource type="POOLED">
  <property name="driver" value="${driver}"/>
  <property name="url" value="${url}"/>
  <property name="username" value="${username}"/>
  <property name="password" value="${password}"/>
</dataSource>

//mapper映射器
mybatis的行为配置后，接下来要定义sql映射语句，要告诉mybatis去哪里找sql、即找映射文件
<mappers></mappers>有多种写法 类路径。文件路径、接口映射权限定名 package下全为映射器等等
<!-- 使用相对于类路径的资源引用 -->
<mappers>
  <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
  <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
  <mapper resource="org/mybatis/builder/PostMapper.xml"/>
</mappers>

<!-- 使用映射器接口实现类的完全限定类名 -->
<mappers>
  <mapper class="org.mybatis.builder.AuthorMapper"/>
  <mapper class="org.mybatis.builder.BlogMapper"/>
  <mapper class="org.mybatis.builder.PostMapper"/>
</mappers>

<mappers>
  <package name="org.mybatis.builder"/>
</mappers>
```

创建sqlSessionFactory后，创建SqlSession -> 开启一个数据库访问会话---创建SqlSession对象：

MyBatis封装了对数据库的访问，把对数据库的会话[操作:select/update/delete等]和事务控制放到了SqlSession对象中。

为SqlSession传递一个配置的Sql语句 的Statement Id和参数，然后返回结果：

```
<environments default="development">
  <environment id="development">
    <transactionManager type="JDBC">
        ...
    <dataSource type="POOLED">
        ...
  </environment>
  <environment id="production">
    <transactionManager type="MANAGED">
        ...
    <dataSource type="JNDI">
        ...
  </environment>
</environments>
```



#### 不使用xml构建SqlSessionFactory

【可以创建自己的Configuration构造器】 还有很多工厂类传参TransactionFactory

```java
DataSource dataSource = BlogDataSourceFactory.getBlogDataSource();
TransactionFactory transactionFactory = new JdbcTransactionFactory();
Environment environment = new Environment("development", transactionFactory, dataSource);
Configuration configuration = new Configuration(environment);
configuration.addMapper(BlogMapper.class);
//通过build传一个封装的对象configuration
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
```

```
MyBatis使用JDBC进行事务管理的时候，会默认开始事务，并设置不自动提交事务
```



#### mybatis xml映射文件

mybatis真正强大的地方就在于它的映射语句，因为强大，xml文件相对简单，对比jdbc可以省下很多代码量。

mybatis就是针对sql构建

```
sql映射文件几个顶级元素
cache【给定明明空间缓存配置的引用】、select、insert、update、delete、sql、resultMap[最复杂也是最强大，用来描述数据库结果集中来加载对象]
```

##### select

多个元素:

```
id:命名空间唯一标识符、可以被用来引用这条语句
parameterType:参数类[可选。 MyBatis 可以通过 TypeHandler 推断出具体传入语句的参数，默认值为 unset]
resultType	从这条语句中返回的期望类型的类的完全限定名或别名。注意如果是集合情形，那应该是集合可以包含的类型，而不能是集合本身。使用 resultType 或 resultMap，但不能同时使用。
resultMap	外部 resultMap 的命名引用。结果集的映射是 MyBatis 最强大的特性，对其有一个很好的理解的话，许多复杂映射的情形都能迎刃而解。使用 resultMap 或 resultType，但不能同时使用。
flushCache	将其设置为 true，任何时候只要语句被调用，都会导致本地缓存和二级缓存都会被清空，默认值：false。
useCache	将其设置为 true，将会导致本条语句的结果被二级缓存，默认值：对 select 元素为 true。
timeout	这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为 unset（依赖驱动）。
statementType	STATEMENT，PREPARED 或 CALLABLE 的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。
```



##### insert

```
selectKey:设置键值 before
<insert id="insertAuthor">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
  </selectKey>
  insert into Author
    (id, username, password, email,bio, favourite_section)
  values
    (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
</insert>

rder	这可以被设置为 BEFORE 或 AFTER。如果设置为 BEFORE，那么它会首先选择主键，设置 keyProperty 然后执行插入语句。如果设置为 AFTER，那么先执行插入语句，然后是 selectKey 元素 - 这和像 Oracle 的数据库相似，在插入语句内部可能有嵌入索引调用。
```



##### sql

```
<sql id="sometable">
  ${prefix}Table
</sql>
<include refid="someinclude">
//支持多层嵌套
```



##### 缓存

mybatis包含非常强大的查询缓存特性、还可以方便的配置和定制。

默认没有开启缓存，除了局部的session缓存、要开启缓存

```
在sql映射文件中添加一行
<cache/>
效果如下
映射语句文件中的所有 select 语句将会被缓存。
映射语句文件中的所有 insert,update 和 delete 语句会刷新缓存。
缓存会使用 Least Recently Used(LRU,最近最少使用的)算法来收回。[LRU – 最近最少使用的:移除最长时间不被使用的对象。
FIFO – 先进先出:按对象进入缓存的顺序来移除它们。
SOFT – 软引用:移除基于垃圾回收器状态和软引用规则的对象。
WEAK – 弱引用:更积极地移除基于垃圾收集器状态和弱引用规则的对象。]
根据时间表(比如 no Flush Interval,没有刷新间隔), 缓存不会以任何时间顺序 来刷新。
缓存会存储列表集合或对象(无论查询方法返回什么)的 1024 个引用。
缓存会被视为是 read/write(可读/可写)的缓存,意味着对象检索不是共享的,而 且可以安全地被调用者修改,而不干扰其他调用者或线程所做的潜在修改。
```

自定义缓存

```
<cache type="com.domain.something.MyCustomCache"/>
```

必须实现org.mybatis.cache.Cache 接口。这个接口是 MyBatis 框架中很多复杂的接口之一，但是用起来简单，给定操作即可

```
public interface Cache {
  String getId();
  int getSize();
  void putObject(Object key, Object value);
  Object getObject(Object key);
  boolean hasKey(Object key);
  Object removeObject(Object key);
  void clear();
}

可以配置是否使用缓存
<select ... flushCache="false" useCache="true"/>
<insert ... flushCache="true"/>
<update ... flushCache="true"/>
<delete ... flushCache="true"/>

还可以配置缓存生成文件
<cache type="com.domain.something.MyCustomCache">
  <property name="cacheFile" value="/tmp/my-custom-cache.tmp"/>
</cache>
```

#### 参照缓存

回想一下上一节内容, 这个特殊命名空间的唯一缓存会被使用或者刷新相同命名空间内 的语句。也许将来的某个时候,你会想在命名空间中共享相同的缓存配置和实例。在这样的 情况下你可以使用 cache-ref 元素来引用另外一个缓存。

```
<cache-ref namespace="com.someone.application.data.SomeMapper"/>
```



### 动态sql

mybatis强大特性之一，如果用jdbc会有拼接的痛苦【例如必要的空格】，方便我们编写动态sql

OGNL表达式

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

```java
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG WHERE state = ‘ACTIVE’
  <choose>
    <when test="title != null">
      AND title like #{title}
    </when>
    <when test="author != null and author.name != null">
      AND author_name like #{author.name}
    </when>
    <otherwise>
      AND featured = 1
    </otherwise>
  </choose>
</select>
```



```
where元素只有当有匹配的时候才会去插入where字句 若语句开头有不必要的AND或OR也会自动去除
<select id="findActiveBlogLike"
     resultType="Blog">
  SELECT * FROM BLOG 
  <where> 
    <if test="state != null">
         state = #{state}
    </if> 
    <if test="title != null">
        AND title like #{title}
    </if>
    <if test="author != null and author.name != null">
        AND author_name like #{author.name}
    </if>
  </where>
</select>
如果where没有按正常套路出牌，可以自定义trim来定制where元素的功能，和 where 元素等价的自定义 trim 元素为：
<trim prefix="WHERE" prefixOverrides="AND |OR ">
  ... 
</trim>
prefixOverrides 属性会忽略通过管道分隔的文本序列（注意此例中的空格也是必要的）。它的作用是移除所有指定在 prefixOverrides 属性中的内容，并且插入 prefix 属性中指定的内容。

类似的用于动态更新语句的解决方案叫做 set。set 元素可以用于动态包含需要更新的列，而舍去其它的。比如：
<update id="updateAuthorIfNecessary">
  update Author
    <set>
      <if test="username != null">username=#{username},</if>
      <if test="password != null">password=#{password},</if>
      <if test="email != null">email=#{email},</if>
      <if test="bio != null">bio=#{bio}</if>
    </set>
  where id=#{id}
</update>

set 元素等价的自定义 trim 元素的真面目：
<trim prefix="SET" suffixOverrides=",">
  ...
</trim>
```



### foreach

​       动态 SQL 的另外一个常用的操作需求是对一个集合进行遍历，通常是在构建 IN 条件语句的时候。比如：

```
<select id="selectPostIn" resultType="domain.blog.Post">
  SELECT *
  FROM POST P
  WHERE ID in
  <foreach item="item" index="index" collection="list"
      open="(" separator="," close=")">
        #{item}
  </foreach>
</select>
```

#### bind

`bind` 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。比如：

```
<select id="selectBlogsLike" resultType="Blog">
  <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
  SELECT * FROM BLOG
  WHERE title LIKE #{pattern}
</select>
```

##### 多数据库支持 

一个配置了“_databaseId”变量的 databaseIdProvider 可用于动态代码中，这样就可以根据不同的数据库厂商构建特定的语句。比如下面的例子：

```
<insert id="insert">
  <selectKey keyProperty="id" resultType="int" order="BEFORE">
    <if test="_databaseId == 'oracle'">
      select seq_users.nextval from dual
    </if>
    <if test="_databaseId == 'db2'">
      select nextval for seq_users from sysibm.sysdummy1"
    </if>
  </selectKey>
  insert into users values (#{id}, #{name})
</insert>
```

```
AbstractSQL提供SELECT() WHERE等方法
构造new SQL时再toString可以返回拼接的sql 可读性好
```



Mybatis 的内置日志工厂提供日志功能，内置日志工厂将日志交给以下其中一种工具作代理：

- SLF4J
- Apache Commons Logging
- Log4j 2
- Log4j
- JDK logging



#### mybatis的事务

事务工厂的创建： 

   Mybatis的事务是交给TransactionFactory来创建，如果我们将<transactionManager>的type 配置为"JDBC",那么，在Mybatis初始化解析<environment>节点时，XMLConfigBuilder会根据type="JDBC"创建一个JdbcTransactionFactory工厂，其源码如下

Mybatis管理事务是分为两种方式:

(1)使用JDBC的事务管理机制,就是利用java.sql.Connection对象完成对事务的提交 

(2)使用MANAGED的事务管理机制，这种机制mybatis自身不会去实现事务管理，而是让程序的容器（JBOSS,WebLogic）来实现对事务的管理

```
sqlSessionFactory是个接口里面有openSession方法 创建sqlSession
```

```
transactionManager的type决定我们用什么事务管理机制
```

```
XMLConfigBuilder类中通过这个配置确定我们用哪个事务
private TransactionFactory transactionManagerElement(XNode context) throws Exception {
    if (context != null) {
        String type = context.getStringAttribute("type");
        Properties props = context.getChildrenAsProperties();
        TransactionFactory factory = (TransactionFactory)this.resolveClass(type).newInstance();
        factory.setProperties(props);
        return factory;
    } else {
        throw new BuilderException("Environment declaration requires a TransactionFactory.");
    }
}

//是个接口 创建事务可以用Connection对象或者DataSource对象
public interface TransactionFactory {
    void setProperties(Properties var1);

    Transaction newTransaction(Connection var1);

    Transaction newTransaction(DataSource var1, TransactionIsolationLevel var2, boolean var3);
}


public class JdbcTransactionFactory implements TransactionFactory {
    public JdbcTransactionFactory() {
    }

    public void setProperties(Properties props) {
    }

    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    public Transaction newTransaction(DataSource ds, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(ds, level, autoCommit);
    }
}

JdbcTransactionFactory 创建事务
JdbcTransaction提供了一系列事务方法
里面也是调了connection对象的提交事务方法[传入connection]
如果我们使用MyBatis构建本地程序，即不是WEB程序，若将type设置成"MANAGED"，那么，我们执行的任何update操作，即使我们最后执行了commit操作，数据也不会保留，不会对数据库造成任何影响。因为我们将MyBatis配置成了“MANAGED”，即MyBatis自己不管理事务，而我们又是运行的本地程序，没有事务管理功能，所以对数据库的update操作都是无效的
```

### myabtis原理

```java
XMLConfigBuilder构造器会调用父类方法，初始化一个Configuration对象作为参数，同时这个参数会初始化很多属性。
public BaseBuilder(Configuration configuration) {
    this.configuration = configuration;
    this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
}
初始化后的又作为
public abstract class BaseBuilder {
    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }
  ....  
 }   

。。。XMLConfigBuilder中的方法
    private void parseConfiguration(XNode root) {
        try {
            this.propertiesElement(root.evalNode("properties"));
            Properties settings = this.settingsAsProperties(root.evalNode("settings"));
            this.loadCustomVfs(settings);
            this.typeAliasesElement(root.evalNode("typeAliases"));
            this.pluginElement(root.evalNode("plugins"));
            this.objectFactoryElement(root.evalNode("objectFactory"));
            this.objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
            this.reflectorFactoryElement(root.evalNode("reflectorFactory"));
            this.settingsElement(settings);
            this.environmentsElement(root.evalNode("environments"));
            this.databaseIdProviderElement(root.evalNode("databaseIdProvider"));
            this.typeHandlerElement(root.evalNode("typeHandlers"));
            this.mapperElement(root.evalNode("mappers"));
        } catch (Exception var3) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + var3, var3);
        }
    }
    


    private void mapperElement(XNode parent) throws Exception {
        if (parent != null) {
            Iterator var2 = parent.getChildren().iterator();

            while(true) {
                while(var2.hasNext()) {
                    XNode child = (XNode)var2.next();
                    String resource;
                    if ("package".equals(child.getName())) {
                        resource = child.getStringAttribute("name");
                        this.configuration.addMappers(resource); //如果是package addMapper name资源路径 放进来
                    } else {
                        resource = child.getStringAttribute("resource");
                        String url = child.getStringAttribute("url");
                        String mapperClass = child.getStringAttribute("class");
                        //这个类用于解析mapper映射文件  
                        XMLMapperBuilder mapperParser;
                        InputStream inputStream;
                        if (resource != null && url == null && mapperClass == null) {
                            ErrorContext.instance().resource(resource);
                            inputStream = Resources.getResourceAsStream(resource);
                            mapperParser = new XMLMapperBuilder(inputStream, this.configuration, resource, this.configuration.getSqlFragments());
                            mapperParser.parse();
                        } else if (resource == null && url != null && mapperClass == null) {
                            ErrorContext.instance().resource(url);
                            inputStream = Resources.getUrlAsStream(url);
                            mapperParser = new XMLMapperBuilder(inputStream, this.configuration, url, this.configuration.getSqlFragments());
                            mapperParser.parse();
                        } else {
                            if (resource != null || url != null || mapperClass == null) {
                                throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
                            }
						  //传入接口
                            Class<?> mapperInterface = Resources.classForName(mapperClass);
                            this.configuration.addMapper(mapperInterface);/
                        }
                    }
                }

                return;
            }
        }
    }
```

mybatis-sqlSession接口和Executor

不管是哪一种数据操作都离不开sqlSession接口实例【？】

以DefaultSqlSession为例

```java
//两个私有变量
private Configuration configuration;
private Executor executor;

//在构造方法给它赋值
public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
    this.configuration = configuration;
    this.executor = executor;
    this.dirty = false;
    this.autoCommit = autoCommit;
}
...
后面会用到
    public Configuration parse() {
        if (this.parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        } else {
            this.parsed = true;
            this.parseConfiguration(this.parser.evalNode("/configuration"));
            return this.configuration;
        }
    }
```

```java
      List<Author> authors = session.selectList("org.apache.ibatis.domain.blog.mappers.AuthorMapper.selectAllAuthors");

@Override
public <E> List<E> selectList(String statement) {
    return this.selectList(statement, null);
}

@Override
public <E> List<E> selectList(String statement, Object parameter) {
  return this.selectList(statement, parameter, RowBounds.DEFAULT);
}

@Override
public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
  try {
     //从配置中取到MappedStatement信息  
     //sql信息也封装在这里
    MappedStatement ms = configuration.getMappedStatement(statement);
    return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
  } catch (Exception e) {
    throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
  } finally {
    ErrorContext.instance().reset();
  }
}
//传入statment从configuration获取MappedStatement实例，再通过Excuter实例的query方法查询
//此处的query不一定从sql查询 也有可能从缓存直接返回
//mybatis根据statement(作为唯一标识的id 就是namespace+sqlid的形式)
//Each XMLConfigBuilder can only be used once

//封装sql信息的过程
StrictMap<V> 继承HashMap

//
   public MappedStatement getMappedStatement(String id) {
        return this.getMappedStatement(id, true);
    }

    protected void buildAllStatements() {
        Collection var1;
        if (!this.incompleteResultMaps.isEmpty()) {
            var1 = this.incompleteResultMaps;
            synchronized(this.incompleteResultMaps) {
                ((ResultMapResolver)this.incompleteResultMaps.iterator().next()).resolve();
            }
        }

        if (!this.incompleteCacheRefs.isEmpty()) {
            var1 = this.incompleteCacheRefs;
            synchronized(this.incompleteCacheRefs) {
                ((CacheRefResolver)this.incompleteCacheRefs.iterator().next()).resolveCacheRef();
            }
        }

        if (!this.incompleteStatements.isEmpty()) {
            var1 = this.incompleteStatements;
            synchronized(this.incompleteStatements) {
                ((XMLStatementBuilder)this.incompleteStatements.iterator().next()).parseStatementNode();
            }
        }

        if (!this.incompleteMethods.isEmpty()) {
            var1 = this.incompleteMethods;
            synchronized(this.incompleteMethods) {
                ((MethodResolver)this.incompleteMethods.iterator().next()).resolve();
            }
        }

    }
```

```java
public SqlSessionFactory build(Configuration config) {
    return new DefaultSqlSessionFactory(config);
}
//build默认返回DefaultSqlSessionFactory

//DefaultSqlSessionFactory的openSession
public SqlSession openSession() {
        return this.openSessionFromDataSource(this.configuration.getDefaultExecutorType(), (TransactionIsolationLevel)null, false);
    }
【此处this.configuration.getDefaultExecutorType()
        public ExecutorType getDefaultExecutorType() {
        return this.defaultExecutorType;
    }

   public Configuration() {
        this.safeResultHandlerEnabled = true;
        this.multipleResultSetsEnabled = true;
        this.useColumnLabel = true;
        this.cacheEnabled = true;
        this.useActualParamName = true;
        this.localCacheScope = LocalCacheScope.SESSION;
        //如果返回类型为map 是否显示为null的数据
        this.jdbcTypeForNull = JdbcType.OTHER;
        this.lazyLoadTriggerMethods = new HashSet(Arrays.asList("equals", "clone", "hashCode", "toString"));
        this.defaultExecutorType = ExecutorType.SIMPLE; //枚举 默认SIMPLE
       。。。很多构造函数默认值
   }
 】

private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
    Transaction tx = null;
    DefaultSqlSession var8;
    try {
        Environment environment = this.configuration.getEnvironment();
        TransactionFactory transactionFactory = this.getTransactionFactoryFromEnvironment(environment);
        //事务相关 
        tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
        Executor executor = this.configuration.newExecutor(tx, execType); //创建Executor
        var8 = new DefaultSqlSession(this.configuration, executor, autoCommit); //此处返回DefaultSqlSession
    } catch (Exception var12) {
        this.closeTransaction(tx);
        throw ExceptionFactory.wrapException("Error opening session.  Cause: " + var12, var12);
    } finally {
        ErrorContext.instance().reset();
    }

    return var8;
}
```

```java
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? this.defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Object executor;
    if (ExecutorType.BATCH == executorType) {
        executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
        executor = new ReuseExecutor(this, transaction);
    } else {
        executor = new SimpleExecutor(this, transaction);
    }

    if (this.cacheEnabled) {
        executor = new CachingExecutor((Executor)executor);
    }

    Executor executor = (Executor)this.interceptorChain.pluginAll(executor);
    return executor;
}

//通过枚举 默认使用SimpleExecutor
这边SimpleExecutor继承自BaseExecutor，里面query方法不是抽象的而且没有重写用的是这边，doQuery之类的抽象的，需要重写【SimpleExecutor是普通类必须重写所有抽象方法】
```

#### mybatis一级缓存和二级缓存



### mybatis-动态代理部分

mybatis如何执行sql，肯定不能单靠接口，需要有个实例。

```
大胆猜测mybatis为接口生成动态代理类后最后还是调用sqlSession的系列方法操作sql
 UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<HashMap<String, Object>> userList = userMapper.getAll();
            for (HashMap<String, Object> map : userList) {
                for(Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println(entry.getKey());
                    System.out.println(entry.getValue());
                }
            }
```

```java
//DefaultSqlSession
public <T> T getMapper(Class<T> type) {
    return this.configuration.getMapper(type, this);
}

//configuration
public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
  return this.mapperRegistry.getMapper(type, sqlSession);
}

//MapperRegistry
//该类用于存放MapperProxyFactory类
public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
  //创建一个代理工厂类
  MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory)this.knownMappers.get(type);
  if (mapperProxyFactory == null) {
    throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
  } else {
    try {
      //newInstance实例化一个 MapperProxy类
      return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception var5) {
      throw new BindingException("Error getting mapper instance. Cause: " + var5, var5);
    }
  }
}

//MapperProxyFactory
public T newInstance(SqlSession sqlSession) {
  MapperProxy<T> mapperProxy = new MapperProxy(sqlSession, this.mapperInterface, this.methodCache);
  return this.newInstance(mapperProxy);
}

//MapperProxyFactory
protected T newInstance(MapperProxy<T> mapperProxy) {
  return Proxy.newProxyInstance(this.mapperInterface.getClassLoader(), new Class[]{this.mapperInterface}, mapperProxy);
}
```

```java
//MapperProxy中的方法
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        if (this.isDefaultMethod(method)) {
            return this.invokeDefaultMethod(proxy, method, args);
        }
    } catch (Throwable var5) {
        throw ExceptionUtil.unwrapThrowable(var5);
    }
    //invoke方法里面的操作 
    MapperMethod mapperMethod = this.cachedMapperMethod(method);
    return mapperMethod.execute(this.sqlSession, args);
}

//放到methodCache缓存[MapperProxy中有个private final Map<Method, MapperMethod> methodCache;缓存]
private MapperMethod cachedMapperMethod(Method method) {
  MapperMethod mapperMethod = (MapperMethod)this.methodCache.get(method);
  if (mapperMethod == null) {
    mapperMethod = new MapperMethod(this.mapperInterface, method, this.sqlSession.getConfiguration());
    this.methodCache.put(method, mapperMethod);
  }

  return mapperMethod;
}
```

```java
getMapper是获取相关数据操作类的接口。mybatis会为这个接口生成一个动态代理类，执行接口方法时，这个时候会触发MapperProxy的invoke方法
//MapperMethod生成一个方法 执行execute类 里面先判断标签类型 根据command.getType()
//再获取参数 最后还是执行sqlSession.
//select相对麻烦点 根据不同类型
public Object execute(SqlSession sqlSession, Object[] args) {
        Object param;
        Object result;
        switch(this.command.getType()) {
        //多个case 增删改比较简单[sqlSession delete update insert返回类型也都是int]  result会对这个做一层简单的包装rowCountResult 判断返回类型是不是int这样的 
        case INSERT:
            param = this.method.convertArgsToSqlCommandParam(args);
            result = this.rowCountResult(sqlSession.insert(this.command.getName(), param));
            break;
        case UPDATE:
            param = this.method.convertArgsToSqlCommandParam(args);
            result = this.rowCountResult(sqlSession.update(this.command.getName(), param));
            break;
        case DELETE:
            param = this.method.convertArgsToSqlCommandParam(args);
            result = this.rowCountResult(sqlSession.delete(this.command.getName(), param));
            break;
        case SELECT:
            if (this.method.returnsVoid() && this.method.hasResultHandler()) {
                this.executeWithResultHandler(sqlSession, args);
                result = null;
            } else if (this.method.returnsMany()) {
                result = this.executeForMany(sqlSession, args);
            } else if (this.method.returnsMap()) {
                result = this.executeForMap(sqlSession, args);
            } else if (this.method.returnsCursor()) {
                result = this.executeForCursor(sqlSession, args);
            } else {
                param = this.method.convertArgsToSqlCommandParam(args);
                result = sqlSession.selectOne(this.command.getName(), param);
            }
            break;
        case FLUSH:
            result = sqlSession.flushStatements();
            break;
        default:
            throw new BindingException("Unknown execution method for: " + this.command.getName());
        }

        if (result == null && this.method.getReturnType().isPrimitive() && !this.method.returnsVoid()) {
            throw new BindingException("Mapper method '" + this.command.getName() + " attempted to return null from a method with a primitive return type (" + this.method.getReturnType() + ").");
        } else {
            return result;
        }
    }
```

```
感觉mybatis的动态代理没有做什么增强操作，最后还是根据参数什么的调用sqlSession的执行方法
```



### mybatis-执行sql部分

mybatis的id是由namespace和id确定唯一性

mybatis根据

sql放在哪

```
Configuration中的    protected final Map<String, MappedStatement> mappedStatements;
其中key方法名 id是方法的全限定名
是个map 取的时候通过key取

value是MappedStatement对象 封装了大量sql属性 比如id，configuration配置 useCache
sqlCommandType(SELECT) statementTYpe(PREPARED等) timeout、resouce、主要的sql存在sqlSource中
```

```
protected final Map<String, MappedStatement> mappedStatements;
this.mappedStatements = new Configuration.StrictMap("Mapped Statements collection");
```

不同类型的xml使用不同的继承BaseBulder抽象类的XML解析器

```
XMLMapperBuilder 比如 sql/select/insert/namespace/delete之类的头部标签
XMLStatementBuilder 比如id/resultType/resultMap等等
XMLScriptBuilder 比如trim/where/set/when 这样的动态sql标签
```



###mybatis缺点




