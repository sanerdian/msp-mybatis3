//服务id  service_prefix.dict
var serviceId = "";

//接口地址
var url = '/dictxiangguanci';

//列表数据
var columns = [
    {type: 'checkbox', fixed: 'left'}
    , {type: 'numbers', title: '序号', fixed: 'left'}
    , { field: 'fenlei', title: '模块名称' }
    , {field: 'relatedword', title: '相关词'}
    , {field: 'wordname', title: '主题词'}
    , {field: 'shuxing', title: '主题词属性'}
 /*   , {field: 'status', title: '词条状态'}
    , {field: 'relatedword', title: '相关词'}
    , {field: 'tongyici', title: '相关词同义词'}
    , {field: 'pinyinword', title: '相关词拼音'}
    , {field: 'reltongyinci', title: '相关同音词'}
    , {field: 'sxtongyici', title: '属性同义词'}
    , {field: 'sxpinyin', title: '属性拼音'}
    , {field: 'sxtongyinci', title: '属性同音词'}*/
    , {field: 'zhutistopwordname', title: '停用词'}
    , {field: 'changjingci', title: '场景词'}
/*    , {field: 'rule', title: '规则'}*/
    , {field: 't1', title: '指标一级分类'}
    , {field: 't2', title: '指标二级分类'}
    , {field: 't3', title: '指标三级分类'}
    , {field: 't4', title: '指标四级分类'}
    , {field: 'crtime', title: '创建时间'}
    , {field: 'cruser', title: '创建人'}
    , {field: 'opertime', title: '修改时间'}
    , {field: 'operuser', title: '修改人'}
/*    , {field: 'operuser', title: '相关贴'}*/
    , {field: '', title: '操作', toolbar: '#operateTemplate', align: 'left', fixed: 'right', width: 200}
];
var form = layui.form, layer = layui.layer, tree = layui.tree, util = layui.util;
// $(document).ready(function() {
// select下拉框选中触发事件

// setListData(serviceId, url);
setListData(curr, defaultPageSize, serviceId, url);
// xiangguanciTable(1,15,serviceId, url,{});
getSearch(serviceId, url,curr, defaultPageSize);
btnClick();
setOpenLayer();
getXiangGuanciPinyin();


// function xiangguanciTable(curr, defaultPageSize,serviceId, url, entity) {
//     var jsonData = {
//         "pager": {
//             "current":curr,
//             "size": defaultPageSize,
//             "sortProps": [{
//                 "key": "crtime",
//                 "value": false
//             }]
//         },
//         "entity": entity
//     };
//     ajax(serviceId, url + '/list', 'post', JSON.stringify(jsonData)).then(function (data) {
//         setTableData1('#tableData', data.obj, columns)
//         curr=data.obj.current
//         defaultPageSize=defaultPageSize
//         var pegeObj={}
//         pegeObj.serviceId=serviceId
//         pegeObj.url=url
//         pegeObj.entity=entity
//         pegeObj.elem="page"
//         pegeObj.total=data.obj.total
//         pegeObj.curr=data.obj.current
//         pegeObj.size=defaultPageSize
//         loadPage1(pegeObj,xiangguanciTable)
//     })
// }

//多行合并
// function setTableData1(obj, data, columns) {
//     table.render({
//         elem: obj
//         , data: data.records
//         , limit: data.size
//         , cols: [columns]
//         , page: false,
//         done: function (res, curr, count) {
//             merge(res);
//         }

        
//     });
// }
//多行合并
// function merge(res) {
//     var data = res.data;
//     // console.log(data)
//     var mergeIndex = 0; //定位需要添加合并属性的行数
//     var mark = 1; //这里涉及到简单的运算，mark是计算每次需要合并的格子数
//     var columsName = ['numbers', 'wordname'];//需要合并的列名称
//     var columsIndex = [1, 2];//需要合并的列索引值
//     for (var k = 0; k < columsName.length; k++) { //这里循环所有要合并的列
//         var trArr = $(".layui-table-body>.layui-table").find("tr"); //所有行
//         for (var i = 1; i < res.data.length; i++) { //这里循环表格当前的数据
//             var tdCurArr = trArr.eq(i).find("td").eq(columsIndex[k]); //获取当前行的当前列
//             var tdPreArr = trArr.eq(mergeIndex).find("td").eq(columsIndex[k]); //获取相同列的第一列
//             if (data[i][columsName[k]] === data[i - 1][columsName[k]]) { //后一行的值与前一行的值做比较，相同就需要合并
//                 mark += 1;
//                 tdPreArr.each(function () { //相同列的第一列增加rowspan属性
//                     $(this).attr("rowspan", mark);
//                     console.log(this)
//                 });
//                 tdCurArr.each(function () { //当前行隐藏
//                     $(this).css("display", "none");
//                 });
//             } else {
//                 mergeIndex = i;
//                 mark = 1; //一旦前后两行的值不一样了，那么需要合并的格子数mark就需要重新计算
//             }
//         }
//         mergeIndex = 0;
//         mark = 1;
//     }
// }

