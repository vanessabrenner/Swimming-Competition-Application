using System.Collections.Generic;
using model;

namespace persistence
{
    public interface Repository<ID, E> where E : Entity<ID>
    {
        E FindOne(ID along);
        IEnumerable<E> FindAll();
        E Save(E entity);
        void Delete(ID id);
        void Update(E entity);
    }
}