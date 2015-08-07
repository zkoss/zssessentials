package org.zkoss.zss.essential.advanced.permission;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.essential.advanced.permission.Permission.NAME;
import org.zkoss.zss.ui.AuxAction;
import org.zkoss.zss.ui.Spreadsheet;

/**
 * All permissions are approved by default. We need to specify which is disapproved for each role.
 */
public class AuthorityService {

	static private Map<Role.Name, Role> roles = new HashMap<Role.Name, Role>();
	
	static{
		Role owner = new Role(Role.Name.OWNER);
		Role editor = new Role(Role.Name.EDITOR);
		Role viewer = new Role(Role.Name.VIEWER);
		editor.assign(new Permission(NAME.SHEET_ADD, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.ADD_SHEET, !approved);
			}
		});
		
		editor.assign(new Permission(NAME.SHEET_DELETE, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.DELETE_SHEET, !approved);
			}
		});
		
		editor.assign(new Permission(NAME.SHEET_MOVE, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.MOVE_SHEET_LEFT, !approved);
				ss.disableUserAction(AuxAction.MOVE_SHEET_RIGHT, !approved);
			}
		});
		
		editor.assign(new Permission(NAME.SHEET_HIDE, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.HIDE_SHEET, !approved);
				ss.disableUserAction(AuxAction.UNHIDE_SHEET, !approved);
			}
		});
		
		editor.assign(new Permission(NAME.SHEET_RENAME, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.RENAME_SHEET, !approved);
			}
		});
		
		editor.assign(new Permission(NAME.SHEET_COPY, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.COPY_SHEET, !approved);
			}
		});
		
		editor.assign(new Permission(NAME.SHEET_PROTECT, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.PROTECT_SHEET, !approved);
			}
		});
		
		viewer.assign(new Permission(NAME.TOOLBAR, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.setShowToolbar(approved);
			}
		});
		viewer.assign(new Permission(NAME.FORMULABAR, false) {

			@Override
			void apply(Spreadsheet ss) {
				ss.setShowFormulabar(approved);
			}
		});
		viewer.assign(new Permission(NAME.CONTEXT_MENU, false) {

			@Override
			void apply(Spreadsheet ss) {
				ss.setShowContextMenu(approved);
			}
		});
		viewer.assign(new Permission(NAME.SHEETBAR, false) {

			@Override
			void apply(Spreadsheet ss) {
				ss.setShowSheetbar(approved);
			}
		});
		
		viewer.assign(new Permission(NAME.EDIT, false) {
			
			@Override
			void apply(Spreadsheet ss) {
				Ranges.range(ss.getSelectedSheet()).protectSheet("", 
					approved, approved, approved, approved, approved, approved, approved, approved, approved, approved, approved, approved, approved, approved, approved);
			}
		});
		roles.put(Role.Name.OWNER, owner);
		roles.put(Role.Name.EDITOR, editor);
		roles.put(Role.Name.VIEWER, viewer);
	}
	
	static public List<Role.Name> getRoles(){
		return Arrays.asList(Role.Name.values());
	}
	
	static public void applyPermission(Spreadsheet ss, Role.Name name){
		Role role = roles.get(name);
		for (Permission p : role.getPermissions()){
			p.apply(ss);
		}
	}
}
