using System.ComponentModel;

namespace Client
{
    partial class Profile
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }

            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.label1 = new System.Windows.Forms.Label();
            this.challenges = new System.Windows.Forms.DataGridView();
            this.IdChallengeCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.StyleCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.DistanceCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.NoParticipantsCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.label2 = new System.Windows.Forms.Label();
            this.participants = new System.Windows.Forms.DataGridView();
            this.label3 = new System.Windows.Forms.Label();
            this.name_txt = new System.Windows.Forms.TextBox();
            this.name_lbl = new System.Windows.Forms.Label();
            this.age_lbl = new System.Windows.Forms.Label();
            this.age_txt = new System.Windows.Forms.TextBox();
            this.submit_btn = new System.Windows.Forms.Button();
            this.signOut_btn = new System.Windows.Forms.Button();
            this.IdParticipantCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.NameCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.AgeCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.ChallengesCol = new System.Windows.Forms.DataGridViewTextBoxColumn();
            ((System.ComponentModel.ISupportInitialize)(this.challenges)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.participants)).BeginInit();
            this.SuspendLayout();
            // 
            // label1
            // 
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(31, 9);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(143, 29);
            this.label1.TabIndex = 1;
            this.label1.Text = "Challenges";
            // 
            // challenges
            // 
            this.challenges.AllowUserToAddRows = false;
            this.challenges.AllowUserToDeleteRows = false;
            this.challenges.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.challenges.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] { this.IdChallengeCol, this.StyleCol, this.DistanceCol, this.NoParticipantsCol });
            this.challenges.Location = new System.Drawing.Point(31, 41);
            this.challenges.Name = "challenges";
            this.challenges.ReadOnly = true;
            this.challenges.RowTemplate.Height = 24;
            this.challenges.Size = new System.Drawing.Size(491, 226);
            this.challenges.TabIndex = 2;
            this.challenges.CellFormatting += new System.Windows.Forms.DataGridViewCellFormattingEventHandler(this.challenges_CellFormatting);
            this.challenges.SelectionChanged += new System.EventHandler(this.challenges_SelectionChanged);
            // 
            // IdChallengeCol
            // 
            this.IdChallengeCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.IdChallengeCol.DataPropertyName = "Id";
            this.IdChallengeCol.HeaderText = "ID";
            this.IdChallengeCol.Name = "IdChallengeCol";
            this.IdChallengeCol.ReadOnly = true;
            // 
            // StyleCol
            // 
            this.StyleCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.StyleCol.DataPropertyName = "Style";
            this.StyleCol.HeaderText = "Style";
            this.StyleCol.Name = "StyleCol";
            this.StyleCol.ReadOnly = true;
            // 
            // DistanceCol
            // 
            this.DistanceCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.DistanceCol.DataPropertyName = "Distance";
            this.DistanceCol.HeaderText = "Distance";
            this.DistanceCol.Name = "DistanceCol";
            this.DistanceCol.ReadOnly = true;
            // 
            // NoParticipantsCol
            // 
            this.NoParticipantsCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.NoParticipantsCol.HeaderText = "No.Participants";
            this.NoParticipantsCol.Name = "NoParticipantsCol";
            this.NoParticipantsCol.ReadOnly = true;
            // 
            // label2
            // 
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(32, 270);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(134, 36);
            this.label2.TabIndex = 3;
            this.label2.Text = "Participants";
            // 
            // participants
            // 
            this.participants.AllowUserToAddRows = false;
            this.participants.AllowUserToDeleteRows = false;
            this.participants.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.participants.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] { this.IdParticipantCol, this.NameCol, this.AgeCol, this.ChallengesCol });
            this.participants.Location = new System.Drawing.Point(32, 300);
            this.participants.Name = "participants";
            this.participants.ReadOnly = true;
            this.participants.RowTemplate.Height = 24;
            this.participants.Size = new System.Drawing.Size(490, 263);
            this.participants.TabIndex = 4;
            this.participants.CellFormatting += new System.Windows.Forms.DataGridViewCellFormattingEventHandler(this.participants_CellFormatting);
            // 
            // label3
            // 
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(658, 200);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(183, 42);
            this.label3.TabIndex = 7;
            this.label3.Text = "Add a participant";
            // 
            // name_txt
            // 
            this.name_txt.Location = new System.Drawing.Point(705, 245);
            this.name_txt.Name = "name_txt";
            this.name_txt.Size = new System.Drawing.Size(166, 22);
            this.name_txt.TabIndex = 8;
            // 
            // name_lbl
            // 
            this.name_lbl.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.name_lbl.Location = new System.Drawing.Point(603, 245);
            this.name_lbl.Name = "name_lbl";
            this.name_lbl.Size = new System.Drawing.Size(96, 22);
            this.name_lbl.TabIndex = 9;
            this.name_lbl.Text = "Name";
            this.name_lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // age_lbl
            // 
            this.age_lbl.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.age_lbl.Location = new System.Drawing.Point(603, 284);
            this.age_lbl.Name = "age_lbl";
            this.age_lbl.Size = new System.Drawing.Size(96, 22);
            this.age_lbl.TabIndex = 10;
            this.age_lbl.Text = "Age";
            this.age_lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // age_txt
            // 
            this.age_txt.Location = new System.Drawing.Point(705, 284);
            this.age_txt.Name = "age_txt";
            this.age_txt.Size = new System.Drawing.Size(166, 22);
            this.age_txt.TabIndex = 11;
            // 
            // submit_btn
            // 
            this.submit_btn.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.2F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.submit_btn.Location = new System.Drawing.Point(678, 348);
            this.submit_btn.Name = "submit_btn";
            this.submit_btn.Size = new System.Drawing.Size(97, 30);
            this.submit_btn.TabIndex = 14;
            this.submit_btn.Text = "Submit";
            this.submit_btn.UseVisualStyleBackColor = true;
            this.submit_btn.MouseClick += new System.Windows.Forms.MouseEventHandler(this.submit_btn_MouseClick);
            // 
            // signOut_btn
            // 
            this.signOut_btn.Font = new System.Drawing.Font("Microsoft Sans Serif", 7.2F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.signOut_btn.Location = new System.Drawing.Point(883, 533);
            this.signOut_btn.Name = "signOut_btn";
            this.signOut_btn.Size = new System.Drawing.Size(72, 30);
            this.signOut_btn.TabIndex = 15;
            this.signOut_btn.Text = "Sign out";
            this.signOut_btn.UseVisualStyleBackColor = true;
            this.signOut_btn.Click += new System.EventHandler(this.signOut_btn_Click);
            // 
            // IdParticipantCol
            // 
            this.IdParticipantCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.IdParticipantCol.DataPropertyName = "id";
            this.IdParticipantCol.HeaderText = "ID";
            this.IdParticipantCol.Name = "IdParticipantCol";
            this.IdParticipantCol.ReadOnly = true;
            // 
            // NameCol
            // 
            this.NameCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.NameCol.DataPropertyName = "name";
            this.NameCol.HeaderText = "Name";
            this.NameCol.Name = "NameCol";
            this.NameCol.ReadOnly = true;
            // 
            // AgeCol
            // 
            this.AgeCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.AgeCol.DataPropertyName = "age";
            this.AgeCol.HeaderText = "Age";
            this.AgeCol.Name = "AgeCol";
            this.AgeCol.ReadOnly = true;
            // 
            // ChallengesCol
            // 
            this.ChallengesCol.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            this.ChallengesCol.DataPropertyName = "challenges";
            this.ChallengesCol.HeaderText = "Challenges";
            this.ChallengesCol.Name = "ChallengesCol";
            this.ChallengesCol.ReadOnly = true;
            // 
            // Profile
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(967, 575);
            this.Controls.Add(this.signOut_btn);
            this.Controls.Add(this.submit_btn);
            this.Controls.Add(this.age_txt);
            this.Controls.Add(this.age_lbl);
            this.Controls.Add(this.name_lbl);
            this.Controls.Add(this.name_txt);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.participants);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.challenges);
            this.Controls.Add(this.label1);
            this.Name = "Profile";
            this.Text = "Profile";
            ((System.ComponentModel.ISupportInitialize)(this.challenges)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.participants)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();
        }

        private System.Windows.Forms.Button signOut_btn;

        private System.Windows.Forms.Label name_lbl;
        private System.Windows.Forms.TextBox name_txt;
        private System.Windows.Forms.Label age_lbl;
        private System.Windows.Forms.TextBox age_txt;
        private System.Windows.Forms.Button submit_btn;

        private System.Windows.Forms.Label label3;

        private System.Windows.Forms.DataGridViewTextBoxColumn ChallengesCol;

        private System.Windows.Forms.DataGridViewTextBoxColumn IdParticipantCol;
        private System.Windows.Forms.DataGridViewTextBoxColumn NameCol;
        private System.Windows.Forms.DataGridViewTextBoxColumn AgeCol;

        private System.Windows.Forms.DataGridView participants;

        private System.Windows.Forms.Label label2;

        private System.Windows.Forms.DataGridViewTextBoxColumn IdChallengeCol;
        private System.Windows.Forms.DataGridViewTextBoxColumn StyleCol;
        private System.Windows.Forms.DataGridViewTextBoxColumn DistanceCol;
        private System.Windows.Forms.DataGridViewTextBoxColumn NoParticipantsCol;

        private System.Windows.Forms.DataGridView challenges;

        private System.Windows.Forms.Label label1;

        #endregion
    }
}