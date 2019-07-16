package model.ability.filter;

import model.TrainerType;

/**
 * Created by nutph on 6/8/2017.
 */
public class TrainerFilter extends Filter{
    private TrainerType trainerType;

    public TrainerFilter(TrainerType trainerType, FilterType filterType) {
        super(filterType);
        this.trainerType = trainerType;
    }

    public TrainerType getTrainerType() {
        return trainerType;
    }

    public void setTrainerType(TrainerType trainerType) {
        this.trainerType = trainerType;
    }
}
