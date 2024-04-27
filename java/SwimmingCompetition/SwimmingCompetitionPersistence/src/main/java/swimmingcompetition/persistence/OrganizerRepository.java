package swimmingcompetition.persistence;


import swimmingcompetition.model.Organizer;

public interface OrganizerRepository extends Repository<Long, Organizer>{
    /**
     * Cauta un cont aferenet organizatorului
     * @param username
     * @param password
     * @return organizatorul cu username ul si parola respectiva
     */
    Organizer findAccount(String username, String password);
}
