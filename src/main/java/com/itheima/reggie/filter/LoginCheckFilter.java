package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//检查用户是否已经完成了登录的过滤器
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*") //过滤器所需的注解
@Slf4j
public class LoginCheckFilter implements Filter {
    //Springboot封装的路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的uri
        String requestURI = request.getRequestURI();

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登录页面
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.如果不需要处理，直接放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }

        //4-1、需要处理，判断登录状态，如果已登录，直接放行
        if(request.getSession().getAttribute("employee") != null){
            filterChain.doFilter(request,response);
            return;
        }
        //4-2、需要处理，判断登录状态，如果已登录，直接放行
        if(request.getSession().getAttribute("user") != null){
            filterChain.doFilter(request,response);
            return;
        }

        //5、如果未登录返回未登录结果,通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    //进行路径匹配
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
