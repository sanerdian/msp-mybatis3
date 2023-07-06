package com.jnetdata.msp.member.group.service;

import com.jnetdata.msp.core.service.BaseService;
import com.jnetdata.msp.member.companyinfo.vo.BaseVo;
import com.jnetdata.msp.member.group.model.GrpUser;
import com.jnetdata.msp.member.user.model.User;
import com.jnetdata.msp.member.user.vo.AddUserVo;
import org.springframework.web.multipart.MultipartFile;
import org.thenicesys.data.api.Pager;

import java.util.List;

/**
 * Created by WFJ on 2019/4/1.
 */
public interface GrpUserService extends BaseService<GrpUser> {
    /**
     * 用户添加机构
     * @param userIds
     * @param GroupId
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    boolean addUserGroup(String userIds, String GroupId);

    /**
     * 获取机构下用户数据
     * @param vo
     * @author hongshou
     * @date 2020/5/26
     * @return
     */
    Pager<GrpUser> getUser(BaseVo<GrpUser> vo);

    boolean updataLoader(Long[] ids, int type);

    /*
    * 获取用户组织
    * @param userid
    * @author hongshou
    * @date 2020/5/26
    * return
    * */
    List<GrpUser> getGroupByUser(Long userId);

    void importGrpUser(List<List<String>> lists);

    /**
     * 导入互联网用户
     * @author 纪凯静
     * @date 2023/1/17
     * @param userList
     */
    void importGrpUserNew(List<User> userList);

    boolean checkInGroup(Long userId,String code);
    List<Long> FindOrganization(Long id);

    User addUser(AddUserVo vo);

    void updateUser(Long id, AddUserVo vo);
}
