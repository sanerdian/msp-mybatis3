package org.flowable.ui.modeler.rest.app;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.ManagementService;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.User;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.flowable.ui.common.model.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app")
public class EditorUsersResource {

    @Autowired
    protected IdmIdentityService idmIdentityService;
    @Autowired
    protected ManagementService managementService;

    @RequestMapping(value = "/rest/editor-users", method = RequestMethod.GET)
    public ResultListDataRepresentation getUsers(@RequestParam(value = "filter", required = false) String filter) {
        if (StringUtils.isNotBlank(filter)) {
            filter = filter.trim();
            String sql = "select * from act_id_user where FIRST_ like #{name}";
            filter = "%"+filter+"%";
            List<User> matchingUsers = idmIdentityService.createNativeUserQuery().sql(sql).parameter("name",filter).listPage(0, 10);List<UserRepresentation> userRepresentations = new ArrayList<>(matchingUsers.size());
            for (User user : matchingUsers) {
                userRepresentations.add(new UserRepresentation(user));
            }
            return new ResultListDataRepresentation(userRepresentations);
        }
        return null;
    }

}
