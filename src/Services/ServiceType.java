/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.processing.Result;
import com.codename1.ui.events.ActionListener;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Reclamation;
import model.Reponse;
import model.Type;
import model.User;
import util.Statics;


/**
 *
 * @author Asma Laaribi
 */
public class ServiceType {

    //indiquer le préfixe de l'URL de service
    private String typePrefix = "/type";

    //une variable statique pour stocker une instance unique de la classe
    static ServiceType instance;
    //un booléen pour indiquer le succès de l'opération
    boolean resultOK = false;
    //une variable pour stocker une requête de connexion
    ConnectionRequest req;
    //une liste d'objets
    ArrayList<Type> types = new ArrayList<>();

    //constructor
    private ServiceType() {
        req = new ConnectionRequest();
    }
 
    //crée une instance unique de la classe à l'aide du modèle singleton.
    public static ServiceType getInstance() {
        if (instance == null) {
            instance = new ServiceType();
        }
        return instance;
    }

    //Ajout
 
    // récupérer les types à partir du service web
    public ArrayList<Type> fetchType() {

        //URL
        String selectURL = Statics.BASE_URL + typePrefix + "/ApilistTypes/all";

        req.setUrl(selectURL);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                String response = new String(req.getResponseData());
                try {
                    
                    types = parseTypes(response);
                } catch (ParseException ex) {
                }
                req.removeResponseListener(this);

            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return types;
    }
 
    //analyse la réponse du serveur et renvoie une liste d'objets
    public ArrayList<Type> parseTypes(String jsonText) throws ParseException {

        types = new ArrayList<>();
        JSONParser jp = new JSONParser();

        try {
            //2
            Map<String, Object> jsonList = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) jsonList.get("root");

            for (Map<String, Object> item : list) {
                Type r = new Type();
                r.setId((int) Double.parseDouble(item.get("id").toString()));
                r.setNomType((String) item.get("NomType"));
                System.err.println(r);
                types.add(r);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return types;
    }
    
    //ajouter un nouveau type au service web
     public boolean addType(Type type) {
         
        //build URL
        String addURL = Statics.BASE_URL + typePrefix
                + "/addTypeJSON/new?nom_type=" + type.getNom_type()
               ;
        req.setUrl(addURL);

        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200;
                req.removeResponseListener(this);
            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);

        return resultOK;
    }

}
