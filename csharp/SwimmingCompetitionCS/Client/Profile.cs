using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using model;


namespace Client
{
    public partial class Profile : Form
    {
        private SwimmingCompetitionCtrl ctrl;

        private BindingList<ChallengeDTO> challengesModel = new BindingList<ChallengeDTO>();
        private BindingList<ParticipantDTO> participantsModel = new BindingList<ParticipantDTO>();
        public Profile(SwimmingCompetitionCtrl ctrl)
        {
            this.ctrl = ctrl;
            InitializeComponent();
            InitModel();
            Initialize();
            ctrl.updateEvent += update;
        }

        private void Initialize()
        {
            challenges.DataSource = challengesModel;
            participants.DataSource = participantsModel;
        }

        private void InitModel()
        {   
            challengesModel.Clear();
            foreach (var challenge in this.ctrl.FindAllChallenges())
            {
                challengesModel.Add(challenge);
            }
        }

        private void challenges_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            if (challenges.Columns[e.ColumnIndex].Name == "DistanceCol" && e.Value != null)
            {
                if (e.Value is Distance)
                {
                    Distance distanceValue = (Distance)e.Value;
                    e.Value = ((int)distanceValue).ToString();
                    e.FormattingApplied = true;
                }
            }
            // if (challenges.Columns[e.ColumnIndex].Name == "NoParticipantsCol")
            // {
            //     Challenge challenge = challenges.Rows[e.RowIndex].DataBoundItem as Challenge;
            //     
            //     if (challenge != null)
            //     {
            //         int participantsCount = this.ctrl.GetNumberOfParticipantsByChallenge(challenge);
            //         e.Value = participantsCount.ToString();
            //         e.FormattingApplied = true;
            //     }
            // }
        }

        private void participants_CellFormatting(object sender, DataGridViewCellFormattingEventArgs e)
        {
            if (participants.Columns[e.ColumnIndex].Name == "ChallengesCol")
            {

                if (e.Value is List<Challenge> challenges)
                {
                    List<long> challengeIds = challenges.Select(c => c.Id).ToList();
                    string idString = string.Join(", ", challengeIds);

                    // Setează valoarea celulei la id-urile challenge-urilor separate de virgulă
                    e.Value = idString;
                }
            }
        }

        private void submit_btn_MouseClick(object sender, MouseEventArgs e)
        {
            string name = name_txt.Text;
            int age = int.Parse(age_txt.Text);

            List<Challenge> selectedChallenges = new List<Challenge>();

            foreach (DataGridViewRow row in challenges.SelectedRows)
            {
                ChallengeDTO selectedChallenge = row.DataBoundItem as ChallengeDTO;
                Challenge challenge = new Challenge(selectedChallenge.distance, selectedChallenge.style);
                challenge.Id = selectedChallenge.Id;
                
                if (selectedChallenge != null)
                {
                    selectedChallenges.Add(challenge);
                }
            }

            Participant participant = this.ctrl.AddParticipant(name, age);
            this.ctrl.AddParticipantToMoreChallenges(participant, selectedChallenges);
            //InitModel();
        }

        private void signOut_btn_Click(object sender, EventArgs e)
        {
            this.ctrl.logout();
            this.ctrl.updateEvent -= update;
            Application.Exit();
        }

        private void challenges_SelectionChanged(object sender, EventArgs e)
        {
            if (challenges.SelectedRows.Count > 0)
            {
                DataGridViewRow selectedRow = challenges.SelectedRows[0];
                
                ChallengeDTO selectedChallenge = selectedRow.DataBoundItem as ChallengeDTO;
                Challenge challenge = new Challenge(selectedChallenge.distance, selectedChallenge.style);
                challenge.Id = selectedChallenge.id;

                if (selectedChallenge != null)
                {
                    participantsModel.Clear();
                    foreach (var participant in this.ctrl.FindParticipantsByChallenge(challenge))
                    {
                        participantsModel.Add(participant);
                    }
                }
            }
        }

        private void updateTables(BindingList<ChallengeDTO> oldData, BindingList<ChallengeDTO> newData)
        {
            oldData.Clear();
            foreach (ChallengeDTO item in newData)
            {
                oldData.Add(item);
            }
        }
        
        public delegate void UpdateDataGridViewCallback(BindingList<ChallengeDTO> oldData, BindingList<ChallengeDTO> newData);

        public void update(object sender, SwimmingCompetitionEventArgs e)
        {
            if (e.SwimmingCompetitionEventType == SwimmingCompetitionEvent.UPDATE_TABLES)
            {
                ChallengeDTO[] data = (ChallengeDTO[])e.Data;
                BindingList<ChallengeDTO> newData = new BindingList<ChallengeDTO>(data);
                
                challenges.Invoke(new UpdateDataGridViewCallback(this.updateTables),
                    new Object[] { challengesModel, newData });
            }
        }
        
    }
}