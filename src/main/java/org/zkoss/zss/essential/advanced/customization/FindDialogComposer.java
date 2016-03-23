package org.zkoss.zss.essential.advanced.customization;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.CellData.CellType;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class FindDialogComposer extends SelectorComposer<Component> {
	@Wire
	private Window findDialog;
	@Wire
	private Textbox keywordBox;
	
	@Listen("onClick=#find; onOK=#keywordBox")
	public void find(Event event) {
		Spreadsheet ss = (Spreadsheet)findDialog.getAttribute(Spreadsheet.class.toString());
		Sheet currentSheet = ss.getSelectedSheet();
		int lastColumn = Ranges.range(currentSheet).getDataRegion().getLastColumn();
		int lastRow = Ranges.range(currentSheet).getDataRegion().getLastRow();
		String keyword = keywordBox.getValue().trim();
		//starting from current selection position
		int row = ss.getSelection().getRow();
		int column = ss.getSelection().getColumn()+1; 
		// by rows
		while (row <= lastRow){
			while (column <= lastColumn){
				Range cell = Ranges.range(currentSheet, row, column);
				if (cell.getCellData().getType() == CellType.STRING){
					if (cell.getCellData().getEditText().toLowerCase().contains(keyword)){
						ss.focusTo(row, column);
						return;
					}
				}
				column++;
			}
			column = 0;
			row++;
		}
	}
	
	@Listen("onClick = #close")
	public void close(MouseEvent event) {
		findDialog.setVisible(false);
	}
	
	@Listen("onOpen = #findDialog")
	public void show(){
		findDialog.setVisible(true);
		keywordBox.setFocus(true);
	}
}



