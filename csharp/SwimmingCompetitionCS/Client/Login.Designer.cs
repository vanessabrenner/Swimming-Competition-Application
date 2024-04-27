namespace Client
{
    partial class Login
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

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
            this.login_lbl = new System.Windows.Forms.Label();
            this.username_txt = new System.Windows.Forms.TextBox();
            this.password_txt = new System.Windows.Forms.TextBox();
            this.username_lbl = new System.Windows.Forms.Label();
            this.password_lbl = new System.Windows.Forms.Label();
            this.login_btn = new System.Windows.Forms.Button();
            this.error_lbl = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // login_lbl
            // 
            this.login_lbl.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) | System.Windows.Forms.AnchorStyles.Right)));
            this.login_lbl.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.login_lbl.Location = new System.Drawing.Point(327, 89);
            this.login_lbl.Name = "login_lbl";
            this.login_lbl.Size = new System.Drawing.Size(101, 44);
            this.login_lbl.TabIndex = 0;
            this.login_lbl.Text = "Login";
            this.login_lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // username_txt
            // 
            this.username_txt.Location = new System.Drawing.Point(333, 156);
            this.username_txt.Name = "username_txt";
            this.username_txt.Size = new System.Drawing.Size(215, 22);
            this.username_txt.TabIndex = 1;
            // 
            // password_txt
            // 
            this.password_txt.Location = new System.Drawing.Point(333, 199);
            this.password_txt.Name = "password_txt";
            this.password_txt.Size = new System.Drawing.Size(215, 22);
            this.password_txt.TabIndex = 2;
            this.password_txt.UseSystemPasswordChar = true;
            // 
            // username_lbl
            // 
            this.username_lbl.Location = new System.Drawing.Point(248, 156);
            this.username_lbl.Name = "username_lbl";
            this.username_lbl.Size = new System.Drawing.Size(79, 25);
            this.username_lbl.TabIndex = 3;
            this.username_lbl.Text = "Username";
            this.username_lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // password_lbl
            // 
            this.password_lbl.Location = new System.Drawing.Point(248, 198);
            this.password_lbl.Name = "password_lbl";
            this.password_lbl.Size = new System.Drawing.Size(79, 25);
            this.password_lbl.TabIndex = 4;
            this.password_lbl.Text = "Password";
            this.password_lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // login_btn
            // 
            this.login_btn.Location = new System.Drawing.Point(327, 255);
            this.login_btn.Name = "login_btn";
            this.login_btn.Size = new System.Drawing.Size(86, 26);
            this.login_btn.TabIndex = 5;
            this.login_btn.Text = "Login";
            this.login_btn.UseVisualStyleBackColor = true;
            this.login_btn.Click += new System.EventHandler(this.login_btn_Click);
            // 
            // error_lbl
            // 
            this.error_lbl.ForeColor = System.Drawing.Color.Red;
            this.error_lbl.Location = new System.Drawing.Point(273, 294);
            this.error_lbl.Name = "error_lbl";
            this.error_lbl.Size = new System.Drawing.Size(200, 27);
            this.error_lbl.TabIndex = 6;
            this.error_lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // Login
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(800, 450);
            this.Controls.Add(this.error_lbl);
            this.Controls.Add(this.login_btn);
            this.Controls.Add(this.password_lbl);
            this.Controls.Add(this.username_lbl);
            this.Controls.Add(this.password_txt);
            this.Controls.Add(this.username_txt);
            this.Controls.Add(this.login_lbl);
            this.Name = "Login";
            this.Text = "Login Page";
            this.ResumeLayout(false);
            this.PerformLayout();
        }

        private System.Windows.Forms.Label error_lbl;

        private System.Windows.Forms.Button login_btn;

        private System.Windows.Forms.Label password_lbl;

        private System.Windows.Forms.Label username_lbl;

        private System.Windows.Forms.TextBox password_txt;

        private System.Windows.Forms.TextBox username_txt;

        private System.Windows.Forms.Label login_lbl;

        #endregion
    }
}