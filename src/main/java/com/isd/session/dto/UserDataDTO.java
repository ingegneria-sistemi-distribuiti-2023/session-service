package com.isd.session.dto;
import com.isd.session.commons.error.CustomHttpResponse;
import com.isd.session.commons.error.CustomServiceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO implements Serializable {
    private static final long serialVersionUID = 6529685099997757690L;
    private Integer userId;
    private List<BetDTO> listOfBets;
    private String sessionId;
    private static int MAX_BETS = 2;

    public void addBet(BetDTO bet) throws Exception {
        // Check if the number of bets in the list is less than or equal to 3
        if (listOfBets.size() <= MAX_BETS) {
            // Iterate through the list of bets
            if (listOfBets.contains(bet)){
                throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Bet is the same"));
            }

            listOfBets.add(bet);
        } else {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Cannot add more than 3 bets to the list."));
        }
    }

    public void removeBet(BetDTO bet) throws Exception {
        // Check if the bet is present in the listOfBets
        if (listOfBets.contains(bet)) {
            // If it is, remove the bet from the listOfBets
            listOfBets.remove(bet);
        } else {
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Bet not founded in list"));
        }
    }

    public BetDTO getBetByBetId(Long ts) throws Exception {
        for (BetDTO bet: getListOfBets()){
            if (bet.getTs().toString().equals(ts.toString())){
                return bet;
            }
        }
        throw new CustomServiceException(new CustomHttpResponse(HttpStatus.BAD_REQUEST, "Bet not founded"));
    }

}

