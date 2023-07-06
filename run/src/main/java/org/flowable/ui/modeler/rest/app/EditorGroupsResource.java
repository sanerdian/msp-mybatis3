package org.flowable.ui.modeler.rest.app;

import org.apache.commons.lang3.StringUtils;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.ui.common.model.GroupRepresentation;
import org.flowable.ui.common.model.ResultListDataRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/app")
public class EditorGroupsResource {

    @Autowired
    protected IdmIdentityService idmIdentityService;

    @RequestMapping(value = "/rest/editor-groups", method = RequestMethod.GET)
    public ResultListDataRepresentation getGroups(@RequestParam(required = false, value = "filter") String filter) {
        if (StringUtils.isNotBlank(filter)) {
            filter = filter.trim();
            String sql = "select * from act_id_group where NAME_ like #{name}";
            filter = "%" + filter + "%";
            List<Group> groups = idmIdentityService.createNativeGroupQuery().sql(sql).parameter("name", filter).listPage(0, 10);
            List<GroupRepresentation> result = new ArrayList<>();
            for (Group group : groups) {
                result.add(new GroupRepresentation(group));
            }
            return new ResultListDataRepresentation(result);
        }
        return null;
    }
}
