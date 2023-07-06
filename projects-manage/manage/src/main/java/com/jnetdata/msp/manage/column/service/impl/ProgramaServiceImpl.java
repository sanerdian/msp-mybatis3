package com.jnetdata.msp.manage.column.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnetdata.msp.core.model.util.SessionUser;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import com.jnetdata.msp.manage.column.mapper.PicturewqMapper;
import com.jnetdata.msp.manage.column.mapper.ProgramaMapper;
import com.jnetdata.msp.manage.column.model.Programa;
import com.jnetdata.msp.manage.column.service.ProgramaService;
import com.jnetdata.msp.manage.site.model.Site;
import com.jnetdata.msp.manage.site.service.SiteService;
import com.jnetdata.msp.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import org.thenicesys.web.JsonResult;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by WFJ on 2019/4/26.
 */
@Slf4j
@Service
public class ProgramaServiceImpl extends BaseServiceImpl<ProgramaMapper, Programa> implements ProgramaService {

    @Autowired
    @Lazy
    private SiteService siteService;

    @Override
    protected PropertyWrapper<Programa> makeSearchWrapper(Map<String, Object> condition) {
        Object conditionIds = condition.get("conditionIds");
        PropertyWrapper<Programa> wrapper = createWrapperUtil(condition)
                .eq("status")
                .eq("stop")
                .eq("parentId")
                .eq("siteId")
                .like("name")
                .getWrapper();
        if (conditionIds != null && ((List<Long>) conditionIds).size() > 0) {
            wrapper.and(i -> i.in(conditionIds != null, "CHANNELID", (List<Long>) conditionIds).or().eq("CRNUMBER", SessionUser.getCurrentUser().getId()));
        } else if (!SecurityUtils.getSubject().isPermitted("column:list")) {
            wrapper.eq("createBy", SessionUser.getCurrentUser().getId());
        }
        return wrapper;
    }

    /**
     * 查找栏目
     * @param entity
     * @return
     */
    @Override
    public  List<Site> getColumnList(Programa entity) {
        return getMap(getTree(entity));
    }

    /**
     * 获取栏目树形结构数据
     * @return
     */
    private Map<Long, List<Programa>> getTrees(){
        List<String> seqList = new ArrayList<>();
        seqList.add("chnlOrder");
        List<Programa> list = super.list(new PropertyWrapper<>(Programa.class).eq("status",0).orderBy(seqList,true));

        Map<Long, List<Programa>> map = list.stream().collect(Collectors.groupingBy(Programa::getParentId));

        list.forEach(menu -> {
            menu.setChildren1(map.containsKey(menu.getId()) ? map.get(menu.getId()) : new ArrayList<>());
        });

        return map;
    }

    /**
     * 获取栏目树形结构数据
     * @return
     */
    private List<Programa> getTree(Programa entity){
        List<String> seqList = new ArrayList<>();
        seqList.add("chnlOrder");
        List<Programa> allList = super.list(new PropertyWrapper<>(Programa.class).eq("status",entity.getStatus()).orderBy(seqList,true));

        //获取最顶级节点
        List<Programa> firstList = new ArrayList<>();
        allList.forEach(res->{
            res.setIsSite(0);
            if(Long.valueOf(0).equals(res.getParentId())||ObjectUtils.isEmpty(res.getParentId())){
                firstList.add(res);
            }
        });

        firstList.forEach(res->{
            getChild(res,allList);
        });

        return firstList;
    }

    /**
     * 获取子节点数据
     * @param entity
     * @param allList
     * @return
     */
    private Programa getChild(Programa entity,List<Programa> allList){
        List<Programa> childList = new ArrayList<>();
        allList.forEach(res->{
            if(entity.getId().equals(res.getParentId())){
                childList.add(res);
            }
        });
        entity.setChildren1(childList);

        if(childList.size()==0){
            return entity;
        }

        childList.forEach(res->{
            getChild(res,allList);
        });
        return entity;

    }


