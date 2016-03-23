package org.zkoss.zss.essential.advanced.customization;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.KeyEvent;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class FindComposer extends SelectorComposer<Component> {

	@Wire
	private Window findDialog;
	@Wire
	private Spreadsheet ss;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		findDialog.setAttribute(Spreadsheet.class.toString(), ss);
	}
	
	/**
	 * Listen ctrl+F
	 * @param e
	 */
	@Listen("onCtrlKey = #ss")
	public void openFindDialog(KeyEvent e){
		if ( e.isCtrlKey() && 
			(e.getKeyCode()== 70)){
			Events.postEvent(findDialog, new Event("onOpen"));
		}
	}
}



