using System.ComponentModel.DataAnnotations;

namespace ChatAndroid.API.Data.InputModels
{
    public class RegisterInputModel
    {
        [Required]
        public string Username { get; set; }
        [Required]
        public string Password { get; set; }
    }
}