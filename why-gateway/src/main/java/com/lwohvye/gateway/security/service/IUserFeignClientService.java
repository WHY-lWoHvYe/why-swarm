/*
 *    Copyright (c) 2022.  lWoHvYe(Hongyan Wang)
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

package com.lwohvye.gateway.security.service;

import com.lwohvye.annotation.log.Log;
import com.lwohvye.base.BaseEntity;
import com.lwohvye.modules.system.domain.User;
import com.lwohvye.modules.system.domain.vo.UserPassVo;
import com.lwohvye.modules.system.service.dto.UserQueryCriteria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@FeignClient(value = "el-system")
@RequestMapping("/api/sys/users")
public interface IUserFeignClientService {

    @GetMapping
    ResponseEntity<Object> query(UserQueryCriteria criteria, Pageable pageable);

    @Log("新增用户")
    @PostMapping
    ResponseEntity<Object> create(@Validated @RequestBody User resources);

    @Log("修改用户")
    @PutMapping
    ResponseEntity<Object> update(@Validated(BaseEntity.Update.class) @RequestBody User resources);

    @Log("修改用户：个人中心")
    @PutMapping(value = "center")
    ResponseEntity<Object> center(@Validated(BaseEntity.Update.class) @RequestBody User resources);

    @Log("删除用户")
    @DeleteMapping
    ResponseEntity<Object> delete(@RequestBody Set<Long> ids);

    @Log("修改密码")
    @PostMapping(value = "/updatePass")
    ResponseEntity<Object> updatePass(@RequestBody UserPassVo passVo);

    @PostMapping(value = "/updateAvatar")
    ResponseEntity<Object> updateAvatar(@RequestParam MultipartFile avatar);

    @Log("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    ResponseEntity<Object> updateEmail(@PathVariable String code, @RequestBody User user);

    @GetMapping("/name/{username}")
    ResponseEntity<Object> queryByName(@PathVariable String username);

}
