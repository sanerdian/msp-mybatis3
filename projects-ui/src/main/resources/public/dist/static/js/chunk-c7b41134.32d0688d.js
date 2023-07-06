(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-c7b41134"],{1462:function(e,t,l){"use strict";l.r(t);var a=function(){var e=this,t=e.$createElement,l=e._self._c||t;return l("div",{staticClass:"app-container"},[l("el-form",{directives:[{name:"show",rawName:"v-show",value:e.showSearch,expression:"showSearch"}],ref:"queryForm",attrs:{model:e.queryParams,size:"small",inline:!0,"label-width":"68px"}},[l("el-form-item",{attrs:{label:"名称",prop:"name"}},[l("el-input",{attrs:{placeholder:"请输入名称",clearable:""},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleQuery(t)}},model:{value:e.queryParams.name,callback:function(t){e.$set(e.queryParams,"name",t)},expression:"queryParams.name"}})],1),l("el-form-item",{attrs:{label:"监听类型",prop:"type"}},[l("el-select",{attrs:{placeholder:"请选择监听类型",clearable:""},model:{value:e.queryParams.type,callback:function(t){e.$set(e.queryParams,"type",t)},expression:"queryParams.type"}},e._l(e.dict.type.sys_listener_type,(function(e){return l("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),l("el-form-item",[l("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("搜索")]),l("el-button",{attrs:{icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),l("el-row",{staticClass:"mb8",attrs:{gutter:10}},[l("el-col",{attrs:{span:1.5}},[l("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:listener:add"],expression:"['system:listener:add']"}],attrs:{type:"primary",plain:"",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1),l("el-col",{attrs:{span:1.5}},[l("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:listener:edit"],expression:"['system:listener:edit']"}],attrs:{type:"success",plain:"",icon:"el-icon-edit",size:"mini",disabled:e.single},on:{click:e.handleUpdate}},[e._v("修改")])],1),l("el-col",{attrs:{span:1.5}},[l("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:listener:remove"],expression:"['system:listener:remove']"}],attrs:{type:"danger",plain:"",icon:"el-icon-delete",size:"mini",disabled:e.multiple},on:{click:e.handleDelete}},[e._v("删除")])],1),l("el-col",{attrs:{span:1.5}},[l("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:listener:export"],expression:"['system:listener:export']"}],attrs:{type:"warning",plain:"",icon:"el-icon-download",size:"mini"},on:{click:e.handleExport}},[e._v("导出")])],1),l("right-toolbar",{attrs:{showSearch:e.showSearch},on:{"update:showSearch":function(t){e.showSearch=t},"update:show-search":function(t){e.showSearch=t},queryTable:e.getList}})],1),l("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.listenerList},on:{"selection-change":e.handleSelectionChange}},[l("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),l("el-table-column",{attrs:{label:"名称",align:"center",prop:"name"}}),l("el-table-column",{attrs:{label:"监听类型",align:"center",prop:"type"},scopedSlots:e._u([{key:"default",fn:function(t){return[l("dict-tag",{attrs:{options:e.dict.type.sys_listener_type,value:t.row.type}})]}}])}),l("el-table-column",{attrs:{label:"事件类型",align:"center",prop:"eventType"}}),l("el-table-column",{attrs:{label:"值类型",align:"center",prop:"valueType"},scopedSlots:e._u([{key:"default",fn:function(t){return[l("dict-tag",{attrs:{options:e.dict.type.sys_listener_value_type,value:t.row.valueType}})]}}])}),l("el-table-column",{attrs:{label:"执行内容",align:"center",prop:"value"}}),l("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[l("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:listener:edit"],expression:"['system:listener:edit']"}],attrs:{size:"mini",type:"text",icon:"el-icon-edit"},on:{click:function(l){return e.handleUpdate(t.row)}}},[e._v("修改")]),l("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:listener:remove"],expression:"['system:listener:remove']"}],attrs:{size:"mini",type:"text",icon:"el-icon-delete"},on:{click:function(l){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),l("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryParams,"pageSize",t)},pagination:e.getList}}),l("el-dialog",{attrs:{title:e.title,visible:e.open,width:"500px","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[l("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"80px"}},[l("el-form-item",{attrs:{label:"名称",prop:"name"}},[l("el-input",{attrs:{placeholder:"请输入名称"},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1),l("el-form-item",{attrs:{label:"监听类型",prop:"type"}},[l("el-select",{attrs:{placeholder:"请选择监听类型"},model:{value:e.form.type,callback:function(t){e.$set(e.form,"type",t)},expression:"form.type"}},e._l(e.dict.type.sys_listener_type,(function(e){return l("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),"1"===e.form.type?l("el-form-item",{attrs:{label:"事件类型",prop:"eventType"}},[l("el-select",{attrs:{placeholder:"请选择事件类型"},model:{value:e.form.eventType,callback:function(t){e.$set(e.form,"eventType",t)},expression:"form.eventType"}},e._l(e.taskListenerEventList,(function(e){return l("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1):l("el-form-item",{attrs:{label:"事件类型",prop:"eventType"}},[l("el-select",{attrs:{placeholder:"请选择事件类型"},model:{value:e.form.eventType,callback:function(t){e.$set(e.form,"eventType",t)},expression:"form.eventType"}},e._l(e.executionListenerEventList,(function(e){return l("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),l("el-form-item",{attrs:{label:"值类型",prop:"valueType"}},[l("el-radio-group",{model:{value:e.form.valueType,callback:function(t){e.$set(e.form,"valueType",t)},expression:"form.valueType"}},e._l(e.dict.type.sys_listener_value_type,(function(t){return l("el-radio",{key:t.value,attrs:{label:t.value}},[e._v(e._s(t.label))])})),1)],1),l("el-form-item",{attrs:{label:"执行内容",prop:"value"}},[l("el-input",{attrs:{placeholder:"请输入执行内容"},model:{value:e.form.value,callback:function(t){e.$set(e.form,"value",t)},expression:"form.value"}})],1)],1),l("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[l("el-button",{attrs:{type:"primary"},on:{click:e.submitForm}},[e._v("确 定")]),l("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1)],1)},n=[],r=l("5530"),s=(l("d81d"),l("5a4d")),i={name:"Listener",dicts:["sys_listener_value_type","sys_listener_type","common_status","sys_listener_event_type"],data:function(){return{loading:!0,ids:[],single:!0,multiple:!0,showSearch:!0,total:0,listenerList:[],title:"",open:!1,queryParams:{pageNum:1,pageSize:10,name:null,type:null,eventType:null,valueType:null,value:null,status:null},form:{},rules:{},taskListenerEventList:[{label:"create",value:"create"},{label:"assignment",value:"assignment"},{label:"complete",value:"complete"},{label:"delete",value:"delete"}],executionListenerEventList:[{label:"start",value:"start"},{label:"end",value:"end"},{label:"take",value:"take"}]}},created:function(){this.getList()},methods:{getList:function(){var e=this;this.loading=!0,Object(s["d"])(this.queryParams).then((function(t){e.listenerList=t.rows,e.total=t.total,e.loading=!1}))},cancel:function(){this.open=!1,this.reset()},reset:function(){this.form={id:null,name:null,type:null,eventType:null,valueType:null,value:null,createTime:null,updateTime:null,createBy:null,updateBy:null,status:null,remark:null},this.resetForm("form")},handleQuery:function(){this.queryParams.pageNum=1,this.getList()},resetQuery:function(){this.resetForm("queryForm"),this.handleQuery()},handleSelectionChange:function(e){this.ids=e.map((function(e){return e.id})),this.single=1!==e.length,this.multiple=!e.length},handleAdd:function(){this.reset(),this.open=!0,this.title="添加流程监听"},handleUpdate:function(e){var t=this;this.reset();var l=e.id||this.ids;Object(s["c"])(l).then((function(e){t.form=e.data,t.open=!0,t.title="修改流程监听"}))},submitForm:function(){var e=this;this.$refs["form"].validate((function(t){t&&(null!=e.form.id?Object(s["e"])(e.form).then((function(t){e.$modal.msgSuccess("修改成功"),e.open=!1,e.getList()})):Object(s["a"])(e.form).then((function(t){e.$modal.msgSuccess("新增成功"),e.open=!1,e.getList()})))}))},handleDelete:function(e){var t=this,l=e.id||this.ids;this.$modal.confirm('是否确认删除流程监听编号为"'+l+'"的数据项？').then((function(){return Object(s["b"])(l)})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")})).catch((function(){}))},handleExport:function(){this.download("system/listener/export",Object(r["a"])({},this.queryParams),"listener_".concat((new Date).getTime(),".xlsx"))}}},o=i,u=l("2877"),c=Object(u["a"])(o,a,n,!1,null,null,null);t["default"]=c.exports},"5a4d":function(e,t,l){"use strict";l.d(t,"d",(function(){return n})),l.d(t,"c",(function(){return r})),l.d(t,"a",(function(){return s})),l.d(t,"e",(function(){return i})),l.d(t,"b",(function(){return o}));var a=l("b775");function n(e){return Object(a["a"])({url:"/system/listener/list",method:"get",params:e})}function r(e){return Object(a["a"])({url:"/system/listener/"+e,method:"get"})}function s(e){return Object(a["a"])({url:"/system/listener",method:"post",data:e})}function i(e){return Object(a["a"])({url:"/system/listener",method:"put",data:e})}function o(e){return Object(a["a"])({url:"/system/listener/"+e,method:"delete"})}}}]);