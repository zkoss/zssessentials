package org.zkoss.zss.essential.advanced;

import java.io.File;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zss.api.BookSeriesBuilder;
import org.zkoss.zss.api.CellOperationUtil;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.Range;
import org.zkoss.zss.api.Ranges;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zss.api.model.Sheet;
import org.zkoss.zss.api.model.CellStyle.Alignment;
import org.zkoss.zss.ui.Spreadsheet;

/**
 * This class demonstrates performance tricks.
 * @author Hawk
 *
 */
@SuppressWarnings("serial")
public class PerformanceComposer extends SelectorComposer<Component> {
	

	@Wire
	private Spreadsheet ss;
	
	private static final int COLUMN_SIZE = 20;
	private static final int ROW_SIZE = 100;
	
	@Listen("onClick = button[label='Fill with autorefresh']")
	public void load(Event e){
		Events.echoEvent("onLoadDataAutoRefresh", ss, null);
		Clients.showBusy("populating");
	}
	
	@Listen("onLoadDataAutoRefresh= #ss")
	public void onLoadDataAutoRefresh(Event e) throws InterruptedException{
		loadDataAutoRefresh();
		Clients.clearBusy();
	}
	
	@Listen("onClick = button[label='Fill without autorefresh']")
	public void loadWithAutoRefresh(Event e){
		Events.echoEvent("onLoadData", ss, null);
		Clients.showBusy("populating");
	}
	
	@Listen("onLoadData= #ss")
	public void onLoadData(Event e) throws InterruptedException{
		loadData();
		Clients.clearBusy();
	}
	
	private void loadData() {
		Sheet sheet = ss.getSelectedSheet();
		for (int column  = 0 ; column < COLUMN_SIZE ; column++){
			for (int row = 0 ; row < ROW_SIZE ; row++ ){
				Range range = Ranges.range(sheet, row, column);
				range.setAutoRefresh(false);
				range.getCellData().setEditText(row+", "+column);
				CellOperationUtil.applyFontColor(range, "#0099FF");
				CellOperationUtil.applyAlignment(range, Alignment.CENTER);
			}
		}
		Ranges.range(ss.getSelectedSheet(), 0, 0, ROW_SIZE, COLUMN_SIZE).notifyChange();
//		Ranges.range(ss.getSelectedSheet()).notifyChange(); // might make a sheet blank for a moment
	}

	private void loadDataAutoRefresh() {
		Sheet sheet = ss.getSelectedSheet();
		for (int column  = 0 ; column < COLUMN_SIZE ; column++){
			for (int row = 0 ; row < ROW_SIZE ; row++ ){
				Range range = Ranges.range(sheet, row, column);
				range.getCellData().setEditText(row+", "+column);
				CellOperationUtil.applyFontColor(range, "#0099FF");
				CellOperationUtil.applyAlignment(range, Alignment.CENTER);
			}
		}
	}
}



