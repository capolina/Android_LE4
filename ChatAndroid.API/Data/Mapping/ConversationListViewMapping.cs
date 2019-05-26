using System;
using System.Linq.Expressions;
using ChatAndroid.API.Data.Models;
using ChatAndroid.API.Data.ViewModels;

namespace ChatAndroid.API.Data.Mapping
{
    public class ConversationListViewMapping
    {
        public static Expression<Func<Conversation, ConversationViewModel>> MapConversationViewModel()
        {
            return c => new ConversationViewModel
            {
                ConversationId = c.ConversationId,
                Messages       = null,
                Active         = c.Active,
                Theme          = c.Theme
            };
        }
    }
}