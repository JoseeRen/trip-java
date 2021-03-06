package com.cppteam.mapper;

import com.cppteam.pojo.Journey;
import com.cppteam.pojo.JourneyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface JourneyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int countByExample(JourneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int deleteByExample(JourneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int insert(Journey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int insertSelective(Journey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    List<Journey> selectByExample(JourneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    Journey selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int updateByExampleSelective(@Param("record") Journey record, @Param("example") JourneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int updateByExample(@Param("record") Journey record, @Param("example") JourneyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int updateByPrimaryKeySelective(Journey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table journey
     *
     * @mbggenerated Sun Oct 29 10:56:54 CST 2017
     */
    int updateByPrimaryKey(Journey record);
}