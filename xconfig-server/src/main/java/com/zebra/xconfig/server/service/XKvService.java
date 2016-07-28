package com.zebra.xconfig.server.service;

import com.zebra.xconfig.server.po.KvPo;
import com.zebra.xconfig.server.vo.KvVo;
import com.zebra.xconfig.server.vo.Pagging;

import java.util.List;

/**
 * Created by ying on 16/7/19.
 */
public interface XKvService {
    public List<KvPo> queryByProjectAndProfile(String project,String profile);

    public Pagging<KvPo> queryByProjectAndProfilePagging(String project,String profile,int pageNum,int pageSize);

    public void addKv(KvPo kvVo) throws Exception;

    public void addKvs(List<KvPo> kvPos) throws Exception;

    public void updateKv(KvPo kvPo) throws Exception;

    public void removeKvBykey(String profile,String key) throws Exception;

    public List<KvPo> queryByProjectAndProfileWithDeps(String project,String profile);

}
