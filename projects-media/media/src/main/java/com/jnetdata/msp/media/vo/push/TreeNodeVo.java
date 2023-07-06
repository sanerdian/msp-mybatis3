package com.jnetdata.msp.media.vo.push;

import lombok.Data;

import java.util.*;

@Data
public class TreeNodeVo {
    public static String TYPE_DW="单位";
    public static String TYPE_XSJG="下属机构";
    String id;
    String pid;
    String name;
    String type="单位";//单位部门树默认是单位；另外一个取值为下属部门
    List<TreeNodeVo> children;

    /**
     * 根据id与pid的父子关系，将子节点放到其父节点的children中
     * @param vos
     * @return
     */
    public static List<TreeNodeVo> listtoTree(List<TreeNodeVo> vos){
        HashMap<String, TreeNodeVo> treeNodeMap=new HashMap<>();//key为id，value为TreeNodeVo
        for(TreeNodeVo vo:vos){
            treeNodeMap.put(vo.getId(),vo);
        }
        HashSet<String> pids = new HashSet<>();//存pid
        int num;
        do{
            pids.clear();
            for(TreeNodeVo vo:treeNodeMap.values()){
                pids.add(vo.getPid());
            }
            num=treeNodeMap.size();
            Iterator<Map.Entry<String, TreeNodeVo>> iterator = treeNodeMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, TreeNodeVo> next = iterator.next();
                String id=next.getKey();
                TreeNodeVo vo=next.getValue();
                if(!pids.contains(id)){//当前节点的id不在父节点列表中，即当前节点不是父节点
                    String pid=vo.getPid();
                    TreeNodeVo parent=treeNodeMap.get(pid);
                    if(parent!=null){//如果取到了当前节点的父节点
                        List<TreeNodeVo> children=parent.getChildren();
                        if(children==null){
                            children=new ArrayList<>();
                            parent.setChildren(children);
                        }
                        children.add(vo);//则将当前节点放到父节点的children中
                        iterator.remove();
                    }
                }
            }

        }while(num!=treeNodeMap.size());
        return new ArrayList<>(treeNodeMap.values());
    }
}
