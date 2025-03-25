package com.whoz_in.network_api.config;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.catalina.Wrapper;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * ping 응답용 서블릿을 직접 등록하는 설정 클래스
 * DispatcherServlet을 우회하여 톰캣 레벨에서 처리됨
 */
@Configuration
public class PingServletConfig {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> customizer() {
        return factory -> {
            if (factory instanceof TomcatServletWebServerFactory tomcat) {
                tomcat.addContextCustomizers(context -> {
                    Wrapper wrapper = context.createWrapper();
                    wrapper.setName("PingServlet");
                    wrapper.setServletClass(PingServlet.class.getName());
                    wrapper.setLoadOnStartup(1);
                    context.addChild(wrapper);
                    context.addServletMappingDecoded("/ping", "PingServlet");
                });
            }
        };
    }

    public static class PingServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(200);
            resp.setContentType("text/plain");
            resp.getWriter().write("pong");
        }
    }
}
