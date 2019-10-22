package org.zkoss.zss.essential.advanced;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
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

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

	}

}



