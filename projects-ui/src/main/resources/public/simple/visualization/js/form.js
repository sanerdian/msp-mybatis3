function getCode(_this){
    // layer.msg($(_this).attr('data-tag'));
    var thisTag = $(_this).attr('data-tag');
    $.ajax({
        url:'form.json',
        contentType: 'application/json',
        dataType: 'json',
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        success:function(data){  
            console.log('data',data.obj.records);
            layer.msg(thisTag);
            var myList = data.obj.records; 
            for(var i in myList){
                if(myList[i].tag == thisTag){
                    console.log(myList[i].code)
                    var html = '<div class="layui-form-item">' + myList[i].code + '</div>'
                    $('#formDesignerForm').append(html);
                    form.render();
                }
            }
        }
    }); 

}
$(function(){
    //新建表单分类
    function groupTypeLayer(){
        layer.open({
            type: 1,
            title: '表单分类',
            area: ['570px','200px'],
            btnAlign: 'c',
            btn: ['确定', '取消'],
            content: '<div id="formTypeLayerView"></div>',
            success:function(layero, index){
                var data = [];
                getData(data, '#formTypeLayerTemplate', '#formTypeLayerView'); 
                form.on('submit(formTypeFormSubmit)', function(data){
                    layer.close(layer.index);
                    return false;
                });
            },
            yes: function(index, layero){
                form.on('submit(formTypeFormSubmit)', function(data){ 
                    layer.close(layer.index);
                    return false;
                });
                layer.close(index);
                layero.find('form').find('button[lay-submit]').click();
            },
            btn2:function(index, layero){
                layer.close(index);
            },
            cancel: function(index, layero){ 
                layer.close(index);
                return false; 
            }
        }); 
    }


    //新建表单弹出框
    function formAddLayer(){
        layer.open({
            type: 1,
            title: '新建表单',
            area: ['570px','220px'],
            btnAlign: 'c',
            btn: ['确定', '取消'],
            content: $('#formAddTemplate').html(),
            success: function(layero, index){
            },
            yes: function(index, layero){
                window.open('simple/visualization/form/formEdit.html', '_blank');
                layer.close(index);
            }
        });
        form.render(); 
    }

    //新建表单分类按钮
    $('#formTypeAddBtn').click(function(){
        groupTypeLayer();
    });

    //新建组件弹出框
    $('#formAddBtn').click(function(){
        formAddLayer();
    });

    // function jsonTag(){
    //     $.ajax({
    //         url:'form.json',
    //         contentType: 'application/json',
    //         dataType: 'json',
    //         crossDomain: true,
    //         xhrFields: {
    //             withCredentials: true
    //         },
    //         success:function(result){  
    //             console.log('result',result);
    //         }
    //     }); 

    // }
    // jsonTag();
    
})