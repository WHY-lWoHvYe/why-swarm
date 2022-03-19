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
package com.lwohvye.modules.system.service.mapstruct;

import com.lwohvye.base.BaseMapper;
import com.lwohvye.modules.system.domain.User;
import com.lwohvye.modules.system.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
// 这里使用uses，相关D2E/E2D的转换会自动使用对应mapper中的，不必在本mapper中再生成一遍。但从父类来的default会存在不知道用哪个类中的问题，这点需注意
// 关于Mapper的定义：理论上需要直接使用的或者需要定义规则的，需要创建对应的Mapper，若是按照基本规则（主体为属姓名）转换的，也可以不进行定义，比如下面这些xxxSmallMapper，若不定义这些类，也是可以转换的。定义的优缺点可以看上一条
@Mapper(componentModel = "spring", uses = {RoleSmallMapper.class, DeptSmallMapper.class, JobSmallMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
//@Mapper(componentModel = "spring", uses = {RoleMapper.class, DeptMapper.class, JobMapper.class, TimestampMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
//若不移除ConvertBlob2StringUtil.class的使用，所以String2String的转换都会走这个方法了。所以还是要配置重新toDto来限制使用范围
//使用uses指定的转换规则，会自动使用，mapstruct会根据入和出自动使用转换规则，但使用maven的compile会报错，也就是说自动编译可以，但使用maven不行，原因未知。这个针对的下面这种使用方式
//@Mapper(componentModel = "spring", uses = {ConvertBlob2StringUtil.class, RoleMapper.class, DeptMapper.class, JobMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {
//    @Override
//    @Mapping(target = "description", source = "description", qualifiedBy = Blob2String.class)
//    UserDto toDto(User entity);
}
