package model;

import javax.swing.*;

/**
 * Created by nutph on 5/6/2017.
 */
public class EnergyCard extends Card{
    private EnergyType energyType;
    public EnergyCard(){this.setCardType(CardType.ENERGY);}
    public EnergyCard(EnergyType energyType, CardType type, String name, String description, ImageIcon visibleCardImage,
                      ImageIcon invisibleCardImage, ImageIcon bigVisibleCardImage, boolean isVisibleCard){
        super(type,name,description,visibleCardImage,invisibleCardImage,bigVisibleCardImage,isVisibleCard);
        this.energyType = energyType;
    }

    public EnergyCard(EnergyType energyType, CardType type, String name, String filename, boolean isVisibleCard){
        super(type,name,filename,isVisibleCard);
        this.energyType = energyType;
    }


    public EnergyType getEnergyType() {
        return energyType;
    }
    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }
}
