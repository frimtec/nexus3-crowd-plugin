# Nexus3 Crowd Plugin  [![setup automated][gitpod-shield]][gitpod]

[![License][license-shield]][license]
![Project Maintenance][maintenance-shield]
[![Code Coverage][codecov-shield]][codecov]

[![Build Status][build-status-shield]][build-status]
[![Deploy Status][deploy-status-shield]][deploy-status]

This plugin adds a Crowd realm to Sonatype Nexus OSS and enables you to authenticate with Crowd Users and authorize with crowd roles.

It works with Nexus 3.x and Crowd 2.x and 3.x.

This is a fork of great repo https://github.com/martinspielmann/nexus3-crowd-plugin.
The fork adds:
* An easy development environment based on Gitpod with a running nexus and crowd server.
* Release versions follow the schema ```<nexus-version>.<plugin-build>``` (e.g. ```3.49.0.1``` is the most recent version compatible with nexus ```3.49.0```).
* Automated dependency updates with dependabot to keep plugin compatible with the newest nexus versions.

##### Directory naming convention:
When Nexus gets downloaded and unzipped, there are typically two directories created:
* nexus-3.23.0-03
* sonatype-work/nexus3

To avoid confusion, the conventions of the Sonatype reference will be used in the following descriptions:
* nexus-3.23.0-03 will be referred to as **$install-dir**
* sonatype-work/nexus3 will be referred to as **$data-dir**

See [https://books.sonatype.com/nexus-book/reference3/install.html#directories](https://books.sonatype.com/nexus-book/reference3/install.html#directories) for reference.

## Installation

Follow the three steps.
More information on Sonatype website: https://help.sonatype.com/repomanager3/bundle-development/installing-bundles.

#### Prerequisites
* JDK 8 is installed
* Sonatype Nexus OSS >= 3.49.0 is installed 

#### 1. Download matching release kar file into nexus **$install-dir**/deploy folder 

Releases can be found here: https://github.com/frimtec/nexus3-crowd-plugin/releases

#### 2. Create crowd.properties

Create a *crowd.properties* file in **$install-dir**/etc<br/>
The file has to contain the following properties:
```
crowd.server.url=http://localhost:8095/crowd (replace by your crowd url)
application.name=nexus (replace by your nexus application name configured in crowd)
application.password=nexus (replace by your nexus application password configured in crowd)
cache.authentication=false (should authentication be cached? default is false)

# optional:
timeout.connect=15000 (default is 15000)
timeout.socket=15000 (default is 15000)
timeout.connectionrequest=15000 (default is 15000)
```

#### 3. Restart Nexus Repo

## Usage
#### 1. Activate Plugin
After installation you have to activate the plugin in the administration frontend.
You have to login with an administrative nexus account to do so. The default admin credentials are
* username: *admin*
* password: *admin123* (don't forget to change it!)

After login you can navigate to the realm administration.
Activate the plugin by dragging it to the right hand side:
<img style="border: 1px solid grey;" src='https://martinspielmann.de/pseudorandombullshitgenerator/wp-content/uploads/2018/01/nexus_crowd.png'>
#### 2. Map Crowd Groups to Nexus Roles
As a last step you have to map your crowd groups to nexus internal roles.
<img style="border: 1px solid grey;" src='https://martinspielmann.de/pseudorandombullshitgenerator/wp-content/uploads/2018/01/nexus-5.png'>
A good starting point is mapping one crowd group to *nx-admin* role, so you can start managing Nexus with your Crowd Login.
* Choose a crowd group
* Think up a new unique name for the mapped role
* Add *nx-admin* to the contained roles
<img style="border: 1px solid grey;" src='https://martinspielmann.de/pseudorandombullshitgenerator/wp-content/uploads/2018/01/nexus-6.png'>

That's it. You should no be able to logout and login with your Crowd user (provided that your Crowd user is in one of you previously mapped groups).

**Remark:** Caching can improve authentication performance significantly 
by moving credential validation into memory instead of requesting it from 
the crowd server every time.
However if cache.authentication is set to true, 
a hashed version of user credentials will be cached. 
This might be a security risk and is also the reason why this property defaults to false.

## Development

#### 1.Build the plugin
Build and install the into your local maven repository using the following commands:
```
git clone https://github.com/martinspielmann/nexus3-crowd-plugin.git
cd nexus3-crowd-plugin
mvn install
```

In order to build the bundle (kar)
```
mvn clean package -PbuildKar
```

#### 2. Start nexus with console
Move into your **$install-dir**. Edit the file bin/nexus.vmoptions to contain the following line
```
-Dkaraf.startLocalConsole=true
```
After that (re-)start nexus. It will then startup with an interactive console enabled. (If the console doesn't show up, you may hit the Enter key after startup).
Your console should look like this afterwards:
```
karaf@root()> 
```
  
#### 3. Install plugin bundle
  Within the console just type
  ```
  bundle:install -s file://ABSOLUTE_PATH_TO_YOUR_JAR
  ```

## Contributing
[![GitHub contributors](https://img.shields.io/github/contributors/frimtec/nexus3-crowd-plugin.svg)](https://github.com/frimtec/nexus3-crowd-plugin/graphs/contributors)

Thanks to all contributors who helped to get this up and running.

[gitpod-shield]: https://img.shields.io/badge/Gitpod-ready_to_code-orange?logo=gitpod
[gitpod]: https://gitpod.io/from-referrer/
[maintenance-shield]: https://img.shields.io/maintenance/yes/2023.svg
[license-shield]: https://img.shields.io/github/license/frimtec/nexus3-crowd-plugin.svg
[license]: https://opensource.org/licenses/Apache-2.0
[build-status-shield]: https://github.com/frimtec/nexus3-crowd-plugin/workflows/Build/badge.svg
[build-status]: https://github.com/frimtec/nexus3-crowd-plugin/actions?query=workflow%3ABuild
[deploy-status-shield]: https://github.com/frimtec/nexus3-crowd-plugin/workflows/Deploy%20release/badge.svg
[deploy-status]: https://github.com/frimtec/nexus3-crowd-plugin/actions?query=workflow%3A%22Deploy+release%22
[codecov-shield]: https://codecov.io/gh/frimtec/nexus3-crowd-plugin/branch/master-frimtec/graph/badge.svg?token=3LRAWVA25O
[codecov]: https://codecov.io/gh/frimtec/nexus3-crowd-plugin
