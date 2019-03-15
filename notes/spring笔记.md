## IOC

Inversion of Controller:控制反转

spring-core中最核心部分一种思想，依赖注入思想

依赖倒置原则



```
所谓IoC，对于spring框架来说，就是由spring来负责控制对象的生命周期和对象间的关系。
```



控制方转变，控制了什么

传统java se设计，直接通过new创建对象，让程序主动去创建对象，ioc是统一用一个容器来创建这些对象，并有容器管理



反转了什么

正传是我们主动去控制依赖的对象 比如说设计一个行李箱，轮子依赖底盘，底盘依赖箱体，反转的话就相反过来了，容器帮我们查找要依赖的对象，对象只是被动的接收依赖对象，依赖对象的获取被反转了



客户端从容器获取用户类



#### ioc能做什么

不是技术，而是一种面向对象的编程思想。

思想上发生"主从换位" 原来应用程序要什么主动去要、创建，但在这个思想里，要被动等待ioc来创建并注入他所需的资源。

很好体现好莱坞原则，别找我们，我们找你，即由ioc容器帮对象找相应的依赖对象并注入，而不是对象主动去找



##### Di-**Dependency Injection**

**组件之间依赖关系**由容器在运行期决定，形象的说，即**由容器动态的将某个依赖关系注入到组件之中**。**依赖注入的目的并非为软件系统带来更多功能，而是为了提升组件重用的频率，并为系统搭建一个灵活、可扩展的平台。**通过依赖注入机制，我们只需要通过简单的配置，而无需任何代码就可指定目标需要的资源，完成自身的业务逻辑，而不需要关心具体的资源来自何处，由谁实现。

```
●谁依赖于谁：当然是应用程序依赖于IoC容器；

●为什么需要依赖：应用程序需要IoC容器来提供对象需要的外部资源；

●谁注入谁：很明显是IoC容器注入应用程序某个对象，应用程序依赖的对象；

●注入了什么：就是注入某个对象所需要的外部资源（包括对象、资源、常量数据）
```

> 题外话:Spring是一个轻量级控制反转(IOC)和面向切面(AOP)的容器框架。它提供的一站式解决方案和“生态圈”，可以说在日常的java开发中已经离不开它，但是说来惭愧，有的在校生在校期间已经把spring源码读透了，而自己一年多的开发经验大都是CRUD这样简单的操作，得不到质的提升，于是痛定思痛下定决心深入学习spring。



### 准备工作

