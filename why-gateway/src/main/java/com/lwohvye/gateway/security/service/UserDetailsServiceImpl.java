/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.lwohvye.gateway.security.service;

import com.lwohvye.gateway.security.config.bean.LoginProperties;
import com.lwohvye.gateway.security.service.dto.JwtUserDto;
import com.lwohvye.sysadaptor.service.ISysDeptFeignClientService;
import com.lwohvye.utils.result.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
// 这里声明了UserDetailsService的实现使用这一个。因为该接口有多个实现
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserLocalCache userLocalCache;
    private final ISysDeptFeignClientService deptFeignClientService;
    private final LoginProperties loginProperties;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        JwtUserDto jwtUserDto;
        if (loginProperties.isCacheEnable()) {
            jwtUserDto = userLocalCache.userLRUCache.get(username); // 这个Cache在目标不存在时，会执行定义的获取方法，若方法中抛出异常，会直接抛出
            var userInner = jwtUserDto.getUser();
            // 检查dataScope是否修改
            List<Long> dataScopes = jwtUserDto.getDataScopes();
            dataScopes.clear();
            var resultInfo = deptFeignClientService.queryEnabledDeptIds(userInner.getId(), userInner.getDeptId());
            dataScopes.addAll(ResultUtil.getListFromResp(resultInfo));
        } else {
            jwtUserDto = userLocalCache.getUserDB(username);
        }
        return Mono.just(jwtUserDto);
    }
}
