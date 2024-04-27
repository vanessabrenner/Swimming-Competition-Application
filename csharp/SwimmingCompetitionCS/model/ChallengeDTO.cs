using System;

namespace model
{
    [Serializable]
    public class ChallengeDTO
    {
        public long id;
        public Distance distance;
        public Style style;
        public int noParticipants;

        public ChallengeDTO(long id, Distance distance, Style style, int noParticipants)
        {
            this.id = id;
            this.distance = distance;
            this.style = style;
            this.noParticipants = noParticipants;
        }

        public long Id
        {
            get => id;
            set => id = value;
        }

        public Distance Distance
        {
            get => distance;
            set => distance = value;
        }

        public Style Style
        {
            get => style;
            set => style = value;
        }

        public int NoParticipants
        {
            get => noParticipants;
            set => noParticipants = value;
        }

        public override string ToString()
        {
            return $"ChallengeDTO {{ Id = {Id}, Distance = {Distance}, Style = {Style} }}";
        }
    }
}