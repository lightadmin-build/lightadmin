package org.lightadmin.boot;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.lightadmin.api.config.LightAdmin;
import org.lightadmin.core.config.LightAdminWebApplicationInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class LightAdminBootApplication extends SpringBootServletInitializer {

    /* Please uncomment for deploying as a web module to servlet container */
    /**
     * public void onStartup(ServletContext servletContext) throws ServletException {
     * LightAdmin.configure(servletContext)
     * .basePackage("org.lightadmin.boot.administration")
     * .baseUrl("/admin")
     * .security(false)
     * .backToSiteUrl("http://lightadmin.org");
     * super.onStartup(servletContext);
     * }
     */

    /* Used for running in "embedded" mode */
    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                LightAdmin.configure(servletContext)
                        .basePackage("org.lightadmin.boot.administration")
                        .baseUrl("/admin")
                        .security(true)
                        .backToSiteUrl("http://lightadmin.org");

                new LightAdminWebApplicationInitializer().onStartup(servletContext);
            }
        };
    }
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/lightadmin?useUnicode=true&connectionCollation=utf8_general_ci&characterSetResults=utf8&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        return dataSource;
    }
	/* https://github.com/spring-projects/spring-boot/issues/2825#issuecomment-93479758 */
    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    customizeTomcat((TomcatEmbeddedServletContainerFactory)container); 
                }
            }

            private void customizeTomcat(TomcatEmbeddedServletContainerFactory tomcatFactory) {
                tomcatFactory.addContextCustomizers(new TomcatContextCustomizer() {

                    @Override
                    public void customize(Context context) {
                        Container jsp = context.findChild("jsp");
                        if (jsp instanceof Wrapper) {
                            ((Wrapper)jsp).addInitParameter("development", "false");
                        }

                    }

                });
            }

        };
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(LightAdminBootApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LightAdminBootApplication.class);
    }
}