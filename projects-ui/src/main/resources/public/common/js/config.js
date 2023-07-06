/**
 * url_prefix 地址前缀
 * service_prefix 微服务id
 */
var com = {
    jnetdata : {},
    columnTree: {async:true},
    groupTree: {async:true},
    api: {async:false},
    encrypted:{
        passwordMd5:false,//密码传输前就用md5+盐进行加密
        userSm4:false,//用户关键信息（真实用户名、手机号、邮箱）传输前用sm4加密
    }
};
com.jnetdata = {
    'url_prefix':'',//msp后台地址（如果访问的是本地的一体化，注意localhost与127.0.0.1是2个不同的域）
    'service_prefix':''
};

function ifApiAsync(){
    return com.api.async && service_prefix.gateway;
}

function getColumnTreeAsync(){
    return com.columnTree.async;
}

function ifGroupTreeAsync(){
    return com.groupTree.async;
}

/**
 * 微服务id(对应地址前缀)
 */
var service_prefix = {
    'gateway': 'apiGateway', //api
    'member': 'member',//用户管理
    'message': 'message',//消息管理
    'task': 'task',//计划任务调度
    'log': 'log',//系统日志
    'msplog': 'msplog',
    'config':'config',//配置项管理
    'flowable': 'flowable',//工作流引擎
    'metadata': 'metadata',//元数据管理
    'resources': 'resources',//资源管理
    'manage': 'manage',
    'dict': "dict",
    'generator':"generator",
    'visual': "visual",
    'cs1':"cs1"
};

//去掉控制台输入日志
// hideConsoleLog()
function hideConsoleLog(){
    window.console = (function() {
        var c = {};
        c.log = c.warn = c.debug =
            c.info = c.error = c.time =
                c.dir = c.profile = c.clear =
                    c.exception = c.trace = c.assert = function() {};
        return c;
    })();
}