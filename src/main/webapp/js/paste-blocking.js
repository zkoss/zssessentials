/**
 * Based on ZSS 3.9.5
 * Purpose: if the number of rows to paste exceeds the preloadRowSize, send an event to the server-side. otherwise, proceed to paste.
 */
zk.afterLoad('zss', function() {
    var exWidget = {};

    zk.override(zss.SSheetCtrl.prototype, exWidget, {
    	pasteToSheet: function () {
    	    var focustag = this.dp.getInputNode();
        	var	valueToPaste = jq(focustag).val();
            var rows = valueToPaste.split('\n');

            if (this._wgt._preloadRowSize > 0 //-1 is unset
                && rows.length > this._wgt._preloadRowSize){
                // no client cache, notify the server side to handle
                this._wgt.fire('onPasteOverCache',{token: "", sheetId: this.serverSheetId}, {toServer: true}, 25);
                return;
            }
            exWidget.pasteToSheet.apply(this, arguments);
    	},
    });
});