package com.epamlearning.configs;

import com.epamlearning.security.JWTFilter;
import com.epamlearning.security.JWTUtil;
import com.epamlearning.services.UserService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.EnumSet;

public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{ApplicationConfig.class, SwaggerConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//
//        FilterRegistration.Dynamic fr = servletContext.addFilter("jwtFilter", JWTFilter.class);
//        fr.addMappingForUrlPatterns(null, true, "/*");
//
//        super.onStartup(servletContext);
//    }
}
