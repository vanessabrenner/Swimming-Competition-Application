using System;
using System.Runtime.Serialization;

namespace services
{
    public class SwimmingCompetitionException : Exception
    {
        public SwimmingCompetitionException():base() {}

        public SwimmingCompetitionException(string message) : base(message)
        {
        }

        public SwimmingCompetitionException(string message, Exception innerException) : base(message, innerException)
        {
        }
    }
}