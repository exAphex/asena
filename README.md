# Asena SCIM Gateway
Asena is a SCIM Gateway to systems which do not have native SCIM interfaces. It can be used as a middleware for identity management systems to provision users and group assignments. Providing a UI for administration it is easy to manage and is already used in productive environments.

## Functionality
* Administrative UI for configuration
* Support for multiple target systems
* Flexible and configurable mapping
* Scripting support (JavaScript)
* Segregation of duties by creating different communication users for each system
* Fieldtested

### Supported target systems
Currently only hardcoded connectors are supported by asena. In the next releases we want to give users to the functionality to create own connectors.

Supported systems:
* CSV

### Supported client systems
* SAP Identity Management

## Getting started
### 1. Create a new target system
Click on "System" -> Add System -> Provide a system name -> Provide a description -> Select type of system -> Save

### 2. Configure Connection properties
Change the suggested connection properties depending on the system type you chose. 

### 3. Change mapping
Change the mapping between the SCIM attributes received by Asena and your target system. If you need to transform the attributes, select a script providing the functionality.

### 4. Activate System
Activate the system. A communication user will be provided. Use this user for basic auth to communicate with Asena. Also a URL for the SCIM communication will be provided.

## Scripting
Using the scripting engine, you can create complex tranformation rules in a simple manner.
Scripts always take "param" as the current value of the attribute. The return value will then be written to the remote system.

### Example
