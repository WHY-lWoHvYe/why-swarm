package com.lwohvye.sysadaptor.service;

import com.lwohvye.modules.system.api.SysUserAPI;
import com.lwohvye.sysadaptor.service.fallback.SysUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "sys-service", fallbackFactory = SysUserFallbackFactory.class)
public interface ISysUserFeignClientService extends SysUserAPI {
}
