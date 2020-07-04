package com.raju;


import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

public class RotationWithMouse extends Application {
    public static final int Z_MOVEMENT = 100;
    private static final int RADIUS = 50;
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 800;

    private double anchorX, anchorY;
    private double angleAnchorX = 0;
    private double angleAnchorY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Box box = new Box(100, 20, 50);
        SmartGroup rootGroup = new SmartGroup();
        rootGroup.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(rootGroup, WIDTH, HEIGHT);
        scene.setFill(Color.SNOW);
        scene.setCamera(camera);

        rootGroup.translateXProperty().set(WIDTH / 2.0);
        rootGroup.translateYProperty().set(HEIGHT / 2.0);
        rootGroup.translateZProperty().set(-1200);

        initMouseControl(rootGroup, scene);

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
                    break;
                case R:
                    rootGroup.reset();
            }
        });

        primaryStage.setTitle("Raj Gaurav 3D Box Rotation By Mouse");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initMouseControl(SmartGroup rootGroup, Scene scene) {
        Rotate xRotate, yRotate;

        rootGroup.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();

            angleAnchorX = angleX.get();
            angleAnchorY = angleY.get();
        });


        scene.setOnMouseDragged(event -> {
            angleX.set(angleAnchorX - (anchorY - event.getSceneY()));
            angleY.set(angleAnchorY + (anchorX - event.getSceneX()));
        });

    }

    class SmartGroup extends Group {
        Transform groupTransform = new Rotate();

        void rotateByX(int ang) {
            angleX.set(angleX.get() + ang);
        }

        void rotateByY(int ang) {
            angleY.set(angleY.get() + ang);
        }

        void reset() {
            angleX.set(0);
            angleY.set(0);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
