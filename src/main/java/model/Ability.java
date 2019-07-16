package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nutph on 5/9/2017.
 */
public abstract class Ability implements IAbility{
    @Override
    public Set<AbilityType> getTypeSet() {
        Set<AbilityType> typeSet = new HashSet<>();
        typeSet.add(getType());
        if(getSubAbility()!=null)
            typeSet.addAll(getSubAbility().getTypeSet());
        return typeSet;
    }
}
