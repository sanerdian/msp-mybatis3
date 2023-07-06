package com.jnetdata.msp.member.shiro.impl;

import com.jnetdata.msp.member.limit.mapper.LimitMapper;
import org.apache.shiro.config.Ini;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.Resource;

public class MyChainDefinitions implements FactoryBean<Ini.Section> {
    public static final String PREMISSION_STRING = "perms[{0}]";
    public static final String ROLE_STRING = "roles[{0}]";
    private String filterChainDefinitions;


    @Resource
    LimitMapper limitMapper;

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }


    @Override
    public Ini.Section getObject() throws Exception {
        /*List<Limit> list =  limitMapper.allList();
        //urls可以从数据库查出来，此处省略代码，直接模拟几条数据
        Set<String> urls = new LinkedHashSet<>();
        urls.add("/simple/menu/list.html");
        //每个url对应的权限也可以从数据库中查出来，这里模拟几条数据
        Map<String, String[]> permsMap = new HashMap<>();
        permsMap.put("/simple/menu/list.html", new String[]{"admin1223"});*/

        //加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        /*for (String url : urls) {
            String[] perms = permsMap.get(url);
            StringBuilder permFilters = new StringBuilder();
            for (int i = 0; i < perms.length; i++) {
                permFilters.append(perms[i]).append(",");
            }
            //去掉末尾的逗号
            String str = permFilters.substring(0, permFilters.length() - 1);
            //生成结果如：/dotest1.html = authc, perms[admin]
            section.put(url, "authc,"+MessageFormat.format(PREMISSION_STRING, str));
        }*/
        return section;
    }

    @Override
    public Class<?> getObjectType() {
        return this.getClass();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}