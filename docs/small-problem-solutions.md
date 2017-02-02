In the following list I try to explain how to solve all the small, every day problems coding in java. It's not a proper list but it will grow ...

- Cast to a primitive type using a default value and prevent exceptions:
	MCast.toXXX(from, default), e.g.
	MCast.toint(from, -1)
	MCast.tolong(from, -1)
- Cast to a Date Object supporting different input formats by default:
	MDate.toDate(from, default) or MDate.toDate(from, default, location). Location is needed to detect different input formats, e.g. English (dd/mm/yyyy) or american (mm/dd/yyyy). They can't be recognized automatically. Examples:
	MDate.toDate("1.2.2003", null)
	MDate.toDate("1.2.2003 10:20, null)
	MDate.toDate("now",null)
- Get current Hostname
	This task depends on the host system and can be difficylt. Use MSystem.getHostname() to get it.
- Get the current Process Id
	Use MSystem.getPid() to get the process Id.
- Compare values they can be null
	Usually a from.equals(to) throws an exception if from is empty. Use MSystem.equals(from,to) to compare without that problem. If both are null the method will return true.
	Compare with MSystem.compareTo(a, b) to avoid NullPointerExceptions
- Load a properties file using different options where the file can be found
	Use MSystem.loadProperties(...) The mechanism checks in the following order ..
	1. Find by system property {property name}.file=
	2. Find in {current dir}
	3. Find in {current dir}/config
	4. Find in {CONFIGURATION}/config
	5. Find in classpath without package name
	6. Find in classpath with package of the owner (if set)
	(7. throws an error)
- Get the id of an object
	The default Object.toString() returns the id of an object. Like 1265712@ObjectClass. But if the subclass overwrites the toString() method the id can't be detected. Use the MSystem.getObjectId() to get the id.
- Load or save the content of a file
	Loading the content of a file can be done in different ways...
	Load binary file: MFile.readBinaryFile()
	Load file as text: MFile.readFile()
	Load the file as set of lines: MFile.readLines()
	The same concept to write files:
	Save binary file: MFile.writeBinaryFile()
	Save file as text: MFile.writeFile()
	Save the lines of a file: MFile.writeLines()
 
- Copy a file or stream
	To copy all content of a file or a stream use MFile.copyFile()
 
- Copy the hole or parts of a directory
	To copy a directory use MFile.copyDir() if you need to filter the directory use the variant with 'FileFilter'

- Delete a hole directory
	To delete a directory with sub directories and files use MFile.deleteDir().
 
- Normalize a file name
	Most time it's important to normalize the name of a file before using it. specially if you take the name from a user input the filename can be use to exploit the application. Use MFile.toFileName() to normalize the name.
- Mime type of a file extension
	To get the mime type use the method MFile.getMimeType(). if you have given a File use this snippet: MFile.getMimeType(MFile.getFileSuffix(file))
 
- Change the extension of a file name
	If you have the name of a file and need to change the extension without complex string chopping use MFile.replaceExtension().
- Simple password encoding/decoding
	Use MPassword.encode() and MPassword.decode() to hide the real password from eyes of an third person. The encoding are not save!
	You can also use the utility class Rot13 to hide text.
- Encode a SQL value
	If you generate SQL queries you need to encode the value parts. In other case SQL injection is possible. Use MSql.escape() to escape special characters and MSql.encode() to generate a full value string (including quots).
- Check if a string is empty
	checking of a string is empty could be tricky. By the fault you have to code 'if (str == null || str.length() == 0) ...'. To reduce the overhead you can use MString.isSet() and MString.isEmpty(). It's also possible to Trim the string using MString.isSetTrim() and MString.isEmptyTrim().
- Chop a string in pieces by delimiters
	To find a special delimiter in a string and chop the string at this position you need to find the position, check if you found one and use substring() to chop the string. That could be annoying to blow the code up with a lot of position variables.
	Use MString.beforeIndex() and MString.afterIndex() to make you code more readable. Also MString.beforeLastIndex() and MString.afterLastIndex() are available. Use MString.isIndex() if you need to check if the delimiter is present.
- Compare a string with a pattern
	Match strings against regular expressions are simple and supported by default. But the most users are not able to create regular expressions. You need to offer more simpler ways to add placeholders. A simple way is to allow asterisk '*' for arbitrary text. Use MString.compareFsLike() to support this method. Alternatively you can use MString.compareSqlLike() the method will allow using '%' as placeholder.
- Fill strings with characters
	To fill a string to a specified size with a defined character use MString.leftPad() and MString.rightPad().
- Join string part together using a glue
	this simple but not supported requirement are done by MString.join().
- Print the time, how long something needs to be done
	Use the class MStopWatch to stop the processing time of an task.
	MStopWatch watch = new MStopWatch().start();
	...
	watch.stop();
	System.out.println( watch.getCurrentTimeAsString() );
	You can MStopWatch start and stop multiple times to collect the time something needs.
- Load and save a XML file
	Use MXml.loadXml() to lad and parse a XML file. Use MXml.saveXml() to store it.
- Encode / decode XML or HTML content
	Use MXml.encode() and MXml.decode() to handle XML or HTML content. The methods will add or remove the html character reference encodings.
- Simply find elements in a XML node
	Collecting or querying sub nodes in a w3c xml structure can be annoying. Use the simple methods in the class MXml to parse XML structures.
	Get sets of elements: MXml.getLocalElement...()
	Get a single element: MXml.getElemetByPath()
	Get the text value inside an element tag: MXml.getValue()
- Base64 encoding / decoding
	Use the utility class Base64 to work with base 64 content.
- URL Encoding
	The class Rfc1738 offers utilities to encode /decode url component strings and explode / implode url based parameters. The text will be encoded as described in RFC 1738 (using a '%' to encode special characters). The class also offers a map like store to handle URL parameters.