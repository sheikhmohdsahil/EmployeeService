package com.PWS.EmployeeService;
import com.PWS.EmployeeService.Utility.AuditAwareImpl;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.PWS.EmployeeService.*"})
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}
	@Bean
	public AuditorAware<String> auditorAware(){return new AuditAwareImpl();
	}

}
