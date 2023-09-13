package com.epix.hawkadmin;

//import com.epix.hawkadmin.config.WebConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableConfigurationProperties(WebConfigProperties.class)
@EnableElasticsearchRepositories(basePackages = "com.epix.hawkadmin.repository")
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HawkAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(HawkAdminApplication.class, args);
	}


	/*@Bean
	public WebMvcConfigurer corsConfigurer (){

		return new  WebMvcConfigurer(){
			@Override
			public void addCorsMappings(CorsRegistry registry) {

				registry.addMapping("/**")
						.allowedOrigins("http://localhost:4200");


			}
		};
	}*/

}