//主题词下拉选
form.on('select(ztc_select)', function (data) {
    $("#shuxingKey").html("");
    $("#tingyongciKey").html("");
    $("#changjingciKey").html("");
    ajax(serviceId, '/shuxingci/sxcByZtc', 'get', {"wordName": data.value}).then(function (data) {
        var optionHtml = "";
        var dobj = data.obj.sxc;
        for (var i = 0; i < dobj.length; i++) {
            optionHtml += "<option value='" + dobj[i] + "'>" + dobj[i] + "</option>";
        }
        $("#shuxingKey").html(optionHtml);
        var tycoptionHtml = "";
        var tycdobj = data.obj.tyc;
        for (var i = 0; i < tycdobj.length; i++) {
            tycoptionHtml += "<option value='" + tycdobj[i] + "'>" + tycdobj[i] + "</option>";
        }
        $("#tingyongciKey").html(tycoptionHtml);
        $("#changjingciKey").html(tycoptionHtml);
        setRender();
    });
});

//获取相关词拼音
function getXiangGuanciPinyin() {
    var zhutici = $('#relatedword').val();
    if (!zhutici) return false;//停留在当前页面
    zhutici = zhutici.replace(/\s*/g, "");
    console.log("zhutici", zhutici);
// 获取全写拼音（调用js中的方法）
    var fullChars = pinyin.getFullChars(zhutici);
//给文本框赋值
    $('#pinyinword').val(fullChars);
};

//属性词拼音
function getShuXingciPinyin() {
    var zhutici = $('#shuxing').val();
    if (!zhutici) return false;//停留在当前页面
    zhutici = zhutici.replace(/\s*/g, "");
    console.log("zhutici", zhutici);
// 获取全写拼音（调用js中的方法）
    var fullChars = pinyin.getFullChars(zhutici);

//给文本框赋值
    $('#sxpinyin').val(fullChars);
}
var jsonData = {
    "pager": {
        "current": 1,
        "size":9999999,
        "sortProps": [
            {
                "key": "crtime",
                "value": false
            }
        ]
    },
    "entity": {}
};

//定义主题词
var themeName;
//定义场景词
var sceneName;
//设置主题词 相关的主题词信息
function setContactTheme(data){
    $('#themeContact [name="tongyici"]').val(data.obj.tongyici);
    $('#themeContact [name="pinyin"]').val(data.obj.pinyin);
    $('#themeContact [name="tongyinci"]').val(data.obj.tongyinci);
}

//设置主题词 相关的属性词信息
function setContactAttribute(id){    
    ajax(serviceId,'/dictshuxing/view/' + id,'post').then(function (data){
        if(data.success){
            $('#attributeContact [name="relationword"]').val(data.obj.relationword);
            $('#attributeContact [name="shuxingtongyici"]').val(data.obj.tongyinci);
            $('#attributeContact [name="shuxingpinyin"]').val(data.obj.pinyin);
        }
    })
}

//获取属性词数据列表
function getAttributeData(id,shuxing){
    var jsonData = {
        "entity": {
            "wordid": id
        },
        "pager": {
            "current": 1,
            "size": 100
        }
    }
    var dataArr = [];
    ajax(serviceId,'/dictshuxing/list','post',JSON.stringify(jsonData)).then(function (data){
        if(data.success){
            dataArr = data.obj.records;
            dataArr.forEach(function(item){
                $('#attributeWord').append("<option value='"+item.shuxingci+"' data-id='"+item.jmetashuxingciid+"'>"+item.shuxingci+"</option>");
            })
            if(shuxing != ''){              
                var shuxingSelect = $("#attributeWord").val(shuxing);
                console.log('shuxing2',shuxing)
                console.log('shuxingSelect',shuxingSelect)
            }else{              
                var selectId = $("#attributeWord").find("option:selected").attr("data-id");
            }     
            // console.log('selectId',selectId);  
            if(selectId){
                //获取属性词数据列表，相关的属性词信息
                setContactAttribute(selectId);
            }     
            //改变属性词，相关的属性词信息
            form.on('select(filterAttributeWord)', function(data){
                var selectId = $("#attributeWord").find("option:selected").attr("data-id");
                setContactAttribute(selectId);
            });
            setRender();
        }
    })
}

