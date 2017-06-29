import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.sql.Time;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by karan on 28/6/17.
 */
public class Main extends Application {

    Stage window;
    int selX=-1,selY=-1;
    int grid[][];
    ImageView imageView[][];
    TreeMap<Integer,Pair<Integer,Integer>> M;
    final static int n = 6;
    int count = (n*n)/2,moves=0;
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        create();
        window.setTitle("Match the tiles");
        Image ico = new Image("icon.png",64,64,true,true);
        window.getIcons().add(ico);
        Scene scene = createScene();
        window.setScene(scene);
        window.show();
    }

    private Scene createScene() {
        M = new TreeMap<>();
        VBox layout = new VBox(10);
        imageView = new ImageView[n][n];
        layout.setAlignment(Pos.CENTER);
        for(int i=0;i<n;++i){
            HBox tempLayout = new HBox(10);
            tempLayout.setAlignment(Pos.CENTER);
            for(int j=0;j<n;++j){
                imageView[i][j] = new ImageView(new Image("grey.png",64,64,true,true));
                M.put(imageView[i][j].hashCode(),new Pair<>(i,j));
                imageView[i][j].setOnMouseClicked(e->{
                    load((ImageView)e.getSource());
                });
                tempLayout.getChildren().add(imageView[i][j]);
            }
            layout.getChildren().add(tempLayout);
        }
        BackgroundImage bgImg = new BackgroundImage(new Image("background.jpg",500,500,false,true),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        layout.setBackground(new Background(bgImg));
        Scene scene = new Scene(layout,500,500);
        return scene;
    }

    public void shuffle(Random rnd) {

        for (int i = n*n; i > 1; i--)
            swap(i - 1, rnd.nextInt(i));
    }

    public void swap(int i, int j) {
        int tmp = grid[i / n][i % n];
        grid[i / n][i % n] = grid[j / n][j % n];
        grid[j / n][j % n] = tmp;
    }
    private void create(){
        grid = new int[n][n];
        int x=1,no = (n*n)/2;
        for(int i=0;i<n;++i)
            for(int j=0;j<n;++j,++x) {
                grid[i][j] = x;
                if(grid[i][j]>no)
                    grid[i][j] = grid[i][j]%no+1;
            }
        shuffle(new Random());
    }
    private void load(ImageView imgView){
        int x = M.get(imgView.hashCode()).getKey(),y = M.get(imgView.hashCode()).getValue();
        if(x==selX && y==selY){
            return;
        }
        moves++;
        Image img1 = new Image("grey.png",64,64,true,true);
        Image img2 = new Image("white.png",64,64,true,true);
        Image img3 =new Image("smiley"+grid[x][y]+".png",64,64,true,true);
        imageView[x][y].setImage(img3);
        if(selX!=-1 && grid[selX][selY]==grid[x][y])
        {

            Timeline tl = createTimeline(imageView[x][y],img3,img2,250);
            Timeline tl1 = createTimeline(imageView[selX][selY],img3,img2,250);
            ParallelTransition pt = new ParallelTransition(tl,tl1);
            pt.play();
            imageView[x][y].setOnMouseClicked(null);
            imageView[selX][selY].setOnMouseClicked(null);
            selX=-1;
            selY=-1;
            count-=1;
            if(count==0){
                boolean ret = ConfirmBox.display(moves/2);
                if(ret){
                    count = (n*n)/2;
                    reload();
                }
                else
                    window.close();
            }
        }
        else if(selX!=-1){
            Timeline tl = createTimeline(imageView[x][y],img3,img1,500);
            Timeline tl1 = createTimeline(imageView[selX][selY],imageView[selX][selY].getImage(),img1,500);
            ParallelTransition pt = new ParallelTransition(tl,tl1);
            pt.play();
            imageView[x][y].setImage(img1);
            imageView[selX][selY].setImage(img1);
            selX=-1;
            selY=-1;
        }
        else{
            Timeline tl = createTimeline(imageView[x][y],img1,img3,250);
            tl.play();
            selX=x;
            selY=y;
        }
    }
    private Timeline createTimeline(ImageView imgView,Image X,Image Y,int dur){

        Timeline tl = new Timeline();
        KeyFrame kf1 = new KeyFrame(Duration.ZERO,new KeyValue(imgView.imageProperty(),X));
        KeyFrame kf2 = new KeyFrame(Duration.millis(dur),new KeyValue(imgView.imageProperty(),Y));
        tl.getKeyFrames().addAll(kf1,kf2);
        tl.setCycleCount(1);
        return tl;
    }
    private void reload(){
        create();
        Image img1 = new Image("grey.png",64,64,true,true);
        for(int i=0;i<n;++i)
            for(int j=0;j<n;++j){
                imageView[i][j].setImage(img1);
                imageView[i][j].setOnMouseClicked(e->{
                    load((ImageView)e.getSource());
                });
            }
    }
}
