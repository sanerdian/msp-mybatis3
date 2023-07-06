package com.jnetdata.msp.core.controller;

import com.jnetdata.msp.base.vo.EntityIdVo;
import com.jnetdata.msp.constants.WebPathConstant;
import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.core.util.ReflectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.thenicesys.data.api.EntityId;
import org.thenicesys.data.api.IBaseService;
import org.thenicesys.data.api.Pager;
import org.thenicesys.data.api.util.ConditionMap;
import org.thenicesys.web.JsonResult;
import org.thenicesys.web.vo.PageVo;
import org.thenicesys.web.vo.PageVo1;
import org.thenicesys.web.vo.PageVo2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Administrator
 * @date 2018/8/28
 * @author hongshou
 * @date 2020/5/26
 */
public class BaseController<IdType extends Serializable, T extends EntityId<IdType>> extends org.thenicesys.web.BaseController {

    /**
     * 添加
     * @param service
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<EntityId<IdType>> doAdd(IBaseService<T> service, T entity) {

        boolean result = service.insert(entity);
        if (result) {
            return renderSuccess(new EntityIdVo(entity.getId()));
        }
        return renderError("新增失败");
    }

    /*
    * 验证之后更新，更新前的验证方法
    * @param service
    * @param entity
    * @author hongshou
    * @date 2020/5/26
    * */
    protected void validateBeforeUpdate(BaseService<T> service, EntityId entity) {

        assertNotEmpty(entity.getId(), "id不能为空");

        T found = service.getById(entity.getId());
        assertNotEmpty(found, "对象必须存在");
    }

    /**
     * 修改指定id对应的信息
     * @param service
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<Void> doUpdateById(BaseService<T> service, IdType id, T entity) {

        assertNotEmpty(id, "id不能为空");

        entity.setId(id);
        validateBeforeUpdate(service, entity);

        return service.updateById(entity) ? renderSuccess("更新成功") : renderError("更新失败");
    }

    /**
     * 更新
     * @param service
     * @param id
     * @param entity
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<Void> doUpdateAllColumnById(BaseService<T> service, IdType id, T entity) {

        assertNotEmpty(id, "id不能为空");

        entity.setId(id);
        validateBeforeUpdate(service, entity);

        return service.updateAllColumnById(entity) ? renderSuccess("更新成功") : renderError("更新失败");
    }

    /**
     * 根据id删除
     * @param service
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<Void> doDeleteById(BaseService<T> service, Serializable id) {

        assertNotEmpty(id, "id不能为空");
        return service.deleteById(id) ? renderSuccess("删除成功") : renderError("删除失败");
    }

    /**
     * 根据多个id进行批量删除
     * @param service
     * @param ids
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<Void> doDeleteBatchIds(BaseService<T> service, String ids) {

        return service.deleteBatchIds(convertIds(ids)) ? renderSuccess("删除成功") : renderError("删除失败");
    }
    /**
     * 根据id批量删除
     * @param service
     * @param ids 多个实体id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<Void> doDeleteBatchIds(BaseService<T> service, List<String> ids) {
        if (ids==null||ids.size()<=0) {
            return renderError("ID不能为空");
        }
        if (service.deleteBatchIds(ids)){
            return renderSuccess("删除成功");
        }
        return renderError("删除失败");
    }


    private Collection<Serializable> convertIds(String ids) {
        return Arrays.stream(ids.split(",")).map(id -> fromStringId(id)).collect(Collectors.toList());
    }
    private IdType fromStringId(String id) {
        Class idTypeClass = getIdTypeClass();
        if (idTypeClass.getName().equals(Integer.class.getName())) {
            return (IdType)Integer.valueOf(id);
        }
        if (idTypeClass.getName().equals(Long.class.getName())) {
            return (IdType)Long.valueOf(id);
        }
        if (idTypeClass.getName().equals(String.class.getName())) {
            return (IdType)id;
        }
        throw new RuntimeException("不支持的Id数据类型");
    }

    /**
     * 查看指定id对应的信息
     * @param service
     * @param id
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<T> doGetById(BaseService<T> service, Serializable id) {
        assertNotEmpty(id, "id不能为空");
        T found = service.getById(id);
        return (null == found) ? renderError("该对象不存在") : renderSuccess(found);
    }

    /**
     * 分页查询
     * @param service
     * @param pageVo
     * @param template
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected JsonResult<Pager<T>> doList(BaseService<T> service, PageVo pageVo, Object template) {
        Pager<T> page = doSearch(service, pageVo, template);
        return renderSuccess(page);
    }
    /*
    * 根据vo分页查询
    * @param service
    * @param PageVo1
    * @param template
    * @author 王树彬
    * @date 2020/5/26
    * return
    * */
    protected JsonResult<Pager<T>> doList(BaseService<T> service, PageVo1 pageVo1, Object template) {
        Pager page = doSearch(service, pageVo1, template);
        return renderSuccess(page);
    }
    /*
    * 分页查询
    * @param service
    * @param PageVo1
    * @param template
    * @author hongshou
    * @date 2020/5/26
    * return
    * */
    protected JsonResult<Pager<T>> doList(BaseService<T> service, Pager<T> page, Object template) {
        service.search(page, createCondition(template));
        return renderSuccess(page);
    }

    /**
     * 执行查询
     * @param service
     * @param pageVo
     * @param template
     * @author hongshou
     * @date  2020/5/26
     * @return
     */
    protected Pager<T> doSearch(BaseService<T> service, PageVo pageVo, Object template) {
        return doSearch1(service, pageVo, template);
    }

    /*
    * 执行查询
    * @param service
    * @param pageVo
    * @param template
    * @author hongshou
    * @date  2020/5/26
    * */
    protected Pager<T> doSearch(BaseService<T> service, PageVo1 pageVo, Object template) {
        return doSearch1(service, pageVo, template);
    }

    /*
     * 执行查询
     * @param service
     * @param pageVo
     * @param template
     * @author hongshou
     * @date 2020/5/26
     * */
    protected Pager<T> doSearch(BaseService<T> service, PageVo2 pageVo, Object template) {
        return doSearch1(service, pageVo, template);
    }
    protected<U extends PageVo> Pager<T> doSearch1(BaseService<T> service, U pageVo, Object template) {
        Pager<T> page = createPager(pageVo);
        service.search(page, createCondition(template));
        return page;
    }
    /*
    * 对空指针异常进行判断并自定义提示语
    * @prarm id
    * @param message
    * @author hongshou
    * @date 2020/5/26
    * */
    private static void assertNotEmpty(Object id, String message) {
        if (ObjectUtils.isEmpty(id)) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 创建条件
     * @param template
     * author hongshou
     * date 2020/5/26
     * @return
     */
    protected ConditionMap createCondition(Object template) {
        if (Objects.isNull(template)) {
            return new ConditionMap<>();
        }
        return ConditionMap.of(template);
    }

    /**
     * 创建分页
     * @param pageVo
     * @param <U>
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    private<U extends PageVo> Pager<T> createPager(U pageVo) {
        Pager<T> pager = new Pager<>();
        BeanUtils.copyProperties(pageVo, pager);
        return pager;
    }

    protected boolean isEmpty(String str) {
        return null==str || "".equals(str.trim());
    }

    /**
     * web路径
     * @param baseUrl
     * @param url
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    protected String webPath(String baseUrl, String url) {
        return WebPathConstant.getHtmlFilePath(baseUrl + url);
    }

    private Class getIdTypeClass() {
        return ReflectionUtil.getSuperClassGenricType(getClass(), 0);
    }

}
