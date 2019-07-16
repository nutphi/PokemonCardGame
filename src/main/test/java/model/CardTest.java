package model;

import org.junit.Test;

import static org.junit.Assert.*;


public class CardTest {
    @Test
    public void createPokemonCardValidateIsPokemon_ReturnTrue() throws Exception {

        String codeForPokemon = "Glameow";
        CardList cardCreator = new CardList();
        Card testPokemonCard = cardCreator.createCardFromCardName(codeForPokemon);
        assertEquals(true, testPokemonCard.isPokemonCard());
    }

    @Test
    public void createTrainerCardValidateIsPokemon_ReturnFalse() throws Exception {

        String codeForPokemon = "Potion";
        CardList cardCreator = new CardList();
        Card testPokemonCard = cardCreator.createCardFromCardName(codeForPokemon);
        assertEquals(false, testPokemonCard.isPokemonCard());
    }

    @Test
    public void createPokemonCardValidateIsBasic_ReturnTrue() throws Exception {
        String codeForPokemon = "Glameow";
        CardList cardCreator = new CardList();
        Card testPokemonCard = cardCreator.createCardFromCardName(codeForPokemon);
        assertEquals(true, testPokemonCard.isBasicPokemonCard());
    }

    @Test
    public void createEvolvedPokemonCardValidateIsBasic_ReturnFalse() throws Exception {
        String codeForEvolvedPokemon = "Purugly";
        CardList cardCreator = new CardList();
        Card testPokemonCard = cardCreator.createCardFromCardName(codeForEvolvedPokemon);
        assertEquals(false, testPokemonCard.isBasicPokemonCard());
    }

    @Test
    public void createEvolvedPokemonValidateIsStageOne_ReturnTrue() throws Exception {
        String codeForEvolvedPokemon = "Purugly";
        CardList cardCreator = new CardList();
        Card testPokemonCard = cardCreator.createCardFromCardName(codeForEvolvedPokemon);
        assertEquals(true, testPokemonCard.isStageOnePokemonCard());
    }

    @Test
    public void createTrainerCardValidateIsTrainerCard_ReturnTrue() throws Exception {
        String trainerCard = "Potion";
        CardList cardCreator = new CardList();
        Card testCard = cardCreator.createCardFromCardName(trainerCard);
        assertEquals(true, testCard.isTrainerCard());
    }


    @Test
    public void createTrainerCardOfTypeItemValidateIfItem_ReturnTrue() throws Exception {
        String trainerCard = "Potion";
        CardList cardCreator = new CardList();
        TrainerCard testCard = (TrainerCard) cardCreator.createCardFromCardName(trainerCard);
        testCard.setTrainerType(TrainerType.ITEM);
        assertEquals(true, testCard.isItemTrainerCard());
    }

    @Test
    public void createTrainerCardOfTypeStadiumValidateIfItem_ReturnFalse() throws Exception {
        String trainerCard = "Potion";
        CardList cardCreator = new CardList();
        TrainerCard testCard = (TrainerCard) cardCreator.createCardFromCardName(trainerCard);
        testCard.setTrainerType(TrainerType.STADIUM);
        assertEquals(false, testCard.isItemTrainerCard());
    }

    @Test
    public void createSupporterTrainerCardValidateIfSupporter_ReturnsTrue() throws Exception {
        String trainerCard = "Potion";
        CardList cardCreator = new CardList();
        TrainerCard testCard = (TrainerCard) cardCreator.createCardFromCardName(trainerCard);
        testCard.setTrainerType(TrainerType.SUPPORTER);
        assertEquals(true, testCard.isSupporterTrainerCard());

    }

    @Test
    public void createEnergyCardValidateIsEnergy_ReturnTrue() throws Exception {
        String energyCard = "LightningEnergy";
        CardList cardCreator = new CardList();
        EnergyCard testCard = (EnergyCard) cardCreator.createCardFromCardName(energyCard);
        testCard.setEnergyType(EnergyType.LIGHTNING);
        assertEquals(true, testCard.isEnergyCard());
    }

    @Test
    public void createTrainerCardValidateIfEnergy_ReturnsFalse() throws Exception {
        String trainerCard = "Potion";
        CardList cardCreator = new CardList();
        TrainerCard testCard = (TrainerCard) cardCreator.createCardFromCardName(trainerCard);
        testCard.setTrainerType(TrainerType.SUPPORTER);
        assertEquals(false, testCard.isEnergyCard());

    }

    @Test
    public void createColorLessEnergyCardValidateIsEnergy_ReturnTrue() throws Exception {
        String energyCard = "LightningEnergy";
        CardList cardCreator = new CardList();
        EnergyCard testCard = (EnergyCard) cardCreator.createCardFromCardName(energyCard);
        testCard.setEnergyType(EnergyType.COLORLESS);
        assertEquals(true, testCard.isEnergyCard());
    }

}