# Const Generate Plugin

The plugin generates const classes parallel to the base class which contains a list of constantes from the
analysis of the base class.

For example if the base class contains a public field firstName then the generator will create a field FIELD_FIRST_NAME. The same for
methods. The const class contains also information about the class and maven project. See a full example at the end of this file.

* Also direct references will be generated. Also for non public elements. Which elements will be direct references can be configured.

* Direct references will start with underscore

* Elements marked with the annotation @GenerateHidden will be fully ignored

* The const class will only be written if the parameters are changed. To generate const for a class you have to mark the class with the
annotation @GenerateConst

* The new const class will be generated using a jtwig template. The default template will be delivered with the plugin.

* set -Dmaven.generate.force=true to set force to true

## Parameters

### outputDirectory

Change the output directory. By default the class will be written to the source directory beside the base class. The default prefix is a underscore.

### artifactInclude

Regex to filter the scanned artifacts for marked classes., e.g. .*mhus.*

### debug

Boolean to enable debug output.

### force

Boolean to force the plugin to write all classes again.

### prefix

Prefix of the generated new const class. Default is a underscore.

### ignore

Comma separated list of field or method names to ignore. By default

EQUALS
CLASS
CLONE
WAIT
FINALIZE
HASH_CODE
NOTIFY
NOTIFY_ALL
WRITE_EXTERNAL
READ_EXTERNAL
TO_STRING

will be ignored.

### template

Path to a alternative jtwig template. For documentation see http://jtwig.org/documentation/reference

## @GenerateConst annotation

### annotation

List of annotation to trigger direct references

### ignore

Optional additional ignore list for the class.

### restricted

Set to true if you want references also for non public elements.

## Template

### constPackage

Package of the const class

### constName

Name of const class (simple name)

### constClass

Canonicla name of the const class

### basePackage

Package of the base class

### baseName

Name of base class (simple name)

### baseClass

Canonicla name of the base class

### fields

Collection (key,value) of references to generate.

# Example

## POM Configuration

```
        <plugin>
            <groupId>de.mhus.lib</groupId>
            <artifactId>constgenerator-maven-plugin</artifactId>
            <version>${mhulib.version}</version>
            <executions>
              <execution>
                <phase>compile</phase>
                <goals>
                  <goal>const-generate</goal>
                </goals>
              </execution>
            </executions>
            <configuration>
              <force>true</force>
            </configuration>
        </plugin>
```

## Base Class

