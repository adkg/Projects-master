var fb_editor={dismissActiveTrigger:false,JSONParser:JSON||{},initEvents:function(){appVar.bringToFront();fb_container.init();fb_toolbox.init();fb_panel.init();fb_undo.init();if(appVar.platform()=="win"){$("#css_ui").attr("href","editordata/css/win_ui.css");$("#colorcss").attr("href","editordata/css/win_colorpicker.css");document.getElementsByTagName("body")[0].onselectstart=function(f){if(((event.srcElement.tagName!="INPUT")&&(event.srcElement.tagName!="TEXTAREA"))||((event.srcElement.tagName=="INPUT")&&event.srcElement.getAttribute("disabled"))){return false}};$("#elements").attr("hideFocus","hidefocus");$("#objectproperties").attr("hideFocus","hidefocus");$("#formproperties").attr("hideFocus","hidefocus");if(fb_translate.get_language()=="es"){$("#css_ui").after('<link id="css_ui_fix" rel="stylesheet" type="text/css" href="editordata/css/es/win_ui_fixes.css"/>')}if(fb_translate.get_language()=="de"){$("#css_ui").after('<link id="css_ui_fix" rel="stylesheet" type="text/css" href="editordata/css/de/win_ui_fixes.css"/>')}}else{$("#css_ui").attr("href","editordata/css/mac_ui.css");if(fb_translate.get_language()=="es"){$("#css_ui").after('<link id="css_ui_fix" rel="stylesheet" type="text/css" href="editordata/css/es/mac_ui_fixes.css"/>')}if(fb_translate.get_language()=="de"){$("#css_ui").after('<link id="css_ui_fix" rel="stylesheet" type="text/css" href="editordata/css/de/mac_ui_fixes.css"/>')}$("#colorcss").attr("href","editordata/css/mac_colorpicker.css")}$("#elements").click(function(){$(".current").removeClass("current");$("#tb_form_tools").addClass("current");$("#elements").addClass("current")});$("#objectproperties").click(function(){$(".current").removeClass("current");$("#tb_control_properties").addClass("current");$("#objectproperties").addClass("current");if(fb_container.itemSelected){fb_container.itemSelected.trigger("mousedown")}});$("#formproperties").click(function(){$(".current").removeClass("current");$("#tb_form_properties").addClass("current");$("#formproperties").addClass("current");fb_views.form()});$("#tb_form_tools").addClass("current");$("#elements").addClass("current");$("#tool_item_delete").click(function(){if(fb_editor.isSelectedItem()){fb_clipboard.doDelete()}else{appVar.displayAlert_(_("Please select an element to delete."))}});$("#tool_item_duplicate").click(function(){if(fb_editor.isSelectedItem()){var e=fb_clipboard.doCopy();fb_clipboard.doPaste(e)}else{appVar.displayAlert_(_("Please select an element to duplicate."))}});$(".fb-item, #fb-form-header1, #fb-submit-button-div, #fb-captcha_control").live("dblclick",function(){$("#objectproperties").trigger("click");setTimeout(function(){$("#control_properties_set input, #control_properties_set textarea").first().focus()},50);fb_panel.resetCursorPosition($("#control_properties_set input, #control_properties_set textarea").first().get(0),100);return false});$("#docContainer").live("dblclick",function(){$("#formproperties").trigger("click");return false});$("#docContainer").click(function(e){if(fb_container.itemSelected){fb_container.itemSelected.trigger("mousedown")}});$("body").keydown(function(i){if($(i.srcElement).is("input, textarea, select")&&!$(i.srcElement).is("#fb-submit-button")){return}if(!fb_container.itemSelected){return}if(fb_container.itemSelected.is("#fb-submit-button")&&(i.keyCode=="37"||i.keyCode=="38")){$("#fb-submit-button-div").trigger("mousedown");return false}if(fb_container.itemSelected.is("#fb-form-header1")&&(i.keyCode=="37"||i.keyCode=="38")){if($("#fb-logo1").css("display")!="none"){$("#fb-link-logo1").trigger("mousedown")}return false}if(fb_container.itemSelected.is("#fb-link-logo1")&&(i.keyCode=="37"||i.keyCode=="38")){return false}if(fb_container.itemSelected.is("#fb-submit-button-div")&&(i.keyCode=="37"||i.keyCode=="38")){if($(".fb-item").size()==0){$("#fb-form-header1").trigger("mousedown")}else{if($("#fb-captcha_control").css("display")!="none"){$("#fb-captcha_control").trigger("mousedown")}else{$(".fb-item").last().trigger("mousedown")}}return false}if(fb_container.itemSelected.is("#fb-captcha_control")&&(i.keyCode=="37"||i.keyCode=="38")){if($(".fb-item").size()==0){$("#fb-form-header1").trigger("mousedown")}else{$(".fb-item").last().trigger("mousedown")}return false}if(fb_container.itemSelected.is("#fb-captcha_control")&&(i.keyCode=="39"||i.keyCode=="40")){$("#fb-submit-button-div").trigger("mousedown");return false}if(fb_container.itemSelected.is("#fb-submit-button")&&(i.keyCode=="39"||i.keyCode=="40")){return false}if(fb_container.itemSelected.is("#fb-form-header1")&&(i.keyCode=="39"||i.keyCode=="40")){if($(".fb-item").size()==0){if($("#fb-captcha_control").css("display")!="none"){$("#fb-captcha_control").trigger("mousedown")}else{$("#fb-submit-button-div").trigger("mousedown")}}else{$(".fb-item").first().trigger("mousedown")}return false}if(fb_container.itemSelected.is("#fb-link-logo1")&&(i.keyCode=="39"||i.keyCode=="40")){$("#fb-form-header1").trigger("mousedown");return false}if(fb_container.itemSelected.is("#fb-submit-button-div")&&(i.keyCode=="39"||i.keyCode=="40")){$("#fb-submit-button").trigger("mousedown");return false}if(i.keyCode=="37"){var h=fb_container.itemSelected[0].previousSibling;if(h){$(h).trigger("mousedown")}else{$("#fb-form-header1").trigger("mousedown")}return false}if(i.keyCode=="38"){var e=fb_container.itemSelected[0].previousSibling;if(e){var l=fb_container.itemSelected[0].offsetTop;var g=fb_container.itemSelected[0].offsetLeft;var k=false;while(e){if((e.offsetTop<l)&&(g>=e.offsetLeft)){if(g==e.offsetLeft){$(e).trigger("mousedown");k=true;break}else{if(e.previousSibling&&(e.previousSibling.offsetLeft<g)&&(e.offsetTop==e.previousSibling.offsetTop)){$(e).trigger("mousedown");k=true;break}else{if((e.previousSibling&&(e.offsetTop!=e.previousSibling.offsetTop))||(!e.previousSibling)){$(e).trigger("mousedown");k=true;break}}e=e.previousSibling}}else{e=e.previousSibling}}if(k==false){$("#fb-form-header1").trigger("mousedown")}}else{$("#fb-form-header1").trigger("mousedown")}return false}if(i.keyCode=="39"){var j=fb_container.itemSelected[0].nextSibling;if(j){$(j).trigger("mousedown")}else{if($("#fb-captcha_control").css("display")!="none"){$("#fb-captcha_control").trigger("mousedown")}else{$("#fb-submit-button-div").trigger("mousedown")}}return false}if(i.keyCode=="40"){var f=fb_container.itemSelected[0].nextSibling;if(f){var l=fb_container.itemSelected[0].offsetTop;var g=fb_container.itemSelected[0].offsetLeft;var k=false;while(f){if((f.offsetTop>l)&&(g>=f.offsetLeft)){if(g==f.offsetLeft){$(f).trigger("mousedown");k=true;break}else{if(f.nextSibling&&(f.nextSibling.offsetLeft>g)&&(f.offsetTop==f.nextSibling.offsetTop)){$(f).trigger("mousedown");k=true;break}else{if((f.nextSibling&&(f.offsetTop!=f.nextSibling.offsetTop))||(!f.nextSibling)){$(f).trigger("mousedown");k=true;break}}f=f.nextSibling}}else{f=f.nextSibling}}if(k==false){if($("#fb-captcha_control").css("display")!="none"){$("#fb-captcha_control").trigger("mousedown")}else{$("#fb-submit-button-div").trigger("mousedown")}}}else{if($("#fb-captcha_control").css("display")!="none"){$("#fb-captcha_control").trigger("mousedown")}else{$("#fb-submit-button-div").trigger("mousedown")}}return false}});$("body").keyup(function(e){if(e.keyCode=="46"){fb_clipboard.doDelete()}});$("#fb-submit-button").click(function(){return false});$("#fb-submit-button").focus(function(){$(this).blur();return false});var d=0;var b=0;if(appVar.platform=="win"){d=13;b=26}else{d=12;b=12}$(window).resize(function(){if(fb_translate.get_language()=="en"||fb_translate.get_language()==""){$("#layout-center").width($(document).width()-332)}else{$("#layout-center").width($(document).width()-355)}$("#controlset").height($(document).height()-$("#control_toolbar").outerHeight()-d+5)});$(window).trigger("resize");var c="placeholder" in document.createElement("input");var a="placeholder" in document.createElement("textarea");if(!c){$("#docContainer input[type=text], #docContainer input[type=email], #docContainer input[type=url], #docContainer input[type=tel]").placeholder();if(appVar.platform()!="win"){$("#docContainer input[type=password]").placeholder()}}if(!a){$("#docContainer textarea").placeholder()}$("#fb-submit-button-div, #fb-form-header1").live("resize",function(){var e=$(this).children(0).attr("data-valign");if(e!="top"&&e!="middle"&&e!="bottom"){e="top"}if(e=="top"){$(this).children(0).css("margin-top","")}else{if(e=="middle"){$(this).children(0).css("margin-top",parseInt(parseInt($(this).height())/2-parseInt($(this).children(0).outerHeight())/2)+"px")}else{if(e=="bottom"){$(this).children(0).css("margin-top",parseInt($(this).height())-parseInt($(this).children(0).outerHeight())+"px")}}}});if(typeof String.prototype.trim!=="function"){String.prototype.trim=function(){return this.replace(/^\s+|\s+$/g,"")}}},initSectionChildren:function(f){for(var d=0;d<f.length;d++){if($(f[d]).hasClass("column")){$(f[d]).sortable(fb_container.sortableProps);var c=$(f[d]).children();for(var b=0;b<c.length;b++){$(c[b]).mousedown(fb_editor.itemToSelectable)}}}fb_editor.notBackgroundActive();$("#docContainer a").live("click",function(h){h.preventDefault();return false});$("#fb-submit-button-div").mousedown(fb_editor.itemToSelectable);var a=$("#fb-submit-button-div").children();for(var e=0;e<a.length;e++){$(a[e]).mousedown(fb_editor.itemToSelectable)}$("#fb-form-header1").mousedown(fb_editor.itemToSelectable);var g=$("#fb-form-header1").children();for(var e=0;e<g.length;e++){$(g[e]).mousedown(fb_editor.itemToSelectable)}$("#fb-captcha_control").mousedown(fb_editor.itemToSelectable)},backgroundActive:function(){if($(".column").hasClass("img_background")){$(".column").removeClass("img_background");$(".column").removeAttr("data-content");if(appVar.platform()=="win"){$(".column #fb_placeholder_text").remove()}$(".column").sortable("option","placeholder","ui-placeholder-bis")}},notBackgroundActive:function(){if(!$(".column").hasClass("img_background")){$(".column").sortable("option","placeholder","ui-placeholder-bis")}},triggerActiveElementChange:function(){if(document.activeElement&&!fb_editor.dismissActiveTrigger){var a=document.activeElement;fb_panel.inputFocused="";if(a.tagName!="SELECT"&&a.type!="checkbox"){$(a).trigger("change")}}fb_editor.dismissActiveTrigger=false},nextSelectedItem:function(a){if(a!=null){$(a).trigger("mousedown")}else{$("#elements").trigger("click");$("#control_properties_set").append('<p id="properties_placeholder">'+_("Select an element and its editable properties will appear here.")+"</p>")}},itemToSelectable:function(){if($("#formproperties").hasClass("current")&&$(this).hasClass("fb-item")){fb_views.form()}fb_panel.show($(this));var c=$(this).offset().top;var b=$("#layout-center");if(c<0){if($(this).get(0)==$(this).parent().children(0).get(0)){$("#layout-center").scrollTop(0)}else{$("#layout-center").scrollTop($("#layout-center").scrollTop()+c)}}else{if(c+$(this).outerHeight(true)>$("#layout-center").outerHeight(true)){if($(this).attr("id")=="fb-submit-button-div"){var a=0;$("#layout-center").children().each(function(){a=$(this).outerHeight()});$("#layout-center").scrollTop(a)}else{$("#layout-center").scrollTop(c-$("#layout-center").height()+$("#layout-center").scrollTop()+$(this).outerHeight()+20)}}}if($(this).attr("id")=="fb-submit-button"||$(this).attr("id")=="fb-link-logo1"){return false}},isSelectedItem:function(){if(fb_container.itemSelected&&!$(fb_container.itemSelected).hasClass("fb-button-special")&&!$(fb_container.itemSelected).hasClass("fb-footer")&&!$(fb_container.itemSelected).hasClass("fb-form-header")&&!$(fb_container.itemSelected).hasClass("fb-link-logo")&&!$(fb_container.itemSelected).hasClass("fb-captcha")){return true}else{return false}},isTextSelected:function(){if(window.getSelection){if(window.getSelection().toString()){return"YES"}else{return"NO"}}else{if(document.selection){var a=document.selection.createRange();if(a.text){return"YES"}else{return"NO"}}}},sendInsertAlert:function(f,e){var c={};var d=document.getElementById("item"+fb_toolbox.itemsCounter);var b=$("#item"+fb_toolbox.itemsCounter).prev();var a="";c.type_operation="insert";c.markup=fb_utils.getItemHtml(d);c.id_father=$("#item"+fb_toolbox.itemsCounter).parent().attr("id");if(b[0]){c.id_previous_brother=b[0].id}if(e){c.validation_object_id=e;c.validation_object=pValidationConfig.get_config_id(e)}c.conditionals_object=pValidationConfig.get_config_id(fb_conditionals.cond_rule_name);c.conditionals_object_specific=pValidationConfig.get_config_value(fb_conditionals.cond_rule_name,"item"+fb_toolbox.itemsCounter);c.conditionals_object_id="item"+fb_toolbox.itemsCounter;appVar.pushUndo_(fb_editor.JSONParser.stringify(c))},resetStyles:function(){var b=document.getElementById("docContainer");var a=fb_undo.getFormParams(b);a.validation_object_id="fb-submit-button";a.validation_object=fb_utils.cloneObject(pValidationConfig.get_config_id("fb-submit-button"));var f=$("#control_toolbar .current");var e=new Array();$.each($(".fb-image").parent(),function(h,g){var i={id:g.id,style:"width:"+$(g).css("width")+";height:"+$(g).css("height")+";"};e.push(i)});$("#docContainer").removeAttr("style");$("#docContainer .column *").removeAttr("style");$("#docContainer #fb-submit-button").removeAttr("style");$("#docContainer").removeAttr("data-margin");$("#docContainer").removeClass("fb-small");$("#docContainer").removeClass("fb-large");$("#docContainer .fb-item-alignment-center").removeClass("fb-item-alignment-center");$("#docContainer .fb-item-alignment-left").removeClass("fb-item-alignment-left");$("#docContainer .fb-item-alignment-right").removeClass("fb-item-alignment-right");$("#docContainer .fb-one-column").removeClass("fb-one-column").addClass("fb-two-column");$("#docContainer .fb-three-column").removeClass("fb-three-column").addClass("fb-two-column");$("#docContainer").removeClass("fb-toplabel");$("#docContainer").removeClass("fb-leftlabel");$("#docContainer").removeClass("fb-rightlabel");fb_editor.resetSubmitStyles();$("#fb-submit-button").addClass("fb-submit-design");$("#fb-submit-button-div").removeAttr("style");var c=$("#fb-form-header1").css("min-height");$("#fb-form-header1").removeAttr("style");$("#fb-form-header1").css("min-height",c);$.each(e,function(h,g){$("#"+g.id).attr("style",g.style)});a.markupredo=fb_utils.getouterHtml(b);a.validation_object_redo=fb_utils.cloneObject(pValidationConfig.get_config_id("fb-submit-button"));var d=fb_editor.JSONParser.stringify(a);appVar.pushUndo_(d);$("#"+f[0].id).trigger("click")},resetSubmitStyles:function(){$("#fb-submit-button-div").css("min-height","");var a=document.getElementById("fb-submit-button");a.removeAttribute("style");a.removeAttribute("data-valign");$("#fb-submit-button-div").removeClass("fb-item-alignment-center");$("#fb-submit-button-div").removeClass("fb-item-alignment-left");$("#fb-submit-button-div").removeClass("fb-item-alignment-right");$("#fb-submit-button").removeClass("non-standard");$("#fb-submit-button").removeClass("fb-submit-design");pValidationConfig.set_config_subtype("fb-submit-button","hover","background-image","");fb_panel.applyBackgroundHover("fb-submit-button");$("#fb-submit-button").attr("data-regular",$("#fb-submit-button").css("background-image"));$("#objectproperties").trigger("click")},initPlaceholderFallback:function(c){var b="placeholder" in document.createElement("input");var a="placeholder" in document.createElement("textarea");if(c==undefined){if(!b){$("#docContainer input[type=text], #docContainer input[type=email], #docContainer input[type=url], #docContainer input[type=tel]").placeholder();if(appVar.platform()!="win"){$("#docContainer input[type=password]").placeholder()}}if(!a){$("#docContainer textarea").placeholder()}}else{b=b&&$(c).get(0).tagName.toLowerCase()=="input";a=a&&$(c).get(0).tagName.toLowerCase()=="textarea";if(!b){$(c).placeholder()}if(!a){$(c).placeholder()}}}};window.onload=fb_editor.initEvents;var fb_clipboard={doCopy:function(){if(fb_panel.inputFocused){if(window.getSelection){return""}else{if(document.selection){var d=document.selection.createRange();if(d.text){return d.text}else{return""}}}}else{if(fb_editor.isSelectedItem()){var a={};var e="";if(fb_container.itemSelected[0].childNodes[1]){if($(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-radio")||$(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-checkbox")){e=fb_container.itemSelected[0].childNodes[1].firstChild.firstChild.id;if($(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-radio")){for(var b=0;b<fb_container.itemSelected[0].childNodes[1].childNodes.length;b++){if(fb_container.itemSelected[0].childNodes[1].childNodes[b].firstChild.checked){a.radiochecked_id=fb_container.itemSelected[0].childNodes[1].childNodes[b].firstChild.id}}}}else{e=fb_container.itemSelected[0].childNodes[1].firstChild.id}}else{if($(fb_container.itemSelected[0].childNodes[0]).hasClass("fb-html")){e=fb_container.itemSelected[0].childNodes[0].firstChild.id}}a.source="item";a.markup=fb_utils.getItemHtml(fb_container.itemSelected[0]);a.validation_object=pValidationConfig.get_config_id(e);a.payments_object=pPaymentsConfig.get_config_id(e);a.conditionals_object_specific=pValidationConfig.get_config_value(fb_conditionals.cond_rule_name,$(fb_container.itemSelected).attr("id"));a.conditionals_object_id=$(fb_container.itemSelected).attr("id");var c=fb_editor.JSONParser.stringify(a);return c}}return""},doCut:function(){if(fb_panel.inputFocused){if(window.getSelection){return""}else{if(document.selection){var h=document.selection.createRange();if(h.text){return h.text}else{return""}}}}else{if(fb_editor.isSelectedItem()){var c="";if(fb_container.itemSelected[0].childNodes[1]){if($(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-radio")||$(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-checkbox")){c=fb_container.itemSelected[0].childNodes[1].firstChild.firstChild.id}else{c=fb_container.itemSelected[0].childNodes[1].firstChild.id}}else{if($(fb_container.itemSelected[0].childNodes[0]).hasClass("fb-html")){c=fb_container.itemSelected[0].childNodes[0].firstChild.id}}var i={};i.source="item";i.markup=fb_utils.getItemHtml(fb_container.itemSelected[0]);i.validation_object=pValidationConfig.get_config_id(c);i.payments_object=pPaymentsConfig.get_config_id(c);i.validation_object_id=c;i.conditionals_object=pValidationConfig.get_config_id(fb_conditionals.cond_rule_name);i.conditionals_object_id=$(fb_container.itemSelected).attr("id");i.conditionals_object_specific=pValidationConfig.get_config_value(fb_conditionals.cond_rule_name,$(fb_container.itemSelected).attr("id"));var f=fb_editor.JSONParser.stringify(i);var e={};e.type_operation="delete";e.markup=fb_utils.getItemHtml(fb_container.itemSelected[0]);e.validation_object=pValidationConfig.get_config_id(c);e.payments_object=pPaymentsConfig.get_config_id(c);e.validation_object_id=c;e.conditionals_object_id=$(fb_container.itemSelected).attr("id");e.conditionals_object_specific=pValidationConfig.get_config_value(fb_conditionals.cond_rule_name,$(fb_container.itemSelected).attr("id"));e.conditionals_object=pValidationConfig.get_config_id(fb_conditionals.cond_rule_name);var g=$(fb_container.itemSelected).parent()[0];e.id_father=g.id;e.id_element=$(fb_container.itemSelected).attr("id");var a="";if($(fb_container.itemSelected).prev()[0]){a=$($(fb_container.itemSelected).prev()[0]).attr("id")}e.id_previous_brother=a;if(c!=""){var b=$("#"+c).attr("name");if(b){if(b.substr(b.length-2,b.length)=="[]"){b=b.substr(0,b.length-2)}fb_conditionals.delete_name(pValidationConfig.normalize_name(b));pValidationConfig.remove_config_type(fb_conditionals.cond_rule_name,$(fb_container.itemSelected).attr("id"));i.conditionals_object_redo=pValidationConfig.get_config_id(fb_conditionals.cond_rule_name)}}var d=fb_editor.JSONParser.stringify(e);$(fb_container.itemSelected).remove();pValidationConfig.remove_config_id(c);pPaymentsConfig.remove_config_id(c);fb_panel.clear();fb_container.itemSelected="";appVar.pushUndo_(d);fb_panel.updateAutofocus();return f}}return""},doPaste:function(a){var f={};var c=/"source":/;if(c.test(a)){f=fb_editor.JSONParser.parse(a);if(fb_editor.isSelectedItem()){$(fb_container.itemSelected).after(f.markup);$(fb_container.itemSelected).next().attr("id","item"+ ++fb_toolbox.itemsCounter)}else{$("#column1").append(f.markup);var b=$("#column1").children().length;$($("#column1").children()[b-1]).attr("id","item"+ ++fb_toolbox.itemsCounter)}$("#item"+fb_toolbox.itemsCounter).mousedown(fb_editor.itemToSelectable);var e=document.getElementById("item"+fb_toolbox.itemsCounter);if(e){var d="";if(f.validation_object_id){d=f.validation_object_id;pValidationConfig.set_config_id(f.validation_object_id,f.validation_object);pPaymentsConfig.set_config_id(f.validation_object_id,f.payments_object)}else{d=fb_toolbox.generateIds(e);if(d){fb_toolbox.generateNames(e);pValidationConfig.set_config_id(d,f.validation_object);pPaymentsConfig.set_config_id(d,f.payments_object);fb_panel.updateVerifyAttrs(e,f)}if(f.radiochecked_id&&document.getElementById(f.radiochecked_id)){$("#"+f.radiochecked_id).attr("checked",true)}}fb_panel.updateAutofocus()}fb_editor.sendInsertAlert(fb_toolbox.itemsCounter,d);if(f.conditionals_object_id){pValidationConfig.set_config_type(fb_conditionals.cond_rule_name,"item"+fb_toolbox.itemsCounter,f.conditionals_object_specific)}fb_editor.initPlaceholderFallback($("#item"+fb_toolbox.itemsCounter+" input, #item"+fb_toolbox.itemsCounter+" textarea").get(0));$("#item"+fb_toolbox.itemsCounter).trigger("mousedown");if($("#workflow_switcher").attr("checked")){$("#objectproperties").trigger("click")}return"YES"}else{return"NO"}},doDelete:function(){if(fb_panel.inputFocused){return"NO"}else{if(fb_editor.isSelectedItem()){var c="";var h="";if(fb_container.itemSelected[0].childNodes[1]){if($(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-radio")||$(fb_container.itemSelected[0].childNodes[1]).hasClass("fb-checkbox")){c=fb_container.itemSelected[0].childNodes[1].firstChild.firstChild.id;h=fb_container.itemSelected[0].childNodes[1].firstChild.firstChild.name}else{c=fb_container.itemSelected[0].childNodes[1].firstChild.id;h=fb_container.itemSelected[0].childNodes[1].firstChild.name}}else{if($(fb_container.itemSelected[0].childNodes[0]).hasClass("fb-html")){c=fb_container.itemSelected[0].childNodes[0].firstChild.id}}if(h){if(h.substr(h.length-2,h.length)=="[]"){h=h.substr(0,h.length-2)}var g="["+h+"]";appVar.updateNameInSettings_(g)}var f={};f.type_operation="delete";f.markup=fb_utils.getItemHtml(fb_container.itemSelected[0]);f.validation_object=pValidationConfig.get_config_id(c);f.payments_object=pPaymentsConfig.get_config_id(c);f.validation_object_id=c;f.conditionals_object=pValidationConfig.get_config_id(fb_conditionals.cond_rule_name);var i=$(fb_container.itemSelected).parent()[0];f.id_father=i.id;f.id_element=$(fb_container.itemSelected).attr("id");var a="";if($(fb_container.itemSelected).prev()[0]){a=$($(fb_container.itemSelected).prev()[0]).attr("id")}f.id_previous_brother=a;var e=fb_editor.JSONParser.stringify(f);var d=null;if(fb_container.itemSelected[0].nextSibling){d=fb_container.itemSelected[0].nextSibling}else{if(fb_container.itemSelected[0].previousSibling){d=fb_container.itemSelected[0].previousSibling}}if(c!=""){var b=$("#"+c).attr("name");if(b){if(b.substr(b.length-2,b.length)=="[]"){b=b.substr(0,b.length-2)}pValidationConfig.remove_config_type(fb_conditionals.cond_rule_name,$(fb_container.itemSelected).attr("id"));fb_conditionals.delete_name(pValidationConfig.normalize_name(b))}}f.conditionals_object_redo=pValidationConfig.get_config_id(fb_conditionals.cond_rule_name);$(fb_container.itemSelected).remove();pValidationConfig.remove_config_id(c);pPaymentsConfig.remove_config_id(c);fb_panel.clear();fb_container.itemSelected="";fb_editor.nextSelectedItem(d);appVar.pushUndo_(e);fb_panel.updateAutofocus();return"YES"}}return"NO"}};var fb_undo={actionMap:[],isInsertAction:false,map:function(a,b){this.actionMap[a]=b},init:function(){fb_undo.map("insert",fb_undo.insertAction);fb_undo.map("delete",fb_undo.deleteAction);fb_undo.map("move",fb_undo.moveAction);fb_undo.map("change",fb_undo.changeAction)},doAction:function(a){new this.actionMap[a.type_operation](a)},insertAction:function(b){if(b.id_previous_brother){var c=document.getElementById(b.id_previous_brother);var a=c.nextSibling;$(a).remove();fb_panel.clear()}else{$($("#"+b.id_father).children()[0]).remove();fb_panel.clear()}if(b.validation_object_id){pValidationConfig.remove_config_id(b.validation_object_id);pPaymentsConfig.remove_config_id(b.validation_object_id)}if(b.conditionals_object_redo){pValidationConfig.set_config_id(fb_conditionals.cond_rule_name,b.conditionals_object_redo)}if(b.conditionals_object_id){pValidationConfig.remove_config_type(fb_conditionals.cond_rule_name,b.id_father)}fb_container.itemSelected="";fb_panel.addLegendToPanel("");$("#control_properties_set").append('<p id="properties_placeholder">'+_("Select an element and its editable properties will appear here.")+"</p>")},deleteAction:function(b){if(b.id_previous_brother){if($("#"+b.id_previous_brother).hasClass("section")){if($("#"+b.id_previous_brother).next().attr("id")==b.id_element){$("#"+b.id_previous_brother).next().remove()}$("#"+b.id_previous_brother).after(b.markup);var a=$("#"+b.id_previous_brother).next().children();fb_editor.initSectionChildren(a)}else{$("#"+b.id_previous_brother).after(b.markup);$("#"+b.id_previous_brother).next().mousedown(fb_editor.itemToSelectable);if(b.validation_object_id){if(b.validation_object){pValidationConfig.set_config_id(b.validation_object_id,b.validation_object)}if(b.payments_object){pPaymentsConfig.set_config_id(b.validation_object_id,b.payments_object)}}if(b.conditionals_object){pValidationConfig.set_config_id(fb_conditionals.cond_rule_name,b.conditionals_object)}if(b.id_element){fb_editor.initPlaceholderFallback($("#"+b.id_element+" input, #"+b.id_element+" textarea").get(0))}$($("#"+b.id_previous_brother).next()).trigger("mousedown")}}else{if($("#"+b.id_father).attr("id")=="docContainer"){if($($("#"+b.id_father).children()[0]).attr("id")==b.id_element){$($("#"+b.id_father).children()[0]).remove()}$("#"+b.id_father).prepend(b.markup);var a=$($("#"+b.id_father).children()[0]).children();fb_editor.initSectionChildren(a)}else{$("#"+b.id_father).prepend(b.markup);$($("#"+b.id_father).children()[0]).mousedown(fb_editor.itemToSelectable);if(b.validation_object_id){if(b.validation_object){pValidationConfig.set_config_id(b.validation_object_id,b.validation_object)}if(b.payments_object){pPaymentsConfig.set_config_id(b.validation_object_id,b.payments_object)}}if(b.conditionals_object){pValidationConfig.set_config_id(fb_conditionals.cond_rule_name,b.conditionals_object)}if(b.id_element){fb_editor.initPlaceholderFallback($("#"+b.id_element+" input, #"+b.id_element+" textarea").get(0))}$($("#"+b.id_father).children()[0]).trigger("mousedown")}}},moveAction:function(f){var b={};var c=f.markup;b.id_previous_brother=f.id_newbrother;b.type_operation="insert";b.markup=c;b.id_father=f.id_father;b.id_element=f.id_element;b.id_previous_brother=f.id_newbrother;var e=fb_editor.JSONParser.stringify(b);fb_undo.doUndo(e);var a={};a.type_operation="delete";a.markup=c;a.id_father=f.id_father;a.id_element=f.id_element;a.id_previous_brother=f.id_previous_brother;var d=fb_editor.JSONParser.stringify(a);fb_undo.doUndo(d)},changeAction:function(f){if(f.id_previous_brother){if($("#"+f.id_previous_brother).hasClass("fb-captcha")){$("#"+f.id_previous_brother).next().remove();$("#"+f.id_previous_brother).after(f.markup);if(f.validation_object_id){pValidationConfig.set_config_id(f.validation_object_id,f.validation_object)}fb_panel.applyBackgroundHover($("#"+f.id_element).children().get(0).id);var d=$("#"+f.id_previous_brother).next().children();fb_editor.initSectionChildren(d);if(!isNaN(f.child_number)){var c=$("#"+f.id_element).children()[f.child_number];$(c).trigger("mousedown")}else{$("#"+f.id_element).trigger("mousedown")}}else{var b=document.getElementById(f.id_previous_brother).nextSibling;$(b).remove();$("#"+f.id_previous_brother).after(f.markup);$($("#"+f.id_previous_brother).next()).mousedown(fb_editor.itemToSelectable);if(f.validation_object_id){if(f.validation_object){pValidationConfig.set_config_id(f.validation_object_id,f.validation_object)}if(f.payments_object){pPaymentsConfig.set_config_id(f.validation_object_id,f.payments_object)}}if(f.conditionals_object){pValidationConfig.set_config_id(fb_conditionals.cond_rule_name,f.conditionals_object)}if(f.id_element){fb_editor.initPlaceholderFallback($("#"+f.id_element+" input, #"+f.id_element+" textarea").get(0))}$($("#"+f.id_previous_brother).next()).trigger("mousedown")}}else{$($("#"+f.id_father).children()[0]).remove();if($("#"+f.id_father).children()[0]){$($("#"+f.id_father).children()[0]).before(f.markup)}else{$("#"+f.id_father).append(f.markup)}if($($("#"+f.id_father).children()[0]).attr("id")=="docContainer"){fb_container.initLoaded();if(f.validation_object_id){if(f.validation_object){pValidationConfig.set_config_id(f.validation_object_id,f.validation_object)}if(f.payments_object){pPaymentsConfig.set_config_id(f.validation_object_id,f.payments_object)}if(f.validation_object_id=="reCaptcha"){$("#fb-captcha_control").trigger("mousedown");return}}if(f.conditionals_object){pValidationConfig.set_config_id(fb_conditionals.cond_rule_name,f.conditionals_object)}fb_views.form();itColumn1=document.getElementById("column1");fb_container.itemSelected=itColumn1.lastChild;if(fb_editor.isSelectedItem()){$("#docContainer *").removeClass("selected-object");$(fb_container.itemSelected).addClass("selected-object");document.getElementById("docContainer").style.webkitTransform="scale(1)";$("#docContainer").css("-webkit-transform","");$(fb_container.itemSelected).trigger("mousedown")}}else{if($($("#"+f.id_father).children()[0]).hasClass("section")){var d=$($("#docContainer").children()[0]).children();fb_editor.initSectionChildren(d)}else{$($("#"+f.id_father).children()[0]).mousedown(fb_editor.itemToSelectable);if($("#"+f.id_father).children()[0].id=="fb-form-header1"){var e=$("#fb-form-header1").children();for(var a=0;a<e.length;a++){$(e[a]).mousedown(fb_editor.itemToSelectable)}}if(f.validation_object_id){if(f.validation_object){pValidationConfig.set_config_id(f.validation_object_id,f.validation_object)}if(f.payments_object){pPaymentsConfig.set_config_id(f.validation_object_id,f.payments_object)}}if(f.conditionals_object){pValidationConfig.set_config_id(fb_conditionals.cond_rule_name,f.conditionals_object)}if(f.id_element){fb_editor.initPlaceholderFallback($("#"+f.id_element+" input, #"+f.id_element+" textarea").get(0))}if(!isNaN(f.child_number)){var c=$($("#"+f.id_father).children()[0]).children()[f.child_number];$(c).trigger("mousedown")}else{$($("#"+f.id_father).children()[0]).trigger("mousedown")}}}}},doUndo:function(a){fb_editor.dismissActiveTrigger=true;var b=fb_editor.JSONParser.parse(a);$(".fb-item").removeClass("selected-object");$("#fb-submit-button-div").removeClass("selected-object");fb_undo.doAction(b);if($("#formproperties").hasClass("current")){fb_panel.updateAutofocus()}$("#docContainer a").live("click",function(c){c.preventDefault();return false})},doRedo:function(a){$("#fb-submit-button-div").removeClass("selected-object");var c=fb_editor.JSONParser.parse(a);var b;if(c.type_operation=="insert"){c.type_operation="delete";b=fb_editor.JSONParser.stringify(c);fb_undo.doUndo(b)}else{if(c.type_operation=="delete"){c.type_operation="insert";b=fb_editor.JSONParser.stringify(c);fb_undo.doUndo(b)}else{if(c.type_operation=="move"){c.type_operation="insert";b=fb_editor.JSONParser.stringify(c);fb_undo.doUndo(b);c.type_operation="delete";c.id_previous_brother=c.id_newbrother;b=fb_editor.JSONParser.stringify(c);fb_undo.doUndo(b)}else{if(c.type_operation=="change"){c.markup=c.markupredo;c.validation_object=c.validation_object_redo;c.payments_object=c.payments_object_redo;c.conditionals_object=c.conditionals_object_redo;b=fb_editor.JSONParser.stringify(c);fb_undo.doUndo(b)}}}}},getItemParams:function(e,d){var b={};var c=d?true:false;var a="";if($(e).parent().prev()[0]){a=$($(e).parent().prev()[0]).attr("id")}b.type_operation="change";b.markup=fb_utils.getItemHtml($(e).parent()[0],c);b.id_father=$(e).parent().parent()[0].id;b.id_previous_brother=a;b.id_element=$(e).parent()[0].id;return b},getFormParams:function(b){var a={};a.type_operation="change";a.markup=fb_utils.getouterHtml(b);a.id_father=$(b).parent()[0].id;a.id_previous_brother="";a.id_element="docContainer";return a},getSubmitParams:function(b){var a={};a.type_operation="change";a.markup=fb_utils.getouterHtml(b);a.id_father="docContainer";a.id_previous_brother="fb-captcha_control";a.id_element="fb-submit-button-div";return a},getLogoAndHeaderParams:function(b){var a={};a.type_operation="change";a.markup=fb_utils.getouterHtml(b);a.id_father="docContainer";a.id_previous_brother="";a.id_element="fb-form-header1";return a}};