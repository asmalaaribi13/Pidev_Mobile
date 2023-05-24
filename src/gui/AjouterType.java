/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

//import Services.OffreService;
import Services.ServiceReclamation;
import Services.ServiceType;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.List;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import java.util.ArrayList;
import model.Reclamation;
import model.Type;

//import model.Offre;

public class AjouterType extends Form {
    private int userID=1;

    AjouterType(Resources theme) {
        this.setLayout(BoxLayout.y());

        //titre interface
        this.setTitle("Ajouter Type");

        this.getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, (evt) -> {
            new Home(theme).show();

        });

        Label libelleLabel = new Label("Nom Type");
        libelleLabel.setUIID("CustomLabel2");

        TextField textFieldLibelle = new TextField("", "Nom Type");
        textFieldLibelle.getHintLabel().setUIID("CustomHint");

      
       
        //btn ajouter
        
        Button buttonAjouter = new Button("Ajouter");
        buttonAjouter.setUIID("BlackRoundFilledBtn");

        buttonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
              

                if (textFieldLibelle.getText().isEmpty()
                        ) {

                    Dialog d = new Dialog();
                    Container dialogContainer = new Container();
                    dialogContainer.setLayout(BoxLayout.y());

                    Label titreDialogLabel = new Label("Veuillez remplir tous les champs");
                    titreDialogLabel.setUIID("CustomLabel2");

                    Button buttonOk = new Button("Ok");
                    buttonOk.setUIID("BlackRoundFilledBtn");

                    buttonOk.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            d.dispose();
                        }
                    });

                    dialogContainer.addAll(titreDialogLabel, buttonOk);

                    d.add(dialogContainer);
                    d.show();

                } else {

                      Type rec = new Type(textFieldLibelle.getText());
                  
                    //create new evnt
                      if (ServiceType.getInstance().addType(rec)) {
                        new AfficherReclamationClient(theme).show();

                    } else {
                        //error toast
                   //     ToastBar.showMessage("Une erreur est survenue lors de l'ajout ", FontImage.MATERIAL_ERROR);

                        Dialog d = new Dialog();
                        Container dialogContainer = new Container();
                        dialogContainer.setLayout(BoxLayout.y());

                        Label titreDialogLabel = new Label("Une erreur est survenue lors de l'ajout");
                        titreDialogLabel.setUIID("CustomLabel2");

                        Button buttonOk = new Button("Ok");
                        buttonOk.setUIID("BlackRoundFilledBtn");

                        buttonOk.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent arg0) {
                                d.dispose();
                            }
                        });

                        dialogContainer.addAll(titreDialogLabel, buttonOk);

                        d.add(dialogContainer);
                        d.show();
                    }
                }
            }
        });

        this.addAll(libelleLabel, textFieldLibelle, buttonAjouter);

    }

    //3
}