    /**
     * 数据处理为需要的数据格式
     * @param list
     * @return
     */
    private  List<Site> getMap(List<Programa> list){

        Map<String,List<Programa>> map = new HashMap<>();
        List<String> idList = new ArrayList<>();
        list.forEach(res->{
            if(!map.containsKey(String.valueOf(res.getSiteId()))){
                List<Programa> tempList = new ArrayList<>();
                tempList.add(res);
                idList.add(String.valueOf(res.getSiteId()));
                map.put(String.valueOf(res.getSiteId()),tempList);
            }else{
                map.get(String.valueOf(res.getSiteId())).add(res);
            }
        });

        List<Site> siteList = siteService.list(new PropertyWrapper<>(Site.class).eq("status",0));

        siteList.forEach(res->{
            res.setIsSite(1);
            res.setChildren1(map.get(String.valueOf(res.getId())));
        });

        return siteList;
    }

    /**
     * 更改逻辑删除状态
     * @param ids
     * @param status
     * @return
     */
    @Override
    public boolean updateStatus(String ids, int status) {
        ArrayList<Programa> list = new ArrayList<>();
        for (String id : ids.split(",")) {
            QueryWrapper<Programa> programaQueryWrapper = new QueryWrapper<>();
            programaQueryWrapper.in("CHANNELID", id).or().in("PARENTID", id);
            List<Programa> list1 = baseMapper.selectList(programaQueryWrapper);
            list1.forEach(s->list.add(s));
        }

       /* List<Programa> list = super.list(new PropertyWrapper<>(Programa.class).in("id",ids)
                .or().in("parentId",ids));*/

        List<Programa> parentList = new ArrayList<>();
        List<Programa> childList = new ArrayList<>();
        list.forEach(res->{
            if(res.getParentId() == null || res.getParentId()==0){
                res.setStatus(status);
                parentList.add(res);
            }else{
                res.setStatus(status);
                childList.add(res);
            }
        });
        return updateParent(parentList,childList,status);
    }
   /* @Resource
    private PictureMapper pictureMapper;*/

    /**
     * 更新parent删除状态
     * @param parentList
     * @param status
     */
    @Resource
    private PicturewqMapper pictureqMapper;
    private boolean updateParent(List<Programa> parentList,List<Programa> childList,int status){
        if(status==1){
            boolean result = false;
            if(parentList.size()>0){
                //result = super.updateBatchById(parentList);

                parentList.forEach(s->{
                    Boolean delectb = pictureqMapper.Delectb(s.getId(), s.getSiteId());
                        }
                );
                result = this.updateBatchById(parentList);
            }
            if(childList.size()>0){


                childList.forEach(s->{
                                pictureqMapper.Delectb(s.getId(), s.getSiteId());
                            }
                    );
                    result = this.updateBatchById(childList);


                //childList.forEach(res->{super.updateBatchById(res)});
               // result = super.updateBatchById(childList);
            }
            return result;
        }else{
            String siteIds = parentList.stream().map(res-> String.valueOf(res.getSiteId())).collect(Collectors.joining(","));
            List<Site> siteList = siteService.list(new PropertyWrapper<>(Site.class).in("id",siteIds).eq("status",0));
            Map<String,Site> siteMap = new HashMap<>();
            siteList.forEach(res->{
                siteMap.put(String.valueOf(res.getId()),res);
            });

            List<Programa> tempList = new ArrayList<>();
            parentList.forEach(res->{
                if(siteMap.containsKey(String.valueOf(res.getSiteId()))){
                    tempList.add(res);
                }
            });
            return updateChild(tempList,childList);
        }
    }
    /**
     * 更新child状态
     * @param parentList
     * @param childList
     * @return
     */
    private boolean updateChild(List<Programa> parentList,List<Programa> childList){
        Map<String,Programa> map = new HashMap<>();
        parentList.forEach(res->{
            map.put(String.valueOf(res.getId()),res);
        });

        List<Programa> tempList = new ArrayList<>();
        childList.forEach(res->{
            if(map.containsKey(String.valueOf(res.getParentId()))){
                tempList.add(res);
            }
        });

        boolean result = false;
        if(parentList.size()>0){
            result = super.updateBatchById(parentList);
        }
        if(tempList.size()>0){
            result = super.updateBatchById(tempList);
        }
        return result;
    }




