package model;


import java.util.Set;

/**
 * Created by nutph on 5/9/2017.
 */
public interface IAbility {
    Set<AbilityType> getTypeSet();
    AbilityType getType();
    AbilityTarget getTarget();
    Ability getSubAbility();

    @Override
    String toString();

}