> 1.了解[设计模式]([patterns.readthedocs.io/zh_CN/latest/creational_patterns/simple_factory.html](https://design-patterns.readthedocs.io/zh_CN/latest/creational_patterns/simple_factory.html)) 
>
> 2.了解反射，注解等基本特性

### spring整体架构图

> spring-context 会自动将 spring-core、spring-beans、spring-aop、spring-expression 这几个基础 jar 包带进来。



### Spring懒加载和非懒加载

default为懒加载

```
<bean id="test1" class="cn.java.ioc1.YelloMouseWolf" lazy-init="default" ></bean>
```

懒加载与非懒加载的优缺点：

懒加载：对象使用的时候才去创建，节省资源，但是不利于提前发现错误。

非懒加载：容器启动的时候立刻创建对象。消耗资源。利于提前发现错误。

当scope=“prototype” (多例)时，默认以懒加载的方式产生对象。因为每创建一个实例就加载一次

当scope=“singleton” (单例)时，默认以非懒加载的方式产生对象。



spring是渐进式的工具、轻量级、模块划分清晰，没有很强的侵入性【就是说即使应用不是web，只想用依赖注入的功能，不引入web相关模块也可以，不会和其它组件有冲突】



ClassPathXmlApplicationContext 兜兜转转很久到ApplicationContext 接口，此外也可以使用

**FileSystemXmlApplicationContext**【基于文件】 和**AnnotationConfigApplicationContext** 【基于注解的方式，不用那么多配置，大势所趋】



```
什么是bean
BeanFactory 是 Bean 容器，那么 Bean 又是什么呢
BeanDefinition就是说的bean 存放bean信息，是否单例，是否懒加载，依赖其它bean等
```



### ClassPathXmlApplicationContext类

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    @Nullable
    private Resource[] configResources;

    public ClassPathXmlApplicationContext() {
    }

    //如果已经有ApplicationContext 并需配置父子关系，那么调用该方法
    public ClassPathXmlApplicationContext(ApplicationContext parent) {
        super(parent);
    }
    
    //一般根据配置文件会比较多
    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation}, true, (ApplicationContext)null);
    }

    public ClassPathXmlApplicationContext(String... configLocations) throws BeansException {
        this(configLocations, true, (ApplicationContext)null);
    }

    public ClassPathXmlApplicationContext(String[] configLocations, @Nullable ApplicationContext parent) throws BeansException {
        this(configLocations, true, parent);
    }

    public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
        this(configLocations, refresh, (ApplicationContext)null);
    }

    public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, @Nullable ApplicationContext parent) throws BeansException {
        super(parent);
        //根据提供路径 处理成配置文件数组【分号、逗号、空格等】
        this.setConfigLocations(configLocations);
        if (refresh) { //一般传true 
            //核心方法 为什么用refresh而不是init是因为refresh会将原来的ApplicationContext销毁重建一次
            this.refresh(); 
        }

    }

    public ClassPathXmlApplicationContext(String path, Class<?> clazz) throws BeansException {
        this(new String[]{path}, clazz);
    }

    public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz) throws BeansException {
        this(paths, clazz, (ApplicationContext)null);
    }

    public ClassPathXmlApplicationContext(String[] paths, Class<?> clazz, @Nullable ApplicationContext parent) throws BeansException {
        super(parent);
        Assert.notNull(paths, "Path array must not be null");
        Assert.notNull(clazz, "Class argument must not be null");
        this.configResources = new Resource[paths.length];

        for(int i = 0; i < paths.length; ++i) {
            this.configResources[i] = new ClassPathResource(paths[i], clazz);
        }

        this.refresh();
    }

    @Nullable
    protected Resource[] getConfigResources() {
        return this.configResources;
    }
}
```

AbstractApplicationContext中的refresh()

```java
public void refresh() throws BeansException, IllegalStateException {
    Object var1 = this.startupShutdownMonitor;
    //refresh这边加锁 不然还没结束就来个启动或销毁容器的操作 会乱掉
    synchronized(this.startupShutdownMonitor) {
        this.prepareRefresh(); //准备工作 记录容器启动时间 标记已启动状态 
        //这步比较重要 这步完成后 配置文件就会解析成一个个bean定义 注册到BeanFactory 这步并没有真正实例化bean，只是提取配置信息放到注册中心 
        ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
        this.prepareBeanFactory(beanFactory);

        try {
            //提供给子类扩展用的
            this.postProcessBeanFactory(beanFactory);
            this.invokeBeanFactoryPostProcessors(beanFactory);
            this.registerBeanPostProcessors(beanFactory);
            this.initMessageSource();
            this.initApplicationEventMulticaster();
            this.onRefresh(); //具体子类可在这初始化一些特殊bean
            this.registerListeners();
            //这边才开始真正初始化bean
            this.finishBeanFactoryInitialization(beanFactory);
            this.finishRefresh();
        } catch (BeansException var9) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Exception encountered during context initialization - cancelling refresh attempt: " + var9);
            }
		   //如果有beansException异常 销毁已经初始化的bean避免一直占用资源
            this.destroyBeans();
            this.cancelRefresh(var9);
            //往外抛
            throw var9;
        } finally {
            this.resetCommonCaches();
        }

    }
}
```



#### obtainFreshBeanFactory加载并注册bean【并未实例化】

```java
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    //关闭旧的bean 创建新的bean、定义加载bean
    this.refreshBeanFactory();
    ConfigurableListableBeanFactory beanFactory = this.getBeanFactory();
    if (this.logger.isDebugEnabled()) {
        this.logger.debug("Bean factory for " + this.getDisplayName() + ": " + beanFactory);
    }

    return beanFactory;
}
```

refreshBeanFactory【在AbstractRefreshableApplicationContext类中】

```java
protected final void refreshBeanFactory() throws BeansException {
    if (this.hasBeanFactory()) {
        //如果已经有 销毁所有bean 并关闭beanFactory
        this.destroyBeans();
        this.closeBeanFactory();
    }

    try {
        //初始化DefaultListableBeanFactory
        DefaultListableBeanFactory beanFactory = this.createBeanFactory();
        //用于BeanFactory的序列化 大多情况用不到
        beanFactory.setSerializationId(this.getId());
        //配置bean的属性 是否允许bean覆盖、允许循环引用
        this.customizeBeanFactory(beanFactory);
        //加载各个bean放到beanFactory 很重要 抽象方法 通过XmlBeanDefinitionReader来
        this.loadBeanDefinitions(beanFactory);
        Object var2 = this.beanFactoryMonitor;
        synchronized(this.beanFactoryMonitor) {
            this.beanFactory = beanFactory;
        }
    } catch (IOException var5) {
        throw new ApplicationContextException("I/O error parsing bean definition source for " + this.getDisplayName(), var5);
    }
}
```



给这个 BeanFactory 实例化一个 XmlBeanDefinitionReader











### Sring启动入口

##### 配置文件的方式

没用springboot前，正常情况都是在web.xml配置一个监听器

```
<listener>  
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>  
</listener>  
```

这里ContextLoaderListener即为spring启动入口，实现了web容器【如tomcat、weblogic】的**ServletContextListener**接口

与其它servlet监听器一样重写两个方法

```
contextInitialized()方法在web容器初始化时执行，contextDestroyed()方法在容器销毁时执行。
web容器启动时会初始化事件
ContextLoaderListener监听到这个事件，其contextInitialized()方法会被调用，在这个方法中【initWebApplicationContext】Spring会初始化一个根上下文，即WebApplicationContext。这是一个接口，其实际默认实现类是XmlWebApplicationContext。这个就是Spring IOC的容器，其对应bean定义的配置信息由web.xml中的context-param
```

```
createWebApplicationContext创建根上下文
```

```java
public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
    //如果servletContext已经存在WebApplicationContext 直接抛异常
    if         (servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE) != null) {
        throw new IllegalStateException("Cannot initialize context because there is already a root application context present - check whether you have multiple ContextLoader* definitions in your web.xml!");
    } else {
        Log logger = LogFactory.getLog(ContextLoader.class);
        servletContext.log("Initializing Spring root WebApplicationContext");
        if (logger.isInfoEnabled()) {
            logger.info("Root WebApplicationContext: initialization started");
        }

        long startTime = System.currentTimeMillis();

        try {
            if (this.context == null) {
                //新建WebApplicationContext 用来创建根上下文
                this.context = this.createWebApplicationContext(servletContext);
            }

            if (this.context instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext)this.context;
                if (!cwac.isActive()) {
                    if (cwac.getParent() == null) {
                        //// 得到根上下文的父上下文，然后设置到根上下文 。一般的web项目parent为空  
                        ApplicationContext parent = this.loadParentContext(servletContext);
                        cwac.setParent(parent);
                    }
				  //从web.xml加载参数 创建bean工厂和bean对象。  
                    this.configureAndRefreshWebApplicationContext(cwac, servletContext);
                }
            }

            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
            ClassLoader ccl = Thread.currentThread().getContextClassLoader();
            if (ccl == ContextLoader.class.getClassLoader()) {
                currentContext = this.context;
            } else if (ccl != null) {
                currentContextPerThread.put(ccl, this.context);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Published root WebApplicationContext as ServletContext attribute with name [" + WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE + "]");
            }

            if (logger.isInfoEnabled()) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                logger.info("Root WebApplicationContext: initialization completed in " + elapsedTime + " ms");
            }

            return this.context;
        } catch (RuntimeException var8) {
            logger.error("Context initialization failed", var8);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, var8);
            throw var8;
        } catch (Error var9) {
            logger.error("Context initialization failed", var9);
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, var9);
            throw var9;
        }
    }
}
```

```java
protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
    Class<?> contextClass = this.determineContextClass(sc);
    if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
        throw new ApplicationContextException("Custom context class [" + contextClass.getName() + "] is not of type [" + ConfigurableWebApplicationContext.class.getName() + "]");
    } else {
        return (ConfigurableWebApplicationContext)BeanUtils.instantiateClass(contextClass);
    }
}
```

### ioc容器几个重要方法

```
refresh()
ClassPathXmlApplicationContext【基于xml构建Application的工具类】、FileSystemApplicationContext还有基于注解的都有使用AnnotationConfigApplicationContext都是基于refresh去创建相应的ApplicationContext
refresh做加锁操作，这个动作主要是用来刷新容器操作，不只是初始化也可以重建销毁旧的容器
1.prepareRefresh 准备工作 校验容器启动时间，标记启动状态 atomicBoolean类型、校验xml

