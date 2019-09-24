package student.uts.edu.au.baristabrosapp;

public class ViewItem {

    String title;
    String desc;
    String imageUrl;
    String category;
    Double price;
    String uploadId;
    String sellerId;
    String sellTime;
    String buyerId;
    String buyTime;
    String buyerAddress1;
    String buyerAddress2;

    public ViewItem() {

    }

    public ViewItem(String title, String desc, String imageUrl, String category, Double price, String uploadId,
                    String sellerId, String sellTime, String buyerId, String buyTime, String buyerAddress1, String buyerAddress2) {

        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.uploadId = uploadId;
        this.sellerId = sellerId;
        this.sellTime = sellTime;
        this.buyerId = buyerId;
        this.buyTime = buyTime;
        this.buyerAddress1 = buyerAddress1;
        this.buyerAddress2 = buyerAddress2;

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

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellTime() {
        return sellTime;
    }

    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getBuyerAddress1() {
        return buyerAddress1;
    }

    public void setBuyerAddress1(String buyerAddress1) {
        this.buyerAddress1 = buyerAddress1;
    }

    public String getBuyerAddress2() {
        return buyerAddress2;
    }

    public void setBuyerAddress2(String buyerAddress2) {
        this.buyerAddress2 = buyerAddress2;
    }

}
