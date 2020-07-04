package com.raju;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class BoxRotation extends Application {
    public static final int Z_MOVEMENT = 100;
    private static final int RADIUS = 50;
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Box box = new Box(100,20,50);
        SmartGroup rootGroup = new SmartGroup();
        rootGroup.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(rootGroup, WIDTH, HEIGHT);
        scene.setFill(Color.SNOW);
        scene.setCamera(camera);

        rootGroup.translateXProperty().set(WIDTH / 2.0);
        rootGroup.translateYProperty().set(HEIGHT / 2.0);
        rootGroup.translateZProperty().set(-1200);


        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    rootGroup.translateZProperty().set(rootGroup.getTranslateZ() + Z_MOVEMENT);
                    break;
                case DOWN:
                    rootGroup.translateZProperty().set(rootGroup.getTranslateZ() - Z_MOVEMENT);
                    break;
                case W:
                    rootGroup.rotateByY(+10);
                    break;
                case S:
                    rootGroup.rotateByY(-10);
                    break;
                case A:
                    rootGroup.rotateByX(-10);
                    break;
                case D:
                    rootGroup.rotateByX(10);
            }
        });

        primaryStage.setTitle("Raj Gaurav 3D Box Rotation");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    class SmartGroup extends Group {
        Transform groupTransform = new Rotate();

        void rotateByX(int ang) {
            Rotate r = new Rotate(ang, Rotate.X_AXIS);
            groupTransform = groupTransform.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(groupTransform);
        }

        void rotateByY(int ang) {
            Rotate r = new Rotate(ang, new Point3D(0,1,0));
            groupTransform = groupTransform.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(groupTransform);
        }

    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