//配置文件将解析成一个个bean定义，注册到beanFctory但还没真正初始化，只是提取了bean的配置信息
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
AbstractRefreshableApplicationContext类中refreshBeanFactory方法中完成，这里完成DefaultListableBeanFactory的创建及初始化
到一个createBeanFactory方法，初始化一个DefaultListableBeanFactory
beanFactory.setSerializationId(getId()); //用于序列化方法

//设置BeanFactory的两个配置属性
customizeBeanFactory(beanFactory);

// 加载 Bean 到 BeanFactory 中
loadBeanDefinitions(beanFactory);


```

```
ApplicationContext继承自BeanFactory，内部持有一个实例化的 BeanFactory（DefaultListableBeanFactory），以后持有BeanFactory的操作都是基于这个实例来处理
```

ConfigurableListableBeanFactory【很重要的接口】只有一个实现类DefaultListableBeanFactory

#### customizeBeanFactory

```
用于配置是否允许BeanDefinition覆盖、是否允许循环引用
bean覆盖:配置文件中定义bean使用相同id或name，默认allowBeanDefinitionOverriding为null
如果在同一配置文件重复会报错、不同会覆盖

循环引用:A依赖B，B依赖A，B依赖C，而C依赖A等等
默认允许循环
```

```java
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
   if (this.allowBeanDefinitionOverriding != null) {
      // 是否允许 Bean 定义覆盖
      beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
   }
   if (this.allowCircularReferences != null) {
      // 是否允许 Bean 间的循环依赖
      beanFactory.setAllowCircularReferences(this.allowCircularReferences);
   }
}
```

#### loadBeanDefinitions

加载bean然后放到BeanFactory

```java
/** 我们可以看到，此方法将通过一个 XmlBeanDefinitionReader 实例来加载各个 Bean。*/
@Override
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
   // 给这个 BeanFactory 实例化一个 XmlBeanDefinitionReader
   XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

   // Configure the bean definition reader with this context's
   // resource loading environment.
   beanDefinitionReader.setEnvironment(this.getEnvironment());
   beanDefinitionReader.setResourceLoader(this);
   beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

   // 初始化 BeanDefinitionReader，其实这个是提供给子类覆写的，
   // 我看了一下，没有类覆写这个方法，我们姑且当做不重要吧
   initBeanDefinitionReader(beanDefinitionReader);
   // 重点来了，继续往下
   loadBeanDefinitions(beanDefinitionReader);
}

