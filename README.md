Auditing micro framework for Java
=====================
[![Travis CI Build Status](https://travis-ci.org/decebals/auditor.png)](https://travis-ci.org/decebals/auditor)
[![Maven Central](http://img.shields.io/maven-central/v/ro.fortsoft.auditor/auditor-parent.svg)](http://search.maven.org/#search|ga|1|ro.fortsoft.auditor)

The goal of this project is to create a tiny auditing micro framework that could be very easy to understand and hack.
 
Features/Benefits
-------------------
With Auditor you can easily create audit information (events) for your application.  
Auditor is an open source (Apache license) lightweight auditing framework for Java, with no dependencies (in `core` module) and very extensible.   
I recommend you to use this library in small and medium applications. 

If you need something more powerful, please take a look at the excellent [Audit4J](http://audit4j.org) library.

Why I created Auditor and I did't use Audit4J?  
First of all, Audit4J is excellent but it's a little heavy (with maybe to many dependencies) for my taste.
In a [comment](https://github.com/audit4j/audit4j-core/issues/62), the man behind Audit4J says:
> From the initial stage, audit4j is designed for the enterprise applications and we haven't designed audit4j for embedded systems. 
Initially we used ActiveMQ to handle audit events asynchronously but It's not the suitable mechanism when we consider the throughput since we had the requirement to process 25000 TP. After several considerations and evaluations we redesigned the Audit4j and used reactor for asynchronous processing.
Currently audit4j is built around reactor and various streams.
  
In my projects I already use Log4J for auditing, so my idea is to use the Log4J ecosystem for auditing.   
So, I created an Log4J based auditor implementation (`Log4jAuditor` class), and I improved the Log4J's pattern layout (`AuditPatternLayout` class)
with an implementation capable of capturing audit event details.  
I don't want to reinvent things (file rolling, layout patterns, different kind of storages, ...) so I will let the Log4J's ecosystem to work for me.   
Log4J comes with a huge number of appender implementations (async, database, ...), so anytime I can choose the implementation that I need it.
In conclusion the performance of Auditor is dictated by the performance of the Log4J's appender implementation.    

For people that don't want to use Log4J based auditor implementation and want something small, 
I will supply some trivial auditor implementation like `ConsoleAuditor` and `FileAuditor`.  

Components
-------------------
- **Auditor** represents the entry point. The auditing implementation may use any appropriate medium and format to store audit events.
- **AuditEvent** provides information about an auditing event.
- **AuditEventFormatter** represents the formatter interface for audit event. It transforms an audit event to a String. 

Artifacts
-------------------
- Auditor Core `auditor-core` (jar)
- Auditor Log4J `auditor-log4j` (jar)

Using Maven
-------------------
In your pom.xml you must define the dependencies to Auditor artifacts with:

```xml
<dependency>
    <groupId>ro.fortsoft.auditor</groupId>
    <artifactId>auditor-core</artifactId>
    <version>${auditor.version}</version>
</dependency>    
```

where ${auditor.version} is the last Auditor version.

You may want to check for the latest released version using [Maven Search](http://search.maven.org/#search%7Cga%7C1%7Cro.fortsoft.auditor).

Also you can use the latest SNAPSHOT via the Sonatype Maven Repository. For this, you must add above lines in your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

How to use
-------------------
It's very simple to add Auditor in your application:

```java
public class MyBusinessClass {


    private Auditor auditor = new Log4jAuditor("audit"); // or inject  
    
    public void bussinesMethod() {
        // business
        ...
        
        // add audit event
        auditor.audit(new AuditEvent("Login").setUsername("decebal1").setSession(getUUID()).setIp("localhost"));
    }

}
```

Internally `Log4jAuditor`, when receive a `AuditEvent`, transforms the audit event in a Log4j's [LoggingEvent](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/spi/LoggingEvent.html)
and append the logging event to the all appenders assigned to audit logger.
Before passing the logging event to appenders, `Log4jAuditor` tries to extract information like `username`, `session` and `ip` from [MDC](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/MDC.html) if this information is missing from the audit event.
So, you can put this information somewhere in your code (for example a servlet filter):
```java
MDC.put("username", username);
MDC.put("session", sessionId);
MDC.put("ip", hostname);
```

and create the audit event with a more short line:
```java
auditor.audit(new AuditEvent("Login"));
```
 
Sure, you can use any out of the box Auditor implementation or create your custom Auditor implementation, but if you use Log4J in your project for logging (like me)
I suggest you to use `Log4jAuditor` (from `auditor-log4j`, si don't forget to add it to your project as dependency).  
  
I will give you addition information about how to use `Log4jAuditor` in your project.  
First of all add a [log4j.properties](https://github.com/decebals/auditor/blob/master/auditor-log4j/src/test/resources/log4j.properties) file to your project.  

To use auditor you should add (in your log4j.properties) the logger:
```
#
# Loggers
#
log4j.logger.audit=INFO, file
``` 

and the appender:
```
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./logs/audit.log
log4j.appender.file.MaxFileSize=1MB
log4j.appender.file.MaxBackupIndex=10
log4j.appender.file.layout=ro.fortsoft.auditor.log4j.AuditPatternLayout
log4j.appender.file.layout.ConversionPattern=[%d{MM/dd/yyyy HH:mm:ss}] %-5p %U %S %H %m%n
```
 
In appender is useful to add `AuditPatternLayout` pattern layout (penultimate line).
Available patterns:
- [%U] - username
- [%S] - session
- [%H] - host name/address  
 
Now if you run your application, in `./logs/audit.log` you will see something like:
```java
[12/24/2017 02:00:06] INFO decebal1 b844d5e4-6fc0-4ec7-b402-8e7668e5a2b3 localhost Login
[12/24/2017 02:00:06] INFO decebal2 25925950-ecab-41dd-ac8f-d7fcf6099263 127.0.0.1 Login
```
 
For the patterns added by `AuditPatternLayout` see the [javadoc](https://github.com/decebals/auditor/blob/master/auditor-log4j/src/main/java/ro/fortsoft/auditor/log4j/AuditPatternLayout.java#L25) of class.
 
Versioning
------------
Auditor will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

`<major>.<minor>.<patch>`

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
--------------
    Copyright 2017 Decebal Suiu

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
