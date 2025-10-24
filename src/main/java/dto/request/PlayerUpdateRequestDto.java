package dto.request;

public class PlayerUpdateRequestDto {

    private final Integer age;
    private final String gender;
    private final String login;
    private final String password;
    private final String role;
    private final String screenName;

    private PlayerUpdateRequestDto(Builder builder) {
        this.age = builder.age;
        this.gender = builder.gender;
        this.login = builder.login;
        this.password = builder.password;
        this.role = builder.role;
        this.screenName = builder.screenName;
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

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getScreenName() {
        return screenName;
    }

    public static class Builder {
        private Integer age;
        private String gender;
        private String login;
        private String password;
        private String role;
        private String screenName;

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

        public Builder password(String password) {
            this.password = password;
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

        public PlayerUpdateRequestDto build() {
            return new PlayerUpdateRequestDto(this);
        }
    }
}
