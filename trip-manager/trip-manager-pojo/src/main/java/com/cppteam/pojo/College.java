package com.cppteam.pojo;

import java.io.Serializable;

public class College implements Serializable {
    private static final long serialVersionUID = 8511562937323557825L;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column college.cID
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    private Integer cid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column college.cName
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    private String cname;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column college.cID
     *
     * @return the value of college.cID
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column college.cID
     *
     * @param cid the value for college.cID
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column college.cName
     *
     * @return the value of college.cName
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public String getCname() {
        return cname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column college.cName
     *
     * @param cname the value for college.cName
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }
}