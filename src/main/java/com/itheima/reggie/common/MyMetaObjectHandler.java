package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

//自定义的元数据对象处理器
@Component //交由Spring框架管理
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    //插入操作时执行自动填充
    @Autowired
    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());

        Long userId =(Long) request.getSession().getAttribute("employee");

        metaObject.setValue("createUser",userId);
        metaObject.setValue("updateUser",userId);
    }
    //更新操作时执行自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        Long userId =(Long) request.getSession().getAttribute("employee");

        metaObject.setValue("updateTime",LocalDateTime.now());

        metaObject.setValue("updateUser",userId);
    }
}
