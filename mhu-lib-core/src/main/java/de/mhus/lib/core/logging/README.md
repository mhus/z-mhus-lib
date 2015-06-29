# Logging

## Basics

* Dirty Logging: In some cases the Log-Engine can't be used. Must time if the Log itself or MSingleton need to write something down.
 MSingleton can be configured to write the dirty log into the System.out stream.
* LocalTrace: Local Trace is a fast way to trace single loggers. Every logger ask MSingleton if LocalTrace is active for the logger name.
MSingleton will log the requests if DirtyTrace is enabled. If LocalTrace is enabled every logging will set to INFO level.
* levelMapper: A levelMapper is able to manipulate the log level. If you define a levelMapper the mapper can manipulate all logging. 
Specially the TrailLevelMapper is able to activate logging for a special thread for a defined time.
* parameterMapper: The parameterMapper is able to change the printed objects before given to the serializer (toString()).

## Concept

The basic idea is to get the engine rid of generating unused log messages if the log level is not reached. For this give every logged 
object direct in an array to the logger like: d("some info with object",object) to debug a text with an object. If debug level is not
enabled the function call will only generate a minimum amount of overhead. If debug is enabled the text will be congatinated to a
string and the function toString() is called on 'object'. The result is similar to "[some info with object][object info]".

