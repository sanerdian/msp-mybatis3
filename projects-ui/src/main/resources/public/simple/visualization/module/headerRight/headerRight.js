$(function () {
    function init() {
        ajax(service_prefix.member, '/user/getLoginUser', 'get').then(res => {
            let data = res.obj.user;
            var username = data.crUser;
            var userId = data.id;
            if (localStorage.getItem('username')) {
                $('#widgetHeaderRight').append('<a href="javascript:;" id="currUser" data-id="0">username</a>|<a href="javascript:logout();" id="exit">退出</a>')
                if (localStorage.getItem('username') == username) {
                    $('#currUser').text(username);
                    $('#currUser').attr('data-id', userId);
                }
            } else {
                $('#widgetHeaderRight').append('<a href="javascript:;">请登录！</a>');
            }
        });
    }
    init();
});
function logout() {
    // console.log(1);
}
