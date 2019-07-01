package com.justinyaodu.rgbcube.ui;

import com.justinyaodu.rgbcube.cube.*;
import com.justinyaodu.rgbcube.environment.Environment;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class GUI extends Application
{
	private RgbCube rgbCube;
	private Environment environment;

	@Override
	public void start(Stage stage)
	{
		stage.setTitle("Interactive RGB Cube");
		stage.setScene(new Scene(buildRoot()));
		stage.sizeToScene();
		stage.show();
	}

	private Parent buildRoot()
	{
		return new BorderPane(buildSubscene(), null, null, null, buildControls());
	}

	private Parent buildSubscene()
	{
		environment = new Environment();

		Group subRoot = new Group();
		SubScene subScene = new SubScene(subRoot, 0, 0, true, SceneAntialiasing.DISABLED);

		// fill isn't a property, so need to initialize and use a listener
		subScene.setFill(environment.backgroundColorProperty().getValue());
		environment.backgroundColorProperty().addListener((o, oldColor, newColor) -> subScene.setFill(newColor));

		rgbCube = new RgbCube();
		Camera camera = buildCamera(subScene);

		AmbientLight light = new AmbientLight();
		light.colorProperty().bindBidirectional(environment.lightColorProperty());

		subRoot.getChildren().addAll(rgbCube, camera, light);

		// allow scrolling and panning with mouse
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setPannable(true);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

		// set width to match cube size if cube is too large to fit; otherwise, fill ScrollPane
		subScene.widthProperty().bind(Bindings.max(rgbCube.maxSizeProperty().multiply(1.1), scrollPane.widthProperty().subtract(2)));
		subScene.heightProperty().bind(Bindings.max(rgbCube.maxSizeProperty().multiply(1.1), scrollPane.heightProperty().subtract(2)));

		scrollPane.setContent(subScene);

		return scrollPane;
	}

	private Camera buildCamera(SubScene subScene)
	{
		ParallelCamera camera = new ParallelCamera();
		camera.setNearClip(1);
		camera.setFarClip(1000);

		Translate cameraTranslate = new Translate();
		cameraTranslate.xProperty().bind(subScene.widthProperty().divide(-2));
		cameraTranslate.yProperty().bind(subScene.heightProperty().divide(-2));
		cameraTranslate.zProperty().bind(rgbCube.maxExtensionZProperty().add(1).multiply(-1));
		camera.getTransforms().add(cameraTranslate);

		subScene.setCamera(camera);

		return camera;
	}

	private Parent buildControls()
	{
		CubeMasker cubeMasker = new CubeMasker(rgbCube);

		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		tabPane.getTabs().add(new Tab("Main", new VBox(new CubeViewUI(rgbCube), new CubeSnapperUI(rgbCube), new QuickCutUI(cubeMasker))));
		tabPane.getTabs().add(new Tab("More Cuts", new VBox(new FaceCutUI(cubeMasker), new CornerCutUI(cubeMasker))));
		tabPane.getTabs().add(new Tab("Display Options", new VBox(new CubeOptionsUI(rgbCube), new EnvironmentOptionsUI(environment))));
		tabPane.getTabs().add(new Tab("Help/About", new VBox(new Help(), new About())));

		return tabPane;
	}
}