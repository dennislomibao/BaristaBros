package student.uts.edu.au.baristabrosapp;

public class ItemData {

    String title;
    String desc;
    String price;
    String category;
    Integer imgId;

    public ItemData(String title, String price, String desc, Integer imgId, String category) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.imgId = imgId;
        this.category = category;
    }

}
