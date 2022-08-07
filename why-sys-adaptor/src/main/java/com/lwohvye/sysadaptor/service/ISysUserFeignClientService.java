package com.lwohvye.sysadaptor.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.api.modules.system.api.SysUserAPI;
import com.lwohvye.api.modules.system.domain.User;
import com.lwohvye.api.modules.system.domain.vo.UserPassVo;
import com.lwohvye.api.modules.system.service.dto.UserQueryCriteria;
import com.lwohvye.sysadaptor.service.fallback.SysUserFallbackFactory;
import com.lwohvye.utils.result.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

// The bean 'sys-service.FeignClientSpecification' could not be registered. A bean with that name has already been defined and overriding is disabled.
// 一个项目中存在多个接口使用@FeignClient调用同一个服务，意思是说一个服务只能用@FeignClient使用一次。 否则需使用contextId标识成不同的
@FeignClient(value = "why-system", contextId = "userValentine", fallbackFactory = SysUserFallbackFactory.class)
public interface ISysUserFeignClientService extends SysUserAPI {

    @GetMapping(value = "/api/sys/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    ResponseEntity<ResultInfo<Map<String, Object>>> query(@SpringQueryMap UserQueryCriteria criteria, Pageable pageable);

    @Log("新增用户")
    @PostMapping("/api/sys/users")
    @Override
    ResponseEntity<ResultInfo<String>> create(User resources);

    @Log("修改用户")
    @PutMapping("/api/sys/users")
    @Override
    ResponseEntity<ResultInfo<String>> update(User resources) throws Exception;

    @Log("删除用户")
    @DeleteMapping("/api/sys/users")
    @Override
    ResponseEntity<ResultInfo<String>> delete(Set<Long> ids);

    @Log("修改密码")
    @PostMapping("/api/sys/users/updatePass")
    @Override
    ResponseEntity<ResultInfo<String>> updatePass(UserPassVo passVo) throws Exception;

    // 通过RequestMapping的headers属性设置。这里可以直接获取配置文件中的属性，试试能不能动态的获取一些属性，比如通过SpEL表达式
    @PostMapping(value = "/createUser1", headers = {"Content-Type=application/json;charset=UTF-8", "App-Secret=${app.secret}"})
    ResponseEntity<Object> createWithHeader(User resources);

    // 通过RequestHeader注解设置
    @PostMapping("/createUser2")
    ResponseEntity<Object> createWithHeader(User resources, @RequestHeader("Authorization") String token);
}
