/*
 *    Copyright (c) 2021-2022.  lWoHvYe(Hongyan Wang)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.lwohvye.gateway.security.handler;

import com.lwohvye.gateway.security.annotation.UserTypeHandlerAnno;
import com.lwohvye.gateway.security.enums.UserTypeEnum;
import com.lwohvye.modules.system.service.dto.RoleSmallDto;
import com.lwohvye.sysadaptor.service.ISysRoleFeignClientService;
import com.lwohvye.utils.SpringContextHolder;
import com.lwohvye.utils.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hongyan Wang
 * @date 2021年11月02日 19:24
 */
@Slf4j
@Component
// 不能用下面这个注解，因为这个的使用方式，决定了要使用空参构造初始化。对于需要注入的对象，需特殊处理
//@RequiredArgsConstructor
@UserTypeHandlerAnno(UserTypeEnum.NORMAL)
public final class NormalUserTypeStrategy implements AUserTypeStrategy {

    private ISysRoleFeignClientService roleFeignClientService;

    /**
     * 属性注入。这里不使用@PostConstruct后置处理，是因为之前有验证在执行后置处理的时候，SpringContextHolder还无法获取到相关的bean（因为applicationContext还未注入）
     * 另，当下@PostConstruct并未被执行，这个跟使用@Autowire未注入roleRepository这两个问题，后续研究一下
     *
     * @date 2022/3/13 6:03 PM
     */
    @Override
    public void doInit() {
        this.roleFeignClientService = SpringContextHolder.getBean(ISysRoleFeignClientService.class);
    }

    @Override
    public List<GrantedAuthority> grantedAuth(Long userId) {
        log.warn(" banana：自由的气息，蕉迟但到。");
        var roleEntity = roleFeignClientService.queryByUid(userId);
        var roles = ResultUtil.getListFromResp(roleEntity, RoleSmallDto.class);
        var permissions = roles.stream().map(role -> "ROLE_" + role.getCode().toUpperCase()).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
