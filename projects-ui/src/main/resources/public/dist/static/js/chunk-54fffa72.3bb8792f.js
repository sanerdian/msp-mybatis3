(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-54fffa72"],{"1c59":function(e,t,n){"use strict";var r=n("6d61"),i=n("6566");r("Set",(function(e){return function(){return e(this,arguments.length?arguments[0]:void 0)}}),i)},2638:function(e,t,n){"use strict";function r(){return r=Object.assign?Object.assign.bind():function(e){for(var t,n=1;n<arguments.length;n++)for(var r in t=arguments[n],t)Object.prototype.hasOwnProperty.call(t,r)&&(e[r]=t[r]);return e},r.apply(this,arguments)}var i=["attrs","props","domProps"],o=["class","style","directives"],a=["on","nativeOn"],s=function(e){return e.reduce((function(e,t){for(var n in t)if(e[n])if(-1!==i.indexOf(n))e[n]=r({},e[n],t[n]);else if(-1!==o.indexOf(n)){var s=e[n]instanceof Array?e[n]:[e[n]],u=t[n]instanceof Array?t[n]:[t[n]];e[n]=[].concat(s,u)}else if(-1!==a.indexOf(n))for(var c in t[n])if(e[n][c]){var f=e[n][c]instanceof Array?e[n][c]:[e[n][c]],d=t[n][c]instanceof Array?t[n][c]:[t[n][c]];e[n][c]=[].concat(f,d)}else e[n][c]=t[n][c];else if("hook"===n)for(var m in t[n])e[n][m]=e[n][m]?l(e[n][m],t[n][m]):t[n][m];else e[n]=t[n];else e[n]=t[n];return e}),{})},l=function(e,t){return function(){e&&e.apply(this,arguments),t&&t.apply(this,arguments)}};e.exports=s},"3ab9":function(e,t,n){"use strict";n.d(t,"f",(function(){return i})),n.d(t,"e",(function(){return o})),n.d(t,"b",(function(){return a})),n.d(t,"g",(function(){return s})),n.d(t,"a",(function(){return l})),n.d(t,"c",(function(){return u})),n.d(t,"d",(function(){return c}));var r=n("b775");function i(e){return Object(r["a"])({url:"/flowable/form/list",method:"get",params:e})}function o(e){return Object(r["a"])({url:"/flowable/form/"+e,method:"get"})}function a(e){return Object(r["a"])({url:"/flowable/form",method:"post",data:e})}function s(e){return Object(r["a"])({url:"/flowable/form",method:"put",data:e})}function l(e){return Object(r["a"])({url:"/flowable/form/addDeployForm",method:"post",data:e})}function u(e){return Object(r["a"])({url:"/flowable/form/"+e,method:"delete"})}function c(e){return Object(r["a"])({url:"/flowable/form/export",method:"get",params:e})}},"466d":function(e,t,n){"use strict";var r=n("c65b"),i=n("d784"),o=n("825a"),a=n("7234"),s=n("50c4"),l=n("577e"),u=n("1d80"),c=n("dc4a"),f=n("8aa5"),d=n("14c3");i("match",(function(e,t,n){return[function(t){var n=u(this),i=a(t)?void 0:c(t,e);return i?r(i,t,n):new RegExp(t)[e](l(n))},function(e){var r=o(this),i=l(e),a=n(t,r,i);if(a.done)return a.value;if(!r.global)return d(r,i);var u=r.unicode;r.lastIndex=0;var c,m=[],p=0;while(null!==(c=d(r,i))){var h=l(c[0]);m[p]=h,""===h&&(r.lastIndex=f(i,s(r.lastIndex),u)),p++}return 0===p?null:m}]}))},"4fad":function(e,t,n){var r=n("d039"),i=n("861d"),o=n("c6b6"),a=n("d86b"),s=Object.isExtensible,l=r((function(){s(1)}));e.exports=l||a?function(e){return!!i(e)&&((!a||"ArrayBuffer"!=o(e))&&(!s||s(e)))}:s},6062:function(e,t,n){n("1c59")},6566:function(e,t,n){"use strict";var r=n("9bf2").f,i=n("7c73"),o=n("6964"),a=n("0366"),s=n("19aa"),l=n("7234"),u=n("2266"),c=n("c6d2"),f=n("4754"),d=n("2626"),m=n("83ab"),p=n("f183").fastKey,h=n("69f3"),v=h.set,b=h.getterFor;e.exports={getConstructor:function(e,t,n,c){var f=e((function(e,r){s(e,d),v(e,{type:t,index:i(null),first:void 0,last:void 0,size:0}),m||(e.size=0),l(r)||u(r,e[c],{that:e,AS_ENTRIES:n})})),d=f.prototype,h=b(t),g=function(e,t,n){var r,i,o=h(e),a=y(e,t);return a?a.value=n:(o.last=a={index:i=p(t,!0),key:t,value:n,previous:r=o.last,next:void 0,removed:!1},o.first||(o.first=a),r&&(r.next=a),m?o.size++:e.size++,"F"!==i&&(o.index[i]=a)),e},y=function(e,t){var n,r=h(e),i=p(t);if("F"!==i)return r.index[i];for(n=r.first;n;n=n.next)if(n.key==t)return n};return o(d,{clear:function(){var e=this,t=h(e),n=t.index,r=t.first;while(r)r.removed=!0,r.previous&&(r.previous=r.previous.next=void 0),delete n[r.index],r=r.next;t.first=t.last=void 0,m?t.size=0:e.size=0},delete:function(e){var t=this,n=h(t),r=y(t,e);if(r){var i=r.next,o=r.previous;delete n.index[r.index],r.removed=!0,o&&(o.next=i),i&&(i.previous=o),n.first==r&&(n.first=i),n.last==r&&(n.last=o),m?n.size--:t.size--}return!!r},forEach:function(e){var t,n=h(this),r=a(e,arguments.length>1?arguments[1]:void 0);while(t=t?t.next:n.first){r(t.value,t.key,this);while(t&&t.removed)t=t.previous}},has:function(e){return!!y(this,e)}}),o(d,n?{get:function(e){var t=y(this,e);return t&&t.value},set:function(e,t){return g(this,0===e?0:e,t)}}:{add:function(e){return g(this,e=0===e?0:e,e)}}),m&&r(d,"size",{get:function(){return h(this).size}}),f},setStrong:function(e,t,n){var r=t+" Iterator",i=b(t),o=b(r);c(e,t,(function(e,t){v(this,{type:r,target:e,state:i(e),kind:t,last:void 0})}),(function(){var e=o(this),t=e.kind,n=e.last;while(n&&n.removed)n=n.previous;return e.target&&(e.last=n=n?n.next:e.state.first)?f("keys"==t?n.key:"values"==t?n.value:[n.key,n.value],!1):(e.target=void 0,f(void 0,!0))}),n?"entries":"values",!n,!0),d(t)}}},6964:function(e,t,n){var r=n("cb2d");e.exports=function(e,t,n){for(var i in t)r(e,i,t[i],n);return e}},"6d61":function(e,t,n){"use strict";var r=n("23e7"),i=n("da84"),o=n("e330"),a=n("94ca"),s=n("cb2d"),l=n("f183"),u=n("2266"),c=n("19aa"),f=n("1626"),d=n("7234"),m=n("861d"),p=n("d039"),h=n("1c7e"),v=n("d44e"),b=n("7156");e.exports=function(e,t,n){var g=-1!==e.indexOf("Map"),y=-1!==e.indexOf("Weak"),w=g?"set":"add",x=i[e],_=x&&x.prototype,k=x,O={},j=function(e){var t=o(_[e]);s(_,e,"add"==e?function(e){return t(this,0===e?0:e),this}:"delete"==e?function(e){return!(y&&!m(e))&&t(this,0===e?0:e)}:"get"==e?function(e){return y&&!m(e)?void 0:t(this,0===e?0:e)}:"has"==e?function(e){return!(y&&!m(e))&&t(this,0===e?0:e)}:function(e,n){return t(this,0===e?0:e,n),this})},N=a(e,!f(x)||!(y||_.forEach&&!p((function(){(new x).entries().next()}))));if(N)k=n.getConstructor(t,e,g,w),l.enable();else if(a(e,!0)){var P=new k,S=P[w](y?{}:-0,1)!=P,z=p((function(){P.has(1)})),C=h((function(e){new x(e)})),E=!y&&p((function(){var e=new x,t=5;while(t--)e[w](t,t);return!e.has(-0)}));C||(k=t((function(e,t){c(e,_);var n=b(new x,e,k);return d(t)||u(t,n[w],{that:n,AS_ENTRIES:g}),n})),k.prototype=_,_.constructor=k),(z||E)&&(j("delete"),j("has"),g&&j("get")),(E||S)&&j(w),y&&_.clear&&delete _.clear}return O[e]=k,r({global:!0,constructor:!0,forced:k!=x},O),v(k,e),y||n.setStrong(k,e,g),k}},9977:function(e,t,n){var r={"./el-button.js":"aace","./el-checkbox-group.js":"9413","./el-input.js":"167d","./el-radio-group.js":"2cfa","./el-select.js":"7f29","./el-upload.js":"0f88"};function i(e){var t=o(e);return n(t)}function o(e){if(!n.o(r,e)){var t=new Error("Cannot find module '"+e+"'");throw t.code="MODULE_NOT_FOUND",t}return r[e]}i.keys=function(){return Object.keys(r)},i.resolve=o,e.exports=i,i.id="9977"},bb2f:function(e,t,n){var r=n("d039");e.exports=!r((function(){return Object.isExtensible(Object.preventExtensions({}))}))},bbd9:function(e,t,n){},d86b:function(e,t,n){var r=n("d039");e.exports=r((function(){if("function"==typeof ArrayBuffer){var e=new ArrayBuffer(8);Object.isExtensible(e)&&Object.defineProperty(e,"a",{value:8})}}))},dc5e:function(e,t,n){"use strict";n.r(t);var r=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("el-form",{directives:[{name:"show",rawName:"v-show",value:e.showSearch,expression:"showSearch"}],ref:"queryForm",attrs:{model:e.queryParams,inline:!0,"label-width":"68px"}},[n("el-form-item",{attrs:{label:"表单名称",prop:"formName"}},[n("el-input",{attrs:{placeholder:"请输入表单名称",clearable:"",size:"small"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.handleQuery(t)}},model:{value:e.queryParams.formName,callback:function(t){e.$set(e.queryParams,"formName",t)},expression:"queryParams.formName"}})],1),n("el-form-item",[n("el-button",{attrs:{type:"primary",icon:"el-icon-search",size:"mini"},on:{click:e.handleQuery}},[e._v("搜索")]),n("el-button",{attrs:{icon:"el-icon-refresh",size:"mini"},on:{click:e.resetQuery}},[e._v("重置")])],1)],1),n("el-row",{staticClass:"mb8",attrs:{gutter:10}},[n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["flowable:form:add"],expression:"['flowable:form:add']"}],attrs:{type:"primary",plain:"",icon:"el-icon-plus",size:"mini"},on:{click:e.handleAdd}},[e._v("新增")])],1),n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["flowable:form:edit"],expression:"['flowable:form:edit']"}],attrs:{type:"success",plain:"",icon:"el-icon-edit",size:"mini",disabled:e.single},on:{click:e.handleUpdate}},[e._v("修改")])],1),n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["flowable:form:remove"],expression:"['flowable:form:remove']"}],attrs:{type:"danger",plain:"",icon:"el-icon-delete",size:"mini",disabled:e.multiple},on:{click:e.handleDelete}},[e._v("删除")])],1),n("el-col",{attrs:{span:1.5}},[n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["flowable:form:export"],expression:"['flowable:form:export']"}],attrs:{type:"warning",plain:"",icon:"el-icon-download",size:"mini"},on:{click:e.handleExport}},[e._v("导出")])],1),n("right-toolbar",{attrs:{showSearch:e.showSearch},on:{"update:showSearch":function(t){e.showSearch=t},"update:show-search":function(t){e.showSearch=t},queryTable:e.getList}})],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],attrs:{data:e.formList},on:{"selection-change":e.handleSelectionChange}},[n("el-table-column",{attrs:{type:"selection",width:"55",align:"center"}}),n("el-table-column",{attrs:{label:"表单主键",align:"center",prop:"formId"}}),n("el-table-column",{attrs:{label:"表单名称",align:"center",prop:"formName"}}),n("el-table-column",{attrs:{label:"备注",align:"center",prop:"remark"}}),n("el-table-column",{attrs:{label:"操作",align:"center","class-name":"small-padding fixed-width"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-button",{attrs:{size:"mini",type:"text",icon:"el-icon-view"},on:{click:function(n){return e.handleDetail(t.row)}}},[e._v("详情")]),n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["flowable:form:edit"],expression:"['flowable:form:edit']"}],attrs:{size:"mini",type:"text",icon:"el-icon-edit"},on:{click:function(n){return e.handleUpdate(t.row)}}},[e._v("修改")]),n("el-button",{directives:[{name:"hasPermi",rawName:"v-hasPermi",value:["flowable:form:remove"],expression:"['flowable:form:remove']"}],attrs:{size:"mini",type:"text",icon:"el-icon-delete"},on:{click:function(n){return e.handleDelete(t.row)}}},[e._v("删除")])]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.total>0,expression:"total>0"}],attrs:{total:e.total,page:e.queryParams.pageNum,limit:e.queryParams.pageSize},on:{"update:page":function(t){return e.$set(e.queryParams,"pageNum",t)},"update:limit":function(t){return e.$set(e.queryParams,"pageSize",t)},pagination:e.getList}}),n("el-dialog",{attrs:{title:e.title,visible:e.open,width:"500px","append-to-body":""},on:{"update:visible":function(t){e.open=t}}},[n("el-form",{ref:"form",attrs:{model:e.form,rules:e.rules,"label-width":"80px"}},[n("el-form-item",{attrs:{label:"表单名称",prop:"formName"}},[n("el-input",{attrs:{placeholder:"请输入表单名称"},model:{value:e.form.formName,callback:function(t){e.$set(e.form,"formName",t)},expression:"form.formName"}})],1),n("el-form-item",{attrs:{label:"表单内容"}},[n("editor",{attrs:{"min-height":192},model:{value:e.form.formContent,callback:function(t){e.$set(e.form,"formContent",t)},expression:"form.formContent"}})],1),n("el-form-item",{attrs:{label:"备注",prop:"remark"}},[n("el-input",{attrs:{placeholder:"请输入备注"},model:{value:e.form.remark,callback:function(t){e.$set(e.form,"remark",t)},expression:"form.remark"}})],1)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{attrs:{type:"primary"},on:{click:e.submitForm}},[e._v("确 定")]),n("el-button",{on:{click:e.cancel}},[e._v("取 消")])],1)],1),n("el-dialog",{attrs:{title:e.formTitle,visible:e.formConfOpen,width:"60%","append-to-body":""},on:{"update:visible":function(t){e.formConfOpen=t}}},[n("div",{staticClass:"test-form"},[n("parser",{key:(new Date).getTime(),attrs:{"form-conf":e.formConf}})],1)])],1)},i=[],o=(n("d81d"),n("b64b"),n("14d9"),n("3ab9")),a=n("095c"),s=n("9f75"),l={name:"Form",components:{Editor:a["a"],Parser:s["a"]},data:function(){return{loading:!0,ids:[],single:!0,multiple:!0,showSearch:!0,total:0,formList:[],title:"",formConf:{},formConfOpen:!1,formTitle:"",open:!1,queryParams:{pageNum:1,pageSize:10,formName:null,formContent:null},form:{},rules:{}}},created:function(){this.getList()},methods:{getList:function(){var e=this;this.loading=!0,Object(o["f"])(this.queryParams).then((function(t){e.formList=t.rows,e.total=t.total,e.loading=!1}))},cancel:function(){this.open=!1,this.reset()},reset:function(){this.form={formId:null,formName:null,formContent:null,createTime:null,updateTime:null,createBy:null,updateBy:null,remark:null},this.resetForm("form")},handleQuery:function(){this.queryParams.pageNum=1,this.getList()},resetQuery:function(){this.resetForm("queryForm"),this.handleQuery()},handleSelectionChange:function(e){this.ids=e.map((function(e){return e.formId})),this.single=1!==e.length,this.multiple=!e.length},handleDetail:function(e){this.formConfOpen=!0,this.formTitle="流程表单配置详细",this.formConf=JSON.parse(e.formContent)},handleAdd:function(){this.$router.push({path:"/tool/build/index",query:{formId:null}})},handleUpdate:function(e){this.$router.push({path:"/tool/build/index",query:{formId:e.formId}})},submitForm:function(){var e=this;this.$refs["form"].validate((function(t){t&&(null!=e.form.formId?Object(o["g"])(e.form).then((function(t){e.$modal.msgSuccess("修改成功"),e.open=!1,e.getList()})):Object(o["b"])(e.form).then((function(t){e.$modal.msgSuccess("新增成功"),e.open=!1,e.getList()})))}))},handleDelete:function(e){var t=this,n=e.formId||this.ids;this.$confirm('是否确认删除流程表单编号为"'+n+'"的数据项?',"警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){return Object(o["c"])(n)})).then((function(){t.getList(),t.$modal.msgSuccess("删除成功")}))},handleExport:function(){var e=this,t=this.queryParams;this.$confirm("是否确认导出所有流程表单数据项?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){return Object(o["d"])(t)})).then((function(t){e.download(t.msg)}))}}},u=l,c=(n("fe1b"),n("2877")),f=Object(c["a"])(u,r,i,!1,null,"57c83139",null);t["default"]=f.exports},ed08:function(e,t,n){"use strict";n.d(t,"b",(function(){return i})),n.d(t,"d",(function(){return o})),n.d(t,"a",(function(){return a})),n.d(t,"g",(function(){return s})),n.d(t,"e",(function(){return l})),n.d(t,"c",(function(){return u})),n.d(t,"f",(function(){return f}));var r=n("53ca");n("ac1f"),n("5319"),n("14d9"),n("a15b"),n("d81d"),n("b64b"),n("d3b7"),n("159b"),n("fb6a"),n("a630"),n("3ca3"),n("6062"),n("ddb0"),n("25f0"),n("466d"),n("4d63"),n("c607"),n("2c3e"),n("00b4"),n("c38a");function i(e,t,n){var r,i,o,a,s,l=function l(){var u=+new Date-a;u<t&&u>0?r=setTimeout(l,t-u):(r=null,n||(s=e.apply(o,i),r||(o=i=null)))};return function(){for(var i=arguments.length,u=new Array(i),c=0;c<i;c++)u[c]=arguments[c];o=this,a=+new Date;var f=n&&!r;return r||(r=setTimeout(l,t)),f&&(s=e.apply(o,u),o=u=null),s}}var o="export default ",a={html:{indent_size:"2",indent_char:" ",max_preserve_newlines:"-1",preserve_newlines:!1,keep_array_indentation:!1,break_chained_methods:!1,indent_scripts:"separate",brace_style:"end-expand",space_before_conditional:!0,unescape_strings:!1,jslint_happy:!1,end_with_newline:!0,wrap_line_length:"110",indent_inner_html:!0,comma_first:!1,e4x:!0,indent_empty_lines:!0},js:{indent_size:"2",indent_char:" ",max_preserve_newlines:"-1",preserve_newlines:!1,keep_array_indentation:!1,break_chained_methods:!1,indent_scripts:"normal",brace_style:"end-expand",space_before_conditional:!0,unescape_strings:!1,jslint_happy:!0,end_with_newline:!0,wrap_line_length:"110",indent_inner_html:!0,comma_first:!1,e4x:!0,indent_empty_lines:!0}};function s(e){return e.replace(/( |^)[a-z]/g,(function(e){return e.toUpperCase()}))}function l(e){return/^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(e)}function u(e){var t=Object.prototype.toString;if(!e||"object"!==Object(r["a"])(e))return e;if(e.nodeType&&"cloneNode"in e)return e.cloneNode(!0);if("[object Date]"===t.call(e))return new Date(e.getTime());if("[object RegExp]"===t.call(e)){var n=[];return e.global&&n.push("g"),e.multiline&&n.push("m"),e.ignoreCase&&n.push("i"),new RegExp(e.source,n.join(""))}var i=Array.isArray(e)?[]:e.constructor?new e.constructor:{};for(var o in e)i[o]=u(e[o]);return i}var c=Function.prototype.call.bind(Object.prototype.toString);function f(e){return"[object Object]"===c(e)}},f183:function(e,t,n){var r=n("23e7"),i=n("e330"),o=n("d012"),a=n("861d"),s=n("1a2d"),l=n("9bf2").f,u=n("241c"),c=n("057f"),f=n("4fad"),d=n("90e3"),m=n("bb2f"),p=!1,h=d("meta"),v=0,b=function(e){l(e,h,{value:{objectID:"O"+v++,weakData:{}}})},g=function(e,t){if(!a(e))return"symbol"==typeof e?e:("string"==typeof e?"S":"P")+e;if(!s(e,h)){if(!f(e))return"F";if(!t)return"E";b(e)}return e[h].objectID},y=function(e,t){if(!s(e,h)){if(!f(e))return!0;if(!t)return!1;b(e)}return e[h].weakData},w=function(e){return m&&p&&f(e)&&!s(e,h)&&b(e),e},x=function(){_.enable=function(){},p=!0;var e=u.f,t=i([].splice),n={};n[h]=1,e(n).length&&(u.f=function(n){for(var r=e(n),i=0,o=r.length;i<o;i++)if(r[i]===h){t(r,i,1);break}return r},r({target:"Object",stat:!0,forced:!0},{getOwnPropertyNames:c.f}))},_=e.exports={enable:x,fastKey:g,getWeakData:y,onFreeze:w};o[h]=!0},fe1b:function(e,t,n){"use strict";n("bbd9")}}]);