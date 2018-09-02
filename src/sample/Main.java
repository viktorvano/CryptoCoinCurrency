package sample;

import com.sun.org.glassfish.external.statistics.Stats;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.event.ActionEvent;

import javax.lang.model.util.Elements;
import java.awt.*;
import java.io.IOException;

import static java.lang.Thread.*;

public class Main extends Application {

    private final String currencyUrl = "https://walletinvestor.com/converter/bitcoin-gold-futures/eur/1";
    private final String element = "converter-title-amount\">";
    private Button btnOK;
    private TextField txtAmount;
    private TextField txtValue;
    private Label lblAmount;
    private Label lblValue;
    private Label lblInvestmentValue;
    private Label lblCryptoCurrency;
    private int flag=0;
    private float moneyOLD=0.0f;
    private float moneyNEW=0.0f;
    private float currencyNEW=0.0f;
    private float currencyOLD=0.0f;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Crypto Coin Currency");

        //create and set button OK
        btnOK = new Button();
        btnOK.setFont(Font.font("Arial", 24));
        btnOK.setText("OK");
        btnOK.setLayoutX(260);
        btnOK.setLayoutY(200);
        btnOK.setPrefSize(75,75);
        btnOK.setOnAction(event -> getDataInputs());

        //create and set TextFields
        txtAmount = new TextField();
        txtAmount.setPromptText("Enter investment.");
        txtAmount.setPrefWidth(200);
        txtAmount.setLayoutX(50);
        txtAmount.setLayoutY(200);
        txtValue = new TextField();
        txtValue.setPromptText("Enter crypto coin exchange value.");
        txtValue.setPrefWidth(200);
        txtValue.setLayoutX(50);
        txtValue.setLayoutY(250);

        //create and set Labels
        lblAmount = new Label();
        lblAmount.setText("Amount of invested money");
        lblAmount.setLayoutX(50);
        lblAmount.setLayoutY(183);
        lblValue = new Label();
        lblValue.setText("Value of crypto coin at investment");
        lblValue.setLayoutX(50);
        lblValue.setLayoutY(233);
        lblCryptoCurrency = new Label();
        lblCryptoCurrency.setFont(Font.font("Arial", 32));
        lblCryptoCurrency.setText("BTG value: 0.0 €");
        lblCryptoCurrency.setLayoutX(50);
        lblCryptoCurrency.setLayoutY(40);
        lblInvestmentValue = new Label();
        lblInvestmentValue.setFont(Font.font("Arial", 32));
        lblInvestmentValue.setText("Investment value: 0.0 €");
        lblInvestmentValue.setLayoutX(50);
        lblInvestmentValue.setLayoutY(90);

        //create layout and add objects
        Pane layout = new Pane();
        layout.getChildren().add(btnOK);
        layout.getChildren().add(txtAmount);
        layout.getChildren().add(txtValue);
        layout.getChildren().add(lblAmount);
        layout.getChildren().add(lblValue);
        layout.getChildren().add(lblCryptoCurrency);
        layout.getChildren().add(lblInvestmentValue);

        Scene scene = new Scene(layout, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame[]{new KeyFrame(Duration.seconds(30), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("this is called every 30 seconds on UI thread");
                runProgram();
                primaryStage.show();
            }
        })});
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();


    }

    private void calcMoney()
    {
        float ratio;
        ratio = currencyNEW/currencyOLD;
        moneyNEW = ratio*moneyOLD;
        updateData();
    }

    private void updateData()
    {
        String strNumber;
        strNumber = Float.toString(currencyNEW);
        lblCryptoCurrency.setText("BTG value: " +strNumber+" €");
        strNumber = Float.toString(moneyNEW);
        lblInvestmentValue.setText("Investment value: " +strNumber+ " €");
    }

    public void getDataInputs(){
        flag=1;
        System.out.println("flag has been set to 1.");
        getData(currencyUrl);
        String strMoney = txtAmount.getText().toString();
        try
        {
            moneyOLD = Float.parseFloat(strMoney);
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
            txtAmount.setText("");
            txtAmount.setPromptText("Enter valid amount.");
            moneyOLD = 1.0f;
            flag = 0;
            System.out.println("flag has been set to 0.");
        }
        strMoney = txtValue.getText().toString();
        try
        {
            currencyOLD = Float.parseFloat(strMoney);
        }catch (NumberFormatException e)
        {
            e.printStackTrace();
            txtValue.setText("");
            txtValue.setPromptText("Enter valid currency.");
            currencyOLD = 1.0f;
            flag = 0;
            System.out.println("flag has been set to 0.");
        }
        calcMoney();
    }


    public static void main(String[] args)
    {
        launch(args);
    }

    public void runProgram(){
        if(flag==1)
        {
            getData(currencyUrl);
            System.out.println(currencyNEW);
            calcMoney();
            updateData();
        }
    }

    private void getData(String url)
    {
        try
        {
            Document document = Jsoup.connect(url).followRedirects(false).timeout(60000).get();
            document.outputSettings().prettyPrint(false);
            String result = document.toString();
            String[] ss= result.split(element);
            String[] ss2= ss[1].split("</span> Eurozone Euro ");
            float euro = Float.parseFloat(ss2[0]);
            currencyNEW = euro;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
