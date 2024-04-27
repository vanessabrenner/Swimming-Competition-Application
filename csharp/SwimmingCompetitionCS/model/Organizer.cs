using System;

namespace model
{
    [Serializable]
    public class Organizer : Entity<long>
    {
        private string _username;
        private string _password;

        public Organizer(string username, string password)
        {
            this._username = username;
            this._password = password;
        }

        public string Password
        {
            get => _password;
            set => _password = value;
        }

        public string Username
        {
            get => _username;
            set => _username = value;
        }

        public override string ToString()
        {
            return "Organizer{" +
                   "id=" + Id +
                   ", username='" + _username + '\'' +
                   ", password='" + _password + '\'' +
                   '}';
        }
    }
}