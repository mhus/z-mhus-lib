
## 3.5.0

* update dependencies for karaf 4.2.3 (needed if running as bundle)
* update source for jdk 11
* remove mongo support for the moment

## 3.4.0

* Java 12 compatible
* Removed Lamda Function Hack (not Java 12 compatible)
* Add const gnerator plugin for maven to use field references
* DB Query generator use integer counter for variable place holders instead of UUID (improve readability of log files)
* Reorganize MTimeInterval and rename it to MPeriod
