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
public class ServiceReclamation {

    //l'URL utilisé pour accéder aux ressources liées aux réclamations
    private String reclamationPrefix = "/reclamation";

    //une variable statique "instance" de type ServiceReclamation qui sera utilisée pour l'implémentation du singleton
    static ServiceReclamation instance;
    boolean resultOK = false;
    ConnectionRequest req;
    ArrayList<Reclamation> reclamations = new ArrayList<>();

    //constructor
    private ServiceReclamation() {
        req = new ConnectionRequest();
    }
    
    
    //le design pattern Singleton pour s'assurer qu'il n'y a qu'une seule instance de cette classe.
    public static ServiceReclamation getInstance() {
        if (instance == null) {
            instance = new ServiceReclamation();
        }
        return instance;
    }

    //Ajout d'une réclamation
    public boolean addReclamation(Reclamation reclamation) {
        //build URL
        String addURL = Statics.BASE_URL + reclamationPrefix
                + "/addReclamationJSON/new?libelle=" + reclamation.getLibelle()
                + "&description=" + reclamation.getDescription()
                + "&photo=" + reclamation.getPhoto()
                + "&type=" + reclamation.getType().getId()
                + "&user_id=" + reclamation.getUser().getIdUser();
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
    //La méthode fetchReclamation() permet de récupérer toutes les réclamations existantes
    public ArrayList<Reclamation> fetchReclamation() {

        //URL
        String selectURL = Statics.BASE_URL + reclamationPrefix + "/reclamation/All";

        req.setUrl(selectURL);
        req.setPost(false);

        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                String response = new String(req.getResponseData());
                try {
                    reclamations = parseReclams(response);
                } catch (ParseException ex) {
                }
                req.removeResponseListener(this);

            }
        });

        NetworkManager.getInstance().addToQueueAndWait(req);
        return reclamations;
    }
    //La méthode parseReclams() est utilisée pour analyser la réponse JSON de la requête de récupération des réclamations.
    //Elle utilise la bibliothèque JSONParser pour convertir le JSON en une structure de données Java. 
    public ArrayList<Reclamation> parseReclams(String jsonText) throws ParseException {

        reclamations = new ArrayList<>();
        JSONParser jp = new JSONParser();

        try {
            
            Map<String, Object> jsonList = jp.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list = (List<Map<String, Object>>) jsonList.get("root");

            for (Map<String, Object> item : list) {
                Result result = Result.fromContent(item);
                Reclamation r = new Reclamation();
                r.setId((int) Double.parseDouble(item.get("id").toString()));
                r.setLibelle((String) item.get("libelle"));
                r.setDescription((String) item.get("description"));
                r.setEtat_reclamation((String) item.get("etatReclamation"));
                r.setDate_reclamation(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse((String) item.get("dateReclamation")
                ));
                try {
                    r.setRep(new Reponse((int) Double.parseDouble(result.getAsString("reponses/id")), null, result.getAsString("reponses/Message"), null));
                } catch (Exception e) {

                }

                try {
                    r.setType(new Type((int) Double.parseDouble(result.getAsString("type/id")), result.getAsString("type/NomType")));

                } catch (Exception e) {

                }

                try {
                    r.setUser(new User((int) Double.parseDouble(result.getAsString("user/iduser")), result.getAsString("user/nomuser"), result.getAsString("user/prenomuser")));
                } catch (Exception e) {

                }

                System.err.println(r);
                reclamations.add(r);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return reclamations;
    }
    //La méthode deleteReclamation() permet de supprimer une réclamation.
    public boolean deleteReclamation(int id) {

        String url = Statics.BASE_URL + reclamationPrefix + "/deleteReclamationJSON/" + id;

        req.setUrl(url);
        req.addResponseListener((e) -> {
            String jsonResponse = new String(req.getResponseData());
            resultOK = req.getResponseCode() == 200;
            System.out.println(jsonResponse);
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
    
    //La méthode updateReclamation() permet de mettre à jour une réclamation existante.
    public boolean updateReclamation(Reclamation rec) {
        String url = Statics.BASE_URL + reclamationPrefix + "/updateReclamationJSON/" + rec.getId() + "?libelle=" + rec.getLibelle() + "&description=" + rec.getDescription() + "&photo=" + rec.getPhoto() + "&type=" + rec.getType().getId() + "&user_id=" + rec.getUser().getIdUser();
        req.setUrl(url);
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
