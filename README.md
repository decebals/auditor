Auditing micro framework for Java
=====================
[![Travis CI Build Status](https://travis-ci.org/decebals/auditor.png)](https://travis-ci.org/decebals/auditor)
[![Maven Central](http://img.shields.io/maven-central/v/ro.fortsoft.auditor/auditor.svg)](http://search.maven.org/#search|ga|1|auditor)

The goal of this project is to create a tiny auditing micro framework that could be very easy to understand and hack.
 
Features/Benefits
-------------------
With Auditor you can easily create audit information (events) for your application.  
Auditor is an open source (Apache license) lightweight auditing framework for Java, with no dependencies and very extensible.   
I recommend you to use this library in small and medium applications. 

If you need something more powerful, please take a look at the excellent [Audit4J](http://audit4j.org) library.

Why I created Auditor and I did't use Audit4J?  
First of all, Audit4J is excellent but it's a little heavy (with maybe to many dependencies) for my taste.
In my projects I already use Log4J for auditing, so my idea is to use the Log4J ecosystem for auditing.   
So, I created an Log4J based auditor implementation (`Log4jAuditor` class), and I improved the Log4J's pattern layout (`AuditPatternLayout` class)
with an implementation capable of capturing audit event details.  
I don't want to reinvent things (file rolling, layout patterns, different kinds of storage, ...) so I will let the Log4J's ecosystem to work for me.   
Log4J comes with a huge number of appender implementations (async, database, ...), so anytime I can choose the implementation that I need.
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

You may want to check for the latest released version using [Maven Search](http://search.maven.org/#search%7Cga%7C1%7Cauditor)

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

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
