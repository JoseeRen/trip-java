package com.cppteam.xcx.service.impl;

import com.cppteam.common.util.SerializeUtil;
import com.cppteam.common.util.TripResult;
import com.cppteam.dao.JedisClient;
import com.cppteam.mapper.SiteMapper;
import com.cppteam.pojo.Site;
import com.cppteam.pojo.SiteExample;
import com.cppteam.xcx.service.SiteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by happykuan on 2017/10/31.
 * @author happykuan
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private JedisClient jedisClient;
    @Value("${MINI_PRO_SITE_KEY}")
    private String MINI_PRO_SITE_KEY;
    @Value("${DEFAULT_NULL}")
    private String DEFAULT_NULL;
    @Value("${REDIS_EXPIRE_TIME}")
    private Integer REDIS_EXPIRE_TIME;

    @Autowired
    private SiteMapper siteMapper;

    private static final Class CLASS_LOCK = SiteServiceImpl.class;
    
    /**
     * 获取地点详细信息
     * @param siteId
     * @return
     */
    @Override
    public TripResult showSite(String siteId) {

        // 从缓存中读取
        try {
            String obj = jedisClient.get(MINI_PRO_SITE_KEY + siteId);
            if (StringUtils.isNotBlank(obj)) {
                return TripResult.ok("ok", SerializeUtil.unSerialize(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 数据库中获取
        SiteExample siteExample = new SiteExample();
        SiteExample.Criteria criteria = siteExample.createCriteria();
        criteria.andIdEqualTo(siteId);
        List<Site> sites = siteMapper.selectByExampleWithBLOBs(siteExample);
        if (sites.isEmpty()) {
            return TripResult.build(404, "not found");
        }
        Site site = sites.get(0);

        // 格式化数据
        site.setId(null);
        site.setDayId(null);
        if (StringUtils.isBlank(site.getImg())) {
            site.setImg(DEFAULT_NULL);
        }
        if (StringUtils.isBlank(site.getImgThumb())) {
            site.setImgThumb(DEFAULT_NULL);
        }

        // 存入redis缓存中
        try{
            synchronized (CLASS_LOCK) {
                Boolean exists = jedisClient.exists(MINI_PRO_SITE_KEY + siteId);
                if (!exists) {
                    jedisClient.set(MINI_PRO_SITE_KEY + siteId, SerializeUtil.serialize(site));
                    jedisClient.expire(MINI_PRO_SITE_KEY + siteId, REDIS_EXPIRE_TIME);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return TripResult.ok("ok", site);
    }
}
