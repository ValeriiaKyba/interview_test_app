package dto.request;

public class PlayerCreateRequestDto {

    private Integer age;
    private String gender;
    private String login;
    private String role;
    private String screenName;
    private String password;

    private PlayerCreateRequestDto(Builder builder) {
        this.age = builder.age;
        this.gender = builder.gender;
        this.login = builder.login;
        this.role = builder.role;
        this.screenName = builder.screenName;
        this.password = builder.password;
    }

    public static class Builder {
        private Integer age;
        private String gender;
        private String login;
        private String role;
        private String screenName;
        private String password;

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder screenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public PlayerCreateRequestDto build() {
            return new PlayerCreateRequestDto(this);
        }
    }

    public Integer getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getLogin() {
        return login;
    }

    public String getRole() {
        return role;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getPassword() {
        return password;
    }
}
