package student.uts.edu.au.baristabrosapp;

public class ImageUpload {

    private String title;
    private String desc;
    private String imageUrl;
    private String category;
    private Double price;

    public ImageUpload() {

    }

    public ImageUpload(String title, String desc, String imageUrl, String category, Double price) {

        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
