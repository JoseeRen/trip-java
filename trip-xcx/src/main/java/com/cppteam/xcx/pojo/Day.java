package com.cppteam.xcx.pojo;

import com.cppteam.pojo.Site;

import java.io.Serializable;
import java.util.List;

/**
 * Created by happykuan on 2017/11/1.
 */
public class Day implements Serializable{
    private static final long serialVersionUID = 5197924700518374077L;
    private String id;
    private Integer count;
    private List<Site> sites;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Site> getSites() {
        return sites;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }
}
