dogstatsd-spring-boot-starter
=============================

[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](https://jez.io/MIT-LICENSE.txt)
[![Maven metadata URI](https://img.shields.io/maven-metadata/v/https/oss.sonatype.org/content/repositories/releases/com/github/robertnetz/dogstatsd-spring-boot-starter/maven-metadata.xml.svg)](https://oss.sonatype.org/content/repositories/releases/com/github/robertnetz/dogstatsd-spring-boot-starter/)
[![Build Status](https://travis-ci.org/robertnetz/dogstatsd-spring-boot-starter.svg?branch=develop)](https://travis-ci.org/robertnetz/dogstatsd-spring-boot-starter)

this project will easily push your actuator metrics to a [dogstatsd-agent](https://docs.datadoghq.com/developers/dogstatsd/ "Datadogs Website about the Dogstatsd-Agent")
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
    <version>x.x.x</version>
</dependency>
```

If you use Gradle, add your dependency as follows:
```gradle
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.github.robertnetz:dogstatsd-spring-boot-starter:x.x.x'
}
```

Usage
-----
tbd.