//获取停用词数据列表
function getStopData(id){
    var jsonData = {
        "entity": {
            "wordid": id
        },
        "pager": {
            "current": 1,
            "size": 100
        }
    }
    var dataArr = [];
    ajax(serviceId,'/dictstopword/list','post',JSON.stringify(jsonData)).then(function (data){
        if(data.success){
            dataArr = data.obj.records;
            dataArr.forEach(function(item){
                $('#stopWord').append("<option value='"+item.stopwordname+"' data-id='"+item.jmetastopwordid+"'>"+item.stopwordname+"</option>");
            })
            setRender(); 
        }
    })    
}
//获取场景词数据列表
function getSceneData(sceneWord){
	ajax(serviceId,'/dictchangjingci/list','post',JSON.stringify(jsonData)).then(function (data){
        if (data.success){
            var wordName = data.obj.records;
            var list = [];
            for (var i = 0; i < wordName.length; i++) {
                var obj = {};
                obj.name = wordName[i].changjingci;
                obj.value = wordName[i].changjingci;
                obj.id = wordName[i].jmetachangjingid;
                list.push(obj);
            }
            sceneName = xmSelect.render({
                el: '#sceneWord',
                radio: true,
                paging: true,
                pageSize: 5,
                filterable: true,
                data: list,
                name: 'changjingci'//修改name
            })
            sceneName.setValue([
                {name: sceneWord, value: sceneWord}
            ]);
        }
    })
}

