package project.mapobed.notbuk.Onboard;

public class OnboardModelclass {
    private String desc, title;
    private int image;

    public OnboardModelclass(String desc, String title, int image) {
        this.desc = desc;
        this.title = title;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
