package com.jnetdata.msp.exercise.carousel_image.service;

import com.jnetdata.msp.exercise.carousel_image.model.CarouselImage;
import com.jnetdata.msp.core.service.BaseService;
import org.thenicesys.mybatis.impl.PropertyWrapper;

import java.util.Map;
import java.util.List;
/**
 * <p>
 * 轮播图片 服务类
 * </p>
 *
 * @author zyj
 * @since 2023-02-10
 */
public interface CarouselImageService extends BaseService<CarouselImage> {
        PropertyWrapper<CarouselImage> makeSearchWrapper(Map<String, Object> condition);
        CarouselImage getEntityAndJoinsById(Long id);
        void getListJoin(List<CarouselImage> list, CarouselImage vo);
}
