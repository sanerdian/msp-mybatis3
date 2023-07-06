$(function () {
    // 获取当前登录用户
    ajax(service_prefix.member, "/user/getLoginUser", "get").then(function (res) {
        console.log(res);
        if (res.success) {
            let userObj = res.obj.user;
            $('#curUser').text(userObj.crUser);
            $('#curUser').attr('data-id', userObj.id);
        } else {
            $('.visual_login_after').html('<a href="javascript:goLogin();">请登录！</a>');
        }

    });
});
// 未登录，跳转到登录
function goLogin() {
    layer.msg('正在跳转到登录页面');
    window.location.href = '/pub/html/ceceshi/dengluceshi/loginPage.html';
}
// 退出登录
function loginOut() {
    ajax(service_prefix.member, "/home/ajaxLeave", "post", {}).then(function (res) {
        if (res) {
            layer.msg('退出登录成功');
            localStorage.setItem('state', false);
            window.location.href = '/pub/html/ceceshi/dengluceshi/loginPage.html';
        } else {
            layer.msg('退出登录失败，请重试');
        }


    });
}