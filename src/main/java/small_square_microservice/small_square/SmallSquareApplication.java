package small_square_microservice.small_square;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "small_square_microservice.small_square.infrastructure.http.feign")
public class SmallSquareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmallSquareApplication.class, args);
	}

}
