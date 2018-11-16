package de.mhus.lib.constgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.xbean.finder.ClassFinder;

import de.mhus.lib.basics.consts.GenerateConst;
import de.mhus.lib.basics.consts.GenerateHidden;
import de.mhus.lib.basics.consts.Identifier;

@Mojo(name = "const-generate", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, inheritByDefault = false)
public class ConstGeneratorMojo extends AbstractMojo {
    
	private HashSet<String> ignoreList = new HashSet<>();
	{
		ignoreList.add("EQUALS");
		ignoreList.add("CLASS");
		ignoreList.add("CLONE");
		ignoreList.add("WAIT");
		ignoreList.add("FINALIZE");
		ignoreList.add("HASH_CODE");
		ignoreList.add("NOTIFY");
		ignoreList.add("NOTIFY_ALL");
		ignoreList.add("WRITE_EXTERNAL");
		ignoreList.add("READ_EXTERNAL");
		ignoreList.add("TO_STRING");
	}
	
	@Parameter(defaultValue = "${project}")
    protected MavenProject project;
	
//	@Parameter(defaultValue = "${project.build.directory}/generated/mhus-const")
	@Parameter
	protected String outputDirectory;
	
	@Parameter(defaultValue = "project")
    protected String classLoader;
    
    @Parameter(defaultValue=".*")
    protected String artifactInclude;

//    @Component
//    private BuildContext buildContext;
    
	@Parameter
	protected boolean debug = false;

	@Parameter
	protected boolean force = false;
	
	@Parameter
	protected String prefix = "_";
	
	@Parameter
	protected String ignore = null;
	
	private URLClassLoader loader;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		// fill ignore list
		if (ignore != null) {
			for (String i : ignore.split(",")) {
				ignoreList.add(i.toUpperCase());
			}
		}
		
		try {
			ClassFinder finder = createFinder(classLoader);
			 List<Class<?>> classes = finder.findAnnotatedClasses(GenerateConst.class);
			 for (Class<?> clazz : classes) {
	                URL classUrl = clazz.getClassLoader().getResource(clazz.getName().replace('.', '/') + ".class");
	                if (classUrl == null || clazz.getName() == null || clazz.getCanonicalName().indexOf('$') > -1) { // do not process inner classes
	                    getLog().info("Ignoring non main class " + classUrl);
	                    continue;
	                }
	                File classSource = findClassSourceFile(clazz);
	                if (classSource == null) {
	                		getLog().info("Ignoring, source not found " + classUrl);
	                		continue;
	                }
	                
	                // find target file
	                String constClassName = prefix + clazz.getCanonicalName();
	                File constFile = null;
	                if (outputDirectory != null)
	                		constFile = new File(outputDirectory + File.separatorChar + constClassName.replace('.', File.separatorChar) + ".java" );
	                else
	                		constFile = new File(classSource.getParentFile(), prefix + clazz.getSimpleName() + ".java" );
	                
	                // find current declared fields
	                HashMap<String, String> constFields = new HashMap<>();
	                try {
	                		Class<?> constClass = loader.loadClass(constClassName);
	                		Field[] fields = constClass.getDeclaredFields();
	                		for (Field f : fields)
	                			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
	                				String value = ((Identifier)f.get(null)).toString();
	                				constFields.put(f.getName(), value);
	                			}
	                } catch (ClassNotFoundException e) {}
	                if (debug) {
		                getLog().info("Found " + classSource);
		                getLog().info("   To: " + constFile);
	                }
	                
	                // find class fields
	                Map<String,String> fields = analyzeClass(clazz);
	                
	                // compare
	                if (!force && 
	                		compareList(constFields.keySet(),fields.keySet()) && 
	                		compareList(constFields.values(),fields.values())
	                	   ) {
	                		if (debug)
	                			getLog().info("not changed");
	                		continue;
	                }
	                
	                getLog().info("Write " + constFile);
	                
	                // create
	                StringBuilder c = new StringBuilder(); // content
	                c.append("package ").append(clazz.getPackage().getName()).append(";\n\n");
	                c.append("import de.mhus.lib.basics.consts.Identifier;\n");
	                c.append("import de.mhus.lib.basics.consts.ConstBase;\n");
	                c.append("/**\n * File created by mhu const generator. Changes will be overwritten.\n").append(" **/\n");
	                c.append("public class ").append(prefix).append(clazz.getSimpleName()).append(" extends ConstBase {\n\n");
	                
	                for (Entry<String, String> field : fields.entrySet() ) {
	                		c.append("public static final Identifier ")
	                			.append(field.getKey()).append(" = new Identifier(")
	                			.append(clazz.getCanonicalName()).append(".class, \"")
	                			.append(field.getValue().replace("\\", "\\\\").replace("\"", "\\\"") )
	                			.append("\");\n");
	                }
	                
	                c.append("\n}");
	                
	                // write
	                File dir = constFile.getParentFile();
	                if (!dir.exists()) dir.mkdirs();
	                
	                FileOutputStream fos = new FileOutputStream(constFile);
	                fos.write(c.toString().getBytes("utf-8"));
	                fos.close();
	                
	                
			 }
			 
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    private boolean compareList(Collection<String> set1, Collection<String> set2) {
    		if (set1.size() != set2.size()) {
    			if (debug)
    				getLog().info("--- differnt size");
    			return false;
    		}
    		for (String key : set1)
    			if (!set2.contains(key)) {
    				if (debug) getLog().info("--- key not found " + key);
    				return false;
    			}
		return true;
	}

