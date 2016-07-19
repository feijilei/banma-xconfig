package com.zebra.xconfig.server.dao.mapper;

import com.zebra.xconfig.server.po.KvPo;

import java.util.List;

/**
 * Created by ying on 16/7/18.
 */
public interface XKvMapper {
    public List<KvPo> queryAll();
}
