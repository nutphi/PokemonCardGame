package model;

import view.JPokemonBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by nutph on 5/6/2017.
 */
public class PokemonCard extends Card {
    int hitpoint;
    int damage;
    //if you just put pokemon on board, you can put stage-one pokemon on this.
    boolean isNewPokemon;
    boolean isHealed;//only for cond:healed
    ArrayList<PokemonAbility> abilities;
    ArrayList<EnergyType> retreat;
    ArrayList<EnergyCard> energies;
    ArrayList<TrainerCard> tools;//add ability
    Set<PokemonStatus> status;
    EnergyType pokemonType;
    String description;
    public PokemonCard(){this.setCardType(CardType.POKEMON);}

    public PokemonCard(CardType type, String name, String description, ImageIcon visibleCardImage, ImageIcon invisibleCardImage, ImageIcon bigVisibleCardImage, boolean isVisibleCard, int hitpoint, int damage, boolean isNewPokemon, boolean isHealed, ArrayList<PokemonAbility> abilities, ArrayList<EnergyType> retreat, ArrayList<EnergyCard> energies, ArrayList<TrainerCard> tools, Set<PokemonStatus> status, EnergyType pokemonType, String description1) {
        super(type, name, description, visibleCardImage, invisibleCardImage, bigVisibleCardImage, isVisibleCard);
        this.hitpoint = hitpoint;
        this.damage = damage;
        this.isNewPokemon = isNewPokemon;
        this.isHealed = isHealed;
        this.abilities = abilities;
        this.retreat = retreat;
        this.energies = energies;
        this.tools = tools;
        this.status = status;
        this.pokemonType = pokemonType;
        this.description = description1;
    }

    //going to delete soon
    public PokemonCard(String name, String description, ImageIcon visibleImage, ImageIcon invisibleImage,
                       ImageIcon bigVisibleImage, boolean isVisibleCard, int hitpoint, int damage,
                       ArrayList<PokemonAbility> abilities, ArrayList<EnergyType> retreat, boolean isNewPokemon,
                       EnergyType pokemonType){
        super(CardType.POKEMON, name, description, visibleImage, invisibleImage, bigVisibleImage, isVisibleCard);
        this.abilities=abilities;
        this.retreat=retreat;
        this.hitpoint = hitpoint;
        this.damage = damage;
        this.energies = new ArrayList();
        this.status = new HashSet();
        this.isNewPokemon = isNewPokemon;
        this.pokemonType = pokemonType;
    }


    public PokemonCard(String name, String filename, String description, boolean isVisibleCard, int hitpoint, int damage,
                       ArrayList<PokemonAbility> abilities, ArrayList<EnergyType> retreat, boolean isNewPokemon,
                       EnergyType pokemonType){
        super(CardType.POKEMON, name, filename, isVisibleCard);
        this.description = description;
        this.abilities=abilities;
        this.retreat=retreat;
        this.hitpoint = hitpoint;
        this.damage = damage;
        this.tools = new ArrayList();
        this.energies = new ArrayList();
        this.status = new HashSet<>();
        this.isNewPokemon = isNewPokemon;
        this.pokemonType = pokemonType;
        this.isHealed = false;
    }

    public EnergyType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(EnergyType pokemonType) {
        this.pokemonType = pokemonType;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public int getHitpoint() {
        return hitpoint;
    }

    public void setHitpoint(int hitpoint) {
        this.hitpoint = hitpoint;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public ArrayList<PokemonAbility> getAbilities() {
        return abilities;
    }

    public void setAbilities(ArrayList<PokemonAbility> abilities) {
        this.abilities = abilities;
    }

    public ArrayList<EnergyType> getRetreat() {
        return retreat;
    }

    public void setRetreat(ArrayList<EnergyType> retreat) {
        this.retreat = retreat;
    }

    public ArrayList<EnergyCard> getEnergies() {
        return energies;
    }

    public void setEnergies(ArrayList<EnergyCard> energies) {
        this.energies = energies;
    }

    public Set<PokemonStatus> getStatus() {
        return status;
    }

    public void setStatus(Set<PokemonStatus> status) {
        this.status = status;
        setDamageStatusText();
    }

    public boolean isNewPokemon() {
        return isNewPokemon;
    }

    public void setNewPokemon(boolean newPokemon) {
        isNewPokemon = newPokemon;
    }

    public ArrayList<TrainerCard> getTools() {
        return tools;
    }

    public void setTools(ArrayList<TrainerCard> tools) {
        this.tools = tools;
    }

    public boolean isHealed() {
        return isHealed;
    }

    public void setHealed(boolean healed) {
        isHealed = healed;
    }

    public ArrayList<EnergyType> getPokemonEnergyTypes(){
        ArrayList<EnergyType> energyTypes = new ArrayList<EnergyType>();
        for (EnergyCard energyCard:getEnergies()){
            energyTypes.add(energyCard.getEnergyType());
        }
        return energyTypes;
    }

    public EnergyCard removeEnergyCardFromEnergyType(EnergyType energyType){
        EnergyCard energyCard;
        for(int i=0;i<getEnergies().size();i++){
            if(energyType.equals(getEnergies().get(i).getEnergyType())){
                energyCard = getEnergies().remove(i);
                return energyCard;
            }
        }
        return null;
    }

    public void setDamageStatusText(){
        if(damage>0||status.contains(PokemonStatus.PARALYZED)||status.contains(PokemonStatus.STUCK)||
                status.contains(PokemonStatus.ASLEEP)||status.contains(PokemonStatus.POISONED)) {
            setHorizontalTextPosition(JLabel.CENTER);
            setFont(new Font("Courier", Font.PLAIN, 20));
            String damageStatusText = "<html><br><p style='text-align:center;'>";
            if(damage>0){
                damageStatusText+=damage+"<br>";
            }
            Iterator<PokemonStatus> statusSet =  status.iterator();
            while(statusSet.hasNext()) {
                damageStatusText += statusSet.next().value;
            }
            damageStatusText += "</p><html>";
            setText(damageStatusText);
        }else{
            setText("");
        }
    }
}
