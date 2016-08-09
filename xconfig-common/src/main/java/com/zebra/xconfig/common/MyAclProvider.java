package com.zebra.xconfig.common;

import org.apache.curator.framework.api.ACLProvider;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying on 16/8/9.
 */
public class MyAclProvider implements ACLProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String userName;
    private String password;
    public MyAclProvider(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    @Override
    public List<ACL> getDefaultAcl() {
        List<ACL> acls = new ArrayList<>();

        try {
            Id id = new Id("digest", DigestAuthenticationProvider.generateDigest(userName + ":" + password));
            ACL acl = new ACL(ZooDefs.Perms.ALL,id);
            acls.add(acl);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(),e);
        }
        return acls;
    }

    @Override
    public List<ACL> getAclForPath(String path) {
        return null;
    }
}
