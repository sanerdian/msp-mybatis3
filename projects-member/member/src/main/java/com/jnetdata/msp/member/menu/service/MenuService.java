package com.jnetdata.msp.member.menu.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.menu.model.Menu;

import java.util.List;
import java.util.Map;

/**
 * Created by TF on 2019/3/19.
 */
public interface MenuService extends BaseService<Menu> {

    void updataSeq(Menu entity);

    int getListMenu(Long parentId, Integer menuType);
    /**
     * 查询所有菜单，并组织成森林(目前代码仅支持二层菜单)
     * @param type 菜单类型 type=1后台菜单， type=2前台菜单
     * @param companyId
     * @author hongshou
     * @date 2020/5/26
     * @return 全部返回(包括无子菜单的一级菜单)
     */
    List<Menu> getMenu(int type, Long companyId);

    /**
     * 根据查询id菜单,并组织成森林(目前代码仅支持二层菜单)
     * @param ids 菜单ids
     * @param type 菜单类型 type=1后台菜单， type=2前台菜单
     * @param companyId 公司id
     * @author hongshou
     * @date 2020/5/26
     * @return 会过滤点没有子菜单的一级菜单
     */
    List<Menu> getMenu(List<Long> ids, int type, Long companyId);

    /*
    * 获取前缀
    * @param realm
    * @author 王树彬
    * @date 2023/3/21
    * return
    * */
    Map<String, String> getPerfix(String realm);

    Map<String, String> getPerfix();
}
