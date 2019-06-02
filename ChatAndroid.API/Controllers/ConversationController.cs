using System;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using ChatAndroid.API.Data;
using ChatAndroid.API.Data.InputModels;
using ChatAndroid.API.Data.Mapping;
using ChatAndroid.API.Data.Models;
using ChatAndroid.API.Data.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ChatAndroid.API.Controllers
{
    [Authorize]
    [ Route("[controller]") ]
    public class ConversationController : Controller
    {
        private readonly ChatAndroidContext _db;

        public ConversationController(ChatAndroidContext db)
        {
            _db = db;
        }
        
        [ HttpGet("") ]
        public async Task<IActionResult> GetConversations()
        {
            var conversations = new ConversationListViewModel
            {
                Conversations = await _db.Conversations
                                         .Select(ConversationListViewMapping.MapConversationViewModel())
                                         .ToListAsync()
            };
            return Ok(conversations);
        }

        [ HttpGet("{id}") ]
        public async Task<IActionResult> GetConversation(int id, int idLastMessage = 0)
        {
            var conversation = await _db.Conversations.Include(m => m.Messages)
                                                      .ThenInclude(m => m.User)
                                                      //.Select(ConversationViewMapping.MapConversationViewModel())
                                                      .SingleOrDefaultAsync(c => c.ConversationId == id);

            if (conversation == null)
            {
                return NotFound();
            }

            var conversationView = ConversationViewMapping.MapConversationViewModel(idLastMessage).Compile().Invoke(conversation);
            
            return Ok(conversationView);
        }
        
        [ HttpPost("") ]
        public async Task<IActionResult> AddConversation([FromBody] ConversationInputModel conversation)
        {
            var newConversation = new Conversation
            {
                Active = true,
                Theme  = conversation.Theme
            };
            await _db.Conversations.AddAsync(newConversation);
            await _db.SaveChangesAsync();
            return Ok(newConversation);
        }
        
        [ HttpPost("{id}/message") ]
        public async Task<IActionResult> AddMessage([FromBody] MessageInputModel message, int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var conversation =
                await _db.Conversations.SingleOrDefaultAsync(x => x.ConversationId == id);
                
            if (conversation == null)
            {
                ModelState.AddModelError("MissingConversation", "There is no conversation with this ID");
                return BadRequest(ModelState);
            }
            
            var newMessage = new Message
            {
                ConversationId = id,
                Contenu = message.Contenu,
                
                UserId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value
            };

            await _db.Messages.AddAsync(newMessage);
            await _db.SaveChangesAsync();
            
            //send empty json so the app doesn't fail
            return Ok(new {});
        }
    }
}