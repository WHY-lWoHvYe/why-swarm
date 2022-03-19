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

import com.lwohvye.exception.BadRequestException;
import com.lwohvye.exception.EntityNotFoundException;
import com.lwohvye.gateway.security.config.bean.LoginProperties;
import com.lwohvye.gateway.security.handler.AuthHandlerContext;
import com.lwohvye.gateway.security.service.dto.JwtUserDto;
import com.lwohvye.modules.system.service.dto.UserInnerDto;
import com.lwohvye.utils.result.ResultInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
// 这里声明了UserDetailsService的实现使用这一个。因为该接口有多个实现
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserFeignClientService userFeignClientService;
    private final AuthHandlerContext authHandlerContext;
    private final IDateFeignClientService dateFeignClientService;
    private final LoginProperties loginProperties;

    public void setEnableCache(boolean enableCache) {
        this.loginProperties.setCacheEnable(enableCache);
    }

    /**
     * 用户信息缓存
     *
     * @see UserCacheClean
     */
    //  这种缓存的方式，也许解决了些问题，但导致无法做集群的扩展，后续解决。
    //  不能存redis中，使用fastjson时没什么问题。但使用jackson反序列化需要实体有空参构造。而SimpleGrantedAuthority无空参构造。
    static Map<String, JwtUserDto> userDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        boolean searchDb = true;
        JwtUserDto jwtUserDto = null;
        if (loginProperties.isCacheEnable() && userDtoCache.containsKey(username)) {
            jwtUserDto = userDtoCache.get(username);

            var userInner = jwtUserDto.getUser();
            // 检查dataScope是否修改
            List<Long> dataScopes = jwtUserDto.getDataScopes();
            dataScopes.clear();
            var deptEntity = dateFeignClientService.queryEnabledDeptIds(userInner.getId(), userInner.getDeptId());
            if (deptEntity.getBody() instanceof ResultInfo<?> resultInfo) {
                var deptIds = (List<Long>) resultInfo.getContent();
                dataScopes.addAll(deptIds);
            }
            searchDb = false;
        }
        if (searchDb) {
            UserInnerDto user;
            try {
                var userEntity = userFeignClientService.queryByName(username);
                if (userEntity.getBody() instanceof ResultInfo<?> resultInfo && resultInfo.getResult() instanceof UserInnerDto userInnerDto)
                    user = userInnerDto;
                else throw new UsernameNotFoundException(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }
            if (Objects.isNull(user.getId())) {
                throw new UsernameNotFoundException("");
            } else {
                if (Boolean.FALSE.equals(user.getEnabled())) {
                    throw new BadRequestException("账号未激活！");
                }
                // TODO: 2022/1/3 当下未处理服务侧报错返回错误信息的情况。且强转降低了灵活性/提升了耦合性
                var deptEntity = dateFeignClientService.queryEnabledDeptIds(user.getId(), user.getDeptId());
                List<Long> deptIds;
                if (deptEntity.getBody() instanceof ResultInfo<?> resultInfo)
                    deptIds = (List<Long>) resultInfo.getContent();
                else deptIds = null;
                jwtUserDto = new JwtUserDto(
                        user,
                        deptIds,
                        authHandlerContext.getInstance(Boolean.TRUE.equals(user.getIsAdmin()) ? 1 : 0).handler(user.getId())
                );
                userDtoCache.put(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
