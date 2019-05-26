using System.ComponentModel.DataAnnotations;

namespace ChatAndroid.API.Data.InputModels
{
    public class ConversationInputModel
    {
        [Required]
        public string Theme { get; set; }
    }
}