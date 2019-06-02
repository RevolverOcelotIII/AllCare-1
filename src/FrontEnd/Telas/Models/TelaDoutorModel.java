package FrontEnd.Telas.Models;

import DataBase.Banco_de_Valores;
import FrontEnd.AllCareApplication;
import allcare.Doenca;
import java.util.ArrayList;

public class TelaDoutorModel {
    
    private static ArrayList<Doenca> todasDoencas;
    
    public TelaDoutorModel(){
        todasDoencas = new ArrayList<Doenca>();
    }
    
    public void atualizar(){
        todasDoencas = Banco_de_Valores.getAllDoencas(AllCareApplication.getCON().getDeclaracao_de_comandos(), AllCareApplication.getCON().getResult_consultas());
    }
    
    public ArrayList<String> getItems(){
        ArrayList<String> doencasNome = new ArrayList<>();
        for(Doenca doencax : todasDoencas){
            doencasNome.add(doencax.getNome());
        }
        return doencasNome;
    }
    
}