    /**
     * 根据条件查询子节点
     * @param parentId
     * @param type
     * @return
     */
    @Override
    public List<Programa> getByParentId(Long parentId,int type) {
       if(type==0){
           return getByparent(parentId);
       }else{
           return getBySite(parentId);
       }
    }

    /**
     * 根据parentid获取数据
     * @param parentId
     * @return
     */
    private List<Programa> getByparent(Long parentId){
        List<String> seqList = new ArrayList<>();
        seqList.add("chnlOrder");
        return super.list(new PropertyWrapper<>(Programa.class).eq("parentId",parentId).eq("status",0)
                .orderBy(seqList,true));
    }

    /**
     * 根据parentid获取数据
     * @param parentId
     * @return
     */
    @Override
    public List<Programa> getAllByparent(Long parentId){
        List<Programa> list = new ArrayList<>();
        getAllByparent1(parentId,list);
        return list;
    }

    @Override
    public Programa getselect(Long id) {
        QueryWrapper<Programa> programaQueryWrapper = new QueryWrapper<>();
        programaQueryWrapper.eq("channelid",id);
        Programa programa = baseMapper.selectOne(programaQueryWrapper);
        return programa;
    }

    /**
     * 根据parentid获取数据
     * @param parentId
     * @return
     */
    private void getAllByparent1(Long parentId,List<Programa> list){
        List<Programa> byparent = getByparent(parentId);
        list.addAll(byparent);
        for (Programa programa : byparent) {
            getAllByparent1(programa.getId(),list);
        }
    }

    /**
     * 根据siteid获取数据
     * @param siteId
     * @return
     */
    private List<Programa> getBySite(Long siteId){
        List<String> seqList = new ArrayList<>();
        seqList.add("chnlOrder");
        return super.list(new PropertyWrapper<>(Programa.class).eq("siteId",siteId).eq("parentId",0)
                .eq("status",0).orderBy(seqList,true));
    }

    /**
     * 根据站点ids更新栏目状态
     * @param siteIds
     * @param status
     * @return
     */
    @Override
    public boolean updateBySite(String siteIds, int status) {
        List<Programa> list = super.list(new PropertyWrapper<>(Programa.class).in("siteId",siteIds));
        if(list.size()<=0){
            return true;
        }
        list.forEach(res->{
            res.setStatus(status);
        });
        return super.updateBatchById(list);
    }


