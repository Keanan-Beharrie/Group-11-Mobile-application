using System;
using System.ComponentModel;

namespace WIL_login_and_register.Models
{
    public class Journal
    {
        public string ID { get; set; }

        [DisplayName("Journal Entry Title")]
        public string Title { get; set; }

        [DisplayName("Journal Entry")]
        public string PostBody { get; set; }

        [DisplayName("Type of abuse")]
        public string abuse { get; set; }
    }
}
