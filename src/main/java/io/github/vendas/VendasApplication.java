package io.github.vendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
//public class VendasApplication{
public class VendasApplication extends SpringBootServletInitializer{
	
//	@Bean
//	public CommandLineRunner commandLineRunner(@Autowired Clientes clientes) {
//		return args -> {
//			Cliente c = new Cliente(null, "joel");
//			clientes.save(c);
//		};
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(VendasApplication.class, args);
	}
	
	
}
