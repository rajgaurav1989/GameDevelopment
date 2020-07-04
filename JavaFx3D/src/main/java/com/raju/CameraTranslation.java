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

public class CameraTranslation extends Application {
    public static final int Z_MOVEMENT = 100;
    private static final int RADIUS = 50;
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Sphere sphere = new Sphere(RADIUS);
        Group rootGroup = new Group();
        rootGroup.getChildren().add(sphere);

        Camera camera = new PerspectiveCamera(true);
        Scene scene = new Scene(rootGroup, WIDTH, HEIGHT);
        scene.setFill(Color.SNOW);
        scene.setCamera(camera);

        camera.translateXProperty().set(0);
        camera.translateYProperty().set(0);
        camera.translateZProperty().set(-500);

        camera.setNearClip(1);
        camera.setFarClip(1000);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    camera.translateZProperty().set(camera.getTranslateZ() + Z_MOVEMENT);
                    break;
                case DOWN:
                    camera.translateZProperty().set(camera.getTranslateZ() - Z_MOVEMENT);
            }
        });

        primaryStage.setTitle("Raj Gaurav 3D Sphere chapter 2");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
