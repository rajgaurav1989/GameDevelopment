package com.raju;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    private final LightBase lightBase = new PointLight();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Box box = getBox();
        SmartGroup rootGroup = new SmartGroup();
        rootGroup.getChildren().add(box);
        rootGroup.getChildren().addAll(prepareLightSource());

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
                    break;
                case Z:
                    rootGroup.rotateByZ(5);
                    break;
                case X:
                    rootGroup.rotateByZ(-5);
            }
        });

        primaryStage.setTitle("Raj Gaurav 3D Box Rotation By Mouse");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                lightBase.setRotate(lightBase.getRotate() + 1);
            }
        };
        timer.start();
    }

    private Node[] prepareLightSource() {
        lightBase.setColor(Color.RED);
        lightBase.getTransforms().add(new Translate(0, -50, 100));
        lightBase.setRotationAxis(Rotate.X_AXIS);

        Sphere sphere = new Sphere(2);
        sphere.getTransforms().addAll(lightBase.getTransforms());
        sphere.rotateProperty().bind(lightBase.rotateProperty());
        sphere.rotationAxisProperty().bind(lightBase.rotationAxisProperty());

        return new Node[]{lightBase, sphere};
    }

    private Box getBox() throws FileNotFoundException {
        PhongMaterial material = new PhongMaterial();
        File textureFile = new File("src/resources/wood.jpg");
        File specularFile = new File("src/resources/illuminati2.jpg");
        Image image = new Image(new FileInputStream(textureFile));
        material.setDiffuseMap(image);
        material.setSpecularMap(new Image(new FileInputStream(specularFile)));
        Box box = new Box(100, 20, 50);
        box.setMaterial(material);
        return box;
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

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double deltaY = event.getDeltaY();
            rootGroup.translateZProperty().set(rootGroup.getTranslateZ() + deltaY);
        });

    }


    class SmartGroup extends Group {
        Transform groupTransform = new Rotate();

        int angleZ = 0;

        void rotateByX(int angle) {
            angleX.set(angleX.get() + angle);
        }

        void rotateByY(int angle) {
            angleY.set(angleY.get() + angle);
        }

        void reset() {
            angleX.set(0);
            angleY.set(0);
            if (this.getTransforms().size() == 3)
                this.getTransforms().remove(this.getTransforms().size() - 1);
            Rotate rotate = new Rotate(-angleZ, new Point3D(0, 0, 1));
            groupTransform = groupTransform.createConcatenation(rotate);
            this.getTransforms().addAll(groupTransform);
            angleZ = 0;
        }

        public void rotateByZ(int angle) {
            Rotate rotate = new Rotate(angle, new Point3D(0, 0, 1));
            angleZ += angle;
            if (this.getTransforms().size() == 3)
                this.getTransforms().remove(this.getTransforms().size() - 1);
            groupTransform = groupTransform.createConcatenation(rotate);
            this.getTransforms().addAll(groupTransform);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
