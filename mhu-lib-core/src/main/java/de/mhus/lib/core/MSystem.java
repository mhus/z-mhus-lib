/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.system.IApi;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;

public class MSystem {

	private static Log log = Log.getLog(MSystem.class);
	private static ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();

	public static final Map<Integer, String> HTTP_STATUS_CODES = Collections
	        .unmodifiableMap(new HashMap<Integer, String>() {
		        private static final long serialVersionUID = 1L;
		        {
			        put(100, "Continue");
			        put(101, "Switching Protocols");
			        put(200, "OK");
			        put(201, "Created");
			        put(202, "Accepted");
			        put(203, "Non-Authoritative Information");
			        put(204, "No Content");
			        put(205, "Reset Content");
			        put(300, "Multiple Choices");
			        put(301, "Moved Permanently");
			        put(302, "Found");
			        put(303, "See Other");
			        put(304, "Not Modified");
			        put(305, "Use Proxy");
			        put(307, "Temporary Redirect");
			        put(400, "Bad Request");
			        put(401, "Unauthorized");
			        put(402, "Payment Required");
			        put(403, "Forbidden");
			        put(404, "Not Found");
			        put(405, "Method Not Allowed");
			        put(406, "Not Acceptable");
			        put(407, "Proxy Authentication Required");
			        put(408, "Request Time-out");
			        put(409, "Conflict");
			        put(410, "Gone");
			        put(411, "Length Required");
			        put(412, "Precondition Failed");
			        put(413, "Request Entity Too Large");
			        put(414, "Request-URI Too Large");
			        put(415, "Unsupported Media Type");
			        put(416, "Requested range not satisfiable");
			        put(417, "SlimExpectation Failed");
			        put(500, "Internal Server Error");
			        put(501, "Not Implemented");
			        put(502, "Bad Gateway");
			        put(503, "Service Unavailable");
			        put(504, "Gateway Time-out");
			        put(505, "HTTP Version not supported");
		        }
	        });

	/**
	 * Returns the name of the current system. COMPUTERNAME or HOSTNAME.
	 * 
	 * @return the hosts name
	 */
	public static String getHostname() {
		String out = System.getenv().get("COMPUTERNAME");
		if (out == null)
			out = System.getenv().get("HOSTNAME");
		if (out == null) {
			RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
			String name = rt.getName();
			out = MString.afterIndex(name, '@');
		}
		return out;
	}

	/**
	 * Returns the process id of the current application.
	 * 
	 * @return the current process id
	 */
	public static String getPid() {
		RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
		String name = rt.getName();
		return MString.beforeIndex(name, '@');
	}

	/**
	 * Load and return a properties file. If the file not exists it will only
	 * log the impact and return a empty properties object.
	 * 
	 * If the properties object is not null this instance will be used to load
	 * the file entries.
	 * 
	 * 1. Find by system property {propertyname}.file= 2. Find in {current dir}
	 * 3. Find in {current dir}/config 4. Find in {CONFIGURATION}/config 5. Find
	 * in classpath without package name 6. Find in classpath with package of
	 * the owner (if set) 7. throw an error
	 * 
	 * @param owner
	 *            null or reference object for the class path
	 * @param properties
	 *            A pre-instanciated properties object
	 * @param propertyFile
	 *            Name of the properties file, e.g. something.properties
	 * @return The loaded properties instance
	 */
	public static Properties loadProperties(Object owner, Properties properties, String propertyFile) {
		log.d("Loading properties", propertyFile);
		// get resource
		if (properties == null)
			properties = new Properties();
		try {
			URL m_url = locateResource(owner, propertyFile);
			if (m_url == null) {
				log.w("Properties file not found", propertyFile);
				return properties;
			}
			log.i("load", m_url);
			InputStream stream = m_url.openStream();
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			log.i("Error loading properties file", propertyFile, e.toString());
			// logger.t(e);
		}
		return properties;
	}

