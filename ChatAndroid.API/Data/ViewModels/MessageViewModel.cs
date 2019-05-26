namespace ChatAndroid.API.Data.ViewModels
{
    public class MessageViewModel
    {
        public int MessageId { get; set; }
        public int ConversationId { get; set; }
        public string UserId { get; set; }
        public string Contenu { get; set; }
    }
}