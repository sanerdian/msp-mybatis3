/**
 * url_prefix 地址前缀
 * service_prefix 服务id
 */
const com = {
    jnetdata: {},
    columnTree: {async: true},
    api: {async: false}
};

com.jnetdata = { 
    // 'url_prefix': 'http://123.57.20.137:8080'    
    'url_prefix': 'http://36.138.169.165:8081'
};
var service_prefix = {
    'member': com.jnetdata.url_prefix + '/member',//用户管理
    'manage': com.jnetdata.url_prefix + '/manage',
    'visual': com.jnetdata.url_prefix + "/visual"
};
