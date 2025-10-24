package dto.response;

import dto.base.PlayerBaseResponseDto;

public class PlayerUpdateResponseDto extends PlayerBaseResponseDto {

    public PlayerUpdateResponseDto() {
        super();
    }

    public PlayerUpdateResponseDto(Integer age, String gender, Long id, String login, String role, String screenName) {
        super(age, gender, id, login, role, screenName);
    }
}
