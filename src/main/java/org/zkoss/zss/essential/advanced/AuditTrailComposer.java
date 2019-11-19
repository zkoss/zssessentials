package org.zkoss.zss.essential.advanced;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.*;
import org.zkoss.zul.Popup;

import java.util.*;

/**
 * An example to implement audit trail.
 * @author Hawk
 */
public class AuditTrailComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	@Wire
	private Spreadsheet ss;

	@Listen(Events.ON_AFTER_UNDOABLE_MANAGER_ACTION + "= #ss")
	public void record(UndoableActionManagerEvent event){
		//combine a user info with an action as an audit trail
		System.out.println(event.getAction() + "," + event.getType());
	}
}



