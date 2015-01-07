package de.mhus.lib.persistence.aaa;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbAccess {
	String attribute() default "acl"; // name of the acl id field
	String original() default "acloriginal"; // name of the field with type RelAcl to store the acl not changeable
	String owner() default ""; // field name of an owner
	String worldAccess() default ""; // set this to a right if the wold should acces is by this way, e.g. "[read]"
	Class<?> ownerType() default Class.class; // the type of an owner, if owner() is specified
	String parent() default "parentid"; // field name with the id of the parent. Ignored if empty
	Class<?> parentType() default Class.class; // the type of the parent, the same as the entity itself if not specified
	String childAcl() default "acl"; // if this entity is used as parent and a new child will be created this define the field where to get the creation acl
}
