using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using ChatAndroid.API.Data;
using ChatAndroid.API.Data.InputModels;
using ChatAndroid.API.Data.Mapping;
using ChatAndroid.API.Data.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

/*
 * Most likely useless
 */
namespace ChatAndroid.API.Controllers
{
    [Authorize]
    [ Route("[controller]") ]
    public class MessageController : Controller
    {
        /*private readonly ChatAndroidContext _db;

        public MessageController(ChatAndroidContext db)
        {
            _db = db;
        }
        
        [ HttpGet("") ]
        public IActionResult GetMessages()
        {
            return Ok(_db.Messages.Select(MessageViewMapping.MapMessageViewModel()));
        }

        [ HttpGet("{id}") ]
        public async Task<IActionResult> GetMessage(int id)
        {
            var message = await _db.Messages.Select(MessageViewMapping.MapMessageViewModel())
                                            .SingleOrDefaultAsync(m => m.MessageId == id);

            if (message == null)
            {
                return NotFound();
            }
            
            return Ok(message);
        }
        
        [ HttpPost("") ]
        public async Task<IActionResult> AddMessage([FromBody] MessageInputModel message)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var conversation =
                await _db.Conversations.SingleOrDefaultAsync(x => x.ConversationId == message.ConversationId);
                
            if (conversation == null)
            {
                ModelState.AddModelError("MissingConversation", "There is no conversation with this ID");
                return BadRequest(ModelState);
            }
            
            var newMessage = new Message
            {
                ConversationId = message.ConversationId,
                Contenu = message.Contenu,
                
                UserId = User.FindFirst(ClaimTypes.NameIdentifier)?.Value
            };

            await _db.Messages.AddAsync(newMessage);
            await _db.SaveChangesAsync();
            
            return Ok();
        }*/
    }
}