package com.jnetdata.msp.cs1.esunstructured.service;

import com.jnetdata.msp.cs1.esunstructured.model.Esunstructured;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.vo.PageVo1;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * testt 服务类
 * </p>
 *
 * @author zyj
 * @since 2021-03-12
 */
public interface EsunstructuredService {

        Pager<Esunstructured> list(PageVo1 pager, Esunstructured entity);

        String insert(Esunstructured entity);

        boolean delete(String id);

        Esunstructured getById(String id);

        void updateById(String id,Esunstructured entity);

        boolean deleteBatch(String[] ids);
}
