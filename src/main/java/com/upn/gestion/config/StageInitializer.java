package com.upn.gestion.config;

import com.upn.gestion.GestionBibliotecaApplication.StageReadyEvent; // Importamos el evento de tu Main
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component //  Esto le dice a Spring que "Cárgame al iniciar"
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Value("classpath:/fxml/login.fxml")
    private Resource mainFxml;

    private final ApplicationContext applicationContext;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            // Cargamos el archivo visual FXML
            FXMLLoader fxmlLoader = new FXMLLoader(mainFxml.getURL());

            // Le decimos a JavaFX que Use Spring para crear los controladores
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            //Creamos la ventana.
            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 800, 600)); // Ancho x Alto
            stage.setTitle("Sistema de Gestión de Biblioteca - UPN");

            // ABRIMOS LA VENTANA
            stage.show();

            System.out.println("VENTANA ABIERTA EXITOSAMENTE");

        } catch (IOException e) {
            System.err.println(" ERROR  AL ABRIR LA VENTANA:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}