package model.ability;

import model.*;
import model.ability.filter.Filter;
import view.BoardPosition;

/**
 * Created by nutph on 6/2/2017.
 */
public class SearchAbility extends Ability {

    private AbilityTarget target;
    private Filter choiceFilter;
    private BoardPosition source;//deck or discard
    private Filter sourceFilter;

    private int pickedAmount;
    private Ability subAbility;//when the ability has more than one effect
    private AbilityType type;//DAM,HEAL, etc.

    //constructor with sourceFilter and choiceFilter
    public SearchAbility(int pickedAmount, Filter sourceFilter, BoardPosition source, Filter choiceFilter, AbilityTarget target, AbilityType type) {
        this.pickedAmount = pickedAmount;
        this.sourceFilter = sourceFilter;
        this.source = source;
        this.choiceFilter = choiceFilter;
        this.target = target;
        this.type = type;
    }

    //constructor with sourceFilter
    public SearchAbility(int pickedAmount, Filter sourceFilter, BoardPosition source, AbilityTarget target, AbilityType type) {
        this.pickedAmount = pickedAmount;
        this.sourceFilter = sourceFilter;
        this.source = source;
        this.target = target;
        this.type = type;
    }

    //constructor without filter
    public SearchAbility(int pickedAmount, BoardPosition source, AbilityTarget target, AbilityType type) {
        this.pickedAmount = pickedAmount;
        this.source = source;
        this.target = target;
        this.type = type;
    }

    //constructor with sourceFilter and choiceFilter
    public SearchAbility(int pickedAmount, Filter sourceFilter, BoardPosition source,
                         Filter choiceFilter, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.pickedAmount = pickedAmount;
        this.sourceFilter = sourceFilter;
        this.source = source;
        this.choiceFilter = choiceFilter;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    //constructor with sourceFilter
    public SearchAbility(int pickedAmount, Filter sourceFilter, BoardPosition source,
                         AbilityTarget target, AbilityType type, Ability subAbility) {
        this.pickedAmount = pickedAmount;
        this.sourceFilter = sourceFilter;
        this.source = source;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    //constructor without filter
    public SearchAbility(int pickedAmount, BoardPosition source, AbilityTarget target,
                         AbilityType type, Ability subAbility) {
        this.pickedAmount = pickedAmount;
        this.source = source;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }


    public Filter getChoiceFilter() {
        return choiceFilter;
    }

    public void setChoiceFilter(Filter choiceFilter) {
        this.choiceFilter = choiceFilter;
    }

    public Filter getSourceFilter() {
        return sourceFilter;
    }

    public void setSourceFilter(Filter sourceFilter) {
        this.sourceFilter = sourceFilter;
    }

    public BoardPosition getSource() {
        return source;
    }

    public void setSource(BoardPosition source) {
        this.source = source;
    }

    public int getPickedAmount() {
        return pickedAmount;
    }

    public void setPickedAmount(int pickedAmount) {
        this.pickedAmount = pickedAmount;
    }

    public AbilityTarget getTarget() {
        return target;
    }

    public void setTarget(AbilityTarget target) {
        this.target = target;
    }

    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    @Override
    public Ability getSubAbility() {
        return subAbility;
    }

    public void setSubAbility(Ability subAbility) {
        this.subAbility = subAbility;
    }

    @Override
    public String toString() {
        return getType()+" "+getPickedAmount();
    }

}
