**<p align="center"><ins>Problem Statement</ins></p>**

**when `example-project` is opened in intellij,   
it doesn't show `commons-lang3` in classpath.**

<ins>**Expectation :-**</ins>

when ever `example-project` is opened in intellij,   
it should have `commons-lang3` in classpath.

</details>


---

**<p align="center"><ins>Steps to reproduce problem</ins></p>**


<details><summary>1. install maven plugin on local</summary>

```
cd download-dependencies-maven-plugin
mvn install
```

This will deploy `download-dependencies-maven-plugin` plugin on local machine.   
After deployment, this plugin can be used in `example-project` maven project.   

</details>

<br/>


<details><summary>2. use installed plugin</summary>

```
cd example-project
mvn clean compile
```

It will execute `DownloadDependencyMojo execute method` from `download-dependencies-maven-plugin` project
and downloads `commons-lang3` that is configured in `example-project` `pom.xml` :-

```
<groupId>org.apache.commons</groupId>
<artifactId>commons-lang3</artifactId>
<version>3.12.0</version>
```

</details>

<br/>


---

<details><summary>maven version</summary>

```
❯ mvn --version

Apache Maven 3.9.0 (9b58d2bad23a66be161c4664ef21ce219c2c8584)
Maven home: /Users/mogli/.sdkman/candidates/maven/current
Java version: 17.0.6, vendor: Eclipse Adoptium, runtime: /Users/mogli/.sdkman/candidates/java/17.0.6-tem
Default locale: en_IN, platform encoding: UTF-8
OS name: "mac os x", version: "12.6.1", arch: "x86_64", family: "mac"
```

</details>
