package org.zkoss.zss.essential;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zss.api.Exporter;
import org.zkoss.zss.api.Exporters;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.essential.util.BookUtil;
import org.zkoss.zss.model.SPrintSetup;
import org.zkoss.zss.model.impl.pdf.PdfExporter;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.*;

/**
 * This class demonstrate pdf exporter API
 * @author Hawk
 *
 */
public class ExportPdfComposer extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;
	
	@Wire
	private Spreadsheet ss;
	@Wire("combobox")
	private Combobox combobox;
	@Wire
	private Radiogroup orientationRadio;
	@Wire
	private Checkbox hcenterBox;
	@Wire
	private Checkbox vcenterBox;
	
	private Exporter exporter = Exporters.getExporter("pdf");
	PdfExporter pdfExporter = new PdfExporter();
	private SPrintSetup printSetup = pdfExporter.getPrintSetup();
	
	
	@Listen("onClick = #exportPdf")
	public void doExport() throws IOException{
		getOverridenPrintSetup();
		
		Book book = ss.getBook();
		
		File file = File.createTempFile(Long.toString(System.currentTimeMillis()),"temp");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			//Exporters.getExporter("pdf").export(book, fos);
			pdfExporter.export(book.getInternalBook(), file);
		}finally{
			if(fos!=null){
				fos.close();
			}
		}
		Filedownload.save(new AMedia(book.getBookName()+".pdf", "pdf", "application/pdf", file, true));
	}

	@Listen("onSelect = combobox")
	public void switchFile(){
		String fileName = combobox.getSelectedItem().getLabel();
		ss.setSrc("/WEB-INF/books/"+fileName);
	}
	
	//FIXME want to override one print setup but will clear all setup
	private void getOverridenPrintSetup(){
		boolean isLandscape = "landscape".equalsIgnoreCase(orientationRadio.getSelectedItem().getLabel());
		printSetup.setLandscape(isLandscape);
		
		printSetup.setHCenter(hcenterBox.isChecked());
		printSetup.setVCenter(vcenterBox.isChecked());			
	}
}