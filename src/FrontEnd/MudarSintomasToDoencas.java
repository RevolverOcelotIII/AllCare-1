package FrontEnd;

import DataBase.Banco_de_Valores;
import DataBase.ConectionFactory;
import allcare.Sintoma;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MudarSintomasToDoencas {
    
    private static ArrayList<Sintoma> todosSintomas;
    private static ArrayList<Sintoma> sintomas_escolhidos;
    private static ScrollPane scrollSintomas;
    private static Button buttonConcluir;
    private static VBox layoutCheckBoxs;
    
    public static ArrayList<Sintoma> display(ConectionFactory con, String doencaSelecionada, ArrayList<Sintoma> sintomasPre){
        sintomas_escolhidos = new ArrayList<>();
        
        //Todos sintomas
        todosSintomas = new ArrayList<>();
        todosSintomas = Banco_de_Valores.puxa_Sintomas(con.getDeclaracao_de_comandos(), con.getResult_consultas());
        
        //Stage
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        
        //buttonConcluir
        buttonConcluir = new Button("Concluir");
        buttonConcluir.setStyle("-fx-text-fill: #ffffff; -fx-background-color: linear-gradient(#ff00a6, #ffa3de)");
        BorderPane.setAlignment(buttonConcluir, Pos.TOP_RIGHT);
        BorderPane.setMargin(buttonConcluir, new Insets(2, 3, 0, 0));
        buttonConcluir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                eventButtonConcluir(con, window);
            }
        });
        
        //CheckBoxs sintomas + selecionar sintomas antigos
        ArrayList<String> sintomasPreEstabelecidos = new ArrayList<>();
        sintomasPreEstabelecidos = Banco_de_Valores.sintomas_de_doenca_especifica(con.getDeclaracao_de_comandos(), con.getResult_consultas(), doencaSelecionada);
        
        layoutCheckBoxs = new VBox();
        layoutCheckBoxs.setSpacing(3);
        for(Sintoma todos : todosSintomas){
            CheckBox box = new CheckBox(todos.getNome());
            VBox.setMargin(box, new Insets(2, 0, 0, 2));
            if(sintomasPre.isEmpty()){
                for(String sintomaPre : sintomasPreEstabelecidos){
                    if(box.getText().equalsIgnoreCase(sintomaPre)){
                        box.setSelected(true);
                    }
                }
            }else{
                for(Sintoma sintomaPreX : sintomasPre){
                    if(box.getText().equalsIgnoreCase(sintomaPreX.getNome())){
                        box.setSelected(true);
                    }
                }
            }
            layoutCheckBoxs.getChildren().add(box);
        }
        layoutCheckBoxs.setStyle("-fx-font-size: 15");
        //
        
        //Scroll sintomas
        scrollSintomas = new ScrollPane(layoutCheckBoxs);
        scrollSintomas.setPrefWidth(200);
        scrollSintomas.setStyle("-fx-font-size: 15");
        scrollSintomas.setFocusTraversable(false);
        BorderPane.setAlignment(scrollSintomas, Pos.CENTER_LEFT);
        BorderPane.setMargin(scrollSintomas, new Insets(2, 0, 2, 3));
        
        //Layout main
        BorderPane layoutMain = new BorderPane();
        layoutMain.setStyle("-fx-focus-color: transparent");
        layoutMain.setLeft(scrollSintomas);
        layoutMain.setRight(buttonConcluir);
        layoutMain.setPrefHeight(200);
        
        //Scene
        Scene cena = new Scene(layoutMain);
        cena.getStylesheets().addAll(MudarSintomasToDoencas.class.getResource("StyleCheckBox.css").toExternalForm());
        
        cena.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    eventButtonConcluir(con, window);
                }
            }
        });
        
        //Salvar sintomas selecionados se o usuário clicar no X
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                sintomas_escolhidos = sintomasPre;
            }
        });
        
        //Stage
        window.getIcons().add(new Image(AddSintomasToDoencas.class.getResourceAsStream("SickChan.png")));
        window.setTitle("Sintomas");
        window.setScene(cena);
        window.setResizable(false);
        window.showAndWait();
        
        return sintomas_escolhidos;
    }
    
    public static void eventButtonConcluir(ConectionFactory con, Stage window){
        ArrayList<CheckBox> boxs = new ArrayList<>();
        for(int i = 0; i < layoutCheckBoxs.getChildren().size(); i++){
            boxs.add((CheckBox) layoutCheckBoxs.getChildren().get(i));
        }
        
        boolean temSelecionado = false;
        
        for(CheckBox boxx : boxs){
            if(boxx.isSelected()){
                temSelecionado = true;
            }
        }
        
        if(temSelecionado){
            ArrayList <String> sintomas = new ArrayList<>();
            for(CheckBox box : boxs){
                if(box.isSelected()){
                    sintomas.add(box.getText());
                }
            }

            for(String nome : sintomas){
                Sintoma sintoma = new Sintoma();
                sintoma.setNome(nome);

                sintomas_escolhidos.add(sintoma);
            }
            window.close();
        }else{
            NovoJOptionPane.display("Você não selecionou nenhum sintoma", false);
        }
    }
    
}