//生成的beanDefinitionReader会开始用来加载xml配置
//loadBeanDefinitions(beanDefinitionReader);
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
   Resource[] configResources = getConfigResources();
   if (configResources != null) {
      // 往下看
      reader.loadBeanDefinitions(configResources);
   }
   String[] configLocations = getConfigLocations();
   if (configLocations != null) {
      // 2
      reader.loadBeanDefinitions(configLocations);
   }
}

// 上面虽然有两个分支，不过第二个分支很快通过解析路径转换为 Resource 以后也会进到这里
@Override
public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
   Assert.notNull(resources, "Resource array must not be null");
   int counter = 0;
   // 注意这里是个 for 循环，也就是每个文件是一个 resource
   for (Resource resource : resources) {
      // 继续往下看
      counter += loadBeanDefinitions(resource);
   }
   // 最后返回 counter，表示总共加载了多少的 BeanDefinition
   return counter;
}

// XmlBeanDefinitionReader 303
@Override
public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
   return loadBeanDefinitions(new EncodedResource(resource));
}

// XmlBeanDefinitionReader 314
public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
   Assert.notNull(encodedResource, "EncodedResource must not be null");
   if (logger.isInfoEnabled()) {
      logger.info("Loading XML bean definitions from " + encodedResource.getResource());
   }
   // 用一个 ThreadLocal 来存放配置文件资源
   Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
   if (currentResources == null) {
      currentResources = new HashSet<EncodedResource>(4);
      this.resourcesCurrentlyBeingLoaded.set(currentResources);
   }
   if (!currentResources.add(encodedResource)) {
      throw new BeanDefinitionStoreException(
            "Detected cyclic loading of " + encodedResource + " - check your import definitions!");
   }
   try {
      InputStream inputStream = encodedResource.getResource().getInputStream();
      try {
         InputSource inputSource = new InputSource(inputStream);
         if (encodedResource.getEncoding() != null) {
            inputSource.setEncoding(encodedResource.getEncoding());
         }
         // 核心部分是这里，往下面看
         return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
      }
      finally {
         inputStream.close();
      }
   }
   catch (IOException ex) {
      throw new BeanDefinitionStoreException(
            "IOException parsing XML document from " + encodedResource.getResource(), ex);
   }
   finally {
      currentResources.remove(encodedResource);
      if (currentResources.isEmpty()) {
         this.resourcesCurrentlyBeingLoaded.remove();
      }
   }
}

