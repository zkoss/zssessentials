package org.zkoss.zss.essential.advanced.permission;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.essential.advanced.permission.Restriction.NAME;
import org.zkoss.zss.ui.AuxAction;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.Events;
import org.zkoss.zss.ui.event.StartEditingEvent;

/**
 * Because most features are available by default except UI visibility, we assign restrictions on each role.
 */
public class AuthorityService {

	static private TreeSet<Role> roles = new TreeSet<Role>();
	 // Define roles here
	public static final Role OWNER = new Role(Role.Name.OWNER);
	public static final Role EDITOR = new Role(Role.Name.EDITOR);
	public static final Role VIEWER = new Role(Role.Name.VIEWER);

	 // Initialize role - restriction relationship
	static{
		EDITOR.assign(new Restriction(NAME.SHEET_ADD) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.ADD_SHEET, true);
			}
		});
		
		EDITOR.assign(new Restriction(NAME.SHEET_DELETE) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.DELETE_SHEET, true);
			}
		});
		
		EDITOR.assign(new Restriction(NAME.SHEET_MOVE) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.MOVE_SHEET_LEFT, true);
				ss.disableUserAction(AuxAction.MOVE_SHEET_RIGHT, true);
			}
		});
		
		EDITOR.assign(new Restriction(NAME.SHEET_HIDE) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.HIDE_SHEET, true);
				ss.disableUserAction(AuxAction.UNHIDE_SHEET, true);
			}
		});
		
		EDITOR.assign(new Restriction(NAME.SHEET_RENAME) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.RENAME_SHEET, true);
			}
		});
		
		EDITOR.assign(new Restriction(NAME.SHEET_COPY) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.COPY_SHEET, true);
			}
		});
		
		EDITOR.assign(new Restriction(NAME.SHEET_PROTECT) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.disableUserAction(AuxAction.PROTECT_SHEET, true);
			}
		});
		
		for (Restriction p : EDITOR.getRestrictions()){
			VIEWER.assign(p);
		}
		VIEWER.assign(new Restriction(NAME.TOOLBAR) {
			
			@Override
			void apply(Spreadsheet ss) {
				ss.setShowToolbar(false);
			}
		});
		VIEWER.assign(new Restriction(NAME.FORMULABAR) {

			@Override
			void apply(Spreadsheet ss) {
				ss.setShowFormulabar(false);
			}
		});
		VIEWER.assign(new Restriction(NAME.CONTEXT_MENU) {

			@Override
			void apply(Spreadsheet ss) {
				ss.setShowContextMenu(false);
			}
		});
		
		VIEWER.assign(new Restriction(NAME.EDIT) {
			
			@Override
			void apply(Spreadsheet ss) {
				String shareScope = ss.getBook().getShareScope();
				if (shareScope == null || shareScope.equals(EventQueues.DESKTOP)){
					for (int i=0 ; i < ss.getBook().getNumberOfSheets() ; i++){
						Ranges.range(ss.getBook().getSheetAt(i)).protectSheet("", 
								false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
					}
				}else{
					/*
					 * When a book is shared, protecting sheets will affect other collaborators.
					 * Need to avoid editing at the component level.
					 */
					ss.addEventListener(Events.ON_START_EDITING, new EventListener<StartEditingEvent>() {
						@Override
						public void onEvent(StartEditingEvent event) throws Exception {
							event.cancel();
							Clients.showNotification("It's read-only", Clients.NOTIFICATION_TYPE_WARNING, null,
									"middle_center", 500);
						}
					});
				}
			}
		});
		roles.add(OWNER);
		roles.add(EDITOR);
		roles.add(VIEWER);
	}
	
	static public Set<Role> getPredefinedRoles(){
		return Collections.unmodifiableSet(roles);
	}
	
	static public void applyRestriction(Spreadsheet ss, Role role){
		checkProtection(ss);
		displayAllUI(ss);
		for (Restriction p : role.getRestrictions()){
			p.apply(ss);
		}
	}

	/**
	 * Some roles has edit permission, a sheet protection conflicts it, so show a warning. 
	 * @param ss
	 */
	public static void checkProtection(Spreadsheet ss) {
		for (int i=0 ; i < ss.getBook().getNumberOfSheets() ; i++){
			Sheet sheet = ss.getBook().getSheetAt(i);
			if (Ranges.range(sheet).isProtected()){
				Clients.showNotification("Sheet "+sheet.getSheetName()+" is protected", Clients.NOTIFICATION_TYPE_WARNING, null,
						"middle_center", 500);
			}
		}
	}

	/**
	 * Since Spreadsheet all UI visibility is false by default, we should make it's visible (no restrictions) at first before applying restrictions.
	 * @param ss
	 */
	private static void displayAllUI(Spreadsheet ss) {
		ss.setShowContextMenu(true);
		ss.setShowFormulabar(true);
		ss.setShowToolbar(true);
		ss.setShowSheetbar(true);
	}
}
