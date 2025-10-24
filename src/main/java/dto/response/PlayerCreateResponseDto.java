package dto.response;

import dto.base.PlayerBaseResponseDto;

public class PlayerCreateResponseDto extends PlayerBaseResponseDto {

    private String password;

    public PlayerCreateResponseDto() {
        super();
    }
    public PlayerCreateResponseDto(Integer age, String gender, Long id, String login, String password,
                                   String role, String screenName) {
        super(age, gender, id, login, role, screenName);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