```
package de.mhus.osgi.sop.api.foundation.model;

import java.util.Date;
import java.util.UUID;

import de.mhus.lib.adb.DbMetadata;
import de.mhus.lib.adb.model.AttributeFeatureCut;
import de.mhus.lib.annotations.adb.DbIndex;
import de.mhus.lib.annotations.adb.DbPersistent;
import de.mhus.lib.annotations.adb.DbPrimaryKey;
import de.mhus.lib.annotations.generic.Public;
import de.mhus.lib.basics.consts.GenerateConst;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;
import de.mhus.osgi.sop.api.foundation.FoundationApi;
import de.mhus.osgi.sop.api.foundation.FoundationRelated;

@GenerateConst(annotation= {DbPersistent.class,DbPrimaryKey.class})
public class SopData extends DbMetadata implements FoundationRelated {
    
    @DbPersistent(ro=true)
    @DbIndex({"parent","t1","t2"})
    private UUID foundation;
    
    // REST access restriction only !!!
    @DbPersistent
    private boolean isPublic   = false;
    // REST access restriction only !!!
    @DbPersistent
    private boolean isWritable = false;
    
    @DbPersistent(ro=true)
    @Public
    @DbIndex({"t3","t1","v0","v1","v2","v3","v4","v5","v6","v7","v8","v9"})
    private String type;
    @DbPersistent
    @Public
    @DbIndex({"v0"})
    private String value0;
    @DbPersistent
    @Public
    @DbIndex({"v1"})
    private String value1;
    @DbPersistent
    @Public
    @DbIndex({"v2"})
    private String value2;
    @DbPersistent
    @Public
    @DbIndex({"v3"})
    private String value3;
    @DbPersistent
    @Public
    @DbIndex({"v4"})
    private String value4;
    @DbPersistent
    @Public
    @DbIndex({"v5"})
    private String value5;
    @DbPersistent
    @Public
    @DbIndex({"v6"})
    private String value6;
    @DbPersistent
    @Public
    @DbIndex({"v7"})
    private String value7;
    @DbPersistent
    @Public
    @DbIndex({"v8"})
    private String value8;
    @DbPersistent
    @Public
    @DbIndex({"v9"})
    private String value9;
    @DbPersistent
    @Public
    @DbIndex({"t2"})
    private Date due;
    @DbPersistent
    @Public
    @DbIndex({"t1","v0","v1","v2","v3","v4","v5","v6","v7","v8","v9"})
    private boolean archived;
    @DbPersistent
    @Public
    @DbIndex({"t4"})
    private String foreignId;
    @DbPersistent
    @Public
    private Date foreignDate;
    @DbPersistent
    @Public
    private String status;

    @DbPersistent()
    @Public
    private MProperties data;

    @DbPersistent
    private Date lastSync;

    @DbPersistent
    @Public(readable=false,writable=false)
    private Date lastSyncTry;

    @DbPersistent(size=100,features=AttributeFeatureCut.NAME)
    @Public(readable=false,writable=false)
    private String lastSyncMsg;
    
    
    public SopData() {}
    
    public SopData(SopFoundation found, String type) {
        this(found.getId(), type);
    }

    public SopData(UUID found, String type) {
        this.type = type;
        this.foundation = found;
    }

    @Override
    public UUID getFoundation() {
        return foundation;
    }

    @Override
    public DbMetadata findParentObject() throws MException {
        if (getFoundation() == null) return null;
        return MApi.lookup(FoundationApi.class).getFoundation(getFoundation());
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isIsWritable() {
        return isWritable;
    }

    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String string1) {
        this.value1 = string1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String string2) {
        this.value2 = string2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String string3) {
        this.value3 = string3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String string4) {
        this.value4 = string4;
    }

    public String getValue5() {
        return value5;
    }

    public void setValue5(String string5) {
        this.value5 = string5;
    }

    public String getType() {
        return type;
    }

    public synchronized MProperties getData() {
        if (data == null) data = new MProperties();
        return data;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastSync() {
        return lastSync;
    }

    public void setLastSync(Date lastSync) {
        this.lastSync = lastSync;
    }

    public Date getLastSyncTry() {
        return lastSyncTry;
    }

    public void setLastSyncTry(boolean save) {
        this.lastSyncTry = new Date();
        if (save && isAdbPersistent())
            try {
                save();
            } catch (MException e) {
                log().e(this,e);
            }
    }

    public String getLastSyncMsg() {
        return lastSyncMsg;
    }

    public void setLastSyncMsg(String lastSyncMsg, boolean save) {
        this.lastSyncMsg = lastSyncMsg;
        if (save && isAdbPersistent())
            try {
                save();
            } catch (MException e) {
                log().e(this,e);
            }
    }
    
    @Override
    public String toString() {
        return MSystem.toString(this, getId(), type, value0, value1, value2,value3,value4,value5,value6,value7,value8,value9,archived,foreignId,status);
    }

    public Date getForeignDate() {
        return foreignDate;
    }

    public void setForeignDate(Date foreignDate) {
        this.foreignDate = foreignDate;
    }

    public String getValue6() {
        return value6;
    }

    public void setValue6(String value6) {
        this.value6 = value6;
    }

    public String getValue7() {
        return value7;
    }

    public void setValue7(String value7) {
        this.value7 = value7;
    }

    public String getValue8() {
        return value8;
    }

    public void setValue8(String value8) {
        this.value8 = value8;
    }

    public String getValue9() {
        return value9;
    }

    public void setValue9(String value9) {
        this.value9 = value9;
    }

    public String getValue0() {
        return value0;
    }

    public void setValue0(String value0) {
        this.value0 = value0;
    }

}
```

## Const Class

