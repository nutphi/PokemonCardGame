package model;

import view.JPokemonBoard;

import javax.swing.*;
import java.awt.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by nutph on 5/4/2017.
 */
public abstract class Card extends JLabel implements ICard{

    //for search/deck ability
    public enum AbilityAction{
        NON_ACTIVE,SEARCH_DECK_ACTIVE,SEARCH_TRASH_ACTIVE,
        TOP_DECK,BOTTOM_DECK,DISCARD,SEARCH_DECK_EVOLVE_ACTIVE;
    }

    private AbilityAction abilityAction;
    private CardType type;
    private String name;
    private String description;

    private String filename;
    private ImageIcon visibleCardImage;
    private ImageIcon bigVisibleCardImage;

    public Card(){}


    //plan to remove soon, will use the one below
    public Card(CardType type, String name, String description, ImageIcon visibleCardImage, ImageIcon invisibleCardImage, ImageIcon bigVisibleCardImage, boolean isVisibleCard) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.visibleCardImage = visibleCardImage;
        this.invisibleCardImage = invisibleCardImage;
        this.bigVisibleCardImage = bigVisibleCardImage;

        this.isVisibleCard = isVisibleCard;
        this.abilityAction = AbilityAction.NON_ACTIVE;

        if(isVisibleCard)
            this.setIcon(visibleCardImage);
        else
            this.setIcon(invisibleCardImage);
    }

    //we will use this one
    public Card(CardType type, String name, String filename, boolean isVisibleCard) {
        this.type = type;
        this.name = name;
        this.filename = filename;
        this.invisibleCardImage = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
        this.visibleCardImage = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/"+filename+".jpg"));
        this.bigVisibleCardImage = new ImageIcon(getClass().getClassLoader().getResource("images/big1/"+filename+".jpg"));
        this.abilityAction = AbilityAction.NON_ACTIVE;

        this.isVisibleCard = isVisibleCard;

        if(isVisibleCard)
            this.setIcon(visibleCardImage);
        else
            this.setIcon(invisibleCardImage);
    }


    private ImageIcon invisibleCardImage;
    private boolean isVisibleCard;


    public void setDescription(String desciption) {
        this.description = desciption;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setCardType(CardType type) {
        this.type = type;
    }

    public CardType getCardType(){
        return type;
    }

    public AbilityAction getAbilityAction() {
        return abilityAction;
    }

    public void setAbilityAction(AbilityAction abilityAction) {
        this.abilityAction = abilityAction;
    }

    public void setVisibleCardImageIcon(ImageIcon img){
        visibleCardImage=img;
    }

    public ImageIcon getVisibleCardImageIcon(){
        return visibleCardImage;
    }

    public void setInvisibleCardImageIcon(ImageIcon img){
        invisibleCardImage=img;
    }

    public ImageIcon getInvisibleCardImageIcon(){
        return invisibleCardImage;
    }

    public void setBigVisibleCardImageIcon(ImageIcon img){
        bigVisibleCardImage=img;
    }

    public ImageIcon getBigVisibleCardImageIcon(){
        return bigVisibleCardImage;
    }

    public void setVisibleCard(boolean isVisibleCard){
        if(isVisibleCard)
        {
            this.setIcon(this.getVisibleCardImageIcon());
        }
        else
        {
            this.setIcon(this.getInvisibleCardImageIcon());
        }
        this.isVisibleCard=isVisibleCard;
    }

    public boolean isVisibleCard(){
        return isVisibleCard;
    }

    public void setLocation(Point p) {
        super.setBounds(p.x, p.y, 70, 100);
    }

    public void setLocation(int x, int y) {
        super.setBounds(x, y, 70, 100);
    }

    public boolean isPokemonCard(){
        return this.getCardType().equals(CardType.POKEMON);
    }

    public boolean isBasicPokemonCard(){
        return isPokemonCard()&&!(this instanceof EvolvedPokemonCard);
    }

    public boolean isStageOnePokemonCard(){
        return isPokemonCard()&&this instanceof EvolvedPokemonCard;
    }

    public boolean isTrainerCard(){
        return this.getCardType().equals(CardType.TRAINER);
    }

    public boolean isStadiumTrainerCard(){
        return this.isTrainerCard()&&((TrainerCard)this).getTrainerType().equals(TrainerType.STADIUM);
    }

    public boolean isItemTrainerCard(){
        return this.isTrainerCard()&&((TrainerCard)this).getTrainerType().equals(TrainerType.ITEM);
    }

    public boolean isSupporterTrainerCard(){
        return this.isTrainerCard()&&((TrainerCard)this).getTrainerType().equals(TrainerType.SUPPORTER);
    }

    public boolean isEnergyCard(){
        return this.getCardType().equals(CardType.ENERGY);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
