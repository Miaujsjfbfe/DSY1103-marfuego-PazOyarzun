package com.example.ms_reservas.Config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI configurarOpenApi() {
        // Información de contacto
        Contact contacto = new Contact()
                .name("Paz Oyarzún")
                .email("pa.oyarzunp@duocuc.cl")
                .url("https://www.duoc.cl");

        // Licencia del proyecto
        License licencia = new License()
                .name("MIT")
                .url("https://opensource.org/licences/MIT");

        // Información principal de la API
        Info informacionApi = new Info()
                .title("MS Reservas - MarFuego")
                .description("""
                        API para la gestión
                        de Reservas de Mesas
                        """)
                .version("1.0")
                .termsOfService("https://www.duoc.cl")
                .contact(contacto)
                .license(licencia);

        // Documentación externa (GitHub)
        ExternalDocumentation github = new ExternalDocumentation()
                .description("Repositorio oficial proyecto MarFuego en GitHub")
                .url("https://github.com/Miaujsjfbfe/DSY1103-marfuego-PazOyarzun");

        // Configuración OpenAPI
        return new OpenAPI()
                .info(informacionApi)
                .externalDocs(github);
    }
}