// 还在这个文件中，第 388 行
protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
      throws BeanDefinitionStoreException {
   try {
      // 这里就不看了，将 xml 文件转换为 Document 对象
      Document doc = doLoadDocument(inputSource, resource);
      // 继续
      return registerBeanDefinitions(doc, resource);
   }
   catch (...
}
// 返回值：返回从当前配置文件加载了多少数量的 Bean
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
   BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
   int countBefore = getRegistry().getBeanDefinitionCount();
   // 这里
   documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
   return getRegistry().getBeanDefinitionCount() - countBefore;
}
// DefaultBeanDefinitionDocumentReader 90
@Override
public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
   this.readerContext = readerContext;
   logger.debug("Loading bean definitions");
   Element root = doc.getDocumentElement();
   // 从 xml 根节点开始解析文件
   doRegisterBeanDefinitions(root);
}         
```

经过漫长的路径、一个配置文件转为一颗Dom树【n个有n个】

```java
// DefaultBeanDefinitionDocumentReader 116
protected void doRegisterBeanDefinitions(Element root) {
   // 我们看名字就知道，BeanDefinitionParserDelegate 必定是一个重要的类，它负责解析 Bean 定义，
   // 这里为什么要定义一个 parent? 看到后面就知道了，是递归问题，
   // 因为 <beans /> 内部是可以定义 <beans /> 的，所以这个方法的 root 其实不一定就是 xml 的根节点，也可以是嵌套在里面的 <beans /> 节点，从源码分析的角度，我们当做根节点就好了
   BeanDefinitionParserDelegate parent = this.delegate;
   this.delegate = createDelegate(getReaderContext(), root, parent);

   if (this.delegate.isDefaultNamespace(root)) {
      // 这块说的是根节点 <beans ... profile="dev" /> 中的 profile 是否是当前环境需要的，
      // 如果当前环境配置的 profile 不包含此 profile，那就直接 return 了，不对此 <beans /> 解析
      // 不熟悉 profile 为何物，不熟悉怎么配置 profile 读者的请移步附录区
      String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
      if (StringUtils.hasText(profileSpec)) {
         String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
               profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
         if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
            if (logger.isInfoEnabled()) {
               logger.info("Skipped XML bean definition file due to specified profiles [" + profileSpec +
                     "] not matching: " + getReaderContext().getResource());
            }
            return;
         }
      }
   }

   preProcessXml(root); // 钩子
   // 往下看
   parseBeanDefinitions(root, this.delegate);
   postProcessXml(root); // 钩子

   this.delegate = parent;
}
```

#### parseBeanDefinitionElement 创建BeanDefinition实例

```java
public AbstractBeanDefinition parseBeanDefinitionElement(
      Element ele, String beanName, BeanDefinition containingBean) {

   this.parseState.push(new BeanEntry(beanName));

   String className = null;
   if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
      className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
   }

   try {
      String parent = null;
      if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
         parent = ele.getAttribute(PARENT_ATTRIBUTE);
      }
      // 创建 BeanDefinition，然后设置类信息而已，很简单，就不贴代码了
      AbstractBeanDefinition bd = createBeanDefinition(className, parent);

      // 设置 BeanDefinition 的一堆属性，这些属性定义在 AbstractBeanDefinition 中
      parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
      bd.setDescription(DomUtils.getChildElementValueByTagName(ele, DESCRIPTION_ELEMENT));

      /**
       * 下面的一堆是解析 <bean>......</bean> 内部的子元素，
       * 解析出来以后的信息都放到 bd 的属性中
       */

      // 解析 <meta />
      parseMetaElements(ele, bd);
      // 解析 <lookup-method />
      parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
      // 解析 <replaced-method />
      parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
    // 解析 <constructor-arg />
      parseConstructorArgElements(ele, bd);
      // 解析 <property />
      parsePropertyElements(ele, bd);
      // 解析 <qualifier />
      parseQualifierElements(ele, bd);

      bd.setResource(this.readerContext.getResource());
      bd.setSource(extractSource(ele));

      return bd;
   }
   catch (ClassNotFoundException ex) {
      error("Bean class [" + className + "] not found", ele, ex);
   }
   catch (NoClassDefFoundError err) {
      error("Class that bean class [" + className + "] depends on not found", ele, err);
   }
   catch (Throwable ex) {
      error("Unexpected failure during bean definition parsing", ele, ex);
   }
   finally {
      this.parseState.pop();
   }

   return null;
}
```



创建一个BeanDefinitionHolder实例



```
parseBeanDefinitionElement用于解析<bean></bean>的子元素
```



注册bean

```
registerBeanDefinition
```



## 什么是bean

```
bean在代码层面上可以认为是 BeanDefinition 的实例
BeanDefinition是一个接口保存bean信息，比如这个bean指向哪个类、是否单例、是否懒加载等等、工厂名称等等
```

## bean的生命周期





## getBean

```java
//通过继承一个方法重写
public class NoBeanOverridingContextLoader extends ContextLoader {

