# TIF
Template Interchange Format (TIF) is an XML format for the definition, exchange and comparison of administrative artifacts defined in 
PTC Integrity Lifecycle Manager.

Administrative artifacts include 
* Item types with their respective workflows, etc.
* Field definitions
* Trigger definitions
* Query definitions
The goal of TIF is to capture, in a declarative form, all the artifacts that make up an Integrity Lifecycle Manager template.
TIF is defined by an XML Schema: TIF.xsd.

TIF’s form (XML, declarative, schema defined) allows it to be subject to a variety of transformations for a variety of purposes: 
* automatic generation of documentation, 
* generation of scripts, 
* differencing of templates
* finding redundant administrative objects

These transformations are supported by the **TIF workbench**. The workbench supports
* deployment of TIF templates
* extraction of TIF from an Integrity Lifecycle Manager server
* differencing of templates
* some level of error checking of manually created templates


## Wiki
Please see the [wiki](http://github.com/PTC-ALM/TIF/wiki) for information on how to contribute to this project.
