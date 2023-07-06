package com.jnetdata.msp.manage.site.mapper;

import com.jnetdata.msp.column.model.Channel;
import com.jnetdata.msp.core.mapper.BaseMapper;
import com.jnetdata.msp.manage.site.model.Site;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SiteMapper extends BaseMapper<Site> {
    @Update("update websiteinfo set SITEORDER = SITEORDER+1 where SITEORDER >= #{siteOrder}")
    void updateSeq(Site entity);


    @Select("<script>SELECT CHANNELID as id,CHNLNAME as name FROM CHANNELINFO  WHERE STATUS = 0 and SITEID=#{webSiteId} and (LISTTPL is not null or EDITTPL is not null) ORDER BY CHNLORDER desc"
            + "</script>")
    List<Channel> getChannelList(@Param("webSiteId")long webSiteId);
}
