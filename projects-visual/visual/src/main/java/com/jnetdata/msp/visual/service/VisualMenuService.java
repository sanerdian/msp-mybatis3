package com.jnetdata.msp.visual.service;

import com.jnetdata.msp.member.menu.mapper.MenuMapper;
import com.jnetdata.msp.member.menu.model.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;
import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class VisualMenuService {


    @Resource
    private MenuMapper menuMapper;


    /**
     * 页面左移
     */
    public JsonResult<String> pageLeft(Long pageId){
        try {
            //获取菜单信息、原父菜单信息
            Menu menu = menuMapper.selectById(pageId);
            Menu parentMenu = menuMapper.selectById(menu.getParentId());
            if(!ObjectUtils.isEmpty(parentMenu)){
                int seq = parentMenu.getSeq() + 1;
                //获取相同层级下排序后一位的排序数字
                Integer nextId = this.getNextSeq(parentMenu.getParentId(), parentMenu.getSeq(), parentMenu.getType());
                if(!ObjectUtils.isEmpty(nextId) && nextId == seq){
                    //更新原父菜单排序：（后面的菜单）排序的数字加一
                    Menu param = new Menu();
                    param.setParentId(menu.getParentId());
                    param.setSeq(nextId);
                    menuMapper.updataSeq(param);
                }
                //更新菜单信息：层级与原父菜单一致，排序的数字为原父菜单加一
                Menu temp = new Menu();
                temp.setId(menu.getId());
                temp.setParentId(parentMenu.getParentId());
                temp.setSeq(seq);
                menuMapper.updateById(temp);
            }else{
                return JsonResult.fail("已经是第一层菜单");
            }
        }catch (Exception e){
            log.error("菜单左移异常：{}, 菜单id：{}", e.getMessage(), pageId);
            return JsonResult.fail();
        }
        return JsonResult.success();
    }


    /**
     * 页面复制
     */
    public JsonResult<String> pageCopy(Long pageId) {
        //根据id查询当前菜单数据
        Menu menu = menuMapper.selectById(pageId);
        int seq = menu.getSeq();

            //获取相同层级下排序后一位的排序数字
        Integer nextId = this.getNextSeq(menu.getParentId(), seq, menu.getType());
            if(!ObjectUtils.isEmpty(nextId) && nextId == (seq +1)){
                //更新原父菜单排序：（后面的菜单）排序的数字加一
                Menu temp = new Menu();
                temp.setSeq(nextId);
                temp.setParentId(menu.getParentId());
                menuMapper.updataSeq(temp);
            }
         //添加数据库
        menu.setId(null);
        menu.setSeq(seq+1);
        menuMapper.insert(menu);
        return JsonResult.success();
    }


    /**
     * 页面右移
     */
    public JsonResult<String> pageRight(Long pageId){
        try {
            //获取上级菜单id
            Menu menu = menuMapper.selectById(pageId);
            Long parentId = null;
            if(!ObjectUtils.isEmpty(menu)){
                parentId = this.getPreviousId(menu.getParentId(), menu.getSeq(), menu.getType());
            }
            if(ObjectUtils.isEmpty(parentId)){
                return JsonResult.fail("没有对应的上级每面");
            }

            //获取父菜单的原子菜单列表
            List<Menu> menuList = menuMapper.selectList(new PropertyWrapper<>(Menu.class)
                    .eq("parentId", parentId).getWrapper());

            //更新菜单信息
            Menu temp = new Menu();
            temp.setId(pageId);
            temp.setParentId(parentId);
            temp.setSeq(this.getMaxSeq(menuList) + 1);
            menuMapper.updateById(temp);
        }catch (Exception e){
            log.error("菜单右移异常：{}, 菜单id：{}", e.getMessage(), pageId);
            return JsonResult.fail();
        }
        return JsonResult.success();
    }

    /**
     * 页面上移
     */
    public JsonResult<String> pageUp(Long pageId){
        try {
            //获取前一个菜单
            Menu menu = menuMapper.selectById(pageId);//1
            Menu previous = this.getPreviousMenu(menu.getParentId(), menu.getSeq(), menu.getType());//23
            if(ObjectUtils.isEmpty(previous)){
                return JsonResult.fail("已到顶部");
            }

            //两个菜单互换排序数字
            Menu tempMenu = new Menu();//4
            tempMenu.setId(menu.getId());
            tempMenu.setSeq(previous.getSeq());
            menuMapper.updateById(tempMenu);

            Menu tempPrevious = new Menu();//5
            tempPrevious.setId(previous.getId());
            tempPrevious.setSeq(menu.getSeq());
            menuMapper.updateById(tempPrevious);
        }catch (Exception e){
            log.error("菜单上移异常：{}, 菜单id：{}", e.getMessage(), pageId);
            return JsonResult.fail();
        }
        return JsonResult.success();
    }



    /**
     * 页面下移
     */
    public JsonResult<String> pageDown(Long pageId){
        try {
            //获取前一个菜单
            Menu menu = menuMapper.selectById(pageId);
            Menu next = this.getNextMenu(menu.getParentId(), menu.getSeq(), menu.getType());
            if(ObjectUtils.isEmpty(next)){
                return JsonResult.fail("已到底部");
            }

            //两个菜单互换排序数字
            Menu tempMenu = new Menu();
            tempMenu.setId(menu.getId());
            tempMenu.setSeq(next.getSeq());
            menuMapper.updateById(tempMenu);
            Menu tempNext = new Menu();
            tempNext.setId(next.getId());
            tempNext.setSeq(menu.getSeq());
            menuMapper.updateById(tempNext);
        }catch (Exception e){
            log.error("菜单下移异常：{}, 菜单id：{}", e.getMessage(), pageId);
            return JsonResult.fail();
        }
        return JsonResult.success();
    }

    /**
     * 获取相同层级下排序前一位的菜单id
     */
    private Long getPreviousId(Long parentId, Integer seq, Integer menuType){
        Menu previous = this.getPreviousMenu(parentId, seq, menuType);
        return ObjectUtils.isEmpty(previous) ? null : previous.getId();
    }

    /**
     * 获取相同层级下排序前一位的菜单
     * @param parentId 上级菜单id
     * @param seq 菜单排序
     * @param menuType 菜单类型
     */
    private Menu getPreviousMenu(Long parentId, Integer seq, Integer menuType){
        //根据父id获取菜单列表
        List<Menu> menuList = menuMapper.selectList(new PropertyWrapper<>(Menu.class)
                .eq("parentId", parentId)
                .eq("type", menuType).getWrapper());

        //遍历列表，获取排序前一位的菜单
        int tempSeq = -1;
        Menu previous = null;
        for(Menu menu: menuList){
            if(menu.getSeq() < seq && menu.getSeq() > tempSeq){
                tempSeq = menu.getSeq();
                previous = menu;
            }
        }
        return previous;
    }

    /**
     * 获取相同层级下排序后一位的排序数字
     */
    private Integer getNextSeq(Long parentId, Integer seq, Integer menuType){
        Menu next = this.getNextMenu(parentId, seq, menuType);
        return ObjectUtils.isEmpty(next) ? null : next.getSeq();
    }



    /**
     * 获取相同层级下排序后一位的菜单
     * @param parentId 上级菜单id
     * @param seq 菜单排序
     * @param menuType 菜单类型
     */
    private Menu getNextMenu(Long parentId, Integer seq, Integer menuType){
        //根据父id获取菜单列表
        List<Menu> menuList = menuMapper.selectList(new PropertyWrapper<>(Menu.class)
                .eq("parentId", parentId)
                .eq("type", menuType).getWrapper());

        //遍历列表，获取排序前一位的菜单id
        Integer tempSeq = Integer.MAX_VALUE;
        Menu next = null;
        for(Menu menu: menuList){
            if(menu.getSeq() > seq && menu.getSeq() < tempSeq){
                tempSeq = menu.getSeq();
                next = menu;
            }
        }
        return next;
    }




    /**
     * 获取菜单列表中最大的排序数字
     */
    private int getMaxSeq(List<Menu> menuList){
        int maxSeq = 0;
        try {
            for(Menu menu: menuList){
                if(menu.getSeq() > maxSeq){
                    maxSeq = menu.getSeq();
                }
            }
        }catch (Exception e){
            log.error("");
        }
        return maxSeq;
    }



}