  @Override
  protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext applicationContext) {
    super.customizeContext(servletContext, applicationContext);
    AbstractRefreshableApplicationContext arac = (AbstractRefreshableApplicationContext) applicationContext;
    arac.setAllowBeanDefinitionOverriding(false);
  }
}
```

生成bean的方式用静态工厂和实例工厂



销毁bean的回调

### 销毁 Bean 的回调

```
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
```

```java
public class AnotherExampleBean implements DisposableBean {

    public void destroy() {
        // do some destruction work (like releasing pooled connections)
    }
}
@Bean(destroyMethod = "cleanup")
public Bar bar() {
    return new Bar();
}
@PreDestroy
public void cleanup() {

}
```



### 基于代码方式替代web.xml

spring3.0后开始推荐用注解取代web.xml配置文件

为了实现这一目的，Spring提供了对应的类。【springboot用这个】

- WebApplicationInitializer
- SpringServletContainerInitializer

```java
@ImportResource({"classpath:META-INF/cxf/cxf.xml"})
public class MyWebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ServletRegistration.Dynamic registration = servletContext.addServlet("cxfServlet", CXFServlet.class);
        registration.setLoadOnStartup(1);
        registration.addMapping("/iodServices/*");
        Properties props = System.getProperties();
        props.setProperty("org.apache.cxf.stax.allowInsecureParser", "1");
    }
}
```

```
WebApplicationInitializer的用处
一种替代web.xml的方式 在启用web项目时会加载这个类的实现类 执行onStartup方法从而实现和web.xml相同的作用，如添加servlet、listener、filter等【ServletContext对象的addServlet、addFilter、addListner方法】
```

org\springframework\spring-web\5.0.4.RELEASE\spring-web-5.0.4.RELEASE.jar!\META-INF\services\javax.servlet.ServletContainerInitializer 下需声明

```
org.springframework.web.SpringServletContainerInitializer
```



##### 为何容器启动后会调用接口的on startup方法，主要是因为和WebApplicationInitializer同级的SpringServletContainerInitializer

```java
https://www.javadoop.com/post/spring-properties
```

ServletContainerInitializer 是位于javax.servlet包下的接口，容器启动之后会调用该类的onStartup方法。META-INF/services目录下声明一个文件，文件的名字是这个接口的完全限定类名称，并将被运行时的服务提供者查找机制或者被容器中特定的类似机制发现。【有点像是委托模式】

```java
package javax.servlet;

