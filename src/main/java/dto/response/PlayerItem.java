package dto.response;

public class PlayerItem {

    private Integer age;
    private String gender;
    private Long id;
    private String role;
    private String screenName;

    public PlayerItem() {}

    public PlayerItem(Integer age, String gender, Long id, String role, String screenName) {
        this.age = age;
        this.gender = gender;
        this.id = id;
        this.role = role;
        this.screenName = screenName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
}
