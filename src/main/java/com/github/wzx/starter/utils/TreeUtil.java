package com.github.wzx.starter.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class TreeUtil {

    private TreeUtil() {
    }

    private static final String GET_PARENTEID = "getParentId";

    private static final String GET_CHILDREN = "getChildren";

    private static final String GET_ID = "getId";

    private static final String SET_CHILDREN = "setChildren";

    /**
     * 将list转为树结构
     *
     * @param list 需要转换的list
     * @param clz  list的泛型
     * @param <T>  泛型
     * @return
     */
    public static <T> List<T> listExchargeTree(List<T> list, Class<T> clz) {
        List<T> result = new ArrayList();
        if (list.isEmpty()) {
            return result;
        }
        try {
            Method getParentID = clz.getDeclaredMethod(GET_PARENTEID);
            Method getChildren = clz.getDeclaredMethod(GET_CHILDREN);
            Method getId = clz.getDeclaredMethod(GET_ID);
            Method setChildren = clz.getDeclaredMethod(SET_CHILDREN, Set.class);

            for (T vo1 : list) {
                if (getChildren.invoke(vo1) == null) {
                    setChildren.invoke(vo1, new LinkedHashSet<>());
                }
                if (getParentID.invoke(vo1).equals(0L)) {
                    result.add(vo1);
                }
                for (T vo2 : list) {
                    if (!getParentID.invoke(vo2).equals(0L) && getParentID.invoke(vo2).equals(getId.invoke(vo1))) {
                        if (getChildren.invoke(vo1) == null) {
                            setChildren.invoke(vo1, new LinkedHashSet<>());
                        }
                        ((LinkedHashSet) getChildren.invoke(vo1)).add(vo2);
                    }
                }
            }
        } catch (Exception e) {
            log.info("没有规定的方法：{}", e);
            return new ArrayList<>();
        }
        return result;
    }
}
