var classIndex;
var siteId = "";
var zTreeObj;
var ip;
var username;
var leftSort = {key: "crtime", value: false};
var searchObj = {};
layui.use('table', function () {
    var $ = layui.$
        , layer = layui.layer
        , table = layui.table
        , form = layui.form;
    ajax(service_prefix.member, "/user/getBaseInfo", "post").then(function (data) {
        if (data.success) {
            username = data.obj.name;
        }
    });
    ajax(service_prefix.member, "/user/getCurrentIp", "post").then(function (data) {
        if (data.success) {
            ip = data.msg;
        }
    });
    form.on('submit(addMetadata)', function (data) {
        if (data.field.id) {
            doEditMetadata(data.field);
            ajax(service_prefix.log, "/metadata", "post", JSON.stringify({
                handleType: "修改元数据表",
                name: data.field.tablename,
                address: ip,
                crUser: username
            })).then(function (data) {
                if (data.success) {

                }
            });
        } else {
            doAddMetadata(data.field);
            ajax(service_prefix.log, "/metadata", "post", JSON.stringify({
                handleType: "新建元数据表",
                name: data.field.tablename,
                address: ip,
                crUser: username
            })).then(function (data) {
                if (data.success) {
                    ip = data.msg;
                }
            });
        }
        return false;
    });
    form.on('submit(addView)', function (data) {
        doAddView(data.field);
        return false;
    });
    form.on('submit(selectChildClass)', function (data) {
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getSelectedNodes();
        layui.form.val("fieldForm", {className: nodes[0].className, classId: nodes[0].id});
        $("#className").text(nodes[0].className);
        layer.close(classIndex);
        return false;
    });
});
/*active.addView = function(data){
  doAddView(data.field);
};*/
active.generateCodes = function () {
    layerOpen('选择站点', 502, 452);
    ajax(service_prefix.manage, "/programa/getTreeData", "post", {}).then(res => {
        lodTpl("site", "openDiv", res.obj);
        zTreeObj = $.fn.zTree.init($("#treeDemo1"), settingss, res.obj); //初始化树
        zTreeObj.expandAll(true); //true 节点全部展开、false节点收缩
    })
}
active.generateTpl = function () {
    getMetadataUri(metadataObj.id).then(function (result) {
        var metadataUri = result;

        var fields;

        var columnid = zTreeObj.getSelectedNodes()[0].id;
        var siteid = 0;
        var siteName = "";
        var treeParents = zTreeObj.getSelectedNodes()[0].getPath();
        treeParents.forEach(function (res) {
            if (res.level = 2) {
                siteid = res.id;
                siteName = res.name;
            }
        })
        ajax(service_prefix.metadata, "/fieldinfo/all", "get", {tableId: metadataObj.id}).then(res => {
            fields = res.obj;
            ajax(service_prefix.manage, "/manage/template/2", "get", {}).then(res1 => {
                var html = res1.obj.temphtml;
                var groupHtml = $(html).find("jnetGroup").html();
                var ueProStr = [];
                var fieldHtml = $(groupHtml).find("jnetFields").html();
                var html1 = "";
                for (var i in fields) {
                    var groupitem = groupHtml;
                    var itemiiii = $(groupitem);
                    itemiiii.find("jnetGroupName").replaceWith(i);
                    var html2 = "";
                    fields[i].forEach(function (res2) {
                        var itemmmm = $(fieldHtml);
                        itemmmm.find("jnetFieldName").replaceWith(res2.anothername);
                        var fieldItemHtml = "";
                        switch (res2.fieldtype) {
                            case 1:
                                fieldItemHtml = '<input type="text" name="' + res2.proName + '" placeholder="请输入" autocomplete="off" class="layui-input">';
                                break;
                            case 2:
                                fieldItemHtml = '<input type="password" name="' + res2.proName + '" placeholder="请输入" autocomplete="off" class="layui-input">';
                                break;
                            case 3:
                                fieldItemHtml = '<textarea name="' + res2.proName + '" placeholder="请输入" class="layui-textarea"></textarea>';
                                break;
                            case 4:
                                fieldItemHtml = '<input type="text" name="' + res2.proName + '" id="date_{{item1.proName}}" lay-verify="date_{{item1.proName}}" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">';
                                break;
                            case 6:
                                var enmvalue = res2.enmvalue.split(",");
                                for (var i in enmvalue) {
                                    fieldItemHtml += '<input type="radio" name="' + res2.proName + '" value="' + enmvalue[i] + '" title="' + enmvalue[i] + '">';
                                }
                                break;
                            case 7:
                                var enmvalue = res2.enmvalue.split(",");
                                for (var i in enmvalue) {
                                    fieldItemHtml += '<input type="checkbox" name="check_{{item1.proName}}_{{enmvalue[i]}}" lay-skin="primary" title="' + enmvalue[i] + '">';
                                }
                                break;
                            case 8:
                                fieldItemHtml += '<select name="' + res2.proName + '" lay-filter="' + res2.proName + '">';
                                var enmvalue = res2.enmvalue.split(",");
                                for (var i in enmvalue) {
                                    fieldItemHtml += '<option value="' + enmvalue[i] + '">' + enmvalue[i] + '</option>';
                                }
                                fieldItemHtml += '</select>';
                                break;
                            case 15:
                                fieldItemHtml += '<div class="layui-upload-list">';
                                fieldItemHtml += '<img class="layui-upload-img" style="max-width: 200px;max-height: 200px;" id="img_' + res2.proName + '">';
                                fieldItemHtml += '<p id="demoText"></p>';
                                fieldItemHtml += '</div>';
                                fieldItemHtml += '<button type="button" class="layui-btn" id="upbtn_' + res2.proName + '">上传图片</button>';
                                fieldItemHtml += '<input type="hidden" id="up_' + res2.proName + '" name="' + res2.proName + '">';
                                break;
                            case 18:
                                fieldItemHtml += '<li style="width: 100%;height: 400px"><textarea style="width: 98%;" name="' + res2.proName + '" id="ue_' + res2.proName + '" placeholder="请输入"></textarea>';
                                ueProStr.push(res2.proName);
                                break;
                            case 13:
                                fieldItemHtml += '<select name="' + res2.proName + '" lay-filter="' + res2.proName + '">';
                                ajax(service_prefix.metadata, "/class/all", "post", JSON.stringify({parentId: res2.classid})).then(function (res3) {
                                    if (res3.success) {
                                        res3.obj.forEach(res4 => {
                                            fieldItemHtml += '<option type="checkbox" value="' + res4.id + '"  title="' + res4.className + '">' + res4.className + '</option>';
                                        })
                                        fieldItemHtml += '</select>';
                                    }
                                });
                                break;
                        }
                        itemmmm.find("jnetFieldPro").replaceWith(fieldItemHtml);
                        html2 += itemmmm.prop("outerHTML");
                    })
                    itemiiii.find("jnetFields").replaceWith(html2);
                    var temdiv = $("<temdiv></temdiv>");
                    temdiv.append(itemiiii);
                    html1 += temdiv.html();
                }
                html = html.replace("<jnetGroup>", html1 + "<jnetGroup>");
                html = html.replace("<jnetMetadataUri>", metadataUri);
                html = html.replace("<jnetuearr>", JSON.stringify(ueProStr));

                var tplObj = {};
                tplObj.tempname = siteName + "：" + metadataObj.name + "编辑模板";
                tplObj.tempext = "html";
                tplObj.temphtml = html;
                tplObj.outputfilename = "edit";
                tplObj.temptype = 3;
                tplObj.siteid = siteid;
                tplObj.columnid = columnid;
                ajax(service_prefix.manage, "/manage/template/list", "post", JSON.stringify({
                    pager: {
                        size: 1,
                        current: 1
                    }, entity: {columnid: columnid, temptype: 3}
                })).then(tplList => {
                    if (tplList.obj.records.length > 0) {
                        ajax(service_prefix.manage, "/manage/template/" + tplList.obj.records[0].id, "put", JSON.stringify(tplObj)).then(function(data) {
                            if (res.success) {
                                layer.closeAll();
                            }
                        })
                    } else {
                        ajax(service_prefix.manage, "/manage/template", "post", JSON.stringify(tplObj)).then(function(data) {
                            if (res.success) {
                                layer.closeAll();
                            }
                        })
                    }
                })
            })
            ajax(service_prefix.manage, "/manage/template/1", "get", {}).then(res1 => {
                var html = res1.obj.temphtml;
                html = html.replace("<jnetMetadataUri>", metadataUri);
                var titles = $(html).find(".jnetTableTitle").prop("outerHTML");
                var tabletitlehtml = "";
                var tableBodyTplHtml = "<tr>\n" +
                    "            <td>{{index+1}}</td>\n";
                ;
                for (var i in fields) {
                    fields[i].forEach(function (res2) {
                        if (res2.showList) {
                            var titlesObj = $(titles);
                            titlesObj.find("jnetTableTitleName").replaceWith(res2.anothername);
                            tabletitlehtml += titlesObj.prop("outerHTML");

                            tableBodyTplHtml += "<td>{{item." + res2.proName + "}}</td><!--" + res2.anothername + "-->\n";
                        }
                    })
                }

                tableBodyTplHtml += "        </tr>"
                html = html.replace(titles, tabletitlehtml);
                html = html.replace("<jnetlistbody>", tableBodyTplHtml);
                html = html.replace("<jnetcolumnid>", columnid);
                html = html.replace("<jnetfields>", JSON.stringify(fields));
                var tplObj = {};
                tplObj.tempname = siteName + "：" + metadataObj.name + "列表模板";
                tplObj.tempext = "html";
                tplObj.temphtml = html;
                tplObj.outputfilename = "list";
                tplObj.temptype = 1;
                tplObj.siteid = siteid;
                tplObj.columnid = columnid;
                ajax(service_prefix.manage, "/manage/template/list", "post", JSON.stringify({
                    pager: {
                        size: 1,
                        current: 1
                    }, entity: {columnid: columnid, temptype: 1}
                })).then(tplList => {
                    if (tplList.obj.records.length > 0) {
                        ajax(service_prefix.manage, "/manage/template/" + tplList.obj.records[0].id, "put", JSON.stringify(tplObj)).then(function(data) {
                            if (res.success) {
                                layer.closeAll();
                            }
                        })
                    } else {
                        ajax(service_prefix.manage, "/manage/template", "post", JSON.stringify(tplObj)).then(function(data) {
                            if (res.success) {
                                layer.closeAll();
                            }
                        })
                    }
                })
            })
            ajax(service_prefix.manage, "/manage/template/3", "get", {}).then(res1 => {
                var html = res1.obj.temphtml;
                html = html.replace("<jnetMetadataUri>", metadataUri);
                var itemHtml = "";
                for (var i in fields) {
                    fields[i].forEach(function (res2) {
                        itemHtml += "<li><b>" + res2.anothername + "：</b><span><jnet d=" + res2.proName + "/></span></li>\n";
                    })
                }
                html = html.replace("<jnetitems>", itemHtml);
                var tplObj = {};
                tplObj.tempname = siteName + "：" + metadataObj.name + "详情模板";
                tplObj.tempext = "html";
                tplObj.temphtml = html;
                tplObj.outputfilename = "detail";
                tplObj.temptype = 2;
                tplObj.siteid = siteid;
                tplObj.columnid = columnid;
                ajax(service_prefix.manage, "/manage/template/list", "post", JSON.stringify({
                    pager: {
                        size: 1,
                        current: 1
                    }, entity: {columnid: columnid, temptype: 2}
                })).then(tplList => {
                    if (tplList.obj.records.length > 0) {
                        ajax(service_prefix.manage, "/manage/template/" + tplList.obj.records[0].id, "put", JSON.stringify(tplObj)).then(function(data) {
                            if (res.success) {
                                layer.closeAll();
                            }
                        })
                    } else {
                        ajax(service_prefix.manage, "/manage/template", "post", JSON.stringify(tplObj)).then(function(data) {
                            if (res.success) {
                                layer.closeAll();
                            }
                        })
                    }
                })
            })
        })
    })
}
active.createCodes = function () {
    var tableName = metadataObj.name;
    if (!tableName) {
        layer.alert("请选择表");
        return false;
    }
    var params = {};
    params.tableNames = tableName;
    if (tableName.indexOf("_") > 0) {
        params.prefixes = tableName.split("_")[0] + "_";
    }
    ajax(service_prefix.metadata, "/searchList", "post", JSON.stringify(tableName)).then(data1 => {
        if (data1.obj[0].ownerid != 0) {
            ajax(service_prefix.metadata, "/moduleinfo/" + data1.obj[0].ownerid, "get", {}).then(data2 => {
                params.moduleNames = data2.obj.englishname;
                params.id = data2.obj.id;
                params.ttype = 0;
                ajax2(service_prefix.generator, "/module/createCode", "post", params).then(res => {
                    if (res.success) {
                        ajax3(service_prefix.metadata, "/crtGeneTime" , "post", JSON.stringify(tableName)).then(res => {
                            if(res.success){
                                layer.alert("生成成功！");
                            }
                        })
                    }
                })
            });
        } else {
            layer.alert("请为该表选择模块！");
            return false;
        }
    });
}
active.mvJar = function () {
    ajax2(service_prefix.generator, "/module/mvnPackage", "post", {}).then(function (res2) {
        if (!res2.success) {
            layer.alert(res2.msg);
            return false;
        }
        ajax(service_prefix.metadata, "/fieldinfo/updateState", "post", metadataObj.id).then(res3 => {
            if (res3.success) {
                layer.alert("部署成功!");
            } else {
                layer.alert(res3.msg);
            }
        });
    })
}
active.delCodes = function () {
    var tableName = metadataObj.name;
    if (!tableName) {
        layer.alert("请选择表");
        return false;
    }
    var params = {};
    params.tableNames = tableName;
    if (tableName.indexOf("_") > 0) {
        params.prefixes = tableName.split("_")[0] + "_";
    }
    layer.open({
        content: "确认删除应用吗？"
        , btn: ['确认', "取消"]
        , yes: function (index, layero) {
            ajax(service_prefix.metadata, "/searchList", "post", JSON.stringify(tableName)).then(data1 => {
                if (data1.obj[0].ownerid != 0) {
                    ajax(service_prefix.metadata, "/moduleinfo/" + data1.obj[0].ownerid, "get", {}).then(data2 => {
                        params.moduleNames = data2.obj.englishname;
                        params.id = data2.obj.id;
                        ajax2(service_prefix.generator, "/module/delCodes", "post", params).then(res => {
                            if (res.success) {
                                layer.alert("删除成功");
                                ajax(service_prefix.metadata, "/fieldinfo/deleteState", "post", metadataObj.id).then(res3 => {
                                    if (res3.success) {
                                        doList(metadataObj.id);
                                    } else {
                                        layer.alert(res3.msg);
                                    }
                                });
                            } else {
                                layer.alert(res.msg);
                            }
                        }).catch(function (res) {
                            }
                        )
                    })
                }
            })
        }
        , btn2: function (index, layero) {
            layer.closeAll();
        }
    });

}
active.delFields = function () {
    doDeleteFields();
};
active.editField = function () {
    editField();
};
function sameCreate () {
    if (checkChecked('fieldTabel')) {
        var data = getTableCheck("fieldTabel");
        layer.open({
            type: 1,
            title: "类似创建字段",
            area: ['550px', '780px'],
            maxmin: true,
            content: '<div id="fieldForm"></div>'
        });
        var field = data[0];
        field.id = "";
        layui.laytpl($("#fieldTemplate").html()).render(data[0], function (html) {
            $("#fieldForm").html(html);
            getDict(data[0].fieldtype);
            getDbDict(data[0].dbtype);
            getGropu(data[0].groupid);
            layui.form.val("fieldForm", data[0]);
            layui.form.render();
        });
    }
};

