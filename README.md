# Service Proxy [![Travis build status](https://travis-ci.org/koushikr/qtrouper.svg?branch=master)](https://travis-ci.org/koushikr/dropwizard-serviceproxy)

A dropwizard bundle to act like a proxy in front of any http web server.

> It is not down on any map; true places never are.
> - Moby- Dick, Herman Melville


### Maven Dependency
Use the following repository:
```xml
<repository>
    <id>clojars</id>
    <name>Clojars repository</name>
    <url>https://clojars.org/repo</url>
</repository>
```
Use the following maven dependency:
```xml
<dependency>
    <groupId>io.github.dropwizard</groupId>
    <artifactId>serviceproxy</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Build instructions
  - Clone the source:

        git clone github.com/koushikr/dropwizard-serviceproxy

  - Build

        mvn install

### Tech

* [Dropwizard](https://github.com/dropwizard/dropwizard) - The bundle that got created
* [OkhttpClient](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/) - Http client that just works.
* [Hystrix](https://github.com/Netflix/Hystrix) - For fault tolerance

### Example

## Sample Configuration

```
static class SampleConfiguration extends Configuration{

        private ProxyConfiguration proxyConfiguration;

    }
}

```

## Bundle Inclusion

```
      ProxyBundle<SampleConfiguration> proxyBundle = new ProxyBundle<SampleConfiguration>() {

                @Override
                public ProxyConfiguration getProxy(SampleConfiguration configuration) {
                    return configuration.getProxyConfiguration();
                }
      };

      bootstrap.addBundle(trouperBundle);

```

LICENSE
-------

Copyright 2020 Koushik R <rkoushik.14@gmail.com>.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


  