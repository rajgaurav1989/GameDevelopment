package com.raju;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class App extends Application {
    public static final int Z_MOVEMENT = 100;
    private static final int RADIUS = 50;
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 800;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Sphere sphere = new Sphere(RADIUS);
        Group rootGroup = new Group();
        rootGroup.getChildren().add(sphere);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(rootGroup, WIDTH, HEIGHT);
        scene.setFill(Color.SNOW);
        scene.setCamera(camera);

        sphere.translateXProperty().set(WIDTH / 2.0);
        sphere.translateYProperty().set(HEIGHT / 2.0);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    sphere.translateZProperty().set(sphere.getTranslateZ() + Z_MOVEMENT);
                    break;
                case DOWN:
                    sphere.translateZProperty().set(sphere.getTranslateZ() - Z_MOVEMENT);
            }
        });

        primaryStage.setTitle("Raj Gaurav 3D Spher with help of Genuine code");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
