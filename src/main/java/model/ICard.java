package model;
import javax.swing.ImageIcon;
/**
 * Created by nutph on 5/4/2017.
 */
public interface ICard {

    void setDescription(String description);
    String getDescription();
    void setName(String name);
    String getName();
    void setCardType(CardType type);
    CardType getCardType();

    void setVisibleCardImageIcon(ImageIcon img);
    ImageIcon getVisibleCardImageIcon();

    void setInvisibleCardImageIcon(ImageIcon img);
    ImageIcon getInvisibleCardImageIcon();

    void setBigVisibleCardImageIcon(ImageIcon img);
    ImageIcon getBigVisibleCardImageIcon();

    void setVisibleCard(boolean isVisibleCard);
    boolean isVisibleCard();

}
