using System.ComponentModel.DataAnnotations;

namespace ChatAndroid.API.Data.InputModels
{
    public class MessageInputModel
    {
        [Required]
        public string Contenu { get; set; }
    }
}