function searchFieldByName(){
    searchObj = {searchName:$("#searchFieldInput").val()};
    console.log("1")
    doList(1);
}

layui.use('layer', function () { //独立版的layer无需执行这一句
    var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

    layui.use(['form'], function () {
        var form = layui.form
            , layer = layui.layer
    })
    layui.use('layer', function () { //独立版的layer无需执行这一句
        var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

        $('#select').on('click', select);

        function select() {
            var that = this;
            layer.open({
                type: 1,
                title: "选择模板",
                area: ['542px', '409px'],
                maxmin: true,
                content: $("#form1")
            });
        }
    })
})
var settingss = {
    data: {
        simpleData: {
            enable: true, //true 、 false 分别表示 使用 、 不使用 简单数据模式
            idKey: "id", //节点数据中保存唯一标识的属性名称
            pIdKey: "parentId", //节点数据中保存其父节点唯一标识的属性名称
            rootPId: -1 //用于修正根节点父节点数据，即 pIdKey 指定的属性值
        },
        key: {
            name: "name" //zTree 节点数据保存节点名称的属性名称  默认值："name"
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

function zTreeOnClick(event, treeId, treeNode) {
}
var metadataObj = {};
var moduleObj = {};

function getSiteById(id) {
    siteId = id;

}

layui.form.on("select(fieldType)", function (data) {
    var dbtype = $(data.elem).find("option:selected").attr("dbtype")
    console.log("打印结果为",data.value,dbtype)
    validateFieldType(data.value,dbtype);
})

function validateFieldType(fieldtype,dbtype) {
    $(".buguanlian").show();
    $(".field_class").hide();
    $("#enum_tr").hide();
    $("#dbtype").val(dbtype);
    $("#dbtype").attr("disabled", "disabled")
    $(".guanlian").hide();

    if(fieldtype == 13) $(".field_class").show();
    if(fieldtype == 18 || fieldtype == 3) $("#dblength_tr").hide();
    if(fieldtype == 6 || fieldtype == 7 || fieldtype == 8) $("#enum_tr").show();
    if(fieldtype == 5) $("#dbtype").removeAttr("disabled", "disabled")
    if(dbtype == -1) {
        $(".buguanlian").hide();
        $(".guanlian").show()
    }

    if (fieldtype != 18) {
        $("#dbLengthDiv").html('<input type="text" id="dbLength" name="dblength" class="layui-input">');
    } else {
        $("#dbLengthDiv").html('<select id="dbLength" name="dblength" class="layui-input">' +
            '<option value = "500">500</option>' +
            '<option value = "1000">1000</option>' +
            '<option value = "2000">2000</option>' +
            '<option value = "4000">4000</option>' +
            '</select>');
    }
    layui.form.render();
}

function doDeleteFields() {
    var data = checkChecked('fieldTabel');
    if (data) {
        layer.open({
            content: "确认删除表字段吗？"
            , btn: ['确认', "取消"]
            , yes: function (index, layero) {
                ajax(service_prefix.metadata, "/fieldinfo/delByIds", "POST", JSON.stringify(data)).then(res => {
                    if (res.success) {
                        doList(1);
                        layer.closeAll();
                    } else {
                        layer.alert("删除失败");
                    }
                }).catch(function (res) {
                    }
                )
            }
            , btn2: function (index, layero) {
                layer.closeAll();
            }
        });
    }
}

function checkChoice() {
    if (!metadataObj.id) {
        layer.alert("请选择菜单");
        return false;
    }
    return true;
}

function doEditField(data) {
    data.showDetail = data.showDetail == "on" ? 1 : 0;
    data.showList = data.showList == "on" ? 1 : 0;
    data.showSearch = data.showSearch == "on" ? 1 : 0;
    data.issort = data.issort == "on" ? 1 : 0;
    if(data.fieldtype == 4) data.dblength = 0;
    ajax(service_prefix.metadata, "/fieldinfo/" + data.id, "put", JSON.stringify(data)).then(res => {
        if (res.success) {
            doList(1);
            layer.closeAll();
        } else {
            layer.alert(res.msg?res.msg:"修改失败");
        }
    })
}

function doAddMetadata(data) {
    data.tablename = data.preTbm + data.tablename;
    ajax(service_prefix.metadata, "/add", "post", JSON.stringify(data)).then(res => {
        if (res.success) {
            layer.closeAll();
            getMetadatas();
        } else {
            layer.alert(res.msg?res.msg:"添加失败");
        }
    })
}

function doAddView(data) {
    data.viewname = data.viewTbm + data.viewname;
    ajax(service_prefix.metadata, "/create?id=" + metadataObj.id + "&viewname=" + data.viewname, "post", data.viewsf).then(function(data) {
        if (data.success) {
            layer.alert(data.msg);
            setTimeout(function () {
                layer.closeAll();
                getMetadatas();
            }, 500);

        } else {
            layer.alert(data.msg);
        }
    })
}

function doEditMetadata(data) {
    ajax(service_prefix.metadata, "/" + data.id, "put", JSON.stringify(data)).then(res => {
        if (res.success) {
            layer.closeAll();
            getMetadatas();
        } else {
            layer.alert(res.msg, function () {
                layer.closeAll();
            });
        }
    })
}

function getMetadatas(pageNo,size,fn) {
    if(!pageNo) pageNo = 1;
    if(!size) size = 15;
    var jsondata = {
        entity: {
           searchName: $("#searchTable").val()
        },
        "pager": {
            "current": pageNo,
            "size": size,
            sortProps: [leftSort]
        }
    }
    ajax(service_prefix.metadata, "/list", "post", JSON.stringify(jsondata)).then(function(data) {
        if (data.success) {
            lodTpl("leftItemTpl","listForm",data.obj.records);
            loadPageLeft("pageleft",data.obj,getMetadatas);
            $(".left-list .jnet-left-item:first").trigger("click");
            if(fn){
                fn();
            }
        }
    });
}

function getDict(id) {
    ajax(service_prefix.metadata, "/dict/list", "post", JSON.stringify("field_type")).then(function(data) {

        for (var i in data) {
            $("#fieldtype").append("<option value='" + data[i].no + "' dbtype='"+data[i].code+"'>" + data[i].name + "</option>");
        }
        layui.form.render('select');
        if (!id) {
            id = 1;
        }
        layui.form.val("fieldForm", {fieldtype: id});
    })
}

//获得分类法分类
function getClass(id) {
    var jsondata = {
        "entity": {
            "parentId": 0
        },
        "pager": {
            "current": 1,
            "size": 20
        }
    }
    ajax(service_prefix.metadata, "/class/list", "post", JSON.stringify(jsondata)).then(function(data) {
        for (var i in data.obj.records) {
            $("#class").append("<option value='" + data.obj.records[i].id + "'>" + data.obj.records[i].className + "</option>");
        }
        layui.form.val("fieldForm", {classparentid: id});
        layui.form.render("select");
    })
}

function getClass2(id) {
    selectClass2(id);
}

layui.form.on('select(classfilter)', function (data) {
    moduleObj.id = data.value;
    layui.form.val("fieldForm");
    layui.form.render('select');
})

function selectClass2(id) {
    var jsondata = {
        "entity": {
            "id": id
        },
        "pager": {
            "current": 1,
            "size": 20
        }
    }
    ajax(service_prefix.metadata, "/class/list", "post", JSON.stringify(jsondata)).then(function(data) {
        layui.form.val("fieldForm", {classId: id, className: data.obj.records[0].className});
        $("#className").text(data.obj.records[0].className);
        layui.form.render();
    })
}

function getDbDict(id) {
    ajax(service_prefix.metadata, "/dict/list", "post", JSON.stringify("db_type")).then(function(data) {
        for (var i in data) {
            $("#dbtype").append("<option value='" + data[i].no + "'>" + data[i].name + "</option>");
        }
        layui.form.render('select');
        if (!id) {
            id = 2;
        }
        layui.form.val("fieldForm", {dbtype: id});
    })
}

function getGropu(id) {
    ajax(service_prefix.metadata, "/group/all", "post", {}).then(function(data) {
        for (var i in data.obj) {
            $("#groupid").append("<option value='" + data.obj[i].id + "'>" + data.obj[i].name + "</option>")
        }
        layui.form.render('select');
        layui.form.val("fieldForm", {groupid: id});
    })
}

function showDesc(obj){
    var desc = $(".jnet-left-item.active").attr("data_desc");
    layer.tips(desc, obj,{tips: [2], time: 5000});
}
function showSearchBody(){
    $(".jnet-search-body").show();
    getdict1();
    getDbDict1();
}


function closeSearchBody(){
    $(".jnet-search-body").hide();
}
layui.laydate.render({
    elem: '.jnet-search-crtime'
    , range: true
});
function getCurrObj(){
    return $(".jnet-left-item.active");
}
function getCurrObjId(){
    return $(getCurrObj()).attr("data_id");
}

layui.form.on("submit(searchTable)",function(data){
    var time = data.field.createTime;
    if(time){
        var times = time.split(" - ");
        data.field.crtimeTo = times[1];
        data.field.createTime = times[0];
        searchObj = data.field;
    }else {
        searchObj = data.field;
    }
    console.log(2)
    doList(1);
    return false;
})


function doList(pageNo) {
    searchObj.tableid = metadataObj.id;
    var jsondata = {
        "entity": searchObj,
        "pager": {
            "current": pageNo,
            "size": 15,
            sortProps: [{key: "fieldorder", value: true}, {key: "id", value: true}]
        }
    }

    ajax(service_prefix.metadata, "/fieldinfo/list", "post", JSON.stringify(jsondata)).then(function(data) {
        var cols = [
            {type: 'checkbox', fixed: 'left'}
            , {type: 'numbers', title: '序号', width: 50, fixed: 'left'}
            , {field: 'anothername', title: '中文名称', width: 150}
            , {field: 'fieldname', title: '英文名称', width: 150}
            , {field: 'fieldTypeStr', title: '字段类型',width:90}
            , {field: 'dbTypeStr', title: '库中类型',width:90}
            , {field: 'dblength', title: '库中长度',width:90}
            , {field: 'groupname', title: '所属分组', width: 150}
            , {field: '', title: '排序', width: 60, templet: "#sortTpl", fixed: 'right'}
            , {field: 'width', title: '列宽', width: 70, edit: 'text', fixed: 'right'}
        ]
        renderRightTable("fieldTabel",cols,data.obj.records);
        $("tbody:first").attr("id", 'foo')
        Sortable.create(document.getElementById('foo'), {
            animation: 150, //动画参数
            handle: '.sort-icon', // handle's class
            onEnd: function (evt) { //拖拽完毕之后发生该事件
                var id = $(evt.item).find(".sort-icon").attr("data-id");
                var sort = $(evt.item).next().find(".sort-icon").attr("data-sort");
                ajax(service_prefix.metadata, "/fieldinfo/sort", "put", JSON.stringify({
                    fieldorder: sort,
                    id: id,
                    tableid: metadataObj.id
                })).then(sortRes => {
                    doList(pageNo);
                })
            }
        });
        loadPage2('fieldPage',data.obj,doList);
        // layui.laypage.render({
        //     elem: 'fieldPage'
        //     , count: data.obj.total //数据总数
        //     , curr: data.obj.current
        //     , prev: '<i class="layui-icon">&#xe603;</i>'
        //     , next: '<i class="layui-icon">&#xe602;</i>'
        //     , layout: ['count', 'prev', 'page', 'next', 'refresh', 'skip']
        //     , limit: 14
        //     , jump: function (obj, first) {
        //         if (!first) {
        //             doList(obj.curr);
        //         }
        //     }
        // });
    })
}

//监听修改宽度
layui.table.on('edit(fieldTabel)', function (obj) {
    var value = obj.value //得到修改后的值
        , data = obj.data //得到所在行所有键值
        , field = obj.field; //得到字段
    if (data.showList == 1) {
        ajax3(service_prefix.metadata, "/fieldinfo/" + data.id, "put", JSON.stringify({width: value})).then(res => {
        })
    } else {
        layer.msg("不可编辑");
    }

});


//生成视图
function create() {
    var fields = [];
    var data = getTableCheck("fieldTabel");
    if (data.length <= 0) {
        layer.alert("请选择表字段！");
        return false;
    }
    data.forEach(function (res) {
        fields.push(res.id);
    })
    createView(fields);
}

function del() {
    var ids = [];
    var tablename = $('.active .layui-input-block p').html();
    var ip;
    var username;
    ajax(service_prefix.member, "/user/getBaseInfo", "post").then(function (data) {
        if (data.success) {
            username = data.obj.name;
        }
    });
    ajax(service_prefix.member, "/user/getCurrentIp", "post").then(function (data) {
        if (data.success) {
            ip = data.msg;
        }
    });
    $("input[name='listid']:checked").each(function () {
        ids.push($(this).val());
    })
    if (ids.length <= 0) {
        layer.alert("请选择元数据");
        return false;
    }
    layer.open({
        content: "确认删除该元数据表吗？"
        , btn: ['确认', "取消"]
        , yes: function (index, layero) {
            ajax(service_prefix.metadata, "/delByIds", "post", JSON.stringify(ids)).then(function(data) {
                if (data.success
                ) {
                    getMetadatas();
                    layer.close(index);
                    ajax(service_prefix.log, "/metadata", "post", JSON.stringify({
                        handleType: "删除元数据表",
                        name: tablename,
                        address: ip,
                        crUser: username
                    })).then(function (data) {
                        if (data.success) {

                        }
                    });
                } else {
                    layer.alert(data.msg);
                }
            }).catch(res => {
                }
            )
        }
        , btn2: function (index, layero) {
            layer.closeAll();
        }
    });
}


function add() {
    //多窗口模式，层叠置顶
    layer.open({
        type: 1,
        title: "新建元数据",
        area: ['500px', '300px'],
        maxmin: true,
        content: '<div id="addMetadataForm"></div>',

    });
    layui.laytpl($("#metadataTemplate").html()).render({edit: false}, function (html) {
        $("#addMetadataForm").html(html)
    });
    getvisual();
    layui.form.val("metadataForm", {preTbm: "JMETA_", ownerid: 0});
}

function edit() {
    if (checkChoice()) {
        ajax(service_prefix.metadata, "/" + metadataObj.id, "get", {}).then(function(data) {
            layer.open({
                type: 1,
                title: "修改元数据",
                area: ['500px', '300px'],
                maxmin: true,
                content: '<div id="addMetadataForm"></div>',
            });
            layui.laytpl($("#metadataTemplate").html()).render({edit: true}, function (html) {
                $("#addMetadataForm").html(html)
            });
            layui.form.val("metadataForm", data.obj);
        })
    }
}

function createView(viewsf) {
    //多窗口模式，层叠置顶
    layer.open({
        type: 1,
        title: "视图命名",
        area: ['600px', '230px'],
        maxmin: true,
        content: '<div id="viewForm"></div>',
    });
    layui.laytpl($("#viewTemplate").html()).render({}, function (html) {
        $("#viewForm").html(html)
    });
    layui.form.val("viewForm", {viewTbm: "VIEW_JMETA_", viewsf: JSON.stringify(viewsf)});
}

active.getChildClass = function () {
    //多窗口模式，层叠置顶
    classIndex = layer.open({
        type: 1,
        title: "选择子分类法",
        area: ['340px', '600px'],
        maxmin: true,
        content: '<div id="addChildTreeForm" class="h100"></div>',
    });
    layui.laytpl($("#childtreeTemplate").html()).render({}, function (html) {
        $("#addChildTreeForm").html(html);
        getChildTree();
    });

}

function getChildTree() {
    var jsondata = {
        "parentId": moduleObj.id
    }
    ajax(service_prefix.metadata, "/class/tree", "post", JSON.stringify(jsondata)).then(function(data) {
        var res = data.obj;
        if (res) {
            showTree(res);
        } else {
        }

    })
}

function importTable() {
    $("#importTableTag").trigger("click");
}

var upload2;
function uploadData2() {
    upload2.reload({
        url: getAjaxUrl(service_prefix.metadata, "/importTable?id=" + metadataObj.id)
    });
}

upload2 = layui.upload.render({
    elem: '#importTableTag'
    , url: getAjaxUrl(service_prefix.metadata, "/importTable?id=" + metadataObj.id)
    , accept: 'file' //普通文件
    , before: function (obj) {
        layer.load();
    }
    , done: function (res) {
        if (res.success) {
            layer.msg(res.msg);
            layer.closeAll('loading');
            doList(1);
        } else {
            layer.closeAll('loading');
        }
    }
});

function exportTable () {
    window.location.href = getAjaxUrl(service_prefix.metadata, "/exportTable?id=" + metadataObj.id);
}

function exportTableData () {
    getMetadataUri(metadataObj.id).then(function(metadataUri){
        layerOpen("导出元数据", 500, 250);
        lodTpl("exportMetadataTpl", "openDiv", {tableId: metadataObj.id});
        $("#exportMetadataForm").attr("action", getAjaxUrl("",  metadataUri + "/export"));
    })
    // window.location.href = getAjaxUrl(service_prefix.metadata, "/exportTable?id=" + metadataObj.id);
}

function getPushTable() {
    var tname = metadataObj.name;
    tname = tname.toLowerCase().replace("jmeta_", "");
    ajax("","http://47.93.246.70/api/v4/projects/17/repository/commits?private_token=HUvSgtfu6tresYAc9zga&path=projects-feiwuzhiwenhuayichan/src/main/java/com/jnetdata/simple/feiwuzhiwenhuayichan/" + tname, "get", {}).then(function(data) {
        var html = "";
        for (var i in data) {
            data[i].committed_date = timeFormat(data[i].committed_date);
        }
        layui.use('table', function () {
            var table = layui.table;
            table.render({
                elem: '#fieldTable4'
                , data: data
                , cellMinWidth: 80 //全局定义常规单元格的最小宽度，layui 2.2.1 新增
                , cols: [[
                    {type: 'numbers', title: '序号', width: 60}
                    , {field: 'title', title: 'git信息', templet: "#toGitHistory"}
                    , {field: 'committer_email', title: '提交名称'}
                    , {field: 'committed_date', title: '提交时间'}
                    , {field: 'short_id', title: 'git提交id'}
                ]]
                , limit: 100
            });
        });
    })
}
function getERpicture() {
        $(".erView").load("thirdparty/andflow_js-master/examples/erd.html");

}
function searchField() {
    $("#searchField").on("keypress", function (event) {
        if (event.keyCode == "13") {
            doList(1);
        }
    })
}

$("#searchTable").on("keypress", function (event) {
    if (event.keyCode == "13") {
        getMetadatas();
    }
})

//ztree
var settings = {
    data: {
        simpleData: {
            enable: true,  //true 、 false 分别表示 使用 、 不使用 简单数据模式
            idKey: "id",  //节点数据中保存唯一标识的属性名称
            pIdKey: "parentId",    //节点数据中保存其父节点唯一标识的属性名称
            rootPId: 0  //用于修正根节点父节点数据，即 pIdKey 指定的属性值
        },
        key: {
            name: "className"
        }
    },
    view: {showLine: false, showIcon: true},
    callback: {
        onClick: zTreeOnClick
    }
};

//渲染树结构
function showTree(data) {
    zTreeObj = $.fn.zTree.init($("#treeDemo"), settings, data); //初始化树
}

function initBtnmore(){
    var dropdown = layui.dropdown;
    dropdown.render({
        elem: '#btnmore' //可绑定在任意元素中，此处以上述按钮为例
        ,data: [{
            title: '导出表数据'
            ,id: 'expertData'
        },{
            title: '类似创建字段'
            ,id: 'createSame'
        },{
            title: '生成视图'
            ,id: "createView"
        },{
            title: '导入元数据表'
            ,id: 'importTable'
        },{
            title: '导出元数据表'
            ,id: 'exportTable'
        }]
        ,id: 'btnmore'
        //菜单被点击的事件
        ,click: function(obj){
            switch(obj.id){
                case 'expertData':exportTableData ();break;
                case 'createSame':sameCreate();break;
                case 'createView':create();break;
                case 'exportTable':exportTable();break;
                case 'importTable':importTable();break;
            }
        }
        ,className: 'jnet-downselect'
        ,ready: function(){
            uploadData2();
        }
    });

    dropdown.render({
        elem: '#sortBtn'
        ,data: [{
            title: '按创建时间倒序'
            ,id: 1
        },{
            title: '按创建时间正序'
            ,id: 2
        },{
            title: '按生成时间倒序'
            ,id: 3
        },{
            title: '按生成时间正序'
            ,id: 4
        }]
        //菜单被点击的事件
        ,click: function(obj){
            switch(obj.id){
                case 1:leftSort={key: "crtime", value: false};break;
                case 2:leftSort={key: "crtime", value: true};break;
                case 3:leftSort={key: "generatetime", value: false};break;
                case 4:leftSort={key: "generatetime", value: true};break;
            }
            getMetadatas();
        }
        ,className: 'jnet-downselect'
    });

    var defaultFieldData = [
        {anothername:"主键id",fieldname:"ID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"创建人名称",fieldname:"CRUSER",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"创建人id",fieldname:"CRNUMBER",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"创建时间",fieldname:"CRTIME",fieldTypeStr:"日期",dbTypeStr:"时间",dblength:"0"},
        {anothername:"最后修改人名称",fieldname:"MODIFY_USER",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"最后修改人id",fieldname:"MODIFY_BY",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"最后修改时间",fieldname:"MODIFY_TIME",fieldTypeStr:"日期",dbTypeStr:"时间",dblength:"0"},
        {anothername:"文档id",fieldname:"DOCCHANNELID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"删除状态",fieldname:"DOCSTATUS",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"2"},
        {anothername:"站点id",fieldname:"SITEID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"发布时间",fieldname:"DOCPUBTIME",fieldTypeStr:"日期",dbTypeStr:"时间",dblength:"0"},
        {anothername:"发布状态",fieldname:"STATUS",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"6"},
        {anothername:"机构id",fieldname:"COMPANYID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"站点id",fieldname:"WEBSITEID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"栏目id",fieldname:"COLUMNID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"排序",fieldname:"SEQENCING",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"10"},
        {anothername:"工作流id",fieldname:"FLOW_ID",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"工作流用户",fieldname:"FLOW_USER",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"引用信息",fieldname:"QUOTAINFO",fieldTypeStr:"多行文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"复制文档id",fieldname:"COPYFROMID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"操作人(暂时没用)",fieldname:"OPERUSER",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"操作时间(暂时没用)",fieldname:"OPERTIME",fieldTypeStr:"日期",dbTypeStr:"时间",dblength:"0"},
        {anothername:"文档标题(暂时没用)",fieldname:"DOCTITLE",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"文档时间(暂时没用)",fieldname:"DOCRELTIME",fieldTypeStr:"日期",dbTypeStr:"时间",dblength:"0"},
        {anothername:"文档发布url(暂时没用)",fieldname:"DOCPUBURL",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"文档链接(暂时没用)",fieldname:"LINKURL",fieldTypeStr:"普通文本",dbTypeStr:"字符串",dblength:"255"},
        {anothername:"分类法id(暂时没用)",fieldname:"CLASSINFOID",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"SINGLETEMPKATE(暂时没用)",fieldname:"SINGLETEMPKATE",fieldTypeStr:"自定义",dbTypeStr:"整数",dblength:"20"},
        {anothername:"DOCVALID(暂时没用)",fieldname:"DOCVALID",fieldTypeStr:"日期",dbTypeStr:"时间",dblength:"0"}
        ]
    var cols = [
        {type: 'numbers', title: '序号', width: 50}
        , {field: 'anothername', title: '中文名称'}
        , {field: 'fieldname', title: '英文名称'}
        , {field: 'fieldTypeStr', title: '字段类型'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
        , {field: 'dbTypeStr', title: '库中类型'}
        , {field: 'dblength', title: '库中长度', width: 100}
    ];
    renderRightTable("deFaultFieldTable",cols,defaultFieldData);
}

$(function () {
    initBtnmore();
    searchField();
/*    layui.form.verify({
        enCode: [
            /^[a-zA-Z]+$/,
            '请输入英文'
        ]
    });*/
    $(".left-list").on("mouseover", ".jnet-left-item", function () {
        $(this).addClass("active2").siblings().removeClass("active2");
    })
    $(".left-list").on("mouseleave", ".jnet-left-item", function () {
        $(this).removeClass("active2");
    })
    $(".left-list").on("click", ".jnet-left-item", function () {
        $(".tableCreateBar").html("创建人:{0} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 创建时间:{1} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 生成人:{2} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 生成时间:{3}".format($(this).attr("created_user"),$(this).attr("created_data_time"),$(this).attr("generate_user"),$(this).attr("generate_time")));
        $(".left-list input").prop("checked", false);
        $(this).find("input").prop("checked", true);
        layui.form.render();
        $(this).addClass("active").siblings().removeClass("active");
        metadataObj.id = $(this).attr("data_id");
        metadataObj.name = $(this).attr("data_name");
        // getPushTable();
        doList(1);
        /*getERpicture()*/
    })
    layui.form.render();
    getMetadatas();
})