//点击按钮
function btnClick() {
    $('.layui-btn').click(function () {
        var type = $(this).attr('data-type');
        var typetxt = $(this).text();
        var curName = $.trim($('.layui-side .layui-this').text());
        var tit = typetxt + curName;
        if (type == 'addDict') {
            setLayer('addDiv', tit, '1000px', '880px');
            //表单提交
            // formSubmitEdit(serviceId, url);
            $('#addDiv').html($('#addTemplate').html());            
			//获取主题词数据列表
			ajax(serviceId,'/zhutici/filter/list','post',JSON.stringify(jsonData)).then(function (data){
				laytpl($('#addTemplate').html()).render(data.obj, function(html){
					$('#addDiv').html(html);
					setRender();
                    //添加模块名称
                    addModuleName();
                    if (data.success) {
                        var wordName = data.obj.records;
						var list = [];
						for (var i = 0; i < wordName.length; i++) {
							var obj = {};
							obj.name = wordName[i].wordname;
							obj.value = wordName[i].wordname;
							obj.id = wordName[i].jmetasearwordid;
							list.push(obj);
						}
                        themeName = xmSelect.render({
                            el: '#themeWord',
                            radio: true,
                            paging: true,
                            pageSize: 5,
                            filterable: true,
                            data: list,
                            name: 'wordname',//修改name
                            on: function (data) {
                                //arr:  当前多选已选中的数据
                                var arr = data.arr;
                                // console.log('arr',arr);
								if(arr.length == 0){	
									$('[name="wordid"]').val('');							
								}else{
									var id = arr[0].id;
									$('[name="wordid"]').val(id);
								}
                                //获取主题词数据列表
                                ajax(serviceId,'/zhutici/view/' + id,'post').then(function (data){
                                    if(data.success){
                                        //主题词，相关的主题词信息
                                        setContactTheme(data);
                                        //获取属性词数据列表
                                        getAttributeData(id);
                                        //获取停用词数据列表
                                        getStopData(id);
                                    }
                                })
                            }
                        })
                    }
                    treeData();
                    //获取场景词数据列表
                    getSceneData();
				});
			});
            //表单提交
            formSubmitAdd(serviceId, url);
        } else if(type == 'editDict') {
            setLayer('editDiv', tit, '1000px', '880px');
            //表单提交
            formSubmitEdit(serviceId, url);
            // ajax(service_prefix.metadata,"/class/tree","post",JSON.stringify(jsondata)).then(function (data) {
            ajax(serviceId, '/zhutici/list','post',JSON.stringify(jsonData)).then(function (data) {
                laytpl($('#editTemplate').html()).render(data, function (html) {
                    $('#editDiv').html(html);
                    setRender();
                    treeData();
                });
            })
        } else if(type == 'delDict') {
            del(4);
        } else if (type == 'examDict') {//审核
            // formSubmitEdit(serviceId, url);
            // id = data.id;
            setLayer('examDiv', tit, '1000px', '880px');
            formSubmitEdit(serviceId, url);
            ajax(serviceId,'/zhutici/list','post',JSON.stringify(jsonData)).then(function (data){
                laytpl($('#examTemplate').html()).render(editdata, function (html) {
                    $('#examDiv').html(html);
                    setFormVal('editForm', editdata);
                    treeData();
                });
                formSubmitEditExam(serviceId,url,editdata.id);
            });
        }
    });
}
var zNodes;
var key;
function zTreeOnClick(event, treeId, treeNode) {
    $("input[name='t1']").val("");
    $("input[name='t2']").val("");
    $("input[name='t3']").val("");
    $("input[name='t4']").val("");
    if(treeNode.level==0){
        $("input[name='t1']").val(treeNode.className);
    }else  if(treeNode.level==1){
        $("input[name='t2']").val(treeNode.className);
        $("input[name='t1']").val(treeNode.getParentNode().className);
    }else  if(treeNode.level==2){
        $("input[name='t1']").val(treeNode.getParentNode().getParentNode().className);
        $("input[name='t2']").val(treeNode.getParentNode().className);
        $("input[name='t3']").val(treeNode.className);
    }else  if(treeNode.level==3){
        $("input[name='t1']").val(treeNode.getParentNode().getParentNode().getParentNode().className);
        $("input[name='t2']").val(treeNode.getParentNode().getParentNode().className);
        $("input[name='t3']").val(treeNode.getParentNode().className);
        $("input[name='t4']").val(treeNode.className);
    };
}
function treeData(flag){
    var moduleName = localStorage.getItem('dictionarySetupModuleName');
    var jsonData = {
        "moduleName": moduleName,
        "parentId": 0
    }
    ajax(service_prefix.metadata, '/class/treeByModule', 'post', JSON.stringify(jsonData)).then(function (data) {
        zNodes = data.obj;
        for (var i in data.obj) {
            if (data.obj[i].parentId == 0) {
                data.obj[i].icon = "../../../common/img/u5548.png";
            } else {
                data.obj[i].icon = "../../../common/img/u94.png";
            }
        }
        var setting = {
            data: {
                simpleData: {
                    enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
                    idKey: "id", //节点数据中保存唯一标识的属性名称
                    pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
                    rootPId: 0 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
                },
                key: {
                    name: "className" //zTree 节点数据保存节点名称的属性名称  默认值："name"
                }
            },
            view: {
                showLine: false,
                showIcon: true
            },
            callback: {
                onClick: zTreeOnClick
            }
        };
        function focusKey(e) {

        }
        var lastValue = "", nodeList = [], fontCss = {}, nodeClasses = [];
        function clickRadio(e) {
            lastValue = "";
            searchNode(e);
        }
        function clickButton(e, treeId, treeNode) {
            if ( $("#styleNodesByCSS").attr('checked') ) return;
            updateNodes(false, treeNode);
            updateNodes(true, treeNode)
        }
        function searchNode(e) {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            var value = $.trim(key.get(0).value);
            var keyType = "className";
            updateNodes(false);
            if (value === "") return;
            nodeList = zTree.getNodesByParamFuzzy(keyType, value);
            updateNodes(true);

        }
        function updateNodes(highlight, node = null) {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            var applyClasses = true;
            var expanded = node && node.open;

            // If expanding a node then it MUST be a parent
            // in which case it cannot be hiding matched nodes
            if ( applyClasses && expanded )
            {
                zTree.setting.view.fontCss['color']="#333";
                node.hiddenNodes = false;
                zTree.updateNode(node);
            }

            for( var i=0, l=nodeList.length; i<l; i++ )
            {
                nodeList[i].highlight = highlight;
                zTree.setting.view.fontCss['color']="#ff0000";
                nodeList[i].hiddenNodes = false;
                zTree.updateNode(nodeList[i]);
                if ( applyClasses && highlight )
                {
                    // Make parent nodes of matched nodes show the
                    // existence of hidden nodes if the parent is closed.
                    var node = nodeList[i];
                    while( true )
                    {
                        if ( ! node.parentTId ) break;
                        var parentNode = zTree.getNodeByTId( node.parentTId );
                        if ( parentNode.isParent && parentNode.open ) break;
                        parentNode.hiddenNodes = true;
                        zTree.updateNode( parentNode );
                        node = parentNode;
                    }
                }
                zTree.updateNode(nodeList[i]);
            }
        }
        function getFontCss(treeId, treeNode) {
            return {color:"#333", "font-weight":"normal"};
        }
        function getNodeClasses(treeId, treeNode) {
            var classes = ! ( !!treeNode.highlight || !!treeNode.hiddenNodes )
                ? {remove: ['highlight','highlight_alt','hiddennodes','highlight_hiddennodes']}
                : ( !!treeNode.highlight
                        ? ( (!!treeNode.hiddenNodes)
                                ? {add:['highlight','highlight_alt','hiddennodes']}
                                : {add:['highlight','highlight_alt']}
                        )
                        : {add:['hiddennodes','highlight_alt']}
                );
            return classes;
        }
        function filter(node) {
            return !node.isParent && node.isFirstNode;
        }

        function resetTree(e) {
            delete setting.view.fontCss;
            setting.view.nodeClasses = getNodeClasses;
            initTree();
            clickRadio(e);
        }
        function initTree() {
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
        //initTree();
        zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes); //初始化树
        zTreeObj.expandAll(flag); //true 节点全部展开、false节点收缩
        key = $("#key");
        key.bind("focus", focusKey)
            .bind("propertychange", searchNode)
            .bind("input", searchNode);
    });
}

