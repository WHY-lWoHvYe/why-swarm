package com.lwohvye.sysadaptor.service;

import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

class ISysUserFeignClientServiceTest {

    @Test
    void extendInterface() {
        Arrays.stream(ISysUserFeignClientService.class.getMethods()).forEach(method -> {
            System.out.println("method  -------- " + method.getName());
            Arrays.stream(method.getAnnotations()).forEach(System.out::println);
            System.out.println(AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class));
            System.out.println(AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class));
        });
    }

    @Test
    void implementsInterface() {
        Arrays.stream(SysUserFeignImpl.class.getMethods()).forEach(method -> {
            System.out.println("method  -------- " + method.getName());
            Arrays.stream(method.getAnnotations()).forEach(System.out::println);
            System.out.println(AnnotatedElementUtils.hasAnnotation(method, RequestMapping.class));
            System.out.println(AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class));
        });
    }
}
