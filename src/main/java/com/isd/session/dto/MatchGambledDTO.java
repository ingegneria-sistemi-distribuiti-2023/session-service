package com.isd.session.dto;

import java.io.Serializable;

import com.isd.session.commons.OutcomeEnum;

public class MatchGambledDTO implements Serializable{
    private static final long serialVersionUID = 6529685064657757690L;
    private int gameId;
    private OutcomeEnum outcome;
    private double quoteAtTimeOfBet;
    private long ts;

    public MatchGambledDTO() {
    }

    public MatchGambledDTO(int gameId, OutcomeEnum outcome, double quoteAtTimeOfBet, long ts) {
        this.gameId = gameId;
        this.outcome = outcome;
        this.quoteAtTimeOfBet = quoteAtTimeOfBet;
        this.ts = ts;
    }

    public int getGameId() {
        return gameId;
    }

    public OutcomeEnum getOutcome() {
        return outcome;
    }

    public double getQuoteAtTimeOfBet() {
        return quoteAtTimeOfBet;
    }

    public long getTs() {
        return ts;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setOutcome(OutcomeEnum outcome) {
        this.outcome = outcome;
    }

    public void setQuoteAtTimeOfBet(double quoteAtTimeOfBet) {
        this.quoteAtTimeOfBet = quoteAtTimeOfBet;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    // toString override
    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", outcome='" + outcome + '\'' +
                ", quoteAtTimeOfBet=" + quoteAtTimeOfBet +
                ", ts=" + ts +
                '}';
    }

    // equals override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MatchGambledDTO gameDTO = (MatchGambledDTO) o;

        if (gameId != gameDTO.gameId) return false;
        if (Double.compare(gameDTO.quoteAtTimeOfBet, quoteAtTimeOfBet) != 0) return false;
        if (ts != gameDTO.ts) return false;
        return outcome == gameDTO.outcome;
    }

    // hashCode override
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = gameId;
        result = 31 * result + (outcome != null ? outcome.hashCode() : 0);
        temp = Double.doubleToLongBits(quoteAtTimeOfBet);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (ts ^ (ts >>> 32));
        return result;
    }
}
