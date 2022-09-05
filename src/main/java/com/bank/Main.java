package com.bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    private static Stage fakeStage;
    public static final float LOGIN_FORM_WIDTH = 600;
    public static final float LOGIN_FORM_HEIGHT = 400;

    public static final float DASHBOARD_WIDTH = 720;
    public static final float DASHBOARD_HEIGHT = 600;

    public static final float EMPLOYEE_DASHBOARD_WIDTH = 900;
    public static final float EMPLOYEE_DASHBOARD_HEIGHT = 600;


    public static Accounts accounts;

    @Override
    public void stop() throws Exception {
        super.stop();

        // saving account properties after program ends
        accounts.serializeAccounts();
    }

    @Override
    public void start(Stage stage) throws IOException {


        accounts = new Accounts();
        loadAccounts();

        fakeStage = stage;
        stage.setResizable(false);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("UserLogForm.fxml")));
        Scene scene = new Scene(root, LOGIN_FORM_WIDTH, LOGIN_FORM_HEIGHT);

        stage.setTitle("Bank Application");
        stage.setScene(scene);
        stage.show();
    }


    public void changeScene(String fxml, double width, double height) throws IOException{
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        fakeStage.getScene().setRoot(parent);
        fakeStage.setWidth(width + 14.4);
        fakeStage.setHeight(height + 37.6);
        fakeStage.centerOnScreen();
    }

    public <T, S>void changeSceneAndInitiateController(String fxml, double width, double height,
                                  InjectObjectToController<T, S> inject, S object) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

        fakeStage.setScene(new Scene(loader.load()));
        T controller = loader.getController();
        inject.injectObjectToController(controller, object);
        fakeStage.setWidth(width + 14.4);
        fakeStage.setHeight(height + 37.6);
        fakeStage.centerOnScreen();
    }

    //public void changeSceneAndInitiateController(String fxml, double width, double height, initateMethod )


    private void loadAccounts(){
        accounts.deserializeAccounts();
    }


    public static Stage getFakeStage() {
        return fakeStage;
    }

    public static void main(String[] args) {
        launch();
    }


}