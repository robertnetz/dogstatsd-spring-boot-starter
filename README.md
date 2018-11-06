dogstatsd-spring-boot-starter
=============================

[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](https://jez.io/MIT-LICENSE.txt)
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/https/oss.sonatype.org/content/repositories/releases/com/github/robertnetz/dogstatsd-spring-boot-starter/maven-metadata.xml.svg)](https://oss.sonatype.org/content/repositories/releases/com/github/robertnetz/dogstatsd-spring-boot-starter/)
[![Build Status](https://travis-ci.org/robertnetz/dogstatsd-spring-boot-starter.svg?branch=develop)](https://travis-ci.org/robertnetz/dogstatsd-spring-boot-starter)

this project will easily push your metrics to a [dogstatsd-agent](https://docs.datadoghq.com/developers/dogstatsd/ "Datadogs Website about the Dogstatsd-Agent")
in a [spring-boot environment](https://projects.spring.io/spring-boot/ "Spring Boot Homepage"). For now, this project will only support Spring-Boot 1.5.x, since
in 2.0.x, spring will introduce [Micrometer](https://micrometer.io/ "Micrometer Homepage") as an abstraction for pushing metrics to various vendors.

Downloads
---------
Add `dogstatsd-spring-boot-starter` as dependency.

If you use Maven, add the starter to your dependencies:
```xml
<dependency>
    <groupId>com.github.robertnetz</groupId>
    <artifactId>dogstatsd-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

If you use Gradle, add your dependency as follows:
```gradle
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.github.robertnetz:dogstatsd-spring-boot-starter:1.0.1'
}
```

Usage
-----
As soon as the starter was added to your project, it will send your actuator metrics
to your dogstatsd-agent. by default it will send to `localhost:8125`.

you can update the configuration by adding new keys to your `application.yml` as follows.
```yml
datadog:
  host: otherhost.example.com
  port: 12345
```

a feature of the dogstatsd is the tagging-support. to tag all your metrics statically
with a provided set of tags, you have to specify them like this:
```yml
datadog:
  tags:
    - foo
    - bar
    - bam
    - baz
```

if you like to have a static prefix to all your metrics, you can do it like that:
```yml
datadog:
  prefix: superapp
```

you also can disable the metrics export by configuration:
```yml
datadog:
  enabled: false
```

You might not want to export spring actuators' [SystemPublicMetrics](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/actuate/endpoint/SystemPublicMetrics.html), then you'll have
to disable them explicitly by:
```yml
datadog:
  includeActuatorMetrics: false
```

### Metric name sanitization

Some libraries you use might be recording metrics with `:` in their names. 
These are currently not supported by the `java-dogstatsd-client` and cause errors. 
There are a few strategies available:

``` yml
datadog:
  nameSanitizer: raise  # default
``` 

The default strategy is `raise`, and will raise an exception when a `:` character is encountered in a metric name. 
This will allow you to quickly fix any code under your control which reported these metrics.   

Other available strategies are:
* `escape`: replace any `:` with a `-`
* `ignore`: silently skip reporting the metric with the illegal name
