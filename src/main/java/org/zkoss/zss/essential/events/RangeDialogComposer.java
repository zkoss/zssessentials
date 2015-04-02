package org.zkoss.zss.essential.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.*;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellSelectionEvent;
import org.zkoss.zul.*;

/**
 * An example of CellSelectionEvent.
 * @author Hawk
 *
 */
public class RangeDialogComposer extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Spreadsheet ss;
	@Wire
	private Window dialog;
	
	private CellSelectionListener cellSelectionListener = new CellSelectionListener();
	
	
	@Listen("onClick = #open")
	public void open(){
		dialog.setVisible(true);
		ss.addEventListener("onCellSelection", cellSelectionListener);
	}
	
	@Listen("onCellSelection = #dialog")
	public void onCellSelection(CellSelectionEvent event){
		Textbox rangeBox  = (Textbox)dialog.getFellow("rangeBox");
		Range selection =Ranges.range(event.getSheet(), event.getArea()); 
		if (selection.isWholeRow()){
			rangeBox.setValue(Ranges.getRowRefString(event.getRow()));
		}else if (selection.isWholeColumn()){
			rangeBox.setValue(Ranges.getColumnRefString(event.getColumn()));
		}else{
			rangeBox.setValue(Ranges.getAreaRefString(event.getSheet(), event.getArea()));
		}
		
				
	}
	
	@Listen("onOpen = #dialog")
	public void openDialog(Event event){
		Textbox rangeBox  = (Textbox)dialog.getFellow("rangeBox");
		rangeBox.setValue("");
	}
	
	@Listen("onClose = #dialog")
	public void hideDialog(Event event){
		dialog.setVisible(false);
		event.stopPropagation();
		ss.removeEventListener("onCellSelection", cellSelectionListener); //reduce traffic to a server
	}
	
	class CellSelectionListener implements EventListener<CellSelectionEvent>{
		@Override
		public void onEvent(CellSelectionEvent event) throws Exception {
			Events.postEvent(dialog, event);
		}
	}
}



