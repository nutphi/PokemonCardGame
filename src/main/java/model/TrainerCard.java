package model;
import javax.swing.*;
import java.util.ArrayList;
/**
 * Created by nutph on 5/4/2017.
 */
public class TrainerCard extends Card {
    private TrainerType trainerType;
    private ArrayList<TrainerAbility> abilities;
    public TrainerCard(){this.setCardType(CardType.TRAINER);}

    //plan to remove it soon
    public TrainerCard(CardType type, String name, String description, ImageIcon visibleCardImage,
                       ImageIcon invisibleCardImage,ImageIcon bigVisibleCardImage, boolean isVisibleCard,
                       TrainerType trainerType, ArrayList<TrainerAbility> abilities) {
        super(type,name,description,visibleCardImage,invisibleCardImage,bigVisibleCardImage,isVisibleCard);
        this.trainerType = trainerType;
        this.abilities = abilities;
    }

    public TrainerCard(CardType type, String name, String filename, boolean isVisibleCard,
                       TrainerType trainerType, ArrayList<TrainerAbility> abilities) {
        super(type,name,filename,isVisibleCard);
        this.trainerType = trainerType;
        this.abilities = abilities;
    }


    public void setTrainerType(TrainerType trainerType) {
        this.trainerType = trainerType;
    }

    public TrainerType getTrainerType() {
        return trainerType;
    }

    public ArrayList<TrainerAbility> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<TrainerAbility> abilities) {
        this.abilities = abilities;
    }
}
