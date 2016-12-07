package org.zkoss.zss.essential.advanced;

import java.io.File;
import java.io.IOException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zss.api.CellOperationUtil;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.PostImport;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.CellStyle.Alignment;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Checkbox;

/**
 * This class demonstrate @PostImport.
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class PostImportComposer extends SelectorComposer<Component> implements PostImport{

	@Wire
	private Spreadsheet ss;
	@Wire("checkbox")
	private Checkbox postImportingBox;
	private String src = "/WEB-INF/books/blank.xlsx";
	private final File FILE = new File(WebApps.getCurrent().getRealPath(src));
	private Importer importer = Importers.getImporter();
	private String POST_IMPORT_KEY = "post-import";

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		long start = System.currentTimeMillis();
		if (isPostImported()){
			loadWithPostImporting();
		}else{
			loadDirectly();
		}
		long end = System.currentTimeMillis();
		postImportingBox.setChecked(isPostImported());
		Clients.showNotification("consumed (ms):"+(end-start));
	}

	@Override
	public void process(Book book) {
		initializeMassiveData(book.getSheetAt(0));
	}

	/**
	 * Increase row and column here, you will see bigger time difference between post importing and non-post importing. 
	 * @param sheet
	 */
	private void initializeMassiveData(Sheet sheet){
		for (int row = 0 ; row < 500 ; row++){
			for (int col = 0 ; col < 50 ; col++){
				Range range = Ranges.range(sheet, row, col);
				range.setCellEditText("=sum("+row+"+"+col+")");
				CellOperationUtil.applyFontColor(range, "#"+String.format("%06x",row+col));
				CellOperationUtil.applyAlignment(range, Alignment.RIGHT);
				CellOperationUtil.applyBackColor(range, "#808080");
			}
		}
	}
	
	private boolean isPostImported() {
		return Sessions.getCurrent().getAttribute(POST_IMPORT_KEY) != null;
	}
	
	private void loadWithPostImporting() throws IOException{
		Book book = importer.imports(FILE, "blank", this);
		ss.setBook(book);
	}
	
	private void loadDirectly() throws IOException{
		Book book = importer.imports(FILE, "blank");
		ss.setBook(book);
		initializeMassiveData(ss.getSelectedSheet());
	}

	@Listen("onCheck = checkbox")
	public void togglePostImporting(){
		if (isPostImported()){
			Sessions.getCurrent().removeAttribute(POST_IMPORT_KEY);
		}else{
			Sessions.getCurrent().setAttribute(POST_IMPORT_KEY, "true");
		}
	}
}



