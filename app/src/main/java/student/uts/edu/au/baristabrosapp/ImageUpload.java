package student.uts.edu.au.baristabrosapp;

public class ImageUpload {

    private String title;
    private String desc;
    private String imageUrl;

    public ImageUpload() {

    }

    public ImageUpload(String title, String desc, String imageUrl) {

        if (title.trim().equals("")) {
            title = "No title";
        }

        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
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

}
