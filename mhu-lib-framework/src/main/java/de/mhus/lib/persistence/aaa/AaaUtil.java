package de.mhus.lib.persistence.aaa;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import de.mhus.lib.adb.DbManager;


public class AaaUtil {

	public static void findObjectTypes(List<Class<?>> list) {
		list.add(Acl.class);
		list.add(Subject.class);
		list.add(AclToSubject.class);
		list.add(SubjectToSubject.class);
	}

	public static List<Subject> findAffectedSubjects(DbManager manager,
			Subject user) throws Exception {
		
		LinkedList<Subject> list = new LinkedList<Subject>();
		list.add(user);
		addParents(user, list);
		return list;
	}
	
	private static void addParents(Subject child, List<Subject> list) throws Exception {
		for (SubjectToSubject parent : child.getParents().getRelations()) {
			Subject nextSubject = parent.getParent().getRelation();
			if (!list.contains(nextSubject)) {
				list.add(nextSubject);
				addParents(nextSubject, list);
			}
		}
	}

	public static String findPolicyForSubject(DbManager manager,
			Subject user, Acl acl) throws Exception {
		
		List<Subject> list = user.getAffectedSubjects(manager);
		
		for (AclToSubject rule : acl.getRules().getRelations()) {
			UUID subjectId = rule.getSubjectId();
			if (contains(list,subjectId)) {
				return rule.getPolicy();
			}
		}
		
		return acl.getDefaultPolicy();
	}

	public static boolean contains(List<Subject> list, UUID subjectId) {
		for (Subject s : list)
			if (s.equals(subjectId)) return true;
		return false;
	}

	public static boolean hasRight(String right, String policy) {
		return policy.indexOf("[" + right + "]") >= 0;
	}

	public static String createPolicy(String ... rights) {
		StringBuffer out = new StringBuffer();
		for (String right : rights)
			out.append('[').append(right).append(']');
		return out.toString();
	}
	
}
