package org.lzx.admin;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.lzx.admin.controller.system.SysRoleController;
import org.lzx.common.domain.entity.SysRole;
import org.lzx.common.domain.vo.SysMenuVO;
import org.lzx.common.utils.JwtTokenUtil;
import org.lzx.system.mapper.SysResourceMapper;
import org.lzx.system.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class AdminApplicationTest {

    @Autowired
    SysResourceMapper sysResourceMapper;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNzE4NjExODM3OTk5LCJpc3MiOiJwZWEiLCJleHAiOjE3MTg2NTUwMzgsImlhdCI6MTcxODYxMTgzNywidXNlcm5hbWUiOiJTb3liZWFuIn0.k15xWXVg7eon9v8ucVHnpwwI10RAH6JkeYWn1sdorQVNwdKb7iPv3KJR5jE5IHWaAEwHLyYKS2qdY6OnzsjW6Q";

    private String toke = "eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNzIxMzkxNDU4NTc1LCJpc3MiOiJITFoiLCJleHAiOjE3MjE0MzQ2NTgsImlhdCI6MTcyMTM5MTQ1OCwidXNlcm5hbWUiOiJTb3liZWFuIn0.ZhpXf4tUM98bGVNC98j7nnJa3mi0IB4frz1Z8NcZ_WDZ1Y-BN_Za8H4q2oNCBBGJntOHVPeI-UAPY49BFZhjZw";

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    SysRoleMapper sysRoleMapper;

    @Autowired
    SysRoleController sysRoleController;
    @Test
    public void tokenTest() {
        String token = jwtTokenUtil.generateToken("HLZ");
        System.out.println(token);
        System.out.println(jwtTokenUtil.getUserNameFromToken(token));
        System.out.println(jwtTokenUtil.canRefresh(token));
    }

    @Test
    public void test() {
        System.out.println(sysResourceMapper.checkMenuNameUnique("异常页面", 0));
    }


    @Test
    public void testAddRole(){
//        LocalDateTime localDateTime = new LocalDateTime();
//        创建LocalDateTime类
        LocalDateTime localDateTime = LocalDateTime.now();
        SysRole sysRole = new SysRole();
        sysRole.setRoleDesc("测试角色");
        sysRole.setType(1);
        sysRole.setStatus("0");
        sysRole.setCreateId(1L);
        sysRole.setCreateBy("admin");
        sysRole.setUpdateId(1L);
        sysRole.setCreateTime(localDateTime);
        sysRole.setUpdateBy("admin");
        sysRole.setUpdateTime(localDateTime);
        sysRole.setDeleteTime(localDateTime);
        sysRole.setIsDeleted(0);
        System.out.println(sysRoleController.addRole(sysRole));
    }
}
