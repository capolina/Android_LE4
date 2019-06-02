using System;
using System.Linq;
using System.Linq.Expressions;
using ChatAndroid.API.Data.Models;
using ChatAndroid.API.Data.ViewModels;

namespace ChatAndroid.API.Data.Mapping
{
    public class ConversationViewMapping
    {
        public static Expression<Func<Conversation, ConversationViewModel>> MapConversationViewModel(int lastId)
        {
            return c => new ConversationViewModel
            {
                ConversationId = c.ConversationId,
                Messages       = c.Messages.Where(m => m.MessageId > lastId).Select(m => MessageViewMapping.MapMessageViewModel().Compile().Invoke(m)).ToList(),
                Active         = c.Active,
                Theme          = c.Theme
            };
        }
    }
}