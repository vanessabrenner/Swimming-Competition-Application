using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Client;
using model;
using services;

namespace Client
{
    public partial class Login : Form
    {
        private SwimmingCompetitionCtrl server;
        public Login(SwimmingCompetitionCtrl server)
        {
            this.server = server;
            InitializeComponent();
        }

        private void login_btn_Click(object sender, EventArgs e)
        {
            this.error_lbl.Text = "";
            string username = username_txt.Text;
            string password = password_txt.Text;
            try
            {
                Organizer organizer = server.FindAccount(username, password);
                server.login(organizer);
                Profile profile = new Profile(this.server);
                profile.Text = "Organizer: " + organizer.ToString();
                profile.Show();
                this.Hide();
            }
            catch (SwimmingCompetitionException ex)
            {
                error_lbl.Text = ex.Message;
            }

            username_txt.Text = "";
            password_txt.Text = "";
        }
    }
}