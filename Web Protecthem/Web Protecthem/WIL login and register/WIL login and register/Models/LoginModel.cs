using System.ComponentModel.DataAnnotations;

namespace WIL_login_and_register.Models
{
    public class LoginModel
    {
        // Requring a login email and password
        [Required]
        [EmailAddress]
        public string Email { get; set; }
        [Required]
        public string Password { get; set; }

    }
}

// (FreeCodeSpot , 2023)

// REFERENCING LIST 
// FreeCodeSpot. 2023. How to integrate firebase in asp net core mvc, 2023. [Online]. Available at: https://www.freecodespot.com/blog/firebase-in-asp-net-core-mvc/#google_vignette [ Accessed 12 August 2023]. 
