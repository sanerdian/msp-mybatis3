(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-1aee96f6"],{1538:function(e,t,n){"use strict";n.d(t,"h",(function(){return a})),n.d(t,"b",(function(){return o})),n.d(t,"g",(function(){return l})),n.d(t,"k",(function(){return i})),n.d(t,"i",(function(){return s})),n.d(t,"f",(function(){return u})),n.d(t,"e",(function(){return c})),n.d(t,"a",(function(){return d})),n.d(t,"j",(function(){return m})),n.d(t,"c",(function(){return p})),n.d(t,"d",(function(){return f}));var r=n("b775");function a(e){return Object(r["a"])({url:"/flowable/definition/list",method:"get",params:e})}function o(e,t){return Object(r["a"])({url:"/flowable/definition/start/"+e,method:"post",data:t})}function l(e){return Object(r["a"])({url:"/flowable/task/processVariables/"+e,method:"get"})}function i(e){return Object(r["a"])({url:"/flowable/definition/updateState",method:"put",params:e})}function s(e){return Object(r["a"])({url:"/flowable/definition/readXml/"+e,method:"get"})}function u(e,t){return Object(r["a"])({url:"/flowable/task/flowViewer/"+e+"/"+t,method:"get"})}function c(e){return Object(r["a"])({url:"/flowable/task/flowXmlAndNode",method:"get",params:e})}function d(e){return Object(r["a"])({url:"/system/deployment",method:"post",data:e})}function m(e){return Object(r["a"])({url:"/system/deployment",method:"put",data:e})}function p(e){return Object(r["a"])({url:"/flowable/definition/"+e,method:"delete"})}function f(e){return Object(r["a"])({url:"/system/deployment/export",method:"get",params:e})}},"6e55":function(e,t,n){"use strict";n.d(t,"d",(function(){return a})),n.d(t,"e",(function(){return o})),n.d(t,"g",(function(){return l})),n.d(t,"f",(function(){return i})),n.d(t,"a",(function(){return s})),n.d(t,"h",(function(){return u})),n.d(t,"b",(function(){return c})),n.d(t,"c",(function(){return d}));var r=n("b775");n("3fd3");function a(e){return Object(r["a"])({url:"/flowable/task/finishedList",method:"get",params:e})}function o(e){return Object(r["a"])({url:"/flowable/task/flowRecord",method:"get",params:e})}function l(e){return Object(r["a"])({url:"/flowable/task/revokeProcess",method:"post",data:e})}function i(e){return Object(r["a"])({url:"/system/deployment/"+e,method:"get"})}function s(e){return Object(r["a"])({url:"/system/deployment",method:"post",data:e})}function u(e){return Object(r["a"])({url:"/system/deployment",method:"put",data:e})}function c(e){return Object(r["a"])({url:"/flowable/instance/delete/"+e,method:"delete"})}function d(e){return Object(r["a"])({url:"/system/deployment/export",method:"get",params:e})}},ee90:function(e,t,n){"use strict";n.r(t);var r=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-form",{directives:[{name:"show",rawName:"v-show",value:e.showSearch,expression:"showSearch"}],ref:"queryForm",attrs:{model:e.queryParams,inline:!0,"label-width":"68px"}},[n("el-form-item",{attrs:{label:"名称",prop:"name"}},[n("el-input",{attrs:{placeholder:"请输入名称",clearable:"",size:"small"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleQuery(t)}},model:{value:e.queryParams.name,callback:function(t){e.$set(e.queryParams,"name",t)},expression:"queryParams.name"}})],1),n("el-form-item",{attrs:{label:"开始时间",prop:"deployTime"}},[n("el-date-picker",{attrs:{clearable:"",size:"small",type:"date","value-format":"yyyy-MM-dd",placeholder:"选择时间"},model:{value:e.queryParams.deployTime,callback:function(t){e.$set(e.queryParams,"deployTime",t)},expression:"queryParams.deployTime"}})],1),n("el-form-item",[n("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("搜索")]),n("el-button",{attrs:{icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),n("el-row",{staticClass:"mb8",attrs:{gutter:10}},[n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:deployment:add"],expression:"['system:deployment:add']"}],attrs:{type:"primary",plain:"",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("新增流程")])],1),n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:deployment:remove"],expression:"['system:deployment:remove']"}],attrs:{type:"danger",plain:"",icon:"el-icon-delete",size:"mini",disabled:e.multiple},on:{click:e.handleDelete}},[e._v("删除")])],1),n("right-toolbar",{attrs:{showSearch:e.showSearch},on:{"update:showSearch":function(t){e.showSearch=t},"update:show-search":function(t){e.showSearch=t},queryTable:e.getList}})],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.myProcessList,border:""},on:{"selection-change":e.handleSelectionChange}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"流程编号",align:"center",prop:"procInsId","show-overflow-tooltip":!0}}),n("el-table-column",{attrs:{label:"流程名称",align:"center",prop:"procDefName","show-overflow-tooltip":!0}}),n("el-table-column",{attrs:{label:"流程类别",align:"center",prop:"category",width:"100px"}}),n("el-table-column",{attrs:{label:"流程版本",align:"center",width:"80px"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-tag",{attrs:{size:"medium"}},[e._v("v"+e._s(t.row.procDefVersion))])]}}])}),n("el-table-column",{attrs:{label:"提交时间",align:"center",prop:"createTime",width:"180"}}),n("el-table-column",{attrs:{label:"流程状态",align:"center",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[null==t.row.finishTime?n("el-tag",{attrs:{size:"mini"}},[e._v("进行中")]):e._e(),null!=t.row.finishTime?n("el-tag",{attrs:{type:"success",size:"mini"}},[e._v("已完成")]):e._e()]}}])}),n("el-table-column",{attrs:{label:"耗时",align:"center",prop:"duration",width:"180"}}),n("el-table-column",{attrs:{label:"当前节点",align:"center",prop:"taskName"}}),n("el-table-column",{attrs:{label:"办理人",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[t.row.assigneeName?n("label",[e._v(e._s(t.row.assigneeName)+" "),n("el-tag",{attrs:{type:"info",size:"mini"}},[e._v(e._s(t.row.assigneeDeptName))])],1):e._e()]}}])}),n("el-table-column",{attrs:{label:"操作",width:"150",fixed:"right","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{type:"text",size:"small"},on:{click:function(n){return e.handleFlowRecord(t.row)}}},[e._v("详情")]),n("el-button",{attrs:{type:"text",size:"small"},on:{click:function(n){return e.handleStop(t.row)}}},[e._v("取消申请")]),n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["system:deployment:remove"],expression:"['system:deployment:remove']"}],attrs:{type:"text",size:"small"},on:{click:function(n){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryParams,"pageSize",t)},pagination:e.getList}}),n("el-dialog",{attrs:{title:e.title,visible:e.open,width:"60%","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[n("el-form",{directives:[{name:"show",rawName:"v-show",value:e.showSearch,expression:"showSearch"}],ref:"queryProcessForm",attrs:{model:e.queryProcessParams,inline:!0,"label-width":"68px"}},[n("el-form-item",{attrs:{label:"名称",prop:"name"}},[n("el-input",{attrs:{placeholder:"请输入名称",clearable:"",size:"small"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleQuery(t)}},model:{value:e.queryProcessParams.name,callback:function(t){e.$set(e.queryProcessParams,"name",t)},expression:"queryProcessParams.name"}})],1),n("el-form-item",[n("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleProcessQuery}},[e._v("搜索")]),n("el-button",{attrs:{icon:"el-icon-refresh",size:"mini"},on:{click:e.resetProcessQuery}},[e._v("重置")])],1)],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.processLoading,expression:"processLoading"}],attrs:{fit:"",data:e.definitionList,border:""}},[n("el-table-column",{attrs:{label:"流程名称",align:"center",prop:"name"}}),n("el-table-column",{attrs:{label:"流程版本",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-tag",{attrs:{size:"medium"}},[e._v("v"+e._s(t.row.version))])]}}])}),n("el-table-column",{attrs:{label:"流程分类",align:"center",prop:"category"}}),n("el-table-column",{attrs:{label:"操作",align:"center",width:"300","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{size:"mini",type:"text",icon:"el-icon-edit-outline"},on:{click:function(n){return e.handleStartProcess(t.row)}}},[e._v("发起流程")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.processTotal>0,expression:"processTotal>0"}],attrs:{total:e.processTotal,page:e.queryProcessParams.pageNum,limit:e.queryProcessParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryProcessParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryProcessParams,"pageSize",t)},pagination:e.listDefinition}})],1)],1)},a=[],o=(n("d81d"),n("14d9"),n("6e55")),l=n("fbc1"),i=n("1538"),s={name:"Deploy",components:{},data:function(){return{loading:!0,processLoading:!0,ids:[],single:!0,multiple:!0,showSearch:!0,total:0,processTotal:0,myProcessList:[],title:"",open:!1,src:"",definitionList:[],queryParams:{pageNum:1,pageSize:10,name:null,category:null,key:null,tenantId:null,deployTime:null,derivedFrom:null,derivedFromRoot:null,parentDeploymentId:null,engineVersion:null},queryProcessParams:{pageNum:1,pageSize:10,name:null,category:null,key:null,tenantId:null,deployTime:null,derivedFrom:null,derivedFromRoot:null,parentDeploymentId:null,engineVersion:null},form:{},rules:{}}},created:function(){this.getList()},methods:{getList:function(){var e=this;this.loading=!0,Object(l["b"])(this.queryParams).then((function(t){e.myProcessList=t.data.records,e.total=t.data.total,e.loading=!1}))},cancel:function(){this.open=!1,this.reset()},reset:function(){this.form={id:null,name:null,category:null,key:null,tenantId:null,deployTime:null,derivedFrom:null,derivedFromRoot:null,parentDeploymentId:null,engineVersion:null},this.resetForm("form")},handleQuery:function(){this.queryParams.pageNum=1,this.getList()},resetQuery:function(){this.resetForm("queryForm"),this.handleQuery()},handleProcessQuery:function(){this.queryProcessParams.pageNum=1,this.listDefinition()},resetProcessQuery:function(){this.resetForm("queryProcessForm"),this.handleProcessQuery()},handleSelectionChange:function(e){this.ids=e.map((function(e){return e.procInsId})),this.single=1!==e.length,this.multiple=!e.length},handleAdd:function(){this.open=!0,this.title="发起流程",this.listDefinition()},listDefinition:function(){var e=this;Object(i["h"])(this.queryProcessParams).then((function(t){e.definitionList=t.data.records,e.processTotal=t.data.total,e.processLoading=!1}))},handleStartProcess:function(e){this.$router.push({path:"/flowable/task/myProcess/send/index",query:{deployId:e.deploymentId,procDefId:e.id}})},handleStop:function(e){var t=this,n={instanceId:e.procInsId};Object(l["c"])(n).then((function(e){t.$modal.msgSuccess(e.msg),t.getList()}))},handleFlowRecord:function(e){this.$router.push({path:"/flowable/task/myProcess/detail/index",query:{procInsId:e.procInsId,deployId:e.deployId,taskId:e.taskId}})},handleUpdate:function(e){var t=this;this.reset();var n=e.id||this.ids;Object(o["f"])(n).then((function(e){t.form=e.data,t.open=!0,t.title="修改流程定义"}))},submitForm:function(){var e=this;this.$refs["form"].validate((function(t){t&&(null!=e.form.id?Object(o["h"])(e.form).then((function(t){e.$modal.msgSuccess("修改成功"),e.open=!1,e.getList()})):Object(o["a"])(e.form).then((function(t){e.$modal.msgSuccess("新增成功"),e.open=!1,e.getList()})))}))},handleDelete:function(e){var t=this,n=e.procInsId||this.ids;this.$confirm('是否确认删除流程定义编号为"'+n+'"的数据项?',"警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){return Object(o["b"])(n)})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")}))},handleExport:function(){var e=this,t=this.queryParams;this.$confirm("是否确认导出所有流程定义数据项?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){return Object(o["c"])(t)})).then((function(t){e.download(t.msg)}))}}},u=s,c=n("2877"),d=Object(c["a"])(u,r,a,!1,null,null,null);t["default"]=d.exports},fbc1:function(e,t,n){"use strict";n.d(t,"b",(function(){return a})),n.d(t,"a",(function(){return o})),n.d(t,"c",(function(){return l}));var r=n("b775");n("3fd3");function a(e){return Object(r["a"])({url:"/flowable/task/myProcess",method:"get",params:e})}function o(e){return Object(r["a"])({url:"/flowable/task/flowFormData",method:"get",params:e})}function l(e){return Object(r["a"])({url:"/flowable/task/stopProcess",method:"post",data:e})}}}]);