package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysUserAPI;
import com.lwohvye.api.modules.system.domain.User;
import com.lwohvye.api.modules.system.domain.vo.UserPassVo;
import com.lwohvye.sysadaptor.service.fallback.SysUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import java.util.Set;

@FeignClient(value = "sys-service", fallbackFactory = SysUserFallbackFactory.class)
public interface ISysUserFeignClientService extends SysUserAPI {

    @Log("新增用户")
    @Override
    ResponseEntity<Object> create(User resources);

    @Log("修改用户")
    @Override
    ResponseEntity<Object> update(User resources) throws Exception;

    @Log("删除用户")
    @Override
    ResponseEntity<Object> delete(Set<Long> ids);

    @Log("修改密码")
    @Override
    ResponseEntity<Object> updatePass(UserPassVo passVo) throws Exception;
}
