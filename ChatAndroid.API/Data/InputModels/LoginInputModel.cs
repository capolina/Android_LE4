using System.ComponentModel.DataAnnotations;

namespace ChatAndroid.API.Data.InputModels
{
    public class LoginInputModel
    {
        [Required]
        public string Username { get; set; }
        [Required]
        public string Password { get; set; }
    }
}