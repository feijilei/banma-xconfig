package com.zebra.xconfig.server.service.impl;

import com.zebra.xconfig.server.dao.mapper.WisdomMapper;
import com.zebra.xconfig.server.service.WisdomService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

/**
 * Created by ying on 16/7/28.
 */
@Service
public class WisdomServiceImpl implements WisdomService {

    private Random random;
    private List<String> wisdoms;
    @Autowired
    private WisdomMapper wisdomMapper;

    @PostConstruct
    public void init(){
        this.wisdoms = this.wisdomMapper.queryAllWisdom();
        this.random = new Random();
    }

    @Override
    public String getOne() {
        if(CollectionUtils.isEmpty(this.wisdoms)){
            return "";
        }

        return wisdoms.get(random.nextInt(wisdoms.size()));
    }
}
