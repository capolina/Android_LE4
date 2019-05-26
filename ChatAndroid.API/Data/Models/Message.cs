using Newtonsoft.Json;

namespace ChatAndroid.API.Data.Models
{
    public class Message
    {
        public int MessageId { get; set; }
        public int ConversationId { get; set; }
        [JsonIgnore]
        public Conversation Conversation { get; set; }
        public string UserId { get; set; }
        public User User { get; set; }
        public string Contenu { get; set; }
    }
}