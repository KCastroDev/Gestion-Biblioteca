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

@Component //  Esto le dice a Spring "Cárgame al iniciar"
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    // Aquí indicamos dónde está tu archivo visual.
    // Asegúrate de que creaste la carpeta 'fxml' dentro de 'resources'
    @Value("classpath:/fxml/login.fxml")
    private Resource mainFxml;

    private final ApplicationContext applicationContext;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            // 1. Cargamos el archivo visual (.fxml)
            FXMLLoader fxmlLoader = new FXMLLoader(mainFxml.getURL());

            // 2. Le decimos a JavaFX: "Usa Spring para crear los controladores"
            // (Esto permite usar @Autowired en tus Controllers)
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            // 3. Creamos la escena (la ventana)
            Parent parent = fxmlLoader.load();
            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 800, 600)); // Ancho x Alto
            stage.setTitle("Sistema de Gestión de Biblioteca - UPN");

            // 4. ¡ABRIMOS LA VENTANA!
            stage.show();

            System.out.println("VENTANA ABIERTA EXITOSAMENTE"); // Mensaje de control

        } catch (IOException e) {
            System.err.println(" ERROR CRÍTICO AL ABRIR LA VENTANA:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}