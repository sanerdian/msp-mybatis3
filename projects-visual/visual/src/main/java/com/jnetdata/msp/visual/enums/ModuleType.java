package com.jnetdata.msp.visual.enums;

/**
 * 组件类型
 */
public enum ModuleType {
    TEXT("text", "文本组件"),
    PICTURE("picture", "图片组件"),
    VIDEO("video", "视频组件"),
    TABLE("table", "表格组件"),
    CHART("chart","图表组件"),
    SEARCH("search", "搜索组件"),
    MENU("menu", "菜单组件"),
    DETAIL("detail", "详情组件"),
    NAVIGATION("navigation", "导航组件"),
    TEXTLIST("textlist", "图文列表组件"),
    CAROUSEL("carousel", "轮播组件"),
    LOGIN("login", "登录组件"),
    CUSTOMBUTTON("custombutton", "自定义按钮组件"),
    FORMELEMENT("formelement", "表单元素组件"),
    FORM("form", "表单组件"),
    ;

    private String code;
    private String name;

    ModuleType(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String value(){
        return this.code;
    }
}
