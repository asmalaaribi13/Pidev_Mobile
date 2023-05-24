/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Gui.ModifierReclamationClient;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;

/**
 *
 * @author Asma Laaribi
 */
public class Home extends Form {

    private boolean admin = false;

    public Home(Resources theme) {

        this.setLayout(BoxLayout.y());
        //titre
        this.setTitle("Acceuil");
        String title = "";

        if (admin) {
            title = "Les Reclamations";
        } else {
            title = "Mes Reclamations";
        }

        this.getToolbar().addCommandToLeftSideMenu(title, null, (evt) -> {
            new AfficherReclamationClient(theme).show();
        });
        if (admin) {
        this.getToolbar().addCommandToLeftSideMenu("Ajouter Type", null, (evt) -> {
            new AjouterType(theme).show();
        });
        } else {
        this.getToolbar().addCommandToLeftSideMenu("Ajouter Reclamation", null, (evt) -> {
            new AjouterReclamation(theme).show();
        });

        this.getToolbar().addCommandToLeftSideMenu("Modifier Reclamation", null, (evt) -> {
            new ModifierReclamationClient(theme).show();
        });
        }
     

    }
}
