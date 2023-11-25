using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Collections.Generic;
using WIL_login_and_register.Models; 
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using System.Diagnostics;

namespace WIL_login_and_register.Controllers
{
    public class JournalController : Controller
    {
        private IFirebaseConfig _config = new FirebaseConfig
        {
            BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/"
        };
        private IFirebaseClient _client;

        private string GetCurrentUserId()
        {
            var userId = HttpContext.Session.GetString("UserId");
            return userId;
        }

        private bool IsUserAuthenticated()
        {
            var userId = GetCurrentUserId();
            return !string.IsNullOrEmpty(userId);
        }

        public async Task<IActionResult> Index()
        {
            var authToken = HttpContext.Session.GetString("_UserToken");
            if (!string.IsNullOrEmpty(authToken))
            {
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };
                var client = new FireSharp.FirebaseClient(config);

                if (IsUserAuthenticated())
                {
                    var userId = GetCurrentUserId();
                    FirebaseResponse response = await client.GetAsync($"Users/{userId}/JournalList");

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var journalsDictionary = JsonConvert.DeserializeObject<Dictionary<string, Journal>>(response.Body);
                        var journalsList = journalsDictionary?.Values.ToList() ?? new List<Journal>();
                        return View(journalsList);
                    }
                    else
                    {
                        // Error handling
                        return RedirectToAction("Error");
                    }
                }
                else
                {
                    return RedirectToAction("SignIn", "Home");
                }
            }
            else
            {
               
                return RedirectToAction("SignIn", "Home");
            }
        }
        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        public IActionResult Create(Journal journal)
        {
            var userId = GetCurrentUserId();
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };

                var client = new FireSharp.FirebaseClient(config);

                journal.ID = null; 
                PushResponse response = client.Push($"Users/{userId}/JournalList/", journal);
                journal.ID = response.Result.name; 
                SetResponse setResponse = client.Set($"Users/{userId}/JournalList/{journal.ID}", journal);
                return RedirectToAction("Index");
            }
            else
            {
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }


        [HttpGet]
        public IActionResult Details(string id)
        {
            var userId = GetCurrentUserId();
            FirebaseResponse response = _client.Get($"Users/{userId}/JournalList/{id}");
            Journal journal = JsonConvert.DeserializeObject<Journal>(response.Body);
            return View(journal);
        }

        [HttpGet]
        public IActionResult Edit(string id)
        {
            var userId = GetCurrentUserId();
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };

                var client = new FireSharp.FirebaseClient(config);

                FirebaseResponse response = client.Get($"Users/{userId}/JournalList/{id}");

                if (response.Body != "null") 
                {
                    Journal journal = JsonConvert.DeserializeObject<Journal>(response.Body);
                    return View(journal);
                }
                else
                {
                    
                    return RedirectToAction("Error"); 
                }
            }
            else
            {
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }

        [HttpPost]
        public IActionResult Edit(Journal journal)
        {
            var userId = GetCurrentUserId();
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };

                var client = new FireSharp.FirebaseClient(config);

                SetResponse setResponse = client.Set($"Users/{userId}/JournalList/{journal.ID}", journal);
                return RedirectToAction("Index");
            }
            else
            {
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }


        [HttpGet]
        public ActionResult Delete(string id)
        {
            var userId = GetCurrentUserId();
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };

                var client = new FireSharp.FirebaseClient(config);

                FirebaseResponse response = client.Get($"Users/{userId}/JournalList/{id}");

                if (response.Body != "null")
                {
                    Journal journal = JsonConvert.DeserializeObject<Journal>(response.Body);
                    return View("Delete", journal);
                }
                else
                {
                   
                    return RedirectToAction("Error"); 
                }
            }
            else
            {
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(string id)
        {
            var userId = GetCurrentUserId();
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };

                var client = new FireSharp.FirebaseClient(config);

                
                await client.DeleteAsync($"Users/{userId}/JournalList/{id}");
                return RedirectToAction("Index");
            }
            else
            {
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }
    }
}


//Reference List


//Broderick, P. (2023). Introduction to Firebase in .NET. [online] Code Maze. Available at: https://code-maze.com/dotnet-firebase/ [Accessed 18 Oct. 2023].
//code2night.com. (n.d.). Using Firebase Database in Asp.net | Code2night.com. [online] Available at: https://code2night.com/Blog/MyBlog/Using-Firebase-Database-in-Asp.net [Accessed 18 Oct. 2023].