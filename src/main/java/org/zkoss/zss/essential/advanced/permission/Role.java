package org.zkoss.zss.essential.advanced.permission;

import java.util.Set;
import java.util.TreeSet;

/**
 * Role is a job function or title which defines an authority level.
 * @author hawk
 *
 */
public class Role {

	public enum Name {OWNER, EDITOR, VIEWER};
	
	private Name name;
	private Set<Permission> permissions = new TreeSet<Permission>();
	
	public Role(Name name){
		this.name = name;
	}
	
	public void assign(Permission p){
		permissions.add(p);
	}

	public Name getName() {
		return name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}
	
	
}
