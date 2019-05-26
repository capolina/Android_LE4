using Microsoft.AspNetCore.Identity;

namespace ChatAndroid.API.Data.Models
{
    public class User : IdentityUser
    {
        public bool Blacklist { get; set; }
        public bool Admin { get; set; }
        public bool Connecte { get; set; }
        public string Couleur { get; set; }
    }
}