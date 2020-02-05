package org.zkoss.zss.essential;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.essential.util.*;
import org.zkoss.zss.model.SName;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.Events;

/**
 * @author Hawk
 */
public class CopySheetComposer extends SelectorComposer<Component> {

	@Wire
	private Spreadsheet srcSpreadsheet;
	@Wire
	private Spreadsheet targetSpreadsheet;
	
	@Listen("onClick = #copy2new")
	public void copySheetToNewBook(){
		Book newBook = BookUtil.copySheetToNewBook("newOne", srcSpreadsheet.getSelectedSheet());
		targetSpreadsheet.setBook(newBook);
	}

	@Listen("onClick = #copy")
	public void copySheet(){
		SheetCopier.clone("cloned", srcSpreadsheet.getSelectedSheet());
	}

	@Listen(Events.ON_CELL_CLICK + " = #list")
	public void listNames(){
		for (SName n : srcSpreadsheet.getBook().getInternalBook().getNames()) {
			System.out.printf("%s %s %s\n", n.getName() , n.getApplyToSheetName(), n.getRefersToFormula());
		}
	}
}
