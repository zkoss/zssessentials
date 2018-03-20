package org.zkoss.zss.essential.advanced.customization;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zss.api.*;
import org.zkoss.zss.api.model.*;
import org.zkoss.zss.model.*;
import org.zkoss.zss.ui.UserActionContext;
import org.zkoss.zss.ui.sys.UndoableActionManager;
import org.zkoss.zssex.ui.dialog.impl.DialogCallbackEvent;
import org.zkoss.zssex.ui.impl.ua.DataValidationHandler;
import org.zkoss.zssex.ui.impl.undo.DataValidationAction;
import org.zkoss.zul.Messagebox;

import java.util.*;

public class MyValidationHandler extends DataValidationHandler {

    @Override
    protected boolean processAction(final UserActionContext ctx) {
        final Sheet sheet = ctx.getSheet();
        final AreaRef selection = ctx.getSelection();
        Range range = Ranges.range(sheet, selection);
        final List<Validation> validations = range.getValidations();
        //ZSS-990
        validTableTotalRows(range);
        if(isOverlapped(range)) {
            String label = Labels.getLabel("zssex.dlg.format.dataValidation.overlapMsg");
            Messagebox.show(label, "ZK Spreadsheet",
                    Messagebox.OK + Messagebox.CANCEL, Messagebox.INFORMATION, new SerializableEventListener<Event>() {
                        private static final long serialVersionUID = 2936000638773070248L;

                        @Override
                        public void onEvent(Event event) throws Exception {
                            if(event.getData().equals(Messagebox.OK)) {
                                showValidationDialog(ctx, sheet, selection, validations);
                            }
                        }
                    });
        } else {
            showValidationDialog(ctx, sheet, selection, validations);
        }
        return true;
    }

    private void validTableTotalRows(Range range) {
        int row1 = range.getRow();
        int row2 = range.getLastRow();
        int col1 = range.getColumn();
        int col2 = range.getLastColumn();
        SSheet sheet = range.getSheet().getInternalSheet();
        Iterator var7 = sheet.getTables().iterator();

        while(var7.hasNext()) {
            STable tb = (STable)var7.next();
            if (tb.getTotalsRowCount() != 0) {
                CellRegion rgn = tb.getAllRegion().getRegion();
                int tc1 = rgn.getColumn();
                int tr2 = rgn.getLastRow();
                int tc2 = rgn.getLastColumn();
                if (tr2 >= row1 && tr2 <= row2 && tc2 >= col1 && tc1 <= col2) {
                    throw new InvalidModelOpException("The operation cannot be applied on Table's totals row");
                }
            }
        }

    }

    private boolean isOverlapped(Range range) {
        List<SDataValidation> validations = range.getInternalRange().getValidations();
        if (validations.size() >= 2) {
            return true;
        } else if (validations.size() != 1) {
            return false;
        } else {
            Iterator<CellRegion> validationIter = ((SDataValidation)validations.get(0)).getRegions().iterator();
            CellRegion cellRegion = new CellRegion(range.getRow(), range.getColumn(), range.getLastRow(), range.getLastColumn());
            List<CellRegion> remaining = new ArrayList(2);
            remaining.add(cellRegion);

            while(validationIter.hasNext()) {
                CellRegion validationRegion = (CellRegion)validationIter.next();
                List<CellRegion> tempRegion = new ArrayList(2);
                Iterator var8 = remaining.iterator();

                while(var8.hasNext()) {
                    CellRegion remainingRegion = (CellRegion)var8.next();
                    tempRegion.addAll(remainingRegion.diff(validationRegion.getOverlap(remainingRegion)));
                }

                remaining = tempRegion;
            }

            return remaining.size() > 0;
        }
    }

    private void showValidationDialog(final UserActionContext ctx, final Sheet sheet, final AreaRef selection, List<Validation> validations) {
        Validation validation = validations.size() > 0 ? (Validation)validations.get(0) : null;
        MyDataValidationCtrl.show(new SerializableEventListener<DialogCallbackEvent>() {
            private static final long serialVersionUID = -1058080304759009998L;

            public void onEvent(DialogCallbackEvent event) throws Exception {
                if ("onOK".equals(event.getName())) {
                    Validation.ValidationType validationType = (Validation.ValidationType)event.getData("validationTypeCode");
                    Validation.OperatorType operatorType = (Validation.OperatorType)event.getData("operatorTypeCode");
                    boolean ignoreBlank = (Boolean)event.getData("ignoreBlankCode");
                    boolean inCellDropDown = (Boolean)event.getData("inCellDropdownCode");
                    String param1 = (String)event.getData("param1Code");
                    String param2 = (String)event.getData("param2Code");
                    boolean isShowInput = (Boolean)event.getData("showInputCode");
                    String inputTitle = (String)event.getData("inputTitleCode");
                    String inputMessage = (String)event.getData("inputMessageCode");
                    boolean isShowError = (Boolean)event.getData("showErrorCode");
                    Validation.AlertStyle alertStyle = (Validation.AlertStyle)event.getData("alertStyleCode");
                    String errorTitle = (String)event.getData("errorTitleCode");
                    String errorMessage = (String)event.getData("errorMessageCode");
                    UndoableActionManager uam = ctx.getSpreadsheet().getUndoableActionManager();
                    uam.doAction(new DataValidationAction(Labels.getLabel("zss.undo.insertComment"), sheet, selection.getRow(), selection.getColumn(), selection.getLastRow(), selection.getLastColumn(), validationType, ignoreBlank, operatorType, inCellDropDown, param1, param2, isShowInput, inputTitle, inputMessage, isShowError, alertStyle, errorTitle, errorMessage));
                }

            }
        }, validation, ctx.getSpreadsheet());
    }
}
