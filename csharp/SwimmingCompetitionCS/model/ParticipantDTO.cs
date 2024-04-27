using System;
using System.Collections.Generic;

namespace model
{
    [Serializable]
    public class ParticipantDTO
    {
        public long id;
        public string name;
        public int age;
        public List<Challenge> challenges;

        public ParticipantDTO(long id, string name, int age, List<Challenge> challenges)
        {
            this.id = id;
            this.name = name;
            this.age = age;
            this.challenges = challenges;
        }

        public long Id
        {
            get => id;
            set => id = value;
        }

        public string Name
        {
            get => name;
            set => name = value;
        }

        public int Age
        {
            get => age;
            set => age = value;
        }

        public List<Challenge> Challenges
        {
            get => challenges;
            set => challenges = value;
        }

        public override string ToString()
        {
            return $"ParticipantDTO {{ Id = {Id}, Name = {Name}, Age = {Age} }}";
        }
    }
}