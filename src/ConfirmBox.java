import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by karan on 29/6/17.
 */
public class ConfirmBox{
    static boolean answer;
    public static boolean display(int moves){
        Stage window = new Stage();
        window.setTitle("Play Again or Quit?");
        window.initModality(Modality.APPLICATION_MODAL);
        Label label = new Label("Game Over. You took "+moves+" moves.\nWanna Play Again?");

        Button yes = new Button("Yes");
        Button no = new Button("Quit");

        yes.setOnAction(e->{
            answer=true;
            window.close();
        });
        no.setOnAction(e->{
            answer=false;
            window.close();
        });
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(yes,no);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,buttonLayout);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,250,100);
        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
}
