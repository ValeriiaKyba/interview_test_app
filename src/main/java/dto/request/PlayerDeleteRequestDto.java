package dto.request;

public class PlayerDeleteRequestDto {

    private final Long playerId;

    private PlayerDeleteRequestDto(Builder builder) {
        this.playerId = builder.playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public static class Builder {
        private Long playerId;

        public Builder() {}

        public Builder playerId(Long playerId) {
            this.playerId = playerId;
            return this;
        }

        public PlayerDeleteRequestDto build() {
            return new PlayerDeleteRequestDto(this);
        }
    }
}
