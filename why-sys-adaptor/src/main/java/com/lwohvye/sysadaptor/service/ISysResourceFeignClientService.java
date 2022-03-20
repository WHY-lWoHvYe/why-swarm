package com.lwohvye.sysadaptor.service;

import com.lwohvye.modules.system.api.SysResourceAPI;
import com.lwohvye.sysadaptor.service.fallback.SysResourceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "sys-service", fallbackFactory = SysResourceFallbackFactory.class)
public interface ISysResourceFeignClientService extends SysResourceAPI {
}
