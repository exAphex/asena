sap.ui.define([
    "controller/core/BaseController"
], function (Controller) {
    "use strict"; 
    return Controller.extend("com.asena.ui5.controller.views.Applications", {

        onInit: function () {
            var target = sap.ui.core.UIComponent.getRouterFor(this).getTarget("Applications");
            target.attachDisplay(this._onDisplay, this);
            sap.ui.core.UIComponent.getRouterFor(this).getRoute("applications").attachPatternMatched(this._onObjectMatched, this);
        },

        _onObjectMatched: function(oEvent) {
            var searchQuery = oEvent.getParameter("arguments").searchString;
            this.loadProjects(searchQuery);
    	},

        _onDisplay: function (oEvent) {
            var mainModel = sap.ui.getCore().getModel("mainModel");
            mainModel.setProperty("/showNavButton", false);
        },

        filterTiles: function(arrData, sQuery) {
            var arrRet = [];
            if (!sQuery) {
                return arrData;
            } else {
                sQuery = sQuery.toUpperCase();
            }

            for (var i = 0; i < arrData.length; i++) {
                var tempObj = arrData[i];
                var posName = (tempObj.projectName ? tempObj.projectName.toUpperCase().includes(sQuery) : false);
                var posDescription = (tempObj.projectDescription ? tempObj.projectDescription.toUpperCase().includes(sQuery) : false);
                if (posName || posDescription) {
                    arrRet.push(tempObj);
                }
            }

            return arrRet;
        },

        loadProjects: function(searchQuery) {
            var sQuery = "/api/v1/project";
            var mParameters = {
                bShowBusyIndicator: true
            };
            this.loadJsonWithAjaxP(sQuery, mParameters)
                .then(function (oData) {
                    this.createProjectTiles(oData, searchQuery);
                    //var oMainModel = new JSONModel(oData);
                    //this.getView().setModel(oMainModel);
                }.bind(this))
                .catch(function (oError) {
                    this.messageBoxGenerator("Status Code: "+oError.status+ " \n Error Message: "+ JSON.stringify(oError.responseJSON), false);
                }.bind(this));
        },

        createProjectTiles: function(projects, searchQuery) {
            var hv = this.getView().byId("dispTiles");
            var hlSearchTiles = this.getView().byId("hlSearchTiles");
            var secSearchTiles = this.getView().byId("searchTiles");
            var avTiles = this.getView().byId("avTiles");
            var isSearch = (searchQuery && searchQuery != "");

            hv.removeAllContent();
            hlSearchTiles.removeAllContent();

            if (isSearch) {
                avTiles.setVisible(false);
                secSearchTiles.setVisible(true); 
            } else {
                avTiles.setVisible(true);
                secSearchTiles.setVisible(false);
            }

            projects = this.filterTiles(projects, searchQuery);
            for (var i = 0; i < projects.length; i++) {
                var tileData = projects[i];
                var tempTile = new sap.m.GenericTile({
                    header: tileData.projectName,
                    subheader: tileData.projectDescription,
                    tooltip: tileData.projectDescription,
                    press: function(oEvent){
                        this._onProjectPressed(oEvent);
                    }.bind(this)
                });
                tempTile.addStyleClass("sapUiTinyMarginBegin");
                tempTile.addStyleClass("sapUiTinyMarginTop");
                tempTile.addStyleClass("sapUiTinyMarginBottom");
                tempTile.addStyleClass("tileLayout");
                tempTile.data("route", tileData.projectId + "");
                if (isSearch) {
                    hlSearchTiles.addContent(tempTile);
                } else {
                    hv.addContent(tempTile);
                }
                
            }
            
        },

        _onProjectPressed: function(oEvent) {
            var projectId = oEvent.getSource().data("route");
            sap.ui.core.UIComponent.getRouterFor(this).navTo("project", {
                    app: "project",
                    projectId: projectId + "" 
            }, false);
        }

    });
});