	/**
	 * 
	 * 1. Find by system property {propertyname}.file= 2. Find in {current dir}
	 * 3. Find in {current dir}/config 4. Find in {CONFIGURATION}/config 5. Find
	 * in classpath without package name 6. Find in classpath with package of
	 * the owner (if set) 7. throw an error
	 * 
	 * @param owner
	 * @param fileName
	 * @return the reference to the resource
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static URL locateResource(Object owner, String fileName) throws IOException {
		fileName = MFile.normalize(fileName);

		URL url = null;

		Class<?> ownerClass = null;
		if (owner != null) {
			if (owner instanceof Class)
				ownerClass = (Class<?>) owner;
			else
				ownerClass = owner.getClass();
		}

		String qName = ownerClass.getPackage().getName() + "." + fileName;

		String location = System.getProperty(qName + ".file");
		if (url == null && location != null) {
			File f = new File(location);
			if (f.exists() && f.isFile())
				url = f.toURL();
			else
				throw new FileNotFoundException("Configured file not found: " + location + " for " + qName);
		}
		
		// do not load file from app root directory any more
		// {
		// File f = new File(fileName);
		// if ( f.exists() && f.isFile() )
		// return f.toURL();
		// }

		{
			File f = MApi.getFile(IApi.SCOPE.ETC, qName);
			if (f.exists() && f.isFile())
				return f.toURL();
		}

// This is done by using MApi.getFile()
//		try {
//			String configurationPath = System.getenv("CONFIGURATION");
//			if (url == null && configurationPath != null) {
//				File f = new File(configurationPath + "/" + qName);
//				if (f.exists() && f.isFile())
//					url = f.toURL();
//			}
//		} catch (SecurityException e) {
//		}

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (url == null && loader != null)
			url = loader.getResource(fileName);

		if (ownerClass != null && url == null) {
			url = ownerClass
			        .getResource("/" + ownerClass.getPackage().getName().replaceAll("\\.", "/") + "/" + fileName);
		}
		if (ownerClass != null && url == null) {
			url = ownerClass.getResource(ownerClass.getPackage().getName().replaceAll("\\.", "/") + "/" + fileName);
		}

		if (url != null)
			return url;
		throw new FileNotFoundException("Cannot locate resource: " + ownerClass.getPackage().getName() + "/" + fileName);
	}

	/**
	 * makeshift system beep if awt.Toolkit.beep is not available. Works also in
	 * JDK 1.02.
	 */
	public static void beep() {
		System.out.print("\007");
		System.out.flush();
	} // end beep

