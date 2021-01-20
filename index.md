![Tests](https://github.com/exAphex/asena/workflows/Tests/badge.svg) [![codecov](https://codecov.io/gh/exAphex/asena/branch/master/graph/badge.svg?token=P1IZLIO13A)](https://codecov.io/gh/exAphex/asena) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
## Overview

Asena is a SCIM Gateway to systems which do not have native SCIM interfaces. It can be used as a middleware for identity management systems to provision users and group assignments.

## Architecture
![image](https://raw.githubusercontent.com/exAphex/asena/master/assets/architecture.png)



## Contents
- [Functionality](#functionality)
- [Prerequisites](#prerequisites)
- [Compatibility](#compatibility)
- [Getting started](#getting-started)
- [ToDo](#todo)

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

## Getting started
A step-by-step documentation of setting up Asena and using it can be found in the [wiki page](https://github.com/exAphex/asena/wiki/Getting-started)

## ToDo
* Group Handling
* SDK for custom connectors
* more connectivity (SAP/IBM)
