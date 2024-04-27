using System;

namespace Client
{
    public enum SwimmingCompetitionEvent
    {
        UPDATE_TABLES
    } ;
    public class SwimmingCompetitionEventArgs : EventArgs
    {
        private readonly SwimmingCompetitionEvent swimmingCompetitionEvent;
        private readonly Object data;

        public SwimmingCompetitionEventArgs(SwimmingCompetitionEvent swimmingCompetitionEvent, object data)
        {
            this.swimmingCompetitionEvent = swimmingCompetitionEvent;
            this.data = data;
        }
        
        public SwimmingCompetitionEvent SwimmingCompetitionEventType
        {
            get { return swimmingCompetitionEvent; }
        }

        public object Data
        {
            get { return data; }
        }
    }
}