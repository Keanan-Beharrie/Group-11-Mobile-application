using System;
using System.ComponentModel;

namespace WIL_login_and_register.Models
{
    public class Contact
    {
        public string ID { get; set; }
        [DisplayName("Full Name")]
        public string Fullname { get; set; }
        public string Relationship { get; set; }
        [DisplayName("Phone Number")]
        public string PhoneNumber { get; set; }
        [DisplayName("Email Address")]
        public string EmailAddress { get; set; }
    }
}
