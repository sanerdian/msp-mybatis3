package com.jnetdata.msp.exercise.carousel_image.service.impl;

import com.jnetdata.msp.exercise.carousel_image.model.CarouselImage;
import com.jnetdata.msp.exercise.carousel_image.mapper.CarouselImageMapper;
import com.jnetdata.msp.exercise.carousel_image.service.CarouselImageService;
import com.jnetdata.msp.core.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenicesys.data.api.Pager;
import org.thenicesys.mybatis.impl.PropertyWrapper;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.function.Consumer;


/**
 * <p>
 * 轮播图片 服务实现类
 * </p>
 *
 * @author zyj
 * @since 2023-02-10
 */
@Service
public class CarouselImageServiceImpl extends BaseServiceImpl<CarouselImageMapper, CarouselImage> implements CarouselImageService {


    private String andOr = "and";
    @Override
    public PropertyWrapper<CarouselImage> makeSearchWrapper(Map<String, Object> condition) {
        if(condition.get("andOr")!=null) this.andOr = condition.get("andOr").toString();
        PropertyWrapper<CarouselImage> pw = createWrapperUtil(condition)
                .eq("imageUrl")
                .eq("imageDesc")
        .eq("createBy")
        .getWrapper();
        if (condition.get("columnid") != null) {
            pw.and(m -> m.eq("columnid",condition.get("columnid")).or().like("quotainfo",condition.get("columnid") + ":"));
        }

        if(condition.get("pubIds")!=null){
            List<Long> pubIds = (List<Long>)condition.get("pubIds");
            pw.in(!pubIds.isEmpty(),"id",pubIds);
        }

        return pw;
    }

    @Override
    public CarouselImage getEntityAndJoinsById(Long id){
        CarouselImage entity = getById(id);
        getJoin(entity, null);
        return entity;
    }

    public void getJoin(CarouselImage entity, CarouselImage vo) {
        if(vo == null) vo = new CarouselImage();
    }

    @Override
    public void getListJoin(List<CarouselImage> list, CarouselImage vo) {
        for (CarouselImage entity : list) {
            getJoin(entity, vo);
        }
    }

    public void andOr(Map<String,Object>condition,PropertyWrapper pw,String proName){
        if(condition.get(proName)!=null&&StringUtils.isNotEmpty(condition.get(proName).toString())){
            Consumer<QueryWrapper<CarouselImage>> queryWrapperConsumer = w -> {
                for(String str:condition.get(proName).toString().split(" ")){
                    if(StringUtils.isNotEmpty(str)){
                        if(andOr.equals("or" )) w.or();
                        w.like(pw.getColumn(proName),str);
                    }
                }
            };
            pw.and(queryWrapperConsumer);
        }
    }
}
