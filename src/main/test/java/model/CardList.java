package model;

import model.ability.HealAbility;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by nutph on 5/8/2017.
 */

/**
 * for temporary pokemon
 */
public class CardList{


    /**
     * check the rule of card
     * 1. we need at least 1 basic pokemon in a deck
     * 2. except energy card, each card has maximum of 4
     * @param deckName deck file name
     * @return list of card deck
     **/
    public ArrayList<Card> importCard(String deckName){
        ArrayList<Card> cardList = new ArrayList();
        InputStream file = ClassLoader.getSystemResourceAsStream(deckName);
        Scanner sc = new Scanner(file);
        HashMap<String,Integer> checkRule = new HashMap<String,Integer>();
        boolean isPokemon = false;
        while(sc.hasNext()){
            String line = sc.nextLine();
            Card c = createCardFromCardName(line);
            if(checkRule.get(line)==null){
                checkRule.put(line,1);
            }else{
                int cardNum = checkRule.get(line);

                if(!(c instanceof EnergyCard) && cardNum >= 4){
                    JOptionPane.showMessageDialog(null,"<html> We can't have more than 4 cards in the deck" +
                            "<br>"+c.getName()+" has more than 4<html>","Alert",JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }

                checkRule.put(line,checkRule.get(line)+1);
            }
            cardList.add(c);
            if(c instanceof PokemonCard && !(c instanceof EvolvedPokemonCard)){
                isPokemon = true;
            }
        }


        if(cardList.size()!=60){
            JOptionPane.showMessageDialog(null,"<html>the deck must have exactly 60 cards (now "+cardList.size()+" cards)<br>please, fix the file<html>","Alert",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        if(!isPokemon){
            JOptionPane.showMessageDialog(null,"<html> We need at least one basic pokemon card in the deck file<html>","Alert",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        return cardList;
    }

    /**
     * read cards from stub
     * @param cardCode the code number of card
     * @return card
     */
    public Card createCardFromCardName(String cardCode)
    {
        if(cardCode.equals("Glameow"))
        {
            return new Glameow();
        }else if(cardCode.equals("Purugly"))
        {
            return new Purugly();
        }else if(cardCode.equals("LightningEnergy"))
        {
            return new LightningEnergy();
        }else if(cardCode.equals("Potion"))
        {
            return new Potion();
        }

        else if(cardCode.equals("WaterEnergy"))
        {
            return new WaterEnergy();
        }else if(cardCode.equals("Shellder"))
        {
            return new Shellder();
        }else if(cardCode.equals("Froakie"))
        {
            return new Froakie();
        }else if(cardCode.equals("Frogadier"))
        {
            return new Frogadier();
        }else if(cardCode.equals("Electabuzz"))
        {
            return new Electabuzz();
        }else if(cardCode.equals("Electivire"))
        {
            return new Electivire();
        }else if(cardCode.equals("Electrike"))
        {
            return new Electrike();
        }








        else if(cardCode.equals("FightingEnergy"))
        {
            return new FightingEnergy();
        }else if(cardCode.equals("Machop"))
        {
            return new Machop();
        }else if(cardCode.equals("Machoke"))
        {
            return new Machoke();
        }return null;
    }

    /**
     * stub no.1
     */
    private class Glameow extends PokemonCard {
        public Glameow(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Glameow.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Glameow.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Glameow");
            this.setDescription("-");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(60);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);


            this.setEnergies(new ArrayList());


            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);
            ArrayList<EnergyType> e1 = new ArrayList<EnergyType>();
            e1.add(EnergyType.COLORLESS);
            e1.add(EnergyType.COLORLESS);

            PokemonAbility a1 = new PokemonAbility("Act Cute",0, e, AbilityTarget.CHOICE_OPPONENT, AbilityType.DAM,EnergyType.COLORLESS);
            PokemonAbility a2 = new PokemonAbility("Scratch",20,e1,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            attack.add(a1);
            attack.add(a2);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.COLORLESS);
        }
    }

    /**
     * stub no.2
     */
    private class Purugly extends EvolvedPokemonCard {
        public Purugly(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Purugly.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Purugly.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Purugly");
            this.setDescription("Evolves from Glameow");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(100);
            this.setDamage(0);
            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            retreat.add(EnergyType.COLORLESS);

            this.setRetreat(retreat);

            this.setEnergies(new ArrayList());


            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);
            e.add(EnergyType.COLORLESS);
            ArrayList<EnergyType> e1 = new ArrayList<EnergyType>();
            e1.add(EnergyType.COLORLESS);
            e1.add(EnergyType.COLORLESS);
            e1.add(EnergyType.COLORLESS);

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            PokemonAbility a1 = new PokemonAbility("Slash",30,e,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            PokemonAbility a2 = new PokemonAbility("Nyan Press",40,e1,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            attack.add(a1);
            attack.add(a2);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.COLORLESS);
        }
    }

    /**
     * stub no.3
     */
    private class LightningEnergy extends EnergyCard {
        public LightningEnergy(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Lightning.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Lightning.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Lightning Energy");
            this.setDescription("Lightning Energy");
            this.setCardType(CardType.ENERGY);
            this.setEnergyType(EnergyType.LIGHTNING);
            this.setVisibleCard(false);
        }
    }

    /**
     * stub no.4
     */
    private class Potion extends TrainerCard {
        public Potion(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Potion.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Potion.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Potion");
            this.setDescription("Heal 30 Damage from 1 of your Pokemon");
            TrainerAbility ability = new TrainerAbility(30,AbilityTarget.CHOICE_OPPONENT,AbilityType.HEAL);
            ArrayList<TrainerAbility> abilities = new ArrayList();

            abilities.add(ability);

            this.setAbilities(abilities);
            this.setCardType(CardType.TRAINER);
            this.setVisibleCard(false);
        }

    }


    /**
     * stub no.5
     */
    private class WaterEnergy extends EnergyCard {
        public WaterEnergy(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Water.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Water.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Water Energy");
            this.setDescription("Water Energy");
            this.setCardType(CardType.ENERGY);
            this.setEnergyType(EnergyType.WATER);
            this.setVisibleCard(false);
        }
    }

    /**
     * stub no.6
     */
    private class Shellder extends PokemonCard {
        public Shellder(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Shellder.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Shellder.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Shellder");
            this.setDescription("-");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(60);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);


            this.setEnergies(new ArrayList());

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.WATER);
            e.add(EnergyType.COLORLESS);

            PokemonAbility a = new PokemonAbility("Rain Splash",20, e, AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.WATER);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.WATER);
        }
    }


