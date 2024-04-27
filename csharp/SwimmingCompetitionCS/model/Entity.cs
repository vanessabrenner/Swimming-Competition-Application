using System;

namespace model
{
    [Serializable]
    public class Entity<TId>
    {
        public TId Id { get; set; }
    }
}