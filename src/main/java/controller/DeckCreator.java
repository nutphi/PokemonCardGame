package controller;

import model.*;
import model.ability.*;
import model.ability.ConditionAbility;
import model.ability.extra.*;
import model.ability.filter.*;
import model.ability.filter.Filter.FilterType;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by nutph on 6/3/2017.
 */
public class DeckCreator {
    private ArrayList<Integer> indexDeck1List;
    private ArrayList<Integer> indexDeck2List;
    private ArrayList<String> cardList;
    private ArrayList<String> abilityList;
    private boolean isTest;

    public DeckCreator(ArrayList<Integer> indexDeck1List, ArrayList<Integer> indexDeck2List, ArrayList<String> cardList, ArrayList<String > abilityList){
        this.indexDeck1List=indexDeck1List;
        this.indexDeck2List=indexDeck2List;
        this.cardList = cardList;
        this.abilityList=abilityList;
        this.isTest = true;
    }

    public DeckCreator(String deckName1, String deckName2, String cardsName, String abilitiesName){

        InputStreamReader deck1Stream = null;
        InputStreamReader deck2Stream = null;
        InputStreamReader cardsStream = null;
        InputStreamReader abilitiesStream = null;
        try {
            deck1Stream = new InputStreamReader(ClassLoader.getSystemResourceAsStream(deckName1), "UTF-8");
            deck2Stream = new InputStreamReader(ClassLoader.getSystemResourceAsStream(deckName2), "UTF-8");
            cardsStream = new InputStreamReader(ClassLoader.getSystemResourceAsStream(cardsName), "UTF-8");
            abilitiesStream = new InputStreamReader(ClassLoader.getSystemResourceAsStream(abilitiesName), "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }

        indexDeck1List = new ArrayList();
        indexDeck2List = new ArrayList();
        cardList = new ArrayList();
        abilityList = new ArrayList();

        Scanner sc = new Scanner(deck1Stream);
        while(sc.hasNext()) {
            indexDeck1List.add(Integer.parseInt(sc.next()));
        }

        sc = new Scanner(deck2Stream);
        while(sc.hasNext()) {
            indexDeck2List.add(Integer.parseInt(sc.next()));
        }

        sc = new Scanner(cardsStream);
        while(sc.hasNext()){
            cardList.add(sc.nextLine());
        }

        sc = new Scanner(abilitiesStream);
        while(sc.hasNext()){
            abilityList.add(sc.nextLine());
        }
        this.isTest = false;
    }


    /**
     * use deckx.txt to create cards on a deck order by the line number of the file
     * @param isOpponent
     * @return
     * @throws Exception
     */
    public ArrayList<Card> createDeck(boolean isOpponent) throws Exception {

        ArrayList<Integer> indexList = isOpponent?indexDeck1List:indexDeck2List;
        ArrayList<Card> actualDeck = new ArrayList();

        for (int i=0;i<indexList.size();i++) {
            String cardText = cardList.get(indexList.get(i) - 1);
            ArrayList<String> portionText = new ArrayList<>(Arrays.asList(cardText.split(":|,")));
            CardType cardType = CardType.valueOf2(portionText.get(1));
            switch (cardType){
                case ENERGY:
                    EnergyType energyType;
                    energyType = EnergyType.valueOf2(portionText.get(3));
                    EnergyCard energyCard = new EnergyCard(energyType,CardType.ENERGY,portionText.get(0),portionText.get(0),false);
                    actualDeck.add(energyCard);
                    break;
                case POKEMON:
                    //(name):(card type):cat:(stage):(evolve from):cat:(pokemon type):(hp):(retreat...):attacks:(ability1,ability2)
                    String pokemonName = portionText.remove(0);//name
                    String pokemonFileName;
                    if(pokemonName.equals("Meowth")){
                        pokemonFileName = portionText.get(portionText.size()-1)+pokemonName;
                    }else{
                        pokemonFileName = pokemonName;
                    }

                    boolean isEvolvedPokemon = false;
                    String description = "";

                    portionText.remove(0);//pokemon

                    if(portionText.get(0).equals("cat")){
                        portionText.remove(0);
                    }else{
                        throw new Exception("'cat' from basic/stage-one missing");
                    }

                    if(portionText.get(0).equals("basic")){
                        portionText.remove(0);
                        isEvolvedPokemon = false;
                    }else if(portionText.get(0).equals("stage-one")){
                        portionText.remove(0);
                        isEvolvedPokemon = true;
                        description = portionText.remove(0);
                    }else{
                        throw new Exception("Error stage ("+portionText.get(0)+")");
                    }

                    //cat
                    if(EnergyType.hasMore(portionText.get(0))){
                        portionText.remove(0);//remove cat
                    }

                    //pokemon 'energy' type
                    EnergyType pokemonEnergyType = EnergyType.valueOf2(portionText.get(0));
                    portionText.remove(0);//remove energy type
                    //hp
                    int hitpoint = Integer.parseInt(portionText.remove(0));
                    ArrayList<EnergyType> retreat = new ArrayList<>();
                    String ret = portionText.get(0);
                    if(ret.equals("retreat")){
                        portionText.remove(0);//remove retreat
                        portionText.remove(0);//remove cat
                        EnergyType retreatType = EnergyType.valueOf2(portionText.get(0));
                        portionText.remove(0);//colorless
                        try {
                            int size = Integer.parseInt(portionText.remove(0));
                            for(int j=0;j<size;j++)
                                retreat.add(retreatType);
                        }catch(NumberFormatException e){
                            throw new Exception("the problem retreat energy number is not number:"+portionText.get(0));
                        }
                    }
                    ArrayList<PokemonAbility> pokemonAbilities = new ArrayList<>();

                    if(portionText.get(0).equals("attacks")) {
                        portionText.remove(0);

                        Iterator<String> attackIterator = portionText.iterator();
                        ArrayList<EnergyType> abilityEnergies = new ArrayList();
                        energyType = null;

                        while(attackIterator.hasNext()){
                            String item = attackIterator.next();
                            if(EnergyType.hasMore(item)){
                                attackIterator.remove();
                                item = attackIterator.next();
                            }
                            energyType = EnergyType.valueOf2(item);
                            attackIterator.remove();

                            if(!attackIterator.hasNext()){
                                throw new Exception("number of ability energy type disappear");
                            }

                            try{
                                int time = Integer.parseInt(attackIterator.next());
                                for(int i1=0;i1<time;i1++){
                                    abilityEnergies.add(energyType);
                                }
                                attackIterator.remove();
                            }catch(NumberFormatException ex){
                                throw new Exception("number of ability energy type disappear");
                            }

                            if(!attackIterator.hasNext()){
                                throw new Exception("ability index disappear");
                            }
                            int abilityIndex;
                            try {
                                abilityIndex = Integer.parseInt(attackIterator.next());
                                attackIterator.remove();
                                Ability ability = null;
                                ArrayList<String> abilityItemsList = new ArrayList<>(Arrays.asList(abilityList.get(abilityIndex-1).split(":[(]|[)],|:|\\[|[]]|[(]|[)]|,")));

                                String name = abilityItemsList.remove(0);
                                ability = createAbility(ability, abilityItemsList, false);
                                PokemonAbility pokemonAbility = new PokemonAbility(name,abilityEnergies,ability);
                                abilityEnergies = new ArrayList();
                                pokemonAbilities.add(pokemonAbility);
                            }catch(NumberFormatException ex){
                                attackIterator = portionText.iterator();
                            }
                        }
                    }else{
                        throw new Exception("Error this is not attacks ("+portionText.get(0)+")");
                    }
                    if(isEvolvedPokemon){
                        if(description.equals(""))throw new Exception("evolving pokemon disappear");
                        EvolvedPokemonCard evolvedPokemonCard = new EvolvedPokemonCard(pokemonName,pokemonFileName, description, false, hitpoint, 0,
                                pokemonAbilities, retreat, true, pokemonEnergyType, null);
                        actualDeck.add(evolvedPokemonCard);
                    }else{
                        PokemonCard pokemonCard = new PokemonCard(pokemonName,pokemonFileName, description, false, hitpoint, 0,
                                pokemonAbilities, retreat, true, pokemonEnergyType);
                        actualDeck.add(pokemonCard);
                    }
                    break;
                case TRAINER:
                    String trainerName = portionText.get(0);
                    TrainerType trainerType = TrainerType.valueOf2(portionText.get(3));

                    ArrayList<TrainerAbility> trainerAbilities = new ArrayList<>();
                    //ability
                    Ability ability = null;
                    int abilityIndex = Integer.parseInt(portionText.get(4))-1;
                    String abilityText = abilityList.get(abilityIndex);

                    ArrayList<String> abilitySplit = new ArrayList<>(Arrays.asList(abilityText.split(":[(]|[)],|:|\\[|[]]|[(]|[)]|,")));
                    abilitySplit.remove(0);
                    ability = createAbility(ability,abilitySplit,false);
                    TrainerAbility trainerAbility = new TrainerAbility(ability);
                    trainerAbilities.add(trainerAbility);

                    TrainerCard trainerCard = new TrainerCard(CardType.TRAINER, trainerName,trainerName, false,
                            trainerType, trainerAbilities);
                    actualDeck.add(trainerCard);
                    break;
            }
        }
        if(!isTest) {
            isFollowingDeckBuilderRule(actualDeck, isOpponent);
        }
        return actualDeck;
    }

    public void isFollowingDeckBuilderRule(ArrayList<Card> actualDeck, boolean isOpponent){
        Map<String,Integer> cardNameNumberMap = new HashMap();
        Map<String,Card> cardNameMap = new HashMap();

        for (Card card: actualDeck) {
            Integer number = cardNameNumberMap.get(card.getFilename());
            if(number==null){
                cardNameNumberMap.put(card.getFilename(),1);
                cardNameMap.put(card.getFilename(),card);
            }else{
                cardNameNumberMap.put(card.getFilename(),number+1);
            }
        }
        String name = isOpponent?"Computer":"Human";
        if(actualDeck.size()!=60){
            JPokemonBoard.getBoard().showMessageDialog(name+"'s deck has "+actualDeck.size()+" ,but we need exact 60 cards","unable to play the game");
            System.exit(0);
        }

        boolean isBasicPokemon = false;
        for (Map.Entry<String, Integer> entry : cardNameNumberMap.entrySet()){
            int numberOfCard = entry.getValue();
            Card card=cardNameMap.get(entry.getKey());

            if(!card.getCardType().equals(CardType.ENERGY)&&numberOfCard<=4){
                if(card.getCardType().equals(CardType.POKEMON)&&!(card instanceof EvolvedPokemonCard)){
                    isBasicPokemon = true;
                }
                if(card instanceof EvolvedPokemonCard){
                    EvolvedPokemonCard evolvedPokemonCard = (EvolvedPokemonCard)card;
                    boolean isEvolvingPokemonPlayable = false;
                    for(Map.Entry<String, Card> cardEntry : cardNameMap.entrySet()) {
                        Card findingCard = cardEntry.getValue();
                        if(evolvedPokemonCard.getDescription().endsWith(findingCard.getName())){
                            isEvolvingPokemonPlayable = true;
                        }
                    }
                    if(!isEvolvingPokemonPlayable){
                        JPokemonBoard.getBoard().showMessageDialog(name+"'s deck has "+card.getName()+" without "+card.getDescription()+". We need at least one","unable to play the game");
                        System.exit(0);
                    }
                }
                if(numberOfCard>4){
                    JPokemonBoard.getBoard().showMessageDialog(name+"'s deck has "+numberOfCard+" "+card.getFilename()+" ,but we need at most 4","unable to play the game");
                    System.exit(0);
                }
            }

        }
        if(!isBasicPokemon){
            JPokemonBoard.getBoard().showMessageDialog(name+"'s deck does not have basic pokemon","unable to play the game");
            System.exit(0);
        }

    }

    /**
     * use ability.txt to create ability
     * @param ability
     * @param abilityTextItemList
     * @return
     */
    public Ability createAbility(Ability ability, ArrayList<String> abilityTextItemList, boolean isCond) throws Exception{
        //pokemon text list
        String text = abilityTextItemList.remove(0);
        AbilityType type = AbilityType.valueOf2(text);
        Ability subAbility = null;
        String textItem;
        AbilityTarget abilityTarget;

        switch (type){
            case DAM:
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("opponent-active")){
                        abilityTarget = AbilityTarget.OPPONENT_ACTIVE;
                    }else if(textItem.equals("your-active")){
                        abilityTarget = AbilityTarget.YOUR_ACTIVE;
                    }else if(textItem.equals("choice")){
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("opponent-bench")){
                            abilityTarget = AbilityTarget.CHOICE_OPPONENT_BENCH;
                        }else if(textItem.equals("your-bench")){
                            abilityTarget = AbilityTarget.CHOICE_YOUR_BENCH;
                        }else if(textItem.equals("opponent")){
                            abilityTarget = AbilityTarget.CHOICE_OPPONENT;
                        }else{
                            throw new Exception("ability target does not exist (choice:"+abilityTextItemList.get(0)+")");
                        }
                    }else if(textItem.equals("opponent")){
                        abilityTarget = AbilityTarget.CHOICE_OPPONENT;
                    }else if(textItem.equals("your-bench")){
                        abilityTarget = AbilityTarget.YOUR_BENCH;
                    }else{
                        throw new Exception("target type does not exist ("+textItem+")");
                    }
                }else{
                    throw new Exception("word 'target' does not exist ("+textItem+")");
                }
                int dam=0;

                textItem = abilityTextItemList.remove(0);
                Calculation calculation = null;
                if(textItem.contains("count")){
                    Calculation.Operator operator = Calculation.Operator.NULL;
                    if(textItem.contains("*")){
                        operator = Calculation.Operator.MULTIPLY;
                        String[] text2 = textItem.split("\\*");
                        for (String t:
                                text2) {
                            if(!t.contains("count")){
                                try{
                                    dam = Integer.parseInt(t);
                                }catch(NumberFormatException e){
                                    throw new Exception("this is not number ("+t+")");
                                }
                            }
                        }
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("target")){
                            textItem = abilityTextItemList.remove(0);
                            if(textItem.equals("your-bench")) {
                                calculation = new Calculation(Calculation.CountTarget.YOUR_BENCH, operator);
                            }else{
                                throw new Exception("this is not kind of count target ("+textItem+")");
                            }
                        }else{
                            throw new Exception("this is not 'target' word ("+textItem+")");
                        }
                    }else{
                        textItem = abilityTextItemList.remove(0);
                        Calculation.CountTarget target;
                        if(textItem.equals("target")){
                            textItem = abilityTextItemList.remove(0);
                            if(textItem.equals("opponent-active")){
                                textItem = abilityTextItemList.remove(0);
                                if(textItem.equals("energy")){
                                    target = Calculation.CountTarget.OPPONENT_ACTIVE_ENERGY;
                                }else{
                                    throw new Exception("this is not count target for dam ("+textItem+")");
                                }
                            }else if(textItem.equals("your-active")){
                                textItem = abilityTextItemList.remove(0);
                                if(textItem.equals("damage")){
                                    target = Calculation.CountTarget.YOUR_ACTIVE_DAMAGE;
                                }else{
                                    throw new Exception("this is not count target for dam ("+textItem+")");
                                }
                            }else{
                                throw new Exception("this is not count target for dam ("+textItem+")");
                            }
                            textItem = abilityTextItemList.remove(0);
                            if(textItem.contains("*")){
                                calculation = new Calculation(target, Calculation.Operator.MULTIPLY);
                                String[] text2= textItem.split("\\*");
                                for (String t:
                                        text2) {
                                    if(!t.equals("")){
                                        try{
                                            dam = Integer.parseInt(t);
                                        }catch(NumberFormatException e){
                                            throw new Exception("this is not number");
                                        }
                                    }
                                }
                            }else{
                                throw new Exception("this is not count target for dam ("+textItem+")");
                            }
                        }else{
                            throw new Exception("this is not 'target' word ("+textItem+")");
                        }
                    }
                }else {
                    try {
                        //text = abilityTextItemList.remove(0);
                        dam = Integer.parseInt(textItem);
                        if(abilityTextItemList.size()>0&&!isCond){
                            subAbility = createAbility(subAbility, abilityTextItemList,false);
                        }
                    } catch (NumberFormatException e) {
                        throw new Exception("this is not number for damage (" + textItem + ")");
                    }
                }
                if(calculation!=null){
                    if (subAbility != null) {
                        ability = new DamageAbility(dam, calculation, abilityTarget, AbilityType.DAM, subAbility);
                    } else {
                        ability = new DamageAbility(dam, calculation, abilityTarget, AbilityType.DAM);
                    }
                }else {
                    if (subAbility != null) {
                        ability = new DamageAbility(dam, abilityTarget, AbilityType.DAM, subAbility);
                    } else {
                        ability = new DamageAbility(dam, abilityTarget, AbilityType.DAM);
                    }
                }
                break;
            case HEAL:
                textItem=abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem=abilityTextItemList.remove(0);
                    if(textItem.equals("your")){
                        abilityTarget = AbilityTarget.CHOICE_YOUR;
                    }else if(textItem.equals("your-active")){
                        abilityTarget = AbilityTarget.YOUR_ACTIVE;
                    }else if(textItem.equals("self")){
                        abilityTarget = AbilityTarget.SELF;
                    }else{
                        throw new Exception("this is target for heal (" + textItem + ")");
                    }

