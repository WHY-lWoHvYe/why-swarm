package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysUserAPI;
import com.lwohvye.api.modules.system.domain.User;
import com.lwohvye.api.modules.system.domain.vo.UserPassVo;
import com.lwohvye.sysadaptor.service.fallback.SysUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

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

    // 通过RequestMapping的headers属性设置。这里可以直接获取配置文件中的属性，试试能不能动态的获取一些属性，比如通过SpEL表达式
    @PostMapping(value = "/createUser1", headers = {"Content-Type=application/json;charset=UTF-8", "App-Secret=${app.secret}"})
    ResponseEntity<Object> createWithHeader(User resources);

    // 通过RequestHeader注解设置
    @PostMapping("/createUser2")
    ResponseEntity<Object> createWithHeader(User resources, @RequestHeader("Authorization") String token);
}
