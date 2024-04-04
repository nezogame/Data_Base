package org.denys.hudymov;

import org.denys.hudymov.gui.AppWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class App {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .headless(false)
                .sources(App.class)
                .run(args);
    }


}