using Firebase.Auth;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Diagnostics;
using WIL_login_and_register.Models;

namespace WIL_login_and_register.Controllers
{
    public class HomeController : Controller
    { 
       
        FirebaseAuthProvider auth;
        FirebaseAuthEventArgs FirebaseError;

        public HomeController()
        {
            auth = new FirebaseAuthProvider(
                            new FirebaseConfig("AIzaSyB5hhIsEtTtYXfgS7BYqrxVyrvN8vRs3UE"));
        }

        // Validation filter
        public IActionResult Index()
        {
            var token = HttpContext.Session.GetString("_UserToken");

            if (token != null)
            {
                return View();
            }
            else
            {
                return RedirectToAction("SignIn");
            }
        }

        // Registration
        public IActionResult Registration()
        {
            return View();
        }

        // Login
        public IActionResult SignIn()
        {
            return View();
        }

        // Logout
        public IActionResult LogOut()
        {
            HttpContext.Session.Remove("_UserToken");
            return RedirectToAction("SignIn");
        }

        public IActionResult Privacy()
        {
            return View();
        }
        [HttpPost]
        public async Task<IActionResult> Registration(LoginModel loginModel)
        {
            try
            {
                // Create the user
                var createUserResponse = await auth.CreateUserWithEmailAndPasswordAsync(loginModel.Email, loginModel.Password);

                // Log in the new user
                var loginUserResponse = await auth.SignInWithEmailAndPasswordAsync(loginModel.Email, loginModel.Password);
                string token = loginUserResponse.FirebaseToken;
                var userId = loginUserResponse.User.LocalId;

                if (token != null)
                {
                    HttpContext.Session.SetString("_UserToken", token);
                    HttpContext.Session.SetString("UserId", userId);

                    var config = new FireSharp.Config.FirebaseConfig
                    {
                        BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                        AuthSecret = token
                    };
                    var client = new FireSharp.FirebaseClient(config);
                    var userStructure = new { JournalList = new { }, EvidenceList = new { }, ContactList = new { } };
                    await client.SetAsync($"Users/{userId}", userStructure);

                    return RedirectToAction("Index");
                }
            }
            catch (FirebaseAuthException ex)
            {
                var firebaseEx = JsonConvert.DeserializeObject<FirebaseError>(ex.ResponseData);
                ModelState.AddModelError(string.Empty, firebaseEx.error.message);
                return View(loginModel);
            }

            return View();
        }



        [HttpPost]
        public async Task<IActionResult> SignIn(LoginModel loginModel)
        {
            try
            {
                // Log in the existing user
                var fbAuthLink = await auth.SignInWithEmailAndPasswordAsync(loginModel.Email, loginModel.Password);
                string token = fbAuthLink.FirebaseToken;

                // Save the token to a session variable
                if (token != null)
                {
                    // Save both the token and the user ID to the session
                    HttpContext.Session.SetString("_UserToken", token);
                    HttpContext.Session.SetString("UserId", fbAuthLink.User.LocalId); // Save the user ID


                    Debug.WriteLine($"User token set in session: {token}");
                    Debug.WriteLine($"User ID set in session: {fbAuthLink.User.LocalId}");

                    return RedirectToAction("Index");
                }
            }
            catch (FirebaseAuthException ex)
            {
                // Handle errors, such as incorrect login credentials
                var firebaseEx = JsonConvert.DeserializeObject<FirebaseError>(ex.ResponseData);
                ModelState.AddModelError(String.Empty, firebaseEx.error.message);
                return View(loginModel);
            }

            // If login fails, stay on the login page
            return View();
        }


        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}

// AIzaSyAi53m-lHgipbHBZRl3tFv20LYvygGqzr0