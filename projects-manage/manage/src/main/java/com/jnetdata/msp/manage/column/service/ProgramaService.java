package com.jnetdata.msp.manage.column.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.site.model.Site;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pager;
import org.thenicesys.web.JsonResult;

import javax.servlet.ServletOutputStream;
import java.util.List;

/**
 * Created by WFJ on 2019/4/26.
 */
public interface ProgramaService extends BaseService<Programa> {

    List<Site> getColumnList(Programa entity);

    boolean updateStatus(String ids,int status);

    List<Programa> getByParentId(Long parentId,int type);

    boolean updateBySite(String siteIds,int status);

    void upLoadExcel(ServletOutputStream os,String sheetName,String [] title,List<Programa> programaList);

    List<Programa> readExcel(MultipartFile file,Long columnId,Long siteId);

    Programa getTreeData(Long id);

    List<Programa> getFirstColumn();

    void updateSeq(Programa entity);

    void updateSeq(Long id, int sort);

    List<Programa> getAllByparent(Long parentId);

    Programa getselect(Long id);

    /**
     * 获取导航路径
     */
    JsonResult getNaviPath(Long id);
}