import java.util.Set;
//继承这个接口的类必须在jar文件中的
public interface ServletContainerInitializer {
    void onStartup(Set<Class<?>> var1, ServletContext var2) throws ServletException;
}
```

在org\springframework\spring-web\5.0.4.RELEASE\spring-web-5.0.4.RELEASE.jar!\META-INF\services\javax.servlet.ServletContainerInitializer下，spring源码帮我们做了这件事，里面内容如下，即SpringServletContainerInitializer

```
org.springframework.web.SpringServletContainerInitializer
```

springboot中就有些类继承WebApplicationInitializer达到它一些初始化的目的。

SpringBootServletInitializer





## Bean的生命周期



















## 代理模式相关

#### 为什么需要代理模式

1.为什么需要代理模式

```
代理模式的作用:为其它对象提供一种代理控制对这个对象的访问【或不想暴露出来】，
```

有点像中介

程序设计时，有时候客户端无法直接操作实际对象，而代理对象可以在客户端和目标对象之间气起到中介作用



####  静态代理的缺点

不能处理未知数据类型【因为是运行前编译好的，而动态代理是运行后通过字节码技术生成代理类】，还需要手动写代理类，代码还得重新测试，会生成大量类。



#### jdk动态代理原理

aop的一种实现方式，在目标对象的方法执行前后进行了增强，利用了了Proxy和InvocationHandler

因为需要拿到目标类实现的接口，根据代理类字节码生成代理类的实例

java.lang.reflect.Proxy
java.lang.reflect.InvocationHandler
java.lang.reflect.WeakCache
sun.misc.ProxyGenerator

限定代理接口不能超过65535个

1、代理类继承了Proxy类并且实现了要代理的接口，由于java不支持多继承，所以JDK动态代理不能代理类

2、重写了equals、hashCode、toString

3、有一个静态代码块，通过反射或者代理类的所有方法

4、通过invoke执行代理类中的目标方法doSomething

```
java.lang.reflect.Proxy: 这是生成代理类的主类，通过 Proxy 类生成的代理类都继承了 Proxy 类，即 DynamicProxyClass extends Proxy。

java.lang.reflect.InvocationHandler:这里称他为"调用处理器"，他是一个接口，我们动态生成的代理类需要完成的具体内容需要自己定义一个类，而这个类必须实现 InvocationHandler 接口。
```

生成的代理类…$Proxy会继承Proxy并实现传入的接口

jdk动态代理是根据定义好的规则，用传入的接口创建一个新类

创建代理类实现InvocationHandler接口

覆盖InvocationHandler对invoke方法织入增强操作

```
为什么一定要实现接口
因为java单继承，动态代理类已经继承Proxy就不能再继承其它类，靠背代理类的接口来
实现接口重写方法 里面再调用invoke方法
invoke方法中再利用jdk反射的方式去调用真正的被代理类的业务方法
```

##### cglib怎么增强类

采用asm字节码技术，Enhancer是CGLIB的字节码增强器，调用*MethodInterceptor*接口中的*intercept*方法

底层将方法全部存入一个数组中，通过数组索引直接进行方法调用【它会判断是否存在实现了MethodInterceptor接口的对象，若存在则将调用intercept方法对委托方法进行代理】



##### 为什么cglib无法完全替代jdk动态代理

1.final class不支持 通过继承

2.强制无参构造函数



## AOP

Aspect-Oriented Programming【aop】、面向切面，和OOP( Object-Oriented Programming, 面向对象编程) 

https://www.cnblogs.com/xys1228/p/6057587.html

关注点分离:不同的问题交给不同部分去解决，可能异常、缓存等要和业务功能挂钩【通用化功能代码的实现其实就是所谓的切面，业务代码和切面代码分开后，架构将变得高内聚低耦合】

aop要实现的就是在原有代码基础上，进行一定包装、如方法执行前、方法返回后、方法抛出异常后等进行一定拦截

spring3.2后spring-core组件中自带cglib和asm包，因此不需要再显示引入

spring aop依赖ioc容器来管理，只能作用于spring容器中的bean

##### aop和aspectj的关系

AspectJ静态织人，它是aop编程的解决方案，但是aop致力于企业开发中的aop需求【方法织入】

AspectJ可以做很多spring aop做不到的事



aop的实现是基于动态代理来实现的，如果要代理的目标类实现接口，使用jdk动态代理，若没有使用cglib

##### aop三种织入方式

```
1.编译时织入 如aspectJ
2.类加载时织入 如aspectJ 或者使用命令[-javaagent:xxx/xxx/aspectjweaver.jar]
3.编译后运行时织入，已经生成class文件或打成jar报，spring采用这种方式
```



##### 几个重要概念





##### join point和point cut



### AOP源码实现