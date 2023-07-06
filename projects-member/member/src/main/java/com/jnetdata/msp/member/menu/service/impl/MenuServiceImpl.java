package com.jnetdata.msp.member.menu.service.impl;

import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.member.menu.mapper.MenuMapper;
import com.jnetdata.msp.member.menu.model.Menu;
import com.jnetdata.msp.member.menu.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by TF on 2019/3/19.
 *
 */

@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper,Menu> implements MenuService {

    @Override
    protected PropertyWrapper<Menu> makeSearchWrapper(Map<String, Object> condition) {
        return createWrapperUtil(condition)
                .eq("companyId")
                .eq("id")
                .eq("type")
                .eq("name")
                .eq("parentId")
                .eq("siteId")
                .like("modstr")
                .getWrapper();
    }

    /**
     * 查询所有菜单，并组织成森林(目前代码仅支持二层菜单)
     * @param type 菜单类型 type=1后台菜单， type=2前台菜单
     * @param companyId
     * @author hongshou
     * @date 2020/5/26
     * @return 全部返回(包括无子菜单的一级菜单)
     */
    @Override
    public List<Menu> getMenu(int type,Long companyId) {

        PropertyWrapper<Menu> pw = new PropertyWrapper<>(Menu.class)
                .eq("active", 1)
                .eq("type", type)
                .eq("companyId", companyId)
                .orderBy(Arrays.asList("seq"), true);
        List<Menu> list = super.list(pw);

        //按一级菜单分组
        Map<Long, List<Menu>> map = list.stream().collect(Collectors.groupingBy(Menu::getParentId));

        list.forEach(menu -> {
            menu.setChildren(map.containsKey(menu.getId()) ? map.get(menu.getId()) : new ArrayList<>());
        });

        if(map.size() > 0 && map.containsKey(0L))
            return map.get(0L);
        else
            return new ArrayList<>();
    }

    /**
     * 根据查询id菜单,并组织成森林(目前代码仅支持二层菜单)
     * @param ids 菜单ids
     * @param type 菜单类型 type=1后台菜单， type=2前台菜单
     * @param companyId 公司id
     * @author hongshou
     * @date 2020/5/26
     * @return 会过滤点没有子菜单的一级菜单
     */
    @Override
    public List<Menu> getMenu(List<Long> ids,int type,Long companyId) {

        if (ids.size()==0) {
            return new ArrayList<>();
        }

        PropertyWrapper<Menu> pw = new PropertyWrapper<>(Menu.class)
                .eq("active", 1)
                .eq("type", type)
                .eq("companyId", companyId)
                .in("id", ids)
                .orderBy(Arrays.asList("seq"), true);
        List<Menu> list = super.list(pw);

        getParentMenu(list);

        List<Menu> collect = list.stream().sorted((x, y) -> {
            return x.getSeq() > y.getSeq() ? 1 : -1;
        }).collect(Collectors.toList());

        Map<Long, List<Menu>> map = collect.stream().collect(Collectors.groupingBy(Menu::getParentId));

        collect.forEach(menu -> {
            menu.setChildren(map.containsKey(menu.getId()) ? map.get(menu.getId()) : new ArrayList<>());
        });


        if(map.size() > 0 && map.containsKey(0L))
            return map.get(0L);
        else
            return new ArrayList<>();

    }

    /*
    * 获取父菜单
    * @param list
    * */
    public void getParentMenu(List<Menu> list) {
        List<Long> ids = list.stream().map(m -> m.getId()).collect(Collectors.toList());
        List<Long> pids = list.stream().filter(m -> (m.getParentId()!=null && !m.getParentId().equals(0L) && !ids.contains(m.getParentId())) ).map(m -> m.getParentId()).collect(Collectors.toList());
        if(!pids.isEmpty()){
            List<Menu> list1 = super.list(new PropertyWrapper<>(Menu.class)
                    .in("id", pids)
                    .orderBy(Arrays.asList("seq"), true)
            );
            list.addAll(list1);
            getParentMenu(list);
        }
    }
    /*
     * 获取前缀
     * @param realm
     * @author 王树彬
     * @date 2023/3/21
     * return
     * */
    @Override
    public Map<String, String> getPerfix(String realm) {
        List<Menu> menus = baseMapper.getPerfix();
        Map<String,String> perfixs = new HashMap<>();
        for (Menu menu : menus) {
            perfixs.put(menu.getModstr(),(menu.getRealmName()==null?realm:menu.getRealmName())+"/"+menu.getModstr());
        }
        return perfixs;
    }
    /*
     * 获取前缀
     * @author 王树彬
     * @date 2023/3/21
     * return
     * */
    @Override
    public Map<String, String> getPerfix() {
        List<Menu> menus = baseMapper.getPerfix();
        Map<String,String> perfixs = new HashMap<>();
        for (Menu menu : menus) {
            perfixs.put(menu.getModstr(),menu.getModstr());
        }
        return perfixs;
    }

    /**
     *
     * @param menu 最上级节点
     * @param list 所有数据集合
     * @return
     */
    private Menu getChild(Menu menu,List<Menu> list){

        List<Menu> childList = new ArrayList<>();
        list.forEach(res->{
            if(menu.getId().equals(res.getParentId())){
                childList.add(res);
            }
        });
        menu.setChildren(childList);

        //递归出口
        if(childList.size()==0){
            return menu;
        }
        //递归调用
        childList.forEach(res->{
            getChild(res,list);
        });

        return menu;
    }

    @Override
    public void updataSeq(Menu entity) {
        baseMapper.updataSeq(entity);
    }

    public int getListMenu(Long parentId, Integer menuType){
        if(ObjectUtils.isEmpty(parentId)){
            parentId = 0L;
        }
//        List<String> list = new ArrayList<>();
//        if(list != null && list.size() > 0)

        //根据父id获取菜单列表
        List<Menu> menuList = baseMapper.selectList(new PropertyWrapper<>(Menu.class)
                .eq("parentId", parentId)
                .eq("type", menuType).getWrapper());
        //遍历列表，获取最大排序数
        Integer maxSeq = 0;
        for (int i=0;i < menuList.size();i++){
            if(menuList.get(i).getSeq() > maxSeq){
                maxSeq = menuList.get(i).getSeq();
            }
        }
        return  maxSeq;
    }
}
