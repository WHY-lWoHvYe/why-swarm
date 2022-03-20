package com.lwohvye.sysadaptor.service;

import com.lwohvye.modules.system.api.SysRoleAPI;
import com.lwohvye.sysadaptor.service.fallback.SysRoleFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "sys-service", fallbackFactory = SysRoleFallbackFactory.class)
public interface ISysRoleFeignClientService extends SysRoleAPI {
}