	public static String findCalling(int returns) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (stack.length > returns)
			return stack[returns].getClassName();
		return "?";
	}

	public static String findCalling() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (StackTraceElement step : stack) {
			String n = step.getClassName();
			if (!n.startsWith("java.lang") && !n.startsWith("de.mhus.lib.core"))
				return n;
		}
		return "?";
	}

	public static String findSourceMethod(int returns) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (stack.length > returns)
			return stack[returns].getMethodName();
		return "?";
	}

	public static String findSourceMethod() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for (StackTraceElement step : stack) {
			String n = step.getClassName();
			if (!n.startsWith("java.lang") && !n.startsWith("de.mhus.lib.core"))
				return step.getMethodName();
		}
		return "?";
	}

	/**
	 * Return the name of the main class or null if not found.
	 * 
	 * @return the main class name
	 */
	public static String getMainClassName() {
		for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
			if (entry.getKey().startsWith("JAVA_MAIN_CLASS"))
				return entry.getValue();
		}
		return null;
	}

	/**
	 * Return the system temp directory.
	 * 
	 * @return path to the tmp directory
	 */
	public static String getTmpDirectory() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * Return a string representation of the data. use this to Implement
	 * toString()
	 * 
	 * @param sender
	 * @param attributes
	 * @return Stringified attributes
	 */
	public static String toString(Object sender, Object... attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		if (sender != null)
			sb.append(sender instanceof String ? sender : sender.getClass().getSimpleName()).append(':');
		boolean first = true;
		for (Object a : attributes) {
			if (!first)
				sb.append(',');
			else
				first = false;
			MString.serialize(sb, a, null);
		}
		sb.append(']');
		return sb.toString();
	}

	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotation) {
		Class<?> current = clazz;
		while (current != null) {
			if (current.isAnnotationPresent(annotation))
				return current.getAnnotation(annotation);
			current = current.getSuperclass();
		}
		return null;
	}

	public static boolean equals(Object a, Object b) {
		if (a == null && b == null)
			return true;
		if (a == null)
			return false;
		return a.equals(b);
	}

	/**
	 * Start a script and return the result as struct.
	 * 
	 * @param dir
	 * @param script
	 * @param timeout
	 * @return The result of execution
	 */
	public static ScriptResult startScript(File dir, String script, long timeout) {
		log.d("script", dir, script);
		ProcessBuilder pb = new ProcessBuilder(new File(dir, script).getAbsolutePath());
		@SuppressWarnings("unused")
		Map<String, String> env = pb.environment();
		pb.directory(dir);
		ScriptResult out = new ScriptResult();
		try {
			Process p = pb.start();

			out.output = new BufferedReader(new InputStreamReader(p.getInputStream()));
			out.error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			p.waitFor();
			p.destroy();

			out.rc = p.exitValue();

		} catch (Throwable t) {
			out.exception = t;
		}
		return out;
	}

	public static class ScriptResult {

		public Throwable exception;
		public int rc;
		public BufferedReader error;
		public BufferedReader output;

	}

	/**
	 * Get the identification of the application host:pid
	 * 
	 * @return host:pid
	 */
	public static String getAppIdent() {
		return getHostname() + ":" + getPid();
	}

	/**
	 * Returns the id of the object like the original toString() will do
	 * 
	 * @param o
	 * @return the id
	 */
	public static String getObjectId(Object o) {
		if (o == null)
			return "null";
		String name = o.getClass().getName();
		if (name == null)
			name = getClassName(o);
		return name + "@" + Integer.toHexString(System.identityHashCode(o));
	}

	/**
	 * Returns the canonical name of the main class.
	 * 
	 * @param obj
	 *            Object or class
	 * @return The name
	 */
	public static String getClassName(Object obj) {
		Class<? extends Object> clazz = getMainClass(obj);
		if (clazz == null)
			return "null";
		return clazz.getCanonicalName();
	}

	public static String getSimpleName(Object obj) {
		Class<? extends Object> clazz = getMainClass(obj);
		if (clazz == null)
			return "null";
		return clazz.getSimpleName();
	}
	
	/**
	 * Returns the class of the object or class or if the class is anonymous the
	 * surrounding main class.
	 * 
	 * @param obj
	 *            object or class
	 * @return The main class
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Class<?> getMainClass(Object obj) {
		if (obj == null)
			return null;
		Class<? extends Object> clazz = obj.getClass();
		if (obj instanceof Class)
			clazz = (Class) obj;
		while (clazz != null && clazz.isAnonymousClass())
			clazz = clazz.getEnclosingClass();
		if (clazz == null)
			return null;
		return clazz;
	}

	/**
	 * Return the declared template class for the index. This is only possible
	 * if the class itself extends a templated class and declares the templates
	 * with classes.
	 * 
	 * @param clazz
	 *            The class to check
	 * @param index
	 *            Template index, starts at 0
	 * @return The canonical name of the defined template
	 */
	// https://stackoverflow.com/questions/3437897/how-to-get-a-class-instance-of-generics-type-t#3437930
	public static String getTemplateCanonicalName(Class<?> clazz, int index) {
		Type mySuperclass = clazz.getGenericSuperclass();
		if (mySuperclass instanceof ParameterizedType) {
			Type[] templates = ((ParameterizedType) mySuperclass).getActualTypeArguments();
			if (index >= templates.length)
				return null;
			Type tType = templates[index];
			String templName = tType.getTypeName();
			return templName;
		}
		return null;
	}

	/**
	 * Compares two objects even if they are null.
	 * 
	 * compareTo(null, null ) === 0 compareTo("", "" ) === 0 compareTo(null, ""
	 * ) === -1 compareTo("", null ) === 1
	 * 
	 * returns s1.comareTo(s2)
	 * 
	 * groovy:000> s1 = new Date(); === Wed Jun 28 12:22:35 CEST 2017
	 * groovy:000> s2 = new Date(); === Wed Jun 28 12:22:40 CEST 2017
	 * groovy:000> groovy:000> s1.compareTo(s2) === -1 groovy:000>
	 * s2.compareTo(s1) === 1
	 * 
	 * @param s1
	 * @param s2
	 * @return see Comparator
	 */
	public static <T extends Comparable<T>> int compareTo(T s1, T s2) {
		if (s1 == null && s2 == null)
			return 0;
		if (s1 == null)
			return -1;
		if (s2 == null)
			return 1;
		return s1.compareTo(s2);
	}

	public static String freeMemoryAsString() {
		long free = freeMemory();
		return MString.toByteDisplayString(free);
	}

	public static String maxMemoryAsString() {
		return MString.toByteDisplayString(Runtime.getRuntime().maxMemory());
	}

	public static String memDisplayString() {
		return freeMemoryAsString() + " / " + maxMemoryAsString();
	}

	/**
	 * Return the amount of free not allocated memory
	 * 
	 * @return memory in bytes
	 */
	public static long freeMemory() {
		Runtime r = Runtime.getRuntime();
		return r.maxMemory() - r.totalMemory() + r.freeMemory();
	}

	/**
	 * Get the field in this or any superclass.
	 * 
	 * @param clazz
	 * @param name
	 * @return The field or null if not found
	 */
	public static Field getDeclaredField(Class<?> clazz, String name) {
		if (clazz == null || name == null)
			return null;
		try {
			Field field = clazz.getDeclaredField(name);
			return field;
		} catch (NoSuchFieldException e) {
		}
		return getDeclaredField(clazz.getSuperclass(), name);
	}

	/**
	 * Load the class for the given type declaration. Also primitive and array
	 * declarations are allowed.
	 * 
	 * @param type
	 *            The type as primitive int, long ... String, Date the class
	 *            name or as Array with pending [].
	 * @param cl
	 *            The class loader or null to use the thread context class
	 *            loader.
	 * @return The class instance.
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(String type, ClassLoader cl) throws ClassNotFoundException {
		if (cl == null)
			cl = Thread.currentThread().getContextClassLoader();
		if ("int".equals(type))
			return int.class;
		if ("long".equals(type))
			return long.class;
		if ("short".equals(type))
			return short.class;
		if ("double".equals(type))
			return double.class;
		if ("float".equals(type))
			return float.class;
		if ("byte".equals(type))
			return byte.class;
		if ("boolean".equals(type))
			return boolean.class;
		if ("char".equals(type))
			return char.class;
		if ("String".equals(type))
			return String.class;
		if ("Date".equals(type))
			return Date.class;

		if ("int[]".equals(type))
			return int[].class;
		if ("long[]".equals(type))
			return long[].class;
		if ("short[]".equals(type))
			return short[].class;
		if ("double[]".equals(type))
			return double[].class;
		if ("float[]".equals(type))
			return float[].class;
		if ("byte[]".equals(type))
			return byte[].class;
		if ("boolean[]".equals(type))
			return boolean[].class;
		if ("char[]".equals(type))
			return char[].class;
		if ("String[]".equals(type))
			return String[].class;
		if ("Date[]".equals(type))
			return Date[].class;

		boolean array = false;
		if (type.endsWith("[]")) {
			array = true;
			type = type.substring(0, type.length() - 2);
		}

		Class<?> clazz = cl.loadClass(type);
		if (array) {
			clazz = Array.newInstance(clazz, 0).getClass();
		}
		return clazz;

	}

	/**
	 * Watch for a defined time of milliseconds all threads and returns the used
	 * cpu time.
	 * 
	 * @param sleep
	 * @return List of found values
	 * @throws InterruptedException
	 */
	public static List<TopThreadInfo> threadTop(long sleep) throws InterruptedException {
		sleep = Math.max(sleep, 200);

		LinkedList<TopThreadInfo> threads = new LinkedList<>();
		for (Entry<Thread, StackTraceElement[]> thread : Thread.getAllStackTraces().entrySet()) {
			threads.add(new TopThreadInfo(tmxb, thread));
		}

		threads.forEach(t -> t.start());
		Thread.sleep(sleep);
		threads.forEach(t -> t.stop());

		long sumCpu = 0;
		long sumUser = 0;
		for (TopThreadInfo t : threads) {
			sumCpu += t.getCpuTime();
			sumUser += t.getUserTime();
		}
		for (TopThreadInfo t : threads)
			t.setSumTime(sumUser, sumCpu);

		return threads;
	}

	public static class TopThreadInfo {

		private Thread thread;
		private StackTraceElement[] stacktrace;
		private long startTime;
		private ThreadMXBean tmxb;
		private long startUser;
		private long startCpu;
		private long stopTime;
		private long stopUser;
		private long stopCpu;
		private long diffTime;
		private long diffUser;
		private long diffCpu;
		private double perCpu;
		private double perUser;

		private TopThreadInfo(ThreadMXBean tmxb, Entry<Thread, StackTraceElement[]> thread) {
			this.tmxb = tmxb;
			this.thread = thread.getKey();
			this.stacktrace = thread.getValue();
		}

		private void start() {
			startTime = System.currentTimeMillis();
			startUser = tmxb.getThreadUserTime(thread.getId());
			startCpu = tmxb.getThreadCpuTime(thread.getId());
		}

		private void stop() {
			stopTime = System.currentTimeMillis();
			stopUser = tmxb.getThreadUserTime(thread.getId());
			stopCpu = tmxb.getThreadCpuTime(thread.getId());

			diffTime = stopTime - startTime;
			diffUser = stopUser - startUser;
			diffCpu = stopCpu - startCpu;

		}

		private void setSumTime(long userTime, long cpuTime) {
			if (cpuTime > 0)
				this.perCpu = (double) (diffCpu * 100) / (double) cpuTime;
			if (userTime > 0)
				this.perUser = (double) (diffUser * 100) / (double) userTime;
		}

		public long getCpuTime() {
			return diffCpu;
		}

		public long getUserTime() {
			return diffUser;
		}

		public long getInterval() {
			return diffTime;
		}

		public double getCpuPercentage() {
			return perCpu;
		}

		public double getUserPercentage() {
			return perUser;
		}

		public Thread getThread() {
			return thread;
		}

		public StackTraceElement[] getStacktrace() {
			return stacktrace;
		}

		public long getCpuTotal() {
			return stopCpu;
		}

	}

	/**
	 * Executes a command and returns an array of 0 = strOut, 1 = stdErr
	 * 
	 * @param command
	 * @return 0 = strOut, 1 = stdErr
	 * @throws IOException
	 */
	public static String[] execute(String... command) throws IOException {

		ProcessBuilder pb = new ProcessBuilder(command);
		Process proc = pb.start();
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

		// read the output from the command
		StringBuilder stdOut = new StringBuilder();
		String s = null;
		while ((s = stdInput.readLine()) != null)
			stdOut.append(s);

		// read any errors from the attempted command
		StringBuilder stdErr = new StringBuilder();
		while ((s = stdError.readLine()) != null)
			stdErr.append(s);

		return new String[] { stdOut.toString(), stdErr.toString() };
	}

	public static boolean isWindows() {
		final String os = System.getProperty("os.name");
		return os.contains("Windows");
	}

	public static File getUserHome() {
		String currentUsersHomeDir = System.getProperty("user.home");
		return new File(currentUsersHomeDir);
	}

	/**
	 * Like System.setProperty(). Sets the property with to a value. But this
	 * one accepts also null values and will clear the property in this case.
	 * 
	 * @param key
	 * @param value
	 * @return Previous value
	 */
	public static String setProperty(String key, String value) {
		if (value == null)
			return System.clearProperty(key);
		else
			return System.setProperty(key, value);
	}

	/**
	 * For completeness. The same functionality like System.getProperty()
	 * 
	 * @param key
	 * @return Current value or null
	 */
	public static String getProperty(String key) {
		return System.getProperty(key);
	}

	/**
	 * For completeness. The same functionality like System.getProperty()
	 * 
	 * @param key
	 * @param def
	 * @return Current value or def
	 */
	public static String getProperty(String key, String def) {
		return System.getProperty(key, def);
	}

	/**
	 * Return a unique class name for the class with package, main class name
	 * and sub class name.
	 * 
	 * @param clazz
	 *            The class to analyze
	 * @return The name of the class
	 */
	public static String getCanonicalClassName(Class<?> clazz) {

		if (clazz.isLocalClass())
			return clazz.getCanonicalName();

		if (clazz.isAnonymousClass())
			return clazz.getName();

		return clazz.getCanonicalName();

	}

	public static boolean isLockedByThread(Object value) {
		if (value == null)
			return false;
		int objectHash = value.hashCode();
		for (long threadId : tmxb.getAllThreadIds()) {
			ThreadInfo info = tmxb.getThreadInfo(threadId);
			for (LockInfo locks : info.getLockedSynchronizers())
				if (locks.getIdentityHashCode() == objectHash)
					return true;
		}
		return false;
	}

	public static Object getJavaVersion() {
		return System.getProperty("java.version");
	}

	private static final Instrumentation instrumentation = ByteBuddyAgent.install();
	/*
	 * Use byte buddy to get the lambda byte code
	 */
	public static byte[] getBytes(Class<?> c) throws IOException {
	    ClassFileLocator locator = ClassFileLocator.AgentBased.of(instrumentation, c);
	    TypeDescription.ForLoadedType desc = new TypeDescription.ForLoadedType(c);
	    ClassFileLocator.Resolution resolution = locator.locate(desc.getName());
	    return resolution.resolve();
	}

}
