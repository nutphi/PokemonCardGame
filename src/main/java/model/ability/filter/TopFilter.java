package model.ability.filter;

/**
 * Created by nutph on 6/8/2017.
 */
public class TopFilter extends Filter{
    private int showNum;

    public TopFilter(int showNum, FilterType filterType) {
        super(filterType);
        this.showNum = showNum;
    }

    public int getShowedNum() {
        return showNum;
    }

    public void setShowedNum(int number) {
        this.showNum = showNum;
    }
}
