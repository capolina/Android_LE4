using ChatAndroid.API.Data.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace ChatAndroid.API.Data
{
    public class ChatAndroidContext : IdentityDbContext<User>
    {
        public ChatAndroidContext()
        {
        }

        public ChatAndroidContext(DbContextOptions<ChatAndroidContext> options)
            : base(options)
        { }
        public DbSet<Message> Messages { get; set; }
        public DbSet<Conversation> Conversations { get; set; }
    }
}
