/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gui;

import Services.ServiceReclamation;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.ComboBox;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import gui.AfficherReclamationClient;
import gui.Home;
import java.util.ArrayList;
import model.Reclamation;


public class ModifierReclamationClient extends Form {

    public ModifierReclamationClient(Resources theme) {
        this.setLayout(BoxLayout.y());

        this.setTitle("Modifier Reclamation");
        Toolbar toolbar = getToolbar();
        toolbar.setBackCommand("", new ActionListener<ActionEvent>() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, Integer.parseInt("200")));
                new Home(theme).show();
            }
        });
        ComboBox<Reclamation> listDeroulante = new ComboBox<>();
        add(listDeroulante);

        ArrayList<Reclamation> reclamationlist= ServiceReclamation.getInstance().fetchReclamation();

      
        if (reclamationlist.isEmpty()) {
            listDeroulante.addItem(new Reclamation("Aucune Reclamation"));
        } 
        else {
            for (Reclamation rec : reclamationlist) {
                listDeroulante.addItem(rec);
            }

            Container cnt = new Container(BoxLayout.y());

         
            Label labellibelle = new Label("Libelle");
            labellibelle.setUIID("CustomLabel2");

            TextField tflibellle = new TextField(listDeroulante.getSelectedItem().getLibelle(), "Libelle");
            tflibellle.getHintLabel().setUIID("CustomHint");

          
            Label descriplabel = new Label("Description");
            descriplabel.setUIID("CustomLabel2");

            TextField tfdescrip = new TextField(listDeroulante.getSelectedItem().getDescription(), "Description");
            tfdescrip.getHintLabel().setUIID("CustomHint");

          

          
            Button update_btn = new Button("Modifier");
            update_btn.setUIID("BlackRoundFilledBtn");

            cnt.addAll(labellibelle, tflibellle,
                    descriplabel, tfdescrip,
                    update_btn);

            tflibellle.setText(listDeroulante.getSelectedItem().getLibelle());
            tfdescrip.setText(listDeroulante.getSelectedItem().getDescription());
           

            addAll(cnt);

            //action add btn
            update_btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {

                    //update data
                    if (tflibellle.getText().isEmpty()
                            || tfdescrip.getText().isEmpty()) {
                        //toast if empty
                        ToastBar.showErrorMessage("Veuillez remplir tous les champs", FontImage.MATERIAL_ERROR);
                    } else {
                        Reclamation rec = new Reclamation();
                        rec.setId(listDeroulante.getSelectedItem().getId());
                        rec.setLibelle(tflibellle.getText());
                        rec.setDescription(tfdescrip.getText());
                        rec.setPhoto(listDeroulante.getSelectedItem().getPhoto());
                        rec.setUser(listDeroulante.getSelectedItem().getUser());
                        rec.setType(listDeroulante.getSelectedItem().getType());
                        if (ServiceReclamation.getInstance().updateReclamation(rec)) {
                            //success toast
                            ToastBar.showMessage("Reclamation modifiée", FontImage.MATERIAL_CHECK_CIRCLE);
                            setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, Integer.parseInt("200")));
                            new AfficherReclamationClient(theme).show();
                        } else {
                            //error toast
                            ToastBar.showMessage("Une erreur est survenue lors de la modification", FontImage.MATERIAL_ERROR);
                        }
                    }
                }
            });

            listDeroulante.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    tflibellle.setText(listDeroulante.getSelectedItem().getLibelle());
                    tfdescrip.setText(listDeroulante.getSelectedItem().getDescription());
                   
                }
            });

        }
    }

}
