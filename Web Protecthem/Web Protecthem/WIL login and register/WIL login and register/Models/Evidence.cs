using System;
using System.ComponentModel;

namespace WIL_login_and_register.Models
{
    public class Evidence
    {
        public string ID { get; set; }
        public string Title { get; set; }
        [DisplayName("Date and Time")]
        public DateTime DateAndTime { get; set; }
        public string Description { get; set; }
        public string ImageUrl { get; set; }
    }
}
