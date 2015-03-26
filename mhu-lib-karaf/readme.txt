
install -s mvn:org.codehaus.jackson/jackson-core-asl/1.9.5
install -s mvn:org.codehaus.jackson/jackson-mapper-asl/1.9.5
install -s mvn:javax.portlet/portlet-api/2.0

install -s mvn:de.mhus.lib/mhu-lib-annotations/3.2.8-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-core/3.2.8-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-persistence/3.2.8-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-logging/3.2.8-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-karaf/3.2.8-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-j2ee/3.2.8-SNAPSHOT
install -s mvn:de.mhus.lib/mhu-lib-jms/3.2.8-SNAPSHOT

bundle:watch mhu-lib-core
bundle:watch mhu-lib-annotations
bundle:watch mhu-lib-persistence
bundle:watch mhu-lib-logging
bundle:watch mhu-lib-karaf
bundle:watch mhu-lib-j2ee
bundle:watch mhu-lib-jms
