package org.zkoss.zss.essential.advanced.permission;

import org.zkoss.zss.ui.Spreadsheet;

/**
 * An approval of a mode of access to a resource.
 * 
 * @author hawk
 *
 */
abstract public class Permission implements Comparable<Permission> {
	enum NAME{TOOLBAR, FORMULABAR, CONTEXT_MENU, SHEETBAR, 
		SHEET_ADD, SHEET_DELETE, SHEET_MOVE, SHEET_RENAME, SHEET_HIDE, SHEET_COPY, SHEET_PROTECT,
		EDIT}
	
	protected NAME name;
	protected boolean approved;
	
	public NAME getName() {
		return name;
	}
	
	public boolean isApproved() {
		return approved;
	}
	
	public Permission(NAME name, boolean approved){
		this.name = name;
		this.approved = approved;
	}
	
	/**
	 * Apply this permission on the resource
	 */
	abstract void apply(Spreadsheet ss);
	
	@Override
	public int compareTo(Permission p) {
		return this.name.compareTo(p.getName());
	}
}