                    textItem = abilityTextItemList.remove(0);
                    int heal;
                    try{
                        heal = Integer.parseInt(textItem);
                    }catch(NumberFormatException e){
                        throw new Exception("this is not number for heal (" + textItem + ")");
                    }
                    if(abilityTextItemList.size()>0){
                        subAbility = createAbility(subAbility, abilityTextItemList,false);
                    }
                    if(subAbility!=null) {
                        ability = new HealAbility(heal, abilityTarget, AbilityType.HEAL, subAbility);
                    }else{
                        ability = new HealAbility(heal, abilityTarget, AbilityType.HEAL);
                    }
                }else{
                    throw new Exception("this is not word 'target' for heal ("+textItem+")");
                }
                break;
            case DEENERGIZE:
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")) {
                    textItem = abilityTextItemList.remove(0);
                    if (textItem.equals("your-active")) {
                        abilityTarget = AbilityTarget.YOUR_ACTIVE;
                    } else if (textItem.equals("opponent-active")) {
                        abilityTarget = AbilityTarget.OPPONENT_ACTIVE;
                    } else {
                        throw new Exception("we don't have this ability target on deenergize yet (" + textItem + ")");
                    }
                }else{
                    throw new Exception("this is not word 'target' for deenergize ("+textItem+")");
                }
                textItem = abilityTextItemList.remove(0);
                Calculation count;
                if(textItem.equals("count")){
                    Calculation.CountTarget countTarget;
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("target")){
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("your-active")){
                            textItem = abilityTextItemList.remove(0);
                            if(textItem.equals("energy")){
                                countTarget = Calculation.CountTarget.YOUR_ACTIVE_ENERGY;
                                count = new Calculation(countTarget, Calculation.Operator.NULL);
                            }else{
                                throw new Exception("this type of count does not have yet ("+textItem+")");
                            }
                        }else{
                            throw new Exception("this count of denergize does not have ability target");
                        }
                    }else{
                        throw new Exception("this is not word 'target' for denergize:target:<x>:count:target");
                    }
                    if(abilityTextItemList.size()>0&&!isCond){
                        subAbility = createAbility(subAbility,abilityTextItemList,false);
                        ability = new DeenergyAbility(0, count, abilityTarget, AbilityType.DEENERGIZE, subAbility);
                    }else{
                        ability = new DeenergyAbility(0, count, abilityTarget, AbilityType.DEENERGIZE);
                    }
                }else{
                    int amount;
                    try {
                        amount = Integer.parseInt(textItem);
                    }catch(NumberFormatException e){
                        throw new Exception("we don't have this on deenergize yet ("+textItem+")");
                    }
                    if(abilityTextItemList.size()>0&&!isCond){
                        subAbility = createAbility(subAbility,abilityTextItemList,false);
                        ability = new DeenergyAbility(amount, abilityTarget, AbilityType.DEENERGIZE, subAbility);
                    }else{
                        ability = new DeenergyAbility(amount, abilityTarget, AbilityType.DEENERGIZE);
                    }
                }
                break;
            case REENERGIZE:
                //only one item do later
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this 'target' ("+textItem+")");
                }
                if(textItem.equals("your")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("choice")){
                    abilityTarget = AbilityTarget.CHOICE_YOUR;
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("your")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                AbilityTarget destinationAbilityTarget;
                if(textItem.equals("choice")){
                    destinationAbilityTarget = AbilityTarget.CHOICE_YOUR;
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(abilityTextItemList.size()>0) {
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    ability =  new ReenergyAbility(abilityTarget, destinationAbilityTarget, AbilityType.REENERGIZE, subAbility);
                }else{
                    ability = new ReenergyAbility(abilityTarget, destinationAbilityTarget, AbilityType.REENERGIZE);
                }

                break;
            case REDAMAGE:
                //only one pokemon do later
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this 'target' ("+textItem+")");
                }
                if(textItem.equals("opponent")){
                    abilityTarget = AbilityTarget.CHOICE_OPPONENT;
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }

                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this 'target' ("+textItem+")");
                }
                if(textItem.equals("opponent")){
                    destinationAbilityTarget = AbilityTarget.CHOICE_OPPONENT;
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }

                if(textItem.equals("count")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }

                Calculation.CountTarget countTarget;
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this 'target' ("+textItem+")");
                }
                if(textItem.equals("last")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("source")){
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("damage")){
                            calculation = new Calculation(Calculation.CountTarget.LAST_SOURCE_DAMAGE, Calculation.Operator.NULL);
                        }else{
                            throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                        }
                    }else{
                        throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                    }
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(abilityTextItemList.size()>0) {
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    ability = new RedamangeAbility(calculation, abilityTarget, destinationAbilityTarget, AbilityType.REDAMAGE, subAbility);
                }else{
                    ability = new RedamangeAbility(calculation, abilityTarget, destinationAbilityTarget, AbilityType.REDAMAGE);
                }
                break;
            case SWAP:
                //only one pokemon do later
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("your-active")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("your")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("choice")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("bench")){
                    abilityTarget = AbilityTarget.CHOICE_YOUR_BENCH;
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }

                if(abilityTextItemList.size()>0) {
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    ability = new SwapAbility(abilityTarget, AbilityType.SWAP,subAbility);
                }else{
                    ability = new SwapAbility(abilityTarget, AbilityType.SWAP);
                }
                break;
            case DESTAT:
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("last")){
                        abilityTarget = AbilityTarget.LAST;
                    }else{
                        throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                    }
                }else{
                    throw new Exception("this is not word 'target' for destat ("+textItem+")");
                }

                if(abilityTextItemList.size()>0) {
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    ability = new DestatusAbility(abilityTarget, AbilityType.DESTAT,subAbility);
                }else{
                    ability = new DestatusAbility(abilityTarget, AbilityType.DESTAT);
                }
                break;
            case APPLYSTAT:
                textItem = abilityTextItemList.remove(0);
                PokemonStatus pokemonStatus;
                if(textItem.equals("status")){
                    textItem= abilityTextItemList.remove(0);
                    if(textItem.equals("paralyzed")){
                        pokemonStatus = PokemonStatus.PARALYZED;
                    }else if(textItem.equals("asleep")){
                        pokemonStatus = PokemonStatus.ASLEEP;
                    }else if(textItem.equals("stuck")){
                        pokemonStatus = PokemonStatus.STUCK;
                    }else if(textItem.equals("poisoned")){
                        pokemonStatus = PokemonStatus.POISONED;
                    }else{
                        throw new Exception("we don't have this status yet ("+textItem+")");
                    }
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("opponent-active")){
                        abilityTarget = AbilityTarget.OPPONENT_ACTIVE;
                    }else{
                        throw new Exception("we don't have this ability target on applystat yet ("+textItem+")");
                    }
                }else{
                    throw new Exception("not word 'status' ("+textItem+")");
                }
                if(abilityTextItemList.size()>0){
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    ability = new ApplyStatusAbility(pokemonStatus,abilityTarget,AbilityType.APPLYSTAT,subAbility);
                }else{
                    ability = new ApplyStatusAbility(pokemonStatus,abilityTarget,AbilityType.APPLYSTAT);
                }
                break;
            case DRAW:
                boolean isOpponent = false;
                textItem = abilityTextItemList.get(0);
                if(textItem.equals("opponent")){
                    isOpponent = true;
                    abilityTextItemList.remove(0);
                    textItem = abilityTextItemList.get(0);
                }
                int amount;
                try{
                    amount = Integer.parseInt(textItem);
                    abilityTextItemList.remove(0);
                }catch(NumberFormatException e){
                    throw new Exception("draw ability need amount of card to draw ("+textItem+")");
                }
                if(abilityTextItemList.size()>0){
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    if(isOpponent){
                        ability = new DrawAbility(amount, AbilityTarget.OPPONENT, AbilityType.DRAW,subAbility);
                    }else {
                        ability = new DrawAbility(amount, AbilityTarget.YOU, AbilityType.DRAW,subAbility);
                    }
                }else {
                    if(isOpponent){
                        ability = new DrawAbility(amount, AbilityTarget.OPPONENT, AbilityType.DRAW);
                    }else {
                        ability = new DrawAbility(amount, AbilityTarget.YOU, AbilityType.DRAW);
                    }
                }
                break;
            case SEARCH:
                textItem = abilityTextItemList.remove(0);//word target
                BoardPosition source = null;
                Filter choiceFilter=null;
                Filter sourceFilter=null;
                int pickNum;

                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("you")){
                        abilityTarget = AbilityTarget.YOU;
                    }else if(textItem.equals("opponent")){
                        abilityTarget = AbilityTarget.OPPONENT;
                    }else if(textItem.equals("your")){
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("choice")){
                            abilityTarget = AbilityTarget.CHOICE_YOUR;
                        }else{
                            throw new Exception("this is not target for search (your:"+textItem+")");
                        }
                    }else{
                        throw new Exception("this is not target for search ("+textItem+")");
                    }
                }else{
                    throw new Exception("this is not word 'target' for search ability ("+textItem+")");
                }
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("filter")) {
                    choiceFilter = getFilterFromTextItemList(abilityTextItemList, AbilityType.SEARCH);
                    textItem = abilityTextItemList.remove(0);
                }
                if(textItem.equals("source")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("deck")){
                        source = BoardPosition.DECK;
                    }else if(textItem.equals("discard")){
                        source = BoardPosition.TRASH;
                    }else{
                        throw new Exception("source has only deck and discard ("+textItem+")");
                    }
                }

                textItem = abilityTextItemList.remove(0);

                if(textItem.equals("filter")) {
                    sourceFilter = getFilterFromTextItemList(abilityTextItemList, AbilityType.SEARCH);
                    textItem = abilityTextItemList.remove(0);
                }

                try{
                    pickNum = Integer.parseInt(textItem);
                }catch(NumberFormatException e){
                    throw new Exception("this is not pick number of search ability ("+textItem+")");
                }


                if(abilityTextItemList.size()>0){
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    if(choiceFilter!=null) {
                        ability = new SearchAbility(pickNum, sourceFilter, source, choiceFilter, abilityTarget, AbilityType.SEARCH, subAbility);
                    }else if(sourceFilter!=null){
                        ability = new SearchAbility(pickNum, sourceFilter, source, abilityTarget, AbilityType.SEARCH, subAbility);
                    }else{
                        ability = new SearchAbility(pickNum, source, abilityTarget, AbilityType.SEARCH, subAbility);
                    }
                }else{
                    if(choiceFilter!=null) {
                        ability = new SearchAbility(pickNum, sourceFilter, source, choiceFilter, abilityTarget, AbilityType.SEARCH);
                    }else if(sourceFilter!=null){
                        ability = new SearchAbility(pickNum, sourceFilter, source, abilityTarget, AbilityType.SEARCH);
                    }else{
                        ability = new SearchAbility(pickNum, source, abilityTarget, AbilityType.SEARCH);
                    }
                }
                break;
            case DECK:
                textItem = abilityTextItemList.remove(0);//word target
                DeckAbility.Destination destination;
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);//word target
                    if(textItem.equals("them")||textItem.equals("opponent")){
                        abilityTarget = AbilityTarget.OPPONENT;
                    }else{
                        throw new Exception("no this target yet ("+textItem+")");
                    }
                    textItem = abilityTextItemList.remove(0);//word target
                }else{
                    abilityTarget = AbilityTarget.YOU;
                }
                if(!textItem.equals("destination")){
                    throw new Exception("should be word 'destination'");
                }
                textItem = abilityTextItemList.remove(0);//discard/deck
                if(textItem.equals("discard")){
                    destination = DeckAbility.Destination.DISCARD;
                }else if(textItem.equals("deck")){
                    if(abilityTextItemList.get(0).equals("bottom")){
                        destination = DeckAbility.Destination.BOTTOM_DECK;
                        textItem = abilityTextItemList.remove(0);//bottom
                    }else {
                        destination = DeckAbility.Destination.TOP_DECK;
                    }
                }else{
                    throw new Exception("should be word 'discard' or 'deck'");
                }
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }
                DeckAbility.Choice choice = null;

                if(textItem.equals("choice")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("you")){
                        choice = DeckAbility.Choice.YOU_CHOOSE;//your hand
                    }else if(textItem.equals("target")){
                        choice = DeckAbility.Choice.OPPONENT_CHOOSE;
                    }else{
                        throw new Exception("no this target yet ("+textItem+")");
                    }
                }

                if(textItem.equals("count")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("opponent")){
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("hand")) {
                            calculation = new Calculation(Calculation.CountTarget.OPPONENT_HAND, Calculation.Operator.NULL);
                        }else{
                            throw new Exception("no this countTarget (opponent:"+textItem+")");
                        }
                    }else if(textItem.equals("your")){
                        textItem = abilityTextItemList.remove(0);
                        if(textItem.equals("hand")) {
                            calculation = new Calculation(Calculation.CountTarget.YOUR_HAND, Calculation.Operator.NULL);
                        }else{
                            throw new Exception("no this countTarget (opponent:"+textItem+")");
                        }
                    }else{
                        throw new Exception("no other calculation type yet ("+textItem+")");
                    }
                    if(abilityTextItemList.size()>0){
                        subAbility = createAbility(subAbility, abilityTextItemList,false);
                        ability = new DeckAbility(calculation,destination,abilityTarget, AbilityType.DECK,subAbility);
                    }else {
                        ability = new DeckAbility(calculation,destination,abilityTarget, AbilityType.DECK);
                    }
                }else{
                    textItem = abilityTextItemList.remove(0);
                    try{
                        amount = Integer.parseInt(textItem);
                        if(abilityTextItemList.size()>0&&!isCond){
                            subAbility = createAbility(subAbility, abilityTextItemList,false);
                            ability = new DeckAbility(amount,destination,choice,abilityTarget, AbilityType.DECK,subAbility);
                        }else {
                            ability = new DeckAbility(amount,destination,choice,abilityTarget, AbilityType.DECK);
                        }
                    }catch(NumberFormatException e){
                        throw new Exception("no other type yet ("+textItem+")");
                    }
                }
                break;
            case SHUFFLE:
                textItem = abilityTextItemList.remove(0);//word target
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                    if(textItem.equals("you")){
                        abilityTarget = AbilityTarget.YOU;
                    }else if(textItem.equals("opponent")){
                        abilityTarget = AbilityTarget.OPPONENT;
                    }else{
                        throw new Exception("this is not target for shuffle ("+textItem+")");
                    }
                }else{
                    throw new Exception("this is not word 'target' for shuffle ability ("+textItem+")");
                }

                //check if there is sub-ability or not
                if(abilityTextItemList.size()>0){
                    subAbility = createAbility(subAbility, abilityTextItemList,false);
                    ability = new ShuffleAbility(abilityTarget, AbilityType.SHUFFLE,subAbility);
                }else {
                    ability = new ShuffleAbility(abilityTarget, AbilityType.SHUFFLE);
                }
                break;
            case COND:
                textItem = abilityTextItemList.remove(0);//remove flip/ability/choice...
                ConditionAbility.Condition cond;
                if(textItem.equals("flip")){
                    cond = ConditionAbility.Condition.FILP;
                }else if(textItem.equals("ability")){
                    cond = ConditionAbility.Condition.ABILITY;
                    Ability conditionAbility = null;
                    conditionAbility = createAbility(conditionAbility, abilityTextItemList,true);
                    Ability head = null;
                    head = createAbility(head, abilityTextItemList,false);
                    return new AbilityConditionAbility(AbilityType.COND,cond,conditionAbility,head);
                }else if(textItem.equals("choice")){//choose yes or no
                    cond = ConditionAbility.Condition.CHOICE;
                }else if(textItem.equals("healed")){
                    cond = ConditionAbility.Condition.HEALED_YOUR_ACTIVE;
                    abilityTextItemList.remove(0);//remove target
                    abilityTextItemList.remove(0);//remove your active
                    abilityTarget = AbilityTarget.YOUR_ACTIVE;
                    subAbility = createAbility(subAbility, abilityTextItemList,true);
                    return new HealedConditionAbility(AbilityType.COND, cond, abilityTarget, subAbility);
                }else if(textItem.equals("count")){
                    cond = ConditionAbility.Condition.COUNT_IF;
                    Calculation.Operator operator;
                    textItem = abilityTextItemList.remove(0);//remove target
                    if(textItem.equals("target")){
                        textItem = abilityTextItemList.remove(0);//remove target
                    }else{
                        throw new Exception("not 'target' ("+textItem+")");
                    }

                    if(textItem.equals("your-active")){
                        textItem = abilityTextItemList.remove(0);//remove target
                    }else{
                        throw new Exception("not 'target' ("+textItem+")");
                    }

                    if(textItem.equals("energy")){
                        textItem = abilityTextItemList.remove(0);//remove target
                    }else{
                        throw new Exception("not 'target' ("+textItem+")");
                    }

                    if(textItem.equals("psychic")){
                        countTarget = Calculation.CountTarget.YOUR_ACTIVE_ENERGY_PSYCHIC;
                        textItem = abilityTextItemList.remove(0);//remove target
                    }else{
                        throw new Exception("not 'target' ("+textItem+")");
                    }

                    if(textItem.contains(">")){
                        operator = Calculation.Operator.GREATER;
                        amount = Integer.parseInt(textItem.replace(">",""));
                    }else{
                        throw new Exception("not operator provide ("+textItem+")");
                    }

                    if(abilityTextItemList.size()>0){
                        Ability headAbility = createAbility(subAbility,abilityTextItemList,false);
                        return new CountConditionAbility(amount,new Calculation(countTarget,operator), cond, headAbility);
                    }else{
                        throw new Exception("not operator provide ("+textItem+")");
                    }
                }else{
                    throw new Exception("the condition is not flip/ability/choice/healed/count ("+textItem+")");
                }
                //find else
                int elseIndex = -1;
                for(int i=0;i<abilityTextItemList.size();++i){
                    if(abilityTextItemList.get(i).equals("else")){
                        elseIndex = i;
                    }
                }
                if(elseIndex!=-1){
                    ArrayList<String> headTextItemList = new ArrayList(abilityTextItemList.subList(0,elseIndex));
                    Ability headAbility = null;
                    headAbility = createAbility(headAbility, headTextItemList,true);
                    ArrayList<String> tailTextItemList = new ArrayList(abilityTextItemList.subList(elseIndex+1,abilityTextItemList.size()));
                    Ability tailAbility = null;
                    tailAbility = createAbility(tailAbility, tailTextItemList,true);


                    if(tailTextItemList.size()>0){
                        subAbility = createAbility(subAbility, tailTextItemList,false);
                    }

                    if(subAbility!=null){
                        ability = new ConditionAbility(AbilityType.COND,cond,headAbility,tailAbility,subAbility);
                    }else {
                        ability = new ConditionAbility(AbilityType.COND, cond, headAbility, tailAbility, new NullAbility());
                    }
                }else{
                    Ability headAbility = null;
                    headAbility = createAbility(headAbility, abilityTextItemList,true);
                    if(abilityTextItemList.size()>0){
                        subAbility = createAbility(subAbility, abilityTextItemList,false);
                        ability = new ConditionAbility(AbilityType.COND,cond,headAbility,subAbility);
                    }else {
                        ability = new ConditionAbility(AbilityType.COND,cond,headAbility,new NullAbility());
                    }
                }
                break;
            case ADD:
                //only one pokemon do later
                textItem = abilityTextItemList.remove(0);
                if(textItem.equals("target")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("your")){
                    abilityTarget = AbilityTarget.CHOICE_YOUR;
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                AddAbility.Trigger trigger;

                if(textItem.equals("trigger")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("opponent")){
                    textItem = abilityTextItemList.remove(0);
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(textItem.equals("turn-end")){
                    trigger = AddAbility.Trigger.OPPONENT_TURN_END;
                }else{
                    throw new Exception("we don't have this ability target on destat yet ("+textItem+")");
                }
                if(abilityTextItemList.size()>0) {
                    subAbility = createAbility(subAbility,abilityTextItemList,false);
                    ability = new AddAbility(trigger, abilityTarget, AbilityType.ADD,subAbility);
                }else{
                    throw new Exception("we need sub ability for add");
                }
                break;
        }
        return ability;
    }


    private AbilityTarget getAbilityTargetFromTextItemList(ArrayList<String> abilityTextItemList, AbilityType abilityType) throws Exception{
        return null;
    }

    private Filter getFilterFromTextItemList(ArrayList<String> abilityTextItemList, AbilityType abilityType) throws Exception{
        String filterText = abilityTextItemList.remove(0);//filter
        if(FilterType.hasTwoItem(filterText)){
            //e.g. cat:item we only need item
            filterText = abilityTextItemList.remove(0);
        }
        FilterType type = FilterType.valueOf2(filterText);
        Filter filter = null;
        int showNum;
        switch(type){
            case TOP:
                filterText = abilityTextItemList.remove(0);
                try{
                    showNum = Integer.parseInt(filterText);
                }catch(NumberFormatException ex){
                    throw new Exception("it is not number");
                }
                filter = new TopFilter(showNum,type);
                break;
            case ENERGY:
                filter = new EnergyFilter(FilterType.ENERGY);
                break;
            case POKEMON:
                filterText = abilityTextItemList.get(0);
                try{
                    Integer.parseInt(filterText);//if number;
                    filter = new PokemonFilter(PokemonFilter.PokemonType.ANY,FilterType.POKEMON);
                }catch(NumberFormatException ex){
                    //if not number;
                    filterText = abilityTextItemList.remove(0);
                    if(filterText.equals("cat")){
                        filterText = abilityTextItemList.remove(0);
                        PokemonFilter.PokemonType pokemonType;
                        if(filterText.equals("basic")){
                            pokemonType = PokemonFilter.PokemonType.BASIC;
                        }else{
                            throw new Exception("this is not pokemon filter type ("+filterText+")");
                        }
                        filter = new PokemonFilter(pokemonType,FilterType.POKEMON);
                    }else{
                        throw new Exception("this is not pokemon filter type ("+filterText+")");
                    }
                }
                break;
            case ITEM:
                filter = new TrainerFilter(TrainerType.ITEM,type);
                break;
            case EVOLVES_FROM:
                filterText = abilityTextItemList.remove(0);
                if(filterText.equals("target")){
                    filterText = abilityTextItemList.remove(0);
                    if(filterText.equals("last")){
                        filter = new EvolvesFromFilter(AbilityTarget.LAST,FilterType.EVOLVES_FROM);
                    }else{
                        throw new Exception("this filter target did not implement yet ("+filterText+")");
                    }
                }else{
                    throw new Exception("this is not 'target' word ("+filterText+")");
                }
                break;
        }
        if(filter == null){
            throw new Exception("filter can't be null ("+filterText+")");
        }
        return filter;
    }

}

