package dto.response;

import java.util.List;

public class PlayerGetAllResponseDto {

    private List<PlayerItem> players;

    public PlayerGetAllResponseDto() {}

    public PlayerGetAllResponseDto(List<PlayerItem> players) {
        this.players = players;
    }

    public List<PlayerItem> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerItem> players) {
        this.players = players;
    }
}
