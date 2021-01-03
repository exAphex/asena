sap.ui.define([ 
	"sap/ui/model/json/JSONModel",
	"sap/ui/core/UIComponent"
], function(JSONModel, UIComponent) {
	"use strict";
	return UIComponent.extend("com.asena.ui5.Component", {
		metadata: {
			manifest: "json"
		},
		init : function() {
			
			
			Promise.prototype.error = Promise.prototype['catch'];
			UIComponent.prototype.init.apply(this, arguments);			
			this.getRouter().initialize();

			
		}
	});
});