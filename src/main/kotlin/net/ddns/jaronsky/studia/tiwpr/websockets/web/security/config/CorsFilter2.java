package net.ddns.jaronsky.studia.tiwpr.websockets.web.security.config;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class CorsFilter2 implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("ALA MA KOTA A KOT MA ALE, ALE ALA NIE MA ALI BO KOTA ALI NIE MA PO KOPALI");
//
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
//        response.setHeader("Access-Control-Allow-Origin", "localhost");
//        response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.setHeader("Access-Control-Allow-Credentials", "TRUE");
//        response.setHeader("Access-Control-Max-Age", "180");
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}