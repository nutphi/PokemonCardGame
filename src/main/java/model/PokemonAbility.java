package model;

import model.ability.*;
import model.ability.ConditionAbility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by nutph on 5/6/2017.
 */
public class PokemonAbility {
    //ability energy type is new
    private EnergyType abilityEnergyType;
    private ArrayList<EnergyType> energyAmount;
    private String name;
    private Ability ability;

    /**
     * the ability can change status to the opponent Pokemon
     */

    public PokemonAbility() {}

    //use this one
    public PokemonAbility(String name, ArrayList<EnergyType> energyAmount, Ability ability) {
        this.name = name;
        Collections.sort(energyAmount);
        this.energyAmount = energyAmount;
        this.ability = ability;
    }

    //ready to remove
    public PokemonAbility(String name, int amount, ArrayList<EnergyType> energyAmount, AbilityTarget target, AbilityType abilityType, EnergyType abilityEnergyType) {
        this.name = name;
        if(abilityType.equals(AbilityType.DAM)){
            ability = new DamageAbility(amount,target,abilityType);
        }else if(abilityType.equals(AbilityType.HEAL)){
            ability = new HealAbility(amount,target,abilityType);
        }
        Collections.sort(energyAmount);
        this.energyAmount = energyAmount;
        this.abilityEnergyType = abilityEnergyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public ArrayList<EnergyType> getEnergyAmount() {
        return energyAmount;
    }

    public void setEnergyAmount(ArrayList<EnergyType> energyAmount) {
        this.energyAmount = energyAmount;
    }

    public EnergyType getAbilityEnergyType() {
        return abilityEnergyType;
    }

    public void setAbilityEnergyType(EnergyType abilityEnergyType) {
        this.abilityEnergyType = abilityEnergyType;
    }

    public boolean isAbleToAttack(ArrayList<EnergyCard> pokemonEnergies){
        //compare energy from pokemon and the ability
        if(energyAmount.size()>pokemonEnergies.size())
            return false;


        ArrayList<EnergyType> pokemonEnergyTypes = new ArrayList();
        for (EnergyCard c:pokemonEnergies) {
            pokemonEnergyTypes.add(c.getEnergyType());
        }

        ArrayList<EnergyType> abilityEnergyTypes = new ArrayList();
        for (EnergyType c:energyAmount) {
            abilityEnergyTypes.add(c);
        }


        Iterator<EnergyType> i_pokemon = pokemonEnergyTypes.iterator();


        while(i_pokemon.hasNext()){
            EnergyType pokemon = i_pokemon.next();
            for(int i=0;i<abilityEnergyTypes.size();++i){
                if(abilityEnergyTypes.get(i).equals(pokemon)){
                    abilityEnergyTypes.remove(i);
                    i_pokemon.remove();
                    break;
                }
            }
        }

        for(EnergyType e:abilityEnergyTypes){
            if(!e.equals(EnergyType.COLORLESS)){
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return name + " " + ability.toString();
    }
}
