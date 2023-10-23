package com.example.cmpt_365_project1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;

import javafx.stage.Stage;

import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.text.*;
import javafx.scene.chart.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import java.io.*;
import java.nio.ByteBuffer;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    // stage -> scene -> scene-graph
    @Override
    public void start(Stage stage) throws IOException {
        double width = 1200;
        double height = 800;
        stage.setTitle("Waveform plotter");
        FileChooser filechooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Wav Files", "*.wav");
        filechooser.getExtensionFilters().add(filter);
        Button button = new Button("Open File");
        button.setLayoutX(500);
        button.setLayoutY(700);
        Group root = new Group(button);
        Group wavData = new Group();
        root.getChildren().add(wavData);
        Text numSamplesText = new Text("# of Samples: " );
        numSamplesText.setX(width/2);
        numSamplesText.setY(700);

        Text sampleFreqText = new Text("# of Samples per second: ");
        sampleFreqText.setX(width/2);
        sampleFreqText.setY(720);
        wavData.getChildren().add(numSamplesText);
        wavData.getChildren().add(sampleFreqText);
        Scene scene = new Scene(root, width, height);


        button.setOnAction(
                actionEvent -> {
                    File selectedFile = filechooser.showOpenDialog(stage);

                    wavData.getChildren().clear();
                    try {
                        AudioInputStream audioInputStream =
                                AudioSystem.getAudioInputStream(selectedFile);
                        AudioFormat audioFormat = audioInputStream.getFormat();

                        int bytesPerFrame =
                                audioInputStream.getFormat().getFrameSize();

                        int numFrames = (int) audioInputStream.getFrameLength();
                        float sampleFreq = audioFormat.getSampleRate();
                        int numBytes = numFrames * bytesPerFrame;


                        numSamplesText.setText("# of Samples: " + 2*numFrames);
                        sampleFreqText.setText("# of Samples per second: " + sampleFreq);

                        wavData.getChildren().add(numSamplesText);
                        wavData.getChildren().add(sampleFreqText);

//                        System.out.println(audioInputStream.getFrameLength());
                        byte[] audioBytes = new byte[numBytes];
                        try {
                            while ((audioInputStream.read(audioBytes)) != -1) {

                            }
                            int bytesLength = audioBytes.length;
                            short[] channel1 = new short[bytesLength/4];
                            short[] channel2 = new short[bytesLength/4];

                            ByteBuffer buffer = ByteBuffer.wrap(audioBytes);
                            if (audioFormat.isBigEndian()){
                                buffer.order(ByteOrder.BIG_ENDIAN);
                            }
                            else{
                                buffer.order(ByteOrder.LITTLE_ENDIAN);
                            }
                            for(int i = 0; i < bytesLength/4; i++){

                                channel1[i] = buffer.getShort();
                                channel2[i] = buffer.getShort();
                            }



//                            System.out.println(audioFormat.getFrameSize());
                            NumberAxis xAx1 = new NumberAxis();
                            NumberAxis yAx1 = new NumberAxis();
                            NumberAxis xAx2 = new NumberAxis();
                            NumberAxis yAx2 = new NumberAxis();

                            xAx1.setTickMarkVisible(false);
                            yAx1.setTickMarkVisible(false);
                            xAx1.setTickLabelsVisible(false);
                            yAx1.setTickLabelsVisible(false);
                            xAx2.setTickMarkVisible(false);
                            yAx2.setTickMarkVisible(false);
                            xAx2.setTickLabelsVisible(false);
                            yAx2.setTickLabelsVisible(false);

                            LineChart channel1Plot = new LineChart(xAx1, yAx1);
                            channel1Plot.setMaxHeight(300);
                            channel1Plot.setPrefWidth(width - 20);
                            LineChart channel2Plot = new LineChart(xAx2, yAx2);
                            channel2Plot.setMaxHeight(300);
                            channel2Plot.setLayoutY(250);
                            channel2Plot.setPrefWidth(width - 20);
                            XYChart.Series series1 = new XYChart.Series();
                            XYChart.Series series2 = new XYChart.Series();

                            List<XYChart.Data<Number, Number>> channel1Data = new ArrayList<>();
                            List<XYChart.Data<Number, Number>> channel2Data = new ArrayList<>();
                            for(int i = 0; i < bytesLength/4; i++){
                                channel1Data.add(new XYChart.Data(i, channel1[i]));
                                channel2Data.add(new XYChart.Data(i, channel2[i]));
//                                series1.getData().add(new XYChart.Data(i, channel1[i]));
//                                series2.getData().add(new XYChart.Data(i, channel2[i]));
                            }

                            series1.getData().addAll(channel1Data);
                            series2.getData().addAll(channel2Data);
                            channel1Plot.getData().add(series1);
                            channel1Plot.setCreateSymbols(false);

                            channel2Plot.getData().add(series2);
                            channel2Plot.setCreateSymbols(false);
                            channel2Plot.setLegendVisible(false);
                            wavData.getChildren().add(channel1Plot);
                            wavData.getChildren().add(channel2Plot);


                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }


//                    System.out.println(selectedFile);
        });



        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}