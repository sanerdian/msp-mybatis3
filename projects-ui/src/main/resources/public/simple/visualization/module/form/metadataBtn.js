$(function(){
    form.on('radio(formMetadataRadio)', function(data){
        console.log(data.elem); //得到radio原始DOM对象
        console.log(data.value); //被点击的radio的value值
        if(data.value == 1){
            $('.visual_form_metadata_button').show();
        }else{
            $('.visual_form_metadata_button').hide();
        }
    });
})