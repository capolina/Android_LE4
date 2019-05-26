using System.Collections.Generic;

namespace ChatAndroid.API.Data.Models
{
    public class Conversation
    {
        public int ConversationId { get; set; }
        public bool Active { get; set; }
        public string Theme { get; set; }
        public List<Message> Messages { get; set; }
    }
}