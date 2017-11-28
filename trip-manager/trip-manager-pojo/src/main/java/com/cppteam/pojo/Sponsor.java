package com.cppteam.pojo;

import java.io.Serializable;
import java.util.Date;

public class Sponsor implements Serializable{
    private static final long serialVersionUID = -969475172499383830L;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sponsor.id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sponsor.journey_id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    private String journeyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sponsor.user_id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    private String userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sponsor.sponsor_time
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    private Date sponsorTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sponsor.id
     *
     * @return the value of sponsor.id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sponsor.id
     *
     * @param id the value for sponsor.id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sponsor.journey_id
     *
     * @return the value of sponsor.journey_id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public String getJourneyId() {
        return journeyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sponsor.journey_id
     *
     * @param journeyId the value for sponsor.journey_id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId == null ? null : journeyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sponsor.user_id
     *
     * @return the value of sponsor.user_id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sponsor.user_id
     *
     * @param userId the value for sponsor.user_id
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sponsor.sponsor_time
     *
     * @return the value of sponsor.sponsor_time
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public Date getSponsorTime() {
        return sponsorTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sponsor.sponsor_time
     *
     * @param sponsorTime the value for sponsor.sponsor_time
     *
     * @mbggenerated Thu Oct 26 21:12:04 CST 2017
     */
    public void setSponsorTime(Date sponsorTime) {
        this.sponsorTime = sponsorTime;
    }
}