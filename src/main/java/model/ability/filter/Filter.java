package model.ability.filter;

/**
 * Created by nutph on 6/8/2017.
 */
public abstract class Filter {
    public enum FilterType{
        TOP, ENERGY, ITEM, POKEMON, EVOLVES_FROM;
        public static FilterType valueOf2(String name){
            return valueOf(name.replace("-","_").toUpperCase());
        }
        public static boolean hasTwoItem(String name){
            if(name.equals("cat")){
                return true;
            }else if(name.equals("type")){
                return true;
            }else {
                return false;
            }
        }
    }



    private FilterType filterType;

    public Filter(FilterType filterType) {
        this.filterType = filterType;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }
}
