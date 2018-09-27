package org.zkoss.zss.essential;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.*;
import org.zkoss.zss.api.Range.AutoFillType;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.StartEditingEvent;
import org.zkoss.zul.*;

import java.util.*;

/**
 * @author Hawk
 * 
 */
@SuppressWarnings("serial")
public class AutoCompleteComposer extends SelectorComposer<Component> {

	@Wire
	private Combobox box;
	@Wire
	private Popup inputPopup;
	@Wire
	private Spreadsheet ss;

	ListModelList<Locale> modelList = new ListModelList<Locale>(Arrays.asList(Locale.getAvailableLocales()));

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// fill the data model with data source (e.g. from a database)
		box.setModel(ListModels.toListSubModel(modelList));
	}

	@Listen("onStartEditing = #ss")
	public void showInputPopup(StartEditingEvent event) {
		if (event.getColumn() == 0) {
			event.cancel();
			inputPopup.open(ss, "at_pointer");
			box.setFocus(true);
		}
	}

	@Listen("onSelect= #box")
	public void endInput(){
		inputPopup.close();
		Ranges.range(ss.getSelectedSheet(), ss.getSelection()).setCellEditText(box.getValue());
		ss.focus();
	}


}
