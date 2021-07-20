package com.solbeg.demo.grpc.chat;


import com.demo.grpc.ChatMessage;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClient extends Application {
    private ObservableList<String> messages = FXCollections.observableArrayList();
    private ListView<String> messagesView = new ListView<>();
    private TextField name = new TextField("name");
    private TextField message = new TextField();
    private Button send = new Button();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        messagesView.setItems(messages);

        send.setText("Send");

        BorderPane pane = new BorderPane();
        pane.setLeft(name);
        pane.setCenter(message);
        pane.setRight(send);

        BorderPane root = new BorderPane();
        root.setCenter(messagesView);
        root.setBottom(pane);

        primaryStage.setTitle("gRPC Chat");
        primaryStage.setScene(new Scene(root, 480, 320));

        primaryStage.show();

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        com.demo.grpc.ChatServiceGrpc.ChatServiceStub chatService = com.demo.grpc.ChatServiceGrpc.newStub(channel);

        StreamObserver<com.demo.grpc.ChatMessage> chat = chatService.chat(new StreamObserver<com.demo.grpc.ChatMessageFromServer>() {

            @Override
            public void onNext(com.demo.grpc.ChatMessageFromServer value) {
                Platform.runLater(() -> {
                    messages.add(value.getChatMessage().getFrom() + ": " + value.getChatMessage().getMessage());
                    messagesView.scrollTo(messages.size());
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                System.out.println("Disconnected");
            }

            @Override
            public void onCompleted() {
                System.out.println("Disconnected");
            }
        });

        send.setOnAction(e -> {
            chat.onNext(com.demo.grpc.ChatMessage.newBuilder().setFrom(name.getText()).setMessage(message.getText()).build());
            message.setText("");
        });

        primaryStage.setOnCloseRequest(e -> {
            ChatMessage livingMSG = ChatMessage.newBuilder()
                    .setFrom(name.getText())
                    .setMessage("I am leaving")
                    .build();

            chat.onNext(livingMSG);
            chat.onCompleted();
            channel.shutdown();
        });
    }
}
