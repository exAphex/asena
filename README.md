
![Tests](https://github.com/exAphex/asena/workflows/Tests/badge.svg) [![codecov](https://codecov.io/gh/exAphex/asena/branch/master/graph/badge.svg?token=P1IZLIO13A)](https://codecov.io/gh/exAphex/asena) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
# Asena SCIM Gateway
Asena is a SCIM Gateway to systems which do not have native SCIM interfaces. It can be used as a middleware for identity management systems to provision users and group assignments. Providing a UI for administration it is easy to manage and is already used in productive environments.

## Contents
- [Functionality](#functionality)
- [Prerequisites](#prerequisites)
- [Compatibility](#compatibility)
    - [Supported SCIM operations](#supported-scim-operations)
    - [Supported target systems](#supported-target-systems)
    - [Supported client systems](#supported-client-systems)
- [ToDo](#todo)
- [Getting started](#getting-started)

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
### Supported SCIM operations
| Operation  | Support?  |
| -----------|:---------:| 
| GET        | :white_check_mark:        |
| POST       | :white_check_mark:       |
| PUT        | :white_check_mark:       |
| PATCH      | :x:        |
| DELETE     | :white_check_mark:       |

### Supported target systems
Currently only hardcoded connectors are supported by asena. In the next releases we want to give users to the functionality to create own connectors.

| System type  | Create  | Update  | Delete  |
| -----------|:---------:|:---------:| :---------:|  
| LDAP        | :white_check_mark:        |:white_check_mark:        |:white_check_mark:        |


### Supported client systems
* SAP Identity Management

## ToDo
* Group Handling
* Authorization 
* SDK for custom connectors
* more connectivity (SAP/IBM)

## Getting started
### 1. Installation
Use installer.jar to launch the installation routine:
```
$ java -jar installer.jar
```

### 2. Login
After the installation and startup of the server you will be greeted with a login form. Default credentials:
```
Username: admin
Password: admin
``` 
