$(function () {
    layui.form.verify({
        enCode: [
            /^[a-zA-Z]+$/,
            '请输入英文'
        ]
    });
    getThisSiteTree();
    initBtnMore();
})


/*********************  tree  **********************/
var zTreeObj;

//获取站点树
function getThisSiteTree() {
    getSiteTree("siteTree",zTreeOnclick,function(zo,datas){
        zTreeObj = zo;
        $("#siteTree_1_a").trigger("click");
    });
}

function zTreeOnclick(treeNode){
    if(treeNode.isSite == 0){
        getRight(treeNode);
        $(".companyView").show();
        $(".siteView").hide();
    }else{
        getSiteById(treeNode.id);
        showTab('infos');
        $(".companyView").hide();
        $(".siteView").show();
    }
}

function siteId(){
    var node = getZtreeCurrNode(zTreeObj);
    if(!node) return null;
    return node.id;
}

function siteName(){
    return zTreeObj.getSelectedNodes()[0].name;
}
/*********************  treeEnd  **********************/

//导出站群
function doexport() {
    // showExportHelp();
    window.open(com.jnetdata.url_prefix + "/metasite/export/"+siteMsgId);
    window.open(com.jnetdata.url_prefix + "/metasite/exportJar1/"+siteMsgId);
    window.open(com.jnetdata.url_prefix + "/metasite/exportJar2/"+siteMsgId);
}

function lead1() {
    layerOpenHtml0("导入站群",350,230,"simple/manage/site/drzq.html")
}

function exportd() {
    window.location.href = service_prefix.manage + '/site/export/' + siteId();
}

function initBtnMore(){
    layui.dropdown.render({
        elem: '#btnmore' //可绑定在任意元素中，此处以上述按钮为例
        ,data: [{
            title: '停用站点'
            ,id: 'stopSite'
        },{
            title: '恢复站点'
            ,id: 'resetSite'
        },{
            title: '复制站点'
            ,id: "copySite"
        },{
            title: '移动站点'
            ,id: 'moveSite'
        },{
            title: '导入站点'
            ,id: 'import'
        },{
            title: '导入站群'
            ,id: 'import1'
        },{
            title: '导出站点'
            ,id: 'exportd'
        }]
        ,id: 'btnmore'
        //菜单被点击的事件
        ,click: function(obj){
            switch(obj.id){
                case 'stopSite':stopSite();break;
                case 'resetSite':resetSite();break;
                case 'copySite':copySite();break;
                case 'publishIndex':publishIndex();break;
                case 'moveSite':moveSite();break;
                case 'import':lead();break;
                case 'import1':lead1();break;
                case 'exportd':exportd();break;
            }
        }
        ,className: 'jnet-downselect'
    });
}
