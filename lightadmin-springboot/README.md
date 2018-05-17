LightAdmin and Spring Boot integration example

This application is a usual spring-boot application, where lightadmin was added.
Applying this to your own, existent project/applications means:

* add lightadmin, tomcat and jasper to your pom
* add some lightadmin init code in (or around) your main code

It is really trivial.

Note: the ideal (and spring-boot like) solution would be to simply add a
`spring-boot-starter-lightadmin` to your POM. We are aware of this, but it's not
yet implemented.

当你搞不定的时候 请下载源码
https://github.com/la-team/light-admin.git
https://github.com/la-team/light-logging-configurer.git
最新版的 spring包会不同 选择相同版本或者引入包过滤重复的jar