    /**
     * stub no.7
     */
    private class Froakie extends PokemonCard {
        public Froakie(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Froakie.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Froakie.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Froakie");
            this.setDescription("-");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(50);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);


            this.setEnergies(new ArrayList());

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);

            PokemonAbility a = new PokemonAbility("Pound",20, e, AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.WATER);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.WATER);
        }
    }


    /**
     * stub no.8
     */
    private class Frogadier extends EvolvedPokemonCard {
        public Frogadier(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Frogadier.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Frogadier.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Frogadier");
            this.setDescription("Evolves from Froakie");
            this.setCardType(CardType.POKEMON);
            this.setHitpoint(70);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);

            this.setEnergies(new ArrayList());


            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);
            e.add(EnergyType.COLORLESS);

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            PokemonAbility a = new PokemonAbility("CUT",30
                    ,e,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.FIGHT);
        }
    }




    /**
     * stub no.9
     */
    private class Electabuzz extends PokemonCard {
        public Electabuzz(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Electabuzz.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Electabuzz.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Electabuzz");
            this.setDescription("-");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(70);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);


            this.setEnergies(new ArrayList());


            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);
            e.add(EnergyType.COLORLESS);

            PokemonAbility a = new PokemonAbility("Knuckle Punch",20,e,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.LIGHTNING);

        }
    }

    /**
     * stub no.10
     */
    private class Electivire extends EvolvedPokemonCard {
        public Electivire(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Electivire.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Electivire.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Electivire");
            this.setDescription("Evolves from Electabuzz");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(110);
            this.setDamage(0);
            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            retreat.add(EnergyType.COLORLESS);
            retreat.add(EnergyType.COLORLESS);

            this.setRetreat(retreat);

            this.setEnergies(new ArrayList());


            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);
            e.add(EnergyType.COLORLESS);
            ArrayList<EnergyType> e1 = new ArrayList<EnergyType>();
            e1.add(EnergyType.LIGHTNING);
            e1.add(EnergyType.LIGHTNING);
            e1.add(EnergyType.COLORLESS);

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            PokemonAbility a1 = new PokemonAbility("Knuckle Punch",30,e,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            PokemonAbility a2 = new PokemonAbility("Electroslug",90
                    ,e1,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.LIGHTNING);
            attack.add(a1);
            attack.add(a2);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.LIGHTNING);
        }
    }


    /**
     * stub no.11
     */
    private class Electrike extends PokemonCard {
        public Electrike(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Electrike.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Electrike.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Electrike");
            this.setDescription("-");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(60);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);


            this.setEnergies(new ArrayList());


            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.COLORLESS);

            PokemonAbility a = new PokemonAbility("Bite",10,e,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.COLORLESS);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.LIGHTNING);
        }
    }



    /**
     * stub no.27
     */
    private class FightingEnergy extends EnergyCard {
        public FightingEnergy(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Fight.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Fight.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Fighting Energy");
            this.setDescription("Fighting Energy");
            this.setCardType(CardType.ENERGY);
            this.setEnergyType(EnergyType.FIGHT);
            this.setVisibleCard(false);
        }
    }


    /**
     * stup no.28
     */
    private class Machop extends PokemonCard {
        public Machop(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Machop.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Machop.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Machop");
            this.setDescription("-");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(70);
            this.setDamage(0);

            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            retreat.add(EnergyType.COLORLESS);
            this.setRetreat(retreat);


            this.setEnergies(new ArrayList());

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.FIGHT);
            PokemonAbility a = new PokemonAbility("Knuckle Punch",10, e, AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.FIGHT);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.FIGHT);
        }
    }

    /**
     * stub no.29
     */
    private class Machoke extends EvolvedPokemonCard {
        public Machoke(){
            ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
            ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/normal1/Machoke.jpg"));
            ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/big1/Machoke.jpg"));
            this.setBigVisibleCardImageIcon(bigVisible);
            this.setVisibleCardImageIcon(visible);
            this.setInvisibleCardImageIcon(invisible);
            this.setName("Machoke");
            this.setDescription("Evolves from Machop");
            this.setCardType(CardType.POKEMON);

            this.setHitpoint(90);
            this.setDamage(0);
            ArrayList<EnergyType> retreat = new ArrayList<EnergyType>();
            retreat.add(EnergyType.COLORLESS);
            retreat.add(EnergyType.COLORLESS);

            this.setRetreat(retreat);

            this.setEnergies(new ArrayList());


            ArrayList<EnergyType> e = new ArrayList<EnergyType>();
            e.add(EnergyType.FIGHT);
            e.add(EnergyType.FIGHT);

            ArrayList<PokemonAbility> attack= new ArrayList<PokemonAbility>();
            PokemonAbility a = new PokemonAbility("Beat Down",40
                    ,e,AbilityTarget.OPPONENT_ACTIVE,AbilityType.DAM,EnergyType.FIGHT);
            attack.add(a);
            this.setAbilities(attack);
            this.setNewPokemon(true);
            this.setVisibleCard(false);
            this.setPokemonType(EnergyType.FIGHT);
        }
    }

}