    /**
     * 下载数据到Excel
     * @param os
     * @param sheetName
     * @param title
     * @param programaList
     */
    @Override
    public void upLoadExcel(ServletOutputStream os, String sheetName, String[] title, List<Programa> programaList) {
        try {
            HSSFWorkbook wb=new HSSFWorkbook();

            //创建sheet
            HSSFSheet sheet = wb.createSheet(sheetName);

            //创建标题行
            HSSFRow titleRow = sheet.createRow(0);

            //创建样式
            HSSFCellStyle style = wb.createCellStyle();
            //style.setAlignment(HorizontalAlignment.CENTER);
            HSSFDataFormat format = wb.createDataFormat();
            style.setDataFormat(format.getFormat("@"));


            HSSFCell cell=null;
            for (int i=0;i<title.length;i++){
                sheet.setColumnWidth(i,5*512);
                 cell= titleRow.createCell(i);
                 cell.setCellValue(title[i]);
                 cell.setCellStyle(style);
            }

            //填充内容
            int rowIndex=1;
            for (Programa programa : programaList) {
                HSSFRow row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(programa.getName());
                row.createCell(1).setCellValue(programa.getId()+"");
                row.createCell(2).setCellValue(programa.getParentId()+"");
                rowIndex++;
            }

            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(0,sheet.getColumnWidth(0)*17/10);
            sheet.autoSizeColumn(1);
            sheet.setColumnWidth(1,sheet.getColumnWidth(1)*17/10);
            sheet.autoSizeColumn(2);
            sheet.setColumnWidth(2,sheet.getColumnWidth(2)*17/10);

            wb.write(os);
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 导入本地Excel文件
     * @param file
     * @return
     */
    @Override
    public List<Programa> readExcel(MultipartFile file,Long columnId,Long siteId) {
        HSSFWorkbook wb=null;
        List<Programa> programaList=new ArrayList<>();
        try {
            BufferedInputStream in=new BufferedInputStream(file.getInputStream());
            POIFSFileSystem ps=new POIFSFileSystem(in);
            wb=new HSSFWorkbook(ps);
            //获取sheet
            for (int sheetIndex=0;sheetIndex<wb.getNumberOfSheets();sheetIndex++){
                //提取数据
                HSSFSheet sheet = wb.getSheetAt(sheetIndex);
                for (int rowIndex=1;rowIndex<=sheet.getLastRowNum();rowIndex++){
                    HSSFRow row = sheet.getRow(rowIndex);
                    if (row==null){
                        continue;
                    }
                    Programa p=new Programa();
                    HSSFCell cell = row.getCell(0);
                    p.setName(cell.getStringCellValue());
                    cell = row.getCell(1);
                    p.setSkuNumber(cell.getStringCellValue());

                    p.setSiteId(siteId);
                    p.setParentId(columnId);
                    p.setStatus(0);
                    p.setMetadataType("table");
                    p.setStop(0);
                    programaList.add(p);
                }
            }
            return programaList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 获取栏目树  机构-站点-栏目
     * @return
     */
    @Override
    public Programa getTreeData(Long id) {
        Map<Long, List<Programa>> trees = getTrees();
        Programa byId = super.getById(id);
        byId.setChildren1(trees.get(id));
        return byId;
    }

    /**
     * 获取顶级栏目parentId=0
     * @return
     */
    @Override
    public List<Programa> getFirstColumn() {
        List<Programa> programaList = super.list(new PropertyWrapper<>(Programa.class).eq("parentid", 0));
        return programaList;
    }

    @Override
    public void updateSeq(Programa byId) {
        List<Programa> list = super.list(new PropertyWrapper<>(Programa.class)
                .eq("siteId", byId.getSiteId())
                .eq("parentId", byId.getParentId())
                .ge("chnlOrder", byId.getChnlOrder()));
        for (Programa programa : list) {
            programa.setChnlOrder(programa.getChnlOrder()+1);
        }
        super.updateBatchById(list);
    }

    @Override
    public void updateSeq(Long id, int sort) {
        Programa byId = super.getById(id);
        List<Programa> list = super.list(new PropertyWrapper<>(Programa.class)
                .eq("siteId", byId.getSiteId())
                .eq("parentId", byId.getParentId())
                .ge("chnlOrder", sort)
                .ne("id", id));
        for (Programa programa : list) {
            programa.setChnlOrder(programa.getChnlOrder()+1);
        }
        super.updateBatchById(list);
        byId.setChnlOrder(sort);
        super.updateById(byId);
    }

    /**
     * 获取导航路径
     */
    @Override
    public JsonResult getNaviPath(Long id){
        try {
            //路径上的所有栏目
            List<Programa> list = new ArrayList<>();
            //获取当前栏目
            Programa programa = this.getById(id);
            list.add(programa);
            //上一级栏目id
            Long parentId = ObjectUtils.isEmpty(programa) ? null : programa.getParentId();
            //循环获取上一级栏目
            while (!ObjectUtils.isEmpty(parentId)){
                Programa parent = this.getById(parentId);
                parentId = ObjectUtils.isEmpty(parent) ? null : parent.getParentId();
                list.add(parent);
            }

            //按顺序组织导航路径
            StringBuilder naviPath = new StringBuilder();
            for (int i = list.size()-1; i >= 0 ; i--) {
                if(!ObjectUtils.isEmpty(list.get(i))){
                    naviPath.append(" > ").append(list.get(i).getName());
                }
            }

            if(naviPath.length() > 0){
                return JsonResult.success(naviPath.toString().substring(3));
            }else{
                return JsonResult.success();
            }
        }catch (Exception e){
            log.error("获取导航路径异常：{}", e.getMessage());
            return JsonResult.fail();
        }
    }
}
