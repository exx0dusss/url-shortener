package s28476.tpo11;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.servlet.context-path}")
    private String serverContext;

   @Bean
   public OpenAPI defineOpenApi() {
       Server server = new Server();
       String url = "http://" + serverAddress + ":" + serverPort + serverContext;
       System.out.println("OpenApi on: " + url);
       server.setUrl(url);
       server.setDescription("Development");

       Contact myContact = new Contact();
       myContact.setName("Tymur Mustafaiev");
       myContact.setEmail("s28476@pjwstk.edu.pl");

       Info information = new Info()
               .title("Links API")
               .version("1.0")
               .description("This API exposes endpoints to manage links.")
               .contact(myContact);
       return new OpenAPI().info(information).servers(List.of(server));
   }
}