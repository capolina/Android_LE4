using System.ComponentModel.DataAnnotations;

namespace ChatAndroid.API.Data.InputModels
{
    public class MessageInputModel
    {
        [Required]
        public int ConversationId { get; set; }
        [Required]
        public string Contenu { get; set; }
    }
}