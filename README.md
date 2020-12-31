
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
| GET        | NO        |
| POST       | YES       |
| PUT        | YES       |
| PATCH      | NO        |
| DELETE     | YES       |

### Supported target systems
Currently only hardcoded connectors are supported by asena. In the next releases we want to give users to the functionality to create own connectors.

| System type  | Create  | Update  | Delete  |
| -----------|:---------:|:---------:| :---------:|  
| LDAP        | YES        |YES        |YES        |


### Supported client systems
* SAP Identity Management

## ToDo
* Group Handling
* Reading entities
* Authorization 
* SDK for custom connectors
* more connectivity (SAP/IBM)

## Getting started
### 1. Create a new target system
Click on "System" -> Add System -> Provide a system name -> Provide a description -> Select type of system -> Save

### 2. Configure Connection properties
Change the suggested connection properties depending on the system type you chose. 

### 3. Change mapping
Change the mapping between the SCIM attributes received by Asena and your target system. If you need to transform the attributes, select a script providing the functionality.

### 4. Activate System
Activate the system. A communication user will be provided. Use this user for basic auth to communicate with Asena. Also a URL for the SCIM communication will be provided.
