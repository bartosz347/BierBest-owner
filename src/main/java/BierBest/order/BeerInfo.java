package BierBest.order;



import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BeerInfo {

    @Column(name = "beer_name")
    protected String name;

    @Column(name = "beer_price")
    protected String priceString;

    @Column(name = "beer_url")
    protected String URL;

    @Column(name = "beer_img_url")
    protected String imgURL;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
