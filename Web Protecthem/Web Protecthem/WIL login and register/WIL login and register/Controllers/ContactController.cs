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
using System.Net.Http.Headers;

namespace WIL_login_and_register.Controllers
{
    public class ContactController : Controller
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
                    FirebaseResponse response = await client.GetAsync($"Users/{userId}/ContactList");

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var contactsDictionary = JsonConvert.DeserializeObject<Dictionary<string, Contact>>(response.Body);
                        var contactsList = contactsDictionary?.Values.ToList() ?? new List<Contact>();
                        return View(contactsList);
                    }
                    else
                    {
                        Debug.WriteLine($"Error: {response.StatusCode}");
                        Debug.WriteLine($"Response: {response.Body}");
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
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }




        [HttpGet]
        public IActionResult Create()
        {
            return View();
        }

        [HttpPost]
        public IActionResult Create(Contact contact)
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

                var userId = GetCurrentUserId();
                contact.ID = null; 
                PushResponse response = client.Push($"Users/{userId}/ContactList/", contact);
                contact.ID = response.Result.name; // Firebase generated ID
                SetResponse setResponse = client.Set($"Users/{userId}/ContactList/{contact.ID}", contact);
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
            FirebaseResponse response = _client.Get($"Users/{userId}/ContactList/{id}");
            Contact contact = JsonConvert.DeserializeObject<Contact>(response.Body);
            return View(contact);
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

                FirebaseResponse response = client.Get($"Users/{userId}/ContactList/{id}");

                if (response.Body != "null") 
                {
                    Contact contact = JsonConvert.DeserializeObject<Contact>(response.Body);
                    return View(contact);
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
        public IActionResult Edit(Contact contact)
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

                
                if (!string.IsNullOrEmpty(contact.ID))
                {
                    SetResponse setResponse = client.Set($"Users/{userId}/ContactList/{contact.ID}", contact);
                    return RedirectToAction("Index");
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

                FirebaseResponse response = client.Get($"Users/{userId}/ContactList/{id}");

                if (response.Body != "null") 
                {
                    Contact contact = JsonConvert.DeserializeObject<Contact>(response.Body);
                    return View("Delete", contact);
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

               
                await client.DeleteAsync($"Users/{userId}/ContactList/{id}");
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