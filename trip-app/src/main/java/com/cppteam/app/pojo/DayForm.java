package com.cppteam.app.pojo;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

/**
 * Created by happykuan on 2017/10/28.
 */
public class DayForm implements Serializable {
    private static final long serialVersionUID = 7346363070882202464L;

    /**
     *  第几天
     */
    @DecimalMin(value = "1")
    private Integer count;

    @Valid
    private List<SiteForm> sites;

    public DayForm() {}

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<SiteForm> getSites() {
        return sites;
    }

    public void setSites(List<SiteForm> sites) {
        this.sites = sites;
    }
}
