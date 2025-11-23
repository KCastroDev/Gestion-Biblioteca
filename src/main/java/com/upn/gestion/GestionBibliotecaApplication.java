package com.upn.gestion;

import javafx.application.Application; // <--- IMPORTANTE
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionBibliotecaApplication extends Application { // <--- DEBE DECIR 'extends Application'

    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        launch(args); // <--- DEBE DECIR 'launch(args)', NO 'SpringApplication.run'
    }

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(GestionBibliotecaApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        System.out.println("✅ JavaFX INICIADO CORRECTAMENTE"); // <--- Agrega esto para depurar
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    public class StageReadyEvent extends ApplicationEvent {
        public Stage getStage() { return (Stage) getSource(); }
        public StageReadyEvent(Stage source) { super(source); }
    }
}