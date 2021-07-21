package com.solbeg.demo.grpc.chat;


import com.demo.grpc.ChatMessage;
import com.demo.grpc.ChatMessageFromServer;
import com.demo.grpc.ChatServiceGrpc;
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
    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    private final ObservableList<String> messages = FXCollections.observableArrayList();
    private final ListView<String> messagesView = new ListView<>();
    private final TextField name = new TextField("name");
    private final TextField message = new TextField();
    private final Button send = new Button();

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

        //
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(HOST, PORT)
                .usePlaintext()
                .build();

        ChatServiceGrpc.ChatServiceStub asyncStub = ChatServiceGrpc.newStub(channel);

        StreamObserver<ChatMessage> chat = asyncStub.chat(getClientStreamObserver());

        send.setOnAction(actionEvent -> {
            ChatMessage msg = ChatMessage.newBuilder().setFrom(name.getText()).setMessage(message.getText()).build();
            chat.onNext(msg);
            // message.setText("");
        });

        primaryStage.setOnCloseRequest(e -> {
            //exit message
            ChatMessage livingMSG = ChatMessage.newBuilder()
                    .setFrom(name.getText())
                    .setMessage(name.getText() + " is leaving chat...")
                    .build();

            chat.onNext(livingMSG);
            chat.onCompleted();
            channel.shutdown();
        });
    }

    private StreamObserver<ChatMessageFromServer> getClientStreamObserver() {
        return new StreamObserver<>() {
            @Override
            public void onNext(ChatMessageFromServer value) {
                Platform.runLater(() -> {
                    messages.add(value.getChatMessage().getFrom() + " : " + value.getChatMessage().getMessage());
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
        };
    }
}
