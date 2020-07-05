package com.raju;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EarthRotation extends Application {
    public static final int WIDTH = 1400;
    public static final int HEIGHT = 800;

    private double anchorX, anchorY;
    private double angleAnchorX = 0;
    private double angleAnchorY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final Sphere sphere = new Sphere(150);

    @Override
    public void start(Stage primaryStage) throws Exception {

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(15000);
        camera.translateZProperty().set(-1200);

        SmartGroup world = new SmartGroup();
        world.getChildren().add(prepareEarth());

        Group root = new Group();
        root.getChildren().add(world);
        root.getChildren().add(prepareImageView());

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SNOW);
        scene.setCamera(camera);

        initMouseControl(world, scene);

        primaryStage.setTitle("Raj Gaurav 3D Box Rotation By Mouse");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation();

    }

    private void prepareAnimation() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.rotateProperty().set(sphere.getRotate() + 0.2);
            }
        };
        animationTimer.start();
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

    private ImageView prepareImageView() throws FileNotFoundException {
        File galaxyFile = new File("src/resources/galaxy.jpg");
        Image image = new Image(new FileInputStream(galaxyFile));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 800));
        return imageView;
    }


    private Node prepareEarth() throws FileNotFoundException {
        PhongMaterial earthMaterial = new PhongMaterial();
        File textureFile = new File("src/resources/earth/earth-d.jpg");
        File illuminationFile = new File("src/resources/earth/earth-l.jpg");
        File specularFile = new File("src/resources/earth/earth-s.jpg");
        File bumpFile = new File("src/resources/earth/earth-n.jpg");
        earthMaterial.setDiffuseMap(new Image(new FileInputStream(textureFile)));
        earthMaterial.setSelfIlluminationMap(new Image(new FileInputStream(illuminationFile)));
        earthMaterial.setSpecularMap(new Image(new FileInputStream(specularFile)));
        earthMaterial.setBumpMap(new Image(new FileInputStream(bumpFile)));
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

    public static void main(String[] args) {
        launch(args);
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
            Rotate r = new Rotate(ang, new Point3D(0, 1, 0));
            groupTransform = groupTransform.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(groupTransform);
        }

    }
}
