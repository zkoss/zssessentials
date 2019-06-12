package org.zkoss.zss.essential.advanced;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.*;
import org.zkoss.zss.api.*;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zss.ui.event.CellMouseEvent;
import org.zkoss.zul.Popup;

import java.util.*;

/**
 * @author Hawk
 */
public class GroupingComposer extends SelectorComposer<Component> {

    private static final long serialVersionUID = 1L;
    final static private String PLUS = "➕";
    final static private String MINUS = "➖";
    int numRows = 4;

    @Wire
    private Spreadsheet ss;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    /**
     * if a grouping symbol + clicked
     * show hidden rows
     * else if a grouping symbol - clicked
     * hide rows
     */
    @Listen("onCellClick = #ss")
    public void toggleGrouping(CellMouseEvent e) {
        Range symbolCell = Ranges.range(e.getSheet(), e.getRow(), e.getColumn());
        String value = symbolCell.getCellData().getStringValue();
        if (!isGroupingSymbol(value)) {
            return;
        }

        toggleGroupingState(symbolCell);
        if (isOpen(value)) {
            closeGroup(symbolCell);
        } else {
            openGroup(symbolCell);
        }

    }

    private void toggleGroupingState(Range symbolCell) {
        if (symbolCell.getCellData().getStringValue().equals(PLUS)) {
            symbolCell.setCellValue(MINUS);
        } else {
            symbolCell.setCellValue(PLUS);
        }
    }

    private void openGroup(Range symbolCell) {
        Ranges.range(symbolCell.getSheet(), symbolCell.getRow()+1, symbolCell.getColumn(), symbolCell.getRow() + numRows, symbolCell.getColumn()).toRowRange().setHidden(false);
    }

    private void closeGroup(Range symbolCell) {
        Ranges.range(symbolCell.getSheet(), symbolCell.getRow()+1, symbolCell.getColumn(), symbolCell.getRow() + numRows, symbolCell.getColumn()).toRowRange().setHidden(true);
    }

    private boolean isGroupingSymbol(String value) {
        return value.equals(PLUS) || value.equals(MINUS);
    }

    private boolean isClosed(String value) {
        return value.equals(PLUS);
    }

    private boolean isOpen(String value) {
        return value.equals(MINUS);
    }
}



