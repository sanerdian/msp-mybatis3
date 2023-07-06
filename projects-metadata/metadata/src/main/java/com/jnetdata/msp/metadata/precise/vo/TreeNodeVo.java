package com.jnetdata.msp.metadata.precise.vo;

import com.jnetdata.msp.metadata.precise.entity.ValueHelper;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class TreeNodeVo {

    public static String TYPE_DW="单位";
    public static String TYPE_XSBM ="下属机构";
    String id;
    String pid;
    String name;
    String type="单位";//单位部门树默认是单位；另外一个取值为下属部门
    List<TreeNodeVo> children;

    public static List<TreeNodeVo> list2tree(List<TreeNodeVo> list,boolean siftRoot){
        List<TreeNodeVo> result=new ArrayList<>();
        if(!ObjectUtils.isEmpty(list)){
            Map<String, TreeNodeVo> idMap = list.stream().collect(Collectors.toMap(vo -> vo.getId(), vo -> vo));
            Map<String, List<TreeNodeVo>> parentMap = list.stream().collect(Collectors.groupingBy(vo -> vo.getPid()));
            Set<String> childSet =new HashSet<>();
            for(String id:parentMap.keySet()){
                if(id!=null&&idMap.containsKey(id)){
                    TreeNodeVo vo = idMap.get(id);
                    List<TreeNodeVo> vos = parentMap.get(id);
                    vo.setChildren(vos);
                    childSet.addAll(vos.stream().map(item->item.getId()).collect(Collectors.toSet()));
                }
            }
            for(String id:idMap.keySet()){
                if(!childSet.contains(id)){
                    TreeNodeVo vo = idMap.get(id);
                    if(!siftRoot||(0L== ValueHelper.longValue(vo.getPid())&&"company".equals(vo.getType()))){
                        result.add(vo);
                    }
                }
            }
            result.sort(Comparator.comparing(TreeNodeVo::getId));
        }
        return result;
    }

    public static long longValue(Object object){
        return longValue(object,0L);
    }
    public static Long longValue(Object object,Long defaultValue){
        if(object!=null&&object instanceof Number){
            return ((Number) object).longValue();
        }else {
            return defaultValue;
        }
    }

}
