package dto.request;

public class PlayerGetByPlayerIdRequestDto {

    private final Long playerId;

    private PlayerGetByPlayerIdRequestDto(Builder builder) {
        this.playerId = builder.playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public static class Builder {
        private Long playerId;

        public Builder playerId(Long playerId) {
            this.playerId = playerId;
            return this;
        }

        public PlayerGetByPlayerIdRequestDto build() {
            return new PlayerGetByPlayerIdRequestDto(this);
        }
    }
}
