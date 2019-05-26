using System.Linq;
using System.Threading.Tasks;
using ChatAndroid.API.Data;
using ChatAndroid.API.Data.InputModels;
using ChatAndroid.API.Data.Mapping;
using ChatAndroid.API.Data.Models;
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
        
        [ HttpGet ]
        [ Route("") ]
        public IActionResult GetConversations()
        {
            return Ok(_db.Conversations.Select(ConversationListViewMapping.MapConversationViewModel()));
        }

        [ HttpGet ]
        [ Route("{id}") ]
        public async Task<IActionResult> GetMessage(int id)
        {
            var conversation = await _db.Conversations.Include(c => c.Messages)
                                                      .Select(ConversationViewMapping.MapConversationViewModel())
                                                      .SingleOrDefaultAsync(c => c.ConversationId == id);

            if (conversation == null)
            {
                return NotFound();
            }
            
            return Ok(conversation);
        }
        
        [ HttpPost ]
        [ Route("") ]
        public async Task<IActionResult> AddMessage([FromBody] ConversationInputModel conversation)
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
    }
}