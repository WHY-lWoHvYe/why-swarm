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

package com.lwohvye.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

public class DateUtilsTest {
    @Test
    public void test1() {
        long l = System.currentTimeMillis() / 1000;
        LocalDateTime localDateTime = DateUtil.fromTimeStamp(l);
        System.out.print(DateUtil.localDateTimeFormatyMdHms(localDateTime));
    }

    @Test
    public void test2() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(DateUtil.localDateTimeFormatyMdHms(now));
        Date date = DateUtil.toDate(now);
        LocalDateTime localDateTime = DateUtil.toLocalDateTime(date);
        System.out.println(DateUtil.localDateTimeFormatyMdHms(localDateTime));
        LocalDateTime localDateTime1 = DateUtil.fromTimeStamp(date.getTime() / 1000);
        System.out.println(DateUtil.localDateTimeFormatyMdHms(localDateTime1));
    }
}
