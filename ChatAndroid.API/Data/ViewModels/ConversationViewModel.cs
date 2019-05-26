using System.Collections.Generic;

namespace ChatAndroid.API.Data.ViewModels
{
    public class ConversationViewModel
    {
        public int ConversationId { get; set; }
        public bool Active { get; set; }
        public string Theme { get; set; }
        public List<MessageViewModel> Messages { get; set; }
    }
}