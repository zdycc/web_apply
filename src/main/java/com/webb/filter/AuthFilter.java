package com.webb.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*") // 拦截所有请求
public class AuthFilter implements Filter {

    // 定义一个白名单，这些URL即使用户被要求强制修改密码也可以访问
    private static final Set<String> ALLOWED_PATHS_FOR_FORCED_CHANGE = new HashSet<>(Arrays.asList(
            "/changePassword",
            "/logout",
            "/css", // 允许访问CSS目录
            "/js",  // 允许访问JS目录
            "/img"  // 允许访问图片目录
    ));

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String path = request.getRequestURI().substring(request.getContextPath().length());

        // 检查是否需要强制修改密码
        if (session != null && session.getAttribute("forcePasswordChange") != null) {
            boolean forceChange = (Boolean) session.getAttribute("forcePasswordChange");
            if (forceChange) {
                boolean isAllowedPath = false;
                // 检查请求路径是否在白名单中
                for (String allowedPath : ALLOWED_PATHS_FOR_FORCED_CHANGE) {
                    if (path.equals(allowedPath) || path.startsWith(allowedPath + "/")) {
                        isAllowedPath = true;
                        break;
                    }
                }

                if (!isAllowedPath) {
                    // 如果请求的不是白名单中的路径，则强制重定向到修改密码页面
                    response.sendRedirect(request.getContextPath() + "/changePassword");
                    return; // 阻止请求继续
                }
            }
        }

        // 如果不需要强制修改密码，则继续正常的请求链
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
}