package com.zebra.xconfig.server.dao.mapper;

import com.zebra.xconfig.server.po.KvPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ying on 16/7/18.
 */
public interface XKvMapper {
    public List<KvPo> queryAll();

    public List<KvPo> queryByProjectAndProfile(@Param("project")String project,@Param("profile")String profile);

    public List<KvPo> queryByProjectsAndProfile(@Param("projects")List<String> projects,@Param("profile")String profile);

    public List<KvPo> queryByProjectsAndProfilePagging(
            @Param("project")String project,
            @Param("profile")String profile,
            @Param("pageNum")int pageNum,
            @Param("pageSize")int pageSize
    );

    public void addOne(KvPo kvPo);

    public void updateOne(KvPo kvPo);

    public KvPo load(
            @Param("project")String project,
            @Param("profile")String profile,
            @Param("key")String key);

    public void delOne(
            @Param("project")String project,
            @Param("profile")String profile,
            @Param("key")String key
    );
}
