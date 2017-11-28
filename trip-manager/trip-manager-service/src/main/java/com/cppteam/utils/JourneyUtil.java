package com.cppteam.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义行程结果集封装工具
 * Created by happykuan on 2017/11/8.
 */
public class JourneyUtil implements Serializable {
    private static final long serialVersionUID = 3042158400740267714L;
    private Object element;
    private List<JourneyUtil> subordinates;

    public JourneyUtil(Object element) {
        this.element = element;
    }

    public JourneyUtil() {}

    public void add(JourneyUtil r) {
        if (subordinates == null) {
            subordinates = new ArrayList<>();
        }
        subordinates.add(r);
    }

    public void remove(JourneyUtil r) {
        subordinates.remove(r);
    }

    public List<JourneyUtil> getSubordinates() {
        return subordinates;
    }

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public void setSubordinates(List<JourneyUtil> subordinates) {
        this.subordinates = subordinates;
    }
}