```
package de.mhus.osgi.sop.api.foundation.model;

import de.mhus.lib.basics.consts.Identifier;
import de.mhus.lib.basics.consts.ConstBase;

/**
 * File created by mhu const generator. Changes will be overwritten.
 * 16.11.2018
 **/

public class _SopData extends ConstBase {

  public static final Identifier CLASS_EXTENDS = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"de.mhus.lib.adb.DbMetadata");
  public static final Identifier CLASS_NAME = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"de.mhus.osgi.sop.api.foundation.model.SopData");
  public static final Identifier CLASS_PATH = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"de.mhus.osgi.sop.api.foundation.model.SopData");
  public static final Identifier METHOD_CREATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"create");
  public static final Identifier METHOD_DELETE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"delete");
  public static final Identifier METHOD_DO_INIT = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doInit");
  public static final Identifier METHOD_DO_POST_CREATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doPostCreate");
  public static final Identifier METHOD_DO_POST_DELETE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doPostDelete");
  public static final Identifier METHOD_DO_POST_LOAD = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doPostLoad");
  public static final Identifier METHOD_DO_PRE_CREATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doPreCreate");
  public static final Identifier METHOD_DO_PRE_DELETE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doPreDelete");
  public static final Identifier METHOD_DO_PRE_SAVE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"doPreSave");
  public static final Identifier METHOD_FIND_PARENT_OBJECT = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"findParentObject");
  public static final Identifier METHOD_GET_CREATION_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getCreationDate");
  public static final Identifier METHOD_GET_DATA = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getData");
  public static final Identifier METHOD_GET_DB_HANDLER = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getDbHandler");
  public static final Identifier METHOD_GET_DUE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getDue");
  public static final Identifier METHOD_GET_FOREIGN_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getForeignDate");
  public static final Identifier METHOD_GET_FOREIGN_ID = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getForeignId");
  public static final Identifier METHOD_GET_FOUNDATION = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getFoundation");
  public static final Identifier METHOD_GET_ID = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getId");
  public static final Identifier METHOD_GET_LAST_SYNC = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getLastSync");
  public static final Identifier METHOD_GET_LAST_SYNC_MSG = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getLastSyncMsg");
  public static final Identifier METHOD_GET_LAST_SYNC_TRY = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getLastSyncTry");
  public static final Identifier METHOD_GET_MODIFY_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getModifyDate");
  public static final Identifier METHOD_GET_STATUS = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getStatus");
  public static final Identifier METHOD_GET_TYPE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getType");
  public static final Identifier METHOD_GET_VALUE0 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue0");
  public static final Identifier METHOD_GET_VALUE1 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue1");
  public static final Identifier METHOD_GET_VALUE2 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue2");
  public static final Identifier METHOD_GET_VALUE3 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue3");
  public static final Identifier METHOD_GET_VALUE4 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue4");
  public static final Identifier METHOD_GET_VALUE5 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue5");
  public static final Identifier METHOD_GET_VALUE6 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue6");
  public static final Identifier METHOD_GET_VALUE7 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue7");
  public static final Identifier METHOD_GET_VALUE8 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue8");
  public static final Identifier METHOD_GET_VALUE9 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getValue9");
  public static final Identifier METHOD_GET_VSTAMP = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"getVstamp");
  public static final Identifier METHOD_IS_ADB_CHANGED = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isAdbChanged");
  public static final Identifier METHOD_IS_ADB_MANAGED = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isAdbManaged");
  public static final Identifier METHOD_IS_ADB_PERSISTENT = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isAdbPersistent");
  public static final Identifier METHOD_IS_ARCHIVED = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isArchived");
  public static final Identifier METHOD_IS_IS_PUBLIC = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isIsPublic");
  public static final Identifier METHOD_IS_IS_WRITABLE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isIsWritable");
  public static final Identifier METHOD_LOG = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"log");
  public static final Identifier METHOD_RELOAD = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"reload");
  public static final Identifier METHOD_SAVE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"save");
  public static final Identifier METHOD_SAVE_CHANGED = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"saveChanged");
  public static final Identifier METHOD_SET_ARCHIVED = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setArchived");
  public static final Identifier METHOD_SET_DB_HANDLER = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setDbHandler");
  public static final Identifier METHOD_SET_DUE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setDue");
  public static final Identifier METHOD_SET_FOREIGN_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setForeignDate");
  public static final Identifier METHOD_SET_FOREIGN_ID = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setForeignId");
  public static final Identifier METHOD_SET_LAST_SYNC = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setLastSync");
  public static final Identifier METHOD_SET_LAST_SYNC_MSG = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setLastSyncMsg");
  public static final Identifier METHOD_SET_LAST_SYNC_TRY = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setLastSyncTry");
  public static final Identifier METHOD_SET_PUBLIC = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setPublic");
  public static final Identifier METHOD_SET_STATUS = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setStatus");
  public static final Identifier METHOD_SET_VALUE0 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue0");
  public static final Identifier METHOD_SET_VALUE1 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue1");
  public static final Identifier METHOD_SET_VALUE2 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue2");
  public static final Identifier METHOD_SET_VALUE3 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue3");
  public static final Identifier METHOD_SET_VALUE4 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue4");
  public static final Identifier METHOD_SET_VALUE5 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue5");
  public static final Identifier METHOD_SET_VALUE6 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue6");
  public static final Identifier METHOD_SET_VALUE7 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue7");
  public static final Identifier METHOD_SET_VALUE8 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue8");
  public static final Identifier METHOD_SET_VALUE9 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setValue9");
  public static final Identifier METHOD_SET_WRITABLE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"setWritable");
  public static final Identifier PROJECT_ARTIFACT = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"mhu-sop-api");
  public static final Identifier PROJECT_DESCRIPTION = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"A set of tools to improve OSGi frameworks and karaf");
  public static final Identifier PROJECT_GROUP = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"de.mhus.osgi");
  public static final Identifier PROJECT_VERSION = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"1.3.4-SNAPSHOT");
  public static final Identifier _ARCHIVED = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"archived");
  public static final Identifier _CREATION_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"creationDate");
  public static final Identifier _DATA = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"data");
  public static final Identifier _DUE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"due");
  public static final Identifier _FOREIGN_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"foreignDate");
  public static final Identifier _FOREIGN_ID = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"foreignId");
  public static final Identifier _FOUNDATION = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"foundation");
  public static final Identifier _ID = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"id");
  public static final Identifier _IS_PUBLIC = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isPublic");
  public static final Identifier _IS_WRITABLE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"isWritable");
  public static final Identifier _LAST_SYNC = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"lastSync");
  public static final Identifier _LAST_SYNC_MSG = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"lastSyncMsg");
  public static final Identifier _LAST_SYNC_TRY = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"lastSyncTry");
  public static final Identifier _MODIFY_DATE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"modifyDate");
  public static final Identifier _STATUS = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"status");
  public static final Identifier _TYPE = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"type");
  public static final Identifier _VALUE0 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value0");
  public static final Identifier _VALUE1 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value1");
  public static final Identifier _VALUE2 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value2");
  public static final Identifier _VALUE3 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value3");
  public static final Identifier _VALUE4 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value4");
  public static final Identifier _VALUE5 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value5");
  public static final Identifier _VALUE6 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value6");
  public static final Identifier _VALUE7 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value7");
  public static final Identifier _VALUE8 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value8");
  public static final Identifier _VALUE9 = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"value9");
  public static final Identifier _VSTAMP = new Identifier(de.mhus.osgi.sop.api.foundation.model.SopData.class,"vstamp");

}
```

## Template

template.jtwig

```
package {{ constPackage }};

import de.mhus.lib.basics.consts.Identifier;
import de.mhus.lib.basics.consts.ConstBase;

/**
 * File created by mhu const generator. Changes will be overwritten.
 * {{ "now"|date("d.M.Y") }}
 **/

public class {{ constName }} extends ConstBase {
{% for name,value in fields %}
  public static final Identifier {{ name }} = new Identifier({{ baseClass }}.class,"{{ value }}");
{%- endfor %}

}
```
