using Firebase.Storage;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Collections.Generic;
using System.Threading.Tasks;
using WIL_login_and_register.Models;
using System.Diagnostics;

namespace WIL_login_and_register.Controllers
{
    public class EvidenceController : Controller
    {
        private IFirebaseConfig _config = new FirebaseConfig
        {
            
            BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/"
        };
        private IFirebaseClient _client;

        public EvidenceController()
        {
            _client = new FireSharp.FirebaseClient(_config);
        }

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

        public IActionResult Index()
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
                    FirebaseResponse response = client.Get($"Users/{userId}/EvidenceList");

                    if (response.StatusCode == System.Net.HttpStatusCode.OK)
                    {
                        var evidenceDictionary = JsonConvert.DeserializeObject<Dictionary<string, Evidence>>(response.Body);
                        var evidenceList = evidenceDictionary?.Values.ToList() ?? new List<Evidence>();
                        return View(evidenceList);
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
        public async Task<IActionResult> Create(Evidence evidence, IFormFile imageUpload, string date, string time)
        {
            var authToken = HttpContext.Session.GetString("_UserToken"); 

            if (!string.IsNullOrEmpty(authToken))
            {
                var userId = GetCurrentUserId();

                if (imageUpload != null && imageUpload.Length > 0)
                {
                    evidence.ImageUrl = await UploadImageToFirebase(imageUpload, userId, authToken);
                }

                if (DateTime.TryParse(date, out DateTime parsedDate) && TimeSpan.TryParse(time, out TimeSpan parsedTime))
                {
                    evidence.DateAndTime = parsedDate.Add(parsedTime);
                }
                else
                {
                    ModelState.AddModelError("DateAndTime", "Invalid date or time.");
                    return View(evidence);
                }

                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };
                var client = new FireSharp.FirebaseClient(config);

                PushResponse pushResponse = client.Push($"Users/{userId}/EvidenceList/", evidence);
                evidence.ID = pushResponse.Result.name;
                client.Set($"Users/{userId}/EvidenceList/" + evidence.ID, evidence);

                return RedirectToAction("Index");
            }
            else
            {
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }


        private async Task<string> UploadImageToFirebase(IFormFile file, string userId, string authToken)
        {
            var cancellation = new CancellationTokenSource();
            var stream = file.OpenReadStream();
            var imageName = Guid.NewGuid() + Path.GetExtension(file.FileName);
            var storage = new FirebaseStorage(
                "victim-support-app.appspot.com",
                new FirebaseStorageOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(authToken),
                    ThrowOnCancel = true
                });

            var task = storage
                .Child("users")
                .Child(userId)
                .Child("evidence")
                .Child(imageName)
                .PutAsync(stream, cancellation.Token);

            try
            {
                string link = await task;
                return link;
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error uploading the image: " + ex.Message);
                return null;
            }
        }


        [HttpGet]
        public IActionResult Details(string id)
        {
            var userId = GetCurrentUserId();
            FirebaseResponse response = _client.Get($"Users/{userId}/EvidenceList/" + id);
            Evidence evidence = JsonConvert.DeserializeObject<Evidence>(response.Body);
            return View(evidence);
        }

        [HttpGet]
        public IActionResult Edit(string id)
        {
            var authToken = HttpContext.Session.GetString("_UserToken"); 

            if (!string.IsNullOrEmpty(authToken))
            {
                var userId = GetCurrentUserId();
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };
                var client = new FireSharp.FirebaseClient(config);

                FirebaseResponse response = client.Get($"Users/{userId}/EvidenceList/" + id);

                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    Evidence evidence = JsonConvert.DeserializeObject<Evidence>(response.Body);
                    TempData["ExistingImageUrl"] = evidence.ImageUrl; 
                    return View(evidence);
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
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }

        [HttpPost]
        public async Task<IActionResult> Edit(Evidence evidence, IFormFile imageUpload)
        {
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var userId = GetCurrentUserId();
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };
                var client = new FireSharp.FirebaseClient(config);

                if (imageUpload != null && imageUpload.Length > 0)
                {
                    
                    evidence.ImageUrl = await UploadImageToFirebase(imageUpload, userId, authToken);
                }
                else
                {
                   
                    evidence.ImageUrl = TempData["ExistingImageUrl"] as string;
                }

                SetResponse setResponse = await client.SetAsync($"Users/{userId}/EvidenceList/" + evidence.ID, evidence);
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
            var authToken = HttpContext.Session.GetString("_UserToken"); 

            if (!string.IsNullOrEmpty(authToken))
            {
                var userId = GetCurrentUserId();
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };
                var client = new FireSharp.FirebaseClient(config);

                FirebaseResponse response = client.Get($"Users/{userId}/EvidenceList/" + id);

                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    Evidence evidence = JsonConvert.DeserializeObject<Evidence>(response.Body);
                    return View("Delete", evidence);
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
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }

        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(string id)
        {
            var authToken = HttpContext.Session.GetString("_UserToken");

            if (!string.IsNullOrEmpty(authToken))
            {
                var userId = GetCurrentUserId();
                var config = new FireSharp.Config.FirebaseConfig
                {
                    BasePath = "https://victim-support-app-default-rtdb.firebaseio.com/",
                    AuthSecret = authToken
                };
                var client = new FireSharp.FirebaseClient(config);

                FirebaseResponse response = await client.GetAsync($"Users/{userId}/EvidenceList/" + id);

                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    Evidence evidence = JsonConvert.DeserializeObject<Evidence>(response.Body);

                    if (!string.IsNullOrWhiteSpace(evidence.ImageUrl))
                    {
                        var storage = new FirebaseStorage("victim-support-app.appspot.com", new FirebaseStorageOptions
                        {
                            AuthTokenAsyncFactory = () => Task.FromResult(authToken),
                            ThrowOnCancel = true // Optional
                        });

                        var imageName = GetImageNameFromUrl(evidence.ImageUrl);
                        var imageToDelete = storage.Child("users").Child(userId).Child("evidence").Child(imageName);
                        await imageToDelete.DeleteAsync();
                    }

                    await client.DeleteAsync($"Users/{userId}/EvidenceList/" + id);
                    return RedirectToAction("Index");
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
                Debug.WriteLine("No auth token found in session.");
                return RedirectToAction("SignIn", "Home");
            }
        }

        private string GetImageNameFromUrl(string imageUrl)
        {
            Uri uri = new Uri(imageUrl);
            return Path.GetFileName(uri.LocalPath);
        }
    }
}


//Reference List

//Bezouška, T. (2017).Firebase Storage C# library. [online] Step Up Labs. Available at: https://medium.com/step-up-labs/firebase-storage-c-library-d1656cc8b3c3 [Accessed 18 Oct. 2023].
//Broderick, P. (2023). Introduction to Firebase in .NET. [online] Code Maze. Available at: https://code-maze.com/dotnet-firebase/ [Accessed 18 Oct. 2023].
//code2night.com. (n.d.). Using Firebase Database in Asp.net | Code2night.com. [online] Available at: https://code2night.com/Blog/MyBlog/Using-Firebase-Database-in-Asp.net [Accessed 18 Oct. 2023].‌