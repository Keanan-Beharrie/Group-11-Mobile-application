namespace WIL_login_and_register.Models
{
    public class FirebaseError
    {
        public Error error { get; set; }
    }


    public class Error
    {
        public int code { get; set; }
        public string message { get; set; }
        public List<Error> errors { get; set; }
    }

}

// (FreeCodeSpot , 2023)

// REFERENCING LIST 
// FreeCodeSpot. 2023. How to integrate firebase in asp net core mvc, 2023. [Online]. Available at: https://www.freecodespot.com/blog/firebase-in-asp-net-core-mvc/#google_vignette [ Accessed 12 August 2023]. 
