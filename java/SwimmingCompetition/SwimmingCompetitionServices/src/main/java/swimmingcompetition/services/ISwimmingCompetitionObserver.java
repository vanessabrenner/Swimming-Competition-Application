package swimmingcompetition.services;


import swimmingcompetition.model.Challenge;
import swimmingcompetition.model.dto.ChallengeDTO;

import java.util.List;

public interface ISwimmingCompetitionObserver {
    void updateTables(ChallengeDTO[] challengeDTOS);
}
