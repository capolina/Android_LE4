using System;
using System.Linq.Expressions;
using ChatAndroid.API.Data.Models;
using ChatAndroid.API.Data.ViewModels;

namespace ChatAndroid.API.Data.Mapping
{
    public class MessageViewMapping
    {
        public static Expression<Func<Message, MessageViewModel>> MapMessageViewModel()
        {
            return m => new MessageViewModel
            {
                Contenu        = m.Contenu,
                ConversationId = m.ConversationId,
                MessageId      = m.MessageId,
                UserId         = m.UserId
            };
        }
    }
}