package org.lzx.common.utils;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.lzx.common.domain.entity.SysResource;
import org.lzx.common.domain.vo.SysMenuTreeVO;
import org.lzx.common.domain.vo.SysMenuVO;
import org.lzx.common.domain.vo.SysRoutesVO;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RouteUtil {

    public List<SysRoutesVO> processRoute(List<SysResource> resourceList) {
        Map<Long, SysRoutesVO> map = new HashMap<>();

        // 先将所有资源按照父节点ID进行分组
        for (SysResource sysResource : resourceList) {
            SysRoutesVO sysRoutesVO = new SysRoutesVO();
            sysRoutesVO.setName(sysResource.getRouteName());
            sysRoutesVO.setPath(sysResource.getRoutePath());
            sysRoutesVO.setComponent(sysResource.getComponent());
            sysRoutesVO.setMeta(JSONUtil.parse(sysResource.getMeta()));
            sysRoutesVO.setChildren(new ArrayList<>());

            map.put(sysResource.getId(), sysRoutesVO);

            // 如果是根节点则直接加入结果列表
            if (sysResource.getParentId() == 0) {
                map.put(sysResource.getId(), sysRoutesVO);
            } else {
                // 如果不是根节点，则加入父节点的 children 列表
                SysRoutesVO parent = map.get(Long.parseLong(sysResource.getParentId().toString()));
                if (parent != null) {
                    parent.getChildren().add(sysRoutesVO);
                }
            }
        }

        // 获取所有根节点并返回
        List<SysRoutesVO> result = new ArrayList<>();
        for (SysResource sysResource : resourceList) {
            if (sysResource.getParentId() == 0) {
                result.add(map.get(sysResource.getId()));
            }
        }
        return result;
    }

    public List<SysMenuVO> processMenu(List<SysMenuVO> sysMenuVOList) {
        Map<Long, SysMenuVO> map = new HashMap<>();
        // 先将所有资源按照父节点ID进行分组
        for (SysMenuVO sysMenuVO : sysMenuVOList) {
            sysMenuVO.setChildren(new ArrayList<>()); // 初始化children属性

            map.put(sysMenuVO.getId(), sysMenuVO);

            // 如果是根节点则直接加入结果列表
            if (sysMenuVO.getParentId() == 0) {
                map.put(sysMenuVO.getId(), sysMenuVO);
            } else {
                // 如果不是根节点，则加入父节点的 children 列表
                SysMenuVO parent = map.get(Long.parseLong(sysMenuVO.getParentId().toString()));
                if (parent != null) {
                    parent.getChildren().add(sysMenuVO);
                }
            }
        }

        // 获取所有根节点并返回
        List<SysMenuVO> result = new ArrayList<>();
        for (SysMenuVO sysMenuVO : sysMenuVOList) {
            if (sysMenuVO.getParentId() == 0) {
                result.add(map.get(sysMenuVO.getId()));
            }
        }
        return result;
    }


    /**
     * 通用函数：List<T> 转为 List<V> T为线性结构，V为树形结构
     *
     * @param list  转化的数据集合
     * @param clazz 转化的目标对象
     * @param map   类型字段不一致，需手动映射的map集合，该参数不是必传
     * @param <T>   转化的数据类型：线性结构
     * @param <V>   转化的目标对象类型：树形结构
     * @return 目标对象集合
     */
    public static <T, V> List<V> convert(List<T> list, Class<V> clazz, Map<String, String> map) throws Exception {
        // 存储已创建的树形结构节点
        Map<Object, V> treeNodeMap = new HashMap<>();
        // 根节点集合
        List<V> rootNodes = new ArrayList<>();

        // 获取线性结构中ID和parentID字段的映射
        String idFieldName = map.getOrDefault("id", "id");
        String parentFieldName ="parentId";

        for (T item : list) {
            // 动态创建目标对象
            V treeNode = clazz.getDeclaredConstructor().newInstance();

            // 将线性结构的字段值复制到树形结构对象中
            copyFields(item, treeNode, map);

            // 获取当前节点的ID和parentID
            Object id = getFieldValue(item, idFieldName);
            Object parentId = getFieldValue(item, parentFieldName);

            // 将当前节点添加到map中
            treeNodeMap.put(id, treeNode);

            // 判断当前节点是否为根节点
            if (parentId == null || parentId.equals(0L)) {
                rootNodes.add(treeNode);
            } else {
                // 获取父节点
                V parentNode = treeNodeMap.get(parentId);
                if (parentNode == null) {
                    // 父节点不存在时创建一个新的父节点（空壳）
                    parentNode = clazz.getDeclaredConstructor().newInstance();
                    treeNodeMap.put(parentId, parentNode);
                }
                // 将当前节点添加到父节点的子节点列表中
                addChild(parentNode, treeNode);
            }
        }

        return rootNodes;
    }

    private static <T, V> void copyFields(T source, V target, Map<String, String> map) throws Exception {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String sourceFieldName = entry.getKey();
            String targetFieldName = entry.getValue();
            Field sourceField = source.getClass().getDeclaredField(sourceFieldName);
            sourceField.setAccessible(true);
            Object value = sourceField.get(source);
            Field targetField = target.getClass().getDeclaredField(targetFieldName);
            targetField.setAccessible(true);
            targetField.set(target, value);
        }
    }

    private static <T> Object getFieldValue(T item, String fieldName) throws Exception {
        Field field = item.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(item);
    }

    private static <V> void addChild(V parent, V child) throws Exception {
        Field childrenField = parent.getClass().getDeclaredField("children");
        childrenField.setAccessible(true);
        List<V> children = (List<V>) childrenField.get(parent);
        if (children == null) {
            children = new ArrayList<>();
            childrenField.set(parent, children);
        }
        children.add(child);
    }


    // 示例用法
//    public static void main(String[] args) throws Exception {
//        List<SysMenuVO> list = new ArrayList<>();
//        SysMenuVO sysMenuVO = new SysMenuVO();
//        sysMenuVO.setMenuName("菜单1");
//        sysMenuVO.setId(1L);
//        sysMenuVO.setParentId(0L);
//        SysMenuVO sysMenuVO1 = new SysMenuVO();
//        sysMenuVO1.setMenuName("菜单2");
//        sysMenuVO1.setId(2L);
//        sysMenuVO1.setParentId(1L);
//        SysMenuVO sysMenuVO2 = new SysMenuVO();
//        sysMenuVO2.setMenuName("菜单3");
//        sysMenuVO2.setId(3L);
//        sysMenuVO2.setParentId(2L);
//        list.add(sysMenuVO);
//        list.add(sysMenuVO1);
//        list.add(sysMenuVO2);
//        Map<String, String> map = new HashMap<>();
//        map.put("id", "id");
//        map.put("parentId", "pId");
//        map.put("menuName", "label");
//
//        List<SysMenuTreeVO> treeNodes = convert(list, SysMenuTreeVO.class, map);
//
//        System.out.println(treeNodes);
//    }

}
