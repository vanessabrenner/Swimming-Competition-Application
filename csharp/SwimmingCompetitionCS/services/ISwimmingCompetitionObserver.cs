using System.Collections;
using System.Collections.Generic;
using model;

namespace services
{
    public interface ISwimmingCompetitionObserver
    {
        void updateTables(ChallengeDTO[] challenges);
    }
}