//列表中删除、编辑、详情
table.on('tool(tableData)', function(obj){
	var data = obj.data;
	var layEvent = obj.event;
	var tr = obj.tr;

	var curName = $('.layui-side .layui-this').text();
	console.log('curName2',data)
	if (layEvent == 'del') { //删除
		layer.alert("确定删除:[" + data.wordname + "]吗？", function () {
			doDeleteById(serviceId,url,data.jmetawordrelationid);
		})
	} else if (layEvent == 'edit') {//编辑
		//表单提交
		formSubmitEdit(serviceId, url);
		// id = data.id;
		id = data.jmetawordrelationid;
		var typetxt = $(this).text();
		var curName = $.trim($('.layui-side .layui-this').text());
		var tit = typetxt + curName;
        setLayer('editDiv', tit, '1000px', '880px');
		//获取主题词数据
		ajax(serviceId,'/zhutici/allList','post',JSON.stringify(jsonData)).then(function (resData) {
			laytpl($('#editTemplate').html()).render(resData.obj, function (html) {
				$('#editDiv').html(html);
				// console.log('edit',data);
				var themeWord = data.wordname;
                console.log('themeWord',themeWord);
				if (resData.success) {
					// console.log('data', data);
					var wordName = resData.obj.records;
					var list = [];
                    for (var i = 0; i < wordName.length; i++) {
                        var obj = {};
                        obj.name = wordName[i].wordname;
                        obj.value = wordName[i].wordname;
                        obj.id = wordName[i].jmetasearwordid;
                        list.push(obj);
                    }
					themeName = xmSelect.render({
						el: '#themeWord',
                        radio: true,
						paging: true,
						pageSize: 5,
						filterable: true,
						data: list,
						name: 'wordname'//修改name
					})
					themeName.setValue([
						{name: themeWord, value: themeWord}
					]);
				}
				setRender();
				treeData(true);
                //场景词返显
                var sceneWord = data.changjingci;
                //获取场景词数据列表
                getSceneData(sceneWord);
                var id = data.wordid;
                //获取属性词数据列表
                var shuxing = data.shuxing;
                console.log('shuxing',shuxing);
                getAttributeData(id,shuxing);
                $('[name="relationword"]').val(shuxing);
                //获取停用词数据列表
                getStopData(id);
				setFormVal('editForm', data);
			});
		})
	} else if (layEvent == 'detail') { //详情
        console.log('detail',data);
        setLayer('detailDiv', tit, '800px', '730px');
        if(data.modifyTime==null){
            data.modifyTime = '';
        }
        getData(data, '#detailTemplate', '#detailDiv');
        treeData(true);
	}
});


//属性词管理属性词拼音,属性词同义词,属性词同音词
form.on('select(sxc)', function (data) {
    var zxc = $("select[name='wordname']").val();
    ajax(serviceId, '/shuxingci/shuxingyouguanByZtc', 'get', {"wordName": zxc, "shuxingci": data.value}).then(function (data) {
        var obj = data.obj;
        var dobj = obj[0];
        $("input[name='sxpinyin']").val(dobj.pinyin);
        $("input[name='sxtongyici']").val(dobj.tongyici);
        $("input[name='sxtongyinci']").val(dobj.tongyinci);
        setRender();
    });
});

formSubmitAdd(serviceId, url);