	private Map<String, String> analyzeClass(Class<?> clazz) {
		
		GenerateConst config = clazz.getAnnotation(GenerateConst.class);
		
    		TreeMap<String,String> out = new TreeMap<String,String>();
    		for (Field field : findFields(clazz)) {
    			
    			String name = field.getName();
    			String orgName = name;
    			name = toName(field.getName());
    			
    			if (ignore(config, name)) continue;
    			if (field.getAnnotation(GenerateHidden.class) != null) continue;
    			
    			if (config.restricted() || Modifier.isPublic(field.getModifiers()) )
    				out.put("FIELD_" + name, orgName);
    			
    			if (!hasAnnotation(config, field.getAnnotations())) continue;

    			out.put("_" + name, orgName);

    		}
    		for (Method meth : findMethods(clazz)) {
			String name = meth.getName();
			
			if (name.startsWith("get") || name.startsWith("set")) name = name.substring(3);
			else
			if (name.startsWith("is")) name = name.substring(2);
			String orgName = name;
			name = toName(name);
			
			if (ignore(config, name)) continue;
			if (meth.getAnnotation(GenerateHidden.class) != null) continue;

			if (config.restricted() || Modifier.isPublic(meth.getModifiers()) )
				out.put("METHOD_" + toName(meth.getName()), meth.getName());
			
			if (!hasAnnotation(config, meth.getAnnotations())) continue;

			out.put("_" + name, orgName);

    		}
    		
    		out.put("CLASS_NAME", clazz.getName());
    		out.put("CLASS_PATH", clazz.getCanonicalName());
    		out.put("CLASS_EXTENDS", clazz.getSuperclass().getCanonicalName());
    		out.put("PROJECT_VERSION", project.getVersion());
    		out.put("PROJECT_ARTIFACT", project.getArtifactId());
    		out.put("PROJECT_GROUP", project.getGroupId());
    		out.put("PROJECT_DESCRIPTION", project.getDescription());

		return out;
	}

	private boolean hasAnnotation(GenerateConst config, Annotation[] annotations) {
		if (config == null || config.annotation().length == 0) return true;
		
		// x^n !!!
		for (Class<? extends Annotation> a1 : config.annotation()) 
			for (Annotation a2 : annotations) {
				if (a1.getCanonicalName().equals( a2.annotationType().getCanonicalName() )) return true;
			}
		return false;
	}

	private boolean ignore(GenerateConst config, String name) {
		name = name.toUpperCase();
		if ( ignoreList.contains(name) ) return true;
		for (String item : config.ignore())
			if (name.equals(item.toUpperCase())) return true;
		return false;
	}

	private List<Method> findMethods(Class<?> clazz) {
		LinkedList<Method> list = new LinkedList<>();
		findMethods(list, clazz);
		return list;
	}

	private void findMethods(LinkedList<Method> list, Class<?> clazz) {
		if (clazz == null) return;
		for (Method m : clazz.getDeclaredMethods())
			list.add(m);
		findMethods(list, clazz.getSuperclass());
	}

	private List<Field> findFields(Class<?> clazz) {
		LinkedList<Field> list = new LinkedList<>();
		findFields(list, clazz);
		return list;
	}

	private void findFields(List<Field> list, Class<?> clazz) {
		if (clazz == null) return;
		for (Field f : clazz.getDeclaredFields())
			list.add(f);
		findFields(list, clazz.getSuperclass());
	}
	
	private String toName(String name) {
		StringBuilder out = new StringBuilder();
		boolean lastUpper = false;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			boolean isUpper = Character.isUpperCase(c);
			if (i != 0 && isUpper && !lastUpper)
				out.append('_');
			lastUpper = isUpper;
			c = Character.toUpperCase(c);
			out.append(c);
		}
		return out.toString();
	}

	private File findClassSourceFile(Class<?> clazz) {
    		String cp = clazz.getCanonicalName().replace('.', File.separatorChar);
    		if (cp.endsWith(".class")) cp = cp.substring(0, cp.length() - 6); // should be every time
    		cp = cp + ".java";
    		for (String source : project.getCompileSourceRoots()) {
    			File f = new File(source + File.separatorChar + cp);
    			if (f.exists() && f.isFile()) return f;
    		}
		return null;
	}

	private ClassFinder createFinder(String classloaderType) throws Exception {
        ClassFinder finder;
        if ("project".equals(classloaderType)) {
            List<URL> urls = new ArrayList<>();

            urls.add(new File(project.getBuild().getOutputDirectory()).toURI().toURL());
            for (Artifact artifact : project.getArtifacts()) {
                if (artifactInclude != null && artifactInclude.length() > 0 && artifact.getArtifactId().matches(artifactInclude)) {
                    File file = artifact.getFile();
                    if (file != null) {
                        getLog().debug("Use artifact " + artifact.getArtifactId() + ": " + file);
                        urls.add(file.toURI().toURL());
                    }
                } else {
                    getLog().debug("Ignore artifact " + artifact.getArtifactId());
                }
            }
            loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());
            finder = new ClassFinder(loader, urls);
        } else if ("plugin".equals(classLoader)) {
            finder = new ClassFinder(getClass().getClassLoader());
        } else {
            throw new MojoFailureException("classLoader attribute must be 'project' or 'plugin'");
        }
        return finder;
    }
}
