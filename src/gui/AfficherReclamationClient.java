/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Services.ServiceReclamation;
import com.codename1.components.FloatingActionButton;
import com.codename1.components.InfiniteProgress;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.AnimationManager;
import com.codename1.ui.Button;
import com.codename1.ui.ComboBox;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.RoundBorder;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Reclamation;

public class AfficherReclamationClient extends Form {

    private boolean admin = false;

    public AfficherReclamationClient() {
        this(Resources.getGlobalResources());
    }

    public AfficherReclamationClient(Resources resourceObjectInstance) {

        //SKELETON
        setLayout(BoxLayout.y());

        //titre
        String title = "Mes reclamations";
        if (admin) {
            title = "Les reclamations";
        }
        setTitle(title);

        Form previous = Display.getInstance().getCurrent();
        Toolbar toolbar = getToolbar();

        //go back to dashboard
        toolbar.setBackCommand("", new ActionListener<ActionEvent>() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, Integer.parseInt("200")));
                new Home(resourceObjectInstance).show();
            }
        });
        if (!admin) {
            FloatingActionButton fab = FloatingActionButton.createFAB(FontImage.MATERIAL_ADD);
            RoundBorder rb = (RoundBorder) fab.getUnselectedStyle().getBorder();
            rb.uiid(true);
            fab.bindFabToContainer(getContentPane());
            fab.addActionListener(e -> {
                new AjouterReclamation(resourceObjectInstance).show();
            });
        }

        ArrayList<Reclamation> listDesElements = ServiceReclamation.getInstance().fetchReclamation();
        ComboBox<String> filter = new ComboBox();
        filter.addItem("Etat Reclamation");
        filter.addItem("En cours");
        filter.addItem("Traité");
        add(filter);
        //  Button test = new Button("test");
        //  add(test);
        ArrayList<Reclamation> tempRecl = new ArrayList<>();

        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {

                if (!filter.getSelectedItem().equals("Etat Reclamation")) {
                    {
                        for (int i = 0; i < listDesElements.size(); i++) {

                            if (listDesElements.get(i).getEtat_reclamation().toUpperCase().contains(filter.getSelectedItem().toUpperCase())) {
                                tempRecl.add(listDesElements.get(i));
                            }

                        }
                        setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_VERTICAL, true, Integer.parseInt("0")));

                        new AfficherReclamationClientFiltred(tempRecl, listDesElements, resourceObjectInstance).show();
                    }
                }
            }
        }
        );
        afficher(resourceObjectInstance, listDesElements);
    }

    private void afficher(Resources resourceObjectInstance, ArrayList<Reclamation> reclamatioList) {

        setLayout(new BoxLayout(BoxLayout.Y_AXIS));

        if (reclamatioList.isEmpty()) {

            setLayout(new FlowLayout(CENTER, CENTER));

            Label emptyLabel = new Label("Aucun element");
            emptyLabel.setUIID("CenterLabel");

            add(emptyLabel);
        } else {

            for (Reclamation reclamation : reclamatioList) {

                //init vars
                String libelleString = "Libelle :" + reclamation.getLibelle();
                String descriptionString = "Description :" + reclamation.getDescription();
                String etatReclamationString = reclamation.getEtat_reclamation();
                String typeString = reclamation.getType().getNom_type();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = formatter.format(reclamation.getDate_reclamation());
                String dateReclamationString = formattedDate;

                Container containerMain = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                containerMain.getStyle().setPaddingUnit(Style.UNIT_TYPE_DIPS);
                containerMain.getStyle().setPaddingTop(2);
                containerMain.getStyle().setPaddingBottom(2);
                containerMain.getStyle().setPaddingLeft(4);
                containerMain.getStyle().setPaddingRight(4);
                containerMain.getStyle().setMargin(10, 10, 20, 20);

                containerMain.getStyle().setBorder(Border.createLineBorder(1, 0xCCCCCC));
                containerMain.getStyle().setBgColor(0xEEEEEE);
                Container containerTop = new Container(new BorderLayout());
                Container containerMid = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                Container containerBottom = new Container(new BorderLayout());

                //libelle
                Label libelle = new Label();
                libelle.setText(libelleString);
                containerTop.add(BorderLayout.WEST, libelle);

                Label type = new Label();
                type.setText(typeString);
                containerTop.add(BorderLayout.EAST, type);
                containerMain.add(containerTop);

                //categ
                Label desc = new Label();
                desc.setText(descriptionString);
                containerMid.add(desc);
                containerMain.add(containerMid);
                Label dateLabel = new Label();
                dateLabel.setText(dateReclamationString);
                containerBottom.add(BorderLayout.WEST, dateLabel);
                //info
                SpanLabel etatLabel = new SpanLabel();
                etatLabel.setText(etatReclamationString);
                if (etatReclamationString.toLowerCase().contains("en")) {
                    etatLabel.setTextUIID("RedLabel");
                } else {
                    etatLabel.setTextUIID("GreenLabel");
                }
                containerBottom.add(BorderLayout.EAST, etatLabel);
                containerMain.add(containerBottom);

                //btn supprimer
                Button supbtn = new Button("Supprimer");
                supbtn.setUIID("IndianredRoundFilledBtn");

                //afichage du boutton
                if (!admin) {
                    containerMain.add(supbtn);
                }

                //evenemnt on click
                supbtn.addActionListener((arg0) -> {

                    if (ServiceReclamation.getInstance().deleteReclamation(reclamation.getId())) {
                        ToastBar.showMessage("Suppression", FontImage.MATERIAL_ERROR);
                        setTransitionOutAnimator(CommonTransitions.createSlide(CommonTransitions.SLIDE_HORIZONTAL, true, Integer.parseInt("0")));

                        new AfficherReclamationClient(resourceObjectInstance).show();
                    } else {
                        //error toast

                        ToastBar.showMessage("Une erreur est survenue lors de la suppression ", FontImage.MATERIAL_ERROR);
                    }

                });
                add(containerMain);

            }
        }
    }

}
