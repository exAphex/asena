
![Tests](https://github.com/exAphex/asena/workflows/Tests/badge.svg) [![codecov](https://codecov.io/gh/exAphex/asena/branch/master/graph/badge.svg?token=P1IZLIO13A)](https://codecov.io/gh/exAphex/asena) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
# Asena SCIM Gateway
Asena is a SCIM Gateway to systems which do not have native SCIM interfaces. It can be used as a middleware for identity management systems to provision users and group assignments. Providing a UI for administration it is easy to manage and is already used in productive environments.

## Overview
![image](https://raw.githubusercontent.com/exAphex/asena/master/assets/architecture.png)

## Contents
- [Functionality](#functionality)
- [Prerequisites](#prerequisites)
- [Compatibility](#compatibility)
- [ToDo](#todo)
- [Installation](#installation)
- [Connect a remote system](#connect-a-remote-system)
- [Scripting](#scripting)
- [Logging](#logging)

## Functionality
* Administrative UI for configuration
* Support for multiple target systems
* Flexible and configurable mapping
* Scripting support (JavaScript)
* Segregation of duties by creating different communication users for each system

## Prerequisites
* JDK 8
* Linux operating system
* Postgresql Datbase

## Compatibility
You can find the list of supported SCIM operations/target systems/client systems [here](https://github.com/exAphex/asena/wiki/Compatibility).

## ToDo
* Group Handling
* SDK for custom connectors
* more connectivity (SAP/IBM)

## Installation
Installation is described step-by-step in the [installation wiki page](https://github.com/exAphex/asena/wiki/Installation)

## Connect a remote system
### Create a connection
To connect a remote system to asena go to "Systems" -> Add system (Plus icon) 
- Select a system type
- Provide a name (must be unique)
- Provide a description of the system (optional)

### Setup connection parameters
Asena provides a standard set of connection parameters for different systems. You will need to customize them according to your server.

Go to "Systems" -> "Edit System" (pencil icon) -> "Connection Properties". Change the parameters according to your setup. 

### Set read mapping
For reading entities from remote systems you will need to provide a mapping of the remote system attributes to the scim scheme. Asena will provide you a set of pre-set mappings. You can customize the attributes to your needs. Setting a transformation in the read mapping will use the attribute of the remote system and execute the transformation before mapping it to the scim attribute.

Go to "Systems" -> "Edit System" (pencil icon) -> "Read mapping".
Either create a new mapping (plus icon), edit an existing mapping (pencil icon) or delete one (bin icon). 

On creation or on edit the following fields can be set:
* Source attribute is the attribute of the remote system. Asena will provide you a set of attributes, but you can extend it depending on the functionality of the connector.
* Destination attribute is the SCIM attribute to whom the source attribute will be mapped into.
* Transformation script can be used to modify the source attribute before being mapped.

### Set write mapping
When writing entities to a remote system you will need to provide a mapping of the SCIM attributes to the remote system attributes. Asena will provide you a set of pre-set mappings. You can customize the attributes to your needs. Setting a transformation in the write mapping will use the SCIM attribute and execute the transformation before mapping it to the remote system attribute.

Go to "Systems" -> "Edit System" (pencil icon) -> "Write mapping".
Either create a new mapping (plus icon), edit an existing mapping (pencil icon) or delete one (bin icon). 

On creation or on edit the following fields can be set:
* Source attribute is the SCIM attribute. 
* Destination attribute is the remote system attribute to whom the source attribute will be mapped into. Asena will provide you a set of attributes, but you can extend it depending on the functionality of the connector.
* Transformation script can be used to modify the source attribute before being mapped.


## Scripting
Asena supports scripting for attribute transformation. A use case might be the automatic population of the distinguished name or the generation of a mail-address.

To create a script go to "Scripts" -> Create new script (Plus icon) -> select a script name (must be unique) -> Save.

Example script:
```javascript
/*
 * Author: Aydin Tekin
 * Description: Populates a dn from a username
 * Created on: 2021-01-15
*/

function getADDN(param) {
	return "cn=" + param + ",ou=example,ou=com";
}
```

You can now use this script as a transformation rule on the write mapping of your remote system.

## Logging
Asena has a logging mechanism with different kinds of logging levels. These are the supported ones:
* INFO
* DEBUG
* WARNING
* ERROR
* NONE

The default logging level is ERROR. If no valid logging level is set, NONE will be used. It is not recommended to use INFO/DEBUG/WARNING on a productive instance, because it might slow down the performance and fill up your database.

To set the loglevel change the following line in application.properties in the installation folder of Asena:
```
com.asena.scimgateway.logger.level=NONE
```
Restart the server to take the changes in effect.

Logs can be reviewed in "Logs". Cleaning up the logs periodically can improve the performance of Asena.
